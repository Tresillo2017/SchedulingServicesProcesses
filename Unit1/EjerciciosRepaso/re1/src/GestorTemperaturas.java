import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;

public class GestorTemperaturas {
    public static void main(String[] args) throws IOException, InterruptedException {
        double celsius = 25;

        String rutaJava = System.getProperty("java.home")
                + File.separator + "bin"
                + File.separator + "java";

        String classpath = System.getProperty("java.class.path");



        ProcessBuilder pbFahrenheit = new ProcessBuilder(
                rutaJava, "-cp", classpath, "CelsiusAFahrenheit"
        );

        Process procesoFahrenheit = pbFahrenheit.start();

        ProcessBuilder pbKelvin = new ProcessBuilder(
                rutaJava, "-cp", classpath, "CelsiusAKelvin"
        );

        Process procesoKelvin = pbKelvin.start();

        BufferedWriter entrada
                = new BufferedWriter(new OutputStreamWriter(procesoFahrenheit.getOutputStream()));

        entrada.write(Double.toString(celsius));

        entrada.newLine();
        entrada.flush();
        entrada.close();

        BufferedWriter entrada2
                = new BufferedWriter(new OutputStreamWriter(procesoKelvin.getOutputStream()));

        entrada2.write(Double.toString(celsius));

        entrada2.newLine();
        entrada2.flush();
        entrada2.close();

        procesoFahrenheit.waitFor();
        procesoKelvin.waitFor();

        BufferedReader lectorFahrenheit
                = new BufferedReader(new InputStreamReader(procesoFahrenheit.getInputStream()));

        String resultadoFahrenheit = lectorFahrenheit.readLine();
        lectorFahrenheit.close();

        BufferedReader lectorKelvin
                = new BufferedReader(new InputStreamReader(procesoKelvin.getInputStream()));

        String resultadoKelvin = lectorKelvin.readLine();
        lectorKelvin.close();

        System.out.println("Resultado Fahrenheit: " + resultadoFahrenheit);
        System.out.println("Resultado Kelvin: " + resultadoKelvin);
    }
}
