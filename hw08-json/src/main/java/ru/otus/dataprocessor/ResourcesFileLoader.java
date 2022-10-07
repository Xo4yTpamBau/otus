package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.Json;
import ru.otus.model.Measurement;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override

    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        try (InputStream resourceAsStream = ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            List<HashMap> list = new ObjectMapper().readValue(resourceAsStream, List.class);

            return list
                    .stream()
                    .map(o -> new Measurement((String) o.get("name"), (Double) o.get("value")))
                    .toList();
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
