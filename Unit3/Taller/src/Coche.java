import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Coche que esta en el taller, con su historial de reparaciones.
 * Los datos del coche son inmutables; el historial solo crece mediante
 * {@link #conReparacion(Reparacion)}, que devuelve un coche NUEVO.
 */
public final class Coche {

	private final String id;
	private final String marca;
	private final String modelo;
	private final int anio;
	private final List<Reparacion> reparaciones;

	public Coche(String id, String marca, String modelo, int anio) {
		this(id, marca, modelo, anio, new ArrayList<>());
	}

	private Coche(String id, String marca, String modelo, int anio, List<Reparacion> reparaciones) {
		this.id = id;
		this.marca = marca;
		this.modelo = modelo;
		this.anio = anio;
		this.reparaciones = Collections.unmodifiableList(reparaciones);
	}

	public String getId() {
		return id;
	}

	public List<Reparacion> getReparaciones() {
		return reparaciones;
	}

	/** Devuelve una copia de este coche con la reparacion anadida al historial. */
	public Coche conReparacion(Reparacion reparacion) {
		List<Reparacion> nuevas = new ArrayList<>(reparaciones);
		nuevas.add(reparacion);
		return new Coche(id, marca, modelo, anio, nuevas);
	}

	/** Formato de una linea usado por LISTCOCHES: "C123 - Ford Focus (2018)". */
	public String resumen() {
		return id + " - " + marca + " " + modelo + " (" + anio + ")";
	}
}
