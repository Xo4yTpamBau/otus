package ru.otus;


import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyClass {

    private static Class<?> testClasses = null;

    private static Method beforeMethod = null;
    private static Method afterMethod = null;
    private static final List<Method> testMethods = new ArrayList<>();


    public static void main(String[] args) {
        startTests("ru.otus.test.Tests");
    }

    public static void startTests(String nameClass) {
        try {
            testClasses = Class.forName(nameClass);
            initMethod();
            Integer successTest = runTestMethod();
            System.out.printf("Всего тестов: %d, успешно выполнено: %d, неуспешно завершились: %d%n",
                    testMethods.size(), successTest, (testMethods.size() - successTest));
        } catch (ClassNotFoundException e) {
            System.out.printf("Класс с такми именнем не найден %s%n", nameClass);
        }
    }

    private static void initMethod() {
        Method[] methods = testClasses.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (Test.class.equals(annotation.annotationType())) {
                    testMethods.add(method);
                    break;
                }
                if (After.class.equals(annotation.annotationType())) {
                    afterMethod = method;
                    break;
                }
                if (Before.class.equals(annotation.annotationType())) {
                    beforeMethod = method;
                    break;
                }
            }
        }
    }

    private static Integer runTestMethod() {
        Integer successTest = 0;
        for (Method testMethod : testMethods) {
            Object instant = instantiate(testClasses);
            try {
                if (beforeMethod != null) {
                    callMethod(instant, beforeMethod.getName());
                }
                callMethod(instant, testMethod.getName());
                successTest++;
            } catch (Exception e) {
                System.out.printf("Тест провален: %s%n", testMethod.getName());
            } finally {
                if (afterMethod != null) {
                    callMethod(instant, afterMethod.getName());
                }
            }
        }
        return successTest;
    }

    private static Object callMethod(Object object, String name, Object... args) {
        try {
            var method = object.getClass().getDeclaredMethod(name, toClasses(args));
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.getDeclaredConstructor().newInstance();
            } else {
                Class<?>[] classes = toClasses(args);
                return type.getDeclaredConstructor(classes).newInstance(args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }
}
