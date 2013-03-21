package es.regueiro.easyrepair.dao.stock.hibernate;

import es.regueiro.easyrepair.api.stock.controller.WarehouseController;
import es.regueiro.easyrepair.api.stock.finder.WarehouseFinder;
import es.regueiro.easyrepair.api.stock.saver.WarehouseSaver;
import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.Phone;
import es.regueiro.easyrepair.model.stock.Warehouse;
import java.util.List;
import org.hibernate.HibernateException;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = WarehouseController.class,
//path = "WarehouseControllerServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.stock.DefaultWarehouseController"}
)
public class WarehouseHibernateController implements WarehouseController {

    private WarehouseFinder finder = Lookup.getDefault().lookup(WarehouseFinder.class);
    private WarehouseSaver saver = Lookup.getDefault().lookup(WarehouseSaver.class);

    @Override
    public List<Warehouse> listAll() {
        List<Warehouse> list = finder.listAll();
        return list;
    }

    @Override
    public List<Warehouse> listAllEnabled() {
        List<Warehouse> list = finder.listAllEnabled();
        return list;
    }

    @Override
    public List<Warehouse> searchByName(String name) {
        List<Warehouse> list = finder.findByName(name);
        return list;
    }

    @Override
    public List<Warehouse> searchByAddressStreet(String street) {
        List<Warehouse> list = finder.findByAddressStreet(street);
        return list;
    }

    @Override
    public List<Warehouse> searchByAddressCity(String city) {
        List<Warehouse> list = finder.findByAddressCity(city);
        return list;
    }

    @Override
    public List<Warehouse> searchByAddressPostalCode(String postalCode) {
        List<Warehouse> list = finder.findByAddressPostalCode(postalCode);
        return list;
    }

    @Override
    public List<Warehouse> searchByAddressProvince(String province) {
        List<Warehouse> list = finder.findByAddressProvince(province);
        return list;
    }

    @Override
    public List<Warehouse> searchByAddressCountry(String country) {
        List<Warehouse> list = finder.findByAddressCountry(country);
        return list;
    }

    @Override
    public List<Warehouse> searchByEmailAddress(String address) {
        List<Warehouse> list = finder.findByEmailAddress(address);
        return list;
    }

    @Override
    public List<Warehouse> searchByPhoneNumber(String number) {
        List<Warehouse> list = finder.findByPhoneNumber(number);
        return list;
    }

    @Override
    public void setWarehouse(Warehouse warehouse) {
        saver.setWarehouse(warehouse);
    }

    @Override
    public Warehouse getWarehouse() {
        return saver.getWarehouse();
    }

    @Override
    public void saveWarehouse() {
        try {
            saver.saveWarehouse();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                throw new RuntimeException("The warehouse was updated or deleted by another user");
            } else {
                throw e;
            }

        }
    }

    @Override
    public Warehouse reloadWarehouse() {
        Warehouse wa = finder.getWarehouse(saver.getWarehouse().getId());
        return wa;
    }


    @Override
    public Warehouse disableWarehouse() {
        try {
            saver.disableWarehouse();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setWarehouse(reloadWarehouse());
                disableWarehouse();
            } else {
                throw e;
            }

        }
        return getWarehouse();
    }

    @Override
    public Warehouse enableWarehouse() {
        try {
            saver.enableWarehouse();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setWarehouse(reloadWarehouse());
                enableWarehouse();
            } else {
                throw e;
            }

        }
        return getWarehouse();
    }

    @Override
    public void overwriteWarehouse() {
        Warehouse old = getWarehouse();
        Warehouse newWarehouse = reloadWarehouse();
        if (newWarehouse == null) {
            newWarehouse = newWarehouse();
        }

        newWarehouse.setName(old.getName());
        newWarehouse.setNotes(old.getNotes());
        newWarehouse.setEnabled(old.getEnabled());


        if (old.getEmail() != null) {
            Email em = new Email(old.getEmail().getAddress(), old.getEmail().getLabel());
            em.setNotes(old.getEmail().getNotes());

            newWarehouse.setEmail(em);
        } else {
            newWarehouse.setEmail(null);
        }

        if (old.getPhone() != null) {
            Phone em = new Phone(old.getPhone().getNumber(), old.getPhone().getLabel());
            em.setNotes(old.getPhone().getNotes());

            newWarehouse.setPhone(em);
        } else {
            newWarehouse.setPhone(null);
        }

        if (old.getAddress() != null) {
            Address ad = new Address();
            ad.setLabel(old.getAddress().getLabel());
            ad.setCity(old.getAddress().getCity());
            ad.setCountry(old.getAddress().getCountry());
            ad.setNotes(old.getAddress().getNotes());
            ad.setPostalCode(old.getAddress().getPostalCode());
            ad.setProvince(old.getAddress().getProvince());
            ad.setStreet(old.getAddress().getStreet());

            newWarehouse.setAddress(ad);
        } else {
            newWarehouse.setAddress(null);
        }

        saver.setWarehouse(newWarehouse);
        saver.saveWarehouse();
    }

    @Override
    public Warehouse newWarehouse() {
        Warehouse emp = new Warehouse();
        return emp;
    }

    @Override
    public void deleteWarehouse() {
        try {
            saver.deleteWarehouse();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setWarehouse(reloadWarehouse());
                deleteWarehouse();
            } else {
                throw e;
            }
        }
    }

    @Override
    public Warehouse getWarehouseById(Long id) {
        return finder.getWarehouse(id);
    }
}
