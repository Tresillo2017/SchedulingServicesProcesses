import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Un animal = una tarea (Runnable) que se ejecutara en su propio hilo.
 */
public class Animal implements Runnable {

    private static final int TOTAL_TRAMOS = 10;
    private static final int PAUSA_MIN_MS = 100;
    private static final int PAUSA_MAX_MS = 400;

    private final String nombre;
    private final JuezCarrera juez;   // meta compartida por todos los animales

    public Animal(String nombre, JuezCarrera juez) {
        this.nombre = nombre;
        this.juez = juez;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public void run() {
        for (int tramo = 1; tramo <= TOTAL_TRAMOS; tramo++) {
            System.out.println("[" + nombre + "] avanzando tramo " + tramo + "...");
            try {
                // sleep aleatorio = velocidad variable en cada tramo
                long pausa = ThreadLocalRandom.current().nextLong(PAUSA_MIN_MS, PAUSA_MAX_MS + 1);
                TimeUnit.MILLISECONDS.sleep(pausa);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[" + nombre + "] abandona la carrera (interrumpido)");
                return;
            }
        }
        juez.llegarMeta(nombre);   // tramo 10 completado
    }
}
