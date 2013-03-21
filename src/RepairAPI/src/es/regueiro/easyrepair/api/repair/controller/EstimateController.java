package es.regueiro.easyrepair.api.repair.controller;

import es.regueiro.easyrepair.model.repair.Estimate;
import java.util.List;

public interface EstimateController {

    public List<Estimate> listAll();
    
    public List<Estimate> listAllEnabled();

    public List<Estimate> searchByEstimateNumber(String estimateNumber);

    public List<Estimate> searchByEstimateDate(String dateAfter, String dateBefore);

    public List<Estimate> searchByAcceptedDate(String dateAfter, String dateBefore);

    public List<Estimate> searchByStatus(String status);

    public List<Estimate> searchByDiscount(String discount);

    public List<Estimate> searchByMaxDiscount(String discount);

    public List<Estimate> searchByMinDiscount(String discount);

    public List<Estimate> searchByResponsible(String name);

    public Estimate getEstimateById(Long id);
}
