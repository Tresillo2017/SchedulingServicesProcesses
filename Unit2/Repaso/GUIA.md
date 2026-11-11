# Repaso Unidad 2: Multihilo en Java

Como usar esto: lee la teoria (15 min), haz los ejercicios EN ORDEN escribiendo el codigo
tu mismo (sin mirar `soluciones/`), y solo despues compara. En el examen tendras que escribir
codigo desde cero, asi que escribir es lo que cuenta, no leer.

---

## 1. Teoria minima

### Proceso vs hilo
- **Proceso**: programa en ejecucion con su propia memoria (Unidad 1).
- **Hilo (thread)**: "linea de ejecucion" dentro de un proceso. Los hilos de un mismo proceso
  **comparten memoria** (mismos objetos y variables). Eso es lo potente y lo peligroso.

### Crear hilos
```java
class Tarea implements Runnable {
    public void run() { /* lo que hace el hilo */ }
}
Thread t = new Thread(new Tarea());
t.start();   // crea el hilo y ejecuta run() EN PARALELO
t.join();    // el hilo actual espera a que t termine
```
- `start()` = nuevo hilo. `run()` directo = se ejecuta en el hilo actual (sin concurrencia).
- Un hilo solo se puede `start()` una vez.
- `Thread.sleep(ms)` / `TimeUnit.MILLISECONDS.sleep(ms)`: pausa el hilo. Lanza `InterruptedException`.

### Por que hace falta sincronizar (condicion de carrera)
`valor++` son 3 pasos (leer, sumar, escribir). Si dos hilos los mezclan, se pierden incrementos.
Lo mismo pasa con "mirar si queda 1 entrada y luego comprarla": entre mirar y comprar otro hilo
se cuela. Hay que hacer que ese bloque sea **atomico** (un hilo a la vez). Ese bloque se llama
**seccion critica**.

### Herramientas
| Herramienta | Que hace | Cuando |
|---|---|---|
| `synchronized` (metodo o bloque) | Solo un hilo a la vez por objeto (monitor) | Proteger datos compartidos |
| `Semaphore(1)` | Igual que un cerrojo: `acquire()` / `release()` | Exclusion mutua |
| `Semaphore(N)` | Deja pasar hasta N hilos a la vez | Limitar acceso (parking, pool) |
| `join()` | Esperar a que un hilo acabe | Antes de leer sus resultados |
| `BlockingQueue` | Cola segura entre hilos; `put` espera si llena, `take` espera si vacia | Productor-consumidor |

Patron del semaforo (memorizalo):
```java
sem.acquire();
try {
    // seccion critica
} finally {
    sem.release();   // SIEMPRE en finally
}
```

### Master-Worker
El **master** crea y reparte el trabajo, lanza a los **workers**, espera con `join()` y
junta resultados. En la tarea: el main/juez = master, los animales = workers.

### InterruptedException
Patron estandar al capturarla:
```java
catch (InterruptedException e) {
    Thread.currentThread().interrupt();  // restaurar la marca
    return;                              // y salir limpiamente
}
```

---

## 2. Como esta resuelta la tarea (Carrera de Animales)

| Pieza | Que hace | Concepto |
|---|---|---|
| `Animal implements Runnable` | `run()`: 10 tramos con `sleep` aleatorio y luego `juez.llegarMeta()` | Runnable + sleep |
| `JuezCarrera` | Objeto **compartido**. `llegarMeta()` protegido con `Semaphore(1)`; guarda la lista de posiciones | Seccion critica |
| `CarreraMain` | Crea juez y 3 hilos, `start()` a todos, `join()` a todos, imprime posiciones | Master + join |

Razonamiento clave: "el primero que entra en `llegarMeta()` con la lista vacia es el ganador".
Sin proteger, dos animales podrian leer "lista vacia" a la vez y ser ambos ganadores.

---

## 3. Ejercicios (de menos a mas dificil)

### Ej1: Hilos basicos (10 min)
Crea 3 hilos (Ana, Beto, Carla). Cada uno imprime `[Nombre] paso i` 5 veces con una pausa
aleatoria de 100-300 ms, y al final `[Nombre] ha terminado`. El main espera a los 3 y imprime
`Todos han terminado`.
- Prueba despues a cambiar `start()` por `run()`. ¿Que pasa y por que?
- Prueba a quitar los `join()`. ¿Donde sale ahora "Todos han terminado"?

### Ej2: Condicion de carrera (15 min)
4 hilos incrementan 100.000 veces un contador compartido. Imprime el resultado (esperado:
400.000).
1. Hazlo sin sincronizar y ejecutalo varias veces. ¿Sale 400.000?
2. Arreglalo con `synchronized`.
Pregunta: ¿por que `valor++` no es seguro aunque sea una sola linea?

### Ej3: Entradas de concierto (15 min)
Una `Taquilla` tiene 3 entradas. 6 fans (hilos) intentan comprar tras una pausa aleatoria.
`comprar(nombre)` comprueba si quedan y, si si, descuenta. Debe vender EXACTAMENTE 3 y los
otros 3 reciben "sin entrada". Imprime el resultado de cada fan.
Pregunta: ¿que bug aparece si `comprar` no es `synchronized`?

### Ej4: Parking con Semaphore(N) (15 min)
Parking de 2 plazas, 5 coches (hilos). Cada coche: llega, espera plaza, aparca 500-1000 ms,
sale y libera. Nunca debe haber mas de 2 aparcados a la vez.
Pregunta: ¿en que se diferencia de `Semaphore(1)` de la tarea?

### Ej5: Master-Worker (20 min)
Array con los numeros 1..1000. El master reparte el array en 4 trozos; 4 workers (Runnable)
suman cada uno su trozo y guardan el resultado en un campo. El master hace `join()` de todos
y suma los parciales. Resultado esperado: 500500.
Pregunta: ¿por que hay que hacer `join()` ANTES de leer el resultado del worker?

### Ej6 (extension): Productor-Consumidor (20 min)
`ArrayBlockingQueue` de capacidad 3. El productor mete los numeros 1..8 y al final un
valor especial (-1) que significa "fin". El consumidor los saca con `take()` (con una pausa
de 150 ms) hasta recibir -1. Observa como el productor se frena cuando la cola se llena.
Pregunta: ¿para que sirve el valor -1?

### Reto final (30 min, sin mirar nada)
Reescribe de cero la tarea: `Animal`, `JuezCarrera` (con `Semaphore(1)`), `CarreraMain`.
Despues cambia el semaforo por `synchronized` y comprueba que sigue funcionando.

---

## 4. Preguntas de teoria (autoevaluacion)

1. Diferencia entre `start()` y `run()`.
2. Diferencia entre `Thread` y `Runnable`; ¿por que se prefiere `Runnable`?
3. ¿Que es una condicion de carrera? Pon un ejemplo.
4. ¿Que es una seccion critica y como se protege?
5. Diferencia entre `synchronized` y `Semaphore`.
6. ¿Que hace `join()` y que pasa si no se usa?
7. ¿Por que `release()` va en un `finally`?
8. ¿Que es un deadlock? (dos hilos esperando cada uno un recurso que tiene el otro)
9. ¿Que hace `BlockingQueue.put()` si la cola esta llena? ¿Y `take()` si esta vacia?
10. ¿Por que se restaura con `interrupt()` dentro del `catch (InterruptedException)`?

<details>
<summary>Respuestas</summary>

1. `start()` crea un hilo nuevo y ejecuta `run()` en paralelo; `run()` es una llamada normal en el hilo actual.
2. `Runnable` separa la tarea del hilo y deja heredar de otra clase (Java no tiene herencia multiple). `Thread` obliga a extender la clase.
3. Resultado que depende del orden en que se intercalan los hilos. Ej.: dos hilos hacen `contador++` y se pierde un incremento.
4. Codigo que accede a datos compartidos y debe ejecutarse por un solo hilo a la vez. Se protege con `synchronized` o `Semaphore(1)`.
5. `synchronized` es un cerrojo ligado a un objeto (monitor), se libera solo al salir del bloque. `Semaphore` es un contador de permisos: permite N hilos, se libera a mano y no pertenece a ningun hilo concreto.
6. Bloquea al hilo actual hasta que el otro termina. Sin el, el main puede seguir y leer resultados incompletos.
7. Para liberar el permiso aunque haya excepcion; si no, los demas hilos se quedan bloqueados para siempre.
8. Bloqueo mutuo: A tiene el recurso 1 y espera el 2; B tiene el 2 y espera el 1. Nadie avanza.
9. `put()` espera hasta que haya hueco; `take()` espera hasta que haya un elemento.
10. Al capturar la excepcion se borra la marca de interrupcion; restaurarla permite que el codigo que llamo sepa que hubo una peticion de parada.
</details>

---

## 5. Plan sugerido para esta noche (~2 h)
1. Teoria (secciones 1 y 2): 15 min.
2. Ej1 a Ej5: ~75 min (escribelos a mano; consulta `soluciones/` solo si te atascas 10 min).
3. Reto final: 30 min.
4. Preguntas de teoria: 10 min. Ej6 solo si te sobra tiempo.

Para compilar y ejecutar un ejercicio desde la terminal:
```bash
javac Ej1_HilosBasicos.java && java Ej1_HilosBasicos
```
