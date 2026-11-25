/**
 * Reparacion realizada a un coche. Clase inmutable: una vez creada no cambia.
 */
public final class Reparacion {

	private final String idCoche;
	private final String problema;
	private final double costo;

	public Reparacion(String idCoche, String problema, double costo) {
		this.idCoche = idCoche;
		this.problema = problema;
		this.costo = costo;
	}

	public String getIdCoche() {
		return idCoche;
	}

	public String getProblema() {
		return problema;
	}

	public double getCosto() {
		return costo;
	}

	@Override
	public String toString() {
		return problema + " (" + costo + " EUR)";
	}
}
