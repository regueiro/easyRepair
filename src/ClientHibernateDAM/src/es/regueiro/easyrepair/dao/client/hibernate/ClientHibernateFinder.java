package es.regueiro.easyrepair.dao.client.hibernate;

import es.regueiro.easyrepair.api.client.finder.ClientFinder;
import es.regueiro.easyrepair.model.client.Client;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.ResourceClosedException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ClientFinder.class,
//path = "ClientFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.client.DefaultClientFinder"}
)
public class ClientHibernateFinder implements ClientFinder {

    private Session session;

    @SuppressWarnings("unchecked")
    @Override
    public List<Client> listAll() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Client> list = (List<Client>) session.createCriteria(Client.class).list();
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
    public List<Client> listAllEnabled() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Client> list = (List<Client>) session.createCriteria(Client.class)
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
    public List<Client> findByName(String name) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Client> list = (List<Client>) session.createCriteria(Client.class)
                    .add(Restrictions.isNotNull("name"))
                    .add(Restrictions.like("name", "%" + name + "%"))
                    .list();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByName(name);
        } catch (SessionException e) {
            return findByName(name);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Client> findBySurname(String surname) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Client> list = (List<Client>) session.createCriteria(Client.class)
                    .add(Restrictions.isNotNull("surname"))
                    .add(Restrictions.like("surname", "%" + surname + "%"))
                    .list();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findBySurname(surname);
        } catch (SessionException e) {
            return findBySurname(surname);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Client> findByClientId(String id) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Client> list = (List<Client>) session.createCriteria(Client.class)
                    .add(Restrictions.isNotNull("clientId"))
                    .add(Restrictions.sqlRestriction("clientId like '%" + id + "%'"))
                    .list();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByClientId(id);
        } catch (SessionException e) {
            return findByClientId(id);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Client> findByNif(String nif) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Client> list = (List<Client>) session.createCriteria(Client.class)
                    .add(Restrictions.sqlRestriction("nif like '%" + nif + "%'"))
                    .list();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByNif(nif);
        } catch (SessionException e) {
            return findByNif(nif);
        }
    }

    @Override
    public Client getClient(Long id) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            Client client = (Client) session.createCriteria(Client.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return client;
        } catch (ResourceClosedException e) {
            return getClient(id);
        } catch (SessionException e) {
            return getClient(id);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Client> findByEmailLabel(String label) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Client> list = (List<Client>) session.createCriteria(Client.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("email", "e")
                    .add(Restrictions.like("e.label", "%" + label + "%"))
                    .list();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByEmailLabel(label);
        } catch (SessionException e) {
            return findByEmailLabel(label);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Client> findByEmailAddress(String address) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Client> list = (List<Client>) session.createCriteria(Client.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("email", "e")
                    .add(Restrictions.like("e.address", "%" + address + "%"))
                    .list();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByEmailAddress(address);
        } catch (SessionException e) {
            return findByEmailAddress(address);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Client> findByPhoneLabel(String label) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Client> list = (List<Client>) session.createCriteria(Client.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("phone", "p")
                    .add(Restrictions.like("p.label", "%" + label + "%"))
                    .list();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByPhoneLabel(label);
        } catch (SessionException e) {
            return findByPhoneLabel(label);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Client> findByPhoneNumber(String number) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Client> list = (List<Client>) session.createCriteria(Client.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("phone", "p")
                    .add(Restrictions.like("p.number", "%" + number + "%"))
                    .list();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByPhoneNumber(number);
        } catch (SessionException e) {
            return findByPhoneNumber(number);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Client> findByAddressLabel(String label) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Client> list = (List<Client>) session.createCriteria(Client.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("address", "a")
                    .add(Restrictions.like("a.label", "%" + label + "%"))
                    .list();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByAddressLabel(label);
        } catch (SessionException e) {
            return findByAddressLabel(label);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Client> findByAddressStreet(String street) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Client> list = (List<Client>) session.createCriteria(Client.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("address", "a")
                    .add(Restrictions.like("a.street", "%" + street + "%"))
                    .list();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByAddressStreet(street);
        } catch (SessionException e) {
            return findByAddressStreet(street);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Client> findByAddressCity(String city) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Client> list = (List<Client>) session.createCriteria(Client.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("address", "a")
                    .add(Restrictions.like("a.city", "%" + city + "%"))
                    .list();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByAddressCity(city);
        } catch (SessionException e) {
            return findByAddressCity(city);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Client> findByAddressPostalCode(String postalCode) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Client> list = (List<Client>) session.createCriteria(Client.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("address", "a")
                    .add(Restrictions.like("a.postalCode", "%" + postalCode + "%"))
                    .list();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByAddressPostalCode(postalCode);
        } catch (SessionException e) {
            return findByAddressPostalCode(postalCode);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Client> findByAddressProvince(String province) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Client> list = (List<Client>) session.createCriteria(Client.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("address", "a")
                    .add(Restrictions.like("a.province", "%" + province + "%"))
                    .list();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByAddressProvince(province);
        } catch (SessionException e) {
            return findByAddressProvince(province);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Client> findByAddressCountry(String country) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Client> list = (List<Client>) session.createCriteria(Client.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("address", "a")
                    .add(Restrictions.like("a.country", "%" + country + "%"))
                    .list();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByAddressCountry(country);
        } catch (SessionException e) {
            return findByAddressCountry(country);
        }
    }
}
