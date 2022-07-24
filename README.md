# Employee Handbook
[Задание](https://docs.google.com/document/d/1_xDM82GGdrmKR16Tyqx9UgHsuNk4YT7e/edit)

## Set-up
1. [Скачать PostgreSQL](https://www.postgresql.org/download/). Создать базу данных `empHandbookdb`.
2. Скачать release jar.
3. Зарегистрировать приложения для отправки сообщений через gmail. https://myaccount.google.com/security
4. Запустить: `java -jar EmployeeHandbook.jar`
    * `--databaseInitializationFile=<Path to file>`. Опционально. Указать путь, из которого будут считанны данные для инициалтзации базы. Пример: [`init.txt`](./init.txt).
    * `--spring.mail.username=<Email address> --spring.mail.password=<Password>` конфигурируют отправку сообщений на email.
    * `--spring.datasource.username=<Username> --spring.datasource.password=<Password>` дают доступ к базе данных.
5. Пример: `java -jar EmployeeHandbook.jar --databaseInitializationFile=init.txt --spring.mail.password=ggg --spring.mail.username=kirdmiv@gmail.com --spring.datasource.username=employee_handbook --spring.datasource.password=123`

### О приложении
При создании сотрудника генерируется пароль и юзернэйм, которые отправляются ему на почту.
Можно добавлять/удалять/изменять все, если начальник или админ. Прсматривать могут все.

### Файл инициализации.
Можно задавать несколько файлов. Организация задается следующей структурой: 
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
Каждая сущность задается строчкой, разделителем в которой участвует `$`. Иначе доллар нужно экранировать: `\$`.

Примеры строк:
* Организация: `organization$title$description$headQuarters`
* Департамент: `department$title$description`
* Команда: `team$title$description`
* Сотрудник: `employee$name$surname$email$birthday$role`

Если роль `USER` можно не указывать.

### Документация
После поднятия сервера Swagger: http://localhost:8080/swagger-ui/index.html

### Тесты
Пока что [тестируются](./src/test/java/com/ivanov/kirill/EmployeeHandbook/EmployeeHandbookApplicationTests.java) только запросы для поиска. Причем для запуска тестов придется вводить логины и пароли. 

#### TODO
* Error handling
* Logs
* Тесты
* Больше возможностей админа: изменение ролей, регенерация паролей.