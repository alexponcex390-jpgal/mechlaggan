# Pruebas pendientes en Minecraft

1. Ejecutar `gradle build` con JDK 17 y conexión a Internet.
2. Instalar el JAR en Forge 1.20.1 junto con GeckoLib 4.4.9.
3. Obtener el núcleo con `/give @p spiralmecha:spiral_core`.
4. Usarlo sobre un bloque para invocar Laggan y comprobar el pilotaje.
5. Comprobar animaciones, cámara y asiento en primera/tercera persona.
6. Probar G y transformación inversa; verificar que el piloto siga montado.
7. Probar habilidades 1-3, R, energía, enfriamientos y daño.
8. Revisar la orientación de los modelos y UV en el juego.

Nota: el control de Shift en Minecraft está ligado por defecto a desmontarse. La combinación exacta de correr con Shift y desmontarse con X requiere una prueba de entrada en cliente; si Minecraft desmonta antes de recibir el paquete, habrá que interceptar esa entrada en una siguiente revisión.
