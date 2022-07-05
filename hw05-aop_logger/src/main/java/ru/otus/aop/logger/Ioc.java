package ru.otus.aop.logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class Ioc {

    private Ioc() {
    }

    static MyClassInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new MyClassImpl());
        return (MyClassInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{MyClassInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final MyClassInterface myClass;
        private final List<Method> logMethods;

        DemoInvocationHandler(MyClassInterface myClass) {
            this.myClass = myClass;
            this.logMethods = getLogMethods();
        }

        private List<Method> getLogMethods() {
            Class<? extends MyClassInterface> aClass = myClass.getClass();
            return Arrays
                    .stream(aClass.getDeclaredMethods())
                    .filter(this::existLogAnnotation)
                    .toList();
        }

        private boolean existLogAnnotation(Method meth) {
            for (Annotation annotation : meth.getAnnotations()) {
                if (Log.class.equals(annotation.annotationType())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isLogging(method, args)) {
                System.out.printf(buildLogString(method, args));
            }
            return method.invoke(myClass, args);
        }

        private boolean isLogging(Method method, Object[] args) {
            for (Method logMethod : logMethods) {
                if (method.getName().equals(logMethod.getName()) &&
                        logMethod.getParameterCount() == args.length) {
                    return true;

                }
            }
            return false;
        }

        private String buildLogString(Method method, Object[] args) {
            String stringArgs = Arrays
                    .stream(args)
                    .map(Objects::toString)
                    .collect(Collectors.joining(","));
            return String.format("executed method: %s, param: %s\n", method.getName(), stringArgs);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }
    }
}
