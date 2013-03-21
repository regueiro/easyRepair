package es.regueiro.easyrepair.ui.employee;

import es.regueiro.easyrepair.api.employee.controller.EmployeeController;
import es.regueiro.easyrepair.login.SecurityManager;
import es.regueiro.easyrepair.model.employee.Employee;
import es.regueiro.easyrepair.model.user.Privilege;
import es.regueiro.easyrepair.reports.ReportPrinter;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.ActionMap;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.DefaultEditorKit;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.StatusDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.employee//EmployeeBrowser//EN",
autostore = false)
@TopComponent.Description(preferredID = "EmployeeBrowserTopComponent",
iconBase = "es/regueiro/easyrepair/ui/employee/icons/mechanic.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 24, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.employee.EmployeeBrowserTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Empleados", position = 0),
    @ActionReference(path = "Toolbars/Empleados", position = 0)
})
@TopComponent.OpenActionRegistration(displayName = "#CTL_EmployeeBrowserAction",
preferredID = "EmployeeBrowserTopComponent")
@Messages({
    "CTL_EmployeeBrowserAction=Buscador de empleados",
    "CTL_EmployeeBrowserTopComponent=Buscador de empleados",
    "HINT_EmployeeBrowserTopComponent=Esta es una ventana del buscador de empleados"
})
public final class EmployeeBrowserTopComponent extends TopComponent implements TableModelListener, ListSelectionListener {

    private EmployeeTableModel model = new EmployeeTableModel();
    private InstanceContent content = new InstanceContent();
    private EmployeeController controller = Lookup.getDefault().lookup(EmployeeController.class);
    private Employee employee;
    private EmployeeLookup lookup = EmployeeLookup.getDefault();

    public EmployeeBrowserTopComponent() {
        initComponents();
        setName(Bundle.CTL_EmployeeBrowserTopComponent());
        setToolTipText(Bundle.HINT_EmployeeBrowserTopComponent());
        this.setFocusable(true);

        String[] inCBStrings = {java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("NAME"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("SURNAME"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EMPLOYEE_ID"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("NIF"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("NSS"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("OCCUPATION"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EMAIL_LABEL"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EMAIL_ADDRESS"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("PHONE_LABEL"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("PHONE_NUMBER"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("ADDRESS_LABEL"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("ADDRESS_STREET"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("ADDRESS_CITY"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("ADDRESS_POSTAL_CODE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("ADDRESS_PROVINCE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("ADDRESS_COUNTRY")};


        for (String s : inCBStrings) {
            inCombobox.addItem(s);
        }

        // Associate the table model
        employeeTable.setModel(model);

        for (int i = 0; i < employeeTable.getColumnCount(); i++) {
            employeeTable.getColumnModel().getColumn(i).setCellRenderer(new EmployeeTableCellRenderer());
        }


        // Listen for intro on the search bar
        searchEntryField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                doSearch();
            }
        });


        // Activate Copy/Paste
        ActionMap actionMap = getActionMap();
        actionMap.put(DefaultEditorKit.copyAction, new DefaultEditorKit.CopyAction());
        actionMap.put(DefaultEditorKit.cutAction, new DefaultEditorKit.CutAction());
        actionMap.put(DefaultEditorKit.pasteAction, new DefaultEditorKit.PasteAction());
        content.add(actionMap);

        // Listen to the changes on the table
        model.addTableModelListener(this);
        employeeTable.getSelectionModel().addListSelectionListener(this);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        searchPanel = new javax.swing.JPanel();
        searchEntryPanel = new javax.swing.JPanel();
        searchLabel = new javax.swing.JLabel();
        searchEntryField = new javax.swing.JTextField();
        inLabel = new javax.swing.JLabel();
        listAllButton = new javax.swing.JButton();
        inCombobox = new javax.swing.JComboBox();
        includeDisabledCheckBox = new javax.swing.JCheckBox();
        searchButtonPanel = new javax.swing.JPanel();
        findButton = new javax.swing.JButton();
        tablePanel = new javax.swing.JScrollPane();
        employeeTable = new javax.swing.JTable();
        topToolBar = new javax.swing.JToolBar();
        detailedViewButton = new javax.swing.JButton();
        newEmployeeButton = new javax.swing.JButton();
        editEmployeeButton = new javax.swing.JButton();
        deleteEmployeeButton = new javax.swing.JButton();
        printSelectedEmployeeButton = new javax.swing.JButton();
        printEmployeeListButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        searchPanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        searchPanel.setLayout(new java.awt.GridBagLayout());

        searchEntryPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(searchLabel, org.openide.util.NbBundle.getMessage(EmployeeBrowserTopComponent.class, "EmployeeBrowserTopComponent.searchLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(searchLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(searchEntryField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(inLabel, org.openide.util.NbBundle.getMessage(EmployeeBrowserTopComponent.class, "EmployeeBrowserTopComponent.inLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(inLabel, gridBagConstraints);

        listAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/group.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(listAllButton, org.openide.util.NbBundle.getMessage(EmployeeBrowserTopComponent.class, "EmployeeBrowserTopComponent.listAllButton.text")); // NOI18N
        listAllButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        listAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listAllButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(listAllButton, gridBagConstraints);

        inCombobox.setPreferredSize(new java.awt.Dimension(79, 31));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(inCombobox, gridBagConstraints);

        includeDisabledCheckBox.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(includeDisabledCheckBox, org.openide.util.NbBundle.getMessage(EmployeeBrowserTopComponent.class, "EmployeeBrowserTopComponent.includeDisabledCheckBox.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(includeDisabledCheckBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchPanel.add(searchEntryPanel, gridBagConstraints);

        searchButtonPanel.setLayout(new java.awt.BorderLayout(5, 5));

        findButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/find32.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(findButton, org.openide.util.NbBundle.getMessage(EmployeeBrowserTopComponent.class, "EmployeeBrowserTopComponent.findButton.text")); // NOI18N
        findButton.setMargin(new java.awt.Insets(5, 15, 5, 15));
        findButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findButtonActionPerformed(evt);
            }
        });
        searchButtonPanel.add(findButton, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 10, 10);
        searchPanel.add(searchButtonPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        add(searchPanel, gridBagConstraints);

        tablePanel.setMinimumSize(new java.awt.Dimension(100, 100));

        employeeTable.setAutoCreateRowSorter(true);
        employeeTable.setDoubleBuffered(true);
        employeeTable.setFillsViewportHeight(true);
        employeeTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        employeeTable.setRowHeight(25);
        employeeTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        employeeTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablePanel.setViewportView(employeeTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 7);
        add(tablePanel, gridBagConstraints);

        topToolBar.setBorder(null);
        topToolBar.setFloatable(false);
        topToolBar.setRollover(true);

        detailedViewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/mechanic_detailed.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(detailedViewButton, org.openide.util.NbBundle.getMessage(EmployeeBrowserTopComponent.class, "EmployeeBrowserTopComponent.detailedViewButton.text")); // NOI18N
        detailedViewButton.setFocusable(false);
        detailedViewButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        detailedViewButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        detailedViewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailedViewButtonActionPerformed(evt);
            }
        });
        topToolBar.add(detailedViewButton);

        newEmployeeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(newEmployeeButton, org.openide.util.NbBundle.getMessage(EmployeeBrowserTopComponent.class, "EmployeeBrowserTopComponent.newEmployeeButton.text")); // NOI18N
        newEmployeeButton.setFocusable(false);
        newEmployeeButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        newEmployeeButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        newEmployeeButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        newEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newEmployeeButtonActionPerformed(evt);
            }
        });
        topToolBar.add(newEmployeeButton);

        editEmployeeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/mechanic_edit.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(editEmployeeButton, org.openide.util.NbBundle.getMessage(EmployeeBrowserTopComponent.class, "EmployeeBrowserTopComponent.editEmployeeButton.text")); // NOI18N
        editEmployeeButton.setEnabled(false);
        editEmployeeButton.setFocusable(false);
        editEmployeeButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        editEmployeeButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        editEmployeeButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        editEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editEmployeeButtonActionPerformed(evt);
            }
        });
        topToolBar.add(editEmployeeButton);

        deleteEmployeeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteEmployeeButton, org.openide.util.NbBundle.getMessage(EmployeeBrowserTopComponent.class, "EmployeeBrowserTopComponent.deleteEmployeeButton.text")); // NOI18N
        deleteEmployeeButton.setEnabled(false);
        deleteEmployeeButton.setFocusable(false);
        deleteEmployeeButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deleteEmployeeButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deleteEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteEmployeeButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deleteEmployeeButton);

        printSelectedEmployeeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printSelectedEmployeeButton, org.openide.util.NbBundle.getMessage(EmployeeBrowserTopComponent.class, "EmployeeBrowserTopComponent.printSelectedEmployeeButton.text")); // NOI18N
        printSelectedEmployeeButton.setEnabled(false);
        printSelectedEmployeeButton.setFocusable(false);
        printSelectedEmployeeButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printSelectedEmployeeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        printSelectedEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSelectedEmployeeButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printSelectedEmployeeButton);

        printEmployeeListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printEmployeeListButton, org.openide.util.NbBundle.getMessage(EmployeeBrowserTopComponent.class, "EmployeeBrowserTopComponent.printEmployeeListButton.text")); // NOI18N
        printEmployeeListButton.setEnabled(false);
        printEmployeeListButton.setFocusable(false);
        printEmployeeListButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printEmployeeListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printEmployeeListButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printEmployeeListButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        add(topToolBar, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void findButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findButtonActionPerformed
        doSearch();
    }//GEN-LAST:event_findButtonActionPerformed

    private void listAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listAllButtonActionPerformed
        doListAll();
    }//GEN-LAST:event_listAllButtonActionPerformed

    private void newEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newEmployeeButtonActionPerformed
        lookup.clear();
        editEmployeeButton.setEnabled(false);
        deleteEmployeeButton.setEnabled(false);
        printSelectedEmployeeButton.setEnabled(false);
        openEmployeeEditor();
    }//GEN-LAST:event_newEmployeeButtonActionPerformed
    private void editEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editEmployeeButtonActionPerformed
        openEmployeeEditor();
    }//GEN-LAST:event_editEmployeeButtonActionPerformed

    private void detailedViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailedViewButtonActionPerformed
        TopComponent window = WindowManager.getDefault().findTopComponent("EmployeeDetailedViewTopComponent");
        if (window == null) {
            window = new EmployeeDetailedViewTopComponent();
        }
        window.open();
        window.requestActive();

    }//GEN-LAST:event_detailedViewButtonActionPerformed

    private void deleteEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteEmployeeButtonActionPerformed

        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EMPLOYEE")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {

            EmployeeEditorTopComponent window = (EmployeeEditorTopComponent) WindowManager.getDefault().findTopComponent("EmployeeEditorTopComponent");
            if (window.getEmployee() != null && employee.getId().equals(window.getEmployee().getId())) {
                window.forceClose();
            }


            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EMPLOYEE")));
                    lookup.clear();
                }

                @Override
                protected Void doInBackground() {
                    model.removeRow(employeeTable.convertRowIndexToModel(employeeTable.getSelectedRow()));
                    controller.setEmployee(employee);
                    controller.deleteEmployee();
                    employee = null;
                    return null;
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_deleteEmployeeButtonActionPerformed

    private void printEmployeeListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printEmployeeListButtonActionPerformed

        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printEmployeeList(model.getEmployeeList());
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printEmployeeListButtonActionPerformed

    private void printSelectedEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSelectedEmployeeButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printEmployee(employee);
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printSelectedEmployeeButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteEmployeeButton;
    private javax.swing.JButton detailedViewButton;
    private javax.swing.JButton editEmployeeButton;
    private javax.swing.JTable employeeTable;
    private javax.swing.JButton findButton;
    private javax.swing.JComboBox inCombobox;
    private javax.swing.JLabel inLabel;
    private javax.swing.JCheckBox includeDisabledCheckBox;
    private javax.swing.JButton listAllButton;
    private javax.swing.JButton newEmployeeButton;
    private javax.swing.JButton printEmployeeListButton;
    private javax.swing.JButton printSelectedEmployeeButton;
    private javax.swing.JPanel searchButtonPanel;
    private javax.swing.JTextField searchEntryField;
    private javax.swing.JPanel searchEntryPanel;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JScrollPane tablePanel;
    private javax.swing.JToolBar topToolBar;
    // End of variables declaration//GEN-END:variables

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("es.regueiro.easyrepair.ui.employee.browser");
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        model.clear();
    }

    @Override
    public void componentActivated() {
        checkPermissions();
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public void tableChanged(TableModelEvent e) {
    }

    private void checkPermissions() {
        if (SecurityManager.getDefault().isUserLoggedIn()) {
            newEmployeeButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.EMPLOYEE_EDIT));
        } else {
            this.close();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            if (employeeTable.getSelectedRow() > -1) {
                employee = model.getRow(employeeTable.convertRowIndexToModel(employeeTable.getSelectedRow()));
                editEmployeeButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.EMPLOYEE_EDIT));
                deleteEmployeeButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.EMPLOYEE_EDIT));
                printSelectedEmployeeButton.setEnabled(true);
                lookup.setEmployee(employee);
            } else {
                editEmployeeButton.setEnabled(false);
                deleteEmployeeButton.setEnabled(false);
                printSelectedEmployeeButton.setEnabled(false);
            }
        }
    }

    private void openEmployeeEditor() {
        TopComponent window = WindowManager.getDefault().findTopComponent("EmployeeEditorTopComponent");
        if (window == null) {
            window = new EmployeeEditorTopComponent();
        } else {
            window.close();
        }
        window.open();
        window.requestActive();
    }

    private void doSearch() {

        printEmployeeListButton.setEnabled(false);
        final ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("SEARCHING"));

        final SwingWorker<List<Employee>, Void> worker = new SwingWorker<List<Employee>, Void>() {
            private int column;

            @Override
            protected void done() {
                try {
                    List<Employee> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printEmployeeListButton.setEnabled(true);
                    }

                    if (column != 1) {
                        employeeTable.getRowSorter().toggleSortOrder(1);
                    } else {
                        employeeTable.getRowSorter().toggleSortOrder(2);
                    }

                    employeeTable.getRowSorter().toggleSortOrder(column);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                p.finish();
            }

            @Override
            protected List<Employee> doInBackground() {
                int selected = inCombobox.getSelectedIndex();
                String toSearch = searchEntryField.getText();
                List<Employee> list = null;
                column = 0;
                if (!toSearch.isEmpty()) {
                    p.setDisplayName(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("SEARCHING_FOR_X"), new Object[]{toSearch}));
                }

                try {
                    switch (selected) {
                        case 0:
                            list = controller.searchByName(toSearch);
                            column = 2;
                            break;
                        case 1:
                            list = controller.searchBySurname(toSearch);
                            column = 3;
                            break;
                        case 2:
                            list = controller.searchByEmployeeId(toSearch);
                            column = 1;
                            break;
                        case 3:
                            list = controller.searchByNif(toSearch);
                            column = 4;
                            break;
                        case 4:
                            list = controller.searchByNss(toSearch);
                            column = 5;
                            break;
                        case 5:
                            list = controller.searchByOccupation(toSearch);
                            column = 6;
                            break;
                        case 6:
                            list = controller.searchByEmailLabel(toSearch);
                            column = 2;
                            break;
                        case 7:
                            list = controller.searchByEmailAddress(toSearch);
                            column = 2;
                            break;
                        case 8:
                            list = controller.searchByPhoneLabel(toSearch);
                            column = 2;
                            break;
                        case 9:
                            list = controller.searchByPhoneNumber(toSearch);
                            column = 2;
                            break;
                        case 10:
                            list = controller.searchByAddressLabel(toSearch);
                            column = 2;
                            break;
                        case 11:
                            list = controller.searchByAddressStreet(toSearch);
                            column = 2;
                            break;
                        case 12:
                            list = controller.searchByAddressCity(toSearch);
                            column = 2;
                            break;
                        case 13:
                            list = controller.searchByAddressPostalCode(toSearch);
                            column = 2;
                            break;
                        case 14:
                            list = controller.searchByAddressProvince(toSearch);
                            column = 2;
                            break;
                        case 15:
                            list = controller.searchByAddressCountry(toSearch);
                            column = 2;
                            break;
                        default:
                            //this should never happen
                            list = null;
                    }
                } catch (Exception e) {
                    p.finish();
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage() + e.getStackTrace(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                }

                if (!includeDisabledCheckBox.isSelected()) {
                    Iterator<Employee> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Employee c = iterator.next();
                        if (!c.getEnabled()) {
                            iterator.remove();
                        }
                    }
                }
                return list;
            }
        };
        p.start();
        worker.execute();
    }

    private void doListAll() {

        printEmployeeListButton.setEnabled(false);

        final ProgressHandle p = ProgressHandleFactory.createHandle(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("LISTING_ALL_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EMPLOYEES")));

        SwingWorker<List<Employee>, Void> worker = new SwingWorker<List<Employee>, Void>() {
            @Override
            protected void done() {
                try {
                    List<Employee> list = get();

                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printEmployeeListButton.setEnabled(true);
                    }
                    employeeTable.getRowSorter().toggleSortOrder(2);
                    employeeTable.getRowSorter().toggleSortOrder(1);
                    p.finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                p.finish();
            }

            @Override
            protected List<Employee> doInBackground() {
                List<Employee> list = null;
                try {
                    list = controller.listAll();
                } catch (Exception e) {
                    p.finish();
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                }

                if (!includeDisabledCheckBox.isSelected()) {
                    Iterator<Employee> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Employee c = iterator.next();
                        if (!c.getEnabled()) {
                            iterator.remove();
                        }
                    }
                }
                return list;
            }
        };

        p.start();
        worker.execute();
    }

    public void deleteEmployee(Employee emp) {
        if (employee != null && emp.getId().equals(employee.getId())) {
            employee = null;
            StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EMPLOYEE")));
            lookup.clear();

        }

        model.removeEmployee(emp);

    }

    public class EmployeeTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component comp = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            int rowN = table.convertRowIndexToModel(row);
            String s = table.getModel().getValueAt(rowN, 7).toString();

            if (s.equalsIgnoreCase("yes")) {
                comp.setForeground(Color.LIGHT_GRAY);
            } else {
                comp.setForeground(null);
            }

            return (comp);

        }
    }
}