package solucion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GestorAnalisisNumerico {

    public static void main(String[] args) {

        final int CANTIDAD_NUMEROS = 5;
        final int TIEMPO_MAXIMO = 10;

        int[] numeros = new int[CANTIDAD_NUMEROS];

        try (Scanner teclado = new Scanner(System.in)) {

            // Solicitar los cinco números
            System.out.println("Introduce cinco números enteros:");

            for (int i = 0; i < numeros.length; i++) {
                System.out.print("Número " + (i + 1) + ": ");
                numeros[i] = teclado.nextInt();
            }

           
            // Crear los tres ProcessBuilder
            ProcessBuilder pbCuadrados = new ProcessBuilder(
                    "java",
                    "-cp",
                    "bin",
                    "solucion.CalculadorCuadrados"
            );

            ProcessBuilder pbFactoriales = new ProcessBuilder(
            		 "java",
                     "-cp",
                     "bin",
                    "solucion.CalculadorFactoriales"
            );

            ProcessBuilder pbPrimos = new ProcessBuilder(
            		 "java",
                     "-cp",
                     "bin",
                    "solucion.ComprobadorPrimos"
            );

            // Unir stderr con stdout
            pbCuadrados.redirectErrorStream(true);
            pbFactoriales.redirectErrorStream(true);
            pbPrimos.redirectErrorStream(true);

            // Iniciar los tres procesos antes de gestionarlos
            Process[] procesos = {
                    pbCuadrados.start(),
                    pbFactoriales.start(),
                    pbPrimos.start()
            };

            String[] nombres = {
                    "CUADRADOS",
                    "FACTORIALES",
                    "PRIMOS"
            };

            // Enviar los mismos cinco números a los tres procesos
            for (Process proceso : procesos) {

                try (BufferedWriter escritor = new BufferedWriter(
                        new OutputStreamWriter(
                                proceso.getOutputStream(),
                                StandardCharsets.UTF_8
                        ))) {

                    for (int numero : numeros) {
                        escritor.write(String.valueOf(numero));
                        escritor.newLine();
                    }

                    // Indicar el final de los datos
                    escritor.write("FIN");
                    escritor.newLine();
                    escritor.flush();
                }
            }

            int[] codigosSalida = new int[procesos.length];
            boolean[] finalizadosEnPlazo = new boolean[procesos.length];
            boolean[] finalizadosForzosamente =
                    new boolean[procesos.length];

            // Gestionar los procesos secuencialmente
            for (int i = 0; i < procesos.length; i++) {

                Process proceso = procesos[i];

                System.out.println();
                System.out.println(
                        "========== " + nombres[i] + " =========="
                );

                boolean terminado = proceso.waitFor(
                        TIEMPO_MAXIMO,
                        TimeUnit.SECONDS
                );

                finalizadosEnPlazo[i] = terminado;

                if (!terminado) {

                    System.err.println(
                            "El proceso " + nombres[i]
                            + " ha superado los "
                            + TIEMPO_MAXIMO + " segundos."
                    );

                    proceso.destroy();

                    // Dar dos segundos para que termine normalmente
                    if (!proceso.waitFor(2, TimeUnit.SECONDS)) {

                        proceso.destroyForcibly();
                        finalizadosForzosamente[i] = true;

                        // Esperar a que termine la destrucción forzosa
                        proceso.waitFor();
                    }
                }

                // Leer toda la salida del proceso
                try (BufferedReader lector = new BufferedReader(
                        new InputStreamReader(
                                proceso.getInputStream(),
                                StandardCharsets.UTF_8
                        ))) {

                    String linea;

                    while ((linea = lector.readLine()) != null) {
                        System.out.println(linea);
                    }
                }

                codigosSalida[i] = proceso.exitValue();

                System.out.println(
                        "Código de salida de "
                        + nombres[i]
                        + ": "
                        + codigosSalida[i]
                );
            }

            // Mostrar el resumen final
            System.out.println();
            System.out.println("========== RESUMEN FINAL ==========");

            for (int i = 0; i < procesos.length; i++) {

                System.out.println();
                System.out.println("Proceso: " + nombres[i]);
                System.out.println(
                        "Código de salida: " + codigosSalida[i]
                );

                if (finalizadosForzosamente[i]) {

                    System.out.println(
                            "Estado: finalizado forzosamente"
                    );

                } else if (!finalizadosEnPlazo[i]) {

                    System.out.println(
                            "Estado: superó el tiempo máximo"
                    );

                } else if (codigosSalida[i] == 0) {

                    System.out.println(
                            "Estado: finalizado correctamente"
                    );

                } else {

                    System.out.println(
                            "Estado: finalizado con errores"
                    );
                }
            }

        } catch (InputMismatchException e) {

            System.err.println(
                    "Error: debes introducir únicamente números enteros."
            );

        } catch (IOException e) {

            System.err.println(
                    "Error al iniciar o comunicar con los procesos: "
                    + e.getMessage()
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.err.println(
                    "La ejecución ha sido interrumpida."
            );
        }
    }
}