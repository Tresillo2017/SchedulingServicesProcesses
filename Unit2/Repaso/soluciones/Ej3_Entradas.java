import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/** Ej3: synchronized en un recurso compartido (comprobar + actuar debe ser atomico). */
public class Ej3_Entradas {

    static class Taquilla {
        private int entradasLibres;

        Taquilla(int entradasLibres) { this.entradasLibres = entradasLibres; }

        // Si NO fuera synchronized: dos fans podrian ver "queda 1" a la vez y comprar los dos.
        synchronized boolean comprar(String fan) {
            if (entradasLibres > 0) {
                entradasLibres--;
                System.out.println("[" + fan + "] ha comprado entrada. Quedan " + entradasLibres);
                return true;
            }
            System.out.println("[" + fan + "] se ha quedado sin entrada");
            return false;
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
                TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(0, 200));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            taquilla.comprar(nombre);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Taquilla taquilla = new Taquilla(3);
        Thread[] fans = new Thread[6];
        for (int i = 0; i < fans.length; i++) {
            fans[i] = new Thread(new Fan("Fan" + (i + 1), taquilla));
            fans[i].start();
        }
        for (Thread f : fans) {
            f.join();
        }
        System.out.println("Taquilla cerrada");
    }
}
