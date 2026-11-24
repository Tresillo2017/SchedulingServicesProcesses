# Carrera de Animales (PSP - Unidad 2, Multihilo)

Simulación de una carrera de 3 animales (Tortuga, Liebre, Zorro) con hilos en Java.

## Contenido
- `Animal.java` – tarea `Runnable`: recorre 10 tramos con `sleep()` aleatorio y llama a `JuezCarrera.llegarMeta()`.
- `JuezCarrera.java` – recurso compartido (meta) protegido con `Semaphore(1, true)`; registra orden y tiempos.
- `CarreraMain.java` – programa principal: crea los hilos, los lanza (`start()`), espera (`join()`) y muestra posiciones.

## Entorno y dependencias
- Java 11 o superior (probado con OpenJDK 27). Sin dependencias externas. Sin paquete (`default package`).

## Ejecución
```bash
javac *.java
java CarreraMain
```
(En IntelliJ/Eclipse: importar la carpeta y ejecutar `CarreraMain`.)
