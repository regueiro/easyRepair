package es.regueiro.easyrepair.dao.user.hibernate;

import es.regueiro.easyrepair.api.user.controller.UserController;
import es.regueiro.easyrepair.api.user.finder.UserFinder;
import es.regueiro.easyrepair.api.user.saver.UserSaver;
import es.regueiro.easyrepair.model.user.BCrypt;
import es.regueiro.easyrepair.model.user.Privilege;
import es.regueiro.easyrepair.model.user.User;
import java.util.List;
import org.hibernate.HibernateException;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = UserController.class,
//path = "UserFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.user.DefaultUserFinder"}
)
public class UserHibernateController implements UserController {

    private UserFinder finder = Lookup.getDefault().lookup(UserFinder.class);
    private UserSaver saver = Lookup.getDefault().lookup(UserSaver.class);

    
    @Override
    public void testConnection() {
        Installer.createSession();
    }
    
    @Override
    public List<User> listAll() {
        List<User> list = finder.listAll();
        return list;
    }

    @Override
    public List<User> searchByName(String name) {
        List<User> list = finder.findByName(name);
        return list;
    }
    
    @Override
    public List<User> searchByRole(String role) {
        List<User> list = finder.findByRole(role);
        return list;
    }

    @Override
    public void setUser(User user) {
        saver.setUser(user);
    }

    @Override
    public User getUser() {
        return saver.getUser();
    }

    @Override
    public void saveUser() {
        try {
            saver.saveUser();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                throw new RuntimeException("The user was updated or deleted by another user");
            } else {
                throw e;
            }

        }
    }

    @Override
    public User reloadUser() {
        User emp = finder.getUser(saver.getUser().getId());
        return emp;
    }

    @Override
    public void overwriteUser() {
        User old = getUser();
        User newUser = reloadUser();
        if (newUser == null) {
            newUser = newUser();
        }
        newUser.setName(old.getName());
        newUser.setPassword(old.getPassword());
        newUser.setRole(old.getRole());
        saver.setUser(newUser);
        saver.saveUser();
    }

    @Override
    public User newUser() {
        User emp = new User();
        return emp;
    }

    @Override
    public void deleteUser() {
        try {
            saver.deleteUser();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setUser(reloadUser());
                deleteUser();
            } else {
                throw e;
            }
        }
    }

    @Override
    public User getUserById(Long id) {
        User user = finder.getUser(id);
        return user;
    }

    @Override
    public User getUserByName(String name) {
        User user = finder.getUser(name);
        return user;
    }

    @Override
    public boolean isValidLogin(String user, String password) {
        User foundUser = finder.getUser(user);
        if (foundUser != null && BCrypt.checkpw(password, foundUser.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Privilege> getUserPrivileges(String username) {
        User user = finder.getUser(username);
        return user.getRole().getPrivileges();
    }
}
