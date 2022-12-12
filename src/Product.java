import java.io.*;
import java.util.ArrayList;

/**
 * PRODUCT CLASS
 *
 * Creates product objects that can be bought by customers and sold by sellers
 *
 * @author Aaryamik Gupta, Paige Lorenz, Dalton Babbs, Mahika Parakh, Matthew Wind
 * @version December 11, 2022
 */
public class Product {
    private String name;
    private String store;
    private String productDescription;
    private double price;
    private int quantity;
    private int sold;

    public Product(String name, String store, String productDescription, int quantity, double price) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.productDescription = productDescription;
        this.store = store;
        this.sold = 0;
        this.writeToFile();

    }

    public Product(String name, String store, String description, int quantity, double price, int amtSold) {
        this.name = name;
        this.store = store;
        this.productDescription = description;
        this.quantity = quantity;
        this.price = price;
        this.sold = amtSold;
    }

    public synchronized void writeToFile() { // write new product to the file
        File f = new File("products.txt");
        try {
            FileOutputStream fos = new FileOutputStream(f,true);
            PrintWriter pw = new PrintWriter(fos);
            String str = this.getName() + "," + this.getStore() + "," + this.getProductDescription() + "," +
                    this.getQuantity() + "," + this.price + "," + this.getAmountSold();
            pw.println(str);
            pw.flush();
            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println("Error writing to file.");
        }
    }

    public synchronized void editProductFile() { // updates all product info EXCEPT THE NAME
        try {
            ArrayList<String> productFileLines = new ArrayList<>();
            BufferedReader bfr = new BufferedReader(new FileReader("products.txt"));
            try {
                String line = null;
                while (true) {
                    line = bfr.readLine();
                    if (line == null) {
                        break;
                    }
                    productFileLines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                //System.out.println("File not found!");
            }

            for (int i = 0; i < productFileLines.size(); i++) {
                String[] lineData = productFileLines.get(i).split(",");
                if (lineData[0].equals(this.getName()) && lineData[1].equals(this.getStore())) {
                    lineData[2] = this.getProductDescription();
                    lineData[3] = String.valueOf(this.getQuantity());
                    lineData[4] = String.valueOf(this.getPrice());
                    lineData[5] = String.valueOf(this.getAmountSold());
                    String str = lineData[0] + "," + lineData[1] + "," + lineData[2] + "," + lineData[3] +
                            "," + lineData[4] + "," + lineData[5];
                    productFileLines.remove(productFileLines.get(i));
                    productFileLines.add(str);
                    break;
                }

            }

            PrintWriter pw = new PrintWriter(new FileOutputStream("products.txt", false));
            for (String fileLine : productFileLines) {
                pw.println(fileLine);
            }
            pw.flush();
            pw.close();

        } catch (IOException e) {

        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product) {
            return (((Product) obj).getName().equals(name)) && ((Product) obj).getStore().equals(store) &&
                    ((Product) obj).getProductDescription().equals(productDescription) &&
                    ((Product) obj).getPrice() == price && ((Product) obj).getQuantity() == quantity &&
                    ((Product) obj).getAmountSold() == this.getAmountSold();
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAmountSold(int sold) { this.sold = sold; }

    public int getAmountSold() { return sold; }

    public String toString() {
        return String.format("Product Name: %s, Store: %s, Description: %s, Price: %.2f, Stock: %d",
                name, store, productDescription, price, quantity);
    }

    // use this to get a product's info when the store is already known
    public static synchronized Product getProduct(String productName)
    {
        int nameLength = productName.length();


        try {
            BufferedReader bfr = new BufferedReader(new FileReader("products.txt"));
            String line = bfr.readLine();
            String[] productInfo = new String[6];

            while (line != null)
            {
                productInfo = line.split(",");
                if (line.substring(0, nameLength).equals(productName))
                {
                    break;
                }
                line = bfr.readLine();

            }

            String productStore = productInfo[1];
            String productDescription = productInfo[2];
            int productQuantity = Integer.parseInt(productInfo[3]);
            double productCost = Double.parseDouble(productInfo[4]);
            int productsSold = Integer.parseInt(productInfo[5]);

            return new Product(productName, productStore, productDescription, productQuantity, productCost,
                    productsSold);

        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("Exception at Product line 176");

            return null;
        }
    }

    // use this for when you need to determine if the product is a part of a store
    public static synchronized Product getProduct(String productName, String productStore)
    {
        int nameLength = productName.length();
        int storeLength = productStore.length();

        try {
            BufferedReader bfr = new BufferedReader(new FileReader("products.txt"));
            String line = bfr.readLine();
            String[] productInfo = new String[6];


            while (line != null)
            {
                productInfo = line.split(",");
                if (line.substring(0, nameLength).equals(productName) && line.substring(nameLength + 1, nameLength +
                        1 + storeLength).equals(productStore))
                {

                    break;
                }
                line = bfr.readLine();

            }

            String productDescription = productInfo[2];
            int productQuantity = Integer.parseInt(productInfo[3]);
            double productCost = Double.parseDouble(productInfo[4]);
            int productsSold = Integer.parseInt(productInfo[5]);

            return new Product(productName, productStore, productDescription, productQuantity, productCost,
                    productsSold);

        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("Exception at Product line 218");

            return null;
        }
    }

    public static synchronized ArrayList<Product> getAllProducts() {
        ArrayList<Product> temp = new ArrayList<>();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("products.txt"));
            String line = bfr.readLine();
            String[] productInfo;

            while (line != null) {
                productInfo = line.split(",");
                temp.add(getProduct(productInfo[0], productInfo[1]));
                line = bfr.readLine();
            }
            bfr.close();
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return temp;
    }

    public static ArrayList<Product> returnSearch(String search) {
        ArrayList<Product> searchArray = new ArrayList<>();

        ArrayList<Product> allProducts = getAllProducts();

        String searchStr;
        for (int i = 0; i < allProducts.size(); i++) {
            searchStr = allProducts.get(i).getName() + allProducts.get(i).getProductDescription() +
                    allProducts.get(i).getStore();
            //System.out.println(searchStr);
            if (searchStr.toLowerCase().contains(search.toLowerCase())) {
                searchArray.add(allProducts.get(i));
                //System.out.println("added " + allProducts.get(i).getName());
            }
        }

        return searchArray;
    }

    public static synchronized ArrayList<String> getAllLines() {
        ArrayList<String> temp = new ArrayList<>();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("products.txt"));
            String line = bfr.readLine();
            while (line != null) {
                temp.add(line);
                line = bfr.readLine();
            }
            bfr.close();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return temp;
    }

    public synchronized void deleteProduct() {
        ArrayList<String> lines = getAllLines();
        ArrayList<String> newLines = new ArrayList<>();
        String[] splitted;
        for (int i = 0; i < lines.size(); i++) {
            splitted = lines.get(i).split(",");
            if (splitted[0].equals(this.getName()) && splitted[1].equals(this.getStore())) {
                continue;
            } else
                newLines.add(lines.get(i));
        }

        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("products.txt", false));
            for (String fileLine : newLines) {
                pw.println(fileLine);
            }
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    }






