import java.io.*;

public class Saludo {
    public static void main(String[] args) throws IOException {
        BufferedReader entrada =
                new BufferedReader(new InputStreamReader(System.in));

        String nombre = entrada.readLine().trim();
        System.out.println("Hola, " + nombre + "!");
    }
}
