package es.regueiro.easyrepair.api.employee.finder;

import es.regueiro.easyrepair.model.employee.Employee;
import java.util.List;

public interface EmployeeFinder {

    public List<Employee> listAll();
    
    public List<Employee> listAllEnabled();

    public List<Employee> findByName(String name);

    public List<Employee> findBySurname(String surname);

    public List<Employee> findByEmployeeId(String id);

    public List<Employee> findByNif(String nif);

    public List<Employee> findByNss(String nss);

    public List<Employee> findByOccupation(String occupation);

    public List<Employee> findByEmailLabel(String label);

    public List<Employee> findByEmailAddress(String address);

    public List<Employee> findByPhoneLabel(String label);

    public List<Employee> findByPhoneNumber(String number);

    public List<Employee> findByAddressLabel(String label);

    public List<Employee> findByAddressStreet(String street);

    public List<Employee> findByAddressCity(String city);

    public List<Employee> findByAddressPostalCode(String postalCode);

    public List<Employee> findByAddressProvince(String province);

    public List<Employee> findByAddressCountry(String country);

    public Employee getEmployee(Long id);
}
