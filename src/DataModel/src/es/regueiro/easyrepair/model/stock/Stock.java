package es.regueiro.easyrepair.model.stock;

public class Stock {

    private Long id;
    private Warehouse warehouse;
    private int units;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        if (units < 0) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("UNITS_NOT_NEGATIVE"));
        } else {
            this.units = units;
        }
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Stock other = (Stock) obj;
        if (this.warehouse != other.warehouse && (this.warehouse == null || !this.warehouse.equals(other.warehouse))) {
            return false;
        }
        if (this.units != other.units) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.warehouse != null ? this.warehouse.hashCode() : 0);
        hash = 67 * hash + this.units;
        return hash;
    }
}
