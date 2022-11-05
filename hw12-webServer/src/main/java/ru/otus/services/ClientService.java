package ru.otus.services;

import ru.otus.model.Client;

import java.util.List;

public interface ClientService {

    Client saveClient(Client user);

    List<Client> findAll();
}
