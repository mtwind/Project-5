import java.io.*;
import java.util.*;
/**
 * DRIVER CLASS
 *
 * Contains sort methods we use in other part of the project
 *
 * @author L12 group 2
 * @version 11 December 2022
 */
public class Driver {

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
