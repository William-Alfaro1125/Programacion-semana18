package semana18.carrera;

public class ContadorInseguro {
    private int valor = 0;

    public void incrementar() {
        valor++;
    }

    public int getValor() {
        return valor;
    }

    public static void main(String[] args) throws InterruptedException {
        ContadorInseguro contador = new ContadorInseguro();
        Runnable tarea = () -> {
            for (int i = 0; i < 100_000; i++) {
                contador.incrementar();
            }
        };

        Thread t1 = new Thread(tarea, "contador-A");
        Thread t2 = new Thread(tarea, "contador-B");
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Esperado: 200000");
        System.out.println("Obtenido: " + contador.getValor());
        System.out.println("El resultado puede variar porque existe una condicion de carrera.");
    }
}
