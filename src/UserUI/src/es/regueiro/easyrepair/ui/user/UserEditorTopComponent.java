package es.regueiro.easyrepair.ui.user;

import es.regueiro.easyrepair.api.user.controller.RoleController;
import es.regueiro.easyrepair.api.user.controller.UserController;
import es.regueiro.easyrepair.login.SecurityManager;
import es.regueiro.easyrepair.model.user.Privilege;
import es.regueiro.easyrepair.model.user.Role;
import es.regueiro.easyrepair.model.user.User;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
@ConvertAsProperties(dtd = "-//es.regueiro.easyrepair.ui.user//UserEditor//EN",
autostore = false)
@TopComponent.Description(iconBase = "es/regueiro/easyrepair/ui/user/icons/ceo_edit.png", preferredID = "UserEditorTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(position = 27, mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "es.regueiro.easyrepair.ui.user.UserEditorTopComponent")
@TopComponent.OpenActionRegistration(displayName = "#CTL_UserEditorAction",
preferredID = "UserEditorTopComponent")
@Messages({
    "CTL_UserEditorAction=Editor de usuarios",
    "CTL_UserEditorTopComponent=Editor de usuarios",
    "HINT_UserEditorTopComponent=Esta es una ventana del editor de usuarios"
})
public final class UserEditorTopComponent extends TopComponent implements LookupListener, DocumentListener {

    private User user = null;
    private User oldUser = null;
    private Lookup.Result<User> result;
    private InstanceContent content = new InstanceContent();
    private UndoRedo.Manager manager = new UndoRedo.Manager();
    private UserController controller = Lookup.getDefault().lookup(UserController.class);
    private RoleController roleController = Lookup.getDefault().lookup(RoleController.class);
    private boolean modified = false;
    private boolean newUser = true;
    private boolean invalid = false;
    private Role selectedRole = null;

    public UserEditorTopComponent() {
        initComponents();
        setName(Bundle.CTL_UserEditorTopComponent());
        setToolTipText(Bundle.HINT_UserEditorTopComponent());
        this.setFocusable(true);
        associateLookup(new AbstractLookup(content));
        ActionMap actionMap = getActionMap();
        actionMap.put(DefaultEditorKit.copyAction, new DefaultEditorKit.CopyAction());
        actionMap.put(DefaultEditorKit.cutAction, new DefaultEditorKit.CutAction());
        actionMap.put(DefaultEditorKit.pasteAction, new DefaultEditorKit.PasteAction());
        content.add(actionMap);


        nameTextField.setDocument(new MaxLengthTextDocument(100));
        passwordField.setDocument(new MaxLengthTextDocument(100));
        repeatPasswordField.setDocument(new MaxLengthTextDocument(100));
        
        setupUndo();

        nameTextField.getDocument().addDocumentListener(this);
        passwordField.getDocument().addDocumentListener(this);
        repeatPasswordField.getDocument().addDocumentListener(this);
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
        saveUserButton = new javax.swing.JButton();
        reloadUserButton = new javax.swing.JButton();
        deleteUserButton = new javax.swing.JButton();
        generalPanel = new javax.swing.JPanel();
        generalLabel = new javax.swing.JLabel();
        idLabel = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        repeatPasswordLabel = new javax.swing.JLabel();
        repeatPasswordField = new javax.swing.JPasswordField();
        roleLabel = new javax.swing.JLabel();
        roleComboBox = new javax.swing.JComboBox();

        setLayout(new java.awt.GridBagLayout());

        topToolBar.setBorder(null);
        topToolBar.setFloatable(false);
        topToolBar.setRollover(true);

        saveUserButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/user/icons/disk.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(saveUserButton, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.saveUserButton.text")); // NOI18N
        saveUserButton.setFocusable(false);
        saveUserButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        saveUserButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        saveUserButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        saveUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveUserButtonActionPerformed(evt);
            }
        });
        topToolBar.add(saveUserButton);

        reloadUserButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/user/icons/arrow_refresh.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(reloadUserButton, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.reloadUserButton.text")); // NOI18N
        reloadUserButton.setFocusable(false);
        reloadUserButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        reloadUserButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        reloadUserButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        reloadUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadUserButtonActionPerformed(evt);
            }
        });
        topToolBar.add(reloadUserButton);

        deleteUserButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/regueiro/easyrepair/ui/user/icons/cross.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(deleteUserButton, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.deleteUserButton.text")); // NOI18N
        deleteUserButton.setFocusable(false);
        deleteUserButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deleteUserButton.setMargin(new java.awt.Insets(5, 5, 5, 5));
        deleteUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteUserButtonActionPerformed(evt);
            }
        });
        topToolBar.add(deleteUserButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(topToolBar, gridBagConstraints);

        generalPanel.setLayout(new java.awt.GridBagLayout());

        generalLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        generalLabel.setForeground(new java.awt.Color(82, 107, 134));
        org.openide.awt.Mnemonics.setLocalizedText(generalLabel, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.generalLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        generalPanel.add(generalLabel, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(idLabel, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.idLabel.text")); // NOI18N
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(idTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(nameLabel, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.nameLabel.text")); // NOI18N
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(nameTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(passwordLabel, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.passwordLabel.text")); // NOI18N
        passwordLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(passwordLabel, gridBagConstraints);

        passwordField.setText(org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.passwordField.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(passwordField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(repeatPasswordLabel, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.repeatPasswordLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(repeatPasswordLabel, gridBagConstraints);

        repeatPasswordField.setText(org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.repeatPasswordField.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(repeatPasswordField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(roleLabel, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.roleLabel.text")); // NOI18N
        roleLabel.setPreferredSize(new java.awt.Dimension(78, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(roleLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        generalPanel.add(roleComboBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(generalPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void saveUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveUserButtonActionPerformed
        saveUser();
    }//GEN-LAST:event_saveUserButtonActionPerformed

    private void reloadUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadUserButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("SURE_RELOAD_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("USER")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("RELOAD"), NotifyDescriptor.YES_NO_OPTION);

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
                                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("USER_RELOAD_NOT_FOUND"),
                                    NotifyDescriptor.ERROR_MESSAGE);
                            Object retval = DialogDisplayer.getDefault().notify(d);
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("RELOADED_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("USER")));
                }

                @Override
                protected Boolean doInBackground() {
                    user = controller.reloadUser();
                    if (user != null) {
                        controller.setUser(user);
                        return true;
                    } else {
                        return false;
                    }
                }
            };

            worker.execute();

        }
    }//GEN-LAST:event_reloadUserButtonActionPerformed

    private void deleteUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteUserButtonActionPerformed
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("SURE_DELETE_X"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("USER")),
                java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("DELETE"), NotifyDescriptor.YES_NO_OPTION);

        Object retval = DialogDisplayer.getDefault().notify(d);

        if (retval == NotifyDescriptor.YES_OPTION) {


            UserLookup.getDefault().clear();

            UserBrowserTopComponent window = (UserBrowserTopComponent) WindowManager.getDefault().findTopComponent("UserBrowserTopComponent");

            window.deleteUser(user);
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected void done() {
                    forceClose();
                    StatusDisplayer.getDefault().setStatusText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("X_DELETED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("USER")));
                }

                @Override
                protected Void doInBackground() {
                    controller.setUser(user);
                    controller.deleteUser();
                    return null;
                }
            };


            worker.execute();

        }
    }//GEN-LAST:event_deleteUserButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteUserButton;
    private javax.swing.JLabel generalLabel;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JLabel idLabel;
    private javax.swing.JTextField idTextField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JButton reloadUserButton;
    private javax.swing.JPasswordField repeatPasswordField;
    private javax.swing.JLabel repeatPasswordLabel;
    private javax.swing.JComboBox roleComboBox;
    private javax.swing.JLabel roleLabel;
    private javax.swing.JButton saveUserButton;
    private javax.swing.JToolBar topToolBar;
    // End of variables declaration//GEN-END:variables

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("es.regueiro.easyrepair.ui.user.editor");
    }

    public User getUser() {
        return user;
    }

    public void forceClose() {
        modified = false;
        this.close();
    }

    @Override
    public void componentOpened() {
        invalid = false;
        result = UserLookup.getDefault().lookupResult(User.class);
        result.removeLookupListener(this);

        setupRoleComboBox();

        Collection<? extends User> userColId = result.allInstances();
        if (!userColId.isEmpty()) {
            user = controller.getUserById(userColId.iterator().next().getId());
            if (user == null) {
                NotifyDescriptor d = new NotifyDescriptor.Message(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("X_NOT_FOUND"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("USER")), NotifyDescriptor.ERROR_MESSAGE);
                Object retval = DialogDisplayer.getDefault().notify(d);
                close();
            } else {
                newUser = false;
            }
        } else {
            user = controller.newUser();
            newUser = true;
        }
        if (user != null) {
            reloadUserButton.setEnabled(!newUser);
            deleteUserButton.setEnabled(!newUser);
            controller.setUser(user);
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
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("CHANGES_WILL_BE_LOST"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("USER")),
                    java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("CLOSE"), NotifyDescriptor.YES_NO_OPTION);

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
        if (!SecurityManager.getDefault().isUserLoggedIn() || !SecurityManager.getDefault().userHasPrivilege(Privilege.USER_EDIT)) {
            this.forceClose();
        }
    }

    private void fillData() {
        if (!invalid) {
            clearGeneral();
            clearRole();

            if (user != null && user.getId() != null) {
                idTextField.setText(user.getId().toString());

                if (user.getName() != null) {
                    nameTextField.setText(user.getName());
                }
            }

            fillRole();
            manager.discardAllEdits();
            modified = invalid;
            saveUserButton.setEnabled(modified);
            deleteUserButton.setEnabled(!newUser);
            reloadUserButton.setEnabled(modified && !newUser);
        }
    }

    private void fillRole() {
        clearRole();
        if (user.getRole() != null) {
            for (int i = 0; i < roleComboBox.getItemCount(); i++) {
                if (((RoleBox) roleComboBox.getItemAt(i)).role.equals(user.getRole())) {
                    roleComboBox.setSelectedIndex(i);
                }
            }
        }
    }

    private void clearGeneral() {
        idTextField.setText("");
        nameTextField.setText("");
        passwordField.setText("");
        repeatPasswordField.setText("");
    }

    private void clearRole() {
        roleComboBox.setSelectedIndex(0);
    }

    private void saveUser() {
        if (!StringUtils.isBlank(nameTextField.getText())) {
            user.setName(nameTextField.getText());
            user.setRole(selectedRole);
            if (passwordField.getPassword().length != 0 || !StringUtils.isBlank(user.getPassword())) {
                if (Arrays.equals(passwordField.getPassword(), repeatPasswordField.getPassword())) {
                    String pass = new String(passwordField.getPassword());
                    if (!StringUtils.isBlank(pass)) {
                        user.setPassword(pass);
                    }
                    pass = null;

                    final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                        ProgressHandle p = ProgressHandleFactory.createHandle(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("SAVING"));

                        @Override
                        protected void done() {
                            fillData();
                            p.finish();
                        }

                        @Override
                        protected Void doInBackground() {
                            p.start();
                            try {
                                controller.setUser(user);
                                controller.saveUser();
                                newUser = false;
                                invalid = false;
                            } catch (Exception e) {
                                if (e.getMessage().contains("was updated or deleted by another user")) {
                                    NotifyDescriptor d = new NotifyDescriptor.Confirmation(
                                            java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("OVERWRITE_DIALOG"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("USER")),
                                            java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("X_MODIFIED"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("USER")));
                                    String options[] = new String[2];
                                    options[0] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("OVERWRITE");
                                    options[1] = java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("RELOAD");
                                    d.setOptions(options);
                                    Object retval = DialogDisplayer.getDefault().notify(d);
                                    if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("OVERWRITE"))) {
                                        controller.overwriteUser();
                                        user = controller.getUser();
                                        invalid = false;
                                    } else if (retval.equals(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("RELOAD"))) {
                                        reloadUserButton.doClick();
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


                } else {
                    NotifyDescriptor d = new NotifyDescriptor.Message(
                            java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("X_NOT_MATCH"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("PASSWORDS")), NotifyDescriptor.WARNING_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    invalid = true;
                }
            } else {
                if (user.getPassword() == null) {
                    NotifyDescriptor d = new NotifyDescriptor.Message(
                            java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("X_NOT_BLANK"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("PASSWORD")), NotifyDescriptor.WARNING_MESSAGE);
                    Object retval = DialogDisplayer.getDefault().notify(d);
                    invalid = true;
                }
            }

        } else {
            NotifyDescriptor d = new NotifyDescriptor.Message(
                    java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("X_NAME_NOT_BLANK"), java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/ui/user/Bundle").getString("USER")), NotifyDescriptor.WARNING_MESSAGE);
            Object retval = DialogDisplayer.getDefault().notify(d);
            invalid = true;
        }
    }

    private void setupRoleComboBox() {
        roleComboBox.removeAllItems();

        List<Role> roleList = roleController.listAll();
        for (Role r : roleList) {
            RoleBox box = new RoleBox(r.getName(), r);
            roleComboBox.addItem(box);
        }

        for (ItemListener i : roleComboBox.getItemListeners()) {
            roleComboBox.removeItemListener(i);
        }
        roleComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    RoleBox role = (RoleBox) e.getItem();

                    if (role != null && role.role != null) {
                        selectedRole = role.role;
                    }
                    modify();
                }
            }
        });

        selectedRole = ((RoleBox) roleComboBox.getItemAt(0)).role;
        roleComboBox.setSelectedIndex(0);
    }

    private void setupUndo() {
        nameTextField.getDocument().addUndoableEditListener(manager);
        passwordField.getDocument().addUndoableEditListener(manager);
        repeatPasswordField.getDocument().addUndoableEditListener(manager);
    }

    private void removeUndo() {
        nameTextField.getDocument().removeUndoableEditListener(manager);
        passwordField.getDocument().removeUndoableEditListener(manager);
        repeatPasswordField.getDocument().removeUndoableEditListener(manager);
    }

    private void modify() {
        modified = true;
        saveUserButton.setEnabled(modified);
        reloadUserButton.setEnabled(modified && !newUser);

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

    private class RoleBox {

        public Role role;
        public String name;

        public RoleBox(String name, Role role) {
            this.name = name;
            this.role = role;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
