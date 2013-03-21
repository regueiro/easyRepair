package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.login.SecurityManager;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import es.regueiro.easyrepair.api.stock.controller.SupplierController;
import es.regueiro.easyrepair.model.stock.Supplier;
import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.NIF;
import es.regueiro.easyrepair.model.shared.Phone;
import es.regueiro.easyrepair.model.user.Privilege;
import es.regueiro.easyrepair.reports.ReportPrinter;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import javax.swing.ActionMap;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.DefaultEditorKit;
import org.apache.commons.lang3.StringUtils;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.*;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.stock//SupplierEditor//EN",
autostore = false)
@TopComponent.Description(iconBase = "es/regueiro/easyrepair/ui/stock/icons/supplier.png", preferredID = "SupplierEditorTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 21, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.stock.SupplierEditorTopComponent")
@TopComponent.OpenActionRegistration(displayName = "#CTL_SupplierEditorAction",
preferredID = "SupplierEditorTopComponent")
@Messages({
    "CTL_SupplierEditorAction=Editor de proveedores",
    "CTL_SupplierEditorTopComponent=Editor de proveedores",
    "HINT_SupplierEditorTopComponent=Esta es una ventana del editor de proveedores"
})
public final class SupplierEditorTopComponent extends TopComponent implements LookupListener, DocumentListener {

    private Supplier supplier = null;
    private Supplier oldSupplier = null;
    private Lookup.Result<Supplier> result;
    private InstanceContent content = new InstanceContent();
    private UndoRedo.Manager manager = new UndoRedo.Manager();
    private SupplierController controller = Lookup.getDefault().lookup(SupplierController.class);
    private DialogDescriptor editEmaildialog;
    private DialogDescriptor editAddressdialog;
    private DialogDescriptor editPhonedialog;
    private boolean modified = false;
    private boolean newSupplier = true;
    private boolean invalid = false;
    private PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public SupplierEditorTopComponent() {
        initComponents();
        setName(Bundle.CTL_SupplierEditorTopComponent());
        setToolTipText(Bundle.HINT_SupplierEditorTopComponent());
        this.setFocusable(true);
        associateLookup(new AbstractLookup(content));
        ActionMap actionMap = getActionMap();
        actionMap.put(DefaultEditorKit.copyAction, new DefaultEditorKit.CopyAction());
        actionMap.put(DefaultEditorKit.cutAction, new DefaultEditorKit.CutAction());
        actionMap.put(DefaultEditorKit.pasteAction, new DefaultEditorKit.PasteAction());
        content.add(actionMap);


        setupAddressViewComboBox();
        setupEmailViewComboBox();
        setupPhoneViewComboBox();


        mainTabbedPane.setIconAt(1, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/book.png")));
        mainTabbedPane.setIconAt(3, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/email.png")));
        mainTabbedPane.setIconAt(2, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/phone.png")));
        mainTabbedPane.setIconAt(0, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/group.png")));
//        emailEditAddressTextField.getDocument().addUndoableEditListener(manager);


        nameTextField.setDocument(new MaxLengthTextDocument(100));
        nifTextField.setDocument(new MaxLengthTextDocument(9));
        webTextField.setDocument(new MaxLengthTextDocument(100));
        categoryTextField.setDocument(new MaxLengthTextDocument(100));
        shippingMethodTextField.setDocument(new MaxLengthTextDocument(100));
        paymentMethodTextField.setDocument(new MaxLengthTextDocument(100));



        emailEditLabelTextField.setDocument(new MaxLengthTextDocument(100));
        emailEditAddressTextField.setDocument(new MaxLengthTextDocument(150));

        phoneEditLabelTextField.setDocument(new MaxLengthTextDocument(100));
        phoneEditNumberTextField.setDocument(new MaxLengthTextDocument(20));

        addressEditLabelTextField.setDocument(new MaxLengthTextDocument(100));
        addressEditStreetTextField.setDocument(new MaxLengthTextDocument(200));
        addressEditCityTextField.setDocument(new MaxLengthTextDocument(100));
        addressEditCountryTextField.setDocument(new MaxLengthTextDocument(100));
        addressEditPostalCodeTextField.setDocument(new MaxLengthTextDocument(10));
        addressEditProvinceTextField.setDocument(new MaxLengthTextDocument(100));

        setupUndo();


        nameTextField.getDocument().addDocumentListener(this);
        nifTextField.getDocument().addDocumentListener(this);
        webTextField.getDocument().addDocumentListener(this);
        categoryTextField.getDocument().addDocumentListener(this);
        shippingMethodTextField.getDocument().addDocumentListener(this);
        paymentMethodTextField.getDocument().addDocumentListener(this);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        addressEditPanel = new javax.swing.JPanel();
        addressEditLabelLabel = new javax.swing.JLabel();
        addressEditStreetLabel = new javax.swing.JLabel();
        addressEditCityLabel = new javax.swing.JLabel();
        addressEditProvinceLabel = new javax.swing.JLabel();
        addressEditPostalCodeLabel = new javax.swing.JLabel();
        addressEditCountryLabel = new javax.swing.JLabel();
        addressEditLabelTextField = new javax.swing.JTextField();
        addressEditStreetTextField = new javax.swing.JTextField();
        addressEditCityTextField = new javax.swing.JTextField();
        addressEditProvinceTextField = new javax.swing.JTextField();
        addressEditPostalCodeTextField = new javax.swing.JTextField();
        addressEditCountryTextField = new javax.swing.JTextField();
        addressEditNotesLabel = new javax.swing.JLabel();
        addressEditNotesScrollPane = new javax.swing.JScrollPane();
        addressEditNotesTextArea = new javax.swing.JTextArea();
        addressEditLabel = new javax.swing.JLabel();
        phoneEditPanel = new javax.swing.JPanel();
        phoneEditLabelLabel = new javax.swing.JLabel();
        phoneEditNumberLabel = new javax.swing.JLabel();
        phoneEditLabelTextField = new javax.swing.JTextField();
        phoneEditNumberTextField = new javax.swing.JTextField();
        phoneEditNotesLabel = new javax.swing.JLabel();
        phoneEditNotesScrollPane = new javax.swing.JScrollPane();
        phoneEditNotesTextArea = new javax.swing.JTextArea();
        phoneEditLabel = new javax.swing.JLabel();
        emailEditPanel = new javax.swing.JPanel();
        emailEditLabelLabel = new javax.swing.JLabel();
        emailEditAddressLabel = new javax.swing.JLabel();
        emailEditLabelTextField = new javax.swing.JTextField();
        emailEditAddressTextField = new javax.swing.JTextField();
        emailEditNotesLabel = new javax.swing.JLabel();
        emailEditNotesScrollPane = new javax.swing.JScrollPane();
        emailEditNotesTextArea = new javax.swing.JTextArea();
        emailEditLabel = new javax.swing.JLabel();
        mainTabbedPane = new javax.swing.JTabbedPane();
        mainScrollPane = new javax.swing.JScrollPane();
        mainGeneralPanel = new javax.swing.JPanel();
        generalPanel = new javax.swing.JPanel();
        generalLabel = new javax.swing.JLabel();
        idLabel = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        nifLabel = new javax.swing.JLabel();
        nifTextField = new javax.swing.JTextField();
        categoryLabel = new javax.swing.JLabel();
        categoryTextField = new javax.swing.JTextField();
        paymentMethodLabel = new javax.swing.JLabel();
        paymentMethodTextField = new javax.swing.JTextField();
        webLabel = new javax.swing.JLabel();
        webTextField = new javax.swing.JTextField();
        shippingMethodLabel = new javax.swing.JLabel();
        shippingMethodTextField = new javax.swing.JTextField();
        notesPanel = new javax.swing.JPanel();
        notesLabel = new javax.swing.JLabel();
        notesScrollPane = new javax.swing.JScrollPane();
        notesTextArea = new javax.swing.JTextArea();
        addressScrollPane = new javax.swing.JScrollPane();
        addressViewPanel = new javax.swing.JPanel();
        addressViewLabelLabel = new javax.swing.JLabel();
        addressViewStreetLabel = new javax.swing.JLabel();
        addressViewCityLabel = new javax.swing.JLabel();
        addressViewProvinceLabel = new javax.swing.JLabel();
        addressViewPostalCodeLabel = new javax.swing.JLabel();
        addressViewCountryLabel = new javax.swing.JLabel();
        addressViewLabelTextField = new javax.swing.JTextField();
        addressViewStreetTextField = new javax.swing.JTextField();
        addressViewCityTextField = new javax.swing.JTextField();
        addressViewProvinceTextField = new javax.swing.JTextField();
        addressViewPostalCodeTextField = new javax.swing.JTextField();
        addressViewCountryTextField = new javax.swing.JTextField();
        addressViewNotesLabel = new javax.swing.JLabel();
        addressViewNotesScrollPane = new javax.swing.JScrollPane();
        addressViewNotesTextArea = new javax.swing.JTextArea();
        addressViewLabel = new javax.swing.JLabel();
        addressViewActionsPanel = new javax.swing.JPanel();
        addressViewComboBox = new javax.swing.JComboBox();
        addressViewButtonsPanel = new javax.swing.JPanel();
        addressViewAddButton = new javax.swing.JButton();
        addressViewEditButton = new javax.swing.JButton();
        addressViewDeleteButton = new javax.swing.JButton();
        phoneScrollPane = new javax.swing.JScrollPane();
        phoneViewPanel = new javax.swing.JPanel();
        phoneViewLabelLabel = new javax.swing.JLabel();
        phoneViewNumberLabel = new javax.swing.JLabel();
        phoneViewLabelTextField = new javax.swing.JTextField();
        phoneViewNumberTextField = new javax.swing.JTextField();
        phoneViewNotesLabel = new javax.swing.JLabel();
        phoneViewNotesScrollPane = new javax.swing.JScrollPane();
        phoneViewNotesTextArea = new javax.swing.JTextArea();
        phoneViewLabel = new javax.swing.JLabel();
        phoneViewActionsPanel = new javax.swing.JPanel();
        phoneViewComboBox = new javax.swing.JComboBox();
        phoneViewButtonsPanel = new javax.swing.JPanel();
        phoneViewAddButton = new javax.swing.JButton();
        phoneViewEditButton = new javax.swing.JButton();
        phoneViewDeleteButton = new javax.swing.JButton();
        emailScrollPane = new javax.swing.JScrollPane();
        emaiViewlPanel = new javax.swing.JPanel();
        emailViewLabelLabel = new javax.swing.JLabel();
        emailViewAddressLabel = new javax.swing.JLabel();
        emailViewLabelTextField = new javax.swing.JTextField();
        emailViewAddressTextField = new javax.swing.JTextField();
        emailViewNotesLabel = new javax.swing.JLabel();
        emailViewNotesScrollPane = new javax.swing.JScrollPane();
        emailViewNotesTextArea = new javax.swing.JTextArea();
        emailLabel = new javax.swing.JLabel();
        emailViewActionsPanel = new javax.swing.JPanel();
        emailViewComboBox = new javax.swing.JComboBox();
        emailViewButtonsPanel = new javax.swing.JPanel();
        emailViewAddButton = new javax.swing.JButton();
        emailViewEditButton = new javax.swing.JButton();
        emailViewDeleteButton = new javax.swing.JButton();
        topToolBar = new javax.swing.JToolBar();
        saveSupplierButton = new javax.swing.JButton();
        reloadSupplierButton = new javax.swing.JButton();
        enableSupplierButton = new javax.swing.JButton();
        disableSupplierButton = new javax.swing.JButton();
        deleteSupplierButton = new javax.swing.JButton();
        printSupplierButton = new javax.swing.JButton();

        addressEditPanel.setPreferredSize(new java.awt.Dimension(400, 400));
        addressEditPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(addressEditLabelLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressEditLabelLabel.text")); // NOI18N
        addressEditLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditStreetLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressEditStreetLabel.text")); // NOI18N
        addressEditStreetLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditStreetLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditCityLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressEditCityLabel.text")); // NOI18N
        addressEditCityLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditCityLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditProvinceLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressEditProvinceLabel.text")); // NOI18N
        addressEditProvinceLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditProvinceLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditPostalCodeLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressEditPostalCodeLabel.text")); // NOI18N
        addressEditPostalCodeLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditPostalCodeLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditCountryLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressEditCountryLabel.text")); // NOI18N
        addressEditCountryLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditCountryLabel, gridBagConstraints);

        addressEditLabelTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        addressEditLabelTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditLabelTextField, gridBagConstraints);

        addressEditStreetTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        addressEditStreetTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditStreetTextField, gridBagConstraints);

        addressEditCityTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        addressEditCityTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditCityTextField, gridBagConstraints);

        addressEditProvinceTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        addressEditProvinceTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditProvinceTextField, gridBagConstraints);

        addressEditPostalCodeTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        addressEditPostalCodeTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditPostalCodeTextField, gridBagConstraints);

        addressEditCountryTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        addressEditCountryTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditCountryTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditNotesLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressEditNotesLabel.text")); // NOI18N
        addressEditNotesLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditNotesLabel, gridBagConstraints);

        addressEditNotesTextArea.setColumns(20);
        addressEditNotesTextArea.setRows(5);
        addressEditNotesScrollPane.setViewportView(addressEditNotesTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditNotesScrollPane, gridBagConstraints);

        addressEditLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        addressEditLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(addressEditLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressEditLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        addressEditPanel.add(addressEditLabel, gridBagConstraints);

        phoneEditPanel.setPreferredSize(new java.awt.Dimension(400, 400));
        phoneEditPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(phoneEditLabelLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.phoneEditLabelLabel.text")); // NOI18N
        phoneEditLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneEditPanel.add(phoneEditLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(phoneEditNumberLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.phoneEditNumberLabel.text")); // NOI18N
        phoneEditNumberLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneEditPanel.add(phoneEditNumberLabel, gridBagConstraints);

        phoneEditLabelTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        phoneEditLabelTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneEditPanel.add(phoneEditLabelTextField, gridBagConstraints);

        phoneEditNumberTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        phoneEditNumberTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneEditPanel.add(phoneEditNumberTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(phoneEditNotesLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.phoneEditNotesLabel.text")); // NOI18N
        phoneEditNotesLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneEditPanel.add(phoneEditNotesLabel, gridBagConstraints);

        phoneEditNotesTextArea.setColumns(20);
        phoneEditNotesTextArea.setRows(5);
        phoneEditNotesScrollPane.setViewportView(phoneEditNotesTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneEditPanel.add(phoneEditNotesScrollPane, gridBagConstraints);

        phoneEditLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        phoneEditLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(phoneEditLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.phoneEditLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        phoneEditPanel.add(phoneEditLabel, gridBagConstraints);

        emailEditPanel.setPreferredSize(new java.awt.Dimension(400, 400));
        emailEditPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(emailEditLabelLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.emailEditLabelLabel.text")); // NOI18N
        emailEditLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emailEditPanel.add(emailEditLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(emailEditAddressLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.emailEditAddressLabel.text")); // NOI18N
        emailEditAddressLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emailEditPanel.add(emailEditAddressLabel, gridBagConstraints);

        emailEditLabelTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        emailEditLabelTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emailEditPanel.add(emailEditLabelTextField, gridBagConstraints);

        emailEditAddressTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        emailEditAddressTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emailEditPanel.add(emailEditAddressTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(emailEditNotesLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.emailEditNotesLabel.text")); // NOI18N
        emailEditNotesLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emailEditPanel.add(emailEditNotesLabel, gridBagConstraints);

        emailEditNotesTextArea.setColumns(20);
        emailEditNotesTextArea.setRows(5);
        emailEditNotesScrollPane.setViewportView(emailEditNotesTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emailEditPanel.add(emailEditNotesScrollPane, gridBagConstraints);

        emailEditLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        emailEditLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(emailEditLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.emailEditLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        emailEditPanel.add(emailEditLabel, gridBagConstraints);

        setLayout(new java.awt.GridBagLayout());

        mainTabbedPane.setDoubleBuffered(true);

        mainScrollPane.setBorder(null);

        mainGeneralPanel.setLayout(new java.awt.GridBagLayout());

        generalPanel.setLayout(new java.awt.GridBagLayout());

        generalLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        generalLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(generalLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.generalLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        generalPanel.add(generalLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(idLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.idLabel.text")); // NOI18N
        idLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(idLabel, gridBagConstraints);

        idTextField.setBackground(new java.awt.Color(232, 231, 231));
        idTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(idTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(nameLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.nameLabel.text")); // NOI18N
        nameLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(nameLabel, gridBagConstraints);

        nameTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        nameTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(nameTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(nifLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.nifLabel.text")); // NOI18N
        nifLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(nifLabel, gridBagConstraints);

        nifTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        nifTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(nifTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(categoryLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.categoryLabel.text")); // NOI18N
        categoryLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(categoryLabel, gridBagConstraints);

        categoryTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        categoryTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(categoryTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(paymentMethodLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.paymentMethodLabel.text")); // NOI18N
        paymentMethodLabel.setMaximumSize(new java.awt.Dimension(90, 14));
        paymentMethodLabel.setMinimumSize(new java.awt.Dimension(90, 14));
        paymentMethodLabel.setPreferredSize(new java.awt.Dimension(90, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(paymentMethodLabel, gridBagConstraints);

        paymentMethodTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        paymentMethodTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(paymentMethodTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(webLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.webLabel.text")); // NOI18N
        webLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(webLabel, gridBagConstraints);

        webTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        webTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(webTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(shippingMethodLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.shippingMethodLabel.text")); // NOI18N
        shippingMethodLabel.setPreferredSize(new java.awt.Dimension(90, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(shippingMethodLabel, gridBagConstraints);

        shippingMethodTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        shippingMethodTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(shippingMethodTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        mainGeneralPanel.add(generalPanel, gridBagConstraints);

        notesPanel.setLayout(new java.awt.GridBagLayout());

        notesLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        notesLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(notesLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.notesLabel.text")); // NOI18N
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

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.mainScrollPane.TabConstraints.tabTitle"), mainScrollPane); // NOI18N

        addressScrollPane.setBorder(null);

        addressViewPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(addressViewLabelLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressViewLabelLabel.text")); // NOI18N
        addressViewLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewStreetLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressViewStreetLabel.text")); // NOI18N
        addressViewStreetLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewStreetLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewCityLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressViewCityLabel.text")); // NOI18N
        addressViewCityLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewCityLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewProvinceLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressViewProvinceLabel.text")); // NOI18N
        addressViewProvinceLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewProvinceLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewPostalCodeLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressViewPostalCodeLabel.text")); // NOI18N
        addressViewPostalCodeLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewPostalCodeLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewCountryLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressViewCountryLabel.text")); // NOI18N
        addressViewCountryLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewCountryLabel, gridBagConstraints);

        addressViewLabelTextField.setBackground(new java.awt.Color(232, 231, 231));
        addressViewLabelTextField.setEditable(false);
        addressViewLabelTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        addressViewLabelTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewLabelTextField, gridBagConstraints);

        addressViewStreetTextField.setBackground(new java.awt.Color(232, 231, 231));
        addressViewStreetTextField.setEditable(false);
        addressViewStreetTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        addressViewStreetTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewStreetTextField, gridBagConstraints);

        addressViewCityTextField.setBackground(new java.awt.Color(232, 231, 231));
        addressViewCityTextField.setEditable(false);
        addressViewCityTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        addressViewCityTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewCityTextField, gridBagConstraints);

        addressViewProvinceTextField.setBackground(new java.awt.Color(232, 231, 231));
        addressViewProvinceTextField.setEditable(false);
        addressViewProvinceTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        addressViewProvinceTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewProvinceTextField, gridBagConstraints);

        addressViewPostalCodeTextField.setBackground(new java.awt.Color(232, 231, 231));
        addressViewPostalCodeTextField.setEditable(false);
        addressViewPostalCodeTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        addressViewPostalCodeTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewPostalCodeTextField, gridBagConstraints);

        addressViewCountryTextField.setBackground(new java.awt.Color(232, 231, 231));
        addressViewCountryTextField.setEditable(false);
        addressViewCountryTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        addressViewCountryTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewCountryTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewNotesLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressViewNotesLabel.text")); // NOI18N
        addressViewNotesLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewNotesLabel, gridBagConstraints);

        addressViewNotesTextArea.setBackground(new java.awt.Color(232, 231, 231));
        addressViewNotesTextArea.setColumns(20);
        addressViewNotesTextArea.setEditable(false);
        addressViewNotesTextArea.setRows(5);
        addressViewNotesScrollPane.setViewportView(addressViewNotesTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewNotesScrollPane, gridBagConstraints);

        addressViewLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        addressViewLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(addressViewLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressViewLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        addressViewPanel.add(addressViewLabel, gridBagConstraints);

        addressViewActionsPanel.setLayout(new java.awt.GridBagLayout());

        addressViewComboBox.setEnabled(false);
        addressViewComboBox.setMinimumSize(new java.awt.Dimension(100, 24));
        addressViewComboBox.setPreferredSize(new java.awt.Dimension(100, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewActionsPanel.add(addressViewComboBox, gridBagConstraints);

        addressViewAddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addressViewAddButton, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressViewAddButton.text")); // NOI18N
        addressViewAddButton.setMinimumSize(new java.awt.Dimension(32, 32));
        addressViewAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressViewAddButtonActionPerformed(evt);
            }
        });
        addressViewButtonsPanel.add(addressViewAddButton);

        addressViewEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/pencil.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addressViewEditButton, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressViewEditButton.text")); // NOI18N
        addressViewEditButton.setEnabled(false);
        addressViewEditButton.setMinimumSize(new java.awt.Dimension(32, 32));
        addressViewEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressViewEditButtonActionPerformed(evt);
            }
        });
        addressViewButtonsPanel.add(addressViewEditButton);

        addressViewDeleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addressViewDeleteButton, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressViewDeleteButton.text")); // NOI18N
        addressViewDeleteButton.setEnabled(false);
        addressViewDeleteButton.setMinimumSize(new java.awt.Dimension(32, 32));
        addressViewDeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressViewDeleteButtonActionPerformed(evt);
            }
        });
        addressViewButtonsPanel.add(addressViewDeleteButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        addressViewActionsPanel.add(addressViewButtonsPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        addressViewPanel.add(addressViewActionsPanel, gridBagConstraints);

        addressScrollPane.setViewportView(addressViewPanel);

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.addressScrollPane.TabConstraints.tabTitle"), addressScrollPane); // NOI18N

        phoneScrollPane.setBorder(null);

        phoneViewPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(phoneViewLabelLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.phoneViewLabelLabel.text")); // NOI18N
        phoneViewLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneViewPanel.add(phoneViewLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(phoneViewNumberLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.phoneViewNumberLabel.text")); // NOI18N
        phoneViewNumberLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneViewPanel.add(phoneViewNumberLabel, gridBagConstraints);

        phoneViewLabelTextField.setBackground(new java.awt.Color(232, 231, 231));
        phoneViewLabelTextField.setEditable(false);
        phoneViewLabelTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        phoneViewLabelTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneViewPanel.add(phoneViewLabelTextField, gridBagConstraints);

        phoneViewNumberTextField.setBackground(new java.awt.Color(232, 231, 231));
        phoneViewNumberTextField.setEditable(false);
        phoneViewNumberTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        phoneViewNumberTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneViewPanel.add(phoneViewNumberTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(phoneViewNotesLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.phoneViewNotesLabel.text")); // NOI18N
        phoneViewNotesLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneViewPanel.add(phoneViewNotesLabel, gridBagConstraints);

        phoneViewNotesTextArea.setBackground(new java.awt.Color(232, 231, 231));
        phoneViewNotesTextArea.setColumns(20);
        phoneViewNotesTextArea.setEditable(false);
        phoneViewNotesTextArea.setRows(5);
        phoneViewNotesScrollPane.setViewportView(phoneViewNotesTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneViewPanel.add(phoneViewNotesScrollPane, gridBagConstraints);

        phoneViewLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        phoneViewLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(phoneViewLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.phoneViewLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        phoneViewPanel.add(phoneViewLabel, gridBagConstraints);

        phoneViewActionsPanel.setLayout(new java.awt.GridBagLayout());

        phoneViewComboBox.setEnabled(false);
        phoneViewComboBox.setMinimumSize(new java.awt.Dimension(100, 24));
        phoneViewComboBox.setPreferredSize(new java.awt.Dimension(100, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneViewActionsPanel.add(phoneViewComboBox, gridBagConstraints);

        phoneViewAddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(phoneViewAddButton, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.phoneViewAddButton.text")); // NOI18N
        phoneViewAddButton.setMinimumSize(new java.awt.Dimension(32, 32));
        phoneViewAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneViewAddButtonActionPerformed(evt);
            }
        });
        phoneViewButtonsPanel.add(phoneViewAddButton);

        phoneViewEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/pencil.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(phoneViewEditButton, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.phoneViewEditButton.text")); // NOI18N
        phoneViewEditButton.setEnabled(false);
        phoneViewEditButton.setMinimumSize(new java.awt.Dimension(32, 32));
        phoneViewEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneViewEditButtonActionPerformed(evt);
            }
        });
        phoneViewButtonsPanel.add(phoneViewEditButton);

        phoneViewDeleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(phoneViewDeleteButton, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.phoneViewDeleteButton.text")); // NOI18N
        phoneViewDeleteButton.setEnabled(false);
        phoneViewDeleteButton.setMinimumSize(new java.awt.Dimension(32, 32));
        phoneViewDeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneViewDeleteButtonActionPerformed(evt);
            }
        });
        phoneViewButtonsPanel.add(phoneViewDeleteButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        phoneViewActionsPanel.add(phoneViewButtonsPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        phoneViewPanel.add(phoneViewActionsPanel, gridBagConstraints);

        phoneScrollPane.setViewportView(phoneViewPanel);

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.phoneScrollPane.TabConstraints.tabTitle"), phoneScrollPane); // NOI18N

        emailScrollPane.setBorder(null);

        emaiViewlPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(emailViewLabelLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.emailViewLabelLabel.text")); // NOI18N
        emailViewLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emaiViewlPanel.add(emailViewLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(emailViewAddressLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.emailViewAddressLabel.text")); // NOI18N
        emailViewAddressLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emaiViewlPanel.add(emailViewAddressLabel, gridBagConstraints);

        emailViewLabelTextField.setBackground(new java.awt.Color(232, 231, 231));
        emailViewLabelTextField.setEditable(false);
        emailViewLabelTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        emailViewLabelTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emaiViewlPanel.add(emailViewLabelTextField, gridBagConstraints);

        emailViewAddressTextField.setBackground(new java.awt.Color(232, 231, 231));
        emailViewAddressTextField.setEditable(false);
        emailViewAddressTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        emailViewAddressTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emaiViewlPanel.add(emailViewAddressTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(emailViewNotesLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.emailViewNotesLabel.text")); // NOI18N
        emailViewNotesLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emaiViewlPanel.add(emailViewNotesLabel, gridBagConstraints);

        emailViewNotesTextArea.setBackground(new java.awt.Color(232, 231, 231));
        emailViewNotesTextArea.setColumns(20);
        emailViewNotesTextArea.setEditable(false);
        emailViewNotesTextArea.setRows(5);
        emailViewNotesScrollPane.setViewportView(emailViewNotesTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emaiViewlPanel.add(emailViewNotesScrollPane, gridBagConstraints);

        emailLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        emailLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(emailLabel, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.emailLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        emaiViewlPanel.add(emailLabel, gridBagConstraints);

        emailViewActionsPanel.setLayout(new java.awt.GridBagLayout());

        emailViewComboBox.setEnabled(false);
        emailViewComboBox.setMinimumSize(new java.awt.Dimension(100, 24));
        emailViewComboBox.setPreferredSize(new java.awt.Dimension(100, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emailViewActionsPanel.add(emailViewComboBox, gridBagConstraints);

        emailViewAddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(emailViewAddButton, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.emailViewAddButton.text")); // NOI18N
        emailViewAddButton.setMinimumSize(new java.awt.Dimension(32, 32));
        emailViewAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailViewAddButtonActionPerformed(evt);
            }
        });
        emailViewButtonsPanel.add(emailViewAddButton);

        emailViewEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/pencil.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(emailViewEditButton, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.emailViewEditButton.text")); // NOI18N
        emailViewEditButton.setEnabled(false);
        emailViewEditButton.setMinimumSize(new java.awt.Dimension(32, 32));
        emailViewEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailViewEditButtonActionPerformed(evt);
            }
        });
        emailViewButtonsPanel.add(emailViewEditButton);

        emailViewDeleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(emailViewDeleteButton, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.emailViewDeleteButton.text")); // NOI18N
        emailViewDeleteButton.setEnabled(false);
        emailViewDeleteButton.setMinimumSize(new java.awt.Dimension(32, 32));
        emailViewDeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailViewDeleteButtonActionPerformed(evt);
            }
        });
        emailViewButtonsPanel.add(emailViewDeleteButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        emailViewActionsPanel.add(emailViewButtonsPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        emaiViewlPanel.add(emailViewActionsPanel, gridBagConstraints);

        emailScrollPane.setViewportView(emaiViewlPanel);

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.emailScrollPane.TabConstraints.tabTitle"), emailScrollPane); // NOI18N

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

        saveSupplierButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/disk.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(saveSupplierButton, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.saveSupplierButton.text")); // NOI18N
        saveSupplierButton.setFocusable(false);
        saveSupplierButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        saveSupplierButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        saveSupplierButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        saveSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveSupplierButtonActionPerformed(evt);
            }
        });
        topToolBar.add(saveSupplierButton);

        reloadSupplierButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/arrow_refresh.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(reloadSupplierButton, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.reloadSupplierButton.text")); // NOI18N
        reloadSupplierButton.setFocusable(false);
        reloadSupplierButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        reloadSupplierButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        reloadSupplierButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        reloadSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadSupplierButtonActionPerformed(evt);
            }
        });
        topToolBar.add(reloadSupplierButton);

        enableSupplierButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/status_online.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(enableSupplierButton, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.enableSupplierButton.text")); // NOI18N
        enableSupplierButton.setFocusable(false);
        enableSupplierButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        enableSupplierButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        enableSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableSupplierButtonActionPerformed(evt);
            }
        });
        topToolBar.add(enableSupplierButton);

        disableSupplierButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/status_offline.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(disableSupplierButton, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.disableSupplierButton.text")); // NOI18N
        disableSupplierButton.setFocusable(false);
        disableSupplierButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        disableSupplierButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        disableSupplierButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        disableSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disableSupplierButtonActionPerformed(evt);
            }
        });
        topToolBar.add(disableSupplierButton);

        deleteSupplierButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteSupplierButton, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.deleteSupplierButton.text")); // NOI18N
        deleteSupplierButton.setFocusable(false);
        deleteSupplierButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deleteSupplierButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deleteSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSupplierButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deleteSupplierButton);

        printSupplierButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printSupplierButton, org.openide.util.NbBundle.getMessage(SupplierEditorTopComponent.class, "SupplierEditorTopComponent.printSupplierButton.text")); // NOI18N
        printSupplierButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSupplierButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printSupplierButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(topToolBar, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void phoneViewAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneViewAddButtonActionPerformed
        phoneEditNumberTextField.setText("");
        phoneEditLabelTextField.setText("");
        phoneEditNotesTextArea.setText("");


        editPhonedialog = new DialogDescriptor(phoneEditPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADD_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PHONE")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() == DialogDescriptor.OK_OPTION) {
                            if (addPhone()) {
                                editPhonedialog.setClosingOptions(null);
                                modify();
                            }
                        }
                    }
                });
        editPhonedialog.setClosingOptions(new Object[]{DialogDescriptor.CANCEL_OPTION});
        DialogDisplayer.getDefault().createDialog(editPhonedialog);
        DialogDisplayer.getDefault().notify(editPhonedialog);
    }//GEN-LAST:event_phoneViewAddButtonActionPerformed

    private void phoneViewEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneViewEditButtonActionPerformed
        phoneEditNumberTextField.setText(phoneViewNumberTextField.getText());
        phoneEditLabelTextField.setText(phoneViewLabelTextField.getText());
        phoneEditNotesTextArea.setText(phoneViewNotesTextArea.getText());


        editPhonedialog = new DialogDescriptor(phoneEditPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("EDIT_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PHONE")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() == DialogDescriptor.OK_OPTION) {
                            if (updatePhone()) {
                                editPhonedialog.setClosingOptions(null);
                                modify();
                            }
                        }
                    }
                });
        editPhonedialog.setClosingOptions(new Object[]{DialogDescriptor.CANCEL_OPTION});
        DialogDisplayer.getDefault().createDialog(editPhonedialog);
        DialogDisplayer.getDefault().notify(editPhonedialog);
    }//GEN-LAST:event_phoneViewEditButtonActionPerformed

    private void emailViewAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailViewAddButtonActionPerformed
        emailEditAddressTextField.setText("");
        emailEditLabelTextField.setText("");
        emailEditNotesTextArea.setText("");

        editEmaildialog = new DialogDescriptor(emailEditPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADD_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("EMAIL")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() == DialogDescriptor.OK_OPTION) {
                            if (addEmail()) {
                                editEmaildialog.setClosingOptions(null);
                                modify();
                            }
                        }
                    }
                });
        editEmaildialog.setClosingOptions(new Object[]{DialogDescriptor.CANCEL_OPTION});
        DialogDisplayer.getDefault().createDialog(editEmaildialog);
        DialogDisplayer.getDefault().notify(editEmaildialog);
    }//GEN-LAST:event_emailViewAddButtonActionPerformed

    private void emailViewEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailViewEditButtonActionPerformed

        emailEditAddressTextField.setText(emailViewAddressTextField.getText());
        emailEditLabelTextField.setText(emailViewLabelTextField.getText());
        emailEditNotesTextArea.setText(emailViewNotesTextArea.getText());


        editEmaildialog = new DialogDescriptor(emailEditPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("EDIT_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("EMAIL")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() == DialogDescriptor.OK_OPTION) {
                            if (updateEmail()) {
                                editEmaildialog.setClosingOptions(null);
                                modify();
                            }
                        }
                    }
                });
        editEmaildialog.setClosingOptions(new Object[]{DialogDescriptor.CANCEL_OPTION});
        DialogDisplayer.getDefault().createDialog(editEmaildialog);
        DialogDisplayer.getDefault().notify(editEmaildialog);
    }//GEN-LAST:event_emailViewEditButtonActionPerformed

    private void phoneViewDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneViewDeleteButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PHONE")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    fillPhones();
                    modify();
                }

                @Override
                protected Void doInBackground() {
                    supplier.removePhone(phoneViewLabelTextField.getText());
                    return null;
                }
            };


            worker.execute();
        }
    }//GEN-LAST:event_phoneViewDeleteButtonActionPerformed

    private void emailViewDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailViewDeleteButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("EMAIL")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    fillEmails();
                    modify();
                }

                @Override
                protected Void doInBackground() {
                    supplier.removeEmail(emailViewLabelTextField.getText());
                    return null;
                }
            };


            worker.execute();
//            controller.removeEmail(emailViewLabelTextField.getText());
//            fillEmails();
        }
    }//GEN-LAST:event_emailViewDeleteButtonActionPerformed

    private void saveSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveSupplierButtonActionPerformed
        saveSupplier();
    }//GEN-LAST:event_saveSupplierButtonActionPerformed

    private void disableSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disableSupplierButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
//                disableSupplierButton.setVisible(false);
//                enableSupplierButton.setVisible(true);
                fillData();
                StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DISABLED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIER")));
            }

            @Override
            protected Void doInBackground() {
                if (updateSupplier()) {
                    supplier = controller.disableSupplier();
                }
                return null;
            }
        };

        worker.execute();

    }//GEN-LAST:event_disableSupplierButtonActionPerformed

    private void reloadSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadSupplierButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SURE_RELOAD_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIER")),
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
                                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIER_RELOAD_NOT_FOUND"),
                                    NotifyDescriptor.ERROR_MESSAGE);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RELOADED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIER")));
                }

                @Override
                protected Boolean doInBackground() {
                    supplier = controller.reloadSupplier();
                    if (supplier != null) {
                        controller.setSupplier(supplier);
                        return true;
                    } else {
                        return false;
                    }
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_reloadSupplierButtonActionPerformed

    private void enableSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableSupplierButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
//                disableSupplierButton.setVisible(true);
//                enableSupplierButton.setVisible(false);
                fillData();
                StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ENABLED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIER")));
            }

            @Override
            protected Void doInBackground() {
                if (updateSupplier()) {
                    supplier = controller.enableSupplier();
                }
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_enableSupplierButtonActionPerformed

    private void deleteSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSupplierButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIER")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {


            SupplierLookup.getDefault().clear();

            SupplierBrowserTopComponent window = (SupplierBrowserTopComponent) WindowManager.getDefault().findTopComponent("SupplierBrowserTopComponent");

            window.deleteSupplier(supplier);
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    forceClose();
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIER")));
                }

                @Override
                protected Void doInBackground() {
                    controller.setSupplier(supplier);
                    controller.deleteSupplier();
                    return null;
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_deleteSupplierButtonActionPerformed

    private void addressViewDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressViewDeleteButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    fillAddresses();
                    modify();
                }

                @Override
                protected Void doInBackground() {
                    supplier.removeAddress(addressViewLabelTextField.getText());
                    return null;
                }
            };


            worker.execute();
        }
    }//GEN-LAST:event_addressViewDeleteButtonActionPerformed

    private void addressViewEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressViewEditButtonActionPerformed
        addressEditStreetTextField.setText(addressViewStreetTextField.getText());
        addressEditProvinceTextField.setText(addressViewProvinceTextField.getText());
        addressEditPostalCodeTextField.setText(addressViewPostalCodeTextField.getText());
        addressEditCityTextField.setText(addressViewCityTextField.getText());
        addressEditCountryTextField.setText(addressViewCountryTextField.getText());
        addressEditLabelTextField.setText(addressViewLabelTextField.getText());
        addressEditNotesTextArea.setText(addressViewNotesTextArea.getText());


        editAddressdialog = new DialogDescriptor(addressEditPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("EDIT_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() == DialogDescriptor.OK_OPTION) {
                            if (updateAddress()) {
                                editAddressdialog.setClosingOptions(null);
                                modify();
                            }
                        }
                    }
                });
        editAddressdialog.setClosingOptions(new Object[]{DialogDescriptor.CANCEL_OPTION});
        DialogDisplayer.getDefault().createDialog(editAddressdialog);
        DialogDisplayer.getDefault().notify(editAddressdialog);
    }//GEN-LAST:event_addressViewEditButtonActionPerformed

    private void addressViewAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressViewAddButtonActionPerformed
        addressEditStreetTextField.setText("");
        addressEditProvinceTextField.setText("");
        addressEditPostalCodeTextField.setText("");
        addressEditCityTextField.setText("");
        addressEditCountryTextField.setText("");
        addressEditLabelTextField.setText("");
        addressEditNotesTextArea.setText("");


        editAddressdialog = new DialogDescriptor(addressEditPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADD_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() == DialogDescriptor.OK_OPTION) {
                            if (addAddress()) {
                                editAddressdialog.setClosingOptions(null);
                                modify();
                            }
                        }
                    }
                });
        editAddressdialog.setClosingOptions(new Object[]{DialogDescriptor.CANCEL_OPTION});
        DialogDisplayer.getDefault().createDialog(editAddressdialog);
        DialogDisplayer.getDefault().notify(editAddressdialog);
    }//GEN-LAST:event_addressViewAddButtonActionPerformed

    private void printSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSupplierButtonActionPerformed
        if (updateSupplier()) {
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
        }
    }//GEN-LAST:event_printSupplierButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addressEditCityLabel;
    private javax.swing.JTextField addressEditCityTextField;
    private javax.swing.JLabel addressEditCountryLabel;
    private javax.swing.JTextField addressEditCountryTextField;
    private javax.swing.JLabel addressEditLabel;
    private javax.swing.JLabel addressEditLabelLabel;
    private javax.swing.JTextField addressEditLabelTextField;
    private javax.swing.JLabel addressEditNotesLabel;
    private javax.swing.JScrollPane addressEditNotesScrollPane;
    private javax.swing.JTextArea addressEditNotesTextArea;
    private javax.swing.JPanel addressEditPanel;
    private javax.swing.JLabel addressEditPostalCodeLabel;
    private javax.swing.JTextField addressEditPostalCodeTextField;
    private javax.swing.JLabel addressEditProvinceLabel;
    private javax.swing.JTextField addressEditProvinceTextField;
    private javax.swing.JLabel addressEditStreetLabel;
    private javax.swing.JTextField addressEditStreetTextField;
    private javax.swing.JScrollPane addressScrollPane;
    private javax.swing.JPanel addressViewActionsPanel;
    private javax.swing.JButton addressViewAddButton;
    private javax.swing.JPanel addressViewButtonsPanel;
    private javax.swing.JLabel addressViewCityLabel;
    private javax.swing.JTextField addressViewCityTextField;
    private javax.swing.JComboBox addressViewComboBox;
    private javax.swing.JLabel addressViewCountryLabel;
    private javax.swing.JTextField addressViewCountryTextField;
    private javax.swing.JButton addressViewDeleteButton;
    private javax.swing.JButton addressViewEditButton;
    private javax.swing.JLabel addressViewLabel;
    private javax.swing.JLabel addressViewLabelLabel;
    private javax.swing.JTextField addressViewLabelTextField;
    private javax.swing.JLabel addressViewNotesLabel;
    private javax.swing.JScrollPane addressViewNotesScrollPane;
    private javax.swing.JTextArea addressViewNotesTextArea;
    private javax.swing.JPanel addressViewPanel;
    private javax.swing.JLabel addressViewPostalCodeLabel;
    private javax.swing.JTextField addressViewPostalCodeTextField;
    private javax.swing.JLabel addressViewProvinceLabel;
    private javax.swing.JTextField addressViewProvinceTextField;
    private javax.swing.JLabel addressViewStreetLabel;
    private javax.swing.JTextField addressViewStreetTextField;
    private javax.swing.JLabel categoryLabel;
    private javax.swing.JTextField categoryTextField;
    private javax.swing.JButton deleteSupplierButton;
    private javax.swing.JButton disableSupplierButton;
    private javax.swing.JPanel emaiViewlPanel;
    private javax.swing.JLabel emailEditAddressLabel;
    private javax.swing.JTextField emailEditAddressTextField;
    private javax.swing.JLabel emailEditLabel;
    private javax.swing.JLabel emailEditLabelLabel;
    private javax.swing.JTextField emailEditLabelTextField;
    private javax.swing.JLabel emailEditNotesLabel;
    private javax.swing.JScrollPane emailEditNotesScrollPane;
    private javax.swing.JTextArea emailEditNotesTextArea;
    private javax.swing.JPanel emailEditPanel;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JScrollPane emailScrollPane;
    private javax.swing.JPanel emailViewActionsPanel;
    private javax.swing.JButton emailViewAddButton;
    private javax.swing.JLabel emailViewAddressLabel;
    private javax.swing.JTextField emailViewAddressTextField;
    private javax.swing.JPanel emailViewButtonsPanel;
    private javax.swing.JComboBox emailViewComboBox;
    private javax.swing.JButton emailViewDeleteButton;
    private javax.swing.JButton emailViewEditButton;
    private javax.swing.JLabel emailViewLabelLabel;
    private javax.swing.JTextField emailViewLabelTextField;
    private javax.swing.JLabel emailViewNotesLabel;
    private javax.swing.JScrollPane emailViewNotesScrollPane;
    private javax.swing.JTextArea emailViewNotesTextArea;
    private javax.swing.JButton enableSupplierButton;
    private javax.swing.JLabel generalLabel;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JLabel idLabel;
    private javax.swing.JTextField idTextField;
    private javax.swing.JPanel mainGeneralPanel;
    private javax.swing.JScrollPane mainScrollPane;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JLabel nifLabel;
    private javax.swing.JTextField nifTextField;
    private javax.swing.JLabel notesLabel;
    private javax.swing.JPanel notesPanel;
    private javax.swing.JScrollPane notesScrollPane;
    private javax.swing.JTextArea notesTextArea;
    private javax.swing.JLabel paymentMethodLabel;
    private javax.swing.JTextField paymentMethodTextField;
    private javax.swing.JLabel phoneEditLabel;
    private javax.swing.JLabel phoneEditLabelLabel;
    private javax.swing.JTextField phoneEditLabelTextField;
    private javax.swing.JLabel phoneEditNotesLabel;
    private javax.swing.JScrollPane phoneEditNotesScrollPane;
    private javax.swing.JTextArea phoneEditNotesTextArea;
    private javax.swing.JLabel phoneEditNumberLabel;
    private javax.swing.JTextField phoneEditNumberTextField;
    private javax.swing.JPanel phoneEditPanel;
    private javax.swing.JScrollPane phoneScrollPane;
    private javax.swing.JPanel phoneViewActionsPanel;
    private javax.swing.JButton phoneViewAddButton;
    private javax.swing.JPanel phoneViewButtonsPanel;
    private javax.swing.JComboBox phoneViewComboBox;
    private javax.swing.JButton phoneViewDeleteButton;
    private javax.swing.JButton phoneViewEditButton;
    private javax.swing.JLabel phoneViewLabel;
    private javax.swing.JLabel phoneViewLabelLabel;
    private javax.swing.JTextField phoneViewLabelTextField;
    private javax.swing.JLabel phoneViewNotesLabel;
    private javax.swing.JScrollPane phoneViewNotesScrollPane;
    private javax.swing.JTextArea phoneViewNotesTextArea;
    private javax.swing.JLabel phoneViewNumberLabel;
    private javax.swing.JTextField phoneViewNumberTextField;
    private javax.swing.JPanel phoneViewPanel;
    private javax.swing.JButton printSupplierButton;
    private javax.swing.JButton reloadSupplierButton;
    private javax.swing.JButton saveSupplierButton;
    private javax.swing.JLabel shippingMethodLabel;
    private javax.swing.JTextField shippingMethodTextField;
    private javax.swing.JToolBar topToolBar;
    private javax.swing.JLabel webLabel;
    private javax.swing.JTextField webTextField;
    // End of variables declaration//GEN-END:variables

    @Override
    public HelpCtx getHelpCtx() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return new HelpCtx("es.regueiro.easyrepair.ui.stock.editor");
    }

    public Supplier getSupplier() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return supplier;
    }

    public void forceClose() {
        modified = false;
        this.close();
    }

    @Override
    public void componentOpened() {
        invalid = false;
        result = SupplierLookup.getDefault().lookupResult(Supplier.class);
        result.removeLookupListener(this);
        mainTabbedPane.setSelectedIndex(0);

        Collection<? extends Supplier> supplierColId = result.allInstances();
        if (!supplierColId.isEmpty()) {
            supplier = controller.getSupplierById(supplierColId.iterator().next().getId());
            if (supplier == null) {
                NotifyDescriptor d = new NotifyDescriptor.Message(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_NOT_FOUND"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIER")), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                close();
            } else {
                newSupplier = false;
            }
        } else {
            supplier = controller.newSupplier();
            newSupplier = true;
        }
        if (supplier != null) {
            reloadSupplierButton.setEnabled(!newSupplier);
            deleteSupplierButton.setEnabled(!newSupplier);
            enableSupplierButton.setVisible(false);
            disableSupplierButton.setEnabled(!newSupplier);
            controller.setSupplier(supplier);
            fillData();
            StatusDisplayer.getDefault().setStatusText("");
        }

    }

    @Override
    public void componentClosed() {
        manager.discardAllEdits();
    }

    @Override
    public void componentActivated() {
        checkPermissions();
    }

    @Override
    public boolean canClose() {
        if (modified) {
            NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("CHANGES_WILL_BE_LOST"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIER")),
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
        if (!SecurityManager.getDefault().isUserLoggedIn() || !SecurityManager.getDefault().userHasPrivilege(Privilege.SUPPLIER_EDIT)) {
            this.forceClose();
        }
    }

    private void fillData() {
        if (!invalid) {
            clearGeneral();
            clearNotes();

            if (supplier != null && supplier.getId() != null) {
                idTextField.setText(supplier.getId().toString());
                if (supplier.getName() != null) {
                    nameTextField.setText(supplier.getName());
                }
                if (supplier.getNif() != null) {
                    nifTextField.setText(supplier.getNif().getNumber());
                }
                if (supplier.getWeb() != null) {
                    webTextField.setText(supplier.getWeb());
                }
                if (supplier.getCategory() != null) {
                    categoryTextField.setText(supplier.getCategory());
                }
                if (supplier.getPaymentMethod() != null) {
                    paymentMethodTextField.setText(supplier.getPaymentMethod());
                }
                if (supplier.getShippingMethod() != null) {
                    shippingMethodTextField.setText(supplier.getShippingMethod());
                }
                if (supplier.getNotes() != null) {
                    notesTextArea.setText(supplier.getNotes());
                }
                if (supplier.getEnabled()) {
                    enableSupplierButton.setVisible(false);
                    disableSupplierButton.setVisible(true);
                } else {
                    enableSupplierButton.setVisible(true);
                    disableSupplierButton.setVisible(false);
                }
            }
            fillAddresses();
            fillPhones();
            fillEmails();

            manager.discardAllEdits();
            modified = invalid;
            saveSupplierButton.setEnabled(modified);
            deleteSupplierButton.setEnabled(!newSupplier);
            disableSupplierButton.setEnabled(!newSupplier);
            reloadSupplierButton.setEnabled(modified && !newSupplier);
        }
    }

    private void fillAddresses() {
        clearAddresses();
        if (supplier.getAddress() != null && !supplier.getAddress().isEmpty()) {
            for (Address a : supplier.getAddress()) {
                AddressBox adddress = new AddressBox(a.getLabel(), a);
                addressViewComboBox.addItem(adddress);
            }
            addressViewComboBox.setEnabled(true);
        }
    }

    private void fillPhones() {
        clearPhones();
        if (supplier.getPhone() != null && !supplier.getPhone().isEmpty()) {
            for (Phone p : supplier.getPhone()) {
                PhoneBox phone = new PhoneBox(p.getLabel(), p);
                phoneViewComboBox.addItem(phone);
            }
            phoneViewComboBox.setEnabled(true);
        }
    }

    private void fillEmails() {
        clearEmails();
        if (supplier.getEmail() != null && !supplier.getEmail().isEmpty()) {
            for (Email e : supplier.getEmail()) {
                EmailBox email = new EmailBox(e.getLabel(), e);
                emailViewComboBox.addItem(email);
            }
            emailViewComboBox.setEnabled(true);
        }
    }

    private void clearGeneral() {
        idTextField.setText("");
        nameTextField.setText("");
        nifTextField.setText("");
        categoryTextField.setText("");
        webTextField.setText("");
        shippingMethodTextField.setText("");
        paymentMethodTextField.setText("");
    }

    private void clearEmails() {
        emailViewComboBox.setEnabled(false);
        emailViewComboBox.removeAllItems();
        emailViewEditButton.setEnabled(false);
        emailViewDeleteButton.setEnabled(false);
        emailViewAddressTextField.setText("");
        emailViewLabelTextField.setText("");
        emailViewNotesTextArea.setText("");
    }

    private void clearPhones() {
        phoneViewComboBox.setEnabled(false);
        phoneViewComboBox.removeAllItems();
        phoneViewEditButton.setEnabled(false);
        phoneViewDeleteButton.setEnabled(false);
        phoneViewNumberTextField.setText("");
        phoneViewLabelTextField.setText("");
        phoneViewNotesTextArea.setText("");
    }

    private void clearNotes() {
        notesTextArea.setText("");
    }

    private void clearAddresses() {
        addressViewComboBox.setEnabled(false);
        addressViewComboBox.removeAllItems();
        addressViewEditButton.setEnabled(false);
        addressViewDeleteButton.setEnabled(false);
        addressViewCityTextField.setText("");
        addressViewCountryTextField.setText("");
        addressViewLabelTextField.setText("");
        addressViewPostalCodeTextField.setText("");
        addressViewProvinceTextField.setText("");
        addressViewStreetTextField.setText("");
        addressViewNotesTextArea.setText("");
    }

    private void setupEmailViewComboBox() {
        emailViewComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    EmailBox email = (EmailBox) e.getItem();

                    if (email != null && email.email != null) {
                        emailViewEditButton.setEnabled(true);
                        emailViewDeleteButton.setEnabled(true);

                        emailViewLabelTextField.setText(email.email.getLabel());
                        emailViewAddressTextField.setText(email.email.getAddress());
                        emailViewNotesTextArea.setText(email.email.getNotes());
                    }
                }
            }
        });
    }

    private void setupAddressViewComboBox() {
        addressViewComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    AddressBox address = (AddressBox) e.getItem();

                    if (address != null && address.address != null) {
                        addressViewEditButton.setEnabled(true);
                        addressViewDeleteButton.setEnabled(true);
                        addressViewStreetTextField.setText(address.address.getStreet());
                        addressViewProvinceTextField.setText(address.address.getProvince());
                        addressViewPostalCodeTextField.setText(address.address.getPostalCode());
                        addressViewCityTextField.setText(address.address.getCity());
                        addressViewCountryTextField.setText(address.address.getCountry());
                        addressViewLabelTextField.setText(address.address.getLabel());
                        addressViewNotesTextArea.setText(address.address.getNotes());
                    }
                }
            }
        });
    }

    private void setupPhoneViewComboBox() {
        phoneViewComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    PhoneBox phone = (PhoneBox) e.getItem();

                    if (phone != null && phone.phone != null) {
                        phoneViewEditButton.setEnabled(true);
                        phoneViewDeleteButton.setEnabled(true);

                        phoneViewLabelTextField.setText(phone.phone.getLabel());
                        String formattedPhone;
                        PhoneNumber numberProto;
                        try {
                            numberProto = phoneUtil.parse(phone.phone.getNumber(), "ES");
                        } catch (NumberParseException ex) {
                            throw new IllegalArgumentException("This shouldn't happen " + ex.getMessage());
                        }
                        if (numberProto.getCountryCode() == 34) {
                            phoneViewNumberTextField.setText(phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
                        } else {
                            phoneViewNumberTextField.setText(phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
                        }
                        phoneViewNotesTextArea.setText(phone.phone.getNotes());
                    }
                }
            }
        });
    }

    private void saveSupplier() {
        if (updateSupplier()) {

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
                        controller.setSupplier(supplier);
                        controller.saveSupplier();
                        newSupplier = false;
                        invalid = false;
                    } catch (Exception e) {
                        if (e.getMessage().contains("was updated or deleted by another user")) {
                            NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("OVERWRITE_DIALOG"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIER")),
                                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_MODIFIED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIER")));
                            String options[] = new String[2];
                            options[0] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("OVERWRITE");
                            options[1] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RELOAD");
                            d.setOptions(options);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                            if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("OVERWRITE"))) {
                                controller.overwriteSupplier();
                                supplier = controller.getSupplier();
                                invalid = false;
                            } else if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RELOAD"))) {
                                reloadSupplierButton.doClick();
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

    private boolean updateSupplier() {
        if (!StringUtils.isBlank(nameTextField.getText())) {
            supplier.setName(nameTextField.getText());

            if (!StringUtils.isBlank(nifTextField.getText())) {
                try {
                    supplier.setNif(new NIF(nifTextField.getText()));
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    invalid = true;
                    return false;
                }
            } else {
                supplier.setNif(null);
            }
            if (!StringUtils.isBlank(categoryTextField.getText())) {
                supplier.setCategory(categoryTextField.getText());
            } else {
                supplier.setCategory(null);
            }
            if (!StringUtils.isBlank(webTextField.getText())) {
                try {
                    supplier.setWeb(webTextField.getText());
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    invalid = true;
                    return false;
                }
            } else {
                supplier.setWeb(null);
            }
            if (!StringUtils.isBlank(paymentMethodTextField.getText())) {
                supplier.setPaymentMethod(paymentMethodTextField.getText());
            } else {
                supplier.setPaymentMethod(null);
            }
            if (!StringUtils.isBlank(shippingMethodTextField.getText())) {
                supplier.setShippingMethod(shippingMethodTextField.getText());
            } else {
                supplier.setShippingMethod(null);
            }
            if (!StringUtils.isBlank(notesTextArea.getText())) {
                supplier.setNotes(notesTextArea.getText());
            } else {
                supplier.setNotes(null);
            }

            invalid = false;
            return true;

        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_NAME_NOT_BLANK"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SUPPLIER")), NotifyDescriptor.WARNING_MESSAGE);
            Object retval = DialogDisplayer.getDefault().notify(d);
            invalid = true;
            return false;
        }
    }

    private boolean addEmail() {
        Email email;

        if (!StringUtils.isBlank(emailEditLabelTextField.getText())) {
            if (!StringUtils.isBlank(emailEditAddressTextField.getText())) {
                try {
                    email = new Email(emailEditAddressTextField.getText(), emailEditLabelTextField.getText());
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage());
                    DialogDisplayer.getDefault().notify(d);
                    return false;
                }
            } else {
                NotifyDescriptor d = new NotifyDescriptor.Message(
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("EMAIL_VALID_ADDRESS"));
                DialogDisplayer.getDefault().notify(d);
                return false;

            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_VALID_LABEL"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("EMAIL")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }

        if (supplier.containsEmailLabel(email.getLabel())) {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("LABEL_INUSE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("EMAIL")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }
        if (!StringUtils.isBlank(emailEditNotesTextArea.getText())) {
            email.setNotes(emailEditNotesTextArea.getText());
        }

        supplier.addEmail(email);
        fillEmails();
        return true;
    }

    private boolean updateEmail() {
        Email email = ((EmailBox) emailViewComboBox.getSelectedItem()).email;
        String oldLabel = email.getLabel();

        if (!StringUtils.isBlank(emailEditLabelTextField.getText())) {
            if (!oldLabel.equals(emailEditLabelTextField.getText())) {
                if (supplier.containsEmailLabel(emailEditLabelTextField.getText())) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(
                            java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("LABEL_INUSE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("EMAIL")));
                    DialogDisplayer.getDefault().notify(d);
                    return false;
                }
                email.setLabel(emailEditLabelTextField.getText());
            }

            if (!StringUtils.isBlank(emailEditAddressTextField.getText())) {
                try {
                    email.setAddress(emailEditAddressTextField.getText());
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(
                            e.getMessage());
                    DialogDisplayer.getDefault().notify(d);
                    return false;
                }
            } else {
                NotifyDescriptor d = new NotifyDescriptor.Message(
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("EMAIL_VALID_ADDRESS"));
                DialogDisplayer.getDefault().notify(d);
                return false;

            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("EMAIL_VALID_ADDRESS"));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }


        if (!StringUtils.isBlank(emailEditNotesTextArea.getText())) {
            email.setNotes(emailEditNotesTextArea.getText());
        }
        //supplier = controller.getSupplier();
        fillEmails();

        return true;
    }

    private boolean addPhone() {
        Phone phone;

        if (!StringUtils.isBlank(phoneEditLabelTextField.getText())) {
            if (!StringUtils.isBlank(phoneEditNumberTextField.getText())) {
                try {
                    phone = new Phone(phoneEditNumberTextField.getText(), phoneEditLabelTextField.getText());
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(
                            e.getMessage());
                    DialogDisplayer.getDefault().notify(d);
                    return false;
                }
            } else {
                NotifyDescriptor d = new NotifyDescriptor.Message(
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PHONE_VALID_NUMBER"));
                DialogDisplayer.getDefault().notify(d);
                return false;

            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_VALID_LABEL"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PHONE")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }

        if (supplier.containsPhoneLabel(phone.getLabel())) {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("LABEL_INUSE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PHONE")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }
        if (!StringUtils.isBlank(phoneEditNotesTextArea.getText())) {
            phone.setNotes(phoneEditNotesTextArea.getText());
        }

        supplier.addPhone(phone);
        //supplier = controller.getSupplier();
        fillPhones();
        return true;
    }

    private boolean updatePhone() {
        Phone phone = ((PhoneBox) phoneViewComboBox.getSelectedItem()).phone;
        String oldLabel = phone.getLabel();

        if (!StringUtils.isBlank(phoneEditLabelTextField.getText())) {
            if (!oldLabel.equals(phoneEditLabelTextField.getText())) {
                if (supplier.containsPhoneLabel(phoneEditLabelTextField.getText())) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(
                            java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("LABEL_INUSE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PHONE")));
                    DialogDisplayer.getDefault().notify(d);
                    return false;
                }
                phone.setLabel(phoneEditLabelTextField.getText());
            }

            if (!StringUtils.isBlank(phoneEditNumberTextField.getText())) {
                try {
                    phone.setNumber(phoneEditNumberTextField.getText());
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(
                            e.getMessage());
                    DialogDisplayer.getDefault().notify(d);
                    return false;
                }
            } else {
                NotifyDescriptor d = new NotifyDescriptor.Message(
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PHONE_VALID_NUMBER"));
                DialogDisplayer.getDefault().notify(d);
                return false;

            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_VALID_LABEL"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PHONE")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }


        if (!StringUtils.isBlank(phoneEditNotesTextArea.getText())) {
            phone.setNotes(phoneEditNotesTextArea.getText());
        }
        //supplier = controller.getSupplier();
        fillPhones();

        return true;
    }

    private boolean addAddress() {
        Address address;

        if (!StringUtils.isBlank(addressEditLabelTextField.getText())) {
            if (!StringUtils.isBlank(addressEditStreetTextField.getText())
                    || !StringUtils.isBlank(addressEditCityTextField.getText())
                    || !StringUtils.isBlank(addressEditCountryTextField.getText())
                    || !StringUtils.isBlank(addressEditPostalCodeTextField.getText())
                    || !StringUtils.isBlank(addressEditProvinceTextField.getText())) {
                address = new Address();
                address.setLabel((addressEditLabelTextField.getText()));
                address.setStreet(addressEditStreetTextField.getText());
                address.setCity(addressEditCityTextField.getText());
                address.setCountry(addressEditCountryTextField.getText());
                address.setPostalCode(addressEditPostalCodeTextField.getText());
                address.setProvince(addressEditProvinceTextField.getText());
            } else {
                NotifyDescriptor d = new NotifyDescriptor.Message(
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS_BLANK"));
                DialogDisplayer.getDefault().notify(d);
                return false;
            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_VALID_LABEL"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }

        if (supplier.containsAddressLabel(address.getLabel())) {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("LABEL_INUSE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }
        if (!StringUtils.isBlank(addressEditNotesTextArea.getText())) {
            address.setNotes(addressEditNotesTextArea.getText());
        }

        supplier.addAddress(address);
        //supplier = controller.getSupplier();
        fillAddresses();
        return true;
    }

    private boolean updateAddress() {
        Address address = ((AddressBox) addressViewComboBox.getSelectedItem()).address;
        String oldLabel = address.getLabel();

        if (!StringUtils.isBlank(addressEditLabelTextField.getText())) {
            if (!oldLabel.equals(addressEditLabelTextField.getText())) {
                if (supplier.containsAddressLabel(addressEditLabelTextField.getText())) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(
                            java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("LABEL_INUSE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS")));
                    DialogDisplayer.getDefault().notify(d);
                    return false;
                }
                address.setLabel(addressEditLabelTextField.getText());
            }

            if (!StringUtils.isBlank(addressEditStreetTextField.getText())
                    || !StringUtils.isBlank(addressEditCityTextField.getText())
                    || !StringUtils.isBlank(addressEditCountryTextField.getText())
                    || !StringUtils.isBlank(addressEditPostalCodeTextField.getText())
                    || !StringUtils.isBlank(addressEditProvinceTextField.getText())) {
//                address = new Address();
//                address.setLabel((addressEditLabelTextField.getText()));
                address.setStreet(addressEditStreetTextField.getText());
                address.setCity(addressEditCityTextField.getText());
                address.setCountry(addressEditCountryTextField.getText());
                address.setPostalCode(addressEditPostalCodeTextField.getText());
                address.setProvince(addressEditProvinceTextField.getText());
            } else {
                NotifyDescriptor d = new NotifyDescriptor.Message(
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS_BLANK"));
                DialogDisplayer.getDefault().notify(d);
                return false;
            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_VALID_LABEL"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ADDRESS")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }


        if (!StringUtils.isBlank(addressEditNotesTextArea.getText())) {
            address.setNotes(addressEditNotesTextArea.getText());
        }
        //supplier = controller.getSupplier();
        fillAddresses();

        return true;
    }

    private void setupUndo() {
        notesTextArea.getDocument().addUndoableEditListener(manager);
        shippingMethodTextField.getDocument().addUndoableEditListener(manager);
        webTextField.getDocument().addUndoableEditListener(manager);
        idTextField.getDocument().addUndoableEditListener(manager);
        addressEditCityTextField.getDocument().addUndoableEditListener(manager);
        //TODO add the rest of the textfields
    }

    private void removeUndo() {
        notesTextArea.getDocument().removeUndoableEditListener(manager);
        shippingMethodTextField.getDocument().removeUndoableEditListener(manager);
        webTextField.getDocument().removeUndoableEditListener(manager);
    }

    private void modify() {
        modified = true;
        saveSupplierButton.setEnabled(modified);
        reloadSupplierButton.setEnabled(modified && !newSupplier);

        StatusDisplayer.getDefault().setStatusText("");
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

    private class AddressBox {

        public Address address;
        public String label;

        public AddressBox(String label, Address address) {
            this.label = label;
            this.address = address;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    private class PhoneBox {

        public Phone phone;
        public String label;

        public PhoneBox(String label, Phone phone) {
            this.label = label;
            this.phone = phone;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    private class EmailBox {

        public Email email;
        public String label;

        public EmailBox(String label, Email email) {
            this.label = label;
            this.email = email;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }
}
