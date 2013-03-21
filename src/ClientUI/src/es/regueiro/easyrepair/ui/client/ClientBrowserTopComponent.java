package es.regueiro.easyrepair.ui.client;

import es.regueiro.easyrepair.api.client.controller.ClientController;
import es.regueiro.easyrepair.login.SecurityManager;
import es.regueiro.easyrepair.model.client.Client;
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
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.client//ClientBrowser//EN",
autostore = false)
@TopComponent.Description(preferredID = "ClientBrowserTopComponent",
iconBase = "es/regueiro/easyrepair/ui/client/icons/client.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 0, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.client.ClientBrowserTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Clientes", position = 1),
    @ActionReference(path = "Toolbars/Clientes", position = 1)
})
@TopComponent.OpenActionRegistration(displayName = "#CTL_ClientBrowserAction",
preferredID = "ClientBrowserTopComponent")
@Messages({
    "CTL_ClientBrowserAction=Buscador de clientes",
    "CTL_ClientBrowserTopComponent=Buscador de clientes",
    "HINT_ClientBrowserTopComponent=Esta es una ventana del buscador de clientes"
})
public final class ClientBrowserTopComponent extends TopComponent implements TableModelListener, ListSelectionListener {

    private ClientTableModel model = new ClientTableModel();
    private InstanceContent content = new InstanceContent();
    private ClientController controller = Lookup.getDefault().lookup(ClientController.class);
    private Client client;
    private ClientLookup lookup = ClientLookup.getDefault();

    public ClientBrowserTopComponent() {
        initComponents();
        setName(Bundle.CTL_ClientBrowserTopComponent());
        setToolTipText(Bundle.HINT_ClientBrowserTopComponent());
        this.setFocusable(true);


        String[] inCBStrings = {
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("NAME"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SURNAME"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CLIENT_ID"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("NIF"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EMAIL_LABEL"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EMAIL_ADDRESS"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("PHONE_LABEL"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("PHONE_NUMBER"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_LABEL"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_STREET"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_CITY"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_POSTAL_CODE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_PROVINCE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_COUNTRY")};

        for (String s : inCBStrings) {
            inCombobox.addItem(s);
        }
        // Associate the table model
        clientTable.setModel(model);

        for (int i = 0; i < clientTable.getColumnCount(); i++) {
            clientTable.getColumnModel().getColumn(i).setCellRenderer(new ClientTableCellRenderer());
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
        clientTable.getSelectionModel().addListSelectionListener(this);

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
        clientTable = new javax.swing.JTable();
        topToolBar = new javax.swing.JToolBar();
        detailedViewButton = new javax.swing.JButton();
        newClientButton = new javax.swing.JButton();
        editClientButton = new javax.swing.JButton();
        deleteClientButton = new javax.swing.JButton();
        printSelectedClientButton = new javax.swing.JButton();
        printClientListButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        searchPanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        searchPanel.setLayout(new java.awt.GridBagLayout());

        searchEntryPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(searchLabel, org.openide.util.NbBundle.getMessage(ClientBrowserTopComponent.class, "ClientBrowserTopComponent.searchLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(inLabel, org.openide.util.NbBundle.getMessage(ClientBrowserTopComponent.class, "ClientBrowserTopComponent.inLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(inLabel, gridBagConstraints);

        listAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/group.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(listAllButton, org.openide.util.NbBundle.getMessage(ClientBrowserTopComponent.class, "ClientBrowserTopComponent.listAllButton.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(includeDisabledCheckBox, org.openide.util.NbBundle.getMessage(ClientBrowserTopComponent.class, "ClientBrowserTopComponent.includeDisabledCheckBox.text")); // NOI18N
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

        findButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/find32.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(findButton, org.openide.util.NbBundle.getMessage(ClientBrowserTopComponent.class, "ClientBrowserTopComponent.findButton.text")); // NOI18N
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

        clientTable.setAutoCreateRowSorter(true);
        clientTable.setDoubleBuffered(true);
        clientTable.setFillsViewportHeight(true);
        clientTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        clientTable.setRowHeight(25);
        clientTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        clientTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablePanel.setViewportView(clientTable);

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

        detailedViewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/client_detailed.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(detailedViewButton, org.openide.util.NbBundle.getMessage(ClientBrowserTopComponent.class, "ClientBrowserTopComponent.detailedViewButton.text")); // NOI18N
        detailedViewButton.setFocusable(false);
        detailedViewButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        detailedViewButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        detailedViewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailedViewButtonActionPerformed(evt);
            }
        });
        topToolBar.add(detailedViewButton);

        newClientButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(newClientButton, org.openide.util.NbBundle.getMessage(ClientBrowserTopComponent.class, "ClientBrowserTopComponent.newClientButton.text")); // NOI18N
        newClientButton.setFocusable(false);
        newClientButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        newClientButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        newClientButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        newClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newClientButtonActionPerformed(evt);
            }
        });
        topToolBar.add(newClientButton);

        editClientButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/client_edit.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(editClientButton, org.openide.util.NbBundle.getMessage(ClientBrowserTopComponent.class, "ClientBrowserTopComponent.editClientButton.text")); // NOI18N
        editClientButton.setEnabled(false);
        editClientButton.setFocusable(false);
        editClientButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        editClientButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        editClientButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        editClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editClientButtonActionPerformed(evt);
            }
        });
        topToolBar.add(editClientButton);

        deleteClientButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteClientButton, org.openide.util.NbBundle.getMessage(ClientBrowserTopComponent.class, "ClientBrowserTopComponent.deleteClientButton.text")); // NOI18N
        deleteClientButton.setEnabled(false);
        deleteClientButton.setFocusable(false);
        deleteClientButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deleteClientButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deleteClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteClientButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deleteClientButton);

        printSelectedClientButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printSelectedClientButton, org.openide.util.NbBundle.getMessage(ClientBrowserTopComponent.class, "ClientBrowserTopComponent.printSelectedClientButton.text")); // NOI18N
        printSelectedClientButton.setEnabled(false);
        printSelectedClientButton.setFocusable(false);
        printSelectedClientButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printSelectedClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSelectedClientButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printSelectedClientButton);

        printClientListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printClientListButton, org.openide.util.NbBundle.getMessage(ClientBrowserTopComponent.class, "ClientBrowserTopComponent.printClientListButton.text")); // NOI18N
        printClientListButton.setEnabled(false);
        printClientListButton.setFocusable(false);
        printClientListButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printClientListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printClientListButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printClientListButton);

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

    private void newClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newClientButtonActionPerformed
        lookup.clear();
        editClientButton.setEnabled(false);
        deleteClientButton.setEnabled(false);
        printSelectedClientButton.setEnabled(false);
        clientTable.clearSelection();
        openClientEditor();
    }//GEN-LAST:event_newClientButtonActionPerformed
    private void editClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editClientButtonActionPerformed
        openClientEditor();
    }//GEN-LAST:event_editClientButtonActionPerformed

    private void detailedViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailedViewButtonActionPerformed
        TopComponent window = WindowManager.getDefault().findTopComponent("ClientDetailedViewTopComponent");
        if (window == null) {
            window = new ClientDetailedViewTopComponent();
        }
        window.open();
        window.requestActive();

    }//GEN-LAST:event_detailedViewButtonActionPerformed

    private void deleteClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteClientButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CLIENT")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {

            ClientEditorTopComponent window = (ClientEditorTopComponent) WindowManager.getDefault().findTopComponent("ClientEditorTopComponent");
            if (window.getClient() != null && client.getId().equals(window.getClient().getId())) {
                window.forceClose();
            }


            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CLIENT")));
                    lookup.clear();
                }

                @Override
                protected Void doInBackground() {

                    List<Client> list = null;
                    try {
                        controller.setClient(client);
                        controller.deleteClient();
                        model.removeRow(clientTable.convertRowIndexToModel(clientTable.getSelectedRow()));
                    } catch (Exception e) {
                        NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                        Object retval = DialogDisplayer.getDefault().notify(d);
                    }


                    client = null;
                    return null;
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_deleteClientButtonActionPerformed

    private void printClientListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printClientListButtonActionPerformed

        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printClientList(model.getClientList());
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printClientListButtonActionPerformed

    private void printSelectedClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSelectedClientButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printClient(client);
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printSelectedClientButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable clientTable;
    private javax.swing.JButton deleteClientButton;
    private javax.swing.JButton detailedViewButton;
    private javax.swing.JButton editClientButton;
    private javax.swing.JButton findButton;
    private javax.swing.JComboBox inCombobox;
    private javax.swing.JLabel inLabel;
    private javax.swing.JCheckBox includeDisabledCheckBox;
    private javax.swing.JButton listAllButton;
    private javax.swing.JButton newClientButton;
    private javax.swing.JButton printClientListButton;
    private javax.swing.JButton printSelectedClientButton;
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
        return new HelpCtx("es.regueiro.easyrepair.ui.client.browser");
    }

    @Override
    public void componentOpened() {
//        model.clear();
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

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            if (clientTable.getSelectedRow() > -1) {
                client = model.getRow(clientTable.convertRowIndexToModel(clientTable.getSelectedRow()));
                lookup.setClient(client);
                editClientButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
                deleteClientButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
                printSelectedClientButton.setEnabled(true);
            } else {
                editClientButton.setEnabled(false);
                deleteClientButton.setEnabled(false);
                printSelectedClientButton.setEnabled(false);
            }
        }
    }

    private void checkPermissions() {
        if (SecurityManager.getDefault().isUserLoggedIn()) {
            newClientButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
        } else {
            this.close();
        }
    }

    private void openClientEditor() {
        TopComponent window = WindowManager.getDefault().findTopComponent("ClientEditorTopComponent");
        if (window == null) {
            window = new ClientEditorTopComponent();
        } else {
            window.close();
        }
        window.open();
        window.requestActive();
    }

    private void doSearch() {

        printClientListButton.setEnabled(false);

        final SwingWorker<List<Client>, Void> worker = new SwingWorker<List<Client>, Void>() {
            private int column;
            ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SEARCHING"));

            @Override
            protected void done() {
                try {
                    List<Client> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printClientListButton.setEnabled(true);
                    }

                    if (column != 1) {
                        clientTable.getRowSorter().toggleSortOrder(column - 1);
                    } else {
                        clientTable.getRowSorter().toggleSortOrder(2);
                    }

                    clientTable.getRowSorter().toggleSortOrder(column);
                    p.finish();
                } catch (InterruptedException ignore) {
                    System.out.println(ignore.getCause());
                } catch (ExecutionException ignore) {
                    System.out.println(ignore.getCause());
                }
            }

            @Override
            protected List<Client> doInBackground() {
                int selected = inCombobox.getSelectedIndex();
                String toSearch = searchEntryField.getText();
                List<Client> list = null;
                column = 0;
                if (!toSearch.isEmpty()) {
                    p.setDisplayName(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SEARCHING_FOR_X"), new Object[]{toSearch}));
                }
                p.start();
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
                            list = controller.searchByClientId(toSearch);
                            column = 1;
                            break;
                        case 3:
                            list = controller.searchByNif(toSearch);
                            column = 4;
                            break;
                        case 4:
                            list = controller.searchByEmailLabel(toSearch);
                            column = 1;
                            break;
                        case 5:
                            list = controller.searchByEmailAddress(toSearch);
                            column = 1;
                            break;
                        case 6:
                            list = controller.searchByPhoneLabel(toSearch);
                            column = 1;
                            break;
                        case 7:
                            list = controller.searchByPhoneNumber(toSearch);
                            column = 1;
                            break;
                        case 8:
                            list = controller.searchByAddressLabel(toSearch);
                            column = 1;
                            break;
                        case 9:
                            list = controller.searchByAddressStreet(toSearch);
                            column = 1;
                            break;
                        case 10:
                            list = controller.searchByAddressCity(toSearch);
                            column = 1;
                            break;
                        case 11:
                            list = controller.searchByAddressPostalCode(toSearch);
                            column = 1;
                            break;
                        case 12:
                            list = controller.searchByAddressProvince(toSearch);
                            column = 1;
                            break;
                        case 13:
                            list = controller.searchByAddressCountry(toSearch);
                            column = 1;
                            break;
                        default:
                            //this should never happen
                            list = null;
                    }
                } catch (Exception e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ERROR_SEARCH"), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    p.finish();
                }
                if (!includeDisabledCheckBox.isSelected()) {
                    Iterator<Client> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Client e = iterator.next();
                        if (!e.getEnabled()) {
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

        printClientListButton.setEnabled(false);

        final ProgressHandle p = ProgressHandleFactory.createHandle(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("LISTING_ALL_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CLIENTS")));

        SwingWorker<List<Client>, Void> worker = new SwingWorker<List<Client>, Void>() {
            @Override
            protected void done() {
                try {
                    List<Client> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printClientListButton.setEnabled(true);
                    }

                    clientTable.getRowSorter().toggleSortOrder(2);
                    clientTable.getRowSorter().toggleSortOrder(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                p.finish();
            }

            @Override
            protected List<Client> doInBackground() {
                List<Client> list = null;
                try {
                    list = controller.listAll();
                } catch (Exception e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    p.finish();
                }

                if (!includeDisabledCheckBox.isSelected()) {
                    Iterator<Client> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Client e = iterator.next();
                        if (!e.getEnabled()) {
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

    public void deleteClient(Client emp) {
        if (client != null && emp.getId().equals(client.getId())) {
            client = null;
            StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CLIENT")));
            lookup.clear();

        }

        model.removeClient(emp);

    }
}