import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Client
 *
 * Invokes an instance of MainInterface for each user
 * Creates a socket to connect to the serversocket
 *
 * @author Aaryamik Gupta, Paige Lorenz, Dalton Babbs, Mahika Parakh, Matthew Wind
 * @version December 11, 2022
 */
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