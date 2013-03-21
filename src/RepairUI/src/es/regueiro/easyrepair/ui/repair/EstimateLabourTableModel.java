package es.regueiro.easyrepair.ui.repair;

import es.regueiro.easyrepair.model.repair.LabourLine;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbPreferences;

public class EstimateLabourTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
    private List<LabourLine> data = new ArrayList<LabourLine>();

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    public void addLabourLine(LabourLine lab) {
        data.add(lab);
        this.fireTableDataChanged();
    }

    public void clear() {
        data = new ArrayList<LabourLine>();
        this.fireTableDataChanged();
    }
    
    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }

    public void fill(List<LabourLine> list) {
        data = new ArrayList<LabourLine>();
        if (list != null) {
            for (LabourLine s : list) {
                data.add(s);
            }
        }
        this.fireTableDataChanged();
    }

    public EstimateLabourTableModel() {
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("NAME"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PRICE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("HOURS"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("DISCOUNT"));
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 3 || column == 4) {
            return true;
        } else {
            return false;
        }
    }

    public List<LabourLine> getLabourLineList() {
        return data;
    }

    public LabourLine getRow(int row) {
        return data.get(row);
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
                return String.format("%011d", data.get(rowIndex).getLabour().getId());
            case 1:
                return data.get(rowIndex).getLabour().getName();
            case 2:
                return formatCurrency(data.get(rowIndex).getLabour().getPrice().setScale(2, RoundingMode.HALF_UP).toPlainString());
            case 3:
                return data.get(rowIndex).getHours().toPlainString();
            case 4:
                return data.get(rowIndex).getDiscount().toPlainString();
            case 5:
                if (data.get(rowIndex).getLabour().getEnabled()) {
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
                case 3:
                    data.get(rowIndex).setHours(stringValue);
                    break;
                case 4:
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

    public void removeLabour(LabourLine ins) {
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