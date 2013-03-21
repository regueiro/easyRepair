package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.model.stock.PartOrderInvoice;
import java.util.Collection;
import java.util.Iterator;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class PartOrderInvoiceLookup extends AbstractLookup {

    private static PartOrderInvoiceLookup Lookup = new PartOrderInvoiceLookup();
    private InstanceContent content = null;

    private PartOrderInvoiceLookup() {
        this(new InstanceContent());
    }

    private PartOrderInvoiceLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static PartOrderInvoiceLookup getDefault() {
        return Lookup;
    }

    public synchronized void setOrderInvoice(PartOrderInvoice partOrderInvoice) {
        clear();
        add(partOrderInvoice);

    }

    public synchronized void clear() {
        Collection<? extends PartOrderInvoice> all =
                lookupAll(PartOrderInvoice.class);
        if (all != null) {
            Iterator<? extends PartOrderInvoice> ia = all.iterator();
            while (ia.hasNext()) {
                remove(ia.next());
            }
        }
    }
    
    public synchronized Long getPartOrderInvoiceId() {
        PartOrderInvoice partOrderInvoice = null;
        Collection<? extends PartOrderInvoice> all =
                lookupAll(PartOrderInvoice.class);
        if (all != null) {
            Iterator<? extends PartOrderInvoice> ia = all.iterator();
            while (ia.hasNext()) {
                partOrderInvoice = ia.next();
            }
        }
        if (partOrderInvoice != null) {
            return partOrderInvoice.getId();
        } else {
            return null;
        }
    }
}
