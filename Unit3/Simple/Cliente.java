// Cliente

import java.io.*;
import java.net.*;
import java.util.*;

public class Cliente {
    public static void main(String[] args) throws IOException {
        Scanner teclado = new Scanner(System.in);

        Socket socket = new Socket("127.0.0.1", 5005);
        BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        String comando = "";
        while (!comando.equals("EXIT")) {

            System.out.println("\n===== TALLER =====");
            System.out.println("1. Añadir un coche");
            System.out.println("2. Eliminar un coche");
            System.out.println("3. Consultar información de un coche");
            System.out.println("4. Listar todos los coches");
            System.out.println("5. Registrar una reparación");
            System.out.println("6. Salir");
            System.out.print("Elige una opción: ");
            String opcion = teclado.nextLine();

            // Según la opción, se obtiene el comando a enviar
            if (opcion.equals("1")) {
                comando = pedirComando(teclado, "ADDCOCHE,ID,Marca,Modelo,Año   (ej: ADDCOCHE,C123,Ford,Focus,2018)");
            } else if (opcion.equals("2")) {
                comando = pedirComando(teclado, "REMOVECOCHE,ID   (ej: REMOVECOCHE,C123)");
            } else if (opcion.equals("3")) {
                comando = pedirComando(teclado, "GETCOCHE,ID   (ej: GETCOCHE,C123)");
            } else if (opcion.equals("4")) {
                comando = "LISTCOCHES";
            } else if (opcion.equals("5")) {
                comando = pedirComando(teclado, "ADDREPARACION,ID,Problema,Costo   (ej: ADDREPARACION,C123,Freno roto,150)");
            } else if (opcion.equals("6")) {
                comando = "EXIT";
            } else {
                System.out.println("Opción no válida. Elige del 1 al 6.");
                continue;   // vuelve a mostrar el menú sin enviar nada
            }

            // Enviar el comando
            escritor.write(comando);
            escritor.newLine();
            escritor.flush();

            // Leer la respuesta (varias líneas) hasta que llegue "FIN"
            String linea = lector.readLine();
            while (linea != null && !linea.equals("FIN")) {
                System.out.println(linea);
                linea = lector.readLine();
            }
        }

        socket.close();
    }

    private static String pedirComando(Scanner teclado, String formato) {
        System.out.println("Formato: " + formato);
        System.out.print("Introduce: ");
        return teclado.nextLine();
    }
}
