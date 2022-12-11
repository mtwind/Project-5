# Project-5
### How to compile and run our project
- Our project is a marketplace, which allows Sellers to create stores and products and for Customers to interact with them in various ways.
- Program must start with three text files that must exist at the start of the program: "products.txt", "stores.txt", and "users.txt". These three text files must all be empty the first time runnning the program. They serve as the foundation of storing data in our program.
- IMPORTANT: To run the program, first run Server.java, and then run Client.java for each user as needed
- User information is stored in "users.txt", each line in the file represents a user.
- Line format for a customer in "users.txt": customer,email,name,password,securityQuestionNumber,securityQuestionAnswer,shopping cart data, purchase history data
- Format for a seller in "users.txt": seller,email,name,password,securityQuestionNumber,securityQuestionAnswer,store names strings
- Product information is stored in "products.txt", each line in the file represents a product
- Line format for a product in "products.txt": productName,storeName,description,quantity,price,amountSold
- Store information is stored in "stores.txt", each line in the file represents a store
- Line format for a store in "stores.txt": storeName,sellerEmail,product names strings,sales,customer email strings,sales per customer ints
- The program should compile and run correctly if these three text files exist and are empty the very first time running the program.
### Who submitted which parts of the assignment to Brightspace?
- Dalton Babbs - Submitted Report on Brightspace
- Paige Lorenz - Submitted Project on Vocareum Workspace
## Descriptions of Classes
### Driver.java
The main method of our project is stored in the Driver.java class. This class implements the functionality that was created by the other classes. Users are first prompted to log in with an email and password. If they don’t have an account already they are given the option to create one with an email, password, and security question. While creating an account they must choose if they are a “Seller” or a “Customer”. If they already have an account but forgot their password, they are given the opportunity to change their password. In order to do this, they must successfully answer a security question that they choose when their account was first created. If they answer correctly, they are prompted to change their password. After logging in, the user is directed to either the seller or customer menu depending on their account type. The driver class also contains a variety of methods that allow Customers to view the marketplace of products and sort it by prices, quantity, or nothing. It contains methods for Sellers to see their store statistics and the ability for them to sort their stores by revenue, number of products, or no sort at all.
Testing:
- Test cases
- Extensive manual testing: Our group members constantly ran through the main method and manual entered in different inputs to see if we got the expected result. For example, we would create an account and see if it gets properly stored in "users.txt". We would then check to see if we could successfully log into the account using its email and password. We checked to see if we could change our password by answering our security question. These are just a few examples of our manual testing. 
- Print statements: We tested using print statements to exactly what the values of our variables were and where our program was actually running
- Breakpoints: We utilized the debugging tools in Intellij to check the program line by line to find errors
### User.java
The User.java class is a basic class that is the parent class of both Customer.java and Seller.java. This class contains basic variables such as the user’s name, email, password, security question number and answer. There are 6 security questions for users to select from stored in this class. The class contains two different constructors, one is for new users and one is for returning users. It also contains a method to check if the user already has an account and two methods that allow a user to delete their account. It also contains functionality for removing stores from the stores.txt file. It can check passwords, security questions, and determine if a user exists or not.
Testing:
- Test cases
- Extensive manual testing: Manually tested if user data was properly stored and updated in "users.txt" among many other things
- Breakpoints: debugging tools used to read program line by line to make sure all variables were the correct value and that the program properly reads "users.txt"
### Seller.java
The Seller class extends the User class. The seller class adds a list of stores to the instance variables of User.java. Seller also has a constructor for a new seller and a returning seller. Sellers can make as many stores as they want and each of these stores can have an unlimited number of products. Once stores and products are created, sellers are able to edit stores by adding, removing, and editing the products. A store cannot have two products with the same name. Sellers can change the name, price, description, and quantity available of each product. Sellers also have the option to view a dashboard which allows them to view the data of their stores. This data includes how many of each product have been sold and a list of customers and how many purchases they have made at the store. The stores can be listed based on revenue or the number of products they have. Additionally, sellers can export a store’s products to a file, import products for stores from a file, and view products that customers currently have in their carts. The class also has a method that gathers returning sellers data from the users.txt file.
Testing:
- Test cases
- Extensive manual testing: manual checking that each text file is properly updated when products are added, removed, or edited or when stores are created or deleted
- Breakpoints: debugging tools used to make sure that stores and products are properly created when reading from the text files
### Customer.java
The Customer.java class extends User.java. It adds two more instance variables which are the shopping cart and the purchase history. Like User, it has two constructors, one for returning customers and one for new ones. Once logging in, customers are given a menu that allows them to view a marketplace, view their cart, look at their purchase history, view a dashboard, export their purchase history to a file, delete their account, and quit the application. In the marketplace, customers can choose to sort products based on prices, quantities available, or nothing. After this, they are able to search for and add products to their cart. Customers can use option 2, view cart, to buy or remove items from their cart. Customers are then able to view their purchase history. The view dashboard option allows customers to look at a list of all stores on the marketplace. They can sort this list based on how many products the store has sold in total or by how many products stores have sold to this particular customer. The class also has a method that gathers returning customers data from the users.txt file.
Testing:
- Test cases
- Extensive manual testing: manual checking that the shopping cart and purchase history are properly updated in the "users.txt" file and manually checking that customer objects are properly recreated based on our files
- Breakpoints: allowed us to make sure that the quantity and amount sold of products is properly updated when the customer purchased products
### Store.java
The Store.java class has instance variables owner, name, a list of products, revenue, a list of customers, and a list of sales to each customer. The store class has methods that create a store object based on the info in the stores.txt file. There is also a method that returns an ArrayList of all of the stores found in the text file. Finally, it has methods to check if a new product already exists within a store, to get the total sales of a store, and to get the sales to each customer.
Testing:
- Test cases
- Extensive manual testing: running through the program and checking that the class is functional such as checking that it properly prevents products of the same name being manually created within the same store
- Breakpoints: allows for line by line checking that store products are properly recreated when read from "stores.txt"
### Product.java
Products.java allows product objects to be created. Each product has a name, a store it is sold at, a description, a price, a quantity available, and a number representing how many have been sold. Similar to the user, customer, and seller classes there are two constructors in Product.java. This class contains methods that retrieve data from a text file and recreate a product based on that data. It also has an equals method to compare products to one another.
Testing:
- Test cases
- Extensive manual testing: closing and rerunning the program to check that products are properly recreated from "products.txt" among other things
- Breakpoints: allowed us to check whether or not the equals() method was properly functional by checking the values of product objects line by line
### RunLocalTest.java
- IMPORTANT: The three text files "products.txt", "stores.txt", and "users.txt" must be empty when running this test class.
- Our test class that tests various functionalities of our project.
- Tests functionalities such as the customer and seller menus that use many methods.
