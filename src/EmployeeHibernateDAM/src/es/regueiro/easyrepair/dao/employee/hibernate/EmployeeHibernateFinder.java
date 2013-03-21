package es.regueiro.easyrepair.dao.employee.hibernate;

import es.regueiro.easyrepair.api.employee.finder.EmployeeFinder;
import es.regueiro.easyrepair.model.employee.Employee;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.ResourceClosedException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = EmployeeFinder.class,
//path = "EmployeeFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.employee.DefaultEmployeeFinder"}
)
public class EmployeeHibernateFinder implements EmployeeFinder {

    private Session session;

    @SuppressWarnings("unchecked")
    @Override
    public List<Employee> listAll() {
        try {
            session = Installer.createSession();

            Transaction transaction = session.beginTransaction();
            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class).list();
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
    public List<Employee> listAllEnabled() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class)
                    .add(Restrictions.isNotNull("enabled"))
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
    public List<Employee> findByName(String name) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class)
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
    public List<Employee> findBySurname(String surname) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class)
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
    public List<Employee> findByEmployeeId(String id) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class)
                    .add(Restrictions.isNotNull("employeeId"))
                    .add(Restrictions.sqlRestriction("employeeId like '%" + id + "%'"))
                    .list();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByEmployeeId(id);
        } catch (SessionException e) {
            return findByEmployeeId(id);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Employee> findByNif(String nif) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class)
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
    public List<Employee> findByNss(String nss) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class)
                    .add(Restrictions.sqlRestriction("nss like '%" + nss + "%'"))
                    .list();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByNss(nss);
        } catch (SessionException e) {
            return findByNss(nss);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Employee> findByOccupation(String occupation) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class)
                    .add(Restrictions.like("occupation", "%" + occupation + "%"))
                    .list();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByOccupation(occupation);
        } catch (SessionException e) {
            return findByOccupation(occupation);
        }
    }

    @Override
    public Employee getEmployee(Long id) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            Employee employee = (Employee) session.createCriteria(Employee.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();
            transaction.commit();

            if (session.isOpen()) {
                session.close();
            }
            return employee;
        } catch (ResourceClosedException e) {
            return getEmployee(id);
        } catch (SessionException e) {
            return getEmployee(id);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Employee> findByEmailLabel(String label) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class)
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
    public List<Employee> findByEmailAddress(String address) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();


            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class)
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
    public List<Employee> findByPhoneLabel(String label) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class)
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
    public List<Employee> findByPhoneNumber(String label) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class)
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
    public List<Employee> findByAddressLabel(String label) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class)
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
    public List<Employee> findByAddressStreet(String street) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class)
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
    public List<Employee> findByAddressCity(String city) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class)
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
    public List<Employee> findByAddressPostalCode(String postalCode) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class)
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
    public List<Employee> findByAddressProvince(String province) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class)
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
    public List<Employee> findByAddressCountry(String country) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Employee> list = (List<Employee>) session.createCriteria(Employee.class)
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
