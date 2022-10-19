package ru.otus.dao;

import ru.otus.constant.NominalValue;
import ru.otus.model.Banknote;

import java.util.ArrayList;
import java.util.List;

public class BanknoteDaoImpl implements BanknoteDao {

    private final List<Banknote> fiveBanknotes = new ArrayList<>();
    private final List<Banknote> tenBanknotes = new ArrayList<>();
    private final List<Banknote> twentyBanknotes = new ArrayList<>();
    private final List<Banknote> fiftyBanknotes = new ArrayList<>();


    @Override
    public void save(Banknote banknote) {
        if (NominalValue.FIVE.getValue() == banknote.getNominal().getValue()) {
            fiveBanknotes.add(banknote);
        }
        if (NominalValue.TEN.getValue() == banknote.getNominal().getValue()) {
            tenBanknotes.add(banknote);
        }
        if (NominalValue.TWENTY.getValue() == banknote.getNominal().getValue()) {
            twentyBanknotes.add(banknote);
        }
        if (NominalValue.FIFTY.getValue() == banknote.getNominal().getValue()) {
            fiftyBanknotes.add(banknote);
        }
    }

    @Override
    public long getAmountAccount() {
        return sumCell(fiveBanknotes) + sumCell(tenBanknotes) + sumCell(twentyBanknotes) + sumCell(fiftyBanknotes);
    }

    @Override
    public List<Banknote> takeMoney(long sum) {
        var result = new ArrayList<Banknote>();
        while (true) {
            if (NominalValue.FIFTY.getValue() <= sum && !fiftyBanknotes.isEmpty()) {
                result.add(fiftyBanknotes.get(0));
                fiftyBanknotes.remove(0);
                sum = sum - NominalValue.FIFTY.getValue();
                continue;
            }
            if (NominalValue.TWENTY.getValue() <= sum && !twentyBanknotes.isEmpty()) {
                result.add(twentyBanknotes.get(0));
                twentyBanknotes.remove(0);
                sum = sum - NominalValue.TWENTY.getValue();
                continue;
            }
            if (NominalValue.TEN.getValue() <= sum && !tenBanknotes.isEmpty()) {
                result.add(tenBanknotes.get(0));
                tenBanknotes.remove(0);
                sum = sum - NominalValue.TEN.getValue();
                continue;
            }
            if (NominalValue.FIVE.getValue() <= sum && !fiveBanknotes.isEmpty()) {
                result.add(fiveBanknotes.get(0));
                fiveBanknotes.remove(0);
                sum = sum - NominalValue.FIVE.getValue();
                continue;
            }
            if (sum != 0) {
                result.forEach(this::save);
                return new ArrayList<>();
            }
            break;
        }
        return result;
    }


    private Integer sumCell(List<Banknote> banknotes) {
        return banknotes
                .stream()
                .map(banknote -> banknote.getNominal().getValue())
                .reduce(0, Integer::sum);
    }

}
