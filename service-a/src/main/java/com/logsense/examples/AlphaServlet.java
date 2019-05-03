package com.logsense.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

public class AlphaServlet extends HttpServlet {
    final Logger logger = LoggerFactory.getLogger(AlphaServlet.class);

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String message = req.getParameter("message");
        String id = UUID.randomUUID().toString();

        logger.info("Service-A assigned ID {} to message: {}", id, message);

        String result = new HttpPostRequestBuilder().withService(Service.B).withPayload(message).withId(id).build();

        PrintWriter writer = resp.getWriter();
        writer.write("The result is: "+result+"\n");
        writer.close();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
