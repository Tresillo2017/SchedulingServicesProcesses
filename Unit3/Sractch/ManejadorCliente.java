// ManejadorCliente

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Atiende a UN cliente en su propio hilo. Entiende los comandos del taller:
 * ADDCOCHE, REMOVECOCHE, GETCOCHE, LISTCOCHES, ADDREPARACION y EXIT.
 *
 * Protocolo de respuesta: una o varias líneas terminadas SIEMPRE por la línea
 * FIN_RESPUESTA, para que el cliente sepa cuándo acaba (LISTCOCHES devuelve varias).
 */
public class ManejadorCliente implements Runnable {
    public static final String FIN_RESPUESTA = "<FIN>";

    private final Socket socket;

    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }

    // ID -> Coche. static = UN solo mapa compartido por todos los hilos.
    // Todo acceso (leer o escribir) va dentro de synchronized (coches).
    private static final HashMap<String, Coche> coches = new HashMap<>();

    @Override
    public void run() {
        try {
            BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            boolean salir = false;
            String linea;
            while (!salir && (linea = lector.readLine()) != null) {
                System.out.println("Recibido: " + linea);

                String[] partes = linea.split(",");
                String comando = partes[0].trim().toUpperCase();

                switch (comando) {
                    case "ADDCOCHE":
                        addCoche(escritor, partes);
                        break;
                    case "REMOVECOCHE":
                        removeCoche(escritor, partes);
                        break;
                    case "GETCOCHE":
                        getCoche(escritor, partes);
                        break;
                    case "LISTCOCHES":
                        listCoches(escritor, partes);
                        break;
                    case "ADDREPARACION":
                        addReparacion(escritor, partes);
                        break;
                    case "EXIT":
                        responder(escritor, "Goodbye.");
                        salir = true;
                        break;
                    default:
                        responder(escritor, "ERROR, comando desconocido");
                }
            }
            socket.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /** ADDCOCHE,ID,Marca,Modelo,Año */
    private void addCoche(BufferedWriter escritor, String[] partes) throws IOException {
        if (partes.length != 5) {
            responder(escritor, "ERROR, parametros incompletos. Uso: ADDCOCHE,ID,Marca,Modelo,Año");
            return;
        }
        int anio;
        try {
            anio = Integer.parseInt(partes[4].trim());
        } catch (NumberFormatException e) {
            responder(escritor, "ERROR, el año debe ser un numero entero");
            return;
        }
        String id = partes[1].trim();
        synchronized (coches) {
            if (coches.containsKey(id)) {
                responder(escritor, "ERROR, el id del coche ya existe");
                return;
            }
            coches.put(id, new Coche(id, partes[2].trim(), partes[3].trim(), anio));
        }
        responder(escritor, "Coche añadido con éxito.");
    }

    /** REMOVECOCHE,ID */
    private void removeCoche(BufferedWriter escritor, String[] partes) throws IOException {
        if (partes.length != 2) {
            responder(escritor, "ERROR, parametros incompletos. Uso: REMOVECOCHE,ID");
            return;
        }
        Coche eliminado;
        synchronized (coches) {
            eliminado = coches.remove(partes[1].trim());
        }
        if (eliminado == null) {
            responder(escritor, "ERROR, no existe ningun coche con ID " + partes[1].trim());
        } else {
            responder(escritor, "Coche eliminado con éxito.");
        }
    }

    /** GETCOCHE,ID -> datos del coche y sus reparaciones. */
    private void getCoche(BufferedWriter escritor, String[] partes) throws IOException {
        if (partes.length != 2) {
            responder(escritor, "ERROR, parametros incompletos. Uso: GETCOCHE,ID");
            return;
        }
        List<String> respuesta = new ArrayList<>();
        synchronized (coches) {
            Coche coche = coches.get(partes[1].trim());
            if (coche != null) {
                respuesta.add(coche.resumen());
                for (Reparacion r : coche.getReparaciones()) {
                    respuesta.add("  - Reparación: " + r);
                }
            }
        }
        if (respuesta.isEmpty()) {
            responder(escritor, "ERROR, no existe ningun coche con ID " + partes[1].trim());
        } else {
            responder(escritor, respuesta.toArray(new String[0]));
        }
    }

    /** LISTCOCHES -> una línea por coche, ordenados por ID. */
    private void listCoches(BufferedWriter escritor, String[] partes) throws IOException {
        if (partes.length != 1) {
            responder(escritor, "ERROR, formato incorrecto. Uso: LISTCOCHES");
            return;
        }
        List<String> respuesta = new ArrayList<>();
        synchronized (coches) { // recorrer también dentro del cerrojo
            for (Coche coche : new TreeMap<>(coches).values()) {
                respuesta.add(coche.resumen());
            }
        }
        if (respuesta.isEmpty()) {
            responder(escritor, "No hay coches en el taller.");
        } else {
            responder(escritor, respuesta.toArray(new String[0]));
        }
    }

    /** ADDREPARACION,ID,Problema,Costo */
    private void addReparacion(BufferedWriter escritor, String[] partes) throws IOException {
        if (partes.length != 4) {
            responder(escritor, "ERROR, parametros incompletos. Uso: ADDREPARACION,ID,Problema,Costo");
            return;
        }
        double costo;
        try {
            costo = Double.parseDouble(partes[3].trim());
        } catch (NumberFormatException e) {
            responder(escritor, "ERROR, el costo debe ser numerico");
            return;
        }
        if (costo < 0) {
            responder(escritor, "ERROR, el costo no puede ser negativo");
            return;
        }
        String id = partes[1].trim();
        synchronized (coches) {
            Coche coche = coches.get(id);
            if (coche == null) {
                responder(escritor, "ERROR, no existe ningun coche con ID " + id);
                return;
            }
            coche.addReparacion(new Reparacion(id, partes[2].trim(), costo));
        }
        responder(escritor, "Reparación registrada para el coche ID: " + id + ".");
    }

    /** Envía una o varias líneas y después el marcador de fin. */
    public void responder(BufferedWriter escritor, String... lineas) throws IOException {
        for (String linea : lineas) {
            escritor.write(linea);
            escritor.newLine();
        }
        escritor.write(FIN_RESPUESTA);
        escritor.newLine();
        escritor.flush();
    }
}
