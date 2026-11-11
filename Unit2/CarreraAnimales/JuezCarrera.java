import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Recurso compartido de la carrera: la "meta".
 * Todos los animales (hilos) llaman a llegarMeta(), pero solo UNO a la vez
 * puede estar dentro de la zona critica. Asi nunca hay dos ganadores.
 */
public class JuezCarrera {

    // Semaforo binario (1 permiso) = funciona como un cerrojo (exclusion mutua).
    // "true" = justo (fair): los hilos pasan en el orden en que llegaron a la cola.
    private final Semaphore meta = new Semaphore(1, true);

    // Orden de llegada. Solo se modifica dentro de la zona critica.
    private final List<String> posiciones = new ArrayList<>();

    // Instante de salida, para calcular el tiempo de llegada de cada animal.
    private final long inicioMs = System.currentTimeMillis();

    /** Llamado por cada animal al terminar el tramo 10. */
    public void llegarMeta(String nombre) {
        try {
            meta.acquire();                       // entrar: si esta ocupado, el hilo espera
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();   // buena practica: restaurar la marca
            return;
        }
        try {
            // ---- ZONA CRITICA: un unico hilo a la vez ----
            long tiempo = System.currentTimeMillis() - inicioMs;
            posiciones.add(nombre);
            System.out.println("[" + nombre + "] ha llegado a la meta 🏁 (" + tiempo + " ms)");
            if (posiciones.size() == 1) {         // el primero en entrar es el ganador
                System.out.println(">> ¡Ganador: " + nombre + "! 🥇");
            }
            // ----------------------------------------------
        } finally {
            meta.release();                       // salir SIEMPRE, aunque haya excepcion
        }
    }

    /** Se llama desde el main despues de los join(), cuando ya han llegado todos. */
    public void mostrarPosiciones() {
        System.out.println(">> Posiciones:");
        for (int i = 0; i < posiciones.size(); i++) {
            System.out.println((i + 1) + ". " + posiciones.get(i));
        }
    }

    public List<String> getPosiciones() {
        return Collections.unmodifiableList(posiciones);
    }
}
