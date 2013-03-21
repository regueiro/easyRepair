package es.regueiro.easyrepair.dao.stock.hibernate;

import es.regueiro.easyrepair.api.stock.finder.PartOrderInvoiceFinder;
import es.regueiro.easyrepair.model.stock.PartOrderInvoice;
import java.util.List;
import org.hibernate.ResourceClosedException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = PartOrderInvoiceFinder.class,
//path = "OrderInvoiceFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.stock.DefaultOrderInvoiceFinder"}
)
public class PartOrderInvoiceHibernateFinder implements PartOrderInvoiceFinder {

    private Session session;

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrderInvoice> listAll() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrderInvoice> list = (List<PartOrderInvoice>) session.createCriteria(PartOrderInvoice.class).list();
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
    public List<PartOrderInvoice> listAllEnabled() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrderInvoice> list = (List<PartOrderInvoice>) session.createCriteria(PartOrderInvoice.class)
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

    @SuppressWarnings("unchecked")
    @Override
    public PartOrderInvoice getOrderInvoice(Long id) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            PartOrderInvoice orderInvoice = (PartOrderInvoice) session.createCriteria(PartOrderInvoice.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return orderInvoice;
        } catch (ResourceClosedException e) {
            return getOrderInvoice(id);
        } catch (SessionException e) {
            return getOrderInvoice(id);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrderInvoice> findByInvoiceDate(String dateAfter, String dateBefore) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrderInvoice> list = (List<PartOrderInvoice>) session.createCriteria(PartOrderInvoice.class)
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
    public List<PartOrderInvoice> findByEstimatedPaymentDate(String dateAfter, String dateBefore) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrderInvoice> list = (List<PartOrderInvoice>) session.createCriteria(PartOrderInvoice.class)
                    .add(Restrictions.isNotNull("estimatedDate"))
                    .add(Restrictions.sqlRestriction("estimatedDate between '" + dateAfter + "' AND '" + dateBefore + "'"))
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
    public List<PartOrderInvoice> findByPaymentDate(String dateAfter, String dateBefore) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrderInvoice> list = (List<PartOrderInvoice>) session.createCriteria(PartOrderInvoice.class)
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
    public List<PartOrderInvoice> findByAcceptedDate(String dateAfter, String dateBefore) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrderInvoice> list = (List<PartOrderInvoice>) session.createCriteria(PartOrderInvoice.class)
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
    public List<PartOrderInvoice> findByStatus(String status) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrderInvoice> list = (List<PartOrderInvoice>) session.createCriteria(PartOrderInvoice.class)
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
    public List<PartOrderInvoice> findByResponsible(String name) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrderInvoice> list = (List<PartOrderInvoice>) session.createCriteria(PartOrderInvoice.class)
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

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrderInvoice> findByPartOrderNumber(String partOrder) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrderInvoice> list = (List<PartOrderInvoice>) session.createCriteria(PartOrderInvoice.class)
                    .add(Restrictions.isNotNull("partOrderNumber"))
                    .add(Restrictions.like("partOrderNumber", "%" + partOrder + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByPartOrderNumber(partOrder);
        } catch (SessionException e) {
            return findByPartOrderNumber(partOrder);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrderInvoice> findByInvoiceNumber(String invoiceNumber) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrderInvoice> list = (List<PartOrderInvoice>) session.createCriteria(PartOrderInvoice.class)
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
    public List<PartOrderInvoice> findByPaymentMenthod(String paymentMethod) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrderInvoice> list = (List<PartOrderInvoice>) session.createCriteria(PartOrderInvoice.class)
                    .add(Restrictions.isNotNull("paymentMethod"))
                    .add(Restrictions.like("paymentMethod", "%" + paymentMethod + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByPaymentMenthod(paymentMethod);
        } catch (SessionException e) {
            return findByPaymentMenthod(paymentMethod);
        }
    }
}
