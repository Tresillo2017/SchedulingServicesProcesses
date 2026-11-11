import java.util.concurrent.ThreadLocalRandom;

public class Ej1 {
    static class Paseante implements Runnable {
        private final String nombre;

        Paseante(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                System.out.println("[" + nombre + "]" + " paso " + i);
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(100, 301));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread a = new Thread(new Paseante("Ana"));
        Thread b = new Thread(new Paseante("Beto"));
        Thread c = new Thread(new Paseante("Carla"));
        a.run();
        b.run();
        c.run();
        a.join();
        b.join();
        c.join();

        System.out.println("Todos han terminado");
    }
}