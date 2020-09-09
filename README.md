# CS591-GradingSystem

This project was done as part of CS591 P1 Object Oriented Programming in Java course at Boston University Graduate School of Arts and Sciences. 

### Project Goals 
The goal of the project is to create a Grading System simulator that would allow instructors to enter grades for classes they teach and also
maintain the grades in a persistent database that can be accessed later. 

### Project Scope and Functionality
    1. Ability to add and view courses
    2. Ability to add categories and items within categories for courses (and modify)
    3. Add weights to categories and items separately and modify
    4. Add students (import from csv)
    5. Data persistence in a local database (MySQL)
    6. Course statistics
    7. Final Report generation for class with assigned letter grade
    8. Ability to apply grade curves. 
    
### How To Run The Project

#### Import the Database
Create a new db named "grading_db". Import the "grading_db.sql" dump file into either MySQLWorkbench or XAMPP. Make sure the localhost port 
and ip matches with the program. If required, these can be changed in src/db/SQLHelper.java 

#### Importing the Project
Import the project into Eclipse or a suitable IDE for ease of access. Click run on Main.java 

##### Team Members 
Aneesh Raman, Boqi Chen, Jake Lee, Heze Yin
