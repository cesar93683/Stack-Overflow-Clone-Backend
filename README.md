# Install MySQL Community Server 8.0.30


Download PostgreSQL  
https://www.enterprisedb.com/downloads/postgres-postgresql-downloads  
Set password to `admin`  
Create database called `stack_clone`

Run sql script in order located in test/resources/sql

In IntelliJ, go to Project Structure and set JDK to JDK 18.  
Create run configuration `clean install` and run it.  
Run StackOverflowCloneApplication.  
Run PopulateDatabase.  
To send requests from swagger, go to http://localhost:8080/swagger-ui/index.html

Not required, but recommend.  
Go to Settings -> Version Control -> Commit -> Uncheck `Use non-modal commit interface`.  
Go to Settings -> Editor -> Inlay Hints -> Code Vision -> Uncheck `Code Author`, `Usages`, and `Inheritors`.  
Inside a file, press Ctrl + Alt + Shift + L -> Check `Optimize Imports`