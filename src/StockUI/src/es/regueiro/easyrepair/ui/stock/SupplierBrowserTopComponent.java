package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.login.SecurityManager;
import es.regueiro.easyrepair.api.stock.controller.SupplierController;
import es.regueiro.easyrepair.login.LoginLookup;
import es.regueiro.easyrepair.model.stock.Supplier;
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
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.stock//SupplierBrowser//EN",
autostore = false)
@TopComponent.Description(preferredID = "SupplierBrowserTopComponent",
iconBase = "es/regueiro/easyrepair/ui/stock/icons/supplier.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 20, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.stock.SupplierBrowserTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Piezas", position = 3),
    @ActionReference(path = "Toolbars/Piezas", position = 3)
})
@TopComponent.OpenActionRegistration(displayName = "#CTL_SupplierBrowserAction",
preferredID = "SupplierBrowserTopComponent")
@Messages({
    "CTL_SupplierBrowserAction=Buscador de proveedores",
    "CTL_SupplierBrowserTopComponent=Buscador de proveedores",
    "HINT_SupplierBrowserTopComponent=Esta es una ventana del buscador de proveedores"
})
public final class SupplierBrowserTopComponent extends TopComponent implements TableModelListener, ListSelectionListener {

    private SupplierTableModel model = new SupplierTableModel();
    private InstanceContent content = new InstanceContent();
    private SupplierController controller = Lookup.getDefault().lookup(SupplierController.class);
    private Supplier supplier;
    private SupplierLookup lookup = SupplierLookup.getDefault();

    public SupplierBrowserTopComponent() {
        initComponents();
        setName(Bundle.CTL_SupplierBrowserTopComponent());
        setToolTipText(Bundle.HINT_SupplierBrowserTopComponent());
        this.setFocusable(true);


        String[] inCBStrings = {java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("NAME"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("NIF"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("CATEGORY"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WEB"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PAYMENTMETHOD"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SHIPPINGMETHOD"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("EMAIL_LABEL"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("EMAIL_ADDRESS"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PHONE_LABEL"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PHONE_NUMBER"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS_LABEL"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS_STREET"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS_CITY"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS_POSTAL_CODE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS_PROVINCE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS_COUNTRY")};

        for (String s : inCBStrings) {
            inCombobox.addItem(s);
        }
        // Associate the table model
        supplierTable.setModel(model);

        for (int i = 0; i < supplierTable.getColumnCount(); i++) {
            supplierTable.getColumnModel().getColumn(i).setCellRenderer(new SupplierTableCellRenderer());
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
        supplierTable.getSelectionModel().addListSelectionListener(this);

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
        supplierTable = new javax.swing.JTable();
        topToolBar = new javax.swing.JToolBar();
        detailedViewButton = new javax.swing.JButton();
        newSupplierButton = new javax.swing.JButton();
        editSupplierButton = new javax.swing.JButton();
        deleteSupplierButton = new javax.swing.JButton();
        printSelectedSupplierButton = new javax.swing.JButton();
        printSupplierListButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        searchPanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        searchPanel.setLayout(new java.awt.GridBagLayout());

        searchEntryPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(searchLabel, org.openide.util.NbBundle.getMessage(SupplierBrowserTopComponent.class, "SupplierBrowserTopComponent.searchLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(inLabel, org.openide.util.NbBundle.getMessage(SupplierBrowserTopComponent.class, "SupplierBrowserTopComponent.inLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(inLabel, gridBagConstraints);

        listAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/group.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(listAllButton, org.openide.util.NbBundle.getMessage(SupplierBrowserTopComponent.class, "SupplierBrowserTopComponent.listAllButton.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(includeDisabledCheckBox, org.openide.util.NbBundle.getMessage(SupplierBrowserTopComponent.class, "SupplierBrowserTopComponent.includeDisabledCheckBox.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(findButton, org.openide.util.NbBundle.getMessage(SupplierBrowserTopComponent.class, "SupplierBrowserTopComponent.findButton.text")); // NOI18N
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

        supplierTable.setAutoCreateRowSorter(true);
        supplierTable.setDoubleBuffered(true);
        supplierTable.setFillsViewportHeight(true);
        supplierTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        supplierTable.setRowHeight(25);
        supplierTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        supplierTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablePanel.setViewportView(supplierTable);

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

        detailedViewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/supplier_detailed.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(detailedViewButton, org.openide.util.NbBundle.getMessage(SupplierBrowserTopComponent.class, "SupplierBrowserTopComponent.detailedViewButton.text")); // NOI18N
        detailedViewButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        detailedViewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailedViewButtonActionPerformed(evt);
            }
        });
        topToolBar.add(detailedViewButton);

        newSupplierButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(newSupplierButton, org.openide.util.NbBundle.getMessage(SupplierBrowserTopComponent.class, "SupplierBrowserTopComponent.newSupplierButton.text")); // NOI18N
        newSupplierButton.setFocusable(false);
        newSupplierButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        newSupplierButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        newSupplierButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        newSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newSupplierButtonActionPerformed(evt);
            }
        });
        topToolBar.add(newSupplierButton);

        editSupplierButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/supplier_edit.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(editSupplierButton, org.openide.util.NbBundle.getMessage(SupplierBrowserTopComponent.class, "SupplierBrowserTopComponent.editSupplierutton.text")); // NOI18N
        editSupplierButton.setEnabled(false);
        editSupplierButton.setFocusable(false);
        editSupplierButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        editSupplierButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        editSupplierButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        editSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSupplierButtonActionPerformed(evt);
            }
        });
        topToolBar.add(editSupplierButton);

        deleteSupplierButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteSupplierButton, org.openide.util.NbBundle.getMessage(SupplierBrowserTopComponent.class, "SupplierBrowserTopComponent.deleteSupplierButton.text")); // NOI18N
        deleteSupplierButton.setEnabled(false);
        deleteSupplierButton.setFocusable(false);
        deleteSupplierButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deleteSupplierButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deleteSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSupplierButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deleteSupplierButton);

        printSelectedSupplierButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printSelectedSupplierButton, org.openide.util.NbBundle.getMessage(SupplierBrowserTopComponent.class, "SupplierBrowserTopComponent.printSelectedSupplierButton.text")); // NOI18N
        printSelectedSupplierButton.setEnabled(false);
        printSelectedSupplierButton.setFocusable(false);
        printSelectedSupplierButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printSelectedSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSelectedSupplierButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printSelectedSupplierButton);

        printSupplierListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printSupplierListButton, org.openide.util.NbBundle.getMessage(SupplierBrowserTopComponent.class, "SupplierBrowserTopComponent.printSupplierListButton.text")); // NOI18N
        printSupplierListButton.setEnabled(false);
        printSupplierListButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printSupplierListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSupplierListButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printSupplierListButton);

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

    private void newSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newSupplierButtonActionPerformed
        lookup.clear();
        editSupplierButton.setEnabled(false);
        deleteSupplierButton.setEnabled(false);
        printSelectedSupplierButton.setEnabled(false);
        supplierTable.clearSelection();
        openSupplierEditor();
    }//GEN-LAST:event_newSupplierButtonActionPerformed
    private void editSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSupplierButtonActionPerformed
        openSupplierEditor();
    }//GEN-LAST:event_editSupplierButtonActionPerformed

    private void deleteSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSupplierButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIER")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {

//            SupplierEditorTopComponent window = (SupplierEditorTopComponent) WindowManager.getDefault().findTopComponent("SupplierEditorTopComponent");
//            if (window.getSupplier() != null && Supplier.getId().equals(window.getSupplier().getId())) {
//                window.forceClose();
//            }


            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIER")));
                    lookup.clear();
                }

                @Override
                protected Void doInBackground() {
                    model.removeRow(supplierTable.convertRowIndexToModel(supplierTable.getSelectedRow()));
                    controller.setSupplier(supplier);
                    controller.deleteSupplier();
                    supplier = null;
                    return null;
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_deleteSupplierButtonActionPerformed

    private void detailedViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailedViewButtonActionPerformed
        TopComponent window = WindowManager.getDefault().findTopComponent("SupplierDetailedViewTopComponent");
        if (window == null) {
//            window = new SupplierDetailedViewTopComponent();
        }
        window.open();
        window.requestActive();

    }//GEN-LAST:event_detailedViewButtonActionPerformed

    private void printSupplierListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSupplierListButtonActionPerformed

        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printSupplierList(model.getSupplierList());
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printSupplierListButtonActionPerformed

    private void printSelectedSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSelectedSupplierButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printSupplier(supplier);
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printSelectedSupplierButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteSupplierButton;
    private javax.swing.JButton detailedViewButton;
    private javax.swing.JButton editSupplierButton;
    private javax.swing.JButton findButton;
    private javax.swing.JComboBox inCombobox;
    private javax.swing.JLabel inLabel;
    private javax.swing.JCheckBox includeDisabledCheckBox;
    private javax.swing.JButton listAllButton;
    private javax.swing.JButton newSupplierButton;
    private javax.swing.JButton printSelectedSupplierButton;
    private javax.swing.JButton printSupplierListButton;
    private javax.swing.JPanel searchButtonPanel;
    private javax.swing.JTextField searchEntryField;
    private javax.swing.JPanel searchEntryPanel;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTable supplierTable;
    private javax.swing.JScrollPane tablePanel;
    private javax.swing.JToolBar topToolBar;
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
            newSupplierButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.SUPPLIER_EDIT));
        } else {
            this.close();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            if (supplierTable.getSelectedRow() > -1) {
                supplier = model.getRow(supplierTable.convertRowIndexToModel(supplierTable.getSelectedRow()));
                editSupplierButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.SUPPLIER_EDIT));
                deleteSupplierButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.SUPPLIER_EDIT));
                printSelectedSupplierButton.setEnabled(true);
                lookup.setSupplier(supplier);
            } else {
                editSupplierButton.setEnabled(false);
                deleteSupplierButton.setEnabled(false);
                printSelectedSupplierButton.setEnabled(false);
            }
        }
    }

    private void openSupplierEditor() {
        TopComponent window = WindowManager.getDefault().findTopComponent("SupplierEditorTopComponent");
        if (window == null) {
//            window = new SupplierEditorTopComponent();
        } else {
            window.close();
        }
        window.open();
        window.requestActive();
    }

    private void doSearch() {

        printSupplierListButton.setEnabled(false);

        final SwingWorker<List<Supplier>, Void> worker = new SwingWorker<List<Supplier>, Void>() {
            private int column;
            ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SEARCHING"));

            @Override
            protected void done() {
                try {
                    List<Supplier> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printSupplierListButton.setEnabled(true);
                    }


                    if (column != 1) {
                        supplierTable.getRowSorter().toggleSortOrder(column - 1);
                    } else {
                        supplierTable.getRowSorter().toggleSortOrder(2);
                    }

                    supplierTable.getRowSorter().toggleSortOrder(column);
                    p.finish();
                } catch (InterruptedException ignore) {
                    System.out.println(ignore.getCause());
                } catch (ExecutionException ignore) {
                    System.out.println(ignore.getCause());
                }
            }

            @Override
            protected List<Supplier> doInBackground() {
                int selected = inCombobox.getSelectedIndex();
                String toSearch = searchEntryField.getText();
                List<Supplier> list = null;
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
                            list = controller.searchByNif(toSearch);
                            column = 2;
                            break;
                        case 2:
                            list = controller.searchByCategory(toSearch);
                            column = 3;
                            break;
                        case 3:
                            list = controller.searchByWeb(toSearch);
                            column = 4;
                            break;
                        case 4:
                            list = controller.searchByPaymentMethod(toSearch);
                            column = 5;
                            break;
                        case 5:
                            list = controller.searchByShippingMethod(toSearch);
                            column = 6;
                            break;
                        case 6:
                            list = controller.searchByEmailLabel(toSearch);
                            column = 1;
                            break;
                        case 7:
                            list = controller.searchByEmailAddress(toSearch);
                            column = 1;
                            break;
                        case 8:
                            list = controller.searchByPhoneLabel(toSearch);
                            column = 1;
                            break;
                        case 9:
                            list = controller.searchByPhoneNumber(toSearch);
                            column = 1;
                            break;
                        case 10:
                            list = controller.searchByAddressLabel(toSearch);
                            column = 1;
                            break;
                        case 11:
                            list = controller.searchByAddressStreet(toSearch);
                            column = 1;
                            break;
                        case 12:
                            list = controller.searchByAddressCity(toSearch);
                            column = 1;
                            break;
                        case 13:
                            list = controller.searchByAddressPostalCode(toSearch);
                            column = 1;
                            break;
                        case 14:
                            list = controller.searchByAddressProvince(toSearch);
                            column = 1;
                            break;
                        case 15:
                            list = controller.searchByAddressCountry(toSearch);
                            column = 1;
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
                    Iterator<Supplier> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Supplier s = iterator.next();
                        if (!s.getEnabled()) {
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

        printSupplierListButton.setEnabled(false);

        final ProgressHandle p = ProgressHandleFactory.createHandle(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("LISTING_ALL_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIERS")));

        SwingWorker<List<Supplier>, Void> worker = new SwingWorker<List<Supplier>, Void>() {
            @Override
            protected void done() {
                try {
                    List<Supplier> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printSupplierListButton.setEnabled(true);
                    }

                    supplierTable.getRowSorter().toggleSortOrder(2);
                    supplierTable.getRowSorter().toggleSortOrder(1);
                    p.finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected List<Supplier> doInBackground() {
                List<Supplier> list = null;
                try {
                    list = controller.listAll();
                } catch (Exception e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    p.finish();
                }

                if (!includeDisabledCheckBox.isSelected()) {
                    Iterator<Supplier> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Supplier s = iterator.next();
                        if (!s.getEnabled()) {
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

    public void deleteSupplier(Supplier sup) {
        if (supplier != null && sup.getId().equals(supplier.getId())) {
            supplier = null;
            StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIER")));
            lookup.clear();

        }

        model.removeSupplier(sup);

    }
}
