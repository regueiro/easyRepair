package es.regueiro.easyrepair.dao.stock.hibernate;

import es.regueiro.easyrepair.api.stock.saver.SupplierSaver;
import es.regueiro.easyrepair.model.stock.Supplier;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = SupplierSaver.class,
//path = "SupplierFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.repair.DefaultSupplierSaver"}
)
public class SupplierHibernateSaver implements SupplierSaver {
    
    private Supplier supplier;
    private Transaction t;
    private Session session;
    
    public SupplierHibernateSaver() {
    }
    
    @Override
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
    
    @Override
    public Supplier getSupplier() {
        return supplier;
    }
    
    @Override
    public void saveSupplier() {
        boolean idSetAuto = false;
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(supplier);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void deleteSupplier() {
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.delete(supplier);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void disableSupplier() {
        supplier.setEnabled(false);
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(supplier);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void enableSupplier() {
        supplier.setEnabled(true);
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(supplier);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }

}
