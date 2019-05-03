package com.logsense.examples;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.security.api.AuthenticationMode;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import static io.undertow.servlet.Servlets.*;

public class ServerBuilder {
    private String contextPath = null;
    private Class<? extends Servlet> servletClass = null;
    private Integer port = null;

    public ServerBuilder withService(Service service) {
        return this.withContextPath(service.getPath()).withPort(service.getPort());
    }

    public ServerBuilder withContextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }

    public ServerBuilder withServletClass(Class<? extends Servlet> servletClass) {
        this.servletClass = servletClass;
        return this;
    }

    public ServerBuilder withPort(int port) {
        this.port = port;
        return this;
    }

    public Undertow build() {
        try {
            DeploymentInfo servletBuilder = deployment()
                    .setClassLoader(servletClass.getClassLoader())
                    .setContextPath(contextPath)
                    .setAuthenticationMode(AuthenticationMode.PRO_ACTIVE)
                    .setDeploymentName("server.war")
                    .addServlets(
                            servlet("Servlet", servletClass)
                                    .addMapping("/*"));

            DeploymentManager manager = defaultContainer().addDeployment(servletBuilder);
            manager.deploy();

            HttpHandler servletHandler = manager.start();
            PathHandler path = Handlers.path(Handlers.redirect(contextPath))
                    .addPrefixPath(contextPath, servletHandler);
            Undertow server = Undertow.builder()
                    .addHttpListener(port, "localhost")
                    .setHandler(path)
                    .build();

            return server;
        } catch (ServletException ex) {
            throw new RuntimeException(ex);
        }
    }
}
