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

                    //customerMenu(s, user);
                } else if (user instanceof Seller) {
                    sellerMenu(s, (Seller) user);
                }
            }
        }
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
                    //viewSellerDash(s, stores);
                    break;
                case 5:
                    user.exportStoreProducts(s);
                    break;
                case 6:
                    user.importStoreProducts(s);
                    break;
                case 7:
                    //user.viewCustomerCarts(s);
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
