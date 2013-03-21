package es.regueiro.easyrepair.dao.repair.hibernate;

import es.regueiro.easyrepair.model.client.Client;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class MockClientSaver {
    
    private Client client;
    private Transaction t;
    private Session session;
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    public Client getClient() {
        return client;
    }
    
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
}
