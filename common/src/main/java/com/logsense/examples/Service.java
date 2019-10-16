package com.logsense.examples;

public enum Service {
    A(8081, "/service-a", "HOSTA"),
    B(8082, "/service-b", "HOSTB"),
    C(new int[] { 8083, 8084 }, "/service-c", "HOSTC");

    private int port;
    private int[] ports;
    private String path;
    private String host="localhost";

    private boolean hasSinglePort;
    private int roundRobinIndex=0;

    Service(int port, String path, String env_field) {
        this.port = port;
        this.path = path;
        this.hasSinglePort = true;
        if (System.getenv().containsKey(env_field)) {
            this.host = System.getenv(env_field);
        }
    }

    Service(int[] ports, String path, String env_field) {
        this.ports = ports;
        this.path = path;
        this.hasSinglePort = false;
        if (System.getenv().containsKey(env_field)) {
            this.host = System.getenv(env_field);
        }
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

    public String getHost() {
        return host;
    }
}
