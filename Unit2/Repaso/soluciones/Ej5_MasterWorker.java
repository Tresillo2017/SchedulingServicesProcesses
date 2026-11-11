/** Ej5: Master-Worker. El master reparte, los workers calculan, el master junta con join(). */
public class Ej5_MasterWorker {

    static class SumaParcial implements Runnable {
        private final int[] datos;
        private final int desde;      // incluido
        private final int hasta;      // excluido
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

        long getResultado() { return resultado; }   // solo leerlo DESPUES del join()
    }

    public static void main(String[] args) throws InterruptedException {
        int[] datos = new int[1000];
        for (int i = 0; i < datos.length; i++) {
            datos[i] = i + 1;                       // 1..1000 -> suma = 500500
        }

        int numWorkers = 4;
        int trozo = datos.length / numWorkers;
        SumaParcial[] workers = new SumaParcial[numWorkers];
        Thread[] hilos = new Thread[numWorkers];

        for (int i = 0; i < numWorkers; i++) {
            int desde = i * trozo;
            int hasta = (i == numWorkers - 1) ? datos.length : desde + trozo;
            workers[i] = new SumaParcial(datos, desde, hasta);
            hilos[i] = new Thread(workers[i]);
            hilos[i].start();
        }

        long total = 0;
        for (int i = 0; i < numWorkers; i++) {
            hilos[i].join();                        // esperar ANTES de leer el resultado
            System.out.println("Worker " + i + " -> " + workers[i].getResultado());
            total += workers[i].getResultado();
        }
        System.out.println("Suma total: " + total + " (esperado 500500)");
    }
}
