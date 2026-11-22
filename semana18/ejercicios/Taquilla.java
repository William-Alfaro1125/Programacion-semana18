package semana18.ejercicios;

public class Taquilla {
    protected int disponibles = 3;

    public boolean comprar(String cliente) {
        if (disponibles > 0) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
            disponibles--;
            System.out.println(cliente + " compro. Quedan " + disponibles);
            return true;
        }
        System.out.println(cliente + ": agotado");
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        Taquilla taquilla = new Taquilla();
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
