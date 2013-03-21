package es.regueiro.easyrepair.model.repair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.commons.lang3.StringUtils;

public class Labour {

    private Long id;
    private int version;
    private String name;
    private String description;
    private String notes;
    private BigDecimal price;
    private boolean enabled = true;

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

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        if (StringUtils.isBlank(notes)) {
            this.notes = null;
        } else {
            this.notes = notes;
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    // Se convierte en un numero, se redondea con dos decimales y se comprueba si
    // es negativo o invalido antes de asignarlo.
    public void setPrice(String price) {
        if (price != null) {
            try {
                BigDecimal pvp = new BigDecimal(price);
                pvp.setScale(2, RoundingMode.HALF_UP);
                if (pvp.compareTo(new BigDecimal("0")) == -1) {
                    throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PRICE_NOT_NEGATIVE"));
                } else {
                    this.price = pvp;
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PRICE_INVALID"));
            }
        } else {
            this.price = new BigDecimal("0");
        }
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Labour other = (Labour) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
            return false;
        }
        if ((this.notes == null) ? (other.notes != null) : !this.notes.equals(other.notes)) {
            return false;
        }
        if ((this.price != null && other.price == null) || this.price == null && other.price != null || this.price != null && this.price.compareTo(other.price) != 0)  {
            return false;
        }
        
        if (this.enabled != other.enabled) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 19 * hash + (this.description != null ? this.description.hashCode() : 0);
        hash = 19 * hash + (this.notes != null ? this.notes.hashCode() : 0);
        hash = 19 * hash + (this.price != null ? this.price.hashCode() : 0);
        hash = 19 * hash + (this.enabled ? 1 : 0);
        return hash;
    }
}
