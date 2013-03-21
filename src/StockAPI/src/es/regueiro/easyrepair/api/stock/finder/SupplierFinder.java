package es.regueiro.easyrepair.api.stock.finder;

import es.regueiro.easyrepair.model.stock.Supplier;
import java.util.List;

public interface SupplierFinder {

    public List<Supplier> listAll();
    
    public List<Supplier> listAllEnabled();

    public List<Supplier> findByName(String name);

    public List<Supplier> findByCategory(String category);

    public List<Supplier> findByWeb(String web);

    public List<Supplier> findByPaymentMethod(String paymentMethod);

    public List<Supplier> findByShippingMethod(String shippingMethod);

    public List<Supplier> findByNif(String nif);

    public Supplier getSupplier(Long id);

    public List<Supplier> findByEmailLabel(String label);

    public List<Supplier> findByEmailAddress(String address);

    public List<Supplier> findByPhoneLabel(String label);

    public List<Supplier> findByPhoneNumber(String number);

    public List<Supplier> findByAddressLabel(String label);

    public List<Supplier> findByAddressStreet(String street);

    public List<Supplier> findByAddressCity(String city);

    public List<Supplier> findByAddressPostalCode(String postalCode);

    public List<Supplier> findByAddressProvince(String province);

    public List<Supplier> findByAddressCountry(String country);
}
