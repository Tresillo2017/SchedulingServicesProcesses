
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorHilos {

	private static final int PUERTO = 5000;

	public static void main(String[] args) {

		System.out.println("Iniciando servidor...");

		try (ServerSocket servidor = new ServerSocket(PUERTO)) {

			System.out.println("Servidor esperando conexiones en el puerto " + PUERTO);

			/*
			 * El servidor no termina después de aceptar al primer cliente. Permanece
			 * continuamente esperando nuevas conexiones.
			 */
			while (true) {

				/*
				 * accept() bloquea el servidor hasta que se conecta un nuevo cliente.
				 */
				Socket socketCliente = servidor.accept();

				System.out.println("Nuevo cliente conectado desde: " + socketCliente.getInetAddress() + ":"
						+ socketCliente.getPort());

				/*
				 * Se crea un hilo independiente para atender al cliente. El servidor puede
				 * volver inmediatamente a accept() para esperar a otro cliente.
				 */
				ManejadorCliente manejador = new ManejadorCliente(socketCliente);

				Thread hiloCliente = new Thread(manejador);

				hiloCliente.start();
			}

		} catch (IOException e) {

			System.err.println("Error en el servidor: " + e.getMessage());
		}
	}

	/**
	 * Esta clase contiene la comunicación con un cliente. Cada objeto
	 * ManejadorCliente se ejecutará en un hilo diferente.
	 */
	private static class ManejadorCliente implements Runnable {

		private final Socket socketCliente;

		public ManejadorCliente(Socket socketCliente) {
			this.socketCliente = socketCliente;
		}

		@Override
		public void run() {

			/*
			 * El nombre del hilo nos permite distinguir en la consola qué hilo está
			 * atendiendo cada comando.
			 */
			String nombreHilo = Thread.currentThread().getName();

			try (Socket socket = socketCliente;

					BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));

					BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

				System.out.println("[" + nombreHilo + "] " + "Atendiendo al cliente " + socket.getInetAddress() + ":"
						+ socket.getPort());

				String comando;

				/*
				 * Cada hilo recibe los comandos enviados únicamente por el cliente que tiene
				 * asignado.
				 */
				while ((comando = lector.readLine()) != null) {

					System.out.println(
							"[" + nombreHilo + "] " + "Comando recibido de " + socket.getPort() + ": " + comando);

					if (comando.equalsIgnoreCase("SALIR")) {

						enviarRespuesta(escritor, "Sayonara");

						break;
					}

					String respuesta = "Comando recibido correctamente: " + comando;

					enviarRespuesta(escritor, respuesta);
				}

			} catch (IOException e) {

				System.err.println("[" + nombreHilo + "] " + "Error con el cliente: " + e.getMessage());

			} finally {

				System.out.println("[" + nombreHilo + "] " + "Cliente desconectado");
			}
		}

		/**
		 * Envía una respuesta al cliente.
		 */
		private void enviarRespuesta(BufferedWriter escritor, String respuesta) throws IOException {

			escritor.write(respuesta);
			escritor.newLine();
			escritor.flush();
		}
	}
}