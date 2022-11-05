package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
import ru.otus.dao.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.services.ClientService;
import ru.otus.services.ClientServiceImpl;
import ru.otus.dao.user.UserDao;
import ru.otus.dao.user.UserDaoImpl;
import ru.otus.dao.repository.DataTemplateHibernate;
import ru.otus.dao.repository.HibernateUtils;
import ru.otus.dao.sessionmanager.TransactionManagerHibernate;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.model.User;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.services.UserAuthService;
import ru.otus.services.UserAuthServiceImpl;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8085;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, User.class, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var userTemplate = new DataTemplateHibernate<>(User.class);
        UserDao userDao = new UserDaoImpl(transactionManager, userTemplate);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        ClientService clientDao = new ClientServiceImpl(transactionManager, clientTemplate);

        userDao.saveUser(new User(null, "admin adminovich", "admin", "admin"));

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, clientDao, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}
