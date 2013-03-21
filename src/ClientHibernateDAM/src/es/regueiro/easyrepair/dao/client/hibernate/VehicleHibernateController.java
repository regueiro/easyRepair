package es.regueiro.easyrepair.dao.client.hibernate;

import es.regueiro.easyrepair.api.client.controller.VehicleController;
import es.regueiro.easyrepair.api.client.finder.VehicleFinder;
//import es.regueiro.easyrepair.api.client.saver.VehicleSaver;
import es.regueiro.easyrepair.model.client.Vehicle;
import java.util.List;
import org.hibernate.HibernateException;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = VehicleController.class,
//path = "VehicleFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.vehicle.DefaultVehicleFinder"}
)
public class VehicleHibernateController implements VehicleController {

    private VehicleFinder finder = Lookup.getDefault().lookup(VehicleFinder.class);

    @Override
    public List<Vehicle> listAll() {
        List<Vehicle> list = finder.listAll();
        return list;
    }
    
    @Override
    public List<Vehicle> listAllEnabled() {
        List<Vehicle> list = finder.listAllEnabled();
        return list;
    }

    @Override
    public List<Vehicle> searchByRegistration(String registration) {
        List<Vehicle> list = finder.findByRegistration(registration);
        return list;
    }

    @Override
    public List<Vehicle> searchByVin(String vin) {
        List<Vehicle> list = finder.findByVin(vin);
        return list;
    }

    @Override
    public List<Vehicle> searchByMake(String make) {
        List<Vehicle> list = finder.findByMake(make);
        return list;
    }

    @Override
    public List<Vehicle> searchByModel(String model) {
        List<Vehicle> list = finder.findByModel(model);
        return list;
    }

    @Override
    public List<Vehicle> searchByYear(String year) {
        List<Vehicle> list = finder.findByYear(year);
        return list;
    }

    @Override
    public List<Vehicle> searchByColour(String colour) {
        List<Vehicle> list = finder.findByColour(colour);
        return list;
    }

    @Override
    public List<Vehicle> searchByType(String type) {
        List<Vehicle> list = finder.findByType(type);
        return list;
    }

    @Override
    public List<Vehicle> searchByFuel(String fuel) {
        List<Vehicle> list = finder.findByFuel(fuel);
        return list;
    }

    @Override
    public List<Vehicle> searchByInsuranceNumber(String insuranceNumber) {
        List<Vehicle> list = finder.findByInsuranceNumber(insuranceNumber);
        return list;
    }

    @Override
    public List<Vehicle> searchByInsuranceCompanyName(String insuranceCompanyName) {
        List<Vehicle> list = finder.findByInsuranceCompanyName(insuranceCompanyName);
        return list;
    }

    @Override
    public List<Vehicle> searchByOwnerName(String ownerName) {
        List<Vehicle> list = finder.findByOwnerName(ownerName);
        return list;
    }
    
    @Override
    public List<Vehicle> searchByOwnerNif(String ownerNif) {
        List<Vehicle> list = finder.findByOwnerNif(ownerNif);
        return list;
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        return finder.getVehicle(id);
    }
}
