package com.logsense.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class ComplexLogic {
    final Logger logger = LoggerFactory.getLogger(CharlieServlet.class);

    private Random r = new Random();
    private int state;
    private final static int t0 = 1571184000;
    private final static int attrs = 10;

    public ComplexLogic() {
    }

    /**
     * @return Value of 1.0 +/- 1.0 if not anomaly and 100.0 +/- 100.0 if anomaly, never less than 0.0
     */
    private double anomalyPeakCreator(boolean enforceAnomaly) {
        if (enforceAnomaly) {
            return Math.max(0.0, 100*(1 + r.nextDouble()));
        } else {
            return Math.max(0.0, 1.0 + r.nextGaussian());
        }
    }

    private int currentAttributeIndex() {
        int days = (int) (System.currentTimeMillis()/1000 - t0) / (3600*24);
        return days % attrs;
    }

    private double anomalyTimeAwarePeak(boolean enforceAnomaly, int attrIndex) {
        boolean anomaly = enforceAnomaly && currentAttributeIndex() == attrIndex;
        return anomalyPeakCreator(anomaly);
    }

    private String randomElement(String[] elements) {
        return elements[r.nextInt(elements.length)];
    }

    private int analyzeState(boolean complexish) {
        logger.info(String.format("Disk seek request duration: %.1f ms on partition %s",
                anomalyTimeAwarePeak(complexish, 0)*10,
                randomElement(new String[] { "/dev/sda1", "/dev/xda1", "/dev/nvme01", "/dev/sda2", "/dev/sda3", "/dev/sdb1"})));
        logger.info(String.format("There are %d events queued in the system currently",
                (int) (anomalyTimeAwarePeak(complexish, 1)*10)));
        logger.info(String.format("Database SELECT to table %s yielded %d records",
                randomElement(new String[] { "cached_records", "customers", "requests_history"}),
                (int) (anomalyTimeAwarePeak(complexish, 2)*23)));
        logger.info(String.format("Current memory usage: %d MB",
                (int) (anomalyTimeAwarePeak(complexish, 3)*29)));
        logger.info(String.format("Charlie service ThreadPool size: %d",
                (int) (anomalyTimeAwarePeak(complexish, 4)*10)));
        logger.info(String.format("Background tasks that are in progress: %d",
                (int) (anomalyTimeAwarePeak(complexish, 5)*1.2)));
        logger.info(String.format("Database UPDATE to table %s changed %d records",
                randomElement(new String[] { "cached_records", "customers", "requests_history"}),
                (int) (anomalyTimeAwarePeak(complexish, 6)*17)));
        logger.info(String.format("There are %d failed requests",
                (int) (anomalyTimeAwarePeak(complexish, 7)*0.05)));
        logger.info(String.format("Backpressure factor: %d elements",
                (int) (anomalyTimeAwarePeak(complexish, 8)*0.5)));

        this.state += r.nextInt(10);
        return state;
    }

    private int normalishCalculate(String input) {
        try {
            Thread.sleep(200+r.nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return this.analyzeState(false);
    }

    private int complexishCalculate(String input) {
        int result=-1;
        try {
            logger.info("Extended iterations required by a request with id: {}", input);
            Thread.sleep(3000 + r.nextInt(6000));
            result = this.analyzeState(true);
            Thread.sleep(100 + r.nextInt(1000));
            logger.info("Finite state machine internal model set to {} on {}", this.state, this.state % 13);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int calculate(String input) {
        if (r.nextInt(13) != 0) {
            return this.normalishCalculate(input);
        } else {
            return this.complexishCalculate(input);
        }

    }
}
