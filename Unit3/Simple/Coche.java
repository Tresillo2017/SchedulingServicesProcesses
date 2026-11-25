// Coche

import java.util.ArrayList;


public class Coche {
    private String id;
    private String marca;
    private String modelo;
    private int anio;
    private ArrayList<Reparacion> reparaciones = new ArrayList<>();

    public Coche(String id, String marca, String modelo, int anio) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
    }


    public String resumen() {
        return id + " - " + marca + " " + modelo + " (" + anio + ")";
    }

    public void addReparacion(Reparacion reparacion) {
        reparaciones.add(reparacion);
    }

    public String detalle() {
        String texto = resumen();
        for (Reparacion r : reparaciones) {
            texto = texto + "\n  - Reparación: " + r;
        }
        return texto;
    }
}
