package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.login.SecurityManager;
import es.regueiro.easyrepair.api.stock.controller.WarehouseController;
import es.regueiro.easyrepair.login.LoginLookup;
import es.regueiro.easyrepair.model.stock.Warehouse;
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
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.stock//WarehouseBrowser//EN",
autostore = false)
@TopComponent.Description(preferredID = "WarehouseBrowserTopComponent",
iconBase = "es/regueiro/easyrepair/ui/stock/icons/factory.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 22, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.stock.WarehouseBrowserTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Piezas", position = 4),
    @ActionReference(path = "Toolbars/Piezas", position = 4)
})
@TopComponent.OpenActionRegistration(displayName = "#CTL_WarehouseBrowserAction",
preferredID = "WarehouseBrowserTopComponent")
@Messages({
    "CTL_WarehouseBrowserAction=Buscador de almacenes",
    "CTL_WarehouseBrowserTopComponent=Buscador de almacenes",
    "HINT_WarehouseBrowserTopComponent=Esta es una ventana del buscador de almacenes"
})
public final class WarehouseBrowserTopComponent extends TopComponent implements TableModelListener, ListSelectionListener {

    private WarehouseTableModel model = new WarehouseTableModel();
    private InstanceContent content = new InstanceContent();
    private WarehouseController controller = Lookup.getDefault().lookup(WarehouseController.class);
    private Warehouse warehouse;
    private WarehouseLookup lookup = WarehouseLookup.getDefault();

    public WarehouseBrowserTopComponent() {
        initComponents();
        setName(Bundle.CTL_WarehouseBrowserTopComponent());
        setToolTipText(Bundle.HINT_WarehouseBrowserTopComponent());
        this.setFocusable(true);


        String[] inCBStrings = {java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("NAME"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("EMAIL_ADDRESS"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PHONE_NUMBER"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS_STREET"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS_CITY"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS_POSTAL_CODE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS_PROVINCE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS_COUNTRY")};


        for (String s : inCBStrings) {
            inCombobox.addItem(s);
        }
        // Associate the table model
        warehouseTable.setModel(model);

        for (int i = 0; i < warehouseTable.getColumnCount(); i++) {
            warehouseTable.getColumnModel().getColumn(i).setCellRenderer(new WarehouseTableCellRenderer());
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
        warehouseTable.getSelectionModel().addListSelectionListener(this);

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
        warehouseTable = new javax.swing.JTable();
        topToolBar = new javax.swing.JToolBar();
        detailedViewButton = new javax.swing.JButton();
        newWarehouseButton = new javax.swing.JButton();
        editWarehouseutton = new javax.swing.JButton();
        deleteWarehouseButton = new javax.swing.JButton();
        printSelectedWarehouseButton = new javax.swing.JButton();
        printWarehouseListButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        searchPanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        searchPanel.setLayout(new java.awt.GridBagLayout());

        searchEntryPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(searchLabel, org.openide.util.NbBundle.getMessage(WarehouseBrowserTopComponent.class, "WarehouseBrowserTopComponent.searchLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(inLabel, org.openide.util.NbBundle.getMessage(WarehouseBrowserTopComponent.class, "WarehouseBrowserTopComponent.inLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(inLabel, gridBagConstraints);

        listAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/group.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(listAllButton, org.openide.util.NbBundle.getMessage(WarehouseBrowserTopComponent.class, "WarehouseBrowserTopComponent.listAllButton.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(includeDisabledCheckBox, org.openide.util.NbBundle.getMessage(WarehouseBrowserTopComponent.class, "WarehouseBrowserTopComponent.includeDisabledCheckBox.text")); // NOI18N
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

        findButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/find32.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(findButton, org.openide.util.NbBundle.getMessage(WarehouseBrowserTopComponent.class, "WarehouseBrowserTopComponent.findButton.text")); // NOI18N
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

        warehouseTable.setAutoCreateRowSorter(true);
        warehouseTable.setDoubleBuffered(true);
        warehouseTable.setFillsViewportHeight(true);
        warehouseTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        warehouseTable.setRowHeight(25);
        warehouseTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        warehouseTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablePanel.setViewportView(warehouseTable);

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

        detailedViewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/factory_detailed.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(detailedViewButton, org.openide.util.NbBundle.getMessage(WarehouseBrowserTopComponent.class, "WarehouseBrowserTopComponent.detailedViewButton.text")); // NOI18N
        detailedViewButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        detailedViewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailedViewButtonActionPerformed(evt);
            }
        });
        topToolBar.add(detailedViewButton);

        newWarehouseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(newWarehouseButton, org.openide.util.NbBundle.getMessage(WarehouseBrowserTopComponent.class, "WarehouseBrowserTopComponent.newWarehouseButton.text")); // NOI18N
        newWarehouseButton.setFocusable(false);
        newWarehouseButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        newWarehouseButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        newWarehouseButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        newWarehouseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newWarehouseButtonActionPerformed(evt);
            }
        });
        topToolBar.add(newWarehouseButton);

        editWarehouseutton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/factory_edit.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(editWarehouseutton, org.openide.util.NbBundle.getMessage(WarehouseBrowserTopComponent.class, "WarehouseBrowserTopComponent.editWarehouseutton.text")); // NOI18N
        editWarehouseutton.setEnabled(false);
        editWarehouseutton.setFocusable(false);
        editWarehouseutton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        editWarehouseutton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        editWarehouseutton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        editWarehouseutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editWarehouseuttonActionPerformed(evt);
            }
        });
        topToolBar.add(editWarehouseutton);

        deleteWarehouseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteWarehouseButton, org.openide.util.NbBundle.getMessage(WarehouseBrowserTopComponent.class, "WarehouseBrowserTopComponent.deleteWarehouseButton.text")); // NOI18N
        deleteWarehouseButton.setEnabled(false);
        deleteWarehouseButton.setFocusable(false);
        deleteWarehouseButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deleteWarehouseButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deleteWarehouseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteWarehouseButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deleteWarehouseButton);

        printSelectedWarehouseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printSelectedWarehouseButton, org.openide.util.NbBundle.getMessage(WarehouseBrowserTopComponent.class, "WarehouseBrowserTopComponent.printSelectedWarehouseButton.text")); // NOI18N
        printSelectedWarehouseButton.setEnabled(false);
        printSelectedWarehouseButton.setFocusable(false);
        printSelectedWarehouseButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printSelectedWarehouseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSelectedWarehouseButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printSelectedWarehouseButton);

        printWarehouseListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printWarehouseListButton, org.openide.util.NbBundle.getMessage(WarehouseBrowserTopComponent.class, "WarehouseBrowserTopComponent.printWarehouseListButton.text")); // NOI18N
        printWarehouseListButton.setEnabled(false);
        printWarehouseListButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printWarehouseListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printWarehouseListButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printWarehouseListButton);

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

    private void newWarehouseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newWarehouseButtonActionPerformed
        lookup.clear();
        editWarehouseutton.setEnabled(false);
        deleteWarehouseButton.setEnabled(false);
        printSelectedWarehouseButton.setEnabled(false);
        warehouseTable.clearSelection();
        openWarehouseEditor();
    }//GEN-LAST:event_newWarehouseButtonActionPerformed
    private void editWarehouseuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editWarehouseuttonActionPerformed
        openWarehouseEditor();
    }//GEN-LAST:event_editWarehouseuttonActionPerformed

    private void deleteWarehouseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteWarehouseButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {

            WarehouseEditorTopComponent window = (WarehouseEditorTopComponent) WindowManager.getDefault().findTopComponent("WarehouseEditorTopComponent");
            if (window.getWarehouse() != null && warehouse.getId().equals(window.getWarehouse().getId())) {
                window.forceClose();
            }


            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE")));
                    lookup.clear();
                }

                @Override
                protected Void doInBackground() {
                    model.removeRow(warehouseTable.convertRowIndexToModel(warehouseTable.getSelectedRow()));
                    controller.setWarehouse(warehouse);
                    controller.deleteWarehouse();
                    warehouse = null;
                    return null;
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_deleteWarehouseButtonActionPerformed

    private void detailedViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailedViewButtonActionPerformed
        TopComponent window = WindowManager.getDefault().findTopComponent("WarehouseDetailedViewTopComponent");
        if (window == null) {
            window = new WarehouseDetailedViewTopComponent();
        }
        window.open();
        window.requestActive();
    }//GEN-LAST:event_detailedViewButtonActionPerformed

    private void printWarehouseListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printWarehouseListButtonActionPerformed

        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printWarehouseList(model.getWarehouseList());
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printWarehouseListButtonActionPerformed

    private void printSelectedWarehouseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSelectedWarehouseButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printWarehouse(warehouse);
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printSelectedWarehouseButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteWarehouseButton;
    private javax.swing.JButton detailedViewButton;
    private javax.swing.JButton editWarehouseutton;
    private javax.swing.JButton findButton;
    private javax.swing.JComboBox inCombobox;
    private javax.swing.JLabel inLabel;
    private javax.swing.JCheckBox includeDisabledCheckBox;
    private javax.swing.JButton listAllButton;
    private javax.swing.JButton newWarehouseButton;
    private javax.swing.JButton printSelectedWarehouseButton;
    private javax.swing.JButton printWarehouseListButton;
    private javax.swing.JPanel searchButtonPanel;
    private javax.swing.JTextField searchEntryField;
    private javax.swing.JPanel searchEntryPanel;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JScrollPane tablePanel;
    private javax.swing.JToolBar topToolBar;
    private javax.swing.JTable warehouseTable;
    // End of variables declaration//GEN-END:variables

    @Override
    public HelpCtx getHelpCtx() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return new HelpCtx("es.regueiro.easyrepair.ui.stock.browser");
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
            newWarehouseButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.WAREHOUSE_EDIT));
        } else {
            this.close();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            if (warehouseTable.getSelectedRow() > -1) {
                warehouse = model.getRow(warehouseTable.convertRowIndexToModel(warehouseTable.getSelectedRow()));
                editWarehouseutton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.WAREHOUSE_EDIT));
                deleteWarehouseButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.WAREHOUSE_EDIT));
                printSelectedWarehouseButton.setEnabled(true);
                lookup.setWarehouse(warehouse);
            } else {
                editWarehouseutton.setEnabled(false);
                deleteWarehouseButton.setEnabled(false);
                printSelectedWarehouseButton.setEnabled(false);
            }
        }
    }

    private void openWarehouseEditor() {
        TopComponent window = WindowManager.getDefault().findTopComponent("WarehouseEditorTopComponent");
        if (window == null) {
            window = new WarehouseEditorTopComponent();
        } else {
            window.close();
        }
        window.open();
        window.requestActive();
    }

    private void doSearch() {

        printWarehouseListButton.setEnabled(false);

        final SwingWorker<List<Warehouse>, Void> worker = new SwingWorker<List<Warehouse>, Void>() {
            private int column;
            ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SEARCHING"));

            @Override
            protected void done() {
                try {
                    List<Warehouse> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printWarehouseListButton.setEnabled(true);
                    }

                    if (column != 1) {
                        warehouseTable.getRowSorter().toggleSortOrder(column - 1);
                    } else {
                        warehouseTable.getRowSorter().toggleSortOrder(2);
                    }

                    warehouseTable.getRowSorter().toggleSortOrder(column);
                    p.finish();
                } catch (InterruptedException ignore) {
                    System.out.println(ignore.getCause());
                } catch (ExecutionException ignore) {
                    System.out.println(ignore.getCause());
                }
            }

            @Override
            protected List<Warehouse> doInBackground() {
                int selected = inCombobox.getSelectedIndex();
                String toSearch = searchEntryField.getText();
                List<Warehouse> list = null;
                column = 0;
                if (!toSearch.isEmpty()) {
                    p.setDisplayName(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SEARCHING_FOR_X"), new Object[]{toSearch}));
                }
                p.start();
                try {
                    switch (selected) {
                        case 0:
                            list = controller.searchByName(toSearch);
                            column = 1;
                            break;
                        case 1:
                            list = controller.searchByEmailAddress(toSearch);
                            column = 2;
                            break;
                        case 2:
                            list = controller.searchByPhoneNumber(toSearch);
                            column = 3;
                            break;
                        case 3:
                            list = controller.searchByAddressStreet(toSearch);
                            column = 4;
                            break;
                        case 4:
                            list = controller.searchByAddressCity(toSearch);
                            column = 5;
                            break;
                        case 5:
                            list = controller.searchByAddressPostalCode(toSearch);
                            column = 6;
                            break;
                        case 6:
                            list = controller.searchByAddressProvince(toSearch);
                            column = 7;
                            break;
                        case 7:
                            list = controller.searchByAddressCountry(toSearch);
                            column = 8;
                            break;
                        default:
                            //this should never happen
                            list = null;
                    }
                } catch (Exception e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ERROR_SEARCH"), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    p.finish();
                }
                if (!includeDisabledCheckBox.isSelected()) {
                    Iterator<Warehouse> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Warehouse w = iterator.next();
                        if (!w.getEnabled()) {
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

        printWarehouseListButton.setEnabled(false);

        final ProgressHandle p = ProgressHandleFactory.createHandle(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("LISTING_ALL_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSES")));

        SwingWorker<List<Warehouse>, Void> worker = new SwingWorker<List<Warehouse>, Void>() {
            @Override
            protected void done() {
                try {
                    List<Warehouse> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printWarehouseListButton.setEnabled(true);
                    }

                    warehouseTable.getRowSorter().toggleSortOrder(2);
                    warehouseTable.getRowSorter().toggleSortOrder(1);
                    p.finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected List<Warehouse> doInBackground() {
                List<Warehouse> list = null;
                try {
                    list = controller.listAll();
                } catch (Exception e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    p.finish();
                }

                if (!includeDisabledCheckBox.isSelected()) {
                    Iterator<Warehouse> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Warehouse w = iterator.next();
                        if (!w.getEnabled()) {
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

    public void deleteWarehouse(Warehouse emp) {
        if (warehouse != null && emp.getId().equals(warehouse.getId())) {
            warehouse = null;
            StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE")));
            lookup.clear();

        }

        model.removeWarehouse(emp);

    }
}
