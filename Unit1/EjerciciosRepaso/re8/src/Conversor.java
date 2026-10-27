import java.io.*;
public class Conversor {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Faltan argumentos");
            return;
        }

        String metodo = args[0];
        double valor = Double.parseDouble(args[1]);

        if (metodo.equals("kmAMillas")) {
            System.out.println(valor + " km = " + (valor * 0.621371) + " millas");
        } else if (metodo.equals("kgALibras")) {
            System.out.println(valor + " kg = " + (valor * 2.20462) + " libras");
        } else if (metodo.equals("cmAPulgadas")) {
            System.out.println(valor + " cm = " + (valor / 2.54) + " pulgadas");
        } else {
            System.out.println("Metodo desconocido");
        }

        
    }
}
