package es.regueiro.easyrepair.model.client;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

public class Vehicle {

    private String registration;
    private String vin;
    private String make;
    private String model;
    private String year;
    private String colour;
    private String type;
    private String fuel;
    private String insuranceNumber;
    private InsuranceCompany insuranceCompany;
    private Client owner;
    private Long id;  // ID para la base de datos
    private String notes;
    private boolean enabled = true;

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        if (StringUtils.isBlank(colour)) {
            this.colour = null;
        } else {
            this.colour = colour;
        }
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        if (StringUtils.isBlank(fuel)) {
            this.fuel = null;
        } else {
            this.fuel = fuel;
        }
    }

    public InsuranceCompany getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(InsuranceCompany insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        if (StringUtils.isBlank(insuranceNumber)) {
            this.insuranceNumber = null;
        } else {
            this.insuranceNumber = insuranceNumber;
        }
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        if (StringUtils.isBlank(make)) {
            this.make = null;
        } else {
            this.make = make;
        }
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        if (StringUtils.isBlank(model)) {
            this.model = null;
        } else {
            this.model = model;
        }
    }

    public Client getOwner() {
        return owner;
    }

    // Si ya tiene dueño, lo elimina, elimina el vehículo de la lista del dueño
    // y luego añade el nuevo dueño.
    public void setOwner(Client owner) {
        if (this.owner != null && this.owner != owner) {
            if (this.owner.hasVehicle(this.registration)) {
                this.owner.removeVehicle(this.getRegistration());
            }
        }
        if (this.owner != owner) {
            this.owner = null;
            if (owner != null) {
                owner.addVehicle(this);
                this.owner = owner;
            }
        }

    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        if (StringUtils.isBlank(registration)) {
            this.registration = null;
        } else {
            this.registration = registration;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (StringUtils.isBlank(type)) {
            this.type = null;
        } else {
            this.type = type;
        }
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getYear() {
        return year;
    }

    // Si el año no es un número o no tiene 4 dígitos es inválido
    public void setYear(String desiredYear) {
        LocalDate date = new LocalDate();
        if (desiredYear != null && desiredYear.length() != 4) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("YEAR_MUSH_HAVE_4_DIGITS"));
        }
        if (desiredYear != null) {
            int tempYear;
            try {
                tempYear = Integer.parseInt(desiredYear);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("YEAR_INVALID"));
            }
            date = date.withYear(tempYear);
            this.year = date.getYear()+"";
        } else {
            this.year = null;
        }

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    // El registro es valido si tiene dueño y matricula
    public void validate() {
        if (this.owner == null) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("OWNER_NOT_NULL"));
        }
        if (this.registration == null) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("REGISTRATION_NOT_NULL"));
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
        Vehicle v = (Vehicle) o;

        if (v.getColour() == null && this.getColour() != null || v.getColour() != null && !v.getColour().equalsIgnoreCase(this.getColour())) {
            return false;
        }
        if (v.getFuel() == null && this.getFuel() != null || v.getFuel() != null && !v.getFuel().equalsIgnoreCase(this.getFuel())) {
            return false;
        }
        if (v.getInsuranceCompany() == null && this.getInsuranceCompany() != null || v.getInsuranceCompany() != null && !v.getInsuranceCompany().equals(this.getInsuranceCompany())) {
            return false;
        }
        if (v.getInsuranceNumber() == null && this.getInsuranceNumber() != null || v.getInsuranceNumber() != null && !v.getInsuranceNumber().equalsIgnoreCase(this.getInsuranceNumber())) {
            return false;
        }
        if (v.getMake() == null && this.getMake() != null || v.getMake() != null && !v.getMake().equalsIgnoreCase(this.getMake())) {
            return false;
        }
        if (v.getModel() == null && this.getModel() != null || v.getModel() != null && !v.getModel().equalsIgnoreCase(this.getModel())) {
            return false;
        }
        if (v.getRegistration() == null && this.getRegistration() != null || v.getRegistration() != null && !v.getRegistration().equalsIgnoreCase(this.getRegistration())) {
            return false;
        }
        if (v.getType() == null && this.getType() != null || v.getType() != null && !v.getType().equalsIgnoreCase(this.getType())) {
            return false;
        }
        if (v.getVin() == null && this.getVin() != null || v.getVin() != null && !v.getVin().equalsIgnoreCase(this.getVin())) {
            return false;
        }
        if (v.getYear() == null && this.getYear() != null || v.getYear() != null && !v.getYear().equalsIgnoreCase(this.getYear())) {
            return false;
        }
        if (v.getNotes() == null && this.getNotes() != null || v.getNotes() != null && !v.getNotes().equalsIgnoreCase(this.getNotes())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.getRegistration() != null ? this.getRegistration().hashCode() : 0);
        hash = 61 * hash + (this.getVin() != null ? this.getVin().hashCode() : 0);
        hash = 61 * hash + (this.getMake() != null ? this.getMake().hashCode() : 0);
        hash = 61 * hash + (this.getModel() != null ? this.getModel().hashCode() : 0);
        hash = 61 * hash + (this.getYear()!= null ? this.getYear().hashCode() : 0);
        hash = 61 * hash + (this.getColour() != null ? this.getColour().hashCode() : 0);
        hash = 61 * hash + (this.getType() != null ? this.getType().hashCode() : 0);
        hash = 61 * hash + (this.getFuel() != null ? this.getFuel().hashCode() : 0);
        hash = 61 * hash + (this.getInsuranceNumber() != null ? this.getInsuranceNumber().hashCode() : 0);
        hash = 61 * hash + (this.getInsuranceCompany() != null ? this.getInsuranceCompany().hashCode() : 0);
        hash = 61 * hash + (this.getNotes() != null ? this.getNotes().hashCode() : 0);
        return hash;
    }
}
