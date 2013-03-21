package es.regueiro.easyrepair.api.employee.saver;

import es.regueiro.easyrepair.model.employee.Employee;

public interface EmployeeSaver {

    public void setEmployee(Employee employee);

    public Employee getEmployee();

    public void saveEmployee();

    public void disableEmployee();

    public void enableEmployee();

    public void deleteEmployee();
}
