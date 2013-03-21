package es.regueiro.easyrepair.dao.repair.hibernate;

import es.regueiro.easyrepair.model.client.Vehicle;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class MockVehicleFinder {

    private Session session;

    public Vehicle getVehicle(Long id) {
        session = Installer.createSession();
        Transaction transaction = session.beginTransaction();
        Vehicle vehicle = (Vehicle) session.createCriteria(Vehicle.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
        transaction.commit();
        session.close();
        return vehicle;
    }
}
