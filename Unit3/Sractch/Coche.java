// Coche

import java.util.ArrayList;
import java.util.List;

public class Coche {
    private String id;
    private String marca;
    private String modelo;
    private int anio;
    // Historial de reparaciones de este coche (se modifica siempre dentro de synchronized(coches))
    private final List<Reparacion> reparaciones = new ArrayList<>();

    public Coche(String id, String marca, String modelo, int anio) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
    }

    public String resumen() {
        return (id + " - " + marca + " " + modelo + " (" + anio +")");
    }

    public void addReparacion(Reparacion reparacion) {
        reparaciones.add(reparacion);
    }

    public List<Reparacion> getReparaciones() {
        return reparaciones;
    }

    public String getId() {
        return id;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAnio() {
        return anio;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }
}
