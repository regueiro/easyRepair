package es.regueiro.easyrepair.dao.stock.hibernate;

import es.regueiro.easyrepair.api.stock.controller.PartOrderController;
import es.regueiro.easyrepair.api.stock.finder.PartOrderFinder;
import es.regueiro.easyrepair.api.stock.saver.PartOrderSaver;
import es.regueiro.easyrepair.model.stock.PartLine;
import es.regueiro.easyrepair.model.stock.PartOrder;
import es.regueiro.easyrepair.model.stock.PartOrderInvoice;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.LocalDate;
import org.hibernate.HibernateException;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = PartOrderController.class,
//path = "PartOrderFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.stock.DefaultPartOrderFinder"}
)
public class PartOrderHibernateController implements PartOrderController {

    private PartOrderFinder finder = Lookup.getDefault().lookup(PartOrderFinder.class);
    private PartOrderSaver saver = Lookup.getDefault().lookup(PartOrderSaver.class);

    @Override
    public List<PartOrder> listAll() {
        List<PartOrder> list = finder.listAll();
        return list;
    }

    @Override
    public List<PartOrder> listAllEnabled() {
        List<PartOrder> list = finder.listAllEnabled();
        return list;
    }

    @Override
    public List<PartOrder> searchByOrderNumber(String number) {
        List<PartOrder> list = finder.findByOrderNumber(number);
        return list;
    }

    @Override
    public List<PartOrder> searchByStatus(String status) {
        List<PartOrder> list = finder.findByStatus(status);
        return list;
    }

    @Override
    public List<PartOrder> searchByResponsible(String name) {
        List<PartOrder> list = finder.findByResponsible(name);
        return list;
    }

    @Override
    public List<PartOrder> searchByOrderDate(String dateAfter, String dateBefore) {
        List<PartOrder> list = finder.findByOrderDate(dateAfter, dateBefore);
        return list;
    }

    @Override
    public List<PartOrder> searchByEstimatedDate(String dateAfter, String dateBefore) {
        List<PartOrder> list = finder.findByEstimatedDate(dateAfter, dateBefore);
        return list;
    }

    @Override
    public List<PartOrder> searchByReceiptDate(String dateAfter, String dateBefore) {
        List<PartOrder> list = finder.findByReceiptDate(dateAfter, dateBefore);
        return list;
    }

    @Override
    public List<PartOrder> searchByWarehouseName(String warehouse) {
        List<PartOrder> list = finder.findByWarehouseName(warehouse);
        return list;
    }

    @Override
    public List<PartOrder> searchByDiscount(String discount) {
        List<PartOrder> list = finder.findByDiscount(discount);
        return list;
    }

    @Override
    public List<PartOrder> searchByMaxDiscount(String discount) {
        List<PartOrder> list = finder.findByMaxDiscount(discount);
        return list;
    }

    @Override
    public List<PartOrder> searchByMinDiscount(String discount) {
        List<PartOrder> list = finder.findByMinDiscount(discount);
        return list;
    }

    @Override
    public List<PartOrder> searchByShippingCosts(String costs) {
        List<PartOrder> list = finder.findByShippingCosts(costs);
        return list;
    }

    @Override
    public List<PartOrder> searchByMaxShippingCosts(String costs) {
        List<PartOrder> list = finder.findByMaxShippingCosts(costs);
        return list;
    }

    @Override
    public List<PartOrder> searchByMinShippingCosts(String costs) {
        List<PartOrder> list = finder.findByMinShippingCosts(costs);
        return list;
    }

    @Override
    public List<PartOrder> searchByOtherCosts(String costs) {
        List<PartOrder> list = finder.findByOtherCosts(costs);
        return list;
    }

    @Override
    public List<PartOrder> searchByMaxOtherCosts(String costs) {
        List<PartOrder> list = finder.findByMaxOtherCosts(costs);
        return list;
    }

    @Override
    public List<PartOrder> searchByMinOtherCosts(String costs) {
        List<PartOrder> list = finder.findByMinOtherCosts(costs);
        return list;
    }

    @Override
    public List<PartOrder> searchBySupplierName(String name) {
        List<PartOrder> list = finder.findBySupplierName(name);
        return list;
    }

    @Override
    public void setPartOrder(PartOrder partOrder) {
        saver.setPartOrder(partOrder);
    }

    @Override
    public PartOrder getPartOrder() {
        return saver.getPartOrder();
    }

    @Override
    public void savePartOrder() {
        try {
            saver.savePartOrder();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                throw new RuntimeException("The partOrder was updated or deleted by another user");
            } else {
                throw e;
            }

        }
    }

    @Override
    public PartOrder reloadPartOrder() {
        PartOrder emp = finder.getPartOrder(saver.getPartOrder().getId());
        return emp;
    }

    @Override
    public PartOrder disablePartOrder() {
        try {
            saver.disablePartOrder();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setPartOrder(reloadPartOrder());
                disablePartOrder();
            } else {
                throw e;
            }

        }
        return getPartOrder();
    }

    @Override
    public PartOrder enablePartOrder() {
        try {
            saver.enablePartOrder();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setPartOrder(reloadPartOrder());
                enablePartOrder();
            } else {
                throw e;
            }

        }
        return getPartOrder();
    }

    @Override
    public void overwritePartOrder() {
        PartOrder old = getPartOrder();
        PartOrder newPartOrder = reloadPartOrder();
        if (newPartOrder == null) {
            newPartOrder = newPartOrder();
        }

        setPartOrder(newPartOrder);
        newPartOrder.setOrderNumber(old.getOrderNumber());
        newPartOrder.setOrderDate(old.getOrderDate());
        newPartOrder.setEstimatedDate(old.getEstimatedDate());
        newPartOrder.setReceiptDate(old.getReceiptDate());
        newPartOrder.setShippingWarehouse(old.getShippingWarehouse());
        if (old.getShippingCosts() != null) {
            newPartOrder.setShippingCosts(old.getShippingCosts().toString());
        }
        if (old.getOtherCosts() != null) {
            newPartOrder.setOtherCosts(old.getOtherCosts().toString());
        }
        if (old.getDiscount() != null) {
            newPartOrder.setDiscount(old.getDiscount().toString());
        }
        newPartOrder.setNotes(old.getNotes());
        newPartOrder.setStatus(old.getStatus());
        newPartOrder.setSupplier(old.getSupplier());
        newPartOrder.setResponsible(old.getResponsible());

        newPartOrder.setEnabled(old.getEnabled());


        PartOrderInvoice inv = newPartOrder.getInvoice();
        inv.setInvoiceNumber(old.getInvoice().getInvoiceNumber());
        inv.setNotes(old.getInvoice().getNotes());
        inv.setStatus(old.getInvoice().getStatus());
        inv.setPaymentMethod(old.getInvoice().getPaymentMethod());
        inv.setResponsible(old.getInvoice().getResponsible());
        inv.setAcceptedDate(old.getInvoice().getAcceptedDate());
        inv.setEstimatedPaymentDate(old.getInvoice().getEstimatedPaymentDate());
        inv.setInvoiceDate(old.getInvoice().getInvoiceDate());
        inv.setPaymentDate(old.getInvoice().getPaymentDate());


        if (old.getPartsList()
                != null) {
            List<PartLine> oldList = old.getPartsList();
            List<PartLine> newList = newPartOrder.getPartsList();
            List<Long> newId = new ArrayList<Long>();

            if (newList != null) {
                for (PartLine e : newList) {
                    newId.add(e.getId());
                }
            }
            newPartOrder.clearPartsList();

            for (PartLine e : oldList) {
                PartLine temp = new PartLine();
                if (e.getDiscount() != null) {
                    temp.setDiscount(e.getDiscount().toString());
                }
                temp.setPart(e.getPart());
                temp.setQuantity(e.getQuantity());
                if (newId.contains(e.getId())) {
                    temp.setId(e.getId());
                }
                newPartOrder.addPart(temp);
            }
        } else {
            if (newPartOrder.getPartsList() != null) {
                newPartOrder.clearPartsList();
            }
        }

        saver.setPartOrder(newPartOrder);

        saver.savePartOrder();
    }

    @Override
    public PartOrder newPartOrder() {
        PartOrder emp = new PartOrder();
        return emp;
    }

    @Override
    public void deletePartOrder() {
        try {
            saver.deletePartOrder();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setPartOrder(reloadPartOrder());
                deletePartOrder();
            } else {
                throw e;
            }
        }
    }

    @Override
    public PartOrder getPartOrderById(Long id) {
        return finder.getPartOrder(id);
    }
}
