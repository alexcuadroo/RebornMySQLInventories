# RebornMySQLInventories - Actualizado para Minecraft 1.21

## Cambios realizados (Versión 2.5.0)

### ✅ Arreglado para compilación
- ✅ Creado `pom.xml` con dependencias de Spigot 1.21.4 y ProtocolLib 5.3.0
- ✅ Configurado Java 21 como requisito
- ✅ Actualizado `api-version` a 1.21 en `plugin.yml`

### ✅ Código actualizado
- ✅ Eliminadas todas las llamadas a `updateInventory()` (deprecado desde 1.9)
- ✅ Mejorado el sistema de detección de versiones para soportar 1.13+
- ✅ Marcado `is13Server = true` por defecto para API modernas
- ✅ Simplificado `getMcVersion()` para aceptar versiones 1.13 en adelante

### 🔧 Cómo compilar

```bash
mvn clean package
```

El archivo JAR compilado estará en: `target/RebornMySQLInventories-2.5.0-1.21.jar`

### 📋 Requisitos
- Java 21
- Maven 3.x
- Servidor Spigot/Paper 1.21.x

### ⚠️ Notas importantes
- **Este plugin solo funcionará en Minecraft 1.21+**
- No se garantiza compatibilidad con versiones anteriores
- Se recomienda usar con ProtocolLib para soporte de items con NBT

### 🔍 Problemas conocidos
El código original tiene algunos problemas que pueden necesitar atención:
- Sistema de sincronización puede tener condiciones de carrera
- Falta manejo de errores en algunas partes de la DB
- No hay sistema de migración de base de datos

### 📝 Estructura de base de datos
Revisar las tablas MySQL creadas por el plugin en la primera ejecución.
