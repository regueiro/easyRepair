package es.regueiro.easyrepair.api.stock.controller;

import es.regueiro.easyrepair.model.stock.PartOrder;
import java.util.List;

public interface PartOrderController {

    public List<PartOrder> listAll();
    
    public List<PartOrder> listAllEnabled();
    
    public List<PartOrder> searchByOrderNumber(String number);

    public List<PartOrder> searchByOrderDate(String dateAfter, String dateBefore);

    public List<PartOrder> searchByEstimatedDate(String dateAfter, String dateBefore);

    public List<PartOrder> searchByReceiptDate(String dateAfter, String dateBefore);

    public List<PartOrder> searchByWarehouseName(String warehouse);

    public List<PartOrder> searchByDiscount(String discount);

    public List<PartOrder> searchByMaxDiscount(String discount);

    public List<PartOrder> searchByMinDiscount(String discount);

    public List<PartOrder> searchByShippingCosts(String costs);

    public List<PartOrder> searchByMaxShippingCosts(String costs);

    public List<PartOrder> searchByMinShippingCosts(String costs);

    public List<PartOrder> searchByOtherCosts(String costs);

    public List<PartOrder> searchByMaxOtherCosts(String costs);

    public List<PartOrder> searchByMinOtherCosts(String costs);

    public List<PartOrder> searchByStatus(String status);

    public List<PartOrder> searchBySupplierName(String name);

    public List<PartOrder> searchByResponsible(String name);

    public void setPartOrder(PartOrder partOrder);

    public PartOrder getPartOrder();

    public void savePartOrder();

    public PartOrder reloadPartOrder();

    public PartOrder disablePartOrder();

    public PartOrder enablePartOrder();

    public void overwritePartOrder();

    public void deletePartOrder();

    public PartOrder newPartOrder();

    public PartOrder getPartOrderById(Long id);
}
