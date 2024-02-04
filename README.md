# Monitoring-Service

Сервис для подачи показаний счетчиков отопления, горячей и холодной воды

## Стэк: 
- Java 17
- Gradle
- PostgreSQL
- JDBC
- Lombok
- AssertJ
- Mockito
- Junit5
- Liquibase
- TestContainers

# Функциональность
- Регистрация/Авторизация пользователя
- Получение актуальных показаний счетчиков
- Подачф показаний
- Просмотр показаний за конкретный месяц
- Просмотр истории подачи показаний
- Контроль прав пользователя
- Аудит действий пользователя (авторизация, завершение работы, подача показаний, получение истории подачи показаний и тд)

# Запуск
1. Склонируйте репозиторий
2. Имеется docker-compose.yml файл для postgres. Он находится в src/main/resources
3. В папке src/main/resources имеется файл application.properties где находится конфигурация для работы приложения с бд.
4. Для создания схем и таблиц с данными требуется запустить DatabaseCreator.main или запустить там метод createAll()
5. Запустите метод main

# Использование
- В программе заранее создан аккаунт админа (username: Admin, password: admin)
- заранее создан 1 пользователь (username: User, password: user) с показаниями
- Приложение имееет консольное меню 
- после входа или авторизации выбирайте пункт ActionMenu для выполнения действий, характерных для типа пользователя (ADMIN/USER)





