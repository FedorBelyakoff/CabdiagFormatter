package com;

import com.interfaces.JavaExecutor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import java.io.File;

public class DefaultJdkExecutor implements JavaExecutor {
    protected final SimpleExecutor executor;

    private static final Logger LOG = Logger.getLogger(DefaultJdkExecutor.class);

    public DefaultJdkExecutor(SimpleExecutor executor) {
        this.executor = executor;
    }

    @Override
    public Process execInConsole(String... args) {
        String[] cmdArgs = ArrayUtils.insert(0, args,
                "cmd.exe", "/C");
        return exec(cmdArgs);
    }


    @Override
    public Process jps(String... args) {
        String[] cmdArgs = ArrayUtils.insert(0, args,
                "jps.exe");
        return exec(cmdArgs);
    }

    @Override
    public Process java(Class<?> c, String... args) {
        String[] cmdArgs = ArrayUtils.insert(0, args,
                "java.exe", classWithPackageName(c));
        return exec(cmdArgs);
    }

    @Override
    public Process javaw(Class<?> c, String... args) {
        String[] cmdArgs = ArrayUtils.insert(0, args,
                "javaw.exe", classWithPackageName(c));
        return exec(cmdArgs);
    }

    protected static String classWithPackageName(Class<?> c) {
//        String packageName = c.getPackage().getName();
//        String className = c.getName().replace(packageName + '.',
//                "");
//        return packageName + "/"
//                + className;
        return c.getName();
    }

    protected Process exec(String... args) {
        LOG.debug("Exec in console: \""
                + String.join(" ", args)+ "\"");
        return executor.exec(PROJECT_DIR, args);
    }
}
