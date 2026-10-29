package ex1;

import java.io.*;


public class ComprobadorPrimos {
	public static void main(String[] args) throws IOException {
		BufferedReader entrada =
				new BufferedReader(new InputStreamReader(System.in));

		// FIX 1: bucle en vez de repetir el mismo bloque 5 veces con a,b,c,d,e.
		String linea;
		while ((linea = entrada.readLine()) != null && !linea.trim().equalsIgnoreCase("FIN")) {
			try {
				int numero = Integer.parseInt(linea.trim());

				// FIX 2: antes "esPrimo" se declaraba una vez y nunca volvia a true tras el
				// primer false. Ahora se reinicia a true para cada numero.
				boolean esPrimo = true;
				if (numero < 2) {
					esPrimo = false;
				} else {
					// FIX 3: antes el for ponia esPrimo = false y hacia break SIN comprobar nada,
					// asi que todo numero > 2 salia "no primo". Hay que mirar si es divisible.
					// (divisor <= numero / divisor equivale a divisor <= raiz(numero) sin desbordar)
					for (int divisor = 2; divisor <= numero / divisor; divisor++) {
						if (numero % divisor == 0) {
							esPrimo = false;
							break;
						}
					}
				}

				// FIX 4: antes imprimias siempre "a" (el primer numero) en las 5 salidas,
				// y sin espacios: "7estrue". Ahora se imprime el numero correcto y texto legible.
				System.out.println(numero + (esPrimo ? " es primo" : " no es primo"));
			} catch (NumberFormatException ex) {
				System.err.println("Dato no valido para primos: " + linea);
			}
		}
	}
}
