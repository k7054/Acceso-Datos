import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Usuarios {
    // Declaro 2 ArrayLists para ir separando cod de usuarios de las aficiones de cada uno
    private ArrayList<String> codUsuarios;
    private ArrayList<String> listaAficiones;

    public Usuarios() {
        // Inizializo los dos arrayLists en el constructor
        this.codUsuarios = new ArrayList<>();
        this.listaAficiones = new ArrayList<>();
    }

    public ArrayList<String> getCodUsuarios() {
        return codUsuarios;
    }

    public ArrayList<String> getListaAficiones() {
        return listaAficiones;
    }

    public boolean existeUsuario(String codigo) {
        return codUsuarios.contains(codigo);
    }

    public void anadirUsuario(String codigo, String aficiones) {
        codUsuarios.add(codigo);
        listaAficiones.add(aficiones);
    }

    public String sugerirCodUsuario() {
        // Sugerimos un código si el fichero está vacío
        if (codUsuarios.isEmpty()) {
            return "U100";
        }

        // En caso de que no esté vacío el fichero, comprobamos cual es el codigo mas grande y asi calcular el siguiente
        int numMax = 0;
        for (String s : codUsuarios) {
            try {
                int num = Integer.parseInt(s.replaceAll("[^0-9]", ""));
                if (num > numMax) numMax = num;
            } catch (NumberFormatException e) {}
        }

        return "U" + (numMax + 1);
    }

    public List<String> obtenerConcordancias(int concordanciaMin) {
        List<String> concordancias = new ArrayList<>();

        for (int i = 0; i < codUsuarios.size(); i++) {
            String user1 = codUsuarios.get(i);
            String[] aficionesUser1 = listaAficiones.get(i).split(" ");

            for (int j = i + 1; j < codUsuarios.size(); j++) {
                String user2 = codUsuarios.get(j);
                List<String> aficionesUser2 = Arrays.asList(listaAficiones.get(j).split(" "));

                int coincidencias = 0;
                List<String> aficionesComunes = new ArrayList<>();

                for (String aficion : aficionesUser1) {
                    if (!aficion.isEmpty() && aficionesUser2.contains(aficion)) {
                        coincidencias++;
                        aficionesComunes.add(aficion);
                    }
                }

                if (coincidencias >= concordanciaMin) {
                    concordancias.add(user1 + " " + user2 + " " + String.join(" ", aficionesComunes));
                }
            }
        }

        return concordancias;
    }
}
