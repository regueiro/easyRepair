package es.regueiro.easyrepair.ui.client;

import es.regueiro.easyrepair.model.client.Vehicle;
import java.util.Collection;
import java.util.Iterator;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class VehicleLookup extends AbstractLookup {

    private static VehicleLookup Lookup = new VehicleLookup();
    private InstanceContent content = null;

    private VehicleLookup() {
        this(new InstanceContent());
    }

    private VehicleLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static VehicleLookup getDefault() {
        return Lookup;
    }

    public synchronized void setVehicle(Vehicle insuranceCompany) {
        clear();
        add(insuranceCompany);

    }

    public synchronized void clear() {
        Collection<? extends Vehicle> all =
                lookupAll(Vehicle.class);
        if (all != null) {
            Iterator<? extends Vehicle> ia = all.iterator();
            while (ia.hasNext()) {
                remove(ia.next());
            }
        }
    }
    
    public synchronized Long getVehicleId() {
        Vehicle veh = null;
        Collection<? extends Vehicle> all =
                lookupAll(Vehicle.class);
        if (all != null) {
            Iterator<? extends Vehicle> ia = all.iterator();
            while (ia.hasNext()) {
                veh = ia.next();
            }
        }
        if (veh != null) {
            return veh.getId();
        } else {
            return null;
        }
    }
}
