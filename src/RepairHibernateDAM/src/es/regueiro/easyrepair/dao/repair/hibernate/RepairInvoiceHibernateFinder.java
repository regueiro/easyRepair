package es.regueiro.easyrepair.dao.repair.hibernate;

import es.regueiro.easyrepair.api.repair.finder.RepairInvoiceFinder;
import es.regueiro.easyrepair.model.repair.RepairInvoice;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.ResourceClosedException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = RepairInvoiceFinder.class,
//path = "RepairInvoiceFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.repairInvoice.DefaultRepairInvoiceFinder"}
)
public class RepairInvoiceHibernateFinder implements RepairInvoiceFinder {

    private Session session;

    @SuppressWarnings("unchecked")
    @Override
    public List<RepairInvoice> listAll() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairInvoice> list = (List<RepairInvoice>) session.createCriteria(RepairInvoice.class).list();
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
    public List<RepairInvoice> listAllEnabled() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairInvoice> list = (List<RepairInvoice>) session.createCriteria(RepairInvoice.class)
                    .add(Restrictions.eq("enabled", true))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return listAllEnabled();
        } catch (SessionException e) {
            return listAllEnabled();
        }
    }

    @Override
    public RepairInvoice getRepairInvoice(Long id) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            RepairInvoice repairInvoice = (RepairInvoice) session.createCriteria(RepairInvoice.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return repairInvoice;
        } catch (ResourceClosedException e) {
            return getRepairInvoice(id);
        } catch (SessionException e) {
            return getRepairInvoice(id);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RepairInvoice> findByInvoiceNumber(String invoiceNumber) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairInvoice> list = (List<RepairInvoice>) session.createCriteria(RepairInvoice.class)
                    .add(Restrictions.isNotNull("invoiceNumber"))
                    .add(Restrictions.sqlRestriction("invoiceNumber like '%" + invoiceNumber + "%'"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByInvoiceNumber(invoiceNumber);
        } catch (SessionException e) {
            return findByInvoiceNumber(invoiceNumber);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RepairInvoice> findByInvoiceDate(String dateAfter, String dateBefore) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairInvoice> list = (List<RepairInvoice>) session.createCriteria(RepairInvoice.class)
                    .add(Restrictions.isNotNull("invoiceDate"))
                    .add(Restrictions.sqlRestriction("invoiceDate between '" + dateAfter + "' AND '" + dateBefore + "'"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByInvoiceDate(dateAfter, dateBefore);
        } catch (SessionException e) {
            return findByInvoiceDate(dateAfter, dateBefore);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RepairInvoice> findByAcceptedDate(String dateAfter, String dateBefore) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairInvoice> list = (List<RepairInvoice>) session.createCriteria(RepairInvoice.class)
                    .add(Restrictions.isNotNull("acceptedDate"))
                    .add(Restrictions.sqlRestriction("acceptedDate between '" + dateAfter + "' AND '" + dateBefore + "'"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByAcceptedDate(dateAfter, dateBefore);
        } catch (SessionException e) {
            return findByAcceptedDate(dateAfter, dateBefore);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RepairInvoice> findByEstimatedPaymentDate(String dateAfter, String dateBefore) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairInvoice> list = (List<RepairInvoice>) session.createCriteria(RepairInvoice.class)
                    .add(Restrictions.isNotNull("estimatedPaymentDate"))
                    .add(Restrictions.sqlRestriction("estimatedPaymentDate between '" + dateAfter + "' AND '" + dateBefore + "'"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByEstimatedPaymentDate(dateAfter, dateBefore);
        } catch (SessionException e) {
            return findByEstimatedPaymentDate(dateAfter, dateBefore);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RepairInvoice> findByPaymentDate(String dateAfter, String dateBefore) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairInvoice> list = (List<RepairInvoice>) session.createCriteria(RepairInvoice.class)
                    .add(Restrictions.isNotNull("paymentDate"))
                    .add(Restrictions.sqlRestriction("paymentDate between '" + dateAfter + "' AND '" + dateBefore + "'"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByPaymentDate(dateAfter, dateBefore);
        } catch (SessionException e) {
            return findByPaymentDate(dateAfter, dateBefore);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RepairInvoice> findByStatus(String status) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairInvoice> list = (List<RepairInvoice>) session.createCriteria(RepairInvoice.class)
                    .add(Restrictions.isNotNull("status"))
                    .add(Restrictions.like("status", "%" + status + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByStatus(status);
        } catch (SessionException e) {
            return findByStatus(status);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RepairInvoice> findByPaymentMethod(String paymentMethod) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairInvoice> list = (List<RepairInvoice>) session.createCriteria(RepairInvoice.class)
                    .add(Restrictions.isNotNull("paymentMethod"))
                    .add(Restrictions.like("paymentMethod", "%" + paymentMethod + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByPaymentMethod(paymentMethod);
        } catch (SessionException e) {
            return findByPaymentMethod(paymentMethod);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RepairInvoice> findByPaymentResponsible(String paymentResponsible) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairInvoice> list = (List<RepairInvoice>) session.createCriteria(RepairInvoice.class)
                    .add(Restrictions.isNotNull("paymentResponsible"))
                    .add(Restrictions.like("paymentResponsible", "%" + paymentResponsible + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByPaymentResponsible(paymentResponsible);
        } catch (SessionException e) {
            return findByPaymentResponsible(paymentResponsible);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RepairInvoice> findByResponsible(String name) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairInvoice> list = (List<RepairInvoice>) session.createCriteria(RepairInvoice.class)
                    .createAlias("responsible", "resp")
                    .add(Restrictions.isNotNull("resp.name"))
                    .add(Restrictions.like("resp.name", "%" + name + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByResponsible(name);
        } catch (SessionException e) {
            return findByResponsible(name);
        }
    }
}
