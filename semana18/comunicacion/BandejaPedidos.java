package semana18.comunicacion;

public class BandejaPedidos {
    private String pedido = null;

    public synchronized void colocar(String nuevo) throws InterruptedException {
        while (pedido != null) {
            wait();
        }
        pedido = nuevo;
        System.out.println("Mesero coloca: " + nuevo);
        notifyAll();
    }

    public synchronized String tomar() throws InterruptedException {
        while (pedido == null) {
            wait();
        }
        String actual = pedido;
        pedido = null;
        System.out.println("Cocina toma: " + actual);
        notifyAll();
        return actual;
    }

    public static void main(String[] args) throws InterruptedException {
        BandejaPedidos bandeja = new BandejaPedidos();
        String[] pedidos = {"Pupusas", "Cafe", "Pan dulce"};

        Thread mesero = new Thread(() -> {
            try {
                for (String p : pedidos) {
                    bandeja.colocar(p);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "mesero");

        Thread cocina = new Thread(() -> {
            try {
                for (int i = 0; i < pedidos.length; i++) {
                    bandeja.tomar();
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "cocina");

        mesero.start();
        cocina.start();
        mesero.join();
        cocina.join();
        System.out.println("Todos los pedidos atendidos.");
    }
}
