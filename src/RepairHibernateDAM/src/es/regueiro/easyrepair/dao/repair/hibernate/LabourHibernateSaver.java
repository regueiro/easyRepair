package es.regueiro.easyrepair.dao.repair.hibernate;

import es.regueiro.easyrepair.api.repair.saver.LabourSaver;
import es.regueiro.easyrepair.model.repair.Labour;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = LabourSaver.class,
//path = "LabourFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.repair.DefaultLabourSaver"}
)
public class LabourHibernateSaver implements LabourSaver {

    private Labour labour;
    private Transaction t;
    private Session session;

    public LabourHibernateSaver() {
    }

    @Override
    public void setLabour(Labour labour) {
        this.labour = labour;
    }

    @Override
    public Labour getLabour() {
        return labour;
    }

    @Override
    public void saveLabour() {
        boolean idSetAuto = false;
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(labour);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }

    @Override
    public void deleteLabour() {
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.delete(labour);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }

    @Override
    public void disableLabour() {
        labour.setEnabled(false);
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(labour);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }

    @Override
    public void enableLabour() {
        labour.setEnabled(true);
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(labour);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
}
