package ru.otus.aop.logger;


public class MyClassImpl implements MyClassInterface {

    @Override
    @Log
    public void secureAccess() {

    }

    @Override
    @Log
    public void secureAccess(String param) {

    }

    @Override
    public void secureAccess(String param, String param2) {
    }

    @Override
    @Log
    public void secureAccess(String param, String param2, String param3) {
    }


    @Override
    public String toString() {
        return "MyClassImpl{}";
    }
}
