package es.regueiro.easyrepair.model.stock;

import es.regueiro.easyrepair.model.employee.Employee;
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
 * @author Santi
 */
public class PartOrder {

    private Long id;
    private int version;
    private boolean enabled = true;
    private String orderNumber;
    private LocalDate orderDate;
    private LocalDate estimatedDate;
    private LocalDate receiptDate;
    private Warehouse shippingWarehouse;
    private BigDecimal shippingCosts;
    private BigDecimal otherCosts;
    private BigDecimal discount;
    private String status;
    private String notes;
    private Supplier supplier;
    private Employee responsible;
    private List<PartLine> partsList;
    private PartOrderInvoice invoice;

    public PartOrder() {
        this.invoice = new PartOrderInvoice(this);
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        if (StringUtils.isBlank(orderNumber)) {
            this.orderNumber = null;
        } else {
            String tempId = parseOrderNumber(orderNumber);
            if (isValidOrderNumber(tempId)) {
                this.orderNumber = tempId;
            } else {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ORDER_NUMBER_INVALID"));
            }
        }
    }

    private String parseOrderNumber(String orderNumber) {
        long number;
        try {
            number = Long.parseLong(orderNumber);

        } catch (NumberFormatException ignore) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ORDER_NUMBER_INVALID"));
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

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        if (discount != null) {
            try {
                BigDecimal disc = new BigDecimal(discount);
                disc.setScale(2, RoundingMode.HALF_UP);
                if (disc.compareTo(new BigDecimal("100")) == 1) {
                    throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("DISCOUNT_NOT_GREATER_100"));
                } else if (disc.compareTo(new BigDecimal("0")) == -1) {
                    throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("DISCOUNT_NOT_NEGATIVE"));
                } else {
                    this.discount = disc;
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("DISCOUNT_INVALID"));
            }
        } else {
            this.discount = new BigDecimal("0");
        }

    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDate getEstimatedDate() {
        return estimatedDate;
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PartOrderInvoice getInvoice() {
        return invoice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        if (StringUtils.isBlank(notes)) {
            this.notes = null;
        } else {
            this.notes = notes;
        }
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        if (orderDate != null) {
            if (this.receiptDate != null && this.receiptDate.isBefore(orderDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ORDER_DATE_AFTER_RECEIPT_DATE"));
            } else if (this.estimatedDate != null && this.estimatedDate.isBefore(orderDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ORDER_DATE_AFTER_ESTIMATED_DATE"));
            } else {
                this.orderDate = orderDate;
            }
        } else {
            this.orderDate = null;
        }
    }

    public BigDecimal getOtherCosts() {
        return otherCosts;
    }

    public void setOtherCosts(String costs) {
        if (costs != null) {
            try {
                BigDecimal co = new BigDecimal(costs);
                co.setScale(2, RoundingMode.HALF_UP);
                this.otherCosts = co;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("OTHER_COST_INVALID"));
            }
        } else {
            this.otherCosts = new BigDecimal("0");
        }

    }

    public void setPartsList(List<PartLine> partsList) {
        this.partsList = partsList;
    }

    public List<PartLine> getPartsList() {
        if (this.partsList != null) {
            return Collections.unmodifiableList(this.partsList);
        } else {
            return null;
        }
    }

    public void addPart(PartLine part) {
        if (this.partsList == null) {
            this.partsList = new ArrayList<PartLine>();
        }
        if (!this.partsList.contains(part)) {
            this.partsList.add(part);
        }
    }

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

    public void clearPartsList() {
        if (this.partsList != null) {
            this.partsList.clear();
        } else {
            this.partsList = new ArrayList<PartLine>();
        }
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        if (receiptDate != null) {
            if (this.orderDate != null && this.orderDate.isAfter(receiptDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("RECEIPT_DATE_BEFORE_ORDER_DATE"));
            } else {
                this.receiptDate = receiptDate;
            }
        } else {
            this.receiptDate = null;
        }
    }

    public Employee getResponsible() {
        return responsible;
    }

    public void setResponsible(Employee responsible) {
        this.responsible = responsible;
    }

    public BigDecimal getShippingCosts() {
        return shippingCosts;
    }

    public void setShippingCosts(String costs) {
        if (costs != null) {
            try {
                BigDecimal co = new BigDecimal(costs);
                co.setScale(2, RoundingMode.HALF_UP);
                this.shippingCosts = co;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("SHIPPING_COST_INVALID"));
            }
        } else {
            this.shippingCosts = new BigDecimal("0");
        }

    }

    public Warehouse getShippingWarehouse() {
        return shippingWarehouse;
    }

    public void setShippingWarehouse(Warehouse shippingWarehouse) {
        this.shippingWarehouse = shippingWarehouse;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (StringUtils.isBlank(status)) {
            this.status = null;
        } else {
            this.status = status;
        }
    }

    // Not to use outside of Hibernate
    public void setInvoice(PartOrderInvoice invoice) {
        this.invoice = invoice;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public int getVersion() {
        return version;
    }

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
        final PartOrder other = (PartOrder) obj;
        if (this.enabled != other.enabled) {
            return false;
        }
        if ((this.orderNumber == null) ? (other.orderNumber != null) : !this.orderNumber.equals(other.orderNumber)) {
            return false;
        }
        if (this.orderDate != other.orderDate && (this.orderDate == null || !this.orderDate.equals(other.orderDate))) {
            return false;
        }
        if (this.estimatedDate != other.estimatedDate && (this.estimatedDate == null || !this.estimatedDate.equals(other.estimatedDate))) {
            return false;
        }
        if (this.receiptDate != other.receiptDate && (this.receiptDate == null || !this.receiptDate.equals(other.receiptDate))) {
            return false;
        }
        if (this.shippingWarehouse != other.shippingWarehouse && (this.shippingWarehouse == null || !this.shippingWarehouse.equals(other.shippingWarehouse))) {
            return false;
        }
        if ((this.shippingCosts != null && other.shippingCosts == null) || this.shippingCosts == null && other.shippingCosts != null || this.shippingCosts != null && this.shippingCosts.compareTo(other.shippingCosts) != 0)  {
            return false;
        }
        if ((this.otherCosts != null && other.otherCosts == null) || this.otherCosts == null && other.otherCosts != null || this.otherCosts != null && this.otherCosts.compareTo(other.otherCosts) != 0)  {
            return false;
        }
        if ((this.discount != null && other.discount == null) || this.discount == null && other.discount != null || this.discount != null && this.discount.compareTo(other.discount) != 0)  {
            return false;
        }
        if ((this.status == null) ? (other.status != null) : !this.status.equals(other.status)) {
            return false;
        }
        if ((this.notes == null) ? (other.notes != null) : !this.notes.equals(other.notes)) {
            return false;
        }
        if (this.supplier != other.supplier && (this.supplier == null || !this.supplier.equals(other.supplier))) {
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
        if (this.invoice != other.invoice && (this.invoice == null || !this.invoice.equals(other.invoice))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.enabled ? 1 : 0);
        hash = 61 * hash + (this.orderDate != null ? this.orderDate.hashCode() : 0);
        hash = 61 * hash + (this.estimatedDate != null ? this.estimatedDate.hashCode() : 0);
        hash = 61 * hash + (this.receiptDate != null ? this.receiptDate.hashCode() : 0);
        hash = 61 * hash + (this.shippingWarehouse != null ? this.shippingWarehouse.hashCode() : 0);
        hash = 61 * hash + (this.shippingCosts != null ? this.shippingCosts.hashCode() : 0);
        hash = 61 * hash + (this.otherCosts != null ? this.otherCosts.hashCode() : 0);
        hash = 61 * hash + (this.discount != null ? this.discount.hashCode() : 0);
        hash = 61 * hash + (this.status != null ? this.status.hashCode() : 0);
        hash = 61 * hash + (this.notes != null ? this.notes.hashCode() : 0);
        hash = 61 * hash + (this.supplier != null ? this.supplier.hashCode() : 0);
        hash = 61 * hash + (this.responsible != null ? this.responsible.hashCode() : 0);
        hash = 61 * hash + (this.getPartsList() != null ? this.getPartsList().hashCode() : 0);
        hash = 61 * hash + (this.invoice != null ? this.invoice.hashCode() : 0);
        return hash;
    }
}
