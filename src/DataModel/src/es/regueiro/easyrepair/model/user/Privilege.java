package es.regueiro.easyrepair.model.user;

public enum Privilege {

    ADMIN(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ADMIN"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("ADMIN_EVERYTHING")),
    USER(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("USER"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("BROWSE_USERS_AND_ROLES")),
    USER_EDIT(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("USER_EDIT"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EDIT_USER_AND_ROLES")),
    EMPLOYEE(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EMPLOYEE"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("BROWSE_EMPLOYEES")),
    EMPLOYEE_EDIT(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EMPLOYEE_EDIT"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EDIT_EMPLOYEES")),
    CLIENT(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("CLIENT"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("BROWSE_CLIENTS")),
    CLIENT_EDIT(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("CLIENT_EDIT"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EDIT_CLIENTS")),
    VEHICLE(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("VEHICLE"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("BROWSE_VEHICLES")),
    VEHICLE_EDIT(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("VEHICLE_EDIT"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EDIT_VEHICLES")),
    INSURANCE_COMPANY(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("INSURANCE_COMPANY"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("BROWSE_INSURANCE_COMPANIES")),
    INSURANCE_COMPANY_EDIT(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("INSURANCE_COMPANY_EDIT"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EDIT_INSURANCE_COMPANIES")),
    REPAIR_ORDER(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("REPAIR_ORDER"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("BROWSE_REPAIR_ORDERS")),
    REPAIR_ORDER_EDIT(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("REPAIR_ORDER_EDIT"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EDIT_REPAIR_ORDERS")),
    REPAIR_ESTIMATE(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("REPAIR_ESTIMATE"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("BROWSE_REPAIR_ESTIMATES")),
    REPAIR_ESTIMATE_EDIT(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("REPAIR_ESTIMATE_EDIT"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EDIT_REPAIR_ESTIMATES")),
    REPAIR_INVOICE(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("REPAIR_INVOICE"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("BROWSE_REPAIR_INVOICES")),
    REPAIR_INVOICE_EDIT(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("REPAIR_INVOICE_EDIT"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EDIT_REPAIR_INVOICES")),
    LABOUR(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("LABOUR"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("BROWSE_LABOUR")),
    LABOUR_EDIT(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("LABOUR_EDIT"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EDIT_LABOUR")),
    PART_ORDER(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PART_ORDER"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("BROWSE_PART_ORDERS")),
    PART_ORDER_EDIT(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PART_ORDER_EDIT"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EDIT_PART_ORDERS")),
    PART_ORDER_INVOICE(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PART_ORDER_INVOICE"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("BROWSE_PART_ORDER_INVOICES")),
    PART_ORDER_INVOICE_EDIT(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PART_ORDER_INVOICE_EDIT"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EDIT_PART_ORDER_INVOICES")),
    PART(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PART"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("BROWSE_PARTS")),
    PART_EDIT(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PART_EDIT"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EDIT_PARTS")),
    PART_STOCK(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PART_STOCK"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("BROWSE_PARTS_STOCK")),
    PART_STOCK_EDIT(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("PART_STOCK_EDIT"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EDIT_PARTS_STOCK")),
    SUPPLIER(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("SUPPLIER"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("BROWSE_SUPPLIERS")),
    SUPPLIER_EDIT(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("SUPPLIER_EDIT"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EDIT_SUPPLIERS")),
    WAREHOUSE(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("WAREHOUSE"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("BROWSE_WAREHOUSES")),
    WAREHOUSE_EDIT(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("WAREHOUSE_EDIT"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/model/Bundle").getString("EDIT_WAREHOUSES"));

    private String displayName;
    private String description;

    Privilege(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
