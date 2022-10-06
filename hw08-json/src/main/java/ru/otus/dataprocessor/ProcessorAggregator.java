package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        //группирует выходящий список по name, при этом суммирует поля value
        var result = new LinkedHashMap<String, Double>();
        for (Measurement datum : data) {
            if (!result.containsKey(datum.getName())) {
                result.put(datum.getName(), datum.getValue());
            } else {
                Double curValue = result.get(datum.getName());
                result.replace(datum.getName(), datum.getValue() + curValue);
            }
        }
        return result;
    }
}
