import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.ArrayList;

/**
 * Almacen de datos del taller, COMPARTIDO por todos los hilos del servidor.
 *
 * Todos los metodos son synchronized sobre la propia instancia: asi
 * "comprobar si existe + actuar" (p. ej. containsKey + put) es atomico y dos
 * clientes simultaneos no pueden anadir el mismo ID ni corromper el mapa.
 */
public class Taller {

	/** Resultado de una operacion: exito o error, mas el mensaje para el cliente. */
	public static final class Resultado {
		private final boolean exito;
		private final String mensaje;

		private Resultado(boolean exito, String mensaje) {
			this.exito = exito;
			this.mensaje = mensaje;
		}

		public static Resultado ok(String mensaje) {
			return new Resultado(true, mensaje);
		}

		public static Resultado error(String mensaje) {
			return new Resultado(false, mensaje);
		}

		public boolean isExito() {
			return exito;
		}

		public String getMensaje() {
			return mensaje;
		}
	}

	// ID -> Coche. Acceso O(1) por ID, que es lo que piden casi todos los comandos.
	private final Map<String, Coche> coches = new HashMap<>();

	public synchronized Resultado anadirCoche(Coche coche) {
		if (coches.containsKey(coche.getId())) {
			return Resultado.error("Error: ya existe un coche con ID " + coche.getId() + ".");
		}
		coches.put(coche.getId(), coche);
		return Resultado.ok("Coche añadido con éxito.");
	}

	public synchronized Resultado eliminarCoche(String id) {
		if (coches.remove(id) == null) {
			return Resultado.error("Error: no existe ningún coche con ID " + id + ".");
		}
		return Resultado.ok("Coche eliminado con éxito.");
	}

	public synchronized Optional<Coche> obtenerCoche(String id) {
		return Optional.ofNullable(coches.get(id));
	}

	/** Copia ordenada por ID (el llamador puede recorrerla sin cerrojo). */
	public synchronized List<Coche> listarCoches() {
		return new ArrayList<>(new TreeMap<>(coches).values());
	}

	public synchronized Resultado anadirReparacion(Reparacion reparacion) {
		Coche coche = coches.get(reparacion.getIdCoche());
		if (coche == null) {
			return Resultado.error("Error: no existe ningún coche con ID " + reparacion.getIdCoche() + ".");
		}
		coches.put(coche.getId(), coche.conReparacion(reparacion));
		return Resultado.ok("Reparación registrada para el coche ID: " + coche.getId() + ".");
	}
}
