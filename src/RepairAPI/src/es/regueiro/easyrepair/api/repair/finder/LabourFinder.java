package es.regueiro.easyrepair.api.repair.finder;

import es.regueiro.easyrepair.model.repair.Labour;
import java.util.List;

public interface LabourFinder {

    public List<Labour> listAll();
    
    public List<Labour> listAllEnabled();

    public List<Labour> findByName(String name);

    public List<Labour> findByDescription(String description);

    public List<Labour> findByPrice(String price);
    
    public List<Labour> findByMinPrice(String price);
    
    public List<Labour> findByMaxPrice(String price);

    public Labour getLabour(Long id);
}
