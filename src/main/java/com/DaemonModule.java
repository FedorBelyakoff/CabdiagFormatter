package com;

import com.annotations.ClipUtilPath;
import com.annotations.FormatDaemonClass;
import com.annotations.JarName;
import com.annotations.JdkPath;
import com.google.inject.AbstractModule;
import com.interfaces.CabdiagFactory;
import com.interfaces.FormatFactory;
import com.interfaces.JavaExecutor;
import com.interfaces.TextBuffer;


public class DaemonModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CabdiagFactory.class).to(CabdiagFactoryImp.class);
        bind(FormatFactory.class).to(FormatFactoryImp.class);
//        bind(StringIOConverter.class).to(StringIOConverter.class);
        bind(TextBuffer.class).to(WindowsBuffer.class);
        bind(JavaExecutor.class).to(IncludedJdkExecutor.class);
        bind(Class.class)
                .annotatedWith(FormatDaemonClass.class)
                .toInstance(FormatDaemon.class);
        String clipUtilPath = "outwit-bin-1.25\\bin\\";
        bind(String.class)
                .annotatedWith(ClipUtilPath.class)
                .toInstance(clipUtilPath);
        String jdkPath = "jdk\\bin\\";
        bind(String.class)
                .annotatedWith(JdkPath.class)
                .toInstance(jdkPath);
        String jarName = "Clipboard-1.0-SNAPSHOT-jar-with-dependencies.jar";
        bind(String.class)
                .annotatedWith(JarName.class)
                .toInstance(jarName);
    }
}
