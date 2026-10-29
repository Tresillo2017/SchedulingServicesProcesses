package ex1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class CalculadorFactoriales {
	public static void main(String[] args) throws IOException {
		BufferedReader entrada =
				new BufferedReader(new InputStreamReader(System.in));

		// FIX 1: bucle en vez de 5 variables y 5 bucles for copiados.
		String linea;
		while ((linea = entrada.readLine()) != null && !linea.trim().equalsIgnoreCase("FIN")) {
			try {
				int numero = Integer.parseInt(linea.trim());

				// FIX 2: antes comprobabas "factorial > 0" y avisabas de "negativo" (al reves).
				// Lo correcto es comprobar el NUMERO de entrada antes de calcular.
				if (numero < 0) {
					System.err.println("No se puede calcular el factorial de " + numero + " porque es negativo.");
					continue;
				}
				// FIX 3: a partir de 21! el resultado no cabe en un long (desbordamiento).
				if (numero > 20) {
					System.err.println("No se puede calcular el factorial de " + numero + " porque el resultado supera long.");
					continue;
				}

				// FIX 4: antes "factorial" se declaraba una sola vez fuera y se multiplicaba
				// con los 5 numeros (nunca se reiniciaba). Ahora se reinicia a 1 por cada numero.
				long factorial = 1;
				for (int i = 2; i <= numero; i++) {
					factorial *= i;
				}

				// FIX 5: antes NUNCA imprimias el resultado.
				System.out.println("El factorial de " + numero + " es " + factorial);
			} catch (NumberFormatException ex) {
				System.err.println("Dato no valido para factoriales: " + linea);
			}
		}
	}
}
