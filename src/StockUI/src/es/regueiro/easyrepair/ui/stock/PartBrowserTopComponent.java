package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.login.SecurityManager;
import es.regueiro.easyrepair.api.stock.controller.PartController;
import es.regueiro.easyrepair.login.LoginLookup;
import es.regueiro.easyrepair.model.stock.Part;
import es.regueiro.easyrepair.model.user.Privilege;
import es.regueiro.easyrepair.reports.ReportPrinter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.stock//PartBrowser//EN",
autostore = false)
@TopComponent.Description(preferredID = "PartBrowserTopComponent",
iconBase = "es/regueiro/easyrepair/ui/stock/icons/tire.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 14, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.stock.PartBrowserTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Piezas", position = 0),
    @ActionReference(path = "Toolbars/Piezas", position = 0)
})
@TopComponent.OpenActionRegistration(displayName = "#CTL_PartBrowserAction",
preferredID = "PartBrowserTopComponent")
@Messages({
    "CTL_PartBrowserAction=Buscador de piezas",
    "CTL_PartBrowserTopComponent=Buscador de piezas",
    "HINT_PartBrowserTopComponent=Esta es una ventana del buscador de piezas"
})
public final class PartBrowserTopComponent extends TopComponent implements TableModelListener, ListSelectionListener {

    private PartTableModel model = new PartTableModel();
    private InstanceContent content = new InstanceContent();
    private PartController controller = Lookup.getDefault().lookup(PartController.class);
    private Part part;
    private PartLookup lookup = PartLookup.getDefault();

    public PartBrowserTopComponent() {
        initComponents();
        setName(Bundle.CTL_PartBrowserTopComponent());
        setToolTipText(Bundle.HINT_PartBrowserTopComponent());
        this.setFocusable(true);


        String[] inCBStrings = {
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("MAKE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("MODEL"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("CATEGORY"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PRICE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("MINPRICE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("MAXPRICE")};

        for (String s : inCBStrings) {
            inCombobox.addItem(s);
        }
        // Associate the table model
        partTable.setModel(model);

        for (int i = 0; i < partTable.getColumnCount(); i++) {
            partTable.getColumnModel().getColumn(i).setCellRenderer(new PartTableCellRenderer());
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
        partTable.getSelectionModel().addListSelectionListener(this);

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
        partTable = new javax.swing.JTable();
        topToolBar = new javax.swing.JToolBar();
        detailedViewButton = new javax.swing.JButton();
        newPartButton = new javax.swing.JButton();
        editPartButton = new javax.swing.JButton();
        deletePartButton = new javax.swing.JButton();
        printSelectedPartButton = new javax.swing.JButton();
        printPartListButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        searchPanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        searchPanel.setLayout(new java.awt.GridBagLayout());

        searchEntryPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(searchLabel, org.openide.util.NbBundle.getMessage(PartBrowserTopComponent.class, "PartBrowserTopComponent.searchLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(inLabel, org.openide.util.NbBundle.getMessage(PartBrowserTopComponent.class, "PartBrowserTopComponent.inLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(inLabel, gridBagConstraints);

        listAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/group.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(listAllButton, org.openide.util.NbBundle.getMessage(PartBrowserTopComponent.class, "PartBrowserTopComponent.listAllButton.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(includeDisabledCheckBox, org.openide.util.NbBundle.getMessage(PartBrowserTopComponent.class, "PartBrowserTopComponent.includeDisabledCheckBox.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(findButton, org.openide.util.NbBundle.getMessage(PartBrowserTopComponent.class, "PartBrowserTopComponent.findButton.text")); // NOI18N
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

        partTable.setAutoCreateRowSorter(true);
        partTable.setDoubleBuffered(true);
        partTable.setFillsViewportHeight(true);
        partTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        partTable.setRowHeight(25);
        partTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        partTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablePanel.setViewportView(partTable);

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

        detailedViewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/tire_detailed.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(detailedViewButton, org.openide.util.NbBundle.getMessage(PartBrowserTopComponent.class, "PartBrowserTopComponent.detailedViewButton.text")); // NOI18N
        detailedViewButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        detailedViewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailedViewButtonActionPerformed(evt);
            }
        });
        topToolBar.add(detailedViewButton);

        newPartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(newPartButton, org.openide.util.NbBundle.getMessage(PartBrowserTopComponent.class, "PartBrowserTopComponent.newPartButton.text")); // NOI18N
        newPartButton.setFocusable(false);
        newPartButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        newPartButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        newPartButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        newPartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newPartButtonActionPerformed(evt);
            }
        });
        topToolBar.add(newPartButton);

        editPartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/tire_edit.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(editPartButton, org.openide.util.NbBundle.getMessage(PartBrowserTopComponent.class, "PartBrowserTopComponent.editPartutton.text")); // NOI18N
        editPartButton.setEnabled(false);
        editPartButton.setFocusable(false);
        editPartButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        editPartButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        editPartButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        editPartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPartButtonActionPerformed(evt);
            }
        });
        topToolBar.add(editPartButton);

        deletePartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deletePartButton, org.openide.util.NbBundle.getMessage(PartBrowserTopComponent.class, "PartBrowserTopComponent.deletePartButton.text")); // NOI18N
        deletePartButton.setEnabled(false);
        deletePartButton.setFocusable(false);
        deletePartButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deletePartButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deletePartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePartButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deletePartButton);

        printSelectedPartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printSelectedPartButton, org.openide.util.NbBundle.getMessage(PartBrowserTopComponent.class, "PartBrowserTopComponent.printSelectedPartButton.text")); // NOI18N
        printSelectedPartButton.setEnabled(false);
        printSelectedPartButton.setFocusable(false);
        printSelectedPartButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printSelectedPartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSelectedPartButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printSelectedPartButton);

        printPartListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printPartListButton, org.openide.util.NbBundle.getMessage(PartBrowserTopComponent.class, "PartBrowserTopComponent.printPartListButton.text")); // NOI18N
        printPartListButton.setEnabled(false);
        printPartListButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printPartListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printPartListButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printPartListButton);

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

    private void newPartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newPartButtonActionPerformed
        lookup.clear();
        editPartButton.setEnabled(false);
        deletePartButton.setEnabled(false);
        printSelectedPartButton.setEnabled(false);
        partTable.clearSelection();
        openPartEditor();
    }//GEN-LAST:event_newPartButtonActionPerformed
    private void editPartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPartButtonActionPerformed
        openPartEditor();
    }//GEN-LAST:event_editPartButtonActionPerformed

    private void deletePartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePartButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PART")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {

//            PartEditorTopComponent window = (PartEditorTopComponent) WindowManager.getDefault().findTopComponent("PartEditorTopComponent");
//            if (window.getPart() != null && Part.getId().equals(window.getPart().getId())) {
//                window.forceClose();
//            }


            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PART")));
                    lookup.clear();
                }

                @Override
                protected Void doInBackground() {
                    model.removeRow(partTable.convertRowIndexToModel(partTable.getSelectedRow()));
                    controller.setPart(part);
                    controller.deletePart();
                    part = null;
                    return null;
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_deletePartButtonActionPerformed

    private void detailedViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailedViewButtonActionPerformed
        TopComponent window = WindowManager.getDefault().findTopComponent("PartDetailedViewTopComponent");
        if (window == null) {
//            window = new PartDetailedViewTopComponent();
        }
        window.open();
        window.requestActive();

    }//GEN-LAST:event_detailedViewButtonActionPerformed

    private void printPartListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printPartListButtonActionPerformed

        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printPartList(model.getPartList());
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printPartListButtonActionPerformed

    private void printSelectedPartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSelectedPartButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printPart(part);
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printSelectedPartButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deletePartButton;
    private javax.swing.JButton detailedViewButton;
    private javax.swing.JButton editPartButton;
    private javax.swing.JButton findButton;
    private javax.swing.JComboBox inCombobox;
    private javax.swing.JLabel inLabel;
    private javax.swing.JCheckBox includeDisabledCheckBox;
    private javax.swing.JButton listAllButton;
    private javax.swing.JButton newPartButton;
    private javax.swing.JTable partTable;
    private javax.swing.JButton printPartListButton;
    private javax.swing.JButton printSelectedPartButton;
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
            newPartButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_EDIT));
        } else {
            this.close();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            if (partTable.getSelectedRow() > -1) {
                part = model.getRow(partTable.convertRowIndexToModel(partTable.getSelectedRow()));
                editPartButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_EDIT) || SecurityManager.getDefault().userHasPrivilege(Privilege.PART_STOCK_EDIT));
                deletePartButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_EDIT));
                printSelectedPartButton.setEnabled(true);
                lookup.setPart(part);
            } else {
                editPartButton.setEnabled(false);
                deletePartButton.setEnabled(false);
                printSelectedPartButton.setEnabled(false);
            }
        }
    }

    private void openPartEditor() {
        TopComponent window = WindowManager.getDefault().findTopComponent("PartEditorTopComponent");
        if (window == null) {
//            window = new PartEditorTopComponent();
        } else {
            window.close();
        }
        window.open();
        window.requestActive();
    }

    private void doSearch() {

        printPartListButton.setEnabled(false);

        final SwingWorker<List<Part>, Void> worker = new SwingWorker<List<Part>, Void>() {
            private int column;
            ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SEARCHING"));

            @Override
            protected void done() {
                try {
                    List<Part> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printPartListButton.setEnabled(true);
                    }

                    if (column != 0) {
                        partTable.getRowSorter().toggleSortOrder(column - 1);
                    } else {
                        partTable.getRowSorter().toggleSortOrder(1);
                    }

                    partTable.getRowSorter().toggleSortOrder(column);
                    p.finish();
                } catch (InterruptedException ignore) {
                    System.out.println(ignore.getCause());
                } catch (ExecutionException ignore) {
                    System.out.println(ignore.getCause());
                }
            }

            @Override
            protected List<Part> doInBackground() {
                int selected = inCombobox.getSelectedIndex();
                String toSearch = searchEntryField.getText();
                List<Part> list = new ArrayList<Part>();
                column = 0;
                if (!toSearch.isEmpty()) {
                    p.setDisplayName(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SEARCHING_FOR_X"), new Object[]{toSearch}));
                }
                p.start();
                try {
                    switch (selected) {
                        case 0:
                            list = controller.searchByMake(toSearch);
                            column = 1;
                            break;
                        case 1:
                            list = controller.searchByModel(toSearch);
                            column = 2;
                            break;
                        case 2:
                            list = controller.searchByCategory(toSearch);
                            column = 3;
                            break;
                        case 3:
                            list = controller.searchByPrice(toSearch);
                            column = 4;
                            break;
                        case 4:
                            try {
                                list = controller.searchByMinPrice(toSearch);
                                column = 4;
                            } catch (Exception e) {
                                NotifyDescriptor d = new NotifyDescriptor.Message(
                                        java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_INVALID"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PRICE")), NotifyDescriptor.WARNING_MESSAGE);
                                Object retval = DialogDisplayer.getDefault().notify(d);
                            }
                            break;
                        case 5:
                            try {
                                list = controller.searchByMaxPrice(toSearch);
                                column = 4;
                            } catch (Exception e) {
                                NotifyDescriptor d = new NotifyDescriptor.Message(
                                        java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_INVALID"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PRICE")), NotifyDescriptor.WARNING_MESSAGE);
                                Object retval = DialogDisplayer.getDefault().notify(d);
                            }
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
                    Iterator<Part> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Part p = iterator.next();
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

        printPartListButton.setEnabled(false);

        final ProgressHandle p = ProgressHandleFactory.createHandle(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("LISTING_ALL_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PARTS")));

        SwingWorker<List<Part>, Void> worker = new SwingWorker<List<Part>, Void>() {
            @Override
            protected void done() {
                try {
                    List<Part> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printPartListButton.setEnabled(true);
                    }

                    partTable.getRowSorter().toggleSortOrder(2);
                    partTable.getRowSorter().toggleSortOrder(1);
                    p.finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected List<Part> doInBackground() {
                List<Part> list = null;
                try {
                    list = controller.listAll();
                } catch (Exception e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    p.finish();
                }

                if (!includeDisabledCheckBox.isSelected()) {
                    Iterator<Part> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Part p = iterator.next();
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

    public void deletePart(Part pa) {
        if (part != null && pa.getId().equals(part.getId())) {
            part = null;
            StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PART")));
            lookup.clear();
        }
        model.removePart(pa);
    }
}
