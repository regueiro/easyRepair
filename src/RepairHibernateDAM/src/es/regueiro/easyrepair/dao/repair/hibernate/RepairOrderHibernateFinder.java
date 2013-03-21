package es.regueiro.easyrepair.dao.repair.hibernate;

import es.regueiro.easyrepair.api.repair.finder.RepairOrderFinder;
import es.regueiro.easyrepair.model.repair.RepairOrder;
import java.util.List;
import org.hibernate.ResourceClosedException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = RepairOrderFinder.class,
//path = "RepairOrderFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.repairOrder.DefaultRepairOrderFinder"}
)
public class RepairOrderHibernateFinder implements RepairOrderFinder {

    private Session session;

    @SuppressWarnings("unchecked")
    @Override
    public List<RepairOrder> listAll() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairOrder> list = (List<RepairOrder>) session.createCriteria(RepairOrder.class).list();
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
    public List<RepairOrder> listAllEnabled() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairOrder> list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
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

    @Override
    public RepairOrder getRepairOrder(Long id) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            RepairOrder repairOrder = (RepairOrder) session.createCriteria(RepairOrder.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();
            transaction.commit();
            session.close();
            return repairOrder;
        } catch (ResourceClosedException e) {
            return getRepairOrder(id);
        } catch (SessionException e) {
            return getRepairOrder(id);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RepairOrder> findByOrderDate(String dateAfter, String dateBefore) {

        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairOrder> list;
            if (!dateAfter.equals("") && !dateBefore.equals("")) {
                list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                        .add(Restrictions.isNotNull("orderDate"))
                        .add(Restrictions.sqlRestriction("orderDate between '" + dateAfter + "' AND '" + dateBefore + "'"))
                        .list();
            } else if (dateAfter.equals("") && !dateBefore.equals("")) {
                list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                        .add(Restrictions.isNotNull("orderDate"))
                        .add(Restrictions.sqlRestriction("orderDate <= '" + dateBefore + "'"))
                        .list();
            } else if (!dateAfter.equals("") && dateBefore.equals("")) {
                list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                        .add(Restrictions.isNotNull("orderDate"))
                        .add(Restrictions.sqlRestriction("orderDate >= '" + dateAfter + "'"))
                        .list();
            } else {
                list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                        .add(Restrictions.isNotNull("orderDate"))
                        .list();
            }

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
    public List<RepairOrder> findByEstimatedDate(String dateAfter, String dateBefore) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairOrder> list;
            if (!dateAfter.equals("") && !dateBefore.equals("")) {
                list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                        .add(Restrictions.isNotNull("estimatedDate"))
                        .add(Restrictions.sqlRestriction("estimatedDate between '" + dateAfter + "' AND '" + dateBefore + "'"))
                        .list();
            } else if (dateAfter.equals("") && !dateBefore.equals("")) {
                list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                        .add(Restrictions.isNotNull("estimatedDate"))
                        .add(Restrictions.sqlRestriction("estimatedDate <= '" + dateBefore + "'"))
                        .list();
            } else if (!dateAfter.equals("") && dateBefore.equals("")) {
                list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                        .add(Restrictions.isNotNull("estimatedDate"))
                        .add(Restrictions.sqlRestriction("estimatedDate >= '" + dateAfter + "'"))
                        .list();
            } else {
                list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                        .add(Restrictions.isNotNull("estimatedDate"))
                        .list();
            }
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
    public List<RepairOrder> findByFinishDate(String dateAfter, String dateBefore) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairOrder> list;
            if (!dateAfter.equals("") && !dateBefore.equals("")) {
                list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                        .add(Restrictions.isNotNull("finishDate"))
                        .add(Restrictions.sqlRestriction("finishDate between '" + dateAfter + "' AND '" + dateBefore + "'"))
                        .list();
            } else if (dateAfter.equals("") && !dateBefore.equals("")) {
                list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                        .add(Restrictions.isNotNull("finishDate"))
                        .add(Restrictions.sqlRestriction("finishDate <= '" + dateBefore + "'"))
                        .list();
            } else if (!dateAfter.equals("") && dateBefore.equals("")) {
                list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                        .add(Restrictions.isNotNull("finishDate"))
                        .add(Restrictions.sqlRestriction("finishDate >= '" + dateAfter + "'"))
                        .list();
            } else {
                list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                        .add(Restrictions.isNotNull("finishDate"))
                        .list();
            }
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByFinishDate(dateAfter, dateBefore);
        } catch (SessionException e) {
            return findByFinishDate(dateAfter, dateBefore);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RepairOrder> findByDeliveryDate(String dateAfter, String dateBefore) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairOrder> list;
            if (!dateAfter.equals("") && !dateBefore.equals("")) {
                list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                        .add(Restrictions.isNotNull("deliveryDate"))
                        .add(Restrictions.sqlRestriction("deliveryDate between '" + dateAfter + "' AND '" + dateBefore + "'"))
                        .list();
            } else if (dateAfter.equals("") && !dateBefore.equals("")) {
                list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                        .add(Restrictions.isNotNull("deliveryDate"))
                        .add(Restrictions.sqlRestriction("deliveryDate <= '" + dateBefore + "'"))
                        .list();
            } else if (!dateAfter.equals("") && dateBefore.equals("")) {
                list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                        .add(Restrictions.isNotNull("deliveryDate"))
                        .add(Restrictions.sqlRestriction("deliveryDate >= '" + dateAfter + "'"))
                        .list();
            } else {
                list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                        .add(Restrictions.isNotNull("deliveryDate"))
                        .list();
            }
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByDeliveryDate(dateAfter, dateBefore);
        } catch (SessionException e) {
            return findByDeliveryDate(dateAfter, dateBefore);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RepairOrder> findByStatus(String status) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairOrder> list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
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
    public List<RepairOrder> findByResponsible(String name) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairOrder> list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
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
    public List<RepairOrder> findByRepairOrderNumber(String orderNumber) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairOrder> list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                    .add(Restrictions.isNotNull("orderNumber"))
                    .add(Restrictions.sqlRestriction("orderNumber like '%" + orderNumber + "%'"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByRepairOrderNumber(orderNumber);
        } catch (SessionException e) {
            return findByRepairOrderNumber(orderNumber);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RepairOrder> findByEstimateNumber(String estimateNumber) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairOrder> list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                    .createAlias("estimate", "est")
                    .add(Restrictions.isNotNull("estimate.estimateNumber"))
                    .add(Restrictions.like("estimate.estimateNumber", "%" + estimateNumber + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByEstimateNumber(estimateNumber);
        } catch (SessionException e) {
            return findByEstimateNumber(estimateNumber);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RepairOrder> findByKilometres(String kilometres) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairOrder> list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                    .add(Restrictions.isNotNull("kilometres"))
                    .add(Restrictions.like("kilometres", "%" + kilometres + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByKilometres(kilometres);
        } catch (SessionException e) {
            return findByKilometres(kilometres);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RepairOrder> findByGasTankLevel(String tankLevel) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairOrder> list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                    .add(Restrictions.isNotNull("gasTankLevel"))
                    .add(Restrictions.like("gasTankLevel", "%" + tankLevel + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByGasTankLevel(tankLevel);
        } catch (SessionException e) {
            return findByGasTankLevel(tankLevel);
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<RepairOrder> findByDiscount(String discount) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairOrder> list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                    .add(Restrictions.isNotNull("discount"))
                    .add(Restrictions.sqlRestriction("discount like '%" + discount + "%'"))
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
    public List<RepairOrder> findByVehicle(String registration) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<RepairOrder> list = (List<RepairOrder>) session.createCriteria(RepairOrder.class)
                    .createAlias("vehicle", "veh")
                    .add(Restrictions.isNotNull("veh.registration"))
                    .add(Restrictions.like("veh.registration", "%" + registration + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByVehicle(registration);
        } catch (SessionException e) {
            return findByVehicle(registration);
        }
    }
}
