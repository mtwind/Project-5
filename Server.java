import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;



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


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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

}
