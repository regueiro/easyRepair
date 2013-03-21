package es.regueiro.easyrepair.ui.client;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import es.regueiro.easyrepair.api.client.controller.ClientController;
import es.regueiro.easyrepair.api.client.controller.InsuranceCompanyController;
import es.regueiro.easyrepair.api.client.controller.VehicleController;
import es.regueiro.easyrepair.model.client.Client;
import es.regueiro.easyrepair.model.client.InsuranceCompany;
import es.regueiro.easyrepair.model.client.Vehicle;
import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.NIF;
import es.regueiro.easyrepair.model.shared.Phone;
import es.regueiro.easyrepair.model.user.Privilege;
import es.regueiro.easyrepair.reports.ReportPrinter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.ActionMap;
import javax.swing.SwingWorker;
import javax.swing.event.*;
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
import es.regueiro.easyrepair.login.SecurityManager;
import java.util.Iterator;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.client//ClientEditor//EN",
autostore = false)
@TopComponent.Description(iconBase = "es/regueiro/easyrepair/ui/client/icons/client_edit.png", preferredID = "ClientEditorTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 1, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.client.ClientEditorTopComponent")
@TopComponent.OpenActionRegistration(displayName = "#CTL_ClientEditorAction",
preferredID = "ClientEditorTopComponent")
@Messages({
    "CTL_ClientEditorAction=Editor de clientes",
    "CTL_ClientEditorTopComponent=Editor de clientes",
    "HINT_ClientEditorTopComponent=Esta es una ventana del editor de clientes"
})
public final class ClientEditorTopComponent extends TopComponent implements LookupListener, DocumentListener, TableModelListener, ListSelectionListener {

    private Client client = null;
    private Client oldClient = null;
    private Lookup.Result<Client> result;
    private InstanceContent content = new InstanceContent();
    private UndoRedo.Manager manager = new UndoRedo.Manager();
    private ClientController controller = Lookup.getDefault().lookup(ClientController.class);
    private InsuranceCompanyController insComController = Lookup.getDefault().lookup(InsuranceCompanyController.class);
    private DialogDescriptor editEmaildialog;
    private DialogDescriptor editAddressdialog;
    private DialogDescriptor editPhonedialog;
    private DialogDescriptor editVehicledialog;
    private DialogDescriptor browseInsuranceCompaniesDialog;
    private boolean modified = false;
    private boolean newClient = true;
    private boolean invalid = false;
    private PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    private Vehicle lastVehicle = null;
    private Email lastEmail = null;
    private Phone lastPhone = null;
    private Address lastAddress = null;
    private Vehicle selectedVehicle = null;
    private VehicleController vehicleController = Lookup.getDefault().lookup(VehicleController.class);
    private InsuranceCompany insuranceCompany;
    private InsuranceCompanyTableModel insuranceCompanyModel = new InsuranceCompanyTableModel();
    private InsuranceCompany selectedInsuranceCompany = null;
    private DialogDescriptor clientBrowserDialog;

    public ClientEditorTopComponent() {
        initComponents();
        setName(Bundle.CTL_ClientEditorTopComponent());
        setToolTipText(Bundle.HINT_ClientEditorTopComponent());
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
        setupVehicleViewComboBox();




        insuranceCompanyTable.setModel(insuranceCompanyModel);

        for (int i = 0; i < insuranceCompanyTable.getColumnCount(); i++) {
            insuranceCompanyTable.getColumnModel().getColumn(i).setCellRenderer(new InsuranceTableCellRenderer());
        }

        String[] inCBStrings = {java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("NAME"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("NIF"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("WEB"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EMAIL_LABEL"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EMAIL_ADDRESS"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("PHONE_LABEL"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("PHONE_NUMBER"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_LABEL"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_STREET"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_CITY"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_POSTAL_CODE"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_PROVINCE"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_COUNTRY")};

        for (String s : inCBStrings) {
            inCombobox.addItem(s);
        }




        mainTabbedPane.setIconAt(1, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/book.png")));
        mainTabbedPane.setIconAt(3, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/email.png")));
        mainTabbedPane.setIconAt(2, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/phone.png")));
        mainTabbedPane.setIconAt(0, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/client.png")));
        mainTabbedPane.setIconAt(4, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/car.png")));
//        emailEditAddressTextField.getDocument().addUndoableEditListener(manager);


        clientIdTextField.setDocument(new MaxLengthTextDocument(11));
        nameTextField.setDocument(new MaxLengthTextDocument(100));
        surnameTextField.setDocument(new MaxLengthTextDocument(100));
        nifTextField.setDocument(new MaxLengthTextDocument(9));



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

        vehicleEditColourTextField.setDocument(new MaxLengthTextDocument(20));
        vehicleEditFuelTextField.setDocument(new MaxLengthTextDocument(20));
        vehicleEditInsuranceNumberTextField.setDocument(new MaxLengthTextDocument(20));
        vehicleEditMakeTextField.setDocument(new MaxLengthTextDocument(50));
        vehicleEditModelTextField.setDocument(new MaxLengthTextDocument(50));
        vehicleEditRegistrationTextField.setDocument(new MaxLengthTextDocument(20));
        vehicleEditTypeTextField.setDocument(new MaxLengthTextDocument(20));
        vehicleEditVinTextField.setDocument(new MaxLengthTextDocument(17));
        vehicleEditYearTextField.setDocument(new MaxLengthTextDocument(4));



        clientIdTextField.getDocument().addDocumentListener(this);
        notesTextArea.getDocument().addDocumentListener(this);
        nifTextField.getDocument().addDocumentListener(this);
        surnameTextField.getDocument().addDocumentListener(this);
        nameTextField.getDocument().addDocumentListener(this);

        setupUndo();

        insuranceCompanyModel.addTableModelListener(this);
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
        vehicleEditPanel = new javax.swing.JPanel();
        vehicleEditLabelLabel = new javax.swing.JLabel();
        vehicleEditRegistrationLabel = new javax.swing.JLabel();
        vehicleEditRegistrationTextField = new javax.swing.JTextField();
        vehicleEditVinLabel = new javax.swing.JLabel();
        vehicleEditVinTextField = new javax.swing.JTextField();
        vehicleEditMakeLabel = new javax.swing.JLabel();
        vehicleEditMakeTextField = new javax.swing.JTextField();
        vehicleEditModelLabel = new javax.swing.JLabel();
        vehicleEditModelTextField = new javax.swing.JTextField();
        vehicleEditYearLabel = new javax.swing.JLabel();
        vehicleEditYearTextField = new javax.swing.JTextField();
        vehicleEditColourLabel = new javax.swing.JLabel();
        vehicleEditColourTextField = new javax.swing.JTextField();
        vehicleEditTypeLabel = new javax.swing.JLabel();
        vehicleEditTypeTextField = new javax.swing.JTextField();
        vehicleEditFuelLabel = new javax.swing.JLabel();
        vehicleEditFuelTextField = new javax.swing.JTextField();
        vehicleEditInsuranceCompanyLabel = new javax.swing.JLabel();
        vehicleEditInsuranceCompanyPanel = new javax.swing.JPanel();
        vehicleEditInsuranceCompanyTextField = new javax.swing.JTextField();
        vehicleEditChooseInsuranceCompanyButton = new javax.swing.JButton();
        vehicleEditRemoveInsuranceCompanyButton = new javax.swing.JButton();
        vehicleEditInsuranceNumberLabel = new javax.swing.JLabel();
        vehicleEditInsuranceNumberTextField = new javax.swing.JTextField();
        vehicleEditNotesLabel = new javax.swing.JLabel();
        vehicleEditNotesScrollPane = new javax.swing.JScrollPane();
        vehicleEditNotesTextArea = new javax.swing.JTextArea();
        insuranceCompanyBrowserPanel = new javax.swing.JPanel();
        insuranceCompanySearchPanel = new javax.swing.JPanel();
        searchEntryPanel = new javax.swing.JPanel();
        searchLabel = new javax.swing.JLabel();
        searchEntryField = new javax.swing.JTextField();
        inLabel = new javax.swing.JLabel();
        listAllButton = new javax.swing.JButton();
        inCombobox = new javax.swing.JComboBox();
        includeDisabledCheckBox = new javax.swing.JCheckBox();
        searchButtonPanel = new javax.swing.JPanel();
        findButton = new javax.swing.JButton();
        insuranceCompanyTablePanel = new javax.swing.JScrollPane();
        insuranceCompanyTable = new javax.swing.JTable();
        mainTabbedPane = new javax.swing.JTabbedPane();
        mainScrollPane = new javax.swing.JScrollPane();
        mainGeneralPanel = new javax.swing.JPanel();
        generalPanel = new javax.swing.JPanel();
        generalLabel = new javax.swing.JLabel();
        idLabel = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();
        clientIdLabel = new javax.swing.JLabel();
        clientIdTextField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        surnameLabel = new javax.swing.JLabel();
        surnameTextField = new javax.swing.JTextField();
        nifLabel = new javax.swing.JLabel();
        nifTextField = new javax.swing.JTextField();
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
        emaiViewPanel = new javax.swing.JPanel();
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
        vehicleScrollPane = new javax.swing.JScrollPane();
        vehicleViewPanel = new javax.swing.JPanel();
        vehicleLabel = new javax.swing.JLabel();
        vehicleViewActionsPanel = new javax.swing.JPanel();
        vehicleViewComboBox = new javax.swing.JComboBox();
        vehicleViewButtonsPanel = new javax.swing.JPanel();
        vehicleViewAddButton = new javax.swing.JButton();
        vehicleViewEditButton = new javax.swing.JButton();
        vehicleViewEnableButton = new javax.swing.JButton();
        vehicleViewDisableButton = new javax.swing.JButton();
        vehicleViewDeleteButton = new javax.swing.JButton();
        vehicleViewRegistrationLabel = new javax.swing.JLabel();
        vehicleViewRegistrationTextField = new javax.swing.JTextField();
        vehicleViewVinLabel = new javax.swing.JLabel();
        vehicleViewVinTextField = new javax.swing.JTextField();
        vehicleViewMakeLabel = new javax.swing.JLabel();
        vehicleViewMakeTextField = new javax.swing.JTextField();
        vehicleViewModelLabel = new javax.swing.JLabel();
        vehicleViewModelTextField = new javax.swing.JTextField();
        vehicleViewYearLabel = new javax.swing.JLabel();
        vehicleViewYearTextField = new javax.swing.JTextField();
        vehicleViewColourLabel = new javax.swing.JLabel();
        vehicleViewColourTextField = new javax.swing.JTextField();
        vehicleViewTypeLabel = new javax.swing.JLabel();
        vehicleViewTypeTextField = new javax.swing.JTextField();
        vehicleViewFuelLabel = new javax.swing.JLabel();
        vehicleViewFuelTextField = new javax.swing.JTextField();
        vehicleViewInsuranceCompanyLabel = new javax.swing.JLabel();
        vehicleViewInsuranceCompanyTextField = new javax.swing.JTextField();
        vehicleViewInsuranceNumberLabel = new javax.swing.JLabel();
        vehicleViewInsuranceNumberTextField = new javax.swing.JTextField();
        vehicleViewNotesLabel = new javax.swing.JLabel();
        vehicleViewNotesScrollPane = new javax.swing.JScrollPane();
        vehicleViewNotesTextArea = new javax.swing.JTextArea();
        topToolBar = new javax.swing.JToolBar();
        saveClientButton = new javax.swing.JButton();
        reloadClientButton = new javax.swing.JButton();
        enableClientButton = new javax.swing.JButton();
        disableClientButton = new javax.swing.JButton();
        deleteClientButton = new javax.swing.JButton();
        printClientButton = new javax.swing.JButton();

        addressEditPanel.setPreferredSize(new java.awt.Dimension(400, 400));
        addressEditPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(addressEditLabelLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressEditLabelLabel.text")); // NOI18N
        addressEditLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditStreetLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressEditStreetLabel.text")); // NOI18N
        addressEditStreetLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditStreetLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditCityLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressEditCityLabel.text")); // NOI18N
        addressEditCityLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditCityLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditProvinceLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressEditProvinceLabel.text")); // NOI18N
        addressEditProvinceLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditProvinceLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditPostalCodeLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressEditPostalCodeLabel.text")); // NOI18N
        addressEditPostalCodeLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditPostalCodeLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditCountryLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressEditCountryLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(addressEditNotesLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressEditNotesLabel.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(addressEditLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressEditLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        addressEditPanel.add(addressEditLabel, gridBagConstraints);

        phoneEditPanel.setPreferredSize(new java.awt.Dimension(400, 400));
        phoneEditPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(phoneEditLabelLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.phoneEditLabelLabel.text")); // NOI18N
        phoneEditLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneEditPanel.add(phoneEditLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(phoneEditNumberLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.phoneEditNumberLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(phoneEditNotesLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.phoneEditNotesLabel.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(phoneEditLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.phoneEditLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        phoneEditPanel.add(phoneEditLabel, gridBagConstraints);

        emailEditPanel.setPreferredSize(new java.awt.Dimension(400, 400));
        emailEditPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(emailEditLabelLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.emailEditLabelLabel.text")); // NOI18N
        emailEditLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emailEditPanel.add(emailEditLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(emailEditAddressLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.emailEditAddressLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(emailEditNotesLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.emailEditNotesLabel.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(emailEditLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.emailEditLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        emailEditPanel.add(emailEditLabel, gridBagConstraints);

        vehicleEditPanel.setLayout(new java.awt.GridBagLayout());

        vehicleEditLabelLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        vehicleEditLabelLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(vehicleEditLabelLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleEditLabelLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        vehicleEditPanel.add(vehicleEditLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleEditRegistrationLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleEditRegistrationLabel.text")); // NOI18N
        vehicleEditRegistrationLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditRegistrationLabel, gridBagConstraints);

        vehicleEditRegistrationTextField.setDoubleBuffered(true);
        vehicleEditRegistrationTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleEditRegistrationTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditRegistrationTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleEditVinLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleEditVinLabel.text")); // NOI18N
        vehicleEditVinLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditVinLabel, gridBagConstraints);

        vehicleEditVinTextField.setDoubleBuffered(true);
        vehicleEditVinTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleEditVinTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditVinTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleEditMakeLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleEditMakeLabel.text")); // NOI18N
        vehicleEditMakeLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditMakeLabel, gridBagConstraints);

        vehicleEditMakeTextField.setDoubleBuffered(true);
        vehicleEditMakeTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleEditMakeTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditMakeTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleEditModelLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleEditModelLabel.text")); // NOI18N
        vehicleEditModelLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditModelLabel, gridBagConstraints);

        vehicleEditModelTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleEditModelTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditModelTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleEditYearLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleEditYearLabel.text")); // NOI18N
        vehicleEditYearLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditYearLabel, gridBagConstraints);

        vehicleEditYearTextField.setDoubleBuffered(true);
        vehicleEditYearTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleEditYearTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditYearTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleEditColourLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleEditColourLabel.text")); // NOI18N
        vehicleEditColourLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditColourLabel, gridBagConstraints);

        vehicleEditColourTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleEditColourTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditColourTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleEditTypeLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleEditTypeLabel.text")); // NOI18N
        vehicleEditTypeLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditTypeLabel, gridBagConstraints);

        vehicleEditTypeTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleEditTypeTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditTypeTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleEditFuelLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleEditFuelLabel.text")); // NOI18N
        vehicleEditFuelLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditFuelLabel, gridBagConstraints);

        vehicleEditFuelTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleEditFuelTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditFuelTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleEditInsuranceCompanyLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleEditInsuranceCompanyLabel.text")); // NOI18N
        vehicleEditInsuranceCompanyLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditInsuranceCompanyLabel, gridBagConstraints);

        vehicleEditInsuranceCompanyPanel.setLayout(new java.awt.GridBagLayout());

        vehicleEditInsuranceCompanyTextField.setBackground(new java.awt.Color(232, 231, 231));
        vehicleEditInsuranceCompanyTextField.setEditable(false);
        vehicleEditInsuranceCompanyTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleEditInsuranceCompanyTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditInsuranceCompanyPanel.add(vehicleEditInsuranceCompanyTextField, gridBagConstraints);

        vehicleEditChooseInsuranceCompanyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/accept.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(vehicleEditChooseInsuranceCompanyButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleEditChooseInsuranceCompanyButton.text")); // NOI18N
        vehicleEditChooseInsuranceCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vehicleEditChooseInsuranceCompanyButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        vehicleEditInsuranceCompanyPanel.add(vehicleEditChooseInsuranceCompanyButton, gridBagConstraints);

        vehicleEditRemoveInsuranceCompanyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/delete.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(vehicleEditRemoveInsuranceCompanyButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleEditRemoveInsuranceCompanyButton.text")); // NOI18N
        vehicleEditRemoveInsuranceCompanyButton.setEnabled(false);
        vehicleEditRemoveInsuranceCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vehicleEditRemoveInsuranceCompanyButtonActionPerformed(evt);
            }
        });
        vehicleEditInsuranceCompanyPanel.add(vehicleEditRemoveInsuranceCompanyButton, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        vehicleEditPanel.add(vehicleEditInsuranceCompanyPanel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleEditInsuranceNumberLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleEditInsuranceNumberLabel.text")); // NOI18N
        vehicleEditInsuranceNumberLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditInsuranceNumberLabel, gridBagConstraints);

        vehicleEditInsuranceNumberTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleEditInsuranceNumberTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditInsuranceNumberTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleEditNotesLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleEditNotesLabel.text")); // NOI18N
        vehicleEditNotesLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditNotesLabel, gridBagConstraints);

        vehicleEditNotesTextArea.setColumns(20);
        vehicleEditNotesTextArea.setRows(5);
        vehicleEditNotesScrollPane.setViewportView(vehicleEditNotesTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleEditPanel.add(vehicleEditNotesScrollPane, gridBagConstraints);

        insuranceCompanyBrowserPanel.setLayout(new java.awt.GridBagLayout());

        insuranceCompanySearchPanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        insuranceCompanySearchPanel.setLayout(new java.awt.GridBagLayout());

        searchEntryPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(searchLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.searchLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(inLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.inLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(inLabel, gridBagConstraints);

        listAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/group.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(listAllButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.listAllButton.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(includeDisabledCheckBox, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.includeDisabledCheckBox.text")); // NOI18N
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
        insuranceCompanySearchPanel.add(searchEntryPanel, gridBagConstraints);

        searchButtonPanel.setLayout(new java.awt.BorderLayout(5, 5));

        findButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/find32.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(findButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.findButton.text")); // NOI18N
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
        insuranceCompanySearchPanel.add(searchButtonPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        insuranceCompanyBrowserPanel.add(insuranceCompanySearchPanel, gridBagConstraints);

        insuranceCompanyTablePanel.setMinimumSize(new java.awt.Dimension(100, 100));

        insuranceCompanyTable.setAutoCreateRowSorter(true);
        insuranceCompanyTable.setDoubleBuffered(true);
        insuranceCompanyTable.setFillsViewportHeight(true);
        insuranceCompanyTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        insuranceCompanyTable.setRowHeight(25);
        insuranceCompanyTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        insuranceCompanyTablePanel.setViewportView(insuranceCompanyTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 7);
        insuranceCompanyBrowserPanel.add(insuranceCompanyTablePanel, gridBagConstraints);

        setLayout(new java.awt.GridBagLayout());

        mainTabbedPane.setDoubleBuffered(true);

        mainScrollPane.setBorder(null);

        mainGeneralPanel.setLayout(new java.awt.GridBagLayout());

        generalPanel.setLayout(new java.awt.GridBagLayout());

        generalLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        generalLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(generalLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.generalLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        generalPanel.add(generalLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(idLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.idLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(clientIdLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.clientIdLabel.text")); // NOI18N
        clientIdLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(clientIdLabel, gridBagConstraints);

        clientIdTextField.setDoubleBuffered(true);
        clientIdTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        clientIdTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(clientIdTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(nameLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.nameLabel.text")); // NOI18N
        nameLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(nameLabel, gridBagConstraints);

        nameTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        nameTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(nameTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(surnameLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.surnameLabel.text")); // NOI18N
        surnameLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(surnameLabel, gridBagConstraints);

        surnameTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        surnameTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(surnameTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(nifLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.nifLabel.text")); // NOI18N
        nifLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(nifLabel, gridBagConstraints);

        nifTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        nifTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(nifTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        mainGeneralPanel.add(generalPanel, gridBagConstraints);

        notesPanel.setLayout(new java.awt.GridBagLayout());

        notesLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        notesLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(notesLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.notesLabel.text")); // NOI18N
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

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.mainScrollPane.TabConstraints.tabTitle"), mainScrollPane); // NOI18N

        addressScrollPane.setBorder(null);

        addressViewPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(addressViewLabelLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressViewLabelLabel.text")); // NOI18N
        addressViewLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewStreetLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressViewStreetLabel.text")); // NOI18N
        addressViewStreetLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewStreetLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewCityLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressViewCityLabel.text")); // NOI18N
        addressViewCityLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewCityLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewProvinceLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressViewProvinceLabel.text")); // NOI18N
        addressViewProvinceLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewProvinceLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewPostalCodeLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressViewPostalCodeLabel.text")); // NOI18N
        addressViewPostalCodeLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewPostalCodeLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewCountryLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressViewCountryLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(addressViewNotesLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressViewNotesLabel.text")); // NOI18N
        addressViewNotesLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewNotesLabel, gridBagConstraints);

        addressViewNotesTextArea.setEditable(false);
        addressViewNotesTextArea.setBackground(new java.awt.Color(232, 231, 231));
        addressViewNotesTextArea.setColumns(20);
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
        org.openide.awt.Mnemonics.setLocalizedText(addressViewLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressViewLabel.text")); // NOI18N
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

        addressViewAddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addressViewAddButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressViewAddButton.text")); // NOI18N
        addressViewAddButton.setMinimumSize(new java.awt.Dimension(32, 32));
        addressViewAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressViewAddButtonActionPerformed(evt);
            }
        });
        addressViewButtonsPanel.add(addressViewAddButton);

        addressViewEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/pencil.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addressViewEditButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressViewEditButton.text")); // NOI18N
        addressViewEditButton.setEnabled(false);
        addressViewEditButton.setMinimumSize(new java.awt.Dimension(32, 32));
        addressViewEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressViewEditButtonActionPerformed(evt);
            }
        });
        addressViewButtonsPanel.add(addressViewEditButton);

        addressViewDeleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addressViewDeleteButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressViewDeleteButton.text")); // NOI18N
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

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.addressScrollPane.TabConstraints.tabTitle"), addressScrollPane); // NOI18N

        phoneScrollPane.setBorder(null);

        phoneViewPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(phoneViewLabelLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.phoneViewLabelLabel.text")); // NOI18N
        phoneViewLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneViewPanel.add(phoneViewLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(phoneViewNumberLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.phoneViewNumberLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(phoneViewNotesLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.phoneViewNotesLabel.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(phoneViewLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.phoneViewLabel.text")); // NOI18N
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

        phoneViewAddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(phoneViewAddButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.phoneViewAddButton.text")); // NOI18N
        phoneViewAddButton.setMinimumSize(new java.awt.Dimension(32, 32));
        phoneViewAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneViewAddButtonActionPerformed(evt);
            }
        });
        phoneViewButtonsPanel.add(phoneViewAddButton);

        phoneViewEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/pencil.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(phoneViewEditButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.phoneViewEditButton.text")); // NOI18N
        phoneViewEditButton.setEnabled(false);
        phoneViewEditButton.setMinimumSize(new java.awt.Dimension(32, 32));
        phoneViewEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneViewEditButtonActionPerformed(evt);
            }
        });
        phoneViewButtonsPanel.add(phoneViewEditButton);

        phoneViewDeleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(phoneViewDeleteButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.phoneViewDeleteButton.text")); // NOI18N
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

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.phoneScrollPane.TabConstraints.tabTitle"), phoneScrollPane); // NOI18N

        emailScrollPane.setBorder(null);

        emaiViewPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(emailViewLabelLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.emailViewLabelLabel.text")); // NOI18N
        emailViewLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emaiViewPanel.add(emailViewLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(emailViewAddressLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.emailViewAddressLabel.text")); // NOI18N
        emailViewAddressLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emaiViewPanel.add(emailViewAddressLabel, gridBagConstraints);

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
        emaiViewPanel.add(emailViewLabelTextField, gridBagConstraints);

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
        emaiViewPanel.add(emailViewAddressTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(emailViewNotesLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.emailViewNotesLabel.text")); // NOI18N
        emailViewNotesLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emaiViewPanel.add(emailViewNotesLabel, gridBagConstraints);

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
        emaiViewPanel.add(emailViewNotesScrollPane, gridBagConstraints);

        emailLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        emailLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(emailLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.emailLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        emaiViewPanel.add(emailLabel, gridBagConstraints);

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

        emailViewAddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(emailViewAddButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.emailViewAddButton.text")); // NOI18N
        emailViewAddButton.setMinimumSize(new java.awt.Dimension(32, 32));
        emailViewAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailViewAddButtonActionPerformed(evt);
            }
        });
        emailViewButtonsPanel.add(emailViewAddButton);

        emailViewEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/pencil.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(emailViewEditButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.emailViewEditButton.text")); // NOI18N
        emailViewEditButton.setEnabled(false);
        emailViewEditButton.setMinimumSize(new java.awt.Dimension(32, 32));
        emailViewEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailViewEditButtonActionPerformed(evt);
            }
        });
        emailViewButtonsPanel.add(emailViewEditButton);

        emailViewDeleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(emailViewDeleteButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.emailViewDeleteButton.text")); // NOI18N
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
        emaiViewPanel.add(emailViewActionsPanel, gridBagConstraints);

        emailScrollPane.setViewportView(emaiViewPanel);

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.emailScrollPane.TabConstraints.tabTitle"), emailScrollPane); // NOI18N

        vehicleScrollPane.setBorder(null);

        vehicleViewPanel.setLayout(new java.awt.GridBagLayout());

        vehicleLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        vehicleLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(vehicleLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        vehicleViewPanel.add(vehicleLabel, gridBagConstraints);

        vehicleViewActionsPanel.setLayout(new java.awt.GridBagLayout());

        vehicleViewComboBox.setEnabled(false);
        vehicleViewComboBox.setMinimumSize(new java.awt.Dimension(100, 24));
        vehicleViewComboBox.setPreferredSize(new java.awt.Dimension(100, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewActionsPanel.add(vehicleViewComboBox, gridBagConstraints);

        vehicleViewAddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(vehicleViewAddButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleViewAddButton.text")); // NOI18N
        vehicleViewAddButton.setMinimumSize(new java.awt.Dimension(32, 32));
        vehicleViewAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vehicleViewAddButtonActionPerformed(evt);
            }
        });
        vehicleViewButtonsPanel.add(vehicleViewAddButton);

        vehicleViewEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/pencil.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(vehicleViewEditButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleViewEditButton.text")); // NOI18N
        vehicleViewEditButton.setEnabled(false);
        vehicleViewEditButton.setMinimumSize(new java.awt.Dimension(32, 32));
        vehicleViewEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vehicleViewEditButtonActionPerformed(evt);
            }
        });
        vehicleViewButtonsPanel.add(vehicleViewEditButton);

        vehicleViewEnableButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/status_online.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(vehicleViewEnableButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleViewEnableButton.text")); // NOI18N
        vehicleViewEnableButton.setEnabled(false);
        vehicleViewEnableButton.setMinimumSize(new java.awt.Dimension(32, 32));
        vehicleViewEnableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vehicleViewEnableButtonActionPerformed(evt);
            }
        });
        vehicleViewButtonsPanel.add(vehicleViewEnableButton);

        vehicleViewDisableButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/status_offline.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(vehicleViewDisableButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleViewDisableButton.text")); // NOI18N
        vehicleViewDisableButton.setEnabled(false);
        vehicleViewDisableButton.setMinimumSize(new java.awt.Dimension(32, 32));
        vehicleViewDisableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vehicleViewDisableButtonActionPerformed(evt);
            }
        });
        vehicleViewButtonsPanel.add(vehicleViewDisableButton);

        vehicleViewDeleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(vehicleViewDeleteButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleViewDeleteButton.text")); // NOI18N
        vehicleViewDeleteButton.setEnabled(false);
        vehicleViewDeleteButton.setMinimumSize(new java.awt.Dimension(32, 32));
        vehicleViewDeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vehicleViewDeleteButtonActionPerformed(evt);
            }
        });
        vehicleViewButtonsPanel.add(vehicleViewDeleteButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        vehicleViewActionsPanel.add(vehicleViewButtonsPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        vehicleViewPanel.add(vehicleViewActionsPanel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleViewRegistrationLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleViewRegistrationLabel.text")); // NOI18N
        vehicleViewRegistrationLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewRegistrationLabel, gridBagConstraints);

        vehicleViewRegistrationTextField.setBackground(new java.awt.Color(232, 231, 231));
        vehicleViewRegistrationTextField.setEditable(false);
        vehicleViewRegistrationTextField.setDoubleBuffered(true);
        vehicleViewRegistrationTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleViewRegistrationTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewRegistrationTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleViewVinLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleViewVinLabel.text")); // NOI18N
        vehicleViewVinLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewVinLabel, gridBagConstraints);

        vehicleViewVinTextField.setBackground(new java.awt.Color(232, 231, 231));
        vehicleViewVinTextField.setEditable(false);
        vehicleViewVinTextField.setDoubleBuffered(true);
        vehicleViewVinTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleViewVinTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewVinTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleViewMakeLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleViewMakeLabel.text")); // NOI18N
        vehicleViewMakeLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewMakeLabel, gridBagConstraints);

        vehicleViewMakeTextField.setBackground(new java.awt.Color(232, 231, 231));
        vehicleViewMakeTextField.setEditable(false);
        vehicleViewMakeTextField.setDoubleBuffered(true);
        vehicleViewMakeTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleViewMakeTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewMakeTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleViewModelLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleViewModelLabel.text")); // NOI18N
        vehicleViewModelLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewModelLabel, gridBagConstraints);

        vehicleViewModelTextField.setBackground(new java.awt.Color(232, 231, 231));
        vehicleViewModelTextField.setEditable(false);
        vehicleViewModelTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleViewModelTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewModelTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleViewYearLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleViewYearLabel.text")); // NOI18N
        vehicleViewYearLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewYearLabel, gridBagConstraints);

        vehicleViewYearTextField.setBackground(new java.awt.Color(232, 231, 231));
        vehicleViewYearTextField.setEditable(false);
        vehicleViewYearTextField.setDoubleBuffered(true);
        vehicleViewYearTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleViewYearTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewYearTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleViewColourLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleViewColourLabel.text")); // NOI18N
        vehicleViewColourLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewColourLabel, gridBagConstraints);

        vehicleViewColourTextField.setBackground(new java.awt.Color(232, 231, 231));
        vehicleViewColourTextField.setEditable(false);
        vehicleViewColourTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleViewColourTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewColourTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleViewTypeLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleViewTypeLabel.text")); // NOI18N
        vehicleViewTypeLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewTypeLabel, gridBagConstraints);

        vehicleViewTypeTextField.setBackground(new java.awt.Color(232, 231, 231));
        vehicleViewTypeTextField.setEditable(false);
        vehicleViewTypeTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleViewTypeTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewTypeTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleViewFuelLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleViewFuelLabel.text")); // NOI18N
        vehicleViewFuelLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewFuelLabel, gridBagConstraints);

        vehicleViewFuelTextField.setBackground(new java.awt.Color(232, 231, 231));
        vehicleViewFuelTextField.setEditable(false);
        vehicleViewFuelTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleViewFuelTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewFuelTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleViewInsuranceCompanyLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleViewInsuranceCompanyLabel.text")); // NOI18N
        vehicleViewInsuranceCompanyLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewInsuranceCompanyLabel, gridBagConstraints);

        vehicleViewInsuranceCompanyTextField.setBackground(new java.awt.Color(232, 231, 231));
        vehicleViewInsuranceCompanyTextField.setEditable(false);
        vehicleViewInsuranceCompanyTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleViewInsuranceCompanyTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewInsuranceCompanyTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleViewInsuranceNumberLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleViewInsuranceNumberLabel.text")); // NOI18N
        vehicleViewInsuranceNumberLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewInsuranceNumberLabel, gridBagConstraints);

        vehicleViewInsuranceNumberTextField.setBackground(new java.awt.Color(232, 231, 231));
        vehicleViewInsuranceNumberTextField.setEditable(false);
        vehicleViewInsuranceNumberTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        vehicleViewInsuranceNumberTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewInsuranceNumberTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(vehicleViewNotesLabel, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleViewNotesLabel.text")); // NOI18N
        vehicleViewNotesLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewNotesLabel, gridBagConstraints);

        vehicleViewNotesTextArea.setBackground(new java.awt.Color(232, 231, 231));
        vehicleViewNotesTextArea.setColumns(20);
        vehicleViewNotesTextArea.setEditable(false);
        vehicleViewNotesTextArea.setRows(5);
        vehicleViewNotesScrollPane.setViewportView(vehicleViewNotesTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        vehicleViewPanel.add(vehicleViewNotesScrollPane, gridBagConstraints);

        vehicleScrollPane.setViewportView(vehicleViewPanel);

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.vehicleScrollPane.TabConstraints.tabTitle"), vehicleScrollPane); // NOI18N

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

        saveClientButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/disk.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(saveClientButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.saveClientButton.text")); // NOI18N
        saveClientButton.setFocusable(false);
        saveClientButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        saveClientButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        saveClientButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        saveClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveClientButtonActionPerformed(evt);
            }
        });
        topToolBar.add(saveClientButton);

        reloadClientButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/arrow_refresh.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(reloadClientButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.reloadClientButton.text")); // NOI18N
        reloadClientButton.setFocusable(false);
        reloadClientButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        reloadClientButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        reloadClientButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        reloadClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadClientButtonActionPerformed(evt);
            }
        });
        topToolBar.add(reloadClientButton);

        enableClientButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/status_online.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(enableClientButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.enableClientButton.text")); // NOI18N
        enableClientButton.setFocusable(false);
        enableClientButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        enableClientButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        enableClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableClientButtonActionPerformed(evt);
            }
        });
        topToolBar.add(enableClientButton);

        disableClientButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/status_offline.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(disableClientButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.disableClientButton.text")); // NOI18N
        disableClientButton.setFocusable(false);
        disableClientButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        disableClientButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        disableClientButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        disableClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disableClientButtonActionPerformed(evt);
            }
        });
        topToolBar.add(disableClientButton);

        deleteClientButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteClientButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.deleteClientButton.text")); // NOI18N
        deleteClientButton.setFocusable(false);
        deleteClientButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deleteClientButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deleteClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteClientButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deleteClientButton);

        printClientButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printClientButton, org.openide.util.NbBundle.getMessage(ClientEditorTopComponent.class, "ClientEditorTopComponent.printClientButton.text")); // NOI18N
        printClientButton.setFocusable(false);
        printClientButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printClientButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        printClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printClientButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printClientButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(topToolBar, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void addressViewAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressViewAddButtonActionPerformed
        addressEditStreetTextField.setText("");
        addressEditProvinceTextField.setText("");
        addressEditPostalCodeTextField.setText("");
        addressEditCityTextField.setText("");
        addressEditCountryTextField.setText("");
        addressEditLabelTextField.setText("");
        addressEditNotesTextArea.setText("");


        editAddressdialog = new DialogDescriptor(addressEditPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADD_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
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

    private void addressViewEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressViewEditButtonActionPerformed
        addressEditStreetTextField.setText(addressViewStreetTextField.getText());
        addressEditProvinceTextField.setText(addressViewProvinceTextField.getText());
        addressEditPostalCodeTextField.setText(addressViewPostalCodeTextField.getText());
        addressEditCityTextField.setText(addressViewCityTextField.getText());
        addressEditCountryTextField.setText(addressViewCountryTextField.getText());
        addressEditLabelTextField.setText(addressViewLabelTextField.getText());
        addressEditNotesTextArea.setText(addressViewNotesTextArea.getText());


        editAddressdialog = new DialogDescriptor(addressEditPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EDIT_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
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

    private void phoneViewAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneViewAddButtonActionPerformed
        phoneEditNumberTextField.setText("");
        phoneEditLabelTextField.setText("");
        phoneEditNotesTextArea.setText("");


        editPhonedialog = new DialogDescriptor(phoneEditPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADD_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("PHONE")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
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


        editPhonedialog = new DialogDescriptor(phoneEditPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EDIT_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("PHONE")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
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

        editEmaildialog = new DialogDescriptor(emailEditPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADD_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EMAIL")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
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


        editEmaildialog = new DialogDescriptor(emailEditPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EDIT_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EMAIL")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
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

    private void addressViewDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressViewDeleteButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

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
                    client.removeAddress(addressViewLabelTextField.getText());
                    lastAddress = null;
                    return null;
                }
            };


            worker.execute();
        }
    }//GEN-LAST:event_addressViewDeleteButtonActionPerformed

    private void phoneViewDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneViewDeleteButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("PHONE")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

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
                    client.removePhone(phoneViewLabelTextField.getText());
                    lastPhone = null;
                    return null;
                }
            };


            worker.execute();
        }
    }//GEN-LAST:event_phoneViewDeleteButtonActionPerformed

    private void emailViewDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailViewDeleteButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EMAIL")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

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
                    client.removeEmail(emailViewLabelTextField.getText());
                    lastEmail = null;
                    return null;
                }
            };


            worker.execute();
//            controller.removeEmail(emailViewLabelTextField.getText());
//            fillEmails();
        }
    }//GEN-LAST:event_emailViewDeleteButtonActionPerformed

    private void saveClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveClientButtonActionPerformed
        saveClient();
    }//GEN-LAST:event_saveClientButtonActionPerformed

    private void disableClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disableClientButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
//                disableClientButton.setVisible(false);
//                enableClientButton.setVisible(true);
                fillData();
                StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("DISABLED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CLIENT")));
            }

            @Override
            protected Void doInBackground() {
                if (updateClient()) {
                    client = controller.disableClient();
                }
                return null;
            }
        };

        worker.execute();

    }//GEN-LAST:event_disableClientButtonActionPerformed

    private void reloadClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadClientButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("WANT_RELOAD_CLIENT"),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("RELOAD"), NotifyDescriptor.YES_NO_OPTION);

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
                                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CLIENT_RELOAD_NOT_FOUND"),
                                    NotifyDescriptor.ERROR_MESSAGE);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                    StatusDisplayer.getDefault().setStatusText(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("RELOADED_CLIENT"));
                }

                @Override
                protected Boolean doInBackground() {
                    client = controller.reloadClient();
                    if (client != null) {
                        controller.setClient(client);
                        return true;
                    } else {
                        return false;
                    }
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_reloadClientButtonActionPerformed

    private void enableClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableClientButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
//                disableClientButton.setVisible(true);
//                enableClientButton.setVisible(false);
//                vehicleViewDisableButton.setVisible(true);
//                vehicleViewEnableButton.setVisible(false);
                fillData();
                StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ENABLED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CLIENT")));
            }

            @Override
            protected Void doInBackground() {
                if (updateClient()) {
                    client = controller.enableClient();
                }
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_enableClientButtonActionPerformed

    private void deleteClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteClientButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CLIENT")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {


            ClientLookup.getDefault().clear();

            ClientBrowserTopComponent window = (ClientBrowserTopComponent) WindowManager.getDefault().findTopComponent("ClientBrowserTopComponent");

            window.deleteClient(client);
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    forceClose();
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CLIENT")));
                }

                @Override
                protected Void doInBackground() {
                    controller.setClient(client);
                    controller.deleteClient();
                    return null;
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_deleteClientButtonActionPerformed

    private void vehicleViewAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vehicleViewAddButtonActionPerformed

        vehicleEditColourTextField.setText("");
        vehicleEditFuelTextField.setText("");
        vehicleEditInsuranceNumberTextField.setText("");
        vehicleEditMakeTextField.setText("");
        vehicleEditModelTextField.setText("");
        vehicleEditRegistrationTextField.setText("");
        vehicleEditTypeTextField.setText("");
        vehicleEditVinTextField.setText("");
        vehicleEditYearTextField.setText("");
        vehicleEditNotesTextArea.setText("");
        vehicleEditInsuranceCompanyTextField.setText("");
        insuranceCompany = null;
        vehicleEditRemoveInsuranceCompanyButton.setEnabled(false);


        editVehicledialog = new DialogDescriptor(vehicleEditPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADD_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("VEHICLE")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() == DialogDescriptor.OK_OPTION) {
                            if (addVehicle()) {
                                editVehicledialog.setClosingOptions(null);
                                modify();
                            }
                        }
                    }
                });
        editVehicledialog.setClosingOptions(new Object[]{DialogDescriptor.CANCEL_OPTION});
        DialogDisplayer.getDefault().createDialog(editVehicledialog);
        DialogDisplayer.getDefault().notify(editVehicledialog);
    }//GEN-LAST:event_vehicleViewAddButtonActionPerformed

    private void vehicleViewEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vehicleViewEditButtonActionPerformed
        vehicleEditColourTextField.setText(vehicleViewColourTextField.getText());
        vehicleEditFuelTextField.setText(vehicleViewFuelTextField.getText());
        vehicleEditInsuranceNumberTextField.setText(vehicleViewInsuranceNumberTextField.getText());
        vehicleEditMakeTextField.setText(vehicleViewMakeTextField.getText());
        vehicleEditModelTextField.setText(vehicleViewModelTextField.getText());
        vehicleEditRegistrationTextField.setText(vehicleViewRegistrationTextField.getText());
        vehicleEditTypeTextField.setText(vehicleViewTypeTextField.getText());
        vehicleEditVinTextField.setText(vehicleViewVinTextField.getText());
        vehicleEditYearTextField.setText(vehicleViewYearTextField.getText());
        vehicleEditNotesTextArea.setText(vehicleViewNotesTextArea.getText());
        vehicleEditInsuranceCompanyTextField.setText(vehicleViewInsuranceCompanyTextField.getText());
        insuranceCompany = selectedVehicle.getInsuranceCompany();
        vehicleEditRemoveInsuranceCompanyButton.setEnabled(insuranceCompany != null);


        editVehicledialog = new DialogDescriptor(vehicleEditPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EDIT_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("VEHICLE")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() == DialogDescriptor.OK_OPTION) {
                            if (updateVehicle()) {
                                editVehicledialog.setClosingOptions(null);
                                modify();
                            }
                        }
                    }
                });
        editVehicledialog.setClosingOptions(new Object[]{DialogDescriptor.CANCEL_OPTION});
        DialogDisplayer.getDefault().createDialog(editVehicledialog);
        DialogDisplayer.getDefault().notify(editVehicledialog);
    }//GEN-LAST:event_vehicleViewEditButtonActionPerformed

    private void vehicleViewDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vehicleViewDeleteButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("VEHICLE")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    fillVehicles();
                    modify();
                }

                @Override
                protected Void doInBackground() {
                    client.removeVehicle(vehicleViewRegistrationTextField.getText());
                    lastVehicle = null;
                    return null;
                }
            };


            worker.execute();
        }
    }//GEN-LAST:event_vehicleViewDeleteButtonActionPerformed

    private void vehicleViewDisableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vehicleViewDisableButtonActionPerformed

        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
                fillVehicles();
                modify();
            }

            @Override
            protected Void doInBackground() {
                selectedVehicle.setEnabled(false);
                lastVehicle = selectedVehicle;
                return null;
            }
        };


        worker.execute();

    }//GEN-LAST:event_vehicleViewDisableButtonActionPerformed

    private void vehicleViewEnableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vehicleViewEnableButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
                fillVehicles();
                modify();
            }

            @Override
            protected Void doInBackground() {
                selectedVehicle.setEnabled(true);
                lastVehicle = selectedVehicle;
                return null;
            }
        };


        worker.execute();
    }//GEN-LAST:event_vehicleViewEnableButtonActionPerformed

    private void vehicleEditChooseInsuranceCompanyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vehicleEditChooseInsuranceCompanyButtonActionPerformed
        browseInsuranceCompaniesDialog = new DialogDescriptor(insuranceCompanyBrowserPanel, java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SELECT_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCECOMPANY")), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() == DialogDescriptor.OK_OPTION) {
                            setSelectedInsuranceCompany();
                            browseInsuranceCompaniesDialog.setClosingOptions(null);
                            modify();
                        } else {
                            selectedInsuranceCompany = null;
                        }
                        insuranceCompanyTable.clearSelection();
                        insuranceCompanyModel.clear();
                    }
                });
        browseInsuranceCompaniesDialog.setClosingOptions(new Object[]{DialogDescriptor.CANCEL_OPTION});
        browseInsuranceCompaniesDialog.setValid(false);
        DialogDisplayer.getDefault().createDialog(browseInsuranceCompaniesDialog);
        DialogDisplayer.getDefault().notify(browseInsuranceCompaniesDialog);
    }//GEN-LAST:event_vehicleEditChooseInsuranceCompanyButtonActionPerformed

    private void vehicleEditRemoveInsuranceCompanyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vehicleEditRemoveInsuranceCompanyButtonActionPerformed
        insuranceCompany = null;
        vehicleEditInsuranceCompanyTextField.setText("");
        vehicleEditRemoveInsuranceCompanyButton.setEnabled(false);
    }//GEN-LAST:event_vehicleEditRemoveInsuranceCompanyButtonActionPerformed

    private void printClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printClientButtonActionPerformed
        if (updateClient()) {

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
        }
    }//GEN-LAST:event_printClientButtonActionPerformed

    private void listAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listAllButtonActionPerformed
        doListAllInsuranceCompanies();
    }//GEN-LAST:event_listAllButtonActionPerformed

    private void findButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findButtonActionPerformed
        doSearchInsuranceCompany();
    }//GEN-LAST:event_findButtonActionPerformed
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
    private javax.swing.JLabel clientIdLabel;
    private javax.swing.JTextField clientIdTextField;
    private javax.swing.JButton deleteClientButton;
    private javax.swing.JButton disableClientButton;
    private javax.swing.JPanel emaiViewPanel;
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
    private javax.swing.JButton enableClientButton;
    private javax.swing.JButton findButton;
    private javax.swing.JLabel generalLabel;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JLabel idLabel;
    private javax.swing.JTextField idTextField;
    private javax.swing.JComboBox inCombobox;
    private javax.swing.JLabel inLabel;
    private javax.swing.JCheckBox includeDisabledCheckBox;
    private javax.swing.JPanel insuranceCompanyBrowserPanel;
    private javax.swing.JPanel insuranceCompanySearchPanel;
    private javax.swing.JTable insuranceCompanyTable;
    private javax.swing.JScrollPane insuranceCompanyTablePanel;
    private javax.swing.JButton listAllButton;
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
    private javax.swing.JButton printClientButton;
    private javax.swing.JButton reloadClientButton;
    private javax.swing.JButton saveClientButton;
    private javax.swing.JPanel searchButtonPanel;
    private javax.swing.JTextField searchEntryField;
    private javax.swing.JPanel searchEntryPanel;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JLabel surnameLabel;
    private javax.swing.JTextField surnameTextField;
    private javax.swing.JToolBar topToolBar;
    private javax.swing.JButton vehicleEditChooseInsuranceCompanyButton;
    private javax.swing.JLabel vehicleEditColourLabel;
    private javax.swing.JTextField vehicleEditColourTextField;
    private javax.swing.JLabel vehicleEditFuelLabel;
    private javax.swing.JTextField vehicleEditFuelTextField;
    private javax.swing.JLabel vehicleEditInsuranceCompanyLabel;
    private javax.swing.JPanel vehicleEditInsuranceCompanyPanel;
    private javax.swing.JTextField vehicleEditInsuranceCompanyTextField;
    private javax.swing.JLabel vehicleEditInsuranceNumberLabel;
    private javax.swing.JTextField vehicleEditInsuranceNumberTextField;
    private javax.swing.JLabel vehicleEditLabelLabel;
    private javax.swing.JLabel vehicleEditMakeLabel;
    private javax.swing.JTextField vehicleEditMakeTextField;
    private javax.swing.JLabel vehicleEditModelLabel;
    private javax.swing.JTextField vehicleEditModelTextField;
    private javax.swing.JLabel vehicleEditNotesLabel;
    private javax.swing.JScrollPane vehicleEditNotesScrollPane;
    private javax.swing.JTextArea vehicleEditNotesTextArea;
    private javax.swing.JPanel vehicleEditPanel;
    private javax.swing.JLabel vehicleEditRegistrationLabel;
    private javax.swing.JTextField vehicleEditRegistrationTextField;
    private javax.swing.JButton vehicleEditRemoveInsuranceCompanyButton;
    private javax.swing.JLabel vehicleEditTypeLabel;
    private javax.swing.JTextField vehicleEditTypeTextField;
    private javax.swing.JLabel vehicleEditVinLabel;
    private javax.swing.JTextField vehicleEditVinTextField;
    private javax.swing.JLabel vehicleEditYearLabel;
    private javax.swing.JTextField vehicleEditYearTextField;
    private javax.swing.JLabel vehicleLabel;
    private javax.swing.JScrollPane vehicleScrollPane;
    private javax.swing.JPanel vehicleViewActionsPanel;
    private javax.swing.JButton vehicleViewAddButton;
    private javax.swing.JPanel vehicleViewButtonsPanel;
    private javax.swing.JLabel vehicleViewColourLabel;
    private javax.swing.JTextField vehicleViewColourTextField;
    private javax.swing.JComboBox vehicleViewComboBox;
    private javax.swing.JButton vehicleViewDeleteButton;
    private javax.swing.JButton vehicleViewDisableButton;
    private javax.swing.JButton vehicleViewEditButton;
    private javax.swing.JButton vehicleViewEnableButton;
    private javax.swing.JLabel vehicleViewFuelLabel;
    private javax.swing.JTextField vehicleViewFuelTextField;
    private javax.swing.JLabel vehicleViewInsuranceCompanyLabel;
    private javax.swing.JTextField vehicleViewInsuranceCompanyTextField;
    private javax.swing.JLabel vehicleViewInsuranceNumberLabel;
    private javax.swing.JTextField vehicleViewInsuranceNumberTextField;
    private javax.swing.JLabel vehicleViewMakeLabel;
    private javax.swing.JTextField vehicleViewMakeTextField;
    private javax.swing.JLabel vehicleViewModelLabel;
    private javax.swing.JTextField vehicleViewModelTextField;
    private javax.swing.JLabel vehicleViewNotesLabel;
    private javax.swing.JScrollPane vehicleViewNotesScrollPane;
    private javax.swing.JTextArea vehicleViewNotesTextArea;
    private javax.swing.JPanel vehicleViewPanel;
    private javax.swing.JLabel vehicleViewRegistrationLabel;
    private javax.swing.JTextField vehicleViewRegistrationTextField;
    private javax.swing.JLabel vehicleViewTypeLabel;
    private javax.swing.JTextField vehicleViewTypeTextField;
    private javax.swing.JLabel vehicleViewVinLabel;
    private javax.swing.JTextField vehicleViewVinTextField;
    private javax.swing.JLabel vehicleViewYearLabel;
    private javax.swing.JTextField vehicleViewYearTextField;
    // End of variables declaration//GEN-END:variables

    @Override
    public HelpCtx getHelpCtx() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return new HelpCtx("es.regueiro.easyrepair.ui.client.editor");
    }

    public Client getClient() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return client;
    }

    public void forceClose() {
        modified = false;
        this.close();
    }

    private void checkPermissions() {
        if (SecurityManager.getDefault().isUserLoggedIn()) {

            clientIdTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
            nameTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
            surnameTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
            nifTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
            notesTextArea.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));


            addressViewAddButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
            addressViewEditButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
            addressViewDeleteButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));

            emailViewAddButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
            emailViewEditButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
            emailViewDeleteButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));

            phoneViewAddButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
            phoneViewEditButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
            phoneViewDeleteButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));


            vehicleViewAddButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.VEHICLE_EDIT));
            vehicleViewEditButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.VEHICLE_EDIT));
            vehicleViewDeleteButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.VEHICLE_EDIT));
            vehicleViewDisableButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.VEHICLE_EDIT));
            vehicleViewEnableButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.VEHICLE_EDIT));

            vehicleEditChooseInsuranceCompanyButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.INSURANCE_COMPANY));
        }
    }

    @Override
    public void componentOpened() {
        invalid = false;
        result = ClientLookup.getDefault().lookupResult(Client.class);
        result.removeLookupListener(this);
        mainTabbedPane.setSelectedIndex(0);

        Collection<? extends Client> clientColId = result.allInstances();
        if (!clientColId.isEmpty()) {
            client = controller.getClientById(clientColId.iterator().next().getId());
            if (client == null) {
                NotifyDescriptor d = new NotifyDescriptor.Message(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_NOT_FOUND"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CLIENT")), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                close();
            } else {
                newClient = false;
            }
        } else {
            client = controller.newClient();
            newClient = true;
        }
        if (client != null) {
            reloadClientButton.setEnabled(!newClient);
            deleteClientButton.setEnabled(!newClient && SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
            enableClientButton.setVisible(false);
            disableClientButton.setEnabled(!newClient && SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
            controller.setClient(client);
            fillData();
            StatusDisplayer.getDefault().setStatusText("");
        }

    }

    @Override
    public void componentClosed() {
        manager.discardAllEdits();
        insuranceCompanyModel.clear();
    }

    @Override
    public void componentActivated() {
        checkPermissions();
    }

    @Override
    public boolean canClose() {
        if (modified) {
            NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CHANGES_WILL_BE_LOST"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CLIENT")),
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CLOSE"), NotifyDescriptor.YES_NO_OPTION);

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

    public void viewVehicle(Vehicle vehicle) {
        if (client != null && vehicle != null && vehicle.getOwner().equals(client)) {
            mainTabbedPane.setSelectedIndex(4);
            for (int i = 0; i < vehicleViewComboBox.getItemCount(); i++) {
                if (((VehicleBox) vehicleViewComboBox.getItemAt(i)).vehicle.equals(vehicle)) {
                    vehicleViewComboBox.setSelectedIndex(i);
                }
            }
//            vehicleViewEditButton.doClick();
        }
    }

    public void deleteVehicle(Vehicle vehicle) {
        if (client != null && vehicle != null && vehicle.getOwner().equals(client)) {
            mainTabbedPane.setSelectedIndex(4);
            for (int i = 0; i < vehicleViewComboBox.getItemCount(); i++) {
                if (((VehicleBox) vehicleViewComboBox.getItemAt(i)).vehicle.equals(vehicle)) {
                    vehicleViewComboBox.setSelectedIndex(i);
                }
            }
            vehicleViewDeleteButton.doClick();
        }
    }

    private void setSelectedInsuranceCompany() {
        if (selectedInsuranceCompany != null) {
            insuranceCompany = selectedInsuranceCompany;
            vehicleEditInsuranceCompanyTextField.setText(insuranceCompany.getName());
            vehicleEditRemoveInsuranceCompanyButton.setEnabled(true);
            modify();
        }
    }

    private void doSearchInsuranceCompany() {
        final SwingWorker<List<InsuranceCompany>, Void> worker = new SwingWorker<List<InsuranceCompany>, Void>() {
            private int column;
            ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SEARCHING"));

            @Override
            protected void done() {
                try {
                    List<InsuranceCompany> list = get();
                    insuranceCompanyModel.fill(list);

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
                            list = insComController.searchByName(toSearch);
                            column = 1;
                            break;
                        case 1:
                            list = insComController.searchByNif(toSearch);
                            column = 2;
                            break;
                        case 2:
                            list = insComController.searchByWeb(toSearch);
                            column = 3;
                            break;
                        case 3:
                            list = insComController.searchByEmailLabel(toSearch);
                            column = 1;
                            break;
                        case 4:
                            list = insComController.searchByEmailAddress(toSearch);
                            column = 1;
                            break;
                        case 5:
                            list = insComController.searchByPhoneLabel(toSearch);
                            column = 1;
                            break;
                        case 6:
                            list = insComController.searchByPhoneNumber(toSearch);
                            column = 1;
                            break;
                        case 7:
                            list = insComController.searchByAddressLabel(toSearch);
                            column = 1;
                            break;
                        case 8:
                            list = insComController.searchByAddressStreet(toSearch);
                            column = 1;
                            break;
                        case 9:
                            list = insComController.searchByAddressCity(toSearch);
                            column = 1;
                            break;
                        case 10:
                            list = insComController.searchByAddressPostalCode(toSearch);
                            column = 1;
                            break;
                        case 11:
                            list = insComController.searchByAddressProvince(toSearch);
                            column = 1;
                            break;
                        case 12:
                            list = insComController.searchByAddressCountry(toSearch);
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

    private void doListAllInsuranceCompanies() {
        final ProgressHandle p = ProgressHandleFactory.createHandle(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("LISTING_ALL_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCECOMPANIES")));

        SwingWorker<List<InsuranceCompany>, Void> worker = new SwingWorker<List<InsuranceCompany>, Void>() {
            @Override
            protected void done() {
                try {
                    List<InsuranceCompany> list = get();
                    insuranceCompanyModel.fill(list);


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
                    list = insComController.listAll();
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
        worker.execute();
    }

    private void fillData() {
        if (!invalid) {
            clearGeneral();
            clearNotes();

            if (client != null && client.getId() != null) {
                idTextField.setText(client.getId().toString());
                if (client.getClientId() != null) {
                    clientIdTextField.setText(client.getClientId());
                }
                if (client.getNif() != null) {
                    nifTextField.setText(client.getNif().getNumber());
                }
                if (client.getName() != null) {
                    nameTextField.setText(client.getName());
                }
                if (client.getSurname() != null) {
                    surnameTextField.setText(client.getSurname());
                }
                if (client.getNotes() != null) {
                    notesTextArea.setText(client.getNotes());
                }
                if (client.getEnabled()) {
                    enableClientButton.setVisible(false);
                    disableClientButton.setVisible(true);
                } else {
                    enableClientButton.setVisible(true);
                    disableClientButton.setVisible(false);
                }
            }
            fillAddresses();
            fillPhones();
            fillEmails();
            fillVehicles();

            manager.discardAllEdits();
            modified = invalid;
            saveClientButton.setEnabled(modified);
            deleteClientButton.setEnabled(!newClient && SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
            disableClientButton.setEnabled(!newClient && SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
            reloadClientButton.setEnabled(modified && !newClient);
        }
    }

    private void fillAddresses() {
        clearAddresses();
        if (client.getAddress() != null && !client.getAddress().isEmpty()) {
            for (Address a : client.getAddress()) {
                AddressBox address = new AddressBox(a.getLabel(), a);
                addressViewComboBox.addItem(address);
                if (lastAddress != null && a.equals(lastAddress)) {
                    addressViewComboBox.setSelectedItem(address);
                }
            }
            addressViewComboBox.setEnabled(true);
        }
    }

    private void fillPhones() {
        clearPhones();
        if (client.getPhone() != null && !client.getPhone().isEmpty()) {
            for (Phone p : client.getPhone()) {
                PhoneBox phone = new PhoneBox(p.getLabel(), p);
                phoneViewComboBox.addItem(phone);
                if (lastPhone != null && p.equals(lastPhone)) {
                    phoneViewComboBox.setSelectedItem(phone);
                }
            }
            phoneViewComboBox.setEnabled(true);
        }
    }

    private void fillEmails() {
        clearEmails();
        if (client.getEmail() != null && !client.getEmail().isEmpty()) {
            for (Email e : client.getEmail()) {
                EmailBox email = new EmailBox(e.getLabel(), e);
                emailViewComboBox.addItem(email);
                if (lastEmail != null && e.equals(lastEmail)) {
                    emailViewComboBox.setSelectedItem(email);
                }
            }
            emailViewComboBox.setEnabled(true);
        }
    }

    private void fillVehicles() {
        clearVehicles();
        if (client.getVehicles() != null && !client.getVehicles().isEmpty()) {
            for (Vehicle v : client.getVehicles()) {
                VehicleBox veh = new VehicleBox(v);
                vehicleViewComboBox.addItem(veh);
                if (lastVehicle != null && v.equals(lastVehicle)) {
                    vehicleViewComboBox.setSelectedItem(veh);
                }
            }
            vehicleViewComboBox.setEnabled(true);
        }
    }

    private void clearGeneral() {
        clientIdTextField.setText("");
        idTextField.setText("");
        nifTextField.setText("");
        nameTextField.setText("");
        surnameTextField.setText("");
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

    private void clearVehicles() {
        vehicleViewComboBox.setEnabled(false);
        vehicleViewComboBox.removeAllItems();
        vehicleViewEditButton.setEnabled(false);
        vehicleViewDeleteButton.setEnabled(false);
        vehicleViewEnableButton.setEnabled(false);
        vehicleViewDisableButton.setEnabled(false);
        vehicleViewEnableButton.setVisible(true);
        vehicleViewDisableButton.setVisible(false);
        vehicleViewColourTextField.setText("");
        vehicleViewFuelTextField.setText("");
        vehicleViewInsuranceCompanyTextField.setText("");
        vehicleViewInsuranceNumberTextField.setText("");
        vehicleViewMakeTextField.setText("");
        vehicleViewModelTextField.setText("");
        vehicleViewRegistrationTextField.setText("");
        vehicleViewTypeTextField.setText("");
        vehicleViewVinTextField.setText("");
        vehicleViewYearTextField.setText("");
        vehicleViewNotesTextArea.setText("");
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

    private void setupVehicleViewComboBox() {
        vehicleViewComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    VehicleBox vehicle = (VehicleBox) e.getItem();

                    if (vehicle != null && vehicle.vehicle != null) {
                        selectedVehicle = vehicle.vehicle;
                        vehicleViewEditButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.VEHICLE_EDIT));
                        vehicleViewDeleteButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.VEHICLE_EDIT));
                        vehicleViewEnableButton.setVisible(!selectedVehicle.getEnabled());
                        vehicleViewEnableButton.setEnabled(!selectedVehicle.getEnabled() && SecurityManager.getDefault().userHasPrivilege(Privilege.VEHICLE_EDIT));
                        vehicleViewDisableButton.setVisible(selectedVehicle.getEnabled());
                        vehicleViewDisableButton.setEnabled(selectedVehicle.getEnabled() && SecurityManager.getDefault().userHasPrivilege(Privilege.VEHICLE_EDIT));


                        vehicleViewColourTextField.setText(selectedVehicle.getColour());
                        vehicleViewFuelTextField.setText(selectedVehicle.getFuel());
                        vehicleViewInsuranceNumberTextField.setText(selectedVehicle.getInsuranceNumber());
                        vehicleViewMakeTextField.setText(selectedVehicle.getMake());
                        vehicleViewModelTextField.setText(selectedVehicle.getModel());
                        vehicleViewRegistrationTextField.setText(selectedVehicle.getRegistration());
                        vehicleViewTypeTextField.setText(selectedVehicle.getType());
                        vehicleViewVinTextField.setText(selectedVehicle.getVin());
                        vehicleViewYearTextField.setText(selectedVehicle.getYear());
                        vehicleViewNotesTextArea.setText(selectedVehicle.getNotes());
                        if (selectedVehicle.getInsuranceCompany() != null) {
                            vehicleViewInsuranceCompanyTextField.setText(selectedVehicle.getInsuranceCompany().getName());
                        }

                    }
                }
            }
        });
    }

    private void setupEmailViewComboBox() {
        emailViewComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    EmailBox email = (EmailBox) e.getItem();

                    if (email != null && email.email != null) {
                        emailViewEditButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
                        emailViewDeleteButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));

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
                        addressViewEditButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
                        addressViewDeleteButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
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
                        phoneViewEditButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));
                        phoneViewDeleteButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.CLIENT_EDIT));

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

    private boolean updateClient() {
        if (!StringUtils.isBlank(nameTextField.getText())) {
            client.setName(nameTextField.getText());

            if (!StringUtils.isBlank(clientIdTextField.getText())) {
                try {
                    client.setClientId(clientIdTextField.getText());
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    invalid = true;
                    return false;
                }
            } else {
                client.setClientId(null);
            }
            if (!StringUtils.isBlank(nifTextField.getText())) {
                try {
                    client.setNif(new NIF(nifTextField.getText()));
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    invalid = true;
                    return false;
                }
            } else {
                client.setNif(null);
            }
            if (!StringUtils.isBlank(nameTextField.getText())) {
                client.setName(nameTextField.getText());
            } else {
                client.setName(null);
            }
            if (!StringUtils.isBlank(surnameTextField.getText())) {
                client.setSurname(surnameTextField.getText());
            } else {
                client.setSurname(null);
            }
            if (!StringUtils.isBlank(notesTextArea.getText())) {
                client.setNotes(notesTextArea.getText());
            } else {
                client.setNotes(null);
            }
            invalid = false;
            return true;

        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_NAME_NOT_BLANK"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CLIENT")), NotifyDescriptor.WARNING_MESSAGE);
            Object retval = DialogDisplayer.getDefault().notify(d);
            invalid = true;
            return false;
        }
    }

    private void saveClient() {
        if (updateClient()) {

            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SAVING"));

                @Override
                protected void done() {
                    fillData();
                    p.finish();
                }

                @Override
                protected Void doInBackground() {
                    p.start();
                    try {
                        controller.setClient(client);
                        controller.saveClient();
                        newClient = false;
                        invalid = false;
                    } catch (Exception e) {
                        if (e.getMessage().contains("was updated or deleted by another user")) {
                            NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("OVERWRITE_DIALOG"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CLIENT")),
                                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_MODIFIED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CLIENT")));
                            String options[] = new String[2];
                            options[0] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("OVERWRITE");
                            options[1] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("RELOAD");
                            d.setOptions(options);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                            if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("OVERWRITE"))) {
                                controller.overwriteClient();
                                client = controller.getClient();
                                invalid = false;
                            } else if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("RELOAD"))) {
                                reloadClientButton.doClick();
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
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EMAIL_VALID_ADDRESS"));
                DialogDisplayer.getDefault().notify(d);
                return false;

            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_VALID_LABEL"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EMAIL")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }

        if (client.containsEmailLabel(email.getLabel())) {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("LABEL_INUSE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EMAIL")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }
        if (!StringUtils.isBlank(emailEditNotesTextArea.getText())) {
            email.setNotes(emailEditNotesTextArea.getText());
        }

        client.addEmail(email);
        lastEmail = email;
        fillEmails();
        return true;
    }

    private boolean updateEmail() {
        Email email = ((EmailBox) emailViewComboBox.getSelectedItem()).email;
        String oldLabel = email.getLabel();

        if (!StringUtils.isBlank(emailEditLabelTextField.getText())) {
            if (!oldLabel.equals(emailEditLabelTextField.getText())) {
                if (client.containsEmailLabel(emailEditLabelTextField.getText())) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(
                            java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("LABEL_INUSE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EMAIL")));
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
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EMAIL_VALID_ADDRESS"));
                DialogDisplayer.getDefault().notify(d);
                return false;

            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EMAIL_VALID_ADDRESS"));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }


        if (!StringUtils.isBlank(emailEditNotesTextArea.getText())) {
            email.setNotes(emailEditNotesTextArea.getText());
        }
        //client = controller.getClient();
        lastEmail = email;
        fillEmails();

        return true;
    }

    private boolean addVehicle() {
        Vehicle vehicle = new Vehicle();



        if (!StringUtils.isBlank(vehicleEditRegistrationTextField.getText())) {
            vehicle.setRegistration(vehicleEditRegistrationTextField.getText());
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("REGISTRATION_NOT_BLANK"));
            DialogDisplayer.getDefault().notify(d);
            return false;
        }


        if (!StringUtils.isBlank(vehicleEditVinTextField.getText())) {
            try {
                vehicle.setVin(vehicleEditVinTextField.getText());
            } catch (IllegalArgumentException e) {
                NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                invalid = true;
                return false;
            }
        } else {
            vehicle.setVin(null);
        }
        if (!StringUtils.isBlank(vehicleEditMakeTextField.getText())) {
            vehicle.setMake(vehicleEditMakeTextField.getText());
        } else {
            vehicle.setMake(null);
        }
        if (!StringUtils.isBlank(vehicleEditModelTextField.getText())) {
            vehicle.setModel(vehicleEditModelTextField.getText());
        } else {
            vehicle.setModel(null);
        }
        if (!StringUtils.isBlank(vehicleEditYearTextField.getText())) {
            try {
                vehicle.setYear(vehicleEditYearTextField.getText());
            } catch (IllegalArgumentException e) {
                NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                invalid = true;
                return false;
            }
        } else {
            vehicle.setYear(null);
        }
        if (!StringUtils.isBlank(vehicleEditColourTextField.getText())) {
            vehicle.setColour(vehicleEditColourTextField.getText());
        } else {
            vehicle.setColour(null);
        }
        if (!StringUtils.isBlank(vehicleEditTypeTextField.getText())) {
            vehicle.setType(vehicleEditTypeTextField.getText());
        } else {
            vehicle.setType(null);
        }
        if (!StringUtils.isBlank(vehicleEditFuelTextField.getText())) {
            vehicle.setFuel(vehicleEditFuelTextField.getText());
        } else {
            vehicle.setFuel(null);
        }
        if (!StringUtils.isBlank(vehicleEditInsuranceNumberTextField.getText())) {
            vehicle.setInsuranceNumber(vehicleEditInsuranceNumberTextField.getText());
        } else {
            vehicle.setInsuranceNumber(null);
        }
        if (!StringUtils.isBlank(vehicleEditNotesTextArea.getText())) {
            vehicle.setNotes(vehicleEditNotesTextArea.getText());
        } else {
            vehicle.setNotes(null);
        }
        if (insuranceCompany != null) {
            vehicle.setInsuranceCompany(insuranceCompany);
            insuranceCompany = null;
        } else {
            vehicle.setInsuranceCompany(null);
        }


        client.addVehicle(vehicle);
        lastVehicle = vehicle;
        fillVehicles();
        return true;
    }

    private boolean updateVehicle() {
        Vehicle vehicle = selectedVehicle;

        if (!StringUtils.isBlank(vehicleEditRegistrationTextField.getText())) {
            vehicle.setRegistration(vehicleEditRegistrationTextField.getText());
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EMAIL_VALID_ADDRESS"));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }
        if (!StringUtils.isBlank(vehicleEditVinTextField.getText())) {
            try {
                vehicle.setVin(vehicleEditVinTextField.getText());
            } catch (IllegalArgumentException e) {
                NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                return false;
            }
        } else {
            vehicle.setVin(null);
        }
        if (!StringUtils.isBlank(vehicleEditMakeTextField.getText())) {
            vehicle.setMake(vehicleEditMakeTextField.getText());
        } else {
            vehicle.setMake(null);
        }
        if (!StringUtils.isBlank(vehicleEditModelTextField.getText())) {
            vehicle.setModel(vehicleEditModelTextField.getText());
        } else {
            vehicle.setModel(null);
        }
        if (!StringUtils.isBlank(vehicleEditYearTextField.getText())) {
            try {
                vehicle.setYear(vehicleEditYearTextField.getText());
            } catch (IllegalArgumentException e) {
                NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                return false;
            }
        } else {
            vehicle.setYear(null);
        }
        if (!StringUtils.isBlank(vehicleEditColourTextField.getText())) {
            vehicle.setColour(vehicleEditColourTextField.getText());
        } else {
            vehicle.setColour(null);
        }
        if (!StringUtils.isBlank(vehicleEditTypeTextField.getText())) {
            vehicle.setType(vehicleEditTypeTextField.getText());
        } else {
            vehicle.setType(null);
        }
        if (!StringUtils.isBlank(vehicleEditFuelTextField.getText())) {
            vehicle.setFuel(vehicleEditFuelTextField.getText());
        } else {
            vehicle.setFuel(null);
        }
        if (!StringUtils.isBlank(vehicleEditInsuranceNumberTextField.getText())) {
            vehicle.setInsuranceNumber(vehicleEditInsuranceNumberTextField.getText());
        } else {
            vehicle.setInsuranceNumber(null);
        }
        if (!StringUtils.isBlank(vehicleEditNotesTextArea.getText())) {
            vehicle.setNotes(vehicleEditNotesTextArea.getText());
        } else {
            vehicle.setNotes(null);
        }
        if (insuranceCompany != null) {
            vehicle.setInsuranceCompany(insuranceCompany);
            insuranceCompany = null;
        } else {
            vehicle.setInsuranceCompany(null);
        }

        //client = controller.getClient();
        lastVehicle = vehicle;
        fillVehicles();

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
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("PHONE_VALID_NUMBER"));
                DialogDisplayer.getDefault().notify(d);
                return false;

            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_VALID_LABEL"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("PHONE")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }

        if (client.containsPhoneLabel(phone.getLabel())) {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("LABEL_INUSE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("PHONE")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }
        if (!StringUtils.isBlank(phoneEditNotesTextArea.getText())) {
            phone.setNotes(phoneEditNotesTextArea.getText());
        }

        client.addPhone(phone);
        //client = controller.getClient();
        lastPhone = phone;
        fillPhones();
        return true;
    }

    private boolean updatePhone() {
        Phone phone = ((PhoneBox) phoneViewComboBox.getSelectedItem()).phone;
        String oldLabel = phone.getLabel();

        if (!StringUtils.isBlank(phoneEditLabelTextField.getText())) {
            if (!oldLabel.equals(phoneEditLabelTextField.getText())) {
                if (client.containsPhoneLabel(phoneEditLabelTextField.getText())) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(
                            java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("LABEL_INUSE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("PHONE")));
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
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("PHONE_VALID_NUMBER"));
                DialogDisplayer.getDefault().notify(d);
                return false;

            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_VALID_LABEL"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("PHONE")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }


        if (!StringUtils.isBlank(phoneEditNotesTextArea.getText())) {
            phone.setNotes(phoneEditNotesTextArea.getText());
        }
        //client = controller.getClient();
        lastPhone = phone;
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
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_BLANK"));
                DialogDisplayer.getDefault().notify(d);
                return false;
            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_VALID_LABEL"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }

        if (client.containsAddressLabel(address.getLabel())) {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("LABEL_INUSE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }
        if (!StringUtils.isBlank(addressEditNotesTextArea.getText())) {
            address.setNotes(addressEditNotesTextArea.getText());
        }

        client.addAddress(address);
        //client = controller.getClient();
        lastAddress = address;
        fillAddresses();
        return true;
    }

    private boolean updateAddress() {
        Address address = ((AddressBox) addressViewComboBox.getSelectedItem()).address;
        String oldLabel = address.getLabel();

        if (!StringUtils.isBlank(addressEditLabelTextField.getText())) {
            if (!oldLabel.equals(addressEditLabelTextField.getText())) {
                if (client.containsAddressLabel(addressEditLabelTextField.getText())) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(
                            java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("LABEL_INUSE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS")));
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
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS_BLANK"));
                DialogDisplayer.getDefault().notify(d);
                return false;
            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_VALID_LABEL"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }


        if (!StringUtils.isBlank(addressEditNotesTextArea.getText())) {
            address.setNotes(addressEditNotesTextArea.getText());
        }
        //client = controller.getClient();
        lastAddress = address;
        fillAddresses();

        return true;
    }

    private void setupUndo() {
        clientIdTextField.getDocument().addUndoableEditListener(manager);
        notesTextArea.getDocument().addUndoableEditListener(manager);
        nifTextField.getDocument().addUndoableEditListener(manager);
        surnameTextField.getDocument().addUndoableEditListener(manager);
        nameTextField.getDocument().addUndoableEditListener(manager);



        //TODO add the rest of the textfields
    }

    private void removeUndo() {
        clientIdTextField.getDocument().removeUndoableEditListener(manager);
        notesTextArea.getDocument().removeUndoableEditListener(manager);
        nifTextField.getDocument().removeUndoableEditListener(manager);
        surnameTextField.getDocument().removeUndoableEditListener(manager);
        nameTextField.getDocument().removeUndoableEditListener(manager);

    }

    private void modify() {
        modified = true;
        saveClientButton.setEnabled(modified);
        reloadClientButton.setEnabled(modified && !newClient);

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

    @Override
    public void tableChanged(TableModelEvent e) {
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
//            if (vehicleTable.getSelectedRow() > -1) {
//                selectedVehicle = vehicleModel.getRow(vehicleTable.convertRowIndexToModel(vehicleTable.getSelectedRow()));
//                deleteVehicleButton.setEnabled(true);
//                disableVehicleButton.setVisible(selectedVehicle.getEnabled());
//                enableVehicleButton.setVisible(!selectedVehicle.getEnabled());
//                enableVehicleButton.setEnabled(true);
//            } else {
//                deleteVehicleButton.setEnabled(false);
//                disableVehicleButton.setVisible(false);
//                enableVehicleButton.setVisible(true);
//                enableVehicleButton.setEnabled(false);
//            }
            if (insuranceCompanyTable.getSelectedRow() > -1) {
                selectedInsuranceCompany = insuranceCompanyModel.getRow(insuranceCompanyTable.convertRowIndexToModel(insuranceCompanyTable.getSelectedRow()));
                browseInsuranceCompaniesDialog.setValid(true);
            } else {
                selectedInsuranceCompany = null;
                browseInsuranceCompaniesDialog.setValid(false);
            }
        }

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

    private class VehicleBox {

        public Vehicle vehicle;

        public VehicleBox(Vehicle vehicle) {
            this.vehicle = vehicle;
        }

        @Override
        public String toString() {
            String ret = this.vehicle.getRegistration();
            String more = "";
            if (this.vehicle.getMake() != null) {
                more += " " + this.vehicle.getMake();
            }
            if (this.vehicle.getModel() != null) {
                more += " " + this.vehicle.getModel();
            }
            if (this.vehicle.getYear() != null) {
                more += " (" + this.vehicle.getYear() + ")";
            }
            if (!more.equals("")) {
                ret += " -" + more;
            }

            return ret;
        }
    }
}
