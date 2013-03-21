package es.regueiro.easyrepair.ui.client;

import es.regueiro.easyrepair.model.client.InsuranceCompany;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class InsuranceCompanyTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
    private List<InsuranceCompany> data = new ArrayList<InsuranceCompany>();

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }

    public void clear() {
        data = new ArrayList<InsuranceCompany>();
        this.fireTableDataChanged();
    }

    public void fill(List<InsuranceCompany> list) {
        if (list != null) {
            data = list;
            this.fireTableDataChanged();
        } else {
            clear();
        }
    }

    public InsuranceCompanyTableModel() {
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("NAME"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("NIF"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("WEB"));
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public InsuranceCompany getRow(int row) {
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

    public List<InsuranceCompany> getInsuranceCompanyList() {
        return data;
    }

    @Override
    public String getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.format("%08d", data.get(rowIndex).getId());
            case 1:
                return data.get(rowIndex).getName();
            case 2:
                if (data.get(rowIndex).getNif() != null) {
                    return data.get(rowIndex).getNif().getNumber();
                } else {
                    return null;
                }
            case 3:
                return data.get(rowIndex).getWeb();
            case 4:
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

    public void removeInsuranceCompany(InsuranceCompany ins) {
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