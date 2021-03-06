package es.regueiro.easyrepair.ui.repair;

import es.regueiro.easyrepair.model.repair.Labour;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.openide.util.NbPreferences;

public class LabourTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
    private List<Labour> data = new ArrayList<Labour>();

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }
    
    public void clear() {
        data = new ArrayList<Labour>();
        this.fireTableDataChanged();
    }

    public void fill(List<Labour> list) {
        if (list != null) {
            data = list;
            this.fireTableDataChanged();
        } else {
            clear();
        }
    }

    public List<Labour> getLabourList() {
        return data;
    }

    public LabourTableModel() {
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("NAME"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("DESCRIPTION"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PRICEWITHOUTIVA"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PRICEWITHIVA"));
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public Labour getRow(int row) {
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

    private String formatWithIVA(BigDecimal price) {
        BigDecimal iva = new BigDecimal(NbPreferences.root().get("iva", "21")).divide(new BigDecimal("100"));

        BigDecimal total = price.add(price.multiply(iva));

        return formatCurrency(total.setScale(2, RoundingMode.HALF_UP).toPlainString());
    }

    private String formatWithoutIVA(BigDecimal price) {
        return formatCurrency(price.setScale(2, RoundingMode.HALF_UP).toPlainString());
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
            case 3:
                if (data.get(rowIndex).getPrice() != null) {
                    return formatWithoutIVA(data.get(rowIndex).getPrice());
                } else {
                    return null;
                }
            case 4:
                if (data.get(rowIndex).getPrice() != null) {
                    return formatWithIVA(data.get(rowIndex).getPrice());
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

    public void removeLabour(Labour ins) {
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