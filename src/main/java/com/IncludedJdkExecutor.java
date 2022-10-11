package com;

import com.annotations.JarName;
import com.annotations.JdkPath;
import com.google.inject.Inject;
import org.apache.commons.lang3.ArrayUtils;


public class IncludedJdkExecutor extends DefaultJdkExecutor {
    private final String jdkPath;
    private final String jarName;

    @Inject
    public IncludedJdkExecutor(@JdkPath String jdkPath,
                               @JarName String jarName,
                               SimpleExecutor executor) {
        super(executor);
        this.jdkPath = jdkPath;
        this.jarName = jarName;
    }

    @Override
    public Process jps(String... args) {
        String[] cmdArgs = ArrayUtils.insert(0, args,
                jdkPath + "jps.exe");
        return exec(cmdArgs);
    }


    @Override
    public Process java(Class<?> c, String... args) {
        String[] cmdArgs = ArrayUtils.insert(0, args,
                jdkPath + "java.exe", "-cp", jarName, classWithPackageName(c));
        return exec(cmdArgs);
    }


    public Process javaw(Class<?> c, String... args) {
        String[] cmdArgs = ArrayUtils.insert(0, args,
                jdkPath + "javaw.exe", "-cp", jarName, classWithPackageName(c));
        return exec(cmdArgs);
    }


}
