package com.logsense.examples;

public enum Service {
    A(8081, "/service-a"),
    B(8082, "/service-b"),
    C(8083, "/service-c");

    private int port;
    private String path;

    Service(int port, String path) {
        this.port = port;
        this.path = path;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }
}
