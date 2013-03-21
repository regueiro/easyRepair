package es.regueiro.easyrepair.login;

import es.regueiro.easyrepair.model.user.User;
import java.util.Collection;
import java.util.Iterator;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class LoginLookup extends AbstractLookup {

    private static LoginLookup Lookup = new LoginLookup();
    private InstanceContent content = null;

    private LoginLookup() {
        this(new InstanceContent());
    }

    private LoginLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static LoginLookup getDefault() {
        return Lookup;
    }

    public synchronized void setUser(User User) {
        clear();
        add(User);

    }

    public synchronized void clear() {
        Collection<? extends User> all =
                lookupAll(User.class);
        if (all != null) {
            Iterator<? extends User> ia = all.iterator();
            while (ia.hasNext()) {
                remove(ia.next());
            }
        }
    }
    
    public synchronized User getUser() {
        User user = null;
        Collection<? extends User> all =
                lookupAll(User.class);
        if (all != null) {
            Iterator<? extends User> ia = all.iterator();
            while (ia.hasNext()) {
                user = ia.next();
            }
        }
        return user;
    }
}
