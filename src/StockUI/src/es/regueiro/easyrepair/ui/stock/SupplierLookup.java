package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.model.stock.Supplier;
import java.util.Collection;
import java.util.Iterator;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class SupplierLookup extends AbstractLookup {

    private static SupplierLookup Lookup = new SupplierLookup();
    private InstanceContent content = null;

    private SupplierLookup() {
        this(new InstanceContent());
    }

    private SupplierLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static SupplierLookup getDefault() {
        return Lookup;
    }

    public synchronized void setSupplier(Supplier warehouse) {
        clear();
        add(warehouse);

    }

    public synchronized void clear() {
        Collection<? extends Supplier> all =
                lookupAll(Supplier.class);
        if (all != null) {
            Iterator<? extends Supplier> ia = all.iterator();
            while (ia.hasNext()) {
                remove(ia.next());
            }
        }
    }
    
    public synchronized Long getSupplierId() {
        Supplier warehouse = null;
        Collection<? extends Supplier> all =
                lookupAll(Supplier.class);
        if (all != null) {
            Iterator<? extends Supplier> ia = all.iterator();
            while (ia.hasNext()) {
                warehouse = ia.next();
            }
        }
        if (warehouse != null) {
            return warehouse.getId();
        } else {
            return null;
        }
    }
}
