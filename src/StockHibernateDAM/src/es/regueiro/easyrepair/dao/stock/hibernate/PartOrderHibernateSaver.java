package es.regueiro.easyrepair.dao.stock.hibernate;

import es.regueiro.easyrepair.api.stock.saver.PartOrderSaver;
import es.regueiro.easyrepair.model.stock.PartOrder;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = PartOrderSaver.class,
//path = "PartOrderFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.repair.DefaultPartOrderSaver"}
)
public class PartOrderHibernateSaver implements PartOrderSaver {
    
    private PartOrder partOrder;
    private Transaction t;
    private Session session;
    
    public PartOrderHibernateSaver() {
    }
    
    @Override
    public void setPartOrder(PartOrder partOrder) {
        this.partOrder = partOrder;
    }
    
    @Override
    public PartOrder getPartOrder() {
        return partOrder;
    }
    
    @Override
    public void savePartOrder() {
        boolean partIdSetAuto = false;
        boolean invoiceIdSetAuto = false;
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(partOrder);
            if (partOrder.getOrderNumber() == null) {
                partOrder.setOrderNumber(partOrder.getId().toString());
                partIdSetAuto = true;
            }
            if (partOrder.getInvoice().getInvoiceNumber() == null) {
                partOrder.getInvoice().setInvoiceNumber(partOrder.getId().toString());
                invoiceIdSetAuto = true;
            }
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            if (partIdSetAuto) {
                partOrder.setOrderNumber(null);
            } 
            if (invoiceIdSetAuto) {
                partOrder.getInvoice().setInvoiceNumber(null);
            }
            session.close();
            throw e;
        }
    }
    
    @Override
    public void deletePartOrder() {
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.delete(partOrder);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void disablePartOrder() {
        partOrder.setEnabled(false);
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(partOrder);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void enablePartOrder() {
        partOrder.setEnabled(true);
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(partOrder);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }

}
