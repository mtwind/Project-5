# Project-5
### How to compile and run our project
- Our project is a marketplace, which allows Sellers to create stores and products and for Customers to interact with them in various ways.
- Program must start with three text files that must exist at the start of the program: "products.txt", "stores.txt", and "users.txt". These three text files must all be empty the first time runnning the program. They serve as the foundation of storing data in our program.
- IMPORTANT: To run the program, first run Server.java, and then run an instance of Client.java for each user as needed.
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
### MainInterface.java
This class contains the GUI for our project that the user interacts with. Client.java creates new instances of this class for every user running the program. Users are first prompted to log in with an email and password or create a new seller or customer type account. After logging in, the user is directed to either the seller or customer menu depending on their account type.
### Client.java
This class must be run for each user while Server.java is running in order for the users to interact with the GUI. This class creates new instances of MainInterface.java for every user and connects sockets to the server socket.
### ClientHandler.java
This class implements the functionality that was created by other server-side classes. This class also contains a variety of methods that allow Customers to view the marketplace of products and sort it by prices or quantity. It contains methods for Sellers to see their store statistics and the ability for them to sort their stores by revenue, number of products, or no sort at all.
### Server.java
This class runs until manually stopped and must be running in order for users to connect. This class creates a server socket with port number 1 for the client to connect to. This class creates new instances of ClientHandler for data processing and starts new threads for each individual user.
### Driver.java ???
This class contains a variety of sorting methods.
### User.java
The User.java class is a basic class that is the parent class of both Customer.java and Seller.java. This class contains basic variables such as the user’s name, email, password, security question number and answer. There are 6 security questions for users to select from stored in this class. The class contains two different constructors, one is for new users and one is for returning users. It also contains a method to check if the user already has an account and two methods that allow a user to delete their account. It also contains functionality for removing stores from the stores.txt file. It can check passwords and determine if a user exists or not. Users can also edit all of their account information.
### Seller.java
The Seller class extends the User class. The seller class adds a list of stores to the instance variables of User.java. Seller also has a constructor for a new seller and a returning seller. Sellers can make as many stores as they want and each of these stores can have an unlimited number of products. Once stores and products are created, sellers are able to edit stores by adding, removing, and editing the products. A store cannot have two products with the same name. Sellers can change the name, price, description, and quantity available of each product. Sellers also have the option to view a dashboard which allows them to view the data of their stores. This data includes how many of each product have been sold and a list of customers and how many purchases they have made at the store. The stores can be listed based on revenue or the number of products they have. Additionally, sellers can export a store’s products to a file, import products for stores from a file, and view products that customers currently have in their carts. The class also has a method that gathers returning sellers data from the users.txt file.
### Customer.java
The Customer.java class extends User.java. It adds two more instance variables which are the shopping cart and the purchase history. Like User, it has two constructors, one for returning customers and one for new ones. Once logging in, customers are given a menu that allows them to view a marketplace, view their cart, look at their purchase history, view a dashboard, export their purchase history to a file, edit their account, and quit the application. In the marketplace, customers can choose to sort products based on prices or quantities available. After this, they are able to search for and add products to their cart. Customers can view cart to buy or remove items from their cart. Customers are also able to view their purchase history. The view dashboard option allows customers to look at a list of all stores on the marketplace. They can sort this list based on how many products the store has sold in total or by how many products stores have sold to this particular customer. The class also has a method that gathers returning customers data from the users.txt file.
### Store.java
The Store.java class has instance variables owner, name, a list of products, revenue, a list of customers, and a list of sales to each customer. The store class has methods that create a store object based on the info in the stores.txt file. There is also a method that returns an ArrayList of all of the stores found in the text file. Finally, it has methods to check if a new product already exists within a store, to get the total sales of a store, and to get the sales to each customer.
### Product.java
Products.java allows product objects to be created. Each product has a name, a store it is sold at, a description, a price, a quantity available, and a number representing how many have been sold. Similar to the user, customer, and seller classes there are two constructors in Product.java. This class contains methods that retrieve data from a text file and recreate a product based on that data. It also has an equals method to compare products to one another.
