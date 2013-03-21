package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.model.stock.PartOrder;
import java.util.Collection;
import java.util.Iterator;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class PartOrderLookup extends AbstractLookup {

    private static PartOrderLookup Lookup = new PartOrderLookup();
    private InstanceContent content = null;

    private PartOrderLookup() {
        this(new InstanceContent());
    }

    private PartOrderLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static PartOrderLookup getDefault() {
        return Lookup;
    }

    public synchronized void setPartOrder(PartOrder part) {
        clear();
        add(part);

    }

    public synchronized void clear() {
        Collection<? extends PartOrder> all =
                lookupAll(PartOrder.class);
        if (all != null) {
            Iterator<? extends PartOrder> ia = all.iterator();
            while (ia.hasNext()) {
                remove(ia.next());
            }
        }
    }
    
    public synchronized Long getPartOrderId() {
        PartOrder part = null;
        Collection<? extends PartOrder> all =
                lookupAll(PartOrder.class);
        if (all != null) {
            Iterator<? extends PartOrder> ia = all.iterator();
            while (ia.hasNext()) {
                part = ia.next();
            }
        }
        if (part != null) {
            return part.getId();
        } else {
            return null;
        }
    }
}
