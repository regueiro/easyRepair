package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.model.stock.PartOrderInvoice;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class PartOrderInvoiceTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
    private List<PartOrderInvoice> data = new ArrayList<PartOrderInvoice>();

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }

    public void clear() {
        data = new ArrayList<PartOrderInvoice>();
        this.fireTableDataChanged();
    }

    public void fill(List<PartOrderInvoice> list) {
        if (list != null) {
            data = list;
            this.fireTableDataChanged();
        } else {
            clear();
        }
    }

    public List<PartOrderInvoice> getPartOrderInvoiceList() {
        return data;
    }

    public PartOrderInvoiceTableModel() {
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("INVOICENUMBER"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("INVOICEDATE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ACCEPTEDDATE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ESTIMATEDPAYMENTDATE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PAYMENTDATE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PAYMENTMETHOD"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("STATUS"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RESPONSIBLE"));


    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public PartOrderInvoice getRow(int row) {
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
                return data.get(rowIndex).getInvoiceNumber();
            case 2:
                if (data.get(rowIndex).getInvoiceDate() != null) {
                    return data.get(rowIndex).getInvoiceDate().toString();
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
                if (data.get(rowIndex).getEstimatedPaymentDate() != null) {
                    return data.get(rowIndex).getEstimatedPaymentDate().toString();
                } else {
                    return null;
                }
            case 5:
                if (data.get(rowIndex).getPaymentDate() != null) {
                    return data.get(rowIndex).getPaymentDate().toString();
                } else {
                    return null;
                }
            case 6:
                return data.get(rowIndex).getPaymentMethod();
            case 7:
                return data.get(rowIndex).getStatus();
            case 8:
                if (data.get(rowIndex).getResponsible() != null) {
                    return data.get(rowIndex).getResponsible().getName();
                } else {
                    return null;
                }
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

    public void removeOrderInvoice(PartOrderInvoice ins) {
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