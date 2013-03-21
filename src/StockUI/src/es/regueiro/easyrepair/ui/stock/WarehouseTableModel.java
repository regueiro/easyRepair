package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.model.stock.Warehouse;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class WarehouseTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
    private List<Warehouse> data = new ArrayList<Warehouse>();

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }

    public void clear() {
        data = new ArrayList<Warehouse>();
        this.fireTableDataChanged();
    }

    public void fill(List<Warehouse> list) {
        if (list != null) {
            data = list;
            this.fireTableDataChanged();
        } else {
            clear();
        }
    }

    public List<Warehouse> getWarehouseList() {
        return data;
    }

    public WarehouseTableModel() {
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("NAME"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("EMAIL"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PHONE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("STREET"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("CITY"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("POSTALCODE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PROVINCE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("COUNTRY"));
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public Warehouse getRow(int row) {
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
                return String.format("%011d", data.get(rowIndex).getId());
            case 1:
                return data.get(rowIndex).getName();
            case 2:
                if (data.get(rowIndex).getEmail() != null) {
                    return data.get(rowIndex).getEmail().getAddress();
                } else {
                    return null;
                }
            case 3:
                if (data.get(rowIndex).getPhone() != null) {
                    return data.get(rowIndex).getPhone().getNumber();
                } else {
                    return null;
                }
            case 4:
                if (data.get(rowIndex).getAddress() != null) {
                    return data.get(rowIndex).getAddress().getStreet();
                } else {
                    return null;
                }
            case 5:
                if (data.get(rowIndex).getAddress() != null) {
                    return data.get(rowIndex).getAddress().getCity();
                } else {
                    return null;
                }
            case 6:
                if (data.get(rowIndex).getAddress() != null) {
                    return data.get(rowIndex).getAddress().getPostalCode();
                } else {
                    return null;
                }
            case 7:
                if (data.get(rowIndex).getAddress() != null) {
                    return data.get(rowIndex).getAddress().getProvince();
                } else {
                    return null;
                }
            case 8:
                if (data.get(rowIndex).getAddress() != null) {
                    return data.get(rowIndex).getAddress().getCountry();
                } else {
                    return null;
                }
            case 9:
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

    public void removeWarehouse(Warehouse ins) {
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