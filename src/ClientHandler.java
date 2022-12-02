import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    Socket socket;
    User user;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            //SwingUtilities.invokeLater(new MainInterface());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            int buttonChoice = 0;
            boolean running = true;
            while (running) {
                buttonChoice = Integer.parseInt(reader.readLine());
                System.out.println(buttonChoice);
                //TODO: enter switch statement that takes input from client (based on button) and processes information
                String email, type;
                switch (buttonChoice) {
                    case 1: // create an account, used for both sellers and customers
                        String[] info = reader.readLine().split(",");
                        boolean alreadyUsed = User.checkNewUser(info[1]);
                        System.out.println(alreadyUsed);

                        if (alreadyUsed == false) {
                            boolean isCustomer = Boolean.parseBoolean(info[3]);

                            if (isCustomer)
                            {
                                user = new Customer(info[0], info[1], info[2], true,
                                        info[4], Integer.parseInt(info[5]));
                            } else {
                                user = new Seller(info[0], info[1], info[2], false,
                                        info[4], Integer.parseInt(info[5]));
                            }
                            writer.write("new");
                            writer.println();
                            writer.flush();
                        } else {
                            writer.write("not new");
                            writer.println();
                            writer.flush();
                        }
                        break;

                    case 2: //seller log in
                        email = reader.readLine();
                        type = User.userExists(email);
                        if (type.equals("seller")) {
                            writer.write("t");
                            writer.println();
                            writer.flush();
                            user = Seller.parseSeller(email);
                            String pass = reader.readLine();
                            boolean verified = user.passwordCheck(pass);
                            if (verified) {
                                writer.write("verified");
                                writer.println();
                                writer.flush();

                                StringBuilder line = new StringBuilder();
                                for (int i = 0; i < ((Seller)user).getStores().size(); i++) {
                                    System.out.println(((Seller)user).getStores().get(i));
                                    line.append(((Seller)user).getStores().get(i).getName());
                                    if (i != ((Seller)user).getStores().size() - 1) {
                                        line.append(",");
                                    }
                                }
                                System.out.println(line.toString());
                                writer.write(line.toString());
                                writer.println();
                                writer.flush();

                            } else {
                                writer.write("!verified");
                                writer.println();
                                writer.flush();
                            }

                        } else if (type.equals("customer")) {
                            writer.write("customerLog");
                            writer.println();
                            writer.flush();

                        } else {
                            writer.write("noLog");
                            writer.println();
                            writer.flush();
                        }
                        break;
                    case 3: // customer log in
                        email = reader.readLine();
                        type = User.userExists(email);
                        if (type.equals("customer")) {
                            writer.write("t");
                            writer.println();
                            writer.flush();
                            user = Seller.parseSeller(email);
                            String pass = reader.readLine();
                            boolean verified = user.passwordCheck(pass);
                            if (verified) {
                                writer.write("verified");
                                writer.println();
                                writer.flush();
                            } else {
                                writer.write("!verified");
                                writer.println();
                                writer.flush();
                            }

                        } else if (type.equals("seller")) {
                            writer.write("sellerLog");
                            writer.println();
                            writer.flush();

                        } else {
                            writer.write("noLog");
                            writer.println();
                            writer.flush();
                        }
                        break;
                    case 7:
                        String selectedStore = reader.readLine();

                        break;
                    default:
                        running = false;
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

