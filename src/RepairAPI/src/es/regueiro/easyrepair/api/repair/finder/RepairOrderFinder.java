package es.regueiro.easyrepair.api.repair.finder;

import es.regueiro.easyrepair.model.repair.RepairOrder;
import java.util.List;

public interface RepairOrderFinder {

    public List<RepairOrder> listAll();
    
    public List<RepairOrder> listAllEnabled();

    public List<RepairOrder> findByRepairOrderNumber(String repairOrderNumber);

    public List<RepairOrder> findByOrderDate(String dateAfter, String dateBefore);

    public List<RepairOrder> findByEstimatedDate(String dateAfter, String dateBefore);

    public List<RepairOrder> findByFinishDate(String dateAfter, String dateBefore);
    
    public List<RepairOrder> findByDeliveryDate(String dateAfter, String dateBefore);

    public RepairOrder getRepairOrder(Long id);

    public List<RepairOrder> findByKilometres(String kilometres);

    public List<RepairOrder> findByGasTankLevel(String tankLevel);

    public List<RepairOrder> findByDiscount(String discount);

    public List<RepairOrder> findByStatus(String status);

    public List<RepairOrder> findByEstimateNumber(String estimateNumber);

    public List<RepairOrder> findByResponsible(String name);

    public List<RepairOrder> findByVehicle(String registration);

}
