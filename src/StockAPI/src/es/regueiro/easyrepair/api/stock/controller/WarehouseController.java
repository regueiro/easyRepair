package es.regueiro.easyrepair.api.stock.controller;

import es.regueiro.easyrepair.model.stock.Warehouse;
import java.util.List;

public interface WarehouseController {

    public List<Warehouse> listAll();
    
    public List<Warehouse> listAllEnabled();

    public List<Warehouse> searchByName(String name);

    public List<Warehouse> searchByAddressStreet(String street);

    public List<Warehouse> searchByAddressCity(String city);

    public List<Warehouse> searchByAddressPostalCode(String postalCode);

    public List<Warehouse> searchByAddressProvince(String province);

    public List<Warehouse> searchByAddressCountry(String country);

    public List<Warehouse> searchByEmailAddress(String address);

    public List<Warehouse> searchByPhoneNumber(String number);

    public void setWarehouse(Warehouse warehouse);

    public Warehouse getWarehouse();

    public void saveWarehouse();

    public Warehouse reloadWarehouse();

    public Warehouse disableWarehouse();

    public Warehouse enableWarehouse();

    public void overwriteWarehouse();

    public void deleteWarehouse();

    public Warehouse newWarehouse();

    public Warehouse getWarehouseById(Long id);
}
