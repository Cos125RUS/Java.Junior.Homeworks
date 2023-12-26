package org.example;

import org.example.packs.Transportable;

import java.io.IOException;

public interface Sender {
    void send(Transportable transportable) throws IOException;
}
