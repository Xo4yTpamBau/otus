package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;

    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл
        try {
            String s = new ObjectMapper().writeValueAsString(data);
            Files.write(Path.of(fileName), s.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
