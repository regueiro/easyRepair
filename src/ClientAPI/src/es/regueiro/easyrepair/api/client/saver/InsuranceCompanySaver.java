package es.regueiro.easyrepair.api.client.saver;

import es.regueiro.easyrepair.model.client.InsuranceCompany;

public interface InsuranceCompanySaver {

    public void setInsuranceCompany(InsuranceCompany insuranceCompany);

    public InsuranceCompany getInsuranceCompany();

    public void saveInsuranceCompany();

    public void disableInsuranceCompany();

    public void enableInsuranceCompany();

    public void deleteInsuranceCompany();
}
