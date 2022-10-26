package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> msgHistory = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        msgHistory.put(msg.getId(), msg.toBuilder().build());

    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.of(msgHistory.get(id));
    }
}
