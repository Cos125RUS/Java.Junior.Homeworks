package org.example.ChatModels;

import java.util.concurrent.atomic.AtomicLong;

public abstract class Chat {
    private static final AtomicLong nextId = new AtomicLong(0L);
    private long id;
    protected UsersList users;

    public Chat() {
        nextId.set(nextId.get() + 1);
    }

    public Chat(UsersList users) {
        this.users = users;
        generate();
    }

    public Chat(User u1, User u2) {
        this.users = new UsersList();
        users.add(u1);
        users.add(u2);
        generate();
    }

    private synchronized void generate() {
        this.id = nextId.get();
        nextId.set(id + 1);
    }

    public long getId() {
        return id;
    }

    public void nextId(long id) {
        nextId.set(id);
    }

    public UsersList getUsers() {
        return users;
    }

    protected void setId(long id) {
        this.id = id;
    }
}
