package ru.otus.aop.logger;

public class ProxyDemo {
    public static void main(String[] args) {
        MyClassInterface myClass = Ioc.createMyClass(new MyClassImpl());
        myClass.secureAccess();
        myClass.secureAccess("1");
        myClass.secureAccess("1","2");
        myClass.secureAccess("1","2", "3");
    }
}



