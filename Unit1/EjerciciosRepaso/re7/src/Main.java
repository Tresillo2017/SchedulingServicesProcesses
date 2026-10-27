import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String metodo = "sumar";
        String a = "8";
        String b = "4";


        String rutaJava = System.getProperty("java.home")
                + File.separator + "bin"
                + File.separator + "java";

        String classpath = System.getProperty("java.class.path");

        ProcessBuilder pbOperaciones = new ProcessBuilder(
                rutaJava, "-cp", classpath, "Operaciones", metodo, a, b
        );
        pbOperaciones.redirectError();

        Process operaciones = pbOperaciones.start();

        BufferedReader lectura =
                new BufferedReader(new InputStreamReader(operaciones.getInputStream()));

        String linea;
        while ((linea = lectura.readLine()) != null) {
            System.out.println(linea);
        }

        operaciones.waitFor();
    }
}
