package org.example.post;

import org.example.packs.Transportable;

public interface Stored {
    <T extends Transportable> T get();
}
