package es.regueiro.easyrepair.api.client.controller;

import es.regueiro.easyrepair.model.client.Client;
import java.util.List;

public interface ClientController {

    public List<Client> listAll();
    
    public List<Client> listAllEnabled();

    public List<Client> searchByName(String name);

    public List<Client> searchBySurname(String surname);

    public List<Client> searchByClientId(String id);

    public List<Client> searchByNif(String nif);

    public void setClient(Client client);

    public Client getClient();

    public void saveClient();

    public Client reloadClient();

    public Client disableClient();

    public Client enableClient();

    public void overwriteClient();

    public void deleteClient();

    public Client newClient();

    public Client getClientById(Long id);

    public List<Client> searchByEmailLabel(String label);

    public List<Client> searchByEmailAddress(String address);

    public List<Client> searchByPhoneLabel(String label);

    public List<Client> searchByPhoneNumber(String number);

    public List<Client> searchByAddressLabel(String label);

    public List<Client> searchByAddressStreet(String street);

    public List<Client> searchByAddressCity(String city);

    public List<Client> searchByAddressPostalCode(String postalCode);

    public List<Client> searchByAddressProvince(String province);

    public List<Client> searchByAddressCountry(String country);
}
