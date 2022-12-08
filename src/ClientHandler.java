import javax.swing.*;
import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;


public class ClientHandler implements Runnable {
    Socket socket;
    User user;
    Store store;
    Object obj;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            //SwingUtilities.invokeLater(new MainInterface());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            int buttonChoice = 0;
            boolean running = true;
            while (running) {
                buttonChoice = Integer.parseInt(reader.readLine());
                //System.out.println(buttonChoice);
                //TODO: enter switch statement that takes input from client (based on button) and processes information
                String email, type;
                switch (buttonChoice) {
                    case 1: // create an account, used for both sellers and customers
                        String[] info = reader.readLine().split(",");
                        boolean alreadyUsed = User.checkNewUser(info[1]);
                        System.out.println(alreadyUsed);

                        if (alreadyUsed == false) {
                            boolean isCustomer = Boolean.parseBoolean(info[3]);

                            if (isCustomer)
                            {
                                user = new Customer(info[0], info[1], info[2], true,
                                        info[4], Integer.parseInt(info[5]));

                            } else {
                                user = new Seller(info[0], info[1], info[2], false,
                                        info[4], Integer.parseInt(info[5]));
                            }
                            writer.write("new");
                            writer.println();
                            writer.flush();

                            StringBuilder market = new StringBuilder();
                            ArrayList<Product> pros = Product.getAllProducts();
                            String productInfo;
                            for (int i = 0; i < pros.size(); i++) {
                                productInfo = String.format("Product: %s   Store: %s   Price: $%.2f",
                                        pros.get(i).getName(), pros.get(i).getStore(), pros.get(i).getPrice());
                                market.append(productInfo);
                                if (i != pros.size() - 1) {
                                    market.append(",");
                                }
                            }

                            writer.write(market.toString());
                            writer.println();
                            writer.flush();

                        } else {
                            writer.write("not new");
                            writer.println();
                            writer.flush();
                        }
                        break;

                    case 2: //seller log in
                        email = reader.readLine();
                        type = User.userExists(email);
                        if (type.equals("seller")) {
                            writer.write("t");
                            writer.println();
                            writer.flush();
                            user = Seller.parseSeller(email);
                            String pass = reader.readLine();
                            boolean verified = user.passwordCheck(pass);
                            if (verified) {
                                writer.write("verified");
                                writer.println();
                                writer.flush();

                                StringBuilder line = new StringBuilder();
                                for (int i = 0; i < ((Seller)user).getStores().size(); i++) {
                                    System.out.println(((Seller)user).getStores().get(i));
                                    line.append(((Seller)user).getStores().get(i).getName());
                                    if (i != ((Seller)user).getStores().size() - 1) {
                                        line.append(",");
                                    }
                                }
                                System.out.println(line.toString());
                                writer.write(line.toString());
                                writer.println();
                                writer.flush();

                            } else {
                                writer.write("!verified");
                                writer.println();
                                writer.flush();
                            }

                        } else if (type.equals("customer")) {
                            writer.write("customerLog");
                            writer.println();
                            writer.flush();

                        } else {
                            writer.write("noLog");
                            writer.println();
                            writer.flush();
                        }
                        System.out.println("in seller log in");
                        break;
                    case 3: // customer log in
                        email = reader.readLine();
                        type = User.userExists(email);
                        if (type.equals("customer")) {
                            writer.write("t");
                            writer.println();
                            writer.flush();
                            user = Customer.parseCustomer(email);
                            String pass = reader.readLine();
                            boolean verified = user.passwordCheck(pass);
                            if (verified) {
                                writer.write("verified");
                                writer.println();
                                writer.flush();

                                StringBuilder market = new StringBuilder();
                                ArrayList<Product> pros = Product.getAllProducts();
                                String productInfo;
                                for (int i = 0; i < pros.size(); i++) {
                                    productInfo = String.format("Product: %s   Store: %s   Price: $%.2f",
                                            pros.get(i).getName(), pros.get(i).getStore(), pros.get(i).getPrice());
                                    market.append(productInfo);
                                    if (i != pros.size() - 1) {
                                        market.append(",");
                                    }
                                }

                                writer.write(market.toString());
                                writer.println();
                                writer.flush();

                            } else {
                                writer.write("!verified");
                                writer.println();
                                writer.flush();
                            }

                        } else if (type.equals("seller")) {
                            writer.write("sellerLog");
                            writer.println();
                            writer.flush();

                        } else {
                            writer.write("noLog");
                            writer.println();
                            writer.flush();
                        }
                        break;
                    case 5: // confirmEditSeller button, edits seller and store info in files
                        System.out.println("here");
                        String sellerNewPass = reader.readLine(); // read new pass from client
                        String sellerNewName = reader.readLine();
                        String sellerNewEmail = reader.readLine(); // read new email from client
                        String oldSellerEmail = user.getEmail();
                        if (user == null) {
                            System.out.println("user null");
                        }
                        user = Seller.parseSeller(oldSellerEmail);
                        if (user == null) {
                            System.out.println("user null2");
                        }
                        System.out.println("here");
                        ArrayList<String> userLines = ((Seller)user).readUserFile();
                        for (int i = 0; i < userLines.size(); i++) {
                            String[] userData = userLines.get(i).split(",");
                            if (userData[1].equals(oldSellerEmail)) {
                                userData[1] = sellerNewEmail;
                                userData[2] = sellerNewName;
                                userData[3] = sellerNewPass;
                                String newUserLine = "";
                                for (int j = 0; j < userData.length; j++) {
                                    if (j == userData.length - 1) {
                                        newUserLine = newUserLine.concat(userData[j]);
                                    } else {
                                        newUserLine = newUserLine.concat(userData[j] + ",");
                                    }
                                }
                                userLines.set(i, newUserLine);
                                PrintWriter pw = new PrintWriter(new FileOutputStream("users.txt"));
                                for (int x = 0; x < userLines.size(); x++) {
                                    pw.println(userLines.get(x));
                                }
                                pw.flush();
                                pw.close();
                                break;
                            }
                        }
                        user.setPassword(sellerNewPass);
                        user.setName(sellerNewName);
                        user.setEmail(sellerNewEmail);
                        for (int i = 0; i < ((Seller)user).getStores().size(); i++) {
                            ((Seller) user).getStores().get(i).setOwner(sellerNewEmail);
                            ((Seller) user).getStores().get(i).editStoreFile();
                        }
                        break;
                    case 6: // confirmEditCustomer buttons
                        //System.out.println("here");
                        String customerNewPass = reader.readLine(); // read new pass from client
                        String customerNewName = reader.readLine();
                        String customerNewEmail = reader.readLine(); // read new email from client
                        String oldCustomerEmail = user.getEmail();
                        if (user == null) {
                            System.out.println("user null");
                        }
                        user = Customer.parseCustomer(oldCustomerEmail);
                        if (user == null) {
                            System.out.println("user null2");
                        }
                        //System.out.println("here");
                        ArrayList<String> customerLines = ((Customer)user).readUserFile();
                        for (int i = 0; i < customerLines.size(); i++) {
                            String[] userData = customerLines.get(i).split(",");
                            if (userData[1].equals(oldCustomerEmail)) {
                                userData[1] = customerNewEmail;
                                userData[2] = customerNewName;
                                userData[3] = customerNewPass;
                                String newUserLine = "";
                                for (int j = 0; j < userData.length; j++) {
                                    if (j == userData.length - 1) {
                                        newUserLine = newUserLine.concat(userData[j]);
                                    } else {
                                        newUserLine = newUserLine.concat(userData[j] + ",");
                                    }
                                }
                                customerLines.set(i, newUserLine);
                                PrintWriter pw = new PrintWriter(new FileOutputStream("users.txt"));
                                for (int x = 0; x < customerLines.size(); x++) {
                                    pw.println(customerLines.get(x));
                                }
                                pw.flush();
                                pw.close();
                                break;
                            }
                        }
                        user.setPassword(customerNewPass);
                        user.setName(customerNewName);
                        user.setEmail(customerNewEmail);

                        // TODO: implement changes to the emails in the store's list of customers
                        break;
                    case 7:
                        String selectedStore = reader.readLine();
                        ArrayList<Store> fileStores = Store.getAllStores();
                        Store selected = null;


                        for (int i = 0; i < fileStores.size(); i++)
                        {
                            if (fileStores.get(i).getOwner().equals(user.getEmail()) &&
                                    fileStores.get(i).getName().equals(selectedStore))
                            {
                                selected = fileStores.get(i);
                                break;
                            }
                        }
                        store = selected;

                        ArrayList<Product> storeProducts;
                        if (selected != null)
                            storeProducts = selected.getProducts();
                        else
                            storeProducts = new ArrayList<>();

                        StringBuilder str = new StringBuilder("");
                        for (int i = 0; i < storeProducts.size(); i++) {
                            str.append(storeProducts.get(i).getName());
                            if (i != storeProducts.size() - 1)
                                str.append(",");
                        }
                        writer.write(str.toString());
                        writer.println();
                        writer.flush();
                        break;
                    case 8:
                        String newStoreName = reader.readLine();
                        ArrayList<Store> allStores = Store.getAllStores();
                        boolean used = false;

                        for (int i = 0; i < allStores.size(); i++)
                        {
                            if (newStoreName.equals(allStores.get(i).getName()))
                            {
                                writer.write("already used");
                                writer.println();
                                writer.flush();
                                used = true;
                                break;
                            }
                        }

                        if (!used)
                        {
                            ArrayList<Store> temp = ((Seller) user).getStores();
                            temp.add(new Store(user.getEmail(), newStoreName, new ArrayList<Product>()));
                            ((Seller) user).setStores(temp);
                            user.editUserFile();

                            StringBuilder line = new StringBuilder();
                            for (int i = 0; i < ((Seller)user).getStores().size(); i++) {
                                line.append(((Seller)user).getStores().get(i).getName());
                                if (i != ((Seller)user).getStores().size() - 1) {
                                    line.append(",");
                                }
                            }

                            writer.write(line.toString());
                            writer.println();
                            writer.flush();

                        }
                        break;
                    case 9:
                        String storeName = reader.readLine();

                        ArrayList<Store> newStoreList = new ArrayList<Store>();
                        ArrayList<Store> currentStores = ((Seller) user).getStores();

                        // copies all non-target stores to a new array
                        for(int i = 0; i < currentStores.size(); i++){
                            if(!(currentStores.get(i).getName().equals(storeName))) {
                                newStoreList.add(currentStores.get(i));
                            }
                        }

                        // updating this user's list of stores and also
                        ((Seller) user).setStores(newStoreList);
                        ((Seller) user).editUserFile();
                        Store.updateProducts(storeName);

                        String newStoreString = "";
                        for (int i = 0; i < newStoreList.size(); i++) {
                            newStoreString += newStoreList.get(i).getName();
                            if (i != newStoreList.size() - 1) {
                                newStoreString += ",";
                            }
                        }

                        writer.write(newStoreString);
                        writer.println();
                        writer.flush();
                        break;

                    case 10:

                        String[] newProductInfo = reader.readLine().split(",");
                        if (newProductInfo[0].contains(",") || newProductInfo[0].contains("~") || newProductInfo[0].contains("-") || newProductInfo[0].isEmpty())
                        {
                            writer.write("name error");
                            writer.println();
                            writer.flush();
                        } else if (newProductInfo[1].contains(",") || newProductInfo[1].contains("~") || newProductInfo[1].isEmpty()) {
                            writer.write("description error");
                            writer.println();
                            writer.flush();
                        } else {
                            boolean keepGoing = true;
                            try {
                                double newProductPrice = Double.parseDouble(newProductInfo[3]);
                                if (newProductPrice < 0)
                                {
                                    writer.write("price error");
                                    writer.println();
                                    writer.flush();
                                    keepGoing = false;
                                }
                            } catch (NumberFormatException e) {
                                writer.write("price error");
                                writer.println();
                                writer.flush();
                                keepGoing = false;
                            }

                            if (keepGoing)
                            {
                                try {
                                    int newProductQuantity = Integer.parseInt(newProductInfo[2]);
                                    if (newProductQuantity < 0)
                                    {
                                        writer.write("quantity error");
                                        writer.println();
                                        writer.flush();
                                        keepGoing = false;
                                    }
                                } catch (NumberFormatException e)
                                {
                                    writer.write("quantity error");
                                    writer.println();
                                    writer.flush();
                                    keepGoing = false;
                                }
                            }


                            if (keepGoing)
                            {

                                Product product = new Product(newProductInfo[0], store.getName(), newProductInfo[1],
                                        Integer.parseInt(newProductInfo[2]), Double.parseDouble(newProductInfo[3]));
                                ArrayList<Product> storeProductsNew = store.getProducts();
                                storeProductsNew.add(product);
                                store.setProducts(storeProductsNew);
                                store.editStoreFile();
                                StringBuilder stri = new StringBuilder();
                                for (int i = 0; i < storeProductsNew.size(); i++) {
                                    stri.append(storeProductsNew.get(i).getName());
                                    if (i != storeProductsNew.size() - 1) {
                                        stri.append(",");
                                    }
                                }
                                writer.write(stri.toString());
                                writer.println();
                                writer.flush();


                            }
                        }
                        break;
                    case 11:
                        String pName = reader.readLine();
                        String sName = reader.readLine();
                        Product p = Product.getProduct(pName, sName);

                        assert p != null;
                        writer.write(String.valueOf(p.getPrice()));
                        writer.println();
                        writer.flush();
                        writer.write(p.getProductDescription());
                        writer.println();
                        writer.flush();
                        writer.write(String.valueOf(p.getQuantity()));
                        writer.println();
                        writer.flush();

                        break;
                    case 12:
                        String wantToFind = reader.readLine();
                        ArrayList<Product> searchResult = Product.returnSearch(wantToFind);
                        StringBuilder results = new StringBuilder();
                        String productInfo;
                        for (int i = 0; i < searchResult.size(); i++) {
                            productInfo = String.format("Product: %s   Store: %s   Price: $%.2f",
                                    searchResult.get(i).getName(), searchResult.get(i).getStore(),
                                    searchResult.get(i).getPrice());
                            results.append(productInfo);
                            if (i != searchResult.size() - 1) {
                                results.append(",");
                            }
                        }
                        System.out.println(results.toString());
                        writer.write(results.toString());
                        writer.println();
                        writer.flush();
                        break;
                    case 13:
                        StringBuilder currentMarket = new StringBuilder();
                        ArrayList<Product> marPros = Product.getAllProducts();
                        String products;
                        for (int i = 0; i < marPros.size(); i++) {
                            products = String.format("Product: %s   Store: %s   Price: $%.2f",
                                    marPros.get(i).getName(), marPros.get(i).getStore(), marPros.get(i).getPrice());
                            currentMarket.append(products);
                            if (i != marPros.size() - 1) {
                                currentMarket.append(",");
                            }
                        }

                        writer.write(currentMarket.toString());
                        writer.println();
                        writer.flush();
                        break;
                    case 14:
                        String productDataViewCustomer = reader.readLine();
                        String productNameViewCustomer = productDataViewCustomer.substring(9,
                                productDataViewCustomer.indexOf("Store:") - 3);
                        System.out.println(productNameViewCustomer);
                        String productStoreViewCustomer = productDataViewCustomer.substring(
                                productDataViewCustomer.indexOf("Store:") + 7,
                                productDataViewCustomer.indexOf("Price:") - 3);
                        System.out.println(productStoreViewCustomer);
                        Product viewCustomerProduct = null;
                        viewCustomerProduct = Product.getProduct(productNameViewCustomer, productStoreViewCustomer);
                        productDataViewCustomer = viewCustomerProduct.getName() + "," + viewCustomerProduct.getProductDescription() + "," +
                                viewCustomerProduct.getStore() + "," + viewCustomerProduct.getPrice() + "," + viewCustomerProduct.getQuantity();
                        writer.write(productDataViewCustomer);
                        writer.println();
                        writer.flush();

                        break;
                    case 15:
                        try {
                            String proNewName = reader.readLine(); // read new pass from client
                            String proNewDescription = reader.readLine();
                            String proNewPrice = reader.readLine(); // read new email from client
                            String proNewQuantity = reader.readLine();
                            String oldProductName = reader.readLine();
                            String s = reader.readLine();
                            int t = Integer.parseInt(proNewQuantity);
                            double d = Double.parseDouble(proNewPrice);
                            ArrayList<String> temp = Store.getAllLines();
                            String[] preSplit = new String[0];
                            boolean ayo = false;
                            for (int i = 0; i < temp.size(); i++) {
                                preSplit = temp.get(i).split(",");
                                if(preSplit[0].equals(s)) {
                                    ayo = true;
                                    break;
                                }
                            }
                            if (ayo) {
                                boolean j = false;
                                preSplit = preSplit[2].split("~");
                                for (int i = 0; i < preSplit.length; i++) {
                                    if (preSplit[i].equals(proNewName)) {
                                        writer.write("repeat");
                                        writer.println();
                                        writer.flush();
                                        j = true;
                                        break;
                                    }
                                }
                                if (j)
                                    break;
                            }
                            if (t < 0 || d < 0) {
                                writer.write("error");
                                writer.println();
                                writer.flush();
                                break;
                            }
                            p = Product.getProduct(oldProductName, s);
                            ArrayList<String> productLines = Product.getAllLines();
                            for (int i = 0; i < productLines.size(); i++) {
                                String[] productData = productLines.get(i).split(",");
                                if (productData[0].equals(oldProductName) && productData[1].equals(s)) {
                                    productData[0] = proNewName;
                                    productData[2] = proNewDescription;
                                    productData[3] = proNewQuantity;
                                    productData[4] = proNewPrice;
                                    StringBuilder newProductLine = new StringBuilder();
                                    for (int j = 0; j < productData.length; j++) {
                                        newProductLine.append(productData[j]);
                                        if (j != productData.length - 1) {
                                            newProductLine.append(",");
                                        }
                                    }
                                    productLines.set(i, newProductLine.toString());
                                    PrintWriter pw = new PrintWriter(new FileOutputStream("products.txt"));
                                    for (int j = 0; j < productLines.size(); j++) {
                                        pw.println(productLines.get(j));
                                    }
                                    pw.flush();
                                    pw.close();
                                    break;
                                }
                            }
                            p.setPrice(d);
                            p.setName(proNewName);
                            p.setQuantity(t);
                            p.setProductDescription(proNewDescription);
                            String[] split = null;
                            for (int i = 0; i < temp.size(); i++) {
                                split = temp.get(i).split(",");
                                if (split[0].equals(s)) {
                                    String[] split2 = split[2].split("~");
                                    StringBuilder replace = new StringBuilder();
                                    for (int j = 0; j < split2.length; j++) {
                                        if (split2[j].equals(oldProductName)) {
                                            replace.append(proNewName);
                                        } else
                                            replace.append(split2[j]);
                                        if (j != split2.length - 1) {
                                            replace.append("~");
                                        }
                                    }
                                    split[2] = replace.toString();
                                }
                            }
                            StringBuilder replace = new StringBuilder();
                            for (int i = 0; i < Objects.requireNonNull(split).length; i++) {
                                replace.append(split[i]);
                                if (i != split.length - 1) {
                                    replace.append(",");
                                }
                            }

                            ArrayList<String> newTemp = new ArrayList<>();
                            for (int i = 0; i < temp.size(); i++) {
                                if (temp.get(i).split(",")[0].equals(s)) {
                                    newTemp.add(replace.toString());
                                } else
                                    newTemp.add(temp.get(i));
                            }

                            PrintWriter pw2 = new PrintWriter(new FileOutputStream("stores.txt", false));
                            for (int j = 0; j < newTemp.size(); j++) {
                                pw2.println(newTemp.get(j));
                            }
                            pw2.flush();
                            pw2.close();
                            writer.write("ok");
                            writer.println();
                            writer.flush();


                        } catch (Exception e) {
                            writer.write("error");
                            writer.println();
                            writer.flush();
                        }
                        break;
                    case 16:
                        String productName = reader.readLine();
                        String stoName = reader.readLine();
                        ArrayList<Store> sto = Store.getAllStores();
                        Store store1 = null;
                        p = Product.getProduct(productName, stoName);
                        for (int i = 0; i < sto.size(); i++) {
                            if(sto.get(i).getName().equals(stoName)) {
                                store1 = sto.get(i);
                                break;
                            }
                        }
                        assert store1 != null;
                        ArrayList<Product> l = store1.getProducts();
                        ArrayList<Product> ll = new ArrayList<>();

                        for (int i = 0; i < l.size(); i++) {
                            if (!l.get(i).equals(p)) {
                                ll.add(l.get(i));
                            }
                        }

                        store1.setProducts(ll);
                        store1.editStoreFile();
                        assert p != null;
                        p.deleteProduct();

                        StringBuilder stri = new StringBuilder();
                        for (int i = 0; i < ll.size(); i++) {
                            stri.append(ll.get(i).getName());
                            if (i != ll.size() - 1) {
                                stri.append(",");
                            }
                        }
                        writer.write(stri.toString());
                        writer.println();
                        writer.flush();
                        break;
                    case 17: // Sort stores by general popularity for customer dashboard
                        ArrayList<Store> sortedByTotalProductsSold =
                                Driver.sortByTotalProductsSold(Store.getAllStores());
                        StringBuilder storesSortedByProductsSold = new StringBuilder("");
                        for (int i = 0; i < sortedByTotalProductsSold.size(); i++) {
                            if (i == sortedByTotalProductsSold.size() - 1) {
                                storesSortedByProductsSold.append("Name: " + sortedByTotalProductsSold.get(i).getName()
                                        + " Total Products Sold: " + sortedByTotalProductsSold.get(i).getTotalSales());
                            } else {
                                storesSortedByProductsSold.append("Name: " + sortedByTotalProductsSold.get(i).getName()
                                        + " Total Products Sold: " + sortedByTotalProductsSold.get(i).getTotalSales()
                                        + ",");
                            }
                        }
                        writer.write(String.valueOf(storesSortedByProductsSold));
                        writer.println();
                        writer.flush();
                        break;
                    case 18: // Sort stores by individual customer popularity for customer dashboard
                        user = Customer.parseCustomer(user.getEmail());
                        ArrayList<Store> sortedByCustomerFavorite =
                                Driver.sortByProductsSoldToUser(Store.getAllStores(), ((Customer)user));
                        StringBuilder storesSortedByCustomerFavorite = new StringBuilder("");
                        for (int i = 0; i < sortedByCustomerFavorite.size(); i++) {
                            if (i == sortedByCustomerFavorite.size() - 1) {
                                storesSortedByCustomerFavorite.append("Name: " +
                                        sortedByCustomerFavorite.get(i).getName() +
                                        " Your purchases: " +
                                        sortedByCustomerFavorite.get(i).getProductsBoughtByCurrentUser
                                                (((Customer)user)));
                            } else {
                                storesSortedByCustomerFavorite.append("Name: " +
                                        sortedByCustomerFavorite.get(i).getName() +
                                        " Your purchases: " +
                                        sortedByCustomerFavorite.get(i).getProductsBoughtByCurrentUser
                                                (((Customer)user)) + ",");
                            }
                        }
                        writer.write(String.valueOf(storesSortedByCustomerFavorite));
                        writer.println();
                        writer.flush();
                        break;
                    case 19:
                        String[] currentProductData = reader.readLine().split(",");
                        String currentProductName = currentProductData[0];
                        String currentProductStore = currentProductData[1];
                        int quantityDesired = Integer.parseInt(currentProductData[2]);
                        Product currentProduct = Product.getProduct(currentProductName, currentProductStore);
                        if (quantityDesired > currentProduct.getQuantity())
                        {
                            writer.write("quantity error");
                            writer.println();
                            writer.flush();
                        } else {
                            for (int i = 0; i < quantityDesired; i++)
                            {
                                ((Customer) user).addToCart(currentProduct);
                            }
                            writer.write("added to cart");
                            writer.println();
                            writer.flush();
                        }
                        break;
                    case 20:
                        ArrayList<Store> stores = ((Seller) user).getStores();
                        for(int i = 0; i < stores.size(); i++) {
                            int max = i;
                            for(int j = i; j < stores.size(); j++) {
                                if(stores.get(j).getTotalSales() > stores.get(max).getTotalSales()) {
                                    max = j;
                                }
                                Store swap = stores.get(i);
                                stores.set(i, stores.get(max));
                                stores.set(max, swap);
                            }
                        }
                        String sortedStores = "";
                        for(int k = 0; k < stores.size(); k++) {
                            if(k == stores.size() - 1) {
                                sortedStores += "Name: " + stores.get(k).getName() + " Sales: " +
                                        stores.get(k).getTotalSales();
                            } else {
                                sortedStores += "Name: " + stores.get(k).getName() + " Sales: " +
                                        stores.get(k).getTotalSales() + ",";
                            }
                        }
                        writer.write(sortedStores);
                        writer.println();
                        writer.flush();
                        break;
                    case 21:

                        break;
                    case 22:
                        ArrayList<Product> userCart = ((Customer) user).getShoppingCart();
                        System.out.println(userCart.size());
                        for (int i = 0; i < userCart.size(); i++)
                        {
                            System.out.println(userCart.get(i).toString());
                        }
                        StringBuilder itemsInCartInfo = new StringBuilder();
                        String singleProductInfo;


                        for (int i = 0; i < userCart.size(); i++)
                        {
                            singleProductInfo = String.format("Product: %s   Store: %s   Price: $%.2f",
                                    userCart.get(i).getName(), userCart.get(i).getStore(),
                                    userCart.get(i).getPrice());
                            itemsInCartInfo.append(singleProductInfo);

                            if (i != userCart.size() - 1) {
                                itemsInCartInfo.append(",");
                            }
                        }

                        writer.write(itemsInCartInfo.toString());
                        writer.println();
                        writer.flush();

                        break;
                    default:
                        running = false;
                        writer.close();
                        break;
                }
            }
        } catch (Exception e) {
            //when the client is done and leaves
        }
    }
}

