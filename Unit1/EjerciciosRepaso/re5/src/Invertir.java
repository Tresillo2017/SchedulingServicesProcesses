import java.io.*;

public class Invertir {
    public static void main(String[] args) throws IOException {
        BufferedReader entrada =
                new BufferedReader(new InputStreamReader(System.in));

        String frase = entrada.readLine();
        System.out.println(new StringBuilder(frase).reverse().toString());
    }
}
