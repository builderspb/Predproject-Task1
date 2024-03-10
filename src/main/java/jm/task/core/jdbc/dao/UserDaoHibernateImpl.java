package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collections;
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





    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = getCurrentSession();
        try {
            session.beginTransaction();

            User user = new User(name, lastName, age);
            session.save(user);

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

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            criteria.from(User.class);

            userList = session.createQuery(criteria).getResultList();

            session.getTransaction().commit();
        } catch (Exception e) {
            handleException(e, session);
        }
        if (userList == null) {
            userList = Collections.emptyList();
        }
        return userList;
    }


    @Override
    public void cleanUsersTable() {
        dropUsersTable();
        createUsersTable();

    }
}
