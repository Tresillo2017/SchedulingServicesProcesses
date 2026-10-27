import java.io.*;

public class Operaciones {
    public static void main(String[] args) throws  IOException {
        String metodo = args[0];
        int a = Integer.parseInt(args[1]);
        int b = Integer.parseInt(args[2]);

        if (metodo.equals("sumar")) {
            sumar(a,b);
        } else if (metodo.equals("restar")) {
            restar(a,b);
        } else if (metodo.equals("multiplicar")) {
            multiplicar(a,b);
        } else {
            System.out.println("Metodo no encontrado");
        }
    }

    public static void sumar(int a, int b) {
        System.out.println("Suma: " + (a + b));
    }

    public static void restar(int a, int b) {
        System.out.println("Resta: " + (a - b));
    }

    public static void multiplicar(int a, int b) {
        System.out.println("Multiplicacion: " + (a * b));
    }
}
