package es.regueiro.easyrepair.model.employee;

import es.regueiro.easyrepair.model.shared.NSS;
import es.regueiro.easyrepair.model.shared.Person;
import org.apache.commons.lang3.StringUtils;

public class Employee extends Person {

    private String occupation;
    private NSS nss;
    private Long id; // ID para la BD
    private String employeeId; // ID para la empresa
    private String notes;
    private int version;
    private boolean enabled = true;

    public void setOccupation(String occupation) {
        if (StringUtils.isBlank(occupation)) {
            this.occupation = null;
        } else {
            this.occupation = occupation;
        }
    }

    public String getOccupation() {
        return occupation;
    }

    public void setNss(NSS nss) {
        this.nss = nss;
    }

    public NSS getNss() {
        return nss;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setEmployeeId(String employeeId) {
        if (StringUtils.isBlank(employeeId)) {
            this.employeeId = null;
        } else {
            String tempId = parseEmployeeId(employeeId);
            if (isValidEmployeeId(tempId)) {
                this.employeeId = tempId;
            } else {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EMPLOYEE_ID_INVALID"));
            }
        }
    }

    public String getEmployeeId() {
        return employeeId;
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

    // Convierte el string en un numero y lo devuelve formateado como string de 8 digitos
    private String parseEmployeeId(String id) {
        long tempId;
        try {
            tempId = Long.parseLong(id);
        } catch (NumberFormatException ignore) {
            throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EMPLOYEE_ID_INVALID"));
        }
        return String.format("%08d", tempId);
    }

    // Si es negativo o distinto de 8 digitos, no es valido.
    private boolean isValidEmployeeId(String id) {
        if (id.length() != 8) {
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
        Employee e = (Employee) o;

        if (e.getName() == null && this.getName() != null || e.getName() != null && !e.getName().equalsIgnoreCase(this.getName())) {
            return false;
        }
        if (e.getSurname() == null && this.getSurname() != null || e.getSurname() != null && !e.getSurname().equalsIgnoreCase(this.getSurname())) {
            return false;
        }
        if (e.getNss() == null && this.getNss() != null || e.getNss() != null && !e.getNss().equals(this.getNss())) {
            return false;
        }
        if (e.getNif() == null && this.getNif() != null || e.getNif() != null && !e.getNif().equals(this.getNif())) {
            return false;
        }
        if (e.getOccupation() == null && this.getOccupation() != null || e.getOccupation() != null && !e.getOccupation().equalsIgnoreCase(this.getOccupation())) {
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
        } else {
            return true;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.getName() != null ? this.getName().hashCode() : 0);
        hash = 89 * hash + (this.getSurname() != null ? this.getSurname().hashCode() : 0);
        hash = 89 * hash + (this.getOccupation() != null ? this.getOccupation().hashCode() : 0);
        hash = 89 * hash + (this.getNss() != null ? this.getNss().hashCode() : 0);
        hash = 89 * hash + (this.getNif() != null ? this.getNif().hashCode() : 0);
        hash = 89 * hash + (this.getPhone() != null ? this.getPhone().hashCode() : 0);
        hash = 89 * hash + (this.getAddress() != null ? this.getAddress().hashCode() : 0);
        hash = 89 * hash + (this.getEmail() != null ? this.getEmail().hashCode() : 0);
        hash = 89 * hash + (this.getNotes() != null ? this.getNotes().hashCode() : 0);
        return hash;
    }
}
