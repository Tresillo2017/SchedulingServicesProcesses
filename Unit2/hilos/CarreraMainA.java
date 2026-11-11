package examA.hilos;  
//   Esta línea indica en qué "paquete" o carpeta lógica se encuentra el archivo.
//   Es como decir en qué carpeta está guardado dentro del proyecto (organización del código).

import java.util.ArrayList;  
import java.util.Comparator;  
import java.util.List;  
//   Estas tres líneas sirven para poder usar herramientas (clases) ya preparadas por Java:
//   - ArrayList: una lista donde podemos guardar varios objetos.
//   - Comparator: una forma de comparar objetos (en este caso, por su tiempo).
//   - List: la forma general (interfaz) de una lista de elementos.

public class CarreraMainA {  
//    Esta línea define una clase llamada "CarreraMainA".
//    Es el programa principal: el que da la orden de empezar la carrera y muestra los resultados.

    public static void main(String[] args) throws InterruptedException {  
    //    Este es el "punto de inicio" del programa.
    //    Es lo primero que se ejecuta cuando arrancamos el programa.
    //    Puede lanzar una "interrupción" (InterruptedException), algo normal al usar hilos.

        Corredor tortuga = new Corredor("Tortuga");  
        Corredor liebre  = new Corredor("Liebre");  
        //    Aquí se crean dos "corredores", dándoles un nombre a cada uno.
        //    Son objetos que representan a los participantes de la carrera.
        //    Cada uno sabe cómo correr (su forma de avanzar está programada en otra clase llamada Corredor).

        Thread t1 = new Thread(tortuga);  
        Thread t2 = new Thread(liebre);  
        //   Aquí se crean dos "hilos de ejecución" (threads).
        //    Un hilo es como una mini-corriente de trabajo dentro del programa:
        //    cada hilo ejecutará a un corredor por separado y al mismo tiempo.
        //    - t1 será el hilo que se encarga de la Tortuga.
        //    - t2 será el hilo que se encarga de la Liebre.

        t1.start();  
        t2.start();  
        //   Estas líneas dan la señal de salida de la carrera.
        //    Cuando se llama a "start()", cada hilo comienza su trabajo en paralelo.
        //    En ese momento, Tortuga y Liebre comienzan a "correr" simultáneamente.

        t1.join();  
        t2.join();  
        //   Estas líneas hacen que el programa principal espere a que ambos corredores terminen.
        //    "join()" significa literalmente "unirse": el programa se detiene aquí hasta que cada hilo termina su carrera.
        //    Así evitamos mostrar los resultados antes de que los dos corredores acaben.

        // Orden por tiempo (menor primero)
        List<Corredor> lista = new ArrayList<>();  
        //   Se crea una lista vacía donde guardaremos los corredores para luego ordenarlos por tiempo.
        lista.add(tortuga);  
        lista.add(liebre);  
        //   Se añaden los dos corredores a la lista: la Tortuga y la Liebre.

        lista.sort(Comparator.comparingLong(Corredor::getTiempoMs));  
        //   Se ordena la lista de corredores según su tiempo total.
        //    La función "getTiempoMs()" devuelve cuánto tardó cada uno en correr.
        //    El que tenga menos tiempo (el más rápido) quedará primero en la lista.

        System.out.printf("Tiempos (ms): Tortuga=%d, Liebre=%d%n", tortuga.getTiempoMs(), liebre.getTiempoMs());
        //   Aquí se muestra en pantalla el tiempo total que tardó cada corredor en terminar la carrera.
        //    "%d" significa que se van a mostrar números enteros, y "%n" es un salto de línea.
        //    Por ejemplo, podría mostrarse:
        //    Tiempos (ms): Tortuga=1030, Liebre=955

        System.out.println("Orden de llegada:");
        //   Mensaje de encabezado antes de mostrar el podio final.

        for (int i = 0; i < lista.size(); i++) {
        //   Este bucle recorre la lista de corredores ya ordenada (primero el más rápido).
        //    "lista.size()" indica cuántos corredores hay (en este caso, 2).

            System.out.printf("%d) %s (%d ms)%n",
                    i + 1,                         //  Número de posición (1º, 2º…)
                    lista.get(i).getNombre(),      //  Nombre del corredor.
                    lista.get(i).getTiempoMs());   //  Tiempo total que tardó.
            // 💬 Muestra cada línea del podio, por ejemplo:
            // 1) Liebre (955 ms)
            // 2) Tortuga (1030 ms)
        }
    }
}
