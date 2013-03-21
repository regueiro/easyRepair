package es.regueiro.easyrepair.dao.employee.hibernate;

import es.regueiro.easyrepair.api.employee.saver.EmployeeSaver;
import es.regueiro.easyrepair.model.employee.Employee;
import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.Phone;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = EmployeeSaver.class,
//path = "EmployeeFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.employee.DefaultEmployeeSaver"}
)
public class EmployeeHibernateSaver implements EmployeeSaver {
    
    private Employee employee;
    private Transaction t;
    private Session session;
        
    @Override
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    @Override
    public Employee getEmployee() {
        return employee;
    }
    
    @Override
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
    
    @Override
    public void deleteEmployee() {
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.delete(employee);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void disableEmployee() {
        employee.setEnabled(false);
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(employee);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void enableEmployee() {
        employee.setEnabled(true);
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(employee);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }

}
