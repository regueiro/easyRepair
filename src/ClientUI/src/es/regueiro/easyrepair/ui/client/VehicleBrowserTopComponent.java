package es.regueiro.easyrepair.ui.client;

import es.regueiro.easyrepair.api.client.controller.VehicleController;
import es.regueiro.easyrepair.login.SecurityManager;
import es.regueiro.easyrepair.model.client.Vehicle;
import es.regueiro.easyrepair.model.user.Privilege;
import es.regueiro.easyrepair.reports.ReportPrinter;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.ActionMap;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.DefaultEditorKit;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
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
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.vehicle//VehicleBrowser//EN",
autostore = false)
@TopComponent.Description(preferredID = "VehicleBrowserTopComponent",
iconBase = "es/regueiro/easyrepair/ui/client/icons/car.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 2, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.client.VehicleBrowserTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Clientes", position = 3),
    @ActionReference(path = "Toolbars/Clientes", position = 3)
})
@TopComponent.OpenActionRegistration(displayName = "#CTL_VehicleBrowserAction",
preferredID = "VehicleBrowserTopComponent")
@Messages({
    "CTL_VehicleBrowserAction=Buscador de vehículos",
    "CTL_VehicleBrowserTopComponent=Buscador de vehículos",
    "HINT_VehicleBrowserTopComponent=Esta es una ventana del buscador de vehículos"
})
public final class VehicleBrowserTopComponent extends TopComponent implements TableModelListener, ListSelectionListener {

    private VehicleTableModel model = new VehicleTableModel();
    private InstanceContent content = new InstanceContent();
    private VehicleController controller = Lookup.getDefault().lookup(VehicleController.class);
    private Vehicle vehicle;
    private VehicleLookup lookup = VehicleLookup.getDefault();
    private ClientLookup clientLookup = ClientLookup.getDefault();
    

    public VehicleBrowserTopComponent() {
        initComponents();
        setName(Bundle.CTL_VehicleBrowserTopComponent());
        setToolTipText(Bundle.HINT_VehicleBrowserTopComponent());
        this.setFocusable(true);

        String[] inCBStrings = {java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("REGISTRATION"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("OWNER_NAME"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("OWNER_NIF"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("VIN"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("MAKE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("MODEL"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("YEAR"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("COLOUR"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("TYPE"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("FUEL"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCE_COMPANY_NAME"),
            java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("INSURANCE_NUMBER")};


        for (String s : inCBStrings) {
            inCombobox.addItem(s);
        }

        // Associate the table model
        vehicleTable.setModel(model);

        for (int i = 0; i < vehicleTable.getColumnCount(); i++) {
            vehicleTable.getColumnModel().getColumn(i).setCellRenderer(new MyTableCellRenderer());
        }



        // Listen for intro on the search bar
        searchEntryField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                doSearch();
            }
        });


        // Activate Copy/Paste
        ActionMap actionMap = getActionMap();
        actionMap.put(DefaultEditorKit.copyAction, new DefaultEditorKit.CopyAction());
        actionMap.put(DefaultEditorKit.cutAction, new DefaultEditorKit.CutAction());
        actionMap.put(DefaultEditorKit.pasteAction, new DefaultEditorKit.PasteAction());
        content.add(actionMap);

        // Listen to the changes on the table
        model.addTableModelListener(this);
        vehicleTable.getSelectionModel().addListSelectionListener(this);

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
        searchEntryField = new javax.swing.JTextField();
        inLabel = new javax.swing.JLabel();
        listAllButton = new javax.swing.JButton();
        inCombobox = new javax.swing.JComboBox();
        includeDisabledCheckBox = new javax.swing.JCheckBox();
        searchButtonPanel = new javax.swing.JPanel();
        findButton = new javax.swing.JButton();
        tablePanel = new javax.swing.JScrollPane();
        vehicleTable = new javax.swing.JTable();
        topToolBar = new javax.swing.JToolBar();
        detailedViewButton = new javax.swing.JButton();
        editVehicleButton = new javax.swing.JButton();
        printSelectedVehicleButton = new javax.swing.JButton();
        printVehicleListButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        searchPanel.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        searchPanel.setLayout(new java.awt.GridBagLayout());

        searchEntryPanel.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(searchLabel, org.openide.util.NbBundle.getMessage(VehicleBrowserTopComponent.class, "VehicleBrowserTopComponent.searchLabel.text")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(inLabel, org.openide.util.NbBundle.getMessage(VehicleBrowserTopComponent.class, "VehicleBrowserTopComponent.inLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchEntryPanel.add(inLabel, gridBagConstraints);

        listAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/group.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(listAllButton, org.openide.util.NbBundle.getMessage(VehicleBrowserTopComponent.class, "VehicleBrowserTopComponent.listAllButton.text")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(includeDisabledCheckBox, org.openide.util.NbBundle.getMessage(VehicleBrowserTopComponent.class, "VehicleBrowserTopComponent.includeDisabledCheckBox.text")); // NOI18N
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
        searchPanel.add(searchEntryPanel, gridBagConstraints);

        searchButtonPanel.setLayout(new java.awt.BorderLayout(5, 5));

        findButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/find32.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(findButton, org.openide.util.NbBundle.getMessage(VehicleBrowserTopComponent.class, "VehicleBrowserTopComponent.findButton.text")); // NOI18N
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

        vehicleTable.setAutoCreateRowSorter(true);
        vehicleTable.setDoubleBuffered(true);
        vehicleTable.setFillsViewportHeight(true);
        vehicleTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        vehicleTable.setRowHeight(25);
        vehicleTable.setSelectionBackground(new java.awt.Color(186, 216, 247));
        vehicleTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablePanel.setViewportView(vehicleTable);

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

        detailedViewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/mechanic_detailed.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(detailedViewButton, org.openide.util.NbBundle.getMessage(VehicleBrowserTopComponent.class, "VehicleBrowserTopComponent.detailedViewButton.text")); // NOI18N
        detailedViewButton.setFocusable(false);
        detailedViewButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        detailedViewButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        detailedViewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailedViewButtonActionPerformed(evt);
            }
        });
        topToolBar.add(detailedViewButton);

        editVehicleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/car_edit.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(editVehicleButton, org.openide.util.NbBundle.getMessage(VehicleBrowserTopComponent.class, "VehicleBrowserTopComponent.editVehicleButton.text")); // NOI18N
        editVehicleButton.setEnabled(false);
        editVehicleButton.setFocusable(false);
        editVehicleButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        editVehicleButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        editVehicleButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        editVehicleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editVehicleButtonActionPerformed(evt);
            }
        });
        topToolBar.add(editVehicleButton);

        printSelectedVehicleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printSelectedVehicleButton, org.openide.util.NbBundle.getMessage(VehicleBrowserTopComponent.class, "VehicleBrowserTopComponent.printSelectedVehicleButton.text")); // NOI18N
        printSelectedVehicleButton.setEnabled(false);
        printSelectedVehicleButton.setFocusable(false);
        printSelectedVehicleButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printSelectedVehicleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSelectedVehicleButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printSelectedVehicleButton);

        printVehicleListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/client/icons/printer.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(printVehicleListButton, org.openide.util.NbBundle.getMessage(VehicleBrowserTopComponent.class, "VehicleBrowserTopComponent.printVehicleListButton.text")); // NOI18N
        printVehicleListButton.setEnabled(false);
        printVehicleListButton.setFocusable(false);
        printVehicleListButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        printVehicleListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printVehicleListButtonActionPerformed(evt);
            }
        });
        topToolBar.add(printVehicleListButton);

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

    private void editVehicleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editVehicleButtonActionPerformed
        openVehicleView();
    }//GEN-LAST:event_editVehicleButtonActionPerformed

    private void detailedViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailedViewButtonActionPerformed
        TopComponent window = WindowManager.getDefault().findTopComponent("VehicleDetailedViewTopComponent");
        if (window == null) {
            window = new VehicleDetailedViewTopComponent();
        }
        window.open();
        window.requestActive();

    }//GEN-LAST:event_detailedViewButtonActionPerformed

    private void printVehicleListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printVehicleListButtonActionPerformed


        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printVehicleList(model.getVehicleList());
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printVehicleListButtonActionPerformed

    private void printSelectedVehicleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSelectedVehicleButtonActionPerformed
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected void done() {
            }

            @Override
            protected Void doInBackground() {
                ReportPrinter printer = new ReportPrinter();
                printer.printVehicle(vehicle);
                return null;
            }
        };
        worker.execute();
    }//GEN-LAST:event_printSelectedVehicleButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton detailedViewButton;
    private javax.swing.JButton editVehicleButton;
    private javax.swing.JButton findButton;
    private javax.swing.JComboBox inCombobox;
    private javax.swing.JLabel inLabel;
    private javax.swing.JCheckBox includeDisabledCheckBox;
    private javax.swing.JButton listAllButton;
    private javax.swing.JButton printSelectedVehicleButton;
    private javax.swing.JButton printVehicleListButton;
    private javax.swing.JPanel searchButtonPanel;
    private javax.swing.JTextField searchEntryField;
    private javax.swing.JPanel searchEntryPanel;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JScrollPane tablePanel;
    private javax.swing.JToolBar topToolBar;
    private javax.swing.JTable vehicleTable;
    // End of variables declaration//GEN-END:variables

    @Override
    public HelpCtx getHelpCtx() {
        //HelpCtx ctx = new HelpCtx("sdk.scene_explorer");
        //this call is for single components:
        return new HelpCtx("es.regueiro.easyrepair.ui.vehicle.browser");
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
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
//            editVehicleButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.VEHICLE_EDIT));
        } else {
            this.close();
        }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            if (vehicleTable.getSelectedRow() > -1) {
                vehicle = model.getRow(vehicleTable.convertRowIndexToModel(vehicleTable.getSelectedRow()));
                editVehicleButton.setEnabled(SecurityManager.getDefault().userHasPrivilege(Privilege.VEHICLE_EDIT));
                printSelectedVehicleButton.setEnabled(true);
                lookup.setVehicle(vehicle);
            } else {
                editVehicleButton.setEnabled(false);
                printSelectedVehicleButton.setEnabled(false);
            }
        }
    }

    private void openVehicleView() {
        clientLookup.setClient(vehicle.getOwner());
        TopComponent window = WindowManager.getDefault().findTopComponent("ClientEditorTopComponent");
        if (window == null) {
            window = new ClientEditorTopComponent();
        } else {
            window.close();
        }
        window.open();
        window.requestActive();
        ((ClientEditorTopComponent) window).viewVehicle(vehicle);
    }

    private void doSearch() {

        printVehicleListButton.setEnabled(false);
        
        final SwingWorker<List<Vehicle>, Void> worker = new SwingWorker<List<Vehicle>, Void>() {

            private int column;
            ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SEARCHING"));

            @Override
            protected void done() {
                try {
                    List<Vehicle> list = get();
                    model.fill(list);

                    if (list != null && !list.isEmpty()) {
                        printVehicleListButton.setEnabled(true);
                    }
                    
                    if (column != 1) {
                        vehicleTable.getRowSorter().toggleSortOrder(column - 1);
                    } else {
                        vehicleTable.getRowSorter().toggleSortOrder(2);
                    }

                    vehicleTable.getRowSorter().toggleSortOrder(column);
                    p.finish();
                } catch (InterruptedException ignore) {
                    System.out.println(ignore.getCause());
                } catch (ExecutionException ignore) {
                    System.out.println(ignore.getCause());
                }
            }

            @Override
            protected List<Vehicle> doInBackground() {
                int selected = inCombobox.getSelectedIndex();
                String toSearch = searchEntryField.getText();
                List<Vehicle> list;
                column = 0;
                if (!toSearch.isEmpty()) {
                    p.setDisplayName(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("SEARCHING_FOR_X"), new Object[]{toSearch}));
                }
                p.start();
                switch (selected) {
                    case 0:
                        list = controller.searchByRegistration(toSearch);
                        column = 1;
                        break;
                    case 1:
                        list = controller.searchByOwnerName(toSearch);
                        column = 1;
                        break;
                    case 2:
                        list = controller.searchByOwnerNif(toSearch);
                        column = 1;
                        break;
                    case 3:
                        list = controller.searchByVin(toSearch);
                        column = 2;
                        break;
                    case 4:
                        list = controller.searchByMake(toSearch);
                        column = 3;
                        break;
                    case 5:
                        list = controller.searchByModel(toSearch);
                        column = 1;
                        break;
                    case 6:
                        list = controller.searchByYear(toSearch);
                        column = 1;
                        break;
                    case 7:
                        list = controller.searchByColour(toSearch);
                        column = 1;
                        break;
                    case 8:
                        list = controller.searchByType(toSearch);
                        column = 1;
                        break;
                    case 9:
                        list = controller.searchByFuel(toSearch);
                        column = 1;
                        break;
                    case 10:
                        list = controller.searchByInsuranceCompanyName(toSearch);
                        column = 1;
                        break;
                    case 11:
                        list = controller.searchByInsuranceNumber(toSearch);
                        column = 1;
                        break;
                    default:
                        //this should never happen
                        list = null;
                }
                
                if (!includeDisabledCheckBox.isSelected()) {
                    Iterator<Vehicle> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Vehicle v = iterator.next();
                        if (!v.getEnabled()) {
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

        printVehicleListButton.setEnabled(false);
        
        final ProgressHandle p = ProgressHandleFactory.createHandle(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("LISTING_ALL_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("VEHICLES")));

        SwingWorker<List<Vehicle>, Void> worker = new SwingWorker<List<Vehicle>, Void>() {

            @Override
            protected void done() {
                try {
                    List<Vehicle> list = get();
                    model.fill(list);
                    
                    if (list != null && !list.isEmpty()) {
                        printVehicleListButton.setEnabled(true);
                    }
                    
                    vehicleTable.getRowSorter().toggleSortOrder(2);
                    vehicleTable.getRowSorter().toggleSortOrder(1);
                    p.finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected List<Vehicle> doInBackground() {
                List<Vehicle> list = controller.listAll();
                
                if (!includeDisabledCheckBox.isSelected()) {
                    Iterator<Vehicle> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Vehicle v = iterator.next();
                        if (!v.getEnabled()) {
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

    public void deleteVehicle(Vehicle veh) {
        if (vehicle != null && veh.getId().equals(vehicle.getId())) {
            vehicle = null;
            StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/client/Bundle").getString("VEHICLE")));
            lookup.clear();

        }

        model.removeVehicle(veh);

    }

    public class MyTableCellRenderer extends DefaultTableCellRenderer {

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
