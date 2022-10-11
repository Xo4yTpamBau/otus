package ru.otus;

import ru.otus.service.ATMService;
import ru.otus.service.ATMServiceImpl;

public class ATMSimulator {
    private static final ATMService atmService = new ATMServiceImpl();

    public static void main(String[] args) {
        atmService.startSession();
    }


}