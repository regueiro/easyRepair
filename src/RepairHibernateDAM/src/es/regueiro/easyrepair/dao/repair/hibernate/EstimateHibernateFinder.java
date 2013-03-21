package es.regueiro.easyrepair.dao.repair.hibernate;

import es.regueiro.easyrepair.api.repair.finder.EstimateFinder;
import es.regueiro.easyrepair.model.repair.Estimate;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.ResourceClosedException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = EstimateFinder.class,
//path = "EstimateFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.estimate.DefaultEstimateFinder"}
)
public class EstimateHibernateFinder implements EstimateFinder {

    private Session session;

    @SuppressWarnings("unchecked")
    @Override
    public List<Estimate> listAll() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Estimate> list = (List<Estimate>) session.createCriteria(Estimate.class).list();
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
    public List<Estimate> listAllEnabled() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Estimate> list = (List<Estimate>) session.createCriteria(Estimate.class)
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
    public Estimate getEstimate(Long id) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            Estimate estimate = (Estimate) session.createCriteria(Estimate.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return estimate;
        } catch (ResourceClosedException e) {
            return getEstimate(id);
        } catch (SessionException e) {
            return getEstimate(id);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Estimate> findByDiscount(String discount) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Estimate> list = (List<Estimate>) session.createCriteria(Estimate.class)
                    .add(Restrictions.isNotNull("discount"))
                    .add(Restrictions.sqlRestriction("discount like '%" + discount + "%'"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByDiscount(discount);
        } catch (SessionException e) {
            return findByDiscount(discount);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Estimate> findByMinDiscount(String discount) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Estimate> list = (List<Estimate>) session.createCriteria(Estimate.class)
                    .add(Restrictions.isNotNull("discount"))
                    .add(Restrictions.ge("discount", discount))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByMinDiscount(discount);
        } catch (SessionException e) {
            return findByMinDiscount(discount);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Estimate> findByMaxDiscount(String discount) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Estimate> list = (List<Estimate>) session.createCriteria(Estimate.class)
                    .add(Restrictions.isNotNull("discount"))
                    .add(Restrictions.le("discount", discount))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByMaxDiscount(discount);
        } catch (SessionException e) {
            return findByMaxDiscount(discount);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Estimate> findByEstimateNumber(String estimateNumber) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Estimate> list = (List<Estimate>) session.createCriteria(Estimate.class)
                    .add(Restrictions.isNotNull("estimateNumber"))
                    .add(Restrictions.sqlRestriction("estimateNumber like '%" + estimateNumber + "%'"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByEstimateNumber(estimateNumber);
        } catch (SessionException e) {
            return findByEstimateNumber(estimateNumber);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Estimate> findByEstimateDate(String dateAfter, String dateBefore) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Estimate> list = (List<Estimate>) session.createCriteria(Estimate.class)
                    .add(Restrictions.isNotNull("estimateDate"))
                    .add(Restrictions.sqlRestriction("estimateDate between '" + dateAfter + "' AND '" + dateBefore + "'"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByEstimateDate(dateAfter, dateBefore);
        } catch (SessionException e) {
            return findByEstimateDate(dateAfter, dateBefore);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Estimate> findByAcceptedDate(String dateAfter, String dateBefore) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Estimate> list = (List<Estimate>) session.createCriteria(Estimate.class)
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
    public List<Estimate> findByStatus(String status) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Estimate> list = (List<Estimate>) session.createCriteria(Estimate.class)
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
    public List<Estimate> findByResponsible(String name) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Estimate> list = (List<Estimate>) session.createCriteria(Estimate.class)
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
