package com.logsense.examples;

public enum Service {
    A(8081, "/service-a"),
    B(8082, "/service-b"),
    C(new int[] { 8083, 8084 }, "/service-c");

    private int port;
    private int[] ports;
    private String path;

    private boolean hasSinglePort;
    private int roundRobinIndex=0;

    Service(int port, String path) {
        this.port = port;
        this.path = path;
        this.hasSinglePort = true;
    }

    Service(int[] ports, String path) {
        this.ports = ports;
        this.path = path;
        this.hasSinglePort = false;
    }

    public boolean hasSinglePort() {
        return hasSinglePort;
    }

    public int getPort() {
        if (hasSinglePort) {
            return port;
        } else {
            return getNextPort();
        }
    }

    public int getNextPort() {
        roundRobinIndex++;
        if (roundRobinIndex >= ports.length) {
            roundRobinIndex = 0;
        }
        return this.ports[roundRobinIndex];
    }

    public int[] getPorts() {
        return ports;
    }

    public String getPath() {
        return path;
    }
}
