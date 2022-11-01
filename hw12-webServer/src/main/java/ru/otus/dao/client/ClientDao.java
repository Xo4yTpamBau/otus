package ru.otus.dao.client;

import ru.otus.model.Client;

import java.util.List;

public interface ClientDao {

    Client saveClient(Client user);

    List<Client> findAll();
}
