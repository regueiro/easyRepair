package es.regueiro.easyrepair.api.stock.controller;

import es.regueiro.easyrepair.model.stock.Supplier;
import java.util.List;

public interface SupplierController {

    public List<Supplier> listAll();
    
    public List<Supplier> listAllEnabled();

    public List<Supplier> searchByName(String name);

    public List<Supplier> searchByCategory(String category);

    public List<Supplier> searchByWeb(String web);

    public List<Supplier> searchByPaymentMethod(String paymentMethod);

    public List<Supplier> searchByShippingMethod(String shippingMethod);

    public List<Supplier> searchByNif(String nif);

    public List<Supplier> searchByEmailLabel(String label);

    public List<Supplier> searchByEmailAddress(String address);

    public List<Supplier> searchByPhoneLabel(String label);

    public List<Supplier> searchByPhoneNumber(String number);

    public List<Supplier> searchByAddressLabel(String label);

    public List<Supplier> searchByAddressStreet(String street);

    public List<Supplier> searchByAddressCity(String city);

    public List<Supplier> searchByAddressPostalCode(String postalCode);

    public List<Supplier> searchByAddressProvince(String province);

    public List<Supplier> searchByAddressCountry(String country);

    public void setSupplier(Supplier supplier);

    public Supplier getSupplier();

    public void saveSupplier();

    public Supplier reloadSupplier();

    public Supplier disableSupplier();

    public Supplier enableSupplier();

    public void overwriteSupplier();

    public void deleteSupplier();

    public Supplier newSupplier();

    public Supplier getSupplierById(Long id);
}
