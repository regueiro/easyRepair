package es.regueiro.easyrepair.api.stock.saver;

import es.regueiro.easyrepair.model.stock.PartOrder;

public interface PartOrderSaver {

    public void setPartOrder(PartOrder partOrder);

    public PartOrder getPartOrder();

    public void savePartOrder();

    public void disablePartOrder();

    public void enablePartOrder();

    public void deletePartOrder();
}
