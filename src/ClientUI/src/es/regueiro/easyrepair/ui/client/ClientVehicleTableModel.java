package es.regueiro.easyrepair.ui.client;

import es.regueiro.easyrepair.model.client.Client;
import es.regueiro.easyrepair.model.client.Vehicle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ClientVehicleTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
//    private List<Vehicle> data = new ArrayList<Vehicle>();
    private Client client;

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }

//    public void fill(List<Vehicle> list) {
//        data.clear();
//        if (list != null) {
//            for (Vehicle v:list) {
//                data.add(v);
//            }
//        } 
//        this.fireTableDataChanged();
//    }
    public void setClient(Client client) {
        this.client = client;
    }

    public ClientVehicleTableModel() {
        //super();
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("REGISTRATION"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("VIN"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("MAKE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("MODEL"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("YEAR"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("COLOUR"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("TYPE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("FUEL"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCECOMPANY"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCE_NUMBER"));


    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public Vehicle getRow(int row) {
        if (client != null && client.getVehicles() != null) {
            return client.getVehicles().get(row);
        } else {
            return null;
        }

    }

    @Override
    public int getRowCount() {
        if (client != null && client.getVehicles() != null) {
            return client.getVehicles().size();
        } else {
            return 0;
        }
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public String getValueAt(int rowIndex, int columnIndex) {
        if (client != null && client.getVehicles() != null) {
            switch (columnIndex) {
                case 0:
                    return String.format("%08d", client.getVehicles().get(rowIndex).getId());
                case 1:
                    return client.getVehicles().get(rowIndex).getRegistration();
                case 2:
                    return client.getVehicles().get(rowIndex).getVin();
                case 3:
                    return client.getVehicles().get(rowIndex).getMake();
                case 4:
                    return client.getVehicles().get(rowIndex).getModel();
                case 5:
                    return client.getVehicles().get(rowIndex).getYear();
                case 6:
                    return client.getVehicles().get(rowIndex).getColour();
                case 7:
                    return client.getVehicles().get(rowIndex).getType();
                case 8:
                    return client.getVehicles().get(rowIndex).getFuel();
                case 9:
                    if (client.getVehicles().get(rowIndex).getInsuranceCompany() != null) {
                        return client.getVehicles().get(rowIndex).getInsuranceCompany().getName();
                    } else {
                        return null;
                    }
                case 10:
                    return client.getVehicles().get(rowIndex).getInsuranceNumber();

                case 11:
                    if (client.getVehicles().get(rowIndex).getEnabled()) {
                        return "no";
                    } else {
                        return "yes";
                    }

                default:
                    return "";
            }
        } else {
            return "";
        }

    }

    public void removeRow(int row) {
        Vehicle veh = client.getVehicles().get(row);
        client.removeVehicle(veh.getRegistration());
        fireTableRowsDeleted(row, row);
    }

    public void removeVehicle(Vehicle ins) {
        int row = client.getVehicles().indexOf(ins);
        client.removeVehicle(ins.getRegistration());
        fireTableRowsDeleted(row, row);
    }
    // Adds the given column to the right hand side of the model

    public void addColumn(String name) {
        columns.add(name);
        fireTableStructureChanged();

    }
}