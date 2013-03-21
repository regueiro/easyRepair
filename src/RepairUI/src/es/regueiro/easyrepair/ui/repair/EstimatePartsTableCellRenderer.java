package es.regueiro.easyrepair.ui.repair;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Santi
 */
public class EstimatePartsTableCellRenderer extends DefaultTableCellRenderer {
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component comp = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            int rowN = table.convertRowIndexToModel(row);
            String s = table.getModel().getValueAt(rowN, 6).toString();
            
            if (s.equalsIgnoreCase("yes")) {
                comp.setForeground(Color.LIGHT_GRAY);
            } else {
                comp.setForeground(null);
            }
            
            if (table.isCellEditable(row, column)) {
                comp.setBackground(Color.YELLOW);
            }
            
            return (comp);
            
        }
    }
