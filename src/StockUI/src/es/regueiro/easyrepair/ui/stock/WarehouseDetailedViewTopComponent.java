package es.regueiro.easyrepair.ui.stock;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import es.regueiro.easyrepair.model.stock.Warehouse;
import es.regueiro.easyrepair.model.shared.Address;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.Phone;
import es.regueiro.easyrepair.reports.ReportPrinter;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import javax.swing.SwingWorker;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.stock//WarehouseDetailedView//EN",
autostore = false)
@TopComponent.Description(iconBase = "es/regueiro/easyrepair/ui/stock/icons/factory_detailed.png", preferredID = "WarehouseDetailedViewTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 11, mode = "explorer", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.stock.WarehouseDetailedViewTopComponent")
@TopComponent.OpenActionRegistration(displayName = "#CTL_WarehouseDetailedViewAction",
preferredID = "WarehouseDetailedViewTopComponent")
@Messages({
    "CTL_WarehouseDetailedViewAction=Vista detallada de almacenes",
    "CTL_WarehouseDetailedViewTopComponent=Vista detallada de almacenes",
    "HINT_WarehouseDetailedViewTopComponent=Esta es una ventana de la vista detallada de almacenes"
})
public final class WarehouseDetailedViewTopComponent extends TopComponent implements LookupListener {

    private Lookup.Result<Warehouse> result = null;
    private Warehouse warehouse;
    private PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public WarehouseDetailedViewTopComponent() {
        initComponents();
        setName(Bundle.CTL_WarehouseDetailedViewTopComponent());
        setToolTipText(Bundle.HINT_WarehouseDetailedViewTopComponent());
        this.setFocusable(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        mainScrollPane = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();
        generalPanel = new javax.swing.JPanel();
        generalLabel = new javax.swing.JLabel();
        idLabel = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        printButton = new javax.swing.JButton();
        addressPanel = new javax.swing.JPanel();
        addressStreetLabel = new javax.swing.JLabel();
        addressCityLabel = new javax.swing.JLabel();
        addressProvinceLabel = new javax.swing.JLabel();
        addressPostalCodeLabel = new javax.swing.JLabel();
        addressCountryLabel = new javax.swing.JLabel();
        addressStreetTextField = new javax.swing.JTextField();
        addressCityTextField = new javax.swing.JTextField();
        addressProvinceTextField = new javax.swing.JTextField();
        addressPostalCodeTextField = new javax.swing.JTextField();
        addressCountryTextField = new javax.swing.JTextField();
        addressNotesLabel = new javax.swing.JLabel();
        addressNotesScrollPane = new javax.swing.JScrollPane();
        addressNotesTextArea = new javax.swing.JTextArea();
        addressLabel = new javax.swing.JLabel();
        notesPanel = new javax.swing.JPanel();
        notesLabel = new javax.swing.JLabel();
        notesScrollPane = new javax.swing.JScrollPane();
        notesTextArea = new javax.swing.JTextArea();
        phonePanel = new javax.swing.JPanel();
        phoneNumberLabel = new javax.swing.JLabel();
        phoneNumberTextField = new javax.swing.JTextField();
        phoneNotesLabel = new javax.swing.JLabel();
        phoneNotesScrollPane = new javax.swing.JScrollPane();
        phoneNotesTextArea = new javax.swing.JTextArea();
        phoneLabel = new javax.swing.JLabel();
        emailPanel = new javax.swing.JPanel();
        emailAddressLabel = new javax.swing.JLabel();
        emailAddressTextField = new javax.swing.JTextField();
        emailNotesLabel = new javax.swing.JLabel();
        emailNotesScrollPane = new javax.swing.JScrollPane();
        emailNotesTextArea = new javax.swing.JTextArea();
        emailLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        mainScrollPane.setBorder(null);
        mainScrollPane.setMinimumSize(new java.awt.Dimension(617, 22));

        mainPanel.setLayout(new java.awt.GridBagLayout());

        generalPanel.setLayout(new java.awt.GridBagLayout());

        generalLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        generalLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(generalLabel, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.generalLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        generalPanel.add(generalLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(idLabel, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.idLabel.text")); // NOI18N
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
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(idTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(nameLabel, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.nameLabel.text")); // NOI18N
        nameLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(nameLabel, gridBagConstraints);

        nameTextField.setEditable(false);
        nameTextField.setBackground(new java.awt.Color(232, 231, 231));
        nameTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        nameTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(nameTextField, gridBagConstraints);

        printButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printButton, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.printButton.text")); // NOI18N
        printButton.setEnabled(false);
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(printButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        mainPanel.add(generalPanel, gridBagConstraints);

        addressPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(addressStreetLabel, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.addressStreetLabel.text")); // NOI18N
        addressStreetLabel.setMinimumSize(new java.awt.Dimension(78, 14));
        addressStreetLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressPanel.add(addressStreetLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressCityLabel, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.addressCityLabel.text")); // NOI18N
        addressCityLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressPanel.add(addressCityLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressProvinceLabel, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.addressProvinceLabel.text")); // NOI18N
        addressProvinceLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressPanel.add(addressProvinceLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressPostalCodeLabel, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.addressPostalCodeLabel.text")); // NOI18N
        addressPostalCodeLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressPanel.add(addressPostalCodeLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressCountryLabel, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.addressCountryLabel.text")); // NOI18N
        addressCountryLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressPanel.add(addressCountryLabel, gridBagConstraints);

        addressStreetTextField.setBackground(new java.awt.Color(232, 231, 231));
        addressStreetTextField.setEditable(false);
        addressStreetTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        addressStreetTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressPanel.add(addressStreetTextField, gridBagConstraints);

        addressCityTextField.setBackground(new java.awt.Color(232, 231, 231));
        addressCityTextField.setEditable(false);
        addressCityTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        addressCityTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressPanel.add(addressCityTextField, gridBagConstraints);

        addressProvinceTextField.setBackground(new java.awt.Color(232, 231, 231));
        addressProvinceTextField.setEditable(false);
        addressProvinceTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        addressProvinceTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressPanel.add(addressProvinceTextField, gridBagConstraints);

        addressPostalCodeTextField.setBackground(new java.awt.Color(232, 231, 231));
        addressPostalCodeTextField.setEditable(false);
        addressPostalCodeTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        addressPostalCodeTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressPanel.add(addressPostalCodeTextField, gridBagConstraints);

        addressCountryTextField.setBackground(new java.awt.Color(232, 231, 231));
        addressCountryTextField.setEditable(false);
        addressCountryTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        addressCountryTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressPanel.add(addressCountryTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(addressNotesLabel, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.addressNotesLabel.text")); // NOI18N
        addressNotesLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressPanel.add(addressNotesLabel, gridBagConstraints);

        addressNotesTextArea.setBackground(new java.awt.Color(232, 231, 231));
        addressNotesTextArea.setColumns(20);
        addressNotesTextArea.setEditable(false);
        addressNotesTextArea.setRows(5);
        addressNotesScrollPane.setViewportView(addressNotesTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        addressPanel.add(addressNotesScrollPane, gridBagConstraints);

        addressLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        addressLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(addressLabel, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.addressLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        addressPanel.add(addressLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        mainPanel.add(addressPanel, gridBagConstraints);

        notesPanel.setLayout(new java.awt.GridBagLayout());

        notesLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        notesLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(notesLabel, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.notesLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        notesPanel.add(notesLabel, gridBagConstraints);

        notesTextArea.setBackground(new java.awt.Color(232, 231, 231));
        notesTextArea.setColumns(20);
        notesTextArea.setEditable(false);
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
        mainPanel.add(notesPanel, gridBagConstraints);

        phonePanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(phoneNumberLabel, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.phoneNumberLabel.text")); // NOI18N
        phoneNumberLabel.setMinimumSize(new java.awt.Dimension(78, 14));
        phoneNumberLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phonePanel.add(phoneNumberLabel, gridBagConstraints);

        phoneNumberTextField.setBackground(new java.awt.Color(232, 231, 231));
        phoneNumberTextField.setEditable(false);
        phoneNumberTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        phoneNumberTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phonePanel.add(phoneNumberTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(phoneNotesLabel, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.phoneNotesLabel.text")); // NOI18N
        phoneNotesLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phonePanel.add(phoneNotesLabel, gridBagConstraints);

        phoneNotesTextArea.setBackground(new java.awt.Color(232, 231, 231));
        phoneNotesTextArea.setColumns(20);
        phoneNotesTextArea.setEditable(false);
        phoneNotesTextArea.setRows(5);
        phoneNotesScrollPane.setViewportView(phoneNotesTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        phonePanel.add(phoneNotesScrollPane, gridBagConstraints);

        phoneLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        phoneLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(phoneLabel, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.phoneLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        phonePanel.add(phoneLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        mainPanel.add(phonePanel, gridBagConstraints);

        emailPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(emailAddressLabel, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.emailAddressLabel.text")); // NOI18N
        emailAddressLabel.setMinimumSize(new java.awt.Dimension(78, 14));
        emailAddressLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emailPanel.add(emailAddressLabel, gridBagConstraints);

        emailAddressTextField.setBackground(new java.awt.Color(232, 231, 231));
        emailAddressTextField.setEditable(false);
        emailAddressTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        emailAddressTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emailPanel.add(emailAddressTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(emailNotesLabel, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.emailNotesLabel.text")); // NOI18N
        emailNotesLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emailPanel.add(emailNotesLabel, gridBagConstraints);

        emailNotesTextArea.setBackground(new java.awt.Color(232, 231, 231));
        emailNotesTextArea.setColumns(20);
        emailNotesTextArea.setEditable(false);
        emailNotesTextArea.setRows(5);
        emailNotesScrollPane.setViewportView(emailNotesTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        emailPanel.add(emailNotesScrollPane, gridBagConstraints);

        emailLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        emailLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(emailLabel, org.openide.util.NbBundle.getMessage(WarehouseDetailedViewTopComponent.class, "WarehouseDetailedViewTopComponent.emailLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        emailPanel.add(emailLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        mainPanel.add(emailPanel, gridBagConstraints);

        mainScrollPane.setViewportView(mainPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(mainScrollPane, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
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
    }//GEN-LAST:event_printButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addressCityLabel;
    private javax.swing.JTextField addressCityTextField;
    private javax.swing.JLabel addressCountryLabel;
    private javax.swing.JTextField addressCountryTextField;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JLabel addressNotesLabel;
    private javax.swing.JScrollPane addressNotesScrollPane;
    private javax.swing.JTextArea addressNotesTextArea;
    private javax.swing.JPanel addressPanel;
    private javax.swing.JLabel addressPostalCodeLabel;
    private javax.swing.JTextField addressPostalCodeTextField;
    private javax.swing.JLabel addressProvinceLabel;
    private javax.swing.JTextField addressProvinceTextField;
    private javax.swing.JLabel addressStreetLabel;
    private javax.swing.JTextField addressStreetTextField;
    private javax.swing.JLabel emailAddressLabel;
    private javax.swing.JTextField emailAddressTextField;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JLabel emailNotesLabel;
    private javax.swing.JScrollPane emailNotesScrollPane;
    private javax.swing.JTextArea emailNotesTextArea;
    private javax.swing.JPanel emailPanel;
    private javax.swing.JLabel generalLabel;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JLabel idLabel;
    private javax.swing.JTextField idTextField;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane mainScrollPane;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JLabel notesLabel;
    private javax.swing.JPanel notesPanel;
    private javax.swing.JScrollPane notesScrollPane;
    private javax.swing.JTextArea notesTextArea;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JLabel phoneNotesLabel;
    private javax.swing.JScrollPane phoneNotesScrollPane;
    private javax.swing.JTextArea phoneNotesTextArea;
    private javax.swing.JLabel phoneNumberLabel;
    private javax.swing.JTextField phoneNumberTextField;
    private javax.swing.JPanel phonePanel;
    private javax.swing.JButton printButton;
    // End of variables declaration//GEN-END:variables

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
    public HelpCtx getHelpCtx() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return new HelpCtx("es.regueiro.easyrepair.ui.stock.detailed");
    }

    @Override
    public void componentOpened() {
        result = WarehouseLookup.getDefault().lookupResult(Warehouse.class);
        result.addLookupListener(this);

        fillData();

    }

    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
        result = null;
    }

    @Override
    public void resultChanged(LookupEvent event) {
        fillData();
    }

    private void fillAddress() {
        clearAddress();
        if (warehouse.getAddress() != null) {
            addressStreetTextField.setText(warehouse.getAddress().getStreet());
            addressProvinceTextField.setText(warehouse.getAddress().getProvince());
            addressPostalCodeTextField.setText(warehouse.getAddress().getPostalCode());
            addressCityTextField.setText(warehouse.getAddress().getCity());
            addressCountryTextField.setText(warehouse.getAddress().getCountry());
            addressNotesTextArea.setText(warehouse.getAddress().getNotes());
        }
    }

    private void fillPhone() {
        clearPhone();
        if (warehouse.getPhone() != null) {
            Phonenumber.PhoneNumber numberProto;
            try {
                numberProto = phoneUtil.parse(warehouse.getPhone().getNumber(), "ES");
            } catch (NumberParseException ex) {
                throw new IllegalArgumentException("This shouldn't happen " + ex.getMessage());
            }
            if (numberProto.getCountryCode() == 34) {
                phoneNumberTextField.setText(phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
            } else {
                phoneNumberTextField.setText(phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
            }
            phoneNotesTextArea.setText(warehouse.getPhone().getNotes());
        }
    }

    private void fillEmail() {
        clearEmail();
        if (warehouse.getEmail() != null) {
            emailAddressTextField.setText(warehouse.getEmail().getAddress());
            emailNotesTextArea.setText(warehouse.getEmail().getNotes());
        }
    }

    private void fillData() {
        Collection<? extends Warehouse> warehouseCol = result.allInstances();
        if (!warehouseCol.isEmpty()) {
            Warehouse tempWarehouse = warehouseCol.iterator().next();
            if (warehouse != tempWarehouse) {
                warehouse = tempWarehouse;
                clearGeneral();
                clearNote();

                idTextField.setText(warehouse.getId().toString());
                nameTextField.setText(warehouse.getName());
                notesTextArea.setText(warehouse.getNotes());
                fillAddress();
                fillEmail();
                fillPhone();
                
                printButton.setEnabled(true);
            }
        } else {
//            clear();
        }
    }

    private void clear() {
        warehouse = null;
        clearGeneral();
        clearAddress();
        clearEmail();
        clearPhone();
        clearNote();
    }

    private void clearGeneral() {
        idTextField.setText("");
        nameTextField.setText("");
        
        printButton.setEnabled(false);
    }

    private void clearEmail() {
        emailAddressTextField.setText("");
        emailNotesTextArea.setText("");
    }

    private void clearPhone() {
        phoneNumberTextField.setText("");
        phoneNotesTextArea.setText("");
    }

    private void clearNote() {
        notesTextArea.setText("");
    }

    private void clearAddress() {
        addressCityTextField.setText("");
        addressCountryTextField.setText("");
        addressPostalCodeTextField.setText("");
        addressProvinceTextField.setText("");
        addressStreetTextField.setText("");
        addressNotesTextArea.setText("");
    }
}
