package es.regueiro.easyrepair.dao.stock.hibernate;

import es.regueiro.easyrepair.api.stock.finder.WarehouseFinder;
import es.regueiro.easyrepair.model.stock.Warehouse;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.ResourceClosedException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = WarehouseFinder.class,
//path = "WarehouseFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.stock.DefaultWarehouseFinder"}
)
public class WarehouseHibernateFinder implements WarehouseFinder {

    private Session session;

    @SuppressWarnings("unchecked")
    @Override
    public List<Warehouse> listAll() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Warehouse> list = (List<Warehouse>) session.createCriteria(Warehouse.class).list();
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
    public List<Warehouse> listAllEnabled() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Warehouse> list = (List<Warehouse>) session.createCriteria(Warehouse.class)
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
    public Warehouse getWarehouse(Long id) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            Warehouse warehouse = (Warehouse) session.createCriteria(Warehouse.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();
            transaction.commit();
            session.close();
            return warehouse;
        } catch (ResourceClosedException e) {
            return getWarehouse(id);
        } catch (SessionException e) {
            return getWarehouse(id);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Warehouse> findByName(String name) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Warehouse> list = (List<Warehouse>) session.createCriteria(Warehouse.class)
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
    public List<Warehouse> findByAddressStreet(String street) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Warehouse> list = (List<Warehouse>) session.createCriteria(Warehouse.class)
                    .add(Restrictions.isNotNull("address"))
                    .add(Restrictions.isNotNull("address.street"))
                    .add(Restrictions.like("address.street", "%" + street + "%"))
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
    public List<Warehouse> findByAddressCity(String city) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Warehouse> list = (List<Warehouse>) session.createCriteria(Warehouse.class)
                    .add(Restrictions.isNotNull("address"))
                    .add(Restrictions.isNotNull("address.city"))
                    .add(Restrictions.like("address.city", "%" + city + "%"))
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
    public List<Warehouse> findByAddressPostalCode(String postalCode) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Warehouse> list = (List<Warehouse>) session.createCriteria(Warehouse.class)
                    .add(Restrictions.isNotNull("address"))
                    .add(Restrictions.isNotNull("address.postalCode"))
                    .add(Restrictions.like("address.postalCode", "%" + postalCode + "%"))
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
    public List<Warehouse> findByAddressProvince(String province) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Warehouse> list = (List<Warehouse>) session.createCriteria(Warehouse.class)
                    .add(Restrictions.isNotNull("address"))
                    .add(Restrictions.isNotNull("address.province"))
                    .add(Restrictions.like("address.province", "%" + province + "%"))
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
    public List<Warehouse> findByAddressCountry(String country) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Warehouse> list = (List<Warehouse>) session.createCriteria(Warehouse.class)
                    .add(Restrictions.isNotNull("address"))
                    .add(Restrictions.isNotNull("address.country"))
                    .add(Restrictions.like("address.country", "%" + country + "%"))
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

    @SuppressWarnings("unchecked")
    @Override
    public List<Warehouse> findByEmailAddress(String address) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Warehouse> list = (List<Warehouse>) session.createCriteria(Warehouse.class)
                    .add(Restrictions.isNotNull("email"))
                    .add(Restrictions.isNotNull("email.address"))
                    .add(Restrictions.like("email.address", "%" + address + "%"))
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
    public List<Warehouse> findByPhoneNumber(String number) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Warehouse> list = (List<Warehouse>) session.createCriteria(Warehouse.class)
                    .add(Restrictions.isNotNull("phone"))
                    .add(Restrictions.isNotNull("phone.number"))
                    .add(Restrictions.like("phone.number", "%" + number + "%"))
                    .list();
            transaction.commit();
            session.close();
            return list;
        } catch (ResourceClosedException e) {
            return findByPhoneNumber(number);
        } catch (SessionException e) {
            return findByPhoneNumber(number);
        }
    }
}
