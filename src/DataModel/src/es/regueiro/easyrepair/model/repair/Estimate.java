package es.regueiro.easyrepair.model.repair;

import es.regueiro.easyrepair.model.employee.Employee;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

public class Estimate {

    private Long id;
    private int version;
    private String estimateNumber;
    private String notes;
    private LocalDate estimateDate;
    private LocalDate acceptedDate;
    private String status;
    private BigDecimal discount;
    private Employee responsible;
    private RepairOrder order;

    
    // Constructor necesario para Hibernate
    public Estimate() {
    }
    
    public Estimate(RepairOrder order) {
        this.order = order;
    }

    public RepairOrder getOrder() {
        return order;
    }

    public boolean getEnabled() {
        return this.getOrder().getEnabled();
    }

    public LocalDate getAcceptedDate() {
        return acceptedDate;
    }

    public void setAcceptedDate(LocalDate acceptedDate) {
        if (acceptedDate != null) {
            if (this.estimateDate != null && this.estimateDate.isAfter(acceptedDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ACCEPTED_DATE_BEFORE_ESTIMATE_DATE"));
            } else {
                this.acceptedDate = acceptedDate;
            }
        } else {
            this.acceptedDate = null;
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

    public LocalDate getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(LocalDate estimateDate) {
        if (estimateDate != null) {
            if (this.acceptedDate != null && this.acceptedDate.isBefore(estimateDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ESTIMATE_DATE_AFTER_ACCEPTED_DATE"));
            } else {
                this.estimateDate = estimateDate;
            }
        } else {
            this.estimateDate = null;
        }
    }

    public String getEstimateNumber() {
        return estimateNumber;
    }

    public void setEstimateNumber(String estimateNumber) {
        if (StringUtils.isBlank(estimateNumber)) {
            this.estimateNumber = null;
        } else {
            String tempId = parseEstimateNumber(estimateNumber);
            if (isValidEstimateNumber(tempId)) {
                this.estimateNumber = tempId;
            } else {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ESTIMATE_NUMBER_INVALID"));
            }
        }
    }

    private String parseEstimateNumber(String estimateNumber) {
        long number;
        try {
            number = Long.parseLong(estimateNumber);
        } catch (NumberFormatException ignore) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ESTIMATE_NUMBER_INVALID"));
        }
        return String.format("%011d", number);
    }

    // Si es negativo o distinto de 11 digitos, no es valido
    private boolean isValidEstimateNumber(String estimateNumber) {
        if (estimateNumber.length() != 11) {
            return false;
        } else {
            try {
                if (Long.parseLong(estimateNumber) <= 0) {
                    return false;
                }
            } catch (NumberFormatException ignore) {
                return false;
            }
            return true;
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Employee getResponsible() {
        return responsible;
    }

    public void setResponsible(Employee responsible) {
        this.responsible = responsible;
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
        final Estimate other = (Estimate) obj;

        if ((this.estimateNumber == null) ? (other.estimateNumber != null) : !this.estimateNumber.equals(other.estimateNumber)) {
            return false;
        }
        if ((this.notes == null) ? (other.notes != null) : !this.notes.equals(other.notes)) {
            return false;
        }
        if (this.estimateDate != other.estimateDate && (this.estimateDate == null || !this.estimateDate.equals(other.estimateDate))) {
            return false;
        }
        if (this.acceptedDate != other.acceptedDate && (this.acceptedDate == null || !this.acceptedDate.equals(other.acceptedDate))) {
            return false;
        }
        if ((this.status == null) ? (other.status != null) : !this.status.equals(other.status)) {
            return false;
        }
        if ((this.discount != null && other.discount == null) || this.discount == null && other.discount != null || this.discount != null && this.discount.compareTo(other.discount) != 0) {
            return false;
        }
        if (this.responsible != other.responsible && (this.responsible == null || !this.responsible.equals(other.responsible))) {
            return false;
        }
        if (this.getEnabled() != other.getEnabled()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.estimateNumber != null ? this.estimateNumber.hashCode() : 0);
        hash = 17 * hash + (this.notes != null ? this.notes.hashCode() : 0);
        hash = 17 * hash + (this.estimateDate != null ? this.estimateDate.hashCode() : 0);
        hash = 17 * hash + (this.acceptedDate != null ? this.acceptedDate.hashCode() : 0);
        hash = 17 * hash + (this.status != null ? this.status.hashCode() : 0);
        hash = 17 * hash + (this.discount != null ? this.discount.hashCode() : 0);
        hash = 17 * hash + (this.responsible != null ? this.responsible.hashCode() : 0);
        hash = 17 * hash + (this.getEnabled() ? 1 : 0);
        return hash;
    }
}
