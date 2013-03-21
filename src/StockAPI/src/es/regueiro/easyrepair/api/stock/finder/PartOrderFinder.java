package es.regueiro.easyrepair.api.stock.finder;

import es.regueiro.easyrepair.model.stock.PartOrder;
import java.util.List;

public interface PartOrderFinder {

    public List<PartOrder> listAll();
    
    public List<PartOrder> listAllEnabled();
    
    public List<PartOrder> findByOrderNumber(String number);

    public List<PartOrder> findByOrderDate(String dateAfter, String dateBefore);

    public List<PartOrder> findByEstimatedDate(String dateAfter, String dateBefore);

    public List<PartOrder> findByReceiptDate(String dateAfter, String dateBefore);

    public List<PartOrder> findByWarehouseName(String warehouse);

    public List<PartOrder> findByDiscount(String discount);

    public List<PartOrder> findByMaxDiscount(String discount);

    public List<PartOrder> findByMinDiscount(String discount);

    public List<PartOrder> findByShippingCosts(String costs);

    public List<PartOrder> findByMaxShippingCosts(String costs);

    public List<PartOrder> findByMinShippingCosts(String costs);

    public List<PartOrder> findByOtherCosts(String costs);

    public List<PartOrder> findByMaxOtherCosts(String costs);

    public List<PartOrder> findByMinOtherCosts(String costs);

    public List<PartOrder> findByStatus(String status);

    public List<PartOrder> findBySupplierName(String name);

    public List<PartOrder> findByResponsible(String name);

    public PartOrder getPartOrder(Long id);
}
