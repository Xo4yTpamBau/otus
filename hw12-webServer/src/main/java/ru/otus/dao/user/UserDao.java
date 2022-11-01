package ru.otus.dao.user;

import ru.otus.model.User;

import java.util.Optional;

public interface UserDao {

    User saveUser(User user);

    Optional<User> findByLogin(String login);


}