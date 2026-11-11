package examA.hilos;
//   Indica en qué “paquete” o carpeta lógica se encuentra esta clase dentro del proyecto.
//    Sirve para organizar el código. En este caso, está en la carpeta examA/hilos.

import java.util.concurrent.ThreadLocalRandom;
// 🎲 Importa una herramienta de Java que permite generar números aleatorios.
//    “ThreadLocalRandom” se usa porque funciona bien cuando hay varios hilos ejecutándose a la vez.

import java.util.concurrent.TimeUnit;
//   Importa otra utilidad que facilita trabajar con unidades de tiempo
//    (milisegundos, segundos, nanosegundos, etc.) de forma clara.

public class Corredor implements Runnable {
//    Define una clase pública llamada “Corredor”.
//    Esta clase representa a un corredor individual en la carrera.
//    Implementa la interfaz “Runnable”, lo que significa que puede ejecutarse en un hilo (Thread).
//    Es decir, define lo que hará el hilo cuando empiece a correr.

    private final String nombre;
    //   Guarda el nombre del corredor, por ejemplo "Tortuga" o "Liebre".
    //    Es “final” porque una vez asignado no cambiará durante la ejecución.

    private long tiempoMs;
    //   Almacena cuánto tiempo (en milisegundos) ha tardado el corredor en completar su carrera.
    //    Empieza en 0 y se calcula al final de la ejecución.

    public Corredor(String nombre) { this.nombre = nombre; }
    //   Este es el constructor: sirve para crear un nuevo corredor y asignarle un nombre.
    //    Por ejemplo, cuando en el main se hace: new Corredor("Tortuga").

    public String getNombre() { return nombre; }
    //   Devuelve (muestra) el nombre del corredor cuando se le pregunta desde fuera.
    //    Por ejemplo, se usa para imprimir quién ganó la carrera.

    public long getTiempoMs() { return tiempoMs; }
    //   Devuelve el tiempo total que tardó el corredor en terminar.
    //    Se usa al final para mostrar los resultados.

    @Override
    public void run() {
    //    Este método contiene lo que el corredor va a hacer cuando empiece a correr.
    //    Se ejecuta automáticamente cuando llamamos a “start()” en su hilo.
    //    Es el “corazón” del comportamiento del corredor.

        long t0 = System.nanoTime();
        //    Marca de tiempo inicial: guarda el momento exacto (en nanosegundos) en el que el corredor empieza a correr.
        //    “nanoTime()” se usa porque es muy preciso, mucho más que un simple reloj en milisegundos.

        for (int i = 0; i < 10; i++) {
        //    Este bucle se repetirá 10 veces.
        //    Cada repetición representa un “tramo” de la carrera.
        //    Así simulamos que el corredor avanza tramo a tramo.

            int pausa = ThreadLocalRandom.current().nextInt(50, 151); // 50-150 ms
            //    Aquí se genera un número aleatorio entre 50 y 150.
            //    Representa los milisegundos que el corredor tardará en recorrer ese tramo.
            //    Cada tramo tendrá un tiempo diferente, para simular que unos son más rápidos o más lentos.

            try {
                TimeUnit.MILLISECONDS.sleep(pausa);
                //    El corredor “duerme” o se detiene durante ese tiempo.
                //    En realidad no está parado: el hilo se pone en pausa para simular el tiempo que tarda en avanzar el tramo.
                //    Por ejemplo, si pausa=120, el hilo espera 120 milisegundos (0,12 segundos).
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
                //   Si alguien interrumpe al hilo mientras está “durmiendo”,
                //    se activa esta parte del código.
                //    - “interrupt()” vuelve a marcar el hilo como interrumpido (buenas prácticas).
                //    - “return” hace que el corredor abandone la carrera de inmediato.
                //    Es una forma ordenada de salir si el hilo es cancelado o detenido.
            }
        }

        long t1 = System.nanoTime();
        //    Marca de tiempo final: se toma justo después de terminar los 10 tramos.
        //    Así sabemos el instante exacto en que el corredor termina.

        tiempoMs = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        //    Calcula la diferencia entre el tiempo final (t1) y el inicial (t0).
        //    Eso da el tiempo total en nanosegundos, que se convierte a milisegundos (más legibles).
        //    Finalmente se guarda en la variable “tiempoMs”.
    }
}
