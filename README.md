"# gfl_final_task" 
## How to launch
### Assign values for environment variables:
1. DB_PASSWORD 
2. DB_USERNAME
3. PGSQL_HOST
### Create postgresql database `icpo_db_2` or go to application.yml and change the name you want.
<br>
### Mandatory! Execute `CREATE EXTENSION IF NOT EXISTS tablefunc;` command in your database.
<br>
Use intellij idea or<br>
Execute `mvnw clean install` and then `mvnw spring-boot:run` commands in console to run the app.
