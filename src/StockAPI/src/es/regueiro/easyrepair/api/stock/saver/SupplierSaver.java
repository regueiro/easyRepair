package es.regueiro.easyrepair.api.stock.saver;

import es.regueiro.easyrepair.model.stock.Supplier;

public interface SupplierSaver {

    public void setSupplier(Supplier supplier);

    public Supplier getSupplier();

    public void saveSupplier();

    public void disableSupplier();

    public void enableSupplier();

    public void deleteSupplier();
}
