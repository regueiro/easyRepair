package es.regueiro.easyrepair.api.user.finder;

import es.regueiro.easyrepair.model.user.Role;
import java.util.List;

public interface RoleFinder {

    public List<Role> listAll();
    
    public List<Role> findByName(String name);

    public List<Role> findByDescription(String description);

    public Role getRole(Long id);
}
