package es.regueiro.easyrepair.model.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class Contact {

    private List<Address> address = new ArrayList<Address>();
    private List<Email> email = new ArrayList<Email>();
    private List<Phone> phone = new ArrayList<Phone>();
    private NIF nif;

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    public List<Address> getAddress() {
        if (this.address != null) {
            return Collections.unmodifiableList(this.address);
        } else {
            return null;
        }
    }

    public void addAddress(Address address) {
        address.validate();

        if (this.address == null) {
            this.address = new ArrayList<Address>();
            this.address.add(address);
        } else {
            if (containsAddressLabel(address.getLabel())) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("LABEL_EXISTS"));
            } else {
                this.address.add(address);
            }
        }
    }

    public boolean containsAddressLabel(String label) {
        if (getAddress() == null || getAddress().isEmpty()) {
            return false;
        }

        List<Address> currentAddresses = getAddress();

        for (Address a : currentAddresses) {
            if (a.getLabel().equalsIgnoreCase(label)) {
                return true;
            }
        }

        return false;
    }

    public void removeAddress(String label) {
        if (this.address != null) {
            Iterator<Address> iterator = this.address.iterator();
            while (iterator.hasNext()) {
                Address p = iterator.next();
                if (p.getLabel().equals(label)) {
                    iterator.remove();
                }
            }
        }
    }

    public void clearAdddress() {
        if (this.address != null) {
            this.address.clear();
        } else {
            this.address = new ArrayList<Address>();
        }
    }

    public void setEmail(List<Email> email) {
        this.email = email;
    }

    public List<Email> getEmail() {
        if (this.email != null) {
            return Collections.unmodifiableList(this.email);
        } else {
            return null;
        }
    }

    public void addEmail(Email email) {
        email.validate();

        if (this.email == null) {
            this.email = new ArrayList<Email>();
            this.email.add(email);
        } else {
            if (containsEmailLabel(email.getLabel())) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("LABEL_EXISTS"));
            } else {
                this.email.add(email);
            }
        }
    }

    public boolean containsEmailLabel(String label) {
        if (getEmail() == null || getEmail().isEmpty()) {
            return false;
        }

        List<Email> currentEmails = getEmail();

        for (Email e : currentEmails) {
            if (e.getLabel().equalsIgnoreCase(label)) {
                return true;
            }
        }

        return false;
    }

    public void removeEmail(String label) {
        if (this.email != null) {
            Iterator<Email> iterator = this.email.iterator();
            while (iterator.hasNext()) {
                Email p = iterator.next();
                if (p.getLabel().equals(label)) {
                    iterator.remove();
                }
            }
        }
    }

    public void clearEmail() {
        if (this.email != null) {
            this.email.clear();
        } else {
            this.email = new ArrayList<Email>();
        }
    }

    public void setPhone(List<Phone> phone) {
        this.phone = phone;
    }

    public List<Phone> getPhone() {
        if (this.phone != null) {
            return Collections.unmodifiableList(this.phone);
        } else {
            return null;
        }
    }

    public void addPhone(Phone phone) {
        phone.validate();

        if (this.phone == null) {
            this.phone = new ArrayList<Phone>();
            this.phone.add(phone);
        } else {
            if (containsPhoneLabel(phone.getLabel())) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("LABEL_EXISTS"));
            } else {
                this.phone.add(phone);
            }
        }
    }

    public boolean containsPhoneLabel(String label) {
        if (getPhone() == null || getPhone().isEmpty()) {
            return false;
        }

        List<Phone> currentPhones = getPhone();

        for (Phone p : currentPhones) {
            if (p.getLabel().equalsIgnoreCase(label)) {
                return true;
            }
        }
        return false;
    }

    public void removePhone(String label) {
        if (this.phone != null) {
            Iterator<Phone> iterator = this.phone.iterator();
            while (iterator.hasNext()) {
                Phone p = iterator.next();
                if (p.getLabel().equals(label)) {
                    iterator.remove();
                }
            }

        }
    }

    public void clearPhone() {
        if (this.phone != null) {
            this.phone.clear();
        } else {
            this.phone = new ArrayList<Phone>();
        }
    }

    public void setNif(NIF nif) {
        this.nif = nif;
    }

    public NIF getNif() {
        return nif;
    }

    public void validate() {
    }
}
