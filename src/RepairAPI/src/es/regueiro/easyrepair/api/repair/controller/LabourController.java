package es.regueiro.easyrepair.api.repair.controller;

import es.regueiro.easyrepair.model.repair.Labour;
import java.util.List;

public interface LabourController {

    public List<Labour> listAll();
    
    public List<Labour> listAllEnabled();

    public List<Labour> searchByName(String name);

    public List<Labour> searchByDescription(String description);

    public List<Labour> searchByPrice(String price);

    public List<Labour> searchByMinPrice(String price);

    public List<Labour> searchByMaxPrice(String price);

    public void setLabour(Labour labour);

    public Labour getLabour();

    public void saveLabour();

    public Labour reloadLabour();

    public Labour disableLabour();

    public Labour enableLabour();

    public void overwriteLabour();

    public void deleteLabour();

    public Labour newLabour();

    public Labour getLabourById(Long id);
}
