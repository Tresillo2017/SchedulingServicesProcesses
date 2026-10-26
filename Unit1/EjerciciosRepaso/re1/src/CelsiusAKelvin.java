import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CelsiusAKelvin {
    public static void main(String[] args) throws IOException {
        BufferedReader entrada =
                new BufferedReader(new InputStreamReader(System.in));

        double celsius = Double.parseDouble(entrada.readLine());

        double kelvin = celsius + 273.15;

        System.out.println(kelvin);
    }
}
