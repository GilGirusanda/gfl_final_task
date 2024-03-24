"# gfl_final_task" 
## How to launch
### Assign values for environment variables:
1. DB_PASSWORD 
2. DB_USERNAME
3. PGSQL_HOST
### Create postgresql database `icpo_db_2` or go to application.yml and change the name you want.
### Mandatory! Execute `CREATE EXTENSION IF NOT EXISTS tablefunc;` command in your database.
<br>
Use intellij idea or<br>
Execute `mvnw clean install` and then `mvnw spring-boot:run` commands in console to run the app.

## Using
- For filtering there is no need to enter full string (for example, just enter 'r' if you don't want to enter full string 'Russo')
- All fields are changable. Some of them are just set with default values, you can change later, like 'Unknown' for Firstname, or 'Few details' for Last Case
- Not all of the fields have to be entered. Some of them can be empty, except for Nickname and Criminal Profession
- In Criminal Details data, Birth Date field has to be of pattern '1990-11-22' or '1990-4-1', that is 'YYYY-MM-DD'
