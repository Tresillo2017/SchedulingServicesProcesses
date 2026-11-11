/**
 * Programa principal (rol "master"): crea al juez y a los animales (workers),
 * lanza los hilos, espera a que todos terminen y anuncia las posiciones.
 */
public class CarreraMain {

    public static void main(String[] args) throws InterruptedException {
        JuezCarrera juez = new JuezCarrera();

        Thread tortuga = new Thread(new Animal("Tortuga", juez), "hilo-Tortuga");
        Thread liebre  = new Thread(new Animal("Liebre", juez), "hilo-Liebre");
        Thread zorro   = new Thread(new Animal("Zorro", juez), "hilo-Zorro");

        System.out.println("=== ¡Comienza la carrera! ===");
        tortuga.start();   // start() crea el hilo y llama a run() en paralelo
        liebre.start();    // (run() directo NO crearia hilo: se ejecutaria en el main)
        zorro.start();

        // join(): el main se bloquea hasta que ese hilo termina.
        tortuga.join();
        liebre.join();
        zorro.join();

        System.out.println("=== Carrera finalizada ===");
        juez.mostrarPosiciones();
    }
}
