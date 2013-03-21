package es.regueiro.easyrepair.api.repair.finder;

import es.regueiro.easyrepair.model.repair.Estimate;
import java.util.List;

public interface EstimateFinder {

    public List<Estimate> listAll();
    
    public List<Estimate> listAllEnabled();

    public List<Estimate> findByEstimateNumber(String estimateNumber);

    public List<Estimate> findByEstimateDate(String dateAfter, String dateBefore);
        
    public List<Estimate> findByAcceptedDate(String dateAfter, String dateBefore);
    
    public List<Estimate> findByStatus(String status);
    
    public List<Estimate> findByDiscount(String discount);
    
    public List<Estimate> findByMaxDiscount(String discount);
    
    public List<Estimate> findByMinDiscount(String discount);
    
    public List<Estimate> findByResponsible(String name);
    
    public Estimate getEstimate(Long id);

}
