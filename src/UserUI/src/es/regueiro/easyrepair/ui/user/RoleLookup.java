package es.regueiro.easyrepair.ui.user;

import es.regueiro.easyrepair.model.user.Role;
import java.util.Collection;
import java.util.Iterator;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class RoleLookup extends AbstractLookup {

    private static RoleLookup Lookup = new RoleLookup();
    private InstanceContent content = null;

    private RoleLookup() {
        this(new InstanceContent());
    }

    private RoleLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static RoleLookup getDefault() {
        return Lookup;
    }

    public synchronized void setRole(Role role) {
        clear();
        add(role);

    }

    public synchronized void clear() {
        Collection<? extends Role> all =
                lookupAll(Role.class);
        if (all != null) {
            Iterator<? extends Role> ia = all.iterator();
            while (ia.hasNext()) {
                remove(ia.next());
            }
        }
    }
    
    public synchronized Long getRoleId() {
        Role role = null;
        Collection<? extends Role> all =
                lookupAll(Role.class);
        if (all != null) {
            Iterator<? extends Role> ia = all.iterator();
            while (ia.hasNext()) {
                role = ia.next();
            }
        }
        if (role != null) {
            return role.getId();
        } else {
            return null;
        }
    }
}
