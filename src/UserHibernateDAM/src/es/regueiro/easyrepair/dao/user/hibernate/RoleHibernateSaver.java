package es.regueiro.easyrepair.dao.user.hibernate;

import es.regueiro.easyrepair.api.user.saver.RoleSaver;
import es.regueiro.easyrepair.model.user.Role;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = RoleSaver.class,
//path = "RoleFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.user.DefaultRoleSaver"}
)
public class RoleHibernateSaver implements RoleSaver {
    
    private Role role;
    private Transaction t;
    private Session session;
    
    public RoleHibernateSaver() {
    }
    
    @Override
    public void setRole(Role role) {
        this.role = role;
    }
    
    @Override
    public Role getRole() {
        return role;
    }
    
    @Override
    public void saveRole() {
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(role);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void deleteRole() {
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.delete(role);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    

}
