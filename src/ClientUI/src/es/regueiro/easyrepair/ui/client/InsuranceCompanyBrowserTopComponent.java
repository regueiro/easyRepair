package es.regueiro.easyrepair.ui.client;

import es.regueiro.easyrepair.login.SecurityManager;
import es.regueiro.easyrepair.api.client.controller.InsuranceCompanyController;
import es.regueiro.easyrepair.login.LoginLookup;
import es.regueiro.easyrepair.model.client.InsuranceCompany;
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
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.client//InsuranceCompanyBrowser//EN",
autostore = false)
@TopComponent.Description(preferredID = "InsuranceCompanyBrowserTopComponent",
iconBase = "es/regueiro/easyrepair/ui/client/icons/insurance.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 4, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.client.InsuranceCompanyBrowserTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Clientes", position = 5),
    @ActionReference(path = "Toolbars/Clientes", position = 5)
})
@TopComponent.OpenActionRegistration(displayName = "#CTL_InsuranceCompanyBrowserAction",
preferredID = "InsuranceCompanyBrowserTopComponent")
@Messages({
    "CTL_InsuranceCompanyBrowserAction=Buscador de aseguradoras",
    "CTL_InsuranceCompanyBrowserTopComponent=Buscador de aseguradoras",
    "HINT_InsuranceCompanyBrowserTopComponent=Esta es una ventana del buscador de aseguradoras"
})
public final class InsuranceCompanyBrowserTopComponent extends TopComponent implements TableModelListener, ListSelectionListener {

    private InsuranceCompanyTableModel model = new InsuranceCompanyTableModel();
    private InstanceContent content = new InstanceContent();
    private InsuranceCompanyController controller = Lookup.getDefault().lookup(InsuranceCompanyController.class);
    private InsuranceCompany insuranceCompany;
    private InsuranceCompanyLookup lookup = InsuranceCompanyLookup.getDefault();

    public InsuranceCompanyBrowserTopComponent() {
        initComponents();
        setName(Bundle.CTL_InsuranceCompanyBrowserTopComponent());
        setToolTipText(Bundle.HINT_InsuranceCompanyBrowserTopComponent());
        this.setFocusable(true);

        String[] inCBStrings = {java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("NAME"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("NIF"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("WEB"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EMAIL_LABEL"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EMAIL_ADDRESS"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("PHONE_LABEL"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("PHONE_NUMBER"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_LABEL"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_STREET"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_CITY"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_POSTAL_CODE"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_PROVINCE"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_COUNTRY")};

        for (String s : inCBStrings) {
            inCombobox.addItem(s);
        }


        // Associate the table model
        insuranceCompanyTable.setModel(model);

        for (int i = 0; i < insuranceCompanyTable.getColumnCount(); i++) {
            insuranceCompanyTable.getColumnModel().getColumn(i).setCellRenderer(new MyTableCellRenderer());
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
        insuranceCompanyTable.getSelectionModel().addListSelectionListener(this);

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
        insuranceCompanyTable = new javax.swing.JTable();
        topToolBar = new javax.swing.JToolBar();
        detailedViewButton = new javax.swing.JButton();
        newInsuranceCompanyButton = new javax.swing.JButton();
        editInsuranceCompanyButton = new javax.swing.JButton();
        deleteInsuranceCompanyButton = new javax.swing.JButton();
        printSelectedInsuranceCompanyButton = new javax.swing.JButton();
        printInsuranceCompanyListButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        searchPanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        searchPanel.setLayout(new java.awt.GridBagLayout());

        searchEntryPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(searchLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyBrowserTopComponent.class, "InsuranceCompanyBrowserTopComponent.searchLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(inLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyBrowserTopComponent.class, "InsuranceCompanyBrowserTopComponent.inLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(inLabel, gridBagConstraints);

        listAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/group.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(listAllButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyBrowserTopComponent.class, "InsuranceCompanyBrowserTopComponent.listAllButton.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(includeDisabledCheckBox, org.openide.util.NbBundle.getMessage(InsuranceCompanyBrowserTopComponent.class, "InsuranceCompanyBrowserTopComponent.includeDisabledCheckBox.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
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
        org.openide.awt.Mnemonics.setLocalizedText(findButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyBrowserTopComponent.class, "InsuranceCompanyBrowserTopComponent.findButton.text")); // NOI18N
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

        insuranceCompanyTable.setAutoCreateRowSorter(true);
        insuranceCompanyTable.setDoubleBuffered(true);
        insuranceCompanyTable.setFillsViewportHeight(true);
        insuranceCompanyTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        insuranceCompanyTable.setRowHeight(25);
        insuranceCompanyTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        insuranceCompanyTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablePanel.setViewportView(insuranceCompanyTable);

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

        detailedViewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/insurance_detailed.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(detailedViewButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyBrowserTopComponent.class, "InsuranceCompanyBrowserTopComponent.detailedViewButton.text")); // NOI18N
        detailedViewButton.setFocusable(false);
        detailedViewButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        detailedViewButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        detailedViewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailedViewButtonActionPerformed(evt);
            }
        });
        topToolBar.add(detailedViewButton);

        newInsuranceCompanyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(newInsuranceCompanyButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyBrowserTopComponent.class, "InsuranceCompanyBrowserTopComponent.newInsuranceCompanyButton.text")); // NOI18N
        newInsuranceCompanyButton.setFocusable(false);
        newInsuranceCompanyButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        newInsuranceCompanyButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        newInsuranceCompanyButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        newInsuranceCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newInsuranceCompanyButtonActionPerformed(evt);
            }
        });
        topToolBar.add(newInsuranceCompanyButton);

        editInsuranceCompanyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/insurance_edit.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(editInsuranceCompanyButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyBrowserTopComponent.class, "InsuranceCompanyBrowserTopComponent.editInsuranceCompanyButton.text")); // NOI18N
        editInsuranceCompanyButton.setEnabled(false);
        editInsuranceCompanyButton.setFocusable(false);
        editInsuranceCompanyButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        editInsuranceCompanyButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        editInsuranceCompanyButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        editInsuranceCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editInsuranceCompanyButtonActionPerformed(evt);
            }
        });
        topToolBar.add(editInsuranceCompanyButton);

        deleteInsuranceCompanyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteInsuranceCompanyButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyBrowserTopComponent.class, "InsuranceCompanyBrowserTopComponent.deleteInsuranceCompanyButton.text")); // NOI18N
        deleteInsuranceCompanyButton.setEnabled(false);
        deleteInsuranceCompanyButton.setFocusable(false);
        deleteInsuranceCompanyButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deleteInsuranceCompanyButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deleteInsuranceCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteInsuranceCompanyButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deleteInsuranceCompanyButton);

        printSelectedInsuranceCompanyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printSelectedInsuranceCompanyButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyBrowserTopComponent.class, "InsuranceCompanyBrowserTopComponent.printSelectedInsuranceCompanyButton.text")); // NOI18N
        printSelectedInsuranceCompanyButton.setEnabled(false);
        printSelectedInsuranceCompanyButton.setFocusable(false);
        printSelectedInsuranceCompanyButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printSelectedInsuranceCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSelectedInsuranceCompanyButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printSelectedInsuranceCompanyButton);

        printInsuranceCompanyListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printInsuranceCompanyListButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyBrowserTopComponent.class, "InsuranceCompanyBrowserTopComponent.printInsuranceCompanyListButton.text")); // NOI18N
        printInsuranceCompanyListButton.setEnabled(false);
        printInsuranceCompanyListButton.setFocusable(false);
        printInsuranceCompanyListButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printInsuranceCompanyListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printInsuranceCompanyListButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printInsuranceCompanyListButton);

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

    private void newInsuranceCompanyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newInsuranceCompanyButtonActionPerformed
        lookup.clear();
        editInsuranceCompanyButton.setEnabled(false);
        deleteInsuranceCompanyButton.setEnabled(false);
        insuranceCompanyTable.clearSelection();
        openInsuranceCompanyEditor();
    }//GEN-LAST:event_newInsuranceCompanyButtonActionPerformed
    private void editInsuranceCompanyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editInsuranceCompanyButtonActionPerformed
        openInsuranceCompanyEditor();
    }//GEN-LAST:event_editInsuranceCompanyButtonActionPerformed

    private void detailedViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailedViewButtonActionPerformed
        TopComponent window = WindowManager.getDefault().findTopComponent("InsuranceCompanyDetailedViewTopComponent");
        if (window == null) {
            window = new InsuranceCompanyDetailedViewTopComponent();
        }
        window.open();
        window.requestActive();

    }//GEN-LAST:event_detailedViewButtonActionPerformed

    private void deleteInsuranceCompanyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteInsuranceCompanyButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCECOMPANY")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {

            InsuranceCompanyEditorTopComponent window = (InsuranceCompanyEditorTopComponent) WindowManager.getDefault().findTopComponent("InsuranceCompanyEditorTopComponent");
            if (window.getInsuranceCompany() != null && insuranceCompany.getId().equals(window.getInsuranceCompany().getId())) {
                window.forceClose();
            }


            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCECOMPANY")));
                    lookup.clear();
                }

                @Override
                protected Void doInBackground() {
                    List<InsuranceCompany> list = null;

                    try {
                        controller.setInsuranceCompany(insuranceCompany);
                        controller.deleteInsuranceCompany();
                        model.removeRow(insuranceCompanyTable.convertRowIndexToModel(insuranceCompanyTable.getSelectedRow()));
                    } catch (Exception e) {
                        NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                        Object retval = DialogDisplayer.getDefault().notify(d);
                    }

                    insuranceCompany = null;
                    return null;
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_deleteInsuranceCompanyButtonActionPerformed

    private void printInsuranceCompanyListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printInsuranceCompanyListButtonActionPerformed


        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printInsuranceCompanyList(model.getInsuranceCompanyList());
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printInsuranceCompanyListButtonActionPerformed

    private void printSelectedInsuranceCompanyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSelectedInsuranceCompanyButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printInsuranceCompany(insuranceCompany);
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printSelectedInsuranceCompanyButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteInsuranceCompanyButton;
    private javax.swing.JButton detailedViewButton;
    private javax.swing.JButton editInsuranceCompanyButton;
    private javax.swing.JButton findButton;
    private javax.swing.JComboBox inCombobox;
    private javax.swing.JLabel inLabel;
    private javax.swing.JCheckBox includeDisabledCheckBox;
    private javax.swing.JTable insuranceCompanyTable;
    private javax.swing.JButton listAllButton;
    private javax.swing.JButton newInsuranceCompanyButton;
    private javax.swing.JButton printInsuranceCompanyListButton;
    private javax.swing.JButton printSelectedInsuranceCompanyButton;
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
        return new HelpCtx("es.regueiro.easyrepair.ui.client.insuranceCompanyBrowser");
    }

    @Override
    public void componentOpened() {
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
//        System.out.println("tablechanged");
        //int row = e.getFirstRow();
        //int column = e.getColumn();
        //InsuranceCompanyListModel model = (InsuranceCompanyListModel) e.getSource();
        //Object data = model.getValueAt(row, column);
    }

    private void checkPermissions() {
        if (SecurityManager.getDefault().isUserLoggedIn()) {
            newInsuranceCompanyButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.INSURANCE_COMPANY_EDIT));
        } else {
            this.close();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            if (insuranceCompanyTable.getSelectedRow() > -1) {
                insuranceCompany = model.getRow(insuranceCompanyTable.convertRowIndexToModel(insuranceCompanyTable.getSelectedRow()));
                //       InsuranceCompany emp = model.getRow(insuranceCompanyTable.getSelectedRow());

//                Convertor<InsuranceCompany, Void> conv = null;
//                content.set(Collections.singleton(insuranceCompany), null);
                editInsuranceCompanyButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.INSURANCE_COMPANY_EDIT));
                deleteInsuranceCompanyButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.INSURANCE_COMPANY_EDIT));
                printSelectedInsuranceCompanyButton.setEnabled(true);
                lookup.setInsuranceCompany(insuranceCompany);
            } else {
                editInsuranceCompanyButton.setEnabled(false);
                deleteInsuranceCompanyButton.setEnabled(false);
                printSelectedInsuranceCompanyButton.setEnabled(false);
            }
        }
    }

    private void openInsuranceCompanyEditor() {
        TopComponent window = WindowManager.getDefault().findTopComponent("InsuranceCompanyEditorTopComponent");
        if (window == null) {
            window = new InsuranceCompanyEditorTopComponent();
        } else {
            window.close();
        }
        window.open();
        window.requestActive();
    }

    private void doSearch() {

        printInsuranceCompanyListButton.setEnabled(false);

        final SwingWorker<List<InsuranceCompany>, Void> worker = new SwingWorker<List<InsuranceCompany>, Void>() {
            private int column;
            ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SEARCHING"));

            @Override
            protected void done() {
                try {
                    List<InsuranceCompany> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printInsuranceCompanyListButton.setEnabled(true);
                    }

                    if (column != 1) {
                        insuranceCompanyTable.getRowSorter().toggleSortOrder(column - 1);
                    } else {
                        insuranceCompanyTable.getRowSorter().toggleSortOrder(2);
                    }

                    insuranceCompanyTable.getRowSorter().toggleSortOrder(column);
                    p.finish();
                } catch (InterruptedException ignore) {
                    System.out.println(ignore.getCause());
                } catch (ExecutionException ignore) {
                    System.out.println(ignore.getCause());
                }
            }

            @Override
            protected List<InsuranceCompany> doInBackground() {
                int selected = inCombobox.getSelectedIndex();
                String toSearch = searchEntryField.getText();
                List<InsuranceCompany> list = null;
                column = 0;
                if (!toSearch.isEmpty()) {
                    p.setDisplayName(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SEARCHING_FOR_X"), new Object[]{toSearch}));
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
                            list = controller.searchByWeb(toSearch);
                            column = 3;
                            break;
                        case 3:
                            list = controller.searchByEmailLabel(toSearch);
                            column = 1;
                            break;
                        case 4:
                            list = controller.searchByEmailAddress(toSearch);
                            column = 1;
                            break;
                        case 5:
                            list = controller.searchByPhoneLabel(toSearch);
                            column = 1;
                            break;
                        case 6:
                            list = controller.searchByPhoneNumber(toSearch);
                            column = 1;
                            break;
                        case 7:
                            list = controller.searchByAddressLabel(toSearch);
                            column = 1;
                            break;
                        case 8:
                            list = controller.searchByAddressStreet(toSearch);
                            column = 1;
                            break;
                        case 9:
                            list = controller.searchByAddressCity(toSearch);
                            column = 1;
                            break;
                        case 10:
                            list = controller.searchByAddressPostalCode(toSearch);
                            column = 1;
                            break;
                        case 11:
                            list = controller.searchByAddressProvince(toSearch);
                            column = 1;
                            break;
                        case 12:
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
                    Iterator<InsuranceCompany> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        InsuranceCompany e = iterator.next();
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

        printInsuranceCompanyListButton.setEnabled(false);

        final ProgressHandle p = ProgressHandleFactory.createHandle(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("LISTING_ALL_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCECOMPANIES")));

        SwingWorker<List<InsuranceCompany>, Void> worker = new SwingWorker<List<InsuranceCompany>, Void>() {
            @Override
            protected void done() {
                try {
                    List<InsuranceCompany> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printInsuranceCompanyListButton.setEnabled(true);
                    }

                    insuranceCompanyTable.getRowSorter().toggleSortOrder(2);
                    insuranceCompanyTable.getRowSorter().toggleSortOrder(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                p.finish();
            }

            @Override
            protected List<InsuranceCompany> doInBackground() {
                List<InsuranceCompany> list = null;
                try {
                    list = controller.listAll();
                } catch (Exception e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    p.finish();
                }

                if (!includeDisabledCheckBox.isSelected()) {
                    Iterator<InsuranceCompany> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        InsuranceCompany e = iterator.next();
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

    public void deleteInsuranceCompany(InsuranceCompany emp) {
        if (insuranceCompany != null && emp.getId().equals(insuranceCompany.getId())) {
            insuranceCompany = null;
            StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCECOMPANY")));
            lookup.clear();

        }

        model.removeInsuranceCompany(emp);

    }

    public class MyTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component comp = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            int rowN = table.convertRowIndexToModel(row);
            String s = table.getModel().getValueAt(rowN, 4).toString();

            if (s.equalsIgnoreCase("yes")) {
                comp.setForeground(Color.LIGHT_GRAY);
            } else {
                comp.setForeground(null);
            }

            return (comp);

        }
    }
}