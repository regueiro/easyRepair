package es.regueiro.easyrepair.dao.client.hibernate;

import es.regueiro.easyrepair.api.client.saver.ClientSaver;
import es.regueiro.easyrepair.model.client.Client;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ClientSaver.class,
//path = "ClientFinderServices",
position = -10 //,supersedes = {"es.regueiro.easyrepair.finder.client.DefaultClientSaver"}
)
public class ClientHibernateSaver implements ClientSaver {
    
    private Client client;
    private Transaction t;
    private Session session;
    
    public ClientHibernateSaver() {
    }
    
    @Override
    public void setClient(Client client) {
        this.client = client;
    }
    
    @Override
    public Client getClient() {
        return client;
    }
    
    @Override
    public void saveClient() {
        boolean idSetAuto = false;
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(client);
            if (client.getClientId() == null) {
                client.setClientId(client.getId().toString());
                idSetAuto = true;
            }
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            if (idSetAuto) {
                client.setClientId(null);
            }
            session.close();
            throw e;
        }
    }
    
    @Override
    public void deleteClient() {
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.delete(client);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void disableClient() {
        client.setEnabled(false);
        
        
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(client);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }
    
    @Override
    public void enableClient() {
        client.setEnabled(true);
        
        
        session = Installer.createSession();
        try {
            t = session.beginTransaction();
            session.saveOrUpdate(client);
            t.commit();
            session.close();
        } catch (HibernateException e) {
            t.rollback();
            session.close();
            throw e;
        }
    }

}
