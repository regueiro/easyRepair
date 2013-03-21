package es.regueiro.easyrepair.api.stock.saver;

import es.regueiro.easyrepair.model.stock.Warehouse;

public interface WarehouseSaver {

    public void setWarehouse(Warehouse warehouse);

    public Warehouse getWarehouse();

    public void saveWarehouse();

    public void disableWarehouse();

    public void enableWarehouse();

    public void deleteWarehouse();
}
