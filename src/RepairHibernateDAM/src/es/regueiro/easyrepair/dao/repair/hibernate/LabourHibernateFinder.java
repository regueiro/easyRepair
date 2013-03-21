package es.regueiro.easyrepair.dao.repair.hibernate;

import es.regueiro.easyrepair.api.repair.finder.LabourFinder;
import es.regueiro.easyrepair.model.repair.Labour;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.ResourceClosedException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = LabourFinder.class,
//path = "LabourFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.labour.DefaultLabourFinder"}
)
public class LabourHibernateFinder implements LabourFinder {
    
    private Session session;
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Labour> listAll() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Labour> list = (List<Labour>) session.createCriteria(Labour.class).list();
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
    public List<Labour> listAllEnabled() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Labour> list = (List<Labour>) session.createCriteria(Labour.class)
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
    public List<Labour> findByName(String name) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Labour> list = (List<Labour>) session.createCriteria(Labour.class)
                    .add(Restrictions.isNotNull("name"))
                    .add(Restrictions.like("name", "%" + name + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByName(name);
        } catch (SessionException e) {
            return findByName(name);
        }
    }
    
    @Override
    public Labour getLabour(Long id) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            Labour labour = (Labour) session.createCriteria(Labour.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();
            transaction.commit();
            session.close();
            return labour;
        } catch (ResourceClosedException e) {
            return getLabour(id);
        } catch (SessionException e) {
            return getLabour(id);
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Labour> findByDescription(String description) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Labour> list = (List<Labour>) session.createCriteria(Labour.class)
                    .add(Restrictions.isNotNull("description"))
                    .add(Restrictions.like("description", "%" + description + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByDescription(description);
        } catch (SessionException e) {
            return findByDescription(description);
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Labour> findByPrice(String price) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Labour> list = (List<Labour>) session.createCriteria(Labour.class)
                    .add(Restrictions.isNotNull("price"))
                    .add(Restrictions.sqlRestriction("price like '%" + price + "%'"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByPrice(price);
        } catch (SessionException e) {
            return findByPrice(price);
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Labour> findByMinPrice(String price) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Labour> list = (List<Labour>) session.createCriteria(Labour.class)
                    .add(Restrictions.isNotNull("price"))
                    .add(Restrictions.ge("price", new BigDecimal(price)))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByMinPrice(price);
        } catch (SessionException e) {
            return findByMinPrice(price);
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Labour> findByMaxPrice(String price) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Labour> list = (List<Labour>) session.createCriteria(Labour.class)
                    .add(Restrictions.isNotNull("price"))
                    .add(Restrictions.le("price", new BigDecimal(price)))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByMaxPrice(price);
        } catch (SessionException e) {
            return findByMaxPrice(price);
        }
    }
}
