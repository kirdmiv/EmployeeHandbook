# Employee Handbook

## Setup
1. [Download PostgreSQL.](https://www.postgresql.org/download/) Create a database with the `empHandbookdb` name.
2. [Download the jar file.](https://github.com/kirdmiv/EmployeeHandbook/releases/download/v1.0.0-beta.2/EmployeeHandbook.jar)
3. [Register](https://myaccount.google.com/security) the app in Gmail (to provide notifications). 
4. To run the application, write `java -jar EmployeeHandbook.jar` in the terminal. Furthermore, one should specify the following flags:
      * [Mandatory] `--spring.datasource.username=<Username> --spring.datasource.password=<Password>` to provide access to database.
      * [Optional] `--spring.mail.username=<Email address> --spring.mail.password=<Password>` to set up email notifications.
      * [Optional] `--databaseInitializationFile=<Path to file>` to import initial data (like [init.txt](./init.txt)).
5. Example: `java -jar EmployeeHandbook.jar --databaseInitializationFile=init.txt --spring.mail.password=ggg --spring.mail.username=kirdmiv@gmail.com --spring.datasource.username=employee_handbook --spring.datasource.password=123`

### About

### Init file structure
One can specify multiple files. The structure of a single file is following:
```
.
├── organization/
│   ├── employee (Head of organization)
│   ├── department/
│   │   ├── employee (Head of department)
│   │   ├── team /
│   │   │   ├── employee (Head of team)
│   │   │   ├── employee (Team member #1)
│   │   │   ├── employee (Team member #2)
│   │   │   └── etc...
│   │   └── team #2/
│   │       └── ...
│   └── department #2/
│       └── ...
├── organization #2/
│   └── ...
└── ...
```
Every string specifies its entity. The delimiter symbol is `$` (not space). It is possible to escape the dollar sign `\$`.

Samples:
* Organization: `organization$title$description$headQuarters`
* Department: `department$title$description`
* Team: `team$title$description`
* Employee: `employee$name$surname$email$birthday$role`

It is not required to set the `USER` role explicitly.

### Documentation
If the server is running, one can access auto-generated docs at `http://localhost:8080/swagger-ui/index.html`.

### Tests
[Here](./src/test/java/com/ivanov/kirill/EmployeeHandbook/EmployeeHandbookApplicationTests.java)

####
* Error handling
* Logs
* More tests
* More admin privileges
