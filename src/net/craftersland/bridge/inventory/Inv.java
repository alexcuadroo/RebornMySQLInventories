package net.craftersland.bridge.inventory;

import java.util.logging.Logger;

import net.craftersland.bridge.inventory.database.InvMysqlInterface;
import net.craftersland.bridge.inventory.database.MysqlSetup;
import net.craftersland.bridge.inventory.events.DropItem;
import net.craftersland.bridge.inventory.events.InventoryClick;
import net.craftersland.bridge.inventory.events.PlayerJoin;
import net.craftersland.bridge.inventory.events.PlayerQuit;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Inv extends JavaPlugin {
	
	public static Logger log;
	public boolean useProtocolLib = false;
	public static String pluginName = "RebornMySQLInventories";
	//public Set<String> playersSync = new HashSet<String>();
	public static boolean is19Server = true;
	public static boolean is13Server = true; // 1.21 uses modern API
	public static boolean isDisabling = false;
	
	private static ConfigHandler configHandler;
	private static SoundHandler sH;
	private static MysqlSetup databaseManager;
	private static InvMysqlInterface invMysqlInterface;
	private static InventoryDataHandler idH;
	private static BackgroundTask bt;
	
	@Override
    public void onEnable() {
		log = getLogger();
		getMcVersion();
    	configHandler = new ConfigHandler(this);
    	sH = new SoundHandler(this);
    	checkDependency();
    	bt = new BackgroundTask(this);
    	databaseManager = new MysqlSetup(this);
    	invMysqlInterface = new InvMysqlInterface(this);
    	idH = new InventoryDataHandler(this);
    	//Register Listeners
    	PluginManager pm = getServer().getPluginManager();
    	pm.registerEvents(new PlayerJoin(this), this);
    	pm.registerEvents(new PlayerQuit(this), this);
    	pm.registerEvents(new DropItem(this), this);
    	pm.registerEvents(new InventoryClick(this), this);
    	log.info(pluginName + " cargado exitosamente!");
	}
	
	@Override
    public void onDisable() {
		isDisabling = true;
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);
		if (databaseManager.getConnection() != null) {
			bt.onShutDownDataSave();
			databaseManager.closeConnection();
		}
		log.info(pluginName + " desactivado!");
	}
	
	public ConfigHandler getConfigHandler() {
		return configHandler;
	}
	public MysqlSetup getDatabaseManager() {
		return databaseManager;
	}
	public InvMysqlInterface getInvMysqlInterface() {
		return invMysqlInterface;
	}
	public SoundHandler getSoundHandler() {
		return sH;
	}
	public BackgroundTask getBackgroundTask() {
		return bt;
	}
	public InventoryDataHandler getInventoryDataHandler() {
		return idH;
	}
	
	private boolean getMcVersion() {
		String[] serverVersion = Bukkit.getBukkitVersion().split("-");
	    String version = serverVersion[0];
	    
	    // Soportar solo versiones modernas (1.13+)
	    String[] versionParts = version.split("\\.");
	    if (versionParts.length >= 2) {
	    	int major = Integer.parseInt(versionParts[0]);
	    	int minor = Integer.parseInt(versionParts[1]);
	    	
	    	if (major == 1 && minor >= 13) {
	    		is13Server = true;
	    		is19Server = true;
	    		return true;
	    	} else if (major == 1 && minor < 9) {
	    		is19Server = false;
	    		is13Server = false;
	    		return true;
	    	}
	    }
	    // Por defecto asumir versión moderna
	    is13Server = true;
	    is19Server = true;
	    return true;
	}
	
	private void checkDependency() {
		//Check dependency
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
        	useProtocolLib = true;
        	log.info("Dependencia ProtocolLib encontrada.");
        } else {
        	useProtocolLib = false;
        	log.warning("Dependencia ProtocolLib no encontrada. ¡No hay soporte para datos NBT de items personalizados!");
        }
	}
}
