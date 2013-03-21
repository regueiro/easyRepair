/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.ui.client;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import es.regueiro.easyrepair.api.client.controller.InsuranceCompanyController;
import es.regueiro.easyrepair.model.client.InsuranceCompany;
import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.NIF;
import es.regueiro.easyrepair.model.shared.Phone;
import es.regueiro.easyrepair.model.user.Privilege;
import es.regueiro.easyrepair.login.SecurityManager;
import es.regueiro.easyrepair.reports.ReportPrinter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import javax.swing.ActionMap;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.client//InsuranceCompanyEditor//EN",
autostore = false)
@TopComponent.Description(iconBase = "es/regueiro/easyrepair/ui/client/icons/insurance_edit.png", preferredID = "InsuranceCompanyEditorTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 5, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.client.InsuranceCompanyEditorTopComponent")
@TopComponent.OpenActionRegistration(displayName = "#CTL_InsuranceCompanyEditorAction",
preferredID = "InsuranceCompanyEditorTopComponent")
@Messages({
    "CTL_InsuranceCompanyEditorAction=Editor de aseguradoras",
    "CTL_InsuranceCompanyEditorTopComponent=Editor de aseguradoras",
    "HINT_InsuranceCompanyEditorTopComponent=Esta es una ventana del editor de aseguradoras"
})
public final class InsuranceCompanyEditorTopComponent extends TopComponent implements LookupListener, DocumentListener {

    private InsuranceCompany insuranceCompany = null;
    private InsuranceCompany oldInsuranceCompany = null;
    private Lookup.Result<InsuranceCompany> result;
    private InstanceContent content = new InstanceContent();
    private UndoRedo.Manager manager = new UndoRedo.Manager();
    private InsuranceCompanyController controller = Lookup.getDefault().lookup(InsuranceCompanyController.class);
    private DialogDescriptor editEmaildialog;
    private DialogDescriptor editAddressdialog;
    private DialogDescriptor editPhonedialog;
    private boolean modified = false;
    private boolean newInsuranceCompany = true;
    private boolean invalid = false;
    private PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public InsuranceCompanyEditorTopComponent() {
        initComponents();
        setName(Bundle.CTL_InsuranceCompanyEditorTopComponent());
        setToolTipText(Bundle.HINT_InsuranceCompanyEditorTopComponent());
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



        mainTabbedPane.setIconAt(1, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/book.png")));
        mainTabbedPane.setIconAt(3, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/email.png")));
        mainTabbedPane.setIconAt(2, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/phone.png")));
        mainTabbedPane.setIconAt(0, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/insurance.png")));
//        emailEditAddressTextField.getDocument().addUndoableEditListener(manager);


        nameTextField.setDocument(new MaxLengthTextDocument(100));
        nifTextField.setDocument(new MaxLengthTextDocument(9));
        webTextField.setDocument(new MaxLengthTextDocument(100));


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
        notesTextArea.getDocument().addDocumentListener(this);
        nifTextField.getDocument().addDocumentListener(this);
        webTextField.getDocument().addDocumentListener(this);

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
        mainGeneralPanel = new javax.swing.JPanel();
        generalPanel = new javax.swing.JPanel();
        generalLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        nifLabel = new javax.swing.JLabel();
        nifTextField = new javax.swing.JTextField();
        webLabel = new javax.swing.JLabel();
        webTextField = new javax.swing.JTextField();
        idLabel = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();
        notesPanel = new javax.swing.JPanel();
        notesLabel = new javax.swing.JLabel();
        notesScrollPane = new javax.swing.JScrollPane();
        notesTextArea = new javax.swing.JTextArea();
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
        saveInsuranceCompanyButton = new javax.swing.JButton();
        reloadInsuranceCompanyButton = new javax.swing.JButton();
        enableInsuranceCompanyButton = new javax.swing.JButton();
        disableInsuranceCompanyButton = new javax.swing.JButton();
        deleteInsuranceCompanyButton = new javax.swing.JButton();
        printInsuranceCompanyButton = new javax.swing.JButton();

        addressEditPanel.setPreferredSize(new java.awt.Dimension(400, 400));
        addressEditPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(addressEditLabelLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressEditLabelLabel.text")); // NOI18N
        addressEditLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditStreetLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressEditStreetLabel.text")); // NOI18N
        addressEditStreetLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditStreetLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditCityLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressEditCityLabel.text")); // NOI18N
        addressEditCityLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditCityLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditProvinceLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressEditProvinceLabel.text")); // NOI18N
        addressEditProvinceLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditProvinceLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditPostalCodeLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressEditPostalCodeLabel.text")); // NOI18N
        addressEditPostalCodeLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditPostalCodeLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditCountryLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressEditCountryLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(addressEditNotesLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressEditNotesLabel.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(addressEditLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressEditLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        addressEditPanel.add(addressEditLabel, gridBagConstraints);

        phoneEditPanel.setPreferredSize(new java.awt.Dimension(400, 400));
        phoneEditPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(phoneEditLabelLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.phoneEditLabelLabel.text")); // NOI18N
        phoneEditLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneEditPanel.add(phoneEditLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(phoneEditNumberLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.phoneEditNumberLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(phoneEditNotesLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.phoneEditNotesLabel.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(phoneEditLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.phoneEditLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        phoneEditPanel.add(phoneEditLabel, gridBagConstraints);

        emailEditPanel.setPreferredSize(new java.awt.Dimension(400, 400));
        emailEditPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(emailEditLabelLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.emailEditLabelLabel.text")); // NOI18N
        emailEditLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emailEditPanel.add(emailEditLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(emailEditAddressLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.emailEditAddressLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(emailEditNotesLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.emailEditNotesLabel.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(emailEditLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.emailEditLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        emailEditPanel.add(emailEditLabel, gridBagConstraints);

        setLayout(new java.awt.GridBagLayout());

        mainTabbedPane.setDoubleBuffered(true);

        mainGeneralPanel.setLayout(new java.awt.GridBagLayout());

        generalPanel.setLayout(new java.awt.GridBagLayout());

        generalLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        generalLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(generalLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.generalLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        generalPanel.add(generalLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(nameLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.nameLabel.text")); // NOI18N
        nameLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(nameLabel, gridBagConstraints);

        nameTextField.setDoubleBuffered(true);
        nameTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        nameTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(nameTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(nifLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.nifLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(webLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.webLabel.text")); // NOI18N
        webLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(webLabel, gridBagConstraints);

        webTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        webTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(webTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(idLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.idLabel.text")); // NOI18N
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        mainGeneralPanel.add(generalPanel, gridBagConstraints);

        notesPanel.setLayout(new java.awt.GridBagLayout());

        notesLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        notesLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(notesLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.notesLabel.text")); // NOI18N
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

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.mainGeneralPanel.TabConstraints.tabTitle"), mainGeneralPanel); // NOI18N

        addressViewPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(addressViewLabelLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressViewLabelLabel.text")); // NOI18N
        addressViewLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewStreetLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressViewStreetLabel.text")); // NOI18N
        addressViewStreetLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewStreetLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewCityLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressViewCityLabel.text")); // NOI18N
        addressViewCityLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewCityLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewProvinceLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressViewProvinceLabel.text")); // NOI18N
        addressViewProvinceLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewProvinceLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewPostalCodeLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressViewPostalCodeLabel.text")); // NOI18N
        addressViewPostalCodeLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewPostalCodeLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewCountryLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressViewCountryLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(addressViewNotesLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressViewNotesLabel.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(addressViewLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressViewLabel.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(addressViewAddButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressViewAddButton.text")); // NOI18N
        addressViewAddButton.setMinimumSize(new java.awt.Dimension(32, 32));
        addressViewAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressViewAddButtonActionPerformed(evt);
            }
        });
        addressViewButtonsPanel.add(addressViewAddButton);

        addressViewEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/pencil.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addressViewEditButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressViewEditButton.text")); // NOI18N
        addressViewEditButton.setEnabled(false);
        addressViewEditButton.setMinimumSize(new java.awt.Dimension(32, 32));
        addressViewEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressViewEditButtonActionPerformed(evt);
            }
        });
        addressViewButtonsPanel.add(addressViewEditButton);

        addressViewDeleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addressViewDeleteButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.addressViewDeleteButton.text")); // NOI18N
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

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle"); // NOI18N
        mainTabbedPane.addTab(bundle.getString("ADDRESS"), addressViewPanel); // NOI18N

        phoneViewPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(phoneViewLabelLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.phoneViewLabelLabel.text")); // NOI18N
        phoneViewLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneViewPanel.add(phoneViewLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(phoneViewNumberLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.phoneViewNumberLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(phoneViewNotesLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.phoneViewNotesLabel.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(phoneViewLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.phoneViewLabel.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(phoneViewAddButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.phoneViewAddButton.text")); // NOI18N
        phoneViewAddButton.setMinimumSize(new java.awt.Dimension(32, 32));
        phoneViewAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneViewAddButtonActionPerformed(evt);
            }
        });
        phoneViewButtonsPanel.add(phoneViewAddButton);

        phoneViewEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/pencil.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(phoneViewEditButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.phoneViewEditButton.text")); // NOI18N
        phoneViewEditButton.setEnabled(false);
        phoneViewEditButton.setMinimumSize(new java.awt.Dimension(32, 32));
        phoneViewEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneViewEditButtonActionPerformed(evt);
            }
        });
        phoneViewButtonsPanel.add(phoneViewEditButton);

        phoneViewDeleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(phoneViewDeleteButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.phoneViewDeleteButton.text")); // NOI18N
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

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.phoneViewPanel.TabConstraints.tabTitle"), phoneViewPanel); // NOI18N

        emaiViewlPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(emailViewLabelLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.emailViewLabelLabel.text")); // NOI18N
        emailViewLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emaiViewlPanel.add(emailViewLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(emailViewAddressLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.emailViewAddressLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(emailViewNotesLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.emailViewNotesLabel.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(emailLabel, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.emailLabel.text")); // NOI18N
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

        emailViewAddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(emailViewAddButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.emailViewAddButton.text")); // NOI18N
        emailViewAddButton.setMinimumSize(new java.awt.Dimension(32, 32));
        emailViewAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailViewAddButtonActionPerformed(evt);
            }
        });
        emailViewButtonsPanel.add(emailViewAddButton);

        emailViewEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/pencil.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(emailViewEditButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.emailViewEditButton.text")); // NOI18N
        emailViewEditButton.setEnabled(false);
        emailViewEditButton.setMinimumSize(new java.awt.Dimension(32, 32));
        emailViewEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailViewEditButtonActionPerformed(evt);
            }
        });
        emailViewButtonsPanel.add(emailViewEditButton);

        emailViewDeleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(emailViewDeleteButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.emailViewDeleteButton.text")); // NOI18N
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

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.emaiViewlPanel.TabConstraints.tabTitle"), emaiViewlPanel); // NOI18N

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

        saveInsuranceCompanyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/disk.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(saveInsuranceCompanyButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.saveInsuranceCompanyButton.text")); // NOI18N
        saveInsuranceCompanyButton.setFocusable(false);
        saveInsuranceCompanyButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        saveInsuranceCompanyButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        saveInsuranceCompanyButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        saveInsuranceCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveInsuranceCompanyButtonActionPerformed(evt);
            }
        });
        topToolBar.add(saveInsuranceCompanyButton);

        reloadInsuranceCompanyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/arrow_refresh.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(reloadInsuranceCompanyButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.reloadInsuranceCompanyButton.text")); // NOI18N
        reloadInsuranceCompanyButton.setFocusable(false);
        reloadInsuranceCompanyButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        reloadInsuranceCompanyButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        reloadInsuranceCompanyButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        reloadInsuranceCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadInsuranceCompanyButtonActionPerformed(evt);
            }
        });
        topToolBar.add(reloadInsuranceCompanyButton);

        enableInsuranceCompanyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/status_online.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(enableInsuranceCompanyButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.enableInsuranceCompanyButton.text")); // NOI18N
        enableInsuranceCompanyButton.setFocusable(false);
        enableInsuranceCompanyButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        enableInsuranceCompanyButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        enableInsuranceCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableInsuranceCompanyButtonActionPerformed(evt);
            }
        });
        topToolBar.add(enableInsuranceCompanyButton);

        disableInsuranceCompanyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/status_offline.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(disableInsuranceCompanyButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.disableInsuranceCompanyButton.text")); // NOI18N
        disableInsuranceCompanyButton.setFocusable(false);
        disableInsuranceCompanyButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        disableInsuranceCompanyButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        disableInsuranceCompanyButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        disableInsuranceCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disableInsuranceCompanyButtonActionPerformed(evt);
            }
        });
        topToolBar.add(disableInsuranceCompanyButton);

        deleteInsuranceCompanyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteInsuranceCompanyButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.deleteInsuranceCompanyButton.text")); // NOI18N
        deleteInsuranceCompanyButton.setFocusable(false);
        deleteInsuranceCompanyButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deleteInsuranceCompanyButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deleteInsuranceCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteInsuranceCompanyButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deleteInsuranceCompanyButton);

        printInsuranceCompanyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printInsuranceCompanyButton, org.openide.util.NbBundle.getMessage(InsuranceCompanyEditorTopComponent.class, "InsuranceCompanyEditorTopComponent.printInsuranceCompanyButton.text")); // NOI18N
        printInsuranceCompanyButton.setFocusable(false);
        printInsuranceCompanyButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printInsuranceCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printInsuranceCompanyButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printInsuranceCompanyButton);

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
                    insuranceCompany.removeAddress(addressViewLabelTextField.getText());
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
                    insuranceCompany.removePhone(phoneViewLabelTextField.getText());
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
                    insuranceCompany.removeEmail(emailViewLabelTextField.getText());
                    return null;
                }
            };


            worker.execute();
//            controller.removeEmail(emailViewLabelTextField.getText());
//            fillEmails();
        }
    }//GEN-LAST:event_emailViewDeleteButtonActionPerformed

    private void saveInsuranceCompanyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveInsuranceCompanyButtonActionPerformed
        saveInsuranceCompany();
    }//GEN-LAST:event_saveInsuranceCompanyButtonActionPerformed

    private void disableInsuranceCompanyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disableInsuranceCompanyButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected void done() {
//                disableInsuranceCompanyButton.setVisible(false);
//                enableInsuranceCompanyButton.setVisible(true);
                fillData();
                StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("DISABLED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCECOMPANY")));
            }

            @Override
            protected Void doInBackground() {
                if (updateInsuranceCompany()) {
                    insuranceCompany = controller.disableInsuranceCompany();
                }
                return null;
            }
        };

        worker.execute();

    }//GEN-LAST:event_disableInsuranceCompanyButtonActionPerformed

    private void reloadInsuranceCompanyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadInsuranceCompanyButtonActionPerformed

        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("WANT_RELOAD_INSURANCECOMPANY"),
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
                                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCECOMPANY_RELOAD_NOT_FOUND"),
                                    NotifyDescriptor.ERROR_MESSAGE);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                    StatusDisplayer.getDefault().setStatusText(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("RELOADED_INSURANCECOMPANY"));
                }

                @Override
                protected Boolean doInBackground() {
                    insuranceCompany = controller.reloadInsuranceCompany();
                    if (insuranceCompany != null) {
                        controller.setInsuranceCompany(insuranceCompany);
                        return true;
                    } else {
                        return false;
                    }
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_reloadInsuranceCompanyButtonActionPerformed

    private void enableInsuranceCompanyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableInsuranceCompanyButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected void done() {
//                disableInsuranceCompanyButton.setVisible(true);
//                enableInsuranceCompanyButton.setVisible(false);
                fillData();
                StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ENABLED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCECOMPANY")));
            }

            @Override
            protected Void doInBackground() {
                if (updateInsuranceCompany()) {
                    insuranceCompany = controller.enableInsuranceCompany();
                }
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_enableInsuranceCompanyButtonActionPerformed

    private void deleteInsuranceCompanyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteInsuranceCompanyButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCECOMPANY")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {


            InsuranceCompanyLookup.getDefault().clear();

            InsuranceCompanyBrowserTopComponent window = (InsuranceCompanyBrowserTopComponent) WindowManager.getDefault().findTopComponent("InsuranceCompanyBrowserTopComponent");

            window.deleteInsuranceCompany(insuranceCompany);
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                @Override
                protected void done() {
                    forceClose();
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCECOMPANY")));
                }

                @Override
                protected Void doInBackground() {
                    controller.setInsuranceCompany(insuranceCompany);
                    controller.deleteInsuranceCompany();
                    return null;
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_deleteInsuranceCompanyButtonActionPerformed

    private void printInsuranceCompanyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printInsuranceCompanyButtonActionPerformed
        if (updateInsuranceCompany()) {
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
        }
    }//GEN-LAST:event_printInsuranceCompanyButtonActionPerformed
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
    private javax.swing.JButton deleteInsuranceCompanyButton;
    private javax.swing.JButton disableInsuranceCompanyButton;
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
    private javax.swing.JButton enableInsuranceCompanyButton;
    private javax.swing.JLabel generalLabel;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JLabel idLabel;
    private javax.swing.JTextField idTextField;
    private javax.swing.JPanel mainGeneralPanel;
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
    private javax.swing.JButton printInsuranceCompanyButton;
    private javax.swing.JButton reloadInsuranceCompanyButton;
    private javax.swing.JButton saveInsuranceCompanyButton;
    private javax.swing.JToolBar topToolBar;
    private javax.swing.JLabel webLabel;
    private javax.swing.JTextField webTextField;
    // End of variables declaration//GEN-END:variables

    @Override
    public HelpCtx getHelpCtx() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return new HelpCtx("es.regueiro.easyrepair.ui.insuranceCompany.editor");
    }

    public InsuranceCompany getInsuranceCompany() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return insuranceCompany;
    }

    public void forceClose() {
        modified = false;
        this.close();
    }

    @Override
    public void componentOpened() {
        invalid = false;
        result = InsuranceCompanyLookup.getDefault().lookupResult(InsuranceCompany.class);
        result.removeLookupListener(this);
        mainTabbedPane.setSelectedIndex(0);

        Collection<? extends InsuranceCompany> insuranceCompanyColId = result.allInstances();
        if (!insuranceCompanyColId.isEmpty()) {
            insuranceCompany = controller.getInsuranceCompanyById(insuranceCompanyColId.iterator().next().getId());
            if (insuranceCompany == null) {
                NotifyDescriptor d = new NotifyDescriptor.Message(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_NOT_FOUND"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCECOMPANY")), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                close();
            } else {
                newInsuranceCompany = false;
            }
        } else {
            insuranceCompany = controller.newInsuranceCompany();
            newInsuranceCompany = true;
        }
        if (insuranceCompany != null) {
            reloadInsuranceCompanyButton.setEnabled(!newInsuranceCompany);
            deleteInsuranceCompanyButton.setEnabled(!newInsuranceCompany);
            enableInsuranceCompanyButton.setVisible(false);
            disableInsuranceCompanyButton.setEnabled(!newInsuranceCompany);
            controller.setInsuranceCompany(insuranceCompany);
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
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("CHANGES_WILL_BE_LOST"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCECOMPANY")),
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

    private void checkPermissions() {
        if (!SecurityManager.getDefault().isUserLoggedIn() || !SecurityManager.getDefault().userHasPrivilege(Privilege.INSURANCE_COMPANY_EDIT)) {
            this.forceClose();
        }
    }
    
    private void fillData() {
        if (!invalid) {
            clearGeneral();
            clearNotes();

            if (insuranceCompany != null && insuranceCompany.getId() != null) {
                idTextField.setText(insuranceCompany.getId().toString());
                if (insuranceCompany.getName() != null) {
                    nameTextField.setText(insuranceCompany.getName());
                }
                if (insuranceCompany.getNif() != null) {
                    nifTextField.setText(insuranceCompany.getNif().getNumber());
                }
                if (insuranceCompany.getWeb() != null) {
                    webTextField.setText(insuranceCompany.getWeb());
                }
                if (insuranceCompany.getNotes() != null) {
                    notesTextArea.setText(insuranceCompany.getNotes());
                }
                if (insuranceCompany.getEnabled()) {
                    enableInsuranceCompanyButton.setVisible(false);
                    disableInsuranceCompanyButton.setVisible(true);
                } else {
                    enableInsuranceCompanyButton.setVisible(true);
                    disableInsuranceCompanyButton.setVisible(false);
                }
            }
            fillAddresses();
            fillPhones();
            fillEmails();

            manager.discardAllEdits();
            modified = invalid;
            saveInsuranceCompanyButton.setEnabled(modified);
            deleteInsuranceCompanyButton.setEnabled(!newInsuranceCompany);
            disableInsuranceCompanyButton.setEnabled(!newInsuranceCompany);
            reloadInsuranceCompanyButton.setEnabled(modified && !newInsuranceCompany);
        }
    }

    private void fillAddresses() {
        clearAddresses();
        if (insuranceCompany.getAddress() != null && !insuranceCompany.getAddress().isEmpty()) {
            for (Address a : insuranceCompany.getAddress()) {
                AddressBox adddress = new AddressBox(a.getLabel(), a);
                addressViewComboBox.addItem(adddress);
            }
            addressViewComboBox.setEnabled(true);
        }
    }

    private void fillPhones() {
        clearPhones();
        if (insuranceCompany.getPhone() != null && !insuranceCompany.getPhone().isEmpty()) {
            for (Phone p : insuranceCompany.getPhone()) {
                PhoneBox phone = new PhoneBox(p.getLabel(), p);
                phoneViewComboBox.addItem(phone);
            }
            phoneViewComboBox.setEnabled(true);
        }
    }

    private void fillEmails() {
        clearEmails();
        if (insuranceCompany.getEmail() != null && !insuranceCompany.getEmail().isEmpty()) {
            for (Email e : insuranceCompany.getEmail()) {
                EmailBox email = new EmailBox(e.getLabel(), e);
                emailViewComboBox.addItem(email);
            }
            emailViewComboBox.setEnabled(true);
        }
    }

    private void clearGeneral() {
        nameTextField.setText("");
        idTextField.setText("");
        nifTextField.setText("");
        webTextField.setText("");
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

    private boolean updateInsuranceCompany() {
        if (!StringUtils.isBlank(nameTextField.getText())) {
            insuranceCompany.setName(nameTextField.getText());
            if (!StringUtils.isBlank(nifTextField.getText())) {
                try {
                    insuranceCompany.setNif(new NIF(nifTextField.getText()));
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    invalid = true;
                    return false;
                }
            } else {
                insuranceCompany.setNif(null);
            }
            if (!StringUtils.isBlank(notesTextArea.getText())) {
                insuranceCompany.setNotes(notesTextArea.getText());
            } else {
                insuranceCompany.setNotes(null);
            }
            if (!StringUtils.isBlank(webTextField.getText())) {
                try {
                    insuranceCompany.setWeb(webTextField.getText());
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    invalid = true;
                    return false;
                }
            } else {
                insuranceCompany.setWeb(null);
            }
            invalid = false;
            return true;

        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_NAME_NOT_BLANK"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCECOMPANY")), NotifyDescriptor.WARNING_MESSAGE);
            Object retval = DialogDisplayer.getDefault().notify(d);
            invalid = true;
            return false;
        }
    }

    private void saveInsuranceCompany() {
        if (updateInsuranceCompany()) {
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
                        controller.setInsuranceCompany(insuranceCompany);
                        controller.saveInsuranceCompany();
                        newInsuranceCompany = false;
                        invalid = false;
                    } catch (Exception e) {
                        if (e.getMessage().contains("was updated or deleted by another user")) {
                            NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("OVERWRITE_DIALOG"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCECOMPANY")),
                                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_MODIFIED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCECOMPANY")));
                            String options[] = new String[2];
                            options[0] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("OVERWRITE");
                            options[1] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("RELOAD");
                            d.setOptions(options);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                            if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("OVERWRITE"))) {
                                controller.overwriteInsuranceCompany();
                                insuranceCompany = controller.getInsuranceCompany();
                                invalid = false;
                            } else if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("RELOAD"))) {
                                reloadInsuranceCompanyButton.doClick();
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

        if (insuranceCompany.containsEmailLabel(email.getLabel())) {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("LABEL_INUSE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("EMAIL")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }
        if (!StringUtils.isBlank(emailEditNotesTextArea.getText())) {
            email.setNotes(emailEditNotesTextArea.getText());
        }

        insuranceCompany.addEmail(email);
        fillEmails();
        return true;
    }

    private boolean updateEmail() {
        Email email = ((EmailBox) emailViewComboBox.getSelectedItem()).email;
        String oldLabel = email.getLabel();

        if (!StringUtils.isBlank(emailEditLabelTextField.getText())) {
            if (!oldLabel.equals(emailEditLabelTextField.getText())) {
                if (insuranceCompany.containsEmailLabel(emailEditLabelTextField.getText())) {
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
        //insuranceCompany = controller.getInsuranceCompany();
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

        if (insuranceCompany.containsPhoneLabel(phone.getLabel())) {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("LABEL_INUSE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("PHONE")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }
        if (!StringUtils.isBlank(phoneEditNotesTextArea.getText())) {
            phone.setNotes(phoneEditNotesTextArea.getText());
        }

        insuranceCompany.addPhone(phone);
        //insuranceCompany = controller.getInsuranceCompany();
        fillPhones();
        return true;
    }

    private boolean updatePhone() {
        Phone phone = ((PhoneBox) phoneViewComboBox.getSelectedItem()).phone;
        String oldLabel = phone.getLabel();

        if (!StringUtils.isBlank(phoneEditLabelTextField.getText())) {
            if (!oldLabel.equals(phoneEditLabelTextField.getText())) {
                if (insuranceCompany.containsPhoneLabel(phoneEditLabelTextField.getText())) {
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
        //insuranceCompany = controller.getInsuranceCompany();
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

        if (insuranceCompany.containsAddressLabel(address.getLabel())) {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("LABEL_INUSE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("ADDRESS")));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }
        if (!StringUtils.isBlank(addressEditNotesTextArea.getText())) {
            address.setNotes(addressEditNotesTextArea.getText());
        }

        insuranceCompany.addAddress(address);
        //insuranceCompany = controller.getInsuranceCompany();
        fillAddresses();
        return true;
    }

    private boolean updateAddress() {
        Address address = ((AddressBox) addressViewComboBox.getSelectedItem()).address;
        String oldLabel = address.getLabel();

        if (!StringUtils.isBlank(addressEditLabelTextField.getText())) {
            if (!oldLabel.equals(addressEditLabelTextField.getText())) {
                if (insuranceCompany.containsAddressLabel(addressEditLabelTextField.getText())) {
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
        //insuranceCompany = controller.getInsuranceCompany();
        fillAddresses();

        return true;
    }

    private void setupUndo() {
        nameTextField.getDocument().addUndoableEditListener(manager);
        notesTextArea.getDocument().addUndoableEditListener(manager);
        nifTextField.getDocument().addUndoableEditListener(manager);
        webTextField.getDocument().addUndoableEditListener(manager);



        //TODO add the rest of the textfields
    }

    private void removeUndo() {
        nameTextField.getDocument().removeUndoableEditListener(manager);
        notesTextArea.getDocument().removeUndoableEditListener(manager);
        nifTextField.getDocument().removeUndoableEditListener(manager);
        webTextField.getDocument().removeUndoableEditListener(manager);

    }

    private void modify() {
        modified = true;
        saveInsuranceCompanyButton.setEnabled(modified);
        reloadInsuranceCompanyButton.setEnabled(modified && !newInsuranceCompany);

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
