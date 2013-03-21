package es.regueiro.easyrepair.model.user;

import org.apache.commons.lang3.StringUtils;

public class User {

    private Long id;
    private String name;
    private String password = null;
    private Role role;
    private int version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || StringUtils.isBlank(password)) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PASSWORD_NOT_BLANK"));
        }
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        password = null;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || StringUtils.isBlank(name)) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("NAME_NOT_BLANK"));
        }
        this.name = name;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void validate() {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("NAME_NOT_BLANK"));
        }
        if (password == null) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PASSWORD_NOT_BLANK"));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
