package es.regueiro.easyrepair.dao.repair.hibernate;

import es.regueiro.easyrepair.api.repair.controller.EstimateController;
import es.regueiro.easyrepair.api.repair.finder.EstimateFinder;
import es.regueiro.easyrepair.model.repair.Estimate;
import java.util.List;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = EstimateController.class,
//path = "EstimateFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.estimate.DefaultEstimateFinder"}
)
public class EstimateHibernateController implements EstimateController {

    private EstimateFinder finder = Lookup.getDefault().lookup(EstimateFinder.class);

    @Override
    public List<Estimate> listAll() {
        List<Estimate> list = finder.listAll();
        return list;
    }

    @Override
    public List<Estimate> listAllEnabled() {
        List<Estimate> list = finder.listAllEnabled();
        return list;
    }

    @Override
    public List<Estimate> searchByEstimateNumber(String estimateNumber) {
        List<Estimate> list = finder.findByEstimateNumber(estimateNumber);
        return list;
    }

    @Override
    public List<Estimate> searchByEstimateDate(String dateAfter, String dateBefore) {
        List<Estimate> list = finder.findByEstimateDate(dateAfter, dateBefore);
        return list;
    }

    @Override
    public List<Estimate> searchByAcceptedDate(String dateAfter, String dateBefore) {
        List<Estimate> list = finder.findByAcceptedDate(dateAfter, dateBefore);
        return list;
    }

    @Override
    public List<Estimate> searchByStatus(String status) {
        List<Estimate> list = finder.findByStatus(status);
        return list;
    }

    @Override
    public List<Estimate> searchByDiscount(String discount) {
        List<Estimate> list = finder.findByDiscount(discount);
        return list;
    }

    @Override
    public List<Estimate> searchByMinDiscount(String discount) {
        List<Estimate> list = finder.findByMinDiscount(discount);
        return list;
    }

    @Override
    public List<Estimate> searchByMaxDiscount(String discount) {
        List<Estimate> list = finder.findByMaxDiscount(discount);
        return list;
    }

    @Override
    public List<Estimate> searchByResponsible(String name) {
        List<Estimate> list = finder.findByResponsible(name);
        return list;
    }


    @Override
    public Estimate getEstimateById(Long id) {
        return finder.getEstimate(id);
    }
}
