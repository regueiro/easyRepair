package es.regueiro.easyrepair.api.repair.saver;

import es.regueiro.easyrepair.model.repair.RepairOrder;

public interface RepairOrderSaver {

    public void setRepairOrder(RepairOrder repairOrder);

    public RepairOrder getRepairOrder();

    public void saveRepairOrder();

    public void disableRepairOrder();

    public void enableRepairOrder();

    public void deleteRepairOrder();
}
