package es.regueiro.easyrepair.model.shared;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

public class Email {

    private String address;
    private String label;
    private Long id;
    private String notes;

    public Email() {
    }

    public Email(String address, String label) {
        validateAddress(address);
        validateLabel(label);

        this.address = address;
        this.label = label;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        validateAddress(address);
        this.address = address;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        validateLabel(label);
        this.label = label;
    }

    public void validate() {
        validateAddress(address);
        validateLabel(label);
    }

    private void validateAddress(String address) {
        if (address == null || StringUtils.isBlank(address)) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EMAIL_MUST_HAVE_ADDRESS"));
        } else {
            EmailValidator emailValidator = EmailValidator.getInstance();
            if (!emailValidator.isValid(address)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EMAIL_INVALID"));
            }
        }
    }

    private void validateLabel(String label) {
        if (label == null || StringUtils.isBlank(label)) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EMAIL_MUST_HAVE_LABEL"));
        }
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        if (StringUtils.isBlank(notes)) {
            this.notes = null;
        } else {
            this.notes = notes;
        }
    }

    @Override
    public final boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!this.getClass().equals(o.getClass())) {
            return false;
        }
        Email e = (Email) o;


        if ((e.getAddress() == null && this.getAddress() != null) || (e.getAddress() != null && !e.getAddress().equalsIgnoreCase(this.getAddress()))) {
            return false;
        }
        if ((e.getLabel() == null && this.getLabel() != null) || (e.getLabel() != null && !e.getLabel().equalsIgnoreCase(this.getLabel()))) {
            return false;
        }
        if ((e.getNotes() == null && this.getNotes() != null) || (e.getNotes() != null && !e.getNotes().equalsIgnoreCase(this.getNotes()))) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.address != null ? this.address.hashCode() : 0);
        hash = 79 * hash + (this.label != null ? this.label.hashCode() : 0);
        hash = 79 * hash + (this.notes != null ? this.notes.hashCode() : 0);
        return hash;
    }
}
