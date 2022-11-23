import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

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
    JTextField username;
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

    /* action listener for buttons */
    final ActionListener actionListener = new ActionListener() {
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
            //NEEDS OTHER IMPLEMENTATION
            // TODO: add error checking for pre-existing email
            if (e.getSource() == createNewSellerButton) {
                seller.setVisible(true);
                createAccount.setVisible(false);
                try {
                    Server.createSeller(createEmail.getText(), createUsername.getText(), createPassword.getText(),
                            chooseSecurityQ.getSelectedIndex(), createSecurityAnswer.getText());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            //login seller button sends to seller home page
            //TODO allow for change password
            if (e.getSource() == loginSellerButton) {
                String email = username.getText();
                String type = User.userExists(email);
                if (type.equals("seller")) {
                    Seller user = Seller.parseSeller(email);
                    String pass = password.getText();
                    boolean verified = user.passwordCheck(pass);
                    if (verified) {
                        seller.setVisible(true);
                        login.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect Credentials.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } else if (type.equals("customer")) {
                    JOptionPane.showMessageDialog(null,
                            "this email is registered as a customer.", "error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "this email is not registered! create a new account.", "error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            //logout button for seller frame sends to login screen
            //NEEDS OTHER IMPLEMENTATION
            if (e.getSource() == logoutButtonSeller) {
                login.setVisible(true);
                seller.setVisible(false);
            }
            //creating new customer button sends to marketplace
            //NEEDS OTHER IMPLEMENTATION
            // TODO: add error checking for pre-existing email
            if (e.getSource() == createNewCustomerButton) {
                customer.setVisible(true);
                createAccount.setVisible(false);
                try {
                    Server.createCustomer(createEmail.getText(), createUsername.getText(), createPassword.getText(),
                            chooseSecurityQ.getSelectedIndex(), createSecurityAnswer.getText());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            //login customer button sends to marketplace
            //TODO allow for change password
            if (e.getSource() == loginCustomerButton) {
                String email = username.getText();
                String type = User.userExists(email);
                if (type.equals("customer")) {
                    Customer user = Customer.parseCustomer(email);
                    String pass = password.getText();
                    boolean verified = user.passwordCheck(pass);
                    if (verified) {
                        customer.setVisible(true);
                        login.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect Credentials.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } else if (type.equals("seller")) {
                    JOptionPane.showMessageDialog(null,
                            "this email is registered as a seller.", "error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "this email is not registered! create a new account.", "error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            //logs customer out sends to login screen
            //NEEDS OTHER IMPLEMENTATION
            if (e.getSource() == logoutButtonCustomer) {
                login.setVisible(true);
                customer.setVisible(false);
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
            if (e.getSource() == confirmEditSeller) {
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
//            if (e.getSource() == ) {
//
//            }
//            if (e.getSource() == ) {
//
//            }
//            if (e.getSource() == ) {
//
//            }
//            if (e.getSource() == ) {
//
//            }
//            if (e.getSource() == ) {
//
//            }


        }
    };


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainInterface());
    }

    public void run() {
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
        loginCustomerButton.setBounds(525,350,350, 60);
        loginCustomerButton.addActionListener(actionListener);
        buttonPanelLogin.add(loginCustomerButton);

        loginSellerButton = new JButton("Login Seller");
        loginSellerButton.setBounds(125,350,350, 60);
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

        username = new JTextField("Enter Username");
        username.setBounds(400, 200, 200, 30);
        username.addActionListener(actionListener);
        buttonPanelLogin.add(username);

        password = new JTextField("Enter Password");
        password.setBounds(400, 250, 200, 30);
        password.addActionListener(actionListener);
        buttonPanelLogin.add(password);



        //Creating panel and buttons for creating account screen
        JPanel buttonPanelCreate = new JPanel();
        buttonPanelCreate.setLayout(null);

        createUsername = new JTextField("Enter Username");
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
        createNewSellerButton.setBounds(125,400,200, 60);
        createNewSellerButton.addActionListener(actionListener);
        buttonPanelCreate.add(createNewSellerButton);

        createNewCustomerButton = new JButton("Create Customer Account");
        createNewCustomerButton.setBounds(525,400,350, 60);;
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

        //Creating buttons and panels for edit account page
        JPanel buttonPanelEditSeller = new JPanel();
        buttonPanelEditSeller.setLayout(null);

        editUsernameSeller = new JTextField("Enter Username");
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

        accountContentSeller.add(buttonPanelEditSeller);
        loginContent.add(buttonPanelLogin);
        createAccountContent.add(buttonPanelCreate);
        sellerContent.add(buttonPanelSeller);
        customerContent.add(buttonPanelCustomer);
    }


}

