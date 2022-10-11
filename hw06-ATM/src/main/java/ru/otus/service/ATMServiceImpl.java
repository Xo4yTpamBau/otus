package ru.otus.service;

import ru.otus.constant.NominalValue;
import ru.otus.dao.RepositoryBanknote;
import ru.otus.dao.RepositoryBanknoteImpl;
import ru.otus.model.Banknote;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ATMServiceImpl implements ATMService {

    private final Scanner scan = new Scanner(System.in);
    private final RepositoryBanknote repositoryBanknote = new RepositoryBanknoteImpl();

    @Override
    public void startSession() {
        while (true) {
            System.out.println("\nКакую операцию вы бы хотели выполнить?");
            System.out.println("1 - Пополнить счёт.");
            System.out.println("2 - Узнать остаток на счёте.");
            System.out.println("3 - Снять со счёта.");
            System.out.println("4 - Завершить.");
            int operation = scan.nextInt();
            if (operation > 4) {
                System.out.println("\nНекорректный ввод, повторите попытку.");
                continue;
            }
            if (operation == 4) {
                System.out.println("\nСессия завершена.");
                break;
            }
            if (operation == 1) {
                introductionBanknotes();
                continue;
            }
            if (operation == 2) {
                getAmountAccount();
                continue;
            }
            if (operation == 3) {
                takeMoney();
            }
        }
    }



    private void introductionBanknotes() {
        while (true) {
            System.out.println("\nВнесите купюру (банкомат принимает номиналы: 5, 10, 20, 50).");
            System.out.println("0 - Завершить.");
            int banknote = scan.nextInt();
            if (banknote == 0) {
                return;
            }
            if (isValidBanknote(banknote)) {
                repositoryBanknote.save(new Banknote(banknote));
                break;
            }
            System.out.println("\nНеверный номинал валюты. Попробуйте ещё раз.");
        }

        System.out.println("\nХотите повторить операцию?");
        System.out.println("1 - да.");
        System.out.println("Любой другой символ - нет.");

        int repeatOperation = scan.nextInt();
        if (repeatOperation == 1) {
            introductionBanknotes();
        }
    }

    private void getAmountAccount() {
        System.out.println("\nОстаток на счёте: " + repositoryBanknote.getAmountAccount());
    }

    private void takeMoney() {
        while (true){
            System.out.println("\nВведите сумму, которую хотите снять со счета.");
            System.out.println("0 - Завершить.");
            int countMoney = scan.nextInt();
            if (countMoney == 0){
                break;
            }
            if (countMoney < 0){
                System.out.println("\nСумма должна быть больше 0. Попробуйте ещё раз.");
                continue;
            }
            List<Banknote> banknotes = repositoryBanknote.takeMoney(countMoney);
            if (banknotes.isEmpty()){
                System.out.println("\nНельзя выдать такую сумму. Попробуйте ещё раз.");
                continue;
            }
            System.out.println(banknotes);
            break;
        }
    }

    private boolean isValidBanknote(int banknote) {
        return Arrays.stream(NominalValue.values())
                .map(NominalValue::getValue)
                .anyMatch(o -> o == banknote);
    }

}
