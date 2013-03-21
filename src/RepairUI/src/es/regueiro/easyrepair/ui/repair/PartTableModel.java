package es.regueiro.easyrepair.ui.repair;

import es.regueiro.easyrepair.model.repair.Labour;
import es.regueiro.easyrepair.model.stock.Part;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.openide.util.NbPreferences;

public class PartTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
    private List<Part> data = new ArrayList<Part>();

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }

    public void clear() {
        data = new ArrayList<Part>();
        this.fireTableDataChanged();
    }

    public void fill(List<Part> list) {
        if (list != null) {
            data = list;
            this.fireTableDataChanged();
        } else {
            clear();
        }
    }

    public PartTableModel() {
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("MAKE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("MODEL"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("CATEGORY"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PRICE"));
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public Part getRow(int row) {
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
                return data.get(rowIndex).getMake();
            case 2:
                return data.get(rowIndex).getModel();
            case 3:
                return data.get(rowIndex).getCategory();
            case 4:
                return formatCurrency(data.get(rowIndex).getPrice().setScale(2, RoundingMode.HALF_UP).toPlainString());
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

    public void removePart(Part ins) {
        int row = data.indexOf(ins);
        data.remove(ins);
        fireTableRowsDeleted(row, row);
    }
    // Adds the given column to the right hand side of the model

    public void addColumn(String name) {
        columns.add(name);
        fireTableStructureChanged();

    }

    private String formatCurrency(String amount) {
        String text = "";
        if (NbPreferences.root().getBoolean("inFront", false)) {
            text += NbPreferences.root().get("currency", " €");
        }
        text += amount;
        if (NbPreferences.root().getBoolean("inFront", false)) {
        } else {
            text += NbPreferences.root().get("currency", " €");
        }
        return text;
    }
}