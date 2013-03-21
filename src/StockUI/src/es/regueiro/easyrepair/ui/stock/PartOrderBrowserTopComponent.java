package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.login.SecurityManager;
import es.regueiro.easyrepair.api.stock.controller.PartOrderController;
import es.regueiro.easyrepair.login.LoginLookup;
import es.regueiro.easyrepair.model.stock.PartOrder;
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
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.stock//PartOrderBrowser//EN",
autostore = false)
@TopComponent.Description(preferredID = "PartOrderBrowserTopComponent",
iconBase = "es/regueiro/easyrepair/ui/stock/icons/package.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 16, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.stock.PartOrderBrowserTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Piezas", position = 1),
    @ActionReference(path = "Toolbars/Piezas", position = 1)
})
@TopComponent.OpenActionRegistration(displayName = "#CTL_PartOrderBrowserAction",
preferredID = "PartOrderBrowserTopComponent")
@Messages({
    "CTL_PartOrderBrowserAction=Buscador de pedidos",
    "CTL_PartOrderBrowserTopComponent=Buscador de pedidos",
    "HINT_PartOrderBrowserTopComponent=Esta es una ventana del buscador de pedidos"
})
public final class PartOrderBrowserTopComponent extends TopComponent implements TableModelListener, ListSelectionListener {

    private PartOrderTableModel model = new PartOrderTableModel();
    private InstanceContent content = new InstanceContent();
    private PartOrderController controller = Lookup.getDefault().lookup(PartOrderController.class);
    private PartOrder partOrder;
    private PartOrderLookup lookup = PartOrderLookup.getDefault();

    public PartOrderBrowserTopComponent() {
        initComponents();
        setName(Bundle.CTL_PartOrderBrowserTopComponent());
        setToolTipText(Bundle.HINT_PartOrderBrowserTopComponent());
        this.setFocusable(true);


        String[] inCBStrings = {
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ORDERNUMBER"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SHIPPINGCOSTS"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("MAXSHIPPINGCOSTS"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("MINSHIPPINGCOSTS"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("OTHERCOSTS"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("MAXOTHERCOSTS"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("MINOTHERCOSTS"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DISCOUNT"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("MAXDISCOUNT"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("MINDISCOUNT"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("STATUS"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIER"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RESPONSIBLE"),};

        for (String s : inCBStrings) {
            inCombobox.addItem(s);
        }
        // Associate the table model
        partOrderTable.setModel(model);

        for (int i = 0; i < partOrderTable.getColumnCount(); i++) {
            partOrderTable.getColumnModel().getColumn(i).setCellRenderer(new PartOrderTableCellRenderer());
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
        partOrderTable.getSelectionModel().addListSelectionListener(this);

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
        partOrderTable = new javax.swing.JTable();
        topToolBar = new javax.swing.JToolBar();
        detailedViewButton = new javax.swing.JButton();
        newPartOrderButton = new javax.swing.JButton();
        editPartOrderButton = new javax.swing.JButton();
        deletePartOrderButton = new javax.swing.JButton();
        printSelectedPartOrderButton = new javax.swing.JButton();
        printPartOrderListButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        searchPanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        searchPanel.setLayout(new java.awt.GridBagLayout());

        searchEntryPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(searchLabel, org.openide.util.NbBundle.getMessage(PartOrderBrowserTopComponent.class, "PartOrderBrowserTopComponent.searchLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(inLabel, org.openide.util.NbBundle.getMessage(PartOrderBrowserTopComponent.class, "PartOrderBrowserTopComponent.inLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(inLabel, gridBagConstraints);

        listAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/group.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(listAllButton, org.openide.util.NbBundle.getMessage(PartOrderBrowserTopComponent.class, "PartOrderBrowserTopComponent.listAllButton.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(includeDisabledCheckBox, org.openide.util.NbBundle.getMessage(PartOrderBrowserTopComponent.class, "PartOrderBrowserTopComponent.includeDisabledCheckBox.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(findButton, org.openide.util.NbBundle.getMessage(PartOrderBrowserTopComponent.class, "PartOrderBrowserTopComponent.findButton.text")); // NOI18N
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

        partOrderTable.setAutoCreateRowSorter(true);
        partOrderTable.setDoubleBuffered(true);
        partOrderTable.setFillsViewportHeight(true);
        partOrderTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        partOrderTable.setRowHeight(25);
        partOrderTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        partOrderTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablePanel.setViewportView(partOrderTable);

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

        detailedViewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/package_detailed.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(detailedViewButton, org.openide.util.NbBundle.getMessage(PartOrderBrowserTopComponent.class, "PartOrderBrowserTopComponent.detailedViewButton.text")); // NOI18N
        detailedViewButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        detailedViewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailedViewButtonActionPerformed(evt);
            }
        });
        topToolBar.add(detailedViewButton);

        newPartOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(newPartOrderButton, org.openide.util.NbBundle.getMessage(PartOrderBrowserTopComponent.class, "PartOrderBrowserTopComponent.newPartOrderButton.text")); // NOI18N
        newPartOrderButton.setFocusable(false);
        newPartOrderButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        newPartOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        newPartOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        newPartOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newPartOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(newPartOrderButton);

        editPartOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/package_edit.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(editPartOrderButton, org.openide.util.NbBundle.getMessage(PartOrderBrowserTopComponent.class, "PartOrderBrowserTopComponent.editPartOrderutton.text")); // NOI18N
        editPartOrderButton.setEnabled(false);
        editPartOrderButton.setFocusable(false);
        editPartOrderButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        editPartOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        editPartOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        editPartOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPartOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(editPartOrderButton);

        deletePartOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deletePartOrderButton, org.openide.util.NbBundle.getMessage(PartOrderBrowserTopComponent.class, "PartOrderBrowserTopComponent.deletePartOrderButton.text")); // NOI18N
        deletePartOrderButton.setEnabled(false);
        deletePartOrderButton.setFocusable(false);
        deletePartOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deletePartOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deletePartOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePartOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deletePartOrderButton);

        printSelectedPartOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printSelectedPartOrderButton, org.openide.util.NbBundle.getMessage(PartOrderBrowserTopComponent.class, "PartOrderBrowserTopComponent.printSelectedPartOrderButton.text")); // NOI18N
        printSelectedPartOrderButton.setEnabled(false);
        printSelectedPartOrderButton.setFocusable(false);
        printSelectedPartOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printSelectedPartOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSelectedPartOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printSelectedPartOrderButton);

        printPartOrderListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printPartOrderListButton, org.openide.util.NbBundle.getMessage(PartOrderBrowserTopComponent.class, "PartOrderBrowserTopComponent.printPartOrderListButton.text")); // NOI18N
        printPartOrderListButton.setEnabled(false);
        printPartOrderListButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printPartOrderListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printPartOrderListButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printPartOrderListButton);

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

    private void newPartOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newPartOrderButtonActionPerformed
        lookup.clear();
        editPartOrderButton.setEnabled(false);
        deletePartOrderButton.setEnabled(false);
        printSelectedPartOrderButton.setEnabled(false);
        partOrderTable.clearSelection();
        openPartOrderEditor();
    }//GEN-LAST:event_newPartOrderButtonActionPerformed
    private void editPartOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPartOrderButtonActionPerformed
        openPartOrderEditor();
    }//GEN-LAST:event_editPartOrderButtonActionPerformed

    private void deletePartOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePartOrderButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PARTORDER")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {

//            PartOrderEditorTopComponent window = (PartOrderEditorTopComponent) WindowManager.getDefault().findTopComponent("PartOrderEditorTopComponent");
//            if (window.getPartOrder() != null && PartOrder.getId().equals(window.getPartOrder().getId())) {
//                window.forceClose();
//            }


            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PARTORDER")));
                    lookup.clear();
                }

                @Override
                protected Void doInBackground() {
                    model.removeRow(partOrderTable.convertRowIndexToModel(partOrderTable.getSelectedRow()));
                    controller.setPartOrder(partOrder);
                    controller.deletePartOrder();
                    partOrder = null;
                    return null;
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_deletePartOrderButtonActionPerformed

    private void detailedViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailedViewButtonActionPerformed
        TopComponent window = WindowManager.getDefault().findTopComponent("PartOrderDetailedViewTopComponent");
        if (window == null) {
//            window = new PartOrderDetailedViewTopComponent();
        }
        window.open();
        window.requestActive();

    }//GEN-LAST:event_detailedViewButtonActionPerformed

    private void printPartOrderListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printPartOrderListButtonActionPerformed

        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printPartOrderList(model.getPartOrderList());
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printPartOrderListButtonActionPerformed

    private void printSelectedPartOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSelectedPartOrderButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printPartOrder(partOrder);
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printSelectedPartOrderButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deletePartOrderButton;
    private javax.swing.JButton detailedViewButton;
    private javax.swing.JButton editPartOrderButton;
    private javax.swing.JButton findButton;
    private javax.swing.JComboBox inCombobox;
    private javax.swing.JLabel inLabel;
    private javax.swing.JCheckBox includeDisabledCheckBox;
    private javax.swing.JButton listAllButton;
    private javax.swing.JButton newPartOrderButton;
    private javax.swing.JTable partOrderTable;
    private javax.swing.JButton printPartOrderListButton;
    private javax.swing.JButton printSelectedPartOrderButton;
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
            newPartOrderButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));
        } else {
            this.close();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            if (partOrderTable.getSelectedRow() > -1) {
                partOrder = model.getRow(partOrderTable.convertRowIndexToModel(partOrderTable.getSelectedRow()));
                editPartOrderButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));
                deletePartOrderButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));
                printSelectedPartOrderButton.setEnabled(true);
                lookup.setPartOrder(partOrder);
            } else {
                editPartOrderButton.setEnabled(false);
                deletePartOrderButton.setEnabled(false);
                printSelectedPartOrderButton.setEnabled(false);
            }
        }
    }

    private void openPartOrderEditor() {
        TopComponent window = WindowManager.getDefault().findTopComponent("PartOrderEditorTopComponent");
        if (window == null) {
//            window = new PartOrderEditorTopComponent();
        } else {
            window.close();
        }
        window.open();
        window.requestActive();
    }

    private void doSearch() {

        printPartOrderListButton.setEnabled(false);

        final SwingWorker<List<PartOrder>, Void> worker = new SwingWorker<List<PartOrder>, Void>() {
            private int column;
            ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SEARCHING"));

            @Override
            protected void done() {
                try {
                    List<PartOrder> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printPartOrderListButton.setEnabled(true);
                    }

                    if (column != 1) {
                        partOrderTable.getRowSorter().toggleSortOrder(column - 1);
                    } else {
                        partOrderTable.getRowSorter().toggleSortOrder(2);
                    }

                    partOrderTable.getRowSorter().toggleSortOrder(column);
                    p.finish();
                } catch (InterruptedException ignore) {
                    System.out.println(ignore.getCause());
                } catch (ExecutionException ignore) {
                    System.out.println(ignore.getCause());
                }
            }

            @Override
            protected List<PartOrder> doInBackground() {
                int selected = inCombobox.getSelectedIndex();
                String toSearch = searchEntryField.getText();
                List<PartOrder> list = null;
                column = 0;
                if (!toSearch.isEmpty()) {
                    p.setDisplayName(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SEARCHING_FOR_X"), new Object[]{toSearch}));
                }
                p.start();
                try {
                    switch (selected) {
                        case 0:
                            list = controller.searchByOrderNumber(toSearch);
                            column = 1;
                            break;
                        case 1:
                            list = controller.searchByShippingCosts(toSearch);
                            column = 1;
                            break;
                        case 2:
                            list = controller.searchByMaxShippingCosts(toSearch);
                            column = 1;
                            break;
                        case 3:
                            list = controller.searchByMinShippingCosts(toSearch);
                            column = 1;
                            break;
                        case 4:
                            list = controller.searchByOtherCosts(toSearch);
                            column = 1;
                            break;
                        case 5:
                            list = controller.searchByMaxOtherCosts(toSearch);
                            column = 1;
                            break;
                        case 6:
                            list = controller.searchByMinOtherCosts(toSearch);
                            column = 1;
                            break;
                        case 7:
                            list = controller.searchByDiscount(toSearch);
                            column = 1;
                            break;
                        case 8:
                            list = controller.searchByMaxDiscount(toSearch);
                            column = 1;
                            break;
                        case 9:
                            list = controller.searchByMinDiscount(toSearch);
                            column = 1;
                            break;
                        case 10:
                            list = controller.searchByStatus(toSearch);
                            column = 1;
                            break;
                        case 11:
                            list = controller.searchBySupplierName(toSearch);
                            column = 1;
                            break;
                        case 12:
                            list = controller.searchByWarehouseName(toSearch);
                            column = 1;
                            break;
                        case 13:
                            list = controller.searchByResponsible(toSearch);
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
                    Iterator<PartOrder> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        PartOrder p = iterator.next();
                        if (!p.getEnabled()) {
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

        printPartOrderListButton.setEnabled(false);

        final ProgressHandle p = ProgressHandleFactory.createHandle(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("LISTING_ALL_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PARTORDERS")));

        SwingWorker<List<PartOrder>, Void> worker = new SwingWorker<List<PartOrder>, Void>() {
            @Override
            protected void done() {
                try {
                    List<PartOrder> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printPartOrderListButton.setEnabled(true);
                    }

                    partOrderTable.getRowSorter().toggleSortOrder(2);
                    partOrderTable.getRowSorter().toggleSortOrder(1);
                    p.finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected List<PartOrder> doInBackground() {
                List<PartOrder> list = null;
                try {
                    list = controller.listAll();
                } catch (Exception e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    p.finish();
                }

                if (!includeDisabledCheckBox.isSelected()) {
                    Iterator<PartOrder> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        PartOrder p = iterator.next();
                        if (!p.getEnabled()) {
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

    public void deletePartOrder(PartOrder ord) {
        if (partOrder != null && ord.getId().equals(partOrder.getId())) {
            partOrder = null;
            StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PARTORDER")));
            lookup.clear();

        }

        model.removePartOrder(ord);

    }
}
