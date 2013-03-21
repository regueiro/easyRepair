package es.regueiro.easyrepair.ui.repair;

import es.regueiro.easyrepair.model.repair.RepairOrder;
import java.util.Collection;
import java.util.Iterator;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class RepairOrderLookup extends AbstractLookup {

    private static RepairOrderLookup Lookup = new RepairOrderLookup();
    private InstanceContent content = null;

    private RepairOrderLookup() {
        this(new InstanceContent());
    }

    private RepairOrderLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static RepairOrderLookup getDefault() {
        return Lookup;
    }

    public synchronized void setRepairOrder(RepairOrder repairOrder) {
        clear();
        add(repairOrder);

    }

    public synchronized void clear() {
        Collection<? extends RepairOrder> all =
                lookupAll(RepairOrder.class);
        if (all != null) {
            Iterator<? extends RepairOrder> ia = all.iterator();
            while (ia.hasNext()) {
                remove(ia.next());
            }
        }
    }
    
    public synchronized Long getRepairOrderId() {
        RepairOrder repairOrder = null;
        Collection<? extends RepairOrder> all =
                lookupAll(RepairOrder.class);
        if (all != null) {
            Iterator<? extends RepairOrder> ia = all.iterator();
            while (ia.hasNext()) {
                repairOrder = ia.next();
            }
        }
        if (repairOrder != null) {
            return repairOrder.getId();
        } else {
            return null;
        }
    }
}
