import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;

public class SumaDosProcesos {
    public static void main(String[] args) throws IOException, InterruptedException {
        int numero = 7;

        String rutaJava = System.getProperty("java.home")
                + File.separator + "bin"
                + File.separator + "java";

        String classpath = System.getProperty("java.class.path");


        ProcessBuilder pbDoble = new ProcessBuilder(
                rutaJava, "-cp", classpath, "Doble"
        );

        ProcessBuilder pbTriple = new ProcessBuilder(
                rutaJava, "-cp", classpath, "Triple"
        );

        Process procesoDoble = pbDoble.start();
        Process procesoTriple = pbTriple.start();

        BufferedWriter entrada
                = new BufferedWriter(new OutputStreamWriter(procesoDoble.getOutputStream()));

        BufferedWriter entrada2
                = new BufferedWriter(new OutputStreamWriter(procesoTriple.getOutputStream()));

        entrada.write(Integer.toString(numero));
        entrada2.write(Integer.toString(numero));

        entrada.newLine();
        entrada2.newLine();

        entrada.flush();
        entrada2.flush();

        entrada.close();
        entrada2.close();

        procesoDoble.waitFor();
        procesoTriple.waitFor();

        BufferedReader lectorDoble =
                new BufferedReader(new InputStreamReader(procesoDoble.getInputStream()));

        BufferedReader lectorTriple =
                new BufferedReader(new InputStreamReader(procesoTriple.getInputStream()));

        String resultadoDoble = lectorDoble.readLine();
        lectorDoble.close();

        String resultadoTriple = lectorTriple.readLine();
        lectorTriple.close();

        System.out.println("Resultado doble " + resultadoDoble);
        System.out.println("Resultado triple " + resultadoTriple);
        int sumaambos = Integer.parseInt(resultadoDoble) + Integer.parseInt(resultadoTriple);
        System.out.println("Suma ambos = " + sumaambos);
    }
}
