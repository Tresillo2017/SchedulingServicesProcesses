# Documentacion tecnica (borrador para el PDF)

> Pasa esto a Word/PDF. Cada seccion pide captura(s) con explicacion: capturas sin explicacion NO cuentan.

## 1. Introduccion y objetivo
Sistema cliente-servidor sobre sockets TCP para gestionar coches y reparaciones de un taller.
El servidor concentra los datos; el cliente es solo interfaz de usuario.

## 2. Arquitectura
Cliente (`ClienteTaller`) <--TCP, texto por lineas--> Servidor (`ServidorTaller`) --> un hilo `ManejadorCliente` por cliente --> `ProcesadorComandos` --> `Taller` (datos compartidos).
Por que TCP: se necesita entrega fiable y ordenada de comandos (ver diferencias TCP/UDP de la Unidad 3).
Por que un hilo por cliente: `accept()` y `readLine()` bloquean; sin hilos el servidor atenderia a un solo cliente a la vez.

## 3. Flujo de datos (un comando)
1. El usuario elige opcion y escribe `ADDCOCHE,C123,Ford,Focus,2018`.
2. El cliente lo escribe en el `BufferedWriter` (`write` + `newLine` + `flush`).
3. El hilo del servidor lo lee con `readLine()`.
4. `ProcesadorComandos` separa por comas, valida y llama a `Taller`.
5. `Taller` actua dentro de un metodo `synchronized` y devuelve el resultado.
6. El hilo escribe la(s) linea(s) de respuesta + `<FIN>` y hace `flush`.
7. El cliente lee hasta `<FIN>` y lo muestra.

## 4. Decisiones tecnicas (justificar en el PDF)
- **HashMap<String, Coche>**: busqueda por ID en O(1); el ID es la clave natural y unica.
- **Historial en `ArrayList<Reparacion>`** dentro de cada coche: orden de insercion, sin claves.
- **Concurrencia**: los datos son compartidos -> condicion de carrera. `containsKey` + `put` debe ser atomico, por eso los metodos de `Taller` son `synchronized`. `listarCoches()` devuelve una copia para recorrerla sin cerrojo.
- **Marcador `<FIN>`**: `LISTCOCHES`/`GETCOCHE` devuelven varias lineas; el marcador evita que el cliente se quede bloqueado en `readLine()` o lea de menos.
- **Inmutabilidad**: `Coche` y `Reparacion` no se modifican; anadir reparacion crea un `Coche` nuevo. Evita efectos colaterales entre hilos.
- **Validaciones**: ID duplicado, ID inexistente, costo numerico (y >= 0), año entero, numero de argumentos, comando desconocido. El servidor siempre responde, nunca cae.
- **`ADDREPARACION`**: el costo es el ultimo campo, asi la descripcion puede contener comas.
- **Cierre**: `EXIT` -> `Goodbye.` y try-with-resources cierra socket y streams; `readLine()==null` cubre clientes que se caen.

## 5. Descripcion de clases y metodos
(Una tabla por clase: ver comentarios Javadoc del codigo; copia aqui cada metodo con "que hace / como / por que".)

## 6. Pruebas (hacer capturas con explicacion)
| Prueba | Entrada | Respuesta esperada |
|---|---|---|
| Añadir | `ADDCOCHE,C123,Ford,Focus,2018` | `Coche añadido con éxito.` |
| ID duplicado | mismo comando otra vez | `Error: ya existe un coche con ID C123.` |
| Eliminar inexistente | `REMOVECOCHE,C999` | `Error: no existe ningún coche con ID C999.` |
| Costo no numerico | `ADDREPARACION,C123,Freno,mucho` | `Error: el costo debe ser numérico ...` |
| Reparacion | `ADDREPARACION,C123,Freno roto,150` | `Reparación registrada para el coche ID: C123.` |
| Consultar | `GETCOCHE,C123` | coche + reparaciones |
| Listar | `LISTCOCHES` | `C123 - Ford Focus (2018)` ... |
| Salir | `EXIT` | `Goodbye.` |
| Concurrencia | 2-3 clientes a la vez | lo que añade uno lo ve el otro |

## 7. Problemas encontrados y soluciones
- Respuestas de varias lineas -> marcador `<FIN>`.
- Puerto 5000 ocupado en macOS (AirPlay) -> puerto 5050 configurable.
- Acceso concurrente al mapa -> `synchronized`.
