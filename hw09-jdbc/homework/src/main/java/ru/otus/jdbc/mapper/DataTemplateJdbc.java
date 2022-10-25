package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor,
                            EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    T t = entityClassMetaData.getConstructor().newInstance();
                    setFields(rs, t);
                    return t;
                }
                return null;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
    }

    private void setFields(ResultSet rs, T t) {
        try {
            for (Field field : entityClassMetaData.getAllFields()) {
                t
                        .getClass()
                        .getMethod("set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), field.getType())
                        .invoke(t, field.getType().equals(Long.class)
                                ? rs.getLong(field.getName().toLowerCase())
                                : rs.getString(field.getName().toLowerCase()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var result = new ArrayList<T>();
            try {
                while (rs.next()) {
                    T t = entityClassMetaData.getConstructor().newInstance();
                    setFields(rs, t);
                    result.add(t);
                }
                return result;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            List<Object> objects = entityClassMetaData.getFieldsWithoutId()
                    .stream()
                    .map(field -> getValueField(client, field))
                    .toList();
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), objects);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            List<Object> objects = entityClassMetaData.getAllFields()
                    .stream()
                    .map(field -> getValueField(client, field))
                    .toList();
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), objects);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private Object getValueField(T client, Field field) {
        try {
            return client
                    .getClass()
                    .getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1))
                    .invoke(client);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
