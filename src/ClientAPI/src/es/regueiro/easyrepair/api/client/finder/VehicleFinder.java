package es.regueiro.easyrepair.api.client.finder;

import es.regueiro.easyrepair.model.client.Vehicle;
import java.util.List;

public interface VehicleFinder {

    public List<Vehicle> listAll();

    public List<Vehicle> listAllEnabled();

    public List<Vehicle> findByRegistration(String registration);

    public List<Vehicle> findByVin(String vin);

    public List<Vehicle> findByMake(String make);

    public List<Vehicle> findByModel(String model);

    public List<Vehicle> findByYear(String year);

    public List<Vehicle> findByColour(String colour);

    public List<Vehicle> findByType(String type);

    public List<Vehicle> findByFuel(String fuel);

    public List<Vehicle> findByInsuranceNumber(String insuranceNumber);

    public List<Vehicle> findByInsuranceCompanyName(String insuranceCompanyName);

    public List<Vehicle> findByOwnerName(String ownerName);

    public List<Vehicle> findByOwnerNif(String ownerNif);

    public Vehicle getVehicle(Long id);
}
