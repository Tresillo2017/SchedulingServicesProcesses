import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
/*
* Lanza tres procesos externos y les envia un numero por stdin
*/
public class GestorProcesos {
    public static void main(String[] args) {
        int numeroCuadrado = 5;
        int numeroFactorial = 4;
        int numeroPrimo = 7;

        String java = System.getProperty("java.home")
                + File.separator + "bin"
                + File.separator + "java";

        String classpath = System.getProperty("java.class.path");

        Process cuadrado = null;
        Process factorial = null;
        Process primo = null;

        try {
            System.out.println("[GestorProcesos] Iniciando proceso para cuadrado...");
            cuadrado = iniciarProceso(java, classpath, "Cuadrado");

            System.out.println("[GestorProcesos] Iniciando proceso para factorial...");
            factorial = iniciarProceso(java, classpath, "Factorial");

            System.out.println("[GestorProcesos] Iniciando proceso para numero primo...");
            primo = iniciarProceso(java, classpath, "Primo");

            // Enviamos los datos a los tres procesos
            enviarNumero(cuadrado, numeroCuadrado);
            enviarNumero(factorial, numeroFactorial);
            enviarNumero(primo, numeroPrimo);

            // Cada proceso produce una linea pequena de salida
            String resultadoCuadrado = leerResultado(cuadrado);
            String resultadoFactorial = leerResultado(factorial);
            String resultadoPrimo = leerResultado(primo);

            // Recogemos el codigo de salida de cada proceso
            int codigoCuadrado = esperarProceso(cuadrado);
            int codigoFactorial = esperarProceso(factorial);
            int codigoPrimo = esperarProceso(primo);

            // Comprueba que los tres procesos terminaron correctamente
            comprobarCodigo("Cuadrado", codigoCuadrado);
            comprobarCodigo("Factorial", codigoFactorial);
            comprobarCodigo("Primo", codigoPrimo);

            System.out.println("[Proceso Cuadrado] El cuadrado de " + numeroCuadrado + " es: " + resultadoCuadrado);
            System.out.println("[Proceso Factorial] El factorial de " + numeroFactorial + " es: " + resultadoFactorial);
            System.out.println("[Proceso Primo] El primo de " + numeroPrimo + " es: " + resultadoPrimo);

            System.out.println("[Gestor Procesos] Todos los procesos finalizaron correctamente.");
        } catch (IOException e) {
            System.err.println("[Gestor Procesos] Error de entrada/salida: "
                    + e.getMessage());
            destruirSiSigueActivo(cuadrado);
            destruirSiSigueActivo(factorial);
            destruirSiSigueActivo(primo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[Gestor Procesos] Se interrumpio la espera.");
            destruirSiSigueActivo(cuadrado);
            destruirSiSigueActivo(factorial);
            destruirSiSigueActivo(primo);
        }
    }

    private static Process iniciarProceso(
            String java, String classpath, String clase)
            throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                java, "-cp", classpath, clase
        );

        return builder.start();
    }

    private static void enviarNumero(Process proceso, int numero)
        throws IOException {

        try (BufferedWriter escritor = new BufferedWriter(
                new OutputStreamWriter(
                        proceso.getOutputStream(),
                        StandardCharsets.UTF_8))) {
            escritor.write(Integer.toString(numero));
            escritor.newLine();
            escritor.flush();
        }
    }

    private static String leerResultado(Process proceso)
        throws IOException {

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(
                        proceso.getInputStream(),
                        StandardCharsets.UTF_8))) {
            String resultado = lector.readLine();

            if (resultado == null) {
                throw new IOException("El proceso termino sin devolver resultado.");
            }
            return resultado;
        }
    }

    private static int esperarProceso(Process proceso)
        throws InterruptedException {

        boolean terminado = proceso.waitFor(5, TimeUnit.SECONDS);

        if (!terminado) {
            proceso.destroyForcibly();
            return proceso.waitFor();
        }

        return proceso.exitValue();
    }


    private static void comprobarCodigo(String nombre, int codigo) {
        if (codigo != 0) {
            throw new IllegalStateException(
                    "El proceso" + nombre + "termino con codigo " + codigo);
        }
    }

    private static void destruirSiSigueActivo(Process proceso) {
        if (proceso != null && proceso.isAlive()) {
            proceso.destroyForcibly();
        }
    }
}
