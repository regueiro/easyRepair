package es.regueiro.easyrepair.api.user.controller;

import es.regueiro.easyrepair.model.user.Role;
import java.util.List;

public interface RoleController {

    public List<Role> listAll();

    public List<Role> searchByName(String name);

    public List<Role> searchByDescription(String description);

    public void setRole(Role role);

    public Role getRole();

    public void saveRole();

    public Role reloadRole();

    public void overwriteRole();

    public void deleteRole();

    public Role newRole();

    public Role getRoleById(Long id);
}
