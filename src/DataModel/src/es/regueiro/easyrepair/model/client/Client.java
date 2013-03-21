package es.regueiro.easyrepair.model.client;

import es.regueiro.easyrepair.model.shared.Person;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class Client extends Person {

    private Long id; // ID para la base de datos
    private String clientId; // ID para la empresa
    private String notes;
    private int version;
    private boolean enabled = true;
    private List<Vehicle> vehicles = new ArrayList<Vehicle>();

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<Vehicle> getVehicles() {
        if (this.vehicles != null) {
            return Collections.unmodifiableList(this.vehicles);
        } else {
            return null;
        }
    }

    public void addVehicle(Vehicle vehicle) {
        if (this.vehicles == null) {
            this.vehicles = new ArrayList<Vehicle>();
        }
        if (!this.vehicles.contains(vehicle)) {
            this.vehicles.add(vehicle);
            vehicle.setOwner(this);
        }
    }

    // Si no posee el vehiculo, no hace nada
    public void removeVehicle(String registration) {
        if (this.vehicles != null) {
            Iterator<Vehicle> iterator = this.vehicles.iterator();
            while (iterator.hasNext()) {
                Vehicle v = iterator.next();
                if (v.getRegistration().equals(registration)) {
                    iterator.remove();
                    v.setOwner(null);
                }
            }
        }
    }

    public boolean hasVehicle(String registration) {
        if (getVehicles() == null || getVehicles().isEmpty()) {
            return false;
        }

        List<Vehicle> currentVehicles = getVehicles();

        for (Vehicle v : currentVehicles) {
            if (v.getRegistration().equalsIgnoreCase(registration)) {
                return true;
            }
        }

        return false;
    }

    public void clearVehicles() {
        if (this.vehicles != null) {
            this.vehicles.clear();
        } else {
            this.vehicles = new ArrayList<Vehicle>();
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setClientId(String clientId) {
        if (StringUtils.isBlank(clientId)) {
            this.clientId = null;
        } else {
            String tempId = parseClientId(clientId);
            if (isValidClientId(tempId)) {
                this.clientId = tempId;
            } else {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("CLIENT_ID_INVALID"));
            }
        }
    }

    public String getClientId() {
        return clientId;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        if (StringUtils.isBlank(notes)) {
            this.notes = null;
        } else {
            this.notes = notes;
        }
    }

    public void setEnabled(boolean b) {
        this.enabled = b;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    // Convierte el string en un número y lo devuelve formateado con 11 dígitos de longitud
    private String parseClientId(String id) {
        long tempId;
        try {
            tempId = Long.parseLong(id);
        } catch (NumberFormatException ignore) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("CLIENT_ID_INVALID"));
        }
        return String.format("%011d", tempId);
    }

    // Si el numero es negativo o distino de 11 digitos, no es valido.
    private boolean isValidClientId(String id) {
        if (id.length() != 11) {
            return false;
        } else {
            try {
                if (Long.parseLong(id) <= 0) {
                    return false;
                }
            } catch (NumberFormatException ignore) {
                return false;
            }
            return true;
        }

    }

    @Override
    public final boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!this.getClass().equals(o.getClass())) {
            return false;
        }
        Client e = (Client) o;

        if (e.getName() == null && this.getName() != null || e.getName() != null && !e.getName().equalsIgnoreCase(this.getName())) {
            return false;
        }
        if (e.getSurname() == null && this.getSurname() != null || e.getSurname() != null && !e.getSurname().equalsIgnoreCase(this.getSurname())) {
            return false;
        }
        if (e.getNif() == null && this.getNif() != null || e.getNif() != null && !e.getNif().equals(this.getNif())) {
            return false;
        }

        if (((e.getVehicles() == null || e.getVehicles().isEmpty()) && (this.getVehicles() != null && !this.getVehicles().isEmpty()))
                || ((e.getVehicles() != null && !e.getVehicles().isEmpty()) && (this.getVehicles() == null || this.getVehicles().isEmpty()))
                || ((e.getVehicles() != null && !e.getVehicles().isEmpty())
                && !(e.getVehicles().containsAll(this.getVehicles()) && e.getVehicles().size() == this.getVehicles().size()))) {
            return false;
        }

        if (((e.getPhone() == null || e.getPhone().isEmpty()) && (this.getPhone() != null && !this.getPhone().isEmpty()))
                || ((e.getPhone() != null && !e.getPhone().isEmpty()) && (this.getPhone() == null || this.getPhone().isEmpty()))
                || ((e.getPhone() != null && !e.getPhone().isEmpty())
                && !(e.getPhone().containsAll(this.getPhone()) && e.getPhone().size() == this.getPhone().size()))) {
            return false;
        }
        if (((e.getAddress() == null || e.getAddress().isEmpty()) && (this.getAddress() != null && !this.getAddress().isEmpty()))
                || ((e.getAddress() != null && !e.getAddress().isEmpty()) && (this.getAddress() == null || this.getAddress().isEmpty()))
                || ((e.getAddress() != null && !e.getAddress().isEmpty())
                && !(e.getAddress().containsAll(this.getAddress()) && e.getAddress().size() == this.getAddress().size()))) {
            return false;
        }
        if (((e.getEmail() == null || e.getEmail().isEmpty()) && (this.getEmail() != null && !this.getEmail().isEmpty()))
                || ((e.getEmail() != null && !e.getEmail().isEmpty()) && (this.getEmail() == null || this.getEmail().isEmpty()))
                || ((e.getEmail() != null && !e.getEmail().isEmpty())
                && !(e.getEmail().containsAll(this.getEmail()) && e.getEmail().size() == this.getEmail().size()))) {
            return false;
        }
        if (e.getNotes() == null && this.getNotes() != null || e.getNotes() != null && !e.getNotes().equalsIgnoreCase(this.getNotes())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.getName() != null ? this.getName().hashCode() : 0);
        hash = 89 * hash + (this.getSurname() != null ? this.getSurname().hashCode() : 0);
        hash = 89 * hash + (this.getNif() != null ? this.getNif().hashCode() : 0);
        hash = 89 * hash + (this.getVehicles() != null ? this.getVehicles().hashCode() : 0);
        hash = 89 * hash + (this.getPhone() != null ? this.getPhone().hashCode() : 0);
        hash = 89 * hash + (this.getAddress() != null ? this.getAddress().hashCode() : 0);
        hash = 89 * hash + (this.getEmail() != null ? this.getEmail().hashCode() : 0);
        hash = 89 * hash + (this.getNotes() != null ? this.getNotes().hashCode() : 0);
        return hash;
    }
}
