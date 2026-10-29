package solucion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class CalculadorCuadrados {

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
                    long numero = Long.parseLong(linea);
                    long cuadrado = numero * numero;

                    escritor.write(
                            "El cuadrado de " + numero
                            + " es " + cuadrado
                    );

                    escritor.newLine();
                    escritor.flush();

                } catch (NumberFormatException e) {

                    System.err.println(
                            "Dato no válido para cuadrados: " + linea
                    );
                }
            }

        } catch (IOException e) {

            System.err.println(
                    "Error en el proceso de cuadrados: "
                    + e.getMessage()
            );
        }
    }
}