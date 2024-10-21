package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.exception.OperationException;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Property;

import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final Logger logger = Logger.getLogger(UserDaoHibernateImpl.class.getName());
    private SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        //sessionFactory = Util.getHibConnection();
        sessionFactory = new Configuration().addAnnotatedClass(User.class).buildSessionFactory();
    }

    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("CREATE TABLE IF NOT EXISTS public.users (id BIGSERIAL PRIMARY KEY, name VARCHAR(50) NOT NULL, lastname VARCHAR(50) NOT NULL, age SMALLINT NOT NULL)", User.class).executeUpdate();
            session.getTransaction().commit();
            logger.log(Level.INFO, "UserDaoHibernateImpl: create table!");
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS public.users", User.class).executeUpdate();
            session.getTransaction().commit();
            logger.log(Level.INFO, "UserDaoHibernateImpl: drop table");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            session.getTransaction().commit();
            logger.log(Level.INFO, "UserDaoHibernateImpl: " + name + " add to database");
        } catch (Exception ex) {
            throw new OperationException("Save user exception: " + ex);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            session.getTransaction().commit();
            logger.log(Level.INFO, "UserDaoHibernateImpl: remove user with id=" + id);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<User> users = session.createQuery("from User").list();
            session.getTransaction().commit();
            for (var item : users) {
                logger.log(Level.INFO, "UserDaoHibernateImpl: find user " + item.toString());
            }
            return users;
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery("delete User").executeUpdate();
            session.getTransaction().commit();
            logger.log(Level.INFO, "UserDaoHibernateImpl: clean table");
        }
    }
}
