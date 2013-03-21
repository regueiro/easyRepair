/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.login;

import java.net.URL;

/**
 *
 * @author Heiko
 */
public enum PermissionGroup {

    HIDE_OPTIONS("Hide Options", "configs/hide_options.xml"),
    SHOW_OPTIONS("Show Options", "configs/show_options.xml"),
    
    HIDE_EMPLOYEES("Hide Employees", "configs/hide_employees.xml"),
    SHOW_EMPLOYEES("Show Employees", "configs/show_employees.xml"),
    ACTIVATE_EMPLOYEE("Activate Employee Module", "configs/activate_employee.xml"),
    DEACTIVATE_EMPLOYEE("Deactivate Employee Module", "configs/deactivate_employee.xml"),
        
    HIDE_CLIENTS("Hide Clients", "configs/hide_clients.xml"),
    SHOW_CLIENTS("Show Clients", "configs/show_client.xml"),
    HIDE_VEHICLES("Hide Vehicles", "configs/hide_vehicles.xml"),
    SHOW_VEHICLES("Show Vehicles", "configs/show_client.xml"),
    HIDE_INSURANCECOMPANIES("Hide Insurance Companies", "configs/hide_insurance.xml"),
    SHOW_INSURANCECOMPANIES("Show Insurance Companies", "configs/show_client.xml"),
    ACTIVATE_CLIENT("Activate Client Module", "configs/activate_client.xml"),
    DEACTIVATE_CLIENT("Deactivate Client Module", "configs/deactivate_client.xml"),
    
    HIDE_REPAIR_ORDERS("Hide Repair Orders", "configs/hide_repair_orders.xml"),
    SHOW_REPAIR_ORDERS("Show Repair Orders", "configs/show_repair.xml"),
    HIDE_REPAIR_ESTIMATES("Hide Repair Estimates", "configs/hide_repair_estimates.xml"),
    SHOW_REPAIR_ESTIMATES("Show Repair Estimates", "configs/show_repair.xml"),
    HIDE_REPAIR_INVOICES("Hide Repair Invoices", "configs/hide_repair_invoices.xml"),
    SHOW_REPAIR_INVOICES("Show Repair Invoices", "configs/show_repair.xml"),
    HIDE_LABOUR("Hide Labour", "configs/hide_labour.xml"),
    SHOW_LABOUR("Show Labour", "configs/show_repair.xml"),
    ACTIVATE_REPAIR("Activate Repair Module", "configs/activate_repair.xml"),
    DEACTIVATE_REPAIR("Deactivate Repair Module", "configs/deactivate_repair.xml"),
    
    HIDE_PART_ORDERS("Hide Part Orders", "configs/hide_part_orders.xml"),
    SHOW_PART_ORDERS("Show Part Orders", "configs/show_stock.xml"),
    HIDE_PART_ORDER_INVOICES("Hide Part Order Invoices", "configs/hide_part_order_invoices.xml"),
    SHOW_PART_ORDER_INVOICES("Show Part Order Invoices", "configs/show_stock.xml"),
    HIDE_PARTS("Hide Parts", "configs/hide_parts.xml"),
    SHOW_PARTS("Show Parts", "configs/show_stock.xml"),
    HIDE_SUPPLIERS("Hide Suppliers", "configs/hide_supplier.xml"),
    SHOW_SUPPLIERS("Show Suppliers", "configs/show_stock.xml"),
    HIDE_WAREHOUSES("Hide Warehouses", "configs/hide_warehouse.xml"),
    SHOW_WAREHOUSES("Show Warehouses", "configs/show_stock.xml"),
    ACTIVATE_STOCK("Activate Stock Module", "configs/activate_stock.xml"),
    DEACTIVATE_STOCK("Deactivate Stock Module", "configs/deactivate_stock.xml"),
    
    HIDE_USERS("Hide Users", "configs/hide_users.xml"),
    SHOW_USERS("Show Users", "configs/show_user.xml"),
    ACTIVATE_USER("Activate User Module", "configs/activate_user.xml"),
    DEACTIVATE_USER("Deactivate User Module", "configs/deactivate_user.xml");
    
    private String groupName;
    private String configPath;

    PermissionGroup(String groupName, String configPath) {
        this.groupName = groupName;
        this.configPath = configPath;
    }

    public URL getConfig() {
        return PermissionGroup.class.getResource(configPath);
    }

    public String getGroup() {
        return this.groupName;
    }

    public boolean equals(String groupName) {
        return this.groupName.equals(groupName);
    }

    @Override
    public String toString() {
        return this.groupName;
    }

    public static PermissionGroup get(String groupName) {
        for (PermissionGroup group : PermissionGroup.values()) {
            if (group.groupName.equals(groupName)) {
                return group;
            }
        }
        return null;
    }
}
