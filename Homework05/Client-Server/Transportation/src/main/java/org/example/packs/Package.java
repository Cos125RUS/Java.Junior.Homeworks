package org.example.packs;

import org.example.packs.annotations.*;

import java.io.Serial;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

public class Package implements Packable, Serializable, Transportable {

    //    region fields
    @Serial
    private static final long serialVersionUID = 1L;
    @LocalAddress
    private InetAddress localAddress;
    @DestinationAddress
    private InetAddress destinationAddress;
    @PostType
    private String methodType;
    @Availability
    private boolean notNull;
    @PostObjectType
    private ArrayList<String> dataTypes;
    @PostObject
    private ArrayList<Serializable> objects;
// endregion

    @Pack
    @Override
    public <T extends Serializable> void pack(T obj) {

    }

    @Unpack
    @Override
    public <T extends Serializable> T unpack() throws ClassNotFoundException {
        return null;
    }

    public int size() {
        return objects.size();
    }
}
