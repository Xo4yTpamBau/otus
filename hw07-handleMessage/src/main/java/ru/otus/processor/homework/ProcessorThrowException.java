package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorThrowException implements Processor {

    private final Processor processor;
    private final int second;

    public ProcessorThrowException(Processor processor, int second) {
        this.processor = processor;
        this.second = second;
    }

    @Override
    public Message process(Message message) {
        if (second % 2 == 0) {
            throw new RuntimeException("Чётная секунда");
        }
        return processor.process(message);
    }
}
