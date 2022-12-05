import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * CUSTOMER CLASS
 *
 * Creates customer objects that can buy products from stores
 *
 * @author L12 group 2
 * @version 2 DEC 2022
 */

public class Customer extends User {

    private ArrayList<Product> shoppingCart;
    private ArrayList<String> purchaseHistory; // all items ever purchased

    public Customer(String name, String email, String password, boolean isCustomer, int questionNum, String answer,
                    String cart, String history) {
        super(name, email, password, isCustomer, questionNum, answer);
        this.shoppingCart = new ArrayList<Product>();
        this.purchaseHistory = new ArrayList<String>();
        try {
            // for shopping cart
            BufferedReader bfr = new BufferedReader(new FileReader("products.txt"));

            if (!cart.equals("none")) {
                String[] parse = cart.split("~");
                String[] doubleParse;

                String storeName, productName, line;
                for (int i = 0; i < parse.length; i++) {
                    doubleParse = parse[i].split("-");
                    storeName = doubleParse[0];
                    productName = doubleParse[1];

                    shoppingCart.add(Product.getProduct(productName, storeName));
                }
                for (int i = 0; i < shoppingCart.size(); i++) {
                    Product p = shoppingCart.get(i);
                    for (int x = 0; x < shoppingCart.size(); x++) {
                        if (p.equals(shoppingCart.get(x))) {
                            shoppingCart.set(x, p);
                        }
                    }
                }
            }

            // for history
            BufferedReader bfrHistory = new BufferedReader(new FileReader("products.txt"));

            if (!history.equals("none")) {
                String[] parse = history.split("~");
                String[] doubleParse;

                String storeName, productName, line;
                for (int i = 0; i < parse.length; i++) {
                    doubleParse = parse[i].split("-");
                    storeName = doubleParse[0];
                    productName = doubleParse[1];
                    purchaseHistory.add(storeName + "-" + productName);
                }
            }
        } catch (IOException e) {
            System.out.println("Error with reading product file");
        }
    }

    public Customer(String name, String email, String password, boolean isCustomer, Scanner s) {
        super(name, email, password, isCustomer, s);
        purchaseHistory = new ArrayList<String>();
        shoppingCart = new ArrayList<Product>();
        this.writeToFile();
    }

    // project 5 customer without a scanner
    public Customer(String name, String email, String password, boolean isCustomer, String securityAnswer, int securityQuestionNum) {
        super(name, email, password, isCustomer, securityAnswer, securityQuestionNum);
        purchaseHistory = new ArrayList<String>();
        shoppingCart = new ArrayList<Product>();
        this.writeToFile();
    }

    public ArrayList<String> readUserFile() { // returns an arrayList of every line in the user's file
        ArrayList<String> fileLines = null;
        try {
            fileLines = new ArrayList<>();
            BufferedReader bfr = new BufferedReader(new FileReader("users.txt"));
            String line = bfr.readLine();
            while (line != null) {
                fileLines.add(line);
                line = bfr.readLine();
            }
            bfr.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileLines;
    }

    public boolean addToCart(Product product) { // allows user to add a product to their shopping cart
        if (product.getQuantity() > 0) { // only add if product's quantity is greater than 0
            shoppingCart.add(product);
            editUserFile();
            System.out.println("Added to cart!");
            return true;
        }
        System.out.println("Product is out of stock!");
        return false; // return false if the product is out of stock
    }

    // does not currently work, use an equals() method to compare products in the array
    public void removeFromCart(Product product) { // user removes a certain product from their cart
        shoppingCart.remove(product);
        editUserFile();
    }

    // user buys all the products in their cart
    public void purchaseProductsInCart() {
        for (int i = 0; i < shoppingCart.size(); i++) {
            if (shoppingCart.get(i).getQuantity() == 0) {
                System.out.println("Product " + shoppingCart.get(i).getName() + " is out of stock!");
            } else {
                purchaseHistory.add(shoppingCart.get(i).getStore() + "-" + shoppingCart.get(i).getName());
                shoppingCart.get(i).setQuantity(shoppingCart.get(i).getQuantity() - 1); // decrement quantity
                shoppingCart.get(i).setAmountSold(shoppingCart.get(i).getAmountSold() + 1); // increment amount sold
                try {
                    BufferedReader bfr = new BufferedReader(new FileReader("stores.txt"));
                    BufferedReader bfr2 = new BufferedReader(new FileReader("products.txt"));
                    String line = bfr.readLine();
                    String[] parse;
                    ArrayList<Product> p = new ArrayList<>();
                    ArrayList<Customer> c = new ArrayList<>();
                    ArrayList<Integer> it = new ArrayList<>();
                    while (line != null) {
                        parse = line.split(","); //splits store line
                        if (parse[0].equals(shoppingCart.get(i).getStore())) {
                            //find the store name based on the stores file. if equal, add the store to the seller
                            //line 2 -> product names
                            //line 4 -> customer emails
                            //line 5 -> ints relating to customer sales
                            if (!parse[2].equals("none")) { // if there are products
                                String line2 = bfr2.readLine();
                                String[] parsedProduct;
                                while (line2 != null) {
                                    parsedProduct = line2.split(",");//reads the product line
                                    //splits up the product line
                                    if(parsedProduct[1].equals(shoppingCart.get(i).getStore())) {
                                        //if the product has the same store name, then it gets added to the store
                                        p.add(new Product(parsedProduct[0], parsedProduct[1], parsedProduct[2],
                                                Integer.parseInt(parsedProduct[3]),
                                                Double.parseDouble(parsedProduct[4]),
                                                Integer.parseInt(parsedProduct[5])));
                                    } //0: produce name 1: storeName 2: description 3: quantity, 4: price 5: sold
                                    line2 = bfr2.readLine(); //goes to the next line in products
                                }
                            }
                            if (!parse[4].equals("none")) { //if there are customers
                                String[] parsedEmails;
                                String[] parsedNums;
                                if (parse[4].contains("~")) {
                                    parsedEmails = parse[4].split("~");
                                    parsedNums = parse[5].split("~");
                                } else {
                                    parsedEmails = new String[]{ parse[4] };
                                    parsedNums = new String[]{ parse[5] };
                                }

                                for (int j = 0; j < parsedEmails.length; j++) {
                                    c.add(Customer.parseCustomer(parsedEmails[j]));
                                    it.add(Integer.parseInt(parsedNums[j]));
                                }
                            }

                            boolean contains = false;

                            int j;
                            for (j = 0; j < c.size(); j++) {
                                if(c.get(j).getEmail().equals(this.getEmail())) {
                                    contains = true;
                                    break;
                                }
                            }
                            if (contains) {
                                it.set(j, it.get(j) + 1);
                            } else {
                                c.add(Customer.parseCustomer(this.getEmail()));
                                it.add(1);
                            }

                            Store store = new Store(parse[1], shoppingCart.get(i).getStore(), p,
                                    Double.parseDouble(parse[3]), c, it);
                            store.setSales(store.getSales() + shoppingCart.get(i).getPrice());
                            //email, storeName, products, sales, customers, customerSales
                            store.editStoreFile();
                            break;
                        }
                        line = bfr.readLine(); //moves onto the next line in Stores
                    }
                } catch (Exception e) {
                    System.out.println("Error reading file. ");
                    e.printStackTrace();
                }

                shoppingCart.get(i).editProductFile();
            }

        }
        shoppingCart.clear(); // empty the cart

        editUserFile();
    }

    @Override
    public synchronized void editUserFile() { // updates user's file info
        ArrayList<String> fileLines = readUserFile();
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("users.txt", false));
            //store name then product name
            String shoppingCartStorageString = "";
            String purchaseHistoryString = "";

            String[] s;
            for (int i = 0; i < fileLines.size(); i++) { // update shopping cart in user file
                s = fileLines.get(i).split(",");
                if (s[1].equals(this.getEmail())) {
                    s[2] = this.getName();
                    s[3] = this.getPassword();
                    s[4] = String.valueOf(this.getSecurityQuestionNumber());
                    s[5] = this.getSecurityAnswer();
                    if (shoppingCart.size() == 0) {
                        s[6] = "none";
                    } else {
                        String pName, sName, combo;
                        for (int j = 0; j < shoppingCart.size(); j++) {
                            sName = shoppingCart.get(j).getStore();
                            pName = shoppingCart.get(j).getName();
                            combo = sName + "-" + pName;
                            shoppingCartStorageString += combo;
                            if (j != shoppingCart.size() - 1) {
                                shoppingCartStorageString += "~";
                            }
                        }
                        s[6] = shoppingCartStorageString;
                    }
                    if (purchaseHistory.size() == 0) {
                        s[7] = "none";
                    } else {
                        String pName, sName, combo;
                        String[] split;
                        for (int j = 0; j < purchaseHistory.size(); j++) {
                            split = purchaseHistory.get(j).split("-");
                            sName = split[0];
                            pName = split[1];
                            combo = sName + "-" + pName;
                            purchaseHistoryString += combo;
                            if (j != purchaseHistory.size() - 1) {
                                purchaseHistoryString += "~";
                            }
                        }
                        s[7] = purchaseHistoryString;
                    }
                    String str = "customer," + s[1] + "," + s[2] + "," + s[3] + "," + s[4] + "," + s[5] + ","
                            + s[6] + "," + s[7];
                    fileLines.remove(fileLines.get(i));
                    fileLines.add(str);
                    break;
                }
            }

            for (String fileLine : fileLines) {
                pw.println(fileLine);
            }

            pw.flush();
            pw.close();

        } catch (Exception e) {
            System.out.println("error");
        }
    }




    public String printPurchaseHistoryString() { // print the user's product purchase history
        String history = "";
        if (purchaseHistory.size() == 0)
        {
            return "You have not purchased anything!";
        }
        String[] split;
        for (int i = 0; i < purchaseHistory.size(); i++)
        {
            split = purchaseHistory.get(i).split("-");
            if (i == purchaseHistory.size() - 1)
            {
                history += "\n------------------------------------";
                history += "\nProduct: " + (i + 1);
                history += "\nName: " + split[1];
                history += "\nStore: " + split[0];
                history += "\n------------------------------------";
            } else {
                history += "\n------------------------------------";
                history += "\nProduct: " + (i + 1);
                history += "\nName: " + split[1];
                history += "\nStore: " + split[0];
            }

        }
        System.out.println(history);
        return history;
    }

    public void exportPurchaseHistory() {
        try {
            if (purchaseHistory.size() == 0) {
                System.out.println("Nothing to export as you have not purchased anything!");
            } else {
                String[] split;
                PrintWriter pw = new PrintWriter(new FileOutputStream(this.getEmail() + "PurchaseHistory.txt"));
                for (int i = 0; i < purchaseHistory.size(); i++) {
                    split = purchaseHistory.get(i).split("-");
                    String str = "Product Name: " + split[1] + ", Store: " + split[0];
                    pw.println(str);
                }
                pw.flush();
                pw.close();
                System.out.println("Exporting complete.");
            }
        } catch (Exception e) {
            System.out.println("Error exporting purchase history!");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Customer) {
            return ((Customer) obj).getName().equals(this.getName()) &&
                    ((Customer) obj).getEmail().equals(this.getEmail()) &&
                    ((Customer) obj).getPassword().equals(this.getPassword()) &&
                    ((Customer) obj).isCustomer() &&
                    ((Customer) obj).getSecurityQuestionNumber() == this.getSecurityQuestionNumber() &&
                    ((Customer) obj).getSecurityAnswer().equals(this.getSecurityAnswer()) &&
                    ((Customer) obj).getShoppingCart().equals(this.getShoppingCart()) &&
                    ((Customer) obj).getPurchaseHistory().equals(this.getPurchaseHistory());
        }
        return false;
    }

    public ArrayList<Product> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ArrayList<Product> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public ArrayList<String> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setPurchaseHistory(ArrayList<String> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    public void writeToFile() {
        File f = new File("users.txt");
        try {
            FileOutputStream fos = new FileOutputStream(f,true);
            PrintWriter pw = new PrintWriter(fos);
            String str = "customer," + this.getEmail() + "," + this.getName() + "," + this.getPassword() + "," +
                    this.getSecurityQuestionNumber() + "," + this.getSecurityAnswer() + ",none" + ",none";
            pw.println(str);
            pw.close();

        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    /**
     * This utility method takes an email string, looks through the users text file to find the
     * corresponding Customer obj information and generates a new Customer object to return.
     * @param em
     * @return Customer object
     */
    public static Customer parseCustomer(String em) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("users.txt"));

            String line = bfr.readLine();
            while (line != null) {
                String[] parse = line.split(",");
                if (parse[1].equals(em)) {
                    String sendEmail = parse[1];
                    String sendName = parse[2];
                    String sendPass = parse[3];
                    int sendNum = Integer.parseInt(parse[4]);
                    String sendAns = parse[5];
                    String sendCart = parse[6];
                    String sendHistory = parse[7];
                    return new Customer(sendName, sendEmail, sendPass, true, sendNum, sendAns, sendCart,
                            sendHistory);
                }
                line = bfr.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error reading file");
        }
        return null;
    }
    /*
    TODO: NEED TO DEBUG! core: Sort dashboard, view marketplace?
     */
}
