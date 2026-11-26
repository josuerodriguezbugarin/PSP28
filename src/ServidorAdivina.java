import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class ServidorAdivina {

    public static void main(String[] args) {
        int puerto = 5000;
        Random generador = new Random();

        try (ServerSocket socketServidor = new ServerSocket(puerto)) {
            System.out.println("Servidor iniciado en el puerto " + puerto);

            while (true) {
                try (Socket socketCliente = socketServidor.accept();
                     BufferedReader lector = new BufferedReader(
                             new InputStreamReader(socketCliente.getInputStream(), StandardCharsets.UTF_8));
                     PrintWriter escritor = new PrintWriter(
                             new OutputStreamWriter(socketCliente.getOutputStream(), StandardCharsets.UTF_8), true)) {

                    int objetivo = generador.nextInt(100) + 1;
                    int contadorIntentos = 0;
                    String mensajeRecibido;

                    while ((mensajeRecibido = lector.readLine()) != null) {
                        String comando = mensajeRecibido.trim();

                        if (comando.equalsIgnoreCase("ADIOS")) {
                            escritor.println("OK Hasta luego");
                            break;
                        }

                        try {
                            int apuesta = Integer.parseInt(comando);
                            contadorIntentos++;

                            if (apuesta < objetivo) {
                                escritor.println("MAYOR");
                            } else if (apuesta > objetivo) {
                                escritor.println("MENOR");
                            } else {
                                escritor.println("ACIERTO en " + contadorIntentos + " intentos");
                                break;
                            }
                        } catch (NumberFormatException ex) {
                            escritor.println("ERR Comando no válido");
                        }
                    }

                } catch (IOException ex) {
                    System.out.println("Cliente desconectado: " + ex.getMessage());
                }
            }

        } catch (IOException ex) {
            System.out.println("No se pudo abrir el servidor: " + ex.getMessage());
        }
    }
}