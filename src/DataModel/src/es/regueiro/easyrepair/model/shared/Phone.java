package es.regueiro.easyrepair.model.shared;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import org.apache.commons.lang3.StringUtils;

public class Phone {

    private String number;
    private String label;
    private Long id;
    private String notes;
    private PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    private PhoneNumber numberProto;

    public Phone() {
    }

    public Phone(String number, String label) {
        this.validateLabel(label);

        this.label = label;
        String formatNumber;
        if (this.isValidNumber(number)) {
            if (numberProto.getCountryCode() == 34) {
                formatNumber = phoneUtil.format(numberProto, PhoneNumberFormat.NATIONAL).replaceAll(" ", "");
            } else {
                formatNumber = phoneUtil.format(numberProto, PhoneNumberFormat.E164).replaceAll(" ", "");
            }
        } else {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PHONE_NUMBER_INVALID"));
        }

        this.number = formatNumber;

    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        if (this.isValidNumber(number)) {
            if (numberProto.getCountryCode() == 34) {
                this.number = phoneUtil.format(numberProto, PhoneNumberFormat.NATIONAL).replaceAll(" ", "");
            } else {
                this.number = phoneUtil.format(numberProto, PhoneNumberFormat.E164).replaceAll(" ", "");
            }
        } else {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PHONE_NUMBER_INVALID"));
        }

    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.validateLabel(label);
        this.label = label;
    }

    public void validate() {
        if (this.isValidNumber(this.number)) {
            this.validateLabel(this.label);
        }
    }

    private boolean isValidNumber(String number) {
        boolean isValid = false;
        if (number == null || StringUtils.isBlank(number)) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PHONE_MUST_HAVE_NUMBER"));
        }

        try {
            numberProto = phoneUtil.parse(number, "ES");
            isValid = phoneUtil.isValidNumber(numberProto);
        } catch (NumberParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return isValid;
    }

    private void validateLabel(String label) {
        if (label == null || StringUtils.isBlank(label)) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PHONE_MUST_HAVE_LABEL"));
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
    public String toString() {
        return this.number;
    }

    @Override
    public final boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!this.getClass().equals(o.getClass())) {
            return false;
        }
        Phone e = (Phone) o;


        if ((e.getNumber() == null && this.getNumber() != null) || (e.getNumber() != null && !e.getNumber().equalsIgnoreCase(this.getNumber()))) {
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
        hash = 79 * hash + (this.number != null ? this.number.hashCode() : 0);
        hash = 79 * hash + (this.label != null ? this.label.hashCode() : 0);
        hash = 79 * hash + (this.notes != null ? this.notes.hashCode() : 0);
        return hash;
    }
}