package ru.otus.dao.client;

import ru.otus.dao.repository.DataTemplate;
import ru.otus.dao.sessionmanager.TransactionManager;
import ru.otus.model.Client;
import ru.otus.model.User;

import java.util.List;

public class ClientDaoImpl implements ClientDao {

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;

    public ClientDaoImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            return clientCloned;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(clientDataTemplate::findAll);
    }


}
