package es.regueiro.easyrepair.api.employee.controller;

import es.regueiro.easyrepair.model.employee.Employee;
import java.util.List;

public interface EmployeeController {

    public List<Employee> listAll();

    public List<Employee> listAllEnabled();

    public List<Employee> searchByName(String name);

    public List<Employee> searchBySurname(String surname);

    public List<Employee> searchByEmployeeId(String id);

    public List<Employee> searchByNif(String nif);

    public List<Employee> searchByNss(String nss);

    public List<Employee> searchByOccupation(String occupation);

    public List<Employee> searchByEmailLabel(String label);

    public List<Employee> searchByEmailAddress(String address);

    public List<Employee> searchByPhoneLabel(String label);

    public List<Employee> searchByPhoneNumber(String number);

    public List<Employee> searchByAddressLabel(String label);

    public List<Employee> searchByAddressStreet(String street);

    public List<Employee> searchByAddressCity(String city);

    public List<Employee> searchByAddressPostalCode(String postalCode);

    public List<Employee> searchByAddressProvince(String province);

    public List<Employee> searchByAddressCountry(String country);

    public void setEmployee(Employee employee);

    public Employee getEmployee();

    public void saveEmployee();

    public Employee reloadEmployee();

    public Employee disableEmployee();

    public Employee enableEmployee();

    public void overwriteEmployee();

    public void deleteEmployee();

    public Employee newEmployee();

    public Employee getEmployeeById(Long id);
}
