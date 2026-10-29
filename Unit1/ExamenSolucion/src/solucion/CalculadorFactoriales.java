package solucion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class CalculadorFactoriales {

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

                    if (numero < 0) {

                        System.err.println(
                                "No se puede calcular el factorial de "
                                + numero
                                + " porque es negativo."
                        );

                        continue;
                    }

                    if (numero > 20) {

                        System.err.println(
                                "No se puede calcular el factorial de "
                                + numero
                                + " porque el resultado supera long."
                        );

                        continue;
                    }

                    long factorial = 1;

                    for (int i = 2; i <= numero; i++) {
                        factorial *= i;
                    }

                    escritor.write(
                            "El factorial de " + numero
                            + " es " + factorial
                    );

                    escritor.newLine();
                    escritor.flush();

                } catch (NumberFormatException e) {

                    System.err.println(
                            "Dato no válido para factoriales: " + linea
                    );
                }
            }

        } catch (IOException e) {

            System.err.println(
                    "Error en el proceso de factoriales: "
                    + e.getMessage()
            );
        }
    }
}