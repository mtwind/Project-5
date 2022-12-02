import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

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
                        System.out.println("in seller log in");
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
                    case 5: // confirmEditSeller button, edits seller and store info in files
                        System.out.println("here");
                        String sellerNewPass = reader.readLine(); // read new pass from client
                        String sellerNewName = reader.readLine();
                        String sellerNewEmail = reader.readLine(); // read new email from client
                        String oldSellerEmail = user.getEmail();
                        if (user == null) {
                            System.out.println("user null");
                        }
                        user = Seller.parseSeller(oldSellerEmail);
                        if (user == null) {
                            System.out.println("user null2");
                        }
                        System.out.println("here");
                        ArrayList<String> userLines = ((Seller)user).readUserFile();
                        for (int i = 0; i < userLines.size(); i++) {
                            String[] userData = userLines.get(i).split(",");
                            if (userData[1].equals(oldSellerEmail)) {
                                userData[1] = sellerNewEmail;
                                userData[2] = sellerNewName;
                                userData[3] = sellerNewPass;
                                String newUserLine = "";
                                for (int j = 0; j < userData.length; j++) {
                                    if (j == userData.length - 1) {
                                        newUserLine = newUserLine.concat(userData[j]);
                                    } else {
                                        newUserLine = newUserLine.concat(userData[j] + ",");
                                    }
                                }
                                userLines.set(i, newUserLine);
                                PrintWriter pw = new PrintWriter(new FileOutputStream("users.txt"));
                                for (int x = 0; x < userLines.size(); x++) {
                                    pw.println(userLines.get(x));
                                }
                                pw.flush();
                                pw.close();
                                break;
                            }
                        }
                        user.setPassword(sellerNewPass);
                        user.setName(sellerNewName);
                        user.setEmail(sellerNewEmail);
                        for (int i = 0; i < ((Seller)user).getStores().size(); i++) {
                            ((Seller) user).getStores().get(i).setOwner(sellerNewEmail);
                            ((Seller) user).getStores().get(i).editStoreFile();
                        }
                        break;
                    case 6: // confirmEditCustomer button
                        System.out.println("here");
                        String customerNewPass = reader.readLine(); // read new pass from client
                        String customerNewName = reader.readLine();
                        String customerNewEmail = reader.readLine(); // read new email from client
                        String oldCustomerEmail = user.getEmail();
                        if (user == null) {
                            System.out.println("user null");
                        }
                        user = Customer.parseCustomer(oldCustomerEmail);
                        if (user == null) {
                            System.out.println("user null2");
                        }
                        System.out.println("here");
                        ArrayList<String> customerLines = ((Customer)user).readUserFile();
                        for (int i = 0; i < customerLines.size(); i++) {
                            String[] userData = customerLines.get(i).split(",");
                            if (userData[1].equals(oldCustomerEmail)) {
                                userData[1] = customerNewEmail;
                                userData[2] = customerNewName;
                                userData[3] = customerNewPass;
                                String newUserLine = "";
                                for (int j = 0; j < userData.length; j++) {
                                    if (j == userData.length - 1) {
                                        newUserLine = newUserLine.concat(userData[j]);
                                    } else {
                                        newUserLine = newUserLine.concat(userData[j] + ",");
                                    }
                                }
                                customerLines.set(i, newUserLine);
                                PrintWriter pw = new PrintWriter(new FileOutputStream("users.txt"));
                                for (int x = 0; x < customerLines.size(); x++) {
                                    pw.println(customerLines.get(x));
                                }
                                pw.flush();
                                pw.close();
                                break;
                            }
                        }
                        user.setPassword(customerNewPass);
                        user.setName(customerNewName);
                        user.setEmail(customerNewEmail);

                        // TODO: implement changes to the emails in the store's list of customers
                        break;
                    case 7:
                        String selectedStore = reader.readLine();
                        ArrayList<Store> fileStores = Store.getAllStores();
                        Store selected = null;
                        for (int i = 0; i < fileStores.size(); i++) {
                            if (fileStores.get(i).getOwner().equals(user.getEmail()) &&
                                    fileStores.get(i).getName().equals(selectedStore)) {
                                selected = fileStores.get(i);
                                break;
                            }
                        }
                        ArrayList<Product> storeProducts;
                        if (selected != null)
                            storeProducts = selected.getProducts();
                        else
                            storeProducts = new ArrayList<>();

                        StringBuilder str = new StringBuilder("");
                        for (int i = 0; i < storeProducts.size(); i++) {
                            str.append(storeProducts.get(i).getName());
                            if (i != storeProducts.size() - 1)
                                str.append(",");
                        }
                        writer.write(str.toString());
                        writer.println();
                        writer.flush();

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

