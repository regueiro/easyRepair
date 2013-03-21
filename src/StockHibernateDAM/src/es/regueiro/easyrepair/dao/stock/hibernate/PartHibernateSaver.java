package es.regueiro.easyrepair.dao.stock.hibernate;

import es.regueiro.easyrepair.api.stock.saver.PartSaver;
import es.regueiro.easyrepair.model.stock.Part;
import es.regueiro.easyrepair.model.stock.Stock;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = PartSaver.class,
//path = "PartFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.repair.DefaultPartSaver"}
)
public class PartHibernateSaver implements PartSaver {
    
    private Part part;
    private Transaction t;
    private Session session;
    
    public PartHibernateSaver() {
    }
    
    @Override
    public void setPart(Part part) {
        this.part = part;
    }
    
    @Override
    public Part getPart() {
        return part;
    }
    
    @Override
    public void savePart() {
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(part);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void deletePart() {
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.delete(part);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
        
    @Override
    public void disablePart() {
        part.setEnabled(false);
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(part);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void enablePart() {
        part.setEnabled(true);
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(part);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }

}
