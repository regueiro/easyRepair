package es.regueiro.easyrepair.dao.stock.hibernate;

import es.regueiro.easyrepair.api.stock.controller.SupplierController;
import es.regueiro.easyrepair.api.stock.finder.SupplierFinder;
import es.regueiro.easyrepair.api.stock.saver.SupplierSaver;
import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.Phone;
import es.regueiro.easyrepair.model.stock.Supplier;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = SupplierController.class,
//path = "SupplierFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.stock.DefaultSupplierFinder"}
)
public class SupplierHibernateController implements SupplierController {

    private SupplierFinder finder = Lookup.getDefault().lookup(SupplierFinder.class);
    private SupplierSaver saver = Lookup.getDefault().lookup(SupplierSaver.class);

    @Override
    public List<Supplier> listAll() {
        List<Supplier> list = finder.listAll();
        return list;
    }

    @Override
    public List<Supplier> listAllEnabled() {
        List<Supplier> list = finder.listAllEnabled();
        return list;
    }

    @Override
    public List<Supplier> searchByName(String name) {
        List<Supplier> list = finder.findByName(name);
        return list;
    }

    @Override
    public List<Supplier> searchByCategory(String category) {
        List<Supplier> list = finder.findByCategory(category);
        return list;
    }

    @Override
    public List<Supplier> searchByWeb(String web) {
        List<Supplier> list = finder.findByWeb(web);
        return list;
    }

    @Override
    public List<Supplier> searchByPaymentMethod(String paymentMethod) {
        List<Supplier> list = finder.findByPaymentMethod(paymentMethod);
        return list;
    }

    @Override
    public List<Supplier> searchByShippingMethod(String shippingMethod) {
        List<Supplier> list = finder.findByShippingMethod(shippingMethod);
        return list;
    }

    @Override
    public List<Supplier> searchByNif(String nif) {
        List<Supplier> list = finder.findByNif(nif);
        return list;
    }

    @Override
    public List<Supplier> searchByEmailLabel(String label) {
        List<Supplier> list = finder.findByEmailLabel(label);
        return list;
    }

    @Override
    public List<Supplier> searchByEmailAddress(String address) {
        List<Supplier> list = finder.findByEmailAddress(address);
        return list;
    }

    @Override
    public List<Supplier> searchByPhoneLabel(String label) {
        List<Supplier> list = finder.findByPhoneLabel(label);
        return list;
    }

    @Override
    public List<Supplier> searchByPhoneNumber(String number) {
        List<Supplier> list = finder.findByPhoneNumber(number);
        return list;
    }

    @Override
    public List<Supplier> searchByAddressLabel(String label) {
        List<Supplier> list = finder.findByAddressLabel(label);
        return list;
    }

    @Override
    public List<Supplier> searchByAddressStreet(String street) {
        List<Supplier> list = finder.findByAddressStreet(street);
        return list;
    }

    @Override
    public List<Supplier> searchByAddressCity(String city) {
        List<Supplier> list = finder.findByAddressCity(city);
        return list;
    }

    @Override
    public List<Supplier> searchByAddressPostalCode(String postalCode) {
        List<Supplier> list = finder.findByAddressPostalCode(postalCode);
        return list;
    }

    @Override
    public List<Supplier> searchByAddressProvince(String province) {
        List<Supplier> list = finder.findByAddressProvince(province);
        return list;
    }

    @Override
    public List<Supplier> searchByAddressCountry(String country) {
        List<Supplier> list = finder.findByAddressCountry(country);
        return list;
    }

    @Override
    public void setSupplier(Supplier supplier) {
        saver.setSupplier(supplier);
    }

    @Override
    public Supplier getSupplier() {
        return saver.getSupplier();
    }

    @Override
    public void saveSupplier() {
        try {
            saver.saveSupplier();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                throw new RuntimeException("The supplier was updated or deleted by another user");
            } else {
                throw e;
            }

        }
    }

    @Override
    public Supplier reloadSupplier() {
        Supplier emp = finder.getSupplier(saver.getSupplier().getId());
        return emp;
    }

    @Override
    public Supplier disableSupplier() {
        try {
            saver.disableSupplier();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setSupplier(reloadSupplier());
                disableSupplier();
            } else {
                throw e;
            }

        }
        return getSupplier();
    }

    @Override
    public Supplier enableSupplier() {
        try {
            saver.enableSupplier();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setSupplier(reloadSupplier());
                enableSupplier();
            } else {
                throw e;
            }

        }
        return getSupplier();
    }

    @Override
    public void overwriteSupplier() {
        Supplier old = getSupplier();
        Supplier newSupplier = reloadSupplier();
        if (newSupplier == null) {
            newSupplier = newSupplier();
        }
        setSupplier(newSupplier);
        newSupplier.setName(old.getName());
        newSupplier.setNif(old.getNif());
        newSupplier.setCategory(old.getCategory());
        newSupplier.setPaymentMethod(old.getPaymentMethod());
        newSupplier.setShippingMethod(old.getShippingMethod());
        newSupplier.setWeb(old.getWeb());
        newSupplier.setNotes(old.getNotes());
        newSupplier.setEnabled(old.getEnabled());


        if (old.getEmail() != null) {
            List<Email> oldList = old.getEmail();
            List<Email> newList = newSupplier.getEmail();
            List<Long> newId = new ArrayList<Long>();

            if (newList != null) {
                for (Email e : newList) {
                    newId.add(e.getId());
                }
            }
            newSupplier.clearEmail();

            for (Email e : oldList) {
                Email temp = new Email(e.getAddress(), e.getLabel());
                temp.setNotes(e.getNotes());
                if (newId.contains(e.getId())) {
                    temp.setId(e.getId());
                }
                newSupplier.addEmail(temp);
            }
        } else {
            if (newSupplier.getEmail() != null) {
                newSupplier.clearEmail();
            }
        }

        if (old.getPhone() != null) {
            List<Phone> oldList = old.getPhone();
            List<Phone> newList = newSupplier.getPhone();
            List<Long> newId = new ArrayList<Long>();

            if (newList != null) {
                for (Phone e : newList) {
                    newId.add(e.getId());
                }
            }
            newSupplier.clearPhone();

            for (Phone e : oldList) {
                Phone temp = new Phone(e.getNumber(), e.getLabel());
                temp.setNotes(e.getNotes());
                if (newId.contains(e.getId())) {
                    temp.setId(e.getId());
                }
                newSupplier.addPhone(temp);
            }
        } else {
            if (newSupplier.getPhone() != null) {
                newSupplier.clearPhone();
            }
        }

        if (old.getAddress() != null) {
            List<Address> oldList = old.getAddress();
            List<Address> newList = newSupplier.getAddress();
            List<Long> newId = new ArrayList<Long>();
            if (newList != null) {
                for (Address a : newList) {
                    newId.add(a.getId());
                }
            }
            newSupplier.clearAdddress();

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
                newSupplier.addAddress(temp);
            }
        } else {
            if (newSupplier.getAddress() != null) {
                newSupplier.clearPhone();
            }
        }

        saver.setSupplier(newSupplier);
        saver.saveSupplier();
    }

    @Override
    public Supplier newSupplier() {
        Supplier emp = new Supplier();
        return emp;
    }

    @Override
    public void deleteSupplier() {
        try {
            saver.deleteSupplier();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setSupplier(reloadSupplier());
                deleteSupplier();
            } else {
                throw e;
            }
        }
    }

    @Override
    public Supplier getSupplierById(Long id) {
        return finder.getSupplier(id);
    }
}
