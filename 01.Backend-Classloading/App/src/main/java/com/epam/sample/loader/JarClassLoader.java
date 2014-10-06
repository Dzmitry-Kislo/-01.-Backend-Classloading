package com.epam.sample.loader;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by dima on 3.10.14.
 */
public class JarClassLoader extends ClassLoader {
    static final Logger LOG = Logger.getLogger(JarClassLoader.class);

    private static final String FILE_PATH_NAME = "jar/functionality.jar";
    private static final String PACKAGE_NAME = "com.epam.sample";

    private Map<String, Class<?>> localCache = new HashMap<String, Class<?>>();

    public JarClassLoader(ClassLoader parent) {
        super(parent);
    }

    public JarClassLoader() {
        this(JarClassLoader.class.getClassLoader());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class result = findLoadedClass(name);
        if (result != null) {
            LOG.info("% Class " + name + "found in cache");
            return result;
        }
        cacheAllClasses(name);

        return loadClassFromLocalCache(name);
    }

    private synchronized Class<?> loadClassFromLocalCache(String name) throws ClassNotFoundException {
        Class<?> result = localCache.get(name);

        if (result == null)
            result = localCache.get(PACKAGE_NAME + "." + name);

        if (result == null)
            result = super.findSystemClass(name);

        return result;
    }

    private void cacheAllClasses(String name) throws ClassNotFoundException {
        JarFile jarFile = null;
        String stripClassName = "";
        try {
            jarFile = new JarFile(FILE_PATH_NAME);
        } catch (IOException e) {
            LOG.error("Jar file " + FILE_PATH_NAME + " not found" + e.getMessage());
        }
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            if (match(normalize(jarEntry.getName()), PACKAGE_NAME)) {
                try {
                    byte[] classData = loadClassData(jarFile, jarEntry);
                    stripClassName = stripClassName(normalize(jarEntry.getName()));
                    if (classData != null && stripClassName.equals(name)) {
                        Class<?> clazz = defineClass(stripClassName, classData, 0, classData.length);
                        localCache.put(clazz.getName(), clazz);
                        LOG.info("Class " + name + " loaded in local cache");
                    }
                } catch (IOException e) {
                    throw new ClassNotFoundException("Cannot load class " + jarEntry.getName() + ": " + e);
                }
            }
        }

    }


    private String stripClassName(String className) {
        return className.substring(0, className.length() - 6);
    }


    private String normalize(String className) {
        return className.replace('/', '.');
    }

    private boolean match(String className, String packageName) {
        return className.startsWith(packageName) && className.endsWith(".class");
    }

    private byte[] loadClassData(JarFile jarFile, JarEntry jarEntry) throws IOException {
        long size = jarEntry.getSize();
        if (size == -1 || size == 0) {
            return null;
        }
        byte[] data = new byte[(int) size];
        InputStream is = jarFile.getInputStream(jarEntry);
        try {
            is.read(data);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }
        return data;
    }

}
