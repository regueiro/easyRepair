package es.regueiro.easyrepair.api.repair.saver;

import es.regueiro.easyrepair.model.repair.Labour;

public interface LabourSaver {

    public void setLabour(Labour labour);

    public Labour getLabour();

    public void saveLabour();

    public void disableLabour();

    public void enableLabour();

    public void deleteLabour();
}
