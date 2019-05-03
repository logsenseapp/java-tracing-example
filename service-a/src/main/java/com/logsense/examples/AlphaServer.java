package com.logsense.examples;

import io.undertow.Undertow;

public class AlphaServer {
    public static void main(final String[] args) {
        Undertow server = new ServerBuilder()
                .withService(Service.A)
                .withServletClass(AlphaServlet.class)
                .build();
        server.start();
    }
}
