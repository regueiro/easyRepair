package es.regueiro.easyrepair.dao.user.hibernate;

import es.regueiro.easyrepair.api.user.controller.RoleController;
import es.regueiro.easyrepair.api.user.finder.RoleFinder;
import es.regueiro.easyrepair.api.user.saver.RoleSaver;
import es.regueiro.easyrepair.model.user.Role;
import java.util.List;
import org.hibernate.HibernateException;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = RoleController.class,
//path = "RoleFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.user.DefaultRoleFinder"}
)
public class RoleHibernateController implements RoleController {

    private RoleFinder finder = Lookup.getDefault().lookup(RoleFinder.class);
    private RoleSaver saver = Lookup.getDefault().lookup(RoleSaver.class);

    @Override
    public List<Role> listAll() {
        List<Role> list = finder.listAll();
        return list;
    }

    @Override
    public void setRole(Role role) {
        saver.setRole(role);
    }

    @Override
    public Role getRole() {
        return saver.getRole();
    }

    @Override
    public void saveRole() {
        try {
            saver.saveRole();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                throw new RuntimeException("The role was updated or deleted by another user");
            } else {
                throw e;
            }

        }
    }

    @Override
    public Role reloadRole() {
        Role emp = finder.getRole(saver.getRole().getId());
        return emp;
    }

    @Override
    public void overwriteRole() {
        Role old = getRole();
        Role newRole = reloadRole();
        if (newRole == null) {
            newRole = newRole();
        }
        newRole.setName(old.getName());
        newRole.setDescription(old.getDescription());
        newRole.setPrivileges(old.getPrivileges());
        saver.setRole(newRole);
        saver.saveRole();
    }

    @Override
    public Role newRole() {
        Role emp = new Role();
        return emp;
    }

    @Override
    public void deleteRole() {
        try {
            saver.deleteRole();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setRole(reloadRole());
                deleteRole();
            } else {
                throw e;
            }
        }
    }

    @Override
    public Role getRoleById(Long id) {
        Role role = finder.getRole(id);
        return role;
    }

    @Override
    public List<Role> searchByName(String name) {
        List<Role> list = finder.findByName(name);
        return list;
    }

    @Override
    public List<Role> searchByDescription(String description) {
        List<Role> list = finder.findByDescription(description);
        return list;
    }
}
