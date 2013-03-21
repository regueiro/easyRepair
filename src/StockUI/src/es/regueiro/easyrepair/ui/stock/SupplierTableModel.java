package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.model.stock.Supplier;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class SupplierTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
    private List<Supplier> data = new ArrayList<Supplier>();

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }

    public void clear() {
        data = new ArrayList<Supplier>();
        this.fireTableDataChanged();
    }

    public void fill(List<Supplier> list) {
        if (list != null) {
            data = list;
            this.fireTableDataChanged();
        } else {
            clear();
        }
    }

    public List<Supplier> getSupplierList() {
        return data;
    }

    public SupplierTableModel() {
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("NAME"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("NIF"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("CATEGORY"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WEB"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PAYMENTMETHOD"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SHIPPINGMETHOD"));
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public Supplier getRow(int row) {
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
                if (data.get(rowIndex).getNif() != null) {
                    return data.get(rowIndex).getNif().getNumber();
                } else {
                    return null;
                }
            case 3:
                return data.get(rowIndex).getCategory();
            case 4:
                return data.get(rowIndex).getWeb();
            case 5:
                return data.get(rowIndex).getPaymentMethod();
            case 6:
                return data.get(rowIndex).getShippingMethod();
            case 7:
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

    public void removeSupplier(Supplier ins) {
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