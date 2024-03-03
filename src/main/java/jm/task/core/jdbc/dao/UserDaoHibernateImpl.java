package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

import static jm.task.core.jdbc.util.ConnectionManager.getCurrentSession;

public class UserDaoHibernateImpl implements UserDao {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Users (" +
            "id SERIAL PRIMARY KEY," +
            "name VARCHAR(50)," +
            "lastname VARCHAR(50)," +
            "age SMALLINT" +
            ")";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS Users";

    private static final String SAVE_USER = "INSERT INTO Users (name, lastName, age) VALUES (:name, :lastName, :age)";

    private static final String GET_ALL_USERS = "SELECT * From Users";

    public UserDaoHibernateImpl() {
    }



    public static void handleException(Exception e, Session session) {
        System.err.println("Произошла ошибка: " + e.getMessage());
        e.printStackTrace();

        if (session != null && session.getTransaction().isActive()) {
            session.getTransaction().rollback();
        }

        if (session != null && session.isOpen()) {
            session.close();
        }
    }


    @Override
    public void createUsersTable() {
        Session session = getCurrentSession();
        try {
            session.beginTransaction();

            session.createSQLQuery(CREATE_TABLE).executeUpdate();
            session.getTransaction().commit();

        } catch (Exception e) {
            handleException(e, session);
        }
    }



    @Override
    public void dropUsersTable() {
        Session session = getCurrentSession();
        try {
            session.beginTransaction();

            session.createSQLQuery(DROP_TABLE).executeUpdate();
            session.getTransaction().commit();

        } catch (Exception e) {
            handleException(e, session);
        }
    }


    // Не разобрался почему этот метод не работает без явного использования SQL запроса. А в HQL нет
    // операции добавления. Только с методом session.save()
    // т.е. вот так не работает
    // User user = new User(name, lastName, age);
    // session.save(user);
    // предпологал, что это как-то связанно с именем таблицы, но вроде все проверил. дебаг показывает
    // как происходит добавление. Месседж выводится об успешном добавлении
    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = getCurrentSession();
        try {
            session.beginTransaction();

            Query query = session.createSQLQuery(SAVE_USER);
            query.setParameter("name", name);
            query.setParameter("lastName", lastName);
            query.setParameter("age", age);
            query.executeUpdate();

            session.getTransaction().commit();

        } catch (Exception e) {
            handleException(e, session);
        }
    }


    @Override
    public void removeUserById(long id) {
        Session session = getCurrentSession();
        try {
            session.beginTransaction();

            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();

        } catch (Exception e) {
            handleException(e, session);
        }
    }


    @Override
    public List<User> getAllUsers() {
        Session session = getCurrentSession();
        List<User> userList = null;

        try {
            session.beginTransaction();
            Query query = session.createSQLQuery(GET_ALL_USERS).addEntity(User.class);
            userList = query.getResultList();

            session.getTransaction().commit();
        } catch (Exception e) {
            handleException(e, session);
        }
        return userList;
    }


    @Override
    public void cleanUsersTable() {
        dropUsersTable();
        createUsersTable();

    }
}
