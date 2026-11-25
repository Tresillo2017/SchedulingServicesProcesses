import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Atiende a UN cliente en su propio hilo.
 *
 * Protocolo: el cliente envia una linea con el comando. El servidor responde con
 * una o varias lineas y termina SIEMPRE con la linea {@link #FIN_RESPUESTA}, para que
 * el cliente sepa cuando acaba la respuesta (LISTCOCHES/GETCOCHE tienen varias lineas).
 */
public class ManejadorCliente implements Runnable {

	public static final String FIN_RESPUESTA = "<FIN>";

	private final Socket socketCliente;
	private final ProcesadorComandos procesador;

	public ManejadorCliente(Socket socketCliente, ProcesadorComandos procesador) {
		this.socketCliente = socketCliente;
		this.procesador = procesador;
	}

	@Override
	public void run() {
		String hilo = Thread.currentThread().getName();
		try (Socket socket = socketCliente;
				BufferedReader lector = new BufferedReader(
						new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
				BufferedWriter escritor = new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

			String linea;
			while ((linea = lector.readLine()) != null) { // null = el cliente cerro la conexion
				System.out.println("[" + hilo + "] Comando: " + linea);
				enviar(escritor, procesador.procesar(linea));
				if (procesador.esSalida(linea)) {
					break;
				}
			}
		} catch (IOException e) {
			System.err.println("[" + hilo + "] Error con el cliente: " + e.getMessage());
		} finally {
			System.out.println("[" + hilo + "] Cliente desconectado");
		}
	}

	private void enviar(BufferedWriter escritor, List<String> lineas) throws IOException {
		for (String l : lineas) {
			escritor.write(l);
			escritor.newLine();
		}
		escritor.write(FIN_RESPUESTA);
		escritor.newLine();
		escritor.flush(); // sin flush el cliente se queda esperando
	}
}
