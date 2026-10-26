import java.io.*;

public class ContarPalabras {
    public static void main(String[] args) throws IOException {
        BufferedReader entrada =
                new BufferedReader(new InputStreamReader(System.in));

        String frase = entrada.readLine().trim();
        String[] palabras = frase.split("\\s+");
        System.out.println(palabras.length);

        if (frase.isEmpty()) {
            System.err.println("Frase vacia");
            System.exit(1);
        }
    }
}
