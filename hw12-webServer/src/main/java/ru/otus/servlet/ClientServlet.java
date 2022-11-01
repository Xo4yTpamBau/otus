package ru.otus.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.dao.client.ClientDao;
import ru.otus.model.Client;
import ru.otus.model.ClientDto;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ClientServlet extends HttpServlet {

    private final ClientDao clientDao;
    private final TemplateProcessor templateProcessor;
    private final Gson gson;

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_ALL_CLIENTS = "clients";


    public ClientServlet(TemplateProcessor templateProcessor, ClientDao clientDao) {
        this.templateProcessor = templateProcessor;
        this.clientDao = clientDao;
        this.gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(TEMPLATE_ATTR_ALL_CLIENTS, clientDao.findAll().stream().map(ClientDto::new).toList());
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ClientDto client = gson.fromJson(req.getReader(), ClientDto.class);
        clientDao.saveClient(new Client(client));
    }

}
