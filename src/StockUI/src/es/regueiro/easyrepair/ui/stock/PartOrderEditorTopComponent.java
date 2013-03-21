package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.login.SecurityManager;
import es.regueiro.easyrepair.api.employee.controller.EmployeeController;
import es.regueiro.easyrepair.api.stock.controller.PartController;
import es.regueiro.easyrepair.api.stock.controller.PartOrderController;
import es.regueiro.easyrepair.api.stock.controller.SupplierController;
import es.regueiro.easyrepair.api.stock.controller.WarehouseController;
import es.regueiro.easyrepair.model.employee.Employee;
import es.regueiro.easyrepair.model.shared.Invoice;
import es.regueiro.easyrepair.model.stock.*;
import es.regueiro.easyrepair.model.user.Privilege;
import es.regueiro.easyrepair.reports.ReportPrinter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.DefaultEditorKit;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateMidnight;
import org.joda.time.LocalDate;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.*;
import org.openide.util.*;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.stock//PartOrderEditor//EN",
autostore = false)
@TopComponent.Description(iconBase = "es/regueiro/easyrepair/ui/stock/icons/package_edit.png", preferredID = "PartOrderEditorTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 17, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.stock.PartOrderEditorTopComponent")
@TopComponent.OpenActionRegistration(displayName = "#CTL_PartOrderEditorAction",
preferredID = "PartOrderEditorTopComponent")
@Messages({
    "CTL_PartOrderEditorAction=Editor de pedidos",
    "CTL_PartOrderEditorTopComponent=Editor de pedidos",
    "HINT_PartOrderEditorTopComponent=Esta es una ventana del editor de pedidos"
})
public final class PartOrderEditorTopComponent extends TopComponent implements LookupListener, DocumentListener, TableModelListener, ListSelectionListener, PropertyChangeListener {

    private PartOrder partOrder = null;
    private Lookup.Result<PartOrder> result;
    private InstanceContent content = new InstanceContent();
    private UndoRedo.Manager manager = new UndoRedo.Manager();
    private PartOrderController controller = Lookup.getDefault().lookup(PartOrderController.class);
    private PartController partController = Lookup.getDefault().lookup(PartController.class);
    private boolean modified = false;
    private boolean newPartOrder = true;
    private boolean invalid = false;
    private DialogDescriptor browsePartsDialog;
    private Part selectedPart;
    private PartLineTableModel partsListModel = new PartLineTableModel();
    private PartTableModel partModel = new PartTableModel();
    private final JDatePickerImpl orderDatePicker;
    private final JDatePickerImpl estimatedDatePicker;
    private final JDatePickerImpl receiptDatePicker;
    private final JDatePickerImpl invoiceDatePicker;
    private final JDatePickerImpl invoiceAcceptedDatePicker;
    private final JDatePickerImpl invoiceEstimatedPaymentDatePicker;
    private final JDatePickerImpl invoicePaymentDatePicker;
    private SupplierController supplierController = Lookup.getDefault().lookup(SupplierController.class);
    private EmployeeController employeeController = Lookup.getDefault().lookup(EmployeeController.class);
    private WarehouseController warehouseController = Lookup.getDefault().lookup(WarehouseController.class);
    private Supplier supplier = null;
    private Employee orderResponsible = null;
    private Warehouse warehouse = null;
    private Employee invoiceResponsible = null;
    private BigDecimal partsTotal = new BigDecimal("0");

    public PartOrderEditorTopComponent() {
        initComponents();
        setName(Bundle.CTL_PartOrderEditorTopComponent());
        setToolTipText(Bundle.HINT_PartOrderEditorTopComponent());
        this.setFocusable(true);
        associateLookup(new AbstractLookup(content));
        ActionMap actionMap = getActionMap();
        actionMap.put(DefaultEditorKit.copyAction, new DefaultEditorKit.CopyAction());
        actionMap.put(DefaultEditorKit.cutAction, new DefaultEditorKit.CutAction());
        actionMap.put(DefaultEditorKit.pasteAction, new DefaultEditorKit.PasteAction());
        content.add(actionMap);



        partsTable.setModel(partsListModel);

        for (int i = 0; i < partsTable.getColumnCount(); i++) {
            partsTable.getColumnModel().getColumn(i).setCellRenderer(new PartLineTableCellRenderer());
        }

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
        partBrowserTable.setModel(partModel);

        for (int i = 0; i < partBrowserTable.getColumnCount(); i++) {
            partBrowserTable.getColumnModel().getColumn(i).setCellRenderer(new PartTableCellRenderer());
        }


        // Listen for intro on the search bar
        searchEntryField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                doSearch();
            }
        });


        setupSupplierComboBox();
        setupOrderResponsibleComboBox();
        setupWarehouseComboBox();
        setupInvoiceResponsibleComboBox();
        setupOrderStatusComboBox();
        setupInvoiceStatusComboBox();


        DateModel orderDateModel = JDateComponentFactory.createDateModel(new DateMidnight());
        DateModel estimatedDateModel = JDateComponentFactory.createDateModel(new DateMidnight());
        DateModel receiptDateModel = JDateComponentFactory.createDateModel(new DateMidnight());

        orderDatePicker = (JDatePickerImpl) JDateComponentFactory.createJDatePicker(orderDateModel);
        estimatedDatePicker = (JDatePickerImpl) JDateComponentFactory.createJDatePicker(estimatedDateModel);
        receiptDatePicker = (JDatePickerImpl) JDateComponentFactory.createJDatePicker(receiptDateModel);

        orderDatePanel.add(orderDatePicker, BorderLayout.NORTH);
        estimatedDatePanel.add(estimatedDatePicker, BorderLayout.NORTH);
        receiptDatePanel.add(receiptDatePicker, BorderLayout.NORTH);


        DateModel invoiceDateModel = JDateComponentFactory.createDateModel(new DateMidnight());
        DateModel invoiceAcceptedDateModel = JDateComponentFactory.createDateModel(new DateMidnight());
        DateModel invoiceEstimatedPaymentDateModel = JDateComponentFactory.createDateModel(new DateMidnight());
        DateModel invoicePaymentDateModel = JDateComponentFactory.createDateModel(new DateMidnight());

        invoiceDatePicker = (JDatePickerImpl) JDateComponentFactory.createJDatePicker(invoiceDateModel);
        invoiceAcceptedDatePicker = (JDatePickerImpl) JDateComponentFactory.createJDatePicker(invoiceAcceptedDateModel);
        invoiceEstimatedPaymentDatePicker = (JDatePickerImpl) JDateComponentFactory.createJDatePicker(invoiceEstimatedPaymentDateModel);
        invoicePaymentDatePicker = (JDatePickerImpl) JDateComponentFactory.createJDatePicker(invoicePaymentDateModel);

        invoiceDatePanel.add(invoiceDatePicker, BorderLayout.NORTH);
        invoiceAcceptedDatePanel.add(invoiceAcceptedDatePicker, BorderLayout.NORTH);
        invoiceEstimatedPaymentDatePanel.add(invoiceEstimatedPaymentDatePicker, BorderLayout.NORTH);
        invoicePaymentDatePanel.add(invoicePaymentDatePicker, BorderLayout.NORTH);


        mainTabbedPane.setIconAt(0, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/package.png")));
        mainTabbedPane.setIconAt(1, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/tire.png")));
        mainTabbedPane.setIconAt(2, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/invoice.png")));
//        emailEditAddressTextField.getDocument().addUndoableEditListener(manager);


        orderNumberTextField.setDocument(new MaxLengthTextDocument(11));
        discountTextField.setDocument(new MaxLengthTextDocument(5));
        shippingCostsTextField.setDocument(new MaxLengthTextDocument(12));
        otherCostsTextField.setDocument(new MaxLengthTextDocument(12));


        invoiceNumberTextField.setDocument(new MaxLengthTextDocument(11));
        invoicePaymentMethodTextField.setDocument(new MaxLengthTextDocument(100));

        setupUndo();


        orderNumberTextField.getDocument().addDocumentListener(this);
        discountTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateTotal();
                modify();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateTotal();
                modify();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateTotal();
                modify();
            }
        });

        shippingCostsTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateTotal();
                modify();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateTotal();
                modify();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateTotal();
                modify();
            }
        });

        otherCostsTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateTotal();
                modify();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateTotal();
                modify();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateTotal();
                modify();
            }
        });

        notesTextArea.getDocument().addDocumentListener(this);
        invoiceNumberTextField.getDocument().addDocumentListener(this);
        invoicePaymentMethodTextField.getDocument().addDocumentListener(this);
        invoiceNotesTextArea.getDocument().addDocumentListener(this);

        partsListModel.addTableModelListener(this);
        partsTable.getSelectionModel().addListSelectionListener(this);

//        partModel.addTableModelListener(this);
        partBrowserTable.getSelectionModel().addListSelectionListener(this);


        invoiceNumberTextField.getDocument().addDocumentListener(this);
        invoicePaymentMethodTextField.getDocument().addDocumentListener(this);

        orderDatePicker.getModel().addPropertyChangeListener(this);
        estimatedDatePicker.getModel().addPropertyChangeListener(this);
        receiptDatePicker.getModel().addPropertyChangeListener(this);
        invoiceDatePicker.getModel().addPropertyChangeListener(this);
        invoiceAcceptedDatePicker.getModel().addPropertyChangeListener(this);
        invoiceEstimatedPaymentDatePicker.getModel().addPropertyChangeListener(this);
        invoicePaymentDatePicker.getModel().addPropertyChangeListener(this);


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        partsBrowserPanel = new javax.swing.JPanel();
        searchPanel = new javax.swing.JPanel();
        searchEntryPanel = new javax.swing.JPanel();
        searchLabel = new javax.swing.JLabel();
        searchEntryField = new javax.swing.JTextField();
        inLabel = new javax.swing.JLabel();
        listAllButton = new javax.swing.JButton();
        inCombobox = new javax.swing.JComboBox();
        searchButtonPanel = new javax.swing.JPanel();
        findButton = new javax.swing.JButton();
        tablePanel = new javax.swing.JScrollPane();
        partBrowserTable = new javax.swing.JTable();
        mainTabbedPane = new javax.swing.JTabbedPane();
        mainScrollPane = new javax.swing.JScrollPane();
        mainGeneralPanel = new javax.swing.JPanel();
        generalPanel = new javax.swing.JPanel();
        generalLabel = new javax.swing.JLabel();
        idLabel = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();
        orderNumberLabel = new javax.swing.JLabel();
        orderNumberTextField = new javax.swing.JTextField();
        orderDateLabel = new javax.swing.JLabel();
        estimatedDateLabel = new javax.swing.JLabel();
        receiptDateLabel = new javax.swing.JLabel();
        shippingCostsLabel = new javax.swing.JLabel();
        shippingCostsTextField = new javax.swing.JTextField();
        otherCostsLabel = new javax.swing.JLabel();
        otherCostsTextField = new javax.swing.JTextField();
        discountLabel = new javax.swing.JLabel();
        discountTextField = new javax.swing.JTextField();
        orderStatusLabel = new javax.swing.JLabel();
        supplierLabel = new javax.swing.JLabel();
        supplierComboBox = new javax.swing.JComboBox();
        responsibleLabel = new javax.swing.JLabel();
        responsibleComboBox = new javax.swing.JComboBox();
        warehouseLabel = new javax.swing.JLabel();
        warehouseComboBox = new javax.swing.JComboBox();
        orderDatePanel = new javax.swing.JPanel();
        estimatedDatePanel = new javax.swing.JPanel();
        receiptDatePanel = new javax.swing.JPanel();
        totalLabel = new javax.swing.JLabel();
        totalWithIVALabel = new javax.swing.JLabel();
        totalTextField = new javax.swing.JTextField();
        totalWithIVATextField = new javax.swing.JTextField();
        orderStatusComboBox = new javax.swing.JComboBox();
        notesPanel = new javax.swing.JPanel();
        notesLabel = new javax.swing.JLabel();
        notesScrollPane = new javax.swing.JScrollPane();
        notesTextArea = new javax.swing.JTextArea();
        partsScrollPane = new javax.swing.JScrollPane();
        partsPanel = new javax.swing.JPanel();
        partsLabel = new javax.swing.JLabel();
        piecesPanel = new javax.swing.JScrollPane();
        partsTable = new javax.swing.JTable();
        bottomPanel = new javax.swing.JPanel();
        partsTotalLabel = new javax.swing.JLabel();
        partsTotalTextField = new javax.swing.JTextField();
        addPartButton = new javax.swing.JButton();
        removeSelectedPartsButton = new javax.swing.JButton();
        partsTotalWithIVALabel = new javax.swing.JLabel();
        partsTotalWithIVATextField = new javax.swing.JTextField();
        invoiceScrollPane = new javax.swing.JScrollPane();
        invoicePanel = new javax.swing.JPanel();
        invoiceGeneralPanel = new javax.swing.JPanel();
        invoiceLabel = new javax.swing.JLabel();
        invoiceNumberLabel = new javax.swing.JLabel();
        invoiceNumberTextField = new javax.swing.JTextField();
        invoiceDateLabel = new javax.swing.JLabel();
        invoiceAcceptedDateLabel = new javax.swing.JLabel();
        invoiceEstimatedPaymentDateLabel = new javax.swing.JLabel();
        invoicePaymentDateLabel = new javax.swing.JLabel();
        invoicePaymentMethodLabel = new javax.swing.JLabel();
        invoicePaymentMethodTextField = new javax.swing.JTextField();
        invoiceStatusLabel = new javax.swing.JLabel();
        invoiceResponsibleLabel = new javax.swing.JLabel();
        invoiceResponsibleComboBox = new javax.swing.JComboBox();
        invoiceDatePanel = new javax.swing.JPanel();
        invoiceAcceptedDatePanel = new javax.swing.JPanel();
        invoiceEstimatedPaymentDatePanel = new javax.swing.JPanel();
        invoicePaymentDatePanel = new javax.swing.JPanel();
        invoiceStatusComboBox = new javax.swing.JComboBox();
        invoiceNotesPanel = new javax.swing.JPanel();
        invoiceNotesLabel = new javax.swing.JLabel();
        invoiceNotesScrollPane = new javax.swing.JScrollPane();
        invoiceNotesTextArea = new javax.swing.JTextArea();
        topToolBar = new javax.swing.JToolBar();
        savePartOrderButton = new javax.swing.JButton();
        reloadPartOrderButton = new javax.swing.JButton();
        enablePartOrderButton = new javax.swing.JButton();
        disablePartOrderButton = new javax.swing.JButton();
        deletePartOrderButton = new javax.swing.JButton();
        printPartOrderButton = new javax.swing.JButton();
        printInvoiceButton = new javax.swing.JButton();

        partsBrowserPanel.setLayout(new java.awt.GridBagLayout());

        searchPanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        searchPanel.setLayout(new java.awt.GridBagLayout());

        searchEntryPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(searchLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.searchLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(inLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.inLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(inLabel, gridBagConstraints);

        listAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/group.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(listAllButton, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.listAllButton.text")); // NOI18N
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchPanel.add(searchEntryPanel, gridBagConstraints);

        searchButtonPanel.setLayout(new java.awt.BorderLayout(5, 5));

        findButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/find.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(findButton, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.findButton.text")); // NOI18N
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
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        partsBrowserPanel.add(searchPanel, gridBagConstraints);

        tablePanel.setMinimumSize(new java.awt.Dimension(100, 100));

        partBrowserTable.setAutoCreateRowSorter(true);
        partBrowserTable.setDoubleBuffered(true);
        partBrowserTable.setFillsViewportHeight(true);
        partBrowserTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        partBrowserTable.setRowHeight(25);
        partBrowserTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        partBrowserTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablePanel.setViewportView(partBrowserTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(17, 17, 17, 17);
        partsBrowserPanel.add(tablePanel, gridBagConstraints);

        setLayout(new java.awt.GridBagLayout());

        mainTabbedPane.setDoubleBuffered(true);

        mainScrollPane.setBorder(null);

        mainGeneralPanel.setLayout(new java.awt.GridBagLayout());

        generalPanel.setLayout(new java.awt.GridBagLayout());

        generalLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        generalLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(generalLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.generalLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        generalPanel.add(generalLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(idLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.idLabel.text")); // NOI18N
        idLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(idLabel, gridBagConstraints);

        idTextField.setBackground(new java.awt.Color(232, 231, 231));
        idTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(idTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(orderNumberLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.orderNumberLabel.text")); // NOI18N
        orderNumberLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(orderNumberLabel, gridBagConstraints);

        orderNumberTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        orderNumberTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(orderNumberTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(orderDateLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.orderDateLabel.text")); // NOI18N
        orderDateLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(orderDateLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(estimatedDateLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.estimatedDateLabel.text")); // NOI18N
        estimatedDateLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(estimatedDateLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(receiptDateLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.receiptDateLabel.text")); // NOI18N
        receiptDateLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(receiptDateLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(shippingCostsLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.shippingCostsLabel.text")); // NOI18N
        shippingCostsLabel.setMaximumSize(new java.awt.Dimension(90, 14));
        shippingCostsLabel.setMinimumSize(new java.awt.Dimension(90, 14));
        shippingCostsLabel.setPreferredSize(new java.awt.Dimension(90, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(shippingCostsLabel, gridBagConstraints);

        shippingCostsTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        shippingCostsTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(shippingCostsTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(otherCostsLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.otherCostsLabel.text")); // NOI18N
        otherCostsLabel.setPreferredSize(new java.awt.Dimension(90, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(otherCostsLabel, gridBagConstraints);

        otherCostsTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        otherCostsTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(otherCostsTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(discountLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.discountLabel.text")); // NOI18N
        discountLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(discountLabel, gridBagConstraints);

        discountTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        discountTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(discountTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(orderStatusLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.orderStatusLabel.text")); // NOI18N
        orderStatusLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(orderStatusLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(supplierLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.supplierLabel.text")); // NOI18N
        supplierLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(supplierLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(supplierComboBox, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(responsibleLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.responsibleLabel.text")); // NOI18N
        responsibleLabel.setPreferredSize(new java.awt.Dimension(90, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(responsibleLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(responsibleComboBox, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(warehouseLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.warehouseLabel.text")); // NOI18N
        warehouseLabel.setMaximumSize(new java.awt.Dimension(90, 14));
        warehouseLabel.setMinimumSize(new java.awt.Dimension(90, 14));
        warehouseLabel.setPreferredSize(new java.awt.Dimension(90, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(warehouseLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(warehouseComboBox, gridBagConstraints);

        orderDatePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(orderDatePanel, gridBagConstraints);

        estimatedDatePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(estimatedDatePanel, gridBagConstraints);

        receiptDatePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(receiptDatePanel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(totalLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.totalLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(totalLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(totalWithIVALabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.totalWithIVALabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(totalWithIVALabel, gridBagConstraints);

        totalTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(totalTextField, gridBagConstraints);

        totalWithIVATextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(totalWithIVATextField, gridBagConstraints);

        orderStatusComboBox.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(orderStatusComboBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        mainGeneralPanel.add(generalPanel, gridBagConstraints);

        notesPanel.setLayout(new java.awt.GridBagLayout());

        notesLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        notesLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(notesLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.notesLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        notesPanel.add(notesLabel, gridBagConstraints);

        notesTextArea.setColumns(20);
        notesTextArea.setRows(5);
        notesScrollPane.setViewportView(notesTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        notesPanel.add(notesScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        mainGeneralPanel.add(notesPanel, gridBagConstraints);

        mainScrollPane.setViewportView(mainGeneralPanel);

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.mainScrollPane.TabConstraints.tabTitle"), mainScrollPane); // NOI18N

        partsScrollPane.setBorder(null);

        partsPanel.setLayout(new java.awt.GridBagLayout());

        partsLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        partsLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(partsLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.partsLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        partsPanel.add(partsLabel, gridBagConstraints);

        piecesPanel.setMinimumSize(new java.awt.Dimension(100, 100));
        piecesPanel.setPreferredSize(new java.awt.Dimension(200, 200));

        partsTable.setAutoCreateRowSorter(true);
        partsTable.setFillsViewportHeight(true);
        partsTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        partsTable.setRowHeight(25);
        partsTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        piecesPanel.setViewportView(partsTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        partsPanel.add(piecesPanel, gridBagConstraints);

        bottomPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(partsTotalLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.partsTotalLabel.text")); // NOI18N
        partsTotalLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        bottomPanel.add(partsTotalLabel, gridBagConstraints);

        partsTotalTextField.setEditable(false);
        partsTotalTextField.setText(org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.partsTotalTextField.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        bottomPanel.add(partsTotalTextField, gridBagConstraints);

        addPartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addPartButton, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.addPartButton.text")); // NOI18N
        addPartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPartButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        bottomPanel.add(addPartButton, gridBagConstraints);

        removeSelectedPartsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(removeSelectedPartsButton, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.removeSelectedPartsButton.text")); // NOI18N
        removeSelectedPartsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSelectedPartsButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        bottomPanel.add(removeSelectedPartsButton, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(partsTotalWithIVALabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.partsTotalWithIVALabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        bottomPanel.add(partsTotalWithIVALabel, gridBagConstraints);

        partsTotalWithIVATextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        bottomPanel.add(partsTotalWithIVATextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        partsPanel.add(bottomPanel, gridBagConstraints);

        partsScrollPane.setViewportView(partsPanel);

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.partsScrollPane.TabConstraints.tabTitle"), partsScrollPane); // NOI18N

        invoiceScrollPane.setBorder(null);

        invoicePanel.setLayout(new java.awt.GridBagLayout());

        invoiceGeneralPanel.setLayout(new java.awt.GridBagLayout());

        invoiceLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        invoiceLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(invoiceLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.invoiceLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        invoiceGeneralPanel.add(invoiceLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(invoiceNumberLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.invoiceNumberLabel.text")); // NOI18N
        invoiceNumberLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        invoiceGeneralPanel.add(invoiceNumberLabel, gridBagConstraints);

        invoiceNumberTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        invoiceNumberTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        invoiceGeneralPanel.add(invoiceNumberTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(invoiceDateLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.invoiceDateLabel.text")); // NOI18N
        invoiceDateLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        invoiceGeneralPanel.add(invoiceDateLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(invoiceAcceptedDateLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.invoiceAcceptedDateLabel.text")); // NOI18N
        invoiceAcceptedDateLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        invoiceGeneralPanel.add(invoiceAcceptedDateLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(invoiceEstimatedPaymentDateLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.invoiceEstimatedPaymentDateLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        invoiceGeneralPanel.add(invoiceEstimatedPaymentDateLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(invoicePaymentDateLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.invoicePaymentDateLabel.text")); // NOI18N
        invoicePaymentDateLabel.setMaximumSize(new java.awt.Dimension(90, 14));
        invoicePaymentDateLabel.setMinimumSize(new java.awt.Dimension(90, 14));
        invoicePaymentDateLabel.setPreferredSize(new java.awt.Dimension(90, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        invoiceGeneralPanel.add(invoicePaymentDateLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(invoicePaymentMethodLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.invoicePaymentMethodLabel.text")); // NOI18N
        invoicePaymentMethodLabel.setPreferredSize(new java.awt.Dimension(118, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        invoiceGeneralPanel.add(invoicePaymentMethodLabel, gridBagConstraints);

        invoicePaymentMethodTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        invoicePaymentMethodTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        invoiceGeneralPanel.add(invoicePaymentMethodTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(invoiceStatusLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.invoiceStatusLabel.text")); // NOI18N
        invoiceStatusLabel.setPreferredSize(new java.awt.Dimension(90, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        invoiceGeneralPanel.add(invoiceStatusLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(invoiceResponsibleLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.invoiceResponsibleLabel.text")); // NOI18N
        invoiceResponsibleLabel.setMaximumSize(new java.awt.Dimension(90, 14));
        invoiceResponsibleLabel.setMinimumSize(new java.awt.Dimension(90, 14));
        invoiceResponsibleLabel.setPreferredSize(new java.awt.Dimension(90, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        invoiceGeneralPanel.add(invoiceResponsibleLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        invoiceGeneralPanel.add(invoiceResponsibleComboBox, gridBagConstraints);

        invoiceDatePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        invoiceGeneralPanel.add(invoiceDatePanel, gridBagConstraints);

        invoiceAcceptedDatePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        invoiceGeneralPanel.add(invoiceAcceptedDatePanel, gridBagConstraints);

        invoiceEstimatedPaymentDatePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        invoiceGeneralPanel.add(invoiceEstimatedPaymentDatePanel, gridBagConstraints);

        invoicePaymentDatePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        invoiceGeneralPanel.add(invoicePaymentDatePanel, gridBagConstraints);

        invoiceStatusComboBox.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        invoiceGeneralPanel.add(invoiceStatusComboBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        invoicePanel.add(invoiceGeneralPanel, gridBagConstraints);

        invoiceNotesPanel.setLayout(new java.awt.GridBagLayout());

        invoiceNotesLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        invoiceNotesLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(invoiceNotesLabel, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.invoiceNotesLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        invoiceNotesPanel.add(invoiceNotesLabel, gridBagConstraints);

        invoiceNotesTextArea.setColumns(20);
        invoiceNotesTextArea.setRows(5);
        invoiceNotesScrollPane.setViewportView(invoiceNotesTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        invoiceNotesPanel.add(invoiceNotesScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        invoicePanel.add(invoiceNotesPanel, gridBagConstraints);

        invoiceScrollPane.setViewportView(invoicePanel);

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.invoiceScrollPane.TabConstraints.tabTitle"), invoiceScrollPane); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(mainTabbedPane, gridBagConstraints);

        topToolBar.setBorder(null);
        topToolBar.setFloatable(false);
        topToolBar.setRollover(true);

        savePartOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/disk.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(savePartOrderButton, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.savePartOrderButton.text")); // NOI18N
        savePartOrderButton.setFocusable(false);
        savePartOrderButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        savePartOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        savePartOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        savePartOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePartOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(savePartOrderButton);

        reloadPartOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/arrow_refresh.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(reloadPartOrderButton, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.reloadPartOrderButton.text")); // NOI18N
        reloadPartOrderButton.setFocusable(false);
        reloadPartOrderButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        reloadPartOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        reloadPartOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        reloadPartOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadPartOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(reloadPartOrderButton);

        enablePartOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/status_online.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(enablePartOrderButton, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.enablePartOrderButton.text")); // NOI18N
        enablePartOrderButton.setFocusable(false);
        enablePartOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        enablePartOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        enablePartOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enablePartOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(enablePartOrderButton);

        disablePartOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/status_offline.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(disablePartOrderButton, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.disablePartOrderButton.text")); // NOI18N
        disablePartOrderButton.setFocusable(false);
        disablePartOrderButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        disablePartOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        disablePartOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        disablePartOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disablePartOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(disablePartOrderButton);

        deletePartOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deletePartOrderButton, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.deletePartOrderButton.text")); // NOI18N
        deletePartOrderButton.setFocusable(false);
        deletePartOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deletePartOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deletePartOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePartOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deletePartOrderButton);

        printPartOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printPartOrderButton, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.printPartOrderButton.text")); // NOI18N
        printPartOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printPartOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printPartOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printPartOrderButton);

        printInvoiceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printInvoiceButton, org.openide.util.NbBundle.getMessage(PartOrderEditorTopComponent.class, "PartOrderEditorTopComponent.printInvoiceButton.text")); // NOI18N
        printInvoiceButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printInvoiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printInvoiceButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printInvoiceButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(topToolBar, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void savePartOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savePartOrderButtonActionPerformed
        savePartOrder();
    }//GEN-LAST:event_savePartOrderButtonActionPerformed

    private void disablePartOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disablePartOrderButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
//                disablePartOrderButton.setVisible(false);
//                enablePartOrderButton.setVisible(true);
                fillData();
                StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DISABLED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PARTORDER")));
            }

            @Override
            protected Void doInBackground() {
                if (updatePartOrder()) {
                    partOrder = controller.disablePartOrder();
                }
                return null;
            }
        };

        worker.execute();

    }//GEN-LAST:event_disablePartOrderButtonActionPerformed

    private void reloadPartOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadPartOrderButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SURE_RELOAD_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PARTORDER")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RELOAD"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {
            final SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected void done() {
                    modified = false;
                    invalid = false;
                    try {
                        if (get()) {
                            fillData();
                        } else {
                            NotifyDescriptor d = new NotifyDescriptor.Message(
                                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PART_ORDER_RELOAD_NOT_FOUND"),
                                    NotifyDescriptor.ERROR_MESSAGE);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RELOADED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PARTORDER")));
                }

                @Override
                protected Boolean doInBackground() {
                    partOrder = controller.reloadPartOrder();
                    if (partOrder != null) {
                        controller.setPartOrder(partOrder);
                        return true;
                    } else {
                        return false;
                    }
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_reloadPartOrderButtonActionPerformed

    private void enablePartOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enablePartOrderButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
//                disablePartOrderButton.setVisible(true);
//                enablePartOrderButton.setVisible(false);
                fillData();
                StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ENABLED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PARTORDER")));
            }

            @Override
            protected Void doInBackground() {
                if (updatePartOrder()) {
                    partOrder = controller.enablePartOrder();
                }
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_enablePartOrderButtonActionPerformed

    private void deletePartOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePartOrderButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PARTORDER")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {


            PartOrderLookup.getDefault().clear();

            PartOrderBrowserTopComponent window = (PartOrderBrowserTopComponent) WindowManager.getDefault().findTopComponent("PartOrderBrowserTopComponent");

            window.deletePartOrder(partOrder);
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    forceClose();
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PARTORDER")));
                }

                @Override
                protected Void doInBackground() {
                    controller.setPartOrder(partOrder);
                    controller.deletePartOrder();
                    return null;
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_deletePartOrderButtonActionPerformed
    private void addSelectedPart() {
        if (selectedPart != null) {
            PartLine part = new PartLine();
            part.setPart(selectedPart);
            part.setQuantity(1);
            part.setDiscount("0");

            partOrder.addPart(part);
            partsListModel.addPartLine(part);
            modify();
        }
    }
    private void addPartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPartButtonActionPerformed
        browsePartsDialog = new DialogDescriptor(partsBrowserPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SELECT_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PART")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() == DialogDescriptor.OK_OPTION) {
                            addSelectedPart();
                            browsePartsDialog.setClosingOptions(null);
                            modify();
                        } else {
                            selectedPart = null;
                        }
                        partBrowserTable.clearSelection();
                    }
                });
        browsePartsDialog.setClosingOptions(new Object[]{DialogDescriptor.CANCEL_OPTION});
        browsePartsDialog.setValid(false);
        DialogDisplayer.getDefault().createDialog(browsePartsDialog);
        DialogDisplayer.getDefault().notify(browsePartsDialog);
    }//GEN-LAST:event_addPartButtonActionPerformed

    private void removeSelectedPartsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSelectedPartsButtonActionPerformed
        int[] selectedRows = partsTable.getSelectedRows();
        PartLine par;
        for (int i = 0; i < selectedRows.length; i++) {
            par = partsListModel.getRow(partsTable.convertRowIndexToModel(selectedRows[i] - i));

            partOrder.removePartLine(par);

            partsListModel.removeRow(partsTable.convertRowIndexToModel(selectedRows[i] - i));
        }
    }//GEN-LAST:event_removeSelectedPartsButtonActionPerformed

    private void listAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listAllButtonActionPerformed
        doListAll();
    }//GEN-LAST:event_listAllButtonActionPerformed

    private void findButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findButtonActionPerformed
        doSearch();
    }//GEN-LAST:event_findButtonActionPerformed

    private void printPartOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printPartOrderButtonActionPerformed
        if (updatePartOrder()) {
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
        }
    }//GEN-LAST:event_printPartOrderButtonActionPerformed

    private void printInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printInvoiceButtonActionPerformed
        if (updatePartOrder()) {
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                }

                @Override
                protected Void doInBackground() {
                    ReportPrinter printer = new ReportPrinter();
                    printer.printPartOrderInvoice(partOrder.getInvoice());
                    return null;
                }
            };
            worker.execute();
        }
    }//GEN-LAST:event_printInvoiceButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addPartButton;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JButton deletePartOrderButton;
    private javax.swing.JButton disablePartOrderButton;
    private javax.swing.JLabel discountLabel;
    private javax.swing.JTextField discountTextField;
    private javax.swing.JButton enablePartOrderButton;
    private javax.swing.JLabel estimatedDateLabel;
    private javax.swing.JPanel estimatedDatePanel;
    private javax.swing.JButton findButton;
    private javax.swing.JLabel generalLabel;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JLabel idLabel;
    private javax.swing.JTextField idTextField;
    private javax.swing.JComboBox inCombobox;
    private javax.swing.JLabel inLabel;
    private javax.swing.JLabel invoiceAcceptedDateLabel;
    private javax.swing.JPanel invoiceAcceptedDatePanel;
    private javax.swing.JLabel invoiceDateLabel;
    private javax.swing.JPanel invoiceDatePanel;
    private javax.swing.JLabel invoiceEstimatedPaymentDateLabel;
    private javax.swing.JPanel invoiceEstimatedPaymentDatePanel;
    private javax.swing.JPanel invoiceGeneralPanel;
    private javax.swing.JLabel invoiceLabel;
    private javax.swing.JLabel invoiceNotesLabel;
    private javax.swing.JPanel invoiceNotesPanel;
    private javax.swing.JScrollPane invoiceNotesScrollPane;
    private javax.swing.JTextArea invoiceNotesTextArea;
    private javax.swing.JLabel invoiceNumberLabel;
    private javax.swing.JTextField invoiceNumberTextField;
    private javax.swing.JPanel invoicePanel;
    private javax.swing.JLabel invoicePaymentDateLabel;
    private javax.swing.JPanel invoicePaymentDatePanel;
    private javax.swing.JLabel invoicePaymentMethodLabel;
    private javax.swing.JTextField invoicePaymentMethodTextField;
    private javax.swing.JComboBox invoiceResponsibleComboBox;
    private javax.swing.JLabel invoiceResponsibleLabel;
    private javax.swing.JScrollPane invoiceScrollPane;
    private javax.swing.JComboBox invoiceStatusComboBox;
    private javax.swing.JLabel invoiceStatusLabel;
    private javax.swing.JButton listAllButton;
    private javax.swing.JPanel mainGeneralPanel;
    private javax.swing.JScrollPane mainScrollPane;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JLabel notesLabel;
    private javax.swing.JPanel notesPanel;
    private javax.swing.JScrollPane notesScrollPane;
    private javax.swing.JTextArea notesTextArea;
    private javax.swing.JLabel orderDateLabel;
    private javax.swing.JPanel orderDatePanel;
    private javax.swing.JLabel orderNumberLabel;
    private javax.swing.JTextField orderNumberTextField;
    private javax.swing.JComboBox orderStatusComboBox;
    private javax.swing.JLabel orderStatusLabel;
    private javax.swing.JLabel otherCostsLabel;
    private javax.swing.JTextField otherCostsTextField;
    private javax.swing.JTable partBrowserTable;
    private javax.swing.JPanel partsBrowserPanel;
    private javax.swing.JLabel partsLabel;
    private javax.swing.JPanel partsPanel;
    private javax.swing.JScrollPane partsScrollPane;
    private javax.swing.JTable partsTable;
    private javax.swing.JLabel partsTotalLabel;
    private javax.swing.JTextField partsTotalTextField;
    private javax.swing.JLabel partsTotalWithIVALabel;
    private javax.swing.JTextField partsTotalWithIVATextField;
    private javax.swing.JScrollPane piecesPanel;
    private javax.swing.JButton printInvoiceButton;
    private javax.swing.JButton printPartOrderButton;
    private javax.swing.JLabel receiptDateLabel;
    private javax.swing.JPanel receiptDatePanel;
    private javax.swing.JButton reloadPartOrderButton;
    private javax.swing.JButton removeSelectedPartsButton;
    private javax.swing.JComboBox responsibleComboBox;
    private javax.swing.JLabel responsibleLabel;
    private javax.swing.JButton savePartOrderButton;
    private javax.swing.JPanel searchButtonPanel;
    private javax.swing.JTextField searchEntryField;
    private javax.swing.JPanel searchEntryPanel;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JLabel shippingCostsLabel;
    private javax.swing.JTextField shippingCostsTextField;
    private javax.swing.JComboBox supplierComboBox;
    private javax.swing.JLabel supplierLabel;
    private javax.swing.JScrollPane tablePanel;
    private javax.swing.JToolBar topToolBar;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JTextField totalTextField;
    private javax.swing.JLabel totalWithIVALabel;
    private javax.swing.JTextField totalWithIVATextField;
    private javax.swing.JComboBox warehouseComboBox;
    private javax.swing.JLabel warehouseLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public HelpCtx getHelpCtx() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return new HelpCtx("es.regueiro.easyrepair.ui.stock.editor");
    }

    public PartOrder getPartOrder() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return partOrder;
    }

    public void forceClose() {
        modified = false;
        this.close();
    }

    @Override
    public void componentOpened() {
        invalid = false;
        result = PartOrderLookup.getDefault().lookupResult(PartOrder.class);
        result.removeLookupListener(this);
        mainTabbedPane.setSelectedIndex(0);

        Collection<? extends PartOrder> partOrderColId = result.allInstances();
        if (!partOrderColId.isEmpty()) {
            partOrder = controller.getPartOrderById(partOrderColId.iterator().next().getId());
            if (partOrder == null) {
                NotifyDescriptor d = new NotifyDescriptor.Message(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_NOT_FOUND"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PARTORDER")), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                close();
            } else {
                newPartOrder = false;
            }
        } else {
            partOrder = controller.newPartOrder();
            newPartOrder = true;
        }
        if (partOrder != null) {
            reloadPartOrderButton.setEnabled(!newPartOrder);
            deletePartOrderButton.setEnabled(!newPartOrder);
            enablePartOrderButton.setVisible(false);
            disablePartOrderButton.setEnabled(!newPartOrder);
            controller.setPartOrder(partOrder);
            fillData();
            StatusDisplayer.getDefault().setStatusText("");
        }

    }

    @Override
    public void componentClosed() {
        manager.discardAllEdits();
        partsListModel.clear();
    }

    @Override
    public void componentActivated() {
        checkPermissions();
    }

    @Override
    public boolean canClose() {
        if (modified) {
            NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("CHANGES_WILL_BE_LOST"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PARTORDER")),
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("CLOSE"), NotifyDescriptor.YES_NO_OPTION);

            Object retval = DialogDisplayer.getDefault().notify(d);

            if (retval == NotifyDescriptor.YES_OPTION) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
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
    public void resultChanged(LookupEvent le) {
    }

    @Override
    public UndoRedo getUndoRedo() {
        return manager;
    }

    private void checkPermissions() {
        if (SecurityManager.getDefault().isUserLoggedIn()) {
            orderNumberTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));
            orderDatePicker.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));
            estimatedDatePicker.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));
            receiptDatePicker.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));
            shippingCostsTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));
            otherCostsTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));
            discountTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));
            orderStatusComboBox.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));
            supplierComboBox.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));
            responsibleComboBox.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));
            warehouseComboBox.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));
            notesTextArea.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));

            partsTable.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));
            addPartButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));
            removeSelectedPartsButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_EDIT));

            invoiceNumberTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_INVOICE_EDIT));
            invoiceDatePicker.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_INVOICE_EDIT));
            invoiceAcceptedDatePicker.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_INVOICE_EDIT));
            invoiceEstimatedPaymentDatePicker.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_INVOICE_EDIT));
            invoicePaymentDatePicker.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_INVOICE_EDIT));
            invoicePaymentMethodTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_INVOICE_EDIT));
            invoiceNumberTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_INVOICE_EDIT));
            invoiceNumberTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_INVOICE_EDIT));
            invoiceStatusComboBox.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_INVOICE_EDIT));
            invoiceResponsibleComboBox.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_INVOICE_EDIT));
            invoiceNotesTextArea.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_ORDER_INVOICE_EDIT));

        } else {
            this.close();
        }
    }

    private void fillData() {
        if (!invalid) {
            clearGeneral();
            clearPartsListTable();
            clearInvoice();

            if (partOrder != null && partOrder.getId() != null) {
                idTextField.setText(partOrder.getId().toString());
                if (partOrder.getOrderNumber() != null) {
                    orderNumberTextField.setText(partOrder.getOrderNumber());
                }
                if (partOrder.getOrderDate() != null) {
                    orderDatePicker.getModel().setDate(partOrder.getOrderDate().getYear(), partOrder.getOrderDate().getMonthOfYear() - 1, partOrder.getOrderDate().getDayOfMonth());
                    orderDatePicker.getModel().setSelected(true);
                    //orderDateTextField.setText(partOrder.getOrderDate().toString());
                }

                if (partOrder.getEstimatedDate() != null) {
                    estimatedDatePicker.getModel().setDate(partOrder.getEstimatedDate().getYear(), partOrder.getEstimatedDate().getMonthOfYear() - 1, partOrder.getEstimatedDate().getDayOfMonth());
                    estimatedDatePicker.getModel().setSelected(true);
//                    estimatedDateTextField.setText(partOrder.getEstimatedDate().toString());
                }
                if (partOrder.getReceiptDate() != null) {
                    receiptDatePicker.getModel().setDate(partOrder.getReceiptDate().getYear(), partOrder.getReceiptDate().getMonthOfYear() - 1, partOrder.getReceiptDate().getDayOfMonth());
                    receiptDatePicker.getModel().setSelected(true);
//                    receiptDateTextField.setText(partOrder.getReceiptDate().toString());
                }
                if (partOrder.getShippingCosts() != null) {
                    shippingCostsTextField.setText(partOrder.getShippingCosts().toPlainString());
                }
                if (partOrder.getOtherCosts() != null) {
                    otherCostsTextField.setText(partOrder.getOtherCosts().toPlainString());
                }
                if (partOrder.getDiscount() != null) {
                    discountTextField.setText(partOrder.getDiscount().toPlainString());
                }
                if (partOrder.getNotes() != null) {
                    notesTextArea.setText(partOrder.getNotes());
                }
                if (partOrder.getEnabled()) {
                    enablePartOrderButton.setVisible(false);
                    disablePartOrderButton.setVisible(true);
                } else {
                    enablePartOrderButton.setVisible(true);
                    disablePartOrderButton.setVisible(false);
                }
                fillPartsList();

                if (partOrder.getInvoice() != null) {
                    if (partOrder.getInvoice().getInvoiceNumber() != null) {
                        invoiceNumberTextField.setText(partOrder.getInvoice().getInvoiceNumber());
                    }


                    if (partOrder.getInvoice().getInvoiceDate() != null) {
                        invoiceDatePicker.getModel().setDate(partOrder.getInvoice().getInvoiceDate().getYear(), partOrder.getInvoice().getInvoiceDate().getMonthOfYear() - 1, partOrder.getInvoice().getInvoiceDate().getDayOfMonth());
                        invoiceDatePicker.getModel().setSelected(true);
                    }

                    if (partOrder.getInvoice().getAcceptedDate() != null) {
                        invoiceAcceptedDatePicker.getModel().setDate(partOrder.getInvoice().getAcceptedDate().getYear(), partOrder.getInvoice().getAcceptedDate().getMonthOfYear() - 1, partOrder.getInvoice().getAcceptedDate().getDayOfMonth());
                        invoiceAcceptedDatePicker.getModel().setSelected(true);
                    }
                    if (partOrder.getInvoice().getEstimatedPaymentDate() != null) {
                        invoiceEstimatedPaymentDatePicker.getModel().setDate(partOrder.getInvoice().getEstimatedPaymentDate().getYear(), partOrder.getInvoice().getEstimatedPaymentDate().getMonthOfYear() - 1, partOrder.getInvoice().getEstimatedPaymentDate().getDayOfMonth());
                        invoiceEstimatedPaymentDatePicker.getModel().setSelected(true);
                    }
                    if (partOrder.getInvoice().getPaymentDate() != null) {
                        invoicePaymentDatePicker.getModel().setDate(partOrder.getInvoice().getPaymentDate().getYear(), partOrder.getInvoice().getPaymentDate().getMonthOfYear() - 1, partOrder.getInvoice().getPaymentDate().getDayOfMonth());
                        invoicePaymentDatePicker.getModel().setSelected(true);
                    }
                    if (partOrder.getInvoice().getPaymentMethod() != null) {
                        invoicePaymentMethodTextField.setText(partOrder.getInvoice().getPaymentMethod());
                    }

                    if (partOrder.getInvoice().getNotes() != null) {
                        invoiceNotesTextArea.setText(partOrder.getInvoice().getNotes());
                    }
                }

            }
            fillSupplierCombobox();
            fillOrderResponsibleCombobox();
            fillOrderStatusCombobox();

            fillWarehouseCombobox();


            fillInvoiceResponsibleCombobox();
            fillInvoiceStatusCombobox();

            manager.discardAllEdits();
            modified = invalid;
            savePartOrderButton.setEnabled(modified);
            deletePartOrderButton.setEnabled(!newPartOrder);
            disablePartOrderButton.setEnabled(!newPartOrder);
            reloadPartOrderButton.setEnabled(modified && !newPartOrder);
        }
    }

    private void fillPartsList() {
//        clearStockTable();
        if (partOrder.getPartsList() != null && !partOrder.getPartsList().isEmpty()) {
            partsListModel.fill(partOrder.getPartsList());
        } else {
            partsListModel.fill(null);
        }
        modified = false;

    }

    private void clearGeneral() {
        idTextField.setText("");
        orderNumberTextField.setText("");
        orderDatePicker.getModel().setSelected(false);
        estimatedDatePicker.getModel().setSelected(false);
        receiptDatePicker.getModel().setSelected(false);
        shippingCostsTextField.setText("");
        otherCostsTextField.setText("");
        discountTextField.setText("");
        notesTextArea.setText("");

    }

    private void clearInvoice() {
        invoiceNumberTextField.setText("");
        invoiceDatePicker.getModel().setSelected(false);
        invoiceAcceptedDatePicker.getModel().setSelected(false);
        invoiceEstimatedPaymentDatePicker.getModel().setSelected(false);
        invoicePaymentDatePicker.getModel().setSelected(false);
        invoicePaymentMethodTextField.setText("");
        invoiceNotesTextArea.setText("");
    }

    private void clearPartsListTable() {
        partsListModel.clear();
    }

    private void savePartOrder() {
//        if (!StringUtils.isBlank(orderNumberTextField.getText())) {
//            try {
//                partOrder.setOrderNumber(orderNumberTextField.getText());
//            } catch (IllegalArgumentException e) {
//                NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
//                Object retval = DialogDisplayer.getDefault().notify(d);
//                invalid = true;
//                return;
//            }
//        } else {
//            partOrder.setOrderNumber(null);
//        }
////            if (!StringUtils.isBlank(estimatedDateTextField.getText())) {
////                partOrder.setCategory(estimatedDateTextField.getText());
////            } else {
////                partOrder.setCategory(null);
////            }
//        if (orderDatePicker.getModel().isSelected()) {
//            partOrder.setOrderDate(new LocalDate(orderDatePicker.getModel().getValue()));
//        } else {
//            partOrder.setOrderDate(null);
//        }
//
//        if (estimatedDatePicker.getModel().isSelected()) {
//            partOrder.setEstimatedDate(new LocalDate(estimatedDatePicker.getModel().getValue()));
//        } else {
//            partOrder.setEstimatedDate(null);
//        }
//
//        if (receiptDatePicker.getModel().isSelected()) {
//            partOrder.setReceiptDate(new LocalDate(receiptDatePicker.getModel().getValue()));
//        } else {
//            partOrder.setReceiptDate(null);
//        }
//
//
//        if (!orderStatusComboBox.getSelectedItem().equals("")) {
//            partOrder.setStatus((String) orderStatusComboBox.getSelectedItem());
//        } else {
//            partOrder.setStatus(null);
//        }
//
//
//        if (!StringUtils.isBlank(notesTextArea.getText())) {
//            partOrder.setNotes(notesTextArea.getText());
//        } else {
//            partOrder.setNotes(null);
//        }
//
//        if (!StringUtils.isBlank(discountTextField.getText())) {
//            try {
//                partOrder.setDiscount(discountTextField.getText());
//            } catch (IllegalArgumentException e) {
//                NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
//                Object retval = DialogDisplayer.getDefault().notify(d);
//                invalid = true;
//                return;
//            }
//        } else {
//            partOrder.setDiscount(null);
//        }
//
//        if (!StringUtils.isBlank(shippingCostsTextField.getText())) {
//            try {
//                partOrder.setShippingCosts(shippingCostsTextField.getText());
//            } catch (IllegalArgumentException e) {
//                NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
//                Object retval = DialogDisplayer.getDefault().notify(d);
//                invalid = true;
//                return;
//            }
//        } else {
//            partOrder.setShippingCosts(null);
//        }
//        if (!StringUtils.isBlank(otherCostsTextField.getText())) {
//            try {
//                partOrder.setOtherCosts(otherCostsTextField.getText());
//            } catch (IllegalArgumentException e) {
//                NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
//                Object retval = DialogDisplayer.getDefault().notify(d);
//                invalid = true;
//                return;
//            }
//        } else {
//            partOrder.setOtherCosts(null);
//        }
//
//        partOrder.setSupplier(supplier);
//
//        partOrder.setShippingWarehouse(warehouse);
//
//        partOrder.setResponsible(orderResponsible);
//
//
//
//
//
//
//
//        if (!StringUtils.isBlank(invoiceNumberTextField.getText())) {
//            try {
//                partOrder.getInvoice().setInvoiceNumber(invoiceNumberTextField.getText());
//            } catch (IllegalArgumentException e) {
//                NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
//                Object retval = DialogDisplayer.getDefault().notify(d);
//                invalid = true;
//                return;
//            }
//        } else {
//            partOrder.getInvoice().setInvoiceNumber(null);
//        }
//
//        if (invoiceDatePicker.getModel().isSelected()) {
//            partOrder.getInvoice().setInvoiceDate(new LocalDate(invoiceDatePicker.getModel().getValue()));
//        } else {
//            partOrder.getInvoice().setInvoiceDate(null);
//        }
//
//        if (invoiceAcceptedDatePicker.getModel().isSelected()) {
//            partOrder.getInvoice().setAcceptedDate(new LocalDate(invoiceAcceptedDatePicker.getModel().getValue()));
//        } else {
//            partOrder.getInvoice().setAcceptedDate(null);
//        }
//
//        if (invoiceEstimatedPaymentDatePicker.getModel().isSelected()) {
//            partOrder.getInvoice().setEstimatedPaymentDate(new LocalDate(invoiceEstimatedPaymentDatePicker.getModel().getValue()));
//        } else {
//            partOrder.getInvoice().setEstimatedPaymentDate(null);
//        }
//
//        if (invoicePaymentDatePicker.getModel().isSelected()) {
//            partOrder.getInvoice().setPaymentDate(new LocalDate(invoicePaymentDatePicker.getModel().getValue()));
//        } else {
//            partOrder.getInvoice().setPaymentDate(null);
//        }
//
//        if (!StringUtils.isBlank(invoicePaymentMethodTextField.getText())) {
//            partOrder.getInvoice().setPaymentMethod(invoicePaymentMethodTextField.getText());
//        } else {
//            partOrder.getInvoice().setPaymentMethod(null);
//        }
//
//        if (!StringUtils.isBlank(invoiceStatusTextField.getText())) {
//            partOrder.getInvoice().setStatus(invoiceStatusTextField.getText());
//        } else {
//            partOrder.getInvoice().setStatus(null);
//        }
//
//        if (!StringUtils.isBlank(invoiceNotesTextArea.getText())) {
//            partOrder.getInvoice().setNotes(invoiceNotesTextArea.getText());
//        } else {
//            partOrder.getInvoice().setNotes(null);
//        }
//        partOrder.getInvoice().setResponsible(invoiceResponsible);


        if (updatePartOrder()) {
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SAVING"));

                @Override
                protected void done() {
                    fillData();
                    p.finish();
                }

                @Override
                protected Void doInBackground() {
                    p.start();
                    try {
                        controller.setPartOrder(partOrder);
                        controller.savePartOrder();
                        newPartOrder = false;
                        invalid = false;
                    } catch (Exception e) {
                        if (e.getMessage().contains("was updated or deleted by another user")) {
                            NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("OVERWRITE_DIALOG"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PARTORDER")),
                                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_MODIFIED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PARTORDER")));
                            String options[] = new String[2];
                            options[0] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("OVERWRITE");
                            options[1] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RELOAD");
                            d.setOptions(options);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                            if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("OVERWRITE"))) {
                                controller.overwritePartOrder();
                                partOrder = controller.getPartOrder();
                                invalid = false;
                            } else if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RELOAD"))) {
                                reloadPartOrderButton.doClick();
                            }
                        } else {
                            NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                            System.out.println(e.toString());
                            invalid = true;
                        }
                    }
                    return null;
                }
            };

            worker.execute();
        }
    }

    private boolean updatePartOrder() {
        if (!StringUtils.isBlank(orderNumberTextField.getText())) {
            try {
                partOrder.setOrderNumber(orderNumberTextField.getText());
            } catch (IllegalArgumentException e) {
                NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                invalid = true;
                return false;
            }
        } else {
            partOrder.setOrderNumber(null);
        }
//            if (!StringUtils.isBlank(estimatedDateTextField.getText())) {
//                partOrder.setCategory(estimatedDateTextField.getText());
//            } else {
//                partOrder.setCategory(null);
//            }
        if (orderDatePicker.getModel().isSelected()) {
            partOrder.setOrderDate(new LocalDate(orderDatePicker.getModel().getValue()));
        } else {
            partOrder.setOrderDate(null);
        }

        if (estimatedDatePicker.getModel().isSelected()) {
            partOrder.setEstimatedDate(new LocalDate(estimatedDatePicker.getModel().getValue()));
        } else {
            partOrder.setEstimatedDate(null);
        }

        if (receiptDatePicker.getModel().isSelected()) {
            partOrder.setReceiptDate(new LocalDate(receiptDatePicker.getModel().getValue()));
        } else {
            partOrder.setReceiptDate(null);
        }

        if (!orderStatusComboBox.getSelectedItem().equals("")) {
            partOrder.setStatus((String) orderStatusComboBox.getSelectedItem());
        } else {
            partOrder.setStatus(null);
        }

        if (!StringUtils.isBlank(notesTextArea.getText())) {
            partOrder.setNotes(notesTextArea.getText());
        } else {
            partOrder.setNotes(null);
        }

        if (!StringUtils.isBlank(discountTextField.getText())) {
            try {
                partOrder.setDiscount(discountTextField.getText());
            } catch (IllegalArgumentException e) {
                NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                invalid = true;
                return false;
            }
        } else {
            partOrder.setDiscount(null);
        }

        if (!StringUtils.isBlank(shippingCostsTextField.getText())) {
            try {
                partOrder.setShippingCosts(shippingCostsTextField.getText());
            } catch (IllegalArgumentException e) {
                NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                invalid = true;
                return false;
            }
        } else {
            partOrder.setShippingCosts(null);
        }
        if (!StringUtils.isBlank(otherCostsTextField.getText())) {
            try {
                partOrder.setOtherCosts(otherCostsTextField.getText());
            } catch (IllegalArgumentException e) {
                NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                invalid = true;
                return false;
            }
        } else {
            partOrder.setOtherCosts(null);
        }

        partOrder.setSupplier(supplier);

        partOrder.setShippingWarehouse(warehouse);

        partOrder.setResponsible(orderResponsible);


        if (!StringUtils.isBlank(invoiceNumberTextField.getText())) {
            try {
                partOrder.getInvoice().setInvoiceNumber(invoiceNumberTextField.getText());
            } catch (IllegalArgumentException e) {
                NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                invalid = true;
                return false;
            }
        } else {
            partOrder.getInvoice().setInvoiceNumber(null);
        }

        if (invoiceDatePicker.getModel().isSelected()) {
            partOrder.getInvoice().setInvoiceDate(new LocalDate(invoiceDatePicker.getModel().getValue()));
        } else {
            partOrder.getInvoice().setInvoiceDate(null);
        }

        if (invoiceAcceptedDatePicker.getModel().isSelected()) {
            partOrder.getInvoice().setAcceptedDate(new LocalDate(invoiceAcceptedDatePicker.getModel().getValue()));
        } else {
            partOrder.getInvoice().setAcceptedDate(null);
        }

        if (invoiceEstimatedPaymentDatePicker.getModel().isSelected()) {
            partOrder.getInvoice().setEstimatedPaymentDate(new LocalDate(invoiceEstimatedPaymentDatePicker.getModel().getValue()));
        } else {
            partOrder.getInvoice().setEstimatedPaymentDate(null);
        }

        if (invoicePaymentDatePicker.getModel().isSelected()) {
            partOrder.getInvoice().setPaymentDate(new LocalDate(invoicePaymentDatePicker.getModel().getValue()));
        } else {
            partOrder.getInvoice().setPaymentDate(null);
        }

        if (!StringUtils.isBlank(invoicePaymentMethodTextField.getText())) {
            partOrder.getInvoice().setPaymentMethod(invoicePaymentMethodTextField.getText());
        } else {
            partOrder.getInvoice().setPaymentMethod(null);
        }

        if (!invoiceStatusComboBox.getSelectedItem().equals("")) {
            partOrder.getInvoice().setStatus((String) invoiceStatusComboBox.getSelectedItem());
        } else {
            partOrder.getInvoice().setStatus(null);
        }

        if (!StringUtils.isBlank(invoiceNotesTextArea.getText())) {
            partOrder.getInvoice().setNotes(invoiceNotesTextArea.getText());
        } else {
            partOrder.getInvoice().setNotes(null);
        }
        partOrder.getInvoice().setResponsible(invoiceResponsible);


        invalid = false;
        return true;

    }

    private void setupUndo() {
        notesTextArea.getDocument().addUndoableEditListener(manager);
        shippingCostsTextField.getDocument().addUndoableEditListener(manager);
        discountTextField.getDocument().addUndoableEditListener(manager);
        idTextField.getDocument().addUndoableEditListener(manager);
        //TODO add the rest of the textfields
    }

    private void removeUndo() {
        notesTextArea.getDocument().removeUndoableEditListener(manager);
        shippingCostsTextField.getDocument().removeUndoableEditListener(manager);
        discountTextField.getDocument().removeUndoableEditListener(manager);
    }

    private void modify() {
        modified = true;
        savePartOrderButton.setEnabled(modified);
        reloadPartOrderButton.setEnabled(modified && !newPartOrder);

        StatusDisplayer.getDefault().setStatusText("");
    }

    private void fillSupplierCombobox() {
        List<Supplier> listSupplier = supplierController.listAll();
        supplierComboBox.removeAllItems();

        supplier = null;
        for (Supplier s : listSupplier) {

            SupplierBox box = new SupplierBox(s);
            if (supplier == null) {
                supplier = box.supplier;
            }
            supplierComboBox.addItem(box);
            if (partOrder.getSupplier() != null && partOrder.getSupplier().equals(s)) {
                supplierComboBox.setSelectedItem(box);
                supplier = box.supplier;
            }
        }
    }

    private void setupSupplierComboBox() {
        supplierComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    supplier = ((SupplierBox) e.getItem()).supplier;
                }
            }
        });
    }

    private void fillOrderResponsibleCombobox() {
        List<Employee> listEmployee = employeeController.listAll();
        responsibleComboBox.removeAllItems();

        orderResponsible = null;
        for (Employee e : listEmployee) {
            EmployeeBox box = new EmployeeBox(e);
            if (orderResponsible == null) {
                orderResponsible = box.employee;
            }
            responsibleComboBox.addItem(box);
            if (partOrder.getResponsible() != null && partOrder.getResponsible().equals(e)) {
                responsibleComboBox.setSelectedItem(box);
                orderResponsible = box.employee;
            }
        }
    }

    private void setupOrderResponsibleComboBox() {
        responsibleComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    orderResponsible = ((EmployeeBox) e.getItem()).employee;
                }
            }
        });
    }

    private void fillInvoiceResponsibleCombobox() {
        List<Employee> listEmployee = employeeController.listAll();
        invoiceResponsibleComboBox.removeAllItems();

        invoiceResponsible = null;
        for (Employee e : listEmployee) {
            EmployeeBox box = new EmployeeBox(e);
            if (invoiceResponsible == null) {
                invoiceResponsible = box.employee;
            }
            invoiceResponsibleComboBox.addItem(box);
            if (partOrder.getInvoice() != null && partOrder.getInvoice().getResponsible() != null && partOrder.getInvoice().getResponsible().equals(e)) {
                invoiceResponsibleComboBox.setSelectedItem(box);
                invoiceResponsible = box.employee;
            }
        }
    }
    
    
    

    private void setupInvoiceResponsibleComboBox() {
        invoiceResponsibleComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    invoiceResponsible = ((EmployeeBox) e.getItem()).employee;
                }
            }
        });
    }

    private void fillInvoiceStatusCombobox() {
        String stat = null;
        if (partOrder.getInvoice() != null) {
            stat = partOrder.getInvoice().getStatus();
        }
        invoiceStatusComboBox.removeAllItems();

        String[] cbStatus = {java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PENDING"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ACCEPTED"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PAYED")
        };

        for (String s : cbStatus) {
            invoiceStatusComboBox.addItem(s);
        }
        invoiceStatusComboBox.setSelectedIndex(0);

        if (stat != null) {
            boolean found = false;
            for (String s : cbStatus) {
                if (stat.equals(s)) {
                    found = true;
                    invoiceStatusComboBox.setSelectedItem(s);
                }
            }
            if (!found) {
                invoiceStatusComboBox.addItem(stat);
                invoiceStatusComboBox.setSelectedItem(stat);
            }
        }
    }

    private void setupInvoiceStatusComboBox() {
        invoiceStatusComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                modify();
            }
        });
    }

    private void fillOrderStatusCombobox() {
        String stat = partOrder.getStatus();

        orderStatusComboBox.removeAllItems();

        String[] cbStatus = {java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PENDING_SHIPPING"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PENDING_INVOICE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PENDING_PAYMENT"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PENDING"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RECEIVED"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PAYED")
        };

        for (String s : cbStatus) {
            orderStatusComboBox.addItem(s);
        }
        orderStatusComboBox.setSelectedIndex(0);

        if (stat != null) {
            boolean found = false;
            for (String s : cbStatus) {
                if (stat.equals(s)) {
                    found = true;
                    orderStatusComboBox.setSelectedItem(s);
                }
            }
            if (!found) {
                orderStatusComboBox.addItem(stat);
                orderStatusComboBox.setSelectedItem(stat);
            }
        }
//        setupOrderStatusComboBox();
    }

    private void setupOrderStatusComboBox() {
//        orderStatusComboBox.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                modify();
//            }
//        });

        orderStatusComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                modify();
            }
        });
    }

    private void fillWarehouseCombobox() {
        List<Warehouse> listWarehouse = warehouseController.listAll();
        warehouseComboBox.removeAllItems();

        warehouse = null;
        for (Warehouse w : listWarehouse) {
            WarehouseBox box = new WarehouseBox(w);
            if (warehouse == null) {
                warehouse = box.warehouse;
            }
            warehouseComboBox.addItem(box);
            if (partOrder.getShippingWarehouse() != null && partOrder.getShippingWarehouse().equals(w)) {
                warehouseComboBox.setSelectedItem(box);
                warehouse = box.warehouse;
            }
        }
    }

    private void setupWarehouseComboBox() {
        warehouseComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    warehouse = ((WarehouseBox) e.getItem()).warehouse;
                }
            }
        });
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        modify();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        modify();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        modify();
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        partsTotal = new BigDecimal("0");
        if (partsListModel.getRowCount() > 0) {
            List<PartLine> partsL = partsListModel.getPartLineList();
            for (PartLine p : partsL) {
                BigDecimal partPrice = p.getPart().getPrice().multiply(new BigDecimal(p.getQuantity()));
                BigDecimal partDiscount = new BigDecimal("1").subtract(p.getDiscount().divide(new BigDecimal("100")));

                partsTotal = partsTotal.add(partPrice.multiply(partDiscount));
            }

        }

        partsTotalTextField.setText(formatCurrency(partsTotal.setScale(2, RoundingMode.HALF_UP).toPlainString()));

        BigDecimal iva = new BigDecimal(NbPreferences.root().get("iva", "21")).divide(new BigDecimal("100"));
        partsTotalWithIVATextField.setText(formatCurrency(partsTotal.add(partsTotal.multiply(iva)).setScale(2, RoundingMode.HALF_UP).toPlainString()));


        modify();
        calculateTotal();
    }

    private void calculateTotal() {
        BigDecimal discount;
        BigDecimal otherCosts;
        BigDecimal shippingCosts;

        if (!StringUtils.isBlank(discountTextField.getText())) {
            try {
                discount = new BigDecimal(discountTextField.getText());
            } catch (NumberFormatException e) {
                discount = new BigDecimal("0");
            }
        } else {
            discount = new BigDecimal("0");
        }
        if (!StringUtils.isBlank(otherCostsTextField.getText())) {
            try {
                otherCosts = new BigDecimal(otherCostsTextField.getText());
            } catch (NumberFormatException e) {
                otherCosts = new BigDecimal("0");
            }
        } else {
            otherCosts = new BigDecimal("0");
        }
        if (!StringUtils.isBlank(shippingCostsTextField.getText())) {
            try {
                shippingCosts = new BigDecimal(shippingCostsTextField.getText());
            } catch (NumberFormatException e) {
                shippingCosts = new BigDecimal("0");
            }
        } else {
            shippingCosts = new BigDecimal("0");
        }
        BigDecimal totalDiscount = new BigDecimal("1").subtract(discount.divide(new BigDecimal("100")));
        BigDecimal iva = new BigDecimal(NbPreferences.root().get("iva", "21")).divide(new BigDecimal("100"));

        BigDecimal total = (partsTotal.add(otherCosts).add(shippingCosts)).multiply(totalDiscount);

        totalTextField.setText(formatCurrency(total.setScale(2, RoundingMode.HALF_UP).toPlainString()));
        totalWithIVATextField.setText(formatCurrency(total.add(total.multiply(iva)).setScale(2, RoundingMode.HALF_UP).toPlainString()));

    }

    private String formatCurrency(String amount) {
        String text = "";
        if (NbPreferences.root().getBoolean("inFront", false)) {
            text += NbPreferences.root().get("currency", " €");
        }
        text += amount;
        if (NbPreferences.root().getBoolean("inFront", false)) {
        } else {
            text += NbPreferences.root().get("currency", " €");
        }
        return text;
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        ListSelectionModel lsm = (ListSelectionModel) event.getSource();

        if (lsm.equals(partsTable.getSelectionModel())) {
            if (!event.getValueIsAdjusting()) {
                if (partsTable.getSelectedRow() > -1) {
                    removeSelectedPartsButton.setEnabled(true);
                } else {
                    removeSelectedPartsButton.setEnabled(false);
                }
            }
        } else if (lsm.equals(partBrowserTable.getSelectionModel())) {

            if (!event.getValueIsAdjusting()) {
                if (partBrowserTable.getSelectedRow() > -1) {
                    selectedPart = partModel.getRow(partBrowserTable.convertRowIndexToModel(partBrowserTable.getSelectedRow()));
                    browsePartsDialog.setValid(true);
                } else {
                    selectedPart = null;
                    browsePartsDialog.setValid(false);
                }
            }
        }

    }

    private void doSearch() {

        final SwingWorker<List<Part>, Void> worker = new SwingWorker<List<Part>, Void>() {
            private int column;
            ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SEARCHING"));

            @Override
            protected void done() {
                try {
                    List<Part> list = get();
                    partModel.fill(list);

                    if (column != 0) {
                        partBrowserTable.getRowSorter().toggleSortOrder(column - 1);
                    } else {
                        partBrowserTable.getRowSorter().toggleSortOrder(1);
                    }

                    partBrowserTable.getRowSorter().toggleSortOrder(column);
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
                switch (selected) {
                    case 0:
                        list = partController.searchByMake(toSearch);
                        column = 1;
                        break;
                    case 1:
                        list = partController.searchByModel(toSearch);
                        column = 2;
                        break;
                    case 2:
                        list = partController.searchByCategory(toSearch);
                        column = 3;
                        break;
                    case 3:
                        list = partController.searchByPrice(toSearch);
                        column = 4;
                        break;
                    case 4:
                        try {
                            list = partController.searchByMinPrice(toSearch);
                            column = 4;
                        } catch (Exception e) {
                            NotifyDescriptor d = new NotifyDescriptor.Message(
                                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_INVALID"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PRICE")), NotifyDescriptor.WARNING_MESSAGE);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                        }
                        break;
                    case 5:
                        try {
                            list = partController.searchByMaxPrice(toSearch);
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
                return list;
            }
        };
        worker.execute();
    }

    private void doListAll() {

        final ProgressHandle p = ProgressHandleFactory.createHandle(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("LISTING_ALL_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PARTS")));

        SwingWorker<List<Part>, Void> worker = new SwingWorker<List<Part>, Void>() {
            @Override
            protected void done() {
                try {
                    partModel.fill(get());
                    partBrowserTable.getRowSorter().toggleSortOrder(2);
                    partBrowserTable.getRowSorter().toggleSortOrder(1);
                    p.finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected List<Part> doInBackground() {
                List<Part> list = partController.listAll();
                return list;
            }
        };

        p.start();
        worker.execute();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        modify();
    }
    
    public void viewInvoice(Invoice invoice) {
        if (partOrder != null && invoice != null && partOrder.getInvoice().equals(invoice)) {
            mainTabbedPane.setSelectedIndex(2);
        }
    }

    private class SupplierBox {

        public Supplier supplier;

        public SupplierBox(Supplier supplier) {
            this.supplier = supplier;
        }

        @Override
        public String toString() {
            String ret = String.format("%08d", this.supplier.getId()) + " - " + this.supplier.getName();
            if (!this.supplier.getEnabled()) {
                ret = ret + " (" + java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DISABLED") + ")";
            }
            return ret;
        }
    }

    private class EmployeeBox {

        public Employee employee;

        public EmployeeBox(Employee employee) {
            this.employee = employee;
        }

        @Override
        public String toString() {
            String ret = this.employee.getEmployeeId() + " - " + this.employee.getName();
            if (this.employee.getSurname() != null) {
                ret += " " + this.employee.getSurname();
            }
            if (this.employee.getOccupation() != null) {
                ret += " - " + this.employee.getOccupation();
            }
            if (!this.employee.getEnabled()) {
                ret = ret + " (" + java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DISABLED") + ")";
            }
            return ret;
        }
    }

    private class WarehouseBox {

        public Warehouse warehouse;

        public WarehouseBox(Warehouse warehouse) {
            this.warehouse = warehouse;
        }

        @Override
        public String toString() {
            String ret = String.format("%04d", this.warehouse.getId()) + " - " + this.warehouse.getName();
            if (!this.warehouse.getEnabled()) {
                ret = ret + " (" + java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DISABLED") + ")";
            }
            return ret;
        }
    }

    public class MyTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component comp = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            int rowN = table.convertRowIndexToModel(row);
            String s = table.getModel().getValueAt(rowN, 11).toString();

            if (s.equalsIgnoreCase("yes")) {
                comp.setForeground(Color.LIGHT_GRAY);
            } else {
                comp.setForeground(null);
            }

            return (comp);

        }
    }
}
