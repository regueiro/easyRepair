package es.regueiro.easyrepair.model.shared;

import org.apache.commons.lang3.StringUtils;

public class Address {

    private String street;
    private String city;
    private String province;
    private String country;
    private String postalCode;
    private String label;
    private String notes;
    private Long id;

    public void setStreet(String street) {
        if (StringUtils.isBlank(street)) {
            this.street = null;
        } else {
            this.street = street;
        }
    }

    public String getStreet() {
        return this.street;
    }

    public void setCity(String city) {
        if (StringUtils.isBlank(city)) {
            this.city = null;
        } else {
            this.city = city;
        }
    }

    public String getCity() {
        return this.city;
    }

    public void setProvince(String province) {
        if (StringUtils.isBlank(province)) {
            this.province = null;
        } else {
            this.province = province;
        }
    }

    public String getProvince() {
        return this.province;
    }

    public void setCountry(String country) {
        if (StringUtils.isBlank(country)) {
            this.country = null;
        } else {
            this.country = country;
        }
    }

    public String getCountry() {
        return this.country;
    }

    public void setPostalCode(String postalCode) {
        if (StringUtils.isBlank(postalCode)) {
            this.postalCode = null;
        } else {
            this.postalCode = postalCode;
        }

    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setLabel(String label) {
        if (label == null || StringUtils.isBlank(label)) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ADDRESS_MUST_HAVE_LABEL"));
        } else {
            this.label = label;
        }
    }

    public String getLabel() {
        return this.label;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void validate() {
        if (street == null && city == null && province == null && country == null
                && postalCode == null && notes == null) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ADDRESS_NOT_BLANK"));
        }
        if (label == null) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ADDRESS_MUST_HAVE_LABEL"));
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
        Address a = (Address) o;
        if ((a.getStreet() == null && this.getStreet() != null) || a.getStreet() != null && !a.getStreet().equalsIgnoreCase(this.getStreet())) {
            return false;
        }
        if ((a.getLabel() == null && this.getLabel() != null) || a.getLabel() != null && !a.getLabel().equalsIgnoreCase(this.getLabel())) {
            return false;
        }
        if ((a.getCity() == null && this.getCity() != null) || a.getCity() != null && !a.getCity().equalsIgnoreCase(this.getCity())) {
            return false;
        }
        if ((a.getProvince() == null && this.getProvince() != null) || a.getProvince() != null && !a.getProvince().equalsIgnoreCase(this.getProvince())) {
            return false;
        }
        if ((a.getCountry() == null && this.getCountry() != null) || a.getCountry() != null && !a.getCountry().equalsIgnoreCase(this.getCountry())) {
            return false;
        }
        if ((a.getPostalCode() == null && this.getPostalCode() != null) || a.getPostalCode() != null && !a.getPostalCode().equalsIgnoreCase(this.getPostalCode())) {
            return false;
        }
        if ((a.getNotes() == null && this.getNotes() != null) || a.getNotes() != null && !a.getNotes().equalsIgnoreCase(this.getNotes())) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.street != null ? this.street.hashCode() : 0);
        hash = 97 * hash + (this.city != null ? this.city.hashCode() : 0);
        hash = 97 * hash + (this.province != null ? this.province.hashCode() : 0);
        hash = 97 * hash + (this.country != null ? this.country.hashCode() : 0);
        hash = 97 * hash + (this.postalCode != null ? this.postalCode.hashCode() : 0);
        hash = 97 * hash + (this.label != null ? this.label.hashCode() : 0);
        hash = 97 * hash + (this.notes != null ? this.notes.hashCode() : 0);
        return hash;
    }
}
