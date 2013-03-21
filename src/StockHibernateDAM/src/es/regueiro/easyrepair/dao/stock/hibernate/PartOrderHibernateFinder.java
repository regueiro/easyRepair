package es.regueiro.easyrepair.dao.stock.hibernate;

import es.regueiro.easyrepair.api.stock.finder.PartOrderFinder;
import es.regueiro.easyrepair.model.stock.PartOrder;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.ResourceClosedException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = PartOrderFinder.class,
//path = "PartOrderFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.stock.DefaultPartOrderFinder"}
)
public class PartOrderHibernateFinder implements PartOrderFinder {

    private Session session;

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> listAll() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class).list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return listAll();
        } catch (SessionException e) {
            return listAll();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> listAllEnabled() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .add(Restrictions.eq("enabled", true))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return listAllEnabled();
        } catch (SessionException e) {
            return listAllEnabled();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> findByOrderNumber(String number) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .add(Restrictions.like("orderNumber", "%" + number + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByOrderNumber(number);
        } catch (SessionException e) {
            return findByOrderNumber(number);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public PartOrder getPartOrder(Long id) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            PartOrder partOrder = (PartOrder) session.createCriteria(PartOrder.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();
            transaction.commit();
            session.close();
            return partOrder;
        } catch (ResourceClosedException e) {
            return getPartOrder(id);
        } catch (SessionException e) {
            return getPartOrder(id);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> findByStatus(String status) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .add(Restrictions.isNotNull("status"))
                    .add(Restrictions.like("status", "%" + status + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByStatus(status);
        } catch (SessionException e) {
            return findByStatus(status);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> findByResponsible(String name) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .createAlias("responsible", "resp")
                    .add(Restrictions.isNotNull("resp.name"))
                    .add(Restrictions.like("resp.name", "%" + name + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByResponsible(name);
        } catch (SessionException e) {
            return findByResponsible(name);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> findByOrderDate(String dateAfter, String dateBefore) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .add(Restrictions.isNotNull("orderDate"))
                    .add(Restrictions.sqlRestriction("orderDate between '" + dateAfter + "' AND '" + dateBefore + "'"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByOrderDate(dateAfter, dateBefore);
        } catch (SessionException e) {
            return findByOrderDate(dateAfter, dateBefore);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> findByEstimatedDate(String dateAfter, String dateBefore) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .add(Restrictions.isNotNull("estimatedDate"))
                    .add(Restrictions.sqlRestriction("estimatedDate between '" + dateAfter + "' AND '" + dateBefore + "'"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByEstimatedDate(dateAfter, dateBefore);
        } catch (SessionException e) {
            return findByEstimatedDate(dateAfter, dateBefore);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> findByReceiptDate(String dateAfter, String dateBefore) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .add(Restrictions.isNotNull("receiptDate"))
                    .add(Restrictions.sqlRestriction("receiptDate between '" + dateAfter + "' AND '" + dateBefore + "'"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByReceiptDate(dateAfter, dateBefore);
        } catch (SessionException e) {
            return findByReceiptDate(dateAfter, dateBefore);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> findByWarehouseName(String warehouse) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .createAlias("warehouse", "war")
                    .add(Restrictions.isNotNull("war.name"))
                    .add(Restrictions.like("war.name", "%" + warehouse + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByWarehouseName(warehouse);
        } catch (SessionException e) {
            return findByWarehouseName(warehouse);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> findByDiscount(String discount) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .add(Restrictions.isNotNull("discount"))
                    .add(Restrictions.sqlRestriction("{alias}.discount like '%" + discount + "%'"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByDiscount(discount);
        } catch (SessionException e) {
            return findByDiscount(discount);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> findByMaxDiscount(String discount) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .add(Restrictions.isNotNull("discount"))
                    .add(Restrictions.le("discount", new BigDecimal(discount)))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByMaxDiscount(discount);
        } catch (SessionException e) {
            return findByMaxDiscount(discount);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> findByMinDiscount(String discount) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .add(Restrictions.isNotNull("discount"))
                    .add(Restrictions.ge("discount", new BigDecimal(discount)))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByMinDiscount(discount);
        } catch (SessionException e) {
            return findByMinDiscount(discount);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> findByShippingCosts(String costs) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .add(Restrictions.isNotNull("shippingCosts"))
                    .add(Restrictions.sqlRestriction("{alias}.shippingCosts like '%" + costs + "%'"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByShippingCosts(costs);
        } catch (SessionException e) {
            return findByShippingCosts(costs);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> findByMaxShippingCosts(String costs) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .add(Restrictions.isNotNull("shippingCosts"))
                    .add(Restrictions.le("shippingCosts", new BigDecimal(costs)))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByMaxShippingCosts(costs);
        } catch (SessionException e) {
            return findByMaxShippingCosts(costs);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> findByMinShippingCosts(String costs) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .add(Restrictions.isNotNull("shippingCosts"))
                    .add(Restrictions.ge("shippingCosts", new BigDecimal(costs)))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByMinShippingCosts(costs);
        } catch (SessionException e) {
            return findByMinShippingCosts(costs);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> findByOtherCosts(String costs) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .add(Restrictions.isNotNull("otherCosts"))
                    .add(Restrictions.sqlRestriction("{alias}.otherCosts like '%" + costs + "%'"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByOtherCosts(costs);
        } catch (SessionException e) {
            return findByOtherCosts(costs);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> findByMaxOtherCosts(String costs) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .add(Restrictions.isNotNull("otherCosts"))
                    .add(Restrictions.le("otherCosts", new BigDecimal(costs)))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByMaxOtherCosts(costs);
        } catch (SessionException e) {
            return findByMaxOtherCosts(costs);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> findByMinOtherCosts(String costs) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .add(Restrictions.isNotNull("otherCosts"))
                    .add(Restrictions.ge("otherCosts", new BigDecimal(costs)))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByMinOtherCosts(costs);
        } catch (SessionException e) {
            return findByMinOtherCosts(costs);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PartOrder> findBySupplierName(String name) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<PartOrder> list = (List<PartOrder>) session.createCriteria(PartOrder.class)
                    .createAlias("supplier", "sup")
                    .add(Restrictions.isNotNull("sup.name"))
                    .add(Restrictions.like("sup.name", "%" + name + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findBySupplierName(name);
        } catch (SessionException e) {
            return findBySupplierName(name);
        }
    }
}
