CS18000 - Project 5
Group 2 - Aaryamik Gupta, Paige Lorenz, Dalton Babbs, Mahika Parakh, Matthew Wind

## Test 1 - Seller Account Creation
- Run Server.java
- Run Client.java
- Expected Result: [Marketplace Login Interface Opens]
- Click Button: Create New Account
- For textbox: Enter Email, write testSellerEmail
- For textbox: Enter Name, write testSellerName
- For textbox: Enter Password, write testSellerPassword
- For dropdown: 1. What is your mother's maiden name
- For textbox: Enter Answer, write: Green
- Click Button: Create Seller Account
- Within IDE: open users.txt
- verify that users.txt contains: seller,testSellerEmail,testSellerName,testSellerPassword,0,green,none
- Click Button: Logout
### Test Passed

## Test 2 - Customer Account Creation
- Run Server.java
- Run Client.java
- Expected Result: [Marketplace Login Interface Opens]
- Click Button: Create New Account
- For textbox: Enter Email, write testCustomerEmail
- For textbox: Enter Name, write testCustomerName
- For textbox: Enter Password, write testCustomerPassword
- For dropdown: 2. What was the name of your first pet?
- For textbox: Enter Answer, write: Spot
- Click Button: Create Customer Account
- Within IDE: open users.txt
- verify that users.txt contains: customer,testCustomerEmail,testCustomerName,testCustomerPassword,1,rubbish,none,none
- Click Button: Logout
### Test Passed

## Test 3 - Create Store and Products
- Run Server.java
- Run Client.java
- Expected Result: [Marketplace Login Interface Opens]
- For textbox: Enter Email, write testSellerEmail
- For textbox: Enter Password, write testSellerPassword
- Click Button: Login Seller
- For JOptionPane: error, click: OK
- Click Button: Add Store
- For JOptionPane: Create Store, write testStore1, click: OK
- For JOptionPane: Success, click OK
- Click Button: Add Store
- For JOptionPane: Create Store, write testStore2, click: OK
- For JOptionPane: Success, click OK
- Click Button: Select Store
- Click Button: Add Product
- For textbox: Enter Product Name, write testItem1
- For textbox: Enter Product Description, write description
- For textbox: Enter Product Price, write 5
- For textbox: Enter Product Quantity, write 20
- Click Button: Create Product
- For JOptionPane: success, click OK
- For textbox: Enter Product Name, write testItem2
- For textbox: Enter Product Description, write description
- For textbox: Enter Product Price, write 10
- For textbox: Enter Product Quantity, write 15
- Click Button: Create Product
- For JOptionPane: success, click OK
- Click Button: Back
- Click Dropdown
- Expected Result: [verify that both testItem1 and testItem2 appear in the dropdown]
- Select Dropdown: testItem1
- Click Button: Select Product
- For textbox: Enter New Product Name, write testItem1
- For textbox: Enter New Product Description, write description
- For textbox: Enter New Product Price, write 5
- For textbox: Enter New Product Quantity, write 30
- Click Button: Edit Product
- Click Button: Back
- Click Button: Logout
- Within IDE: open users.txt
- verify that users.txt contains: seller,testSellerEmail,testSellerName,testSellerPassword,0,green,testStore1
- Within IDE: open stores.txt
- verify that stores.txt contains: testStore1,testSellerEmail,testItem1~testItem2,0.0,none,none
- Within IDE: open products.txt
- verify that products.txt contains: testItem1,testStore1,description,20,5.0,0
- verify that products.txt contains:testItem2,testStore1,description,15,10.0,0
### Test Passed

## Test 4 - Adding Products to Cart, Viewing Products in Carts, and Viewing Seller Dashboard
- Run Server.java
- Run Client.java
- Expected Result: [Marketplace Login Interface Opens]
- For textbox: Enter Email, write testCustomerEmail
- For textbox: Enter Name, write testCustomerName
- Click Button: Login Customer
- Click Button: Select Product
- Click Button: Add to Cart
- For JOptionPane: Success, click OK
- Click Button: View Cart
- Click Button: Buy All
- For JOptionPane: Success, click OK
- Click Button: View Purchase History
- Expected Result: [verify that testItem1 shows up in purchase history]
- Click Button: Back
- Select dropdown option: testItem2
- Click Button: Select Product
- Click Button: Add to Cart
- For JOptionPane: Success, click OK
- Click Button: View Cart
- Expected Result: [verify that testItem2 shows up in cart]
- Click Button: Back
- Click Button: Logout
- For textbox: Enter Email, write testSellerEmail
- For textbox: Enter Password, write testSellerPassword
- Click Button: View Products in carts
- Expected Result: [verify that testItem2 appears]
- Click Button: Back
- Click Button: View Dashboard
- Click Button: Sort by Revenue
- Expected Result: [verify that testStore1 appears before testStore2]
- Click Button: Logout
### Test Passed

## Test 5 - Exporting Purchase History
- Run Server.java
- Run Client.java
- Expected Result: [Marketplace Login Interface Opens]
- For textbox: Enter Email, write testCustomerEmail
- For textbox: Enter Name, write testCustomerName
- Click Button: Login Customer
- Click Button: Export Purchase History
- For JOptionPane: Export Purchase History, click OK
- Click Button: Logout
- Within IDE, open .txt file titled: testCustomer
- Expected Result: [verify that file contains: Product Name: testItem1 Store: testStore1]
### Test Passed
