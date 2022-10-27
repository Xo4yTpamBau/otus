package otus.appcontainer;

import otus.appcontainer.api.AppComponent;
import otus.appcontainer.api.AppComponentsContainer;
import otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
            Method[] methods = configClass.getDeclaredMethods();
            for (int order = 0; appComponents.size() != methods.length; order++) {
                for (Method method : methods) {
                    AppComponent annotation = method.getAnnotation(AppComponent.class);
                    if (order == annotation.order()) {
                        Object resultInvokeMethod = invokeMethod(instanceConfigClass, method);
                        appComponents.add(resultInvokeMethod);
                        appComponentsByName.put(annotation.name().toLowerCase(), resultInvokeMethod);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    private Object invokeMethod(Object o, Method method) throws IllegalAccessException, InvocationTargetException {
        Parameter[] parameters = method.getParameters();
        Object resultInvokeMethod;
        if (parameters.length == 0) {
            resultInvokeMethod = method.invoke(o);
        } else {
            Object[] objects = Arrays
                    .stream(parameters)
                    .map(par -> appComponentsByName.get(par.getType().getSimpleName().toLowerCase()))
                    .toArray();
            resultInvokeMethod = method.invoke(o, objects);

        }
        return resultInvokeMethod;
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
                .filter(component ->
                        component.getClass().equals(componentClass)
                                || Arrays.asList(component.getClass().getInterfaces()).contains(componentClass))
                .findFirst()
                .orElseGet(null);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName.toLowerCase());
    }
}
