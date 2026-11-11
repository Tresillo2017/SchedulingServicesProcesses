import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Ej4 {
    static class Coche implements Runnable{
        private final String nombre;
        private final Semaphore plazas;

        Coche(String nombre, Semaphore plazas) {
            this.nombre = nombre;
            this.plazas = plazas;
        }

        @Override
        public void run() {
            System.out.println("[" + nombre + "]" + " llega al parking");
            try {
                plazas.acquire();
                try {
                    System.out.println("[" + nombre + "]" + " APARCA");
                    Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1000));
                } finally {
                    System.out.println("[" + nombre + "]" + " sale");
                    plazas.release();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }

    public static void main(String[] args) {
        Semaphore plazas = new Semaphore(2);

        Thread[] hilos = new Thread[5];

        for (int i = 0; i < hilos.length; i++) {
            hilos[i] = new Thread(new Coche("Coche" + (i + 1), plazas));
            hilos[i].start();
        }

        for  (int i = 0; i < hilos.length; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.println("Parking vacio");
    }
}
