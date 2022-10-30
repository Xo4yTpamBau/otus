package otus.appcontainer;

import otus.appcontainer.api.AppComponent;
import otus.appcontainer.api.AppComponentsContainer;
import otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        try {
            checkConfigClass(configClass);
            Object instanceConfigClass = configClass.getConstructors()[0].newInstance();

            for (Method method : sortMethodsByOrder(configClass)) {
                AppComponent annotation = method.getAnnotation(AppComponent.class);

                if (appComponentsByName.containsKey(annotation.name())) {
                    throw new RuntimeException(String.format("Bean with this name(%s) already exists", annotation.name()));
                }

                Object resultInvokeMethod = invokeMethod(instanceConfigClass, method);
                appComponents.add(resultInvokeMethod);
                appComponentsByName.put(annotation.name(), resultInvokeMethod);
            }

        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    private static List<Method> sortMethodsByOrder(Class<?> configClass) {
        return Arrays.stream(configClass.getDeclaredMethods())
                .sorted((o1, o2) -> {
                    int order1 = o1.getAnnotation(AppComponent.class).order();
                    int order2 = o2.getAnnotation(AppComponent.class).order();
                    return Integer.compare(order1, order2);
                }).toList();
    }

    private Object invokeMethod(Object o, Method method) throws IllegalAccessException, InvocationTargetException {
        return method.invoke(o, buildArrayArgs(method));
    }

    private Object[] buildArrayArgs(Method method) {
        return Arrays
                .stream(method.getParameters())
                .map(par -> getAppComponent(par.getType()))
                .toArray();
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents
                .stream()
                .filter(component -> componentClass.isAssignableFrom(component.getClass()))
                .findFirst()
                .orElseGet(null);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
