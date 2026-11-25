import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Cliente de consola con menu. El usuario elige una operacion, escribe el comando
 * en el formato del enunciado (se le muestra un ejemplo) y se envia al servidor.
 *
 * Uso: java ClienteTaller [host] [puerto]   (por defecto localhost 5050)
 */
public class ClienteTaller {

	private static final String HOST_POR_DEFECTO = "localhost";
	private static final int PUERTO_POR_DEFECTO = 5050;
	private static final String FIN_RESPUESTA = "<FIN>"; // debe coincidir con el servidor

	/** Opcion de menu: comando esperado y ejemplo de formato. */
	private static final String[][] OPCIONES = {
			{ "1", "Añadir un coche", "ADDCOCHE", "ADDCOCHE,C123,Ford,Focus,2018" },
			{ "2", "Eliminar un coche", "REMOVECOCHE", "REMOVECOCHE,C123" },
			{ "3", "Consultar información de un coche", "GETCOCHE", "GETCOCHE,C123" },
			{ "4", "Listar todos los coches", "LISTCOCHES", "LISTCOCHES" },
			{ "5", "Registrar una reparación", "ADDREPARACION", "ADDREPARACION,C123,Freno roto,150" },
			{ "6", "Salir", "EXIT", "EXIT" } };

	public static void main(String[] args) {
		String host = args.length > 0 ? args[0] : HOST_POR_DEFECTO;
		int puerto = args.length > 1 ? Integer.parseInt(args[1]) : PUERTO_POR_DEFECTO;

		try (Socket socket = new Socket(host, puerto);
				BufferedReader lector = new BufferedReader(
						new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
				BufferedWriter escritor = new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
				Scanner teclado = new Scanner(System.in)) {

			System.out.println("Conectado al taller en " + host + ":" + puerto);

			boolean salir = false;
			while (!salir) {
				String[] opcion = pedirOpcion(teclado);
				if (opcion == null) { // fin de la entrada estandar
					break;
				}
				String comando = pedirComando(teclado, opcion);
				if (comando == null) {
					break;
				}
				escritor.write(comando);
				escritor.newLine();
				escritor.flush();

				if (!mostrarRespuesta(lector)) {
					System.out.println("El servidor ha cerrado la conexión.");
					break;
				}
				salir = opcion[2].equals("EXIT");
			}
		} catch (IOException e) {
			System.err.println("Error en el cliente: " + e.getMessage());
		}
		System.out.println("Cliente finalizado.");
	}

	private static String[] pedirOpcion(Scanner teclado) {
		while (true) {
			System.out.println("\n===== TALLER =====");
			for (String[] o : OPCIONES) {
				System.out.println(o[0] + ". " + o[1]);
			}
			System.out.print("Elige una opción: ");
			if (!teclado.hasNextLine()) {
				return null;
			}
			String eleccion = teclado.nextLine().trim();
			for (String[] o : OPCIONES) {
				if (o[0].equals(eleccion)) {
					return o;
				}
			}
			System.out.println("Opción no válida (1-6).");
		}
	}

	/** Pide el comando y comprueba que empieza por el que corresponde a la opcion. */
	private static String pedirComando(Scanner teclado, String[] opcion) {
		while (true) {
			System.out.println("Formato: " + opcion[3]);
			System.out.print("Introduce: ");
			if (!teclado.hasNextLine()) {
				return null;
			}
			String comando = teclado.nextLine().trim();
			if (comando.toUpperCase().startsWith(opcion[2])) {
				return comando;
			}
			System.out.println("El comando debe empezar por " + opcion[2] + ".");
		}
	}

	/** Lee lineas hasta el marcador de fin. @return false si el servidor cerro. */
	private static boolean mostrarRespuesta(BufferedReader lector) throws IOException {
		String linea;
		while ((linea = lector.readLine()) != null) {
			if (linea.equals(FIN_RESPUESTA)) {
				return true;
			}
			System.out.println(linea);
		}
		return false;
	}
}
