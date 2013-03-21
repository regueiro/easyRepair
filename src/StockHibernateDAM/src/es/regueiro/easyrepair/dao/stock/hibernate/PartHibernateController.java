package es.regueiro.easyrepair.dao.stock.hibernate;

import es.regueiro.easyrepair.api.stock.controller.PartController;
import es.regueiro.easyrepair.api.stock.finder.PartFinder;
import es.regueiro.easyrepair.api.stock.saver.PartSaver;
import es.regueiro.easyrepair.model.stock.Part;
import es.regueiro.easyrepair.model.stock.PartLine;
import es.regueiro.easyrepair.model.stock.Stock;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = PartController.class,
//path = "PartFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.stock.DefaultPartFinder"}
)
public class PartHibernateController implements PartController {
    
    private PartFinder finder = Lookup.getDefault().lookup(PartFinder.class);
    private PartSaver saver = Lookup.getDefault().lookup(PartSaver.class);
    
    @Override
    public List<Part> listAll() {
        List<Part> list = finder.listAll();
        return list;
    }
    
    @Override
    public List<Part> listAllEnabled() {
        List<Part> list = finder.listAllEnabled();
        return list;
    }
    
    @Override
    public List<Part> searchByMake(String make) {
        List<Part> list = finder.findByMake(make);
        return list;
    }
    
    @Override
    public List<Part> searchByModel(String model) {
        List<Part> list = finder.findByModel(model);
        return list;
    }
    
    @Override
    public List<Part> searchByCategory(String category) {
        List<Part> list = finder.findByCategory(category);
        return list;
    }
    
    @Override
    public List<Part> searchByPrice(String price) {
        List<Part> list = finder.findByPrice(price);
        return list;
    }
    
    @Override
    public List<Part> searchByMaxPrice(String price) {
        List<Part> list = finder.findByMaxPrice(price);
        return list;
    }
    
    @Override
    public List<Part> searchByMinPrice(String price) {
        List<Part> list = finder.findByMinPrice(price);
        return list;
    }
    
    @Override
    public void setPart(Part part) {
        saver.setPart(part);
    }
    
    @Override
    public Part getPart() {
        return saver.getPart();
    }
    
    @Override
    public void savePart() {
        try {
            saver.savePart();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                throw new RuntimeException("The part was updated or deleted by another user");
            } else {
                throw e;
            }
            
        }
    }
    
    @Override
    public Part reloadPart() {
        Part emp = finder.getPart(saver.getPart().getId());
        return emp;
    }
    
    @Override
    public Part disablePart() {
        try {
            saver.disablePart();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setPart(reloadPart());
                disablePart();
            } else {
                throw e;
            }
            
        }
        return getPart();
    }
    
    @Override
    public Part enablePart() {
        try {
            saver.enablePart();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setPart(reloadPart());
                enablePart();
            } else {
                throw e;
            }
            
        }
        return getPart();
    }
    
    @Override
    public void overwritePart() {
        Part old = getPart();
        Part newPart = reloadPart();
        if (newPart == null) {
            newPart = newPart();
        }
        setPart(newPart);
        newPart.setMake(old.getMake());
        newPart.setModel(old.getModel());
        newPart.setCategory(old.getCategory());
        newPart.setNotes(old.getNotes());
        if (old.getPrice() != null) {
            newPart.setPrice(old.getPrice().toString());
        }
        newPart.setEnabled(old.getEnabled());

        if (old.getStock() != null) {
            List<Stock> oldList = old.getStock();
            List<Stock> newList = newPart.getStock();
            List<Long> newId = new ArrayList<Long>();

            if (newList != null) {
                for (Stock v : newList) {
                    newId.add(v.getId());
                }
            }
            newPart.clearStock();

            for (Stock st : oldList) {
                Stock temp = new Stock();
                temp.setUnits(st.getUnits());
                temp.setWarehouse(st.getWarehouse());
                if (newId.contains(st.getId())) {
                    temp.setId(st.getId());
                }
                newPart.addStock(temp);
            }
        } else {
            if (newPart.getStock() != null) {
                newPart.clearStock();
            }
        }

        
        saver.setPart(newPart);
        saver.savePart();
    }
    
    @Override
    public Part newPart() {
        Part emp = new Part();
        return emp;
    }
    
    @Override
    public void deletePart() {
        try {
            saver.deletePart();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setPart(reloadPart());
                deletePart();
            } else {
                throw e;
            }
        }
    }

    @Override
    public Part getPartById(Long id) {
        return finder.getPart(id);
    }
}
