import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Client {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 1);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            SwingUtilities.invokeLater(new MainInterface());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}