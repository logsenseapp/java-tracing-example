package com.logsense.examples;

import io.undertow.Undertow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.undertow.servlet.Servlets.servlet;

public class CharlieServer {
    private static List<Undertow> buildServers() {
        List<Undertow> list = new ArrayList<>();

        for (int port : Service.C.getPorts()) {
            list.add(new ServerBuilder()
                    .withContextPath(Service.C.getPath())
                    .withPort(port)
                    .withServletClass(CharlieServlet.class)
                    .build());
        }

        return list;
    }
    public static void main(final String[] args) {
        List<Undertow> servers = buildServers();
        ExecutorService executor = Executors.newFixedThreadPool(servers.size());
        for (Undertow u : servers) {
            executor.submit(() -> { u.start(); });
        }
    }
}
