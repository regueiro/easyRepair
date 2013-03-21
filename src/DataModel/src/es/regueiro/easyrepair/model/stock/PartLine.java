package es.regueiro.easyrepair.model.stock;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PartLine {

    private Long id;
    private int version;
    private Part part;
    private int quantity;
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

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("NUMBER_PARTS_MORE_ONE"));
        } else {
            this.quantity = quantity;
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
        final PartLine other = (PartLine) obj;
        if (this.part != other.part && (this.part == null || !this.part.equals(other.part))) {
            return false;
        }
        if (this.quantity != other.quantity) {
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
        hash = 41 * hash + (this.part != null ? this.part.hashCode() : 0);
        hash = 41 * hash + this.quantity;
        hash = 41 * hash + (this.discount != null ? this.discount.hashCode() : 0);
        return hash;
    }
}
