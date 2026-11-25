
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClienteHilos {

	private static final String HOST = "localhost";
	private static final int PUERTO = 5000;

	public static void main(String[] args) {

		System.out.println("Conectando con el servidor...");

		try (Socket socket = new Socket(HOST, PUERTO);

				BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

				Scanner teclado = new Scanner(System.in)) {

			System.out.println("Conexión establecida con " + HOST + ":" + PUERTO);

			String comando;
			String respuesta;

			do {

				System.out.print("Introduce un comando: ");

				comando = teclado.nextLine();

				// Enviar el comando al servidor
				escritor.write(comando);
				escritor.newLine();
				escritor.flush();

				// Esperar la respuesta del servidor
				respuesta = lector.readLine();

				if (respuesta == null) {

					System.out.println("El servidor ha cerrado la conexión.");

					break;
				}

				System.out.println("Respuesta del servidor: " + respuesta);

			} while (!comando.equalsIgnoreCase("SALIR"));

		} catch (IOException e) {

			System.err.println("Error en el cliente: " + e.getMessage());
		}

		System.out.println("Cliente finalizado.");
	}
}