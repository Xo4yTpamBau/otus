package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

import java.io.File;
import java.io.IOException;
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
        try {
            List<HashMap> list = new ObjectMapper().readValue(new File(fileName), List.class);

            return list
                    .stream()
                    .map(o -> new Measurement((String) o.get("name"), (Double) o.get("value")))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
