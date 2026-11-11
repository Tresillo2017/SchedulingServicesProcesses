public class Ej2 {

    static class Contador {
        private int valor = 0;

        synchronized void incrementar() { valor++; }
        synchronized int getValor() { return valor; }
    }

    static class Incrementador implements Runnable {
        private final Contador contador;

        Incrementador(Contador contador) { this.contador = contador; }

        @Override
        public void run() {
            for (int i = 0; i < 100_000; i++) {
                contador.incrementar();
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Contador contador = new Contador();

        Thread[] hilos = new Thread[4];

        for (int i = 0; i <= 3; i++) {
            hilos[i] = new Thread(new Incrementador(contador));
            hilos[i].start();
        }

        for (int i = 0; i < hilos.length; i++) {
            hilos[i].join();
        }

        System.out.println("Esperando 400000");
        System.out.println("Obtenido: " + contador.getValor());
    }
}
