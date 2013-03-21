package es.regueiro.easyrepair.dao.client.hibernate;

import es.regueiro.easyrepair.api.client.controller.InsuranceCompanyController;
import es.regueiro.easyrepair.api.client.finder.InsuranceCompanyFinder;
import es.regueiro.easyrepair.api.client.saver.InsuranceCompanySaver;
import es.regueiro.easyrepair.model.client.Client;
import es.regueiro.easyrepair.model.client.InsuranceCompany;
import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.Phone;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = InsuranceCompanyController.class,
//path = "InsuranceCompanyFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.client.DefaultInsuranceCompanyFinder"}
)
public class InsuranceCompanyHibernateController implements InsuranceCompanyController {

    private InsuranceCompanyFinder finder = Lookup.getDefault().lookup(InsuranceCompanyFinder.class);
    private InsuranceCompanySaver saver = Lookup.getDefault().lookup(InsuranceCompanySaver.class);

    @Override
    public List<InsuranceCompany> listAll() {
        List<InsuranceCompany> list = finder.listAll();
        return list;
    }
    
    @Override
    public List<InsuranceCompany> listAllEnabled() {
        List<InsuranceCompany> list = finder.listAllEnabled();
        return list;
    }

    @Override
    public void setInsuranceCompany(InsuranceCompany insuranceCompany) {
        saver.setInsuranceCompany(insuranceCompany);
    }

    @Override
    public InsuranceCompany getInsuranceCompany() {
        return saver.getInsuranceCompany();
    }

    @Override
    public void saveInsuranceCompany() {
        try {
            saver.saveInsuranceCompany();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                throw new RuntimeException("The Insurance Company was updated or deleted by another user");
            } else {
                throw e;
            }

        }
    }

    @Override
    public InsuranceCompany reloadInsuranceCompany() {
        InsuranceCompany emp = finder.getInsuranceCompany(saver.getInsuranceCompany().getId());
        return emp;
    }

    @Override
    public InsuranceCompany disableInsuranceCompany() {
        try {
            saver.disableInsuranceCompany();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setInsuranceCompany(reloadInsuranceCompany());
                disableInsuranceCompany();
            } else {
                throw e;
            }

        }
        return getInsuranceCompany();
    }

    @Override
    public InsuranceCompany enableInsuranceCompany() {
        try {
            saver.enableInsuranceCompany();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setInsuranceCompany(reloadInsuranceCompany());
                enableInsuranceCompany();
            } else {
                throw e;
            }

        }
        return getInsuranceCompany();
    }

    @Override
    public void overwriteInsuranceCompany() {
        InsuranceCompany old = getInsuranceCompany();
        InsuranceCompany newInsuranceCompany = reloadInsuranceCompany();
        if (newInsuranceCompany == null) {
            newInsuranceCompany = newInsuranceCompany();
        }

        setInsuranceCompany(newInsuranceCompany);
        newInsuranceCompany.setName(old.getName());
        newInsuranceCompany.setNif(old.getNif());
        newInsuranceCompany.setNotes(old.getNotes());
        newInsuranceCompany.setEnabled(old.getEnabled());

         
        if (old.getEmail() != null) {
            List<Email> oldList = old.getEmail();
            List<Email> newList = newInsuranceCompany.getEmail();
            List<Long> newId = new ArrayList<Long>();

            if (newList != null) {
                for (Email e : newList) {
                    newId.add(e.getId());
                }
            }
            newInsuranceCompany.clearEmail();

            for (Email e : oldList) {
                Email temp = new Email(e.getAddress(), e.getLabel());
                temp.setNotes(e.getNotes());
                if (newId.contains(e.getId())) {
                    temp.setId(e.getId());
                }
                newInsuranceCompany.addEmail(temp);
            }
        } else {
            if (newInsuranceCompany.getEmail() != null) {
                newInsuranceCompany.clearEmail();
            }
        }

        if (old.getPhone() != null) {
            List<Phone> oldList = old.getPhone();
            List<Phone> newList = newInsuranceCompany.getPhone();
            List<Long> newId = new ArrayList<Long>();

            if (newList != null) {
                for (Phone e : newList) {
                    newId.add(e.getId());
                }
            }
            newInsuranceCompany.clearPhone();

            for (Phone e : oldList) {
                Phone temp = new Phone(e.getNumber(), e.getLabel());
                temp.setNotes(e.getNotes());
                if (newId.contains(e.getId())) {
                    temp.setId(e.getId());
                }
                newInsuranceCompany.addPhone(temp);
            }
        } else {
            if (newInsuranceCompany.getPhone() != null) {
                newInsuranceCompany.clearPhone();
            }
        }

        if (old.getAddress() != null) {
            List<Address> oldList = old.getAddress();
            List<Address> newList = newInsuranceCompany.getAddress();
            List<Long> newId = new ArrayList<Long>();
            if (newList != null) {
                for (Address a : newList) {
                    newId.add(a.getId());
                }
            }
            newInsuranceCompany.clearAdddress();

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
                newInsuranceCompany.addAddress(temp);
            }
        } else {
            if (newInsuranceCompany.getAddress() != null) {
                newInsuranceCompany.clearPhone();
            }
        }

        saver.setInsuranceCompany(newInsuranceCompany);
        saver.saveInsuranceCompany();
    }

    @Override
    public InsuranceCompany newInsuranceCompany() {
        InsuranceCompany emp = new InsuranceCompany();
        return emp;
    }

    @Override
    public void deleteInsuranceCompany() {
        try {
            saver.deleteInsuranceCompany();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setInsuranceCompany(reloadInsuranceCompany());
                deleteInsuranceCompany();
            } else {
                throw e;
            }
        }
    }

    @Override
    public InsuranceCompany getInsuranceCompanyById(Long id) {
        return finder.getInsuranceCompany(id);
    }

    @Override
    public List<InsuranceCompany> searchByName(String name) {
        List<InsuranceCompany> list = finder.findByName(name);
        return list;
    }

    @Override
    public List<InsuranceCompany> searchByNif(String nif) {
        List<InsuranceCompany> list = finder.findByNif(nif);
        return list;
    }

    @Override
    public List<InsuranceCompany> searchByWeb(String web) {
        List<InsuranceCompany> list = finder.findByWeb(web);
        return list;
    }

    @Override
    public List<InsuranceCompany> searchByEmailLabel(String label) {
        List<InsuranceCompany> list = finder.findByEmailLabel(label);
        return list;
    }

    @Override
    public List<InsuranceCompany> searchByEmailAddress(String address) {
        List<InsuranceCompany> list = finder.findByEmailAddress(address);
        return list;
    }

    @Override
    public List<InsuranceCompany> searchByPhoneLabel(String label) {
        List<InsuranceCompany> list = finder.findByPhoneLabel(label);
        return list;
    }

    @Override
    public List<InsuranceCompany> searchByPhoneNumber(String number) {
        List<InsuranceCompany> list = finder.findByPhoneNumber(number);
        return list;
    }

    @Override
    public List<InsuranceCompany> searchByAddressLabel(String label) {
        List<InsuranceCompany> list = finder.findByAddressLabel(label);
        return list;
    }

    @Override
    public List<InsuranceCompany> searchByAddressStreet(String street) {
        List<InsuranceCompany> list = finder.findByAddressStreet(street);
        return list;
    }

    @Override
    public List<InsuranceCompany> searchByAddressCity(String city) {
        List<InsuranceCompany> list = finder.findByAddressCity(city);
        return list;
    }

    @Override
    public List<InsuranceCompany> searchByAddressPostalCode(String postalCode) {
        List<InsuranceCompany> list = finder.findByAddressPostalCode(postalCode);
        return list;
    }

    @Override
    public List<InsuranceCompany> searchByAddressProvince(String province) {
        List<InsuranceCompany> list = finder.findByAddressProvince(province);
        return list;
    }

    @Override
    public List<InsuranceCompany> searchByAddressCountry(String country) {
        List<InsuranceCompany> list = finder.findByAddressCountry(country);
        return list;
    }

}
