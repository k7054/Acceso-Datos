import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce el nombre del fichero:");
        String nombreFichero = scanner.nextLine();

        String ruta = nombreFichero + ".txt";

        // Validamos el fichero mediante la clase Fichero
        File file = Fichero.validarFichero(ruta);
        // Si el fichero no es válido termina el programa
        if (file == null) {
            return;
        }

        Usuarios usuarios = new Usuarios();
        Fichero.cargarDatosUsuarios(file, usuarios);

        int opcion;

        // Menu
        do {
            System.out.println("Menú principal:");
            System.out.println("  1. Añadir usuario");
            System.out.println("  2. Mostrar usuarios introducidos");
            System.out.println("  3. Generar fichero de concordancias");
            System.out.println("  4. Salir");

            // Valido si la opcion introducida es correcta, en caso de que no vuelve al principio del bucle
            System.out.println("Seleccione una opción:");
            if (scanner.hasNext()) {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Por favor, introduce un número entero válido:");
                scanner.nextLine();
                opcion = 0;
                continue;
            }

            switch (opcion) {

                // Añadir un nuevo usuario
                case 1 -> {
                    // Pedimos una sugerencia de código de usuario
                    String codUsuario = usuarios.sugerirCodUsuario();
                    System.out.println("Código sugerido automáticamente: " + codUsuario);

                    System.out.println("Introduce las aficiones separadas por espacios:");
                    String aficiones = scanner.nextLine().trim().toUpperCase();

                    // Si no se ha introducido ninguna aficion vuelve al menu
                    if (aficiones.isEmpty()) {
                        System.out.println("No se puede introducir usuarios sin aficiones.");
                        break;
                    }

                    // Guardo el usuario introducido en el fichero
                    if (Fichero.guardarNuevoUsuario(file, codUsuario, aficiones)) {
                        usuarios.anadirUsuario(codUsuario, aficiones);
                        System.out.println("Usuario " + codUsuario + " registrado.");
                    }
                }

                // Mostrar usuarios registrados
                case 2 -> {
                    System.out.println("Usuarios registrados:");

                    if (usuarios.getCodUsuarios().isEmpty()) {
                        System.out.println("No hay usuarios registrados actualmente.");
                    } else {
                        for (int i = 0; i < usuarios.getCodUsuarios().size(); i++) {
                            System.out.println(usuarios.getCodUsuarios().get(i) + " " + usuarios.getListaAficiones().get(i));
                        }
                    }
                }

                // Comprobar si hay concordancias y generar el fichero
                case 3 -> {
                    System.out.println("Fichero de concordancias:");

                    // Compruebo que haya al menos 2 usuarios
                    if (usuarios.getCodUsuarios().size() < 2) {
                        System.out.println("Tiene que haber al menos 2 usuarios registrados.");
                        break;
                    }

                    int concordanciaMin = 0;

                    // Compruebo que el número de concordancias introducido sea válido
                    do {
                        System.out.println("Introduce el número mínimo de aficiones en común (mínimo 1):");
                        if (scanner.hasNextInt()) {
                            concordanciaMin = scanner.nextInt();
                            scanner.nextLine();

                            if (concordanciaMin < 1) System.out.println("El número debe de ser mayor o igual que 1.");
                        } else {
                            System.out.println("Introduce un número entero válido.");
                            scanner.nextLine();
                        }
                    } while (concordanciaMin < 1);

                    // Voy guardando en una lista las concordancias
                    List<String> concordancias = usuarios.obtenerConcordancias(concordanciaMin);
                    String nombreFichSalida = "concordancias.txt";

                    // Creo el fichero de concordancias
                    if (Fichero.guardarConcordancias(nombreFichSalida, concordancias)) {
                        System.out.println("Fichero generado");
                        System.out.println("Se han registrado " + concordancias.size() + " parejas con al menos " + concordanciaMin + " aficion/es en común");
                    }
                }

                case 4 -> System.out.println("Saliendo del programa...");

                default -> throw new IllegalStateException("Unexpected value, introduce un número entre 1 y 4: ");
            }
        } while (opcion != 4);
    }
}