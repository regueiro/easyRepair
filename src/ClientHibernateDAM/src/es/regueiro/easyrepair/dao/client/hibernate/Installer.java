package es.regueiro.easyrepair.dao.client.hibernate;

import java.io.File;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.openide.modules.InstalledFileLocator;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    private static final SessionFactory sessionFactory;
    private static final ServiceRegistry serviceRegistry;

    static {
        try {
            // Buscamos el archivo de configuración de hibernate
            File cfgFile = InstalledFileLocator.getDefault().locate("config/hibernate.cfg.xml", "es.regueiro.easyrepair.persistency", false);

            Configuration cfg = new Configuration();
            
            // Si existe se utiliza. Si no, se utiliza el suminsitrado por defecto.
            if (cfgFile != null) {
                cfg.configure(cfgFile);
            } else {
                cfg.configure();
            }

            cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Client_Phone.hbm.xml");
            cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Client_Email.hbm.xml");
            cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Client_Address.hbm.xml");
            cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Client.hbm.xml");
            cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Insurance_Company_Phone.hbm.xml");
            cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Insurance_Company_Email.hbm.xml");
            cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Insurance_Company_Address.hbm.xml");
            cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Insurance_Company.hbm.xml");
            cfg.addResource("es/regueiro/easyrepair/dao/client/hibernate/Vehicle.hbm.xml");

            serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
            sessionFactory = cfg.buildSessionFactory(serviceRegistry);

        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session createSession() {
        return sessionFactory.openSession();
    }

    public static Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void close() {
        sessionFactory.close();
    }
}