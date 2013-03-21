package es.regueiro.easyrepair.api.stock.saver;

import es.regueiro.easyrepair.model.stock.Part;
import es.regueiro.easyrepair.model.stock.Stock;

public interface PartSaver {

    public void setPart(Part part);

    public Part getPart();

    public void savePart();

    public void disablePart();

    public void enablePart();

    public void deletePart();

}
