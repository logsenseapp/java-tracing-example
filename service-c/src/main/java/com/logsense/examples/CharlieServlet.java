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
import java.util.Random;

public class CharlieServlet extends HttpServlet {
    final Logger logger = LoggerFactory.getLogger(CharlieServlet.class);
    final Random r = new Random();


    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String payload = req.getParameter("payload");
        String id = req.getParameter("id");

        int complexResult=0;

        if ("foo".equals(payload)) {
            // Some very complex calculations needed
            try {
                logger.info("Doing complex calculations for request with id: {}", id);
                Thread.sleep(200+r.nextInt(1000));
                complexResult = r.nextInt(100000);
            } catch (InterruptedException e) {
                logger.debug("Sleep failed", e);
            }
        } else {
            complexResult = 42;
        }

        PrintWriter writer = resp.getWriter();
        writer.write(Integer.toString(complexResult));
        writer.close();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
