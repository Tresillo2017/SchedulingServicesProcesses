
public class RelevosMain {
    public static void main(String[] args) throws InterruptedException {
        JuezCarrera juez = new JuezCarrera();

        System.out.println("=== Comienza la carrera de relevos! ===");

        Equipo equipoRojo = new Equipo("Rojo", juez);
        Equipo equipoAzul = new Equipo("Azul", juez);

        Thread rojo = new Thread(equipoRojo, "equipo-Rojo");
        Thread azul = new Thread(equipoAzul, "equipo-Azul");

        rojo.start();
        azul.start();

        rojo.join();
        azul.join();

        System.out.println("=== Carrera Finalizada ===");

        juez.mostrarPosiciones();

        System.out.printf("Tiempos (ms): Rojo=%d, Azul=%d%n", equipoRojo.getTiempoMs(), equipoAzul.getTiempoMs());
    }
}