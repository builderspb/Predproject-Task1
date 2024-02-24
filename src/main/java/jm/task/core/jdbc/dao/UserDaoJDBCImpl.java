package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.ConnectionManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {
    }

    // Дорогой, любимый наставник, брат, многоуважаемый Илья Викторович
    // почему при умышленно неправильном пароле в файле application.properties тест отрабатывает правильно?
    // ведь у нас прежде чем выполняется запрос на создание таблицы, происходит подключение к БД в блоке try
    // при этом, если выполнять соединение отдельно в методе main, прилетает эксепшен
    // разобрался, в тест не добавлена проверка на подключение к базе БД. Подключение к БД обрабатывается в
    // каждом методе класса UserDAO.
    // В тестах токое писать не имеет смысла???
    // АХАХ я затупил, и вместо создания таблицы, создал БД, а тест один хер проходил
    public void createUsersTable()  {
        String sql = """
                CREATE TABLE IF NOT EXISTS Users (
                 id SERIAL PRIMARY KEY,
                                name VARCHAR(50),
                                lastName VARCHAR(50),
                                age INT
                            );
                """;
        try (Connection connection = ConnectionManager.open();
            var statement = connection.createStatement()) {
         statement.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
//  Удаление таблицы User(ов) – не должно приводить к исключению, если таблицы не существует??????
    }

    public void saveUser(String name, String lastName, byte age) {

    }

    public void removeUserById(long id) {

    }

    public List<User> getAllUsers() {
        return null;
    }

    public void cleanUsersTable() {








    }
}
