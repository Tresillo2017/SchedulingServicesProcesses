# Gestion de Taller de Coches con Sockets (Unidad 3)

Cliente-servidor TCP en Java (JDK 8+; probado con el JDK instalado en macOS). Sin dependencias externas.

## Ejecucion
```bash
cd Unit3/Taller
mkdir -p out && javac -encoding UTF-8 -d out src/*.java
java -cp out ServidorTaller          # terminal 1 (puerto 5050; otro: java -cp out ServidorTaller 6000)
java -cp out ClienteTaller           # terminal 2, 3... (varios clientes a la vez)
```
Puerto 5050 por defecto porque el 5000 lo ocupa AirPlay Receiver en macOS.

## Estructura
| Clase | Responsabilidad |
|---|---|
| `ServidorTaller` | `ServerSocket` + `accept()` en bucle, un hilo por cliente |
| `ManejadorCliente` | Hilo que atiende a un cliente: lee comando, responde, cierra con `EXIT` |
| `ProcesadorComandos` | Parseo y validacion de comandos (texto -> texto, sin sockets) |
| `Taller` | Datos compartidos (`HashMap<String, Coche>`), metodos `synchronized` |
| `Coche`, `Reparacion` | Modelo de datos inmutable |
| `ClienteTaller` | Menu de consola y envio de comandos |

## Protocolo
Comandos (exactamente los del enunciado): `ADDCOCHE,ID,Marca,Modelo,Año` · `REMOVECOCHE,ID` ·
`GETCOCHE,ID` · `LISTCOCHES` · `ADDREPARACION,ID,Problema,Costo` · `EXIT`.
Cada respuesta son N lineas terminadas por la linea `<FIN>` (el cliente no la muestra).
