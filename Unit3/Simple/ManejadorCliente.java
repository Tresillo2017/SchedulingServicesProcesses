// ManejadorCliente

import java.io.*;
import java.net.*;
import java.util.*;

public class ManejadorCliente implements Runnable {
    private Socket socket;

    private static HashMap<String, Coche> coches = new HashMap<>();

    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String linea;
            while ((linea = lector.readLine()) != null) {
                System.out.println("Recibido: " + linea);

                String respuesta = procesar(linea);
                responder(escritor, respuesta);

                if (linea.trim().equalsIgnoreCase("EXIT")) {
                    break;
                }
            }
            socket.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static synchronized String procesar(String linea) {
        String[] partes = linea.trim().split(",");
        String comando = partes[0].toUpperCase();

        if (comando.equals("ADDCOCHE")) return addCoche(partes);
        if (comando.equals("REMOVECOCHE")) return removeCoche(partes);
        if (comando.equals("GETCOCHE")) return getCoche(partes);
        if (comando.equals("LISTCOCHES")) return listCoches();
        if (comando.equals("ADDREPARACION")) return addReparacion(partes);
        if (comando.equals("EXIT")) return "Goodbye.";

        return "ERROR, comando desconocido";
    }


    private static String addCoche(String[] partes) {
        if (partes.length != 5) {
            return "ERROR, formato: ADDCOCHE,ID,Marca,Modelo,Año";
        }
        int anio;
        try {
            anio = Integer.parseInt(partes[4]);
        } catch (NumberFormatException e) {
            return "ERROR, el año debe ser un numero";
        }
        if (coches.containsKey(partes[1])) {
            return "ERROR, ya existe un coche con ese ID";
        }
        coches.put(partes[1], new Coche(partes[1], partes[2], partes[3], anio));
        return "Coche añadido con éxito.";
    }


    private static String removeCoche(String[] partes) {
        if (partes.length != 2) {
            return "ERROR, formato: REMOVECOCHE,ID";
        }
        if (coches.remove(partes[1]) == null) {   // remove devuelve null si no existía
            return "ERROR, no existe ningun coche con ese ID";
        }
        return "Coche eliminado con éxito.";
    }


    private static String getCoche(String[] partes) {
        if (partes.length != 2) {
            return "ERROR, formato: GETCOCHE,ID";
        }
        Coche coche = coches.get(partes[1]);      // get devuelve null si no existe
        if (coche == null) {
            return "ERROR, no existe ningun coche con ese ID";
        }
        return coche.detalle();
    }


    private static String listCoches() {
        if (coches.isEmpty()) {
            return "No hay coches en el taller.";
        }
        String texto = "";
        for (Coche coche : coches.values()) {
            texto = texto + coche.resumen() + "\n";
        }
        return texto.trim();
    }


    private static String addReparacion(String[] partes) {
        if (partes.length != 4) {
            return "ERROR, formato: ADDREPARACION,ID,Problema,Costo";
        }
        Coche coche = coches.get(partes[1]);
        if (coche == null) {
            return "ERROR, no existe ningun coche con ese ID";
        }
        double costo;
        try {
            costo = Double.parseDouble(partes[3]);
        } catch (NumberFormatException e) {
            return "ERROR, el costo debe ser un numero";
        }
        coche.addReparacion(new Reparacion(partes[1], partes[2], costo));
        return "Reparación registrada para el coche ID: " + partes[1] + ".";
    }


    private void responder(BufferedWriter escritor, String texto) throws IOException {
        escritor.write(texto);
        escritor.newLine();
        escritor.write("FIN");
        escritor.newLine();
        escritor.flush();
    }
}
