package src;

import java.util.*;
import java.io.*;
/**
 * USER CLASS
 *
 * this class contains the basic variables that customers and sellers share
 * It also implements methods to change a password through security questions
 *
 * @author L12 group 2
 * @version 13 NOV 2022
 */
public class User {

    //final strings for security questions
    private static final String QUESTION_1 = "1. What is your mother's maiden name?";
    private static final String QUESTION_2 = "2. What was the name of your first pet?";
    private static final String QUESTION_3 = "3. Who was your childhood best friend?";
    private static final String QUESTION_4 = "4. What high school did you attend?";
    private static final String QUESTION_5 = "5. In what city were you born?";
    private static final String QUESTION_6 = "6. What was your favorite food as a child?";
    public static final String[] questionList = {QUESTION_1, QUESTION_2, QUESTION_3, QUESTION_4, QUESTION_5, QUESTION_6};

    // instance variables
    private String name;
    private String email;
    private String password;
    private boolean isCustomer;
    private int securityQuestionNumber;
    private String securityAnswer;

    private File userFile;

    //use this constructor for a returning user. It gathers all of their basic data from their text file



    // use this constructor when creating a new account
    public User(String name, String email, String password, boolean isCustomer, Scanner s) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isCustomer = isCustomer;
        this.securityAnswer = securityQuestions(s);
    }

    // user for project 5 without a scanner
    public User(String name, String email, String password, boolean isCustomer, String securityAnswer, int securityQuestionNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isCustomer = isCustomer;
        this.securityAnswer = securityAnswer;
        this.securityQuestionNumber = securityQuestionNumber;

    }


    public User(String name, String email, String password, boolean isCustomer, int securityQuestionNumber,
                String securityAnswer) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isCustomer = isCustomer;
        this.securityQuestionNumber = securityQuestionNumber;
        this.securityAnswer = securityAnswer;
    }

    public static String userExists(String email) { //if the user exists, returns the type of user. if not, returns an
        //empty string
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("users.txt"));

            String line = bfr.readLine();
            if (line == null) {
                return "";
            } else {
                while (line != null) {
                    String e = line.split(",")[1];
                    if (e.equals(email)) {
                        return line.split(",")[0];
                    }
                    line = bfr.readLine();
                }
            }
        } catch (IOException e) {
            System.out.println("That file does not exist.");
        }
        return "";
    }

    // writes a file for new users
    // returns true if the file is written successfully
    // returns false if there is an error writing the file or the user is returning


    // edits already existing files by updating all of their info
    public void editUserFile() {
        try {
            FileWriter fw = new FileWriter(this.userFile);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("Name: " + this.name + " " + this.name);
            bw.write("\nEmail: " + this.email);
            bw.write("\nPassword: " + this.password);
            bw.write("\nUser Type: ");
            if (isCustomer) {
                bw.write("Customer");
            } else {
                bw.write("Seller");
            }
            bw.write("\nSecurity Question Number: " + this.securityQuestionNumber);
            bw.write("\nSecurity Question Answer: " + this.securityAnswer);

            bw.close();
        } catch (Exception e) {

        }

    }

    // checks to make sure user entered the correct password
    public boolean passwordCheck(String password) {
        if (this.password.equals(password)) {
            return true;
        }
        return false;
    }

    //asks for security questions when creating an account
    public String securityQuestions(Scanner s) {
        System.out.println("Choose a security questions below:");
        for (int i = 0; i < questionList.length; i++) {
            System.out.println(questionList[i]);
        }

        do {
            try {
                this.securityQuestionNumber = Integer.parseInt(s.nextLine());
                int num = this.securityQuestionNumber;

                if (num != 1 && num != 2 && num != 3 && num != 4 && num != 5 && num != 6) {
                    System.out.println("Invalid Input!");
                    System.out.println("Choose a security questions number:");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("please enter a number.");
            }
        } while (true);

        System.out.println(questionList[this.securityQuestionNumber - 1]);
        String answer = s.nextLine();

        return answer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    // changes a user's password and then updates their file
    public void setPassword(String password) {
        this.password = password;
        editUserFile();
    }

    public boolean isCustomer() {
        return isCustomer;
    }

    public void setCustomer(boolean customer) {
        isCustomer = customer;
    }

    public File getUserFile() {
        return userFile;
    }


    public String[] getQuestionList() {
        return questionList;
    }

    public int getSecurityQuestionNumber() {
        return securityQuestionNumber;
    }

    public void setSecurityQuestionNumber(int securityQuestionNumber) {
        this.securityQuestionNumber = securityQuestionNumber;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public void removeStoreFromFile(String name) {
        try {
            ArrayList<String> lines = new ArrayList<>();
            BufferedReader bfr = new BufferedReader(new FileReader("stores.txt"));
            String line = bfr.readLine();
            String[] bananaSplit;
            while (line != null) {
                bananaSplit = line.split(",");
                if (bananaSplit[0].equals(name)) {
                    line = bfr.readLine();
                    continue;
                }
                lines.add(line);
                line = bfr.readLine();
            }

            bfr.close();

            PrintWriter pw = new PrintWriter(new FileWriter("stores.txt", false));
            for (int i = 0; i < lines.size(); i++) {
                pw.println(lines.get(i));
            }

            pw.flush();
            pw.close();

        } catch (Exception e) {
            System.out.println("Error reading file");
        }
    }

    public void deleteAccount() {
        String em = this.getEmail();
        String[] storeNames = null;
        String[] split;
        ArrayList<String> fileLines = new ArrayList<>();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("users.txt"));
            String line = bfr.readLine();

            while (line != null) {
                split = line.split(",");
                if (split[1].equals(em)) {
                    if (split[0].equals("customer")) {
                        line = bfr.readLine();
                        continue;
                    } else {
                        storeNames = split[6].split("~");
                        line = bfr.readLine();
                        continue;
                    }
                }
                fileLines.add(line);
                line = bfr.readLine();
            }

            PrintWriter pw = new PrintWriter(new FileOutputStream("users.txt", false));
            for (int i = 0; i < fileLines.size(); i++) {
                pw.println(fileLines.get(i));
            }
            pw.flush();
            pw.close();

            if (storeNames != null && storeNames.length != 0) {
                BufferedReader bfr3 = new BufferedReader(new FileReader("users.txt"));
                line = bfr3.readLine();
                Customer c;
                ArrayList<Product> cart = new ArrayList<>();
                while (line != null) {
                    split = line.split(",");
                    if (split[0].equals("customer")) {
                        c = Customer.parseCustomer(split[1]);
                        cart = c.getShoppingCart();
                        if (cart != null && cart.size() != 0) {
                            for (int i = 0; i < storeNames.length; i++) {
                                for (int j = cart.size() - 1; j >= 0; j--) {
                                    if (cart.get(j).getStore().equals(storeNames[i])) {
                                        cart.remove(j);
                                    }
                                }
                            }
                        }

                        c.setShoppingCart(cart);
                        c.editUserFile();
                    }
                    line = bfr3.readLine();
                }

                BufferedReader bfr2 = new BufferedReader(new FileReader("products.txt"));
                fileLines = new ArrayList<>();

                line = bfr2.readLine();
                boolean broken = false;
                while (line != null) {
                    broken = false;
                    split = line.split(",");
                    for (int i = 0; i < storeNames.length; i++) {
                        if (split[1].equals(storeNames[i])) {
                            broken = true;
                            break;
                        }
                    }
                    if (broken) {
                        line = bfr2.readLine();
                        continue;
                    }
                    fileLines.add(line);
                    line = bfr2.readLine();
                }

                PrintWriter pw2 = new PrintWriter(new FileOutputStream("products.txt", false));
                for (int i = 0; i < fileLines.size(); i++) {
                    pw2.println(fileLines.get(i));
                }
                pw2.flush();
                pw2.close();


                BufferedReader bfr4 = new BufferedReader(new FileReader("stores.txt"));
                line = bfr4.readLine();
                fileLines = new ArrayList<>();

                while (line != null) {
                    split = line.split(",");
                    if (split[1].equals(em)) {
                        line = bfr4.readLine();
                        continue;
                    }
                    fileLines.add(line);
                    line = bfr4.readLine();
                }

                PrintWriter pw3 = new PrintWriter(new FileOutputStream("stores.txt", false));
                for (int i = 0; i < fileLines.size(); i++) {
                    pw3.println(fileLines.get(i));
                }
                pw3.flush();
                pw3.close();
            }

        } catch (Exception e) {
            System.out.println("Error reading file");
        }
    }

    public String toString() {
        String userType, userString;
        if(this.isCustomer) {
            userType = "customer";
        } else {
            userType = "seller";
        }

        userString = userType + "," + this.email + "," + this.name + "," + this.password + "," +
                this.securityQuestionNumber + "," + this.securityAnswer;
        return userString;
    }

}
