package es.regueiro.easyrepair.api.user.saver;

import es.regueiro.easyrepair.model.user.Role;

public interface RoleSaver {

    public void setRole(Role role);

    public Role getRole();

    public void saveRole();

    public void deleteRole();
}
