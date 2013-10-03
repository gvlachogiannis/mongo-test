package com.velti.mmbu.mongo;


import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author gvlachogiannis */
public class DokimiTest {

    private static Logger logger = LoggerFactory.getLogger(DokimiTest.class);

    @Test
    public void testName() throws Exception {

        logger.info("something");

        // print internal state
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);

    }
}
