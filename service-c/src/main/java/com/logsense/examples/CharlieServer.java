package com.logsense.examples;

import io.undertow.Undertow;

import static io.undertow.servlet.Servlets.servlet;

public class CharlieServer {
    public static void main(final String[] args) {
        Undertow server = new ServerBuilder()
                .withService(Service.C)
                .withServletClass(CharlieServlet.class)
                .build();
        server.start();
    }
}
