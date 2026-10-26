import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Proceso externo que comprueba si un entero es primo.
 */

public class Primo {
    public static void main (String[] args) throws Exception {
        BufferedReader entrada =
            new BufferedReader(new InputStreamReader(System.in));

        int numero = Integer.parseInt(entrada.readLine());
        boolean esPrimo = numero >= 2;

        for (int divisor = 2;
             esPrimo && divisor <= numero / divisor;
             divisor++) {
            if (numero % divisor == 0) {
                esPrimo = false;
            }
        }

        System.out.println(esPrimo ? "es primo" : "no es primo");
    }
}
