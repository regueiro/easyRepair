package es.regueiro.easyrepair.api.stock.controller;

import es.regueiro.easyrepair.model.stock.Part;
import es.regueiro.easyrepair.model.stock.Stock;
import java.util.List;

public interface PartController {

    public List<Part> listAll();
    
    public List<Part> listAllEnabled();

    public List<Part> searchByMake(String make);

    public List<Part> searchByModel(String model);

    public List<Part> searchByCategory(String category);

    public List<Part> searchByPrice(String price);

    public List<Part> searchByMaxPrice(String price);

    public List<Part> searchByMinPrice(String price);

    public void setPart(Part part);

    public Part getPart();

    public void savePart();

    public Part reloadPart();

    public Part disablePart();

    public Part enablePart();

    public void overwritePart();

    public void deletePart();
    
    public Part newPart();

    public Part getPartById(Long id);

}
