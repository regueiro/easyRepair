package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.login.SecurityManager;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import es.regueiro.easyrepair.api.stock.controller.WarehouseController;
import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.Phone;
import es.regueiro.easyrepair.model.stock.Warehouse;
import es.regueiro.easyrepair.model.user.Privilege;
import es.regueiro.easyrepair.reports.ReportPrinter;
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
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.stock//WarehouseEditor//EN",
autostore = false)
@TopComponent.Description(iconBase = "es/regueiro/easyrepair/ui/stock/icons/factory.png", preferredID = "WarehouseEditorTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 23, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.stock.WarehouseEditorTopComponent")
@TopComponent.OpenActionRegistration(displayName = "#CTL_WarehouseEditorAction",
preferredID = "WarehouseEditorTopComponent")
@Messages({
    "CTL_WarehouseEditorAction=Editor de almacenes",
    "CTL_WarehouseEditorTopComponent=Editor de almacenes",
    "HINT_WarehouseEditorTopComponent=Esta es una ventana del editor de almacenes"
})
public final class WarehouseEditorTopComponent extends TopComponent implements LookupListener, DocumentListener {

    private Warehouse warehouse = null;
    private Lookup.Result<Warehouse> result;
    private InstanceContent content = new InstanceContent();
    private UndoRedo.Manager manager = new UndoRedo.Manager();
    private WarehouseController controller = Lookup.getDefault().lookup(WarehouseController.class);
    private boolean modified = false;
    private boolean newWarehouse = true;
    private boolean invalid = false;
    private Address address;
    private Phone phone;
    private Email email;
    private PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public WarehouseEditorTopComponent() {
        initComponents();
        setName(Bundle.CTL_WarehouseEditorTopComponent());
        setToolTipText(Bundle.HINT_WarehouseEditorTopComponent());
        this.setFocusable(true);
        associateLookup(new AbstractLookup(content));
        ActionMap actionMap = getActionMap();
        actionMap.put(DefaultEditorKit.copyAction, new DefaultEditorKit.CopyAction());
        actionMap.put(DefaultEditorKit.cutAction, new DefaultEditorKit.CutAction());
        actionMap.put(DefaultEditorKit.pasteAction, new DefaultEditorKit.PasteAction());
        content.add(actionMap);


        nameTextField.setDocument(new MaxLengthTextDocument(100));

        emailEditAddressTextField.setDocument(new MaxLengthTextDocument(150));

        phoneEditNumberTextField.setDocument(new MaxLengthTextDocument(20));

        addressEditStreetTextField.setDocument(new MaxLengthTextDocument(200));
        addressEditCityTextField.setDocument(new MaxLengthTextDocument(100));
        addressEditCountryTextField.setDocument(new MaxLengthTextDocument(100));
        addressEditPostalCodeTextField.setDocument(new MaxLengthTextDocument(10));
        addressEditProvinceTextField.setDocument(new MaxLengthTextDocument(100));

        setupUndo();

        notesTextArea.getDocument().addDocumentListener(this);
        nameTextField.getDocument().addDocumentListener(this);
        emailEditAddressTextField.getDocument().addDocumentListener(this);
        phoneEditNumberTextField.getDocument().addDocumentListener(this);
        addressEditStreetTextField.getDocument().addDocumentListener(this);
        addressEditCityTextField.getDocument().addDocumentListener(this);
        addressEditCountryTextField.getDocument().addDocumentListener(this);
        addressEditPostalCodeTextField.getDocument().addDocumentListener(this);
        addressEditProvinceTextField.getDocument().addDocumentListener(this);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        topToolBar = new javax.swing.JToolBar();
        saveWarehouseButton = new javax.swing.JButton();
        reloadWarehouseButton = new javax.swing.JButton();
        enableWarehouseButton = new javax.swing.JButton();
        disableWarehouseButton = new javax.swing.JButton();
        deleteWarehouseButton = new javax.swing.JButton();
        printWarehouseButton = new javax.swing.JButton();
        mainScrollPane = new javax.swing.JScrollPane();
        mainGeneralPanel = new javax.swing.JPanel();
        generalPanel = new javax.swing.JPanel();
        generalLabel = new javax.swing.JLabel();
        idLabel = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        addressEditPanel = new javax.swing.JPanel();
        addressEditStreetLabel = new javax.swing.JLabel();
        addressEditCityLabel = new javax.swing.JLabel();
        addressEditProvinceLabel = new javax.swing.JLabel();
        addressEditPostalCodeLabel = new javax.swing.JLabel();
        addressEditCountryLabel = new javax.swing.JLabel();
        addressEditStreetTextField = new javax.swing.JTextField();
        addressEditCityTextField = new javax.swing.JTextField();
        addressEditProvinceTextField = new javax.swing.JTextField();
        addressEditPostalCodeTextField = new javax.swing.JTextField();
        addressEditCountryTextField = new javax.swing.JTextField();
        addressEditLabel = new javax.swing.JLabel();
        notesPanel = new javax.swing.JPanel();
        notesLabel = new javax.swing.JLabel();
        notesScrollPane = new javax.swing.JScrollPane();
        notesTextArea = new javax.swing.JTextArea();
        phoneEditPanel = new javax.swing.JPanel();
        phoneEditNumberLabel = new javax.swing.JLabel();
        phoneEditNumberTextField = new javax.swing.JTextField();
        phoneEditLabel = new javax.swing.JLabel();
        emailEditlPanel = new javax.swing.JPanel();
        emailEditAddressLabel = new javax.swing.JLabel();
        emailEditAddressTextField = new javax.swing.JTextField();
        emailLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        topToolBar.setBorder(null);
        topToolBar.setFloatable(false);
        topToolBar.setRollover(true);

        saveWarehouseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/disk.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(saveWarehouseButton, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.saveWarehouseButton.text")); // NOI18N
        saveWarehouseButton.setFocusable(false);
        saveWarehouseButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        saveWarehouseButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        saveWarehouseButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        saveWarehouseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveWarehouseButtonActionPerformed(evt);
            }
        });
        topToolBar.add(saveWarehouseButton);

        reloadWarehouseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/arrow_refresh.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(reloadWarehouseButton, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.reloadWarehouseButton.text")); // NOI18N
        reloadWarehouseButton.setFocusable(false);
        reloadWarehouseButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        reloadWarehouseButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        reloadWarehouseButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        reloadWarehouseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadWarehouseButtonActionPerformed(evt);
            }
        });
        topToolBar.add(reloadWarehouseButton);

        enableWarehouseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/status_online.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(enableWarehouseButton, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.enableWarehouseButton.text")); // NOI18N
        enableWarehouseButton.setFocusable(false);
        enableWarehouseButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        enableWarehouseButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        enableWarehouseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableWarehouseButtonActionPerformed(evt);
            }
        });
        topToolBar.add(enableWarehouseButton);

        disableWarehouseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/status_offline.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(disableWarehouseButton, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.disableWarehouseButton.text")); // NOI18N
        disableWarehouseButton.setFocusable(false);
        disableWarehouseButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        disableWarehouseButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        disableWarehouseButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        disableWarehouseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disableWarehouseButtonActionPerformed(evt);
            }
        });
        topToolBar.add(disableWarehouseButton);

        deleteWarehouseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteWarehouseButton, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.deleteWarehouseButton.text")); // NOI18N
        deleteWarehouseButton.setFocusable(false);
        deleteWarehouseButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deleteWarehouseButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deleteWarehouseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteWarehouseButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deleteWarehouseButton);

        printWarehouseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printWarehouseButton, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.printWarehouseButton.text")); // NOI18N
        printWarehouseButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printWarehouseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printWarehouseButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printWarehouseButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(topToolBar, gridBagConstraints);

        mainScrollPane.setBorder(null);
        mainScrollPane.setMinimumSize(new java.awt.Dimension(617, 22));

        mainGeneralPanel.setLayout(new java.awt.GridBagLayout());

        generalPanel.setLayout(new java.awt.GridBagLayout());

        generalLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        generalLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(generalLabel, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.generalLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        generalPanel.add(generalLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(idLabel, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.idLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(nameLabel, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.nameLabel.text")); // NOI18N
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        mainGeneralPanel.add(generalPanel, gridBagConstraints);

        addressEditPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(addressEditStreetLabel, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.addressEditStreetLabel.text")); // NOI18N
        addressEditStreetLabel.setMinimumSize(new java.awt.Dimension(78, 14));
        addressEditStreetLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditStreetLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditCityLabel, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.addressEditCityLabel.text")); // NOI18N
        addressEditCityLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditCityLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditProvinceLabel, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.addressEditProvinceLabel.text")); // NOI18N
        addressEditProvinceLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditProvinceLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditPostalCodeLabel, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.addressEditPostalCodeLabel.text")); // NOI18N
        addressEditPostalCodeLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditPostalCodeLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressEditCountryLabel, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.addressEditCountryLabel.text")); // NOI18N
        addressEditCountryLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressEditPanel.add(addressEditCountryLabel, gridBagConstraints);

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

        addressEditLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        addressEditLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(addressEditLabel, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.addressEditLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        addressEditPanel.add(addressEditLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        mainGeneralPanel.add(addressEditPanel, gridBagConstraints);

        notesPanel.setLayout(new java.awt.GridBagLayout());

        notesLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        notesLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(notesLabel, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.notesLabel.text")); // NOI18N
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
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        mainGeneralPanel.add(notesPanel, gridBagConstraints);

        phoneEditPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(phoneEditNumberLabel, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.phoneEditNumberLabel.text")); // NOI18N
        phoneEditNumberLabel.setMinimumSize(new java.awt.Dimension(78, 14));
        phoneEditNumberLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phoneEditPanel.add(phoneEditNumberLabel, gridBagConstraints);

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

        phoneEditLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        phoneEditLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(phoneEditLabel, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.phoneEditLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        phoneEditPanel.add(phoneEditLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        mainGeneralPanel.add(phoneEditPanel, gridBagConstraints);

        emailEditlPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(emailEditAddressLabel, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.emailEditAddressLabel.text")); // NOI18N
        emailEditAddressLabel.setMinimumSize(new java.awt.Dimension(78, 14));
        emailEditAddressLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emailEditlPanel.add(emailEditAddressLabel, gridBagConstraints);

        emailEditAddressTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        emailEditAddressTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emailEditlPanel.add(emailEditAddressTextField, gridBagConstraints);

        emailLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        emailLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(emailLabel, org.openide.util.NbBundle.getMessage(WarehouseEditorTopComponent.class, "WarehouseEditorTopComponent.emailLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        emailEditlPanel.add(emailLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        mainGeneralPanel.add(emailEditlPanel, gridBagConstraints);

        mainScrollPane.setViewportView(mainGeneralPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(mainScrollPane, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void saveWarehouseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveWarehouseButtonActionPerformed
        saveWarehouse();
    }//GEN-LAST:event_saveWarehouseButtonActionPerformed

    private void disableWarehouseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disableWarehouseButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
//                disableWarehouseButton.setVisible(false);
//                enableWarehouseButton.setVisible(true);
                fillData();
                StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DISABLED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE")));
            }

            @Override
            protected Void doInBackground() {
                if (updateWarehouse()) {
                    warehouse = controller.disableWarehouse();
                }
                return null;
            }
        };

        worker.execute();

    }//GEN-LAST:event_disableWarehouseButtonActionPerformed

    private void reloadWarehouseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadWarehouseButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SURE_RELOAD_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE")),
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
                                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE_RELOAD_NOT_FOUND"),
                                    NotifyDescriptor.ERROR_MESSAGE);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RELOADED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE")));
                }

                @Override
                protected Boolean doInBackground() {
                    warehouse = controller.reloadWarehouse();
                    if (warehouse != null) {
                        controller.setWarehouse(warehouse);
                        return true;
                    } else {
                        return false;
                    }
                }
            };

            worker.execute();

        }
    }//GEN-LAST:event_reloadWarehouseButtonActionPerformed

    private void enableWarehouseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableWarehouseButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
//                disableWarehouseButton.setVisible(true);
//                enableWarehouseButton.setVisible(false);
                fillData();
                StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ENABLED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE")));
            }

            @Override
            protected Void doInBackground() {
                if (updateWarehouse()) {
                    warehouse = controller.enableWarehouse();
                }
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_enableWarehouseButtonActionPerformed

    private void deleteWarehouseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteWarehouseButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {


            WarehouseLookup.getDefault().clear();

            WarehouseBrowserTopComponent window = (WarehouseBrowserTopComponent) WindowManager.getDefault().findTopComponent("WarehouseBrowserTopComponent");

            window.deleteWarehouse(warehouse);
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    forceClose();
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE")));
                }

                @Override
                protected Void doInBackground() {
                    controller.setWarehouse(warehouse);
                    controller.deleteWarehouse();
                    return null;
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_deleteWarehouseButtonActionPerformed

    private void printWarehouseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printWarehouseButtonActionPerformed
        if (updateWarehouse()) {
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                }

                @Override
                protected Void doInBackground() {
                    ReportPrinter printer = new ReportPrinter();
                    printer.printWarehouse(warehouse);
                    return null;
                }
            };
            worker.execute();
        }
    }//GEN-LAST:event_printWarehouseButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addressEditCityLabel;
    private javax.swing.JTextField addressEditCityTextField;
    private javax.swing.JLabel addressEditCountryLabel;
    private javax.swing.JTextField addressEditCountryTextField;
    private javax.swing.JLabel addressEditLabel;
    private javax.swing.JPanel addressEditPanel;
    private javax.swing.JLabel addressEditPostalCodeLabel;
    private javax.swing.JTextField addressEditPostalCodeTextField;
    private javax.swing.JLabel addressEditProvinceLabel;
    private javax.swing.JTextField addressEditProvinceTextField;
    private javax.swing.JLabel addressEditStreetLabel;
    private javax.swing.JTextField addressEditStreetTextField;
    private javax.swing.JButton deleteWarehouseButton;
    private javax.swing.JButton disableWarehouseButton;
    private javax.swing.JLabel emailEditAddressLabel;
    private javax.swing.JTextField emailEditAddressTextField;
    private javax.swing.JPanel emailEditlPanel;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JButton enableWarehouseButton;
    private javax.swing.JLabel generalLabel;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JLabel idLabel;
    private javax.swing.JTextField idTextField;
    private javax.swing.JPanel mainGeneralPanel;
    private javax.swing.JScrollPane mainScrollPane;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JLabel notesLabel;
    private javax.swing.JPanel notesPanel;
    private javax.swing.JScrollPane notesScrollPane;
    private javax.swing.JTextArea notesTextArea;
    private javax.swing.JLabel phoneEditLabel;
    private javax.swing.JLabel phoneEditNumberLabel;
    private javax.swing.JTextField phoneEditNumberTextField;
    private javax.swing.JPanel phoneEditPanel;
    private javax.swing.JButton printWarehouseButton;
    private javax.swing.JButton reloadWarehouseButton;
    private javax.swing.JButton saveWarehouseButton;
    private javax.swing.JToolBar topToolBar;
    // End of variables declaration//GEN-END:variables

    @Override
    public HelpCtx getHelpCtx() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return new HelpCtx("es.regueiro.easyrepair.ui.stock.editor");
    }

    public Warehouse getWarehouse() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return warehouse;
    }

    public void forceClose() {
        modified = false;
        this.close();
    }

    @Override
    public void componentOpened() {
        invalid = false;
        result = WarehouseLookup.getDefault().lookupResult(Warehouse.class);
        result.removeLookupListener(this);

        Collection<? extends Warehouse> WarehouseColId = result.allInstances();
        if (!WarehouseColId.isEmpty()) {
            warehouse = controller.getWarehouseById(WarehouseColId.iterator().next().getId());
            if (warehouse == null) {
                NotifyDescriptor d = new NotifyDescriptor.Message(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_NOT_FOUND"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE")), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                close();
            } else {
                newWarehouse = false;
            }
        } else {
            warehouse = controller.newWarehouse();
            newWarehouse = true;
        }
        if (warehouse != null) {
            reloadWarehouseButton.setEnabled(!newWarehouse);
            deleteWarehouseButton.setEnabled(!newWarehouse);
            enableWarehouseButton.setVisible(false);
            disableWarehouseButton.setEnabled(!newWarehouse);
            controller.setWarehouse(warehouse);
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
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("CHANGES_WILL_BE_LOST"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE")),
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
        if (!SecurityManager.getDefault().isUserLoggedIn() || !SecurityManager.getDefault().userHasPrivilege(Privilege.WAREHOUSE_EDIT)) {
            this.forceClose();
        }
    }

    private void fillData() {
        if (!invalid) {
            clearGeneral();
            clearNotes();

            if (warehouse != null && warehouse.getId() != null) {
                idTextField.setText(warehouse.getId().toString());
                if (warehouse.getName() != null) {
                    nameTextField.setText(warehouse.getName());
                }
                if (warehouse.getNotes() != null) {
                    notesTextArea.setText(warehouse.getNotes());
                }
                if (warehouse.getEnabled()) {
                    enableWarehouseButton.setVisible(false);
                    disableWarehouseButton.setVisible(true);
                } else {
                    enableWarehouseButton.setVisible(true);
                    disableWarehouseButton.setVisible(false);
                }
            }
            fillAddress();
            fillPhone();
            fillEmail();

            manager.discardAllEdits();
            modified = invalid;
            saveWarehouseButton.setEnabled(modified);
            deleteWarehouseButton.setEnabled(!newWarehouse);
            disableWarehouseButton.setEnabled(!newWarehouse);
            reloadWarehouseButton.setEnabled(modified && !newWarehouse);
        }
    }

    private void fillAddress() {
        clearAddress();
        if (warehouse.getAddress() != null) {
            addressEditStreetTextField.setText(warehouse.getAddress().getStreet());
            addressEditProvinceTextField.setText(warehouse.getAddress().getProvince());
            addressEditPostalCodeTextField.setText(warehouse.getAddress().getPostalCode());
            addressEditCityTextField.setText(warehouse.getAddress().getCity());
            addressEditCountryTextField.setText(warehouse.getAddress().getCountry());
        }
    }

    private void fillPhone() {
        clearPhone();
        if (warehouse.getPhone() != null) {
            String formattedPhone;
            PhoneNumber numberProto;
            try {
                numberProto = phoneUtil.parse(warehouse.getPhone().getNumber(), "ES");
            } catch (NumberParseException ex) {
                throw new IllegalArgumentException("This shouldn't happen " + ex.getMessage());
            }
            if (numberProto.getCountryCode() == 34) {
                phoneEditNumberTextField.setText(phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
            } else {
                phoneEditNumberTextField.setText(phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
            }
        }
    }

    private void fillEmail() {
        clearEmail();
        if (warehouse.getEmail() != null) {
            emailEditAddressTextField.setText(warehouse.getEmail().getAddress());
        }
    }

    private void clearGeneral() {
        idTextField.setText("");
        nameTextField.setText("");
    }

    private void clearEmail() {
        emailEditAddressTextField.setText("");
    }

    private void clearPhone() {
        phoneEditNumberTextField.setText("");
    }

    private void clearNotes() {
        notesTextArea.setText("");
    }

    private void clearAddress() {
        addressEditCityTextField.setText("");
        addressEditCountryTextField.setText("");
        addressEditPostalCodeTextField.setText("");
        addressEditProvinceTextField.setText("");
        addressEditStreetTextField.setText("");
    }

    private boolean updateAddress() {
        address = warehouse.getAddress();
        if (address == null) {
            address = new Address();
            address.setLabel("def");
        }
        if (!StringUtils.isBlank(addressEditStreetTextField.getText())
                || !StringUtils.isBlank(addressEditCityTextField.getText())
                || !StringUtils.isBlank(addressEditCountryTextField.getText())
                || !StringUtils.isBlank(addressEditPostalCodeTextField.getText())
                || !StringUtils.isBlank(addressEditProvinceTextField.getText())) {
            address.setStreet(addressEditStreetTextField.getText());
            address.setCity(addressEditCityTextField.getText());
            address.setCountry(addressEditCountryTextField.getText());
            address.setPostalCode(addressEditPostalCodeTextField.getText());
            address.setProvince(addressEditProvinceTextField.getText());

        } else {
            address = null;
        }
        return true;
    }

    private boolean updateEmail() {
        email = warehouse.getEmail();
        if (email == null) {
            email = new Email();
            email.setLabel("def");
        }
        if (!StringUtils.isBlank(emailEditAddressTextField.getText())) {
            try {
                email.setAddress(emailEditAddressTextField.getText());
            } catch (IllegalArgumentException e) {
                NotifyDescriptor d = new NotifyDescriptor.Message(
                        e.getMessage());
                DialogDisplayer.getDefault().notify(d);
                email = null;
                return false;
            }
        } else {
            email = null;
        }
        return true;
    }

    private boolean updatePhone() {
        phone = warehouse.getPhone();


        if (phone == null) {
            phone = new Phone();
            phone.setLabel("def");
        }


        if (!StringUtils.isBlank(phoneEditNumberTextField.getText())) {
            try {
                phone.setNumber(phoneEditNumberTextField.getText());
            } catch (IllegalArgumentException e) {
                NotifyDescriptor d = new NotifyDescriptor.Message(
                        e.getMessage());
                DialogDisplayer.getDefault().notify(d);
                phone = null;
                return false;
            }
        } else {
            phone = null;
        }
        return true;



    }

    private void saveWarehouse() {

        if (updateWarehouse()) {

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
                        controller.setWarehouse(warehouse);
                        controller.saveWarehouse();
                        newWarehouse = false;
                        invalid = false;
                    } catch (Exception e) {
                        if (e.getMessage().contains("was updated or deleted by another user")) {
                            NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("OVERWRITE_DIALOG"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE")),
                                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_MODIFIED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE")));
                            String options[] = new String[2];
                            options[0] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("OVERWRITE");
                            options[1] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RELOAD");
                            d.setOptions(options);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                            if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("OVERWRITE"))) {
                                controller.overwriteWarehouse();
                                warehouse = controller.getWarehouse();
                                invalid = false;
                            } else if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RELOAD"))) {
                                reloadWarehouseButton.doClick();
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

    private boolean updateWarehouse() {

        if (updateAddress() && updateEmail() && updatePhone()) {

            if (!StringUtils.isBlank(nameTextField.getText())) {
                warehouse.setName(nameTextField.getText());

                if (!StringUtils.isBlank(notesTextArea.getText())) {
                    warehouse.setNotes(notesTextArea.getText());
                } else {
                    warehouse.setNotes(null);
                }

                warehouse.setAddress(address);
                warehouse.setEmail(email);
                warehouse.setPhone(phone);

                invalid = false;
                return true;

            } else {
                NotifyDescriptor d = new NotifyDescriptor.Message(
                        java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_NAME_NOT_BLANK"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("WAREHOUSE")), NotifyDescriptor.WARNING_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                invalid = true;
                return false;
            }
        }

        return false;
    }

    private void setupUndo() {
        notesTextArea.getDocument().addUndoableEditListener(manager);
        nameTextField.getDocument().addUndoableEditListener(manager);
        emailEditAddressTextField.getDocument().addUndoableEditListener(manager);
        phoneEditNumberTextField.getDocument().addUndoableEditListener(manager);
        addressEditStreetTextField.getDocument().addUndoableEditListener(manager);
        addressEditCityTextField.getDocument().addUndoableEditListener(manager);
        addressEditCountryTextField.getDocument().addUndoableEditListener(manager);
        addressEditPostalCodeTextField.getDocument().addUndoableEditListener(manager);
        addressEditProvinceTextField.getDocument().addUndoableEditListener(manager);
        //TODO add the rest of the textfields
    }

    private void removeUndo() {
        notesTextArea.getDocument().removeUndoableEditListener(manager);
        nameTextField.getDocument().removeUndoableEditListener(manager);
        emailEditAddressTextField.getDocument().removeUndoableEditListener(manager);
        phoneEditNumberTextField.getDocument().removeUndoableEditListener(manager);
        addressEditStreetTextField.getDocument().removeUndoableEditListener(manager);
        addressEditCityTextField.getDocument().removeUndoableEditListener(manager);
        addressEditCountryTextField.getDocument().removeUndoableEditListener(manager);
        addressEditPostalCodeTextField.getDocument().removeUndoableEditListener(manager);
        addressEditProvinceTextField.getDocument().removeUndoableEditListener(manager);
    }

    private void modify() {
        modified = true;
        saveWarehouseButton.setEnabled(modified);
        reloadWarehouseButton.setEnabled(modified && !newWarehouse);

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
}
