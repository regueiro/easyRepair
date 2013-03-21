package es.regueiro.easyrepair.model.shared;

import es.regueiro.easyrepair.model.employee.Employee;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

public class Invoice {

    private String invoiceNumber;
    private LocalDate invoiceDate;
    private LocalDate acceptedDate;
    private LocalDate estimatedPaymentDate;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String status;
    private String notes;
    private Employee responsible;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        if (StringUtils.isBlank(paymentMethod)) {
            this.paymentMethod = null;
        } else {
            this.paymentMethod = paymentMethod;
        }
    }

    public LocalDate getAcceptedDate() {
        return acceptedDate;
    }

    public void setAcceptedDate(LocalDate acceptedDate) {
        if (acceptedDate != null) {
            if (this.invoiceDate != null && this.invoiceDate.isAfter(acceptedDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ACCEPTED_DATE_BEFORE_INVOICE_DATE"));
            } else if (this.paymentDate != null && this.paymentDate.isBefore(acceptedDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ACCEPTED_DATE_AFTER_PAYMENT_DATE"));
            } else {
                this.acceptedDate = acceptedDate;
            }
        } else {
            this.acceptedDate = null;
        }
    }

    public LocalDate getEstimatedPaymentDate() {
        return estimatedPaymentDate;
    }

    public void setEstimatedPaymentDate(LocalDate estimatedPaymentDate) {
        if (estimatedPaymentDate != null) {
            if (this.invoiceDate != null && this.invoiceDate.isAfter(estimatedPaymentDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ESTIMATED_DATE_BEFORE_INVOICE_DATE"));
            } else {
                this.estimatedPaymentDate = estimatedPaymentDate;
            }
        } else {
            this.estimatedPaymentDate = null;
        }
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        if (invoiceDate != null) {
            if (this.paymentDate != null && this.paymentDate.isBefore(invoiceDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("INVOICE_DATE_AFTER_PAYMENT_DATE"));
            } else if (this.acceptedDate != null && this.acceptedDate.isBefore(invoiceDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("INVOICE_DATE_AFTER_ACCEPTED_DATE"));
            } else if (this.estimatedPaymentDate != null && this.estimatedPaymentDate.isBefore(invoiceDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("INVOICE_DATE_AFTER_ESTIMATED_DATE"));
            } else {
                this.invoiceDate = invoiceDate;
            }
        } else {
            this.invoiceDate = null;
        }

    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        if (StringUtils.isBlank(invoiceNumber)) {
            this.invoiceNumber = null;
        } else {
            String tempId = parseInvoiceNumber(invoiceNumber);
            if (isValidInvoiceNumber(tempId)) {
                this.invoiceNumber = tempId;
            } else {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("INVOICE_NUMBER_INVALID"));
            }
        }
    }

    private String parseInvoiceNumber(String invoiceNumber) {
        long number;
        try {
            number = Long.parseLong(invoiceNumber);
        } catch (NumberFormatException ignore) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("INVOICE_NUMBER_INVALID"));
        }
        return String.format("%011d", number);
    }

    private boolean isValidInvoiceNumber(String invoiceNumber) {
        if (invoiceNumber.length() != 11) {
            return false;
        } else {
            try {
                if (Long.parseLong(invoiceNumber) <= 0) {
                    return false;
                }
            } catch (NumberFormatException ignore) {
                return false;
            }
            return true;
        }
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

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        if (paymentDate != null) {
            if (this.invoiceDate != null && this.invoiceDate.isAfter(paymentDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PAYMENT_DATE_BEFORE_INVOICE_DATE"));
            } else if (this.acceptedDate != null && this.acceptedDate.isAfter(paymentDate)) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PAYMENT_DATE_BEFORE_ACCEPTED_DATE"));
            } else {
                this.paymentDate = paymentDate;
            }
        } else {
            this.paymentDate = null;
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
}
