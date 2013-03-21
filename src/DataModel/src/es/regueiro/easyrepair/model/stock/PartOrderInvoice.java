package es.regueiro.easyrepair.model.stock;

import es.regueiro.easyrepair.model.shared.Invoice;

public class PartOrderInvoice extends Invoice {

    private Long id;
    private int version;
    private PartOrder order;

    public PartOrderInvoice() {
        //Not to use outside hibernate
    }
    
    public PartOrderInvoice(PartOrder order) {
        this.order = order;
    }
    
    public boolean getEnabled() {
        return order.getEnabled();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PartOrder getOrder() {
        return order;
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
        final PartOrderInvoice other = (PartOrderInvoice) obj;

        if ((this.getInvoiceNumber() == null) ? (other.getInvoiceNumber() != null) : !this.getInvoiceNumber().equals(other.getInvoiceNumber())) {
            return false;
        }
        if (this.getInvoiceDate() != other.getInvoiceDate() && (this.getInvoiceDate() == null || !this.getInvoiceDate().equals(other.getInvoiceDate()))) {
            return false;
        }
        if (this.getAcceptedDate() != other.getAcceptedDate() && (this.getAcceptedDate() == null || !this.getAcceptedDate().equals(other.getAcceptedDate()))) {
            return false;
        }
        if (this.getEstimatedPaymentDate() != other.getEstimatedPaymentDate() && (this.getEstimatedPaymentDate() == null || !this.getEstimatedPaymentDate().equals(other.getEstimatedPaymentDate()))) {
            return false;
        }
        if (this.getPaymentDate() != other.getPaymentDate() && (this.getPaymentDate() == null || !this.getPaymentDate().equals(other.getPaymentDate()))) {
            return false;
        }
        if ((this.getPaymentMethod() == null) ? (other.getPaymentMethod() != null) : !this.getPaymentMethod().equals(other.getPaymentMethod())) {
            return false;
        }
        if ((this.getStatus() == null) ? (other.getStatus() != null) : !this.getStatus().equals(other.getStatus())) {
            return false;
        }
        if ((this.getNotes() == null) ? (other.getNotes() != null) : !this.getNotes().equals(other.getNotes())) {
            return false;
        }
        if (this.getResponsible() != other.getResponsible() && (this.getResponsible() == null || !this.getResponsible().equals(other.getResponsible()))) {
            return false;
        }
        if (this.getEnabled() != other.getEnabled()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.getInvoiceNumber() != null ? this.getInvoiceNumber().hashCode() : 0);
        hash = 41 * hash + (this.getInvoiceDate() != null ? this.getInvoiceDate().hashCode() : 0);
        hash = 41 * hash + (this.getAcceptedDate() != null ? this.getAcceptedDate().hashCode() : 0);
        hash = 41 * hash + (this.getEstimatedPaymentDate() != null ? this.getEstimatedPaymentDate().hashCode() : 0);
        hash = 41 * hash + (this.getPaymentMethod() != null ? this.getPaymentMethod().hashCode() : 0);
        hash = 41 * hash + (this.getStatus() != null ? this.getStatus().hashCode() : 0);
        hash = 41 * hash + (this.getNotes() != null ? this.getNotes().hashCode() : 0);
        hash = 41 * hash + (this.getResponsible() != null ? this.getResponsible().hashCode() : 0);
        hash = 41 * hash + (this.getEnabled() ? 1 : 0);

        return hash;
    }
}
