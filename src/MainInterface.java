import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A marketplace that allows for multiple users to connect, uses GUI, and is fully run on servers and sockets
 *
 * @author Aaryamik Gupta, Paige Lorenz, Dalton Babbs, Mahika Parakh, Matthew Wind
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
    //JButton exitButtonSeller;
    //JButton exitButtonCustomer;
    JTextField editUsernameSeller;
    JTextField editPasswordSeller;
    JTextField editEmailSeller;
    JButton deleteAccountSeller;
    JButton confirmEditSeller;
    JButton backSeller;
    JButton backStore;
    JTextField editUsernameCustomer;
    JTextField editPasswordCustomer;
    JTextField editEmailCustomer;
    JButton deleteAccountCustomer;
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

    //Buttons and frame for customerViewProduct frame
    JFrame customerViewProductFrame;
    JButton customerViewProdouctBack;
    JButton customerViewProdouctBackForCart;
    JButton addToCart;
    JTextField customerViewProductHowMany;
    JLabel customerViewProductHowManyLabel;
    JLabel customerViewProductName;
    JLabel customerViewProductStore;
    JLabel customerViewProductDescription;
    JLabel customerViewProductPrice;
    JLabel customerViewProductStock;

    JFrame viewPurchaseHistory;
    JButton historyBack;
    JComboBox<String> history;

    JFrame viewProductsInCartsFrame;
    JButton inCartsBack;
    JComboBox<String> items;

    // Buttons and frame for view cart
    JFrame viewCartFrame;
    JButton buyCart;
    JButton removeFromCart;
    JButton cartViewItem;
    JButton removeAllFromCart;
    JButton cartBackButton;
    JComboBox<String> itemsInCartDropdown;
    JLabel emptyCartLabel;
    String[] cartList = new String[0];
    JLabel directionLabel;

    // Buttons for export and imports csv files
    JButton exportPurchaseHistory;
    JButton viewProductsInCarts;
    JButton importProducts;
    JButton exportProductsFromAStore;



    String[] storeList = new String[0];
    String[] productList = new String[0];
    String[] marketPlace = new String[0];


    JLabel proName;
    JLabel proPrice;
    JLabel proQuantity;
    JLabel proStore;
    JLabel proDescription;
    JButton historyBtn;

    JButton deleteProduct;

    JFrame viewCustomersandProducts;
    JComboBox<String> viewCustomers;
    JComboBox<String> viewProducts;
    JButton backViewCustomersandProducts;
    JButton moreInfo;

    JLabel proLabel;
    JLabel cusLabel;
    JButton sortProductsByPrice;
    JButton sortProductsByQuantity;



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

                createEmail.setText("Enter Email:");
                createUsername.setText("Enter Username:");
                createPassword.setText("Enter Password:");
                createSecurityAnswer.setText("Enter Answer:");
                chooseSecurityQ.setSelectedIndex(0);
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
                newCustomerInfo += createUsername.getText() + "," + createEmail.getText() + "," +
                        createPassword.getText() + ",true" + ","  + createSecurityAnswer.getText() + ","  +
                        chooseSecurityQ.getSelectedIndex();

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

                    try {
                        String productList = reader.readLine();
                        if (!productList.equals("")) {
                            marketPlace = productList.split(",");
                            marketSelect.setModel(new DefaultComboBoxModel<String>(marketPlace));
                            marketSelect.setVisible(true);
                            searchBox.setVisible(true);
                            searchBtn.setVisible(true);
                            sortProductsByPrice.setVisible(true);
                            sortProductsByQuantity.setVisible(true);
                            customerViewProPage.setVisible(true);
                            allProBtn.setVisible(true);
                        } else {
                            searchBtn.setVisible(false);
                            sortProductsByPrice.setVisible(false);
                            sortProductsByQuantity.setVisible(false);
                            marketSelect.setVisible(false);
                            searchBox.setVisible(false);
                            customerViewProPage.setVisible(false);
                            allProBtn.setVisible(false);
                            JOptionPane.showMessageDialog(null,
                                    "There are currently no products in the marketplace.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        //ex.printStackTrace();
                    }
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
                                //System.out.println(line);
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
                                            "There are no stores. add stores", "error",
                                            JOptionPane.ERROR_MESSAGE);
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
                                "this email is registered as a customer.", "error",
                                JOptionPane.ERROR_MESSAGE);
                    } else if (result.equals("noLog")) {
                        JOptionPane.showMessageDialog(null,
                                "this email is not registered! create a new account.", "error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception h) {
                    h.printStackTrace();
                }

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
                                sortProductsByPrice.setVisible(true);
                                sortProductsByQuantity.setVisible(true);
                                customerViewProPage.setVisible(true);
                                allProBtn.setVisible(true);
                            } else {
                                searchBtn.setVisible(false);
                                sortProductsByPrice.setVisible(false);
                                sortProductsByQuantity.setVisible(false);
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
                                "this email is registered as a seller.", "error",
                                JOptionPane.ERROR_MESSAGE);
                    } else if (result.equals("noLog")) {
                        JOptionPane.showMessageDialog(null,
                                "this email is not registered! create a new account.", "error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception h) {
                    h.printStackTrace();
                }
            }

            // resets account creation text fields after an account is created
            if (e.getSource() == logoutButtonCustomer || e.getSource() == logoutButtonSeller ||
                    e.getSource() == logoutButtonStore || e.getSource() == logoutButtonEdit) {
                createEmail.setText("Enter Email");
                createUsername.setText("Enter Username");
                createPassword.setText("Enter Password");
                chooseSecurityQ.setSelectedIndex(0);
                createSecurityAnswer.setText("Enter Answer");
                email.setText("Enter Email");
                password.setText("Enter Password");
            }
            //logout button for seller frame sends to login screen
            //NEEDS OTHER IMPLEMENTATION
            if (e.getSource() == logoutButtonSeller) {
                login.setVisible(true);
                seller.setVisible(false);
            }

            //logs customer out sends to login screen
            //NEEDS OTHER IMPLEMENTATION
            if (e.getSource() == logoutButtonCustomer) {
                login.setVisible(true);
                customer.setVisible(false);
                storeList = new String[0];
            }
            //takes customer to account screen

            if (e.getSource() == accountSeller) {
                editSeller.setVisible(true);
                seller.setVisible(false);
            }

            //takes user back to seller screen
            if (e.getSource() == backSeller) {
                refreshSellerPage(writer, reader);
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
                refreshSellerPage(writer, reader);
                seller.setVisible(true);
                editSeller.setVisible(false);
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
                refreshCustomerPage(writer, reader);
                customer.setVisible(true);
                editCustomer.setVisible(false);
            }


            //takes user to stores screen and needs to change values of dropdowns in store settings
            //NEEDS OTHER IMPLEMENTATION 7
            if (e.getSource() == selectStoreButton) {

                writer.write("7");
                writer.println();
                writer.flush();

                //System.out.println(storeList[storesDropdown.getSelectedIndex()]);
                writer.write(storeList[storesDropdown.getSelectedIndex()]);
                writer.println();
                writer.flush();

                try {
                    //takes the store names separated by commas
                    String line = reader.readLine();
                    //System.out.println(line);
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

                String newStoreName = JOptionPane.showInputDialog(null, "Enter the Store Name: ",
                        "Create Store", JOptionPane.QUESTION_MESSAGE);
                if (newStoreName == null)
                {
                    writer.write("x button");
                    writer.println();
                    writer.flush();
                } else {
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
                        JOptionPane.showMessageDialog(null,
                                "A store under that name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Store Created!", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        //takes the store names separated by commas

                        storeList = serverAnswer.split(",");
                        storesDropdown.setVisible(true);
                        selectStoreButton.setVisible(true);
                        storesDropdown.setModel(new DefaultComboBoxModel<String>(storeList));

                    }
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
                JOptionPane.showMessageDialog(null, "Store Removed.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                storeList = serverResponse.split(",");
                ArrayList<String> storeArrayList = new ArrayList<>();
                //System.out.println("storeList: ");
                for(String s: storeList) {
                    //System.out.println(s);
                    storeArrayList.add(s);
                }
                refreshSellerPage(writer, reader);
                seller.setVisible(true);
                stores.setVisible(false);
                storesDropdown.setModel(new DefaultComboBoxModel<String>(storeList));
                storesDropdown.setVisible(true);
                selectStoreButton.setVisible(true);
            }

            if (e.getSource() == newProductBackButton) {
                refreshStoresPage(writer, reader);
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
                    JOptionPane.showMessageDialog(null, "The product name must not be empty or " +
                                    "contain the characters" + " [\",\" \"~\" \"-\"]", "Name Error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (serverResponse.equals("description error")) {
                    JOptionPane.showMessageDialog(null, "The product name must not be empty or " +
                                    "contain the characters " + "[\",\" \"~\"]", "Description Error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (serverResponse.equals("price error")) {
                    JOptionPane.showMessageDialog(null, "The price must be a number greater" +
                            " than 0.00", "Price Error", JOptionPane.ERROR_MESSAGE);
                } else if (serverResponse.equals("quantity error")) {
                    JOptionPane.showMessageDialog(null, "The quantity must be a number " +
                            "greater than 0", "Quantity Error", JOptionPane.ERROR_MESSAGE);
                } else {

                    //takes the product names separated by commas
                    if (serverResponse.equals("duplicate")) {
                        JOptionPane.showMessageDialog(null, "Product already exists in store!",
                                "Add Product", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Product Created!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        productList = serverResponse.split(",");

                        productsDropdown.setVisible(true);
                        selectProductButton.setVisible(true);
                        productsDropdown.setModel(new DefaultComboBoxModel<String>(productList));

                    }
                    productName.setText("Enter Product Name:");
                    productDescription.setText("Enter Product Description:");
                    productPrice.setText("Enter Product Price:");
                    productQuantity.setText("Enter Product Quantity:");
                }
            }


            //takes user back to seller homepage
            if (e.getSource() == backStore) {
                refreshSellerPage(writer, reader);
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

            if (e.getSource() == customerViewProPage) { // sends customer to product page
                // view viewProductPage method for details
                boolean successful = viewProductPage(writer, reader);

                if (successful) {
                    customerViewProdouctBackForCart.setVisible(false);
                    customerViewProdouctBack.setVisible(true);
                    customerViewProductHowMany.setVisible(true);
                    customerViewProductHowManyLabel.setVisible(true);
                    addToCart.setVisible(true);
                    customerViewProductFrame.setVisible(true);
                    customer.setVisible(false);
                }

            }

            if (e.getSource() == customerViewProdouctBack)
            {
                customerViewProductFrame.setVisible(false);
                refreshCustomerPage(writer, reader);
                customer.setVisible(true);
            }

            if (e.getSource() == backButtonEdit) {
                refreshStoresPage(writer, reader);
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
                refreshCustomerPage(writer, reader);
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
                directionLabel.setVisible(true);
                sellerDashboardFrame.setVisible(true);
                sellerDash.setModel(new DefaultComboBoxModel<String>());
                seller.setVisible(false);
            }
            if (e.getSource() == customerDashboard) {
                customerDashboardFrame.setVisible(true);
                customer.setVisible(false);
            }
            if (e.getSource() == backButtonCustomerDash) {
                customer.setVisible(true);
                refreshCustomerPage(writer, reader);
                customerDashboardFrame.setVisible(false);
            }
            if (e.getSource() == backButtonSellerDash) {
                refreshSellerPage(writer, reader);
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

            if (e.getSource() == addToCart) // button 19, adds a product to cart
            {
                writer.write("19");
                writer.println();
                writer.flush();

                String productName = customerViewProductName.getText().substring(9);
                String productStore = customerViewProductStore.getText().substring(7);

                writer.write(productName + "," + productStore + "," + customerViewProductHowMany.getText());
                writer.println();
                writer.flush();


                try {
                    String serverResponse = reader.readLine();


                    if (serverResponse.equals("added to cart"))
                    {
                        JOptionPane.showMessageDialog(null, "Product Added!", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        refreshCustomerPage(writer, reader);
                        customer.setVisible(true);
                        customerViewProductFrame.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "Quantity Error. Decrease the" +
                                        " quantity desired until it is " + "less than the quantity available.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


            }


            if (e.getSource() == sortBySales) {
                directionLabel.setVisible(false);
                writer.write("20");
                writer.println();
                writer.flush();
                String sortedStores;
                try {
                    sortedStores = reader.readLine();
                    String[] stores = sortedStores.split(",");
                    sellerDash.setModel(new DefaultComboBoxModel<String>(stores));
                    if (stores[0].equals("")) {
                        JOptionPane.showMessageDialog(null, "you have no stores.", "error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    //System.out.println("Error sorting by sales");
                }

            }
            if (e.getSource() == sortByRevenue) {
                directionLabel.setVisible(false);
                writer.write("21");
                writer.println();
                writer.flush();

                try{
                    String sortedStores = reader.readLine();
                    String[] stores = sortedStores.split(",");
                    sellerDash.setModel(new DefaultComboBoxModel<String>(stores));
                    if (stores[0].equals("")) {
                        JOptionPane.showMessageDialog(null, "you have no stores.", "error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch(IOException ioException) {
                    ioException.printStackTrace();
                    //System.out.println("Error sorting by revenue");
                }

            }

            // button 22 takes customer to their cart, look at resetViewCart method for implementation details
            if (e.getSource() == cartButton)
            {
                resetViewCart(writer, reader);
            }

            if (e.getSource() == cartBackButton)
            {
                viewCartFrame.setVisible(false);
                refreshCustomerPage(writer, reader);
                customer.setVisible(true);
            }

            // button 23 purchases all products in cart
            if (e.getSource() == buyCart)
            {
                writer.write("23");
                writer.println();
                writer.flush();

                String failedProducts;

                try {
                    failedProducts = reader.readLine();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                if (failedProducts.equals(""))
                {
                    JOptionPane.showMessageDialog(null, "All Products Purchased!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String[] failedProductNames = failedProducts.split(",");
                    JOptionPane.showInputDialog(null, "These items could not be added to the " +
                                    "cart because they were " + "out of stock.", "Failed Purchases",
                            JOptionPane.ERROR_MESSAGE,
                            null, failedProductNames, null);
                }

                resetViewCart(writer, reader);
            }

            if (e.getSource() == removeFromCart)
            {
                writer.write("24");
                writer.println();
                writer.flush();


                writer.write(cartList[itemsInCartDropdown.getSelectedIndex()]);
                writer.println();
                writer.flush();

                JOptionPane.showMessageDialog(null, "Product Removed!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                resetViewCart(writer, reader);
            }

            if (e.getSource() == removeAllFromCart)
            {
                writer.write("25");
                writer.println();
                writer.flush();

                JOptionPane.showMessageDialog(null, "Cart Cleared!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                resetViewCart(writer, reader);
            }

            if (e.getSource() == cartViewItem)
            {
                viewProductPage(writer, reader);


                customerViewProdouctBackForCart.setVisible(true);
                customerViewProdouctBack.setVisible(false);
                customerViewProductHowMany.setVisible(false);
                customerViewProductHowManyLabel.setVisible(false);
                addToCart.setVisible(false);
                customerViewProductFrame.setVisible(true);
                viewCartFrame.setVisible(false);
            }

            if (e.getSource() == customerViewProdouctBackForCart)
            {
                customerViewProductFrame.setVisible(false);
                viewCartFrame.setVisible(true);
                resetViewCart(writer, reader);
            }

            if (e.getSource() == historyBtn) {//27
                refreshCustomerPage(writer, reader);
                writer.write("27");
                writer.println();
                writer.flush();
                String h;
                try {
                    h = reader.readLine();
                    if (!h.equals("none")) {
                        String[] sp = h.split(",");
                        String[] sp2 = new String[sp.length];
                        String[] sp3;
                        for (int i = 0; i < sp.length; i++) {
                            sp3 = sp[i].split("-");
                            sp2[i] = ("Store: " + sp3[0] + "     Product: " + sp3[1]);
                        }
                        history.setModel(new DefaultComboBoxModel<String>(sp2));
                        customer.setVisible(false);
                        viewPurchaseHistory.setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(null,
                                "You have no purchase History to view!", "error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ez) {
                    //ez.printStackTrace();
                }

            }

            if (e.getSource() == historyBack) {
                refreshCustomerPage(writer, reader);
                customer.setVisible(true);
                viewPurchaseHistory.setVisible(false);
            }

            // button 26, export customer purchase history
            if (e.getSource() == exportPurchaseHistory) {
                writer.write("26");
                writer.println();
                writer.flush();
                try {
                    String customerPurchaseHistory = reader.readLine();
                    if (customerPurchaseHistory.equals("empty")) {
                        JOptionPane.showMessageDialog(null,
                                "You have no purchase history to export!", "Export Purchase History",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        String customerEmail = reader.readLine();
                        PrintWriter pw = new PrintWriter(customerEmail + "PurchaseHistory.txt");
                        String[] pHistoryData = customerPurchaseHistory.split("~");
                        for (int i = 0; i < pHistoryData.length; i++) {
                            pw.println(pHistoryData[i]);
                            pw.flush();
                        }
                        pw.close();
                        JOptionPane.showMessageDialog(null,
                                "Purchase history exported successfully!", "Export Purchase History",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error exporting purchase history!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            if (e.getSource() == viewProductsInCarts) {//28
                writer.write("28");
                writer.println();
                writer.flush();
                try {
                    String s = reader.readLine();
                    if (!s.equals("none")) {
                        items.setModel(new DefaultComboBoxModel<String>(s.split(",")));
                        viewProductsInCartsFrame.setVisible(true);
                        seller.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "No products in carts", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ez) {
                    //ez.printStackTrace();
                }

            }
            if (e.getSource() == inCartsBack) {
                viewProductsInCartsFrame.setVisible(false);
                refreshSellerPage(writer, reader);
                seller.setVisible(true);
            }

            // button 29, import products to stores from a file
            if (e.getSource() == importProducts) {
                StringBuilder productImportFileData = new StringBuilder();
                String fileName = JOptionPane.showInputDialog(null,
                        "Enter the filename you wish to import from");
                if (fileName.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "File name cannot be empty!", "Import Products", JOptionPane.ERROR_MESSAGE);
                } else {
                    File f = new File(fileName);
                    if (f.exists()) {
                        ArrayList<String> productImportFileLines = Seller.readCSVProductFile(fileName);
                        assert productImportFileLines != null;
                        if (productImportFileLines.size() == 0) {
                            JOptionPane.showMessageDialog(null,
                                    "File is empty!", "Import Products", JOptionPane.ERROR_MESSAGE);
                        } else {
                            writer.write("29");
                            writer.println();
                            writer.flush();
                            for (int i = 0; i < productImportFileLines.size(); i++) {
                                if (i == productImportFileLines.size() - 1) {
                                    productImportFileData.append(productImportFileLines.get(i));
                                } else {
                                    productImportFileData.append(productImportFileLines.get(i)).append("~");
                                }
                            }
                            writer.write(String.valueOf(productImportFileData));
                            writer.println();
                            writer.flush();
                            JOptionPane.showMessageDialog(null,
                                    "New products imported successfully!",
                                    "Import Products", JOptionPane.INFORMATION_MESSAGE);
                            selectProductButton.setVisible(true);
                        }

                    } else {
                        JOptionPane.showMessageDialog(null,
                                "File does not exist!", "Import Products", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            // button 30, export products from a store to a file
            if (e.getSource() == exportProductsFromAStore) {
                writer.write("30");
                writer.println();
                writer.flush();

                String exportedStore = storesDropdown.getItemAt(storesDropdown.getSelectedIndex());
                writer.write(exportedStore);
                writer.println();
                writer.flush();

                try {
                    String processedStoreData = reader.readLine();
                    if (processedStoreData.isEmpty()) {
                        JOptionPane.showMessageDialog(null,
                                "No products to export!", "Export Products", JOptionPane.ERROR_MESSAGE);
                    } else {
                        String[] splitStoreProducts = processedStoreData.split("~");
                        PrintWriter pw =
                                new PrintWriter(new FileWriter(exportedStore + "ExportedProducts.txt"));
                        for (int i = 0; i < splitStoreProducts.length; i++) {
                            pw.println(splitStoreProducts[i]);
                        }
                        pw.flush();
                        pw.close();
                        JOptionPane.showMessageDialog(null,
                                "Export finished!", "Export Products", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,
                            "An error has occurred", "Export Products", JOptionPane.ERROR_MESSAGE);
                }
            }

            if (e.getSource() == deleteAccountSeller) {
                writer.write("34");
                writer.println();
                writer.flush();

                JOptionPane.showMessageDialog(null, "Account Deleted", "Success", JOptionPane.INFORMATION_MESSAGE);

                login.setVisible(true);
                editSeller.setVisible(false);

                email.setText("Enter Email:");
                password.setText("Enter Password:");
            }

            if (e.getSource() == deleteAccountCustomer) {
                writer.write("34");
                writer.println();
                writer.flush();

                JOptionPane.showMessageDialog(null, "Account Deleted", "Success", JOptionPane.INFORMATION_MESSAGE);

                login.setVisible(true);
                editCustomer.setVisible(false);

                email.setText("Enter Email:");
                password.setText("Enter Password:");
            }

            if (e.getSource() == backViewCustomersandProducts) {
                sellerDashboardFrame.setVisible(true);
                viewProductsInCartsFrame.setVisible(false);
            }

            if (e.getSource() == moreInfo) {
                if (sellerDash.getSelectedItem() != null && !sellerDash.getSelectedItem().toString().equals("")) {
                    writer.write("35");
                    writer.println();
                    writer.flush();

                    writer.write(Objects.requireNonNull(sellerDash.getSelectedItem()).toString());
                    writer.println();
                    writer.flush();

                    try {
                        String customerList = reader.readLine();
                        String proList = reader.readLine();
                        viewCustomers.setModel(new DefaultComboBoxModel<String>(customerList.split(",")));
                        viewProducts.setModel(new DefaultComboBoxModel<String>(proList.split(",")));

                        if (customerList.split(",")[0].equals("")) {
                            cusLabel.setText("Customers: no customers");
                        } else {
                            cusLabel.setText("Customers: ");
                        }
                        if (proList.split(",")[0].equals("")) {
                            proLabel.setText("Products: no customers");
                        } else {
                            proLabel.setText("Products: ");
                        }


                        viewCustomersandProducts.setVisible(true);
                        sellerDashboardFrame.setVisible(false);
                    } catch (Exception ez) {
                        ez.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No Store to view", "error", JOptionPane.ERROR_MESSAGE);
                }
            }

            // button 36, sort by price customer marketplace
            if (e.getSource() == sortProductsByPrice ) {
                writer.write("36");
                writer.println();
                writer.flush();
                try {
                    String priceSortedProducts = reader.readLine();
                    if (!priceSortedProducts.equals("")) {
                        if (priceSortedProducts.contains(","))
                            marketPlace = priceSortedProducts.split(",");
                        else
                            marketPlace =  new String[]{priceSortedProducts};
                        marketSelect.setModel(new DefaultComboBoxModel<String>(marketPlace));
                    } else {
                        JOptionPane.showMessageDialog(null, "No Results", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to sort",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }

            }

            // button 37, sort by quantity customer marketplace
            if (e.getSource() == sortProductsByQuantity) {
                writer.write("37");
                writer.println();
                writer.flush();
                try {
                    String quantitySortedProducts = reader.readLine();
                    if (!quantitySortedProducts.equals("")) {
                        if (quantitySortedProducts.contains(","))
                            marketPlace = quantitySortedProducts.split(",");
                        else
                            marketPlace =  new String[]{quantitySortedProducts};
                        marketSelect.setModel(new DefaultComboBoxModel<String>(marketPlace));
                    } else {
                        JOptionPane.showMessageDialog(null, "No Results", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to sort",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        }
    };


    public static void main(String[] args) {


    }

    // method to reset a users cart after they choose the view cart button or buy/remove items from cart
    public void resetViewCart(PrintWriter writer, BufferedReader reader) {
        writer.write("22");
        writer.println();
        writer.flush();
        String cartInfo = "";

        try {
            cartInfo = reader.readLine();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        viewCartFrame.setVisible(true);
        customer.setVisible(false);

        if (cartInfo.equals(""))
        {
            itemsInCartDropdown.setVisible(false);
            buyCart.setVisible(false);
            removeAllFromCart.setVisible(false);
            removeFromCart.setVisible(false);
            cartViewItem.setVisible(false);

            emptyCartLabel.setVisible(true);
        } else {
            cartList = cartInfo.split(",");
            itemsInCartDropdown.setModel(new DefaultComboBoxModel<>(cartList));

            itemsInCartDropdown.setVisible(true);
            buyCart.setVisible(true);
            removeAllFromCart.setVisible(true);
            removeFromCart.setVisible(true);
            cartViewItem.setVisible(true);

            emptyCartLabel.setVisible(false);
        }
    }

    public void refreshSellerPage(PrintWriter writer, BufferedReader reader) {
        writer.write("32");
        writer.println();
        writer.flush();
        String str = "";

        try {
            str = reader.readLine();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (str.equals("")) {
            storesDropdown.setVisible(false);
            selectStoreButton.setVisible(false);

            JOptionPane.showMessageDialog(null, "you have no stores", "error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            storeList = str.split(",");
            selectStoreButton.setVisible(true);
            storesDropdown.setModel(new DefaultComboBoxModel<>(storeList));
        }
    }
    public void refreshStoresPage(PrintWriter writer, BufferedReader reader) {
        writer.write("33");
        writer.println();
        writer.flush();

        writer.write(storeList[storesDropdown.getSelectedIndex()]);
        writer.println();
        writer.flush();
        String str = "";

        try {
            str = reader.readLine();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (str.equals("")) {
            productsDropdown.setVisible(false);
            selectProductButton.setVisible(false);

            JOptionPane.showMessageDialog(null, "you have no products in this store",
                    "error", JOptionPane.ERROR_MESSAGE);
        } else {
            productList = str.split(",");
            selectProductButton.setVisible(true);
            productsDropdown.setModel(new DefaultComboBoxModel<>(productList));
        }
    }


    public void refreshCustomerPage(PrintWriter writer, BufferedReader reader) {
        writer.write("31");
        writer.println();
        writer.flush();
        String str = "";

        try {
            str = reader.readLine();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (str.equals("")) {
            marketSelect.setVisible(false);
            customerViewProPage.setVisible(false);
            searchBtn.setVisible(false);
            searchBox.setVisible(false);
            allProBtn.setVisible(false);
            JOptionPane.showMessageDialog(null, "No Products in the marketplace", "error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            marketPlace = str.split(",");
            marketSelect.setModel(new DefaultComboBoxModel<>(marketPlace));

            marketSelect.setVisible(true);
            customerViewProPage.setVisible(true);
            searchBtn.setVisible(true);
            searchBox.setVisible(true);
            allProBtn.setVisible(true);

        }
    }


    public boolean viewProductPage(PrintWriter writer, BufferedReader reader)
    {
        writer.write("14");
        writer.println();
        writer.flush();

        writer.write(marketPlace[marketSelect.getSelectedIndex()]);
        writer.println();
        writer.flush();

        String[] productDataViewCustomer;
        String p;
        try {
            p = reader.readLine();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        if (!p.equals("error")) {
            productDataViewCustomer = p.split(",");
            customerViewProductName.setText("Product: " + productDataViewCustomer[0]);
            customerViewProductDescription.setText("Description: " + productDataViewCustomer[1]);
            customerViewProductStore.setText("Store: " + productDataViewCustomer[2]);
            customerViewProductPrice.setText("Price: " + productDataViewCustomer[3]);
            customerViewProductStock.setText("Quantity Available: " + productDataViewCustomer[4]);
        } else {
            JOptionPane.showMessageDialog(null, "this object has been updated or deleted. " +
                    "refreshing now.", "error", JOptionPane.ERROR_MESSAGE);
            refreshCustomerPage(writer, reader);
            return false;
        }

        return true;
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
        viewCustomersandProducts = new JFrame("More store Info");
        Container extraView = viewCustomersandProducts.getContentPane();
        extraView.setLayout(new BorderLayout());
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
        customerViewProductFrame = new JFrame("View Product");
        Container customerViewProductContent = customerViewProductFrame.getContentPane();
        customerViewProductContent.setLayout(new BorderLayout());
        viewCartFrame = new JFrame("View Cart");
        Container viewCartContent = viewCartFrame.getContentPane();
        viewCartContent.setLayout(new BorderLayout());



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

        customerViewProductFrame.setSize(1000, 600);
        customerViewProductFrame.setLocationRelativeTo(null);
        customerViewProductFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        customerViewProductFrame.setVisible(false);

        viewCartFrame.setSize(1000, 600);
        viewCartFrame.setLocationRelativeTo(null);
        viewCartFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewCartFrame.setVisible(false);

        viewPurchaseHistory = new JFrame("Purchase History");
        Container purchaseHistoryContent = viewPurchaseHistory.getContentPane();
        purchaseHistoryContent.setLayout(new BorderLayout());
        viewPurchaseHistory.setSize(1000, 600);
        viewPurchaseHistory.setLocationRelativeTo(null);
        viewPurchaseHistory.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewPurchaseHistory.setVisible(false);
        history = new JComboBox<String>();
        historyBack = new JButton("Back");
        purchaseHistoryContent.setLayout(null);
        historyBack.setBounds(50, 475, 350, 60);
        historyBack.addActionListener(actionListener);
        purchaseHistoryContent.add(historyBack);
        history = new JComboBox<String>();
        history.setBounds(viewPurchaseHistory.getWidth() / 2 - 350, 250, 700, 30);
        purchaseHistoryContent.add(history);

        viewCustomersandProducts.setSize(1000,600);
        viewCustomersandProducts.setLocationRelativeTo(null);
        viewCustomersandProducts.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewCustomersandProducts.setVisible(false);

        viewCustomers = new JComboBox<String>();
        viewProducts = new JComboBox<String>();
        extraView.setLayout(null);
        backViewCustomersandProducts = new JButton("Back");
        backViewCustomersandProducts.setBounds(50, 475, 350, 60);
        backViewCustomersandProducts.addActionListener(actionListener);
        extraView.add(backViewCustomersandProducts);
        viewCustomers.setBounds(50,250,375, 30);
        viewProducts.setBounds(575, 250, 375, 30);
        extraView.add(viewCustomers);
        extraView.add(viewProducts);
        viewCustomers.setVisible(true);
        viewProducts.setVisible(true);
        moreInfo = new JButton("View more about this store");
        moreInfo.setBounds(250, 350, 500, 30);
        moreInfo.addActionListener(actionListener);
        proLabel = new JLabel("Products: ");
        proLabel.setBounds(713, 200, 250, 30);
        extraView.add(proLabel);
        cusLabel = new JLabel("Customers: ");
        cusLabel.setBounds(120, 200, 250, 30);
        extraView.add(cusLabel);
        sellerDashboardContent.add(moreInfo);


        viewProductsInCartsFrame = new JFrame("Products in Carts");
        Container productsInCartsContent = viewProductsInCartsFrame.getContentPane();
        productsInCartsContent.setLayout(new BorderLayout());
        viewProductsInCartsFrame.setSize(1000, 600);
        viewProductsInCartsFrame.setLocationRelativeTo(null);
        viewProductsInCartsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewProductsInCartsFrame.setVisible(false);

        items = new JComboBox<String>();
        inCartsBack = new JButton("Back");
        productsInCartsContent.setLayout(null);
        inCartsBack.setBounds(50, 475, 350, 60);
        inCartsBack.addActionListener(actionListener);
        productsInCartsContent.add(inCartsBack);
        items = new JComboBox<String>();
        items.setBounds(viewProductsInCartsFrame.getWidth() / 2 - 450, 250, 900, 30);
        productsInCartsContent.add(items);

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
        createAccountButton.setBounds(325, 475, 350, 60);
        createAccountButton.addActionListener(actionListener);
        buttonPanelLogin.add(createAccountButton);

        /*exitButton = new JButton("Exit");
        exitButton.setBounds(50, 475, 350, 60);
        exitButton.addActionListener(actionListener);
        buttonPanelLogin.add(exitButton);*/

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

        viewProductsInCarts = new JButton("View Products in Carts");
        viewProductsInCarts.setBounds(300, 20, 250, 30);
        viewProductsInCarts.addActionListener(actionListener);
        buttonPanelSeller.add(viewProductsInCarts);

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

        /*exitButtonSeller = new JButton("Exit");
        exitButtonSeller.setBounds(50, 475, 350, 60);
        exitButtonSeller.addActionListener(actionListener);
        buttonPanelSeller.add(exitButtonSeller);*/



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

        historyBtn = new JButton("View Purchase History");
        historyBtn.setBounds(300, 20, 250, 30);
        historyBtn.addActionListener(actionListener);
        buttonPanelCustomer.add(historyBtn);
        historyBtn.setVisible(true);

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

        /*exitButtonCustomer = new JButton("Exit");
        exitButtonCustomer.setBounds(50, 475, 350, 60);
        exitButtonCustomer.addActionListener(actionListener);
        buttonPanelCustomer.add(exitButtonCustomer);*/

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

        deleteAccountSeller = new JButton("Delete Account");
        deleteAccountSeller.setBounds(600, 475, 350, 60);
        deleteAccountSeller.addActionListener(actionListener);
        buttonPanelEditSeller.add(deleteAccountSeller);

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

        deleteAccountCustomer = new JButton("Delete Account");
        deleteAccountCustomer.setBounds(600, 475, 350, 60);
        deleteAccountCustomer.addActionListener(actionListener);
        buttonPanelEditCustomer.add(deleteAccountCustomer);

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
        JPanel buttonPanelViewCart = new JPanel();
        buttonPanelViewCart.setLayout(null);

        removeAllFromCart = new JButton("Clear Cart");
        removeAllFromCart.setBounds(650, 475, 300, 60);
        removeAllFromCart.addActionListener(actionListener);
        buttonPanelViewCart.add(removeAllFromCart);

        removeFromCart = new JButton("Remove Selected Item");
        removeFromCart.setBounds(300, 400, 200, 40);
        removeFromCart.addActionListener(actionListener);
        buttonPanelViewCart.add(removeFromCart);

        buyCart = new JButton("Buy All");
        buyCart.setBounds(350, 150, 300, 60);
        buyCart.addActionListener(actionListener);
        buttonPanelViewCart.add(buyCart);

        cartBackButton = new JButton("Back");
        cartBackButton.setBounds(50, 475, 300, 60);
        cartBackButton.addActionListener(actionListener);
        buttonPanelViewCart.add(cartBackButton);

        cartViewItem = new JButton("View Item Details");
        cartViewItem.setBounds(500, 400, 200, 40);
        cartViewItem.addActionListener(actionListener);
        buttonPanelViewCart.add(cartViewItem);

        itemsInCartDropdown = new JComboBox<>();
        itemsInCartDropdown.setBounds(300, 300, 400, 40);
        buttonPanelViewCart.add(itemsInCartDropdown);

        emptyCartLabel = new JLabel("Your cart is empty.");
        emptyCartLabel.setBounds(450, 300, 150, 40);
        buttonPanelViewCart.add(emptyCartLabel);



        //Create buttons and panels for product page for customers
        //add to cart, back
        JPanel buttonPanelCustomerViewProduct = new JPanel();
        buttonPanelCustomerViewProduct.setLayout(null);

        customerViewProdouctBack = new JButton("Back");
        customerViewProdouctBack.setBounds(50,475,350,60);
        customerViewProdouctBack.addActionListener(actionListener);
        buttonPanelCustomerViewProduct.add(customerViewProdouctBack);

        customerViewProdouctBackForCart = new JButton("Back");
        customerViewProdouctBackForCart.setBounds(50,475,350,60);
        customerViewProdouctBackForCart.addActionListener(actionListener);
        buttonPanelCustomerViewProduct.add(customerViewProdouctBackForCart);

        addToCart = new JButton("Add to Cart");
        addToCart.setBounds(600, 475, 350, 60);
        addToCart.addActionListener(actionListener);
        buttonPanelCustomerViewProduct.add(addToCart);
        customerViewProductName = new JLabel("Product Name: ");
        customerViewProductName.setBounds(400, 100, 450, 20);
        buttonPanelCustomerViewProduct.add(customerViewProductName);
        customerViewProductDescription = new JLabel("Product Description: ");
        customerViewProductDescription.setBounds(400, 125, 450, 20);
        buttonPanelCustomerViewProduct.add(customerViewProductDescription);
        customerViewProductStore = new JLabel("Product Store: ");
        customerViewProductStore.setBounds(400, 150, 450, 20);
        buttonPanelCustomerViewProduct.add(customerViewProductStore);
        customerViewProductPrice = new JLabel("Product Price: ");
        customerViewProductPrice.setBounds(400, 175, 450, 20);
        buttonPanelCustomerViewProduct.add(customerViewProductPrice);
        customerViewProductStock = new JLabel("Product Stock:");
        customerViewProductStock.setBounds(400, 200, 450, 20);
        buttonPanelCustomerViewProduct.add(customerViewProductStock);
        customerViewProductHowMany = new JTextField("1");
        customerViewProductHowMany.setBounds(400, 250, 50, 20);
        buttonPanelCustomerViewProduct.add(customerViewProductHowMany);
        customerViewProductHowManyLabel = new JLabel("Enter Quantity Above");
        customerViewProductHowManyLabel.setBounds(400,270,450, 20);
        buttonPanelCustomerViewProduct.add(customerViewProductHowManyLabel);


        //create a seller dashboard frame and the buttons and panels
        //sortbyrevenue, sortbysales,
        JPanel buttonPanelSellerDashboard = new JPanel();
        buttonPanelSellerDashboard.setLayout(null);

        directionLabel = new JLabel("select a sort option to see your stores.");
        directionLabel.setBounds(375, 210, 300, 30);
        buttonPanelSellerDashboard.add(directionLabel);

        backButtonSellerDash = new JButton("Back");
        backButtonSellerDash.setBounds(50, 475, 350, 60);
        backButtonSellerDash.addActionListener(actionListener);
        buttonPanelSellerDashboard.add(backButtonSellerDash);

        logoutButtonSellerDash = new JButton("Logout");
        logoutButtonSellerDash.setBounds(50, 20, 150, 30);
        logoutButtonSellerDash.addActionListener(actionListener);
        buttonPanelSellerDashboard.add(logoutButtonSellerDash);

        sellerDash = new JComboBox<>();
        sellerDash.setBounds(275, 250, 475, 30);
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
        customerDash.setBounds(275, 250, 475, 30);
        buttonPanelCustomerDashboard.add(customerDash);

        sortByUserPop = new JButton("Sort By Your # of Purchases");
        sortByUserPop.setBounds(225,300,250,30);
        sortByUserPop.addActionListener(actionListener);
        buttonPanelCustomerDashboard.add(sortByUserPop);

        sortByGenPop = new JButton("Sort By General Popularity");
        sortByGenPop.setBounds(525,300,250,30);
        sortByGenPop.addActionListener(actionListener);
        buttonPanelCustomerDashboard.add(sortByGenPop);

        exportPurchaseHistory = new JButton("Export Purchase History");
        exportPurchaseHistory.setBounds(675, 60, 200, 30);
        exportPurchaseHistory.addActionListener(actionListener);
        buttonPanelCustomer.add(exportPurchaseHistory);

        importProducts = new JButton("Import Products");
        importProducts.setBounds(825, 70, 150, 30);
        importProducts.addActionListener(actionListener);
        buttonPanelSeller.add(importProducts);

        exportProductsFromAStore = new JButton("Export Store's Products");
        exportProductsFromAStore.setBounds(600, 400, 350, 60);
        exportProductsFromAStore.addActionListener(actionListener);
        buttonPanelStore.add(exportProductsFromAStore);

        sortProductsByPrice = new JButton("Sort By Price");
        sortProductsByPrice.setBounds(300, 400, 200, 30);
        sortProductsByPrice.addActionListener(actionListener);
        buttonPanelCustomer.add(sortProductsByPrice);

        sortProductsByQuantity = new JButton("Sort by Quantity");
        sortProductsByQuantity.setBounds(500, 400, 200, 30);
        sortProductsByQuantity.addActionListener(actionListener);
        buttonPanelCustomer.add(sortProductsByQuantity);

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
        customerViewProductContent.add(buttonPanelCustomerViewProduct);
        viewCartContent.add(buttonPanelViewCart);
    }

}