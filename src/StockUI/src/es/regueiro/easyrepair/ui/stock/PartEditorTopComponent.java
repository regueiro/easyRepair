package es.regueiro.easyrepair.ui.stock;

import es.regueiro.easyrepair.login.SecurityManager;
import es.regueiro.easyrepair.api.stock.controller.PartController;
import es.regueiro.easyrepair.api.stock.controller.WarehouseController;
import es.regueiro.easyrepair.model.shared.Email;
import es.regueiro.easyrepair.model.shared.Phone;
import es.regueiro.easyrepair.model.stock.Part;
import es.regueiro.easyrepair.model.stock.Stock;
import es.regueiro.easyrepair.model.stock.Warehouse;
import es.regueiro.easyrepair.model.user.Privilege;
import es.regueiro.easyrepair.reports.ReportPrinter;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
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
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.*;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.stock//PartEditor//EN",
autostore = false)
@TopComponent.Description(iconBase = "es/regueiro/easyrepair/ui/stock/icons/tire_edit.png", preferredID = "PartEditorTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 15, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.stock.PartEditorTopComponent")
@TopComponent.OpenActionRegistration(displayName = "#CTL_PartEditorAction",
preferredID = "PartEditorTopComponent")
@Messages({
    "CTL_PartEditorAction=Editor de piezas",
    "CTL_PartEditorTopComponent=Editor de piezas",
    "HINT_PartEditorTopComponent=Esta es una ventana del editor de piezas"
})
public final class PartEditorTopComponent extends TopComponent implements LookupListener, DocumentListener, TableModelListener, ListSelectionListener {

    private Part part = null;
    private Lookup.Result<Part> result;
    private InstanceContent content = new InstanceContent();
    private UndoRedo.Manager manager = new UndoRedo.Manager();
    private PartController controller = Lookup.getDefault().lookup(PartController.class);
    private boolean modified = false;
    private boolean newPart = true;
    private boolean invalid = false;
    private PartStockTableModel model = new PartStockTableModel();
    private WarehouseController warehouseController = Lookup.getDefault().lookup(WarehouseController.class);
    private WarehouseBox warehouse;

    public PartEditorTopComponent() {
        initComponents();
        setName(Bundle.CTL_PartEditorTopComponent());
        setToolTipText(Bundle.HINT_PartEditorTopComponent());
        this.setFocusable(true);
        associateLookup(new AbstractLookup(content));
        ActionMap actionMap = getActionMap();
        actionMap.put(DefaultEditorKit.copyAction, new DefaultEditorKit.CopyAction());
        actionMap.put(DefaultEditorKit.cutAction, new DefaultEditorKit.CutAction());
        actionMap.put(DefaultEditorKit.pasteAction, new DefaultEditorKit.PasteAction());
        content.add(actionMap);



        warehouseComboBox.setModel(new SortedComboboxModel());

        fillWarehouseCombobox();
        setupWarehouseComboBox();

        stockTable.setModel(model);


        for (int i = 0; i < stockTable.getColumnCount(); i++) {
            stockTable.getColumnModel().getColumn(i).setCellRenderer(new PartStockTableCellRenderer());
        }


        mainTabbedPane.setIconAt(1, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/stock.png")));
        mainTabbedPane.setIconAt(0, new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/tire.png")));
//        emailEditAddressTextField.getDocument().addUndoableEditListener(manager);


        makeTextField.setDocument(new MaxLengthTextDocument(100));
        modelTextField.setDocument(new MaxLengthTextDocument(100));
        categoryTextField.setDocument(new MaxLengthTextDocument(100));
        priceWithoutIVATextField.setDocument(new MaxLengthTextDocument(10));


        setupUndo();

        makeTextField.getDocument().addDocumentListener(this);
        modelTextField.getDocument().addDocumentListener(this);
        categoryTextField.getDocument().addDocumentListener(this);
        priceWithoutIVATextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculatePriceWithIVA();
                modify();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculatePriceWithIVA();
                modify();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculatePriceWithIVA();
                modify();
            }
        });
        model.addTableModelListener(this);
        stockTable.getSelectionModel().addListSelectionListener(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        mainTabbedPane = new javax.swing.JTabbedPane();
        mainGeneralPanel = new javax.swing.JPanel();
        generalPanel = new javax.swing.JPanel();
        generalLabel = new javax.swing.JLabel();
        idLabel = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();
        makeLabel = new javax.swing.JLabel();
        makeTextField = new javax.swing.JTextField();
        modelLabel = new javax.swing.JLabel();
        modelTextField = new javax.swing.JTextField();
        categoryLabel = new javax.swing.JLabel();
        categoryTextField = new javax.swing.JTextField();
        priceWithoutIVALabel = new javax.swing.JLabel();
        priceWithoutIVATextField = new javax.swing.JTextField();
        priceWithIVATextField = new javax.swing.JTextField();
        priceWithIVALabel = new javax.swing.JLabel();
        notesPanel = new javax.swing.JPanel();
        notesLabel = new javax.swing.JLabel();
        notesScrollPane = new javax.swing.JScrollPane();
        notesTextArea = new javax.swing.JTextArea();
        stockPanel = new javax.swing.JPanel();
        stockLabel = new javax.swing.JLabel();
        tablePanel = new javax.swing.JScrollPane();
        stockTable = new javax.swing.JTable();
        stockButtonPanel = new javax.swing.JPanel();
        addWarehouseButton = new javax.swing.JButton();
        removeSelectedStockButton = new javax.swing.JButton();
        warehouseComboBox = new javax.swing.JComboBox();
        topToolBar = new javax.swing.JToolBar();
        savePartButton = new javax.swing.JButton();
        reloadPartButton = new javax.swing.JButton();
        enablePartButton = new javax.swing.JButton();
        disablePartButton = new javax.swing.JButton();
        deletePartButton = new javax.swing.JButton();
        printPartButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        mainTabbedPane.setDoubleBuffered(true);

        mainGeneralPanel.setLayout(new java.awt.GridBagLayout());

        generalPanel.setLayout(new java.awt.GridBagLayout());

        generalLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        generalLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(generalLabel, org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.generalLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        generalPanel.add(generalLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(idLabel, org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.idLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(makeLabel, org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.makeLabel.text")); // NOI18N
        makeLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(makeLabel, gridBagConstraints);

        makeTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        makeTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(makeTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(modelLabel, org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.modelLabel.text")); // NOI18N
        modelLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(modelLabel, gridBagConstraints);

        modelTextField.setMinimumSize(new java.awt.Dimension(180, 20));
        modelTextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(modelTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(categoryLabel, org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.categoryLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(priceWithoutIVALabel, org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.priceWithoutIVALabel.text")); // NOI18N
        priceWithoutIVALabel.setPreferredSize(new java.awt.Dimension(90, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(priceWithoutIVALabel, gridBagConstraints);

        priceWithoutIVATextField.setMinimumSize(new java.awt.Dimension(180, 20));
        priceWithoutIVATextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(priceWithoutIVATextField, gridBagConstraints);

        priceWithIVATextField.setEditable(false);
        priceWithIVATextField.setMinimumSize(new java.awt.Dimension(180, 20));
        priceWithIVATextField.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(priceWithIVATextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(priceWithIVALabel, org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.priceWithIVALabel.text")); // NOI18N
        priceWithIVALabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(priceWithIVALabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        mainGeneralPanel.add(generalPanel, gridBagConstraints);

        notesPanel.setLayout(new java.awt.GridBagLayout());

        notesLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        notesLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(notesLabel, org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.notesLabel.text")); // NOI18N
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

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.mainGeneralPanel.TabConstraints.tabTitle"), mainGeneralPanel); // NOI18N

        stockPanel.setLayout(new java.awt.GridBagLayout());

        stockLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        stockLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(stockLabel, org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.stockLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        stockPanel.add(stockLabel, gridBagConstraints);

        tablePanel.setMinimumSize(new java.awt.Dimension(100, 100));

        stockTable.setAutoCreateRowSorter(true);
        stockTable.setDoubleBuffered(true);
        stockTable.setFillsViewportHeight(true);
        stockTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        stockTable.setRowHeight(25);
        stockTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        tablePanel.setViewportView(stockTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        stockPanel.add(tablePanel, gridBagConstraints);

        stockButtonPanel.setLayout(new java.awt.GridBagLayout());

        addWarehouseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/add.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(addWarehouseButton, org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.addWarehouseButton.text")); // NOI18N
        addWarehouseButton.setEnabled(false);
        addWarehouseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addWarehouseButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        stockButtonPanel.add(addWarehouseButton, gridBagConstraints);

        removeSelectedStockButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(removeSelectedStockButton, org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.removeSelectedStockButton.text")); // NOI18N
        removeSelectedStockButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSelectedStockButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        stockButtonPanel.add(removeSelectedStockButton, gridBagConstraints);

        warehouseComboBox.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        stockButtonPanel.add(warehouseComboBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        stockPanel.add(stockButtonPanel, gridBagConstraints);

        mainTabbedPane.addTab(org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.stockPanel.TabConstraints.tabTitle"), stockPanel); // NOI18N

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

        savePartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/disk.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(savePartButton, org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.savePartButton.text")); // NOI18N
        savePartButton.setFocusable(false);
        savePartButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        savePartButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        savePartButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        savePartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePartButtonActionPerformed(evt);
            }
        });
        topToolBar.add(savePartButton);

        reloadPartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/arrow_refresh.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(reloadPartButton, org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.reloadPartButton.text")); // NOI18N
        reloadPartButton.setFocusable(false);
        reloadPartButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        reloadPartButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        reloadPartButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        reloadPartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadPartButtonActionPerformed(evt);
            }
        });
        topToolBar.add(reloadPartButton);

        enablePartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/status_online.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(enablePartButton, org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.enablePartButton.text")); // NOI18N
        enablePartButton.setFocusable(false);
        enablePartButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        enablePartButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        enablePartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enablePartButtonActionPerformed(evt);
            }
        });
        topToolBar.add(enablePartButton);

        disablePartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/status_offline.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(disablePartButton, org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.disablePartButton.text")); // NOI18N
        disablePartButton.setFocusable(false);
        disablePartButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        disablePartButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        disablePartButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        disablePartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disablePartButtonActionPerformed(evt);
            }
        });
        topToolBar.add(disablePartButton);

        deletePartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deletePartButton, org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.deletePartButton.text")); // NOI18N
        deletePartButton.setFocusable(false);
        deletePartButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deletePartButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deletePartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePartButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deletePartButton);

        printPartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/stock/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printPartButton, org.openide.util.NbBundle.getMessage(PartEditorTopComponent.class, "PartEditorTopComponent.printPartButton.text")); // NOI18N
        printPartButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printPartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printPartButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printPartButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(topToolBar, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void savePartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savePartButtonActionPerformed
        savePart();
    }//GEN-LAST:event_savePartButtonActionPerformed

    private void disablePartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disablePartButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
//                disablePartButton.setVisible(false);
//                enablePartButton.setVisible(true);
                fillData();
                StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DISABLED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PART")));
            }

            @Override
            protected Void doInBackground() {
                if (updatePart()) {
                    part = controller.disablePart();
                }
                return null;
            }
        };

        worker.execute();

    }//GEN-LAST:event_disablePartButtonActionPerformed

    private void reloadPartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadPartButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SURE_RELOAD_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PART")),
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
                                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PART_RELOAD_NOT_FOUND"),
                                    NotifyDescriptor.ERROR_MESSAGE);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RELOADED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PART")));
                }

                @Override
                protected Boolean doInBackground() {
                    part = controller.reloadPart();
                    if (part != null) {
                        controller.setPart(part);
                        return true;
                    } else {
                        return false;
                    }
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_reloadPartButtonActionPerformed

    private void enablePartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enablePartButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected void done() {
//                disablePartButton.setVisible(true);
//                enablePartButton.setVisible(false);
                fillData();
                StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("ENABLED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PART")));
            }

            @Override
            protected Void doInBackground() {
                if (updatePart()) {
                    part = controller.enablePart();
                }
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_enablePartButtonActionPerformed

    private void deletePartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePartButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PART")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {


            PartLookup.getDefault().clear();

            PartBrowserTopComponent window = (PartBrowserTopComponent) WindowManager.getDefault().findTopComponent("PartBrowserTopComponent");

            window.deletePart(part);
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    forceClose();
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PART")));
                }

                @Override
                protected Void doInBackground() {
                    controller.setPart(part);
                    controller.deletePart();
                    return null;
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_deletePartButtonActionPerformed

    private void addWarehouseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addWarehouseButtonActionPerformed
        Stock st = new Stock();
        st.setWarehouse(warehouse.warehouse);
        st.setUnits(0);
        part.addStock(st);
        model.addStock(st);
        warehouseComboBox.removeItem(warehouse);
        if (warehouseComboBox.getItemCount() == 0) {
            warehouseComboBox.setEnabled(false);
            addWarehouseButton.setEnabled(false);
        }
    }//GEN-LAST:event_addWarehouseButtonActionPerformed

    private void removeSelectedStockButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSelectedStockButtonActionPerformed
        int[] selectedRows = stockTable.getSelectedRows();
        Stock st;
        for (int i = 0; i < selectedRows.length; i++) {
            st = model.getRow(stockTable.convertRowIndexToModel(selectedRows[i] - i));

            part.removeStock(st.getWarehouse().getId());

            WarehouseBox box = new WarehouseBox(st.getWarehouse());
            if (warehouse == null) {
                warehouse = box;
            }
            warehouseComboBox.addItem(box);

            warehouseComboBox.setSelectedItem(box);

            model.removeRow(stockTable.convertRowIndexToModel(selectedRows[i] - i));
        }


        warehouseComboBox.setEnabled(true);
        addWarehouseButton.setEnabled(true);
    }//GEN-LAST:event_removeSelectedStockButtonActionPerformed

    private void printPartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printPartButtonActionPerformed
        if (updatePart()) {
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                }

                @Override
                protected Void doInBackground() {
                    ReportPrinter printer = new ReportPrinter();
                    printer.printPart(part);
                    return null;
                }
            };
            worker.execute();
        }
    }//GEN-LAST:event_printPartButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addWarehouseButton;
    private javax.swing.JLabel categoryLabel;
    private javax.swing.JTextField categoryTextField;
    private javax.swing.JButton deletePartButton;
    private javax.swing.JButton disablePartButton;
    private javax.swing.JButton enablePartButton;
    private javax.swing.JLabel generalLabel;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JLabel idLabel;
    private javax.swing.JTextField idTextField;
    private javax.swing.JPanel mainGeneralPanel;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JLabel makeLabel;
    private javax.swing.JTextField makeTextField;
    private javax.swing.JLabel modelLabel;
    private javax.swing.JTextField modelTextField;
    private javax.swing.JLabel notesLabel;
    private javax.swing.JPanel notesPanel;
    private javax.swing.JScrollPane notesScrollPane;
    private javax.swing.JTextArea notesTextArea;
    private javax.swing.JLabel priceWithIVALabel;
    private javax.swing.JTextField priceWithIVATextField;
    private javax.swing.JLabel priceWithoutIVALabel;
    private javax.swing.JTextField priceWithoutIVATextField;
    private javax.swing.JButton printPartButton;
    private javax.swing.JButton reloadPartButton;
    private javax.swing.JButton removeSelectedStockButton;
    private javax.swing.JButton savePartButton;
    private javax.swing.JPanel stockButtonPanel;
    private javax.swing.JLabel stockLabel;
    private javax.swing.JPanel stockPanel;
    private javax.swing.JTable stockTable;
    private javax.swing.JScrollPane tablePanel;
    private javax.swing.JToolBar topToolBar;
    private javax.swing.JComboBox warehouseComboBox;
    // End of variables declaration//GEN-END:variables

    @Override
    public HelpCtx getHelpCtx() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return new HelpCtx("es.regueiro.easyrepair.ui.stock.editor");
    }

    public Part getPart() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return part;
    }

    public void forceClose() {
        modified = false;
        this.close();
    }

    @Override
    public void componentOpened() {
        invalid = false;
        result = PartLookup.getDefault().lookupResult(Part.class);
        result.removeLookupListener(this);
        mainTabbedPane.setSelectedIndex(0);

        Collection<? extends Part> partColId = result.allInstances();
        if (!partColId.isEmpty()) {
            part = controller.getPartById(partColId.iterator().next().getId());
            if (part == null) {
                NotifyDescriptor d = new NotifyDescriptor.Message(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_NOT_FOUND"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PART")), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                close();
            } else {
                newPart = false;
            }
        } else {
            part = controller.newPart();
            newPart = true;
        }
        if (part != null) {
            reloadPartButton.setEnabled(!newPart);
            deletePartButton.setEnabled(!newPart);
            enablePartButton.setVisible(false);
            disablePartButton.setEnabled(!newPart);
            controller.setPart(part);
            fillData();
            StatusDisplayer.getDefault().setStatusText("");
        }

    }

    @Override
    public void componentClosed() {
        manager.discardAllEdits();
        model.clear();
    }

    @Override
    public void componentActivated() {
        checkPermissions();
    }

    @Override
    public boolean canClose() {
        if (modified) {
            NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("CHANGES_WILL_BE_LOST"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PART")),
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
            makeTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_EDIT));
            modelTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_EDIT));
            categoryTextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_EDIT));
            priceWithoutIVATextField.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_EDIT));
            notesTextArea.setEditable(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_EDIT));

            addWarehouseButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_STOCK_EDIT));
            removeSelectedStockButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_STOCK_EDIT));
            warehouseComboBox.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_STOCK_EDIT));
            stockTable.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.PART_STOCK_EDIT));

        } else {
            this.close();
        }
    }

    private void fillData() {
        if (!invalid) {
            clearGeneral();
            clearNotes();
            clearStockTable();

            if (part != null && part.getId() != null) {
                idTextField.setText(part.getId().toString());
                if (part.getMake() != null) {
                    makeTextField.setText(part.getMake());
                }
                if (part.getModel() != null) {
                    modelTextField.setText(part.getModel());
                }
                if (part.getCategory() != null) {
                    categoryTextField.setText(part.getCategory());
                }
                if (part.getPrice() != null) {
                    priceWithoutIVATextField.setText(part.getPrice().toString());
                }
                if (part.getNotes() != null) {
                    notesTextArea.setText(part.getNotes());
                }
                if (part.getEnabled()) {
                    enablePartButton.setVisible(false);
                    disablePartButton.setVisible(true);
                } else {
                    enablePartButton.setVisible(true);
                    disablePartButton.setVisible(false);
                }
                fillStock();
            }
            fillWarehouseCombobox();
            manager.discardAllEdits();
            modified = invalid;
            savePartButton.setEnabled(modified);
            deletePartButton.setEnabled(!newPart);
            disablePartButton.setEnabled(!newPart);
            reloadPartButton.setEnabled(modified && !newPart);
        }
    }

    private void clearGeneral() {
        idTextField.setText("");
        makeTextField.setText("");
        modelTextField.setText("");
        categoryTextField.setText("");
        priceWithoutIVATextField.setText("");
    }

    private void clearNotes() {
        notesTextArea.setText("");
    }

    private void clearStockTable() {
//        model.removeTableModelListener(this);
        model.fill(null);
//        model.addTableModelListener(this);
    }

    private void fillStock() {
//        clearStockTable();
        model.removeTableModelListener(this);
        if (part.getStock() != null && !part.getStock().isEmpty()) {
            model.fill(part.getStock());
        } else {
            model.fill(null);
        }
        model.addTableModelListener(this);
    }

    private void calculatePriceWithIVA() {
        BigDecimal price;
        BigDecimal iva = new BigDecimal(NbPreferences.root().get("iva", "21")).divide(new BigDecimal("100"));

        if (!StringUtils.isBlank(priceWithoutIVATextField.getText())) {
            try {
                price = new BigDecimal(priceWithoutIVATextField.getText());
            } catch (NumberFormatException e) {
                price = new BigDecimal("0");
            }
        } else {
            price = new BigDecimal("0");
        }

        priceWithIVATextField.setText(formatCurrency(price.add(price.multiply(iva)).setScale(2, RoundingMode.HALF_UP).toPlainString()));

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

    private void savePart() {
        if (updatePart()) {
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
                        controller.setPart(part);
                        controller.savePart();
                        newPart = false;
                        invalid = false;
                    } catch (Exception e) {
                        if (e.getMessage().contains("was updated or deleted by another user")) {
                            NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("OVERWRITE_DIALOG"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PART")),
                                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_MODIFIED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PART")));
                            String options[] = new String[2];
                            options[0] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("OVERWRITE");
                            options[1] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RELOAD");
                            d.setOptions(options);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                            if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("OVERWRITE"))) {
                                controller.overwritePart();
                                part = controller.getPart();
                                invalid = false;
                            } else if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("RELOAD"))) {
                                reloadPartButton.doClick();
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

    private boolean updatePart() {
        if (!StringUtils.isBlank(makeTextField.getText()) && !StringUtils.isBlank(modelTextField.getText())) {
            part.setMake(makeTextField.getText());
            part.setModel(modelTextField.getText());

            if (!StringUtils.isBlank(categoryTextField.getText())) {
                part.setCategory(categoryTextField.getText());
            } else {
                part.setCategory(null);
            }
            if (!StringUtils.isBlank(notesTextArea.getText())) {
                part.setNotes(notesTextArea.getText());
            } else {
                part.setNotes(null);
            }
            if (!StringUtils.isBlank(priceWithoutIVATextField.getText())) {
                try {
                    part.setPrice(priceWithoutIVATextField.getText());
                } catch (IllegalArgumentException e) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(e.getMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    invalid = true;
                    return false;
                }
            } else {
                part.setPrice(null);
            }

            invalid = false;
            return true;
        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("X_NAME_NOT_BLANK"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("PART")), NotifyDescriptor.WARNING_MESSAGE);
            Object retval = DialogDisplayer.getDefault().notify(d);
            invalid = true;
            return false;
        }
    }

    private void setupUndo() {
        notesTextArea.getDocument().addUndoableEditListener(manager);
        makeTextField.getDocument().addUndoableEditListener(manager);
        modelTextField.getDocument().addUndoableEditListener(manager);
        categoryTextField.getDocument().addUndoableEditListener(manager);
        priceWithoutIVATextField.getDocument().addUndoableEditListener(manager);
        //TODO add the rest of the textfields
    }

    private void removeUndo() {
        notesTextArea.getDocument().removeUndoableEditListener(manager);
        makeTextField.getDocument().removeUndoableEditListener(manager);
        modelTextField.getDocument().removeUndoableEditListener(manager);
        categoryTextField.getDocument().removeUndoableEditListener(manager);
        priceWithoutIVATextField.getDocument().removeUndoableEditListener(manager);
    }

    private void modify() {
        modified = true;
        savePartButton.setEnabled(modified);
        reloadPartButton.setEnabled(modified && !newPart);

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

    private void fillWarehouseCombobox() {
        List<Warehouse> listWarehouse = warehouseController.listAll();
        warehouseComboBox.removeAllItems();

        warehouse = null;
        for (Warehouse w : listWarehouse) {
            if (part == null || (part != null && !part.hasStockInWarehouse(w.getId()))) {
                WarehouseBox box = new WarehouseBox(w);
                if (warehouse == null) {
                    warehouse = box;
                }
                warehouseComboBox.addItem(box);
            }

        }
        if (warehouseComboBox.getItemCount() != 0) {
            warehouseComboBox.setEnabled(true);
            addWarehouseButton.setEnabled(true);
            warehouseComboBox.setSelectedIndex(0);
        }

    }

    private void setupWarehouseComboBox() {
        warehouseComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    warehouse = (WarehouseBox) e.getItem();
                }
            }
        });
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        modify();
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            if (stockTable.getSelectedRow() > -1) {
                removeSelectedStockButton.setEnabled(true);
            } else {
                removeSelectedStockButton.setEnabled(false);
            }
        }
    }

    private class WarehouseBox implements Comparable {

        public Warehouse warehouse;
        public String label;

        public WarehouseBox(Warehouse warehouse) {
            this.warehouse = warehouse;
        }

        @Override
        public String toString() {
            String ret = this.warehouse.getId() + " - " + this.warehouse.getName();

            if (!this.warehouse.getEnabled()) {
                ret = ret + " (" + java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/stock/Bundle").getString("DISABLED") + ")";
            }
            return ret;
        }

        @Override
        public int compareTo(Object o) {
            return this.warehouse.getId().compareTo(((WarehouseBox) o).warehouse.getId());
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
