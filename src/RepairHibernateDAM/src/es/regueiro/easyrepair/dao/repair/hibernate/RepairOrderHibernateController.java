package es.regueiro.easyrepair.dao.repair.hibernate;

import es.regueiro.easyrepair.api.repair.controller.RepairOrderController;
import es.regueiro.easyrepair.api.repair.finder.RepairOrderFinder;
import es.regueiro.easyrepair.api.repair.saver.RepairOrderSaver;
import es.regueiro.easyrepair.model.repair.LabourLine;
import es.regueiro.easyrepair.model.repair.RepairOrder;
import es.regueiro.easyrepair.model.stock.PartLine;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.joda.time.LocalDate;

@ServiceProvider(service = RepairOrderController.class,
//path = "RepairOrderFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.repairOrder.DefaultRepairOrderFinder"}
)
public class RepairOrderHibernateController implements RepairOrderController {

    private RepairOrderFinder finder = Lookup.getDefault().lookup(RepairOrderFinder.class);
    private RepairOrderSaver saver = Lookup.getDefault().lookup(RepairOrderSaver.class);

    @Override
    public List<RepairOrder> listAll() {
        List<RepairOrder> list = finder.listAll();
        return list;
    }

    @Override
    public List<RepairOrder> listAllEnabled() {
        List<RepairOrder> list = finder.listAllEnabled();
        return list;
    }

    @Override
    public List<RepairOrder> searchByRepairOrderNumber(String repairOrderNumber) {
        List<RepairOrder> list = finder.findByRepairOrderNumber(repairOrderNumber);
        return list;
    }

    @Override
    public List<RepairOrder> searchByStatus(String status) {
        List<RepairOrder> list = finder.findByStatus(status);
        return list;
    }

    @Override
    public List<RepairOrder> searchByResponsible(String name) {
        List<RepairOrder> list = finder.findByResponsible(name);
        return list;
    }

    @Override
    public List<RepairOrder> searchByOrderDate(String dateAfter, String dateBefore) {
        List<RepairOrder> list = finder.findByOrderDate(dateAfter, dateBefore);
        return list;
    }

    @Override
    public List<RepairOrder> searchByEstimatedDate(String dateAfter, String dateBefore) {
        List<RepairOrder> list = finder.findByEstimatedDate(dateAfter, dateBefore);
        return list;
    }

    @Override
    public List<RepairOrder> searchByFinishDate(String dateAfter, String dateBefore) {
        List<RepairOrder> list = finder.findByFinishDate(dateAfter, dateBefore);
        return list;
    }

    @Override
    public List<RepairOrder> searchByDeliveryDate(String dateAfter, String dateBefore) {
        List<RepairOrder> list = finder.findByDeliveryDate(dateAfter, dateBefore);
        return list;
    }

    @Override
    public List<RepairOrder> searchByKilometres(String kilometres) {
        List<RepairOrder> list = finder.findByKilometres(kilometres);
        return list;
    }

    @Override
    public List<RepairOrder> searchByGasTankLevel(String tankLevel) {
        List<RepairOrder> list = finder.findByGasTankLevel(tankLevel);
        return list;
    }

    @Override
    public List<RepairOrder> searchByDiscount(String discount) {
        List<RepairOrder> list = finder.findByDiscount(discount);
        return list;
    }

    @Override
    public List<RepairOrder> searchByEstimateNumber(String estimateNumber) {
        List<RepairOrder> list = finder.findByEstimateNumber(estimateNumber);
        return list;
    }

    @Override
    public List<RepairOrder> searchByVehicle(String registration) {
        List<RepairOrder> list = finder.findByVehicle(registration);
        return list;
    }

    @Override
    public void setRepairOrder(RepairOrder repairOrder) {
        saver.setRepairOrder(repairOrder);
    }

    @Override
    public RepairOrder getRepairOrder() {
        return saver.getRepairOrder();
    }

    @Override
    public void saveRepairOrder() {
        try {
            saver.saveRepairOrder();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                throw new RuntimeException("The repairOrder was updated or deleted by another user");
            } else {
                throw e;
            }

        }
    }

    @Override
    public RepairOrder reloadRepairOrder() {
        RepairOrder emp = finder.getRepairOrder(saver.getRepairOrder().getId());
        return emp;
    }

    @Override
    public RepairOrder disableRepairOrder() {
        try {
            saver.disableRepairOrder();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setRepairOrder(reloadRepairOrder());
                disableRepairOrder();
            } else {
                throw e;
            }

        }
        return getRepairOrder();
    }

    @Override
    public RepairOrder enableRepairOrder() {
        try {
            saver.enableRepairOrder();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setRepairOrder(reloadRepairOrder());
                enableRepairOrder();
            } else {
                throw e;
            }

        }
        return getRepairOrder();
    }

    @Override
    public void overwriteRepairOrder() {
        RepairOrder old = getRepairOrder();
        RepairOrder newRepairOrder = reloadRepairOrder();
        if (newRepairOrder == null) {
            newRepairOrder = newRepairOrder();
        }
        setRepairOrder(newRepairOrder);
        newRepairOrder.setVehicle(old.getVehicle());
        newRepairOrder.setOrderDate(old.getOrderDate());
        newRepairOrder.setDeliveryDate(old.getDeliveryDate());
        newRepairOrder.setEstimatedDate(old.getEstimatedDate());
        newRepairOrder.setFinishDate(old.getFinishDate());
        newRepairOrder.setDescription(old.getDescription());
        newRepairOrder.setGasTankLevel(old.getGasTankLevel());
        if (old.getKilometres() != null) {
            newRepairOrder.setKilometres(old.getKilometres().toString());
        }
        newRepairOrder.setNotes(old.getNotes());
        newRepairOrder.setOrderNumber(old.getOrderNumber());
        newRepairOrder.setStatus(old.getStatus());
        newRepairOrder.setResponsible(old.getResponsible());
        newRepairOrder.setEnabled(old.getEnabled());


        if (old.getLabourList() != null) {
            List<LabourLine> oldList = old.getLabourList();
            List<LabourLine> newList = newRepairOrder.getLabourList();
            List<Long> newId = new ArrayList<Long>();

            if (newList != null) {
                for (LabourLine e : newList) {
                    newId.add(e.getId());
                }
            }
            newRepairOrder.clearLabourList();

            for (LabourLine e : oldList) {
                LabourLine temp = new LabourLine();
                if (temp.getDiscount() != null) {
                    temp.setDiscount(e.getDiscount().toString());
                }
                if (temp.getHours() != null) {
                    temp.setHours(e.getHours().toString());
                }
                temp.setLabour(e.getLabour());
                if (newId.contains(e.getId())) {
                    temp.setId(e.getId());
                }
                newRepairOrder.addLabour(temp);
            }
        } else {
            if (newRepairOrder.getLabourList() != null) {
                newRepairOrder.clearLabourList();
            }
        }

        if (old.getPartsList() != null) {
            List<PartLine> oldList = old.getPartsList();
            List<PartLine> newList = newRepairOrder.getPartsList();
            List<Long> newId = new ArrayList<Long>();

            if (newList != null) {
                for (PartLine e : newList) {
                    newId.add(e.getId());
                }
            }
            newRepairOrder.clearPartsList();

            for (PartLine e : oldList) {
                PartLine temp = new PartLine();
                if (temp.getDiscount() != null) {
                    temp.setDiscount(e.getDiscount().toString());
                }
                temp.setPart(e.getPart());
                temp.setQuantity(e.getQuantity());
                if (newId.contains(e.getId())) {
                    temp.setId(e.getId());
                }
                newRepairOrder.addPart(temp);
            }
        } else {
            if (newRepairOrder.getPartsList() != null) {
                newRepairOrder.clearPartsList();
            }
        }

        saver.setRepairOrder(newRepairOrder);
        saver.saveRepairOrder();
    }

    @Override
    public RepairOrder newRepairOrder() {
        RepairOrder emp = new RepairOrder();
        return emp;
    }

    @Override
    public void deleteRepairOrder() {

        try {
            saver.deleteRepairOrder();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setRepairOrder(reloadRepairOrder());
                deleteRepairOrder();
            } else {
                throw e;
            }
        }
    }

    @Override
    public RepairOrder getRepairOrderById(Long id) {
        return finder.getRepairOrder(id);
    }
}
