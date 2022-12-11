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
    public static final Object obj = new Object();

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            int buttonChoice = 0;
            boolean running = true;
            while (running) {
                buttonChoice = Integer.parseInt(reader.readLine());
                String email, type;
                switch (buttonChoice) {
                    case 1: // create an account, used for both sellers and customers
                        String[] info = reader.readLine().split(",");
                        boolean alreadyUsed = User.checkNewUser(info[1]);

                        if (!alreadyUsed) {
                            boolean isCustomer = Boolean.parseBoolean(info[3]);
                            writer.write("new");
                            writer.println();
                            writer.flush();

                            if (isCustomer) {
                                user = new Customer(info[0], info[1], info[2], true,
                                        info[4], Integer.parseInt(info[5]));

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
                                user = new Seller(info[0], info[1], info[2], false,
                                        info[4], Integer.parseInt(info[5]));
                            }

                        } else {
                            //System.out.println("already used");
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
                                for (int i = 0; i < ((Seller) user).getStores().size(); i++) {
                                    line.append(((Seller) user).getStores().get(i).getName());
                                    if (i != ((Seller) user).getStores().size() - 1) {
                                        line.append(",");
                                    }
                                }
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

                        String sellerNewPass = reader.readLine(); // read new pass from client
                        String sellerNewName = reader.readLine();
                        String sellerNewEmail = reader.readLine(); // read new email from client
                        String oldSellerEmail = user.getEmail();

                        user = Seller.parseSeller(oldSellerEmail);


                        assert user != null;
                        ArrayList<String> userLines = ((Seller) user).readUserFile();
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
                                    synchronized (obj) {
                                        pw.println(userLines.get(x));
                                    }
                                }
                                pw.flush();
                                pw.close();
                                break;
                            }
                        }
                        user.setPassword(sellerNewPass);
                        user.setName(sellerNewName);
                        user.setEmail(sellerNewEmail);
                        for (int i = 0; i < ((Seller) user).getStores().size(); i++) {
                            ((Seller) user).getStores().get(i).setOwner(sellerNewEmail);
                            ((Seller) user).getStores().get(i).editStoreFile();
                        }
                        break;
                    case 6: // confirmEditCustomer buttons

                        String customerNewPass = reader.readLine(); // read new pass from client
                        String customerNewName = reader.readLine();
                        String customerNewEmail = reader.readLine(); // read new email from client
                        String oldCustomerEmail = user.getEmail();

                        user = Customer.parseCustomer(oldCustomerEmail);


                        assert user != null;
                        ArrayList<String> customerLines = ((Customer) user).readUserFile();
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
                                    synchronized (obj) {
                                        pw.println(customerLines.get(x));
                                    }
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
                    case 7: // takes seller to the selected store screen, puts all the products in a dropdown
                        String selectedStore = reader.readLine();
                        ArrayList<Store> fileStores = Store.getAllStores();
                        Store selected = null;

                        for (int i = 0; i < fileStores.size(); i++) {
                            if (fileStores.get(i).getOwner().equals(user.getEmail()) &&
                                    fileStores.get(i).getName().equals(selectedStore)) {
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
                    case 8: // create a store
                        String newStoreName = reader.readLine();
                        if (newStoreName.equals("x button"))
                        {
                            break;
                        } else {
                            ArrayList<Store> allStores = Store.getAllStores();
                            boolean used = false;

                            for (int i = 0; i < allStores.size(); i++) {
                                if (newStoreName.equals(allStores.get(i).getName())) {
                                    writer.write("already used");
                                    writer.println();
                                    writer.flush();
                                    used = true;
                                    break;
                                }
                            }

                            if (!used) {
                                ArrayList<Store> temp = ((Seller) user).getStores();
                                temp.add(new Store(user.getEmail(), newStoreName, new ArrayList<Product>()));
                                ((Seller) user).setStores(temp);
                                user.editUserFile();

                                StringBuilder line = new StringBuilder();
                                for (int i = 0; i < ((Seller) user).getStores().size(); i++) {
                                    line.append(((Seller) user).getStores().get(i).getName());
                                    if (i != ((Seller) user).getStores().size() - 1) {
                                        line.append(",");
                                    }
                                }

                                writer.write(line.toString());
                                writer.println();
                                writer.flush();

                            }
                            break;
                        }
                    case 9: // delete store
                        String storeName = reader.readLine();

                        ArrayList<Store> newStoreList = new ArrayList<Store>();
                        ArrayList<Store> currentStores = ((Seller) user).getStores();

                        // copies all non-target stores to a new array
                        for (int i = 0; i < currentStores.size(); i++) {
                            if (!(currentStores.get(i).getName().equals(storeName))) {
                                newStoreList.add(currentStores.get(i));
                            }
                        }

                        //updating customer carts by removing product that belong to the target store
                        ArrayList<Store> allStores1 = Store.getAllStores();
                        Store selectStore = null;
                        for (Store store : allStores1) {
                            if (store.getName().equals(storeName)) {
                                selectStore = store;
                                //System.out.println("selectStore: " + selectStore.getName());
                            }
                        }
                        ArrayList<Product> prods;
                        ArrayList<Customer> customer = Customer.getAllCustomers();
                        ArrayList<Product> tempArr;
                        for (int i = 0; i < customer.size(); i++) {
                            tempArr = new ArrayList<Product>();
                            prods = customer.get(i).getShoppingCart();
                            for (int j = 0; j < prods.size(); j++) {
                                assert selectStore != null;
                                if (!(prods.get(j).getStore().equals(selectStore.getName()))) {
                                    tempArr.add(prods.get(j));
                                }
                            }
                            /*System.out.println("New Products: ");
                            for(Product x : tempArr)
                                System.out.print(x.getName());*/
                            //System.out.println();
                            customer.get(i).setShoppingCart(tempArr);
                            customer.get(i).editUserFile();
                        }

                        // updating this user's list of stores and also
                        ((Seller) user).setStores(newStoreList); // changes Seller object's stores array
                        user.removeStoreFromFile(storeName); // removes store from stores.txt
                        ((Seller) user).editUserFile(); // updates users.txt with new stores arraylist
                        Store.updateProducts(storeName); //updates products.txt; removes all products from deleted store

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

                    case 10: // create new product

                        String[] newProductInfo = reader.readLine().split(",");
                        if (newProductInfo[0].contains(",") || newProductInfo[0].contains("~") || newProductInfo[0].contains("-") || newProductInfo[0].isEmpty()) {
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
                                if (newProductPrice < 0) {
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

                            if (keepGoing) {
                                try {
                                    int newProductQuantity = Integer.parseInt(newProductInfo[2]);
                                    if (newProductQuantity < 0) {
                                        writer.write("quantity error");
                                        writer.println();
                                        writer.flush();
                                        keepGoing = false;
                                    }
                                } catch (NumberFormatException e) {
                                    writer.write("quantity error");
                                    writer.println();
                                    writer.flush();
                                    keepGoing = false;
                                }
                            }


                            if (keepGoing) {
                                ArrayList<String> productFileLines = Seller.readProductFile();
                                boolean duplicate = false;
                                for (int i = 0; i < Objects.requireNonNull(productFileLines).size(); i++) {
                                    String[] productSplit = productFileLines.get(i).split(",");
                                    int pQuantity = Integer.parseInt(productSplit[3]);
                                    double pPrice = Double.parseDouble(productSplit[4]);
                                    if (productSplit[0].equals(newProductInfo[0]) &&
                                            productSplit[1].equals(store.getName()) &&
                                            productSplit[2].equals(newProductInfo[1]) &&
                                            pQuantity == Integer.parseInt(newProductInfo[2]) &&
                                            pPrice == Double.parseDouble(newProductInfo[3])) {
                                        duplicate = true;
                                        break;
                                    }
                                }

                                if (duplicate) {
                                    writer.write("duplicate");
                                } else {
                                    Product product = new Product(newProductInfo[0], store.getName(), newProductInfo[1],
                                            Integer.parseInt(newProductInfo[2]), Double.parseDouble(newProductInfo[3]));
                                    ArrayList<Product> storeProductsNew = store.getProducts();
                                    storeProductsNew.add(product);
                                    store.setProducts(storeProductsNew);
                                    store.editStoreFile();
                                    StringBuilder stri = new StringBuilder("");
                                    for (int i = 0; i < storeProductsNew.size(); i++) {
                                        stri.append(storeProductsNew.get(i).getName());
                                        if (i != storeProductsNew.size() - 1) {
                                            stri.append(",");
                                        }
                                    }
                                    writer.write(stri.toString());
                                }
                                writer.println();
                                writer.flush();


                            }
                        }
                        break;
                    case 11: // select product for seller
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
                    case 12: // search for the customer
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
                        //System.out.println(results.toString());
                        writer.write(results.toString());
                        writer.println();
                        writer.flush();
                        break;
                    case 13: // view all products
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
                    case 14: // sends the data of a selected product back to client
                        // this allows client to list the data in the products' page
                        String productDataViewCustomer = reader.readLine();
                        String productNameViewCustomer = productDataViewCustomer.substring(9,
                                productDataViewCustomer.indexOf("Store:") - 3);

                        String productStoreViewCustomer = productDataViewCustomer.substring(
                                productDataViewCustomer.indexOf("Store:") + 7,
                                productDataViewCustomer.indexOf("Price:") - 3);

                        Product viewCustomerProduct = null;
                        viewCustomerProduct = Product.getProduct(productNameViewCustomer, productStoreViewCustomer);

                        productDataViewCustomer = viewCustomerProduct.getName() + "," + viewCustomerProduct.getProductDescription() + "," +
                                viewCustomerProduct.getStore() + "," + String.format("%.2f", viewCustomerProduct.getPrice())
                                + "," + viewCustomerProduct.getQuantity();


                        writer.write(productDataViewCustomer);
                        writer.println();
                        writer.flush();

                        break;
                    case 15: // edit a product
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
                                if (preSplit[0].equals(s)) {
                                    ayo = true;
                                    break;
                                }
                            }
                            if (ayo) {
                                boolean j = false;
                                preSplit = preSplit[2].split("~");
                                for (int i = 0; i < preSplit.length; i++) {
                                    if (preSplit[i].equals(proNewName) && !proNewName.equals(oldProductName)) {
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
                                        synchronized (obj) {
                                            pw.println(productLines.get(j));
                                        }
                                    }
                                    pw.flush();
                                    pw.close();
                                    break;
                                }
                            }
                            assert p != null;
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
                                synchronized (obj) {
                                    pw2.println(newTemp.get(j));
                                }
                            }
                            pw2.flush();
                            pw2.close();

                            ArrayList<Customer> cus = Customer.getAllCustomers();
                            ArrayList<Product> editing;
                            for (int i = 0; i < cus.size(); i++) {
                                editing = cus.get(i).getShoppingCart();
                                for (int j = 0; j < editing.size(); j++) {
                                    if (editing.get(j).getName().equals(oldProductName) &&
                                            editing.get(j).getStore().equals(s)) {
                                        editing.get(j).setName(proNewName);
                                    }
                                }
                                cus.get(i).setShoppingCart(editing);
                                cus.get(i).editUserFile();
                            }

                            writer.write("ok");
                            writer.println();
                            writer.flush();


                        } catch (Exception e) {
                            e.printStackTrace();
                            writer.write("error");
                            writer.println();
                            writer.flush();
                        }
                        break;
                    case 16: // delete product
                        String productName = reader.readLine();
                        String stoName = reader.readLine();
                        ArrayList<Store> sto = Store.getAllStores();
                        Store store1 = null;
                        p = Product.getProduct(productName, stoName);
                        for (int i = 0; i < sto.size(); i++) {
                            if (sto.get(i).getName().equals(stoName)) {
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
                        ArrayList<Customer> c = Customer.getAllCustomers();
                        ArrayList<Product> temp;
                        ArrayList<Product> temp2;
                        for (int i = 0; i < c.size(); i++) {
                            temp2 = new ArrayList<Product>();
                            temp = c.get(i).getShoppingCart();
                            for (int j = 0; j < temp.size(); j++) {
                                if (!temp.get(j).getName().equals(productName) && !temp.get(j).getStore().equals(stoName)) {
                                    temp2.add(temp.get(j));
                                }
                            }
                            c.get(i).setShoppingCart(temp2);
                            c.get(i).editUserFile();
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
                                storesSortedByProductsSold.append("Name: ").append(sortedByTotalProductsSold.get(i).getName()).append(" Total Products Sold: ").append(sortedByTotalProductsSold.get(i).getTotalSales());
                            } else {
                                storesSortedByProductsSold.append("Name: ").append(sortedByTotalProductsSold.get(i).getName()).append(" Total Products Sold: ").append(sortedByTotalProductsSold.get(i).getTotalSales()).append(",");
                            }
                        }
                        writer.write(String.valueOf(storesSortedByProductsSold));
                        writer.println();
                        writer.flush();
                        break;
                    case 18: // Sort stores by individual customer popularity for customer dashboard
                        user = Customer.parseCustomer(user.getEmail());
                        ArrayList<Store> sortedByCustomerFavorite =
                                Driver.sortByProductsSoldToUser(Store.getAllStores(), ((Customer) user));
                        StringBuilder storesSortedByCustomerFavorite = new StringBuilder("");
                        for (int i = 0; i < sortedByCustomerFavorite.size(); i++) {
                            if (i == sortedByCustomerFavorite.size() - 1) {
                                storesSortedByCustomerFavorite.append("Name: ").append(sortedByCustomerFavorite.get(i).getName()).append(" Your purchases: ").append(sortedByCustomerFavorite.get(i).getProductsBoughtByCurrentUser
                                        (((Customer) user)));
                            } else {
                                storesSortedByCustomerFavorite.append("Name: ").append(sortedByCustomerFavorite.get(i).getName()).append(" Your purchases: ").append(sortedByCustomerFavorite.get(i).getProductsBoughtByCurrentUser
                                        (((Customer) user))).append(",");
                            }
                        }
                        writer.write(String.valueOf(storesSortedByCustomerFavorite));
                        writer.println();
                        writer.flush();
                        break;
                    case 19: // add to cart
                        String[] currentProductData = reader.readLine().split(",");
                        String currentProductName = currentProductData[0];
                        String currentProductStore = currentProductData[1];
                        int quantityDesired = Integer.parseInt(currentProductData[2]);
                        Product currentProduct = Product.getProduct(currentProductName, currentProductStore);
                        assert currentProduct != null;
                        if (quantityDesired > currentProduct.getQuantity()) {
                            writer.write("quantity error");
                            writer.println();
                            writer.flush();
                        } else {
                            for (int i = 0; i < quantityDesired; i++) {
                                ((Customer) user).addToCart(currentProduct);
                            }
                            writer.write("added to cart");
                            writer.println();
                            writer.flush();
                        }
                        break;
                    case 20:// sort stores by greatest sales for seller dash
                        ArrayList<Store> stores = Store.getAllStores(user.getEmail());
                        ((Seller) user).setStores(stores);
                        for (int i = 0; i < stores.size(); i++) {
                            int max = i;
                            for (int j = i; j < stores.size(); j++) {
                                if (stores.get(j).getTotalSales() > stores.get(max).getTotalSales()) {
                                    max = j;
                                }
                                Store swap = stores.get(i);
                                stores.set(i, stores.get(max));
                                stores.set(max, swap);
                            }
                        }
                        StringBuilder sortedStores = new StringBuilder();
                        for (int k = 0; k < stores.size(); k++) {
                            if (k == stores.size() - 1) {
                                sortedStores.append("Name: ").append(stores.get(k).getName()).append(" Sales: ").append(stores.get(k).getTotalSales());
                            } else {
                                sortedStores.append("Name: ").append(stores.get(k).getName()).append(" Sales: ").append(stores.get(k).getTotalSales()).append(",");
                            }
                        }
                        writer.write(sortedStores.toString());
                        writer.println();
                        writer.flush();
                        break;
                    case 21: // sort stores by greatest revenue for seller dash
                        /**
                         * first get a list of the seller's stores
                         * then for each store get a list of all the products
                         * for each product, multiply the price times the number sold; this is revenue
                         * for every product in a store, add up all the revenue; this is the store's revenue
                         * store each store's revenue in an arraylist and sort from highest to lowest
                         */

                        stores = Store.getAllStores(user.getEmail());
                        ((Seller) user).setStores(stores);
                        ArrayList<Product> prod;
                        ArrayList<Double> storeRevenue = new ArrayList<Double>();
                        double rev;
                        for (int i = 0; i < stores.size(); i++) {
                            prod = new ArrayList<Product>();
                            rev = 0;
                            prod = stores.get(i).getProducts();
                            for (Product product : prod) {
                                rev += product.getPrice() * product.getAmountSold();
                            }
                            storeRevenue.add(rev);
                        }

                        for (int j = 0; j < stores.size(); j++) {
                            int max = j;
                            for (int k = j; k < stores.size(); k++) {
                                if (storeRevenue.get(k) > storeRevenue.get(j)) {
                                    max = k;
                                }
                                double revSwap = storeRevenue.get(j);
                                storeRevenue.set(j, storeRevenue.get(max));
                                storeRevenue.set(max, revSwap);

                                Store swap = stores.get(j);
                                stores.set(j, stores.get(max));
                                stores.set(max, swap);
                            }
                        }
                        StringBuilder storesSortedByRevenue = new StringBuilder();
                        String oneStore;
                        for(int i = 0; i < stores.size(); i++) {
                            if(i == stores.size() - 1) {
                                oneStore = String.format("Store: %s - Revenue: $%.2f", stores.get(i).getName(), storeRevenue.get(i));
                                storesSortedByRevenue.append(oneStore);
                                //storesSortedByRevenue.append("Store: ").append(stores.get(i).getName()).append(" - Revenue: $").append(storeRevenue.get(i));
                            } else {
                                oneStore = String.format("Store: %s - Revenue: $%.2f,", stores.get(i).getName(), storeRevenue.get(i));
                                storesSortedByRevenue.append(oneStore);
                                //storesSortedByRevenue.append("Store: ").append(stores.get(i).getName()).append(" - Revenue: $").append(storeRevenue.get(i)).append(", ");
                            }
                        }

                        writer.write(storesSortedByRevenue.toString());
                        writer.println();
                        writer.flush();
                        break;
                    case 22: // sends customer to their cart, is used in resetViewCart
                        ArrayList<Product> userCart = ((Customer) user).getShoppingCart();
                        StringBuilder itemsInCartInfo = new StringBuilder();
                        String singleProductInfo;

                        for (int i = 0; i < userCart.size(); i++) {
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

                    case 23: // buy cart
                        StringBuilder failedProducts = ((Customer) user).purchaseProductsInCartProject5();
                        writer.write(failedProducts.toString());
                        writer.println();
                        writer.flush();

                        break;
                    case 24: // remove from cart
                        String line = reader.readLine();

                        String removableProductName = line.substring(9, line.indexOf("Store:") - 3);
                        String removableProductStore =
                                line.substring(line.indexOf("Store:") + 7, line.indexOf("Price:") - 3);

                        ArrayList<Product> cart = ((Customer) user).getShoppingCart();
                        ArrayList<Product> newCart = new ArrayList<Product>();

                        Product remove = null;
                        for (int i = 0; i < cart.size(); i++) {
                            if (cart.get(i).getName().equals(removableProductName) &&
                                    cart.get(i).getStore().equals(removableProductStore)) {
                                remove = cart.get(i);
                                break;
                            }
                        }

                        boolean removed = false;


                        for (int i = 0; i < cart.size(); i++) {
                            assert remove != null;
                            if (!cart.get(i).getName().equals(remove.getName())) {
                                newCart.add(cart.get(i));
                            } else if (cart.get(i).getName().equals(remove.getName()) && removed) {
                                newCart.add(cart.get(i));
                            } else {
                                removed = true;
                            }
                        }
                        ((Customer) user).setShoppingCart(newCart);
                        ((Customer) user).editUserFile();
                        break;

                    case 25: // remove all from cart
                        ((Customer) user).setShoppingCart(new ArrayList<Product>());
                        ((Customer) user).editUserFile();
                        break;
                    case 26: // export customer's purchase history to a file on their PC ("emailPurchaseHistory.txt")
                        StringBuilder purchaseHistoryString = new StringBuilder("");
                        String[] split;
                        if (((Customer) user).getPurchaseHistory().size() == 0) {
                            purchaseHistoryString = new StringBuilder("empty");
                        } else {
                            String pInfo;
                            for (int i = 0; i < ((Customer) user).getPurchaseHistory().size(); i++) {
                                split = ((Customer) user).getPurchaseHistory().get(i).split("-");
                                if (i == ((Customer) user).getPurchaseHistory().size() - 1) {
                                    pInfo = "Product Name: " + split[1] + ", Store: " + split[0];
                                } else {
                                    pInfo = "Product Name: " + split[1] + ", Store: " + split[0] + "~";
                                }
                                purchaseHistoryString.append(pInfo);
                            }
                        }
                        writer.write(String.valueOf(purchaseHistoryString));
                        writer.println();
                        writer.flush();
                        if (((Customer) user).getPurchaseHistory().size() > 0) {
                            String customerEmail = user.getEmail();
                            writer.write(customerEmail);
                            writer.println();
                            writer.flush();
                        }
                        break;
                    case 27: // view purchase history
                        ArrayList<String> history = ((Customer)user).getPurchaseHistory();
                        if (history.size() == 0) {
                            writer.write("none");
                        } else {
                            StringBuilder string = new StringBuilder();
                            for (int i = 0; i < history.size(); i++) {
                                string.append(history.get(i));
                                if (i != history.size() - 1) {
                                    string.append(",");
                                }
                            }
                            writer.write(string.toString());
                        }
                        writer.println();
                        writer.flush();
                        break;
                    case 28: // view products in carts for the seller
                        ArrayList<String> sending = ((Seller) user).viewCustomerCarts();
                        if (sending.size() > 0) {
                            StringBuilder s = new StringBuilder();
                            for (int i = 0; i < sending.size(); i++) {
                                s.append(sending.get(i));
                                //System.out.println(sending.get(i));
                                if (i != sending.size() - 1) {
                                    s.append(",");
                                }
                            }
                            writer.write(s.toString());
                        } else {
                            writer.write("none");
                        }
                        writer.println();
                        writer.flush();
                        break;
                    case 29: // import products for stores from a file
                        String importProductFileData = reader.readLine();
                        ArrayList<String> productFileLines = Seller.readProductFile();
                        String[] importProductData = importProductFileData.split("~");
                        for (int i = 0; i < importProductData.length; i++) {
                            boolean alreadyExists = false; // true if the product already exists in the user's store
                            String[] importLineData = importProductData[i].split(",");
                            for (int j = 0; j < Objects.requireNonNull(productFileLines).size(); j++) {
                                String[] productLineData = productFileLines.get(j).split(",");
                                if (importLineData[0].equals(productLineData[0]) &&
                                        importLineData[1].equals(productLineData[1])) {
                                    alreadyExists = true; // product exists in user's store already so set true
                                    break;
                                }
                            }
                            if (!alreadyExists) { // creates new product and updates the store file accordingly
                                try {
                                    Product newP = new Product(importLineData[0], importLineData[1], importLineData[2],
                                            Integer.parseInt(importLineData[3]), Double.parseDouble(importLineData[4]));
                                    ArrayList<Store> userStores = ((Seller) user).getStores();
                                    for (int x = 0; x < userStores.size(); x++) {
                                        if (userStores.get(x).getName().equals(newP.getStore())) {
                                            userStores.get(x).getProducts().add(newP);
                                            userStores.get(x).editStoreFile();
                                        }
                                    }
                                } catch (Exception e) {
                                    // System.out.println("Error parsing products!");
                                }
                            }
                        }
                        break;
                    case 30: // export a store's products to a file
                        user = Seller.parseSeller(user.getEmail());
                        StringBuilder storeProductData = new StringBuilder("");
                        assert user != null;
                        ArrayList<Store> userStores = ((Seller) user).getStores();
                        String storeToBeExportedName = reader.readLine();
                        Store storeToBeExported = null;
                        for (int i = 0; i < userStores.size(); i++) {
                            if (userStores.get(i).getName().equals(storeToBeExportedName)) {
                                storeToBeExported = userStores.get(i);
                                break;
                            }
                        }
                        assert storeToBeExported != null;
                        ArrayList<Product> storeToBeExportedProducts = storeToBeExported.getProducts();
                        for (int i = 0; i < storeToBeExportedProducts.size(); i++) {
                            Product product = storeToBeExportedProducts.get(i);
                            String priceFormatted = String.format("%.2f", product.getPrice());
                            String productString;
                            if (i == storeToBeExportedProducts.size() - 1) {
                                productString = product.getName() + "," + product.getStore() + "," +
                                        product.getProductDescription() + "," + product.getQuantity() + "," +
                                        priceFormatted + "," + product.getAmountSold();
                            } else {
                                productString = product.getName() + "," + product.getStore() + "," +
                                        product.getProductDescription() + "," + product.getQuantity() + "," +
                                        priceFormatted + "," + product.getAmountSold() + "~";
                            }
                            storeProductData.append(productString);
                        }
                        writer.write(String.valueOf(storeProductData));
                        writer.println();
                        writer.flush();
                        break;
                    case 31: // refresh customer page
                        ArrayList<Product> pro = Product.getAllProducts();
                        StringBuilder pro2 = new StringBuilder();
                        String singlePro;

                        for (int i = 0; i < pro.size(); i++) {
                            singlePro = String.format("Product: %s   Store: %s   Price: $%.2f",
                                    pro.get(i).getName(), pro.get(i).getStore(),
                                    pro.get(i).getPrice());
                            pro2.append(singlePro);

                            if (i != pro.size() - 1) {
                                pro2.append(",");
                            }
                        }
                        writer.write(pro2.toString());
                        writer.println();
                        writer.flush();
                        break;
                    case 32: // refresh seller page
                        StringBuilder lines = new StringBuilder();
                        for (int i = 0; i < ((Seller) user).getStores().size(); i++) {
                            lines.append(((Seller) user).getStores().get(i).getName());
                            if (i != ((Seller) user).getStores().size() - 1) {
                                lines.append(",");
                            }
                        }
                        writer.write(lines.toString());
                        writer.println();
                        writer.flush();
                        break;
                    case 33: // refresh stores page
                        String ss = reader.readLine();
                        ArrayList<Store> fileStore = Store.getAllStores();
                        Store select = null;

                        for (int i = 0; i < fileStore.size(); i++) {
                            if (fileStore.get(i).getOwner().equals(user.getEmail()) &&
                                    fileStore.get(i).getName().equals(ss)) {
                                select = fileStore.get(i);
                                break;
                            }
                        }
                        store = select;

                        ArrayList<Product> storeProduct;
                        if (select != null)
                            storeProduct = select.getProducts();
                        else
                            storeProduct = new ArrayList<>();

                        StringBuilder strings = new StringBuilder("");
                        for (int i = 0; i < storeProduct.size(); i++) {
                            strings.append(storeProduct.get(i).getName());
                            if (i != storeProduct.size() - 1)
                                strings.append(",");
                        }
                        writer.write(strings.toString());
                        writer.println();
                        writer.flush();
                        break;

                    case 34:
                        user.deleteAccount();
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