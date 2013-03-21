package es.regueiro.easyrepair.dao.user.hibernate;

import es.regueiro.easyrepair.api.user.finder.UserFinder;
import es.regueiro.easyrepair.model.user.User;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.ResourceClosedException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = UserFinder.class,
//path = "UserFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.user.DefaultUserFinder"}
)
public class UserHibernateFinder implements UserFinder {

    private Session session;

    @SuppressWarnings("unchecked")
    @Override
    public List<User> listAll() {
        try {
        session = Installer.createSession();
        Transaction transaction = session.beginTransaction();
        List<User> list = (List<User>) session.createCriteria(User.class).list();
        transaction.commit();
        if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return listAll();
        } catch (SessionException e) {
            return listAll();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findByName(String name) {
        try {
        session = Installer.createSession();
        Transaction transaction = session.beginTransaction();
        List<User> list = (List<User>) session.createCriteria(User.class)
                .add(Restrictions.isNotNull("name"))
                .add(Restrictions.like("name", "%" + name + "%"))
                .list();
        transaction.commit();
        if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByName(name);
        } catch (SessionException e) {
            return findByName(name);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findByRole(String role) {
        try {
        session = Installer.createSession();
        Transaction transaction = session.beginTransaction();
        List<User> list = (List<User>) session.createCriteria(User.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .createAlias("role", "r")
                .add(Restrictions.like("r.name", "%" + role + "%"))
                .list();
        transaction.commit();
        if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByRole(role);
        } catch (SessionException e) {
            return findByRole(role);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public User getUser(String name) {
        try {
        session = Installer.createSession();
        Transaction transaction = session.beginTransaction();
        User user = (User) session.createCriteria(User.class)
                .add(Restrictions.isNotNull("name"))
                .add(Restrictions.eq("name", name))
                .uniqueResult();
        transaction.commit();
        if (session.isOpen()) {
                session.close();
            }
            return user;
        } catch (ResourceClosedException e) {
            return getUser(name);
        } catch (SessionException e) {
            return getUser(name);
        } catch (ExceptionInInitializerError e) {
            return null;
        } 
    }

    @SuppressWarnings("unchecked")
    @Override
    public User getUser(Long id) {
        try {
        session = Installer.createSession();
        Transaction transaction = session.beginTransaction();
        User user = (User) session.createCriteria(User.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
        transaction.commit();
        if (session.isOpen()) {
                session.close();
            }
            return user;
        } catch (ResourceClosedException e) {
            return getUser(id);
        } catch (SessionException e) {
            return getUser(id);
        }
    }
}
