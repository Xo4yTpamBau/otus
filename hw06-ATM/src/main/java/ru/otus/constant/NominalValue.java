package ru.otus.constant;


public enum NominalValue {

    FIVE(5),
    TEN(10),
    TWENTY(20),
    FIFTY(50);

    final int value;

    NominalValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static NominalValue getNominalValue(int value) {
        if (NominalValue.FIVE.getValue() == value) {
            return FIVE;
        }
        if (NominalValue.TEN.getValue() == value) {
            return TEN;
        }
        if (NominalValue.TWENTY.getValue() == value) {
            return TWENTY;
        }
        if (NominalValue.FIFTY.getValue() == value) {
            return FIFTY;
        }
        throw new RuntimeException("Какой-то странный номинал");
    }
}
