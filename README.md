# ePortfolio-Artifact-Three
This artifact is an expansion upon the first artifact, and demonstrates my use of databases, GUI, and security policies. Made in Java using Eclipse IDE. 

This is part of my ePortfolio, which can be found [here](https://ehickey96.github.io/)

Artifact one, which this is based off, can be found [here](https://github.com/ehickey96/ePortfolio-Artifact-One)

## Startup
This artifact contains a GUI. To use, simply download the JAR file, and run. 

Since the MySQL database is not distributed with it, I will only include the logins that are hardcoded. These only apply to the latest version of this project. The logins are as follows. Omit quotes. 

> Username: "snhuEhick" Password: "4255" , Admin

> Username: "HappyClouds" Password: "200" , Employee

> Username: "unknown person" Password: "0" , Intern

> Username: "hcUser" Password: "hcPWD" , Admin

Interns are limited to displaying the clients.

Employees can change a clients choice, add a client, or display clients

Admins can do everything an employee can do, but also can remove clients.

If utilizing the project files, you can swap between console and GUI by changing the GUICONF final String in the driver.java file equal to "CONFIRM". "CONFIRM" will launch as GUI, otherwise it will launch as console. 

![image](https://user-images.githubusercontent.com/79385657/137393699-b269c6a5-2c28-4675-b502-1f8513a51cbe.png)

Here is the GUI version and console Version.

![image](https://user-images.githubusercontent.com/79385657/137393935-7da2fd70-d8a1-42e5-a3d3-c2487b19f6e1.png)

![image](https://user-images.githubusercontent.com/79385657/137393986-b15d6d77-2125-4990-81f9-e9c276796943.png)

## Original artifact Three About
This artifact enhanced upon my first artifact ( found [here](https://github.com/ehickey96/ePortfolio-Artifact-One) ). It added a database incorporation using a connector class. This allowed for connections to be made to the MySQL database I created. This database was not distributed for security purposes, so it also can function without it. 

## Artifact Three Updated About
The updated version of the artifact also can be run with GUI, as well as adding proper user permision management, and allowing users with proper permissions to add and/or remove clients. 
