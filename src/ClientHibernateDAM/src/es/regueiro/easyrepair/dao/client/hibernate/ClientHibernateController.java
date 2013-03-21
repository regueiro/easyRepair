package es.regueiro.easyrepair.dao.client.hibernate;

import es.regueiro.easyrepair.api.client.controller.ClientController;
import es.regueiro.easyrepair.api.client.finder.ClientFinder;
import es.regueiro.easyrepair.api.client.saver.ClientSaver;
import es.regueiro.easyrepair.model.client.Client;
import es.regueiro.easyrepair.model.client.Vehicle;
import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.Phone;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ClientController.class,
//path = "ClientFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.client.DefaultClientFinder"}
)
public class ClientHibernateController implements ClientController {

    private ClientFinder finder = Lookup.getDefault().lookup(ClientFinder.class);
    private ClientSaver saver = Lookup.getDefault().lookup(ClientSaver.class);

    @Override
    public List<Client> listAll() {
        List<Client> list = finder.listAll();
        return list;
    }

    @Override
    public List<Client> listAllEnabled() {
        List<Client> list = finder.listAllEnabled();
        return list;
    }
    
    @Override
    public List<Client> searchByName(String name) {
        List<Client> list = finder.findByName(name);
        return list;
    }

    @Override
    public List<Client> searchBySurname(String surname) {
        List<Client> list = finder.findBySurname(surname);
        return list;
    }

    @Override
    public List<Client> searchByClientId(String id) {
        List<Client> list = finder.findByClientId(id);
        return list;
    }

    @Override
    public List<Client> searchByNif(String nif) {
        List<Client> list = finder.findByNif(nif);
        return list;
    }

    @Override
    public List<Client> searchByEmailLabel(String label) {
        List<Client> list = finder.findByEmailLabel(label);
        return list;
    }

    @Override
    public List<Client> searchByEmailAddress(String address) {
        List<Client> list = finder.findByEmailAddress(address);
        return list;
    }

    @Override
    public List<Client> searchByPhoneLabel(String label) {
        List<Client> list = finder.findByPhoneLabel(label);
        return list;
    }

    @Override
    public List<Client> searchByPhoneNumber(String number) {
        List<Client> list = finder.findByPhoneNumber(number);
        return list;
    }

    @Override
    public List<Client> searchByAddressLabel(String label) {
        List<Client> list = finder.findByAddressLabel(label);
        return list;
    }

    @Override
    public List<Client> searchByAddressStreet(String street) {
        List<Client> list = finder.findByAddressStreet(street);
        return list;
    }

    @Override
    public List<Client> searchByAddressCity(String city) {
        List<Client> list = finder.findByAddressCity(city);
        return list;
    }

    @Override
    public List<Client> searchByAddressPostalCode(String postalCode) {
        List<Client> list = finder.findByAddressPostalCode(postalCode);
        return list;
    }

    @Override
    public List<Client> searchByAddressProvince(String province) {
        List<Client> list = finder.findByAddressProvince(province);
        return list;
    }

    @Override
    public List<Client> searchByAddressCountry(String country) {
        List<Client> list = finder.findByAddressCountry(country);
        return list;
    }

    @Override
    public void setClient(Client client) {
        saver.setClient(client);
    }

    @Override
    public Client getClient() {
        return saver.getClient();
    }

    @Override
    public void saveClient() {
        try {
            saver.saveClient();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                throw new RuntimeException("The client was updated or deleted by another user");
            } else {
                throw e;
            }

        }
    }

    @Override
    public Client reloadClient() {
        Client emp = finder.getClient(saver.getClient().getId());
        return emp;
    }

    @Override
    public Client disableClient() {
        try {
            saver.disableClient();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setClient(reloadClient());
                disableClient();
            } else {
                throw e;
            }

        }
        return getClient();
    }

    @Override
    public Client enableClient() {
        try {
            saver.enableClient();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setClient(reloadClient());
                enableClient();
            } else {
                throw e;
            }

        }
        return getClient();
    }

    @Override
    public void overwriteClient() {
        Client old = getClient();
        Client newClient = reloadClient();
        if (newClient == null) {
            newClient = newClient();
        }
        setClient(newClient);
        newClient.setClientId(old.getClientId());
        newClient.setName(old.getName());
        newClient.setSurname(old.getSurname());
        newClient.setNif(old.getNif());
        newClient.setNotes(old.getNotes());
        newClient.setEnabled(old.getEnabled());

         
        if (old.getEmail() != null) {
            List<Email> oldList = old.getEmail();
            List<Email> newList = newClient.getEmail();
            List<Long> newId = new ArrayList<Long>();

            if (newList != null) {
                for (Email e : newList) {
                    newId.add(e.getId());
                }
            }
            newClient.clearEmail();

            for (Email e : oldList) {
                Email temp = new Email(e.getAddress(), e.getLabel());
                temp.setNotes(e.getNotes());
                if (newId.contains(e.getId())) {
                    temp.setId(e.getId());
                }
                newClient.addEmail(temp);
            }
        } else {
            if (newClient.getEmail() != null) {
                newClient.clearEmail();
            }
        }

        if (old.getPhone() != null) {
            List<Phone> oldList = old.getPhone();
            List<Phone> newList = newClient.getPhone();
            List<Long> newId = new ArrayList<Long>();

            if (newList != null) {
                for (Phone e : newList) {
                    newId.add(e.getId());
                }
            }
            newClient.clearPhone();

            for (Phone e : oldList) {
                Phone temp = new Phone(e.getNumber(), e.getLabel());
                temp.setNotes(e.getNotes());
                if (newId.contains(e.getId())) {
                    temp.setId(e.getId());
                }
                newClient.addPhone(temp);
            }
        } else {
            if (newClient.getPhone() != null) {
                newClient.clearPhone();
            }
        }

        if (old.getAddress() != null) {
            List<Address> oldList = old.getAddress();
            List<Address> newList = newClient.getAddress();
            List<Long> newId = new ArrayList<Long>();
            if (newList != null) {
                for (Address a : newList) {
                    newId.add(a.getId());
                }
            }
            newClient.clearAdddress();

            for (Address a : oldList) {
                Address temp = new Address();
                temp.setLabel(a.getLabel());
                temp.setStreet(a.getStreet());
                temp.setCity(a.getCity());
                temp.setCountry(a.getCountry());
                temp.setPostalCode(a.getPostalCode());
                temp.setProvince(a.getProvince());
                temp.setNotes(a.getNotes());

                if (newId.contains(a.getId())) {
                    temp.setId(a.getId());
                }
                newClient.addAddress(temp);
            }
        } else {
            if (newClient.getAddress() != null) {
                newClient.clearPhone();
            }
        }
        
        if (old.getVehicles() != null) {
            List<Vehicle> oldList = old.getVehicles();
            List<Vehicle> newList = newClient.getVehicles();
            List<Long> newId = new ArrayList<Long>();

            if (newList != null) {
                for (Vehicle v : newList) {
                    newId.add(v.getId());
                }
            }
            newClient.clearVehicles();

            for (Vehicle v : oldList) {
                Vehicle temp = new Vehicle();
                temp.setColour(v.getColour());
                temp.setEnabled(v.getEnabled());
                temp.setFuel(v.getFuel());
                temp.setInsuranceCompany(v.getInsuranceCompany());
                temp.setInsuranceNumber(v.getInsuranceNumber());
                temp.setMake(v.getMake());
                temp.setModel(v.getModel());
                temp.setNotes(v.getNotes());
                temp.setRegistration(v.getRegistration());
                temp.setType(v.getType());
                temp.setVin(v.getVin());
                temp.setYear(v.getYear());
                temp.setOwner(newClient);
                if (newId.contains(v.getId())) {
                    temp.setId(v.getId());
                }
                newClient.addVehicle(temp);
            }
        } else {
            if (newClient.getVehicles() != null) {
                newClient.clearVehicles();
            }
        }

        saver.setClient(newClient);
        saver.saveClient();
    }

    @Override
    public Client newClient() {
        Client emp = new Client();
        return emp;
    }

    @Override
    public void deleteClient() {
        if (!getClient().getVehicles().isEmpty()) {
            throw new IllegalArgumentException("The client can not be deleted because it has vehicles still asigned");
        }
        try {
            saver.deleteClient();
        } catch (HibernateException e) {
            if (e.getMessage().contains("Row was updated or deleted by another transaction")) {
                setClient(reloadClient());
                deleteClient();
            } else {
                throw e;
            }
        }
    }

    @Override
    public Client getClientById(Long id) {
        return finder.getClient(id);
    }
}
