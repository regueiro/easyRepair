/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.regueiro.easyrepair.ui.employee;

import es.regueiro.easyrepair.login.SecurityManager;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import es.regueiro.easyrepair.api.employee.controller.EmployeeController;
import es.regueiro.easyrepair.model.employee.Employee;
import es.regueiro.easyrepair.model.shared.*;
import es.regueiro.easyrepair.model.user.Privilege;
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
import org.openide.util.Exceptions;
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
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.employee//EmployeeEditor//EN",
autostore = false)
@TopComponent.Description(iconBase = "es/regueiro/easyrepair/ui/employee/icons/mechanic_edit.png", preferredID = "EmployeeEditorTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 25, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.employee.EmployeeEditorTopComponent")
@TopComponent.OpenActionRegistration(displayName = "#CTL_EmployeeEditorAction",
preferredID = "EmployeeEditorTopComponent")
@Messages({
    "CTL_EmployeeEditorAction=Editor de empleados",
    "CTL_EmployeeEditorTopComponent=Editor de empleados",
    "HINT_EmployeeEditorTopComponent=Esta es una ventana del editor de empleados"
})
public final class EmployeeEditorTopComponent extends TopComponent implements LookupListener, DocumentListener {

    private Employee employee = null;
    private Lookup.Result<Employee> result;
    private InstanceContent content = new InstanceContent();
    private UndoRedo.Manager manager = new UndoRedo.Manager();
    private EmployeeController controller = Lookup.getDefault().lookup(EmployeeController.class);
    private DialogDescriptor editEmaildialog;
    private DialogDescriptor editAddressdialog;
    private DialogDescriptor editPhonedialog;
    private boolean modified = false;
    private boolean newEmployee = true;
    private boolean invalid = false;
    private PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public EmployeeEditorTopComponent() {
        initComponents();
        setName(Bundle.CTL_EmployeeEditorTopComponent());
        setToolTipText(Bundle.HINT_EmployeeEditorTopComponent());
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



        mainTabbedPane.setIconAt(1, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/book.png")));
        mainTabbedPane.setIconAt(2, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/email.png")));
        mainTabbedPane.setIconAt(3, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/phone.png")));
        mainTabbedPane.setIconAt(0, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/mechanic.png")));
//        emailEditAddressTextField.getDocument().addUndoableEditListener(manager);


        employeeIdTextField.setDocument(new MaxLengthTextDocument(8));
        nameTextField.setDocument(new MaxLengthTextDocument(100));
        surnameTextField.setDocument(new MaxLengthTextDocument(100));
        nifTextField.setDocument(new MaxLengthTextDocument(9));
        nssTextField.setDocument(new MaxLengthTextDocument(14));
        occupationTextField.setDocument(new MaxLengthTextDocument(100));


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

        employeeIdTextField.getDocument().addDocumentListener(this);
        nameTextField.getDocument().addDocumentListener(this);
        surnameTextField.getDocument().addDocumentListener(this);
        notesTextArea.getDocument().addDocumentListener(this);
        nifTextField.getDocument().addDocumentListener(this);
        nssTextField.getDocument().addDocumentListener(this);
        occupationTextField.getDocument().addDocumentListener(this);

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
        surnameLabel = new javax.swing.JLabel();
        surnameTextField = new javax.swing.JTextField();
        nifLabel = new javax.swing.JLabel();
        nifTextField = new javax.swing.JTextField();
        nssLabel = new javax.swing.JLabel();
        nssTextField = new javax.swing.JTextField();
        occupationLabel = new javax.swing.JLabel();
        occupationTextField = new javax.swing.JTextField();
        idLabel = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();
        employeeIdLabel = new javax.swing.JLabel();
        employeeIdTextField = new javax.swing.JTextField();
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
        saveEmployeeButton = new javax.swing.JButton();
        reloadEmployeeButton = new javax.swing.JButton();
        enableEmployeeButton = new javax.swing.JButton();
        disableEmployeeButton = new javax.swing.JButton();
        deleteEmployeeButton = new javax.swing.JButton();
        printEmployeeButton = new javax.swing.JButton();

        addressEditPanel.setPreferredSize(new java.awt.Dimension(400, 400));
        addressEditPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(addressEditLabelLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressEditLabelLabel.text")); // NOI18N
        addressEditLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditStreetLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressEditStreetLabel.text")); // NOI18N
        addressEditStreetLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditStreetLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditCityLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressEditCityLabel.text")); // NOI18N
        addressEditCityLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditCityLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditProvinceLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressEditProvinceLabel.text")); // NOI18N
        addressEditProvinceLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditProvinceLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditPostalCodeLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressEditPostalCodeLabel.text")); // NOI18N
        addressEditPostalCodeLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditPostalCodeLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditCountryLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressEditCountryLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(addressEditNotesLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressEditNotesLabel.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(addressEditLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressEditLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        addressEditPanel.add(addressEditLabel, gridBagConstraints);

        phoneEditPanel.setPreferredSize(new java.awt.Dimension(400, 400));
        phoneEditPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(phoneEditLabelLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.phoneEditLabelLabel.text")); // NOI18N
        phoneEditLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneEditPanel.add(phoneEditLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(phoneEditNumberLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.phoneEditNumberLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(phoneEditNotesLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.phoneEditNotesLabel.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(phoneEditLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.phoneEditLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        phoneEditPanel.add(phoneEditLabel, gridBagConstraints);

        emailEditPanel.setPreferredSize(new java.awt.Dimension(400, 400));
        emailEditPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(emailEditLabelLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.emailEditLabelLabel.text")); // NOI18N
        emailEditLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emailEditPanel.add(emailEditLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(emailEditAddressLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.emailEditAddressLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(emailEditNotesLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.emailEditNotesLabel.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(emailEditLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.emailEditLabel.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(generalLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.generalLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        generalPanel.add(generalLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(nameLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.nameLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(surnameLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.surnameLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(nifLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.nifLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(nssLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.nssLabel.text")); // NOI18N
        nssLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(nssLabel, gridBagConstraints);

        nssTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        nssTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(nssTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(occupationLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.occupationLabel.text")); // NOI18N
        occupationLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(occupationLabel, gridBagConstraints);

        occupationTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        occupationTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(occupationTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(idLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.idLabel.text")); // NOI18N
        idLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(idLabel, gridBagConstraints);

        idTextField.setEditable(false);
        idTextField.setBackground(new java.awt.Color(232, 231, 231));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(idTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(employeeIdLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.employeeIdLabel.text")); // NOI18N
        employeeIdLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(employeeIdLabel, gridBagConstraints);

        employeeIdTextField.setDoubleBuffered(true);
        employeeIdTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        employeeIdTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(employeeIdTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        mainGeneralPanel.add(generalPanel, gridBagConstraints);

        notesPanel.setLayout(new java.awt.GridBagLayout());

        notesLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        notesLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(notesLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.notesLabel.text")); // NOI18N
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

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.mainGeneralPanel.TabConstraints.tabTitle"), mainGeneralPanel); // NOI18N

        addressViewPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(addressViewLabelLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressViewLabelLabel.text")); // NOI18N
        addressViewLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewStreetLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressViewStreetLabel.text")); // NOI18N
        addressViewStreetLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewStreetLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewCityLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressViewCityLabel.text")); // NOI18N
        addressViewCityLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewCityLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewProvinceLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressViewProvinceLabel.text")); // NOI18N
        addressViewProvinceLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewProvinceLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewPostalCodeLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressViewPostalCodeLabel.text")); // NOI18N
        addressViewPostalCodeLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewPostalCodeLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressViewCountryLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressViewCountryLabel.text")); // NOI18N
        addressViewCountryLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressViewPanel.add(addressViewCountryLabel, gridBagConstraints);

        addressViewLabelTextField.setEditable(false);
        addressViewLabelTextField.setBackground(new java.awt.Color(232, 231, 231));
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

        addressViewStreetTextField.setEditable(false);
        addressViewStreetTextField.setBackground(new java.awt.Color(232, 231, 231));
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

        addressViewCityTextField.setEditable(false);
        addressViewCityTextField.setBackground(new java.awt.Color(232, 231, 231));
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

        addressViewProvinceTextField.setEditable(false);
        addressViewProvinceTextField.setBackground(new java.awt.Color(232, 231, 231));
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

        addressViewPostalCodeTextField.setEditable(false);
        addressViewPostalCodeTextField.setBackground(new java.awt.Color(232, 231, 231));
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

        addressViewCountryTextField.setEditable(false);
        addressViewCountryTextField.setBackground(new java.awt.Color(232, 231, 231));
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

        org.openide.awt.Mnemonics.setLocalizedText(addressViewNotesLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressViewNotesLabel.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(addressViewLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressViewLabel.text")); // NOI18N
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

        addressViewAddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addressViewAddButton, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressViewAddButton.text")); // NOI18N
        addressViewAddButton.setMinimumSize(new java.awt.Dimension(32, 32));
        addressViewAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressViewAddButtonActionPerformed(evt);
            }
        });
        addressViewButtonsPanel.add(addressViewAddButton);

        addressViewEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/pencil.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addressViewEditButton, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressViewEditButton.text")); // NOI18N
        addressViewEditButton.setEnabled(false);
        addressViewEditButton.setMaximumSize(null);
        addressViewEditButton.setMinimumSize(new java.awt.Dimension(32, 32));
        addressViewEditButton.setPreferredSize(null);
        addressViewEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressViewEditButtonActionPerformed(evt);
            }
        });
        addressViewButtonsPanel.add(addressViewEditButton);

        addressViewDeleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addressViewDeleteButton, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.addressViewDeleteButton.text")); // NOI18N
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

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle"); // NOI18N
        mainTabbedPane.addTab(bundle.getString("ADDRESS"), addressViewPanel); // NOI18N

        phoneViewPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(phoneViewLabelLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.phoneViewLabelLabel.text")); // NOI18N
        phoneViewLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneViewPanel.add(phoneViewLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(phoneViewNumberLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.phoneViewNumberLabel.text")); // NOI18N
        phoneViewNumberLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneViewPanel.add(phoneViewNumberLabel, gridBagConstraints);

        phoneViewLabelTextField.setEditable(false);
        phoneViewLabelTextField.setBackground(new java.awt.Color(232, 231, 231));
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

        phoneViewNumberTextField.setEditable(false);
        phoneViewNumberTextField.setBackground(new java.awt.Color(232, 231, 231));
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

        org.openide.awt.Mnemonics.setLocalizedText(phoneViewNotesLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.phoneViewNotesLabel.text")); // NOI18N
        phoneViewNotesLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneViewPanel.add(phoneViewNotesLabel, gridBagConstraints);

        phoneViewNotesTextArea.setEditable(false);
        phoneViewNotesTextArea.setBackground(new java.awt.Color(232, 231, 231));
        phoneViewNotesTextArea.setColumns(20);
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
        org.openide.awt.Mnemonics.setLocalizedText(phoneViewLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.phoneViewLabel.text")); // NOI18N
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

        phoneViewAddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(phoneViewAddButton, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.phoneViewAddButton.text")); // NOI18N
        phoneViewAddButton.setMinimumSize(new java.awt.Dimension(32, 32));
        phoneViewAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneViewAddButtonActionPerformed(evt);
            }
        });
        phoneViewButtonsPanel.add(phoneViewAddButton);

        phoneViewEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/pencil.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(phoneViewEditButton, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.phoneViewEditButton.text")); // NOI18N
        phoneViewEditButton.setEnabled(false);
        phoneViewEditButton.setMaximumSize(null);
        phoneViewEditButton.setMinimumSize(new java.awt.Dimension(32, 32));
        phoneViewEditButton.setPreferredSize(null);
        phoneViewEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneViewEditButtonActionPerformed(evt);
            }
        });
        phoneViewButtonsPanel.add(phoneViewEditButton);

        phoneViewDeleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(phoneViewDeleteButton, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.phoneViewDeleteButton.text")); // NOI18N
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

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.phoneViewPanel.TabConstraints.tabTitle"), phoneViewPanel); // NOI18N

        emaiViewlPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(emailViewLabelLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.emailViewLabelLabel.text")); // NOI18N
        emailViewLabelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emaiViewlPanel.add(emailViewLabelLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(emailViewAddressLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.emailViewAddressLabel.text")); // NOI18N
        emailViewAddressLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emaiViewlPanel.add(emailViewAddressLabel, gridBagConstraints);

        emailViewLabelTextField.setEditable(false);
        emailViewLabelTextField.setBackground(new java.awt.Color(232, 231, 231));
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

        emailViewAddressTextField.setEditable(false);
        emailViewAddressTextField.setBackground(new java.awt.Color(232, 231, 231));
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

        org.openide.awt.Mnemonics.setLocalizedText(emailViewNotesLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.emailViewNotesLabel.text")); // NOI18N
        emailViewNotesLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emaiViewlPanel.add(emailViewNotesLabel, gridBagConstraints);

        emailViewNotesTextArea.setEditable(false);
        emailViewNotesTextArea.setBackground(new java.awt.Color(232, 231, 231));
        emailViewNotesTextArea.setColumns(20);
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
        org.openide.awt.Mnemonics.setLocalizedText(emailLabel, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.emailLabel.text")); // NOI18N
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

        emailViewAddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(emailViewAddButton, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.emailViewAddButton.text")); // NOI18N
        emailViewAddButton.setMinimumSize(new java.awt.Dimension(32, 32));
        emailViewAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailViewAddButtonActionPerformed(evt);
            }
        });
        emailViewButtonsPanel.add(emailViewAddButton);

        emailViewEditButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/pencil.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(emailViewEditButton, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.emailViewEditButton.text")); // NOI18N
        emailViewEditButton.setEnabled(false);
        emailViewEditButton.setMaximumSize(null);
        emailViewEditButton.setMinimumSize(new java.awt.Dimension(32, 32));
        emailViewEditButton.setPreferredSize(null);
        emailViewEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailViewEditButtonActionPerformed(evt);
            }
        });
        emailViewButtonsPanel.add(emailViewEditButton);

        emailViewDeleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(emailViewDeleteButton, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.emailViewDeleteButton.text")); // NOI18N
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

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.emaiViewlPanel.TabConstraints.tabTitle"), emaiViewlPanel); // NOI18N

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

        saveEmployeeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/disk.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(saveEmployeeButton, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.saveEmployeeButton.text")); // NOI18N
        saveEmployeeButton.setFocusable(false);
        saveEmployeeButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        saveEmployeeButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        saveEmployeeButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        saveEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveEmployeeButtonActionPerformed(evt);
            }
        });
        topToolBar.add(saveEmployeeButton);

        reloadEmployeeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/arrow_refresh.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(reloadEmployeeButton, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.reloadEmployeeButton.text")); // NOI18N
        reloadEmployeeButton.setFocusable(false);
        reloadEmployeeButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        reloadEmployeeButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        reloadEmployeeButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        reloadEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadEmployeeButtonActionPerformed(evt);
            }
        });
        topToolBar.add(reloadEmployeeButton);

        enableEmployeeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/status_online.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(enableEmployeeButton, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.enableEmployeeButton.text")); // NOI18N
        enableEmployeeButton.setFocusable(false);
        enableEmployeeButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        enableEmployeeButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        enableEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableEmployeeButtonActionPerformed(evt);
            }
        });
        topToolBar.add(enableEmployeeButton);

        disableEmployeeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/status_offline.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(disableEmployeeButton, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.disableEmployeeButton.text")); // NOI18N
        disableEmployeeButton.setFocusable(false);
        disableEmployeeButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        disableEmployeeButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        disableEmployeeButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        disableEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disableEmployeeButtonActionPerformed(evt);
            }
        });
        topToolBar.add(disableEmployeeButton);

        deleteEmployeeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteEmployeeButton, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.deleteEmployeeButton.text")); // NOI18N
        deleteEmployeeButton.setFocusable(false);
        deleteEmployeeButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deleteEmployeeButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deleteEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteEmployeeButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deleteEmployeeButton);

        printEmployeeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/employee/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printEmployeeButton, org.openide.util.NbBundle.getMessage(EmployeeEditorTopComponent.class, "EmployeeEditorTopComponent.printEmployeeButton.text")); // NOI18N
        printEmployeeButton.setFocusable(false);
        printEmployeeButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printEmployeeButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printEmployeeButton);

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


        editAddressdialog = new DialogDescriptor(addressEditPanel, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("ADD_ADDRESS"), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
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


        editAddressdialog = new DialogDescriptor(addressEditPanel, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EDIT_ADDRESS"), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
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


        editPhonedialog = new DialogDescriptor(phoneEditPanel, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("ADD_PHONE"), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
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


        editPhonedialog = new DialogDescriptor(phoneEditPanel, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EDIT_PHONE"), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
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

        editEmaildialog = new DialogDescriptor(emailEditPanel, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("ADD_EMAIL"), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
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


        editEmaildialog = new DialogDescriptor(emailEditPanel, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EDIT_EMAIL"), true, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.OK_OPTION,
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
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("SURE_DELETE_ADDRESS"),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

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
                    employee.removeAddress(addressViewLabelTextField.getText());
                    return null;
                }
            };


            worker.execute();
        }
    }//GEN-LAST:event_addressViewDeleteButtonActionPerformed

    private void phoneViewDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneViewDeleteButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("SURE_DELETE_PHONE"),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

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
                    employee.removePhone(phoneViewLabelTextField.getText());
                    return null;
                }
            };


            worker.execute();
        }
    }//GEN-LAST:event_phoneViewDeleteButtonActionPerformed

    private void emailViewDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailViewDeleteButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("SURE_DELETE_EMAIL"),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

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
                    employee.removeEmail(emailViewLabelTextField.getText());
                    return null;
                }
            };


            worker.execute();
//            controller.removeEmail(emailViewLabelTextField.getText());
//            fillEmails();
        }
    }//GEN-LAST:event_emailViewDeleteButtonActionPerformed

    private void saveEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveEmployeeButtonActionPerformed
        saveEmployee();
    }//GEN-LAST:event_saveEmployeeButtonActionPerformed

    private void disableEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disableEmployeeButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
                disableEmployeeButton.setVisible(false);
                enableEmployeeButton.setVisible(true);
                fillData();
                StatusDisplayer.getDefault().setStatusText(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("DISABLED_EMPLOYEE"));
            }

            @Override
            protected Void doInBackground() {
                employee = controller.disableEmployee();
                return null;
            }
        };

        worker.execute();

    }//GEN-LAST:event_disableEmployeeButtonActionPerformed

    private void reloadEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadEmployeeButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("WANT_RELOAD_EMPLOYEE"),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("RELOAD"), NotifyDescriptor.YES_NO_OPTION);

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
                                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EMPLOYEE_RELOAD_NOT_FOUND"),
                                    NotifyDescriptor.ERROR_MESSAGE);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                    StatusDisplayer.getDefault().setStatusText(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("RELOADED_EMPLOYEE"));
                }

                @Override
                protected Boolean doInBackground() {
                    employee = controller.reloadEmployee();
                    if (employee != null) {
                        controller.setEmployee(employee);
                        return true;
                    } else {
                        return false;
                    }
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_reloadEmployeeButtonActionPerformed

    private void enableEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableEmployeeButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
                disableEmployeeButton.setVisible(true);
                enableEmployeeButton.setVisible(false);
                fillData();
                StatusDisplayer.getDefault().setStatusText(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("ENABLED_EMPLOYEE"));
            }

            @Override
            protected Void doInBackground() {
                employee = controller.enableEmployee();
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_enableEmployeeButtonActionPerformed

    private void deleteEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteEmployeeButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("SURE_DELETE_EMPLOYEE"),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {


            EmployeeLookup.getDefault().clear();

            EmployeeBrowserTopComponent window = (EmployeeBrowserTopComponent) WindowManager.getDefault().findTopComponent("EmployeeBrowserTopComponent");

            window.deleteEmployee(employee);
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    forceClose();
                    StatusDisplayer.getDefault().setStatusText(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EMPLOYEE_DELETED"));
                }

                @Override
                protected Void doInBackground() {
                    controller.setEmployee(employee);
                    controller.deleteEmployee();
                    return null;
                }
            };

            worker.execute();
        }
    }//GEN-LAST:event_deleteEmployeeButtonActionPerformed

    private void printEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printEmployeeButtonActionPerformed
        if (updateEmployee()) {
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                }

                @Override
                protected Void doInBackground() {
                    ReportPrinter printer = new ReportPrinter();
                    printer.printEmployee(employee);
                    return null;
                }
            };
            worker.execute();
        }
    }//GEN-LAST:event_printEmployeeButtonActionPerformed
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
    private javax.swing.JButton deleteEmployeeButton;
    private javax.swing.JButton disableEmployeeButton;
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
    private javax.swing.JLabel employeeIdLabel;
    private javax.swing.JTextField employeeIdTextField;
    private javax.swing.JButton enableEmployeeButton;
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
    private javax.swing.JLabel nssLabel;
    private javax.swing.JTextField nssTextField;
    private javax.swing.JLabel occupationLabel;
    private javax.swing.JTextField occupationTextField;
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
    private javax.swing.JButton printEmployeeButton;
    private javax.swing.JButton reloadEmployeeButton;
    private javax.swing.JButton saveEmployeeButton;
    private javax.swing.JLabel surnameLabel;
    private javax.swing.JTextField surnameTextField;
    private javax.swing.JToolBar topToolBar;
    // End of variables declaration//GEN-END:variables

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("es.regueiro.easyrepair.ui.employee.editor");
    }

    public Employee getEmployee() {
        return employee;
    }

    public void forceClose() {
        modified = false;
        this.close();
    }

    @Override
    public void componentOpened() {
        invalid = false;
        result = EmployeeLookup.getDefault().lookupResult(Employee.class);
        result.removeLookupListener(this);
        mainTabbedPane.setSelectedIndex(0);

        Collection<? extends Employee> employeeColId = result.allInstances();
        if (!employeeColId.isEmpty()) {
            employee = controller.getEmployeeById(employeeColId.iterator().next().getId());
            if (employee == null) {
                NotifyDescriptor d = new NotifyDescriptor.Message(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EMPLOYEE_NOT_FOUND"), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                close();
            } else {
                newEmployee = false;
            }
        } else {
            employee = controller.newEmployee();
            newEmployee = true;
        }
        if (employee != null) {
            reloadEmployeeButton.setEnabled(!newEmployee);
            deleteEmployeeButton.setEnabled(!newEmployee);
            enableEmployeeButton.setVisible(false);
            disableEmployeeButton.setEnabled(!newEmployee);
            controller.setEmployee(employee);
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
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("CHANGES_WILL_BE_LOST"),
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("CLOSE"), NotifyDescriptor.YES_NO_OPTION);

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
//        Collection<? extends Employee> employeeCol = result.allInstances();
//        if (!employeeCol.isEmpty()) {
//            Employee tempEmployee = employeeCol.iterator().next();
//            if (employee != tempEmployee) {
//                employee = tempEmployee;
//            }
//        } else {
//            employee = new Employee();
//        }
//        controller.setEmployee(employee);
//
//        fillData();
    }

    @Override
    public UndoRedo getUndoRedo() {
        return manager;
    }

    private void checkPermissions() {
        if (!SecurityManager.getDefault().isUserLoggedIn() || !SecurityManager.getDefault().userHasPrivilege(Privilege.EMPLOYEE_EDIT)) {
            this.forceClose();
        }
    }

    private void fillData() {
        clearGeneral();
        clearNotes();

        if (employee != null && employee.getId() != null) {
            idTextField.setText(employee.getId().toString());
            if (employee.getEmployeeId() != null) {
                employeeIdTextField.setText(employee.getEmployeeId());
            }
            if (employee.getName() != null) {
                nameTextField.setText(employee.getName());
            }
            if (employee.getSurname() != null) {
                surnameTextField.setText(employee.getSurname());
            }
            if (employee.getNif() != null) {
                nifTextField.setText(employee.getNif().getNumber());
            }
            if (employee.getNss() != null) {
                nssTextField.setText(employee.getNss().getNumber());
            }
            if (employee.getOccupation() != null) {
                occupationTextField.setText(employee.getOccupation());
            }
            if (employee.getNotes() != null) {
                notesTextArea.setText(employee.getNotes());
            }
            if (employee.getEnabled()) {
                enableEmployeeButton.setVisible(false);
                disableEmployeeButton.setVisible(true);
                disableEmployeeButton.setEnabled(true);
            } else {
                enableEmployeeButton.setVisible(true);
                disableEmployeeButton.setVisible(false);
            }
        }
        fillAddresses();
        fillPhones();
        fillEmails();

        manager.discardAllEdits();
        modified = invalid;
        saveEmployeeButton.setEnabled(modified);
        deleteEmployeeButton.setEnabled(!newEmployee);
        reloadEmployeeButton.setEnabled(modified && !newEmployee);
    }

    private void fillAddresses() {
        clearAddresses();
        if (employee.getAddress() != null && !employee.getAddress().isEmpty()) {
            for (Address a : employee.getAddress()) {
                AddressBox adddress = new AddressBox(a.getLabel(), a);
                addressViewComboBox.addItem(adddress);
            }
            addressViewComboBox.setEnabled(true);
        }
    }

    private void fillPhones() {
        clearPhones();
        if (employee.getPhone() != null && !employee.getPhone().isEmpty()) {
            for (Phone p : employee.getPhone()) {
                PhoneBox phone = new PhoneBox(p.getLabel(), p);
                phoneViewComboBox.addItem(phone);
            }
            phoneViewComboBox.setEnabled(true);
        }
    }

    private void fillEmails() {
        clearEmails();
        if (employee.getEmail() != null && !employee.getEmail().isEmpty()) {
            for (Email e : employee.getEmail()) {
                EmailBox email = new EmailBox(e.getLabel(), e);
                emailViewComboBox.addItem(email);
            }
            emailViewComboBox.setEnabled(true);
        }
    }

    private void clearGeneral() {
        employeeIdTextField.setText("");
        nameTextField.setText("");
        surnameTextField.setText("");
        idTextField.setText("");
        nifTextField.setText("");
        nssTextField.setText("");
        occupationTextField.setText("");
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

    private void saveEmployee() {
        if (updateEmployee()) {
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("SAVING"));

                @Override
                protected void done() {
                    fillData();
                    p.finish();
                }

                @Override
                protected Void doInBackground() {
                    p.start();
                    try {
                        controller.setEmployee(employee);
                        controller.saveEmployee();
                        newEmployee = false;
                        invalid = false;
                    } catch (Exception e) {
                        if (e.getMessage().contains("The employee was updated or deleted by another user")) {
                            NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("OVERWRITE_DIALOG"),
                                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EMPLOYEE_MODIFIED"));
                            String options[] = new String[2];
                            options[0] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("OVERWRITE");
                            options[1] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("RELOAD");
                            d.setOptions(options);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                            if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("OVERWRITE"))) {
                                controller.overwriteEmployee();
                                employee = controller.getEmployee();
                                invalid = false;
                            } else if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("RELOAD"))) {
                                reloadEmployeeButton.doClick();
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

    private boolean updateEmployee() {
        if (!StringUtils.isBlank(nameTextField.getText())) {
            employee.setName(nameTextField.getText());
            if (!StringUtils.isBlank(employeeIdTextField.getText())) {
                try {
                    employee.setEmployeeId(employeeIdTextField.getText());
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    invalid = true;
                    return false;
                }
            } else {
                employee.setEmployeeId(null);
            }
            if (!StringUtils.isBlank(surnameTextField.getText())) {
                employee.setSurname(surnameTextField.getText());
            } else {
                employee.setSurname(null);
            }
            if (!StringUtils.isBlank(nifTextField.getText())) {
                try {
                    employee.setNif(new NIF(nifTextField.getText()));
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    invalid = true;
                    return false;
                }
            } else {
                employee.setNif(null);
            }
            if (!StringUtils.isBlank(nssTextField.getText())) {
                try {
                    employee.setNss(new NSS(nssTextField.getText()));
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    invalid = true;
                    return false;
                }
            } else {
                employee.setNss(null);
            }
            if (!StringUtils.isBlank(notesTextArea.getText())) {
                employee.setNotes(notesTextArea.getText());
            } else {
                employee.setNotes(null);
            }
            if (!StringUtils.isBlank(occupationTextField.getText())) {
                employee.setOccupation(occupationTextField.getText());
            } else {
                employee.setOccupation(null);
            }
            invalid = false;
            return true;

        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("NAME_NOT_BLANK"), NotifyDescriptor.WARNING_MESSAGE);
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
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EMAIL_VALID_NUMBER"));
                DialogDisplayer.getDefault().notify(d);
                return false;

            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EMAIL_HAVE_LABEL"));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }

        if (employee.containsEmailLabel(email.getLabel())) {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("LABEL_IN_USE_EMAIL"));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }
        if (!StringUtils.isBlank(emailEditNotesTextArea.getText())) {
            email.setNotes(emailEditNotesTextArea.getText());
        }

        employee.addEmail(email);
        fillEmails();
        return true;
    }

    private boolean updateEmail() {
        Email email = ((EmailBox) emailViewComboBox.getSelectedItem()).email;
        String oldLabel = email.getLabel();

        if (!StringUtils.isBlank(emailEditLabelTextField.getText())) {
            if (!oldLabel.equals(emailEditLabelTextField.getText())) {
                if (employee.containsEmailLabel(emailEditLabelTextField.getText())) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(
                            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("LABEL_IN_USE_EMAIL"));
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
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EMAIL_VALID_ADDRESS"));
                DialogDisplayer.getDefault().notify(d);
                return false;

            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("EMAIL_HAVE_LABEL"));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }


        if (!StringUtils.isBlank(emailEditNotesTextArea.getText())) {
            email.setNotes(emailEditNotesTextArea.getText());
        }
        //employee = controller.getEmployee();
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
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("PHONE_VALID_NUMBER"));
                DialogDisplayer.getDefault().notify(d);
                return false;

            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("PHONE_HAVE_LABEL"));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }

        if (employee.containsPhoneLabel(phone.getLabel())) {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("LABEL_IN_USE_PHONE"));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }
        if (!StringUtils.isBlank(phoneEditNotesTextArea.getText())) {
            phone.setNotes(phoneEditNotesTextArea.getText());
        }

        employee.addPhone(phone);
        //employee = controller.getEmployee();
        fillPhones();
        return true;
    }

    private boolean updatePhone() {
        Phone phone = ((PhoneBox) phoneViewComboBox.getSelectedItem()).phone;
        String oldLabel = phone.getLabel();

        if (!StringUtils.isBlank(phoneEditLabelTextField.getText())) {
            if (!oldLabel.equals(phoneEditLabelTextField.getText())) {
                if (employee.containsPhoneLabel(phoneEditLabelTextField.getText())) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(
                            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("LABEL_IN_USE_PHONE"));
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
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("PHONE_VALID_NUMBER"));
                DialogDisplayer.getDefault().notify(d);
                return false;

            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("PHONE_HAVE_LABEL"));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }


        if (!StringUtils.isBlank(phoneEditNotesTextArea.getText())) {
            phone.setNotes(phoneEditNotesTextArea.getText());
        }
        //employee = controller.getEmployee();
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
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("ADDRESS_FILED"));
                DialogDisplayer.getDefault().notify(d);
                return false;
            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("ADDRESS_HAVE_LABEL"));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }

        if (employee.containsAddressLabel(address.getLabel())) {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("LABEL_IN_USE_ADDRESS"));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }
        if (!StringUtils.isBlank(addressEditNotesTextArea.getText())) {
            address.setNotes(addressEditNotesTextArea.getText());
        }

        employee.addAddress(address);
        //employee = controller.getEmployee();
        fillAddresses();
        return true;
    }

    private boolean updateAddress() {
        Address address = ((AddressBox) addressViewComboBox.getSelectedItem()).address;
        String oldLabel = address.getLabel();

        if (!StringUtils.isBlank(addressEditLabelTextField.getText())) {
            if (!oldLabel.equals(addressEditLabelTextField.getText())) {
                if (employee.containsAddressLabel(addressEditLabelTextField.getText())) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(
                            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("LABEL_IN_USE_ADDRESSADDRESS"));
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
                        java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("ADDRESS_FILED"));
                DialogDisplayer.getDefault().notify(d);
                return false;
            }
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/employee/Bundle").getString("ADDRESS_HAVE_LABELLABEL"));
            DialogDisplayer.getDefault().notify(d);
            return false;

        }


        if (!StringUtils.isBlank(addressEditNotesTextArea.getText())) {
            address.setNotes(addressEditNotesTextArea.getText());
        }
        //employee = controller.getEmployee();
        fillAddresses();

        return true;
    }

    private void setupUndo() {
        employeeIdTextField.getDocument().addUndoableEditListener(manager);
        nameTextField.getDocument().addUndoableEditListener(manager);
        surnameTextField.getDocument().addUndoableEditListener(manager);
        notesTextArea.getDocument().addUndoableEditListener(manager);
        nifTextField.getDocument().addUndoableEditListener(manager);
        nssTextField.getDocument().addUndoableEditListener(manager);
        occupationTextField.getDocument().addUndoableEditListener(manager);

    }

    private void removeUndo() {
        nameTextField.getDocument().removeUndoableEditListener(manager);
        surnameTextField.getDocument().removeUndoableEditListener(manager);
        notesTextArea.getDocument().removeUndoableEditListener(manager);
        nifTextField.getDocument().removeUndoableEditListener(manager);
        nssTextField.getDocument().removeUndoableEditListener(manager);
        occupationTextField.getDocument().removeUndoableEditListener(manager);

    }

    private void modify() {
        modified = true;
        saveEmployeeButton.setEnabled(modified);
        reloadEmployeeButton.setEnabled(modified && !newEmployee);

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
