package es.regueiro.easyrepair.login;

import java.io.File;
import org.openide.modules.InstalledFileLocator;
import org.openide.modules.ModuleInstall;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
       WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

            @Override
            public void run() {
                LoginHandler.getDefault().showLoginDialog();
            }
        });
    }

    @Override
    public boolean closing() {
        for (TopComponent t : TopComponent.getRegistry().getOpened()) {
            if (!t.close()) {
                return false;
            }
        }
        return true;
    }
}
