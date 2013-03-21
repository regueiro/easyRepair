package es.regueiro.easyrepair.api.client.finder;

import es.regueiro.easyrepair.model.client.InsuranceCompany;
import java.util.List;

public interface InsuranceCompanyFinder {

    public List<InsuranceCompany> listAll();
    
    public List<InsuranceCompany> listAllEnabled();

    public List<InsuranceCompany> findByName(String name);

    public List<InsuranceCompany> findByWeb(String web);

    public List<InsuranceCompany> findByNif(String nif);

    public InsuranceCompany getInsuranceCompany(Long id);

    public List<InsuranceCompany> findByEmailLabel(String label);

    public List<InsuranceCompany> findByEmailAddress(String address);

    public List<InsuranceCompany> findByPhoneLabel(String label);

    public List<InsuranceCompany> findByPhoneNumber(String number);

    public List<InsuranceCompany> findByAddressLabel(String label);

    public List<InsuranceCompany> findByAddressStreet(String street);

    public List<InsuranceCompany> findByAddressCity(String city);

    public List<InsuranceCompany> findByAddressPostalCode(String postalCode);

    public List<InsuranceCompany> findByAddressProvince(String province);

    public List<InsuranceCompany> findByAddressCountry(String country);
}
