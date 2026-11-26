import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClienteAdivina {

    public static void main(String[] args) {
        String ipServidor = "127.0.0.1";
        int puerto = 5000;

        try (Socket conexion = new Socket(ipServidor, puerto);
             BufferedReader entrada = new BufferedReader(
                     new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter salida = new PrintWriter(
                     new OutputStreamWriter(conexion.getOutputStream(), StandardCharsets.UTF_8), true);
             Scanner teclado = new Scanner(System.in)) {

            boolean jugando = true;

            while (jugando) {
                System.out.print("Escribe un número del 1 al 100 (o ADIOS para salir): ");
                String textoUsuario = teclado.nextLine();

                salida.println(textoUsuario);

                String contestacion = entrada.readLine();
                if (contestacion == null) {
                    System.out.println("Se ha perdido la conexión con el servidor.");
                    break;
                }

                System.out.println("Respuesta: " + contestacion);

                if (contestacion.startsWith("OK") || contestacion.startsWith("ACIERTO")) {
                    jugando = false;
                }
            }

        } catch (IOException ex) {
            System.out.println("Error de conexión: " + ex.getMessage());
        }
    }
}