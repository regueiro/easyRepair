package es.regueiro.easyrepair.ui.repair;

import es.regueiro.easyrepair.model.repair.Estimate;
import java.util.Collection;
import java.util.Iterator;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class EstimateLookup extends AbstractLookup {

    private static EstimateLookup Lookup = new EstimateLookup();
    private InstanceContent content = null;

    private EstimateLookup() {
        this(new InstanceContent());
    }

    private EstimateLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static EstimateLookup getDefault() {
        return Lookup;
    }

    public synchronized void setEstimate(Estimate estimate) {
        clear();
        add(estimate);

    }

    public synchronized void clear() {
        Collection<? extends Estimate> all =
                lookupAll(Estimate.class);
        if (all != null) {
            Iterator<? extends Estimate> ia = all.iterator();
            while (ia.hasNext()) {
                remove(ia.next());
            }
        }
    }
    
    public synchronized Long getEstimateId() {
        Estimate estimate = null;
        Collection<? extends Estimate> all =
                lookupAll(Estimate.class);
        if (all != null) {
            Iterator<? extends Estimate> ia = all.iterator();
            while (ia.hasNext()) {
                estimate = ia.next();
            }
        }
        if (estimate != null) {
            return estimate.getId();
        } else {
            return null;
        }
    }
}
