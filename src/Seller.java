import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * SELLER CLASS
 *
 * Seller objects can make stores and products
 *
 * @author Aaryamik Gupta, Paige Lorenz, Dalton Babbs, Mahika Parakh, Matthew Wind
 * @version December 11, 2022
 */
public class Seller extends User{

    private ArrayList<Store> stores;

    public Seller(String name, String username, String password, Boolean isCustomer, Scanner s) {
        super(name, username, password, isCustomer, s);
        stores = new ArrayList<Store>();
        this.writeToFile();
    }

    // project 5 seller constructor, no scanner
    public Seller(String name, String username, String password, Boolean isCustomer, String securityAnswer,
                  int securityQuestionNum) {
        super(name, username, password, isCustomer, securityAnswer, securityQuestionNum);
        stores = new ArrayList<Store>();
        this.writeToFile();
    }



    public Seller(String name, String email, String password, boolean isCustomer, int questionNum, String answer,
                  String sto) {
        super(name, email, password, isCustomer, questionNum, answer);
        this.stores = new ArrayList<Store>();
        //System.out.println(sto);
        try {
            String parse[];
            if (!sto.equals("none")) {
                if (sto.contains("~")) {
                    parse = sto.split("~"); //array of the store names
                } else {
                    parse = new String[] {sto};
                }
                String storeName, line;
                for (int i = 0; i < parse.length; i++) {
                    BufferedReader bfr = new BufferedReader(new FileReader("stores.txt"));
                    BufferedReader bfr2 = new BufferedReader(new FileReader("products.txt"));

                    storeName = parse[i];
                    //System.out.println(storeName);

                    line = bfr.readLine();
                    String[] moreParse;
                    ArrayList<Product> p = new ArrayList<>();
                    ArrayList<Customer> c = new ArrayList<>();
                    ArrayList<Integer> it = new ArrayList<>();
                    while (line != null) {
                        moreParse = line.split(","); //splits store line
                        if (moreParse[0].equals(storeName)) {
                            //find the store name based on the stores file. if equal, add the store to the seller
                            //line 2 -> product names
                            //line 4 -> customer emails
                            //line 5 -> ints relating to customer sales
                            if (!moreParse[2].equals("none")) { // if there are products
                                String line2 = bfr2.readLine();
                                String[] parsedProduct;
                                while (line2 != null) {
                                    parsedProduct = line2.split(",");//reads the product line
                                    //splits up the product line
                                    if(parsedProduct[1].equals(storeName)) { //if the product has the same store name,
                                        // then it gets added to the store
                                        p.add(new Product(parsedProduct[0], parsedProduct[1], parsedProduct[2],
                                                Integer.parseInt(parsedProduct[3]),
                                                Double.parseDouble(parsedProduct[4]),
                                                Integer.parseInt(parsedProduct[5])));
                                    } //0: produce name 1: storeName 2: description 3: quantity, 4: price 5: sold
                                    line2 = bfr2.readLine(); //goes to the next line in products
                                }
                            }
                            if (!moreParse[4].equals("none")) { //if there are customers
                                String[] parsedEmails = moreParse[4].split("~");
                                String[] parsedNums = moreParse[5].split("~");
                                for (int j = 0; j < parsedEmails.length; j++) {
                                    c.add(Customer.parseCustomer(parsedEmails[j]));
                                    it.add(Integer.parseInt(parsedNums[j]));
                                }
                            }
                            stores.add(new Store(moreParse[1], storeName, p, Double.parseDouble(moreParse[3]), c, it));
                            //email, storeName, products, sales, customers, customerSales
                            break;
                        }
                        line = bfr.readLine(); //moves onto the next line in Stores
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println("Error with reading store file");
        }
    }


    public ArrayList<Store> getStores() {
        return stores;
    }

    public synchronized ArrayList<String> readUserFile() { // returns an arrayList of every line in the user's file
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
            //System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileLines;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }


    public static void updateCustomerCartForNewProduct(Product oldProduct, Product newProduct) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("users.txt"));
            String line = bfr.readLine();

            while (line != null) {
                if (line.substring(0, 8).equals("customer")) {
                    String[] customerData = line.split(",");

                    if (!customerData[6].equals("none")) {
                        String[] productsInCart = customerData[6].split("~");


                        String[] product = new String[2];

                        for (int i = 0; i < productsInCart.length; i++) {

                            product = productsInCart[i].split("-");
                            if (product[1].equals(oldProduct.getName())) {
                                Customer customer = Customer.parseCustomer(customerData[1]);
                                customer.getShoppingCart().set(i, newProduct);
                                customer.editUserFile();
                            }
                        }
                    }
                }

                line = bfr.readLine();
            }

        } catch (StringIndexOutOfBoundsException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeProductFromCustomerCart(Product oldProduct) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("users.txt"));
            String line = bfr.readLine();

            while (line != null) {
                if (line.substring(0, 8).equals("customer")) {
                    String[] customerData = line.split(",");

                    if (!customerData[6].equals("none")) {
                        String[] productsInCart = customerData[6].split("~");


                        String[] product = new String[2];

                        for (int i = 0; i < productsInCart.length; i++) {

                            product = productsInCart[i].split("-");
                            if (product[1].equals(oldProduct.getName())) {
                                Customer customer = Customer.parseCustomer(customerData[1]);
                                customer.getShoppingCart().remove(i);
                                customer.editUserFile();
                                i--;
                                productsInCart = customerData[6].split("~");
                            }
                        }
                    }
                }

                line = bfr.readLine();
            }

        } catch (StringIndexOutOfBoundsException e) {

        } catch (IndexOutOfBoundsException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static synchronized ArrayList<String> readCSVProductFile(String filename) {
        try {
            ArrayList<String> csvProductFileImportLines = new ArrayList<>();
            BufferedReader bfr = new BufferedReader(new FileReader(filename));
            String line = null;
            while (true) {
                line = bfr.readLine();
                if (line == null) {
                    break;
                }
                csvProductFileImportLines.add(line);
            }
            return csvProductFileImportLines;
        } catch (FileNotFoundException e) {
            //System.out.println("CSV file of products could not be found!");
            return null;
        } catch (IOException e) {
            //System.out.println("Error reading the csv file of products!");
            return null;
        }
    }


    public static synchronized ArrayList<String> readProductFile() {
        try {
            ArrayList<String> productFileLines = new ArrayList<>();
            BufferedReader bfrProduct = new BufferedReader(new FileReader("products.txt"));
            String line = null;
            while (true) {
                line = bfrProduct.readLine();
                if (line == null) {
                    break;
                }
                productFileLines.add(line);
            }
            return productFileLines;
        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println("Error reading product file!");
            return null;
        }
    }

    public static synchronized Seller parseSeller(String em) {
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
                    String sendStores = parse[6];
                    return new Seller(sendName, sendEmail, sendPass, false, sendNum, sendAns, sendStores);
                }
                line = bfr.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println("Error reading file");
        }
        return null;
    }

    public synchronized void editUserFile() { // updates user's file info
        ArrayList<String> fileLines = readUserFile();
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("users.txt", false));
            //store name then product name
            StringBuilder productNameStorage = new StringBuilder();

            String[] s;
            for (int i = 0; i < fileLines.size(); i++) { // update shopping cart in user file
                s = fileLines.get(i).split(",");
                if (s[1].equals(this.getEmail())) {
                    s[2] = this.getName();
                    s[3] = this.getPassword();
                    s[4] = String.valueOf(this.getSecurityQuestionNumber());
                    s[5] = this.getSecurityAnswer();
                    if (stores.size() == 0) {
                        s[6] = "none";
                    } else {
                        String sName;
                        for (int j = 0; j < stores.size(); j++) {
                            sName = stores.get(j).getName();
                            productNameStorage.append(sName);
                            if (j != stores.size() - 1) {
                                productNameStorage.append("~");
                            }
                        }
                        s[6] = productNameStorage.toString();
                    }
                    String str = "seller" + "," + s[1] + "," + s[2] + "," + s[3] + "," + s[4] + "," + s[5] + "," + s[6];
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
            e.printStackTrace();
            //System.out.println("error");
        }
    }

    public synchronized void writeToFile() {
        File f = new File("users.txt");
        try {
            FileOutputStream fos = new FileOutputStream(f,true);
            PrintWriter pw = new PrintWriter(fos);
            String str = "seller," + this.getEmail() + "," + this.getName() + "," + this.getPassword() + "," +
                    this.getSecurityQuestionNumber() + "," + this.getSecurityAnswer() + ",none";
            pw.println(str);
            pw.flush();
            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println("Error writing to file.");
        }
    }

    public ArrayList<String> viewCustomerCarts() {
        Customer c;
        ArrayList<Product> carts;
        int totalInCarts = 0;
        StringBuilder str = new StringBuilder();
        ArrayList<String> built = new ArrayList<>();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("users.txt"));
            String line = bfr.readLine();
            String[] parse;
            while (line != null) {
                parse = line.split(",");
                if (!parse[0].equals("customer")) {
                    line = bfr.readLine();
                    continue;
                } else {
                    c = Customer.parseCustomer(parse[1]);
                    assert c != null;
                    carts = c.getShoppingCart();
                    if (carts == null || carts.size() == 0) {
                        line = bfr.readLine();
                        continue;
                    } else {
                        for (int i = 0; i < this.getStores().size(); i ++) {
                            for (int j = 0; j < carts.size(); j++) {
                                if (carts.get(j).getStore().equals(this.getStores().get(i).getName())) {
                                    str = new StringBuilder();
                                    totalInCarts++;
                                    str.append(String.format("Customer: %s   Store: %s   Product: %s  ", c.getEmail(),
                                            this.getStores().get(i).getName(), carts.get(j).getName()));
                                    str.append(String.format("Product Quantity: %d   Product Price: $%.2f   " +
                                                    "Amount Sold: %d", carts.get(j).getQuantity(),
                                            carts.get(j).getPrice(), carts.get(j).getAmountSold()));
                                    built.add(str.toString());
                                }
                            }
                        }

                    }
                }
                line = bfr.readLine();
            }
        } catch (Exception e) {
            //System.out.println("Error reading users file");
            e.printStackTrace();
        }
        return built;
    }

}
