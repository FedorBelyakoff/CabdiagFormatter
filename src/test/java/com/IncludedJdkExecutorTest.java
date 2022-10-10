package com;

import com.interfaces.JavaExecutor;
import org.apache.commons.exec.DefaultExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.interfaces.JavaExecutor.PROJECT_DIR;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class IncludedJdkExecutorTest {

    private JavaExecutor executor;
    private PrintStream defaultOut;
    private PrintStream out;
    private SimpleExecutor simpleExecutor;
    private String jarName;

    @BeforeEach
    void setUp() {
        simpleExecutor = mock(SimpleExecutor.class);
        jarName = "testJarName";
        executor = new IncludedJdkExecutor("jdk\\bin\\", jarName,
                simpleExecutor);
    }

    @Test
    void shouldExecJpsWhenMethodInvoked() {
        executor.jps();
        verify(simpleExecutor).exec(PROJECT_DIR,
                "jdk\\bin\\jps.exe");
    }


    @Test
    void shouldExecJavaWithCorrectClassAndPathNames() {
        executor.java(TestDaemon.class);
        verify(simpleExecutor).exec(PROJECT_DIR,
                "jdk\\bin\\java.exe", "-cp", jarName,
                "com.TestDaemon");
    }



    @Test
    void shouldExecJavawWhenClassWithArgsPutted() {
        executor.javaw(TestDaemon.class, "status");
        verify(simpleExecutor).exec(PROJECT_DIR,
                "jdk\\bin\\javaw.exe", "-cp", jarName,
                "com.TestDaemon",
                "status");
    }
}