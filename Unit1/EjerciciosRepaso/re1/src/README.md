## Ejercicio de repaso: conversor de temperaturas

Crea un programa principal llamado `GestorTemperaturas` que lance **dos procesos Java hijos**:

- `CelsiusAFahrenheit`: recibe grados Celsius y devuelve Fahrenheit.
- `CelsiusAKelvin`: recibe grados Celsius y devuelve Kelvin.

El principal les enviará el mismo número por `stdin`, leerá las respuestas de `stdout`, esperará a que terminen y mostrará los resultados.

Usa como entrada **25 grados Celsius**. Las fórmulas son:

- Fahrenheit = Celsius × 9 / 5 + 32
- Kelvin = Celsius + 273.15

### Parte 1: los procesos hijos

Sin mirar tus otros archivos, empieza por `CelsiusAFahrenheit.java`.

Escribe la clase completa. Debe:

1. Leer una línea desde `System.in`.
2. Convertirla a un número decimal (`double`).
3. Calcular Fahrenheit.
4. Imprimir **solo el resultado** con `System.out.println`.