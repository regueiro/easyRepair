package es.regueiro.easyrepair.login;

/**
 *
 * @author Santi
 */
import es.regueiro.easyrepair.login.SecurityManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.LifecycleManager;
import org.openide.windows.TopComponent;

public class LoginHandler implements ActionListener {

    private static LoginHandler instance = new LoginHandler();
    private static final String MOD2DISABLE = "Modules2Disable"; //NOI18N
    private static final String MOD2ENABLE = "Modules2Enable"; //NOI18N
    private LoginPanel loginPanel;
    private DialogDescriptor dialog = null;
    private LoginLookup lookup = LoginLookup.getDefault();

    private LoginHandler() {
    }

    public static LoginHandler getDefault() {
        return instance;
    }

    private void closeTopComponents() {
        for (TopComponent t : TopComponent.getRegistry().getOpened()) {
            t.close();
        }
    }

    public void showLoginDialog() {
        loginPanel = new LoginPanel();
        loginPanel.reset();
        dialog = new DialogDescriptor(loginPanel, java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/login/Bundle").getString("LOGIN"), true, this);
        dialog.setClosingOptions(new Object[]{});
        dialog.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(DialogDescriptor.PROP_VALUE)
                        && evt.getNewValue().equals(DialogDescriptor.CLOSED_OPTION)) {
                    LifecycleManager.getDefault().exit();
                }
            }
        });
      
        closeTopComponents();
        DialogDisplayer.getDefault().notify(dialog);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == DialogDescriptor.CANCEL_OPTION) {
            LifecycleManager.getDefault().exit();
        } else {
            loginPanel.setInfo("");
            login();
        }
    }

    private void login() {
        if (!SecurityManager.getDefault().login(
                loginPanel.getUsername(), loginPanel.getPassword())) {
            loginPanel.setInfo(java.util.ResourceBundle.getBundle("es/regueiro/easyrepair/login/Bundle").getString("USER_OR_PASS_INCORRECT"));
        } else {
            List<PermissionGroup> groups = SecurityManager.getDefault().getPermissionGroups();

            try {
                PermissionGroupFileSystem.getDefault().setPermissionGroups(groups);
                PermissionGroupModuleSystem.handleModules(
                        MOD2DISABLE, false);
                PermissionGroupModuleSystem.handleModules(
                        MOD2ENABLE, true);
                lookup.setUser(SecurityManager.getDefault().getUser());

                loginPanel.reset();
                
                dialog.setClosingOptions(null);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
