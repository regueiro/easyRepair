package es.regueiro.easyrepair.model.shared;

public class NSS {

    private String number;

    public NSS() {
    }

    public NSS(String number) {
        String num = parseNumber(number);
        if (isValid(num)) {
            this.number = num;
        } else {
            throw new IllegalArgumentException(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("NSS_INVALID"), new Object[]{number}));
        }
    }

    public void setNumber(String number) {
        String num = parseNumber(number);
        if (isValid(num)) {
            this.number = num;
        } else {
            throw new IllegalArgumentException(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("NSS_INVALID"), new Object[]{number}));
        }
    }

    public String getNumber() {
        return this.number;
    }

    private String parseNumber(String number) {
        if (number != null) {
            if (number.length() == 14) {
                String slash = number.substring(2, 3) + number.substring(number.length() - 3, number.length() - 2);
                if (!slash.equals("//")) {
                    return null;
                } else {
                    return number.substring(0, 2) + number.substring(3, number.length() - 3) + number.substring(number.length() - 2, number.length());
                }
            } else {
                return number;
            }
        }
        return null;
    }

    private boolean isValid(String number) {
        if (number != null) {
            if (number.startsWith("-")) {
                return false;
            } else if (number.length() == 12) {
                try {
                    long a = Long.parseLong(number.substring(0, 2));
                    long b = Long.parseLong(number.substring(2, number.length() - 2));
                    long c = Long.parseLong(number.substring(number.length() - 2, number.length()));
                    long d;
                    if (b < 10000000) {
                        d = b + a * 10000000;
                    } else {
                        d = Long.parseLong(a + "" + b);
                    }
                    return (c == d % 97);

                } catch (NumberFormatException e) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public final boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!this.getClass().equals(o.getClass())) {
            return false;
        }
        NSS n = (NSS) o;
        if (this.getNumber() == null && n.getNumber() == null || n.getNumber() != null && n.getNumber().equalsIgnoreCase(this.getNumber())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.number != null ? this.number.hashCode() : 0);
        return hash;
    }
}