import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String[] metodos = {"kmAmillas", "kgALibras", "cmAPulgadas"};

        String valor = "10";

        String rutaJava = System.getProperty("java.home")
                + File.separator + "bin"
                + File.separator + "java";

        String classpath = System.getProperty("java.class.path");

        for (int i = 0; i < metodos.length; i++) {
            ProcessBuilder pbConversor = new ProcessBuilder(
                    rutaJava, "-cp", classpath, "Conversor", metodos[1], valor
            );

            pbConversor.redirectErrorStream(true);

            Process conversor = pbConversor.start();

            BufferedReader lectura =
                    new BufferedReader(new InputStreamReader(conversor.getInputStream()));

            String linea;
            while ((linea = lectura.readLine()) != null) {
                System.out.println(linea);
            }
            lectura.close();

            int exit = conversor.waitFor();
            System.out.println("Exit code: " + exit);
        }
    }
}
