package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("select * from %s",
                entityClassMetaData.getName().toLowerCase());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("select * from %s where %s = ?",
                entityClassMetaData.getName().toLowerCase(),
                entityClassMetaData.getIdField().getName().toLowerCase());
    }

    @Override
    public String getInsertSql() {
        return String.format("insert into %s(%s) values (%s)",
                entityClassMetaData.getName().toLowerCase(),
                entityClassMetaData.getFieldsWithoutId()
                        .stream()
                        .map(Field::getName)
                        .collect(Collectors.joining(", ")),
                entityClassMetaData.getFieldsWithoutId()
                        .stream()
                        .map(field -> "?")
                        .collect(Collectors.joining(", ")));
    }

    @Override
    public String getUpdateSql() {
        return String.format("update %s set %s = %s where %s = ?",
                entityClassMetaData.getName().toLowerCase(),
                entityClassMetaData.getFieldsWithoutId()
                        .stream()
                        .map(Field::getName)
                        .collect(Collectors.joining(", ")),
                entityClassMetaData.getFieldsWithoutId()
                        .stream()
                        .map(field -> "?")
                        .collect(Collectors.joining(", ")),
                entityClassMetaData.getIdField().getName().toLowerCase());
    }
}
