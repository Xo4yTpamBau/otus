package ru.otus.dao.user;

import ru.otus.dao.repository.DataTemplate;
import ru.otus.dao.sessionmanager.TransactionManager;
import ru.otus.model.User;

import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private final DataTemplate<User> userDataTemplate;
    private final TransactionManager transactionManager;

    public UserDaoImpl(TransactionManager transactionManager, DataTemplate<User> userDataTemplate) {
        this.transactionManager = transactionManager;
        this.userDataTemplate = userDataTemplate;
    }

    @Override
    public User saveUser(User user) {
        return transactionManager.doInTransaction(session -> {
            var userCloned = user.clone();
            if (user.getId() == null) {
                userDataTemplate.insert(session, userCloned);
                return userCloned;
            }
            userDataTemplate.update(session, userCloned);
            return userCloned;
        });
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return transactionManager
                .doInReadOnlyTransaction(session -> userDataTemplate.findByEntityField(session, "login", login))
                .stream()
                .findFirst();
    }
}
