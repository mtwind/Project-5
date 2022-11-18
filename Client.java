import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Client
{

    public static void main(String[] args)
    {
        Scanner s = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 1);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Driver.checkIfFilesExist();
            logIn(s, writer, reader);

            if (reader.readLine().equals("customer"))
            {
                customerMenu(s, writer, reader);
            } else {
                sellerMenu(s, writer, reader);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void logIn(Scanner s, PrintWriter writer, BufferedReader reader) throws Exception {
        boolean unknownUser = true;
        int choice = 0;

        do {
            System.out.println("Enter email: ");
            String email = s.nextLine();

            // sends email to server and the server responds with false if the user has an account, and true otherwise
            writer.write(email);
            writer.println();
            writer.flush();
            unknownUser = Boolean.parseBoolean(reader.readLine());

            // used if user is new, allows account creation
            if (unknownUser)
            {
                // asks user to create acount, uses exceptions and loops to assure they answer 1 or 2
                System.out.println("Would you like to create an account? 1 = yes, 2 = no");
                while (true)
                {
                    try
                    {
                        choice = s.nextInt();
                        String waste = s.nextLine();
                        if (choice == 1 || choice == 2) {
                            break;
                        } else {
                            System.out.println("Invalid input, enter 1 or 2: ");
                        }
                    } catch (InputMismatchException e) {
                        String waste = s.nextLine();
                        System.out.println("Invalid input, enter 1 or 2: ");
                    }
                }

                writer.write(String.valueOf(choice));
                writer.println();
                writer.flush();
                // if user chooses to make a new account
                if (choice == 1)
                {
                    System.out.println("What is your name?");
                    String name = s.nextLine();
                    System.out.println("Enter a password:");
                    String password = s.nextLine();

                    // loops until user choose customer or seller
                    int customerSeller = 0;
                    while (true)
                    {
                        System.out.println("Customer or Seller? 1 = Customer, 2 = Seller");
                        try {
                            customerSeller = s.nextInt();
                            String waste = s.nextLine();
                            if (customerSeller == 1 || customerSeller == 2)
                            {
                                break;
                            }
                            System.out.println("Invalid input, enter 1 or 2: ");
                        } catch (InputMismatchException e) {
                            String waste = s.nextLine();
                            System.out.println("Invalid input, enter 1 or 2");
                        }
                    }

                    // loops until the user choose and answers a security question
                    int securityQuestionNum = 0;
                    String securityAnswer = "";
                    while (true)
                    {
                        System.out.println("Choose a security question:");
                        for (int i = 0; i < User.questionList.length; i++)
                        {
                            System.out.println(User.questionList[i]);
                        }
                        try {
                            securityQuestionNum = s.nextInt();
                            String waste = s.nextLine();

                            if (securityQuestionNum >= 1 && securityQuestionNum <= 6)
                            {
                                writer.write(String.valueOf(securityQuestionNum));
                                writer.println();
                                writer.flush();

                                System.out.println(reader.readLine().substring(3));
                                securityAnswer = s.nextLine();
                                break;
                            }
                            System.out.println("Invalid input, enter 1-6: ");

                        } catch (InputMismatchException e) {
                            String waste = s.nextLine();
                            System.out.println("Invalid input, enter 1-6: ");
                        }
                    }

                    // gives the server the new user's basic info
                    writer.write(name + "," + password + "," + customerSeller + "," + securityAnswer + "," + securityQuestionNum);
                    writer.println();
                    writer.flush();

                    unknownUser = Boolean.parseBoolean(reader.readLine());

                    // this else is for if they select no when asked to create an account
                } else {
                    System.out.println("Would you like to reenter an email? 1 = yes, 2 = no");
                    int reenter = 0;
                    while (true)
                    {
                        try
                        {
                            reenter = s.nextInt();
                            String waste = s.nextLine();
                            if (reenter != 1 && reenter != 2)
                            {
                                System.out.println("Invalid input, enter 1 or 2: ");
                            } else {
                                break;
                            }
                        } catch (InputMismatchException e) {
                            String waste = s.nextLine();
                            System.out.println("Invalid input, enter 1 or 2: ");
                        }
                    }
                    writer.write(String.valueOf(reenter));
                    writer.println();
                    writer.flush();

                    String tryAgain = reader.readLine();
                    if (tryAgain.equals("break"))
                    {
                        break;
                    } else {
                        unknownUser = true;
                    }
                }
            }
            //runs until a user is verified as existing / created or until the user quits
        } while (unknownUser);


        // password check begins here
        if (unknownUser)
        { //if they choose not to create an account and to not retry, leaves
            System.out.println("bye");
        } else
        { //else, makes them log in
            boolean passwordLoop = true;
            while (passwordLoop)
            {
                System.out.println("Enter password:");
                String password = s.nextLine();
                writer.write(password);
                writer.println();
                writer.flush();

                boolean passwordCheck = Boolean.parseBoolean(reader.readLine());
                if (passwordCheck == false)
                {
                    int iteration = 0;
                    while (true)
                    {
                        if (iteration == 0) {
                            System.out.println("Incorrect credentials.\n1. Try again\n2. Change Password\n3. Quit");
                            iteration = 1;
                        } else {
                            System.out.println("1. Try again\n2. Change Password\n3. Quit");
                        }
                        try {
                            int passwordChoice = s.nextInt();
                            String waste = s.nextLine();
                            if (passwordChoice >= 1 && passwordChoice <= 3)
                            {
                                writer.write(String.valueOf(passwordChoice));
                                writer.println();
                                writer.flush();
                                break;
                            } else {
                                System.out.println("Invalid input, enter 1-3:");
                            }
                        } catch (InputMismatchException e) {
                            String waste = s.nextLine();
                            System.out.println("Invalid input, enter 1-3:");
                        }
                    }
                } else {
                    System.out.println("Success!");
                    break;
                }

                String passwordDirectionsFromServer = reader.readLine();
                if (passwordDirectionsFromServer.equals("true"))
                {
                    continue;
                } else if (passwordDirectionsFromServer.equals("break")) {
                    System.out.println("goodbye");
                    break;
                } else {
                    System.out.println(passwordDirectionsFromServer);
                    String answer = s.nextLine();
                    writer.write(answer);
                    writer.println();
                    writer.flush();

                    boolean success = reader.readLine().equals("correct");
                    if (success)
                    {
                        System.out.println("Success!\nEnter new password: ");
                        String newPassword = s.nextLine();
                        writer.write(newPassword);
                        writer.println();
                        writer.flush();
                    } else {
                        System.out.println("Incorrect! Try again later.");
                    }
                }
            }

        }
    }

    public static void customerMenu(Scanner s, PrintWriter writer, BufferedReader reader) throws IOException
    {
        String menuOptions = "Customer Menu\n1. View the marketplace\n2. View cart\n3. View Purchase History\n" +
                "4. View Dashboard\n5. Export Purchase History to a file\n6. Delete Account\n7. Quit";
        int menuChoice = -1;
        boolean inLoop = true;
        while (inLoop)
        {
            // loop assures the user selects a number 1-7
            menuChoice = checkChoice(1, 7, menuOptions, s);

            writer.write(String.valueOf(menuChoice));
            writer.println();
            writer.flush();

            switch (menuChoice)
            {
                case 1:
                    viewMarket(s, writer, reader);
                    break;
                case 2:
                    viewCart(s, writer, reader);
                    break;
                case 3:
                    String line = reader.readLine();
                    while (!line.equals("history printed"))
                    {
                        System.out.println(line);
                        line = reader.readLine();
                    }
                    break;
                case 4:
                    viewCostumerDash(s, writer, reader);
                    break;
                case 5:
                    System.out.println("Exporting complete.");
                    break;
                case 6:
                    System.out.println("Account deleted");
                    inLoop = false;
                    break;
                case 7:
                    System.out.println("Goodbye");
                    inLoop = false;
                    break;
            }
        }
    }

    public static void viewMarket(Scanner s, PrintWriter writer, BufferedReader reader) throws IOException
    {
        if (reader.readLine().equals("no products")) {
            System.out.println("There are no products in the marketplace.");
        } else {
            String sortList = "Sort the list:\n1. By Price\n2. By Quantity\n3. No Sort";
            int sortChoice = checkChoice(1, 3, sortList, s); // check choice is a method that handles all errors

            writer.write(String.valueOf(sortChoice));
            writer.println();
            writer.flush();

            // this writes out all the products to the user
            String productInfo = reader.readLine();
            while (!productInfo.equals("no more products"))
            {
                System.out.println(productInfo);
                productInfo = reader.readLine();
            }

            // begin part where user preforms a search
            String search;
            while (true)
            {
                System.out.println("Search for a product: enter 'quit' to exit the search");
                search = s.nextLine();
                writer.write(search);
                writer.println();
                writer.flush();

                if (reader.readLine().equals("quit"))
                {
                    break;
                }

                if (reader.readLine().equals("no results found"))
                {
                    System.out.println("There were no results aligning with you search");

                } else {
                    while (true)
                    {
                        String productResults = reader.readLine();
                        int howManyResults = -1;
                        while (!productResults.equals("no more products"))
                        {
                            howManyResults++;
                            System.out.println(productResults);
                            productResults = reader.readLine();
                        }
                        String selectAProduct = "Select a product from the list above, enter 0 to exit: ";
                        int selectedProduct = checkChoice(0, howManyResults, selectAProduct, s);
                        writer.write(String.valueOf(selectedProduct));
                        writer.println();
                        writer.flush();

                        String focusOrQuit = reader.readLine();
                        if (focusOrQuit.equals("no product selected"))
                        {
                            break;
                        } else {
                            System.out.println(focusOrQuit);
                            String addToCartSting = "Add to cart? 1 = yes, 2 = no";
                            int addToCartChoice = checkChoice(1, 2, addToCartSting, s);

                            writer.write(String.valueOf(addToCartChoice));
                            writer.println();
                            writer.flush();
                        }
                    }
                } // end else statement for the search having at least one result
            } // end loop for searching
        } // end else statement for when the marketplace has at least one product
    } // end viewMarket

    public static void viewCart(Scanner s, PrintWriter writer, BufferedReader reader) throws IOException
    {
        if(reader.readLine().equals("empty cart"))
        {
            System.out.println("No items in cart");
        } else {
            System.out.println("Shopping cart:");

            String cartResult = reader.readLine();
            while (!cartResult.equals("no more items in cart"))
            {
                System.out.println(cartResult);
                cartResult = reader.readLine();
            }

            String cartOptions = "Shopping cart options: enter 0 to quit.\n" +
                    "1. Purchase your cart\n2. Remove an item from your cart\n3. Clear your cart";
            int cartChoice = checkChoice(0,3, cartOptions, s);
            writer.write(String.valueOf(cartChoice));
            writer.println();
            writer.flush();


            String choiceResult = reader.readLine();
            if (choiceResult.equals("break"))
            {

            } else if (choiceResult.equals("purchased"))
            {
                System.out.println("Items purchased!");
            } else if (choiceResult.equals("cart cleared")) {
                System.out.println("Your cart has been emptied.");
            } else {
                while (true)
                {
                    System.out.println("Enter the name of the item you wish to remove: ");
                    String removedItem = s.nextLine();
                    writer.write(removedItem);
                    writer.println();
                    writer.flush();

                    String removeItemResult = reader.readLine();


                    if (removeItemResult.equals("item not found in cart"))
                    {
                        String itemNotFoundOptions = "Item not found in cart.\n1. Try again\n2. Quit";
                        int itemNotFoundChoice = checkChoice(1,2, itemNotFoundOptions, s);


                        writer.write(String.valueOf(itemNotFoundChoice));
                        writer.println();
                        writer.flush();


                        String itemNotFoundChoiceResult = reader.readLine();

                        if (itemNotFoundChoiceResult.equals("break"))
                        {
                            break;
                        }

                    } else {
                        System.out.println("Item removed.");
                        break;
                    }
                }

            }
        } // end else statement that runs for a cart with products inside

    }

    public static void viewCostumerDash(Scanner s, PrintWriter writer, BufferedReader reader) throws IOException
    {
        String chooseSort = "How do you want to the stores to be sorted?\n1. By Most Popular\n2. By Your Favorite";
        int sortChoice = checkChoice(1, 2, chooseSort, s);
        writer.write(String.valueOf(sortChoice));
        writer.println();
        writer.flush();

        String customerDash = reader.readLine();
        if (customerDash.equals("no stores"))
        {
            System.out.println("There are no stores in the market");
        } else {
            while (!customerDash.equals("all stores printed"))
            {
                System.out.println(customerDash);
                customerDash = reader.readLine();
            }
        }
    }



    public static void sellerMenu(Scanner s, PrintWriter writer, BufferedReader reader)
    {
        System.out.println("seller");
    }



    public static int checkChoice (int low, int high, String choices, Scanner s)
    {
        int input = -1;
        while (true)
        {
            System.out.println(choices);
            try {
                input = s.nextInt();
                String waste = s.nextLine();
                if (input >= low && input <= high)
                {
                    return input;
                } else {
                    System.out.printf("Invalid input, enter a number %d-%d\n", low, high);
                }

            } catch (InputMismatchException e)
            {
                String waste = s.nextLine();
                System.out.printf("Invalid input, enter a number %d-%d\n", low, high);
            }
        }
    }
}
