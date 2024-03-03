package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.SQLException;

// реализуйте алгоритм здесь
public class Main {
    public static void main(String[] args) throws SQLException {
        UserDaoHibernateImpl userDao = new UserDaoHibernateImpl();
        UserService userService = new UserServiceImpl(userDao);

        userService.createUsersTable();
        System.out.println();

        userService.saveUser("Ахуеть", "как это было не просто", (byte) 127);
        userService.saveUser("Петр", "Петров", (byte) 20);
        userService.saveUser("Владимир", "Владимиров", (byte) 35);
        userService.saveUser("Александр", "Александров", (byte) 88);
        System.out.println();

        userService.getAllUsers();
        System.out.println();

        userService.cleanUsersTable();
        System.out.println();

        userService.dropUsersTable();
    }

}

