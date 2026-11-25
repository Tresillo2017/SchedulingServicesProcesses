// Servidor

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(5005);

        while (true) {
            Socket socket = server.accept();
            System.out.println("Cliente conectado");
            Thread manejadorCliente = new Thread(new ManejadorCliente(socket));
            manejadorCliente.start();
        }
    }
}