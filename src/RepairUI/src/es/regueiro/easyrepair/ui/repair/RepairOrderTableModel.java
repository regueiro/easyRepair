package es.regueiro.easyrepair.ui.repair;

import es.regueiro.easyrepair.model.repair.RepairOrder;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class RepairOrderTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
    private List<RepairOrder> data = new ArrayList<RepairOrder>();

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }

    public void clear() {
        data = new ArrayList<RepairOrder>();
        this.fireTableDataChanged();
    }

    public void fill(List<RepairOrder> list) {
        if (list != null) {
            data = list;
            this.fireTableDataChanged();
        } else {
            clear();
        }
    }

    public List<RepairOrder> getRepairOrderList() {
        return data;
    }

    public RepairOrderTableModel() {
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ORDERNUMBER"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ORDERDATE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ESTIMATEDDATE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("FINISHDATE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("DELIVERYDATE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("STATUS"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("GASTANKLEVEL"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("KILOMETRES"));
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public RepairOrder getRow(int row) {
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
                return data.get(rowIndex).getOrderNumber();
            case 2:
                if (data.get(rowIndex).getOrderDate() != null) {
                    return data.get(rowIndex).getOrderDate().toString();
                } else {
                    return null;
                }
            case 3:
                if (data.get(rowIndex).getEstimatedDate() != null) {
                    return data.get(rowIndex).getEstimatedDate().toString();
                } else {
                    return null;
                }
            case 4:
                if (data.get(rowIndex).getFinishDate() != null) {
                    return data.get(rowIndex).getFinishDate().toString();
                } else {
                    return null;
                }
            case 5:
                if (data.get(rowIndex).getDeliveryDate() != null) {
                    return data.get(rowIndex).getDeliveryDate().toString();
                } else {
                    return null;
                }
            case 6:
                return data.get(rowIndex).getStatus();
            case 7:
                return data.get(rowIndex).getGasTankLevel();
            case 8:
                return data.get(rowIndex).getKilometres();
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

    public void removeRepairOrder(RepairOrder ins) {
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