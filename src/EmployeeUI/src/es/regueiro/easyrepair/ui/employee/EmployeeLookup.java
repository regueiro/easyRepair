package es.regueiro.easyrepair.ui.employee;

import es.regueiro.easyrepair.model.employee.Employee;
import java.util.Collection;
import java.util.Iterator;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class EmployeeLookup extends AbstractLookup {

    private static EmployeeLookup Lookup = new EmployeeLookup();
    private InstanceContent content = null;

    private EmployeeLookup() {
        this(new InstanceContent());
    }

    private EmployeeLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static EmployeeLookup getDefault() {
        return Lookup;
    }

    public synchronized void setEmployee(Employee employee) {
        clear();
        add(employee);

    }

    public synchronized void clear() {
        Collection<? extends Employee> all =
                lookupAll(Employee.class);
        if (all != null) {
            Iterator<? extends Employee> ia = all.iterator();
            while (ia.hasNext()) {
                remove(ia.next());
            }
        }
    }
    
    public synchronized Long getEmployeeId() {
        Employee emp = null;
        Collection<? extends Employee> all =
                lookupAll(Employee.class);
        if (all != null) {
            Iterator<? extends Employee> ia = all.iterator();
            while (ia.hasNext()) {
                emp = ia.next();
            }
        }
        if (emp != null) {
            return emp.getId();
        } else {
            return null;
        }
    }
}
