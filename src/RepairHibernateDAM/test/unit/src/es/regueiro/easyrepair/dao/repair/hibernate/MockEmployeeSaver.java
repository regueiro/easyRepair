package es.regueiro.easyrepair.dao.repair.hibernate;

import es.regueiro.easyrepair.model.employee.Employee;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MockEmployeeSaver {

    private Employee employee;
    private Transaction t;
    private Session session;

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void saveEmployee() {
        boolean idSetAuto = false;
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(employee);
            if (employee.getEmployeeId() == null) {
                employee.setEmployeeId(employee.getId().toString());
                idSetAuto = true;
            }
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            if (idSetAuto) {
                employee.setEmployeeId(null);
            }
            session.close();
            throw e;
        }
    }
}
