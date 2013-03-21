package es.regueiro.easyrepair.dao.stock.hibernate;

import es.regueiro.easyrepair.api.stock.controller.PartOrderInvoiceController;
import es.regueiro.easyrepair.api.stock.finder.PartOrderInvoiceFinder;
import es.regueiro.easyrepair.model.stock.PartOrderInvoice;
import java.util.List;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = PartOrderInvoiceController.class,
//path = "OrderInvoiceFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.stock.DefaultOrderInvoiceFinder"}
)
public class PartOrderInvoiceHibernateController implements PartOrderInvoiceController {

    private PartOrderInvoiceFinder finder = Lookup.getDefault().lookup(PartOrderInvoiceFinder.class);
//    private PartOrderInvoiceSaver saver = Lookup.getDefault().lookup(PartOrderInvoiceSaver.class);

    @Override
    public List<PartOrderInvoice> listAll() {
        List<PartOrderInvoice> list = finder.listAll();
        return list;
    }
    
    @Override
    public List<PartOrderInvoice> listAllEnabled() {
        List<PartOrderInvoice> list = finder.listAllEnabled();
        return list;
    }
    
    @Override
    public List<PartOrderInvoice> searchByInvoiceDate(String dateAfter, String dateBefore) {
        List<PartOrderInvoice> list = finder.findByInvoiceDate(dateAfter, dateBefore);
        return list;
    }

    @Override
    public List<PartOrderInvoice> searchByAcceptedDate(String dateAfter, String dateBefore) {
        List<PartOrderInvoice> list = finder.findByAcceptedDate(dateAfter, dateBefore);
        return list;
    }

    @Override
    public List<PartOrderInvoice> searchByEstimatedPaymentDate(String dateAfter, String dateBefore) {
        List<PartOrderInvoice> list = finder.findByEstimatedPaymentDate(dateAfter, dateBefore);
        return list;
    }

    @Override
    public List<PartOrderInvoice> searchByPaymentDate(String dateAfter, String dateBefore) {
        List<PartOrderInvoice> list = finder.findByPaymentDate(dateAfter, dateBefore);
        return list;
    }

    @Override
    public List<PartOrderInvoice> searchByStatus(String status) {
        List<PartOrderInvoice> list = finder.findByStatus(status);
        return list;
    }

    @Override
    public List<PartOrderInvoice> searchByResponsible(String name) {
        List<PartOrderInvoice> list = finder.findByResponsible(name);
        return list;
    }

    @Override
    public List<PartOrderInvoice> searchByPartOrderNumber(String partOrder) {
        List<PartOrderInvoice> list = finder.findByPartOrderNumber(partOrder);
        return list;
    }

    @Override
    public List<PartOrderInvoice> searchByInvoiceNumber(String invoiceNumber) {
        List<PartOrderInvoice> list = finder.findByInvoiceNumber(invoiceNumber);
        return list;
    }

    @Override
    public List<PartOrderInvoice> searchByPaymentMethod(String paymentMethod) {
        List<PartOrderInvoice> list = finder.findByPaymentMenthod(paymentMethod);
        return list;
    }

    @Override
    public PartOrderInvoice getOrderInvoiceById(Long id) {
        return finder.getOrderInvoice(id);
    }
}
