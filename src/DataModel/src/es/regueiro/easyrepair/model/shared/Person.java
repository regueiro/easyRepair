package es.regueiro.easyrepair.model.shared;

import org.apache.commons.lang3.StringUtils;

public abstract class Person extends Contact {

    private String name;
    private String surname;

    public void setName(String name) {
        if (name == null || StringUtils.isBlank(name)) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("NAME_NOT_BLANK"));
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSurname(String surname) {
        if (StringUtils.isBlank(surname)) {
            this.surname = null;
        } else {
            this.surname = surname;
        }
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("NAME_NOT_BLANK"));
        }
    }
}
