package org.example.post;

import org.example.packs.Transportable;
import org.example.packs.annotations.PostCode;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PostBox {
    private UUID uuid;
    @PostCode
    private int postCode;
    private final Deque<Transportable> mails;

    public PostBox(int postCode) {
        this.postCode = postCode;
        uuid = UUID.randomUUID();
        mails = new LinkedList<>();
    }

    public void add (Transportable transportable) {
        mails.add(transportable);
    }

    public Transportable getFirst() {
        if (!mails.isEmpty())
            return mails.pollFirst();
        else return null;
    }

    public Transportable getLast() {
        if (!mails.isEmpty())
            return mails.pollLast();
        else return null;
    }

    public boolean isEmpty() {
        return mails.isEmpty();
    }

    public Transportable[] toArray() {
        if (!mails.isEmpty())
            return (Transportable[]) mails.toArray();
        else return null;
    }

    public List<Transportable> toList() {
        return mails.stream().toList();
    }

    public int getPostCode() {
        return postCode;
    }
}
