package ru.otus.model;

import ru.otus.constant.NominalValue;

public class Banknote {

    private NominalValue nominal;

    public Banknote(NominalValue nominal) {
        this.nominal = nominal;
    }

    public Banknote(int banknote) {
        this.nominal = NominalValue.getNominalValue(banknote);
    }

    public NominalValue getNominal() {
        return nominal;
    }

    public void setNominal(NominalValue nominal) {
        this.nominal = nominal;
    }

    @Override
    public String toString() {
        return "Banknote{" +
                "nominal=" + nominal +
                '}';
    }
}
