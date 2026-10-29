package solucion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class ComprobadorPrimos {

    public static void main(String[] args) {

        try (
            BufferedReader lector = new BufferedReader(
                    new InputStreamReader(
                            System.in,
                            StandardCharsets.UTF_8
                    )
            );

            BufferedWriter escritor = new BufferedWriter(
                    new OutputStreamWriter(
                            System.out,
                            StandardCharsets.UTF_8
                    )
            )
        ) {

            String linea;

            while ((linea = lector.readLine()) != null) {

                linea = linea.trim();

                if (linea.equalsIgnoreCase("FIN")) {
                    break;
                }

                try {
                    int numero = Integer.parseInt(linea);
                    boolean esPrimo = true;

                    if (numero < 2) {

                        esPrimo = false;

                    } else {

                        for (int divisor = 2;
                             divisor <= numero / divisor;
                             divisor++) {

                            if (numero % divisor == 0) {
                                esPrimo = false;
                                break;
                            }
                        }
                    }

                    if (esPrimo) {

                        escritor.write(numero + " es primo");

                    } else {

                        escritor.write(numero + " no es primo");
                    }

                    escritor.newLine();
                    escritor.flush();

                } catch (NumberFormatException e) {

                    System.err.println(
                            "Dato no válido para primos: " + linea
                    );
                }
            }

        } catch (IOException e) {

            System.err.println(
                    "Error en el proceso de primos: "
                    + e.getMessage()
            );
        }
    }
}