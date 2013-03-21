package es.regueiro.easyrepair.ui.repair;

import es.regueiro.easyrepair.login.SecurityManager;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import es.regueiro.easyrepair.api.client.controller.VehicleController;
import es.regueiro.easyrepair.api.employee.controller.EmployeeController;
import es.regueiro.easyrepair.api.repair.controller.LabourController;
import es.regueiro.easyrepair.api.repair.controller.RepairOrderController;
import es.regueiro.easyrepair.api.stock.controller.PartController;
import es.regueiro.easyrepair.model.client.Vehicle;
import es.regueiro.easyrepair.model.employee.Employee;
import es.regueiro.easyrepair.model.repair.Estimate;
import es.regueiro.easyrepair.model.repair.Labour;
import es.regueiro.easyrepair.model.repair.LabourLine;
import es.regueiro.easyrepair.model.repair.RepairOrder;
import es.regueiro.easyrepair.model.shared.Invoice;
import es.regueiro.easyrepair.model.stock.Part;
import es.regueiro.easyrepair.model.stock.PartLine;
import es.regueiro.easyrepair.model.stock.Supplier;
import es.regueiro.easyrepair.model.stock.Warehouse;
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
import javax.swing.ActionMap;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultEditorKit;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePanel;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
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
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.repair//RepairOrderEditor//EN",
autostore = false)
@TopComponent.Description(iconBase = "es/regueiro/easyrepair/ui/repair/icons/toolbox.png", preferredID = "RepairOrderEditorTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 7, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.repair.RepairOrderEditorTopComponent")
@TopComponent.OpenActionRegistration(displayName = "#CTL_RepairOrderEditorAction",
preferredID = "RepairOrderEditorTopComponent")
@Messages({
    "CTL_RepairOrderEditorAction=Editor de reparaciones",
    "CTL_RepairOrderEditorTopComponent=Editor de reparaciones",
    "HINT_RepairOrderEditorTopComponent=Esta es una ventana del editor de reparaciones"
})
public final class RepairOrderEditorTopComponent extends TopComponent implements LookupListener, DocumentListener, TableModelListener, ListSelectionListener, PropertyChangeListener {

    private RepairOrder repairOrder = null;
    private Lookup.Result<RepairOrder> result;
    private InstanceContent content = new InstanceContent();
    private UndoRedo.Manager manager = new UndoRedo.Manager();
    private RepairOrderController controller = Lookup.getDefault().lookup(RepairOrderController.class);
    private EmployeeController employeeController = Lookup.getDefault().lookup(EmployeeController.class);
    private PartController partController = Lookup.getDefault().lookup(PartController.class);
    private LabourController labourController = Lookup.getDefault().lookup(LabourController.class);
    private VehicleController vehicleController = Lookup.getDefault().lookup(VehicleController.class);
    private boolean modified = false;
    private boolean newRepairOrder = true;
    private boolean invalid = false;
    private PartTableModel partModel = new PartTableModel();
    private LabourTableModel labourModel = new LabourTableModel();
    private VehicleTableModel vehicleModel = new VehicleTableModel();
    private final JDatePickerImpl orderDatePicker;
    private final JDatePickerImpl estimatedDatePicker;
    private final JDatePickerImpl finishDatePicker;
    private final JDatePickerImpl deliveryDatePicker;
    private final JDatePickerImpl invoiceDatePicker;
    private final JDatePickerImpl invoiceAcceptedDatePicker;
    private final JDatePickerImpl invoiceEstimatedPaymentDatePicker;
    private final JDatePickerImpl invoicePaymentDatePicker;
    private final JDatePickerImpl estimateDatePicker;
    private final JDatePickerImpl estimateAcceptedDatePicker;
    private EstimatePartsTableModel partsListModel = new EstimatePartsTableModel();
    private EstimateLabourTableModel labourListModel = new EstimateLabourTableModel();
    private Employee orderResponsible = null;
    private Employee invoiceResponsible = null;
    private Employee estimateResponsible = null;
    private DialogDescriptor browsePartsDialog;
    private Part selectedPart;
    private DialogDescriptor browseLabourDialog;
    private Labour selectedLabour;
    private DialogDescriptor browseVehicleDialog;
    private Vehicle selectedVehicle;
    private BigDecimal labourTotal = new BigDecimal("0");
    private BigDecimal partsTotal = new BigDecimal("0");

    public RepairOrderEditorTopComponent() {
        initComponents();
        setName(Bundle.CTL_RepairOrderEditorTopComponent());
        setToolTipText(Bundle.HINT_RepairOrderEditorTopComponent());
        this.setFocusable(true);
        associateLookup(new AbstractLookup(content));
        ActionMap actionMap = getActionMap();
        actionMap.put(DefaultEditorKit.copyAction, new DefaultEditorKit.CopyAction());
        actionMap.put(DefaultEditorKit.cutAction, new DefaultEditorKit.CutAction());
        actionMap.put(DefaultEditorKit.pasteAction, new DefaultEditorKit.PasteAction());
        content.add(actionMap);

        partsTable.setModel(partsListModel);

        for (int i = 0; i < partsTable.getColumnCount(); i++) {
            partsTable.getColumnModel().getColumn(i).setCellRenderer(new EstimatePartsTableCellRenderer());
        }
        labourTable.setModel(labourListModel);

        for (int i = 0; i < labourTable.getColumnCount(); i++) {
            labourTable.getColumnModel().getColumn(i).setCellRenderer(new EstimateLabourTableCellRenderer());
        }

        String[] inCBStringsParts = {
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("MAKE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("MODEL"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("CATEGORY"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PRICE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("MINPRICE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("MAXPRICE")};

        for (String s : inCBStringsParts) {
            inPartsCombobox.addItem(s);
        }


        // Associate the table model
        partBrowserTable.setModel(partModel);

        for (int i = 0; i < partBrowserTable.getColumnCount(); i++) {
            partBrowserTable.getColumnModel().getColumn(i).setCellRenderer(new PartTableCellRenderer());
        }


        // Listen for intro on the search bar
        searchPartsEntryField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                doSearchParts();
            }
        });

        String[] inCBStringsLabour = {
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("NAME"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("DESCRIPTION"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PRICE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("MAXPRICE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("MINPRICE")};


        for (String s : inCBStringsLabour) {
            inLabourCombobox.addItem(s);
        }
        // Associate the table model
        labourBrowserTable.setModel(labourModel);

        for (int i = 0; i < labourBrowserTable.getColumnCount(); i++) {
            labourBrowserTable.getColumnModel().getColumn(i).setCellRenderer(new LabourTableCellRenderer());
        }


        // Listen for intro on the search bar
        searchLabourEntryField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                doSearchLabour();
            }
        });


        String[] inCBVehicleStrings = {java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("REGISTRATION"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("OWNER_NAME"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("OWNER_NIF"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("VIN"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("MAKE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("MODEL"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("YEAR"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("COLOUR"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("TYPE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("FUEL"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("INSURANCE_COMPANY_NAME"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("INSURANCE_NUMBER")};


        for (String s : inCBVehicleStrings) {
            inVehicleCombobox.addItem(s);
        }

        // Associate the table model
        vehicleBrowserTable.setModel(vehicleModel);

        for (int i = 0; i < vehicleBrowserTable.getColumnCount(); i++) {
            vehicleBrowserTable.getColumnModel().getColumn(i).setCellRenderer(new VehicleTableCellRenderer());
        }



        // Listen for intro on the search bar
        searchVehicleEntryField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                doSearchVehicles();
            }
        });






        setupOrderResponsibleComboBox();
        setupEstimateResponsibleComboBox();
        setupInvoiceResponsibleComboBox();
        setupOrderStatusComboBox();
        setupEstimateStatusComboBox();
        setupInvoiceStatusComboBox();
        setupInvoicePaymentResponsibleComboBox();


        DateModel orderDateModel = JDateComponentFactory.createDateModel(new DateMidnight());
        DateModel estimatedDateModel = JDateComponentFactory.createDateModel(new DateMidnight());
        DateModel finishDateModel = JDateComponentFactory.createDateModel(new DateMidnight());
        DateModel deliveryDateModel = JDateComponentFactory.createDateModel(new DateMidnight());

        orderDatePicker = (JDatePickerImpl) JDateComponentFactory.createJDatePicker(orderDateModel);
        estimatedDatePicker = (JDatePickerImpl) JDateComponentFactory.createJDatePicker(estimatedDateModel);
        finishDatePicker = (JDatePickerImpl) JDateComponentFactory.createJDatePicker(finishDateModel);
        deliveryDatePicker = (JDatePickerImpl) JDateComponentFactory.createJDatePicker(deliveryDateModel);


        orderDatePanel.add(orderDatePicker, BorderLayout.NORTH);
        estimatedDatePanel.add(estimatedDatePicker, BorderLayout.NORTH);
        finishDatePanel.add(finishDatePicker, BorderLayout.NORTH);
        deliveryDatePanel.add(deliveryDatePicker, BorderLayout.NORTH);



        DateModel estimateDateModel = JDateComponentFactory.createDateModel(new DateMidnight());
        DateModel estimateAcceptedDateModel = JDateComponentFactory.createDateModel(new DateMidnight());

        estimateDatePicker = (JDatePickerImpl) JDateComponentFactory.createJDatePicker(estimateDateModel);
        estimateAcceptedDatePicker = (JDatePickerImpl) JDateComponentFactory.createJDatePicker(estimateAcceptedDateModel);

        estimateDatePanel.add(estimateDatePicker, BorderLayout.NORTH);
        estimateAcceptedDatePanel.add(estimateAcceptedDatePicker, BorderLayout.NORTH);



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





        mainTabbedPane.setIconAt(0, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/toolbox.png")));
        mainTabbedPane.setIconAt(1, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/estimate.png")));
        mainTabbedPane.setIconAt(2, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/invoice.png")));
//        emailEditAddressTextField.getDocument().addUndoableEditListener(manager);


        orderNumberTextField.setDocument(new MaxLengthTextDocument(11));
        kilometresTextField.setDocument(new MaxLengthTextDocument(10));
        gasTankLevelTextField.setDocument(new MaxLengthTextDocument(100));
        discountTextField.setDocument(new MaxLengthTextDocument(5));
        
        estimateNumberTextField.setDocument(new MaxLengthTextDocument(11));
        
        invoiceNumberTextField.setDocument(new MaxLengthTextDocument(11));
        invoicePaymentMethodTextField.setDocument(new MaxLengthTextDocument(100));


        setupUndo();

        orderNumberTextField.getDocument().addDocumentListener(this);
        descriptionTextArea.getDocument().addDocumentListener(this);
        kilometresTextField.getDocument().addDocumentListener(this);
        gasTankLevelTextField.getDocument().addDocumentListener(this);
        notesTextArea.getDocument().addDocumentListener(this);
        estimateNumberTextField.getDocument().addDocumentListener(this);
        estimateNotesTextArea.getDocument().addDocumentListener(this);
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
        
        invoiceNumberTextField.getDocument().addDocumentListener(this);
        invoicePaymentMethodTextField.getDocument().addDocumentListener(this);
        invoiceNotesTextArea.getDocument().addDocumentListener(this);


        orderDatePicker.getModel().addPropertyChangeListener(this);
        estimatedDatePicker.getModel().addPropertyChangeListener(this);
        finishDatePicker.getModel().addPropertyChangeListener(this);
        deliveryDatePicker.getModel().addPropertyChangeListener(this);
        estimateDatePicker.getModel().addPropertyChangeListener(this);
        estimateAcceptedDatePicker.getModel().addPropertyChangeListener(this);
        invoiceDatePicker.getModel().addPropertyChangeListener(this);
        invoiceAcceptedDatePicker.getModel().addPropertyChangeListener(this);
        invoiceEstimatedPaymentDatePicker.getModel().addPropertyChangeListener(this);
        invoicePaymentDatePicker.getModel().addPropertyChangeListener(this);

        partsListModel.addTableModelListener(this);
        partsTable.getSelectionModel().addListSelectionListener(this);

        labourListModel.addTableModelListener(this);
        labourTable.getSelectionModel().addListSelectionListener(this);


        partBrowserTable.getSelectionModel().addListSelectionListener(this);
        labourBrowserTable.getSelectionModel().addListSelectionListener(this);
        vehicleBrowserTable.getSelectionModel().addListSelectionListener(this);

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
        searchPartsPanel = new javax.swing.JPanel();
        searchEntryPanel = new javax.swing.JPanel();
        searchPartsLabel = new javax.swing.JLabel();
        searchPartsEntryField = new javax.swing.JTextField();
        inPartsLabel = new javax.swing.JLabel();
        listAllPartsButton = new javax.swing.JButton();
        inPartsCombobox = new javax.swing.JComboBox();
        searchPartsButtonPanel = new javax.swing.JPanel();
        findPartsButton = new javax.swing.JButton();
        partsTablePanel = new javax.swing.JScrollPane();
        partBrowserTable = new javax.swing.JTable();
        labourBrowserPanel = new javax.swing.JPanel();
        searchLabourPanel = new javax.swing.JPanel();
        searchLabourEntryPanel = new javax.swing.JPanel();
        searchLabourLabel = new javax.swing.JLabel();
        searchLabourEntryField = new javax.swing.JTextField();
        inLabourLabel = new javax.swing.JLabel();
        listAlLabourlButton = new javax.swing.JButton();
        inLabourCombobox = new javax.swing.JComboBox();
        searchLabourButtonPanel = new javax.swing.JPanel();
        findLabourButton = new javax.swing.JButton();
        labourTablePanel = new javax.swing.JScrollPane();
        labourBrowserTable = new javax.swing.JTable();
        vehicleBrowserPanel = new javax.swing.JPanel();
        searchVehiclePanel = new javax.swing.JPanel();
        searchVehicleEntryPanel = new javax.swing.JPanel();
        searchVehicleLabel = new javax.swing.JLabel();
        searchVehicleEntryField = new javax.swing.JTextField();
        inVehicleLabel = new javax.swing.JLabel();
        listAllVehiclesButton = new javax.swing.JButton();
        inVehicleCombobox = new javax.swing.JComboBox();
        searchVehicleButtonPanel = new javax.swing.JPanel();
        findVehicleButton = new javax.swing.JButton();
        vehicleTablePanel = new javax.swing.JScrollPane();
        vehicleBrowserTable = new javax.swing.JTable();
        mainTabbedPane = new javax.swing.JTabbedPane();
        mainScrollPane = new javax.swing.JScrollPane();
        mainGeneralPanel = new javax.swing.JPanel();
        generalPanel = new javax.swing.JPanel();
        generalLabel = new javax.swing.JLabel();
        idLabel = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();
        orderNumberLabel = new javax.swing.JLabel();
        orderNumberTextField = new javax.swing.JTextField();
        descriptionLabel = new javax.swing.JLabel();
        orderStatusLabel = new javax.swing.JLabel();
        orderStatusComboBox = new javax.swing.JComboBox();
        vehicleLabel = new javax.swing.JLabel();
        kilometresLabel = new javax.swing.JLabel();
        kilometresTextField = new javax.swing.JTextField();
        gasTankLevelLabel = new javax.swing.JLabel();
        gasTankLevelTextField = new javax.swing.JTextField();
        orderDateLabel = new javax.swing.JLabel();
        estimatedDateLabel = new javax.swing.JLabel();
        finishDateLabel = new javax.swing.JLabel();
        deliveryDateLabel = new javax.swing.JLabel();
        responsibleLabel = new javax.swing.JLabel();
        responsibleComboBox = new javax.swing.JComboBox();
        vehiclePanel = new javax.swing.JPanel();
        vehicleTextField = new javax.swing.JTextField();
        selectVehicleButton = new javax.swing.JButton();
        orderDatePanel = new javax.swing.JPanel();
        estimatedDatePanel = new javax.swing.JPanel();
        finishDatePanel = new javax.swing.JPanel();
        deliveryDatePanel = new javax.swing.JPanel();
        descriptionScrollPane = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        notesPanel = new javax.swing.JPanel();
        notesLabel = new javax.swing.JLabel();
        notesScrollPane = new javax.swing.JScrollPane();
        notesTextArea = new javax.swing.JTextArea();
        estimateScrollPane = new javax.swing.JScrollPane();
        estimatePanel = new javax.swing.JPanel();
        estimateLabel = new javax.swing.JLabel();
        partsLabel = new javax.swing.JLabel();
        partsPanel = new javax.swing.JScrollPane();
        partsTable = new javax.swing.JTable();
        labourLabel = new javax.swing.JLabel();
        labourPanel = new javax.swing.JScrollPane();
        labourTable = new javax.swing.JTable();
        partsButtonsPanel = new javax.swing.JPanel();
        partsTotalLabel = new javax.swing.JLabel();
        partsTotalTextField = new javax.swing.JTextField();
        addPartsButton = new javax.swing.JButton();
        removeSelectedPartsButton = new javax.swing.JButton();
        partsTotalWithIVALabel = new javax.swing.JLabel();
        partsTotalWithIVATextField = new javax.swing.JTextField();
        labourButtonsPanel = new javax.swing.JPanel();
        labourTotalLabel = new javax.swing.JLabel();
        labourTotalTextField = new javax.swing.JTextField();
        addLabourButton = new javax.swing.JButton();
        removeSelectedLabourButton = new javax.swing.JButton();
        labourTotaWithIVALabel = new javax.swing.JLabel();
        labourTotalWithIVATextField = new javax.swing.JTextField();
        pricePanel = new javax.swing.JPanel();
        discountLabel = new javax.swing.JLabel();
        discountTextField = new javax.swing.JTextField();
        totalLabel = new javax.swing.JLabel();
        totalTextField = new javax.swing.JTextField();
        totalWithIVALabel = new javax.swing.JLabel();
        totalWithIVATextField = new javax.swing.JTextField();
        estimateNumberLabel = new javax.swing.JLabel();
        estimateNumberTextField = new javax.swing.JTextField();
        estimateDateLabel = new javax.swing.JLabel();
        estimateAcceptedDateLabel = new javax.swing.JLabel();
        estimateDatePanel = new javax.swing.JPanel();
        estimateAcceptedDatePanel = new javax.swing.JPanel();
        estimateResponsibleLabel = new javax.swing.JLabel();
        estimateResponsibleComboBox = new javax.swing.JComboBox();
        estimateStatusLabel = new javax.swing.JLabel();
        estimateNotesScrollPane = new javax.swing.JScrollPane();
        estimateNotesTextArea = new javax.swing.JTextArea();
        estimateNotesLabel = new javax.swing.JLabel();
        estimateStatusComboBox = new javax.swing.JComboBox();
        invoiceScrollPane = new javax.swing.JScrollPane();
        invoicePanel = new javax.swing.JPanel();
        mainInvoicePanel = new javax.swing.JPanel();
        invoiceLabel = new javax.swing.JLabel();
        invoiceNumberLabel = new javax.swing.JLabel();
        invoiceNumberTextField = new javax.swing.JTextField();
        invoiceDateLabel = new javax.swing.JLabel();
        invoiceEstimatedPaymentDateLabel = new javax.swing.JLabel();
        invoicePaymentDateLabel = new javax.swing.JLabel();
        invoiceacceptedDateLabel = new javax.swing.JLabel();
        invoicePaymentMethodLabel = new javax.swing.JLabel();
        invoicePaymentMethodTextField = new javax.swing.JTextField();
        invoiceStatusLabel = new javax.swing.JLabel();
        invoiceResponsibleLabel = new javax.swing.JLabel();
        invoiceResponsibleComboBox = new javax.swing.JComboBox();
        invoiceDatePanel = new javax.swing.JPanel();
        invoiceEstimatedPaymentDatePanel = new javax.swing.JPanel();
        invoicePaymentDatePanel = new javax.swing.JPanel();
        invoiceAcceptedDatePanel = new javax.swing.JPanel();
        invoiceStatusComboBox = new javax.swing.JComboBox();
        invoicePaymentResponsibleLabel = new javax.swing.JLabel();
        invoicePaymentResponsibleComboBox = new javax.swing.JComboBox();
        invoiceNotesPanel = new javax.swing.JPanel();
        invoiceNotesLabel = new javax.swing.JLabel();
        invoiceNotesScrollPane = new javax.swing.JScrollPane();
        invoiceNotesTextArea = new javax.swing.JTextArea();
        topToolBar = new javax.swing.JToolBar();
        saveRepairOrderButton = new javax.swing.JButton();
        reloadRepairOrderButton = new javax.swing.JButton();
        enableRepairOrderButton = new javax.swing.JButton();
        disableRepairOrderButton = new javax.swing.JButton();
        deleteRepairOrderButton = new javax.swing.JButton();
        printRepairOrderButton = new javax.swing.JButton();
        printEstimateButton = new javax.swing.JButton();
        printInvoiceButton = new javax.swing.JButton();

        partsBrowserPanel.setLayout(new java.awt.GridBagLayout());

        searchPartsPanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        searchPartsPanel.setLayout(new java.awt.GridBagLayout());

        searchEntryPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(searchPartsLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.searchPartsLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(searchPartsLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(searchPartsEntryField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(inPartsLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.inPartsLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(inPartsLabel, gridBagConstraints);

        listAllPartsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/group.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(listAllPartsButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.listAllPartsButton.text")); // NOI18N
        listAllPartsButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        listAllPartsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listAllPartsButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(listAllPartsButton, gridBagConstraints);

        inPartsCombobox.setPreferredSize(new java.awt.Dimension(79, 31));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(inPartsCombobox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchPartsPanel.add(searchEntryPanel, gridBagConstraints);

        searchPartsButtonPanel.setLayout(new java.awt.BorderLayout(5, 5));

        findPartsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/find32.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(findPartsButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.findPartsButton.text")); // NOI18N
        findPartsButton.setMargin(new java.awt.Insets(5, 15, 5, 15));
        findPartsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findPartsButtonActionPerformed(evt);
            }
        });
        searchPartsButtonPanel.add(findPartsButton, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 10, 10);
        searchPartsPanel.add(searchPartsButtonPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        partsBrowserPanel.add(searchPartsPanel, gridBagConstraints);

        partsTablePanel.setMinimumSize(new java.awt.Dimension(100, 100));

        partBrowserTable.setAutoCreateRowSorter(true);
        partBrowserTable.setDoubleBuffered(true);
        partBrowserTable.setFillsViewportHeight(true);
        partBrowserTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        partBrowserTable.setRowHeight(25);
        partBrowserTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        partBrowserTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        partsTablePanel.setViewportView(partBrowserTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(17, 17, 17, 17);
        partsBrowserPanel.add(partsTablePanel, gridBagConstraints);

        labourBrowserPanel.setLayout(new java.awt.GridBagLayout());

        searchLabourPanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        searchLabourPanel.setLayout(new java.awt.GridBagLayout());

        searchLabourEntryPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(searchLabourLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.searchLabourLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchLabourEntryPanel.add(searchLabourLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchLabourEntryPanel.add(searchLabourEntryField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(inLabourLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.inLabourLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchLabourEntryPanel.add(inLabourLabel, gridBagConstraints);

        listAlLabourlButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/group.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(listAlLabourlButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.listAlLabourlButton.text")); // NOI18N
        listAlLabourlButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        listAlLabourlButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listAlLabourlButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchLabourEntryPanel.add(listAlLabourlButton, gridBagConstraints);

        inLabourCombobox.setPreferredSize(new java.awt.Dimension(79, 31));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchLabourEntryPanel.add(inLabourCombobox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchLabourPanel.add(searchLabourEntryPanel, gridBagConstraints);

        searchLabourButtonPanel.setLayout(new java.awt.BorderLayout(5, 5));

        findLabourButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/find.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(findLabourButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.findLabourButton.text")); // NOI18N
        findLabourButton.setMargin(new java.awt.Insets(5, 15, 5, 15));
        findLabourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findLabourButtonActionPerformed(evt);
            }
        });
        searchLabourButtonPanel.add(findLabourButton, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 10, 10);
        searchLabourPanel.add(searchLabourButtonPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        labourBrowserPanel.add(searchLabourPanel, gridBagConstraints);

        labourTablePanel.setMinimumSize(new java.awt.Dimension(100, 100));

        labourBrowserTable.setAutoCreateRowSorter(true);
        labourBrowserTable.setDoubleBuffered(true);
        labourBrowserTable.setFillsViewportHeight(true);
        labourBrowserTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        labourBrowserTable.setRowHeight(25);
        labourBrowserTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        labourBrowserTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        labourTablePanel.setViewportView(labourBrowserTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(17, 17, 17, 17);
        labourBrowserPanel.add(labourTablePanel, gridBagConstraints);

        vehicleBrowserPanel.setLayout(new java.awt.GridBagLayout());

        searchVehiclePanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        searchVehiclePanel.setLayout(new java.awt.GridBagLayout());

        searchVehicleEntryPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(searchVehicleLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.searchVehicleLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchVehicleEntryPanel.add(searchVehicleLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchVehicleEntryPanel.add(searchVehicleEntryField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(inVehicleLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.inVehicleLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchVehicleEntryPanel.add(inVehicleLabel, gridBagConstraints);

        listAllVehiclesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/group.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(listAllVehiclesButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.listAllVehiclesButton.text")); // NOI18N
        listAllVehiclesButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        listAllVehiclesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listAllVehiclesButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchVehicleEntryPanel.add(listAllVehiclesButton, gridBagConstraints);

        inVehicleCombobox.setPreferredSize(new java.awt.Dimension(79, 31));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchVehicleEntryPanel.add(inVehicleCombobox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchVehiclePanel.add(searchVehicleEntryPanel, gridBagConstraints);

        searchVehicleButtonPanel.setLayout(new java.awt.BorderLayout(5, 5));

        findVehicleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/find32.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(findVehicleButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.findVehicleButton.text")); // NOI18N
        findVehicleButton.setMargin(new java.awt.Insets(5, 15, 5, 15));
        findVehicleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findVehicleButtonActionPerformed(evt);
            }
        });
        searchVehicleButtonPanel.add(findVehicleButton, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 10, 10);
        searchVehiclePanel.add(searchVehicleButtonPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        vehicleBrowserPanel.add(searchVehiclePanel, gridBagConstraints);

        vehicleTablePanel.setMinimumSize(new java.awt.Dimension(100, 100));

        vehicleBrowserTable.setAutoCreateRowSorter(true);
        vehicleBrowserTable.setDoubleBuffered(true);
        vehicleBrowserTable.setFillsViewportHeight(true);
        vehicleBrowserTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        vehicleBrowserTable.setRowHeight(25);
        vehicleBrowserTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        vehicleBrowserTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        vehicleTablePanel.setViewportView(vehicleBrowserTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(17, 17, 17, 17);
        vehicleBrowserPanel.add(vehicleTablePanel, gridBagConstraints);

        setLayout(new java.awt.GridBagLayout());

        mainTabbedPane.setDoubleBuffered(true);

        mainScrollPane.setBorder(null);

        mainGeneralPanel.setLayout(new java.awt.GridBagLayout());

        generalPanel.setLayout(new java.awt.GridBagLayout());

        generalLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        generalLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(generalLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.generalLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        generalPanel.add(generalLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(idLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.idLabel.text")); // NOI18N
        idLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(idLabel, gridBagConstraints);

        idTextField.setEditable(false);
        idTextField.setBackground(new java.awt.Color(232, 231, 231));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(idTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(orderNumberLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.orderNumberLabel.text")); // NOI18N
        orderNumberLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(orderNumberLabel, gridBagConstraints);

        orderNumberTextField.setDoubleBuffered(true);
        orderNumberTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        orderNumberTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(orderNumberTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(descriptionLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.descriptionLabel.text")); // NOI18N
        descriptionLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(descriptionLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(orderStatusLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.orderStatusLabel.text")); // NOI18N
        orderStatusLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(orderStatusLabel, gridBagConstraints);

        orderStatusComboBox.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(orderStatusComboBox, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.vehicleLabel.text")); // NOI18N
        vehicleLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(vehicleLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(kilometresLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.kilometresLabel.text")); // NOI18N
        kilometresLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(kilometresLabel, gridBagConstraints);

        kilometresTextField.setDoubleBuffered(true);
        kilometresTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        kilometresTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(kilometresTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(gasTankLevelLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.gasTankLevelLabel.text")); // NOI18N
        gasTankLevelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(gasTankLevelLabel, gridBagConstraints);

        gasTankLevelTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        gasTankLevelTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(gasTankLevelTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(orderDateLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.orderDateLabel.text")); // NOI18N
        orderDateLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(orderDateLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(estimatedDateLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.estimatedDateLabel.text")); // NOI18N
        estimatedDateLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(estimatedDateLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(finishDateLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.finishDateLabel.text")); // NOI18N
        finishDateLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(finishDateLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(deliveryDateLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.deliveryDateLabel.text")); // NOI18N
        deliveryDateLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(deliveryDateLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(responsibleLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.responsibleLabel.text")); // NOI18N
        responsibleLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(responsibleLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(responsibleComboBox, gridBagConstraints);

        vehiclePanel.setLayout(new java.awt.GridBagLayout());

        vehicleTextField.setEditable(false);
        vehicleTextField.setDoubleBuffered(true);
        vehicleTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehiclePanel.add(vehicleTextField, gridBagConstraints);

        selectVehicleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/car.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(selectVehicleButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.selectVehicleButton.text")); // NOI18N
        selectVehicleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectVehicleButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        vehiclePanel.add(selectVehicleButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        generalPanel.add(vehiclePanel, gridBagConstraints);

        orderDatePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(orderDatePanel, gridBagConstraints);

        estimatedDatePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(estimatedDatePanel, gridBagConstraints);

        finishDatePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(finishDatePanel, gridBagConstraints);

        deliveryDatePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(deliveryDatePanel, gridBagConstraints);

        descriptionTextArea.setColumns(20);
        descriptionTextArea.setRows(4);
        descriptionScrollPane.setViewportView(descriptionTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(descriptionScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        mainGeneralPanel.add(generalPanel, gridBagConstraints);

        notesPanel.setLayout(new java.awt.GridBagLayout());

        notesLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        notesLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(notesLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.notesLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        notesPanel.add(notesLabel, gridBagConstraints);

        notesTextArea.setColumns(20);
        notesTextArea.setRows(4);
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

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.mainScrollPane.TabConstraints.tabTitle"), mainScrollPane); // NOI18N

        estimateScrollPane.setBorder(null);

        estimatePanel.setLayout(new java.awt.GridBagLayout());

        estimateLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        estimateLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(estimateLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.estimateLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        estimatePanel.add(estimateLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(partsLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.partsLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(partsLabel, gridBagConstraints);

        partsPanel.setMinimumSize(new java.awt.Dimension(100, 100));
        partsPanel.setPreferredSize(new java.awt.Dimension(50, 50));

        partsTable.setAutoCreateRowSorter(true);
        partsTable.setDoubleBuffered(true);
        partsTable.setFillsViewportHeight(true);
        partsTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        partsTable.setRowHeight(25);
        partsTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        partsPanel.setViewportView(partsTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(partsPanel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(labourLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.labourLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(labourLabel, gridBagConstraints);

        labourPanel.setMinimumSize(new java.awt.Dimension(100, 100));
        labourPanel.setPreferredSize(new java.awt.Dimension(50, 50));

        labourTable.setAutoCreateRowSorter(true);
        labourTable.setDoubleBuffered(true);
        labourTable.setFillsViewportHeight(true);
        labourTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        labourTable.setRowHeight(25);
        labourTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        labourPanel.setViewportView(labourTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(labourPanel, gridBagConstraints);

        partsButtonsPanel.setLayout(new java.awt.GridBagLayout());

        partsTotalLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(partsTotalLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.partsTotalLabel.text")); // NOI18N
        partsTotalLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        partsButtonsPanel.add(partsTotalLabel, gridBagConstraints);

        partsTotalTextField.setEditable(false);
        partsTotalTextField.setText(org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.partsTotalTextField.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        partsButtonsPanel.add(partsTotalTextField, gridBagConstraints);

        addPartsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addPartsButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.addPartsButton.text")); // NOI18N
        addPartsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPartsButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        partsButtonsPanel.add(addPartsButton, gridBagConstraints);

        removeSelectedPartsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(removeSelectedPartsButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.removeSelectedPartsButton.text")); // NOI18N
        removeSelectedPartsButton.setEnabled(false);
        removeSelectedPartsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSelectedPartsButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        partsButtonsPanel.add(removeSelectedPartsButton, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(partsTotalWithIVALabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.partsTotalWithIVALabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        partsButtonsPanel.add(partsTotalWithIVALabel, gridBagConstraints);

        partsTotalWithIVATextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        partsButtonsPanel.add(partsTotalWithIVATextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(partsButtonsPanel, gridBagConstraints);

        labourButtonsPanel.setLayout(new java.awt.GridBagLayout());

        labourTotalLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(labourTotalLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.labourTotalLabel.text")); // NOI18N
        labourTotalLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        labourButtonsPanel.add(labourTotalLabel, gridBagConstraints);

        labourTotalTextField.setEditable(false);
        labourTotalTextField.setText(org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.labourTotalTextField.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        labourButtonsPanel.add(labourTotalTextField, gridBagConstraints);

        addLabourButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addLabourButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.addLabourButton.text")); // NOI18N
        addLabourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLabourButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        labourButtonsPanel.add(addLabourButton, gridBagConstraints);

        removeSelectedLabourButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(removeSelectedLabourButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.removeSelectedLabourButton.text")); // NOI18N
        removeSelectedLabourButton.setEnabled(false);
        removeSelectedLabourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSelectedLabourButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        labourButtonsPanel.add(removeSelectedLabourButton, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(labourTotaWithIVALabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.labourTotaWithIVALabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        labourButtonsPanel.add(labourTotaWithIVALabel, gridBagConstraints);

        labourTotalWithIVATextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        labourButtonsPanel.add(labourTotalWithIVATextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(labourButtonsPanel, gridBagConstraints);

        pricePanel.setLayout(new java.awt.GridBagLayout());

        discountLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(discountLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.discountLabel.text")); // NOI18N
        discountLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pricePanel.add(discountLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pricePanel.add(discountTextField, gridBagConstraints);

        totalLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(totalLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.totalLabel.text")); // NOI18N
        totalLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pricePanel.add(totalLabel, gridBagConstraints);

        totalTextField.setEditable(false);
        totalTextField.setText(org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.totalTextField.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pricePanel.add(totalTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(totalWithIVALabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.totalWithIVALabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pricePanel.add(totalWithIVALabel, gridBagConstraints);

        totalWithIVATextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pricePanel.add(totalWithIVATextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(pricePanel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(estimateNumberLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.estimateNumberLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(estimateNumberLabel, gridBagConstraints);

        estimateNumberTextField.setDoubleBuffered(true);
        estimateNumberTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        estimateNumberTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(estimateNumberTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(estimateDateLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.estimateDateLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(estimateDateLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(estimateAcceptedDateLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.estimateAcceptedDateLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(estimateAcceptedDateLabel, gridBagConstraints);

        estimateDatePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(estimateDatePanel, gridBagConstraints);

        estimateAcceptedDatePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(estimateAcceptedDatePanel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(estimateResponsibleLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.estimateResponsibleLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(estimateResponsibleLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(estimateResponsibleComboBox, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(estimateStatusLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.estimateStatusLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(estimateStatusLabel, gridBagConstraints);

        estimateNotesScrollPane.setPreferredSize(new java.awt.Dimension(30, 30));

        estimateNotesTextArea.setColumns(20);
        estimateNotesScrollPane.setViewportView(estimateNotesTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(estimateNotesScrollPane, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(estimateNotesLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.estimateNotesLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(estimateNotesLabel, gridBagConstraints);

        estimateStatusComboBox.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        estimatePanel.add(estimateStatusComboBox, gridBagConstraints);

        estimateScrollPane.setViewportView(estimatePanel);

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.estimateScrollPane.TabConstraints.tabTitle"), estimateScrollPane); // NOI18N

        invoiceScrollPane.setBorder(null);

        invoicePanel.setLayout(new java.awt.GridBagLayout());

        mainInvoicePanel.setLayout(new java.awt.GridBagLayout());

        invoiceLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        invoiceLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(invoiceLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.invoiceLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        mainInvoicePanel.add(invoiceLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(invoiceNumberLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.invoiceNumberLabel.text")); // NOI18N
        invoiceNumberLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoiceNumberLabel, gridBagConstraints);

        invoiceNumberTextField.setDoubleBuffered(true);
        invoiceNumberTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        invoiceNumberTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoiceNumberTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(invoiceDateLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.invoiceDateLabel.text")); // NOI18N
        invoiceDateLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoiceDateLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(invoiceEstimatedPaymentDateLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.invoiceEstimatedPaymentDateLabel.text")); // NOI18N
        invoiceEstimatedPaymentDateLabel.setPreferredSize(new java.awt.Dimension(120, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoiceEstimatedPaymentDateLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(invoicePaymentDateLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.invoicePaymentDateLabel.text")); // NOI18N
        invoicePaymentDateLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoicePaymentDateLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(invoiceacceptedDateLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.invoiceacceptedDateLabel.text")); // NOI18N
        invoiceacceptedDateLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoiceacceptedDateLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(invoicePaymentMethodLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.invoicePaymentMethodLabel.text")); // NOI18N
        invoicePaymentMethodLabel.setPreferredSize(new java.awt.Dimension(120, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoicePaymentMethodLabel, gridBagConstraints);

        invoicePaymentMethodTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        invoicePaymentMethodTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoicePaymentMethodTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(invoiceStatusLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.invoiceStatusLabel.text")); // NOI18N
        invoiceStatusLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoiceStatusLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(invoiceResponsibleLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.invoiceResponsibleLabel.text")); // NOI18N
        invoiceResponsibleLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoiceResponsibleLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoiceResponsibleComboBox, gridBagConstraints);

        invoiceDatePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoiceDatePanel, gridBagConstraints);

        invoiceEstimatedPaymentDatePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoiceEstimatedPaymentDatePanel, gridBagConstraints);

        invoicePaymentDatePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoicePaymentDatePanel, gridBagConstraints);

        invoiceAcceptedDatePanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoiceAcceptedDatePanel, gridBagConstraints);

        invoiceStatusComboBox.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoiceStatusComboBox, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(invoicePaymentResponsibleLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.invoicePaymentResponsibleLabel.text")); // NOI18N
        invoicePaymentResponsibleLabel.setPreferredSize(new java.awt.Dimension(120, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoicePaymentResponsibleLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainInvoicePanel.add(invoicePaymentResponsibleComboBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        invoicePanel.add(mainInvoicePanel, gridBagConstraints);

        invoiceNotesPanel.setLayout(new java.awt.GridBagLayout());

        invoiceNotesLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        invoiceNotesLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(invoiceNotesLabel, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.invoiceNotesLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        invoiceNotesPanel.add(invoiceNotesLabel, gridBagConstraints);

        invoiceNotesTextArea.setColumns(20);
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        invoicePanel.add(invoiceNotesPanel, gridBagConstraints);

        invoiceScrollPane.setViewportView(invoicePanel);

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.invoiceScrollPane.TabConstraints.tabTitle"), invoiceScrollPane); // NOI18N

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

        saveRepairOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/disk.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(saveRepairOrderButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.saveRepairOrderButton.text")); // NOI18N
        saveRepairOrderButton.setFocusable(false);
        saveRepairOrderButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        saveRepairOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        saveRepairOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        saveRepairOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveRepairOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(saveRepairOrderButton);

        reloadRepairOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/arrow_refresh.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(reloadRepairOrderButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.reloadRepairOrderButton.text")); // NOI18N
        reloadRepairOrderButton.setFocusable(false);
        reloadRepairOrderButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        reloadRepairOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        reloadRepairOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        reloadRepairOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadRepairOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(reloadRepairOrderButton);

        enableRepairOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/status_online.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(enableRepairOrderButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.enableRepairOrderButton.text")); // NOI18N
        enableRepairOrderButton.setFocusable(false);
        enableRepairOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        enableRepairOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        enableRepairOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableRepairOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(enableRepairOrderButton);

        disableRepairOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/status_offline.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(disableRepairOrderButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.disableRepairOrderButton.text")); // NOI18N
        disableRepairOrderButton.setFocusable(false);
        disableRepairOrderButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        disableRepairOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        disableRepairOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        disableRepairOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disableRepairOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(disableRepairOrderButton);

        deleteRepairOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteRepairOrderButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.deleteRepairOrderButton.text")); // NOI18N
        deleteRepairOrderButton.setFocusable(false);
        deleteRepairOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deleteRepairOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deleteRepairOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRepairOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deleteRepairOrderButton);

        printRepairOrderButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printRepairOrderButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.printRepairOrderButton.text")); // NOI18N
        printRepairOrderButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printRepairOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printRepairOrderButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printRepairOrderButton);

        printEstimateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printEstimateButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.printEstimateButton.text")); // NOI18N
        printEstimateButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printEstimateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printEstimateButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printEstimateButton);

        printInvoiceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/repair/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printInvoiceButton, org.openide.util.NbBundle.getMessage(RepairOrderEditorTopComponent.class, "RepairOrderEditorTopComponent.printInvoiceButton.text")); // NOI18N
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

    private void saveRepairOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveRepairOrderButtonActionPerformed
        saveRepairOrder();
    }//GEN-LAST:event_saveRepairOrderButtonActionPerformed

    private void disableRepairOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disableRepairOrderButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
//                disableRepairOrderButton.setVisible(false);
//                enableRepairOrderButton.setVisible(true);
                fillData();
                StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("DISABLED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("REPAIRORDER")));
            }

            @Override
            protected Void doInBackground() {
                if (updateRepairOrder()) {
                    repairOrder = controller.disableRepairOrder();
                }
                return null;
            }
        };

        worker.execute();

    }//GEN-LAST:event_disableRepairOrderButtonActionPerformed

    private void reloadRepairOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadRepairOrderButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SURE_RELOAD_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("REPAIRORDER")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("RELOAD"), NotifyDescriptor.YES_NO_OPTION);

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
                                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("REPAIR_ORDER_RELOAD_NOT_FOUND"),
                                    NotifyDescriptor.ERROR_MESSAGE);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("RELOADED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("REPAIRORDER")));
                }

                @Override
                protected Boolean doInBackground() {
                    repairOrder = controller.reloadRepairOrder();
                    if (repairOrder != null) {
                        controller.setRepairOrder(repairOrder);
                        return true;
                    } else {
                        return false;
                    }
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_reloadRepairOrderButtonActionPerformed

    private void enableRepairOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableRepairOrderButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
//                disableRepairOrderButton.setVisible(true);
//                enableRepairOrderButton.setVisible(false);
                fillData();
                StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ENABLED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("REPAIRORDER")));
            }

            @Override
            protected Void doInBackground() {
                if (updateRepairOrder()) {
                    repairOrder = controller.enableRepairOrder();
                }
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_enableRepairOrderButtonActionPerformed

    private void deleteRepairOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteRepairOrderButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("REPAIRORDER")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {


            RepairOrderLookup.getDefault().clear();

            RepairOrderBrowserTopComponent window = (RepairOrderBrowserTopComponent) WindowManager.getDefault().findTopComponent("RepairOrderBrowserTopComponent");

            window.deleteRepairOrder(repairOrder);
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    forceClose();
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("REPAIRORDER")));
                }

                @Override
                protected Void doInBackground() {
                    controller.setRepairOrder(repairOrder);
                    controller.deleteRepairOrder();
                    return null;
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_deleteRepairOrderButtonActionPerformed

    private void listAlLabourlButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listAlLabourlButtonActionPerformed
        doListAllLabour();
    }//GEN-LAST:event_listAlLabourlButtonActionPerformed

    private void findLabourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findLabourButtonActionPerformed
        doSearchLabour();
    }//GEN-LAST:event_findLabourButtonActionPerformed

    private void addPartsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPartsButtonActionPerformed
        browsePartsDialog = new DialogDescriptor(partsBrowserPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SELECT_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PART")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
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
    }//GEN-LAST:event_addPartsButtonActionPerformed

    private void addSelectedPart() {
        if (selectedPart != null) {
            PartLine part = new PartLine();
            part.setPart(selectedPart);
            part.setQuantity(1);
            part.setDiscount("0");

            repairOrder.addPart(part);
            partsListModel.addPartLine(part);
            modify();
        }
    }

    private void addSelectedLabour() {
        if (selectedLabour != null) {
            LabourLine lab = new LabourLine();
            lab.setLabour(selectedLabour);
            lab.setHours("1");
            lab.setDiscount("0");

            repairOrder.addLabour(lab);
            labourListModel.addLabourLine(lab);
            modify();
        }
    }

    private void removeSelectedPartsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSelectedPartsButtonActionPerformed
        int[] selectedRows = partsTable.getSelectedRows();
        PartLine par;
        for (int i = 0; i < selectedRows.length; i++) {
            par = partsListModel.getRow(partsTable.convertRowIndexToModel(selectedRows[i] - i));

            repairOrder.removePartLine(par);

            partsListModel.removeRow(partsTable.convertRowIndexToModel(selectedRows[i] - i));
        }
    }//GEN-LAST:event_removeSelectedPartsButtonActionPerformed

    private void addLabourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLabourButtonActionPerformed
        browseLabourDialog = new DialogDescriptor(labourBrowserPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SELECT_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("LABOUR")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() == DialogDescriptor.OK_OPTION) {
                            addSelectedLabour();
                            browseLabourDialog.setClosingOptions(null);
                            modify();
                        } else {
                            selectedLabour = null;
                        }
                        labourBrowserTable.clearSelection();
                    }
                });
        browseLabourDialog.setClosingOptions(new Object[]{DialogDescriptor.CANCEL_OPTION});
        browseLabourDialog.setValid(false);
        DialogDisplayer.getDefault().createDialog(browseLabourDialog);
        DialogDisplayer.getDefault().notify(browseLabourDialog);
    }//GEN-LAST:event_addLabourButtonActionPerformed

    private void removeSelectedLabourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSelectedLabourButtonActionPerformed
        int[] selectedRows = labourTable.getSelectedRows();
        LabourLine lab;
        for (int i = 0; i < selectedRows.length; i++) {
            lab = labourListModel.getRow(labourTable.convertRowIndexToModel(selectedRows[i] - i));

            repairOrder.removeLabourLine(lab);

            labourListModel.removeRow(labourTable.convertRowIndexToModel(selectedRows[i] - i));
        }
    }//GEN-LAST:event_removeSelectedLabourButtonActionPerformed

    private void listAllPartsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listAllPartsButtonActionPerformed
        doListAllParts();
    }//GEN-LAST:event_listAllPartsButtonActionPerformed

    private void findPartsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findPartsButtonActionPerformed
        doSearchParts();
    }//GEN-LAST:event_findPartsButtonActionPerformed

    private void listAllVehiclesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listAllVehiclesButtonActionPerformed
        doListAllVehicles();
    }//GEN-LAST:event_listAllVehiclesButtonActionPerformed

    private void findVehicleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findVehicleButtonActionPerformed
        doSearchVehicles();
    }//GEN-LAST:event_findVehicleButtonActionPerformed

    private void selectVehicleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectVehicleButtonActionPerformed
        browseVehicleDialog = new DialogDescriptor(vehicleBrowserPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SELECT_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("VEHICLE")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() == DialogDescriptor.OK_OPTION) {
                            updateSelectedVehicle();
                            browseVehicleDialog.setClosingOptions(null);
                        } else {
                            selectedVehicle = null;
                        }
                        vehicleBrowserTable.clearSelection();
                    }
                });
        browseVehicleDialog.setClosingOptions(new Object[]{DialogDescriptor.CANCEL_OPTION});
        browseVehicleDialog.setValid(false);
        DialogDisplayer.getDefault().createDialog(browseVehicleDialog);
        DialogDisplayer.getDefault().notify(browseVehicleDialog);
    }//GEN-LAST:event_selectVehicleButtonActionPerformed

    private void printRepairOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printRepairOrderButtonActionPerformed
        if (updateRepairOrder()) {
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
        }
    }//GEN-LAST:event_printRepairOrderButtonActionPerformed

    private void printEstimateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printEstimateButtonActionPerformed
        if (updateRepairOrder()) {
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                }

                @Override
                protected Void doInBackground() {
                    ReportPrinter printer = new ReportPrinter();
                    printer.printEstimate(repairOrder.getEstimate());
                    return null;
                }
            };
            worker.execute();
        }
    }//GEN-LAST:event_printEstimateButtonActionPerformed

    private void printInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printInvoiceButtonActionPerformed
        if (updateRepairOrder()) {
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                }

                @Override
                protected Void doInBackground() {
                    ReportPrinter printer = new ReportPrinter();
                    printer.printRepairInvoice(repairOrder.getInvoice());
                    return null;
                }
            };
            worker.execute();
        }
    }//GEN-LAST:event_printInvoiceButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addLabourButton;
    private javax.swing.JButton addPartsButton;
    private javax.swing.JButton deleteRepairOrderButton;
    private javax.swing.JLabel deliveryDateLabel;
    private javax.swing.JPanel deliveryDatePanel;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JScrollPane descriptionScrollPane;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JButton disableRepairOrderButton;
    private javax.swing.JLabel discountLabel;
    private javax.swing.JTextField discountTextField;
    private javax.swing.JButton enableRepairOrderButton;
    private javax.swing.JLabel estimateAcceptedDateLabel;
    private javax.swing.JPanel estimateAcceptedDatePanel;
    private javax.swing.JLabel estimateDateLabel;
    private javax.swing.JPanel estimateDatePanel;
    private javax.swing.JLabel estimateLabel;
    private javax.swing.JLabel estimateNotesLabel;
    private javax.swing.JScrollPane estimateNotesScrollPane;
    private javax.swing.JTextArea estimateNotesTextArea;
    private javax.swing.JLabel estimateNumberLabel;
    private javax.swing.JTextField estimateNumberTextField;
    private javax.swing.JPanel estimatePanel;
    private javax.swing.JComboBox estimateResponsibleComboBox;
    private javax.swing.JLabel estimateResponsibleLabel;
    private javax.swing.JScrollPane estimateScrollPane;
    private javax.swing.JComboBox estimateStatusComboBox;
    private javax.swing.JLabel estimateStatusLabel;
    private javax.swing.JLabel estimatedDateLabel;
    private javax.swing.JPanel estimatedDatePanel;
    private javax.swing.JButton findLabourButton;
    private javax.swing.JButton findPartsButton;
    private javax.swing.JButton findVehicleButton;
    private javax.swing.JLabel finishDateLabel;
    private javax.swing.JPanel finishDatePanel;
    private javax.swing.JLabel gasTankLevelLabel;
    private javax.swing.JTextField gasTankLevelTextField;
    private javax.swing.JLabel generalLabel;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JLabel idLabel;
    private javax.swing.JTextField idTextField;
    private javax.swing.JComboBox inLabourCombobox;
    private javax.swing.JLabel inLabourLabel;
    private javax.swing.JComboBox inPartsCombobox;
    private javax.swing.JLabel inPartsLabel;
    private javax.swing.JComboBox inVehicleCombobox;
    private javax.swing.JLabel inVehicleLabel;
    private javax.swing.JPanel invoiceAcceptedDatePanel;
    private javax.swing.JLabel invoiceDateLabel;
    private javax.swing.JPanel invoiceDatePanel;
    private javax.swing.JLabel invoiceEstimatedPaymentDateLabel;
    private javax.swing.JPanel invoiceEstimatedPaymentDatePanel;
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
    private javax.swing.JComboBox invoicePaymentResponsibleComboBox;
    private javax.swing.JLabel invoicePaymentResponsibleLabel;
    private javax.swing.JComboBox invoiceResponsibleComboBox;
    private javax.swing.JLabel invoiceResponsibleLabel;
    private javax.swing.JScrollPane invoiceScrollPane;
    private javax.swing.JComboBox invoiceStatusComboBox;
    private javax.swing.JLabel invoiceStatusLabel;
    private javax.swing.JLabel invoiceacceptedDateLabel;
    private javax.swing.JLabel kilometresLabel;
    private javax.swing.JTextField kilometresTextField;
    private javax.swing.JPanel labourBrowserPanel;
    private javax.swing.JTable labourBrowserTable;
    private javax.swing.JPanel labourButtonsPanel;
    private javax.swing.JLabel labourLabel;
    private javax.swing.JScrollPane labourPanel;
    private javax.swing.JTable labourTable;
    private javax.swing.JScrollPane labourTablePanel;
    private javax.swing.JLabel labourTotaWithIVALabel;
    private javax.swing.JLabel labourTotalLabel;
    private javax.swing.JTextField labourTotalTextField;
    private javax.swing.JTextField labourTotalWithIVATextField;
    private javax.swing.JButton listAlLabourlButton;
    private javax.swing.JButton listAllPartsButton;
    private javax.swing.JButton listAllVehiclesButton;
    private javax.swing.JPanel mainGeneralPanel;
    private javax.swing.JPanel mainInvoicePanel;
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
    private javax.swing.JTable partBrowserTable;
    private javax.swing.JPanel partsBrowserPanel;
    private javax.swing.JPanel partsButtonsPanel;
    private javax.swing.JLabel partsLabel;
    private javax.swing.JScrollPane partsPanel;
    private javax.swing.JTable partsTable;
    private javax.swing.JScrollPane partsTablePanel;
    private javax.swing.JLabel partsTotalLabel;
    private javax.swing.JTextField partsTotalTextField;
    private javax.swing.JLabel partsTotalWithIVALabel;
    private javax.swing.JTextField partsTotalWithIVATextField;
    private javax.swing.JPanel pricePanel;
    private javax.swing.JButton printEstimateButton;
    private javax.swing.JButton printInvoiceButton;
    private javax.swing.JButton printRepairOrderButton;
    private javax.swing.JButton reloadRepairOrderButton;
    private javax.swing.JButton removeSelectedLabourButton;
    private javax.swing.JButton removeSelectedPartsButton;
    private javax.swing.JComboBox responsibleComboBox;
    private javax.swing.JLabel responsibleLabel;
    private javax.swing.JButton saveRepairOrderButton;
    private javax.swing.JPanel searchEntryPanel;
    private javax.swing.JPanel searchLabourButtonPanel;
    private javax.swing.JTextField searchLabourEntryField;
    private javax.swing.JPanel searchLabourEntryPanel;
    private javax.swing.JLabel searchLabourLabel;
    private javax.swing.JPanel searchLabourPanel;
    private javax.swing.JPanel searchPartsButtonPanel;
    private javax.swing.JTextField searchPartsEntryField;
    private javax.swing.JLabel searchPartsLabel;
    private javax.swing.JPanel searchPartsPanel;
    private javax.swing.JPanel searchVehicleButtonPanel;
    private javax.swing.JTextField searchVehicleEntryField;
    private javax.swing.JPanel searchVehicleEntryPanel;
    private javax.swing.JLabel searchVehicleLabel;
    private javax.swing.JPanel searchVehiclePanel;
    private javax.swing.JButton selectVehicleButton;
    private javax.swing.JToolBar topToolBar;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JTextField totalTextField;
    private javax.swing.JLabel totalWithIVALabel;
    private javax.swing.JTextField totalWithIVATextField;
    private javax.swing.JPanel vehicleBrowserPanel;
    private javax.swing.JTable vehicleBrowserTable;
    private javax.swing.JLabel vehicleLabel;
    private javax.swing.JPanel vehiclePanel;
    private javax.swing.JScrollPane vehicleTablePanel;
    private javax.swing.JTextField vehicleTextField;
    // End of variables declaration//GEN-END:variables

    @Override
    public HelpCtx getHelpCtx() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return new HelpCtx("es.regueiro.easyrepair.ui.repair.editor");
    }

    public RepairOrder getRepairOrder() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return repairOrder;
    }

    public void forceClose() {
        modified = false;
        this.close();
    }

    @Override
    public void componentOpened() {
        invalid = false;
        result = RepairOrderLookup.getDefault().lookupResult(RepairOrder.class);
        result.removeLookupListener(this);
        mainTabbedPane.setSelectedIndex(0);

        Collection<? extends RepairOrder> repairOrderColId = result.allInstances();

        if (!repairOrderColId.isEmpty()) {
            repairOrder = controller.getRepairOrderById(repairOrderColId.iterator().next().getId());
            if (repairOrder == null) {
                NotifyDescriptor d = new NotifyDescriptor.Message(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("X_NOT_FOUND"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("REPAIRORDER")), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                close();
            } else {
                newRepairOrder = false;
            }
        } else {
            repairOrder = controller.newRepairOrder();
            newRepairOrder = true;
        }
        if (repairOrder != null) {
            reloadRepairOrderButton.setEnabled(!newRepairOrder);
            deleteRepairOrderButton.setEnabled(!newRepairOrder && SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
            enableRepairOrderButton.setVisible(false);
            disableRepairOrderButton.setEnabled(!newRepairOrder && SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
            controller.setRepairOrder(repairOrder);
            fillData();
            StatusDisplayer.getDefault().setStatusText("");
        }
    }

    @Override
    public void componentClosed() {
        manager.discardAllEdits();
        partsListModel.clear();
        labourListModel.clear();
    }

    @Override
    public void componentActivated() {
        checkPermissions();
    }

    @Override
    public boolean canClose() {
        if (modified) {
            NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("CHANGES_WILL_BE_LOST"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("REPAIRORDER")),
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("CLOSE"), NotifyDescriptor.YES_NO_OPTION);

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

            selectVehicleButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
            orderNumberTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
            descriptionTextArea.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
            orderStatusComboBox.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
            kilometresTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
            gasTankLevelTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
            orderDatePicker.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
            estimatedDatePicker.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
            finishDatePicker.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
            deliveryDatePicker.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
            responsibleComboBox.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
            notesTextArea.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));


            estimateNumberTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ESTIMATE_EDIT));
            estimateDatePicker.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ESTIMATE_EDIT));
            estimateAcceptedDatePicker.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ESTIMATE_EDIT));
            estimateStatusComboBox.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ESTIMATE_EDIT));
            estimateResponsibleComboBox.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ESTIMATE_EDIT));
            estimateNotesTextArea.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ESTIMATE_EDIT));
            addPartsButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ESTIMATE_EDIT));
            removeSelectedPartsButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ESTIMATE_EDIT));
            addLabourButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ESTIMATE_EDIT));
            removeSelectedLabourButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ESTIMATE_EDIT));
            discountTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ESTIMATE_EDIT));
            partsTable.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ESTIMATE_EDIT));
            labourTable.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ESTIMATE_EDIT));

            invoiceNumberTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_INVOICE_EDIT));
            invoiceDatePicker.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_INVOICE_EDIT));
            invoiceAcceptedDatePicker.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_INVOICE_EDIT));
            invoicePaymentDatePicker.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_INVOICE_EDIT));
            invoiceEstimatedPaymentDatePicker.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_INVOICE_EDIT));
            invoicePaymentMethodTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_INVOICE_EDIT));
            invoicePaymentResponsibleComboBox.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_INVOICE_EDIT));
            invoiceStatusComboBox.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_INVOICE_EDIT));
            invoiceResponsibleComboBox.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_INVOICE_EDIT));
            invoiceNotesTextArea.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_INVOICE_EDIT));

        } else {
            this.forceClose();
        }
    }

    private void fillData() {
        if (!invalid) {
            clearGeneral();
            clearEstimate();
            clearInvoice();
            clearPartsListTable();
            clearLabourListTable();

            if (repairOrder != null && repairOrder.getId() != null) {
                idTextField.setText(repairOrder.getId().toString());
                if (repairOrder.getOrderNumber() != null) {
                    orderNumberTextField.setText(repairOrder.getOrderNumber());
                }
                if (repairOrder.getOrderDate() != null) {
                    orderDatePicker.getModel().setDate(repairOrder.getOrderDate().getYear(), repairOrder.getOrderDate().getMonthOfYear() - 1, repairOrder.getOrderDate().getDayOfMonth());
                    orderDatePicker.getModel().setSelected(true);
                }

                if (repairOrder.getEstimatedDate() != null) {
                    estimatedDatePicker.getModel().setDate(repairOrder.getEstimatedDate().getYear(), repairOrder.getEstimatedDate().getMonthOfYear() - 1, repairOrder.getEstimatedDate().getDayOfMonth());
                    estimatedDatePicker.getModel().setSelected(true);
                }
                if (repairOrder.getFinishDate() != null) {
                    finishDatePicker.getModel().setDate(repairOrder.getFinishDate().getYear(), repairOrder.getFinishDate().getMonthOfYear() - 1, repairOrder.getFinishDate().getDayOfMonth());
                    finishDatePicker.getModel().setSelected(true);
                }
                if (repairOrder.getDeliveryDate() != null) {
                    deliveryDatePicker.getModel().setDate(repairOrder.getDeliveryDate().getYear(), repairOrder.getDeliveryDate().getMonthOfYear() - 1, repairOrder.getDeliveryDate().getDayOfMonth());
                    deliveryDatePicker.getModel().setSelected(true);
                }
                if (repairOrder.getDescription() != null) {
                    descriptionTextArea.setText(repairOrder.getDescription());
                }


                if (repairOrder.getVehicle() != null) {
                    selectedVehicle = repairOrder.getVehicle();
                    updateSelectedVehicle();
                }

                if (repairOrder.getNotes() != null) {
                    notesTextArea.setText(repairOrder.getNotes());
                }
                if (repairOrder.getKilometres() != null) {
                    kilometresTextField.setText(repairOrder.getKilometres());
                }
                if (repairOrder.getGasTankLevel() != null) {
                    gasTankLevelTextField.setText(repairOrder.getGasTankLevel());
                }
                if (repairOrder.getEnabled()) {
                    enableRepairOrderButton.setVisible(false);
                    disableRepairOrderButton.setVisible(true);
                } else {
                    enableRepairOrderButton.setVisible(true);
                    disableRepairOrderButton.setVisible(false);
                }

                if (repairOrder.getEstimate() != null) {

                    if (repairOrder.getEstimate().getEstimateNumber() != null) {
                        estimateNumberTextField.setText(repairOrder.getEstimate().getEstimateNumber());
                    }
                    if (repairOrder.getEstimate().getEstimateDate() != null) {
                        estimateDatePicker.getModel().setDate(repairOrder.getEstimate().getEstimateDate().getYear(), repairOrder.getEstimate().getEstimateDate().getMonthOfYear() - 1, repairOrder.getEstimate().getEstimateDate().getDayOfMonth());
                        estimateDatePicker.getModel().setSelected(true);
                    }

                    if (repairOrder.getEstimate().getAcceptedDate() != null) {
                        estimateAcceptedDatePicker.getModel().setDate(repairOrder.getEstimate().getAcceptedDate().getYear(), repairOrder.getEstimate().getAcceptedDate().getMonthOfYear() - 1, repairOrder.getEstimate().getAcceptedDate().getDayOfMonth());
                        estimateAcceptedDatePicker.getModel().setSelected(true);
                    }


                    if (repairOrder.getEstimate().getNotes() != null) {
                        estimateNotesTextArea.setText(repairOrder.getEstimate().getNotes());
                    }

                    if (repairOrder.getEstimate().getDiscount() != null) {
                        discountTextField.setText(repairOrder.getEstimate().getDiscount().toString());
                    }

                }
                fillPartsList();
                fillLabourList();

                if (repairOrder.getInvoice() != null) {
                    if (repairOrder.getInvoice().getInvoiceNumber() != null) {
                        invoiceNumberTextField.setText(repairOrder.getInvoice().getInvoiceNumber());
                    }


                    if (repairOrder.getInvoice().getInvoiceDate() != null) {
                        invoiceDatePicker.getModel().setDate(repairOrder.getInvoice().getInvoiceDate().getYear(), repairOrder.getInvoice().getInvoiceDate().getMonthOfYear() - 1, repairOrder.getInvoice().getInvoiceDate().getDayOfMonth());
                        invoiceDatePicker.getModel().setSelected(true);
                    }

                    if (repairOrder.getInvoice().getAcceptedDate() != null) {
                        invoiceAcceptedDatePicker.getModel().setDate(repairOrder.getInvoice().getAcceptedDate().getYear(), repairOrder.getInvoice().getAcceptedDate().getMonthOfYear() - 1, repairOrder.getInvoice().getAcceptedDate().getDayOfMonth());
                        invoiceAcceptedDatePicker.getModel().setSelected(true);
                    }
                    if (repairOrder.getInvoice().getEstimatedPaymentDate() != null) {
                        invoiceEstimatedPaymentDatePicker.getModel().setDate(repairOrder.getInvoice().getEstimatedPaymentDate().getYear(), repairOrder.getInvoice().getEstimatedPaymentDate().getMonthOfYear() - 1, repairOrder.getInvoice().getEstimatedPaymentDate().getDayOfMonth());
                        invoiceEstimatedPaymentDatePicker.getModel().setSelected(true);
                    }
                    if (repairOrder.getInvoice().getPaymentDate() != null) {
                        invoicePaymentDatePicker.getModel().setDate(repairOrder.getInvoice().getPaymentDate().getYear(), repairOrder.getInvoice().getPaymentDate().getMonthOfYear() - 1, repairOrder.getInvoice().getPaymentDate().getDayOfMonth());
                        invoicePaymentDatePicker.getModel().setSelected(true);
                    }
                    if (repairOrder.getInvoice().getPaymentMethod() != null) {
                        invoicePaymentMethodTextField.setText(repairOrder.getInvoice().getPaymentMethod());
                    }
                    if (repairOrder.getInvoice().getNotes() != null) {
                        invoiceNotesTextArea.setText(repairOrder.getInvoice().getNotes());
                    }
                }
            }

            fillOrderResponsibleCombobox();
            fillOrderStatusCombobox();
            fillEstimateStatusCombobox();
            fillInvoiceStatusCombobox();
            fillInvoiceResponsibleCombobox();
            fillEstimateResponsibleCombobox();
            fillInvoicePaymentResponsibleCombobox();

            manager.discardAllEdits();
            modified = invalid;
            saveRepairOrderButton.setEnabled(modified);
            deleteRepairOrderButton.setEnabled(!newRepairOrder && SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
            disableRepairOrderButton.setEnabled(!newRepairOrder && SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ORDER_EDIT));
            reloadRepairOrderButton.setEnabled(modified && !newRepairOrder);
        }
    }

    private void fillPartsList() {
        if (repairOrder.getPartsList() != null && !repairOrder.getPartsList().isEmpty()) {
            partsListModel.fill(repairOrder.getPartsList());
        } else {
            partsListModel.fill(null);
        }
        modified = false;
    }

    private void fillLabourList() {
        if (repairOrder.getLabourList() != null && !repairOrder.getLabourList().isEmpty()) {
            labourListModel.fill(repairOrder.getLabourList());
        } else {
            labourListModel.fill(null);
        }
    }

    private void clearPartsListTable() {
        partsListModel.fill(null);
    }

    private void clearLabourListTable() {
        labourListModel.fill(null);
    }

    private void clearGeneral() {
        idTextField.setText("");
        orderNumberTextField.setText("");
        descriptionTextArea.setText("");
        gasTankLevelTextField.setText("");
        kilometresTextField.setText("");
        vehicleTextField.setText("");
        selectedVehicle = null;
        orderDatePicker.getModel().setSelected(false);
        estimatedDatePicker.getModel().setSelected(false);
        finishDatePicker.getModel().setSelected(false);
        deliveryDatePicker.getModel().setSelected(false);
        notesTextArea.setText("");
    }

    private void clearEstimate() {
        estimateNumberTextField.setText("");
        estimateAcceptedDatePicker.getModel().setSelected(false);
        estimateDatePicker.getModel().setSelected(false);
        estimateNotesTextArea.setText("");
        discountTextField.setText("");
    }

    private void clearInvoice() {
        invoiceNumberTextField.setText("");
        invoiceDatePicker.getModel().setSelected(false);
        invoiceEstimatedPaymentDatePicker.getModel().setSelected(false);
        invoicePaymentDatePicker.getModel().setSelected(false);
        invoiceAcceptedDatePicker.getModel().setSelected(false);
        invoicePaymentMethodTextField.setText("");
        invoiceNotesTextArea.setText("");
    }

    private void saveRepairOrder() {
        if (updateRepairOrder()) {

            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SAVING"));

                @Override
                protected void done() {
                    fillData();
                    p.finish();
                }

                @Override
                protected Void doInBackground() {
                    p.start();
                    try {
                        controller.setRepairOrder(repairOrder);
                        controller.saveRepairOrder();
                        newRepairOrder = false;
                        invalid = false;
                    } catch (Exception e) {
                        if (e.getMessage().contains("was updated or deleted by another user")) {
                            NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("OVERWRITE_DIALOG"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("REPAIRORDER")),
                                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("X_MODIFIED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("REPAIRORDER")));
                            String options[] = new String[2];
                            options[0] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("OVERWRITE");
                            options[1] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("RELOAD");
                            d.setOptions(options);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                            if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("OVERWRITE"))) {
                                controller.overwriteRepairOrder();
                                repairOrder = controller.getRepairOrder();
                                invalid = false;
                            } else if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("RELOAD"))) {
                                reloadRepairOrderButton.doClick();
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

    private boolean updateRepairOrder() {
        if (!StringUtils.isBlank(vehicleTextField.getText())) {
            if (!StringUtils.isBlank(orderNumberTextField.getText())) {
                try {
                    repairOrder.setOrderNumber(orderNumberTextField.getText());
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    invalid = true;
                    return false;
                }
            } else {
                repairOrder.setOrderNumber(null);
            }
            if (orderDatePicker.getModel().isSelected()) {
                repairOrder.setOrderDate(new LocalDate(orderDatePicker.getModel().getValue()));
            } else {
                repairOrder.setOrderDate(null);
            }

            if (estimatedDatePicker.getModel().isSelected()) {
                repairOrder.setEstimatedDate(new LocalDate(estimatedDatePicker.getModel().getValue()));
            } else {
                repairOrder.setEstimatedDate(null);
            }

            if (finishDatePicker.getModel().isSelected()) {
                repairOrder.setFinishDate(new LocalDate(finishDatePicker.getModel().getValue()));
            } else {
                repairOrder.setFinishDate(null);
            }


            if (deliveryDatePicker.getModel().isSelected()) {
                repairOrder.setDeliveryDate(new LocalDate(deliveryDatePicker.getModel().getValue()));
            } else {
                repairOrder.setDeliveryDate(null);
            }

            if (!StringUtils.isBlank(descriptionTextArea.getText())) {
                repairOrder.setDescription(descriptionTextArea.getText());
            } else {
                repairOrder.setDescription(null);
            }

            if (!orderStatusComboBox.getSelectedItem().equals("")) {
                repairOrder.setStatus((String) orderStatusComboBox.getSelectedItem());
            } else {
                repairOrder.setStatus(null);
            }

            if (!StringUtils.isBlank(notesTextArea.getText())) {
                repairOrder.setNotes(notesTextArea.getText());
            } else {
                repairOrder.setNotes(null);
            }

            if (!StringUtils.isBlank(kilometresTextField.getText())) {
                try {
                    repairOrder.setKilometres(kilometresTextField.getText());
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    invalid = true;
                    return false;
                }
            } else {
                repairOrder.setKilometres(null);
            }
            if (!StringUtils.isBlank(gasTankLevelTextField.getText())) {
                repairOrder.setGasTankLevel(gasTankLevelTextField.getText());
            } else {
                repairOrder.setGasTankLevel(null);
            }


            repairOrder.setResponsible(orderResponsible);


            /*
             * Estimate
             */

            if (!StringUtils.isBlank(estimateNumberTextField.getText())) {
                try {
                    repairOrder.getEstimate().setEstimateNumber(estimateNumberTextField.getText());
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    invalid = true;
                    return false;
                }
            } else {
                repairOrder.getEstimate().setEstimateNumber(null);
            }

            if (estimateDatePicker.getModel().isSelected()) {
                repairOrder.getEstimate().setEstimateDate(new LocalDate(estimateDatePicker.getModel().getValue()));
            } else {
                repairOrder.getEstimate().setEstimateDate(null);
            }

            if (estimateAcceptedDatePicker.getModel().isSelected()) {
                repairOrder.getEstimate().setAcceptedDate(new LocalDate(estimateAcceptedDatePicker.getModel().getValue()));
            } else {
                repairOrder.getEstimate().setAcceptedDate(null);
            }

            if (!estimateStatusComboBox.getSelectedItem().equals("")) {
                repairOrder.getEstimate().setStatus((String) estimateStatusComboBox.getSelectedItem());
            } else {
                repairOrder.getEstimate().setStatus(null);
            }

            if (!StringUtils.isBlank(estimateNotesTextArea.getText())) {
                repairOrder.getEstimate().setNotes(estimateNotesTextArea.getText());
            } else {
                repairOrder.getEstimate().setNotes(null);
            }

            if (!StringUtils.isBlank(discountTextField.getText())) {
                try {
                    repairOrder.getEstimate().setDiscount(discountTextField.getText());
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    invalid = true;
                    return false;
                }
            } else {
                repairOrder.getEstimate().setDiscount(null);
            }

            repairOrder.getEstimate().setResponsible(estimateResponsible);

            /**
             * ************
             * Invoice
             */
            if (!StringUtils.isBlank(invoiceNumberTextField.getText())) {
                try {
                    repairOrder.getInvoice().setInvoiceNumber(invoiceNumberTextField.getText());
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    invalid = true;
                    return false;
                }
            } else {
                repairOrder.getInvoice().setInvoiceNumber(null);
            }

            if (invoiceDatePicker.getModel().isSelected()) {
                repairOrder.getInvoice().setInvoiceDate(new LocalDate(invoiceDatePicker.getModel().getValue()));
            } else {
                repairOrder.getInvoice().setInvoiceDate(null);
            }

            if (invoiceAcceptedDatePicker.getModel().isSelected()) {
                repairOrder.getInvoice().setAcceptedDate(new LocalDate(invoiceAcceptedDatePicker.getModel().getValue()));
            } else {
                repairOrder.getInvoice().setAcceptedDate(null);
            }

            if (invoiceEstimatedPaymentDatePicker.getModel().isSelected()) {
                repairOrder.getInvoice().setEstimatedPaymentDate(new LocalDate(invoiceEstimatedPaymentDatePicker.getModel().getValue()));
            } else {
                repairOrder.getInvoice().setEstimatedPaymentDate(null);
            }

            if (invoicePaymentDatePicker.getModel().isSelected()) {
                repairOrder.getInvoice().setPaymentDate(new LocalDate(invoicePaymentDatePicker.getModel().getValue()));
            } else {
                repairOrder.getInvoice().setPaymentDate(null);
            }

            if (!StringUtils.isBlank(invoicePaymentMethodTextField.getText())) {
                repairOrder.getInvoice().setPaymentMethod(invoicePaymentMethodTextField.getText());
            } else {
                repairOrder.getInvoice().setPaymentMethod(null);
            }

            if (!invoicePaymentResponsibleComboBox.getSelectedItem().equals("")) {
                repairOrder.getInvoice().setPaymentResponsible((String) invoicePaymentResponsibleComboBox.getSelectedItem());
            } else {
                repairOrder.getInvoice().setPaymentResponsible(null);
            }
            if (!invoiceStatusComboBox.getSelectedItem().equals("")) {
                repairOrder.getInvoice().setStatus((String) invoiceStatusComboBox.getSelectedItem());
            } else {
                repairOrder.getInvoice().setStatus(null);
            }

            if (!StringUtils.isBlank(invoiceNotesTextArea.getText())) {
                repairOrder.getInvoice().setNotes(invoiceNotesTextArea.getText());
            } else {
                repairOrder.getInvoice().setNotes(null);
            }
            repairOrder.getInvoice().setResponsible(invoiceResponsible);


            invalid = false;
            return true;

        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("MUST_SELECT_VEHICLE"), NotifyDescriptor.WARNING_MESSAGE);
            Object retval = DialogDisplayer.getDefault().notify(d);
            invalid = true;
            return false;
        }
    }

    private void updateSelectedVehicle() {
        repairOrder.setVehicle(selectedVehicle);
        String vehicleText = selectedVehicle.getRegistration() + " - ";
        if (selectedVehicle.getMake() != null) {
            vehicleText += selectedVehicle.getMake() + " ";
        }
        if (selectedVehicle.getModel() != null) {
            vehicleText += selectedVehicle.getModel() + " ";
        }
        vehicleText += "(";
        if (selectedVehicle.getYear() != null) {
            vehicleText += selectedVehicle.getYear() + " ";
        }

        if (selectedVehicle.getColour() != null) {
            vehicleText += selectedVehicle.getColour();
        }
        vehicleText += ") - ";

        if (selectedVehicle.getOwner() != null) {
            vehicleText += selectedVehicle.getOwner().getName() + " ";
            if (selectedVehicle.getOwner().getSurname() != null) {
                vehicleText += selectedVehicle.getOwner().getSurname() + " ";
            }
            if (selectedVehicle.getOwner().getNif() != null) {
                vehicleText += "(" + selectedVehicle.getOwner().getNif().getNumber() + ") - ";
            }
        }

        if (selectedVehicle.getInsuranceCompany() != null) {
            vehicleText += selectedVehicle.getInsuranceCompany().getName() + " ";
        }

        if (selectedVehicle.getInsuranceNumber() != null) {
            vehicleText += selectedVehicle.getInsuranceNumber();
        }

        vehicleTextField.setText(vehicleText);
        modify();
    }

    private void setupUndo() {
        kilometresTextField.getDocument().addUndoableEditListener(manager);
        notesTextArea.getDocument().addUndoableEditListener(manager);
        descriptionTextArea.getDocument().addUndoableEditListener(manager);
        discountTextField.getDocument().addUndoableEditListener(manager);
        //TODO add the rest of the textfields
    }

    private void removeUndo() {
        kilometresTextField.getDocument().removeUndoableEditListener(manager);
        notesTextArea.getDocument().removeUndoableEditListener(manager);
        descriptionTextArea.getDocument().removeUndoableEditListener(manager);
        discountTextField.getDocument().removeUndoableEditListener(manager);
    }

    private void modify() {
        modified = true;
        saveRepairOrderButton.setEnabled(modified);
        reloadRepairOrderButton.setEnabled(modified && !newRepairOrder);

        StatusDisplayer.getDefault().setStatusText("");
    }

    private void doSearchParts() {

        final SwingWorker<List<Part>, Void> worker = new SwingWorker<List<Part>, Void>() {
            private int column;
            ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SEARCHING"));

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
                int selected = inPartsCombobox.getSelectedIndex();
                String toSearch = searchPartsEntryField.getText();
                List<Part> list = new ArrayList<Part>();
                column = 0;
                if (!toSearch.isEmpty()) {
                    p.setDisplayName(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SEARCHING_FOR_X"), new Object[]{toSearch}));
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
                                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("X_INVALID"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PRICE")), NotifyDescriptor.WARNING_MESSAGE);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                        }
                        break;
                    case 5:
                        try {
                            list = partController.searchByMaxPrice(toSearch);
                            column = 4;
                        } catch (Exception e) {
                            NotifyDescriptor d = new NotifyDescriptor.Message(
                                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("X_INVALID"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PRICE")), NotifyDescriptor.WARNING_MESSAGE);
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

    private void doListAllParts() {

        final ProgressHandle p = ProgressHandleFactory.createHandle(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("LISTING_ALL_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PARTS")));

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

    private void doSearchLabour() {

        final SwingWorker<List<Labour>, Void> worker = new SwingWorker<List<Labour>, Void>() {
            private int column;
            ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SEARCHING"));

            @Override
            protected void done() {
                try {
                    List<Labour> list = get();
                    labourModel.fill(list);

                    if (column != 1) {
                        labourBrowserTable.getRowSorter().toggleSortOrder(1);
                    } else {
                        labourBrowserTable.getRowSorter().toggleSortOrder(2);
                    }

                    labourBrowserTable.getRowSorter().toggleSortOrder(column);
                    p.finish();
                } catch (InterruptedException ignore) {
                    System.out.println(ignore.getCause());
                } catch (ExecutionException ignore) {
                    System.out.println(ignore.getCause());
                }
            }

            @Override
            protected List<Labour> doInBackground() {
                int selected = inLabourCombobox.getSelectedIndex();
                String toSearch = searchLabourEntryField.getText();
                List<Labour> list;
                column = 0;
                if (!toSearch.isEmpty()) {
                    p.setDisplayName(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SEARCHING_FOR_X"), new Object[]{toSearch}));
                }
                p.start();
                switch (selected) {
                    case 0:
                        list = labourController.searchByName(toSearch);
                        column = 1;
                        break;
                    case 1:
                        list = labourController.searchByDescription(toSearch);
                        column = 3;
                        break;
                    case 2:
                        list = labourController.searchByPrice(toSearch);
                        column = 2;
                        break;
                    case 3:
                        list = labourController.searchByMaxPrice(toSearch);
                        column = 2;
                        break;
                    case 4:
                        list = labourController.searchByMinPrice(toSearch);
                        column = 2;
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

    private void doListAllLabour() {

        final ProgressHandle p = ProgressHandleFactory.createHandle(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("LISTING_ALL_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("LABOURS")));

        SwingWorker<List<Labour>, Void> worker = new SwingWorker<List<Labour>, Void>() {
            @Override
            protected void done() {
                try {
                    labourModel.fill(get());
                    labourBrowserTable.getRowSorter().toggleSortOrder(2);
                    labourBrowserTable.getRowSorter().toggleSortOrder(1);
                    p.finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected List<Labour> doInBackground() {
                List<Labour> list = labourController.listAll();
                return list;
            }
        };

        p.start();
        worker.execute();
    }

    private void doSearchVehicles() {

        final SwingWorker<List<Vehicle>, Void> worker = new SwingWorker<List<Vehicle>, Void>() {
            private int column;
            ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SEARCHING"));

            @Override
            protected void done() {
                try {
                    List<Vehicle> list = get();
                    vehicleModel.fill(list);

                    if (column != 1) {
                        vehicleBrowserTable.getRowSorter().toggleSortOrder(column - 1);
                    } else {
                        vehicleBrowserTable.getRowSorter().toggleSortOrder(2);
                    }

                    vehicleBrowserTable.getRowSorter().toggleSortOrder(column);
                    p.finish();
                } catch (InterruptedException ignore) {
                    System.out.println(ignore.getCause());
                } catch (ExecutionException ignore) {
                    System.out.println(ignore.getCause());
                }
            }

            @Override
            protected List<Vehicle> doInBackground() {
                int selected = inVehicleCombobox.getSelectedIndex();
                String toSearch = searchVehicleEntryField.getText();
                List<Vehicle> list;
                column = 0;
                if (!toSearch.isEmpty()) {
                    p.setDisplayName(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("SEARCHING_FOR_X"), new Object[]{toSearch}));
                }
                p.start();
                switch (selected) {
                    case 0:
                        list = vehicleController.searchByRegistration(toSearch);
                        column = 1;
                        break;
                    case 1:
                        list = vehicleController.searchByOwnerName(toSearch);
                        column = 1;
                        break;
                    case 2:
                        list = vehicleController.searchByOwnerNif(toSearch);
                        column = 1;
                        break;
                    case 3:
                        list = vehicleController.searchByVin(toSearch);
                        column = 2;
                        break;
                    case 4:
                        list = vehicleController.searchByMake(toSearch);
                        column = 3;
                        break;
                    case 5:
                        list = vehicleController.searchByModel(toSearch);
                        column = 1;
                        break;
                    case 6:
                        list = vehicleController.searchByYear(toSearch);
                        column = 1;
                        break;
                    case 7:
                        list = vehicleController.searchByColour(toSearch);
                        column = 1;
                        break;
                    case 8:
                        list = vehicleController.searchByType(toSearch);
                        column = 1;
                        break;
                    case 9:
                        list = vehicleController.searchByFuel(toSearch);
                        column = 1;
                        break;
                    case 10:
                        list = vehicleController.searchByInsuranceCompanyName(toSearch);
                        column = 1;
                        break;
                    case 11:
                        list = vehicleController.searchByInsuranceNumber(toSearch);
                        column = 1;
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

    private void doListAllVehicles() {

        final ProgressHandle p = ProgressHandleFactory.createHandle(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("LISTING_ALL_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("VEHICLES")));

        SwingWorker<List<Vehicle>, Void> worker = new SwingWorker<List<Vehicle>, Void>() {
            @Override
            protected void done() {
                try {
                    vehicleModel.fill(get());
                    vehicleBrowserTable.getRowSorter().toggleSortOrder(2);
                    vehicleBrowserTable.getRowSorter().toggleSortOrder(1);
                    p.finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected List<Vehicle> doInBackground() {
                List<Vehicle> list = vehicleController.listAll();
                return list;
            }
        };

        p.start();
        worker.execute();
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
    public void tableChanged(TableModelEvent event) {
        TableModel tm = (TableModel) event.getSource();

        if (tm.equals(partsTable.getModel())) {
            partsTotal = new BigDecimal("0");
            if (partsListModel.getRowCount() > 0) {
                List<PartLine> partsL = partsListModel.getPartLineList();
                for (PartLine p : partsL) {
                    BigDecimal partPrice = p.getPart().getPrice().multiply(new BigDecimal(p.getQuantity()));
                    BigDecimal partDiscount = new BigDecimal("1").subtract(p.getDiscount().divide(new BigDecimal("100")));

                    partsTotal = partsTotal.add(partPrice.multiply(partDiscount));
                }

                //TODO add options support to set currency
            }

            partsTotalTextField.setText(formatCurrency(partsTotal.setScale(2, RoundingMode.HALF_UP).toPlainString()));
            BigDecimal iva = new BigDecimal(NbPreferences.root().get("iva", "21")).divide(new BigDecimal("100"));
            partsTotalWithIVATextField.setText(formatCurrency(partsTotal.add(partsTotal.multiply(iva)).setScale(2, RoundingMode.HALF_UP).toPlainString()));
        } else if (tm.equals(labourTable.getModel())) {

            labourTotal = new BigDecimal("0");
            if (labourListModel.getRowCount() > 0) {
                List<LabourLine> labourL = labourListModel.getLabourLineList();
                for (LabourLine l : labourL) {
                    BigDecimal labourPrice = l.getLabour().getPrice().multiply(l.getHours());
                    BigDecimal labourDiscount = new BigDecimal("1").subtract(l.getDiscount().divide(new BigDecimal("100")));

                    labourTotal = labourTotal.add(labourPrice.multiply(labourDiscount));
                }

                //TODO add options support to set currency
            }
            labourTotalTextField.setText(formatCurrency(labourTotal.setScale(2, RoundingMode.HALF_UP).toPlainString()));
            BigDecimal iva = new BigDecimal(NbPreferences.root().get("iva", "21")).divide(new BigDecimal("100"));
            labourTotalWithIVATextField.setText(formatCurrency(labourTotal.add(labourTotal.multiply(iva)).setScale(2, RoundingMode.HALF_UP).toPlainString()));

        }

        modify();
        calculateTotal();
    }

    private void calculateTotal() {
        BigDecimal discount;

        if (!StringUtils.isBlank(discountTextField.getText())) {
            try {
                discount = new BigDecimal(discountTextField.getText());
            } catch (NumberFormatException e) {
                discount = new BigDecimal("0");
            }
        } else {
            discount = new BigDecimal("0");
        }
        BigDecimal totalDiscount = new BigDecimal("1").subtract(discount.divide(new BigDecimal("100")));
        BigDecimal iva = new BigDecimal(NbPreferences.root().get("iva", "21")).divide(new BigDecimal("100"));

        BigDecimal total = (partsTotal.add(labourTotal)).multiply(totalDiscount);


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
                    removeSelectedPartsButton.setEnabled(true && SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ESTIMATE_EDIT));
                } else {
                    removeSelectedPartsButton.setEnabled(false);
                }
            }
        } else if (lsm.equals(labourTable.getSelectionModel())) {
            if (!event.getValueIsAdjusting()) {
                if (labourTable.getSelectedRow() > -1) {
                    removeSelectedLabourButton.setEnabled(true && SecurityManager.getDefault().userHasPrivilege(Privilege.REPAIR_ESTIMATE_EDIT));
                } else {
                    removeSelectedLabourButton.setEnabled(false);
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
        } else if (lsm.equals(labourBrowserTable.getSelectionModel())) {

            if (!event.getValueIsAdjusting()) {
                if (labourBrowserTable.getSelectedRow() > -1) {
                    selectedLabour = labourModel.getRow(labourBrowserTable.convertRowIndexToModel(labourBrowserTable.getSelectedRow()));
                    browseLabourDialog.setValid(true);
                } else {
                    selectedLabour = null;
                    browseLabourDialog.setValid(false);
                }
            }
        } else if (lsm.equals(vehicleBrowserTable.getSelectionModel())) {

            if (!event.getValueIsAdjusting()) {
                if (vehicleBrowserTable.getSelectedRow() > -1) {
                    selectedVehicle = vehicleModel.getRow(vehicleBrowserTable.convertRowIndexToModel(vehicleBrowserTable.getSelectedRow()));
                    browseVehicleDialog.setValid(true);
                } else {
                    selectedVehicle = null;
                    browseVehicleDialog.setValid(false);
                }
            }
        }
    }

    private void fillInvoicePaymentResponsibleCombobox() {
        String res = null;
        if (repairOrder.getInvoice() != null) {
            res = repairOrder.getInvoice().getPaymentResponsible();
        }

        invoicePaymentResponsibleComboBox.removeAllItems();

        String[] cbStatus = {java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("CLIENT"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("INSURANCECOMPANY")
        };

        for (String s : cbStatus) {
            invoicePaymentResponsibleComboBox.addItem(s);
        }
        invoicePaymentResponsibleComboBox.setSelectedIndex(0);

        if (res != null) {
            for (String s : cbStatus) {
                if (res.equals(s)) {
                    invoicePaymentResponsibleComboBox.setSelectedItem(s);
                }
            }
        }
    }

    private void setupInvoicePaymentResponsibleComboBox() {

        invoicePaymentResponsibleComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                modify();
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
            if (repairOrder.getResponsible() != null && repairOrder.getResponsible().equals(e)) {
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

    private void fillOrderStatusCombobox() {
        String stat = repairOrder.getStatus();
//        for (ActionListener a : orderStatusComboBox.getActionListeners()) {
//            orderStatusComboBox.removeActionListener(a);
//        }
        orderStatusComboBox.removeAllItems();

        String[] cbStatus = {java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PENDING_REPAIR"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PENDING_ESTIMATE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PENDING_INVOICE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PENDING_PAYMENT"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("REPAIR_FINISHED"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PAYED")
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

    private void fillEstimateStatusCombobox() {
        String stat = null;
        if (repairOrder.getEstimate() != null) {
            stat = repairOrder.getEstimate().getStatus();
        }
        estimateStatusComboBox.removeAllItems();

        String[] cbStatus = {java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PENDING"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("ESTIMATEACCEPTED")
        };

        for (String s : cbStatus) {
            estimateStatusComboBox.addItem(s);
        }
        estimateStatusComboBox.setSelectedIndex(0);

        if (stat != null) {
            boolean found = false;
            for (String s : cbStatus) {
                if (stat.equals(s)) {
                    found = true;
                    estimateStatusComboBox.setSelectedItem(s);
                }
            }
            if (!found) {
                estimateStatusComboBox.addItem(stat);
                estimateStatusComboBox.setSelectedItem(stat);
            }
        }
    }

    private void setupEstimateStatusComboBox() {

        estimateStatusComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                modify();
            }
        });
    }

    private void fillInvoiceStatusCombobox() {
        String stat = null;
        if (repairOrder.getInvoice() != null) {
            stat = repairOrder.getInvoice().getStatus();
        }
        invoiceStatusComboBox.removeAllItems();

        String[] cbStatus = {java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PENDING"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("INVOICEACCEPTED"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("PAYED")
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

    private void fillEstimateResponsibleCombobox() {
        List<Employee> listEmployee = employeeController.listAll();
        estimateResponsibleComboBox.removeAllItems();

        estimateResponsible = null;
        for (Employee e : listEmployee) {
            EmployeeBox box = new EmployeeBox(e);
            if (estimateResponsible == null) {
                estimateResponsible = box.employee;
            }
            estimateResponsibleComboBox.addItem(box);
            if (repairOrder.getEstimate() != null && repairOrder.getEstimate().getResponsible() != null && repairOrder.getEstimate().getResponsible().equals(e)) {
                estimateResponsibleComboBox.setSelectedItem(box);
                estimateResponsible = box.employee;
            }
        }
    }

    private void setupEstimateResponsibleComboBox() {
        estimateResponsibleComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    estimateResponsible = ((EmployeeBox) e.getItem()).employee;
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
            if (repairOrder.getInvoice() != null && repairOrder.getInvoice().getResponsible() != null && repairOrder.getInvoice().getResponsible().equals(e)) {
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
    
    public void viewEstimate(Estimate estimate) {
        if (repairOrder != null && estimate != null && estimate.getOrder().equals(repairOrder)) {
            mainTabbedPane.setSelectedIndex(1);
        }
    }
    
    public void viewInvoice(Invoice invoice) {
        if (repairOrder != null && invoice != null && repairOrder.getInvoice().equals(invoice)) {
            mainTabbedPane.setSelectedIndex(2);
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
                ret = ret + " (" + java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/repair/Bundle").getString("DISABLED") + ")";
            }
            return ret;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        modify();
    }

    public class VehicleTableCellRenderer extends DefaultTableCellRenderer {

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
