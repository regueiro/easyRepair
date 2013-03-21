package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.model.stock.Part;
import java.util.Collection;
import java.util.Iterator;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class PartLookup extends AbstractLookup {

    private static PartLookup Lookup = new PartLookup();
    private InstanceContent content = null;

    private PartLookup() {
        this(new InstanceContent());
    }

    private PartLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static PartLookup getDefault() {
        return Lookup;
    }

    public synchronized void setPart(Part part) {
        clear();
        add(part);

    }

    public synchronized void clear() {
        Collection<? extends Part> all =
                lookupAll(Part.class);
        if (all != null) {
            Iterator<? extends Part> ia = all.iterator();
            while (ia.hasNext()) {
                remove(ia.next());
            }
        }
    }
    
    public synchronized Long getPartId() {
        Part part = null;
        Collection<? extends Part> all =
                lookupAll(Part.class);
        if (all != null) {
            Iterator<? extends Part> ia = all.iterator();
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
