package es.regueiro.easyrepair.api.repair.finder;

import es.regueiro.easyrepair.model.repair.RepairInvoice;
import java.util.List;

public interface RepairInvoiceFinder {

    public List<RepairInvoice> listAll();
    
    public List<RepairInvoice> listAllEnabled();

    public List<RepairInvoice> findByInvoiceNumber(String invoiceNumber);

    public List<RepairInvoice> findByInvoiceDate(String dateAfter, String dateBefore);

    public List<RepairInvoice> findByAcceptedDate(String dateAfter, String dateBefore);
    
    public List<RepairInvoice> findByEstimatedPaymentDate(String dateAfter, String dateBefore);

    public List<RepairInvoice> findByPaymentDate(String dateAfter, String dateBefore);

    public List<RepairInvoice> findByPaymentMethod(String method);

    public List<RepairInvoice> findByStatus(String status);

    public List<RepairInvoice> findByResponsible(String responsible);

    public List<RepairInvoice> findByPaymentResponsible(String paymentResponsible);

    public RepairInvoice getRepairInvoice(Long id);

}
