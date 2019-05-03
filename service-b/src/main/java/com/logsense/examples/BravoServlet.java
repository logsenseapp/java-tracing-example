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

public class BravoServlet extends HttpServlet {
    final Logger logger = LoggerFactory.getLogger(BravoServlet.class);
    final long WARNING_DURATION_THRESHOLD_MS = 1000;


    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String payload = req.getParameter("payload");
        String id = req.getParameter("id");

        long t0 = System.currentTimeMillis();

        String result = new HttpPostRequestBuilder().withService(Service.C).withPayload(payload).withId(id).build();

        long t1 = System.currentTimeMillis();
        if (t1-t0 > WARNING_DURATION_THRESHOLD_MS) {
            logger.warn("Calculations for payload with ID: {} took: {} ms", id, t1-t0);
        }

        PrintWriter writer = resp.getWriter();
        writer.write(result);
        writer.close();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
