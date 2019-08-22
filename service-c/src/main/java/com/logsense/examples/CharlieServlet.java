package com.logsense.examples;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.util.logging.resources.logging;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Random;

public class CharlieServlet extends HttpServlet {
    final Logger logger = LoggerFactory.getLogger(CharlieServlet.class);
    final Random r = new Random();

    private HazelcastInstance instance;
    private CharlieStorage storage;

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);

        // It's odd but the purpose of that is to have HazelcastClient separately
        Config cfg = new Config();
        Hazelcast.newHazelcastInstance(cfg);

        instance = HazelcastClient.newHazelcastClient();
        storage = new CharlieStorage();
    }

    private int complexCalculation(String id) {
        logger.info("Doing complex calculations for request with id: {}", id);
        try {
            Thread.sleep(200+r.nextInt(1000));
            if (r.nextInt(13) == 0) {
                logger.info("Extended iterations required by a request with id: {}", id);
                Thread.sleep(4000 + r.nextInt(6000));
            }
        } catch (InterruptedException e) {
            logger.debug("Sleep failed", e);
        }
        return r.nextInt(100000);
    }

    private int cachedCalculationMaybe(String id, String payload) {
        Map<String, Integer> cachedCalculation = instance.getMap("complex-results");
        if (cachedCalculation.containsKey(payload)) {
            logger.info("Found cached value for payload: {}", payload);
            return cachedCalculation.get(payload);
        } else {
            int value = complexCalculation(id);
            cachedCalculation.put(payload, value);
            logger.info("Stored cached value for payload: {}", payload);
            return value;
        }
    }

    private void invalidateCache() {
        logger.info("Cleaning cache");
        Map<String, Integer> cachedCalculation = instance.getMap("complex-results");
        cachedCalculation.clear();
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String payload = req.getParameter("payload");
        String id = req.getParameter("id");

        int complexResult=0;

        long t0 = System.currentTimeMillis();

        if ("foo".equals(payload)) {
            // Some very complex calculations needed
            complexResult = cachedCalculationMaybe(id, payload);
        } else {
            try {
                Thread.sleep(10+r.nextInt(10));
            } catch (InterruptedException e) {
                logger.debug("Sleep failed", e);
            }

            invalidateCache();
            complexResult = r.nextInt(100);
        }

        long t1 = System.currentTimeMillis();
        logger.info("Overall execution duration for item: {} was: {} ms", payload, t1-t0);

        storage.insertEntry(id, payload);

        PrintWriter writer = resp.getWriter();
        writer.write(Integer.toString(complexResult));
        writer.close();
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
