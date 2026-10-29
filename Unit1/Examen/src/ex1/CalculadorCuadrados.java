package ex1;

import java.io.*;


public class CalculadorCuadrados {
	public static void main(String[] args) throws IOException {
		BufferedReader entrada =
				new BufferedReader(new InputStreamReader(System.in));

		// FIX 1: antes leias 5 variables (a,b,c,d,e) a mano. Ahora un bucle que lee
		// hasta "FIN" o hasta que se cierre la entrada (readLine() == null).
		String linea;
		while ((linea = entrada.readLine()) != null && !linea.trim().equalsIgnoreCase("FIN")) {
			try {
				long numero = Long.parseLong(linea.trim());
				// FIX 2: antes hacias a * 2 (el doble). El cuadrado es numero * numero.
				System.out.println("El cuadrado de " + numero + " es " + (numero * numero));
			} catch (NumberFormatException ex) {
				// FIX 3: antes un dato no numerico tumbaba el proceso con una excepcion.
				System.err.println("Dato no valido para cuadrados: " + linea);
			}
		}
	}
}
