import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Traduce una linea de comando recibida por el socket en una operacion sobre el
 * {@link Taller} y devuelve la respuesta como lista de lineas.
 *
 * No conoce sockets: solo texto de entrada -> texto de salida (facil de probar).
 * Nunca lanza excepciones por entradas incorrectas: responde con "Error: ...".
 */
public class ProcesadorComandos {

	public static final String CMD_EXIT = "EXIT";
	public static final String MSG_ADIOS = "Goodbye.";

	private final Taller taller;

	public ProcesadorComandos(Taller taller) {
		this.taller = taller;
	}

	/** @return true si el comando es EXIT (el manejador cierra la conexion). */
	public boolean esSalida(String linea) {
		return linea.trim().equalsIgnoreCase(CMD_EXIT);
	}

	public List<String> procesar(String linea) {
		String[] partes = linea.trim().split(",", -1);
		String comando = partes[0].trim().toUpperCase();

		switch (comando) {
		case "ADDCOCHE":
			return una(addCoche(partes));
		case "REMOVECOCHE":
			return una(removeCoche(partes));
		case "GETCOCHE":
			return getCoche(partes);
		case "LISTCOCHES":
			return listCoches(partes);
		case "ADDREPARACION":
			return una(addReparacion(partes));
		case CMD_EXIT:
			return una(MSG_ADIOS);
		default:
			return una("Error: comando desconocido '" + partes[0] + "'. Comandos: ADDCOCHE, REMOVECOCHE, "
					+ "GETCOCHE, LISTCOCHES, ADDREPARACION, EXIT.");
		}
	}

	private String addCoche(String[] p) {
		if (p.length != 5) {
			return "Error: formato incorrecto. Uso: ADDCOCHE,ID,Marca,Modelo,Año";
		}
		String id = p[1].trim();
		String marca = p[2].trim();
		String modelo = p[3].trim();
		if (id.isEmpty() || marca.isEmpty() || modelo.isEmpty()) {
			return "Error: ID, marca y modelo no pueden estar vacíos.";
		}
		if (!id.matches("[A-Za-z0-9]+")) {
			return "Error: el ID debe ser alfanumérico.";
		}
		int anio;
		try {
			anio = Integer.parseInt(p[4].trim());
		} catch (NumberFormatException e) {
			return "Error: el año debe ser un número entero.";
		}
		return taller.anadirCoche(new Coche(id, marca, modelo, anio)).getMensaje();
	}

	private String removeCoche(String[] p) {
		if (p.length != 2 || p[1].trim().isEmpty()) {
			return "Error: formato incorrecto. Uso: REMOVECOCHE,ID";
		}
		return taller.eliminarCoche(p[1].trim()).getMensaje();
	}

	private List<String> getCoche(String[] p) {
		if (p.length != 2 || p[1].trim().isEmpty()) {
			return una("Error: formato incorrecto. Uso: GETCOCHE,ID");
		}
		Optional<Coche> coche = taller.obtenerCoche(p[1].trim());
		if (!coche.isPresent()) {
			return una("Error: no existe ningún coche con ID " + p[1].trim() + ".");
		}
		List<String> salida = new ArrayList<>();
		salida.add(coche.get().resumen());
		if (coche.get().getReparaciones().isEmpty()) {
			salida.add("  Sin reparaciones registradas.");
		}
		for (Reparacion r : coche.get().getReparaciones()) {
			salida.add("  - Reparación: " + r);
		}
		return salida;
	}

	private List<String> listCoches(String[] p) {
		if (p.length != 1) {
			return una("Error: formato incorrecto. Uso: LISTCOCHES");
		}
		List<Coche> todos = taller.listarCoches();
		if (todos.isEmpty()) {
			return una("No hay coches en el taller.");
		}
		List<String> salida = new ArrayList<>();
		for (Coche c : todos) {
			salida.add(c.resumen());
		}
		return salida;
	}

	/** ADDREPARACION,ID,Problema,Costo (el problema puede contener comas). */
	private String addReparacion(String[] p) {
		if (p.length < 4) {
			return "Error: formato incorrecto. Uso: ADDREPARACION,ID,Problema,Costo";
		}
		String id = p[1].trim();
		String costoTexto = p[p.length - 1].trim();
		String problema = String.join(",", java.util.Arrays.copyOfRange(p, 2, p.length - 1)).trim();
		if (id.isEmpty() || problema.isEmpty()) {
			return "Error: el ID y la descripción del problema no pueden estar vacíos.";
		}
		double costo;
		try {
			costo = Double.parseDouble(costoTexto);
		} catch (NumberFormatException e) {
			return "Error: el costo debe ser numérico (recibido '" + costoTexto + "').";
		}
		if (Double.isNaN(costo) || Double.isInfinite(costo) || costo < 0) {
			return "Error: el costo debe ser un número válido mayor o igual que 0.";
		}
		return taller.anadirReparacion(new Reparacion(id, problema, costo)).getMensaje();
	}

	private static List<String> una(String mensaje) {
		List<String> lista = new ArrayList<>();
		lista.add(mensaje);
		return lista;
	}
}
