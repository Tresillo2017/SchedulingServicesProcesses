import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

public class CalculadoraProcesos {
    public static void main(String[] args) throws IOException, InterruptedException{
        String numero = ("12 5");

        String rutaJava = System.getProperty("java.home")
                + File.separator + "bin"
                + File.separator + "java";

        String classpath = System.getProperty("java.class.path");

        ProcessBuilder pbProducto =
                new ProcessBuilder(
                        rutaJava, "-cp", classpath, "Producto"
                );

        ProcessBuilder pbResta =
                new ProcessBuilder(
                        rutaJava, "-cp", classpath, "Resta"
                );

        ProcessBuilder pbSuma =
                new ProcessBuilder(
                        rutaJava, "-cp", classpath, "Suma"
                );

        Process suma = pbSuma.start();
        Process resta = pbResta.start();
        Process producto = pbProducto.start();

        BufferedWriter entrada =
                new BufferedWriter(new OutputStreamWriter(suma.getOutputStream()));

        BufferedWriter entrada2 =
                new BufferedWriter(new OutputStreamWriter(resta.getOutputStream()));

        BufferedWriter entrada3 =
                new BufferedWriter(new OutputStreamWriter(producto.getOutputStream()));

        entrada.write(numero);
        entrada2.write(numero);
        entrada3.write(numero);

        entrada.newLine();
        entrada2.newLine();
        entrada3.newLine();

        entrada.flush();
        entrada2.flush();
        entrada3.flush();

        entrada.close();
        entrada2.close();
        entrada3.close();

        suma.waitFor();
        resta.waitFor();
        producto.waitFor();

        BufferedReader lector =
                new BufferedReader(new InputStreamReader(suma.getInputStream()));

        BufferedReader lector2 =
                new BufferedReader(new InputStreamReader(resta.getInputStream()));

        BufferedReader lector3 =
                new BufferedReader(new InputStreamReader(producto.getInputStream()));

        String resultadosuma = lector.readLine();
        String resultadoresta = lector2.readLine();
        String resultadoproducto = lector3.readLine();


        System.out.println("Suma: " + resultadosuma);
        System.out.println("Resta: " + resultadoresta);
        System.out.println("Producto: " + resultadoproducto);
    }
}
