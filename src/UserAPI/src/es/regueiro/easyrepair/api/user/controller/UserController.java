package es.regueiro.easyrepair.api.user.controller;

import es.regueiro.easyrepair.model.user.Privilege;
import es.regueiro.easyrepair.model.user.User;
import java.util.List;

public interface UserController {

    public void testConnection();
    
    public List<User> listAll();
    
    public List<User> searchByName(String name);
    
    public List<User> searchByRole(String role);

    public void setUser(User user);

    public User getUser();

    public void saveUser();

    public User reloadUser();

    public void overwriteUser();

    public void deleteUser();

    public User newUser();

    public User getUserById(Long id);

    public User getUserByName(String name);

    public boolean isValidLogin(String user, String password);

    public List<Privilege> getUserPrivileges(String username);
}
