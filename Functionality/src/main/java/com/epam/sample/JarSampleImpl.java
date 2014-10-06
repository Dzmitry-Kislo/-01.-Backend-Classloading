package com.epam.sample;

/**
 * Created by dima on 3.10.14.
 */
public class JarSampleImpl implements JarSample {

    protected static StringBuffer counter1 = new StringBuffer();

    private static int i = 0;

    public JarSampleImpl() {
    }

    @Override
    public int increment() {
        return i++;
    }

    @Override
    public String toString() {
        return "Static fields --> STATIC_COUNTER_INTERFACE {"+counter.append("1")+"}:::STATIC_COUNTER_IMPL{"+counter1.append(2) + "}";
    }
}
