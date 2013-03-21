package es.regueiro.easyrepair.dao.client.hibernate;

import es.regueiro.easyrepair.api.client.saver.InsuranceCompanySaver;
import es.regueiro.easyrepair.model.client.InsuranceCompany;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = InsuranceCompanySaver.class,
//path = "InsuranceCompanyFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.client.DefaultInsuranceCompanySaver"}
)
public class InsuranceCompanyHibernateSaver implements InsuranceCompanySaver {

    private InsuranceCompany insuranceCompany;
    private Transaction t;
    private Session session;

    public InsuranceCompanyHibernateSaver() {
    }

    @Override
    public void setInsuranceCompany(InsuranceCompany insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    @Override
    public InsuranceCompany getInsuranceCompany() {
        return insuranceCompany;
    }

    @Override
    public void saveInsuranceCompany() {
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(insuranceCompany);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }

    @Override
    public void deleteInsuranceCompany() {
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.delete(insuranceCompany);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }

    @Override
    public void disableInsuranceCompany() {
        insuranceCompany.setEnabled(false);
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(insuranceCompany);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }

    @Override
    public void enableInsuranceCompany() {
        insuranceCompany.setEnabled(true);
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(insuranceCompany);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
}
