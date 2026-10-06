package procesosArgumentos;

public class Mensaje {

    public static void main(String[] args) {

        /*
         * args[0] contiene el nombre del método.
         *
         * En nuestro ejemplo:
         * args[0] = "imprimirMensaje"
         */
        String metodo = args[0];

        /*
         * args[1] contiene el argumento que
         * queremos pasar al método.
         *
         * En nuestro ejemplo:
         * args[1] = "Hola desde Main"
         */
        String argumento = args[1];

        /*
         * Comprobamos qué método nos han pedido ejecutar.
         */
        if (metodo.equals("imprimirMensaje")) {

            // Ejecutamos el método y le pasamos el argumento
            imprimirMensaje(argumento);
        }
    }

    public static void imprimirMensaje(String mensaje) {

        System.out.println(mensaje);
    }
}
