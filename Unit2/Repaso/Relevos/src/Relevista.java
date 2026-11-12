import java.util.concurrent.ThreadLocalRandom;

public class Relevista implements Runnable {
    private final String equipo;
    private final int numero;

    public Relevista(String equipo, int numero) {
        this.equipo = equipo;
        this.numero = numero;
    }

    @Override
    public void run() {
        System.out.println("["+ equipo + "-" + numero + "]"  + " sale...");
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(50, 201));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("["+ equipo + "-" + numero + "]"  + " interrumpido");
                return;
            }
        }
        System.out.println("["+ equipo + "-" + numero + "]" + " pasa el testigo");
    }
}
