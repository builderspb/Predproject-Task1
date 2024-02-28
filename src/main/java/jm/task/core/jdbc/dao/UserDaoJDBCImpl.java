package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {
    }

    UserDaoJDBCImpl userDaoJDBC = new UserDaoJDBCImpl();

    /**
     * Создание таблицы:
     * 1. Переменная для SQL запроса, сам запрос с использованием IF NOT EXISTS, для того, чтобы не привело к исключению если такая таблица уже существует.
     * 2. В блоке try  создается объект Connection с помощью метода open() который находится в моем классе ConnectionManager.
     * Этот ресурс будет автоматически закрыт после завершения блока try.
     * 3. Создается объект Statement с помощью метода createStatement() у объекта connection. Этот ресурс также будет автоматически
     * закрыт.
     * 4. Выполнение SQL запроса.
     * 5. Обработка возможного исключения. (ошибка в SQL запросе, проблемы с соединением)
     */
    public void createUsersTable() {
        String create = """
                CREATE TABLE IF NOT EXISTS users (
                 id SERIAL PRIMARY KEY,
                                name VARCHAR(50) NOT NULL, 
                                lastName VARCHAR(50) NOT NULL,
                                age INT
                            );
                """;
        try (Connection connection = ConnectionManager.open();
             var statement = connection.createStatement()) {
            statement.execute(create);
            System.out.println("Таблица users успешно создана");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Удаление таблицы:
     * 1. Переменная для SQL запроса, сам запрос с использованием IF NOT EXISTS, для того, чтобы не привело к исключению если такой таблицы не существует.
     * 2. В блоке try  создается объект Connection с помощью метода open() который находится в моем классе ConnectionManager.
     * Этот ресурс будет автоматически закрыт после завершения блока try.
     * 3. Создается объект Statement с помощью метода createStatement() у объекта connection. Этот ресурс также будет автоматически
     * закрыт.
     * 4. Выполнение SQL запроса.
     * 5. Обработка возможного исключения. (ошибка в SQL запросе, проблемы с соединением)
     */
    public void dropUsersTable() {
        String drop = """
                DROP TABLE IF EXISTS users;
                """;

        try (Connection connection = ConnectionManager.open();
             var statement = connection.createStatement()) {
            statement.execute(drop);
            System.out.println("Таблица users успешно удалена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Добавление строк в таблицу:
     * 1. В SQL запросе поля name и lastName обязательны для заполнения. Поэтому посчитал логичным сделать проверку переданных в метод аргументов
     * (name и lastName), на null и пустое значение. Если будет null или пустота, метод прекращает свою работу, тем самым не используя дальше ресурсы.
     * 2. Переменная для SQL запроса.
     * 3. В блоке try  создается объект Connection с помощью метода open() который находится в моем классе ConnectionManager.
     * Этот ресурс будет автоматически закрыт после завершения блока try.
     * 4. Создается объект PreparedStatement. На объекте connection вызывается метод prepareStatement() принимающий в параметры SQL запрос.
     * В запросе поля отмечены вопросительными знаками, это означает, что их можно заменить конкретными аргументами, которые будут переданы в метод.
     * Этот ресурс также будет автоматически закрыт.
     * 5. Установка значений для SQL запроса.
     * 6. Выполнение SQL запроса.
     * 7. Вывод в консоль каждого добавленного User-a.
     * 8. Обработка возможного исключения. (ошибка в SQL запросе, проблемы с соединением)
     */
    public void saveUser(String name, String lastName, byte age) {

        if (name == null || lastName == null || name.isEmpty() || lastName.isEmpty()) {
            System.out.println("Имя и фамилия, обязательные поля");
            return;
        }
        String insert = """
                INSERT INTO users (name, lastname, age)
                VALUES (?,?,?);
                """;

        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(insert)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
            System.out.println("User с именем " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Удаление строки из таблицы по id:
     * 1. Переменная для SQL запроса.
     * 2. В блоке try  создается объект Connection с помощью метода open() который находится в моем классе ConnectionManager.
     * Этот ресурс будет автоматически закрыт после завершения блока try.
     * 3. Создается объект PreparedStatement. На объекте connection вызывается метод prepareStatement() принимающий в параметры SQL запрос.
     * В запросе поле отмечено вопросительным знаком, это означает, что его можно заменить конкретным аргументом, который будет передан в метод.
     * Этот ресурс также будет автоматически закрыт.
     * 4. Установка значений для SQL запроса.
     * 5. Выполнение SQL запроса.
     * 6. Вывод в консоль удаленного User-a вместе с его айдишником.
     * 7. Обработка возможного исключения. (ошибка в SQL запросе, проблемы с соединением)
     */
    public void removeUserById(long id) {
        String delete = """
                   DELETE FROM users
                   WHERE id = (?);
                """;

        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(delete)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            System.out.println("User с id № " + id + " удален из таблицы");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Получение всех записей из таблицы:
     * 1. Переменная для SQL запроса.
     * 2. Создание ArrayList<User>
     * 3. В блоке try  создается объект Connection с помощью метода open() который находится в моем классе ConnectionManager.
     * Этот ресурс будет автоматически закрыт после завершения блока try.
     * 4. Создается объект Statement с помощью метода createStatement() у объекта connection.
     * 5. Создается объект класса ResultSet, на объекте statement вызывыается метод executeQuery в парметры которого, передается SQL запрос. Метод executeQuery возвращает результат запроса.
     * Этот ресурс также будет автоматически закрыт.
     * 6. Выполнение SQL запроса.
     * 7. В цикле while, с помощью метода next(), смотрим есть ли следующий элемент в resultSet. После присваиваем полученные данные переменным, потом создается объект User с данными из переменных.
     * Далее User добавляется в лист. И так по кругу пока есть записи в таблице. У меня в методе добавления записей, есть проверка на наличие обязательных полей, значит в этом методе,
     * name и lastName пустыми быть не могут. А в случае не указаного аозраста, SQL запрос вернет "0", и цикл while это скушает.
     * 8. Обработка возможного исключения. (ошибка в SQL запросе, проблемы с соединением)
     * 9. Проверка ArrayList<User> на наличие строк. Если лист пуст, выводится сообщение - "Список пользователей пуст". Если строки есть, итерируемся по листу в цикле for, и выводим в консоль.
     * (Переопределенный toString выводит не красиво, какие-то скобки, запятые, избавиться не смог, поэтому применил цикл for.
     * 10. Ну и return листа, если будет пустым, значит вернет пустой.
     */
    public List<User> getAllUsers() {
        String select = """
                SELECT * FROM users;
                """;

        List<User> userList = new ArrayList<>();

        try (Connection connection = ConnectionManager.open();
             var statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(select);

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastname");
                byte age = resultSet.getByte("age");
                User user = new User(name, lastName, age);
                userList.add(user);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (userList.isEmpty()) {
            System.out.println("Список пользователей пуст");
        } else {
            for (User user : userList)
                System.out.println(user);
        }
        return userList;
    }


    /**
     * Очистка таблицы:
     * 1. Вызов метода удаления таблицы, а если таблица удалена, значит и записей в ней нет.
     * 2. Вызов метода создания таблицы, т.к. метод не предпологает удаление самой таблицы.
     * 3. Вывод в консоль сообщения "Все данные из таблицы users удалены"
     * 4. Пробрасываем исключение, говоря о том, что вызванный код может выбросить исключение, и в этом случае перекладываем отвественность на вызванный код.
     * А в наших методах по удалению и созданию таблицы исключения имеют логику обработки.
     *
     * Не понял зачем так, отдельный метод по очистке таблицы от записей, должен использовать меньше ресурсов, в сравнении с вызовом двух методов,
     * которые будут создавать и закрывать подключения два раза. Плюс не понимаю как поступить с принтами которые в этих методах выводятся в консоль,
     * получается они несут другую логику, нежели должен предоставлять метод по очистке таблицы от записей.
     */
    public void cleanUsersTable() throws SQLException {
        userDaoJDBC.dropUsersTable();
        userDaoJDBC.createUsersTable();
//        System.out.println("Все данные из таблицы users удалены");
    }


}
