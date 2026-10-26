import java.io.*;

public class ProcesadorTextos {
    public static void main(String[] args) throws IOException, InterruptedException {

        String frase = "hola mundo desde java";

        String rutaJava = System.getProperty("java.home")
                + File.separator + "bin"
                + File.separator + "java";

        String classpath = System.getProperty("java.class.path");

        ProcessBuilder pbContarPalabras = new ProcessBuilder(
                rutaJava, "-cp", classpath, "ContarPalabras"
        );

        ProcessBuilder pbInvertir = new ProcessBuilder(
                rutaJava, "-cp", classpath, "Invertir"
        );

        ProcessBuilder pbMayusculas = new ProcessBuilder(
                rutaJava, "-cp", classpath, "Mayusculas"
        );

        Process contarpalabras = pbContarPalabras.start();
        Process invertir = pbInvertir.start();
        Process mayusculas = pbMayusculas.start();

        BufferedWriter entrada =
                new BufferedWriter(new OutputStreamWriter(contarpalabras.getOutputStream()));

        BufferedWriter entrada2 =
                new BufferedWriter(new OutputStreamWriter(invertir.getOutputStream()));

        BufferedWriter entrada3 =
                new BufferedWriter(new OutputStreamWriter(mayusculas.getOutputStream()));

        entrada.write(frase);
        entrada2.write(frase);
        entrada3.write(frase);

        entrada.newLine();
        entrada2.newLine();
        entrada3.newLine();

        entrada.flush();
        entrada2.flush();
        entrada3.flush();

        entrada.close();
        entrada2.close();
        entrada3.close();

        contarpalabras.waitFor();
        invertir.waitFor();
        mayusculas.waitFor();

        BufferedReader lectura =
                new BufferedReader(new InputStreamReader(contarpalabras.getInputStream()));

        BufferedReader lectura2 =
                new BufferedReader(new InputStreamReader(invertir.getInputStream()));

        BufferedReader lectura3 =
                new BufferedReader(new InputStreamReader(mayusculas.getInputStream()));

        String resultadocontar = lectura.readLine();
        String resultadoinvertir = lectura2.readLine();
        String resultadomayusculas = lectura3.readLine();


        System.out.println("Mayusculas: " + resultadomayusculas);
        System.out.println("Invertir: " + resultadoinvertir);
        System.out.println("ContarPalabras " + resultadocontar);

    }
}
