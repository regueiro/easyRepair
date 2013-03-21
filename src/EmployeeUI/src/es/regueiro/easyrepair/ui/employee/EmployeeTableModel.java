package es.regueiro.easyrepair.ui.employee;

import es.regueiro.easyrepair.model.employee.Employee;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class EmployeeTableModel extends AbstractTableModel {

    private List<String> columns = new ArrayList<String>();
    private List<Employee> data = new ArrayList<Employee>();

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    @Override
    public String getColumnName(int col) {
        return columns.get(col);
    }

    public void clear() {
        data = new ArrayList<Employee>();
        this.fireTableDataChanged();
    }

    public void fill(List<Employee> list) {
        if (list != null) {
            data = list;
            this.fireTableDataChanged();
        } else {
            clear();
        }
    }

    public List<Employee> getEmployeeList() {
        return data;
    }

    public EmployeeTableModel() {
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EMPLOYEE_ID"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("NAME"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("SURNAME"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("NIF"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("NSS"));
        columns.add(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("OCCUPATION"));
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public Employee getRow(int row) {
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
                return String.format("%08d", data.get(rowIndex).getId());
            case 1:
                return data.get(rowIndex).getEmployeeId();
            case 2:
                return data.get(rowIndex).getName();
            case 3:
                return data.get(rowIndex).getSurname();
            case 4:
                if (data.get(rowIndex).getNif() != null) {
                    return data.get(rowIndex).getNif().getNumber();
                } else {
                    return null;
                }
            case 5:
                if (data.get(rowIndex).getNss() != null) {
                    return data.get(rowIndex).getNss().getNumber();
                } else {
                    return null;
                }
            case 6:
                return data.get(rowIndex).getOccupation();
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

    public void removeEmployee(Employee emp) {
        int row = data.indexOf(emp);
        data.remove(emp);
        fireTableRowsDeleted(row, row);
    }

    public void addColumn(String name) {
        columns.add(name);
        fireTableStructureChanged();

    }
}