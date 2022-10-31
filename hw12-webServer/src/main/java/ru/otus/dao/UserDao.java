package ru.otus.dao;

import ru.otus.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    User saveUser(User user);

    Optional<User> findById(long id);

    Optional<User> findByLogin(String login);

    List<User> findAll();
}