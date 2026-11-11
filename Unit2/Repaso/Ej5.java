public class Ej5 {
    static class SumaParcial implements Runnable {
        private final int[] datos;
        private final int desde;
        private final int hasta;
        private long resultado;

        SumaParcial(int[] datos, int desde, int hasta) {
            this.datos = datos;
            this.desde = desde;
            this.hasta = hasta;
        }

        @Override
        public void run() {
            long suma = 0;
            for (int i = desde; i < hasta; i++) {
                suma += datos[i];
            }
            resultado = suma;
        }
        long getResultado() { return resultado; }
    }

    public static void main(String[] args) throws InterruptedException {
        int[] datos = new int[1000];
        for (int i = 0; i < 1000; i++) {
            datos[i] = i + 1;
        }

        int numWorkers = 4;
        int trozo = datos.length / numWorkers;

        SumaParcial[] workers =  new SumaParcial[numWorkers];
        Thread[] hilos = new Thread[numWorkers];

        for (int i = 0; i < numWorkers; i++) {
            int desde = i * trozo;
            int hasta = desde + trozo;
            workers[i] = new SumaParcial(datos, desde, hasta);
            hilos[i] = new Thread(workers[i]);
            hilos[i].start();
        }

        long total = 0;

        for (int i = 0; i < numWorkers; i++) {
            hilos[i].join();
            total += workers[i].getResultado();
        }

        System.out.println("Suma total: " + total + " (esperado 500500)");


    }
}
