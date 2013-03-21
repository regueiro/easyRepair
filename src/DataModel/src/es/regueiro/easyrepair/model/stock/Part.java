package es.regueiro.easyrepair.model.stock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class Part {

    private Long id;
    private int version;
    private String make;
    private String model;
    private String category;
    private String notes;
    private BigDecimal price;
    private boolean enabled = true;
    private List<Stock> stock;

    public void setStock(List<Stock> stock) {
        this.stock = stock;
    }

    public List<Stock> getStock() {
        if (this.stock != null) {
            return Collections.unmodifiableList(this.stock);
        } else {
            return null;
        }
    }

    public void addStock(Stock Stock) {
        if (this.stock == null) {
            this.stock = new ArrayList<Stock>();
        }
        if (!this.stock.contains(Stock)) {
            this.stock.add(Stock);
        }
    }

    public void removeStock(Long warehouseId) {
        if (this.stock != null) {
            Iterator<Stock> iterator = this.stock.iterator();
            while (iterator.hasNext()) {
                Stock v = iterator.next();
                if (v.getWarehouse().getId().compareTo(warehouseId) == 0) {
                    iterator.remove();
                }
            }
        }
    }

    public boolean hasStockInWarehouse(Long warehouseId) {
        if (getStock() == null || getStock().isEmpty()) {
            return false;
        }

        List<Stock> currentStock = getStock();

        for (Stock v : currentStock) {
            if (v.getWarehouse() != null && v.getWarehouse().getId().compareTo(warehouseId) == 0) {
                return true;
            }
        }

        return false;
    }

    public void clearStock() {
        if (this.stock != null) {
            this.stock.clear();
        } else {
            this.stock = new ArrayList<Stock>();
        }
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (StringUtils.isBlank(category)) {
            this.category = null;
        } else {
            this.category = category;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        if (StringUtils.isBlank(make)) {
            this.make = null;
        } else {
            this.make = make;
        }
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        if (StringUtils.isBlank(model)) {
            this.model = null;
        } else {
            this.model = model;
        }
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

    public void setPrice(String price) {
        if (price != null) {
            try {
                BigDecimal pr = new BigDecimal(price);
                pr.setScale(2, RoundingMode.HALF_UP);
                if (pr.compareTo(new BigDecimal("0")) == -1) {
                    throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PRICE_NOT_NEGATIVE"));
                } else {
                    this.price = pr;
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
        final Part other = (Part) obj;
        if ((this.make == null) ? (other.make != null) : !this.make.equals(other.make)) {
            return false;
        }
        if ((this.model == null) ? (other.model != null) : !this.model.equals(other.model)) {
            return false;
        }
        if ((this.category == null) ? (other.category != null) : !this.category.equals(other.category)) {
            return false;
        }
        if ((this.notes == null) ? (other.notes != null) : !this.notes.equals(other.notes)) {
            return false;
        }
        if ((this.price != null && other.price == null) || this.price == null && other.price != null || this.price != null && this.price.compareTo(other.price) != 0) {
            return false;
        }

        if (((other.getStock() == null || other.getStock().isEmpty()) && (this.getStock() != null && !this.getStock().isEmpty()))
                || ((other.getStock() != null && !other.getStock().isEmpty()) && (this.getStock() == null || this.getStock().isEmpty()))
                || ((other.getStock() != null && !other.getStock().isEmpty())
                && !(other.getStock().containsAll(this.getStock()) && other.getStock().size() == this.getStock().size()))) {
            return false;
        }
        if (this.enabled != other.enabled) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.make != null ? this.make.hashCode() : 0);
        hash = 41 * hash + (this.model != null ? this.model.hashCode() : 0);
        hash = 41 * hash + (this.category != null ? this.category.hashCode() : 0);
        hash = 41 * hash + (this.notes != null ? this.notes.hashCode() : 0);
        hash = 41 * hash + (this.price != null ? this.price.hashCode() : 0);
        hash = 41 * hash + (this.stock != null ? this.stock.hashCode() : 0);
        hash = 41 * hash + (this.enabled ? 1 : 0);
        return hash;
    }
}
