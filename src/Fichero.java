import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class Fichero {
    public static File validarFichero(String ruta) {
        File file = new File(ruta);

        // Comprobamos si el fichero existe
        if (!file.exists()) {
            System.out.println("Fichero no existe, se creará uno nuevo...");
            try {
                if (!file.createNewFile()) {
                    System.out.println("No se pudo crear el fichero");
                    return null;
                }
            } catch (IOException e) {
                System.out.println("Error al intentar crear el fichero: " + e.getMessage());
                return null;
            }
        } else { // Si ya existe pasamos a comprobar si es válido, si se puede leer y si el tamaño no supera los 10000 bytes
            if (!file.isFile()) {
                System.out.println("Fichero no válido");
                return null;
            } else if (!file.canRead()) {
                System.out.println("Este fichero no se puede leer");
                return null;
            } else if (file.length() > 10000) {
                System.out.println("Fichero demasiado grande");
                return null;
            }
        }
        return file;
    }

    public static void cargarDatosUsuarios(File file, Usuarios usuario) {
        // Leemos el fichero
        try (Scanner leerFichero = new Scanner(file)) {
            // Mientras que haya una siguiente linea en el fichero, vamos leyendo
            while (leerFichero.hasNextLine()) {
                String linea = leerFichero.nextLine().trim();

                // Si no está vacía, separo la linea en 2, por un lado el codigo de usuario y por otro lado la cadena de aficiones
                if (!linea.isEmpty()) {
                    String[] partes = linea.split(" ", 2);
                    // primera parte asigno codigo de usuario
                    String codigo = partes[0];
                    // segunda parte asigno cadena de aficiones, en caso que no tenga aficiones asigno una cadena vacía
                    String aficiones = (partes.length > 1) ? partes[1] : "";
                    usuario.anadirUsuario(codigo, aficiones);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el fichero" + e.getMessage());
        }
    }

    public static boolean guardarNuevoUsuario(File file, String codigo, String aficiones) {
        // Abro el fichero con append en true para poder escribir sin borrar lo anterior
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            fileWriter.write(codigo + " " + aficiones + "\n");
            return true;
        } catch (IOException e) {
            System.out.println("Error al guardar en el fichero: " + e.getMessage());
            return false;
        }
    }

    // Escribo las concordancia que ha encontrado en un nuevo fichero
    public static boolean guardarConcordancias(String nombreFicheroSalida, List<String> lineasConcordancias) {
        File ficheroSalida = new File(nombreFicheroSalida);
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(ficheroSalida))) {
            for (String linea : lineasConcordancias) {
                printWriter.println(linea);
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error al escribir fichero de concordancias: " + e.getMessage());
            return false;
        }
    }
}
