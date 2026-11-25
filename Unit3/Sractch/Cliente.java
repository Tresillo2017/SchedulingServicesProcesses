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


        String texto;
        do {
            texto = teclado.nextLine();

            escritor.write(texto);
            escritor.newLine();
            escritor.flush();

            // La respuesta puede tener varias líneas: se lee hasta el marcador <FIN>
            String respuesta;
            while ((respuesta = lector.readLine()) != null && !respuesta.equals("<FIN>")) {
                System.out.println("El servidor dice: " + respuesta);
            }
        } while (!texto.equalsIgnoreCase("EXIT"));

        socket.close();


    }
}
