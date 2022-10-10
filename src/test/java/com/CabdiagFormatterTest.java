package com;

import com.interfaces.JavaExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;


class CabdiagFormatterTest {
    public static final String TEST_PID = "1111";
    private Class<TestDaemon> daemonClass;
    private String daemonName;
    private JavaExecutor executor;
    private StringIOConverter converter;
    private CabdiagFormatter formatter;
    private String jarName;

    public static void main(String... args) {

    }

    @BeforeEach
    void setUp() {
        daemonClass = TestDaemon.class;
        daemonName = "TestDaemon";
        executor = mock(JavaExecutor.class);
        converter = mock(StringIOConverter.class);
        jarName = "testJarName";
        formatter = new CabdiagFormatter(daemonClass, executor, converter);
        when(executor.jps(any())).thenReturn(jpsProcessMock());
    }



    @Test
    void shouldRunDaemonWhenNoKeysPutted()  {
        System.out.println(System.getProperty("user.dir"));
        when(converter.read(any())).thenReturn(strWithoutDaemon());
        formatter.testRun();
        verify(executor).javaw(daemonClass);
    }

    @Test
    void shouldNotRunDaemonWhenAlreadyRunning() {
        when(executor.jps()).thenReturn(jpsProcessMock());
        when(converter.read(any())).thenReturn(strWithoutDaemon());
        formatter.testRun();
        when(converter.read(any())).thenReturn(testProcessStr());
        formatter.testRun();
        verify(executor).javaw(daemonClass);
    }

    @Test
    void shouldStopDaemonWhenKeyStopPutted() {
        when(converter.read(any())).thenReturn(testProcessStr());
        formatter.testRun("stop");
        verify(executor).execInConsole("taskkill.exe", "/PID", TEST_PID, "/F");
    }

    @Test
    void shouldInvokeJpsWhenKeyStatusPutted() {
        when(converter.read(any())).thenReturn(testProcessStr());
        formatter.testRun("status");
        verify(executor).jps();
    }



    private String testProcessStr() {
        return "7728 Launcher\n" +
                "11144 RemoteMavenServer36\n" +
                "3096 Jps                 \n" +
                "2492 \n" +
                TEST_PID + " TestDaemon\n";
    }

    private Process jpsProcessMock() {
        Process result = mock(Process.class);
        return result;
    }


    private String strWithoutDaemon() {
        return "7728 Launcher\n" +
                "11144 RemoteMavenServer36\n" +
                "3096 Jps                 \n" +
                "2492 \n";
    }


}