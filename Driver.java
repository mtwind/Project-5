import java.io.*;
import java.util.*;
/**
 * DRIVER CLASS
 *
 * Contains the main method for the project
 * Allows users to log in and navigate the customer and seller menus
 *
 * @author L12 group 2
 * @version 13 NOV 2022
 */
public class Driver {
    public static void main(String[] args) {
        User user = null;
        Scanner s = new Scanner(System.in); //declares scanner for console input
        checkIfFilesExist();

        boolean unknownUser = true;
        do {
            System.out.println("Enter email: ");
            String username = s.nextLine(); //stores the username as a string. if it does not exist, asks to
            // create an account
            int choice = 0;
            if (User.userExists(username).equals("customer")) { //checks to see if the user exists
                user = Customer.parseCustomer(username);

                unknownUser = false;
            } else if (User.userExists(username).equals("seller")) {
                user = Seller.parseSeller(username);
                unknownUser = false;
            } else { //asks to create a new account
                System.out.println("Would you like to create an account? 1 = yes, 2 = no");
                do {
                    try {
                        choice = s.nextInt();
                        if (choice < 1 || choice > 2) { // if the choice is not valid it makes them try again.
                            s.nextLine();
                            System.out.println("Please enter valid input.");
                            continue;
                        }
                        s.nextLine();
                        break;
                    } catch (Exception e) {
                        s.nextLine();
                        System.out.println("Please enter an integer.");
                    }
                } while (true);
                if (choice == 1) { //case for yes
                    String name, password;

                    System.out.println("What is your name?");
                    name = s.nextLine();
                    System.out.println("Enter a password: ");
                    password = s.nextLine();
                    System.out.println("Customer or Seller? 1 = Customer, 2 = Seller");
                    do {
                        try {
                            choice = s.nextInt();
                            if (choice < 1 || choice > 2) {
                                s.nextLine();
                                System.out.println("Please enter valid input.");
                                continue;
                            }
                            s.nextLine();
                            break;
                        } catch (Exception e) {
                            s.nextLine();
                            System.out.println("Please enter an integer.");
                        }
                    } while (true);

                    if (choice == 1) {
                        user = new Customer(name, username, password, true, s);
                    } else
                        user = new Seller(name, username, password, false, s);
                    unknownUser = false;

                } else { //case for no. check if they want to try reentering (mistype?)
                    System.out.println("Would you like to reenter an email? 1 = yes, 2 = no");
                    do {
                        try {
                            choice = s.nextInt();
                            if (choice < 1 || choice > 2) {
                                s.nextLine();
                                System.out.println("Please enter valid input.");
                                continue;
                            }
                            s.nextLine();
                            break;
                        } catch (Exception e) {
                            s.nextLine();
                            System.out.println("Please enter an integer.");
                        }
                    } while (true);

                    if (choice == 2) {
                        break;
                    }
                }
            }

        } while (unknownUser);//runs until a user is verified as existing / created or until the user quits

        if (unknownUser) { //if they choose not to create an account and to not retry, leaves
            System.out.println("bye");
        } else { //else, makes them log in
            boolean verified = false;
            int iteration = 0;
            int choice;
            String password;
            do { //loops until they log in or until they quit
                if (iteration > 0) { //lists error message if not the first iteration
                    System.out.println("Incorrect credentials.\n1. Try again\n2. Change Password\n3. Quit");
                    do {
                        try {
                            choice = Integer.parseInt(s.nextLine());
                            if (choice < 1 || choice > 3) {
                                System.out.println("Please enter valid input.");
                                continue;
                            }
                            break;
                        } catch (Exception e) {
                            System.out.println("Please enter an integer.");
                        }
                    } while (true);

                    if (choice == 3)
                        break;

                    if (choice == 2 ) {
                        int questionNum = user.getSecurityQuestionNumber();
                        System.out.println(user.getQuestionList()[questionNum - 1].substring(3));
                        String answer = s.nextLine();
                        if (answer.equals(user.getSecurityAnswer()))
                        {
                            System.out.println("Success!\nEnter new password: ");
                            String newPassword = s.nextLine();
                            user.setPassword(newPassword);
                            user.editUserFile();
                        }
                    }
                }
                System.out.println("Enter your password to log in: ");
                password = s.nextLine();
                verified = user.passwordCheck(password);
                iteration++;
            } while (!verified);
            if (!verified) { // stops if the user gives up trying to enter their password
                /*
                how to improve: edit password
                 */
                System.out.println("bye");
            } else { //when logged in, sends to correct menu based on user type
                if (user instanceof Customer) {

                    customerMenu(s, user);
                } else if (user instanceof Seller) {
                    sellerMenu(s, (Seller) user);
                }
            }
        }
    }

    public static void customerMenu(Scanner s, User user) {
        int choice = 0;
        String options = "1. View the marketplace\n2. View cart\n3. View Purchase History\n4. View Dashboard\n" +
                "5. Export Purchase History to a file\n6. Delete Account\n7. Quit";
        boolean invalid = true;
        do {
            System.out.println(options);
            do {
                try {
                    choice = Integer.parseInt(s.nextLine());
                    if (choice < 1 || choice > 7) {
                        System.out.println("Please enter a number 1 - 7. ");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number. ");
                }
            } while (true);

            switch (choice) {
                case 1:
                    viewMarket(s, (Customer)user);
                    break;
                case 2:
                    viewCart(s, (Customer)user);
                    break;
                case 3:
                    ((Customer) user).printPurchaseHistoryString();
                    break;
                case 4:
                    ArrayList<Store> stores = Store.getAllStores();
                    viewCostumerDash(s, stores, (Customer) user);
                    break;
                case 5:
                    ((Customer)user).exportPurchaseHistory();
                    break;
                case 6:
                    user.deleteAccount();
                    choice = 7;
                case 7:
                    System.out.println("goodbye!");
                    break;
            }
        } while (choice != 7);
        System.out.println("Logging out...");
    }
    /**
     * sellerMenu()
     * loops until the seller is done. when done, logs out.
     * lists all options available to the customer and connects them to the correct methods
     *
     */
    public static void sellerMenu(Scanner s, Seller user) {
        //sort
        int choice = 0;
        ArrayList<Store> stores = user.getStores();
        if (stores == null || stores.size() == 0) {
            System.out.println("You currently have no stores.");
        } else {
            System.out.println("Stores: ");
            for (int i = 0; i < stores.size(); i++) {
                System.out.println((i+1)+ ": " + stores.get(i).getName());
            }
            System.out.println("------------------");
        }

        String options = "1. Add a store\n2. Remove a store\n3. Edit a store\n4. View Dashboard\n" +
                "5. Export a store's products to a file\n6. Import products for stores from a file\n7. " +
                "View products in" + " carts\n" + "8. Delete Account\n9. Exit";
        do {
            System.out.println(options);
            do {
                try {
                    choice = Integer.parseInt(s.nextLine());
                    if (choice < 1 || choice > 9) {
                        System.out.println("Please enter a number 1 - 9. ");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number. ");
                }
            } while (true);

            String name;
            ArrayList<Store> temp;
            boolean validName = false, broken = false;
            switch (choice) {
                case 1:
                    System.out.println("What is the name of the store?");
                    do {
                        name = s.nextLine();
                        try {
                            BufferedReader bfr = new BufferedReader(new FileReader("stores.txt"));
                            String line = bfr.readLine();
                            String[] splitted;
                            while (line != null) {
                                splitted = line.split(",");
                                if (splitted[0].equals(name)) {
                                    System.out.println("this store name is taken. enter another name.");
                                    broken = true;
                                    break;
                                }
                                line = bfr.readLine();
                            }
                            if (!broken) {
                                validName = true;
                            } else
                                broken = false;
                        } catch (IOException e) {
                            System.out.println("Error reading stores file.");
                        }
                    } while (!validName);
                    temp = user.getStores();
                    temp.add(new Store(user.getEmail(), name, new ArrayList<Product>()));
                    user.setStores(temp);
                    System.out.println("Store added: to add products, go to edit store.");
                    user.editUserFile();
                    break;
                case 2:
                    if (stores != null && stores.size() > 0) {
                        temp = user.getStores();
                        System.out.println("Enter the name of the store you want to remove: ");
                        name = s.nextLine();
                        boolean removed = false;
                        for (int i = 0; i < temp.size(); i++) {
                            if (stores.get(i).getName().equals(name)) {
                                temp.remove(i);
                                removed = true;
                                break;
                            }
                        }
                        if (removed) {
                            System.out.println("The store was removed.");
                            user.setStores(temp);
                            user.editUserFile();
                            user.removeStoreFromFile(name);
                            try {
                                ArrayList<String> fileLines = new ArrayList<>();
                                BufferedReader bfr = new BufferedReader(new FileReader("products.txt"));
                                String[] split;
                                String line = bfr.readLine();
                                while (line != null) {
                                    split = line.split(",");
                                    if (split[1].equals(name)) {
                                        line = bfr.readLine();
                                        continue;
                                    } else {
                                        fileLines.add(line);
                                    }
                                    line = bfr.readLine();
                                }

                                PrintWriter pw = new PrintWriter(new FileOutputStream("products.txt",
                                        false));
                                for (int i = 0; i < fileLines.size(); i++) {
                                    pw.println(fileLines.get(i));
                                }
                                pw.flush();
                                pw.close();
                            } catch (Exception e) {
                                System.out.println("Error with products file");
                            }

                        } else
                            System.out.println("The store you entered does not exist.");
                    } else {
                        System.out.println("you have no stores to remove.");
                    }
                    break;
                case 3:
                    user.editStore(s);
                    break;
                case 4:
                    viewSellerDash(s, stores);
                    break;
                case 5:
                    user.exportStoreProducts(s);
                    break;
                case 6:
                    user.importStoreProducts(s);
                    break;
                case 7:
                    user.viewCustomerCarts(s);
                    break;
                case 8:
                    user.deleteAccount();
                    choice = 9;
                case 9:
                    System.out.println("goodbye!");
                    break;
            }
        } while (choice != 9);
        System.out.println("Logging out...");
    }

    public static void viewMarket(Scanner s, Customer user) {
        File f = new File("products.txt");
        ArrayList<Product> products = new ArrayList<Product>();
        ArrayList<String> productStrings = new ArrayList<String>();
        ArrayList<Product> productSearch = new ArrayList<Product>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            String[] productSplice;
            if (!f.exists() || line == null) {
                System.out.println("There are no products in the marketplace.");
            } else {
                while (line != null) {
                    productSplice = line.split(",");
                    productStrings.add(productSplice[0] + "," + productSplice[1] + "," + productSplice[2]);
                    products.add(new Product(productSplice[0], productSplice[1], productSplice[2],
                            Integer.parseInt(productSplice[3]), Double.parseDouble(productSplice[4]),
                            Integer.parseInt(productSplice[5])));
                    line = br.readLine();
                }

                ArrayList<String> temp = new ArrayList<>();
                System.out.println("Sort the list:\n1. By Price\n2. By Quantity\n0. No Sort");
                int z;
                do {
                    try {
                        z = Integer.parseInt(s.nextLine());
                        if (z < 0 || z > 2) {
                            System.out.println("Please enter valid input");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a number.");
                    }
                } while (true);
                String[] doubleSplit;
                switch (z) {
                    case 0:
                        temp = productStrings;

                        for (int i = 0; i < products.size(); i++) {
                            System.out.printf("%d. Product: %s, Store: %s, Price: %.2f, Quantity: %d\n", i + 1,
                                    products.get(i).getName(), products.get(i).getStore(), products.get(i).getPrice(),
                                    products.get(i).getQuantity());

                        }

                        break;
                    case 1:
                        products = sortByPrice(products);
                        for (int i = 0; i < products.size(); i++) {
                            for (int j = 0; j < productStrings.size(); j++) {
                                doubleSplit = productStrings.get(j).split(",");
                                if (doubleSplit[0].equals(products.get(i).getName()) &&
                                        doubleSplit[1].equals(products.get(i).getStore())) {
                                    temp.add(doubleSplit[0] + doubleSplit[1] + doubleSplit[2]);
                                    break;
                                }
                            }
                        }

                        for (int i = 0; i < products.size(); i++) {
                            System.out.printf("%d. Price: %.2f, Product: %s, Store: %s, Quantity: %d\n", i + 1,
                                    products.get(i).getPrice(), products.get(i).getName(), products.get(i).getStore(),
                                    products.get(i).getQuantity());

                        }
                        break;
                    case 2:
                        products = sortByQuantity(products);
                        for (int i = 0; i < products.size(); i++) {
                            for (int j = 0; j < productStrings.size(); j++) {
                                doubleSplit = productStrings.get(j).split(",");
                                if (doubleSplit[0].equals(products.get(i).getName()) &&
                                        doubleSplit[1].equals(products.get(i).getStore())) {
                                    temp.add(doubleSplit[0] + doubleSplit[1] + doubleSplit[2]);
                                    break;
                                }
                            }
                        }

                        for (int i = 0; i < products.size(); i++) {
                            System.out.printf("%d. Quantity: %d, Product: %s, Store: %s, Price: %.2f\n", i + 1,
                                    products.get(i).getQuantity(), products.get(i).getName(),
                                    products.get(i).getStore(), products.get(i).getPrice());
                        }
                        break;
                }


                String search;
                while (true) {
                    productSearch = new ArrayList<>();
                    System.out.println("Search for a product: enter 'quit' to exit the search");
                    search = s.nextLine();
                    if (search.equals("quit")) {
                        break;
                    }
                    for (int i = 0; i < temp.size(); i++) {
                        if (temp.get(i).contains(search)) {
                            productSearch.add(products.get(i));
                        }
                    }
                    int choice;

                    if (productSearch.size() > 0) {
                        do {
                            System.out.println("Product results: select a product to see its page, enter 0 to exit");
                            for (int i = 0; i < productSearch.size(); i++) {
                                System.out.printf("%d. Product: %s,   Store: %s,   Price: %.2f\n", i + 1,
                                        productSearch.get(i).getName(), productSearch.get(i).getStore(),
                                        productSearch.get(i).getPrice());
                            }

                            do {
                                try {
                                    choice = Integer.parseInt(s.nextLine());
                                    break;
                                } catch (NumberFormatException e) {
                                    System.out.println("Please enter a number.");
                                }
                            } while (true);

                            while (choice < 0 || choice > productSearch.size()) {
                                System.out.println("Please enter a number 0 - " + productSearch.size());
                                do {
                                    try {
                                        choice = Integer.parseInt(s.nextLine());
                                        break;
                                    } catch (NumberFormatException e) {
                                        System.out.println("Please enter a number.");
                                    }
                                } while (true);
                            }
                            if (choice == 0) {
                                break;
                            } else {
                                Product focus = productSearch.get(choice - 1);
                                System.out.println(focus.toString());
                                System.out.println("Add to cart? 1 = yes, 2 = no");
                                int c;
                                do {
                                    try {
                                        c = Integer.parseInt(s.nextLine());
                                        if (c != 1 && c != 2) {
                                            System.out.println("Please enter valid input.");
                                            continue;
                                        }
                                        break;
                                    } catch (NumberFormatException e) {
                                        System.out.println("Please enter a number.");
                                    }
                                } while (true);
                                if (c == 1) {
                                    user.addToCart(focus);
                                }
                            }
                        } while (true);
                    } else {
                        System.out.println("There were no results aligning with your search.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewCart(Scanner s, Customer user) {
        ArrayList<Product> cart;
        boolean going = true;
        do {
            cart = user.getShoppingCart();
            if (cart == null || cart.size() == 0) {
                System.out.println("There is nothing in your cart.");
            } else {
                System.out.println("Shopping cart: ");
                for (int i = 0; i < cart.size(); i++) {
                    System.out.println(cart.get(i).toString());
                }

                System.out.println("Shopping cart options: enter 0 to quit.");
                System.out.println("1. Purchase your cart\n2. Remove an item from your cart\n3. Clear your cart");
                int p;
                do {
                    try {
                        p = Integer.parseInt(s.nextLine());
                        if (p < 0 || p > 3) {
                            System.out.println("Please enter valid input.");
                            continue;
                        }
                        break;

                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a number.");
                    }

                } while (true);
                switch (p) {
                    case 0:
                        going = false;
                        break;
                    case 1:
                        user.purchaseProductsInCart();
                        break;
                    case 2:
                        do {
                            Product remove = null;
                            System.out.println("Enter the item you wish to remove: ");
                            String item = s.nextLine();
                            for (int i = 0; i < cart.size(); i++) {
                                if (cart.get(i).getName().equals(item)) {
                                    remove = cart.get(i);
                                    break;
                                }
                            }
                            boolean removed = false;
                            if (remove != null) {
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
                                user.setShoppingCart(newCart);
                                user.editUserFile();
                                break;
                            } else {
                                int q;
                                System.out.println("that is not in the cart. Reenter? 1 = yes, 2 = no");
                                do {
                                    try {
                                        q = Integer.parseInt(s.nextLine());
                                        if (q != 1 && q != 2) {
                                            System.out.println("Please enter valid input.");
                                            continue;
                                        }
                                        break;
                                    } catch (NumberFormatException e) {
                                        System.out.println("Please enter a number.");
                                    }
                                } while (true);
                                if (q == 2) {
                                    break;
                                }
                            }
                        } while (true);
                        break;
                    case 3:
                        user.setShoppingCart(new ArrayList<Product>());
                        user.editUserFile();
                        break;
                }
            }
        } while (cart != null && cart.size() != 0 && going);
    }

    public static void viewSellerDash(Scanner s, ArrayList<Store> stores) {
        if (stores == null || stores.size() == 0) {
            System.out.println("You have no stores or sales!");
        } else {
            System.out.println("Sort the list:\n1. By Revenue\n2. By Number of Products\n0. No Sort");
            int z;
            do {
                try {
                    z = Integer.parseInt(s.nextLine());
                    if (z < 0 || z > 2) {
                        System.out.println("Please enter valid input");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number.");
                }
            } while (true);
            switch (z) {
                case 0:
                    break;
                case 1:
                    stores = sortBySales(stores);
                    break;
                case 2:
                    stores = sortByNumProducts(stores);
                    break;
            }
            for (int i = 0; i < stores.size(); i++) {
                System.out.println("---------------------------");
                System.out.println("Store: " + stores.get(i).getName());

                System.out.printf("Revenue: $%.2f\n", stores.get(i).getSales());
                if (stores.get(i).getProducts() != null && stores.get(i).getProducts().size() > 0) {
                    System.out.println("Products: ");
                    for (int j = 0; j < stores.get(i).getProducts().size(); j++) {
                        System.out.println(stores.get(i).getProducts().get(j).getName() + ". Number Sold: " +
                                stores.get(i).getProducts().get(j).getAmountSold());
                    }
                } else {
                    System.out.println("There are no products in this store!");
                }
                if (stores.get(i).getCustomers() != null && stores.get(i).getCustomers().size() != 0) {
                    System.out.println("Customers: ");
                    for (int j = 0; j < stores.get(i).getCustomers().size(); j++) {
                        System.out.println(stores.get(i).getCustomers().get(j).getName() + ". Number Bought: "
                                + stores.get(i).getCustomerSales().get(j));
                    }
                } else {
                    System.out.println("There are no customers in this store!");
                }
                System.out.println("---------------------------");
            }
        }
    }


    public static void viewCostumerDash(Scanner s, ArrayList<Store> stores, Customer customer)
    {
        System.out.println("How do you want to the stores to be sorted?");
        System.out.println("1. By Most Popular\n2. By Your Favorite");
        boolean keepLooping = true;
        int choice = 0;

        do {
            try {
                choice = Integer.parseInt(s.nextLine());
                if (choice < 1 || choice > 2) {
                    System.out.println("please enter valid input");
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("please enter a number");
            }
        } while (true);

        switch (choice)
        {
            case 1:
                System.out.println("Sorted by Store Popularity");
                if (stores == null || stores.size() == 0) {
                    System.out.println("There are no stores in the market!");
                } else {
                    System.out.println("All stores are listed in the following format:");
                    System.out.println("Store Name, Total Products Sold");
                    ArrayList<Store> sortedByProductsSold = sortByTotalProductsSold(stores);
                    for (int i = 0; i < sortedByProductsSold.size(); i++) {
                        System.out.println(sortedByProductsSold.get(i).getName() + ", "
                                + sortedByProductsSold.get(i).getTotalSales());
                    }
                }
                break;
            case 2:
                System.out.println("Sorted by Your Favorite Stores");
                if (stores == null || stores.size() == 0) {
                    System.out.println("There are no stores in the market!");
                } else {
                    System.out.println("All stores are listed in the following format:");
                    System.out.println("Store Name, Products You Have Bought");
                    ArrayList<Store> sortedByProductsSoldToCustomer = sortByProductsSoldToUser(stores, customer);
                    for (int i = 0; i < sortedByProductsSoldToCustomer.size(); i++) {
                        System.out.println(sortedByProductsSoldToCustomer.get(i).getName() + ", "
                                + sortedByProductsSoldToCustomer.get(i).getProductsBoughtByCurrentUser(customer));
                    }
                }
                break;
        }
    }




    public static ArrayList<Product> sortByQuantity(ArrayList<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            int max = i;
            for (int j = i + 1; j < products.size(); j++) {
                if (products.get(j).getQuantity() > products.get(max).getQuantity())
                    max = j;
            }

            // Swap max (highest num) to current position on array
            Product swap = products.get(i);
            products.set(i, products.get(max));
            products.set(max, swap);
        }
        return products;
    }
    public static ArrayList<Product> sortByPrice(ArrayList<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            int min = i;
            for (int j = i + 1; j < products.size(); j++) {
                if (products.get(j).getPrice() < products.get(min).getPrice())
                    min = j;
            }

            // Swap max (highest num) to current position on array
            Product swap = products.get(i);
            products.set(i, products.get(min));
            products.set(min, swap);
        }

        return products;
    }



    public static ArrayList<Store> sortBySales(ArrayList<Store> stores) {
        for (int i = 0; i < stores.size(); i++) {
            int max = i;
            for (int j = i + 1; j < stores.size(); j++) {
                if (stores.get(j).getSales() > stores.get(max).getSales())
                    max = j;
            }

            // Swap max (highest num) to current position on array
            Store swap = stores.get(i);
            stores.set(i, stores.get(max));
            stores.set(max, swap);
        }
        return stores;
    }
    public static ArrayList<Store> sortByNumProducts(ArrayList<Store> stores) {
        for (int i = 0; i < stores.size(); i++) {
            int max = i;
            for (int j = i + 1; j < stores.size(); j++) {
                if (stores.get(j).getProducts().size() > stores.get(max).getProducts().size())
                    max = j;
            }
            // Swap max (highest num) to current position on array
            Store swap = stores.get(i);
            stores.set(i, stores.get(max));
            stores.set(max, swap);
        }
        return stores;
    }

    public static ArrayList<Store> sortByTotalProductsSold(ArrayList<Store> stores)
    {
        for (int i = 0; i < stores.size(); i++)
        {

            int max = i;
            for (int j = i + 1; j < stores.size(); j++)
            {
                if (stores.get(j).getTotalSales() > stores.get(max).getTotalSales())
                    max = j;
            }
            // Swap max (highest num) to current position on array
            Store swap = stores.get(i);
            stores.set(i, stores.get(max));
            stores.set(max, swap);
        }
        return stores;
    }

    public static ArrayList<Store> sortByProductsSoldToUser(ArrayList<Store> stores, Customer customer)
    {

        for (int i = 0; i < stores.size(); i++)
        {
            int max = i;
            for (int j = i + 1; j < stores.size(); j++)
            {
                if (stores.get(j).getProductsBoughtByCurrentUser(customer) >
                        stores.get(max).getProductsBoughtByCurrentUser(customer))
                    max = j;
            }
            // Swap max (highest num) to current position on array
            Store swap = stores.get(i);
            stores.set(i, stores.get(max));
            stores.set(max, swap);
        }

        return stores;
    }

    public static void checkIfFilesExist()
    {


        File users = new File("users.txt");
        if (!users.exists())
        {
            try {
                users.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        File products = new File("products.txt");
        if (!products.exists())
        {
            try {
                products.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        File stores = new File("stores.txt");
        if (!stores.exists())
        {
            try {
                stores.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
