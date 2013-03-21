package es.regueiro.easyrepair.ui.client;

import es.regueiro.easyrepair.model.client.Vehicle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class VehicleTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
    private List<Vehicle> data = new ArrayList<Vehicle>();

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }

    public void fill(List<Vehicle> list) {
        data = list;
        this.fireTableDataChanged();
    }
    
    public List<Vehicle> getVehicleList() {
        return data;
    }

    public VehicleTableModel() {
        //super();
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("REGISTRATION"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("OWNER"));
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
        return data.get(row);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public String getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.format("%08d", data.get(rowIndex).getId());
            case 1:
                return data.get(rowIndex).getRegistration();
            case 2:
                if (data.get(rowIndex).getOwner() != null) {
                    String text = data.get(rowIndex).getOwner().getName();
                    if (data.get(rowIndex).getOwner().getSurname() != null) {
                        text += " " + data.get(rowIndex).getOwner().getSurname();
                    }
                    return text;
                } else {
                    return null;
                }
            case 3:
                return data.get(rowIndex).getVin();
            case 4:
                return data.get(rowIndex).getMake();
            case 5:
                return data.get(rowIndex).getModel();
            case 6:
                return data.get(rowIndex).getYear();
            case 7:
                return data.get(rowIndex).getColour();
            case 8:
                return data.get(rowIndex).getType();
            case 9:
                return data.get(rowIndex).getFuel();
            case 10:
                if (data.get(rowIndex).getInsuranceCompany() != null) {
                    return data.get(rowIndex).getInsuranceCompany().getName();
                } else {
                    return null;
                }
            case 11:
                return data.get(rowIndex).getInsuranceNumber();

            case 12:
                if (data.get(rowIndex).getEnabled()) {
                    return "no";
                } else {
                    return "yes";
                }

            default:
                return "";
        }
    }

    public void removeRow(int row) {
        data.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void removeVehicle(Vehicle ins) {
        int row = data.indexOf(ins);
        data.remove(ins);
        fireTableRowsDeleted(row, row);
    }
    // Adds the given column to the right hand side of the model

    public void addColumn(String name) {
        columns.add(name);
        fireTableStructureChanged();

    }
}