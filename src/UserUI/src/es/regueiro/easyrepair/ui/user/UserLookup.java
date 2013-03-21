package es.regueiro.easyrepair.ui.user;

import es.regueiro.easyrepair.model.user.User;
import java.util.Collection;
import java.util.Iterator;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class UserLookup extends AbstractLookup {

    private static UserLookup Lookup = new UserLookup();
    private InstanceContent content = null;

    private UserLookup() {
        this(new InstanceContent());
    }

    private UserLookup(InstanceContent content) {
        super(content);
        this.content = content;
    }

    public void add(Object instance) {
        content.add(instance);
    }

    public void remove(Object instance) {
        content.remove(instance);
    }

    public static UserLookup getDefault() {
        return Lookup;
    }

    public synchronized void setUser(User user) {
        clear();
        add(user);

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
    
    public synchronized Long getUserId() {
        User user = null;
        Collection<? extends User> all =
                lookupAll(User.class);
        if (all != null) {
            Iterator<? extends User> ia = all.iterator();
            while (ia.hasNext()) {
                user = ia.next();
            }
        }
        if (user != null) {
            return user.getId();
        } else {
            return null;
        }
    }
}
