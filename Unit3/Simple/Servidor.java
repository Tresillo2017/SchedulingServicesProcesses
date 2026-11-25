// Servidor

import java.io.*;
import java.net.*;

public class Servidor {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(5005);
        System.out.println("Servidor esperando clientes...");

        while (true) {
            Socket socket = server.accept();
            System.out.println("Cliente conectado");

            Thread hilo = new Thread(new ManejadorCliente(socket));
            hilo.start();
        }
    }
}
