package es.regueiro.easyrepair.ui.repair;

import es.regueiro.easyrepair.model.repair.Estimate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class EstimateTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
    private List<Estimate> data = new ArrayList<Estimate>();

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }

    public void clear() {
        data = new ArrayList<Estimate>();
        this.fireTableDataChanged();
    }
    
    public void fill(List<Estimate> list) {
        data = list;
        this.fireTableDataChanged();
    }
    
    public List<Estimate> getEstimateList() {
        return data;
    }

    public EstimateTableModel() {
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ESTIMATENUMBER"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ESTIMATEDATE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ACCEPTEDDATE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("STATUS"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("DISCOUNT"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("RESPONSIBLE"));

    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public Estimate getRow(int row) {
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
                return data.get(rowIndex).getEstimateNumber();
            case 2:
                if (data.get(rowIndex).getEstimateDate() != null) {
                    return data.get(rowIndex).getEstimateDate().toString();
                } else {
                    return null;
                }
            case 3:
                if (data.get(rowIndex).getAcceptedDate() != null) {
                    return data.get(rowIndex).getAcceptedDate().toString();
                } else {
                    return null;
                }
            case 4:
                return data.get(rowIndex).getStatus();
            case 5:
                if (data.get(rowIndex).getDiscount() != null) {
                    return data.get(rowIndex).getDiscount().toPlainString();
                } else {
                    return null;
                }
            case 6:
                if (data.get(rowIndex).getResponsible() != null) {
                    return data.get(rowIndex).getResponsible().getName();
                } else {
                    return null;
                }
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

    public void removeEstimate(Estimate ins) {
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