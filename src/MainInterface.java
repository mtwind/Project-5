import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * A marketplace that allows for multiple users to connect, uses GUI, and is fully run on servers and sockets
 *
 * @author Aaryamik Gupta, Paige Lorenz, Dalton Babbs, Mahika Parekh, Matthew Wind
 * @version November 15, 2022
 */
public class MainInterface extends JComponent implements Runnable {
    JFrame login;
    JFrame createAccount;
    JFrame customer;
    JFrame seller;
    JFrame cart;
    JFrame products;
    JFrame stores;
    JFrame editSeller;
    JFrame editCustomer;
    JTextField email;
    JTextField password;
    JButton createAccountButton;
    JButton loginCustomerButton;
    JButton loginSellerButton;
    JButton exitButton;
    JTextField createUsername;
    JTextField createEmail;
    JTextField createPassword;
    JComboBox chooseSecurityQ;
    JTextField createSecurityAnswer;
    JButton createNewSellerButton;
    JButton createNewCustomerButton;
    JButton back;
    JButton cartButton;
    JButton accountSeller;
    JButton accountCustomer;
    JComboBox storesDropdown;
    JButton logoutButtonSeller;
    JButton logoutButtonCustomer;
    JButton sellerDashboard;
    JButton selectStoreButton;
    JButton exitButtonSeller;
    JButton exitButtonCustomer;
    JTextField editUsernameSeller;
    JTextField editPasswordSeller;
    JTextField editEmailSeller;
    JButton confirmEditSeller;
    JButton backSeller;
    JButton backStore;
    JTextField editUsernameCustomer;
    JTextField editPasswordCustomer;
    JTextField editEmailCustomer;
    JButton confirmEditCustomer;
    JButton logoutButtonStore;
    JButton addProduct;
    JComboBox productsDropdown;
    JButton selectProductButton;
    JButton deleteStore;
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;

    /* action listener for buttons */
    final ActionListener actionListener = new ActionListener() {
        User user;

        @Override
        //Programming different buttons in action listener using if statements
        public void actionPerformed(ActionEvent e) {

            //create account button sends to create account page
            //NEEDS OTHER IMPLEMENTATION
            if (e.getSource() == createAccountButton) {
                login.setVisible(false);
                createAccount.setVisible(true);
            }
            //back button sends back to login screen
            if (e.getSource() == back) {
                login.setVisible(true);
                createAccount.setVisible(false);
            }

            //new seller button sends to seller home page
            //button 1
            if (e.getSource() == createNewSellerButton) {
                writer.write("1");
                writer.println();
                writer.flush();

                String newSellerInfo = "";
                newSellerInfo += createUsername.getText() + "," + createEmail.getText() + "," + createPassword.getText()
                        + ",false" + ","  + createSecurityAnswer.getText() + ","  + chooseSecurityQ.getSelectedIndex();

                writer.write(newSellerInfo);
                writer.println();
                writer.flush();

                String isNew = null;
                try {
                    isNew = reader.readLine();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                if (isNew.equals("new")) {
                    seller.setVisible(true);
                    createAccount.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "this email is already registered to an account", "error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            //creating new customer button sends to marketplace
            // also button 1 because implementation is the same in the server
            if (e.getSource() == createNewCustomerButton) {
                writer.write("1");
                writer.println();
                writer.flush();

                String newCustomerInfo = "";
                newCustomerInfo += createUsername.getText() + "," + createEmail.getText() + "," + createPassword.getText()
                        + ",true" + ","  + createSecurityAnswer.getText() + ","  + chooseSecurityQ.getSelectedIndex();

                writer.write(newCustomerInfo);
                writer.println();
                writer.flush();

                String isNew = null;
                try {
                    isNew = reader.readLine();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                if (isNew.equals("new")) {
                    customer.setVisible(true);
                    createAccount.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "this email is already registered to an account", "error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }


            //login seller button sends to seller home page
            //button num: 2
            //TODO allow for change password
            if (e.getSource() == loginSellerButton) {
                writer.write("2");
                writer.println();
                writer.flush();
                String emailtxt = email.getText();
                writer.write(emailtxt);
                writer.println();
                writer.flush();

                try {
                    String result = reader.readLine();
                    if (result.equals("t")) {
                        writer.write(password.getText());
                        writer.println();
                        writer.flush();
                        if (reader.readLine().equals("verified")) {
                            seller.setVisible(true);
                            login.setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(null, "Incorrect Credentials.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (result.equals("customerLog")) {
                        JOptionPane.showMessageDialog(null,
                                "this email is registered as a customer.", "error", JOptionPane.ERROR_MESSAGE);
                    } else if (result.equals("noLog")) {
                        JOptionPane.showMessageDialog(null,
                                "this email is not registered! create a new account.", "error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception h) {
                    h.printStackTrace();
                }

            }
            //logout button for seller frame sends to login screen
            //NEEDS OTHER IMPLEMENTATION
            if (e.getSource() == logoutButtonSeller) {
                    user = null;
                    login.setVisible(true);
                    seller.setVisible(false);


            }

            //login customer button sends to marketplace
            //button 3
            //TODO allow for change password
            if (e.getSource() == loginCustomerButton) {
                writer.write("3");
                writer.println();
                writer.flush();
                String emailtxt = email.getText();
                writer.write(emailtxt);
                writer.println();
                writer.flush();
                try {
                    String result = reader.readLine();
                    if (result.equals("t")) {
                        writer.write(password.getText());
                        writer.println();
                        writer.flush();
                        if (reader.readLine().equals("verified")) {
                            seller.setVisible(true);
                            login.setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(null, "Incorrect Credentials.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (result.equals("sellerLog")) {
                        JOptionPane.showMessageDialog(null,
                                "this email is registered as a seller.", "error", JOptionPane.ERROR_MESSAGE);
                    } else if (result.equals("noLog")) {
                        JOptionPane.showMessageDialog(null,
                                "this email is not registered! create a new account.", "error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception h) {
                    h.printStackTrace();
                }
            }
            //logs customer out sends to login screen
            //NEEDS OTHER IMPLEMENTATION
            if (e.getSource() == logoutButtonCustomer) {
                login.setVisible(true);
                customer.setVisible(false);
                user = null;
            }
            //takes customer to account screen

            if (e.getSource() == accountSeller) {
                editSeller.setVisible(true);
                seller.setVisible(false);
            }
            //takes user back to seller screen
            if (e.getSource() == backSeller) {
                seller.setVisible(true);
                editSeller.setVisible(false);
            }
            //takes user back to seller screen
            //NEEDS OTHER IMPLEMENTATION
            //button 5
            if (e.getSource() == confirmEditSeller) {
                user.setPassword(editPasswordSeller.getText());
                user.setEmail(editEmailSeller.getText());
                user.editUserFile();
                seller.setVisible(true);
                editSeller.setVisible(false);
            }

            // resets account creation text fields after an account is created
            if (e.getSource() == logoutButtonCustomer || e.getSource() == logoutButtonSeller) {
                createEmail.setText("Enter Email");
                createUsername.setText("Enter Username");
                createPassword.setText("Enter Password");
                chooseSecurityQ.setSelectedIndex(0);
                createSecurityAnswer.setText("Enter Answer");
            }
            //takes user back to customer screen and edits settings
            //NEEDS OTHER IMPLEMENTATION
            //button 6
            if (e.getSource() == confirmEditCustomer) {
                customer.setVisible(true);
                editCustomer.setVisible(false);
            }
            //takes user to stores screen and needs to change values of dropdowns in store settings
            //NEEDS OTHER IMPLEMENTATION

            if (e.getSource() == selectStoreButton) {
                stores.setVisible(true);
                seller.setVisible(false);
            }
            //deletes the store that's currently selected and takes back to seller homepage
            //NEEDS OTHER IMPLEMENTATION
            if (e.getSource() == deleteStore) {
                seller.setVisible(true);
                stores.setVisible(false);
            }
            //takes user back to seller homepage
            if (e.getSource() == backStore) {
                seller.setVisible(true);
                stores.setVisible(false);
            }
            //takes user back to login screen
            //NEEDS OTHER IMPLEMENTATION
            if (e.getSource() == logoutButtonStore) {
                login.setVisible(true);
                stores.setVisible(false);
            }


        }
    };


    public static void main(String[] args) {


    }

    public void run() {


        // creating socket
        try {
            socket = new Socket("localhost", 1);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }


        // set up JFrames for all different frames
        login = new JFrame("Login");
        Container loginContent = login.getContentPane();
        createAccount = new JFrame("Create Account");
        Container createAccountContent = createAccount.getContentPane();
        createAccountContent.setLayout(new BorderLayout());
        customer = new JFrame("MarketPlace");
        Container customerContent = customer.getContentPane();
        customerContent.setLayout(new BorderLayout());
        seller = new JFrame("Seller Interface");
        Container sellerContent = seller.getContentPane();
        sellerContent.setLayout(new BorderLayout());
        cart = new JFrame("Shopping Cart");
        Container cartContent = cart.getContentPane();
        cartContent.setLayout(new BorderLayout());
        stores = new JFrame("Stores");
        Container storesContent = stores.getContentPane();
        storesContent.setLayout(new BorderLayout());
        products = new JFrame("Products");
        Container productsContent = products.getContentPane();
        productsContent.setLayout(new BorderLayout());
        editSeller = new JFrame("Account Settings");
        Container accountContentSeller = editSeller.getContentPane();
        accountContentSeller.setLayout(new BorderLayout());
        editCustomer = new JFrame("Account Settings");
        Container accountContentCustomer = editCustomer.getContentPane();
        accountContentCustomer.setLayout(new BorderLayout());


        //Setting Default Sizes of frames and such
        login.setSize(1000, 600);
        login.setLocationRelativeTo(null);
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.setVisible(true);

        createAccount.setSize(1000, 600);
        createAccount.setLocationRelativeTo(null);
        createAccount.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createAccount.setVisible(false);

        customer.setSize(1000, 600);
        customer.setLocationRelativeTo(null);
        customer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        customer.setVisible(false);

        seller.setSize(1000, 600);
        seller.setLocationRelativeTo(null);
        seller.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        seller.setVisible(false);

        cart.setSize(1000, 600);
        cart.setLocationRelativeTo(null);
        cart.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cart.setVisible(false);

        stores.setSize(1000, 600);
        stores.setLocationRelativeTo(null);
        stores.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        stores.setVisible(false);

        products.setSize(1000, 600);
        products.setLocationRelativeTo(null);
        products.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        products.setVisible(false);

        editSeller.setSize(1000, 600);
        editSeller.setLocationRelativeTo(null);
        editSeller.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        editSeller.setVisible(false);

        editCustomer.setSize(1000, 600);
        editCustomer.setLocationRelativeTo(null);
        editCustomer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        editCustomer.setVisible(false);

        //Creating Panels and Buttons for Login
        JPanel buttonPanelLogin = new JPanel();

        buttonPanelLogin.setLayout(null);
        loginCustomerButton = new JButton("Login Customer");
        loginCustomerButton.setBounds(525, 350, 350, 60);
        loginCustomerButton.addActionListener(actionListener);
        buttonPanelLogin.add(loginCustomerButton);

        loginSellerButton = new JButton("Login Seller");
        loginSellerButton.setBounds(125, 350, 350, 60);
        loginSellerButton.addActionListener(actionListener);
        buttonPanelLogin.add(loginSellerButton);

        createAccountButton = new JButton("Create New Account");
        createAccountButton.setBounds(600, 475, 350, 60);
        createAccountButton.addActionListener(actionListener);
        buttonPanelLogin.add(createAccountButton);

        exitButton = new JButton("Exit");
        exitButton.setBounds(50, 475, 350, 60);
        exitButton.addActionListener(actionListener);
        buttonPanelLogin.add(exitButton);

        email = new JTextField("Enter Email");
        email.setBounds(400, 200, 200, 30);
        email.addActionListener(actionListener);
        buttonPanelLogin.add(email);

        password = new JTextField("Enter Password");
        password.setBounds(400, 250, 200, 30);
        password.addActionListener(actionListener);
        buttonPanelLogin.add(password);


        //Creating panel and buttons for creating account screen
        JPanel buttonPanelCreate = new JPanel();
        buttonPanelCreate.setLayout(null);

        createUsername = new JTextField("Enter Name");
        createUsername.setBounds(400, 100, 200, 30);
        createUsername.addActionListener(actionListener);
        buttonPanelCreate.add(createUsername);

        createPassword = new JTextField("Enter Password");
        createPassword.setBounds(400, 150, 200, 30);
        createPassword.addActionListener(actionListener);
        buttonPanelCreate.add(createPassword);

        createEmail = new JTextField("Enter Email");
        createEmail.setBounds(400, 50, 200, 30);
        createEmail.addActionListener(actionListener);
        buttonPanelCreate.add(createEmail);

        chooseSecurityQ = new JComboBox(User.questionList);
        chooseSecurityQ.setBounds(360, 200, 275, 30);
        chooseSecurityQ.addActionListener(actionListener);
        buttonPanelCreate.add(chooseSecurityQ);

        createSecurityAnswer = new JTextField("Enter Answer");
        createSecurityAnswer.setBounds(400, 250, 200, 30);
        createSecurityAnswer.addActionListener(actionListener);
        buttonPanelCreate.add(createSecurityAnswer);

        createNewSellerButton = new JButton("Create Seller Account");
        createNewSellerButton.setBounds(125, 400, 350, 60);
        createNewSellerButton.addActionListener(actionListener);
        buttonPanelCreate.add(createNewSellerButton);

        createNewCustomerButton = new JButton("Create Customer Account");
        createNewCustomerButton.setBounds(525, 400, 350, 60);
        ;
        createNewCustomerButton.addActionListener(actionListener);
        buttonPanelCreate.add(createNewCustomerButton);

        back = new JButton("Back");
        back.setBounds(50, 475, 350, 60);
        back.addActionListener(actionListener);
        buttonPanelCreate.add(back);

        //Creating buttons and panel for Seller Page
        JPanel buttonPanelSeller = new JPanel();
        buttonPanelSeller.setLayout(null);

        accountSeller = new JButton("Edit Account");
        accountSeller.setBounds(850, 20, 150, 30);
        accountSeller.addActionListener(actionListener);
        buttonPanelSeller.add(accountSeller);

        logoutButtonSeller = new JButton("Logout");
        logoutButtonSeller.setBounds(50, 20, 150, 30);
        logoutButtonSeller.addActionListener(actionListener);
        buttonPanelSeller.add(logoutButtonSeller);

        sellerDashboard = new JButton("View Dashboard");
        sellerDashboard.setBounds(650, 20, 150, 30);
        sellerDashboard.addActionListener(actionListener);
        buttonPanelSeller.add(sellerDashboard);

        storesDropdown = new JComboBox();
        storesDropdown.setBounds(400, 250, 200, 30);
        buttonPanelSeller.add(storesDropdown);

        selectStoreButton = new JButton("Select Store");
        selectStoreButton.setBounds(400, 300, 200, 30);
        selectStoreButton.addActionListener(actionListener);
        buttonPanelSeller.add(selectStoreButton);

        exitButtonSeller = new JButton("Exit");
        exitButtonSeller.setBounds(50, 475, 350, 60);
        exitButtonSeller.addActionListener(actionListener);
        buttonPanelSeller.add(exitButtonSeller);


        //Creating buttons and panel for Customer frame
        JPanel buttonPanelCustomer = new JPanel();
        buttonPanelCustomer.setLayout(null);

        accountCustomer = new JButton("Edit Account");
        accountCustomer.setBounds(850, 20, 150, 30);
        accountCustomer.addActionListener(actionListener);
        buttonPanelCustomer.add(accountCustomer);

        cartButton = new JButton("View Cart");
        cartButton.setBounds(650, 20, 150, 30);
        cartButton.addActionListener(actionListener);
        buttonPanelCustomer.add(cartButton);

        logoutButtonCustomer = new JButton("Logout");
        logoutButtonCustomer.setBounds(50, 20, 150, 30);
        logoutButtonCustomer.addActionListener(actionListener);
        buttonPanelCustomer.add(logoutButtonCustomer);

        exitButtonCustomer = new JButton("Exit");
        exitButtonCustomer.setBounds(50, 475, 350, 60);
        exitButtonCustomer.addActionListener(actionListener);
        buttonPanelCustomer.add(exitButtonCustomer);

        //Creating buttons and panels for edit Seller page
        JPanel buttonPanelEditSeller = new JPanel();
        buttonPanelEditSeller.setLayout(null);

        editUsernameSeller = new JTextField("Enter Name");
        editUsernameSeller.setBounds(400, 200, 200, 30);
        editUsernameSeller.addActionListener(actionListener);
        buttonPanelEditSeller.add(editUsernameSeller);

        editPasswordSeller = new JTextField("Enter Password");
        editPasswordSeller.setBounds(400, 250, 200, 30);
        editPasswordSeller.addActionListener(actionListener);
        buttonPanelEditSeller.add(editPasswordSeller);

        editEmailSeller = new JTextField("Enter Email");
        editEmailSeller.setBounds(400, 150, 200, 30);
        editEmailSeller.addActionListener(actionListener);
        buttonPanelEditSeller.add(editEmailSeller);

        confirmEditSeller = new JButton("Edit Settings");
        confirmEditSeller.setBounds(400, 300, 200, 60);
        confirmEditSeller.addActionListener(actionListener);
        buttonPanelEditSeller.add(confirmEditSeller);

        backSeller = new JButton("Back");
        backSeller.setBounds(50, 475, 350, 60);
        backSeller.addActionListener(actionListener);
        buttonPanelEditSeller.add(backSeller);

        //Creating buttons and panels for edit customer page
        JPanel buttonPanelEditCustomer = new JPanel();
        buttonPanelEditCustomer.setLayout(null);

        editUsernameCustomer = new JTextField("Enter Name");
        editUsernameCustomer.setBounds(400, 200, 200, 30);
        editUsernameCustomer.addActionListener(actionListener);
        buttonPanelEditCustomer.add(editUsernameCustomer);

        editPasswordCustomer = new JTextField("Enter Password");
        editPasswordCustomer.setBounds(400, 250, 200, 30);
        editPasswordCustomer.addActionListener(actionListener);
        buttonPanelEditCustomer.add(editPasswordCustomer);

        editEmailCustomer = new JTextField("Enter Email");
        editEmailCustomer.setBounds(400, 150, 200, 30);
        editEmailCustomer.addActionListener(actionListener);
        buttonPanelEditCustomer.add(editEmailCustomer);

        confirmEditCustomer = new JButton("Edit Settings");
        confirmEditCustomer.setBounds(400, 300, 200, 60);
        confirmEditCustomer.addActionListener(actionListener);
        buttonPanelEditCustomer.add(confirmEditCustomer);

        backSeller = new JButton("Back");
        backSeller.setBounds(50, 475, 350, 60);
        backSeller.addActionListener(actionListener);
        buttonPanelEditCustomer.add(backSeller);

        //Creating buttons and panels for store page
        JPanel buttonPanelStore = new JPanel();
        buttonPanelStore.setLayout(null);

        logoutButtonStore = new JButton("Logout");
        logoutButtonStore.setBounds(50, 20, 150, 30);
        logoutButtonStore.addActionListener(actionListener);
        buttonPanelStore.add(logoutButtonStore);

        addProduct = new JButton("Add Product");
        addProduct.setBounds(800, 20, 150, 30);
        addProduct.addActionListener(actionListener);
        buttonPanelStore.add(addProduct);

        productsDropdown = new JComboBox();
        productsDropdown.setBounds(400, 250, 200, 30);
        buttonPanelStore.add(productsDropdown);

        selectProductButton = new JButton("Select Product");
        selectProductButton.setBounds(400, 300, 200, 30);
        selectProductButton.addActionListener(actionListener);
        buttonPanelStore.add(selectProductButton);

        backStore = new JButton("Back");
        backStore.setBounds(50, 475, 350, 60);
        backStore.addActionListener(actionListener);
        buttonPanelStore.add(backStore);

        deleteStore = new JButton("Remove This Store");
        deleteStore.setBounds(600, 475, 350, 60);
        deleteStore.addActionListener(actionListener);
        buttonPanelStore.add(deleteStore);

        //Creating buttons and panels for products page




        //Creating buttons and panels for cart page (For this I don't know exactly what is needed i may need help)




        accountContentSeller.add(buttonPanelEditSeller);
        loginContent.add(buttonPanelLogin);
        createAccountContent.add(buttonPanelCreate);
        sellerContent.add(buttonPanelSeller);
        customerContent.add(buttonPanelCustomer);
        storesContent.add(buttonPanelStore);
        accountContentCustomer.add(buttonPanelEditCustomer);
    }

}
