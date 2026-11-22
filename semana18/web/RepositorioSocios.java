package semana18.web;

import java.util.List;

public class RepositorioSocios {
    private final List<Socio> socios = List.of(
            new Socio(1, "Ana", 175),
            new Socio(2, "Luis", 250),
            new Socio(3, "Marta", 80)
    );

    public String listarJson() {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < socios.size(); i++) {
            if (i > 0) json.append(",");
            json.append(socios.get(i).toJson());
        }
        return json.append("]").toString();
    }

    public int cantidadSocios() { return socios.size(); }
    public int saldoTotal() {
        return socios.stream().mapToInt(Socio::getSaldo).sum();
    }
}
