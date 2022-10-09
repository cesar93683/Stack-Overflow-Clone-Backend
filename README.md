Download PostgreSQL  
https://www.enterprisedb.com/downloads/postgres-postgresql-downloads  
Set password to `admin`  
Create database called `stack_clone`

Run sql script in order located in test/resources/sql

In IntelliJ, go to Project Structure and set JDK to JDK 18.  
Create run configuration `clean install` and run it.  
Run StackOverflowCloneApplication.  
An error will occur.  
Go to `Run` -> `Edit Configurations` -> Add the following to `Environment variables`  
`DB_URL=jdbc:postgresql://localhost:5432/stack_clone; DB_USER_NAME=postgres; DB_USER_PASSWORD=admin`  
Run PopulateDatabase.  
To send requests from swagger, go to `http://localhost:8080/swagger-ui/index.html`

Not required, but recommend.  
Go to Settings -> Version Control -> Commit -> Uncheck `Use non-modal commit interface`.  
Go to Settings -> Editor -> Inlay Hints -> Code Vision -> Uncheck `Code Author`, `Usages`, and `Inheritors`.  
Inside a file, press Ctrl + Alt + Shift + L -> Check `Optimize Imports`


## Deploy on heroku 
Create app called `stack-clone-database-cesar`  
Go to resources and add the add-on `Heroku Postgres`  
Download heroku cli `https://devcenter.heroku.com/articles/heroku-cli`  
Add PATH environment variable `C:\Program Files\PostgreSQL\<VERSION>\bin`  
Open powershell in `controller/src/test/resources`  
Run `cat .\script.sql | heroku pg:psql --app stack-clone-database-cesar`    
Go to stack-clone-database-cesar -> Click `Heroku Postgres` -> Click `Settings` -> Click `View Credentials`  
Copy the `host`, `database`, `port`, `user`, and `password`.  These will be used in `stack-clone-backend-cesar`

Create app called `stack-clone-backend-cesar`
Link it to this GitHub repository  
Go to `Settings` -> Click `Reveal Config Vars`  
Add the following:
>`DB_URL`: `jdbc:postgresql://` + `host` from `stack-clone-database-cesar` + `:` + `port` from `stack-clone-database-cesar` + `/` + `database` from `stack-clone-database-cesar`  
>`DB_USER_NAME`: use user from `stack-clone-database-cesar`  
>`DB_USER_PASSWORD`: use password from `stack-clone-database-cesar`

Go to `Deploy` -> Click `Deploy Branch`  
Check if up `https://stack-clone-backend-cesar.herokuapp.com/swagger-ui/index.html`  
Update `API_URI` in `PopulateDatabase` to `https://stack-clone-backend-cesar.herokuapp.com/`    
Run `PopulateDatabase`.  