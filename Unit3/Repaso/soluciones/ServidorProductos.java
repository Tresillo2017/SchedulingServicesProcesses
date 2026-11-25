import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Servidor TCP multihilo con un HashMap COMPARTIDO entre todos los clientes.
 * Protocolo (una linea = un comando, una linea = una respuesta):
 *   ADD id nombre...     -> crea (ERROR si ya existe)
 *   GET id               -> devuelve el nombre
 *   UPDATE id nombre...  -> modifica (ERROR si no existe)
 *   DELETE id            -> borra (ERROR si no existe)
 *   LIST                 -> todos en UNA linea (el cliente solo hace un readLine)
 *   SALIR                -> "Sayonara" y se cierra la conexion
 */
public class ServidorProductos {

	private static final int PUERTO = 5000;

	// Estado compartido por TODOS los hilos -> hay que protegerlo.
	private static final Map<Integer, String> PRODUCTOS = new HashMap<>();
	private static final Object CERROJO = new Object();

	public static void main(String[] args) {
		try (ServerSocket servidor = new ServerSocket(PUERTO)) {
			System.out.println("Servidor en puerto " + PUERTO);
			while (true) {
				Socket cliente = servidor.accept(); // bloquea hasta que llega uno
				new Thread(new ManejadorCliente(cliente)).start(); // y vuelve a accept()
			}
		} catch (IOException e) {
			System.err.println("Error en el servidor: " + e.getMessage());
		}
	}

	private static class ManejadorCliente implements Runnable {
		private final Socket socketCliente;

		ManejadorCliente(Socket socketCliente) {
			this.socketCliente = socketCliente;
		}

		@Override
		public void run() {
			String hilo = Thread.currentThread().getName();
			try (Socket socket = socketCliente;
					BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

				String linea;
				while ((linea = lector.readLine()) != null) {
					System.out.println("[" + hilo + "] " + linea);
					if (linea.trim().equalsIgnoreCase("SALIR")) {
						responder(escritor, "Sayonara");
						break;
					}
					responder(escritor, procesar(linea));
				}
			} catch (IOException e) {
				System.err.println("[" + hilo + "] Error con el cliente: " + e.getMessage());
			} finally {
				System.out.println("[" + hilo + "] Cliente desconectado");
			}
		}

		private void responder(BufferedWriter escritor, String texto) throws IOException {
			escritor.write(texto);
			escritor.newLine();
			escritor.flush(); // sin flush el cliente se queda esperando
		}
	}

	/** Interpreta un comando. Nunca lanza excepcion: devuelve "ERROR ..." */
	private static String procesar(String linea) {
		String[] partes = linea.trim().split("\\s+", 3); // comando, id, resto (nombre con espacios)
		String comando = partes[0].toUpperCase();

		try {
			switch (comando) {
			case "LIST":
				return listar();
			case "GET":
				return obtener(Integer.parseInt(partes[1]));
			case "ADD":
				return anadir(Integer.parseInt(partes[1]), partes[2]);
			case "UPDATE":
				return modificar(Integer.parseInt(partes[1]), partes[2]);
			case "DELETE":
				return borrar(Integer.parseInt(partes[1]));
			default:
				return "ERROR comando desconocido: " + comando;
			}
		} catch (NumberFormatException e) {
			return "ERROR el id debe ser un numero";
		} catch (ArrayIndexOutOfBoundsException e) {
			return "ERROR faltan argumentos";
		}
	}

	// Cada operacion es una seccion critica: comprobar + actuar debe ser atomico.

	private static String anadir(int id, String nombre) {
		synchronized (CERROJO) {
			if (PRODUCTOS.containsKey(id)) {
				return "ERROR ya existe el id " + id;
			}
			PRODUCTOS.put(id, nombre);
			return "OK anadido " + id;
		}
	}

	private static String obtener(int id) {
		synchronized (CERROJO) {
			String nombre = PRODUCTOS.get(id);
			return nombre == null ? "ERROR no existe el id " + id : "OK " + id + " = " + nombre;
		}
	}

	private static String modificar(int id, String nombre) {
		synchronized (CERROJO) {
			if (!PRODUCTOS.containsKey(id)) {
				return "ERROR no existe el id " + id;
			}
			PRODUCTOS.replace(id, nombre);
			return "OK modificado " + id;
		}
	}

	private static String borrar(int id) {
		synchronized (CERROJO) {
			return PRODUCTOS.remove(id) == null ? "ERROR no existe el id " + id : "OK borrado " + id;
		}
	}

	private static String listar() {
		synchronized (CERROJO) { // recorrer tambien dentro del cerrojo (si no: ConcurrentModificationException)
			if (PRODUCTOS.isEmpty()) {
				return "OK (vacio)";
			}
			StringBuilder sb = new StringBuilder("OK ");
			for (Map.Entry<Integer, String> e : PRODUCTOS.entrySet()) {
				sb.append(e.getKey()).append("=").append(e.getValue()).append(" | ");
			}
			return sb.toString();
		}
	}
}
