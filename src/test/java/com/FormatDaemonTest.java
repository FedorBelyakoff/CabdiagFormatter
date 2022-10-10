package com;

import com.Cabdiag.PairState;
import com.interfaces.CabdiagFactory;
import com.interfaces.FormatFactory;
import com.interfaces.TextBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static com.Cabdiag.CableState.*;
import static com.Cabdiag.PairState.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class FormatDaemonTest {
    private static final File OUT_DIR = new File("C:\\Users\\Fred" +
                     "\\IdeaProjects\\Clipboard\\target\\test-classes");

    private Cabdiag cabdiag;
    private Format doubleState;
    private Format stateOn;
    private Format stateOff;
    private Runtime runtime;
    private FormatDaemon daemon;
    private CabdiagFactory cabdiagFactory;
    private FormatFactory formatFactory;
    private TextBuffer buffer;


    @BeforeEach
    void setUp()  {
        cabdiag = mock(Cabdiag.class);
        doubleState = mock(Format.class);
        stateOn = mock(Format.class);
        stateOff = mock(Format.class);
        runtime = mock(Runtime.class);
        cabdiagFactory = mock(CabdiagFactory.class);
        formatFactory = mock(FormatFactory.class);
        buffer = mock(TextBufferImp.class);
        daemon = new FormatDaemon(cabdiagFactory, formatFactory, buffer);
        when(doubleState.formattedText()).thenReturn("doubleStateFormattedText");
        when(stateOn.formattedText()).thenReturn("state_on_formatted_text");
        when(stateOff.formattedText()).thenReturn("state_off_formatted_text");
        when(cabdiag.isCorrect()).thenReturn(true);
        when(formatFactory.doubleState(any(), any())).thenReturn(doubleState);
        when(formatFactory.stateOn(any())).thenReturn(stateOn);
        when(formatFactory.stateOff(any())).thenReturn(stateOff);
        when(buffer.isEmpty()).thenReturn(false);
        when(buffer.savedText()).thenReturn("5543534");
    }


    @Test
    void shouldRunInBackground() throws IOException, InterruptedException, URISyntaxException {
//        File dir = new File(System.getProperty("user.dir"));
        Process daemon = new ProcessBuilder()
                         .directory(OUT_DIR)
                         .command("cmd.exe", "/k", "java", "com/TestDaemon")
                         .start();


        assertTrue(daemon.isAlive());
        daemon.destroy();
    }


    public static void main(String[] args) throws InterruptedException, IOException {
        Process daemon = new ProcessBuilder()
                         .directory(OUT_DIR)
                         .command("cmd.exe", "/k", "java", "com/TestDaemon")
//                         .command("CMD.EXE", "/K", "ipconfig")
//                         .command("cmd.exe", "/k")
                         .start();

//        Thread.sleep(2000);
//        assertTrue(daemon.isAlive());
    }


    @Test
    void shouldCreateOnStateFormatWhen2PairOk() {
        Cabdiag cabdiag = singleInput(OK, OK);
        when(cabdiag.cableState()).thenReturn(ON);
        daemon.update();
        verify(formatFactory).stateOn(cabdiag);
    }


    @Test
    void shouldCreateOnStateFormatWhen2PairShutdown() {
        Cabdiag cabdiag = singleInput(SHUTDOWN, SHUTDOWN);
        when(cabdiag.cableState()).thenReturn(ON);
        daemon.update();
        verify(formatFactory).stateOn(cabdiag);
    }


    @Test
    void shouldCreateDoubleFormatWhen2DiagCopied() {
//        when(cabdiagFactory.getInstance(any())).thenReturn(cabdiag1);
        Cabdiag cabdiag1 = singleInput(SHUTDOWN, SHUTDOWN);
        when(cabdiag1.cableState()).thenReturn(ON);
        daemon.update();
        Cabdiag cabdiag2 = singleInput(OPEN, SHORT);
        when(cabdiag2.cableState()).thenReturn(OFF);
        daemon.update();
        verify(formatFactory).doubleState(cabdiag1, cabdiag2);
    }


    @Test
    void shouldCreateOnStateFormatThen1PairOkAnd2Open() {
        Cabdiag cabdiag = singleInput(OK, OPEN);
        when(cabdiag.cableState()).thenReturn(ON);
        daemon.update();
        verify(formatFactory).stateOn(cabdiag);
    }


    @Test
    void shouldCreateOnStateFormatThen1PairOkAnd2Short() {
        Cabdiag cabdiag = singleInput(OK, SHORT);
        when(cabdiag.cableState()).thenReturn(ON);
        daemon.update();
        verify(formatFactory).stateOn(cabdiag);
    }


    @Test
    void shouldCreateOffStateFormatWhen1PairShortAnd2Open() {
        Cabdiag in = singleInput(SHORT, OPEN);
        daemon.update();
        verify(formatFactory).stateOff(in);
    }


    @Test
    void shouldCreateOffStateFormatWhen2PairShort() {
        Cabdiag in = singleInput(SHORT, SHORT);
        daemon.update();
        verify(formatFactory).stateOff(in);
    }


    @Test
    void shouldCreateOffStateFormatWhen2PairOpen() {
        Cabdiag in = singleInput(OPEN, OPEN);
        daemon.update();
        verify(formatFactory).stateOff(in);
    }


    private Cabdiag singleInput(PairState first, PairState second) {
        Cabdiag cabdiag = mock(Cabdiag.class);
        when(cabdiag.isCorrect()).thenReturn(true);
        when(cabdiag.firstState()).thenReturn(first);
        when(cabdiag.secondState()).thenReturn(second);
        when(cabdiagFactory.cabdiagOf(anyString())).thenReturn(cabdiag);
        return cabdiag;
    }
}