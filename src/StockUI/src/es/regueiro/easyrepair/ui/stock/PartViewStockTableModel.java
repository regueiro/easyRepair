package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.model.stock.Stock;
import es.regueiro.easyrepair.model.stock.Warehouse;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

public class PartViewStockTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
    private List<Stock> data = new ArrayList<Stock>();

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    public void clear() {
        data = new ArrayList<Stock>();
        this.fireTableDataChanged();
    }
    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }

    public void addStock(Stock st) {
        data.add(st);
        this.fireTableDataChanged();
    }

    public void fill(List<Stock> list) {
        data = new ArrayList<Stock>();
        if (list != null) {
            for (Stock s : list) {
                data.add(s);
            }
        }
        this.fireTableDataChanged();
    }

    public PartViewStockTableModel() {
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("UNITS"));
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public Stock getRow(int row) {
        return data.get(row);
    }

    public List<Stock> getStockList() {
        return data;
    }

    @Override
    public int getRowCount() {
        if (data != null) {
            return data.size();
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
        switch (columnIndex) {
            case 0:
                return String.format("%011d", data.get(rowIndex).getWarehouse().getId());
            case 1:
                return data.get(rowIndex).getWarehouse().getName();
            case 2:
                return data.get(rowIndex).getUnits() + "";
            case 3:
                if (data.get(rowIndex).getWarehouse().getEnabled()) {
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

    public void removePart(Stock ins) {
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
