package es.regueiro.easyrepair.api.client.finder;

import es.regueiro.easyrepair.model.client.Client;
import java.util.List;

public interface ClientFinder {

    public List<Client> listAll();

    public List<Client> listAllEnabled();

    public List<Client> findByName(String name);

    public List<Client> findBySurname(String surname);

    public List<Client> findByClientId(String id);

    public List<Client> findByNif(String nif);

    public Client getClient(Long id);

    public List<Client> findByEmailLabel(String label);

    public List<Client> findByEmailAddress(String address);

    public List<Client> findByPhoneLabel(String label);

    public List<Client> findByPhoneNumber(String number);

    public List<Client> findByAddressLabel(String label);

    public List<Client> findByAddressStreet(String street);

    public List<Client> findByAddressCity(String city);

    public List<Client> findByAddressPostalCode(String postalCode);

    public List<Client> findByAddressProvince(String province);

    public List<Client> findByAddressCountry(String country);
}
