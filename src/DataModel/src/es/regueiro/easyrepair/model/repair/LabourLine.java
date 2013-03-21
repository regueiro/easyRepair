package es.regueiro.easyrepair.model.repair;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LabourLine {

    private Long id;
    private int version;
    private Labour labour;
    private BigDecimal hours;
    private BigDecimal discount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    // Se convierte en un numero, se redondea con dos decimales y se comprueba si
    // es negativo, mayor que 100 o invalido antes de asignarlo.
    public void setDiscount(String discount) {
        if (discount != null) {
            try {
                BigDecimal disc = new BigDecimal(discount);
                disc.setScale(2, RoundingMode.HALF_UP);
                if (disc.compareTo(new BigDecimal("100")) == 1) {
                    throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("DISCOUNT_NOT_GREATER_100"));
                } else if (disc.compareTo(new BigDecimal("0")) == -1) {
                    throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("DISCOUNT_NOT_NEGATIVE"));
                } else {
                    this.discount = disc;
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("DISCOUNT_INVALID"));
            }
        } else {
            this.discount = new BigDecimal("0");
        }
    }

    public BigDecimal getHours() {
        return hours;
    }

    // Se convierte en un numero, se redondea con dos decimales y se comprueba si
    // son negativas o invalidas antes de asignarlo.
    public void setHours(String hours) {
        if (hours != null) {
            try {
                BigDecimal hr = new BigDecimal(hours);
                hr.setScale(2, RoundingMode.HALF_UP);
                if (hr.compareTo(new BigDecimal("0")) == -1) {
                    throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("HOURS_NOT_NEGATIVE"));
                } else {
                    this.hours = hr;
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("HOURS_INVALID"));
            }
        } else {
            this.hours = new BigDecimal("0");
        }
    }

    public Labour getLabour() {
        return labour;
    }

    public void setLabour(Labour labour) {
        this.labour = labour;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LabourLine other = (LabourLine) obj;
        if (this.labour != other.labour && (this.labour == null || !this.labour.equals(other.labour))) {
            return false;
        }
        if (this.hours != other.hours && (this.hours == null || !this.hours.equals(other.hours))) {
            return false;
        }
        if (this.discount != other.discount && (this.discount == null || !this.discount.equals(other.discount))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.labour != null ? this.labour.hashCode() : 0);
        hash = 47 * hash + (this.hours != null ? this.hours.hashCode() : 0);
        hash = 47 * hash + (this.discount != null ? this.discount.hashCode() : 0);
        return hash;
    }
}
