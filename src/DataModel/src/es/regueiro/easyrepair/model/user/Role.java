package es.regueiro.easyrepair.model.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class Role {

    private Long id;
    private String name;
    private String description;
    private List<Privilege> privileges;
    private int version;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (StringUtils.isBlank(description)) {
            this.description = null;
        } else {
            this.description = description;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }

    public List<Privilege> getPrivileges() {
        if (this.privileges != null) {
            return Collections.unmodifiableList(this.privileges);
        } else {
            return null;
        }
    }

    public void addPrivilege(Privilege privilege) {
        if (this.privileges == null) {
            this.privileges = new ArrayList<Privilege>();
        }
        if (!this.privileges.contains(privilege)) {
            this.privileges.add(privilege);
        }
    }

    public void removePrivilege(Privilege p) {
        if (this.privileges != null) {
            Iterator<Privilege> iterator = this.privileges.iterator();
            while (iterator.hasNext()) {
                Privilege v = iterator.next();
                if (v.equals(p)) {
                    iterator.remove();
                }
            }
        }
    }

    public boolean hasPrivilege(Privilege p) {
        if (getPrivileges() == null || getPrivileges().isEmpty()) {
            return false;
        }

        List<Privilege> currentPrivileges = getPrivileges();

        for (Privilege v : currentPrivileges) {
            if (v.equals(p)) {
                return true;
            }
        }

        return false;
    }

    public void clearPrivileges() {
        if (this.privileges != null) {
            this.privileges.clear();
        } else {
            this.privileges = new ArrayList<Privilege>();
        }
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
        if (this.privileges == null || this.privileges.isEmpty()) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ROLE_MUST_HAVE_PRIVILEGE"));
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
        final Role other = (Role) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
            return false;
        }
        if (((other.getPrivileges() == null || other.getPrivileges().isEmpty()) && (this.getPrivileges() != null && !this.getPrivileges().isEmpty()))
                || ((other.getPrivileges() != null && !other.getPrivileges().isEmpty()) && (this.getPrivileges() == null || this.getPrivileges().isEmpty()))
                || ((other.getPrivileges() != null && !other.getPrivileges().isEmpty())
                && !(other.getPrivileges().containsAll(this.getPrivileges()) && other.getPrivileges().size() == this.getPrivileges().size()))) {
            return false;
        }
        return true;
    }

   

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 79 * hash + (this.description != null ? this.description.hashCode() : 0);
        hash = 79 * hash + (this.privileges != null ? this.privileges.hashCode() : 0);
        return hash;
    }
}
