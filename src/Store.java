import java.io.*;
import java.util.ArrayList;
/**
 * STORE CLASS
 *
 * Stores contain a list of products and customers and can be edited by the seller that owns it
 *
 * @author L12 group 2
 * @version 13 NOV 2022
 */
public class Store {
    private String owner;
    private String name;
    private ArrayList<Product> products;
    private double sales;
    private ArrayList<Customer> customers;
    private ArrayList<Integer> customerSales;

    public Store(String owner, String name, ArrayList<Product> products) {
        this.owner = owner;
        this.name = name;
        this.products = products;
        customers = new ArrayList<>();
        customerSales = new ArrayList<>();
        sales = 0;
        this.writeToFile(); //writes a new store to the stores.txt file
    }

    public Store(String email, String name, ArrayList<Product> products, double sales, ArrayList<Customer> c,
                 ArrayList<Integer> i) {
        this.owner = email;
        this.name = name;
        this.products = products;
        this.sales = sales;
        this.customers = c;
        this.customerSales = i;
        //constructs a store based on file information
    }

    public String getOwner() {
        return owner;
    }
    public String getName() {
        return name;
    }
    public ArrayList<Product> getProducts() {
        return products;
    }
    public double getSales() {
        return sales;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
    public void setSales(int sales) {
        this.sales = sales;
    }
    public void sellProduct(String productName) {
        int index = -1;
        for (int i = 0; i < products.size(); i++) {
            if (productName.equals(products.get(i).getName())) {
                index = i;
            }
        }
        if (index == -1) {
            System.out.print("Sorry, that product doesn't exist at this store.");
        }
        sales++;
        products.get(index).setQuantity(products.get(index).getQuantity() - 1);
    }
    public void returnProduct(String productName) {
        int index = -1;
        for (int i = 0; i < products.size(); i++) {
            if (productName.equals(products.get(i).getName())) {
                index = i;
            }
        }
        if (index == -1) {
            System.out.print("Sorry, that product doesn't exist at this store.");
        }
        sales--;
        products.get(index).setQuantity(products.get(index).getQuantity() + 1);
    }
    public void writeToFile() { // write new store to the file
        File f = new File("stores.txt");
        try {
            FileOutputStream fos = new FileOutputStream(f,true);
            PrintWriter pw = new PrintWriter(fos);
            String str = this.getName() + "," + this.getOwner() + ",none,0,none,none";
            //Store name, email, productArray(just names of the products will be stored), sales, customerEmail array,
            // SalespercustomerArray
            pw.println(str);
            pw.flush();
            pw.close();

        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    public synchronized void editStoreFile() { //edits the store file based on this
        try {
            StringBuilder productNames = new StringBuilder();
            StringBuilder customerEmails = new StringBuilder();
            StringBuilder customersSales = new StringBuilder();
            ArrayList<String> storeFileLines = new ArrayList<>();
            BufferedReader bfr = new BufferedReader(new FileReader("stores.txt"));
            try {
                String line = null;
                while (true) {
                    line = bfr.readLine();
                    if (line == null) {
                        break;
                    }
                    storeFileLines.add(line);
                } // adds all the lines from the store file to an arrayList
            } catch (IOException e) {
                System.out.println("File not found!");
            }

            //splits based on our file formatting system
            for (int i = 0; i < storeFileLines.size(); i++) {
                String[] lineData = storeFileLines.get(i).split(",");
                if (lineData[0].equalsIgnoreCase(this.getName())) {
                    lineData[0] = this.getName();
                    lineData[1] = this.getOwner();
                    if (products.size() == 0) { //if the store has no products, write "none"
                        lineData[2] = "none";
                    } else { // writes the product names to the file
                        String productName;
                        for (int j = 0; j < products.size(); j++) {
                            productName = products.get(j).getName();
                            productNames.append(productName);
                            if (j != products.size() - 1) {
                                productNames.append("~"); //seperates product names with ~
                            }
                        }
                        lineData[2] = productNames.toString();
                    }
                    lineData[3] = String.valueOf(this.getSales());
                    if (customers.size() == 0) { //if there are no customers, sets file to none and their sales to none
                        lineData[4] = "none";
                        lineData[5] = "none";
                    } else { //else, it loops through and adds the customers to the file
                        String customerEmail;
                        String customerSale;
                        for (int x = 0; x < customers.size(); x++) {
                            customerEmail = customers.get(x).getEmail();
                            customerEmails.append(customerEmail);
                            if (x != customers.size() - 1) {
                                customerEmails.append("~");
                            }
                            customerSale = String.valueOf(customerSales.get(x));
                            customersSales.append(customerSale);
                            if (x != customerSales.size() - 1) {
                                customersSales.append("~");
                            }
                        }
                        lineData[4] = customerEmails.toString();
                        lineData[5] = customersSales.toString();
                    }
                    String str = lineData[0] + "," + lineData[1] + "," + lineData[2] + "," + lineData[3] + ","
                            + lineData[4] + "," + lineData[5];
                    storeFileLines.remove(storeFileLines.get(i));
                    storeFileLines.add(str);
                    break;
                }
            }
            PrintWriter pw = new PrintWriter(new FileOutputStream("stores.txt", false));
            for (String fileLine : storeFileLines) {
                pw.println(fileLine);
            }
            pw.flush();
            pw.close();

        } catch (IOException e) {
            System.out.println("Error: Store file could not be properly edited!");
        }
    }

    public static synchronized void updateProducts(String storeName) throws FileNotFoundException {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("products.txt"));
            ArrayList<String> lines = new ArrayList();
            ArrayList<String> newProducts = new ArrayList<>();

            while(true) {
                String line = "";
                String[] elements;
                line = bfr.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }

            for(int i = 0; i < lines.size(); i++) {
                String store = lines.get(i).split(",")[1];
                if(!(store.equals(storeName))) {
                    newProducts.add(lines.get(i));
                }
            }

            PrintWriter pw = new PrintWriter(new FileOutputStream("products.txt", false));
            for(String product : newProducts) {
                pw.println(product);
            }
            pw.flush();
            pw.close();


        } catch(IOException io) {
            System.out.println("Error: file products.txt was not properly updated.");
        }

    }

    public String toString() {
        String ret = String.format("Store Name: %s, Owner: %s, Sales: $%.2f, Total Products: %d,", name, owner, sales,
                products.size());
        for (int i = 0; i < products.size(); i++) {
            ret += products.get(i).toString();
            ret += ",";
        }
        return ret;
    }

    public void setSales(double sales) {
        this.sales = sales;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    public ArrayList<Integer> getCustomerSales() {
        return customerSales;
    }

    public void setCustomerSales(ArrayList<Integer> customerSales) {
        this.customerSales = customerSales;
    }

    public int getTotalSales() {  //goes through the array for customer sales and returns the total number of sales
        int totalSales = 0;

        for (int i = 0; i < customerSales.size(); i++) {
            totalSales += customerSales.get(i);
        }

        return totalSales;
    }

    public int getProductsBoughtByCurrentUser(Customer customer) { //finds the number of products bought by a
        // certain customer
        String customerEmail = customer.getEmail();
        int numBought = 0;

        for(int i = 0; i < customers.size(); i++) {
            if (customers.get(i) == null) {
                return 0;
            }
            if (customers.get(i).getEmail().equals(customerEmail)) {
                numBought = customerSales.get(i);
                break;
            }
        }

        return numBought;
    }


    public static ArrayList<Store> getAllStores() { //returns an array of the lines in Stores.txt
        ArrayList<Store> stores = new ArrayList<Store>();

        try {
            FileReader fr = new FileReader("stores.txt");
            BufferedReader bfr = new BufferedReader(fr);

            String storeLine = bfr.readLine();

            while (storeLine != null) {
                stores.add(makeStore(storeLine));
                storeLine = bfr.readLine();
            }

        } catch (Exception e) {
            System.out.println("Error in getAllStores, Store line 268");
            e.printStackTrace();
        }
        return stores;
    }

    public static Store makeStore(String storeLine) { //constructs a store based on a line from the store file
        String[] storeVariables = storeLine.split(",");

        String owner = storeVariables[0];
        String name = storeVariables[1];

        ArrayList<Product> products = new ArrayList<>();
        if (storeVariables[2].equals("none")) {
        } else {
            String[] productUnformatted = storeVariables[2].split("~");
            for (int i = 0; i < productUnformatted.length; i++) {
                products.add(Product.getProduct(productUnformatted[i]));
            }
        }

        Double sales = Double.parseDouble(storeVariables[3]);

        ArrayList<Customer> customerArrayList = new ArrayList<>();
        if (storeVariables[4].equals("none")) {
        } else {
            String[] customerNames = storeVariables[4].split("~");
            for (int i = 0; i < customerNames.length; i++) {
                customerArrayList.add(Customer.parseCustomer(customerNames[i]));
            }
        }
        String[] salesPerCustomerStrings = storeVariables[5].split("~");
        ArrayList<Integer> salesPerCustomerInts = new ArrayList<>();

        for (int i = 0; i < salesPerCustomerStrings.length; i++) {
            if (salesPerCustomerStrings[i].equals("none")) {
                salesPerCustomerInts.add(0);
            } else {
                salesPerCustomerInts.add(Integer.parseInt(salesPerCustomerStrings[i]));
            }
        }

        return new Store(name, owner, products, sales, customerArrayList, salesPerCustomerInts);
    }


    public boolean checkRepeat(String newProductName) {
        boolean repeat = false;
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("products.txt"));
            String line = bfr.readLine();

            while (line != null) {
                String[] productContents = line.split(",");
                String productName = productContents[0];
                String whichStore = productContents[1];
                if (newProductName.equals(productName) && this.name.equals(whichStore)) {
                    repeat = true;
                }
                line = bfr.readLine();
            }
        } catch (StringIndexOutOfBoundsException e) {

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return repeat;
    }

    public static ArrayList<String> getAllLines() {
        ArrayList<String> stores = new ArrayList<>();

        try {
            FileReader fr = new FileReader("stores.txt");
            BufferedReader bfr = new BufferedReader(fr);

            String storeLine = bfr.readLine();

            while (storeLine != null) {
                stores.add(storeLine);
                storeLine = bfr.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stores;
    }

}
