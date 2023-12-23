package org.example.ChatModels;

import java.util.concurrent.atomic.AtomicLong;

public abstract class Chat {
    private long id;
    private static final AtomicLong nextId = new AtomicLong(0L);

    public Chat() {
        generate();
    }

    private synchronized void generate() {
        this.id = nextId.get();
        nextId.set(nextId.get() + 1);
    }

    public long getId() {
        return id;
    }

    public void nextId(long id) {
        nextId.set(id);
    }
}
