package es.regueiro.easyrepair.ui.repair;

import es.regueiro.easyrepair.model.stock.PartLine;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbPreferences;

public class EstimatePartsTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
    private List<PartLine> data = new ArrayList<PartLine>();

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }

    public void clear() {
        data = new ArrayList<PartLine>();
        this.fireTableDataChanged();
    }
    
    public void addPartLine(PartLine st) {
        data.add(st);
        this.fireTableDataChanged();
    }

    public void fill(List<PartLine> list) {
        data = new ArrayList<PartLine>();
        if (list != null) {
            for (PartLine s : list) {
                data.add(s);
            }
        }
        this.fireTableDataChanged();
    }

    public EstimatePartsTableModel() {
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PARTID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("MAKE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("MODEL"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PRICE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("QUANTITY"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("DISCOUNT"));

    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 4 || column == 5) {
            return true;
        } else {
            return false;
        }
    }

    public PartLine getRow(int row) {
        return data.get(row);
    }

    public List<PartLine> getPartLineList() {
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
                return String.format("%011d", data.get(rowIndex).getPart().getId());
            case 1:
                return data.get(rowIndex).getPart().getMake();
            case 2:
                return data.get(rowIndex).getPart().getModel();
            case 3:
                return formatCurrency(data.get(rowIndex).getPart().getPrice().setScale(2, RoundingMode.HALF_UP).toPlainString());
            case 4:
                return data.get(rowIndex).getQuantity() + "";
            case 5:
                return data.get(rowIndex).getDiscount().toPlainString();
            case 6:
                if (data.get(rowIndex).getPart().getEnabled()) {
                    return "no";
                } else {
                    return "yes";
                }
            default:
                return "";
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        String stringValue = (String) value;
        try {
            switch (columnIndex) {
                case 4:
                    data.get(rowIndex).setQuantity(Integer.parseInt(stringValue));
                    break;
                case 5:
                    data.get(rowIndex).setDiscount(stringValue);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("X_INVALID"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("VALUE")), NotifyDescriptor.WARNING_MESSAGE);
            Object retval = DialogDisplayer.getDefault().notify(d);
        }
        fireTableDataChanged();
    }

    public void removeRow(int row) {
        data.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void removePart(PartLine ins) {
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
