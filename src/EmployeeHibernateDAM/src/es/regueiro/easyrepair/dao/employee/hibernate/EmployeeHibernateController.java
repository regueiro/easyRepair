package es.regueiro.easyrepair.dao.employee.hibernate;

import es.regueiro.easyrepair.api.employee.controller.EmployeeController;
import es.regueiro.easyrepair.model.employee.Employee;
import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.Phone;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = EmployeeController.class,
//path = "EmployeeFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.employee.DefaultEmployeeFinder"}
)
public class EmployeeHibernateController implements EmployeeController {

    private EmployeeHibernateFinder finder = new EmployeeHibernateFinder();
    private EmployeeHibernateSaver saver = new  EmployeeHibernateSaver();

    @Override
    public List<Employee> listAll() {
        List<Employee> list = finder.listAll();
        return list;
    }
    
    @Override
    public List<Employee> listAllEnabled() {
        List<Employee> list = finder.listAllEnabled();
        return list;
    }

    @Override
    public List<Employee> searchByName(String name) {
        List<Employee> list = finder.findByName(name);
        return list;
    }

    @Override
    public List<Employee> searchBySurname(String surname) {
        List<Employee> list = finder.findBySurname(surname);
        return list;
    }

    @Override
    public List<Employee> searchByEmployeeId(String id) {
        List<Employee> list = finder.findByEmployeeId(id);
        return list;
    }

    @Override
    public List<Employee> searchByNif(String nif) {
        List<Employee> list = finder.findByNif(nif);
        return list;
    }

    @Override
    public List<Employee> searchByNss(String nss) {
        List<Employee> list = finder.findByNss(nss);
        return list;
    }

    @Override
    public List<Employee> searchByOccupation(String occupation) {
        List<Employee> list = finder.findByOccupation(occupation);
        return list;
    }

    @Override
    public List<Employee> searchByEmailLabel(String label) {
        List<Employee> list = finder.findByEmailLabel(label);
        return list;
    }

    @Override
    public List<Employee> searchByEmailAddress(String address) {
        List<Employee> list = finder.findByEmailAddress(address);
        return list;
    }

    @Override
    public List<Employee> searchByPhoneLabel(String label) {
        List<Employee> list = finder.findByPhoneLabel(label);
        return list;
    }

    @Override
    public List<Employee> searchByPhoneNumber(String number) {
        List<Employee> list = finder.findByPhoneNumber(number);
        return list;
    }

    @Override
    public List<Employee> searchByAddressLabel(String label) {
        List<Employee> list = finder.findByAddressLabel(label);
        return list;
    }

    @Override
    public List<Employee> searchByAddressStreet(String street) {
        List<Employee> list = finder.findByAddressStreet(street);
        return list;
    }

    @Override
    public List<Employee> searchByAddressCity(String city) {
        List<Employee> list = finder.findByAddressCity(city);
        return list;
    }

    @Override
    public List<Employee> searchByAddressPostalCode(String postalCode) {
        List<Employee> list = finder.findByAddressPostalCode(postalCode);
        return list;
    }

    @Override
    public List<Employee> searchByAddressProvince(String province) {
        List<Employee> list = finder.findByAddressProvince(province);
        return list;
    }

    @Override
    public List<Employee> searchByAddressCountry(String country) {
        List<Employee> list = finder.findByAddressCountry(country);
        return list;
    }

    @Override
    public void setEmployee(Employee employee) {
        saver.setEmployee(employee);
    }

    @Override
    public Employee getEmployee() {
        return saver.getEmployee();
    }

    @Override
    public void saveEmployee() {
        try {
            saver.saveEmployee();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                throw new RuntimeException("The employee was updated or deleted by another user");
            } else {
                throw e;
            }

        }
    }

    @Override
    public Employee reloadEmployee() {
        Employee emp = finder.getEmployee(saver.getEmployee().getId());
        return emp;
    }

    @Override
    public Employee disableEmployee() {
        try {
            saver.disableEmployee();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setEmployee(reloadEmployee());
                disableEmployee();
            } else {
                throw e;
            }

        }
        return getEmployee();
    }

    @Override
    public Employee enableEmployee() {
        try {
            saver.enableEmployee();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setEmployee(reloadEmployee());
                enableEmployee();
            } else {
                throw e;
            }

        }
        return getEmployee();
    }
   
    @Override
    public void overwriteEmployee() {
        Employee old = getEmployee();
        Employee newEmp = reloadEmployee();
        if (newEmp == null) {
            newEmp = newEmployee();
        }
        setEmployee(newEmp);
        newEmp.setEmployeeId(old.getEmployeeId());
        newEmp.setName(old.getName());
        newEmp.setSurname(old.getSurname());
        newEmp.setNif(old.getNif());
        newEmp.setNss(old.getNss());
        newEmp.setOccupation(old.getOccupation());
        newEmp.setNotes(old.getNotes());
        newEmp.setEnabled(old.getEnabled());

        if (old.getEmail() != null) {
            List<Email> oldList = old.getEmail();
            List<Email> newList = newEmp.getEmail();
            List<Long> newId = new ArrayList<Long>();

            if (newList != null) {
                for (Email e : newList) {
                    newId.add(e.getId());
                }
            }
            newEmp.clearEmail();

            for (Email e : oldList) {
                Email temp = new Email(e.getAddress(), e.getLabel());
                temp.setNotes(e.getNotes());
                if (newId.contains(e.getId())) {
                    temp.setId(e.getId());
                }
                newEmp.addEmail(temp);
            }
        } else {
            if (newEmp.getEmail() != null) {
               newEmp.clearEmail();
            }
        }

        if (old.getPhone() != null) {
            List<Phone> oldList = old.getPhone();
            List<Phone> newList = newEmp.getPhone();
            List<Long> newId = new ArrayList<Long>();

            if (newList != null) {
                for (Phone e : newList) {
                    newId.add(e.getId());
                }
            }
            newEmp.clearPhone();

            for (Phone e : oldList) {
                Phone temp = new Phone(e.getNumber(), e.getLabel());
                temp.setNotes(e.getNotes());
                if (newId.contains(e.getId())) {
                    temp.setId(e.getId());
                }
                newEmp.addPhone(temp);
            }
        } else {
            if (newEmp.getPhone() != null) {
                newEmp.clearPhone();
            }
        }

        if (old.getAddress() != null) {
            List<Address> oldList = old.getAddress();
            List<Address> newList = newEmp.getAddress();
            List<Long> newId = new ArrayList<Long>();
            if (newList != null) {
                for (Address a : newList) {
                    newId.add(a.getId());
                }
            }
            newEmp.clearAdddress();

            for (Address a : oldList) {
                Address temp = new Address();
                temp.setLabel(a.getLabel());
                temp.setStreet(a.getStreet());
                temp.setCity(a.getCity());
                temp.setCountry(a.getCountry());
                temp.setPostalCode(a.getPostalCode());
                temp.setProvince(a.getProvince());
                temp.setNotes(a.getNotes());

                if (newId.contains(a.getId())) {
                    temp.setId(a.getId());
                }
                newEmp.addAddress(temp);
            }
        } else {
            if (newEmp.getAddress() != null) {
                newEmp.clearPhone();
            }
        }

        saver.setEmployee(newEmp);
        saver.saveEmployee();
    }

    @Override
    public Employee newEmployee() {
        Employee emp = new Employee();
        return emp;
    }

    @Override
    public void deleteEmployee() {
        try {
            saver.deleteEmployee();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setEmployee(reloadEmployee());
                deleteEmployee();
            } else {
                throw e;
            }
        }
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return finder.getEmployee(id);
    }
}
