import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/** Ej6: BlockingQueue (extension de la tarea). put() espera si esta llena, take() si esta vacia. */
public class Ej6_ProductorConsumidor {

    private static final int FIN = -1;   // "pastilla venenosa": avisa al consumidor de que no hay mas

    static class Productor implements Runnable {
        private final BlockingQueue<Integer> cola;

        Productor(BlockingQueue<Integer> cola) { this.cola = cola; }

        @Override
        public void run() {
            try {
                for (int i = 1; i <= 8; i++) {
                    cola.put(i);                                    // bloquea si la cola esta llena
                    System.out.println("Produce " + i + "  (en cola: " + cola.size() + ")");
                }
                cola.put(FIN);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Consumidor implements Runnable {
        private final BlockingQueue<Integer> cola;

        Consumidor(BlockingQueue<Integer> cola) { this.cola = cola; }

        @Override
        public void run() {
            try {
                while (true) {
                    int valor = cola.take();                        // bloquea si la cola esta vacia
                    if (valor == FIN) {
                        System.out.println("Consumidor: fin");
                        return;
                    }
                    System.out.println("    Consume " + valor);
                    TimeUnit.MILLISECONDS.sleep(150);               // consumidor mas lento
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> cola = new ArrayBlockingQueue<>(3);  // capacidad 3
        Thread p = new Thread(new Productor(cola));
        Thread c = new Thread(new Consumidor(cola));
        p.start();
        c.start();
        p.join();
        c.join();
    }
}
