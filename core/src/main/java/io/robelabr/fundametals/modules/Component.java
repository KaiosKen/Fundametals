package io.robelabr.fundametals.modules;

import java.util.*;

public abstract class Component {
    private final Map<Integer,Port> ports;
    private final String name;
    private boolean output;


    public Component(String name) {
        ports = new HashMap<>();
        this.name = name;
    }

    public void addPort(Port port) {
        this.ports.put(port.getID(), port);
    }

    public Port removePort(int id) {
        return this.ports.remove(id);
    }

    public Port getPort(int id) {
        return ports.get(id);
    }
    public String getName() {
        return name;
    }
}
