package es.regueiro.easyrepair.ui.client;

import es.regueiro.easyrepair.model.client.Client;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ClientTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
    private List<Client> data = new ArrayList<Client>();

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }

    public void clear() {
        data = new ArrayList<Client>();
        this.fireTableDataChanged();
    }
    
    public void fill(List<Client> list) {
        if (list != null) {
            data = list;
            this.fireTableDataChanged();
        } else {
            clear();
        }
    }

    public List<Client> getClientList() {
        return data;
    }
    public ClientTableModel() {
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CLIENT_ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("NAME"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SURNAME"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("NIF"));
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public Client getRow(int row) {
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
                return data.get(rowIndex).getClientId();
            case 2:
                return data.get(rowIndex).getName();
            case 3:
                return data.get(rowIndex).getSurname();
            case 4:
                if (data.get(rowIndex).getNif() != null) {
                    return data.get(rowIndex).getNif().getNumber();
                } else {
                    return null;
                }
            case 5:
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

    public void removeClient(Client ins) {
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