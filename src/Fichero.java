import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Fichero {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce el nombre del fichero:");
        String nombreFichero = scanner.nextLine();

        String ruta = nombreFichero + ".txt";

        File file = new File(ruta);

        if (file.exists()) {
            System.out.println("Fichero ya existe, usando este fichero...");
        } else {
            System.out.println("Fichero no existe, se creará uno nuevo...");
        }

        try (FileWriter fileWriter = new FileWriter(ruta, true)) {
            int opcion;

            do {

                System.out.println("Menú principal:");
                System.out.println("  1. Añadir usuario");
                System.out.println("  2. Mostrar usuarios introducidos");
                System.out.println("  3. Generar fichero de concordancias");
                System.out.println("  4. Salir");

                System.out.println("Seleccione una opción:");
                opcion = scanner.nextInt();

                switch (opcion) {
                    case 1 -> {
                        System.out.println("Introduce el código de usuario:");
                        int codUsuario = scanner.nextInt();
                        fileWriter.write(codUsuario);

                    }
                }
            } while (opcion != 0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}