# Spiral Mecha — Forge 1.20.1

Proyecto inicial de mod para Minecraft Java 1.20.1 / Forge 47.x / GeckoLib 4.4.9.

## Modelos importados
- Laggan: 55 cubos, 12 animaciones; escala geométrica uniforme 0.7547; altura objetivo 2,5 bloques.
- Gurren Lagann: 227 cubos, 14 animaciones; escala geométrica uniforme 0.6173; altura objetivo 20 bloques.

Los modelos y texturas se convierten desde los `.bbmodel` adjuntos a JSON de geometría/animación GeckoLib. Se conserva la jerarquía de grupos y los keyframes exportables. Revisa visualmente la orientación y UV en Blockbench/cliente; algunas particularidades de Blockbench pueden requerir retoque.

## Compilar
Requiere JDK 17 y conexión a Internet para descargar ForgeGradle y GeckoLib.

```bash
./gradlew genIntellijRuns
./gradlew build
```
En Windows: `gradlew.bat build`.

El JAR queda en `build/libs/`.

## Controles previstos
WASD, Espacio, Shift, clic izquierdo, 1-3, R, G y X. Las teclas de habilidad y pilotaje se configuran desde Opciones > Controles.

## Estado
Este paquete es una primera base jugable de desarrollo: invocación, pilotaje, movimiento, transformación, energía, ataques y HUD están implementados como sistemas iniciales. Debe compilarse y probarse en un cliente Forge 1.20.1 con GeckoLib 4.4.9 antes de distribuirlo.
