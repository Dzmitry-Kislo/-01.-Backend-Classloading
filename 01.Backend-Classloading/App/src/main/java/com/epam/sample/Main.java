package com.epam.sample;

import com.epam.sample.loader.JarClassLoader;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;


/**
 * Created by dima on 3.10.14.
 */
public class Main {
    static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        int swValue = 0;

        System.out.println("============================================================================");
        System.out.println("|                             MENU SELECTION DEMO                          |");
        System.out.println("============================================================================");
        System.out.println("| Options:                                                                 |");
        System.out.println("|        1. Create JarClassLoader only once and load new functionality     |");
        System.out.println("|        2. Create new JarClassLoader every time and load new functionality|");
        System.out.println("|        3. Exit                                                           |");
        System.out.println("============================================================================");

        ClassLoader loader = new JarClassLoader();

        while (swValue != 3) {
            swValue = Keyin.inInt(" Select option: ");


            switch (swValue) {
                case 1:
                    Class clazz = Class.forName("com.epam.sample.JarSampleImpl", true, loader);
                    JarSample object = (JarSample) clazz.newInstance();
                    LOG.info(object);

                    JarSampleWrapper jarSampleWrapper = new JarSampleWrapper(object);
                    jarSampleWrapper.plus();

                    //output classpath
                    String classpath = System.getProperty("java.class.path");
                    String[] classpathEntries = classpath.split(File.pathSeparator);
                    for (int i = 0; i < classpathEntries.length; i++) {
                        LOG.info("CLASSPATH include: " + classpathEntries[i]);
                    }

                    LOG.info("New functionality was loaded by" + object.getClass().getClassLoader());
                    break;
                case 2:
                    ClassLoader loader1 = new JarClassLoader();
                    Class clazz1 = Class.forName("com.epam.sample.JarSampleImpl", true, loader1);
                    JarSample object1 = (JarSample) clazz1.newInstance();
                    LOG.info(object1);

                    JarSampleWrapper jarSampleWrapper1 = new JarSampleWrapper(object1);
                    jarSampleWrapper1.plus();

                    //output classpath
                    String classpath1 = System.getProperty("java.class.path");
                    String[] classpathEntries1 = classpath1.split(File.pathSeparator);
                    for (int i = 0; i < classpathEntries1.length; i++) {
                        LOG.info("CLASSPATH include: " + classpathEntries1[i]);
                    }

                    LOG.info("New functionality was loaded by" + object1.getClass().getClassLoader());
                    break;
                case 3:
                    System.out.println("Exit selected");
                    break;
                default:
                    System.out.println("Invalid selection");
                    break;
            }
        }
    }
}
