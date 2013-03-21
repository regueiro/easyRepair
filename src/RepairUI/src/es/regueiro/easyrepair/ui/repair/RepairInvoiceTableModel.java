package es.regueiro.easyrepair.ui.repair;

import es.regueiro.easyrepair.model.repair.RepairInvoice;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class RepairInvoiceTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
    private List<RepairInvoice> data = new ArrayList<RepairInvoice>();

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }

    public void clear() {
        data = new ArrayList<RepairInvoice>();
        this.fireTableDataChanged();
    }

    public void fill(List<RepairInvoice> list) {
        data = list;
        this.fireTableDataChanged();
    }

    public List<RepairInvoice> getRepairInvoiceList() {
        return data;
    }

    public RepairInvoiceTableModel() {
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("INVOICENUMBER"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("INVOICEDATE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ACCEPTEDDATE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ESTIMATEDPAYMENTDATE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PAYMENTDATE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PAYMENTMETHOD"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("STATUS"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("RESPONSIBLE"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PAYMENTRESPONSIBLE"));

    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public RepairInvoice getRow(int row) {
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
                return data.get(rowIndex).getPaymentResponsible();
            case 10:
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

    public void removeRepairInvoice(RepairInvoice ins) {
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