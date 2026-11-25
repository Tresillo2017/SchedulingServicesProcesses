import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor TCP del taller. Acepta clientes en bucle y lanza un hilo
 * ({@link ManejadorCliente}) por cada uno, de modo que varios clientes pueden
 * trabajar a la vez contra el MISMO {@link Taller}.
 *
 * Uso: java ServidorTaller [puerto]   (por defecto 5050)
 */
public class ServidorTaller {

	public static final int PUERTO_POR_DEFECTO = 5050;

	public static void main(String[] args) {
		int puerto = args.length > 0 ? Integer.parseInt(args[0]) : PUERTO_POR_DEFECTO;

		Taller taller = new Taller(); // unico objeto de datos, compartido
		ProcesadorComandos procesador = new ProcesadorComandos(taller);

		try (ServerSocket servidor = new ServerSocket(puerto)) {
			System.out.println("Servidor del taller escuchando en el puerto " + puerto);
			while (true) {
				Socket cliente = servidor.accept(); // bloquea hasta que llega un cliente
				System.out.println("Cliente conectado: " + cliente.getInetAddress() + ":" + cliente.getPort());
				new Thread(new ManejadorCliente(cliente, procesador)).start();
			}
		} catch (IOException e) {
			System.err.println("Error en el servidor: " + e.getMessage());
		}
	}
}
