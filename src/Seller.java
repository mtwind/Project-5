import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * SELLER CLASS
 *
 * Seller objects can make stores and products
 *
 * @author L12 group 2
 * @version 13 NOV 2022
 */
public class Seller extends User{

    private ArrayList<Store> stores;

    public Seller(String name, String username, String password, Boolean isCustomer, Scanner s) {
        super(name, username, password, isCustomer, s);
        stores = new ArrayList<Store>();
        this.writeToFile();
    }

    // project 5 seller constructor, no scanner
    public Seller(String name, String username, String password, Boolean isCustomer, String securityAnswer, int securityQuestionNum) {
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
            System.out.println("Error with reading store file");
        }
    }


    public ArrayList<Store> getStores() {
        return stores;
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

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }

    public synchronized void editStore(Scanner s) {
        try {
            Store store = null;
            if (stores.size() == 0) {
                System.out.println("You have no stores to edit!");
            } else {
                System.out.println("Type the name of the store you wish to edit: ");
                for (int i = 0; i < stores.size(); i++) {
                    System.out.println((i + 1) + ": " + stores.get(i).getName());
                }
                String choice = null;
                String storeToEditName = null;
                boolean needInput = true;
                while (needInput) {
                    try {
                        choice = s.nextLine();
                        if (choice == null || choice.isEmpty() || choice.equals(" ")) {
                            System.out.println("Enter a valid store name!");
                        } else {
                            needInput = false;
                        }
                    } catch (Exception e) {
                        System.out.println("Enter a valid store name!");
                    }
                }
                for (int i = 0; i < stores.size(); i++) {
                    if (choice.equalsIgnoreCase(stores.get(i).getName())) {
                        storeToEditName = stores.get(i).getName();
                        store = stores.get(i);
                    }
                }
                if (storeToEditName == null) {
                    System.out.println("No store with such name exists.");
                } else {

                    String secondChoiceS;
                    int secondChoice = 0;
                    boolean needInput2 = true;
                    while (needInput2) {
                        System.out.println("What do you wish to edit about " + store.getName() + "?");
                        System.out.println("1. Add a product manually");
                        System.out.println("2. Remove a product");
                        System.out.println("3. Edit a product");
                        System.out.println("4. Go back");
                        try { // error trapping loop until user enters an integer 1-4
                            secondChoiceS = s.nextLine();
                            secondChoice = Integer.parseInt(secondChoiceS);
                            if (secondChoice <= 4 && secondChoice >= 1) {
                                needInput2 = false;
                            } else {
                                System.out.println("Please enter a number 1-4.");
                            }
                        } catch (Exception e) {
                            System.out.println("Please enter an integer 1-4.");
                        }
                    }

                    if (secondChoice == 1) { // create a new product
                        System.out.println("Create a product");
                        String name;
                        while (true) {
                            System.out.println("Name your product: ");
                            name = s.nextLine();
                            if (name.isEmpty()) {
                                System.out.println("Name cannot be empty.");
                            } else if (name.contains(",")) {
                                System.out.println("Name cannot contain commas.");
                            } else if (name.contains("~")) {
                                System.out.println("Name cannot contain '~'.");
                            } else if (name.contains("-")) {
                                System.out.println("Name cannot contain '-'");
                            } else if (store.checkRepeat(name)) {
                                System.out.println("Invalid Name, that Product already exists.");
                            } else {
                                break;
                            }
                        }
                        String description;
                        while (true) {
                            System.out.println("Create a product description: ");
                            description = s.nextLine();
                            if (description.isEmpty()) {
                                System.out.println("Description cannot be empty.");
                            } else if (description.contains(",")) {
                                System.out.println("Description cannot contain commas.");
                            } else if (description.contains("~")) {
                                System.out.println("Description cannot contain '~'.");
                            } else {
                                break;
                            }
                        }
                        boolean needInput3 = true;
                        int quantity = 0;
                        while (needInput3) { // prompting user to enter integer quantity above 0
                            try {
                                System.out.println("How many of this product do you have to sell?");
                                String quantityS = s.nextLine();
                                quantity = Integer.parseInt(quantityS);
                                if (quantity < 1) {
                                    System.out.println("Product must have a quantity larger than 0.");
                                } else {
                                    needInput3 = false;
                                }
                            } catch (Exception e) {
                                System.out.println("Enter an integer.");
                            }
                        }
                        boolean needInput4 = true;
                        double price = 0.0;
                        while (needInput4) {
                            try { // prompt user to enter double price above 0, trapping errors
                                System.out.println("What is the price of this product?");
                                String priceS = s.nextLine();
                                price = Double.parseDouble(priceS);
                                if (price <= 0) {
                                    System.out.println("Product must have a price above 0.");
                                } else {
                                    needInput4 = false;
                                }
                            } catch (Exception e) {
                                System.out.println("Enter a double.");
                            }
                        }
                        Product product = new Product(name, store.getName(), description, quantity, price);
                        ArrayList<Product> storeProducts = store.getProducts();
                        storeProducts.add(product);
                        store.setProducts(storeProducts);
                        store.editStoreFile();
                        System.out.println("Product added to " + store.getName());
                        // update stores.txt to display added product
                    } else if (secondChoice == 2) { // remove product
                        Product productToBeRemoved = null;
                        if (store.getProducts().size() == 0) {
                            System.out.println("This store has no products!");
                        } else {
                            for (int i = 0; i < store.getProducts().size(); i++) {
                                System.out.println((i + 1) + ". " + store.getProducts().get(i).getName());
                                System.out.println("Description: " +
                                        store.getProducts().get(i).getProductDescription());
                            }
                            System.out.println("Enter the name of the product you wish to remove.");
                            String response = null;
                            do {
                                try {
                                    response = s.nextLine();
                                    if (response == null || response.isEmpty() || response.equals(" ")) {
                                        System.out.println("Enter a valid product name!");
                                    } else {
                                        break;
                                    }
                                } catch (Exception e) {
                                    System.out.println("Enter a valid product name!");
                                }
                            } while (response == null || response.isEmpty() || response.equals(" "));
                            String productToBeRemovedName = null;
                            for (int i = 0; i < store.getProducts().size(); i++) {
                                if (response.equalsIgnoreCase(store.getProducts().get(i).getName())) {
                                    productToBeRemovedName = store.getProducts().get(i).getName();
                                    productToBeRemoved = store.getProducts().get(i);
                                    store.getProducts().remove(productToBeRemoved);
                                    removeProductFromCustomerCart(productToBeRemoved);
                                    store.editStoreFile();
                                }
                            }
                            if (productToBeRemovedName == null) {
                                System.out.println("No product with such name exists.");
                            } else {
                                ArrayList<String> productFileLines = readProductFile();
                                for (int i = 0; i < productFileLines.size(); i++) {
                                    String[] lineData = productFileLines.get(i).split(",");
                                    if (lineData[0].equalsIgnoreCase(productToBeRemovedName)) {
                                        productFileLines.remove(i);
                                        i -= 1;
                                    }
                                }
                                PrintWriter pw = new PrintWriter(new FileOutputStream("products.txt",
                                        false));
                                for (int i = 0; i < productFileLines.size(); i++) {
                                    pw.println(productFileLines.get(i));
                                }
                                pw.flush();
                                pw.close();
                                System.out.println("Product removed.");
                            }
                        }
                    } else if (secondChoice == 3) {
                        if (store.getProducts().size() == 0) {
                            System.out.println("This store has no products!");
                        } else {
                            for (int i = 0; i < store.getProducts().size(); i++) {
                                System.out.println((i + 1) + ". " + store.getProducts().get(i).getName());
                                System.out.println("----Description: " +
                                        store.getProducts().get(i).getProductDescription());
                                System.out.println("----Quantity: " + store.getProducts().get(i).getQuantity());
                                System.out.printf("----Price: $%.2f\n", store.getProducts().get(i).getPrice());
                            }
                            System.out.println("Enter the name of the product you wish to edit.");
                            String response = null;
                            while (true) {
                                try {
                                    response = s.nextLine();
                                    if (response == null || response.isEmpty() || response.equals(" ")) {
                                        System.out.println("Please enter a valid product name!");
                                    } else {
                                        break;
                                    }
                                } catch (Exception e) {
                                    System.out.println("Please enter a valid product name!");
                                }
                            }
                            Product oldProduct = null;
                            Product oldProduct2 = null;
                            Product productToBeEdited = null;
                            for (int i = 0; i < store.getProducts().size(); i++) {
                                if (response.equalsIgnoreCase(store.getProducts().get(i).getName())) {
                                    productToBeEdited = store.getProducts().get(i);
                                    oldProduct = store.getProducts().get(i);

                                }
                            }
                            if (productToBeEdited == null) {
                                System.out.println("No product with such name exists.");
                            } else {
                                String selectionS;
                                int selection;
                                while (true) {
                                    try {
                                        System.out.println("What do you wish to edit about " +
                                                productToBeEdited.getName() + "?");
                                        System.out.println("1. Change Name");
                                        System.out.println("2. Change Description");
                                        System.out.println("3. Increase Quantity");
                                        System.out.println("4. Change Price");
                                        System.out.println("5. Go back");
                                        selectionS = s.nextLine();
                                        selection = Integer.parseInt(selectionS);
                                        if (selection > 5 || selection < 1) {
                                            System.out.println("Please enter a number 1-5.");
                                        } else {
                                            break;
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Please enter an integer.");
                                    }
                                }
                                if (selection == 1) {
                                    ArrayList<String> productFileLines = readProductFile();
                                    String oldName = productToBeEdited.getName();
                                    String newName;
                                    while (true) {
                                        System.out.println("Enter the new name of " + productToBeEdited.getName()
                                                + ": ");
                                        newName = s.nextLine();
                                        if (newName.contains(",")) {
                                            System.out.println("Name cannot contain commas.");
                                        } else if (newName.isEmpty()) {
                                            System.out.println("Name cannot be empty.");
                                        } else if (newName.contains("~")) {
                                            System.out.println("Name cannot contain '~'.");
                                        } else if (newName.contains("-")) {
                                            System.out.println("Name cannot contain '-'.");
                                        } else {
                                            break;
                                        }
                                    }
                                    oldProduct2 = new Product(oldProduct.getName(), oldProduct.getStore(),
                                            oldProduct.getProductDescription(), oldProduct.getQuantity(),
                                            oldProduct.getPrice(), oldProduct.getAmountSold());

                                    productToBeEdited.setName(newName);

                                    for (int i = 0; i < productFileLines.size(); i++) {
                                        String[] lineData = productFileLines.get(i).split(",");
                                        if (lineData[0].equalsIgnoreCase(oldName)) {
                                            lineData[0] = newName;
                                            String str = lineData[0] + "," + lineData[1] + "," + lineData[2] + ","
                                                    + lineData[3] + "," + lineData[4] + "," + lineData[5];
                                            productFileLines.remove(productFileLines.get(i));
                                            productFileLines.add(str);
                                            break;
                                        }
                                    }

                                    PrintWriter pw = new PrintWriter(new FileOutputStream("products.txt",
                                            false));
                                    for (String fileLine : productFileLines) {
                                        pw.println(fileLine);
                                    }
                                    pw.flush();
                                    pw.close();

                                    System.out.println("Name changed to: " + productToBeEdited.getName());
                                    store.editStoreFile();
                                } else if (selection == 2) {
                                    String newDesc;
                                    while (true) {
                                        System.out.println("Enter the new description of " +
                                                productToBeEdited.getName() + ": ");
                                        newDesc = s.nextLine();
                                        if (newDesc.contains(",")) {
                                            System.out.println("The description cannot contain commas.");
                                        } else if (newDesc.isEmpty()) {
                                            System.out.println("The description cannot be empty.");
                                        } else if (newDesc.contains("~")) {
                                            System.out.println("The description cannot contain '~'.");
                                        } else {
                                            break;
                                        }
                                    }
                                    oldProduct2 = new Product(oldProduct.getName(), oldProduct.getStore(),
                                            oldProduct.getProductDescription(), oldProduct.getQuantity(),
                                            oldProduct.getPrice(), oldProduct.getAmountSold());

                                    productToBeEdited.setProductDescription(newDesc);
                                    productToBeEdited.editProductFile();
                                    System.out.println("Description changed to: " +
                                            productToBeEdited.getProductDescription());
                                    store.editStoreFile();
                                } else if (selection == 3) {
                                    String increaseQuantityS;
                                    int increaseQuantity;
                                    while (true) {
                                        try {
                                            System.out.println("How much do you want to increase the quantity of " +
                                                    productToBeEdited.getName() + " by?");
                                            increaseQuantityS = s.nextLine();
                                            increaseQuantity = Integer.parseInt(increaseQuantityS);
                                            if (increaseQuantity < 0) {
                                                System.out.println("Please enter a number equal to or above 0.");
                                            } else {
                                                break;
                                            }
                                        } catch (Exception e) {
                                            System.out.println("Please enter an integer.");
                                        }
                                    }
                                    oldProduct2 = new Product(oldProduct.getName(), oldProduct.getStore(),
                                            oldProduct.getProductDescription(), oldProduct.getQuantity(),
                                            oldProduct.getPrice(), oldProduct.getAmountSold());

                                    productToBeEdited.setQuantity(productToBeEdited.getQuantity() + increaseQuantity);
                                    productToBeEdited.editProductFile();
                                    System.out.println("Quantity of " + productToBeEdited.getName() + " increased by "
                                            + increaseQuantity + ".");
                                } else if (selection == 4) {
                                    String newPriceS;
                                    double newPrice;
                                    while (true) {
                                        try {
                                            System.out.println("What do you want to change the price of " +
                                                    productToBeEdited.getName() + " to?");
                                            newPriceS = s.nextLine();
                                            newPrice = Double.parseDouble(newPriceS);
                                            if (newPrice <= 0) {
                                                System.out.println("Please enter a price greater than 0.");
                                            } else {
                                                break;
                                            }
                                        } catch (Exception e) {
                                            System.out.println("Please enter a double.");
                                        }
                                    }
                                    oldProduct2 = new Product(oldProduct.getName(), oldProduct.getStore(),
                                            oldProduct.getProductDescription(), oldProduct.getQuantity(),
                                            oldProduct.getPrice(), oldProduct.getAmountSold());

                                    productToBeEdited.setPrice(newPrice);
                                    productToBeEdited.editProductFile();
                                    System.out.println("Price of " + productToBeEdited.getName() + " set to "
                                            + String.format("%.2f", productToBeEdited.getPrice()));
                                } else if (selection == 5) {
                                    System.out.println("Going back...");
                                }

                                updateCustomerCartForNewProduct(oldProduct2, productToBeEdited);
                            }
                        }

                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }

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

    public void exportStoreProducts(Scanner s) { // export a store's products to a csv file
        Store store = null;
        if (stores.size() == 0) {
            System.out.println("You have no stores!");
        } else {
            for (int i = 0; i < stores.size(); i++) { // print all store names
                System.out.println("Store " + (i + 1) + ": " + stores.get(i).getName());
            }
            System.out.println("Enter the name of the store whose products you want to export: ");
            String storeChoice = s.nextLine();
            for (int i = 0; i < stores.size(); i++) {
                if (storeChoice.equals(stores.get(i).getName())) {
                    store = stores.get(i);
                    break;
                }
            }
            if (store == null) {
                System.out.println("There are no stores with this name!");
            } else {
                try {
                    System.out.println("Exporting products from " + store.getName() + "...");
                    PrintWriter pw = new PrintWriter(new FileOutputStream("exported" + store.getName() +
                            "Products.csv"));
                    for (int i = 0; i < store.getProducts().size(); i++) {
                        String priceFormatted = String.format("%.2f", store.getProducts().get(i).getPrice());
                        pw.println(store.getProducts().get(i).getName() + "," + store.getProducts().get(i).getStore() +
                                "," + store.getProducts().get(i).getProductDescription() + "," +
                                store.getProducts().get(i).getQuantity() + "," + priceFormatted
                                + "," + store.getProducts().get(i).getAmountSold());
                    }
                    pw.flush();
                    pw.close();
                    System.out.println("Products exported successfully!");
                } catch (FileNotFoundException e) {
                    System.out.println("Error exporting store's products!");
                }
            }
        }

    }

    public void importStoreProducts(Scanner s) { // imports products from a file to user's stores
        if (stores.size() == 0) {
            System.out.println("You have no stores to import to!");
        } else {
            System.out.println("Enter the file name from which you want to import products: ");
            String fileName = s.nextLine();
            File f = new File(fileName);
            if (!f.exists()) {
                System.out.println("File does not exist!");
            } else {
                ArrayList<String> productImportFileLines = readCSVProductFile(fileName);
                ArrayList<String> productFileLines = readProductFile();
                if (productImportFileLines.size() == 0) {
                    System.out.println("There are no products to import in selected file!");
                } else {
                    for (int i = 0; i < productImportFileLines.size(); i++) {
                        boolean alreadyExists = false; // true if the product already exists in the user's store
                        String[] importLineData = productImportFileLines.get(i).split(",");
                        for (int j = 0; j < productFileLines.size(); j++) {
                            String[] productLineData = productFileLines.get(j).split(",");
                            if (importLineData[0].equals(productLineData[0]) &&
                                    importLineData[1].equals(productLineData[1])) {
                                System.out.println("Product already exists!");
                                alreadyExists = true; // product exists in user's store already so set true
                                break;
                            }
                        }
                        if (!alreadyExists) { // creates new product and updates the store file accordingly
                            try {
                                Product p = new Product(importLineData[0], importLineData[1], importLineData[2],
                                        Integer.parseInt(importLineData[3]), Double.parseDouble(importLineData[4]));
                                for (int x = 0; x < stores.size(); x++) {
                                    if (stores.get(x).getName().equals(p.getStore())) {
                                        stores.get(x).getProducts().add(p);
                                        stores.get(x).editStoreFile();
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println("Error parsing products!");
                            }
                        }
                    }
                    System.out.println("Import finished.");
                }
            }
        }

    }

    public static ArrayList<String> readCSVProductFile(String filename) {
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


    public static ArrayList<String> readProductFile() {
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
            System.out.println("Error reading product file!");
            return null;
        }
    }

    public static Seller parseSeller(String em) {
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
            System.out.println("Error reading file");
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
            System.out.println("error");
        }
    }

    public void writeToFile() {
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
            System.out.println("Error writing to file.");
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
                                                    "Product Description: %s   Amount Sold: %d", carts.get(j).getQuantity(),
                                            carts.get(j).getPrice(), carts.get(j).getProductDescription(),
                                            carts.get(j).getAmountSold()));
                                    built.add(str.toString());
                                }
                            }
                        }

                    }
                }
                line = bfr.readLine();
            }
        } catch (Exception e) {
            System.out.println("Error reading users file");
        }
        return built;
    }

}
