package es.regueiro.easyrepair.ui.client;

import es.regueiro.easyrepair.model.client.InsuranceCompany;
import java.util.Collection;
import java.util.Iterator;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class InsuranceCompanyLookup extends AbstractLookup {

    private static InsuranceCompanyLookup Lookup = new InsuranceCompanyLookup();
    private InstanceContent content = null;

    private InsuranceCompanyLookup() {
        this(new InstanceContent());
    }

    private InsuranceCompanyLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static InsuranceCompanyLookup getDefault() {
        return Lookup;
    }

    public synchronized void setInsuranceCompany(InsuranceCompany insuranceCompany) {
        clear();
        add(insuranceCompany);

    }

    public synchronized void clear() {
        Collection<? extends InsuranceCompany> all =
                lookupAll(InsuranceCompany.class);
        if (all != null) {
            Iterator<? extends InsuranceCompany> ia = all.iterator();
            while (ia.hasNext()) {
                remove(ia.next());
            }
        }
    }
    
    public synchronized Long getInsuranceCompanyId() {
        InsuranceCompany emp = null;
        Collection<? extends InsuranceCompany> all =
                lookupAll(InsuranceCompany.class);
        if (all != null) {
            Iterator<? extends InsuranceCompany> ia = all.iterator();
            while (ia.hasNext()) {
                emp = ia.next();
            }
        }
        if (emp != null) {
            return emp.getId();
        } else {
            return null;
        }
    }
}
