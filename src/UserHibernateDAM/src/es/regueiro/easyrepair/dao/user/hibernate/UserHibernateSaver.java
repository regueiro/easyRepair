package es.regueiro.easyrepair.dao.user.hibernate;

import es.regueiro.easyrepair.api.user.saver.UserSaver;
import es.regueiro.easyrepair.model.user.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = UserSaver.class,
//path = "UserFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.user.DefaultUserSaver"}
)
public class UserHibernateSaver implements UserSaver {
    
    private User user;
    private Transaction t;
    private Session session;
    
    public UserHibernateSaver() {
    }
    
    @Override
    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public User getUser() {
        return user;
    }
    
    @Override
    public void saveUser() {
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(user);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void deleteUser() {
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.delete(user);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    

}
