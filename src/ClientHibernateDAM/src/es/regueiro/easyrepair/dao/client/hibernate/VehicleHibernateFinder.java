package es.regueiro.easyrepair.dao.client.hibernate;

import es.regueiro.easyrepair.api.client.finder.VehicleFinder;
import es.regueiro.easyrepair.model.client.Vehicle;
import java.util.List;
import org.hibernate.ResourceClosedException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = VehicleFinder.class,
//path = "VehicleFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.vehicle.DefaultVehicleFinder"}
)
public class VehicleHibernateFinder implements VehicleFinder {

    private Session session;

    @SuppressWarnings("unchecked")
    @Override
    public List<Vehicle> listAll() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Vehicle> list = (List<Vehicle>) session.createCriteria(Vehicle.class).list();
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
    public List<Vehicle> listAllEnabled() {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Vehicle> list = (List<Vehicle>) session.createCriteria(Vehicle.class)
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
    public Vehicle getVehicle(Long id) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            Vehicle vehicle = (Vehicle) session.createCriteria(Vehicle.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return vehicle;
        } catch (ResourceClosedException e) {
            return getVehicle(id);
        } catch (SessionException e) {
            return getVehicle(id);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vehicle> findByRegistration(String registration) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Vehicle> list = (List<Vehicle>) session.createCriteria(Vehicle.class)
                    .add(Restrictions.isNotNull("registration"))
                    .add(Restrictions.like("registration", "%" + registration + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByRegistration(registration);
        } catch (SessionException e) {
            return findByRegistration(registration);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vehicle> findByVin(String vin) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Vehicle> list = (List<Vehicle>) session.createCriteria(Vehicle.class)
                    .add(Restrictions.isNotNull("vin"))
                    .add(Restrictions.like("vin", "%" + vin + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByVin(vin);
        } catch (SessionException e) {
            return findByVin(vin);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vehicle> findByMake(String make) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Vehicle> list = (List<Vehicle>) session.createCriteria(Vehicle.class)
                    .add(Restrictions.isNotNull("make"))
                    .add(Restrictions.like("make", "%" + make + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByMake(make);
        } catch (SessionException e) {
            return findByMake(make);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vehicle> findByModel(String model) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Vehicle> list = (List<Vehicle>) session.createCriteria(Vehicle.class)
                    .add(Restrictions.isNotNull("model"))
                    .add(Restrictions.like("model", "%" + model + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByModel(model);
        } catch (SessionException e) {
            return findByModel(model);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vehicle> findByYear(String year) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Vehicle> list = (List<Vehicle>) session.createCriteria(Vehicle.class)
                    .add(Restrictions.isNotNull("year"))
                    .add(Restrictions.like("year", "%" + year + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByYear(year);
        } catch (SessionException e) {
            return findByYear(year);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vehicle> findByColour(String colour) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Vehicle> list = (List<Vehicle>) session.createCriteria(Vehicle.class)
                    .add(Restrictions.isNotNull("colour"))
                    .add(Restrictions.like("colour", "%" + colour + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByColour(colour);
        } catch (SessionException e) {
            return findByColour(colour);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vehicle> findByType(String type) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Vehicle> list = (List<Vehicle>) session.createCriteria(Vehicle.class)
                    .add(Restrictions.isNotNull("type"))
                    .add(Restrictions.like("type", "%" + type + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByType(type);
        } catch (SessionException e) {
            return findByType(type);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vehicle> findByFuel(String fuel) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Vehicle> list = (List<Vehicle>) session.createCriteria(Vehicle.class)
                    .add(Restrictions.isNotNull("fuel"))
                    .add(Restrictions.like("fuel", "%" + fuel + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByFuel(fuel);
        } catch (SessionException e) {
            return findByFuel(fuel);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vehicle> findByInsuranceNumber(String insuranceNumber) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Vehicle> list = (List<Vehicle>) session.createCriteria(Vehicle.class)
                    .add(Restrictions.isNotNull("insuranceNumber"))
                    .add(Restrictions.like("insuranceNumber", "%" + insuranceNumber + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByInsuranceNumber(insuranceNumber);
        } catch (SessionException e) {
            return findByInsuranceNumber(insuranceNumber);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vehicle> findByInsuranceCompanyName(String insuranceCompany) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Vehicle> list = (List<Vehicle>) session.createCriteria(Vehicle.class)
                    .createAlias("insuranceCompany", "ins")
                    .add(Restrictions.isNotNull("ins.name"))
                    .add(Restrictions.like("ins.name", "%" + insuranceCompany + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByInsuranceCompanyName(insuranceCompany);
        } catch (SessionException e) {
            return findByInsuranceCompanyName(insuranceCompany);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vehicle> findByOwnerName(String ownerName) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Vehicle> list = (List<Vehicle>) session.createCriteria(Vehicle.class)
                    .createAlias("owner", "own")
                    .add(Restrictions.isNotNull("own.name"))
                    .add(Restrictions.like("own.name", "%" + ownerName + "%"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByOwnerName(ownerName);
        } catch (SessionException e) {
            return findByOwnerName(ownerName);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vehicle> findByOwnerNif(String ownerNif) {
        try {
            session = Installer.createSession();
            Transaction transaction = session.beginTransaction();
            List<Vehicle> list = (List<Vehicle>) session.createCriteria(Vehicle.class, "vehicle")
                    .createCriteria("vehicle.owner", "owner")
                    .add(Restrictions.isNotNull("owner.nif"))
                    .add(Restrictions.sqlRestriction("{alias}.nif like '%" + ownerNif + "%'"))
                    .list();
            transaction.commit();
            if (session.isOpen()) {
                session.close();
            }
            return list;
        } catch (ResourceClosedException e) {
            return findByOwnerNif(ownerNif);
        } catch (SessionException e) {
            return findByOwnerNif(ownerNif);
        }
    }
}
