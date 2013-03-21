package es.regueiro.easyrepair.ui.repair;

import es.regueiro.easyrepair.model.repair.Labour;
import java.util.Collection;
import java.util.Iterator;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class LabourLookup extends AbstractLookup {

    private static LabourLookup Lookup = new LabourLookup();
    private InstanceContent content = null;

    private LabourLookup() {
        this(new InstanceContent());
    }

    private LabourLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static LabourLookup getDefault() {
        return Lookup;
    }

    public synchronized void setLabour(Labour labour) {
        clear();
        add(labour);

    }

    public synchronized void clear() {
        Collection<? extends Labour> all =
                lookupAll(Labour.class);
        if (all != null) {
            Iterator<? extends Labour> ia = all.iterator();
            while (ia.hasNext()) {
                remove(ia.next());
            }
        }
    }
    
    public synchronized Long getLabourId() {
        Labour labour = null;
        Collection<? extends Labour> all =
                lookupAll(Labour.class);
        if (all != null) {
            Iterator<? extends Labour> ia = all.iterator();
            while (ia.hasNext()) {
                labour = ia.next();
            }
        }
        if (labour != null) {
            return labour.getId();
        } else {
            return null;
        }
    }
}
