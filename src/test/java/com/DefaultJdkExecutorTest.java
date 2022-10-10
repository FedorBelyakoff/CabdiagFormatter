package com;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.interfaces.JavaExecutor.PROJECT_DIR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DefaultJdkExecutorTest {

    private SimpleExecutor simpleExecutor;
    private DefaultJdkExecutor executor;

    @BeforeEach
    void setUp() {
        simpleExecutor = mock(SimpleExecutor.class);
        executor = new DefaultJdkExecutor(simpleExecutor);
    }


    @Test
    void shouldPutParametersInCorrectOrderWhenMethodExecInConsoleInvoked() {
        executor.execInConsole("ipconfig", "/ALL");
        verify(simpleExecutor).exec(PROJECT_DIR,
                "cmd.exe", "/C",
                "ipconfig", "/ALL");
    }

    @Test
    void shouldExecJpsWhenMethodInvoked() {
        executor.jps();
        verify(simpleExecutor).exec(PROJECT_DIR,
                "jps.exe");
    }


    @Test
    void shouldExecJavaWithCorrectClassAndPathNames() {
        executor.java(TestDaemon.class);
        verify(simpleExecutor).exec(PROJECT_DIR,
                "java.exe",
                "com.TestDaemon");
    }



    @Test
    void shouldExecJavawWhenClassWithArgsPutted() {
        executor.javaw(TestDaemon.class, "status");
        verify(simpleExecutor).exec(PROJECT_DIR,
                "javaw.exe", "com.TestDaemon", "status");
    }
}