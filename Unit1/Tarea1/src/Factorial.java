import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Proceso externo que lee un entero y devuleve su factorial
 * Admite valores entre 0 y 20 para evitar desbordar un long.
 */

public class Factorial {
    public static void main (String[] args) throws Exception {
        BufferedReader entrada =
                new BufferedReader(new InputStreamReader(System.in));
        int numero = Integer.parseInt(entrada.readLine());

        if (numero < 0 || numero > 20) {
            System.out.println("Error: introduce un numero entre 0 y 20");
            System.exit(1);
        }
        long resultado = 1;

        for (int i = 2;i <= numero; i++) {
            resultado *= i;
        }

        System.out.println(resultado);
    }
}
