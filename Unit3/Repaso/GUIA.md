# Repaso Unidad 3: Sockets

El PDF lo dice en la ultima diapositiva: la practica final de la 1a evaluacion
**"sera la misma estructura para el examen"**. Esa estructura es la de `Ejemplos/`:

> Servidor TCP que hace `accept()` en bucle, **un hilo por cliente**, protocolo de
> comandos por lineas (`readLine` / `write + newLine + flush`), y (por el
> `EjemploHashMap`) **un HashMap con datos** que los comandos consultan/modifican.

Como usar esto: lee la teoria (10 min), escribe `ServidorProductos` TU MISMO desde cero
(sin mirar `soluciones/`), y compara despues. En el examen se escribe, no se lee.

---

## 1. Teoria (lo que puede caer como pregunta corta)

**Modelos**
- OSI = 7 capas (teorico): Fisica, Enlace, Red, Transporte, Sesion, Presentacion, Aplicacion.
- TCP/IP = 4 capas (practico): Aplicacion, Transporte, Internet, Acceso a red
  (la version de 5 capas separa Acceso a red en Enlace + Fisica).
- Transporte = TCP/UDP. Red = IP. Aplicacion = HTTP, FTP, SMTP, IMAP...

| | TCP (`Socket`/`ServerSocket`) | UDP (`DatagramSocket`/`DatagramPacket`) |
|---|---|---|
| Conexion | Orientado a conexion (handshake 3 pasos) | Sin conexion |
| Fiabilidad | Garantiza entrega y orden, retransmite | No garantiza nada |
| Velocidad | Menor | Mayor |
| Uso | Web, email, FTP | Streaming, juegos, VoIP, DNS |

**Cliente-servidor**: el cliente **inicia** la conexion y pide; el servidor **escucha** en un puerto,
esta siempre disponible y atiende a varios clientes (hilos, procesos o asincrono).

**Socket** = IP + puerto. Puertos < 1024 reservados. Utiles:
20/21 FTP (datos/control) · 22 SSH · 23 Telnet · 25 SMTP · 53 DNS · 80 HTTP · 110 POP3 ·
143 IMAP · 443 HTTPS · 465 SMTPS · 993 IMAPS · 995 POP3S.

**FTP**: control en 21, datos en 20. Modo activo (el servidor abre el canal de datos desde el 20
hacia un puerto del cliente) vs pasivo (el servidor dice un puerto > 1024 y el cliente se conecta).
IMAP vs POP3: IMAP deja los correos en el servidor y los sincroniza; POP3 los descarga.

**Flujo en Java**

| Accion | Cliente | Servidor |
|---|---|---|
| Crear | `new Socket(host, puerto)` | `new ServerSocket(puerto)` |
| Esperar | - | `accept()` (bloquea, devuelve un `Socket`) |
| Comunicar | `getInputStream()` / `getOutputStream()` | igual |
| Cerrar | `close()` | `close()` |

---

## 2. Plantilla que hay que memorizar

### Servidor
```java
try (ServerSocket servidor = new ServerSocket(PUERTO)) {
    while (true) {
        Socket cliente = servidor.accept();                    // bloquea
        new Thread(new ManejadorCliente(cliente)).start();     // y vuelve a accept()
    }
} catch (IOException e) { ... }
```

### Manejador (un hilo por cliente)
```java
private static class ManejadorCliente implements Runnable {
    private final Socket socketCliente;
    ManejadorCliente(Socket s) { this.socketCliente = s; }

    public void run() {
        try (Socket socket = socketCliente;
             BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            String linea;
            while ((linea = lector.readLine()) != null) {     // null = el cliente se fue
                if (linea.equalsIgnoreCase("SALIR")) { responder(escritor, "Sayonara"); break; }
                responder(escritor, procesar(linea));
            }
        } catch (IOException e) { ... }
    }
}
private void responder(BufferedWriter w, String t) throws IOException {
    w.write(t); w.newLine(); w.flush();                        // el flush es obligatorio
}
```

### Cliente
`new Socket(HOST, PUERTO)` + lector + escritor + `Scanner(System.in)`, y en bucle:
`nextLine()` -> `write/newLine/flush` -> `readLine()` -> imprimir. Termina con `SALIR`
o si `readLine()` devuelve `null`. (Es `Ejemplos/ClienteHilos.java`, no cambia.)

---

## 3. Lo que cambia respecto al ejemplo: el HashMap compartido

Todos los hilos usan **el mismo** mapa (`static`), asi que es una **condicion de carrera**
(Unidad 2). Reglas:

1. Cada operacion que mira y luego actua (`containsKey` + `put`) va en `synchronized (CERROJO) { ... }`.
2. **Recorrer** el mapa (`LIST`) tambien dentro del cerrojo, o salta `ConcurrentModificationException`.
3. Alternativa valida: `ConcurrentHashMap` (pero `containsKey` + `put` sigue sin ser atomico: usa `putIfAbsent`).
4. Una peticion = **una** linea de respuesta, porque el cliente solo hace un `readLine()`.
   Por eso `LIST` devuelve todo en una linea separado por ` | `.
5. Validar entrada: id no numerico (`NumberFormatException`), faltan argumentos
   (`ArrayIndexOutOfBounds`), comando desconocido -> responder `ERROR ...`, **nunca** dejar que el hilo muera.
6. `split("\\s+", 3)` para que el nombre pueda llevar espacios (`ADD 1 Teclado Mecanico`).

Operaciones del `HashMap` (de `EjemploHashMap`): `put`, `get`, `containsKey`, `replace`
(solo si existe), `remove`, `entrySet()`, `isEmpty()`.

---

## 4. Ejercicios (en orden)

1. **Eco con hilos**: escribe `ServidorHilos` + `ClienteHilos` de memoria. Abre 2 clientes a la vez.
2. **Productos** (`soluciones/ServidorProductos.java`): comandos `ADD id nombre`, `GET id`,
   `UPDATE id nombre`, `DELETE id`, `LIST`, `SALIR`. Prueba con 2 clientes: lo que anade uno lo ve el otro.
3. **Variante alumnos**: `HashMap<String, Integer>` (nombre -> nota). Comandos `NOTA nombre n`,
   `VER nombre`, `MEDIA`, `BORRAR nombre`, `LISTA`. (`MEDIA` recorre el mapa: dentro del cerrojo.)
4. **Contador global**: `INC`, `DEC`, `VER` sobre un `int` compartido (o `AtomicInteger`). Comprueba
   que con 5 clientes pulsando `INC` no se pierden incrementos.
5. **Extra**: que el servidor imprima el nombre del hilo en cada comando (como en el ejemplo) y
   un comando `CLIENTES` que diga cuantos hay conectados (contador compartido, `AtomicInteger`).

## 5. Como probarlo

```bash
cd Unit3/Repaso/soluciones
javac ServidorProductos.java ../../Ejemplos/ClienteHilos.java -d /tmp/out
java -cp /tmp/out ServidorProductos        # terminal 1
java -cp /tmp/out ClienteHilos             # terminal 2 y 3
```
OJO en Mac: el puerto **5000 lo ocupa AirPlay Receiver** (`Address already in use`). Usa otro
puerto (p. ej. 5055) en servidor y cliente, o desactiva AirPlay Receiver en Ajustes. En el aula
(Windows) el 5000 va bien.

## 6. Errores tipicos (y como se notan)

| Sintoma | Causa |
|---|---|
| El cliente se queda colgado tras enviar | Falta `flush()` o `newLine()` en el servidor |
| Solo atiende a un cliente a la vez | `run()` directo en vez de `new Thread(...).start()` |
| `Address already in use` | Puerto ocupado u otra instancia del servidor viva |
| El servidor muere con un comando raro | No se captura `NumberFormatException` / falta validar |
| `ConcurrentModificationException` | Recorrer el mapa sin cerrojo mientras otro hilo modifica |
| Datos "perdidos" entre clientes | Mapa declarado dentro del manejador (no `static`/compartido) |
