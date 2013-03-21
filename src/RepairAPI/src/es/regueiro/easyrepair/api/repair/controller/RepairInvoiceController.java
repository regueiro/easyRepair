package es.regueiro.easyrepair.api.repair.controller;

import es.regueiro.easyrepair.model.repair.RepairInvoice;
import java.util.List;

public interface RepairInvoiceController {

    public List<RepairInvoice> listAll();
    
    public List<RepairInvoice> listAllEnabled();
        
    public List<RepairInvoice> searchByInvoiceNumber(String invoiceNumber);

    public List<RepairInvoice> searchByInvoiceDate(String dateAfter, String dateBefore);

    public List<RepairInvoice> searchByAcceptedDate(String dateAfter, String dateBefore);
    
    public List<RepairInvoice> searchByEstimatedPaymentDate(String dateAfter, String dateBefore);

    public List<RepairInvoice> searchByPaymentDate(String dateAfter, String dateBefore);

    public List<RepairInvoice> searchByPaymentMethod(String method);

    public List<RepairInvoice> searchByStatus(String status);

    public List<RepairInvoice> searchByResponsible(String responsible);

    public List<RepairInvoice> searchByPaymentResponsible(String paymentResponsible);

    public RepairInvoice getRepairInvoiceById(Long id);
}
