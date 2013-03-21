package es.regueiro.easyrepair.api.user.saver;

import es.regueiro.easyrepair.model.user.User;

public interface UserSaver {

    public void setUser(User user);

    public User getUser();

    public void saveUser();

    public void deleteUser();
}
