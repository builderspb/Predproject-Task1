package jm.task.core.jdbc.service;

import jm.task.core.jdbc.model.User;

import java.sql.SQLException;
import java.util.List;

import jm.task.core.jdbc.dao.UserDao;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }



    public void createUsersTable() throws SQLException {
        userDao.createUsersTable();
        System.out.println("Таблица Users создана");
    }

    public void dropUsersTable() {
        userDao.dropUsersTable();
        System.out.println("Таблица Users удалена");
    }

    public void saveUser(String name, String lastName, byte age) {
        if (name == null || lastName == null || name.isEmpty() || lastName.isEmpty()) {
            System.out.println("Имя и фамилия, обязательные поля");
            return;
        }
        userDao.saveUser(name, lastName, age);
        System.out.println("User с именем " + name + " добавлен в базу данных");
    }

    public void removeUserById(long id) {
        userDao.removeUserById(id);
        System.out.println("Пользователь с id № " + id + " удален из таблицы Users");
    }

    public List<User> getAllUsers() {
        List<User> userList = userDao.getAllUsers();
        if (userList.isEmpty()) {
            System.out.println("Список пользователей пуст");
        }else {
            for (User user : userList) {
                System.out.println(user);
            }
        }
        return userList;
    }

    public void cleanUsersTable() throws SQLException {
        userDao.cleanUsersTable();
    }

}