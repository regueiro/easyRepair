package es.regueiro.easyrepair.dao.user.hibernate;

import es.regueiro.easyrepair.api.user.finder.RoleFinder;
import es.regueiro.easyrepair.model.user.Role;
import java.util.List;
import org.hibernate.ResourceClosedException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = RoleFinder.class,
//path = "RoleFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.user.DefaultRoleFinder"}
)
public class RoleHibernateFinder implements RoleFinder {

    private Session session;

    @SuppressWarnings("unchecked")
    @Override
    public List<Role> listAll() {
        try {
        session = Installer.createSession();
        Transaction transaction = session.beginTransaction();
        List<Role> list = (List<Role>) session.createCriteria(Role.class).list();
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
    public List<Role> findByName(String name) {
        try {
        session = Installer.createSession();
        Transaction transaction = session.beginTransaction();
        List<Role> list = (List<Role>) session.createCriteria(Role.class)
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
    public List<Role> findByDescription(String description) {
        try {
        session = Installer.createSession();
        Transaction transaction = session.beginTransaction();
        List<Role> list = (List<Role>) session.createCriteria(Role.class)
                .add(Restrictions.isNotNull("description"))
                .add(Restrictions.like("description", "%" + description + "%"))
                .list();
        transaction.commit();
        if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByDescription(description);
        } catch (SessionException e) {
            return findByDescription(description);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Role getRole(Long id) {
        try {
        session = Installer.createSession();
        Transaction transaction = session.beginTransaction();
        Role role = (Role) session.createCriteria(Role.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
        transaction.commit();
        if (session.isOpen()) {
                session.close();
            }
            return role;
        } catch (ResourceClosedException e) {
            return getRole(id);
        } catch (SessionException e) {
            return getRole(id);
        }
    }
}
