import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/** Ej1: Runnable + Thread + start + join + sleep. */
public class Ej1_HilosBasicos {

    static class Paseante implements Runnable {
        private final String nombre;

        Paseante(String nombre) { this.nombre = nombre; }

        @Override
        public void run() {
            for (int paso = 1; paso <= 5; paso++) {
                System.out.println("[" + nombre + "] paso " + paso);
                try {
                    TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(100, 301));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            System.out.println("[" + nombre + "] ha terminado");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread a = new Thread(new Paseante("Ana"));
        Thread b = new Thread(new Paseante("Beto"));
        Thread c = new Thread(new Paseante("Carla"));

        a.start();
        b.start();
        c.start();

        a.join();
        b.join();
        c.join();

        System.out.println("Todos han terminado");   // siempre sale el ultimo gracias a join()
    }
}
