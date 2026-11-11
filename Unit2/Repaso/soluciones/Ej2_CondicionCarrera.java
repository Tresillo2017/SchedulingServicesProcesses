/** Ej2: condicion de carrera y como arreglarla con synchronized. */
public class Ej2_CondicionCarrera {

    static class ContadorInseguro {
        private int valor = 0;
        void incrementar() { valor++; }          // NO es atomico: leer, sumar, escribir
        int getValor() { return valor; }
    }

    static class ContadorSeguro {
        private int valor = 0;
        synchronized void incrementar() { valor++; }   // un hilo a la vez
        synchronized int getValor() { return valor; }
    }

    private static final int HILOS = 4;
    private static final int INCREMENTOS = 100_000;

    public static void main(String[] args) throws InterruptedException {
        ContadorInseguro inseguro = new ContadorInseguro();
        ContadorSeguro seguro = new ContadorSeguro();

        lanzarYEsperar(inseguro::incrementar);
        lanzarYEsperar(seguro::incrementar);

        int esperado = HILOS * INCREMENTOS;
        System.out.println("Esperado:  " + esperado);
        System.out.println("Inseguro:  " + inseguro.getValor() + "  (suele ser MENOR)");
        System.out.println("Seguro:    " + seguro.getValor());
    }

    private static void lanzarYEsperar(Runnable incremento) throws InterruptedException {
        Thread[] hilos = new Thread[HILOS];
        for (int i = 0; i < HILOS; i++) {
            hilos[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTOS; j++) {
                    incremento.run();
                }
            });
            hilos[i].start();
        }
        for (Thread h : hilos) {
            h.join();
        }
    }
}
