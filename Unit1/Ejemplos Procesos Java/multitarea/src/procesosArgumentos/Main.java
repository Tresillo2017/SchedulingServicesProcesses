package procesosArgumentos;


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws Exception {

        // Nombre del método que queremos ejecutar
        String metodo = "imprimirMensaje";

        // Argumento que queremos pasarle al método
        String argumento = "Hola desde los argumentos";

        /*
         * Creamos el proceso.
         *
         * procesos.Mensaje -> clase que ejecutaremos
         * metodo           -> primer argumento: método a ejecutar
         * argumento        -> segundo argumento: parámetro del método
         */
        ProcessBuilder pb = new ProcessBuilder(
                "java",
                "-cp",
                "bin",
                "procesosArgumentos.Mensaje",
                metodo,
                argumento
        );

        // Iniciamos el proceso
        Process proceso = pb.start();

        // Leemos lo que imprime el segundo proceso
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(proceso.getInputStream())
        );

        String linea;

        while ((linea = reader.readLine()) != null) {
            System.out.println(linea);
        }

        // Esperamos a que termine
        proceso.waitFor();
    }
}
