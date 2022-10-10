package com.interfaces;

import java.io.File;

public interface JavaExecutor {

    File PROJECT_DIR = new File(System.getProperty("user.dir"));
    Process jps(String... args);
    Process java(Class<?> c, String... args);

    Process javaw(Class<?> c, String... args);

    Process execInConsole(String... args);
}
