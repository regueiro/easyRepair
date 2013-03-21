package es.regueiro.easyrepair.api.user.finder;

import es.regueiro.easyrepair.model.user.User;
import java.util.List;

public interface UserFinder {

    public List<User> listAll();

    public List<User> findByName(String name);
    
    public List<User> findByRole(String role);

    public User getUser(String name);

    public User getUser(Long id);
}
