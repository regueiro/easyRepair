package es.regueiro.easyrepair.ui.repair;

import es.regueiro.easyrepair.login.SecurityManager;
import es.regueiro.easyrepair.api.repair.controller.EstimateController;
import es.regueiro.easyrepair.model.repair.Estimate;
import es.regueiro.easyrepair.model.user.Privilege;
import es.regueiro.easyrepair.reports.ReportPrinter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import org.joda.time.DateMidnight;
import org.joda.time.LocalDate;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
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
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.repair//EstimateBrowser//EN",
autostore = false)
@TopComponent.Description(preferredID = "EstimateBrowserTopComponent",
iconBase = "es/regueiro/easyrepair/ui/repair/icons/estimate.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 8, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.repair.EstimateBrowserTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Reparaciones", position = 1),
    @ActionReference(path = "Toolbars/Reparaciones", position = 1)
})
@TopComponent.OpenActionRegistration(displayName = "#CTL_EstimateBrowserAction",
preferredID = "EstimateBrowserTopComponent")
@Messages({
    "CTL_EstimateBrowserAction=Buscador de presupuestos",
    "CTL_EstimateBrowserTopComponent=Buscador de presupuestos",
    "HINT_EstimateBrowserTopComponent=Esta es una ventana del buscador de presupuestos"
})
public final class EstimateBrowserTopComponent extends TopComponent implements TableModelListener, ListSelectionListener {

    private EstimateTableModel model = new EstimateTableModel();
    private InstanceContent content = new InstanceContent();
    private EstimateController controller = Lookup.getDefault().lookup(EstimateController.class);
    private Estimate estimate;
    private EstimateLookup lookup = EstimateLookup.getDefault();
    private RepairOrderLookup orderLookup = RepairOrderLookup.getDefault();
    private final JDatePickerImpl dateSearchFromDatePicker;
    private final JDatePickerImpl dateSearchToDatePicker;

    public EstimateBrowserTopComponent() {
        initComponents();
        setName(Bundle.CTL_EstimateBrowserTopComponent());
        setToolTipText(Bundle.HINT_EstimateBrowserTopComponent());
        this.setFocusable(true);

        String[] inCBStrings = {java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ESTIMATENUMBER"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ESTIMATEDATE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ACCEPTEDDATE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("STATUS"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("DISCOUNT"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("RESPONSIBLE")};




        for (String s : inCBStrings) {
            inCombobox.addItem(s);
        }

        // Associate the table model
        estimateTable.setModel(model);

        for (int i = 0; i < estimateTable.getColumnCount(); i++) {
            estimateTable.getColumnModel().getColumn(i).setCellRenderer(new MyTableCellRenderer());
        }


        setupInComboBox();
        textSearchEntryField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                doSearch();
            }
        });


        DateModel dateSearchFromModel = JDateComponentFactory.createDateModel(new DateMidnight());
        DateModel dateSearchToModel = JDateComponentFactory.createDateModel(new DateMidnight());

        dateSearchFromDatePicker = (JDatePickerImpl) JDateComponentFactory.createJDatePicker(dateSearchFromModel);
        dateSearchToDatePicker = (JDatePickerImpl) JDateComponentFactory.createJDatePicker(dateSearchToModel);

        dateSearchFromDatePicker.getJFormattedTextField().setMargin(new Insets(2, 2, 2, 2));
        dateSearchToDatePicker.getJFormattedTextField().setMargin(new Insets(2, 2, 2, 2));

        dateSearchFromPanel.add(dateSearchFromDatePicker, BorderLayout.NORTH);
        dateSearchToPanel.add(dateSearchToDatePicker, BorderLayout.NORTH);

        dateSearchPanel.setVisible(false);

        // Activate Copy/Paste
        ActionMap actionMap = getActionMap();
        actionMap.put(DefaultEditorKit.copyAction, new DefaultEditorKit.CopyAction());
        actionMap.put(DefaultEditorKit.cutAction, new DefaultEditorKit.CutAction());
        actionMap.put(DefaultEditorKit.pasteAction, new DefaultEditorKit.PasteAction());
        content.add(actionMap);

        // Listen to the changes on the table
        model.addTableModelListener(this);
        estimateTable.getSelectionModel().addListSelectionListener(this);

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
        inLabel = new javax.swing.JLabel();
        listAllButton = new javax.swing.JButton();
        inCombobox = new javax.swing.JComboBox();
        textSearchPanel = new javax.swing.JPanel();
        textSearchEntryField = new javax.swing.JTextField();
        dateSearchPanel = new javax.swing.JPanel();
        dateSearchFromLabel = new javax.swing.JLabel();
        dateSearchToLabel = new javax.swing.JLabel();
        dateSearchToPanel = new javax.swing.JPanel();
        dateSearchFromPanel = new javax.swing.JPanel();
        includeDisabledCheckBox = new javax.swing.JCheckBox();
        searchButtonPanel = new javax.swing.JPanel();
        findButton = new javax.swing.JButton();
        tablePanel = new javax.swing.JScrollPane();
        estimateTable = new javax.swing.JTable();
        topToolBar = new javax.swing.JToolBar();
        detailedViewButton = new javax.swing.JButton();
        editEstimateButton = new javax.swing.JButton();
        printSelectedEstimateButton = new javax.swing.JButton();
        printEstimateListButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        searchPanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        searchPanel.setLayout(new java.awt.GridBagLayout());

        searchEntryPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(searchLabel, org.openide.util.NbBundle.getMessage(EstimateBrowserTopComponent.class, "EstimateBrowserTopComponent.searchLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(searchLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(inLabel, org.openide.util.NbBundle.getMessage(EstimateBrowserTopComponent.class, "EstimateBrowserTopComponent.inLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(inLabel, gridBagConstraints);

        listAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/group.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(listAllButton, org.openide.util.NbBundle.getMessage(EstimateBrowserTopComponent.class, "EstimateBrowserTopComponent.listAllButton.text")); // NOI18N
        listAllButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        listAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listAllButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
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

        textSearchPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        textSearchPanel.add(textSearchEntryField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        searchEntryPanel.add(textSearchPanel, gridBagConstraints);

        dateSearchPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(dateSearchFromLabel, org.openide.util.NbBundle.getMessage(EstimateBrowserTopComponent.class, "EstimateBrowserTopComponent.dateSearchFromLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        dateSearchPanel.add(dateSearchFromLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(dateSearchToLabel, org.openide.util.NbBundle.getMessage(EstimateBrowserTopComponent.class, "EstimateBrowserTopComponent.dateSearchToLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        dateSearchPanel.add(dateSearchToLabel, gridBagConstraints);

        dateSearchToPanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        dateSearchPanel.add(dateSearchToPanel, gridBagConstraints);

        dateSearchFromPanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        dateSearchPanel.add(dateSearchFromPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        searchEntryPanel.add(dateSearchPanel, gridBagConstraints);

        includeDisabledCheckBox.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(includeDisabledCheckBox, org.openide.util.NbBundle.getMessage(EstimateBrowserTopComponent.class, "EstimateBrowserTopComponent.includeDisabledCheckBox.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
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
        org.openide.awt.Mnemonics.setLocalizedText(findButton, org.openide.util.NbBundle.getMessage(EstimateBrowserTopComponent.class, "EstimateBrowserTopComponent.findButton.text")); // NOI18N
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

        estimateTable.setAutoCreateRowSorter(true);
        estimateTable.setDoubleBuffered(true);
        estimateTable.setFillsViewportHeight(true);
        estimateTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        estimateTable.setRowHeight(25);
        estimateTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        estimateTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablePanel.setViewportView(estimateTable);

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

        detailedViewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/estimate_detailed.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(detailedViewButton, org.openide.util.NbBundle.getMessage(EstimateBrowserTopComponent.class, "EstimateBrowserTopComponent.detailedViewButton.text")); // NOI18N
        detailedViewButton.setFocusable(false);
        detailedViewButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        detailedViewButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        detailedViewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailedViewButtonActionPerformed(evt);
            }
        });
        topToolBar.add(detailedViewButton);

        editEstimateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/book_edit.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(editEstimateButton, org.openide.util.NbBundle.getMessage(EstimateBrowserTopComponent.class, "EstimateBrowserTopComponent.editEstimateButton.text")); // NOI18N
        editEstimateButton.setEnabled(false);
        editEstimateButton.setFocusable(false);
        editEstimateButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        editEstimateButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        editEstimateButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        editEstimateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editEstimateButtonActionPerformed(evt);
            }
        });
        topToolBar.add(editEstimateButton);

        printSelectedEstimateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printSelectedEstimateButton, org.openide.util.NbBundle.getMessage(EstimateBrowserTopComponent.class, "EstimateBrowserTopComponent.printSelectedEstimateButton.text")); // NOI18N
        printSelectedEstimateButton.setEnabled(false);
        printSelectedEstimateButton.setFocusable(false);
        printSelectedEstimateButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printSelectedEstimateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSelectedEstimateButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printSelectedEstimateButton);

        printEstimateListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printEstimateListButton, org.openide.util.NbBundle.getMessage(EstimateBrowserTopComponent.class, "EstimateBrowserTopComponent.printEstimateListButton.text")); // NOI18N
        printEstimateListButton.setEnabled(false);
        printEstimateListButton.setFocusable(false);
        printEstimateListButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printEstimateListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printEstimateListButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printEstimateListButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        add(topToolBar, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void editEstimateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editEstimateButtonActionPerformed
        openEstimateView();
    }//GEN-LAST:event_editEstimateButtonActionPerformed

    private void detailedViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailedViewButtonActionPerformed
        TopComponent window = WindowManager.getDefault().findTopComponent("RepairOrderDetailedViewTopComponent");
        if (window == null) {
            window = new RepairOrderDetailedViewTopComponent();
        }
        window.open();
        window.requestActive();
        ((RepairOrderDetailedViewTopComponent) window).viewEstimate(estimate);
    }//GEN-LAST:event_detailedViewButtonActionPerformed

    private void printEstimateListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printEstimateListButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printEstimateList(model.getEstimateList());
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printEstimateListButtonActionPerformed

    private void printSelectedEstimateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSelectedEstimateButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printEstimate(estimate);
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printSelectedEstimateButtonActionPerformed

    private void listAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listAllButtonActionPerformed
        doListAll();
    }//GEN-LAST:event_listAllButtonActionPerformed

    private void findButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findButtonActionPerformed
        doSearch();
    }//GEN-LAST:event_findButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dateSearchFromLabel;
    private javax.swing.JPanel dateSearchFromPanel;
    private javax.swing.JPanel dateSearchPanel;
    private javax.swing.JLabel dateSearchToLabel;
    private javax.swing.JPanel dateSearchToPanel;
    private javax.swing.JButton detailedViewButton;
    private javax.swing.JButton editEstimateButton;
    private javax.swing.JTable estimateTable;
    private javax.swing.JButton findButton;
    private javax.swing.JComboBox inCombobox;
    private javax.swing.JLabel inLabel;
    private javax.swing.JCheckBox includeDisabledCheckBox;
    private javax.swing.JButton listAllButton;
    private javax.swing.JButton printEstimateListButton;
    private javax.swing.JButton printSelectedEstimateButton;
    private javax.swing.JPanel searchButtonPanel;
    private javax.swing.JPanel searchEntryPanel;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JScrollPane tablePanel;
    private javax.swing.JTextField textSearchEntryField;
    private javax.swing.JPanel textSearchPanel;
    private javax.swing.JToolBar topToolBar;
    // End of variables declaration//GEN-END:variables

    @Override
    public HelpCtx getHelpCtx() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return new HelpCtx("es.regueiro.easyrepair.ui.estimate.browser");
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
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
//            editEstimateButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ESTIMATE_EDIT));
        } else {
            this.close();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            if (estimateTable.getSelectedRow() > -1) {
                estimate = model.getRow(estimateTable.convertRowIndexToModel(estimateTable.getSelectedRow()));
                editEstimateButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ESTIMATE_EDIT));
                printSelectedEstimateButton.setEnabled(true);
                lookup.setEstimate(estimate);
                orderLookup.setRepairOrder(estimate.getOrder());
            } else {
                editEstimateButton.setEnabled(false);
                printSelectedEstimateButton.setEnabled(false);
            }
        }
    }

    private void openEstimateView() {
        orderLookup.setRepairOrder(estimate.getOrder());
        TopComponent window = WindowManager.getDefault().findTopComponent("RepairOrderEditorTopComponent");
        if (window == null) {
            window = new RepairOrderEditorTopComponent();
        } else {
            window.close();
        }
        window.open();
        window.requestActive();
        ((RepairOrderEditorTopComponent) window).viewEstimate(estimate);
    }

    private void doSearch() {

        printEstimateListButton.setEnabled(false);

        final SwingWorker<List<Estimate>, Void> worker = new SwingWorker<List<Estimate>, Void>() {
            private int column;
            ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SEARCHING"));

            @Override
            protected void done() {
                try {
                    List<Estimate> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printEstimateListButton.setEnabled(true);
                    }

                    if (column != 1) {
                        estimateTable.getRowSorter().toggleSortOrder(column - 1);
                    } else {
                        estimateTable.getRowSorter().toggleSortOrder(2);
                    }

                    estimateTable.getRowSorter().toggleSortOrder(column);
                    p.finish();
                } catch (InterruptedException ignore) {
                    System.out.println(ignore.getCause());
                } catch (ExecutionException ignore) {
                    System.out.println(ignore.getCause());
                }
            }

            @Override
            protected List<Estimate> doInBackground() {
                int selected = inCombobox.getSelectedIndex();
                String toSearch = textSearchEntryField.getText();
                List<Estimate> list;
                column = 0;
                String fromDate = "";
                String toDate = "";
                if (!toSearch.isEmpty()) {
                    p.setDisplayName(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SEARCHING_FOR_X"), new Object[]{toSearch}));
                }
                p.start();
                switch (selected) {
                    case 0:
                        list = controller.searchByEstimateNumber(toSearch);
                        column = 1;
                        break;
                    case 1:
                        fromDate = "";
                        toDate = "";
                        if (dateSearchFromDatePicker.getModel().isSelected()) {
                            fromDate = (new LocalDate(dateSearchFromDatePicker.getModel().getValue())).toString();
                        }
                        if (dateSearchToDatePicker.getModel().isSelected()) {
                            toDate = (new LocalDate(dateSearchToDatePicker.getModel().getValue())).toString();
                        }
                        list = controller.searchByEstimateDate(fromDate, toDate);
                        column = 2;
                        break;
                    case 2:
                        fromDate = "";
                        toDate = "";
                        if (dateSearchFromDatePicker.getModel().isSelected()) {
                            fromDate = (new LocalDate(dateSearchFromDatePicker.getModel().getValue())).toString();
                        }
                        if (dateSearchToDatePicker.getModel().isSelected()) {
                            toDate = (new LocalDate(dateSearchToDatePicker.getModel().getValue())).toString();
                        }
                        list = controller.searchByAcceptedDate(fromDate, toDate);
                        column = 3;
                        break;
                    case 3:
                        list = controller.searchByStatus(toSearch);
                        column = 4;
                        break;
                    case 4:
                        list = controller.searchByDiscount(toSearch);
                        column = 5;
                        break;
                    case 5:
                        list = controller.searchByResponsible(toSearch);
                        column = 6;
                        break;
                    default:
                        //this should never happen
                        list = null;
                }

                if (!includeDisabledCheckBox.isSelected()) {
                    Iterator<Estimate> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Estimate v = iterator.next();
                        if (!v.getEnabled()) {
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

        printEstimateListButton.setEnabled(false);

        final ProgressHandle p = ProgressHandleFactory.createHandle(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("LISTING_ALL_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ESTIMATES")));

        SwingWorker<List<Estimate>, Void> worker = new SwingWorker<List<Estimate>, Void>() {
            @Override
            protected void done() {
                try {
                    List<Estimate> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printEstimateListButton.setEnabled(true);
                    }

                    estimateTable.getRowSorter().toggleSortOrder(2);
                    estimateTable.getRowSorter().toggleSortOrder(1);
                    p.finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected List<Estimate> doInBackground() {
                List<Estimate> list = controller.listAll();

                if (!includeDisabledCheckBox.isSelected()) {
                    Iterator<Estimate> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Estimate v = iterator.next();
                        if (!v.getEnabled()) {
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

    public void deleteEstimate(Estimate veh) {
        if (estimate != null && veh.getId().equals(estimate.getId())) {
            estimate = null;
            StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ESTIMATE")));
            lookup.clear();

        }

        model.removeEstimate(veh);

    }

    private void setupInComboBox() {

        inCombobox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String item = (String) e.getItem();
                    if (item.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ESTIMATENUMBER"))) {
                        textSearchPanel.setVisible(true);
                        dateSearchPanel.setVisible(false);
                    }
                    if (item.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ESTIMATEDATE"))) {
                        textSearchPanel.setVisible(false);
                        dateSearchPanel.setVisible(true);
                    }
                    if (item.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ACCEPTEDDATE"))) {
                        textSearchPanel.setVisible(false);
                        dateSearchPanel.setVisible(true);
                    }
                    if (item.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("STATUS"))) {
                        textSearchPanel.setVisible(true);
                        dateSearchPanel.setVisible(false);
                    }
                    if (item.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("DISCOUNT"))) {
                        textSearchPanel.setVisible(true);
                        dateSearchPanel.setVisible(false);
                    }
                    if (item.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("RESPONSIBLE"))) {
                        textSearchPanel.setVisible(true);
                        dateSearchPanel.setVisible(false);
                    }
                }
            }
        });
    }

    public class MyTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component comp = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            int rowN = table.convertRowIndexToModel(row);
            String s = table.getModel().getValueAt(rowN, 12).toString();

            if (s.equalsIgnoreCase("yes")) {
                comp.setForeground(Color.LIGHT_GRAY);
            } else {
                comp.setForeground(null);
            }

            return (comp);

        }
    }
}
