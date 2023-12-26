package org.example.post;

import org.example.packs.Transportable;

import java.io.IOException;

public interface Sender {
    void send(Transportable transportable) throws IOException;
}
