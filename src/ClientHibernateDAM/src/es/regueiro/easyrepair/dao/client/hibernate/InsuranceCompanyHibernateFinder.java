package es.regueiro.easyrepair.dao.client.hibernate;

import es.regueiro.easyrepair.api.client.finder.InsuranceCompanyFinder;
import es.regueiro.easyrepair.model.client.InsuranceCompany;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.ResourceClosedException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = InsuranceCompanyFinder.class,
//path = "InsuranceCompanyFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.client.DefaultInsuranceCompanyFinder"}
)
public class InsuranceCompanyHibernateFinder implements InsuranceCompanyFinder {

    private Session session;

    @SuppressWarnings("unchecked")
    @Override
    public List<InsuranceCompany> listAll() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<InsuranceCompany> list = (List<InsuranceCompany>) session.createCriteria(InsuranceCompany.class).list();
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
    public List<InsuranceCompany> listAllEnabled() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<InsuranceCompany> list = (List<InsuranceCompany>) session.createCriteria(InsuranceCompany.class)
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
    public InsuranceCompany getInsuranceCompany(Long id) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            InsuranceCompany insuranceCompany = (InsuranceCompany) session.createCriteria(InsuranceCompany.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return insuranceCompany;
        } catch (ResourceClosedException e) {
            return getInsuranceCompany(id);
        } catch (SessionException e) {
            return getInsuranceCompany(id);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<InsuranceCompany> findByName(String name) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<InsuranceCompany> list = (List<InsuranceCompany>) session.createCriteria(InsuranceCompany.class)
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
    public List<InsuranceCompany> findByWeb(String web) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<InsuranceCompany> list = (List<InsuranceCompany>) session.createCriteria(InsuranceCompany.class)
                    .add(Restrictions.isNotNull("web"))
                    .add(Restrictions.like("web", "%" + web + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByWeb(web);
        } catch (SessionException e) {
            return findByWeb(web);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<InsuranceCompany> findByNif(String nif) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<InsuranceCompany> list = (List<InsuranceCompany>) session.createCriteria(InsuranceCompany.class)
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

    @SuppressWarnings("unchecked")
    @Override
    public List<InsuranceCompany> findByEmailLabel(String label) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<InsuranceCompany> list = (List<InsuranceCompany>) session.createCriteria(InsuranceCompany.class)
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
    public List<InsuranceCompany> findByEmailAddress(String address) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();


            List<InsuranceCompany> list = (List<InsuranceCompany>) session.createCriteria(InsuranceCompany.class)
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
    public List<InsuranceCompany> findByPhoneLabel(String label) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<InsuranceCompany> list = (List<InsuranceCompany>) session.createCriteria(InsuranceCompany.class)
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
    public List<InsuranceCompany> findByPhoneNumber(String label) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<InsuranceCompany> list = (List<InsuranceCompany>) session.createCriteria(InsuranceCompany.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                    .createAlias("phone", "p")
                    .add(Restrictions.like("p.number", "%" + label + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByPhoneNumber(label);
        } catch (SessionException e) {
            return findByPhoneNumber(label);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<InsuranceCompany> findByAddressLabel(String label) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<InsuranceCompany> list = (List<InsuranceCompany>) session.createCriteria(InsuranceCompany.class)
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
    public List<InsuranceCompany> findByAddressStreet(String street) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<InsuranceCompany> list = (List<InsuranceCompany>) session.createCriteria(InsuranceCompany.class)
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
    public List<InsuranceCompany> findByAddressCity(String city) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<InsuranceCompany> list = (List<InsuranceCompany>) session.createCriteria(InsuranceCompany.class)
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
    public List<InsuranceCompany> findByAddressPostalCode(String postalCode) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<InsuranceCompany> list = (List<InsuranceCompany>) session.createCriteria(InsuranceCompany.class)
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
    public List<InsuranceCompany> findByAddressProvince(String province) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<InsuranceCompany> list = (List<InsuranceCompany>) session.createCriteria(InsuranceCompany.class)
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
    public List<InsuranceCompany> findByAddressCountry(String country) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<InsuranceCompany> list = (List<InsuranceCompany>) session.createCriteria(InsuranceCompany.class)
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
