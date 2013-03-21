package es.regueiro.easyrepair.model.stock;

import es.regueiro.easyrepair.model.shared.Company;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.lang3.StringUtils;

public class Supplier extends Company {

    private Long id;
    private int version;
    private String category;
    private String web;
    private String paymentMethod;
    private String shippingMethod;
    private String notes;
    private boolean enabled = true;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (StringUtils.isBlank(category)) {
            this.category = null;
        } else {
            this.category = category;
        }
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        if (StringUtils.isBlank(notes)) {
            this.notes = null;
        } else {
            this.notes = notes;
        }
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        if (StringUtils.isBlank(paymentMethod)) {
            this.paymentMethod = null;
        } else {
            this.paymentMethod = paymentMethod;
        }
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        if (StringUtils.isBlank(shippingMethod)) {
            this.shippingMethod = null;
        } else {
            this.shippingMethod = shippingMethod;
        }
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        if (StringUtils.isBlank(web)) {
            this.web = null;
        } else {
            try {
                String url = web;
                if (!web.startsWith("http://")) {
                    url = "http://".concat(web);
                }
                URI uri = new URI(url);
                this.web = uri.toString();
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("WEB_ADDRESS_INVALID"));
            }

        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Supplier other = (Supplier) obj;

        if ((this.getName() == null) ? (other.getName() != null) : !this.getName().equals(other.getName())) {
            return false;
        }
        if ((this.getNif() == null) ? (other.getNif() != null) : !this.getNif().equals(other.getNif())) {
            return false;
        }
        if ((this.category == null) ? (other.category != null) : !this.category.equals(other.category)) {
            return false;
        }
        if ((this.web == null) ? (other.web != null) : !this.web.equals(other.web)) {
            return false;
        }
        if ((this.paymentMethod == null) ? (other.paymentMethod != null) : !this.paymentMethod.equals(other.paymentMethod)) {
            return false;
        }
        if ((this.shippingMethod == null) ? (other.shippingMethod != null) : !this.shippingMethod.equals(other.shippingMethod)) {
            return false;
        }
        if ((this.notes == null) ? (other.notes != null) : !this.notes.equals(other.notes)) {
            return false;
        }
        if (((other.getPhone() == null || other.getPhone().isEmpty()) && (this.getPhone() != null && !this.getPhone().isEmpty()))
                || ((other.getPhone() != null && !other.getPhone().isEmpty()) && (this.getPhone() == null || this.getPhone().isEmpty()))
                || ((other.getPhone() != null && !other.getPhone().isEmpty())
                && !(other.getPhone().containsAll(this.getPhone()) && other.getPhone().size() == this.getPhone().size()))) {
            return false;
        }
        if (((other.getAddress() == null || other.getAddress().isEmpty()) && (this.getAddress() != null && !this.getAddress().isEmpty()))
                || ((other.getAddress() != null && !other.getAddress().isEmpty()) && (this.getAddress() == null || this.getAddress().isEmpty()))
                || ((other.getAddress() != null && !other.getAddress().isEmpty())
                && !(other.getAddress().containsAll(this.getAddress()) && other.getAddress().size() == this.getAddress().size()))) {
            return false;
        }
        if (((other.getEmail() == null || other.getEmail().isEmpty()) && (this.getEmail() != null && !this.getEmail().isEmpty()))
                || ((other.getEmail() != null && !other.getEmail().isEmpty()) && (this.getEmail() == null || this.getEmail().isEmpty()))
                || ((other.getEmail() != null && !other.getEmail().isEmpty())
                && !(other.getEmail().containsAll(this.getEmail()) && other.getEmail().size() == this.getEmail().size()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + (this.getName() != null ? this.getName().hashCode() : 0);
        hash = 31 * hash + (this.getNif() != null ? this.getNif().hashCode() : 0);
        hash = 31 * hash + (this.category != null ? this.category.hashCode() : 0);
        hash = 31 * hash + (this.web != null ? this.web.hashCode() : 0);
        hash = 31 * hash + (this.paymentMethod != null ? this.paymentMethod.hashCode() : 0);
        hash = 31 * hash + (this.shippingMethod != null ? this.shippingMethod.hashCode() : 0);
        hash = 31 * hash + (this.notes != null ? this.notes.hashCode() : 0);
        hash = 31 * hash + (this.getPhone() != null ? this.getPhone().hashCode() : 0);
        hash = 31 * hash + (this.getAddress() != null ? this.getAddress().hashCode() : 0);
        hash = 31 * hash + (this.getEmail() != null ? this.getEmail().hashCode() : 0);
        return hash;
    }
}
