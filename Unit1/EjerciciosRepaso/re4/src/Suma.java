import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Suma {
    public static void main(String[] args) throws IOException {
        BufferedReader entrada =
                new BufferedReader(new InputStreamReader(System.in));

        String linea = entrada.readLine();
        String[] partes = linea.trim().split("\\s+");


        int a = Integer.parseInt(partes[0]);
        int b = Integer.parseInt(partes[1]);

        System.out.println(a + b);
    }
}
