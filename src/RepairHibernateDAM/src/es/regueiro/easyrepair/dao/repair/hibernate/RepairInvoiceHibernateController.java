package es.regueiro.easyrepair.dao.repair.hibernate;

import es.regueiro.easyrepair.api.repair.controller.RepairInvoiceController;
import es.regueiro.easyrepair.api.repair.finder.RepairInvoiceFinder;
import es.regueiro.easyrepair.model.repair.RepairInvoice;
import java.util.List;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = RepairInvoiceController.class,
//path = "RepairInvoiceFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.repairInvoice.DefaultRepairInvoiceFinder"}
)
public class RepairInvoiceHibernateController implements RepairInvoiceController {

    private RepairInvoiceFinder finder = Lookup.getDefault().lookup(RepairInvoiceFinder.class);

    @Override
    public List<RepairInvoice> listAll() {
        List<RepairInvoice> list = finder.listAll();
        return list;
    }

    @Override
    public List<RepairInvoice> listAllEnabled() {
        List<RepairInvoice> list = finder.listAllEnabled();
        return list;
    }

    @Override
    public List<RepairInvoice> searchByInvoiceNumber(String repairInvoiceNumber) {
        List<RepairInvoice> list = finder.findByInvoiceNumber(repairInvoiceNumber);
        return list;
    }

    @Override
    public List<RepairInvoice> searchByInvoiceDate(String dateAfter, String dateBefore) {
        List<RepairInvoice> list = finder.findByInvoiceDate(dateAfter, dateBefore);
        return list;
    }

    @Override
    public List<RepairInvoice> searchByAcceptedDate(String dateAfter, String dateBefore) {
        List<RepairInvoice> list = finder.findByAcceptedDate(dateAfter, dateBefore);
        return list;
    }
    
    
    @Override
    public List<RepairInvoice> searchByEstimatedPaymentDate(String dateAfter, String dateBefore) {
        List<RepairInvoice> list = finder.findByEstimatedPaymentDate(dateAfter, dateBefore);
        return list;
    }

    @Override
    public List<RepairInvoice> searchByPaymentDate(String dateAfter, String dateBefore) {
        List<RepairInvoice> list = finder.findByPaymentDate(dateAfter, dateBefore);
        return list;
    }

    @Override
    public List<RepairInvoice> searchByPaymentMethod(String paymentMethod) {
        List<RepairInvoice> list = finder.findByPaymentMethod(paymentMethod);
        return list;
    }

    @Override
    public List<RepairInvoice> searchByStatus(String status) {
        List<RepairInvoice> list = finder.findByStatus(status);
        return list;
    }

    @Override
    public List<RepairInvoice> searchByResponsible(String responsible) {
        List<RepairInvoice> list = finder.findByResponsible(responsible);
        return list;
    }

    @Override
    public List<RepairInvoice> searchByPaymentResponsible(String paymentRespnsible) {
        List<RepairInvoice> list = finder.findByPaymentResponsible(paymentRespnsible);
        return list;
    }

    @Override
    public RepairInvoice getRepairInvoiceById(Long id) {
        return finder.getRepairInvoice(id);
    }
}
