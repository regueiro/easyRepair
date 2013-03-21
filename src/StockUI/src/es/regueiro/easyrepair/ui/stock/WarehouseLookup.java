package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.model.stock.Warehouse;
import java.util.Collection;
import java.util.Iterator;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class WarehouseLookup extends AbstractLookup {

    private static WarehouseLookup Lookup = new WarehouseLookup();
    private InstanceContent content = null;

    private WarehouseLookup() {
        this(new InstanceContent());
    }

    private WarehouseLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static WarehouseLookup getDefault() {
        return Lookup;
    }

    public synchronized void setWarehouse(Warehouse warehouse) {
        clear();
        add(warehouse);

    }

    public synchronized void clear() {
        Collection<? extends Warehouse> all =
                lookupAll(Warehouse.class);
        if (all != null) {
            Iterator<? extends Warehouse> ia = all.iterator();
            while (ia.hasNext()) {
                remove(ia.next());
            }
        }
    }
    
    public synchronized Long getWarehouseId() {
        Warehouse warehouse = null;
        Collection<? extends Warehouse> all =
                lookupAll(Warehouse.class);
        if (all != null) {
            Iterator<? extends Warehouse> ia = all.iterator();
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
