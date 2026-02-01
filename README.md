

 Project Overview

The Dispatch Management System is a Java desktop application developed using pure Object-Oriented Programming (OOP) principles. It manages the transportation of products from a Seller to a Destination (Store/Receiver) using a Transporter (Driver). The system is supervised by a Managing Company (Admin / PLC) that monitors dispatch activities and calculates transporter payments based on delivery time.

This project is designed as a simple, clean, and academic system, without using frameworks or build tools, and is suitable for university-level software engineering and OOP courses.

 Project Objectives

 Automate the dispatch and delivery process
 Track delivery confirmation at each stage
 Calculate transporter payment automatically
 Demonstrate proper OOP and SOLID principles
 Use JavaFX for GUI and JDBC for database connectivity


Technologies Used

 Java (Pure OOP)
 JavaFX (Graphical User Interface)
 MySQL (Relational Database)
 JDBC (Database Access)
 IDE: IntelliJ IDEA / NetBeans / Eclipse

 No Spring
 No Hibernate
 No Maven or Gradle


 User Roles

The system supports four user roles:

 Admin (Managing Company) – monitors dispatches and calculates payment
 Seller – creates dispatch records and hands over products
 Transporter (Driver) – confirms product receipt
 Destination / Store – confirms product arrival

Each role has its own permissions and JavaFX screen.


 System Workflow

1. The Seller creates a dispatch record and enters product weight (in tons).
2. The Transporter confirms receiving the product.
3. The Destination confirms product arrival.
4. The Admin monitors the process and the system automatically calculates payment.


 Payment Calculation Rules

 Payment is calculated per ton
 Agreed delivery time: 3 days
 Delivery within or equal to 3 days → 4000 ETB per ton
 Delivery greater than 3 days → 3000 ETB per ton

Delivery time is calculated using:

arrival_date - dispatch_date



 Features

 JavaFX-based graphical user interface
 Role-based login system
 Create, Read, Update, Delete (CRUD) operations
 Automatic payment calculation
 Input validation and error handling
 MySQL database persistence
 Clean separation of UI, service, and DAO layers


 Object-Oriented Design

This project demonstrates the following OOP principles:

 Encapsulation – private fields with getters and setters
 Inheritance – User base class extended by Admin, Seller, Transporter, and Destination
 Abstraction – service interfaces for business logic
 Polymorphism – interface-based behavior and method overriding




 SOLID Principles Applied

 Single Responsibility Principle (SRP)
 Open/Closed Principle (OCP)
 Liskov Substitution Principle (LSP)
 Interface Segregation Principle (ISP)
 Dependency Inversion Principle (DIP)

All principles are applied simply and practically, without overengineering.


 Project Structure

src/
 ├── model/       # Business objects
 ├── dao/         # JDBC database access
 ├── service/     # Business logic interfaces and implementations
 └── ui/          # JavaFX user interfaces



 Database Design

The system uses a MySQL database with the following tables:

 users
 dispatch
 payment

Each table uses primary keys, foreign keys, and date fields to track dispatch lifecycle and payments.


 How to Run the Project

1. Clone the repository
2. Import the project into your Java IDE.
3. Set up the MySQL database and update database credentials in DBConnection.java.
4. Run the application starting from the JavaFX main class.

UML & Documentation
 UML Class Diagram
 Service and DAO design
 Clear separation of concerns




  
Academic Note

This project was developed for educational purposes to demonstrate:

 Pure Object-Oriented Programming
 JavaFX GUI development
 JDBC database integration
 Clean and maintainable code structure


Author

Biruk Zewdu-------------------- UGR/9623/17
Obsera Ayele------------------- UGR/8067/17
Ribka G.medhin---------------  UGR/8057/17
Yeabsira Ayele-----------------  UGR/6645/17
Yasir Hamza--------------------  UGR/4999/17
