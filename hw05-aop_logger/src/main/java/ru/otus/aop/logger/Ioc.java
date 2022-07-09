package ru.otus.aop.logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class Ioc {

    private Ioc() {
    }

    static MyClassInterface createMyClass(MyClassInterface classes) {
        InvocationHandler handler = new DemoInvocationHandler(classes);
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
                    .filter(method -> method.isAnnotationPresent(Log.class))
                    .toList();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isLogging(method)) {
                System.out.printf(buildLogString(method, args));
            }
            return method.invoke(myClass, args);
        }

        private boolean isLogging(Method method) {
            for (Method logMethod : logMethods) {
                if (method.getName().equals(logMethod.getName())) {

                    Parameter[] paramsLogMeth = logMethod.getParameters();
                    Parameter[] paramsInvokeMeth = method.getParameters();
                    if (paramsLogMeth.length != paramsInvokeMeth.length) {
                        continue;
                    }
                    if (paramsInvokeMeth.length == 0) {
                        return true;
                    }
                    if (equalsParams(paramsLogMeth, paramsInvokeMeth)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private static boolean equalsParams(Parameter[] paramsLogMeth, Parameter[] paramsInvokeMeth) {
            for (int i = 0; i < paramsLogMeth.length; i++) {
                if (!paramsLogMeth[i].getType().equals(paramsInvokeMeth[i].getType())) {
                    break;
                }
                if (i == paramsLogMeth.length - 1) {
                    return true;
                }
            }
            return false;
        }

        private String buildLogString(Method method, Object[] args) {
            String stringArgs = args == null ? "" :
                    Arrays
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
