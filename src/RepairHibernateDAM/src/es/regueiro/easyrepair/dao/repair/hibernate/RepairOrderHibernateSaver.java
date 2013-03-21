package es.regueiro.easyrepair.dao.repair.hibernate;

import es.regueiro.easyrepair.api.repair.saver.RepairOrderSaver;
import es.regueiro.easyrepair.model.repair.RepairOrder;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = RepairOrderSaver.class,
//path = "RepairOrderFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.repair.DefaultRepairOrderSaver"}
)
public class RepairOrderHibernateSaver implements RepairOrderSaver {
    
    private RepairOrder repairOrder;
    private Transaction t;
    private Session session;
    
    public RepairOrderHibernateSaver() {
    }
    
    @Override
    public void setRepairOrder(RepairOrder repairOrder) {
        this.repairOrder = repairOrder;
    }
    
    @Override
    public RepairOrder getRepairOrder() {
        return repairOrder;
    }
    
    @Override
    public void saveRepairOrder() {
        boolean idSetAuto = false;
        boolean invoiceIdSetAuto = false;
        boolean estimateIdSetAuto = false;
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(repairOrder);
            if (repairOrder.getOrderNumber() == null) {
                repairOrder.setOrderNumber(repairOrder.getId().toString());
                idSetAuto = true;
            }
            if (repairOrder.getInvoice().getInvoiceNumber() == null) {
                repairOrder.getInvoice().setInvoiceNumber(repairOrder.getId().toString());
                invoiceIdSetAuto = true;
            }
            if (repairOrder.getEstimate().getEstimateNumber() == null) {
                repairOrder.getEstimate().setEstimateNumber(repairOrder.getId().toString());
                estimateIdSetAuto = true;
            }
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            if (idSetAuto) {
                repairOrder.setOrderNumber(null);
            }
            if (invoiceIdSetAuto) {
                repairOrder.getInvoice().setInvoiceNumber(null);
            }
            if (estimateIdSetAuto) {
                repairOrder.getEstimate().setEstimateNumber(null);
            }
            session.close();
            throw e;
        }
    }
    
    @Override
    public void deleteRepairOrder() {
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.delete(repairOrder);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void disableRepairOrder() {
        repairOrder.setEnabled(false);
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(repairOrder);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void enableRepairOrder() {
        repairOrder.setEnabled(true);
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(repairOrder);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }

}
