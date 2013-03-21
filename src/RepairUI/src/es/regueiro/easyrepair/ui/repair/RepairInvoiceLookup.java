package es.regueiro.easyrepair.ui.repair;

import es.regueiro.easyrepair.model.repair.RepairInvoice;
import java.util.Collection;
import java.util.Iterator;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class RepairInvoiceLookup extends AbstractLookup {

    private static RepairInvoiceLookup Lookup = new RepairInvoiceLookup();
    private InstanceContent content = null;

    private RepairInvoiceLookup() {
        this(new InstanceContent());
    }

    private RepairInvoiceLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static RepairInvoiceLookup getDefault() {
        return Lookup;
    }

    public synchronized void setRepairInvoice(RepairInvoice repairInvoice) {
        clear();
        add(repairInvoice);

    }

    public synchronized void clear() {
        Collection<? extends RepairInvoice> all =
                lookupAll(RepairInvoice.class);
        if (all != null) {
            Iterator<? extends RepairInvoice> ia = all.iterator();
            while (ia.hasNext()) {
                remove(ia.next());
            }
        }
    }
    
    public synchronized Long getRepairInvoiceId() {
        RepairInvoice repairInvoice = null;
        Collection<? extends RepairInvoice> all =
                lookupAll(RepairInvoice.class);
        if (all != null) {
            Iterator<? extends RepairInvoice> ia = all.iterator();
            while (ia.hasNext()) {
                repairInvoice = ia.next();
            }
        }
        if (repairInvoice != null) {
            return repairInvoice.getId();
        } else {
            return null;
        }
    }
}
