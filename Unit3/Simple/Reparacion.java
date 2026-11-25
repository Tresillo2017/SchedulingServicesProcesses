// Reparacion

public class Reparacion {
    private String idCoche;
    private String problema;
    private double costo;

    public Reparacion(String idCoche, String problema, double costo) {
        this.idCoche = idCoche;
        this.problema = problema;
        this.costo = costo;
    }

    // Así se muestra una reparación al imprimirla
    public String toString() {
        return problema + " (" + costo + " EUR)";
    }
}
