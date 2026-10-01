import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Concordancia {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce el nombre del fichero:");
        String nombreFichero = scanner.nextLine();

        String ruta = nombreFichero;

        File file = new File(ruta);

        // Comprobamos si el fichero existe
        if (!file.exists()) {
            System.out.println("Fichero no existe, se creará uno nuevo...");
            try {
                if (!file.createNewFile()) {
                    System.out.println("No se pudo crear el fichero");
                    return;
                }
            } catch (IOException e) {
                System.out.println("Error al intentar crear el fichero: " + e.getMessage());
                return;
            }
        } else { // Si ya existe pasamos a comprobar si es válido, si se puede leer y si el tamaño no supera los 10000 bytes
            if (!file.isFile()) {
                System.out.println("Fichero no válido");
                return;
            } else if (!file.canRead()) {
                System.out.println("Este fichero no se puede leer");
                return;
            } else if (file.length() > 10000) {
                System.out.println("Fichero demasiado grande");
                return;
            }
        }

        // Declaro 2 ArrayLists para ir separando cod de usuarios de las aficiones de cada uno
        ArrayList<String> codUsuarios = new ArrayList<>();
        ArrayList<String> ListaAficiones = new ArrayList<>();

        // Leemos el fichero
        try (Scanner leerFichero = new Scanner(file)) {
            // Mientras que haya una siguiente linea en el fichero, vamos leyendo
            while (leerFichero.hasNextLine()) {
                String linea = leerFichero.nextLine().trim();

                // Si no está vacía, separo la liena en 2, por un lado el codigo de usuario y por otro lado la cadena de aficiones
                if (!linea.isEmpty()) {
                    String[] partes = linea.split(" ", 2);
                    codUsuarios.add(partes[0]);
                    if (partes.length > 1) {
                        ListaAficiones.add(partes[1]);
                    } else {
                        ListaAficiones.add("");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el fichero" + e.getMessage());
            return;
        }

        int opcion;

        do {
            System.out.println("Menú principal:");
            System.out.println("  1. Añadir usuario");
            System.out.println("  2. Mostrar usuarios introducidos");
            System.out.println("  3. Generar fichero de concordancias");
            System.out.println("  4. Salir");

            System.out.println("Seleccione una opción:");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> {
                    // Sugerimos un código
                    String codSugerido = "U100";


                    // En caso de que no esté vacío el fichero, comprobamos cual es el codigo mas grande y asi calcular el siguiente
                    if (!codUsuarios.isEmpty()) {
                        int numMax = 0;
                        for (String s : codUsuarios) {
                            try {
                                int num = Integer.parseInt(s.replaceAll("[^0-9]", ""));
                                if (num > numMax) numMax = num;
                            } catch (NumberFormatException e) {}
                        }
                        codSugerido = "U" + (numMax + 1);
                    }

                    System.out.println("Codigo de usuario sugerido automáticamente: " + codSugerido);
                    String codUsuario = codSugerido;

                    System.out.println("Introduce las aficiones separadas por espacios:");
                    String aficion = scanner.nextLine().trim().toUpperCase();

                    if (aficion.isEmpty()) {
                        System.out.println("No se puede introducir un usuario sin aficiones.");
                        break;
                    }

                    codUsuarios.add(codUsuario);
                    ListaAficiones.add(aficion);

                    try (FileWriter fileWriter = new FileWriter(file, true)) {
                        fileWriter.write(codUsuario + " " + aficion + "\n");
                        System.out.println("Usuario " + codUsuario + " añadido.");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                case 2 -> {
                    System.out.println("Usuarios registrados:");

                    if (codUsuarios.isEmpty()) {
                        System.out.println("No hay usuarios registrados actualmente.");
                    } else {
                        for (int i = 0; i < codUsuarios.size(); i++) {
                            String codigo = codUsuarios.get(i);
                            String aficiones = ListaAficiones.get(i);

                            System.out.println(codigo + " " + aficiones);
                        }
                    }
                }

                case 3 -> {
                    System.out.println("Fichero de concordancias:");

                    if (codUsuarios.size() < 2) {
                        System.out.println("Tiene que haber al menos 2 usuarios registrados.");
                        break;
                    }

                    int concordanciaMin = 0;
                    do {
                        System.out.println("Introduce el número mínimo de aficiones en común (mínimo 1):");
                        if (scanner.hasNextInt()) {
                            concordanciaMin = scanner.nextInt();
                            scanner.nextLine();

                            if (concordanciaMin < 1) System.out.println("El número debe de ser mayor o igual que 1.");
                        } else {
                            System.out.println("Por favor introduce un número entero válido.");
                            scanner.nextLine();
                        }
                    } while (concordanciaMin < 1);

                    String nombreFichSalida = "concordancias.txt";

                    File ficheroSalida = new File(nombreFichSalida);

                    try (PrintWriter printWriter = new PrintWriter(new FileWriter(ficheroSalida))) {
                        int parejasEncontradas = 0;

                        for (int i = 0; i < codUsuarios.size(); i++) {
                            String user1 = codUsuarios.get(i);
                            String[] aficionesUser1 = ListaAficiones.get(i).split(" ");
                            for (int j = i + 1; j < codUsuarios.size(); j++) {
                                String user2 = codUsuarios.get(j);
                                List<String> aficionesUser2 = Arrays.asList(ListaAficiones.get(j).split(" "));

                                int coincidencias = 0;
                                List<String> aficionesComunes = new ArrayList<>();

                                for (String aficion : aficionesUser1) {
                                    if (!aficion.isEmpty() && aficionesUser2.contains(aficion)) {
                                        coincidencias++;
                                        aficionesComunes.add(aficion);
                                    }
                                }

                                if (coincidencias >= concordanciaMin) {
                                    printWriter.println(user1 + " " + user2 + " " + String.join(" ", aficionesComunes));
                                    parejasEncontradas++;
                                }
                            }
                        }

                        System.out.println("Fichero generado");
                        System.out.println("Se han registrado " + parejasEncontradas + " parejas con al menos " + concordanciaMin + " aficion/es en común.");
                    } catch (IOException e) {
                        System.out.println("Error al escribir el fichero de concordancias" + e.getMessage());
                    }
                }

                case 4 -> System.out.println("Saliendo de la aplicación.");

                default -> throw new IllegalStateException("Unexpected value, introduce un número entre 1 y 4: ");
            }
        } while (opcion != 4);
    }
}