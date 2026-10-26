import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CelsiusAFahrenheit {
    public static void main(String[] args) throws IOException {
        BufferedReader entrada =
                new BufferedReader(new InputStreamReader(System.in));

        double celsius = Double.parseDouble(entrada.readLine());

        double fahrenheit = celsius * 9 / 5 + 32;

        System.out.println(fahrenheit);
    }
}
