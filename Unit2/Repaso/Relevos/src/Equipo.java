import java.util.concurrent.TimeUnit;

public class Equipo implements Runnable {
    private final String nombre;
    private final JuezCarrera juez;

    private long tiempoMs;
    public Equipo(String nombre, JuezCarrera juez) {
        this.nombre = nombre;
        this.juez = juez;
    }

    public long getTiempoMs() { return tiempoMs; }

    @Override
    public void run() {
        long t0 = System.nanoTime();
        try {
            for (int numero = 1; numero <= 4; numero++) {
                Thread hilo = new Thread(new Relevista(nombre, numero));
                hilo.start();
                hilo.join();
            }
            long t1 = System.nanoTime();
            tiempoMs = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
            juez.llegarMeta(nombre, tiempoMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[" + nombre + "]" + " relevista interrumpido");
        }
    }
}
