package es.regueiro.easyrepair.dao.stock.hibernate;

import es.regueiro.easyrepair.api.stock.finder.SupplierFinder;
import es.regueiro.easyrepair.model.stock.Supplier;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.ResourceClosedException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = SupplierFinder.class,
//path = "SupplierFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.stock.DefaultSupplierFinder"}
)
public class SupplierHibernateFinder implements SupplierFinder {

    private Session session;

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> listAll() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class).list();
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
    public List<Supplier> listAllEnabled() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class)
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
    public Supplier getSupplier(Long id) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            Supplier supplier = (Supplier) session.createCriteria(Supplier.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();
            transaction.commit();
            session.close();
            return supplier;
        } catch (ResourceClosedException e) {
            return getSupplier(id);
        } catch (SessionException e) {
            return getSupplier(id);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> findByName(String name) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class)
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

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> findByCategory(String category) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class)
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
    public List<Supplier> findByWeb(String web) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class)
                    .add(Restrictions.isNotNull("web"))
                    .add(Restrictions.like("web", "%" + web + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByWeb(web);
        } catch (SessionException e) {
            return findByWeb(web);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> findByPaymentMethod(String paymentMethod) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class)
                    .add(Restrictions.isNotNull("paymentMethod"))
                    .add(Restrictions.like("paymentMethod", "%" + paymentMethod + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByPaymentMethod(paymentMethod);
        } catch (SessionException e) {
            return findByPaymentMethod(paymentMethod);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> findByShippingMethod(String shippingMethod) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class)
                    .add(Restrictions.isNotNull("shippingMethod"))
                    .add(Restrictions.like("shippingMethod", "%" + shippingMethod + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByShippingMethod(shippingMethod);
        } catch (SessionException e) {
            return findByShippingMethod(shippingMethod);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> findByNif(String nif) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class)
                    .add(Restrictions.isNotNull("name"))
                    .add(Restrictions.sqlRestriction("nif like '%" + nif + "%'"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByNif(nif);
        } catch (SessionException e) {
            return findByNif(nif);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> findByEmailLabel(String label) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("email", "e")
                    .add(Restrictions.like("e.label", "%" + label + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByEmailLabel(label);
        } catch (SessionException e) {
            return findByEmailLabel(label);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> findByEmailAddress(String address) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();


            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("email", "e")
                    .add(Restrictions.like("e.address", "%" + address + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByEmailAddress(address);
        } catch (SessionException e) {
            return findByEmailAddress(address);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> findByPhoneLabel(String label) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("phone", "p")
                    .add(Restrictions.like("p.label", "%" + label + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByPhoneLabel(label);
        } catch (SessionException e) {
            return findByPhoneLabel(label);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> findByPhoneNumber(String label) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("phone", "p")
                    .add(Restrictions.like("p.number", "%" + label + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByPhoneNumber(label);
        } catch (SessionException e) {
            return findByPhoneNumber(label);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> findByAddressLabel(String label) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("address", "a")
                    .add(Restrictions.like("a.label", "%" + label + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByAddressLabel(label);
        } catch (SessionException e) {
            return findByAddressLabel(label);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> findByAddressStreet(String street) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("address", "a")
                    .add(Restrictions.like("a.street", "%" + street + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByAddressStreet(street);
        } catch (SessionException e) {
            return findByAddressStreet(street);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> findByAddressCity(String city) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("address", "a")
                    .add(Restrictions.like("a.city", "%" + city + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByAddressCity(city);
        } catch (SessionException e) {
            return findByAddressCity(city);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> findByAddressPostalCode(String postalCode) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("address", "a")
                    .add(Restrictions.like("a.postalCode", "%" + postalCode + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByAddressPostalCode(postalCode);
        } catch (SessionException e) {
            return findByAddressPostalCode(postalCode);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> findByAddressProvince(String province) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("address", "a")
                    .add(Restrictions.like("a.province", "%" + province + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByAddressProvince(province);
        } catch (SessionException e) {
            return findByAddressProvince(province);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> findByAddressCountry(String country) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Supplier> list = (List<Supplier>) session.createCriteria(Supplier.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("address", "a")
                    .add(Restrictions.like("a.country", "%" + country + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByAddressCountry(country);
        } catch (SessionException e) {
            return findByAddressCountry(country);
        }
    }
}
