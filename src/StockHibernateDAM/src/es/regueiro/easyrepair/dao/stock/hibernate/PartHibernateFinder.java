package es.regueiro.easyrepair.dao.stock.hibernate;

import es.regueiro.easyrepair.api.stock.finder.PartFinder;
import es.regueiro.easyrepair.model.stock.Part;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.ResourceClosedException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = PartFinder.class,
//path = "PartFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.stock.DefaultPartFinder"}
)
public class PartHibernateFinder implements PartFinder {

    private Session session;

    @SuppressWarnings("unchecked")
    @Override
    public List<Part> listAll() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Part> list = (List<Part>) session.createCriteria(Part.class).list();
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
    public List<Part> listAllEnabled() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Part> list = (List<Part>) session.createCriteria(Part.class)
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
    public Part getPart(Long id) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            Part part = (Part) session.createCriteria(Part.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();
            transaction.commit();
            session.close();
            return part;
        } catch (ResourceClosedException e) {
            return getPart(id);
        } catch (SessionException e) {
            return getPart(id);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Part> findByMake(String make) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Part> list = (List<Part>) session.createCriteria(Part.class)
                    .add(Restrictions.isNotNull("make"))
                    .add(Restrictions.like("make", "%" + make + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByMake(make);
        } catch (SessionException e) {
            return findByMake(make);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Part> findByModel(String model) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Part> list = (List<Part>) session.createCriteria(Part.class)
                    .add(Restrictions.isNotNull("model"))
                    .add(Restrictions.like("model", "%" + model + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByModel(model);
        } catch (SessionException e) {
            return findByModel(model);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Part> findByCategory(String category) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Part> list = (List<Part>) session.createCriteria(Part.class)
                    .add(Restrictions.isNotNull("category"))
                    .add(Restrictions.like("category", "%" + category + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByCategory(category);
        } catch (SessionException e) {
            return findByCategory(category);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Part> findByPrice(String price) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Part> list = (List<Part>) session.createCriteria(Part.class)
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
    public List<Part> findByMaxPrice(String price) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Part> list = (List<Part>) session.createCriteria(Part.class)
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

    @SuppressWarnings("unchecked")
    @Override
    public List<Part> findByMinPrice(String price) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Part> list = (List<Part>) session.createCriteria(Part.class)
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
}
