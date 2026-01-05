# RebornMySQLInventories

Plugin de Minecraft para sincronizar inventarios de jugadores entre múltiples servidores usando MySQL. Fork de https://github.com/brunyman

**Versión:** 2.5.x-1.21  
**Minecraft:** 1.21+ (Spigot/Paper)  
**Java:** 21
---

##  ¿Qué hace este plugin?

Sincroniza automáticamente el inventario y armadura de los jugadores a través de una base de datos MySQL, permitiendo que los jugadores mantengan sus items al cambiar entre servidores de una red BungeeCord/Velocity.

---

## Requisitos

### Obligatorios:
- Servidor Spigot/Paper 1.21+
- Java 21
- Servidor MySQL o MariaDB

### Opcionales:
- **ProtocolLib** - Solo necesario si usas items con datos NBT personalizados (mods, plugins custom, etc.)

**Respuesta corta:** **NO necesitas ProtocolLib para usar el plugin normalmente**. Solo es necesario si tienes:
- Servidores con mods que añaden items personalizados
- Plugins que crean items con NBT complejo
- Items custom con metadatos especiales

Si solo usas items vanilla de Minecraft, el plugin funciona perfectamente sin ProtocolLib.

---

## Instalación

### 1. Preparar la base de datos MySQL

Conecta a tu servidor MySQL y crea una base de datos:

```sql
CREATE DATABASE minecraft_inventory;
CREATE USER 'minecraft'@'%' IDENTIFIED BY 'tu_contraseña_segura';
GRANT ALL PRIVILEGES ON minecraft_inventory.* TO 'minecraft'@'%';
FLUSH PRIVILEGES;
```

### 2. Instalar el plugin

1. Descarga el JAR compilado: `RebornMySQLInventories-x.x.x-1.21.jar`
2. Coloca el archivo en la carpeta `plugins/` de **cada servidor** que quieras sincronizar
3. **(Opcional)** Si necesitas soporte de items con NBT, instala [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/) en todos los servidores

### 3. Primera ejecución

Inicia el servidor para generar el archivo de configuración:

```bash
[RebornMySQLInventories] ¡No se encontró archivo de configuración! Creando uno nuevo...
[RebornMySQLInventories] Cargando archivo de configuración...
```

Detén el servidor y configura el archivo `plugins/RebornMySQLInventories/config.yml`

---

## Configuración

Edita `plugins/RebornMySQLInventories/config.yml`:

### Configuración básica de MySQL:

```yaml
database:
    mysql:
        # Dirección del servidor MySQL
        host: 127.0.0.1
        # Puerto (normalmente 3306)
        port: 3306
        # Nombre de la base de datos creada anteriormente
        databaseName: 'minecraft_inventory'
        # Tabla (se crea automáticamente)
        tableName: 'meb_inventory'
        # Usuario MySQL
        user: 'minecraft'
        # Contraseña
        password: 'tu_contraseña_segura'
        # SSL (true si tu MySQL lo requiere)
        sslEnabled: false
```

### Opciones importantes:

```yaml
General:
    # Guardar datos automáticamente cada X minutos (recomendado para evitar pérdida de datos)
    saveDataTask:
        enabled: true
        interval: 3  # minutos
        
    # Delay de sincronización al cambiar de servidor (en ms)
    loginSyncDelay: 1000
    
    # Sincronizar armadura además del inventario
    syncArmorEnabled: true
    
    # ⚠️ SOLO ACTIVAR SI TIENES PROTOCOLLIB INSTALADO
    # Para items con NBT personalizado (mods/plugins custom)
    enableModdedItemsSupport: false
```

### Mantenimiento de base de datos:

```yaml
    maintenance:
        # Limpiar jugadores inactivos automáticamente
        enabled: true
        # Días de inactividad antes de eliminar
        inactivity: 60
```

---

## Uso

### El plugin funciona automáticamente:

1. **Al conectarse un jugador:**
   - El plugin carga su inventario desde la base de datos
   - Reemplaza el inventario local con el sincronizado
   - Muestra mensaje: `[MIB] Data sync complete!`

2. **Al desconectarse:**
   - Guarda el inventario actual en la base de datos
   - Los cambios están disponibles en todos los servidores

3. **Guardado periódico:**
   - Si está habilitado, guarda datos cada X minutos (configurable)
   - Previene pérdida de datos por crashes

### Configuración para redes BungeeCord/Velocity:

1. Instala el plugin en **todos los servidores** de la red
2. Configura la **misma base de datos MySQL** en todos
3. Asegúrate que todos usen el **mismo `tableName`**
4. **IMPORTANTE:** Si activas `enableModdedItemsSupport`, debe estar activado en TODOS los servidores

---

## Debug y solución de problemas

### Activar mensajes de debug:

```yaml
Debug:
    InventorySync: true
```

Esto mostrará mensajes detallados en consola:
```
[RebornMySQLInventories] Inventory Debug - Save Data - Start - PlayerName
[RebornMySQLInventories] Inventory Debug - Set Data - Loading inventory - PlayerName
```

### Problemas comunes:

#### ❌ "Could not connect to MySQL"
- Verifica que MySQL esté ejecutándose
- Comprueba host, puerto, usuario y contraseña
- Asegúrate que el usuario tiene permisos en la base de datos

#### ❌ "Item data loss" o items desaparecen
- Si usas items con NBT custom, activa `enableModdedItemsSupport: true`
- Instala ProtocolLib en todos los servidores
- Verifica que todos los servidores tengan la misma configuración

#### ❌ Inventario no sincroniza entre servidores
- Confirma que todos los servidores usan la misma base de datos
- Verifica que `tableName` sea idéntico en todos
- Revisa que no haya errores de conexión MySQL en la consola

#### ⚠️ Jugador pierde items al cambiar de servidor muy rápido
- Aumenta `loginSyncDelay` a 2000 o 3000 ms
- Activa `saveDataTask.enabled: true` con intervalo corto

---

## Mensajes personalizables

Puedes personalizar los mensajes en `config.yml`:

```yaml
ChatMessages:
    syncComplete: '&2[MIB] &aSincronización completa!'
    inventorySyncError: '&4[WARNING] &cError al cargar el inventario, contacta con el administrador.'
    inventorySyncBackup: '&6[!] &eInventario local restaurado! No dejes nada en tu inventario al desconectarte'
```

Códigos de color: [Minecraft Formatting Codes](https://minecraft.fandom.com/wiki/Formatting_codes)

---

## Compilación desde código fuente

Si quieres modificar el plugin:

```bash
# Clonar/descargar el proyecto
cd MysqlInventoryBridge

# Compilar con Maven
mvn clean package

# El JAR estará en:
# target/RebornMySQLInventories-x.x.x-1.21.jar
```

**Requisitos para compilar:**
- Maven 3.x
- Java Development Kit (JDK) 21

---

## Advertencias importantes

1. **Haz backups regulares de la base de datos MySQL** - Contiene todos los inventarios
2. **No mezcles versiones diferentes** del plugin entre servidores
3. **No cambies `enableModdedItemsSupport`** en servidores en producción sin backup
4. **Todos los servidores deben usar la misma configuración** de soporte NBT
5. Este plugin **solo funciona en Minecraft 1.21+**, no soporta versiones anteriores

---

## Créditos

- **Autor original:** Brunyman
- **Proyecto original:** InventoryBridge / MysqlInventoryBridge
- **Website:** https://github.com/brunyman/InventoryBridge

---

## Licencia

Esta versión actualizada es una restauración comunitaria para mantener el proyecto funcional en versiones modernas de Minecraft.
