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



}
