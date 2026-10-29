package ex1;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class GestorAnalisisNumerico {
	public static void main(String[] args) throws IOException, InterruptedException, java.net.URISyntaxException {
		Scanner sc = new Scanner(System.in);

		// FIX 1: array + bucle en vez de a,b,c,d,e. Asi todo lo de abajo tambien es un bucle.
		String[] numeros = new String[5];
		for (int i = 0; i < numeros.length; i++) {
			System.out.print("Numero " + (i + 1) + ": ");
			numeros[i] = sc.nextLine();
		}

		// Asignamos rutaJava pra poder crear el proceso
		String rutaJava = System.getProperty("java.home")
				+ File.separator + "bin"
				+ File.separator + "java";

		// Asignamos el java class path
		// FIX 8: con module-info.java el IDE ejecuta como modulo y "java.class.path" sale VACIO,
		// asi que los hijos no encontraban ex1.*. Se usa la carpeta donde esta compilada esta clase.
		String classpath = new File(GestorAnalisisNumerico.class
				.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();

		// FIX 2: antes el nombre de clase era "CalculadorCuadrados" a secas. Las clases estan
		// en el paquete ex1, asi que hay que usar el nombre completo "ex1.CalculadorCuadrados"
		// o el hijo falla con ClassNotFoundException.
		String[] nombres = {"CUADRADOS", "FACTORIALES", "PRIMOS"};
		String[] clases = {"ex1.CalculadorCuadrados", "ex1.CalculadorFactoriales", "ex1.ComprobadorPrimos"};

		System.out.println("[GESTOR] Iniciando Procesos...");
		Process[] procesos = new Process[clases.length];
		for (int i = 0; i < clases.length; i++) {
			ProcessBuilder pb = new ProcessBuilder(rutaJava, "-cp", classpath, clases[i]);
			// Unimos la salida normal y los errores
			pb.redirectErrorStream(true);
			procesos[i] = pb.start();
		}

		// Enviamos los mismos 5 numeros a los 3 procesos
		for (Process proceso : procesos) {
			// FIX 3: try-with-resources CIERRA el writer al terminar, lo que hace flush y manda
			// fin de entrada al hijo. Antes nunca hacias flush() ni close(), asi que los datos
			// se quedaban en el buffer y el hijo se quedaba bloqueado en readLine().
			try (BufferedWriter escritor = new BufferedWriter(
					new OutputStreamWriter(proceso.getOutputStream()))) {
				for (String numero : numeros) {
					// FIX 4: antes para el 2o y 3er proceso escribias en entrada2/entrada3 pero
					// llamabas a entrada.newLine() (el writer del 1er proceso). Los saltos de linea
					// iban al proceso equivocado. Con un unico "escritor" esto no puede fallar.
					escritor.write(numero);
					escritor.newLine();
				}
				escritor.write("FIN");
				escritor.newLine();
			}
		}

		for (int i = 0; i < procesos.length; i++) {
			Process proceso = procesos[i];
			System.out.println("\n========== " + nombres[i] + " ==========");

			// FIX 5: antes hacias waitFor(10) y luego waitFor(11) (hasta 21 s por proceso), y el
			// if estaba INVERTIDO: waitFor devuelve true si el proceso YA TERMINO, asi que
			// destruias los que acabaron bien. Se espera una vez y se destruye solo si devuelve false.
			boolean terminado = proceso.waitFor(10, TimeUnit.SECONDS);
			if (!terminado) {            // OJO: el "!" es lo que faltaba
				proceso.destroy();
				if (!proceso.waitFor(2, TimeUnit.SECONDS)) {
					proceso.destroyForcibly();
					proceso.waitFor();
				}
			}

			// FIX 6: antes leias UNA sola linea (readLine()) por proceso, pero cada uno imprime 5.
			// Hay que leer en bucle hasta null. Ademas se lee despues de esperar/destruir, que
			// funciona con salidas pequenas (con salidas enormes habria que leer ANTES de esperar).
			try (BufferedReader lectura = new BufferedReader(
					new InputStreamReader(proceso.getInputStream()))) {
				String linea;
				while ((linea = lectura.readLine()) != null) {
					System.out.println(linea);
				}
			}

			// FIX 7: antes no mostrabas el codigo de salida. Sirve para saber si acabo bien (0).
			System.out.println("Codigo de salida de " + nombres[i] + ": " + proceso.exitValue());
		}

		sc.close();
	}

}
