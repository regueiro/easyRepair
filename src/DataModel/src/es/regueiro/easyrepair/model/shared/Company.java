package es.regueiro.easyrepair.model.shared;

import org.apache.commons.lang3.StringUtils;

public abstract class Company extends Contact {

    private String name;

    public void setName(String name) {
        if (name == null || StringUtils.isBlank(name)) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("NAME_NOT_BLANK"));
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("NAME_NOT_BLANK"));
        }
    }
}
