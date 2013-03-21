package es.regueiro.easyrepair.ui.user;

import es.regueiro.easyrepair.model.user.Role;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class RoleTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
    private List<Role> data = new ArrayList<Role>();

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }

    public void clear() {
        data = new ArrayList<Role>();
        this.fireTableDataChanged();
    }

    public void fill(List<Role> list) {
        if (list != null) {
            data = list;
            this.fireTableDataChanged();
        } else {
            clear();
        }
    }

    public RoleTableModel() {
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("NAME"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("DESCRIPTION"));
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public Role getRow(int row) {
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
                return data.get(rowIndex).getDescription();
            default:
                return "";
        }
    }

    public void removeRow(int row) {
        data.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void removeRole(Role ins) {
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