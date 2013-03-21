package es.regueiro.easyrepair.model.repair;

import es.regueiro.easyrepair.model.client.Vehicle;
import es.regueiro.easyrepair.model.employee.Employee;
import es.regueiro.easyrepair.model.stock.PartLine;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

/**
 *
 * @author Santiago Regueiro Ovelleiro
 */
public class RepairOrder {

    private Long id;
    private int version;
    private boolean enabled = true;
    private LocalDate orderDate;
    private LocalDate estimatedDate;
    private LocalDate finishDate;
    private LocalDate deliveryDate;
    private String kilometres;
    private String gasTankLevel;
    private String description;
    private String status;
    private String notes;
    private Estimate estimate;
    private Employee responsible;
    private List<LabourLine> labourList;
    private List<PartLine> partsList;
    private Vehicle vehicle;
    private String orderNumber;
    private RepairInvoice invoice;

    /**
     * Crea una nueva orden de reparación. Genera automáticamente una factura y
     * un presupuesto.
     */
    public RepairOrder() {
        this.invoice = new RepairInvoice(this);
        this.estimate = new Estimate(this);
    }

    /**
     * Devuelve la factura de la reparación
     *
     * @return la factura de la reparación
     */
    public RepairInvoice getInvoice() {
        return invoice;
    }

    /**
     * Devuelve el número de orden
     *
     * @return el número de orden
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * Asigna el número de orden
     *
     * @param orderNumber el número de orden a asignar
     */
    public void setOrderNumber(String orderNumber) {
        if (StringUtils.isBlank(orderNumber)) {
            this.orderNumber = null;
        } else {
            String tempId = parseOrderNumber(orderNumber);
            if (isValidOrderNumber(tempId)) {
                this.orderNumber = tempId;
            } else {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("REPAIRORDER_NUMBER_INVALID"));
            }
        }
    }

    private String parseOrderNumber(String orderNumber) {
        long number;
        try {
            number = Long.parseLong(orderNumber);
        } catch (NumberFormatException ignore) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("REPAIRORDER_NUMBER_INVALID"));
        }
        return String.format("%011d", number);
    }

    private boolean isValidOrderNumber(String orderNumber) {
        if (orderNumber.length() != 11) {
            return false;
        } else {
            try {
                if (Long.parseLong(orderNumber) <= 0) {
                    return false;
                }
            } catch (NumberFormatException ignore) {
                return false;
            }
            return true;
        }

    }

    /**
     * Devuelve el vehículo correspondiente a la orden
     *
     * @return el vehículo correspondiente a la orden
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Asigna el vehículo correspondiente a la orden
     *
     * @param vehicle el vehículo correspondiente a la orden
     */
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * Devuelve la fecha de entrega
     *
     * @return la fecha de entrega
     */
    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Asigna la fecha de entrega
     *
     * @param deliveryDate la fecha de entrega
     */
    public void setDeliveryDate(LocalDate deliveryDate) {
        if (deliveryDate != null) {
            if (this.orderDate != null && this.orderDate.isAfter(deliveryDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("DELIVERY_DATE_BEFORE_ORDER_DATE"));
            } else if (this.finishDate != null && this.finishDate.isAfter(deliveryDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("DELIVERY_DATE_BEFORE_FINISH_DATE"));
            } else {
                this.deliveryDate = deliveryDate;
            }
        } else {
            this.deliveryDate = null;
        }
    }

    /**
     * Devuelve la descripción
     *
     * @return la descripción
     */
    public String getDescription() {
        return description;
    }

    /**
     * Asigna la descripción
     *
     * @param description la descripción
     */
    public void setDescription(String description) {
        if (StringUtils.isBlank(description)) {
            this.description = null;
        } else {
            this.description = description;
        }
    }

    /**
     * Devuelve si la orden está habilitada o no
     *
     * @return true si la orden está habilitada. false en caso contrario
     */
    public boolean getEnabled() {
        return enabled;
    }

    /**
     *
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     *
     * @return
     */
    public Estimate getEstimate() {
        return estimate;
    }

    /**
     *
     * @return
     */
    public LocalDate getEstimatedDate() {
        return estimatedDate;
    }

    /**
     *
     * @param estimatedDate
     */
    public void setEstimatedDate(LocalDate estimatedDate) {
        if (estimatedDate != null) {
            if (this.orderDate != null && this.orderDate.isAfter(estimatedDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ESTIMATED_DATE_BEFORE_ORDER_DATE"));
            } else {
                this.estimatedDate = estimatedDate;
            }
        } else {
            this.estimatedDate = null;
        }
    }

    /**
     *
     * @return
     */
    public LocalDate getFinishDate() {
        return finishDate;
    }

    /**
     *
     * @param finishDate
     */
    public void setFinishDate(LocalDate finishDate) {
        if (finishDate != null) {
            if (this.orderDate != null && this.orderDate.isAfter(finishDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("FINISH_DATE_BEFORE_ORDER_DATE"));
            } else if (this.deliveryDate != null && this.deliveryDate.isBefore(finishDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("FINISH_DATE_AFTER_DELIVERY_DATE"));
            } else {
                this.finishDate = finishDate;
            }
        } else {
            this.finishDate = null;
        }
    }

    /**
     *
     * @return
     */
    public String getGasTankLevel() {
        return gasTankLevel;
    }

    /**
     *
     * @param gasTankLevel
     */
    public void setGasTankLevel(String gasTankLevel) {
        if (StringUtils.isBlank(gasTankLevel)) {
            this.gasTankLevel = null;
        } else {
            this.gasTankLevel = gasTankLevel;
        }
    }

    /**
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getKilometres() {
        return kilometres;
    }

    /**
     *
     * @param kilometres
     */
    public void setKilometres(String kilometres) {
        if (StringUtils.isBlank(kilometres)) {
            this.kilometres = null;
        } else {
            long number;
            try {
                number = Long.parseLong(kilometres);
                if (!(number < 0 || kilometres.length() > 10))  {
                    this.kilometres = kilometres;
                } else {
                    throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("KILOMETRES_INVALID"));
                }
            } catch (NumberFormatException ignore) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("KILOMETRES_INVALID"));
            }
        }
    }


    /**
     *
     * @param partsList
     */
    public void setPartsList(List<PartLine> partsList) {
        this.partsList = partsList;
    }

    /**
     *
     * @return
     */
    public List<PartLine> getPartsList() {
        if (this.partsList != null) {
            return Collections.unmodifiableList(this.partsList);
        } else {
            return null;
        }
    }

    /**
     *
     * @param part
     */
    public void addPart(PartLine part) {
        if (this.partsList == null) {
            this.partsList = new ArrayList<PartLine>();
        }
        if (!this.partsList.contains(part)) {
            this.partsList.add(part);
        }
    }

    /**
     *
     * @param partId
     */
    public void removePart(Long partId) {
        if (this.partsList != null) {
            Iterator<PartLine> iterator = this.partsList.iterator();
            while (iterator.hasNext()) {
                PartLine p = iterator.next();
                if (p.getPart().getId().compareTo(partId) == 0) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     *
     * @param part
     */
    public void removePartLine(PartLine part) {
        if (this.partsList != null) {
            Iterator<PartLine> iterator = this.partsList.iterator();
            while (iterator.hasNext()) {
                PartLine p = iterator.next();
                if (p.equals(part)) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     *
     * @param partId
     * @return
     */
    public boolean hasPart(Long partId) {
        if (getPartsList() == null || getPartsList().isEmpty()) {
            return false;
        }

        List<PartLine> currentParts = getPartsList();

        for (PartLine p : currentParts) {
            if (p.getPart().getId().compareTo(partId) == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     */
    public void clearPartsList() {
        if (this.partsList != null) {
            this.partsList.clear();
        } else {
            this.partsList = new ArrayList<PartLine>();
        }
    }

    /**
     *
     * @param labourList
     */
    public void setLabourList(List<LabourLine> labourList) {
        this.labourList = labourList;
    }

    /**
     *
     * @return
     */
    public List<LabourLine> getLabourList() {
        if (this.labourList != null) {
            return Collections.unmodifiableList(this.labourList);
        } else {
            return null;
        }
    }

    /**
     *
     * @param labour
     */
    public void addLabour(LabourLine labour) {
        if (this.labourList == null) {
            this.labourList = new ArrayList<LabourLine>();
        }
        if (!this.labourList.contains(labour)) {
            this.labourList.add(labour);
        }
    }

    /**
     *
     * @param labourId
     */
    public void removeLabour(Long labourId) {
        if (this.labourList != null) {
            Iterator<LabourLine> iterator = this.labourList.iterator();
            while (iterator.hasNext()) {
                LabourLine p = iterator.next();
                if (p.getLabour().getId().compareTo(labourId) == 0) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     *
     * @param labour
     */
    public void removeLabourLine(LabourLine labour) {
        if (this.labourList != null) {
            Iterator<LabourLine> iterator = this.labourList.iterator();
            while (iterator.hasNext()) {
                LabourLine p = iterator.next();
                if (p.equals(labour)) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     *
     * @param labourId
     * @return
     */
    public boolean hasLabour(Long labourId) {
        if (getLabourList() == null || getLabourList().isEmpty()) {
            return false;
        }

        List<LabourLine> currentLabour = getLabourList();

        for (LabourLine p : currentLabour) {
            if (p.getLabour().getId().compareTo(labourId) == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     */
    public void clearLabourList() {
        if (this.labourList != null) {
            this.labourList.clear();
        } else {
            this.labourList = new ArrayList<LabourLine>();
        }
    }

    /**
     *
     * @return
     */
    public String getNotes() {
        return notes;
    }

    /**
     *
     * @param notes
     */
    public void setNotes(String notes) {
        if (StringUtils.isBlank(notes)) {
            this.notes = null;
        } else {
            this.notes = notes;
        }
    }

    /**
     *
     * @return
     */
    public LocalDate getOrderDate() {
        return orderDate;
    }

    /**
     *
     * @param orderDate
     */
    public void setOrderDate(LocalDate orderDate) {
        if (orderDate != null) {
            if (this.deliveryDate != null && this.deliveryDate.isBefore(orderDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("REPAIRORDER_DATE_AFTER_DELIVERY_DATE"));
            } else if (this.finishDate != null && this.finishDate.isBefore(orderDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("REPAIRORDER_DATE_AFTER_FINISH_DATE"));
            } else if (this.estimatedDate != null && this.estimatedDate.isBefore(orderDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("REPAIRORDER_DATE_AFTER_ESTIMATED_DATE"));
            } else {
                this.orderDate = orderDate;
            }
        } else {
            this.orderDate = null;
        }
    }

    /**
     *
     * @return
     */
    public Employee getResponsible() {
        return responsible;
    }

    /**
     *
     * @param responsible
     */
    public void setResponsible(Employee responsible) {
        this.responsible = responsible;
    }

    /**
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        if (StringUtils.isBlank(status)) {
            this.status = null;
        } else {
            this.status = status;
        }
    }

    /**
     *
     * @return
     */
    public int getVersion() {
        return version;
    }

    /**
     *
     * @param version
     */
    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RepairOrder other = (RepairOrder) obj;
        if (this.enabled != other.enabled) {
            return false;
        }
        if (this.orderDate != other.orderDate && (this.orderDate == null || !this.orderDate.equals(other.orderDate))) {
            return false;
        }
        if (this.estimatedDate != other.estimatedDate && (this.estimatedDate == null || !this.estimatedDate.equals(other.estimatedDate))) {
            return false;
        }
        if (this.finishDate != other.finishDate && (this.finishDate == null || !this.finishDate.equals(other.finishDate))) {
            return false;
        }
        if (this.deliveryDate != other.deliveryDate && (this.deliveryDate == null || !this.deliveryDate.equals(other.deliveryDate))) {
            return false;
        }
        if ((this.kilometres == null) ? (other.kilometres != null) : !this.kilometres.equals(other.kilometres)) {
            return false;
        }
        if ((this.gasTankLevel == null) ? (other.gasTankLevel != null) : !this.gasTankLevel.equals(other.gasTankLevel)) {
            return false;
        }
        if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
            return false;
        }
        if ((this.status == null) ? (other.status != null) : !this.status.equals(other.status)) {
            return false;
        }
        if ((this.notes == null) ? (other.notes != null) : !this.notes.equals(other.notes)) {
            return false;
        }
        if (this.estimate != other.estimate && (this.estimate == null || !this.estimate.equals(other.estimate))) {
            return false;
        }
        if (this.responsible != other.responsible && (this.responsible == null || !this.responsible.equals(other.responsible))) {
            return false;
        }

        if (((other.getPartsList() == null || other.getPartsList().isEmpty()) && (this.getPartsList() != null && !this.getPartsList().isEmpty()))
                || ((other.getPartsList() != null && !other.getPartsList().isEmpty()) && (this.getPartsList() == null || this.getPartsList().isEmpty()))
                || ((other.getPartsList() != null && !other.getPartsList().isEmpty())
                && !(other.getPartsList().containsAll(this.getPartsList()) && other.getPartsList().size() == this.getPartsList().size()))) {
            return false;
        }

        if (((other.getLabourList() == null || other.getLabourList().isEmpty()) && (this.getLabourList() != null && !this.getLabourList().isEmpty()))
                || ((other.getLabourList() != null && !other.getLabourList().isEmpty()) && (this.getLabourList() == null || this.getLabourList().isEmpty()))
                || ((other.getLabourList() != null && !other.getLabourList().isEmpty())
                && !(other.getLabourList().containsAll(this.getLabourList()) && other.getLabourList().size() == this.getLabourList().size()))) {
            return false;
        }
        if (this.vehicle != other.vehicle && (this.vehicle == null || !this.vehicle.equals(other.vehicle))) {
            return false;
        }
        if ((this.orderNumber == null) ? (other.orderNumber != null) : !this.orderNumber.equals(other.orderNumber)) {
            return false;
        }
        if (this.invoice != other.invoice && (this.invoice == null || !this.invoice.equals(other.invoice))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.enabled ? 1 : 0);
        hash = 47 * hash + (this.orderDate != null ? this.orderDate.hashCode() : 0);
        hash = 47 * hash + (this.estimatedDate != null ? this.estimatedDate.hashCode() : 0);
        hash = 47 * hash + (this.finishDate != null ? this.finishDate.hashCode() : 0);
        hash = 47 * hash + (this.deliveryDate != null ? this.deliveryDate.hashCode() : 0);
        hash = 47 * hash + (this.kilometres != null ? this.kilometres.hashCode() : 0);
        hash = 47 * hash + (this.gasTankLevel != null ? this.gasTankLevel.hashCode() : 0);
        hash = 47 * hash + (this.description != null ? this.description.hashCode() : 0);
        hash = 47 * hash + (this.status != null ? this.status.hashCode() : 0);
        hash = 47 * hash + (this.notes != null ? this.notes.hashCode() : 0);
        hash = 47 * hash + (this.estimate != null ? this.estimate.hashCode() : 0);
        hash = 47 * hash + (this.responsible != null ? this.responsible.hashCode() : 0);
        hash = 47 * hash + (this.labourList != null ? this.labourList.hashCode() : 0);
        hash = 47 * hash + (this.partsList != null ? this.partsList.hashCode() : 0);
        hash = 47 * hash + (this.vehicle != null ? this.vehicle.hashCode() : 0);
        hash = 47 * hash + (this.orderNumber != null ? this.orderNumber.hashCode() : 0);
        hash = 47 * hash + (this.invoice != null ? this.invoice.hashCode() : 0);
        return hash;
    }
}
