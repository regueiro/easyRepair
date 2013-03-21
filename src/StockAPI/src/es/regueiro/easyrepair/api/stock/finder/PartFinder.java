package es.regueiro.easyrepair.api.stock.finder;

import es.regueiro.easyrepair.model.stock.Part;
import java.util.List;

public interface PartFinder {

    public List<Part> listAll();
    
    public List<Part> listAllEnabled();

    public List<Part> findByMake(String make);

    public List<Part> findByModel(String model);

    public List<Part> findByCategory(String category);

    public List<Part> findByPrice(String price);

    public List<Part> findByMaxPrice(String price);

    public List<Part> findByMinPrice(String price);

    public Part getPart(Long id);
}
