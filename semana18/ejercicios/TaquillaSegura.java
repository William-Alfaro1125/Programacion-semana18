package semana18.ejercicios;

public class TaquillaSegura {
    private int disponibles = 3;

    public synchronized boolean comprar(String cliente) {
        if (disponibles > 0) {
            disponibles--;
            System.out.println(cliente + " compro. Quedan " + disponibles);
            return true;
        }
        System.out.println(cliente + ": agotado");
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        TaquillaSegura taquilla = new TaquillaSegura();
        Thread[] clientes = new Thread[5];

        for (int i = 0; i < clientes.length; i++) {
            String nombre = "Cliente " + (i + 1);
            clientes[i] = new Thread(() -> taquilla.comprar(nombre));
            clientes[i].start();
        }
        for (Thread cliente : clientes) cliente.join();
        System.out.println("Disponibles al final: " + taquilla.disponibles);
    }
}
