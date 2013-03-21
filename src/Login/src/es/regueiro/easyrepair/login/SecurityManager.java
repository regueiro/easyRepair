package es.regueiro.easyrepair.login;

import es.regueiro.easyrepair.api.user.controller.UserController;
import es.regueiro.easyrepair.model.user.Privilege;
import es.regueiro.easyrepair.model.user.User;
import java.util.ArrayList;
import java.util.List;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Lookup;

public class SecurityManager {

    private static SecurityManager inst = new SecurityManager();
    private User user;
    private List<PermissionGroup> groups = new ArrayList<PermissionGroup>();
    private UserController controller = Lookup.getDefault().lookup(UserController.class);
    private boolean loggedIn = false;

    private SecurityManager() {
    }

    public static SecurityManager getDefault() {
        return inst;
    }

    public boolean login(String user, String password) {

        this.user = null;
        this.groups.clear();

        try {
            if (controller.isValidLogin(user, password)) {
                List<Privilege> priv = controller.getUserPrivileges(user);
                if (!priv.contains(Privilege.ADMIN)) {
                    this.groups.remove(PermissionGroup.SHOW_OPTIONS);
                    this.groups.add(PermissionGroup.HIDE_OPTIONS);

                    if (!(priv.contains(Privilege.CLIENT) || priv.contains(Privilege.VEHICLE) || priv.contains(Privilege.INSURANCE_COMPANY))) {
                        this.groups.remove(PermissionGroup.ACTIVATE_CLIENT);
                        this.groups.add(PermissionGroup.DEACTIVATE_CLIENT);
                    } else {
                        this.groups.remove(PermissionGroup.DEACTIVATE_CLIENT);
                        this.groups.add(PermissionGroup.ACTIVATE_CLIENT);
                        if (!priv.contains(Privilege.CLIENT)) {
                            this.groups.remove(PermissionGroup.SHOW_CLIENTS);
                            this.groups.add(PermissionGroup.HIDE_CLIENTS);
                        } else {
                            this.groups.remove(PermissionGroup.HIDE_CLIENTS);
                            this.groups.add(PermissionGroup.SHOW_CLIENTS);
                        }
                        if (!priv.contains(Privilege.VEHICLE)) {
                            this.groups.remove(PermissionGroup.SHOW_VEHICLES);
                            this.groups.add(PermissionGroup.HIDE_VEHICLES);
                        } else {
                            this.groups.remove(PermissionGroup.HIDE_VEHICLES);
                            this.groups.add(PermissionGroup.SHOW_VEHICLES);
                        }
                        if (!priv.contains(Privilege.INSURANCE_COMPANY)) {
                            this.groups.remove(PermissionGroup.SHOW_INSURANCECOMPANIES);
                            this.groups.add(PermissionGroup.HIDE_INSURANCECOMPANIES);
                        } else {
                            this.groups.remove(PermissionGroup.HIDE_INSURANCECOMPANIES);
                            this.groups.add(PermissionGroup.SHOW_INSURANCECOMPANIES);
                        }
                    }

                    if (!(priv.contains(Privilege.EMPLOYEE))) {
                        this.groups.remove(PermissionGroup.ACTIVATE_EMPLOYEE);
                        this.groups.add(PermissionGroup.DEACTIVATE_EMPLOYEE);
                        this.groups.remove(PermissionGroup.SHOW_EMPLOYEES);
                        this.groups.add(PermissionGroup.HIDE_EMPLOYEES);
                    } else {
                        this.groups.remove(PermissionGroup.DEACTIVATE_EMPLOYEE);
                        this.groups.add(PermissionGroup.ACTIVATE_EMPLOYEE);
                        this.groups.remove(PermissionGroup.HIDE_EMPLOYEES);
                        this.groups.add(PermissionGroup.SHOW_EMPLOYEES);

                    }


                    if (!(priv.contains(Privilege.REPAIR_ORDER) || priv.contains(Privilege.REPAIR_INVOICE) || priv.contains(Privilege.REPAIR_ESTIMATE) || priv.contains(Privilege.LABOUR))) {
                        this.groups.remove(PermissionGroup.ACTIVATE_REPAIR);
                        this.groups.add(PermissionGroup.DEACTIVATE_REPAIR);
                    } else {
                        this.groups.remove(PermissionGroup.DEACTIVATE_REPAIR);
                        this.groups.add(PermissionGroup.ACTIVATE_REPAIR);
                        if (!priv.contains(Privilege.REPAIR_ORDER)) {
                            this.groups.remove(PermissionGroup.SHOW_REPAIR_ORDERS);
                            this.groups.add(PermissionGroup.HIDE_REPAIR_ORDERS);
                        } else {
                            this.groups.remove(PermissionGroup.HIDE_REPAIR_ORDERS);
                            this.groups.add(PermissionGroup.SHOW_REPAIR_ORDERS);
                        }
                        if (!priv.contains(Privilege.REPAIR_INVOICE)) {
                            this.groups.remove(PermissionGroup.SHOW_REPAIR_INVOICES);
                            this.groups.add(PermissionGroup.HIDE_REPAIR_INVOICES);
                        } else {
                            this.groups.remove(PermissionGroup.HIDE_REPAIR_INVOICES);
                            this.groups.add(PermissionGroup.SHOW_REPAIR_INVOICES);
                        }
                        if (!priv.contains(Privilege.REPAIR_ESTIMATE)) {
                            this.groups.remove(PermissionGroup.SHOW_REPAIR_ESTIMATES);
                            this.groups.add(PermissionGroup.HIDE_REPAIR_ESTIMATES);
                        } else {
                            this.groups.remove(PermissionGroup.HIDE_REPAIR_ESTIMATES);
                            this.groups.add(PermissionGroup.SHOW_REPAIR_ESTIMATES);
                        }
                        if (!priv.contains(Privilege.LABOUR)) {
                            this.groups.remove(PermissionGroup.SHOW_LABOUR);
                            this.groups.add(PermissionGroup.HIDE_LABOUR);
                        } else {
                            this.groups.remove(PermissionGroup.HIDE_LABOUR);
                            this.groups.add(PermissionGroup.SHOW_LABOUR);
                        }
                    }

                    if (!(priv.contains(Privilege.PART_ORDER) || priv.contains(Privilege.PART_ORDER_INVOICE) || priv.contains(Privilege.SUPPLIER) || priv.contains(Privilege.PART) || priv.contains(Privilege.PART_STOCK) || priv.contains(Privilege.WAREHOUSE))) {
                        this.groups.remove(PermissionGroup.ACTIVATE_STOCK);
                        this.groups.add(PermissionGroup.DEACTIVATE_STOCK);
                    } else {
                        this.groups.remove(PermissionGroup.DEACTIVATE_STOCK);
                        this.groups.add(PermissionGroup.ACTIVATE_STOCK);
                        if (!priv.contains(Privilege.PART_ORDER)) {
                            this.groups.remove(PermissionGroup.SHOW_PART_ORDERS);
                            this.groups.add(PermissionGroup.HIDE_PART_ORDERS);
                        } else {
                            this.groups.remove(PermissionGroup.HIDE_PART_ORDERS);
                            this.groups.add(PermissionGroup.SHOW_PART_ORDERS);
                        }
                        if (!priv.contains(Privilege.PART_ORDER_INVOICE)) {
                            this.groups.remove(PermissionGroup.SHOW_PART_ORDER_INVOICES);
                            this.groups.add(PermissionGroup.HIDE_PART_ORDER_INVOICES);
                        } else {
                            this.groups.remove(PermissionGroup.HIDE_PART_ORDER_INVOICES);
                            this.groups.add(PermissionGroup.SHOW_PART_ORDER_INVOICES);
                        }
                        if (!priv.contains(Privilege.PART) && !priv.contains(Privilege.PART_STOCK)) {
                            this.groups.remove(PermissionGroup.SHOW_PARTS);
                            this.groups.add(PermissionGroup.HIDE_PARTS);
                        } else {
                            this.groups.remove(PermissionGroup.HIDE_PARTS);
                            this.groups.add(PermissionGroup.SHOW_PARTS);
                        }
                        if (!priv.contains(Privilege.SUPPLIER)) {
                            this.groups.remove(PermissionGroup.SHOW_SUPPLIERS);
                            this.groups.add(PermissionGroup.HIDE_SUPPLIERS);
                        } else {
                            this.groups.remove(PermissionGroup.HIDE_SUPPLIERS);
                            this.groups.add(PermissionGroup.SHOW_SUPPLIERS);
                        }
                        if (!priv.contains(Privilege.WAREHOUSE)) {
                            this.groups.remove(PermissionGroup.SHOW_WAREHOUSES);
                            this.groups.add(PermissionGroup.HIDE_WAREHOUSES);
                        } else {
                            this.groups.remove(PermissionGroup.HIDE_WAREHOUSES);
                            this.groups.add(PermissionGroup.SHOW_WAREHOUSES);
                        }
                    }

                    if (!(priv.contains(Privilege.USER))) {
                        this.groups.remove(PermissionGroup.ACTIVATE_USER);
                        this.groups.add(PermissionGroup.DEACTIVATE_USER);
                        this.groups.remove(PermissionGroup.SHOW_USERS);
                        this.groups.add(PermissionGroup.HIDE_USERS);
                    } else {
                        this.groups.remove(PermissionGroup.DEACTIVATE_USER);
                        this.groups.add(PermissionGroup.ACTIVATE_USER);
                        this.groups.remove(PermissionGroup.HIDE_USERS);
                        this.groups.add(PermissionGroup.SHOW_USERS);
                    }

                } else {
                    this.groups.remove(PermissionGroup.HIDE_OPTIONS);
                    this.groups.add(PermissionGroup.SHOW_OPTIONS);
                    this.groups.remove(PermissionGroup.DEACTIVATE_CLIENT);
                    this.groups.add(PermissionGroup.ACTIVATE_CLIENT);
                    this.groups.remove(PermissionGroup.HIDE_CLIENTS);
                    this.groups.add(PermissionGroup.SHOW_CLIENTS);
                    this.groups.remove(PermissionGroup.HIDE_VEHICLES);
                    this.groups.add(PermissionGroup.SHOW_VEHICLES);
                    this.groups.remove(PermissionGroup.HIDE_INSURANCECOMPANIES);
                    this.groups.add(PermissionGroup.SHOW_INSURANCECOMPANIES);
                    this.groups.remove(PermissionGroup.DEACTIVATE_EMPLOYEE);
                    this.groups.add(PermissionGroup.ACTIVATE_EMPLOYEE);
                    this.groups.remove(PermissionGroup.HIDE_EMPLOYEES);
                    this.groups.add(PermissionGroup.SHOW_EMPLOYEES);
                    this.groups.remove(PermissionGroup.DEACTIVATE_REPAIR);
                    this.groups.add(PermissionGroup.ACTIVATE_REPAIR);
                    this.groups.remove(PermissionGroup.HIDE_REPAIR_ORDERS);
                    this.groups.add(PermissionGroup.SHOW_REPAIR_ORDERS);
                    this.groups.remove(PermissionGroup.HIDE_REPAIR_INVOICES);
                    this.groups.add(PermissionGroup.SHOW_REPAIR_INVOICES);
                    this.groups.remove(PermissionGroup.HIDE_REPAIR_ESTIMATES);
                    this.groups.add(PermissionGroup.SHOW_REPAIR_ESTIMATES);
                    this.groups.remove(PermissionGroup.HIDE_LABOUR);
                    this.groups.add(PermissionGroup.SHOW_LABOUR);
                    this.groups.remove(PermissionGroup.DEACTIVATE_STOCK);
                    this.groups.add(PermissionGroup.ACTIVATE_STOCK);
                    this.groups.remove(PermissionGroup.HIDE_PART_ORDERS);
                    this.groups.add(PermissionGroup.SHOW_PART_ORDERS);
                    this.groups.remove(PermissionGroup.HIDE_PARTS);
                    this.groups.add(PermissionGroup.SHOW_PARTS);
                    this.groups.remove(PermissionGroup.HIDE_SUPPLIERS);
                    this.groups.add(PermissionGroup.SHOW_SUPPLIERS);
                    this.groups.remove(PermissionGroup.HIDE_WAREHOUSES);
                    this.groups.add(PermissionGroup.SHOW_WAREHOUSES);
                    this.groups.remove(PermissionGroup.DEACTIVATE_USER);
                    this.groups.add(PermissionGroup.ACTIVATE_USER);
                    this.groups.remove(PermissionGroup.HIDE_USERS);
                    this.groups.add(PermissionGroup.SHOW_USERS);
                }

                this.user = controller.getUserByName(user);
                loggedIn = true;
                return loggedIn;
            } else {
                this.user = null;
                this.groups.clear();
                loggedIn = false;
                return loggedIn;
            }
        } catch (Exception e) {
            this.user = null;
            this.groups.clear();
            loggedIn = false;
            NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
            Object retval = DialogDisplayer.getDefault().notify(d);
        }
        return false;

    }

    public User getUser() {
        return user;
    }

    public boolean userHasPrivilege(Privilege priv) {
        return getUser().getRole().hasPrivilege(Privilege.ADMIN) || this.getUser().getRole().hasPrivilege(priv);
    }

    public List<PermissionGroup> getPermissionGroups() {
        return this.groups;
    }

    public boolean isUserLoggedIn() {
        return loggedIn;
    }
}
