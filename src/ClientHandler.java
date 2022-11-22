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
        SwingUtilities.invokeLater(new MainInterface());
    }
}
