package es.regueiro.easyrepair.api.client.saver;

import es.regueiro.easyrepair.model.client.Client;

public interface ClientSaver {

    public void setClient(Client client);

    public Client getClient();

    public void saveClient();

    public void disableClient();

    public void enableClient();

    public void deleteClient();
}
