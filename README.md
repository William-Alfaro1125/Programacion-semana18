# Programación III — Semana 18
## Ciclo de vida y sincronización de hilos

Implementación de la guía de trabajo práctico Semana 18 (Unidad V, contenidos 5.3 y 5.4), usando Java 21.

## Estructura

```text
Programacion-semana18/
├── README.md
├── .gitignore
└── semana18/
    ├── estados/EstadosHilo.java
    ├── carrera/
    │   ├── ContadorInseguro.java
    │   ├── ContadorSeguro.java
    │   ├── ContadorAtomico.java
    │   ├── CuentaCompartida.java
    │   ├── CuentaSincronizada.java
    │   └── SaldoInsuficienteException.java
    ├── comunicacion/BandejaPedidos.java
    ├── web/
    │   ├── ServidorVisitas.java
    │   ├── Socio.java
    │   ├── RepositorioSocios.java
    │   ├── SocioNoEncontradoException.java
    │   ├── DatoInvalidoException.java
    │   └── index.html
    ├── swing/VentanaConsulta.java
    └── ejercicios/
        ├── Taquilla.java
        └── TaquillaSegura.java
```

## Requisitos y compilación

- JDK 21.
- No requiere librerías externas.
- El servidor HTTP usa el módulo `jdk.httpserver`.

Desde la raíz del repositorio, en Windows PowerShell:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
$files = Get-ChildItem -Recurse semana18 -Filter *.java | ForEach-Object { $_.FullName }
javac --add-modules jdk.httpserver -encoding UTF-8 -d out $files
```

Linux/macOS:

```bash
mkdir -p out
javac --add-modules jdk.httpserver -encoding UTF-8 -d out $(find semana18 -name "*.java")
```

## Ejecutar los ejemplos

```bash
java -cp out semana18.estados.EstadosHilo
java -cp out semana18.carrera.ContadorInseguro
java -cp out semana18.carrera.ContadorSeguro
java -cp out semana18.carrera.ContadorAtomico
java -cp out semana18.carrera.CuentaCompartida
java -cp out semana18.carrera.CuentaSincronizada
java -cp out semana18.comunicacion.BandejaPedidos
java -cp out semana18.ejercicios.Taquilla
java -cp out semana18.ejercicios.TaquillaSegura
```

## Servidor y página web

Ejecuta desde la raíz del repositorio para que el servidor encuentre el HTML:

```bash
java --add-modules jdk.httpserver -cp out semana18.web.ServidorVisitas
```

Abre en el navegador:

| URL | Función |
|---|---|
| http://localhost:8080/ | Página HTML que consulta el servidor con fetch() |
| http://localhost:8080/socios | Lista de socios en JSON (espera 1.5 segundos) |
| http://localhost:8080/visita | Incrementa el contador compartido no protegido |
| http://localhost:8080/visita-segura | Incrementa el contador AtomicInteger |
| http://localhost:8080/conteo | Muestra ambos contadores |

Para comparar los contadores, envía muchas peticiones concurrentes a las dos rutas y luego consulta `/conteo`. La ruta insegura puede perder incrementos; la segura conserva el total. Los resultados cambian entre ejecuciones.

## Ventana Swing

Con el servidor encendido, ejecuta:

```bash
java --add-modules jdk.httpserver -cp out semana18.swing.VentanaConsulta
```

Pulsa **Consultar socios**. La petición HTTP ocurre en `SwingWorker.doInBackground()`; la interfaz se actualiza en `done()`. La barra de progreso indica la consulta. Si el servidor está apagado, aparece un mensaje de error y el botón vuelve a habilitarse.

## Evidencias de entrega

Toma capturas propias de:
1. Estados de los hilos en consola.
2. Diferencia entre el contador inseguro, el sincronizado y AtomicInteger.
3. Productor-consumidor con `wait()` y `notifyAll()`.
4. Servidor: rutas de visita y resultado de `/conteo`.
5. Página web mostrando la tabla de socios y, con el servidor apagado, el mensaje de error.
6. Ventana Swing mientras consulta y después de recibir datos; también el error con el servidor apagado.
7. Taquilla insegura y taquilla corregida.

Las capturas no se incluyen porque deben reflejar ejecuciones reales en tu computadora.
