package es.regueiro.easyrepair.api.client.controller;

import es.regueiro.easyrepair.model.client.InsuranceCompany;
import java.util.List;

public interface InsuranceCompanyController {

    public List<InsuranceCompany> listAll();
    
    public List<InsuranceCompany> listAllEnabled();

    public List<InsuranceCompany> searchByName(String name);

    public List<InsuranceCompany> searchByNif(String nif);

    public List<InsuranceCompany> searchByWeb(String web);

    public void setInsuranceCompany(InsuranceCompany insuranceCompany);

    public InsuranceCompany getInsuranceCompany();

    public void saveInsuranceCompany();

    public InsuranceCompany reloadInsuranceCompany();

    public InsuranceCompany disableInsuranceCompany();

    public InsuranceCompany enableInsuranceCompany();

    public void overwriteInsuranceCompany();

    public void deleteInsuranceCompany();

    public InsuranceCompany newInsuranceCompany();

    public InsuranceCompany getInsuranceCompanyById(Long id);

    public List<InsuranceCompany> searchByEmailLabel(String label);

    public List<InsuranceCompany> searchByEmailAddress(String address);

    public List<InsuranceCompany> searchByPhoneLabel(String label);

    public List<InsuranceCompany> searchByPhoneNumber(String number);

    public List<InsuranceCompany> searchByAddressLabel(String label);

    public List<InsuranceCompany> searchByAddressStreet(String street);

    public List<InsuranceCompany> searchByAddressCity(String city);

    public List<InsuranceCompany> searchByAddressPostalCode(String postalCode);

    public List<InsuranceCompany> searchByAddressProvince(String province);

    public List<InsuranceCompany> searchByAddressCountry(String country);
}
