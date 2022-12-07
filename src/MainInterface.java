import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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
    JFrame makeProduct;
    JFrame stores;
    JFrame editSeller;
    JFrame editCustomer;
    JFrame sellerDashboardFrame;
    JFrame customerDashboardFrame;

    JTextField email;
    JTextField password;
    JButton createAccountButton;
    JButton loginCustomerButton;
    JButton loginSellerButton;
    JButton exitButton;
    JTextField createUsername;
    JTextField createEmail;
    JTextField createPassword;
    JComboBox<String> chooseSecurityQ;
    JTextField createSecurityAnswer;
    JButton createNewSellerButton;
    JButton createNewCustomerButton;
    JButton back;
    JButton cartButton;
    JButton accountSeller;
    JButton accountCustomer;
    JComboBox<String> storesDropdown;
    JButton logoutButtonSeller;
    JButton logoutButtonCustomer;
    JButton sellerDashboard;
    JButton addStore;
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
    JTextField productName;
    JTextField productDescription;
    JTextField productPrice;
    JTextField productQuantity;
    JButton newProductBackButton;
    JButton createNewProductButton;
    JComboBox<String> productsDropdown;
    JButton selectProductButton;
    JButton deleteStore;
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    JComboBox<String> marketSelect;
    JTextField searchBox;
    JButton searchBtn;
    JButton allProBtn;
    JButton customerViewProPage;
    JTextField productNameEdit;
    JButton productButtonEdit;
    JButton backButtonEdit;
    JTextField productDescriptionEdit;
    JTextField productQuantityEdit;
    JTextField productPriceEdit;
    JButton productButtonRemove;
    JButton logoutButtonEdit;
    JButton customerDashboard;
    JButton backCustomer;
    JButton backButtonSellerDash;
    JButton logoutButtonSellerDash;
    JButton logoutButtonCustomerDash;
    JButton backButtonCustomerDash;
    JComboBox<String> customerDash;
    JComboBox<String> sellerDash;
    JButton sortByUserPop;
    JButton sortByGenPop;
    JButton sortBySales;
    JButton sortByRevenue;


    String[] storeList = new String[0];
    String[] productList = new String[0];
    String[] marketPlace = new String[0];

    JLabel proName;
    JLabel proPrice;
    JLabel proQuantity;
    JLabel proStore;
    JLabel proDescription;

    JButton deleteProduct;


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
                    storesDropdown.setVisible(false);
                    selectStoreButton.setVisible(false);
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
                            login.setVisible(false);
                            try {
                                //takes the store names separated by commas
                                String line = reader.readLine();
                                System.out.println(line);
                                if (!line.equals(""))
                                    storeList = line.split(",");
                                else {
                                    storeList = new String[0];
                                    storesDropdown.setVisible(false);
                                    selectStoreButton.setVisible(false);
                                }
                                if (storeList.length > 0) {
                                    storesDropdown.setVisible(true);
                                    selectStoreButton.setVisible(true);
                                    storesDropdown.setModel(new DefaultComboBoxModel<String>(storeList));
                                } else {
                                    JOptionPane.showMessageDialog(null,
                                            "There are no stores. add stores", "error", JOptionPane.ERROR_MESSAGE);
                                }

                            } catch (Exception p) {
                                p.printStackTrace();
                            }
                            seller.setVisible(true);
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
                            customer.setVisible(true);
                            login.setVisible(false);

                            String productList = reader.readLine();
                            if (!productList.equals("")) {
                                marketPlace = productList.split(",");
                                marketSelect.setModel(new DefaultComboBoxModel<String>(marketPlace));
                                marketSelect.setVisible(true);
                                searchBox.setVisible(true);
                                searchBtn.setVisible(true);
                                customerViewProPage.setVisible(true);
                                allProBtn.setVisible(true);
                            } else {
                                searchBtn.setVisible(false);
                                marketSelect.setVisible(false);
                                searchBox.setVisible(false);
                                customerViewProPage.setVisible(false);
                                allProBtn.setVisible(false);
                                JOptionPane.showMessageDialog(null,
                                        "There are currently no products in the marketplace.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
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
                storeList = new String[0];
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
                writer.write("5");
                writer.println();
                writer.flush();
                writer.write(editPasswordSeller.getText()); // write new password to server
                writer.println();
                writer.flush();
                writer.write(editUsernameSeller.getText());
                writer.println();
                writer.flush();
                writer.write(editEmailSeller.getText()); // write new email to server
                writer.println();
                writer.flush();
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
            //takes user back to customer screen and edits account settings
            //NEEDS OTHER IMPLEMENTATION
            //button 6
            if (e.getSource() == confirmEditCustomer) {
                writer.write("6");
                writer.println();
                writer.flush();
                writer.write(editPasswordCustomer.getText()); // write new password to server
                writer.println();
                writer.flush();
                writer.write(editUsernameCustomer.getText());
                writer.println();
                writer.flush();
                writer.write(editEmailCustomer.getText()); // write new email to server
                writer.println();
                writer.flush();
                customer.setVisible(true);
                editCustomer.setVisible(false);
            }


            //takes user to stores screen and needs to change values of dropdowns in store settings
            //NEEDS OTHER IMPLEMENTATION 7
            if (e.getSource() == selectStoreButton) {

                writer.write("7");
                writer.println();
                writer.flush();

                System.out.println(storeList[storesDropdown.getSelectedIndex()]);
                writer.write(storeList[storesDropdown.getSelectedIndex()]);
                writer.println();
                writer.flush();

                try {
                    //takes the store names separated by commas
                    String line = reader.readLine();
                    System.out.println(line);
                    if (!line.equals(""))
                        productList = line.split(",");
                    else {
                        productList = new String[0];
                        productsDropdown.setVisible(false);
                        selectProductButton.setVisible(false);
                    }
                    if (storeList.length > 0) {
                        productsDropdown.setVisible(true);
                        productsDropdown.setVisible(true);
                        productsDropdown.setModel(new DefaultComboBoxModel<String>(productList));
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "There are no products. add products", "error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception p) {
                    p.printStackTrace();
                }

                stores.setVisible(true);
                seller.setVisible(false);
            }

            // add store button = button 8
            if (e.getSource() == addStore)
            {
                writer.write("8");
                writer.println();
                writer.flush();

                String newStoreName = JOptionPane.showInputDialog(null, "Enter the Store Name: ", "Create Store",
                        JOptionPane.QUESTION_MESSAGE);
                writer.write(newStoreName);
                writer.println();
                writer.flush();

                String serverAnswer = null;
                try {
                    serverAnswer = reader.readLine();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                if (serverAnswer.equals("already used"))
                {
                    JOptionPane.showMessageDialog(null, "A store under that name already exists.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Store Created!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    //takes the store names separated by commas

                    storeList = serverAnswer.split(",");
                    storesDropdown.setVisible(true);
                    selectStoreButton.setVisible(true);
                    storesDropdown.setModel(new DefaultComboBoxModel<String>(storeList));

                }
            }

            //deletes the store that's currently selected and takes back to seller homepage
            //NEEDS OTHER IMPLEMENTATION 9
            if (e.getSource() == deleteStore) {
                writer.write("9");
                writer.println();
                writer.flush();

                // accesses and sends name of store to ClientHandler
                String targetStore = storesDropdown.getItemAt(storesDropdown.getSelectedIndex());
                writer.write(targetStore);
                writer.println();
                writer.flush();

                String serverResponse;
                try {
                    serverResponse = reader.readLine();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(null, "Store Removed.", "Success", JOptionPane.INFORMATION_MESSAGE);

                storeList = serverResponse.split(",");
                ArrayList<String> storeArrayList = new ArrayList<>();
                System.out.println("storeList: ");
                for(String s: storeList) {
                    System.out.println(s);
                    storeArrayList.add(s);
                }

                seller.setVisible(true);
                stores.setVisible(false);
                storesDropdown.setModel(new DefaultComboBoxModel<String>(storeList));
                storesDropdown.setVisible(true);
                selectStoreButton.setVisible(true);
            }

            if (e.getSource() == newProductBackButton) {
                stores.setVisible(true);
                makeProduct.setVisible(false);
            }


            if (e.getSource() == addProduct) {
                makeProduct.setVisible(true);
                stores.setVisible(false);

            }

            // button 10 adds products to a store
            if (e.getSource() == createNewProductButton)
            {
                writer.write("10");
                writer.println();
                writer.flush();

                String newProductInfo = productName.getText() + "," + productDescription.getText() + ","
                        + productQuantity.getText() + "," + productPrice.getText();
                writer.write(newProductInfo);
                writer.println();
                writer.flush();

                String serverResponse = "";
                try {
                    serverResponse = reader.readLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                if (serverResponse.equals("name error")) {
                    JOptionPane.showMessageDialog(null, "The product name must not be empty or contain the characters"
                            + " [\",\" \"~\" \"-\"]", "Name Error", JOptionPane.ERROR_MESSAGE);
                } else if (serverResponse.equals("description error")) {
                    JOptionPane.showMessageDialog(null, "The product name must not be empty or contain the characters "
                            + "[\",\" \"~\"]", "Description Error", JOptionPane.ERROR_MESSAGE);
                } else if (serverResponse.equals("price error")) {
                    JOptionPane.showMessageDialog(null, "The price must be a number greater than 0.00", "Price Error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (serverResponse.equals("quantity error")) {
                    JOptionPane.showMessageDialog(null, "The quantity must be a number greater than 0",
                            "Quantity Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Product Created!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    //takes the product names separated by commas
                    productList = serverResponse.split(",");
                    productsDropdown.setVisible(true);
                    selectProductButton.setVisible(true);
                    productsDropdown.setModel(new DefaultComboBoxModel<String>(productList));

                    productName.setText("Enter Product Name:");
                    productDescription.setText("Enter Product Description:");
                    productPrice.setText("Enter Product Price:");
                    productQuantity.setText("Enter Product Quantity:");
                }
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

            if (e.getSource() == selectProductButton) {// button 11, shows product page/details for SELLER w/in store
                writer.write("11");
                writer.println();
                writer.flush();

                writer.write(productList[productsDropdown.getSelectedIndex()]);
                writer.println();
                writer.flush();

                writer.write(storeList[storesDropdown.getSelectedIndex()]);
                writer.println();
                writer.flush();

                try {
                    proName.setText("Product: " + productList[productsDropdown.getSelectedIndex()]);
                    proStore.setText("Store: " + storeList[storesDropdown.getSelectedIndex()]);
                    proPrice.setText(String.format("Price: $%.2f", Double.parseDouble(reader.readLine())));
                    proDescription.setText("Description: " + reader.readLine());
                    proQuantity.setText("Quantity: " + reader.readLine());

                } catch (Exception ez) {
                    ez.printStackTrace();
                }

                products.setVisible(true);
                stores.setVisible(false);
            }

            if (e.getSource() == searchBtn) { //button 12
                writer.write("12");
                writer.println();
                writer.flush();

                if (!searchBox.getText().replaceAll(" ", "").equals("")) {
                    writer.write(searchBox.getText());
                    writer.println();
                    writer.flush();
                    String result = "";
                    try {
                        result = reader.readLine();
                    } catch (Exception er) {
                        er.printStackTrace();
                    }

                    if (!result.equals("")) {
                        if (result.contains(","))
                            marketPlace = result.split(",");
                        else
                            marketPlace =  new String[]{result};
                        marketSelect.setModel(new DefaultComboBoxModel<String>(marketPlace));
                    } else {
                        JOptionPane.showMessageDialog(null, "No Results", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Enter a search", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }

            if (e.getSource() == allProBtn) { //sets the marketplace back to all products. 13
                writer.write("13");
                writer.println();
                writer.flush();

                String list = "";
                try {
                    list = reader.readLine();
                } catch (Exception ea) {
                    ea.printStackTrace();
                }

                if (!list.equals("")) {
                    if (list.contains(","))
                        marketPlace = list.split(",");
                    else
                        marketPlace = new String[] {list};
                    marketSelect.setModel(new DefaultComboBoxModel<String>(marketPlace));

                } else {
                    JOptionPane.showMessageDialog(null,
                            "There are currently no products in the marketplace.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }

            if (e.getSource() == customerViewProPage) { // 14 case for customer pressing button to see product -> own panel?
                writer.write("14");
                writer.println();
                writer.flush();

                writer.write(productList[productsDropdown.getSelectedIndex()]);
                writer.println();
                writer.flush();

                writer.write(storeList[storesDropdown.getSelectedIndex()]);
                writer.println();
                writer.flush();

            }

            if (e.getSource() == backButtonEdit) {
                stores.setVisible(true);
                products.setVisible(false);
            }
            if (e.getSource() == productButtonEdit) { // 15
                writer.write("15");
                writer.println();
                writer.flush();
                writer.write(productNameEdit.getText());
                writer.println();
                writer.flush();
                writer.write(productDescriptionEdit.getText());
                writer.println();
                writer.flush();
                writer.write(productPriceEdit.getText());
                writer.println();
                writer.flush();
                writer.write(productQuantityEdit.getText());
                writer.println();
                writer.flush();
                writer.write(productList[productsDropdown.getSelectedIndex()]);
                writer.println();
                writer.flush();
                writer.write(storeList[storesDropdown.getSelectedIndex()]);
                writer.println();
                writer.flush();
                try {
                    String see = reader.readLine();
                    if (see.equals("ok")) {
                        productList[productsDropdown.getSelectedIndex()] = productNameEdit.getText();
                        productsDropdown.setModel(new DefaultComboBoxModel<String>(productList));
                        stores.setVisible(true);
                        products.setVisible(false);
                    } else if (see.equals("error")){
                        JOptionPane.showMessageDialog(null,
                                "Enter valid values for price and quantity.", "error",
                                JOptionPane.ERROR_MESSAGE);
                    } else if (see.equals("repeat")) {
                        JOptionPane.showMessageDialog(null,
                                "a product with this name already exists in your store.", "error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ez) {
                    //ez.printStackTrace
                }
            }

            if (e.getSource() == accountCustomer) {
                editCustomer.setVisible(true);
                customer.setVisible(false);
            }

            //NEEDS OTHER IMPLEMENTATION
            if (e.getSource() == logoutButtonEdit) {
                login.setVisible(true);
                products.setVisible(false);
            }
            if (e.getSource() == backCustomer) {
                customer.setVisible(true);
                editCustomer.setVisible(false);
            }
            if (e.getSource() == deleteProduct) { //16
                writer.write("16");
                writer.println();
                writer.flush();
                writer.write(productList[productsDropdown.getSelectedIndex()]);
                writer.println();
                writer.flush();
                writer.write(storeList[storesDropdown.getSelectedIndex()]);
                writer.println();
                writer.flush();
                try {
                    productList = reader.readLine().split(",");
                    productsDropdown.setModel(new DefaultComboBoxModel<String>(productList));
                    if (productList.length == 0 || productList[0].equals("")) {
                        selectProductButton.setVisible(false);
                    }
                } catch (Exception ez) {
                    //ez.printStackTrace();
                }
                stores.setVisible(true);
                products.setVisible(false);
            }
            if (e.getSource() == sellerDashboard) {
                sellerDashboardFrame.setVisible(true);
                seller.setVisible(false);
            }
            if (e.getSource() == customerDashboard) {
                customerDashboardFrame.setVisible(true);
                customer.setVisible(false);
            }
            if (e.getSource() == backButtonCustomerDash) {
                customer.setVisible(true);
                customerDashboardFrame.setVisible(false);
            }
            if (e.getSource() == backButtonSellerDash) {
                seller.setVisible(true);
                sellerDashboardFrame.setVisible(false);
            }
            //NEEDS OTHER IMPLEMENTATION
            if (e.getSource() == logoutButtonSellerDash) {
                login.setVisible(true);
                sellerDashboardFrame.setVisible(false);
            }
            //NEEDS OTHER IMPLEMENTATION
            if (e.getSource() == logoutButtonCustomerDash) {
                login.setVisible(true);
                customerDashboardFrame.setVisible(false);
            }
            // button 17, sort by general popularity on customer dash
            if (e.getSource() == sortByGenPop) {
                writer.write("17");
                writer.println();
                writer.flush();
                try {
                    String storeSortedByProductsSold = reader.readLine();
                    String[] sortedStoresByTotalSoldArray = storeSortedByProductsSold.split(",");
                    customerDash.setModel(new DefaultComboBoxModel<String>(sortedStoresByTotalSoldArray));
                } catch (IOException ex) {
                    //ex.printStackTrace();
                }
            }
            // button 18, sort by customer's favorite
            if (e.getSource() == sortByUserPop) {
                writer.write("18");
                writer.println();
                writer.flush();
                String storeSortedByCustomerFavorite = null;
                try {
                    storeSortedByCustomerFavorite = reader.readLine();
                    String[] sortedStoresByCustomerFavorite = storeSortedByCustomerFavorite.split(",");
                    customerDash.setModel(new DefaultComboBoxModel<String>(sortedStoresByCustomerFavorite));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
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
        makeProduct = new JFrame("Create Product");
        Container makeProductContent = makeProduct.getContentPane();
        makeProductContent.setLayout(new BorderLayout());
        sellerDashboardFrame = new JFrame("Seller Dashboard");
        Container sellerDashboardContent = sellerDashboardFrame.getContentPane();
        sellerDashboardContent.setLayout(new BorderLayout());
        customerDashboardFrame = new JFrame("Customer Dashboard");
        Container customerDashboardContent = customerDashboardFrame.getContentPane();
        customerDashboardContent.setLayout(new BorderLayout());



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

        makeProduct.setSize(1000, 600);
        makeProduct.setLocationRelativeTo(null);
        makeProduct.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        makeProduct.setVisible(false);

        sellerDashboardFrame.setSize(1000, 600);
        sellerDashboardFrame.setLocationRelativeTo(null);
        sellerDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sellerDashboardFrame.setVisible(false);

        customerDashboardFrame.setSize(1000, 600);
        customerDashboardFrame.setLocationRelativeTo(null);
        customerDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        customerDashboardFrame.setVisible(false);


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

        addStore = new JButton("Add Store");
        addStore.setBounds(550, 20, 150, 30);
        addStore.addActionListener(actionListener);
        buttonPanelSeller.add(addStore);

        sellerDashboard = new JButton("View Dashboard");
        sellerDashboard.setBounds(700, 20, 150, 30);
        sellerDashboard.addActionListener(actionListener);
        buttonPanelSeller.add(sellerDashboard);

        storesDropdown = new JComboBox<String>();
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
        cartButton.setBounds(550, 20, 150, 30);
        cartButton.addActionListener(actionListener);
        buttonPanelCustomer.add(cartButton);

        customerDashboard = new JButton("View Dashboard");
        customerDashboard.setBounds(700, 20, 150, 30);
        customerDashboard.addActionListener(actionListener);
        buttonPanelCustomer.add(customerDashboard);

        logoutButtonCustomer = new JButton("Logout");
        logoutButtonCustomer.setBounds(50, 20, 150, 30);
        logoutButtonCustomer.addActionListener(actionListener);
        buttonPanelCustomer.add(logoutButtonCustomer);

        searchBox = new JTextField("Enter a Search");
        searchBox.setBounds(400, 290, 200, 30);
        searchBox.addActionListener(actionListener);
        buttonPanelCustomer.add(searchBox);

        searchBtn = new JButton("search");
        searchBtn.setBounds(200, 350, 200, 30);
        searchBtn.addActionListener(actionListener);
        buttonPanelCustomer.add(searchBtn);

        allProBtn = new JButton("View All");
        allProBtn.setBounds(400, 350, 200, 30);
        allProBtn.addActionListener(actionListener);
        buttonPanelCustomer.add(allProBtn);

        customerViewProPage = new JButton("Select Product");
        customerViewProPage.setBounds(600, 350, 200, 30);
        customerViewProPage.addActionListener(actionListener);
        buttonPanelCustomer.add(customerViewProPage);

        marketSelect = new JComboBox<String>();
        int temp = customer.getWidth() / 2;
        marketSelect.setBounds(temp - 350, 250, 700, 30);
        buttonPanelCustomer.add(marketSelect);

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

        backCustomer = new JButton("Back");
        backCustomer.setBounds(50, 475, 350, 60);
        backCustomer.addActionListener(actionListener);
        buttonPanelEditCustomer.add(backCustomer);

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

        productsDropdown = new JComboBox<String>(storeList);
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

        //Creating buttons and panels for creating a new product page
        JPanel buttonPanelNewProduct = new JPanel();
        buttonPanelNewProduct.setLayout(null);

        productName = new JTextField("Enter Product Name: ");
        productName.setBounds(400, 200, 200, 30);
        productName.addActionListener(actionListener);
        buttonPanelNewProduct.add(productName);

        productDescription = new JTextField("Enter Product Description: ");
        productDescription.setBounds(400, 250, 200, 30);
        productDescription.addActionListener(actionListener);
        buttonPanelNewProduct.add(productDescription);

        productPrice = new JTextField("Enter Product Price: ");
        productPrice.setBounds(400, 300, 200, 30);
        productPrice.addActionListener(actionListener);
        buttonPanelNewProduct.add(productPrice);

        productQuantity = new JTextField("Enter Product Quantity: ");
        productQuantity.setBounds(400, 350, 200, 30);
        productQuantity.addActionListener(actionListener);
        buttonPanelNewProduct.add(productQuantity);

        newProductBackButton = new JButton("Back");
        newProductBackButton.setBounds(50, 475, 350, 60);
        newProductBackButton.addActionListener(actionListener);
        buttonPanelNewProduct.add(newProductBackButton);

        createNewProductButton = new JButton("Create Product");
        createNewProductButton.setBounds(600, 475, 350, 60);
        createNewProductButton.addActionListener(actionListener);
        buttonPanelNewProduct.add(createNewProductButton);

        //frame for editing products, seller
        JPanel buttonPanelEditProduct = new JPanel();
        buttonPanelEditProduct.setLayout(null);

        productNameEdit = new JTextField("Enter New Product Name: ");
        productNameEdit.setBounds(400, 200, 200, 30);
        productNameEdit.addActionListener(actionListener);
        buttonPanelEditProduct.add(productNameEdit);

        productDescriptionEdit = new JTextField("Enter New Product Description: ");
        productDescriptionEdit.setBounds(400, 250, 200, 30);
        productDescriptionEdit.addActionListener(actionListener);
        buttonPanelEditProduct.add(productDescriptionEdit);

        productPriceEdit = new JTextField("Enter New Product Price: ");
        productPriceEdit.setBounds(400, 300, 200, 30);
        productPriceEdit.addActionListener(actionListener);
        buttonPanelEditProduct.add(productPriceEdit);

        productQuantityEdit = new JTextField("Enter New Product Quantity: ");
        productQuantityEdit.setBounds(400, 350, 200, 30);
        productQuantityEdit.addActionListener(actionListener);
        buttonPanelEditProduct.add(productQuantityEdit);

        backButtonEdit = new JButton("Back");
        backButtonEdit.setBounds(50, 475, 350, 60);
        backButtonEdit.addActionListener(actionListener);
        buttonPanelEditProduct.add(backButtonEdit);

        productButtonEdit = new JButton("Edit Product");
        productButtonEdit.setBounds(600, 475, 350, 60);
        productButtonEdit.addActionListener(actionListener);
        buttonPanelEditProduct.add(productButtonEdit);

        deleteProduct = new JButton("Delete Product");
        deleteProduct.setBounds(800, 20, 150, 30);;
        deleteProduct.addActionListener(actionListener);
        buttonPanelEditProduct.add(deleteProduct);

        logoutButtonEdit = new JButton("Logout");
        logoutButtonEdit.setBounds(50, 20, 150, 30);
        logoutButtonEdit.addActionListener(actionListener);
        buttonPanelEditProduct.add(logoutButtonEdit);

        proDescription = new JLabel();
        proPrice = new JLabel();
        proName = new JLabel();
        proStore = new JLabel();
        proQuantity = new JLabel();
        proDescription.setBounds(20, 125, 450, 20);
        proPrice.setBounds(20, 175, 450, 20);
        proName.setBounds(20, 100, 450, 20);
        proStore.setBounds(20, 150, 450, 20);
        proQuantity.setBounds(20, 200, 450, 20);
        buttonPanelEditProduct.add(proDescription);
        buttonPanelEditProduct.add(proPrice);
        buttonPanelEditProduct.add(proName);
        buttonPanelEditProduct.add(proStore);
        buttonPanelEditProduct.add(proQuantity);

        //Creating buttons and panels for cart page (For this I don't know exactly what is needed I may need help)
        //remove, remove all, purchase all, dropdown for items


        //Create buttons and panels for product page for customers
        //add to cart, back


        //create a seller dashboard frame and the buttons and panels
        //sortbyrevenue, sortbysales,
        JPanel buttonPanelSellerDashboard = new JPanel();
        buttonPanelSellerDashboard.setLayout(null);

        backButtonSellerDash = new JButton("Back");
        backButtonSellerDash.setBounds(50, 475, 350, 60);
        backButtonSellerDash.addActionListener(actionListener);
        buttonPanelSellerDashboard.add(backButtonSellerDash);

        logoutButtonSellerDash = new JButton("Logout");
        logoutButtonSellerDash.setBounds(50, 20, 150, 30);
        logoutButtonSellerDash.addActionListener(actionListener);
        buttonPanelSellerDashboard.add(logoutButtonSellerDash);

        sellerDash = new JComboBox<>();
        sellerDash.setBounds(400, 250, 200, 30);
        buttonPanelSellerDashboard.add(sellerDash);

        sortByRevenue = new JButton("Sort By Revenue");
        sortByRevenue.setBounds(225,300,250,30);
        sortByRevenue.addActionListener(actionListener);
        buttonPanelSellerDashboard.add(sortByRevenue);

        sortBySales = new JButton("Sort By Sales");
        sortBySales.setBounds(525,300,250,30);
        sortBySales.addActionListener(actionListener);
        buttonPanelSellerDashboard.add(sortBySales);

        //create dashboard for customers
        //sortbyownpop, sortbygenpop
        JPanel buttonPanelCustomerDashboard = new JPanel();
        buttonPanelCustomerDashboard.setLayout(null);

        backButtonCustomerDash = new JButton("Back");
        backButtonCustomerDash.setBounds(50, 475, 350, 60);
        backButtonCustomerDash.addActionListener(actionListener);
        buttonPanelCustomerDashboard.add(backButtonCustomerDash);

        logoutButtonCustomerDash = new JButton("Logout");
        logoutButtonCustomerDash.setBounds(50, 20, 150, 30);
        logoutButtonCustomerDash.addActionListener(actionListener);
        buttonPanelCustomerDashboard.add(logoutButtonCustomerDash);

        customerDash = new JComboBox<>();
        customerDash.setBounds(400, 250, 200, 30);
        buttonPanelCustomerDashboard.add(customerDash);

        sortByUserPop = new JButton("Sort By Your # of Purchases");
        sortByUserPop.setBounds(225,300,250,30);
        sortByUserPop.addActionListener(actionListener);
        buttonPanelCustomerDashboard.add(sortByUserPop);

        sortByGenPop = new JButton("Sort By General Popularity");
        sortByGenPop.setBounds(525,300,250,30);
        sortByGenPop.addActionListener(actionListener);
        buttonPanelCustomerDashboard.add(sortByGenPop);


        productsContent.add(buttonPanelEditProduct);
        accountContentSeller.add(buttonPanelEditSeller);
        loginContent.add(buttonPanelLogin);
        createAccountContent.add(buttonPanelCreate);
        sellerContent.add(buttonPanelSeller);
        customerContent.add(buttonPanelCustomer);
        storesContent.add(buttonPanelStore);
        accountContentCustomer.add(buttonPanelEditCustomer);
        makeProductContent.add(buttonPanelNewProduct);
        customerDashboardContent.add(buttonPanelCustomerDashboard);
        sellerDashboardContent.add(buttonPanelSellerDashboard);
    }


}
