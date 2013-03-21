package es.regueiro.easyrepair.api.stock.finder;

import es.regueiro.easyrepair.model.stock.Warehouse;
import java.util.List;

public interface WarehouseFinder {

    public List<Warehouse> listAll();

    public List<Warehouse> listAllEnabled();

    public List<Warehouse> findByName(String name);

    public Warehouse getWarehouse(Long id);

    public List<Warehouse> findByAddressStreet(String street);

    public List<Warehouse> findByAddressCity(String city);

    public List<Warehouse> findByAddressPostalCode(String postalCode);

    public List<Warehouse> findByAddressProvince(String province);

    public List<Warehouse> findByAddressCountry(String country);

    public List<Warehouse> findByEmailAddress(String address);

    public List<Warehouse> findByPhoneNumber(String number);
}
