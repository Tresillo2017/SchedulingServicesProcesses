import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class JuezCarrera {
    private final Semaphore meta = new Semaphore(1, true);
    private final List<String> posiciones = new ArrayList<>();

    public void llegarMeta(String equipo, long tiempoMs) {
        try {
            meta.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        try {
            posiciones.add(equipo);
            System.out.println("[" + equipo + "]" + " llega a meta " + tiempoMs);
            if (posiciones.size() == 1) {
                System.out.println(posiciones.get(0) + " ha ganado");
            }
        } finally {
            meta.release();
        }
    }

    public void mostrarPosiciones() {
        System.out.println(">>> Posiciones");
        for (int i = 0; i < posiciones.size(); i++) {
            System.out.println((i + 1) + ". " + posiciones.get(i));
        }
    }
}
