package es.regueiro.easyrepair.model.client;

import es.regueiro.easyrepair.model.shared.Company;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.lang3.StringUtils;

public class InsuranceCompany extends Company {

    private Long id;
    private String web;
    private String notes;
    private int version;
    private boolean enabled = true;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getWeb() {
        return this.web;
    }

    // Convierte el string en una url con http y comprueba que sea válida antes de asignarla
    public void setWeb(String web) {
        if (StringUtils.isBlank(web)) {
            this.web = null;
        } else {
            try {
                String url = web;
                if (!web.startsWith("http://") && !web.startsWith("https://")) {
                    url = "http://".concat(web);
                }
                URI uri = new URI(url);
                this.web = uri.toString();
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("WEB_ADDRESS_INVALID"));
            }

        }
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

    @Override
    public final boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!this.getClass().equals(o.getClass())) {
            return false;
        }
        InsuranceCompany e = (InsuranceCompany) o;

        if (e.getName() == null && this.getName() != null || e.getName() != null && !e.getName().equalsIgnoreCase(this.getName())) {
            return false;
        }
        if (e.getNif() == null && this.getNif() != null || e.getNif() != null && !e.getNif().equals(this.getNif())) {
            return false;
        }

        if (e.getWeb() == null && this.getWeb() != null || e.getWeb() != null && !e.getWeb().equals(this.getWeb())) {
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
        hash = 89 * hash + (this.getWeb() != null ? this.getWeb().hashCode() : 0);
        hash = 89 * hash + (this.getNif() != null ? this.getNif().hashCode() : 0);
        hash = 89 * hash + (this.getPhone() != null ? this.getPhone().hashCode() : 0);
        hash = 89 * hash + (this.getAddress() != null ? this.getAddress().hashCode() : 0);
        hash = 89 * hash + (this.getEmail() != null ? this.getEmail().hashCode() : 0);
        hash = 89 * hash + (this.getNotes() != null ? this.getNotes().hashCode() : 0);
        return hash;
    }
}
