import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Vigilante {
    static void lanzar(String segundos) throws IOException, InterruptedException {
        String rutaJava = System.getProperty("java.home")
                + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");

        List<String> comando = new ArrayList<>();
        comando.add(rutaJava);
        comando.add("-cp");
        comando.add(classpath);
        comando.add("Tarea");
        if (segundos != null) {
            comando.add(segundos);
        }

        ProcessBuilder pb = new ProcessBuilder(comando);
        Process proceso = pb.start();

        boolean terminoATiempo = proceso.waitFor(4, TimeUnit.SECONDS);

        if (terminoATiempo) {
            BufferedReader lector = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            BufferedReader lectorErr = new BufferedReader(new InputStreamReader(proceso.getErrorStream()));
            String salida = lector.readLine() + " " + lector.readLine();
            String error = lectorErr.readLine();
            System.out.println("Tarea " + segundos + " -> exit " + proceso.exitValue()
                    + ", salida: " + salida + ", error: " + error);
        } else
            proceso.destroy();
        if (!proceso.waitFor(1, TimeUnit.SECONDS)) {
            proceso.destroyForcibly();
        }
        System.out.println("Tarea " + segundos + " -> cancelada por timeout");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        lanzar("1");
        lanzar("3");
        lanzar("10");
        lanzar("null");
    }
}
