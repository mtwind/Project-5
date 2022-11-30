import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    Socket socket;
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            SwingUtilities.invokeLater(new MainInterface());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            int buttonChoice = 0;
            boolean running = true;
            while (running) {
                //TODO: enter switch statement that takes input from client (based on button) and processes information
                switch(buttonChoice) {
                    default:
                        running = false;
                        break;
                }
            }

            reader.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
