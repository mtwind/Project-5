import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;


public class Server
{
    public static void main(String[] args)
    {

        try {
            // the next lines start and accept a socket from the client and create objects to read and write to
            // the client
            Socket socket = new ServerSocket(1).accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            User user = logIn(writer, reader);

            if (user.isCustomer())
            {
                customerMenu(writer, reader, user);
            } else {
                sellerMenu(writer, reader, (Seller) user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // for logging in
    public static User logIn(PrintWriter writer, BufferedReader reader) throws Exception
    {
        boolean unknownUser = true;
        User user = null;

        // while loop can be used to log a returning user in, create a new account, or quit the program
        boolean userNameLoop = true;
        while (userNameLoop)
        {
            String email = reader.readLine();


            // if the user has an account, recreates their object from stored data
            if (User.userExists(email).equals("customer")) { //checks to see if the user exists
                user = Customer.parseCustomer(email);
                unknownUser = false;
                writer.write(String.valueOf(unknownUser));
                writer.println();
                writer.flush();
                break;
            } else if (User.userExists(email).equals("seller")) {
                user = Seller.parseSeller(email);

                unknownUser = false;
                writer.write(String.valueOf(unknownUser));
                writer.println();
                writer.flush();
                break;
            } else {
                writer.write("true");
                writer.println();
                writer.flush();

                int makeAccount = Integer.parseInt(reader.readLine());

                if (makeAccount == 1)
                {
                    // sends the user the security question based on their choice
                    int securityQuestionNum = Integer.parseInt(reader.readLine());
                    writer.write(User.questionList[securityQuestionNum - 1]);
                    writer.println();
                    writer.flush();

                    // server receives user data and creates a customer or seller object based on it
                    String[] basicData = reader.readLine().split(",");
                    int customerSeller = Integer.parseInt(basicData[2]);
                    if (customerSeller == 1) {
                        user = new Customer(basicData[0], email, basicData[1], true, basicData[3], Integer.parseInt(basicData[4]));
                    } else {
                        user = new Seller(basicData[0], email, basicData[1], false, basicData[3], Integer.parseInt(basicData[4]));
                    }
                    writer.write("false");
                    writer.println();
                    writer.flush();
                    break;
                } else {
                    int reenter = Integer.parseInt(reader.readLine());

                    if (reenter == 1){
                        writer.write("true");
                        writer.println();
                        writer.flush();
                    } else {
                        writer.write("break");
                        writer.println();
                        writer.flush();
                        break;
                    }
                }
            }
        }


        // start password checking
        boolean passwordLoop = true;
        while (passwordLoop)
        {
            String password = reader.readLine();
            if (password.equals(user.getPassword()))
            {
                writer.write("true");
                writer.println();
                writer.flush();
                passwordLoop = false;

                // happens when password is incorrect
            } else {
                writer.write("false");
                writer.println();
                writer.flush();
                int passwordChoice = Integer.parseInt(reader.readLine());

                //if they want to try again
                if (passwordChoice == 1)
                {
                    writer.write("true");
                    writer.println();
                    writer.flush();

                    //if the user wants to change their password
                } else if (passwordChoice == 2) {


                    String securityQuestion = User.questionList[user.getSecurityQuestionNumber() - 1].substring(3);
                    writer.write(securityQuestion);
                    writer.println();
                    writer.flush();

                    String answer = reader.readLine();
                    if (answer.equals(user.getSecurityAnswer()))
                    {
                        writer.write("correct");
                        writer.println();
                        writer.flush();

                        String newPassword = reader.readLine();
                        user.setPassword(newPassword);
                        user.editUserFile();
                    } else {
                        writer.write("incorrect");
                        writer.println();
                        writer.flush();
                    }

                    // when user chooses to quit
                } else if (passwordChoice == 3) {
                    writer.write("break");
                    writer.println();
                    writer.flush();
                    break;
                }
            }
        }
        return user;
    }

    // customer menu and all the methods called in it starts here
    public static void customerMenu(PrintWriter writer, BufferedReader reader, User user) throws IOException
    {
        writer.write("customer");
        writer.println();
        writer.flush();
        int menuChoice;
        boolean inLoop = true;
        while (inLoop)
        {
            menuChoice = Integer.parseInt(reader.readLine());

            switch (menuChoice)
            {
                case 1:
                    viewMarket(user, writer, reader);
                    break;
                case 2:
                    viewCart(user, writer, reader);
                    break;
                case 3:
                    String history = ((Customer) user).printPurchaseHistoryString();
                    writer.write(history);
                    writer.println();
                    writer.flush();
                    writer.write("history printed");
                    writer.println();
                    writer.flush();
                    break;
                case 4:
                    ArrayList<Store> stores = Store.getAllStores();
                    viewCostumerDash((Customer)user, stores, writer, reader);
                    break;
                case 5:
                    ((Customer)user).exportPurchaseHistory();
                    break;
                case 6:
                    user.deleteAccount();
                    menuChoice = 7;
                    inLoop = false;
                    break;
                case 7:
                    writer.write("quit");
                    writer.println();
                    writer.flush();
                    inLoop = false;
                    break;
            }
        }

    }

    // displays the market to the user
    public static void viewMarket(User user, PrintWriter writer, BufferedReader reader) throws IOException
    {
        File f = new File("products.txt");
        ArrayList<Product> products = new ArrayList<Product>();
        ArrayList<String> productStrings = new ArrayList<String>();
        ArrayList<Product> productSearch = new ArrayList<Product>();

        BufferedReader br = new BufferedReader(new FileReader(f));
        String line = br.readLine();
        String[] productSplice;
        if (!f.exists() || line == null) {
            writer.write("no products");
            writer.println();
            writer.flush();
        } else {
            writer.write("products available");
            writer.println();
            writer.flush();

            while (line != null) {
                productSplice = line.split(",");
                productStrings.add(productSplice[0] + "," + productSplice[1] + "," + productSplice[2]);
                products.add(new Product(productSplice[0], productSplice[1], productSplice[2],
                        Integer.parseInt(productSplice[3]), Double.parseDouble(productSplice[4]),
                        Integer.parseInt(productSplice[5])));
                line = br.readLine();
            }


            ArrayList<String> temp = new ArrayList<>();
            int sortChoice = Integer.valueOf(reader.readLine());

            String[] doubleSplit;

            // the following cases sort the products on the marketplace based on the user input
            switch (sortChoice) {
                // sorts by price
                case 1:
                    products = Driver.sortByPrice(products);
                    for (int i = 0; i < products.size(); i++)
                    {
                        for (int j = 0; j < productStrings.size(); j++)
                        {
                            doubleSplit = productStrings.get(j).split(",");

                            if (doubleSplit[0].equals(products.get(i).getName()) &&
                                    doubleSplit[1].equals(products.get(i).getStore()))
                            {
                                temp.add(doubleSplit[0] + doubleSplit[1] + doubleSplit[2]);
                                break;
                            }
                        }
                    }

                    for (int i = 0; i < products.size(); i++)
                    {
                        String productInfo = String.format("%d. Price: %.2f, Product: %s, Store: %s, Quantity: %d", i + 1,
                                products.get(i).getPrice(), products.get(i).getName(), products.get(i).getStore(),
                                products.get(i).getQuantity());

                        writer.write(productInfo);
                        writer.println();
                        writer.flush();
                    }

                    writer.write("no more products");
                    writer.println();
                    writer.flush();
                    break;

                // sorts by quantity available
                case 2:
                    products = Driver.sortByQuantity(products);
                    for (int i = 0; i < products.size(); i++)
                    {
                        for (int j = 0; j < productStrings.size(); j++)
                        {
                            doubleSplit = productStrings.get(j).split(",");
                            if (doubleSplit[0].equals(products.get(i).getName()) &&
                                    doubleSplit[1].equals(products.get(i).getStore()))
                            {
                                temp.add(doubleSplit[0] + doubleSplit[1] + doubleSplit[2]);
                                break;
                            }
                        }
                    }

                    for (int i = 0; i < products.size(); i++)
                    {
                        String productInfo = String.format("%d. Quantity: %d, Product: %s, Store: %s, Price: %.2f", i + 1,
                                products.get(i).getQuantity(), products.get(i).getName(),
                                products.get(i).getStore(), products.get(i).getPrice());
                        writer.write(productInfo);
                        writer.println();
                        writer.flush();
                    }
                    writer.write("no more products");
                    writer.println();
                    writer.flush();
                    break;

                // sorts by how they are listed in products.txt
                case 3:
                    temp = productStrings;

                    for (int i = 0; i < products.size(); i++)
                    {
                        String productInfo = String.format("%d. Product: %s, Store: %s, Price: %.2f, Quantity: %d", i + 1,
                                products.get(i).getName(), products.get(i).getStore(), products.get(i).getPrice(),
                                products.get(i).getQuantity());
                        writer.write(productInfo);
                        writer.println();
                        writer.flush();
                    }
                    writer.write("no more products");
                    writer.println();
                    writer.flush();

                    break;
            } // end of switch statement

            // begin part where user enters a search
            String search;
            while (true)
            {
                productSearch = new ArrayList<>();
                search = reader.readLine();

                if (search.equals("quit")) {
                    writer.write("quit");
                    writer.println();
                    writer.flush();
                    break;
                } else {
                    writer.write("continue");
                    writer.println();
                    writer.flush();
                }

                for (int i = 0; i < temp.size(); i++)
                {
                    if (temp.get(i).contains(search))
                    {
                        productSearch.add(products.get(i));
                    }
                }

                if (productSearch.size() > 0)
                {
                    writer.write("results found");
                    writer.println();
                    writer.flush();

                    do {
                        String productResults = "Product results: select a product to see its page, enter 0 to exit";
                        writer.write(productResults);
                        writer.println();
                        writer.flush();

                        for (int i = 0; i < productSearch.size(); i++) {
                            productResults = String.format("%d. Product: %s,   Store: %s,   Price: %.2f", i + 1,
                                    productSearch.get(i).getName(), productSearch.get(i).getStore(),
                                    productSearch.get(i).getPrice());
                            writer.write(productResults);
                            writer.println();
                            writer.flush();
                        }
                        writer.write("no more products");
                        writer.println();
                        writer.flush();

                        int selectedProduct = Integer.valueOf(reader.readLine());

                        if (selectedProduct == 0)
                        {
                            writer.write("no product selected");
                            writer.println();
                            writer.flush();
                            break;
                        } else {
                            Product focus = productSearch.get(selectedProduct - 1);
                            String focusString = focus.toString();

                            writer.write(focusString);
                            writer.println();
                            writer.flush();

                            int addToCartChoice = Integer.valueOf(reader.readLine());
                            if (addToCartChoice == 1)
                            {
                                ((Customer)user).addToCart(focus);

                            }
                        }
                    } while (true);
                } else {
                    writer.write("no results found");
                    writer.println();
                    writer.flush();
                }

            }


        }
    }

    // displays cart to user, allows them to purchase or remove items from cart, or clear the entire cart
    public static void viewCart(User user, PrintWriter writer, BufferedReader reader) throws IOException
    {
        ArrayList<Product> cart;
        boolean inLoop = true;
        do {
            cart = ((Customer)user).getShoppingCart();
            if (cart == null || cart.size() == 0) {
                writer.write("empty cart");
                writer.println();
                writer.flush();
            } else {
                writer.write("objects in cart");
                writer.println();
                writer.flush();

                for (int i = 0; i < cart.size(); i++)
                {
                    writer.write(cart.get(i).toString());
                    writer.println();
                    writer.flush();
                }
                writer.write("no more items in cart");
                writer.println();
                writer.flush();

                int cartChoice = Integer.parseInt(reader.readLine());
                switch (cartChoice)
                {
                    case 0: // leave the cart without doing anything
                        inLoop = false;
                        writer.write("break");
                        writer.println();
                        writer.flush();
                        break;
                    case 1:
                        ((Customer) user).purchaseProductsInCart();
                        writer.write("purchased");
                        writer.println();
                        writer.flush();
                        break;
                    case 2:
                        writer.write("remove item");
                        writer.println();
                        writer.flush();

                        while (true)
                        {
                            Product remove = null;
                            String removedItem = reader.readLine();


                            for (int i = 0; i < cart.size(); i++)
                            {
                                if (cart.get(i).getName().equals(removedItem))
                                {
                                    remove = cart.get(i);
                                    inLoop = false;
                                    break;
                                }
                            }
                            boolean removed = false;

                            if (remove != null)
                            {
                                ArrayList<Product> newCart = new ArrayList<Product>();
                                for (int i = 0; i < cart.size(); i++) {
                                    if (!cart.get(i).getName().equals(remove.getName())) {
                                        newCart.add(cart.get(i));
                                    } else if (cart.get(i).getName().equals(remove.getName()) && removed) {
                                        newCart.add(cart.get(i));
                                    } else {
                                        removed = true;
                                    }
                                }
                                ((Customer) user).setShoppingCart(newCart);
                                user.editUserFile();

                                writer.write("removed product from cart");
                                writer.println();
                                writer.flush();
                                break;
                            } else {
                                writer.write("item not found in cart");
                                writer.println();
                                writer.flush();




                                int itemNotFoundChoice = Integer.parseInt(reader.readLine());


                                if (itemNotFoundChoice == 2)
                                {
                                    writer.write("break");
                                    writer.println();
                                    writer.flush();
                                    break;
                                } else {
                                    writer.write("reenter");
                                    writer.println();
                                    writer.flush();
                                }
                            }
                        }
                        break;

                    case 3:
                        ((Customer) user).setShoppingCart(new ArrayList<Product>());
                        user.editUserFile();
                        writer.write("cart cleared");
                        writer.println();
                        writer.flush();
                        break;
                } // end of switch statement
            }

        } while (cart != null && cart.size() != 0 && inLoop);
    }

    // allows customers to view sale stores, either by total sales or sales to that individual
    public static void viewCostumerDash(Customer customer, ArrayList<Store> stores, PrintWriter writer,
                                        BufferedReader reader) throws IOException
    {
        int sortChoice = Integer.valueOf(reader.readLine());

        if (stores == null || stores.size() == 0)
        {
            writer.write("no stores");
            writer.println();
            writer.flush();
        } else {
            String sorted = "";

            switch (sortChoice)
            {
                case 1:
                    sorted = "Sorted by Store Popularity\nAll stores are listed in the following format:" +
                            "\nStore Name, Total Products Sold";
                    ArrayList<Store> sortedByProductsSold = Driver.sortByTotalProductsSold(stores);
                    for (int i = 0; i < sortedByProductsSold.size(); i++)
                    {
                        sorted += "\n" + sortedByProductsSold.get(i).getName() + ", "
                                + sortedByProductsSold.get(i).getTotalSales();
                    }
                    writer.write(sorted);
                    writer.println();
                    writer.flush();
                    writer.write("all stores printed");
                    writer.println();
                    writer.flush();
                    break;
                case 2:
                    sorted = "Sorted by Your favorite store\nAll stores are listed in the following format:" +
                            "\nStore Name, Number of Products Sold to You";
                    ArrayList<Store> sortedByProductsSoldToCustomer = Driver.sortByProductsSoldToUser(stores, customer);
                    for (int i = 0; i < sortedByProductsSoldToCustomer.size(); i++)
                    {
                        sorted += "\n" + sortedByProductsSoldToCustomer.get(i).getName() + ", "
                                + sortedByProductsSoldToCustomer.get(i).getProductsBoughtByCurrentUser(customer);
                    }
                    writer.write(sorted);
                    writer.println();
                    writer.flush();
                    writer.write("all stores printed");
                    writer.println();
                    writer.flush();
            }
        }
    }

    public static void sellerMenu(PrintWriter writer, BufferedReader reader, Seller seller) throws  IOException
    {
        writer.write("seller");
        writer.println();
        writer.flush();

        int choice = 0;
        ArrayList<Store> stores = seller.getStores();

        if (stores == null || stores.size() == 0)
        {
            writer.write("no stores");
            writer.println();
            writer.flush();
        } else {
            writer.write("Stores:");
            writer.println();
            writer.flush();
            for (int i = 0; i < stores.size(); i++)
            {
                writer.write((i + 1) + ": " + stores.get(i).getName());
                writer.println();
                writer.flush();

            }
        }

        writer.write("------------------");
        writer.println();
        writer.flush();

        int sellerMenuChoice = -1;

        do {
            sellerMenuChoice = Integer.valueOf(reader.readLine());

            String name;
            ArrayList<Store> temp;
            boolean validName = false, broken = false;

            switch (sellerMenuChoice) {
                // adding a store
                case 1:
                    String newStoreName;

                    do {
                        newStoreName = reader.readLine();

                        BufferedReader bfr = new BufferedReader(new FileReader("stores.txt"));
                        String line = bfr.readLine();
                        String[] splitted;
                        while (line != null) {
                            splitted = line.split(",");
                            if (splitted[0].equals(newStoreName)) {
                                writer.write("name taken");
                                writer.println();
                                writer.flush();
                                broken = true;
                                break;
                            }
                            line = bfr.readLine();
                        }
                        if (!broken) {
                            validName = true;
                        } else
                            broken = false;

                    } while (!validName);

                    temp = seller.getStores();
                    temp.add(new Store(seller.getEmail(), newStoreName, new ArrayList<Product>()));
                    seller.setStores(temp);
                    writer.write("store added");
                    writer.println();
                    writer.flush();
                    seller.editUserFile();
                    break;

                // remove a store
                case 2:
                    if (stores != null && stores.size() > 0)
                    {
                        writer.write("stores found");
                        writer.println();
                        writer.flush();

                        do {
                            temp = seller.getStores();

                            String removedStoreName = reader.readLine();

                            if (removedStoreName.equals("quit")) {
                                break;
                            }

                            boolean removed = false;
                            for (int i = 0; i < temp.size(); i++) {
                                if (stores.get(i).getName().equals(removedStoreName)) {
                                    temp.remove(i);
                                    removed = true;
                                    break;
                                }
                            }

                            if (removed) {

                                writer.write("store removed");
                                writer.println();
                                writer.flush();

                                seller.setStores(temp);
                                seller.editUserFile();
                                seller.removeStoreFromFile(removedStoreName);

                                ArrayList<String> fileLines = new ArrayList<>();
                                BufferedReader bfr = new BufferedReader(new FileReader("products.txt"));
                                String[] split;
                                String line = bfr.readLine();
                                while (line != null) {
                                    split = line.split(",");
                                    if (split[1].equals(removedStoreName)) {
                                        line = bfr.readLine();
                                        continue;
                                    } else {
                                        fileLines.add(line);
                                    }
                                    line = bfr.readLine();
                                }

                                PrintWriter pw = new PrintWriter(new FileOutputStream("products.txt", false));
                                for (int i = 0; i < fileLines.size(); i++) {
                                    pw.println(fileLines.get(i));
                                }
                                pw.flush();
                                pw.close();
                                break;


                            } else {
                                writer.write("store not found");
                                writer.println();
                                writer.flush();
                            }
                        } while (true);

                    } else {
                        writer.write("no stores");
                        writer.println();
                        writer.flush();
                        break;
                    }
                    break;

                // edit a store
                case 3:

                    // view dashboard
                case 4:
                    viewSellerDash(seller, stores, writer, reader);
                    break;

                // Export store's products to a file
                case 5:
                    exportStoreProducts(seller, writer, reader);
                    break;

                // Import products from stores to a file
                case 6:
                    importStoreProducts(seller, writer, reader);
                    break;

                // View products in carts
                case 7:
                    viewCustomerCarts(seller, writer, reader);
                    break;

                // delete an account
                case 8:
                    writer.write("Account Deleted");
                    writer.println();
                    writer.flush();
                    seller.deleteAccount();
                    sellerMenuChoice = 9;
                    break;

                // exit
                case 9:
                    break;
            }
        } while (sellerMenuChoice != 9);

    }

    public static void viewSellerDash(Seller user, ArrayList<Store> stores, PrintWriter writer, BufferedReader reader)
            throws IOException
    {
        if (stores == null || stores.size() == 0) {
            writer.write("no stores");
            writer.println();
            writer.flush();
        } else {
            writer.write("stores");
            writer.println();
            writer.flush();

            int dashChoice = Integer.parseInt(reader.readLine());

            switch (dashChoice)
            {
                case 1:
                    stores = Driver.sortBySales(stores);
                    break;
                case 2:
                    stores = Driver.sortByNumProducts(stores);
                case 3:
                    break;
            }

            writer.write("---------------------------");
            writer.println();
            writer.flush();

            for (int i = 0; i < stores.size(); i++)
            {


                writer.write("Store: " + stores.get(i).getName());
                writer.println();
                writer.flush();

                String revenue = String.format("Revenue: $%.2f", stores.get(i).getSales());
                writer.write(revenue);
                writer.println();
                writer.flush();

                if (stores.get(i).getProducts() != null && stores.get(i).getProducts().size() > 0)
                {
                    writer.write("Products: ");
                    writer.println();
                    writer.flush();
                    for (int j = 0; j < stores.get(i).getProducts().size(); j++)
                    {
                        String numSold = "- " + stores.get(i).getProducts().get(j).getName() + ". Number Sold: " +
                                stores.get(i).getProducts().get(j).getAmountSold();
                        writer.write(numSold);
                        writer.println();
                        writer.flush();
                    }
                } else {
                    writer.write("This store has no products");
                    writer.println();
                    writer.flush();
                }

                if (stores.get(i).getCustomers() != null && stores.get(i).getCustomers().size() != 0)
                {
                    writer.write("Customers: ");
                    writer.println();
                    writer.flush();
                    for (int j = 0; j < stores.get(i).getCustomers().size(); j++)
                    {
                        String numBoughtByCustomer = "- " + stores.get(i).getCustomers().get(j).getName() + ". Products sold to: "
                                + stores.get(i).getCustomerSales().get(j);
                        writer.write(numBoughtByCustomer);
                        writer.println();
                        writer.flush();
                    }
                } else {
                    writer.write("This store has no Customers");
                    writer.println();
                    writer.flush();
                }

                writer.write("---------------------------");
                writer.println();
                writer.flush();
            } // end for loop for printing stores

            writer.write("all done");
            writer.println();
            writer.flush();
        }
    }

    public static void exportStoreProducts(Seller seller, PrintWriter writer, BufferedReader reader) throws IOException
    {
        Store store = null;
        if (seller.getStores().size() == 0) {
            writer.write("no stores");
            writer.println();
            writer.flush();
        } else {
            writer.write("stores found");
            writer.println();
            writer.flush();
            for (int i = 0; i < seller.getStores().size(); i++)
            { // print all store names
                String storeNames = "Store " + (i + 1) + ": " + seller.getStores().get(i).getName();
                writer.write(storeNames);
                writer.println();
                writer.flush();
            }

            writer.write("all stores printed");
            writer.println();
            writer.flush();

            String exportedStore = reader.readLine();

            for (int i = 0; i < seller.getStores().size(); i++) {
                if (exportedStore.equals(seller.getStores().get(i).getName())) {
                    store = seller.getStores().get(i);
                    break;
                }
            }
            if (store == null) {
                writer.write("store not found");
                writer.println();
                writer.flush();
            } else {
                try {
                    writer.write("Exporting products from " + store.getName() + "...");
                    writer.println();
                    writer.flush();
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
                    writer.write("Products exported successfully!");
                    writer.println();
                    writer.flush();
                } catch (FileNotFoundException e) {
                    System.out.println("Error exporting store's products!");
                }
            }
        }
    }

    public static void importStoreProducts(Seller seller, PrintWriter writer, BufferedReader reader) throws IOException
    {
        Store store = null;
        if (seller.getStores().size() == 0)
        {
            writer.write("no stores");
            writer.println();
            writer.flush();
        } else {
            writer.write("stores found");
            writer.println();
            writer.flush();

            String fileName = reader.readLine();
            File f = new File(fileName);
            if (!f.exists())
            {
                writer.write("file not found");
                writer.println();
                writer.flush();
            } else {
                writer.write("File found");
                writer.println();
                writer.flush();

                ArrayList<String> productImportFileLines = seller.readCSVProductFile(fileName);
                ArrayList<String> productFileLines = seller.readProductFile();
                if (productImportFileLines.size() == 0)
                {
                    writer.write("The file has no products to import");
                    writer.println();
                    writer.flush();
                } else {
                    for (int i = 0; i < productImportFileLines.size(); i++)
                    {
                        boolean alreadyExists = false; // true if the product already exists in the user's store
                        String[] importLineData = productImportFileLines.get(i).split(",");
                        for (int j = 0; j < productFileLines.size(); j++) {
                            String[] productLineData = productFileLines.get(j).split(",");
                            if (importLineData[0].equals(productLineData[0]) &&
                                    importLineData[1].equals(productLineData[1])) {
                                writer.write("Product already exists");
                                writer.println();
                                writer.flush();
                                alreadyExists = true; // product exists in user's store already so set true
                                break;
                            }
                        }
                        if (!alreadyExists) { // creates new product and updates the store file accordingly
                            try {
                                Product p = new Product(importLineData[0], importLineData[1], importLineData[2],
                                        Integer.parseInt(importLineData[3]), Double.parseDouble(importLineData[4]));
                                for (int x = 0; x < seller.getStores().size(); x++) {
                                    if (seller.getStores().get(x).getName().equals(p.getStore())) {
                                        seller.getStores().get(x).getProducts().add(p);
                                        seller.getStores().get(x).editStoreFile();
                                    }
                                }
                            } catch (Exception e) {
                                writer.write("Error parsing products, make sure that the file is correctly formatted.");
                                writer.println();
                                writer.flush();

                            }
                        }
                    }
                    writer.write("Import finished");
                    writer.println();
                    writer.flush();
                }
            }
        }

    }

    public static void viewCustomerCarts(Seller seller, PrintWriter writer, BufferedReader reader) throws IOException
    {
        Customer c;
        ArrayList<Product> carts;
        int totalInCarts = 0;

        BufferedReader bfr = new BufferedReader(new FileReader("users.txt"));
        String line = bfr.readLine();
        String[] parse;
        while (line != null) {
            parse = line.split(",");
            if (!parse[0].equals("customer"))
            {
                line = bfr.readLine();
                continue;
            } else {
                c = Customer.parseCustomer(parse[1]);
                carts = c.getShoppingCart();
                if (carts == null || carts.size() == 0) {
                    line = bfr.readLine();
                    continue;
                } else {
                    for (int i = 0; i < seller.getStores().size(); i ++)
                    {
                        for (int j = 0; j < carts.size(); j++)
                        {
                            if (carts.get(j).getStore().equals(seller.getStores().get(i).getName()))
                            {
                                totalInCarts++;
                                writer.write("-------------------------------------------------------------");
                                writer.println();
                                writer.flush();

                                String customerInfo = String.format("Customer: %s,   Store: %s,   Product: %s   ",
                                        c.getEmail(), seller.getStores().get(i).getName(), carts.get(j).getName());
                                writer.write(customerInfo);
                                writer.println();
                                writer.flush();


                                String productInfo = String.format("Product Quantity: %d,   Product Price: $%.2f,   " +
                                                "Product Description: %s,   Amount Sold: %d",
                                        carts.get(j).getQuantity(), carts.get(j).getPrice(),
                                        carts.get(j).getProductDescription(), carts.get(j).getAmountSold());
                                writer.write(productInfo);
                                writer.println();
                                writer.flush();

                            }
                        }
                    }
                    writer.write("-------------------------------------------------------------");
                    writer.println();
                    writer.flush();
                }
            }
            line = bfr.readLine();
        }
        writer.write("Total number of products in carts: " + totalInCarts);
        writer.println();
        writer.flush();

        writer.write("all done");
        writer.println();
        writer.flush();
    }

}
