package es.regueiro.easyrepair.api.repair.controller;

import es.regueiro.easyrepair.model.repair.RepairOrder;
import java.util.List;

public interface RepairOrderController {

    public List<RepairOrder> listAll();
    
    public List<RepairOrder> listAllEnabled();
    
    public List<RepairOrder> searchByRepairOrderNumber(String repairOrderNumber);

    public List<RepairOrder> searchByOrderDate(String dateAfter, String dateBefore);

    public List<RepairOrder> searchByEstimatedDate(String dateAfter, String dateBefore);

    public List<RepairOrder> searchByFinishDate(String dateAfter, String dateBefore);
    
    public List<RepairOrder> searchByDeliveryDate(String dateAfter, String dateBefore);

    public List<RepairOrder> searchByKilometres(String kilometres);

    public List<RepairOrder> searchByGasTankLevel(String tankLevel);

    public List<RepairOrder> searchByDiscount(String discount);

    public List<RepairOrder> searchByStatus(String status);

    public List<RepairOrder> searchByEstimateNumber(String estimateNumber);

    public List<RepairOrder> searchByResponsible(String name);

    public List<RepairOrder> searchByVehicle(String registration);

    public void setRepairOrder(RepairOrder repairOrder);

    public RepairOrder getRepairOrder();

    public void saveRepairOrder();

    public RepairOrder reloadRepairOrder();

    public RepairOrder disableRepairOrder();

    public RepairOrder enableRepairOrder();

    public void overwriteRepairOrder();

    public void deleteRepairOrder();

    public RepairOrder newRepairOrder();

    public RepairOrder getRepairOrderById(Long id);

}
