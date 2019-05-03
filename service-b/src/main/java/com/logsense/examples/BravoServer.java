package com.logsense.examples;

import io.undertow.Undertow;

import static io.undertow.servlet.Servlets.servlet;

public class BravoServer {
    public static void main(final String[] args) {
        Undertow server = new ServerBuilder()
                .withService(Service.B)
                .withServletClass(BravoServlet.class)
                .build();
        server.start();
    }
}
