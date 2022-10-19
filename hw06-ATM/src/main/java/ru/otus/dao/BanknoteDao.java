package ru.otus.dao;

import ru.otus.model.Banknote;

import java.util.List;

public interface BanknoteDao {

    void save(Banknote banknote);

    long getAmountAccount();

    List<Banknote> takeMoney(long sum);
}
