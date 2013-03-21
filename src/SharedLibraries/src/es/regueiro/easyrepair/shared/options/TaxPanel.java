/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.shared.options;

import java.math.BigDecimal;
import org.openide.util.NbPreferences;

final class TaxPanel extends javax.swing.JPanel {

    private final TaxOptionsPanelController controller;

    TaxPanel(TaxOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        
        // TODO listen to changes in form fields and call controller.changed()
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel = new javax.swing.JPanel();
        ivaLabel = new javax.swing.JLabel();
        ivaTextField = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        jPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(ivaLabel, org.openide.util.NbBundle.getMessage(TaxPanel.class, "TaxPanel.ivaLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel.add(ivaLabel, gridBagConstraints);

        ivaTextField.setText(org.openide.util.NbBundle.getMessage(TaxPanel.class, "TaxPanel.ivaTextField.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel.add(ivaTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        add(jPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    void load() {
        // TODO read settings and initialize GUI
        // Example:        
        // someCheckBox.setSelected(Preferences.userNodeForPackage(TaxPanel.class).getBoolean("someFlag", false));
        // or for org.openide.util with API spec. version >= 7.4:
        // someCheckBox.setSelected(NbPreferences.forModule(TaxPanel.class).getBoolean("someFlag", false));
        // or:
        // someTextField.setText(SomeSystemOption.getDefault().getSomeStringProperty());
        ivaTextField.setText(NbPreferences.root().get("iva", "21"));
        
    }

    void store() {
        // TODO store modified settings
        // Example:
        // Preferences.userNodeForPackage(TaxPanel.class).putBoolean("someFlag", someCheckBox.isSelected());
        // or for org.openide.util with API spec. version >= 7.4:
        // NbPreferences.forModule(TaxPanel.class)..getBoolean("someFlag", false));("someFlag", someCheckBox.isSelected());
        // or:
        // SomeSystemOption.getDefault().setSomeStringProperty(someTextField.getText());
        NbPreferences.root().put("iva", ivaTextField.getText());
    }

    boolean valid() {
        // TODO check why it doesnt work
        try {
            BigDecimal iva = new BigDecimal(ivaTextField.getText());
            
            if (iva.compareTo(new BigDecimal("0")) < 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ivaLabel;
    private javax.swing.JTextField ivaTextField;
    private javax.swing.JPanel jPanel;
    // End of variables declaration//GEN-END:variables
}
