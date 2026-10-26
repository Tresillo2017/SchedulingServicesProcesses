import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Triple {
    public static void main(String[] args) throws IOException {
        BufferedReader entrada
                = new BufferedReader(new InputStreamReader(System.in));

        int numero = Integer.parseInt(entrada.readLine());

        int resultado = numero * 3;

        System.out.println(resultado);
    }
}
