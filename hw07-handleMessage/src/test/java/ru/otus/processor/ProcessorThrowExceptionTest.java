package ru.otus.processor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.handler.ComplexProcessor;
import ru.otus.model.Message;
import ru.otus.processor.homework.ProcessorSwapFields;
import ru.otus.processor.homework.ProcessorThrowException;

import java.time.LocalTime;
import java.util.List;

class ProcessorThrowExceptionTest {

    @Test
    void processorTest() {
        //given
        int second = LocalTime.now().getSecond();
        var processors = List.of(
                new ProcessorThrowException(new ProcessorSwapFields(), second),
                new LoggerProcessor(new ProcessorUpperField10()));

        var complexProcessor = new ComplexProcessor(processors, ex -> {
        });

        String field11 = "field11";
        String field12 = "field12";

        var message = new Message.Builder(1L)
                .field10("field10")
                .field11(field11)
                .field12(field12)
                .build();

        var result = complexProcessor.handle(message);

        String expectedValueField11 = second % 2 == 0 ? field11 : field12;
        String expectedValueField12 = second % 2 == 0 ? field12 : field11;
        Assertions.assertEquals(expectedValueField11, result.getField11());
        Assertions.assertEquals(expectedValueField12, result.getField12());
    }
}
