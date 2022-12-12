import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Server
 *
 * Creates serversocket with port number 1
 * Starts new threads using ClientHandler for each client as necessary
 *
 * @author Aaryamik Gupta, Paige Lorenz, Dalton Babbs, Mahika Parakh, Matthew Wind
 * @version December 11, 2022
 */
public class Server {

    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(1);
            server.setReuseAddress(true);
            // the next lines start and accept a socket from the client and create objects to read and write to
            // the client
            while (true) {
                Socket client = server.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter writer = new PrintWriter(client.getOutputStream());
                ClientHandler clientSock = new ClientHandler(client);
                new Thread(clientSock).start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}