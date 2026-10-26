import java.io.*;

public class LanzadorSaludo {
    public static void main(String[] args) throws IOException, InterruptedException {
        String nombre = "Ana";

        String rutaJava = System.getProperty("java.home")
                + File.separator + "bin"
                + File.separator + "java";

        String classpath = System.getProperty("java.class.path");


        ProcessBuilder pbLanzadorSaludo = new ProcessBuilder(
                rutaJava, "-cp", classpath, "Saludo"
        );

        Process lanzadorSaludo = pbLanzadorSaludo.start();

        BufferedWriter entrada =
                new BufferedWriter(new OutputStreamWriter(lanzadorSaludo.getOutputStream()));

        entrada.write(nombre);

        entrada.newLine();

        entrada.flush();

        entrada.close();

        lanzadorSaludo.waitFor();

        BufferedReader lectura =
                new BufferedReader(new InputStreamReader(lanzadorSaludo.getInputStream()));

        String resultado = lectura.readLine();

        System.out.println(resultado);
    }
}
