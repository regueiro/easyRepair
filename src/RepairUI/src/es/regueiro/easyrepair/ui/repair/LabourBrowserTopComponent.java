package es.regueiro.easyrepair.ui.repair;

import es.regueiro.easyrepair.login.SecurityManager;
import es.regueiro.easyrepair.api.repair.controller.LabourController;
import es.regueiro.easyrepair.login.LoginLookup;
import es.regueiro.easyrepair.model.repair.Labour;
import es.regueiro.easyrepair.model.user.Privilege;
import es.regueiro.easyrepair.reports.ReportPrinter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.ActionMap;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.repair//LabourBrowser//EN",
autostore = false)
@TopComponent.Description(preferredID = "LabourBrowserTopComponent",
iconBase = "es/regueiro/easyrepair/ui/repair/icons/labour.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 12, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.repair.LabourBrowserTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Reparaciones", position = 3),
    @ActionReference(path = "Toolbars/Reparaciones", position = 3)
})
@TopComponent.OpenActionRegistration(displayName = "#CTL_LabourBrowserAction",
preferredID = "LabourBrowserTopComponent")
@Messages({
    "CTL_LabourBrowserAction=Buscador de mano de obra",
    "CTL_LabourBrowserTopComponent=Buscador de mano de obra",
    "HINT_LabourBrowserTopComponent=Esta es una ventana del buscador de mano de obra"
})
public final class LabourBrowserTopComponent extends TopComponent implements TableModelListener, ListSelectionListener {

    private LabourTableModel model = new LabourTableModel();
    private InstanceContent content = new InstanceContent();
    private LabourController controller = Lookup.getDefault().lookup(LabourController.class);
    private Labour labour;
    private LabourLookup lookup = LabourLookup.getDefault();

    public LabourBrowserTopComponent() {
        initComponents();
        setName(Bundle.CTL_LabourBrowserTopComponent());
        setToolTipText(Bundle.HINT_LabourBrowserTopComponent());
        this.setFocusable(true);


        String[] inCBStrings = {
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("NAME"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("DESCRIPTION"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PRICE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("MAXPRICE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("MINPRICE")};

        for (String s : inCBStrings) {
            inCombobox.addItem(s);
        }
        // Associate the table model
        labourTable.setModel(model);

        for (int i = 0; i < labourTable.getColumnCount(); i++) {
            labourTable.getColumnModel().getColumn(i).setCellRenderer(new LabourTableCellRenderer());
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
        labourTable.getSelectionModel().addListSelectionListener(this);

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
        labourTable = new javax.swing.JTable();
        topToolBar = new javax.swing.JToolBar();
        detailedViewButton = new javax.swing.JButton();
        newLabourButton = new javax.swing.JButton();
        editLabourButton = new javax.swing.JButton();
        deleteLabourButton = new javax.swing.JButton();
        printSelectedLabourButton = new javax.swing.JButton();
        printLabourListButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        searchPanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        searchPanel.setLayout(new java.awt.GridBagLayout());

        searchEntryPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(searchLabel, org.openide.util.NbBundle.getMessage(LabourBrowserTopComponent.class, "LabourBrowserTopComponent.searchLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(inLabel, org.openide.util.NbBundle.getMessage(LabourBrowserTopComponent.class, "LabourBrowserTopComponent.inLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(inLabel, gridBagConstraints);

        listAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/group.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(listAllButton, org.openide.util.NbBundle.getMessage(LabourBrowserTopComponent.class, "LabourBrowserTopComponent.listAllButton.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(includeDisabledCheckBox, org.openide.util.NbBundle.getMessage(LabourBrowserTopComponent.class, "LabourBrowserTopComponent.includeDisabledCheckBox.text")); // NOI18N
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

        findButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/find32.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(findButton, org.openide.util.NbBundle.getMessage(LabourBrowserTopComponent.class, "LabourBrowserTopComponent.findButton.text")); // NOI18N
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

        labourTable.setAutoCreateRowSorter(true);
        labourTable.setDoubleBuffered(true);
        labourTable.setFillsViewportHeight(true);
        labourTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        labourTable.setRowHeight(25);
        labourTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        labourTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablePanel.setViewportView(labourTable);

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

        detailedViewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/labour_detailed.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(detailedViewButton, org.openide.util.NbBundle.getMessage(LabourBrowserTopComponent.class, "LabourBrowserTopComponent.detailedViewButton.text")); // NOI18N
        detailedViewButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        detailedViewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailedViewButtonActionPerformed(evt);
            }
        });
        topToolBar.add(detailedViewButton);

        newLabourButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(newLabourButton, org.openide.util.NbBundle.getMessage(LabourBrowserTopComponent.class, "LabourBrowserTopComponent.newLabourButton.text")); // NOI18N
        newLabourButton.setFocusable(false);
        newLabourButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        newLabourButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        newLabourButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        newLabourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newLabourButtonActionPerformed(evt);
            }
        });
        topToolBar.add(newLabourButton);

        editLabourButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/labour_edit.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(editLabourButton, org.openide.util.NbBundle.getMessage(LabourBrowserTopComponent.class, "LabourBrowserTopComponent.editLabourutton.text")); // NOI18N
        editLabourButton.setEnabled(false);
        editLabourButton.setFocusable(false);
        editLabourButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        editLabourButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        editLabourButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        editLabourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editLabourButtonActionPerformed(evt);
            }
        });
        topToolBar.add(editLabourButton);

        deleteLabourButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteLabourButton, org.openide.util.NbBundle.getMessage(LabourBrowserTopComponent.class, "LabourBrowserTopComponent.deleteLabourButton.text")); // NOI18N
        deleteLabourButton.setEnabled(false);
        deleteLabourButton.setFocusable(false);
        deleteLabourButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deleteLabourButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deleteLabourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteLabourButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deleteLabourButton);

        printSelectedLabourButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printSelectedLabourButton, org.openide.util.NbBundle.getMessage(LabourBrowserTopComponent.class, "LabourBrowserTopComponent.printSelectedLabourButton.text")); // NOI18N
        printSelectedLabourButton.setEnabled(false);
        printSelectedLabourButton.setFocusable(false);
        printSelectedLabourButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printSelectedLabourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSelectedLabourButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printSelectedLabourButton);

        printLabourListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printLabourListButton, org.openide.util.NbBundle.getMessage(LabourBrowserTopComponent.class, "LabourBrowserTopComponent.printLabourListButton.text")); // NOI18N
        printLabourListButton.setEnabled(false);
        printLabourListButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printLabourListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printLabourListButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printLabourListButton);

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

    private void newLabourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newLabourButtonActionPerformed
        lookup.clear();
        editLabourButton.setEnabled(false);
        deleteLabourButton.setEnabled(false);
        printSelectedLabourButton.setEnabled(false);
        labourTable.clearSelection();
        openLabourEditor();
    }//GEN-LAST:event_newLabourButtonActionPerformed
    private void editLabourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editLabourButtonActionPerformed
        openLabourEditor();
    }//GEN-LAST:event_editLabourButtonActionPerformed

    private void deleteLabourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteLabourButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("LABOUR")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {

//            LabourEditorTopComponent window = (LabourEditorTopComponent) WindowManager.getDefault().findTopComponent("LabourEditorTopComponent");
//            if (window.getLabour() != null && Labour.getId().equals(window.getLabour().getId())) {
//                window.forceClose();
//            }


            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("LABOUR")));
                    lookup.clear();
                }

                @Override
                protected Void doInBackground() {
                    model.removeRow(labourTable.convertRowIndexToModel(labourTable.getSelectedRow()));
                    controller.setLabour(labour);
                    controller.deleteLabour();
                    labour = null;
                    return null;
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_deleteLabourButtonActionPerformed

    private void detailedViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailedViewButtonActionPerformed
        TopComponent window = WindowManager.getDefault().findTopComponent("LabourDetailedViewTopComponent");
        if (window == null) {
//            window = new LabourDetailedViewTopComponent();
        }
        window.open();
        window.requestActive();

    }//GEN-LAST:event_detailedViewButtonActionPerformed

    private void printLabourListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printLabourListButtonActionPerformed
        ReportPrinter printer = new ReportPrinter();
        printer.printLabourList(model.getLabourList());
//        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
//            @Override
//            protected void done() {
//            }
//
//            @Override
//            protected Void doInBackground() {
//                ReportPrinter printer = new ReportPrinter();
//                printer.printLabourList(model.getLabourList());
//                return null;
//            }
//        };
//        worker.execute();
    }//GEN-LAST:event_printLabourListButtonActionPerformed

    private void printSelectedLabourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSelectedLabourButtonActionPerformed
        ReportPrinter printer = new ReportPrinter();
        printer.printLabour(labour);

//        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
//            @Override
//            protected void done() {
//            }
//
//            @Override
//            protected Void doInBackground() {
//                ReportPrinter printer = new ReportPrinter();
//                printer.printLabour(labour);
//                return null;
//            }
//        };
//        worker.execute();
    }//GEN-LAST:event_printSelectedLabourButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteLabourButton;
    private javax.swing.JButton detailedViewButton;
    private javax.swing.JButton editLabourButton;
    private javax.swing.JButton findButton;
    private javax.swing.JComboBox inCombobox;
    private javax.swing.JLabel inLabel;
    private javax.swing.JCheckBox includeDisabledCheckBox;
    private javax.swing.JTable labourTable;
    private javax.swing.JButton listAllButton;
    private javax.swing.JButton newLabourButton;
    private javax.swing.JButton printLabourListButton;
    private javax.swing.JButton printSelectedLabourButton;
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
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return new HelpCtx("es.regueiro.easyrepair.ui.repair.browser");
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
            newLabourButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.LABOUR_EDIT));
        } else {
            this.close();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            if (labourTable.getSelectedRow() > -1) {
                labour = model.getRow(labourTable.convertRowIndexToModel(labourTable.getSelectedRow()));
                editLabourButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.LABOUR_EDIT));
                deleteLabourButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.LABOUR_EDIT));
                printSelectedLabourButton.setEnabled(true);
                lookup.setLabour(labour);
            } else {
                editLabourButton.setEnabled(false);
                deleteLabourButton.setEnabled(false);
                printSelectedLabourButton.setEnabled(false);
            }
        }
    }

    private void openLabourEditor() {
        TopComponent window = WindowManager.getDefault().findTopComponent("LabourEditorTopComponent");
        if (window == null) {
//            window = new LabourEditorTopComponent();
        } else {
            window.close();
        }
        window.open();
        window.requestActive();
    }

    private void doSearch() {

        printLabourListButton.setEnabled(false);

        final SwingWorker<List<Labour>, Void> worker = new SwingWorker<List<Labour>, Void>() {
            private int column;
            ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SEARCHING"));

            @Override
            protected void done() {
                try {
                    List<Labour> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printLabourListButton.setEnabled(true);
                    }

                    if (column != 1) {
                        labourTable.getRowSorter().toggleSortOrder(1);
                    } else {
                        labourTable.getRowSorter().toggleSortOrder(2);
                    }

                    labourTable.getRowSorter().toggleSortOrder(column);
                    p.finish();
                } catch (InterruptedException ignore) {
                    System.out.println(ignore.getCause());
                } catch (ExecutionException ignore) {
                    System.out.println(ignore.getCause());
                }
            }

            @Override
            protected List<Labour> doInBackground() {
                int selected = inCombobox.getSelectedIndex();
                String toSearch = searchEntryField.getText();
                List<Labour> list = null;
                column = 0;
                if (!toSearch.isEmpty()) {
                    p.setDisplayName(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SEARCHING_FOR_X"), new Object[]{toSearch}));
                }
                p.start();
                try {
                    switch (selected) {
                        case 0:
                            list = controller.searchByName(toSearch);
                            column = 1;
                            break;
                        case 1:
                            list = controller.searchByDescription(toSearch);
                            column = 3;
                            break;
                        case 2:
                            list = controller.searchByPrice(toSearch);
                            column = 2;
                            break;
                        case 3:
                            list = controller.searchByMaxPrice(toSearch);
                            column = 2;
                            break;
                        case 4:
                            list = controller.searchByMinPrice(toSearch);
                            column = 2;
                            break;
                        default:
                            //this should never happen
                            list = null;
                    }
                } catch (Exception e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ERROR_SEARCH"), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    p.finish();
                }
                if (!includeDisabledCheckBox.isSelected()) {
                    Iterator<Labour> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Labour l = iterator.next();
                        if (!l.getEnabled()) {
                            iterator.remove();
                        }
                    }
                }
                return list;
            }
        };
        worker.execute();
    }

    private void doListAll() {

        printLabourListButton.setEnabled(false);

        final ProgressHandle p = ProgressHandleFactory.createHandle(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("LISTING_ALL_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("LABOURS")));

        SwingWorker<List<Labour>, Void> worker = new SwingWorker<List<Labour>, Void>() {
            @Override
            protected void done() {
                try {
                    List<Labour> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printLabourListButton.setEnabled(true);
                    }


                    labourTable.getRowSorter().toggleSortOrder(2);
                    labourTable.getRowSorter().toggleSortOrder(1);
                    p.finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected List<Labour> doInBackground() {
                List<Labour> list = null;
                try {
                    list = controller.listAll();
                } catch (Exception e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    p.finish();
                }

                if (!includeDisabledCheckBox.isSelected()) {
                    Iterator<Labour> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Labour l = iterator.next();
                        if (!l.getEnabled()) {
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

    public void deleteLabour(Labour lab) {
        if (labour != null && lab.getId().equals(labour.getId())) {
            labour = null;
            StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("LABOUR")));
            lookup.clear();

        }

        model.removeLabour(lab);

    }
}
