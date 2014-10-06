package com.epam.sample;

import org.apache.log4j.Logger;

/**
 * Created by dima on 4.10.14.
 */
public class JarSampleWrapper {
    static final Logger LOG = Logger.getLogger(JarSampleWrapper.class);

    JarSample jarSample = null;

    public JarSampleWrapper(JarSample jarSample) {
        this.jarSample = jarSample;
    }

    public void plus() {
        LOG.info("Increment static field : i="+jarSample.increment());
    }
}
