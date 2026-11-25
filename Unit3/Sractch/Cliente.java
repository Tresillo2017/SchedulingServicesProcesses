// Cliente

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Cliente de consola con menú. El usuario elige una opción (1-6), se le muestra el
 * formato del comando, lo escribe y se envía al servidor tal cual (formato del enunciado).
 */
public class Cliente {
    private static final String HOST = "127.0.0.1";
    private static final int PUERTO = 5005;
    private static final String FIN_RESPUESTA = "<FIN>"; // debe coincidir con el del servidor

    // Cada opción del menú: {número, texto del menú, comando esperado, ejemplo de formato}
    private static final String[][] OPCIONES = {
            {"1", "Añadir un coche", "ADDCOCHE", "ADDCOCHE,C123,Ford,Focus,2018"},
            {"2", "Eliminar un coche", "REMOVECOCHE", "REMOVECOCHE,C123"},
            {"3", "Consultar información de un coche", "GETCOCHE", "GETCOCHE,C123"},
            {"4", "Listar todos los coches", "LISTCOCHES", "LISTCOCHES"},
            {"5", "Registrar una reparación", "ADDREPARACION", "ADDREPARACION,C123,Freno roto,150"},
            {"6", "Salir", "EXIT", "EXIT"}
    };

    public static void main(String[] args) throws IOException {
        Scanner teclado = new Scanner(System.in);

        Socket socket = new Socket(HOST, PUERTO);

        BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        boolean salir = false;
        while (!salir) {
            String[] opcion = pedirOpcion(teclado);
            if (opcion == null) { // se acabó la entrada del teclado
                break;
            }
            String comando = pedirComando(teclado, opcion);
            if (comando == null) {
                break;
            }

            enviar(escritor, comando);

            if (!mostrarRespuesta(lector)) {
                System.out.println("El servidor ha cerrado la conexión.");
                break;
            }
            salir = opcion[2].equals("EXIT");
        }

        socket.close();
    }

    /** Muestra el menú hasta que el usuario elige una opción válida. null si no hay más entrada. */
    private static String[] pedirOpcion(Scanner teclado) {
        while (true) {
            System.out.println("\n===== TALLER =====");
            for (String[] o : OPCIONES) {
                System.out.println(o[0] + ". " + o[1]);
            }
            System.out.print("Elige una opción: ");

            if (!teclado.hasNextLine()) {
                return null;
            }
            String eleccion = teclado.nextLine().trim();
            for (String[] o : OPCIONES) {
                if (o[0].equals(eleccion)) {
                    return o;
                }
            }
            System.out.println("Opción no válida. Elige un número del 1 al 6.");
        }
    }

    /**
     * Pide el comando de la opción elegida. LISTCOCHES y EXIT no llevan datos,
     * así que se envían directamente. Comprueba que lo escrito empieza por el comando
     * correcto, para que "2" no envíe un ADDCOCHE por error.
     */
    private static String pedirComando(Scanner teclado, String[] opcion) {
        String comandoEsperado = opcion[2];
        if (comandoEsperado.equals("LISTCOCHES") || comandoEsperado.equals("EXIT")) {
            return comandoEsperado;
        }
        while (true) {
            System.out.println("Formato: " + opcion[3]);
            System.out.print("Introduce: ");
            if (!teclado.hasNextLine()) {
                return null;
            }
            String comando = teclado.nextLine().trim();
            if (comando.toUpperCase().startsWith(comandoEsperado + ",")) {
                return comando;
            }
            System.out.println("El comando debe empezar por " + comandoEsperado + ",");
        }
    }

    private static void enviar(BufferedWriter escritor, String comando) throws IOException {
        escritor.write(comando);
        escritor.newLine();
        escritor.flush();
    }

    /** Lee las líneas de la respuesta hasta el marcador <FIN>. false si el servidor cerró. */
    private static boolean mostrarRespuesta(BufferedReader lector) throws IOException {
        String linea;
        while ((linea = lector.readLine()) != null) {
            if (linea.equals(FIN_RESPUESTA)) {
                return true;
            }
            System.out.println(linea);
        }
        return false;
    }
}
