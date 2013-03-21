package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.model.stock.PartOrder;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.openide.util.NbPreferences;

public class PartOrderTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
    private List<PartOrder> data = new ArrayList<PartOrder>();

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }

    public void clear() {
        data = new ArrayList<PartOrder>();
        this.fireTableDataChanged();
    }
    
    public void fill(List<PartOrder> list) {
        if (list != null) {
            data = list;
            this.fireTableDataChanged();
        } else {
            clear();
        }
    }

    public List<PartOrder> getPartOrderList() {
        return data;
    }

    public PartOrderTableModel() {
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ORDERNUMBER"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ORDERDATE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ESTIMATEDDATE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RECEIPTDATE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SHIPPINGCOSTS"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("OTHERCOSTS"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DISCOUNT"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("STATUS"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIER"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RESPONSIBLE"));


    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public PartOrder getRow(int row) {
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
                if (data.get(rowIndex).getReceiptDate() != null) {
                    return data.get(rowIndex).getReceiptDate().toString();
                } else {
                    return null;
                }
            case 5:
                if (data.get(rowIndex).getShippingWarehouse() != null) {
                    return data.get(rowIndex).getShippingWarehouse().getName();
                } else {
                    return null;
                }
            case 6:
                if (data.get(rowIndex).getShippingCosts() != null) {
                    return formatWithoutIVA(data.get(rowIndex).getShippingCosts());
                } else {
                    return null;
                }
            case 7:
                if (data.get(rowIndex).getOtherCosts() != null) {
                    return formatWithoutIVA(data.get(rowIndex).getOtherCosts());
                } else {
                    return null;
                }
            case 8:
                if (data.get(rowIndex).getDiscount() != null) {
                    return data.get(rowIndex).getDiscount().toPlainString() + "%";
                } else {
                    return null;
                }
            case 9:
                return data.get(rowIndex).getStatus();
            case 10:
                if (data.get(rowIndex).getSupplier() != null) {
                    return data.get(rowIndex).getSupplier().getName();
                } else {
                    return null;
                }
            case 11:
                if (data.get(rowIndex).getResponsible() != null) {
                    return data.get(rowIndex).getResponsible().getName();
                } else {
                    return null;
                }
            case 12:
                if (data.get(rowIndex).getEnabled()) {
                    return "no";
                } else {
                    return "yes";
                }
            default:
                return "";
        }
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

    public void removeRow(int row) {
        data.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void removePartOrder(PartOrder ins) {
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