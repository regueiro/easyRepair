package es.regueiro.easyrepair.dao.stock.hibernate;

import es.regueiro.easyrepair.api.stock.saver.WarehouseSaver;
import es.regueiro.easyrepair.model.stock.Warehouse;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = WarehouseSaver.class,
//path = "WarehouseSaverServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.stock.DefaultWarehouseSaver"}
)
public class WarehouseHibernateSaver implements WarehouseSaver {
    
    private Warehouse warehouse;
    private Transaction t;
    private Session session;
    
    public WarehouseHibernateSaver() {
    }
    
    @Override
    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
    
    @Override
    public Warehouse getWarehouse() {
        return warehouse;
    }
    
    @Override
    public void saveWarehouse() {
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(warehouse);
                t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void deleteWarehouse() {
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.delete(warehouse);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void disableWarehouse() {
        warehouse.setEnabled(false);
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(warehouse);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void enableWarehouse() {
        warehouse.setEnabled(true);
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(warehouse);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }

}
