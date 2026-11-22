package semana18.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ServidorVisitas {
    private static int visitas = 0;
    private static final AtomicInteger visitasSeguras = new AtomicInteger(0);
    private static final RepositorioSocios repo = new RepositorioSocios();

    static void responder(HttpExchange ex, int codigo, String tipo, String cuerpo)
            throws IOException {
        byte[] datos = cuerpo.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", tipo + "; charset=utf-8");
        ex.sendResponseHeaders(codigo, datos.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(datos);
        }
    }

    private static synchronized void contarVisitaSincronizada() {
        visitas++;
    }

    public static void main(String[] args) throws IOException {
        HttpServer servidor = HttpServer.create(new InetSocketAddress(8080), 0);

        servidor.createContext("/visita", ex -> {
            try {
                int actual = visitas;
                Thread.yield();
                visitas = actual + 1;
                responder(ex, 200, "application/json", "{\"ok\":true}");
            } finally {
                ex.close();
            }
        });

        servidor.createContext("/visita-segura", ex -> {
            try {
                visitasSeguras.incrementAndGet();
                responder(ex, 200, "application/json", "{\"ok\":true}");
            } finally {
                ex.close();
            }
        });

        servidor.createContext("/visita-synchronized", ex -> {
            try {
                contarVisitaSincronizada();
                responder(ex, 200, "application/json", "{\"ok\":true}");
            } finally {
                ex.close();
            }
        });

        servidor.createContext("/conteo", ex -> {
            try {
                String json = "{\"visitas\":" + visitas
                        + ",\"visitas_seguras\":" + visitasSeguras.get() + "}";
                responder(ex, 200, "application/json", json);
            } finally {
                ex.close();
            }
        });

        servidor.createContext("/socios", ex -> {
            try {
                Thread.sleep(1500);
                responder(ex, 200, "application/json", repo.listarJson());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                responder(ex, 503, "application/json", "{\"error\":\"Consulta interrumpida\"}");
            } finally {
                ex.close();
            }
        });

        servidor.createContext("/", ex -> {
            try {
                if (!"GET".equalsIgnoreCase(ex.getRequestMethod())) {
                    responder(ex, 405, "text/plain", "Metodo no permitido");
                    return;
                }
                String html = Files.readString(Path.of("semana18", "web", "index.html"));
                responder(ex, 200, "text/html", html);
            } catch (IOException e) {
                responder(ex, 500, "text/plain",
                        "No se pudo leer semana18/web/index.html. Ejecuta desde la raiz del proyecto.");
            } finally {
                ex.close();
            }
        });

        servidor.setExecutor(Executors.newFixedThreadPool(8));
        servidor.start();
        System.out.println("Servidor iniciado en http://localhost:8080");
        System.out.println("Rutas: /, /socios, /visita, /visita-segura, /visita-synchronized, /conteo");
    }
}
