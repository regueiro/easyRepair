package es.regueiro.easyrepair.ui.repair;

import es.regueiro.easyrepair.login.SecurityManager;
import es.regueiro.easyrepair.api.repair.controller.RepairOrderController;
import es.regueiro.easyrepair.login.LoginLookup;
import es.regueiro.easyrepair.model.repair.RepairOrder;
import es.regueiro.easyrepair.model.user.Privilege;
import es.regueiro.easyrepair.reports.ReportPrinter;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import org.joda.time.DateMidnight;
import org.joda.time.LocalDate;
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
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.repair//RepairOrderBrowser//EN",
autostore = false)
@TopComponent.Description(preferredID = "RepairOrderBrowserTopComponent",
iconBase = "es/regueiro/easyrepair/ui/repair/icons/toolbox.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 6, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.repair.RepairOrderBrowserTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Reparaciones", position = 0),
    @ActionReference(path = "Toolbars/Reparaciones", position = 0)
})
@TopComponent.OpenActionRegistration(displayName = "#CTL_RepairOrderBrowserAction",
preferredID = "RepairOrderBrowserTopComponent")
@Messages({
    "CTL_RepairOrderBrowserAction=Buscador de reparaciones",
    "CTL_RepairOrderBrowserTopComponent=Buscador de reparaciones",
    "HINT_RepairOrderBrowserTopComponent=Esta es una ventana del buscador de reparaciones"
})
public final class RepairOrderBrowserTopComponent extends TopComponent implements TableModelListener, ListSelectionListener {

    private RepairOrderTableModel model = new RepairOrderTableModel();
    private InstanceContent content = new InstanceContent();
    private RepairOrderController controller = Lookup.getDefault().lookup(RepairOrderController.class);
    private RepairOrder repairOrder;
    private RepairOrderLookup lookup = RepairOrderLookup.getDefault();
    private final JDatePickerImpl dateSearchFromDatePicker;
    private final JDatePickerImpl dateSearchToDatePicker;

    public RepairOrderBrowserTopComponent() {
        initComponents();
        setName(Bundle.CTL_RepairOrderBrowserTopComponent());
        setToolTipText(Bundle.HINT_RepairOrderBrowserTopComponent());
        this.setFocusable(true);

        String[] inCBStrings = {java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ORDERNUMBER"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ORDERDATE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ESTIMATEDDATE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("FINISHDATE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("DELIVERYDATE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("STATUS"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("GASTANKLEVEL"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("KILOMETRES")};


        for (String s : inCBStrings) {
            inCombobox.addItem(s);
        }
        // Associate the table model
        repairOrderTable.setModel(model);

        for (int i = 0; i < repairOrderTable.getColumnCount(); i++) {
            repairOrderTable.getColumnModel().getColumn(i).setCellRenderer(new RepairOrderTableCellRenderer());
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
        numberSearchPanel.setVisible(false);


        // Activate Copy/Paste
        ActionMap actionMap = getActionMap();
        actionMap.put(DefaultEditorKit.copyAction, new DefaultEditorKit.CopyAction());
        actionMap.put(DefaultEditorKit.cutAction, new DefaultEditorKit.CutAction());
        actionMap.put(DefaultEditorKit.pasteAction, new DefaultEditorKit.PasteAction());
        content.add(actionMap);

        // Listen to the changes on the table
        model.addTableModelListener(this);
        repairOrderTable.getSelectionModel().addListSelectionListener(this);

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
        numberSearchPanel = new javax.swing.JPanel();
        numberSearchFromTextField = new javax.swing.JTextField();
        numberSearchFromLabel = new javax.swing.JLabel();
        numberSearchToLabel = new javax.swing.JLabel();
        numberSearchToTextField = new javax.swing.JTextField();
        includeDisabledCheckBox = new javax.swing.JCheckBox();
        searchButtonPanel = new javax.swing.JPanel();
        findButton = new javax.swing.JButton();
        tablePanel = new javax.swing.JScrollPane();
        repairOrderTable = new javax.swing.JTable();
        topToolBar = new javax.swing.JToolBar();
        detailedViewButton = new javax.swing.JButton();
        newRepairOrderButton = new javax.swing.JButton();
        editRepairOrderButton = new javax.swing.JButton();
        deleteRepairOrderButton = new javax.swing.JButton();
        printSelectedRepairOrderButton = new javax.swing.JButton();
        printRepairOrderListButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        searchPanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        searchPanel.setLayout(new java.awt.GridBagLayout());

        searchEntryPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(searchLabel, org.openide.util.NbBundle.getMessage(RepairOrderBrowserTopComponent.class, "RepairOrderBrowserTopComponent.searchLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(searchLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(inLabel, org.openide.util.NbBundle.getMessage(RepairOrderBrowserTopComponent.class, "RepairOrderBrowserTopComponent.inLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(inLabel, gridBagConstraints);

        listAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/group.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(listAllButton, org.openide.util.NbBundle.getMessage(RepairOrderBrowserTopComponent.class, "RepairOrderBrowserTopComponent.listAllButton.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(dateSearchFromLabel, org.openide.util.NbBundle.getMessage(RepairOrderBrowserTopComponent.class, "RepairOrderBrowserTopComponent.dateSearchFromLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        dateSearchPanel.add(dateSearchFromLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(dateSearchToLabel, org.openide.util.NbBundle.getMessage(RepairOrderBrowserTopComponent.class, "RepairOrderBrowserTopComponent.dateSearchToLabel.text")); // NOI18N
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

        numberSearchPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        numberSearchPanel.add(numberSearchFromTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(numberSearchFromLabel, org.openide.util.NbBundle.getMessage(RepairOrderBrowserTopComponent.class, "RepairOrderBrowserTopComponent.numberSearchFromLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        numberSearchPanel.add(numberSearchFromLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(numberSearchToLabel, org.openide.util.NbBundle.getMessage(RepairOrderBrowserTopComponent.class, "RepairOrderBrowserTopComponent.numberSearchToLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        numberSearchPanel.add(numberSearchToLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        numberSearchPanel.add(numberSearchToTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        searchEntryPanel.add(numberSearchPanel, gridBagConstraints);

        includeDisabledCheckBox.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(includeDisabledCheckBox, org.openide.util.NbBundle.getMessage(RepairOrderBrowserTopComponent.class, "RepairOrderBrowserTopComponent.includeDisabledCheckBox.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(findButton, org.openide.util.NbBundle.getMessage(RepairOrderBrowserTopComponent.class, "RepairOrderBrowserTopComponent.findButton.text")); // NOI18N
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

        repairOrderTable.setAutoCreateRowSorter(true);
        repairOrderTable.setDoubleBuffered(true);
        repairOrderTable.setFillsViewportHeight(true);
        repairOrderTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        repairOrderTable.setRowHeight(25);
        repairOrderTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        repairOrderTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablePanel.setViewportView(repairOrderTable);

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

        detailedViewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/toolbox_detailed.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(detailedViewButton, org.openide.util.NbBundle.getMessage(RepairOrderBrowserTopComponent.class, "RepairOrderBrowserTopComponent.detailedViewButton.text")); // NOI18N
        detailedViewButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        detailedViewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailedViewButtonActionPerformed(evt);
            }
        });
        topToolBar.add(detailedViewButton);

        newRepairOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(newRepairOrderButton, org.openide.util.NbBundle.getMessage(RepairOrderBrowserTopComponent.class, "RepairOrderBrowserTopComponent.newRepairOrderButton.text")); // NOI18N
        newRepairOrderButton.setFocusable(false);
        newRepairOrderButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        newRepairOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        newRepairOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        newRepairOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newRepairOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(newRepairOrderButton);

        editRepairOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/toolbox_edit.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(editRepairOrderButton, org.openide.util.NbBundle.getMessage(RepairOrderBrowserTopComponent.class, "RepairOrderBrowserTopComponent.editRepairOrderutton.text")); // NOI18N
        editRepairOrderButton.setEnabled(false);
        editRepairOrderButton.setFocusable(false);
        editRepairOrderButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        editRepairOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        editRepairOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        editRepairOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editRepairOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(editRepairOrderButton);

        deleteRepairOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteRepairOrderButton, org.openide.util.NbBundle.getMessage(RepairOrderBrowserTopComponent.class, "RepairOrderBrowserTopComponent.deleteRepairOrderButton.text")); // NOI18N
        deleteRepairOrderButton.setEnabled(false);
        deleteRepairOrderButton.setFocusable(false);
        deleteRepairOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deleteRepairOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deleteRepairOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRepairOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deleteRepairOrderButton);

        printSelectedRepairOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printSelectedRepairOrderButton, org.openide.util.NbBundle.getMessage(RepairOrderBrowserTopComponent.class, "RepairOrderBrowserTopComponent.printSelectedRepairOrderButton.text")); // NOI18N
        printSelectedRepairOrderButton.setEnabled(false);
        printSelectedRepairOrderButton.setFocusable(false);
        printSelectedRepairOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printSelectedRepairOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSelectedRepairOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printSelectedRepairOrderButton);

        printRepairOrderListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printRepairOrderListButton, org.openide.util.NbBundle.getMessage(RepairOrderBrowserTopComponent.class, "RepairOrderBrowserTopComponent.printRepairOrderListButton.text")); // NOI18N
        printRepairOrderListButton.setEnabled(false);
        printRepairOrderListButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printRepairOrderListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printRepairOrderListButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printRepairOrderListButton);

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

    private void newRepairOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newRepairOrderButtonActionPerformed
        lookup.clear();
        editRepairOrderButton.setEnabled(false);
        deleteRepairOrderButton.setEnabled(false);
        printSelectedRepairOrderButton.setEnabled(false);
        repairOrderTable.clearSelection();
        openRepairOrderEditor();
    }//GEN-LAST:event_newRepairOrderButtonActionPerformed
    private void editRepairOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editRepairOrderButtonActionPerformed
        openRepairOrderEditor();
    }//GEN-LAST:event_editRepairOrderButtonActionPerformed

    private void deleteRepairOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteRepairOrderButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("REPAIRORDER")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {

            RepairOrderEditorTopComponent window = (RepairOrderEditorTopComponent) WindowManager.getDefault().findTopComponent("RepairOrderEditorTopComponent");
            if (window.getRepairOrder() != null && repairOrder.getId().equals(window.getRepairOrder().getId())) {
                window.forceClose();
            }


            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("REPAIRORDER")));
                    lookup.clear();
                }

                @Override
                protected Void doInBackground() {
                    model.removeRow(repairOrderTable.convertRowIndexToModel(repairOrderTable.getSelectedRow()));
                    controller.setRepairOrder(repairOrder);
                    controller.deleteRepairOrder();
                    repairOrder = null;
                    return null;
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_deleteRepairOrderButtonActionPerformed

    private void detailedViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailedViewButtonActionPerformed
        TopComponent window = WindowManager.getDefault().findTopComponent("RepairOrderDetailedViewTopComponent");
        if (window == null) {
            window = new RepairOrderDetailedViewTopComponent();
        }
        window.open();
        window.requestActive();

    }//GEN-LAST:event_detailedViewButtonActionPerformed

    private void printRepairOrderListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printRepairOrderListButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printRepairOrderList(model.getRepairOrderList());
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printRepairOrderListButtonActionPerformed

    private void printSelectedRepairOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSelectedRepairOrderButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printRepairOrder(repairOrder);
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printSelectedRepairOrderButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dateSearchFromLabel;
    private javax.swing.JPanel dateSearchFromPanel;
    private javax.swing.JPanel dateSearchPanel;
    private javax.swing.JLabel dateSearchToLabel;
    private javax.swing.JPanel dateSearchToPanel;
    private javax.swing.JButton deleteRepairOrderButton;
    private javax.swing.JButton detailedViewButton;
    private javax.swing.JButton editRepairOrderButton;
    private javax.swing.JButton findButton;
    private javax.swing.JComboBox inCombobox;
    private javax.swing.JLabel inLabel;
    private javax.swing.JCheckBox includeDisabledCheckBox;
    private javax.swing.JButton listAllButton;
    private javax.swing.JButton newRepairOrderButton;
    private javax.swing.JLabel numberSearchFromLabel;
    private javax.swing.JTextField numberSearchFromTextField;
    private javax.swing.JPanel numberSearchPanel;
    private javax.swing.JLabel numberSearchToLabel;
    private javax.swing.JTextField numberSearchToTextField;
    private javax.swing.JButton printRepairOrderListButton;
    private javax.swing.JButton printSelectedRepairOrderButton;
    private javax.swing.JTable repairOrderTable;
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
            newRepairOrderButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
        } else {
            this.close();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            if (repairOrderTable.getSelectedRow() > -1) {
                repairOrder = model.getRow(repairOrderTable.convertRowIndexToModel(repairOrderTable.getSelectedRow()));
                editRepairOrderButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
                deleteRepairOrderButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
                printSelectedRepairOrderButton.setEnabled(true);
                lookup.setRepairOrder(repairOrder);
            } else {
                editRepairOrderButton.setEnabled(false);
                deleteRepairOrderButton.setEnabled(false);
                printSelectedRepairOrderButton.setEnabled(false);
            }
        }
    }

    private void openRepairOrderEditor() {
        TopComponent window = WindowManager.getDefault().findTopComponent("RepairOrderEditorTopComponent");
        if (window == null) {
//            window = new RepairOrderEditorTopComponent();
        } else {
            window.close();
        }
        window.open();
        window.requestActive();
    }

    private void doSearch() {

        printRepairOrderListButton.setEnabled(false);

        final SwingWorker<List<RepairOrder>, Void> worker = new SwingWorker<List<RepairOrder>, Void>() {
            private int column;
            ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SEARCHING"));

            @Override
            protected void done() {
                try {
                    List<RepairOrder> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printRepairOrderListButton.setEnabled(true);
                    }

                    if (column != 1) {
                        repairOrderTable.getRowSorter().toggleSortOrder(column - 1);
                    } else {
                        repairOrderTable.getRowSorter().toggleSortOrder(2);
                    }

                    repairOrderTable.getRowSorter().toggleSortOrder(column);
                    p.finish();
                } catch (InterruptedException ignore) {
                    System.out.println(ignore.getCause());
                } catch (ExecutionException ignore) {
                    System.out.println(ignore.getCause());
                }
            }

            @Override
            protected List<RepairOrder> doInBackground() {
                int selected = inCombobox.getSelectedIndex();
                String toSearch = textSearchEntryField.getText();
                List<RepairOrder> list = null;
                column = 0;
                String fromDate = "";
                String toDate = "";
                if (!toSearch.isEmpty()) {
                    p.setDisplayName(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SEARCHING_FOR_X"), new Object[]{toSearch}));
                }
                p.start();
                try {
                    switch (selected) {
                        case 0:
                            list = controller.searchByRepairOrderNumber(toSearch);
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
                            list = controller.searchByOrderDate(fromDate, toDate);
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
                            list = controller.searchByEstimatedDate(fromDate, toDate);
                            column = 3;
                            break;
                        case 3:
                            fromDate = "";
                            toDate = "";
                            if (dateSearchFromDatePicker.getModel().isSelected()) {
                                fromDate = (new LocalDate(dateSearchFromDatePicker.getModel().getValue())).toString();
                            }
                            if (dateSearchToDatePicker.getModel().isSelected()) {
                                toDate = (new LocalDate(dateSearchToDatePicker.getModel().getValue())).toString();
                            }
                            list = controller.searchByFinishDate(fromDate, toDate);
                            column = 4;
                            break;
                        case 4:
                            fromDate = "";
                            toDate = "";
                            if (dateSearchFromDatePicker.getModel().isSelected()) {
                                fromDate = (new LocalDate(dateSearchFromDatePicker.getModel().getValue())).toString();
                            }
                            if (dateSearchToDatePicker.getModel().isSelected()) {
                                toDate = (new LocalDate(dateSearchToDatePicker.getModel().getValue())).toString();
                            }
                            list = controller.searchByDeliveryDate(fromDate, toDate);
                            column = 5;
                            break;
                        case 5:
                            list = controller.searchByStatus(toSearch);
                            column = 6;
                            break;
                        case 6:
                            list = controller.searchByGasTankLevel(toSearch);
                            column = 7;
                            break;
                        case 7:
                            list = controller.searchByKilometres(toSearch);
                            column = 8;
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
                    Iterator<RepairOrder> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        RepairOrder r = iterator.next();
                        if (!r.getEnabled()) {
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

        printRepairOrderListButton.setEnabled(false);

        final ProgressHandle p = ProgressHandleFactory.createHandle(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("LISTING_ALL_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("REPAIRORDERS")));

        SwingWorker<List<RepairOrder>, Void> worker = new SwingWorker<List<RepairOrder>, Void>() {
            @Override
            protected void done() {
                try {
                    List<RepairOrder> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printRepairOrderListButton.setEnabled(true);
                    }

                    repairOrderTable.getRowSorter().toggleSortOrder(2);
                    repairOrderTable.getRowSorter().toggleSortOrder(1);
                    p.finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected List<RepairOrder> doInBackground() {
                List<RepairOrder> list = null;
                try {
                    list = controller.listAll();
                } catch (Exception e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    p.finish();
                }

                if (!includeDisabledCheckBox.isSelected()) {
                    Iterator<RepairOrder> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        RepairOrder r = iterator.next();
                        if (!r.getEnabled()) {
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

    public void deleteRepairOrder(RepairOrder rep) {
        if (repairOrder != null && rep.getId().equals(repairOrder.getId())) {
            repairOrder = null;
            StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("REPAIRORDER")));
            lookup.clear();

        }

        model.removeRepairOrder(rep);

    }

    private void setupInComboBox() {

        inCombobox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String item = (String) e.getItem();

                    if (item.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ORDERNUMBER"))) {
                        textSearchPanel.setVisible(true);
                        dateSearchPanel.setVisible(false);
                        numberSearchPanel.setVisible(false);
                    }
                    if (item.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ORDERDATE"))) {
                        textSearchPanel.setVisible(false);
                        dateSearchPanel.setVisible(true);
                        numberSearchPanel.setVisible(false);
                    }
                    if (item.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ESTIMATEDDATE"))) {
                        textSearchPanel.setVisible(false);
                        dateSearchPanel.setVisible(true);
                        numberSearchPanel.setVisible(false);
                    }
                    if (item.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("FINISHDATE"))) {
                        textSearchPanel.setVisible(false);
                        dateSearchPanel.setVisible(true);
                        numberSearchPanel.setVisible(false);
                    }
                    if (item.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("DELIVERYDATE"))) {
                        textSearchPanel.setVisible(false);
                        dateSearchPanel.setVisible(true);
                        numberSearchPanel.setVisible(false);
                    }
                    if (item.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("STATUS"))) {
                        textSearchPanel.setVisible(true);
                        dateSearchPanel.setVisible(false);
                        numberSearchPanel.setVisible(false);
                    }
                    if (item.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("GASTANKLEVEL"))) {
                        textSearchPanel.setVisible(true);
                        dateSearchPanel.setVisible(false);
                        numberSearchPanel.setVisible(false);
                    }
                    if (item.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("KILOMETRES"))) {
                        textSearchPanel.setVisible(true);
                        dateSearchPanel.setVisible(false);
                        numberSearchPanel.setVisible(false);
                    }
                    if (item.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("DISCOUNT"))) {
                        textSearchPanel.setVisible(false);
                        dateSearchPanel.setVisible(false);
                        numberSearchPanel.setVisible(true);
                    }
                }
            }
        });
    }
}
