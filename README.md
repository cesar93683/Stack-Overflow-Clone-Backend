# Install MySQL Community Server 8.0.30

Download the smaller file (mysql-installer-web-community-8.0.30.0.msi)  
https://dev.mysql.com/downloads/windows/installer/8.0.html  
During install, select "Developer Default"  
Download the requirements MySQL needs. (I needed to download Python 3.9.10. I did not download Visual Studio 2019)  
For the password, set it as "admin"

Run sql script in order located in test/resources/sql

In IntelliJ, go to Project Structure and set JDK to JDK 18.  
Create run configuration "clean install" and run it.  
Run StackOverflowCloneApplication.  
Run PopulateDatabase.  
To send requests from swagger, go to http://localhost:8080/swagger-ui/index.html

Not required, but recommend.  
Go to Settings -> Version Control -> Commit -> Uncheck "Use non-modal commit interface".  
Go to Settings -> Editor -> Inlay Hints -> Code Vision -> Uncheck "Code Author", "Usages", and "Inheritors".  
Inside a file, press Ctrl + Alt + Shift + L -> Check "Optimize Imports"