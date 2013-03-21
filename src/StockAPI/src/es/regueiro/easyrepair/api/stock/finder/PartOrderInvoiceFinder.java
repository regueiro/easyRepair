package es.regueiro.easyrepair.api.stock.finder;

import es.regueiro.easyrepair.model.stock.PartOrderInvoice;
import java.util.List;

public interface PartOrderInvoiceFinder {

    public List<PartOrderInvoice> listAll();
    
    public List<PartOrderInvoice> listAllEnabled();

    public List<PartOrderInvoice> findByPartOrderNumber(String partOrder);

    public List<PartOrderInvoice> findByInvoiceNumber(String invoiceNumber);

    public List<PartOrderInvoice> findByInvoiceDate(String dateAfter,String dateBefore);
    
    public List<PartOrderInvoice> findByAcceptedDate(String dateAfter,String dateBefore);
    
    public List<PartOrderInvoice> findByEstimatedPaymentDate(String dateAfter,String dateBefore);

    public List<PartOrderInvoice> findByPaymentDate(String dateAfter,String dateBefore);

    public List<PartOrderInvoice> findByPaymentMenthod(String paymentMethod);

    public List<PartOrderInvoice> findByStatus(String status);

    public PartOrderInvoice getOrderInvoice(Long id);

    public List<PartOrderInvoice> findByResponsible(String name);
}
