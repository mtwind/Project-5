CS18000 - Project 5
Group 2 - Aaryamik Gupta, Paige Lorenz, Dalton Babbs, Mahika Parakh, Matthew Wind

Test 1 - Seller Account Creation
Run Server.java
Run Client.java
[Marketplace Login Interface Opens]
Click Button: Create New Account
For textbox: Enter Email, write testSellerEmail
For textbox: Enter Name, write testSellerName
For textbox: Enter Password, write testSellerPassword
For dropdown: 1. What is your mother's maiden name
For textbox: Enter Answer, write: Green
Click Button: Create Seller Account
Within IDE: open users.txt
verify that users.txt contains: seller,testSellerEmail,testSellerName,testSellerPassword,0,green,none
Click Button: Logout

Test 2 - Customer Account Creation
Run Server.java
Run Client.java
[Marketplace Login Interface Opens]
Click Button: Create New Account
For textbox: Enter Email, write testCustomerEmail
For textbox: Enter Name, write testCustomerName
For textbox: Enter Password, write testCustomerPassword
For dropdown: 2. What was the name of your first pet?
For textbox: Enter Answer, write: Spot
Click Button: Create Customer Account
Within IDE: open users.txt
verify that users.txt contains: customer,testCustomerEmail,testCustomerName,testCustomerPassword,1,rubbish,none,none
Click Button: Logout

Test 3 - Create Store and Products
Run Server.java
Run Client.java
[Marketplace Login Interface Opens]
For textbox: Enter Email, write testSellerEmail
For textbox: Enter Password, write testSellerPassword
Click Button: Login Seller
For JOptionPane: error, click: OK
Click Button: Add Store
For JOptionPane: Create Store, write testStore1, click: OK
For JOptionPane: Success, click OK
Click Button: Select Store
Click Button: Add Product
For textbox: Enter Product Name, write testItem1
For textbox: Enter Product Description, write description
For textbox: Enter Product Price, write 5
For textbox: Enter Product Quantity, write 20
Click Button: Create Product
For JOptionPane: success, click OK
For textbox: Enter Product Name, write testItem2
For textbox: Enter Product Description, write description
For textbox: Enter Product Price, write 10
For textbox: Enter Product Quantity, write 15
Click Button: Create Product
For JOptionPane: success, click OK
Click Button: Back
Click Dropdown
[verify that both testItem1 and testItem2 appear in the dropdown]
Select Dropdown: testItem1
Click Button: Select Product
For textbox: Enter New Product Name, write testItem1
For textbox: Enter New Product Description, write description
For textbox: Enter New Product Price, write 5
For textbox: Enter New Product Quantity, write 30
Click Button: Edit Product
Click Button: Back
Click Button: Logout
Within IDE: open users.txt
verify that users.txt contains: seller,testSellerEmail,testSellerName,testSellerPassword,0,green,testStore1
Within IDE: open stores.txt
verify that stores.txt contains: testStore1,testSellerEmail,testItem1~testItem2,0.0,none,none
Within IDE: open products.txt
verify that products.txt contains: testItem1,testStore1,description,20,5.0,0
verify that products.txt contains:testItem2,testStore1,description,15,10.0,0














