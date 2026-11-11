import java.util.concurrent.ThreadLocalRandom;

public class Ej3 {
    static class Taquilla {
        private int entradasLibres;

        Taquilla(int entradasLibres) {this.entradasLibres = entradasLibres; }

        synchronized boolean comprar(String fan) {
            if (entradasLibres > 0) {
                entradasLibres--;
                System.out.println("[" + fan + "]" + " ha comprado entrada. Quedan " + entradasLibres);
                return true;
            } else {
                System.out.println("[" + fan + "]" + " se ha quedado sin entrada");
                return false;
            }
        }
    }

    static class Fan implements Runnable {
        private final String nombre;
        private final Taquilla taquilla;

        Fan(String nombre, Taquilla taquilla) {
            this.nombre = nombre;
            this.taquilla = taquilla;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(0, 200));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            taquilla.comprar(nombre);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Taquilla taquilla = new Taquilla(3);

        Thread[] hilos = new Thread[6];

        for (int i = 0; i < hilos.length; i++) {
            hilos[i] = new Thread(new Fan("Fan" + (i + 1), taquilla));
            hilos[i].start();
        }

        for (int i = 0; i < hilos.length; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.println("Taquilla cerrada");
    }
}
