package es.regueiro.easyrepair.api.stock.controller;

import es.regueiro.easyrepair.model.stock.PartOrderInvoice;
import java.util.List;

public interface PartOrderInvoiceController {

    public List<PartOrderInvoice> listAll();

    public List<PartOrderInvoice> listAllEnabled();

    public List<PartOrderInvoice> searchByPartOrderNumber(String partOrder);

    public List<PartOrderInvoice> searchByInvoiceNumber(String invoiceNumber);

    public List<PartOrderInvoice> searchByInvoiceDate(String dateAfter, String dateBefore);

    public List<PartOrderInvoice> searchByAcceptedDate(String dateAfter, String dateBefore);

    public List<PartOrderInvoice> searchByEstimatedPaymentDate(String dateAfter, String dateBefore);

    public List<PartOrderInvoice> searchByPaymentDate(String dateAfter, String dateBefore);

    public List<PartOrderInvoice> searchByPaymentMethod(String paymentMethod);

    public List<PartOrderInvoice> searchByStatus(String status);

    public List<PartOrderInvoice> searchByResponsible(String name);

    public PartOrderInvoice getOrderInvoiceById(Long id);
}
