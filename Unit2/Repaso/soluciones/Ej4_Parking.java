import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/** Ej4: Semaphore(N) = limitar cuantos hilos usan un recurso a la vez. */
public class Ej4_Parking {

    static class Coche implements Runnable {
        private final String nombre;
        private final Semaphore plazas;

        Coche(String nombre, Semaphore plazas) {
            this.nombre = nombre;
            this.plazas = plazas;
        }

        @Override
        public void run() {
            try {
                System.out.println("[" + nombre + "] llega al parking");
                plazas.acquire();                       // si no hay plaza, espera aqui
                try {
                    System.out.println("[" + nombre + "] APARCA (plazas libres: " + plazas.availablePermits() + ")");
                    TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(500, 1001));
                } finally {
                    System.out.println("[" + nombre + "] sale");
                    plazas.release();                   // libera la plaza
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Semaphore plazas = new Semaphore(2);            // 2 plazas
        Thread[] coches = new Thread[5];
        for (int i = 0; i < coches.length; i++) {
            coches[i] = new Thread(new Coche("Coche" + (i + 1), plazas));
            coches[i].start();
        }
        for (Thread c : coches) {
            c.join();
        }
        System.out.println("Parking vacio");
    }
}
