package es.regueiro.easyrepair.model.shared;

import com.aeat.valida.Validador;

public class NIF {

    private String number;

    public NIF() {
    }

    public NIF(String number) {
        String newNumber = number.toUpperCase();
        if (isValid(newNumber)) {
            this.number = newNumber;
        } else {
            throw new IllegalArgumentException(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("NIF_INVALID"), new Object[]{number}));
        }
    }

    public void setNumber(String number) {
        String newNumber = null;
        if (number != null) {
            newNumber = number.toUpperCase();
        }
        if (newNumber != null && isValid(newNumber)) {
            this.number = newNumber;
        } else {
            throw new IllegalArgumentException(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("NIF_INVALID"), new Object[]{number}));
        }
    }

    public String getNumber() {
        return this.number;
    }

    private boolean isValid(String number) {
        Validador val = new Validador();
        if (val.checkNif(number) > 0) {
            return true;
        } else {
            return false;
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
        NIF n = (NIF) o;

        if (n.getNumber() == null && this.getNumber() == null || n.getNumber() != null && n.getNumber().equalsIgnoreCase(this.getNumber())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.number != null ? this.number.hashCode() : 0);
        return hash;
    }
}
