package es.regueiro.easyrepair.api.client.controller;

import es.regueiro.easyrepair.model.client.Vehicle;
import java.util.List;

public interface VehicleController {

    public List<Vehicle> listAll();

    public List<Vehicle> listAllEnabled();

    public List<Vehicle> searchByRegistration(String registration);

    public List<Vehicle> searchByVin(String vin);

    public List<Vehicle> searchByMake(String make);

    public List<Vehicle> searchByModel(String model);

    public List<Vehicle> searchByYear(String year);

    public List<Vehicle> searchByColour(String colour);

    public List<Vehicle> searchByType(String type);

    public List<Vehicle> searchByFuel(String fuel);

    public List<Vehicle> searchByInsuranceNumber(String insuranceNumber);

    public List<Vehicle> searchByInsuranceCompanyName(String insuranceCompany);

    public List<Vehicle> searchByOwnerName(String ownerName);

    public List<Vehicle> searchByOwnerNif(String ownerNif);

    public Vehicle getVehicleById(Long id);
}
