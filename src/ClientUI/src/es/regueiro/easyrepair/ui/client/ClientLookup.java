package es.regueiro.easyrepair.ui.client;

import es.regueiro.easyrepair.model.client.Client;
import java.util.Collection;
import java.util.Iterator;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class ClientLookup extends AbstractLookup {

    private static ClientLookup Lookup = new ClientLookup();
    private InstanceContent content = null;

    private ClientLookup() {
        this(new InstanceContent());
    }

    private ClientLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static ClientLookup getDefault() {
        return Lookup;
    }

    public synchronized void setClient(Client client) {
        clear();
        add(client);

    }

    public synchronized void clear() {
        Collection<? extends Client> all =
                lookupAll(Client.class);
        if (all != null) {
            Iterator<? extends Client> ia = all.iterator();
            while (ia.hasNext()) {
                remove(ia.next());
            }
        }
    }
    
    public synchronized Long getClientId() {
        Client client = null;
        Collection<? extends Client> all =
                lookupAll(Client.class);
        if (all != null) {
            Iterator<? extends Client> ia = all.iterator();
            while (ia.hasNext()) {
                client = ia.next();
            }
        }
        if (client != null) {
            return client.getId();
        } else {
            return null;
        }
    }
}
