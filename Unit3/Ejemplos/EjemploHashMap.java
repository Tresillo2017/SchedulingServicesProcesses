
import java.util.HashMap;
import java.util.Map;

public class EjemploHashMap {
    @SuppressWarnings("unchecked")
	public static void main(String[] args) {
        // Crear el HashMap (Clave: ID del producto, Valor: Nombre)
        HashMap<Integer, String> productos = new HashMap<>();

        // 1. INSERTAR elementos
        productos.put(101, "Teclado Mecánico");
        productos.put(102, "Ratón Inalámbrico");
        productos.put(103, "Monitor 24 pulgadas");
        productos.put(103, "Monitor 24 pulgadas");

        // Obtenemos el elemento en la posición 0 , esto es para sacar un unico elemento
        Map.Entry<Integer, String> elemento0 = (Map.Entry<Integer, String>) productos.entrySet().toArray()[0];

        // Sysout de la clave y del valor
        System.out.println("Clave: " + elemento0.getKey() + " | Valor: " + elemento0.getValue());
    
        
        
        // 2. LISTAR elementos (recorriendo clave y valor)
        System.out.println("--- Lista inicial ---");
        for (Map.Entry<Integer, String> entrada : productos.entrySet()) {
            System.out.println("ID: " + entrada.getKey() + " | Producto: " + entrada.getValue());
        }

        // 3. MODIFICAR un elemento
        // Opción A: replace() solo actualiza si la clave ya existe
        productos.replace(102, "Ratón Gaming RGB");
        // Opción B: put() sobrescribe el valor si la clave existe (o lo crea si no existe)
        productos.put(101, "Teclado Mecánico Inalámbrico");

        // 4. BORRAR elementos
        productos.remove(103); // Elimina el elemento con clave 103

        // Listar resultado final
        System.out.println("\n--- Lista tras modificar y borrar ---");
        productos.forEach((id, nombre) -> System.out.println("ID: " + id + " | Producto: " + nombre));
    }
}
