package semana18.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.SwingUtilities;

public class VentanaConsulta extends JFrame {
    private static final String URL = "http://localhost:8080/socios";
    private final JButton btnConsultar = new JButton("Consultar socios");
    private final JLabel lblEstado = new JLabel("Esperando consulta...");
    private final JTextArea areaDatos = new JTextArea(10, 36);
    private final JProgressBar barra = new JProgressBar();

    public VentanaConsulta() {
        super("Consulta de socios - Cooperativa UPED");
        setLayout(new BorderLayout(10, 10));

        areaDatos.setEditable(false);
        barra.setIndeterminate(false);
        barra.setVisible(false);

        JPanel superior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        superior.add(btnConsultar);
        superior.add(lblEstado);

        JPanel inferior = new JPanel(new BorderLayout(5, 5));
        inferior.add(barra, BorderLayout.CENTER);

        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(areaDatos), BorderLayout.CENTER);
        add(inferior, BorderLayout.SOUTH);

        btnConsultar.addActionListener(e -> consultar());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void consultar() {
        btnConsultar.setEnabled(false);
        lblEstado.setText("Consultando...");
        areaDatos.setText("");
        barra.setVisible(true);
        barra.setIndeterminate(true);

        SwingWorker<String, Void> trabajador = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                HttpClient cliente = HttpClient.newHttpClient();
                HttpRequest peticion = HttpRequest.newBuilder(URI.create(URL)).GET().build();
                HttpResponse<String> respuesta = cliente.send(
                        peticion, HttpResponse.BodyHandlers.ofString()
                );
                if (respuesta.statusCode() != 200) {
                    throw new IllegalStateException("HTTP " + respuesta.statusCode());
                }
                return respuesta.body();
            }

            @Override
            protected void done() {
                try {
                    String json = get();
                    areaDatos.setText(json.replace("},{", "},\n{"));
                    lblEstado.setText("Datos recibidos");
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    lblEstado.setText("Consulta interrumpida");
                } catch (ExecutionException ex) {
                    lblEstado.setText("Error de conexion");
                    JOptionPane.showMessageDialog(
                            VentanaConsulta.this,
                            "No se pudo conectar con el servidor. Verifica que este encendido.",
                            "Error de conexion",
                            JOptionPane.ERROR_MESSAGE
                    );
                } finally {
                    barra.setIndeterminate(false);
                    barra.setVisible(false);
                    btnConsultar.setEnabled(true);
                }
            }
        };

        trabajador.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaConsulta().setVisible(true));
    }
}
