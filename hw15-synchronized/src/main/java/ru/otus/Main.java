package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private final Object monitor = new Object();
    private int idCurrentThread = 1;

    private boolean directOrder = true;


    public static void main(String[] args) {
        new Main().go();
    }

    private void go() {
        Thread thread1 = new Thread(this::printSequence);
        thread1.setName("1");
        Thread thread2 = new Thread(this::printSequence);
        thread2.setName("2");

        thread1.start();
        thread2.start();
    }

    private void printSequence() {
        try {
            for (int i = 1; i > 0; i = incrementValue(i)) {
                synchronized (monitor) {
                    String nameThread = Thread.currentThread().getName();
                    if (!nameThread.equals(String.valueOf(idCurrentThread))) {
                        monitor.wait();
                    }
                    logger.info("{}", i);
                    swapThread();
                    monitor.notifyAll();
                }
            }
        } catch (Exception e) {
            logger.error("error");
        }
    }

    private int incrementValue(int currentValue) {
        if (directOrder && currentValue >= 10) {
            directOrder = false;
        }
        return directOrder ? currentValue + 1 : currentValue - 1;
    }

    private void swapThread() {
        idCurrentThread = idCurrentThread == 1 ? 2 : 1;
    }
}