package es.regueiro.easyrepair.dao.repair.hibernate;

import es.regueiro.easyrepair.api.repair.controller.LabourController;
import es.regueiro.easyrepair.api.repair.finder.LabourFinder;
import es.regueiro.easyrepair.api.repair.saver.LabourSaver;
import es.regueiro.easyrepair.model.client.Client;
import es.regueiro.easyrepair.model.repair.Labour;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = LabourController.class,
//path = "LabourFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.labour.DefaultLabourFinder"}
)
public class LabourHibernateController implements LabourController {

    private LabourFinder finder = Lookup.getDefault().lookup(LabourFinder.class);
    private LabourSaver saver = Lookup.getDefault().lookup(LabourSaver.class);

    @Override
    public List<Labour> listAll() {
        List<Labour> list = finder.listAll();
        return list;
    }

    @Override
    public List<Labour> listAllEnabled() {
        List<Labour> list = finder.listAllEnabled();
        return list;
    }

    @Override
    public List<Labour> searchByName(String name) {
        List<Labour> list = finder.findByName(name);
        return list;
    }

    @Override
    public List<Labour> searchByDescription(String description) {
        List<Labour> list = finder.findByDescription(description);
        return list;
    }

    @Override
    public List<Labour> searchByPrice(String price) {
        List<Labour> list = finder.findByPrice(price);
        return list;
    }

    @Override
    public List<Labour> searchByMinPrice(String price) {
        List<Labour> list = finder.findByMinPrice(price);
        return list;
    }

    @Override
    public List<Labour> searchByMaxPrice(String price) {
        List<Labour> list = finder.findByMaxPrice(price);
        return list;
    }

    @Override
    public void setLabour(Labour labour) {
        saver.setLabour(labour);
    }

    @Override
    public Labour getLabour() {
        return saver.getLabour();
    }

    @Override
    public void saveLabour() {
        try {
            saver.saveLabour();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                throw new RuntimeException("The labour was updated or deleted by another user");
            } else {
                throw e;
            }

        }
    }

    @Override
    public Labour reloadLabour() {
        Labour emp = finder.getLabour(saver.getLabour().getId());
        return emp;
    }

    @Override
    public Labour disableLabour() {
        try {
            saver.disableLabour();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setLabour(reloadLabour());
                disableLabour();
            } else {
                throw e;
            }

        }
        return getLabour();
    }

    @Override
    public Labour enableLabour() {
        try {
            saver.enableLabour();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setLabour(reloadLabour());
                enableLabour();
            } else {
                throw e;
            }

        }
        return getLabour();
    }

    @Override
    public void overwriteLabour() {
        Labour old = getLabour();
        Labour newLabour = reloadLabour();
        if (newLabour == null) {
            newLabour = newLabour();
        }

        setLabour(newLabour);
        newLabour.setDescription(old.getDescription());
        newLabour.setName(old.getName());
        newLabour.setNotes(old.getNotes());
        if (old.getPrice() != null) {
            newLabour.setPrice(old.getPrice().toString());
        }
        newLabour.setEnabled(old.getEnabled());


        saver.setLabour(newLabour);
        saver.saveLabour();
    }

    @Override
    public Labour newLabour() {
        Labour emp = new Labour();
        return emp;
    }

    @Override
    public void deleteLabour() {
        try {
            saver.deleteLabour();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setLabour(reloadLabour());
                deleteLabour();
            } else {
                throw e;
            }
        }
    }

    @Override
    public Labour getLabourById(Long id) {
        return finder.getLabour(id);
    }
}
