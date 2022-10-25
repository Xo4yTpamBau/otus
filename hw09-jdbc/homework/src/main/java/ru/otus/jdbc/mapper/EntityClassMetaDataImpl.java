package ru.otus.jdbc.mapper;

import ru.otus.crm.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    final String name;
    final Constructor<T> constructor;

    Field idField;
    final List<Field> fieldsWithoutId = new ArrayList<>();
    final List<Field> allFields = new ArrayList<>();


    public EntityClassMetaDataImpl(Class<T> type) {
        this.name = type.getSimpleName();
        initFields(type);
        try {
            this.constructor = type.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void initFields(Class<T> entity) {
        for (Field field : entity.getDeclaredFields()) {
            if (field.getAnnotation(Id.class) != null) {
                idField = field;
            } else {
                fieldsWithoutId.add(field);
            }
        }
        allFields.addAll(fieldsWithoutId);
        allFields.add(idField);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
