# Employee Handbook
[Задание](https://docs.google.com/document/d/1_xDM82GGdrmKR16Tyqx9UgHsuNk4YT7e/edit)

## Set-up
1. [Скачать PostgreSQL](https://www.postgresql.org/download/). Создать базу данных `empHandbookdb`.
2. Скачать release jar.
3. Зарегистрировать приложения для отправки сообщений через gmail. https://myaccount.google.com/security
4. `java -jar EmployeeHandbook.jar --databaseInitializationFile=<Path to init file> --spring.mail.password=<password> --spring.mail.username=<username>`
5. Без инициализации: `java -jar EmployeeHandbook.jar --spring.mail.password=<password> --spring.mail.username=<username>`

### Файл инициализации.
TODO

### Документация
[JavaDoc](TODO)

После поднятия сервера Swagger: http://localhost:8080/swagger-ui/index.html

#### TODO
* Добавить метод в TeamController
* Исправить проблемы с доступом
* Изменить чтение файла инициализации
* Unit тесты
* Документация, README.md и jar