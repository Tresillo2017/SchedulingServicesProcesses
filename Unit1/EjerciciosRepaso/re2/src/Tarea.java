public class Tarea {
    public static void main(String[] args) throws InterruptedException{
        if (args.length == 0) {
            System.err.println("Falta argumento");
            System.exit(2);
        }

        int segundos = Integer.parseInt(args[0]);

        System.out.println("Trabajando...");
        Thread.sleep(segundos * 1000L);
        System.out.println("Terminado");
    }
}
