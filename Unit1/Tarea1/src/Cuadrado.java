import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
* Proceso externo que lee un entero y devuelve su cuadrado
*/

public class Cuadrado {
    public static void main(String[] args) throws IOException {
        BufferedReader entrada =
                new BufferedReader(new InputStreamReader(System.in));

                int numero = Integer.parseInt(entrada.readLine());
                long resultado = (long) numero * numero;

                System.out.println(resultado);
    }
}