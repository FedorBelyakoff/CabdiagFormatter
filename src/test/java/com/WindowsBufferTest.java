package com;

import com.interfaces.JavaExecutor;
import com.interfaces.TextBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class WindowsBufferTest {
    private TextBuffer buffer;
    private StringIOConverter converter;
    private JavaExecutor executor;
    private Process winclip;
    private OutputStream clipOut;
    private InputStream clipIn;


    @BeforeEach
    void setUp() {
        executor = mock(JavaExecutor.class);
        converter = mock(StringIOConverter.class);
        buffer = new WindowsBuffer("target\\outwit-bin-1.25\\bin",
                executor, converter);

        winclip = mock(Process.class);
        clipOut = mock(OutputStream.class);
        clipIn = mock(InputStream.class);
        when(executor.execInConsole(any())).thenReturn(winclip);
        when(winclip.getOutputStream()).thenReturn(clipOut);
        when(winclip.getInputStream()).thenReturn(clipIn);
    }


    @Test
    void shouldSaveMultiLineTextWhenSaveInvoked() {
        String expected = "Line1: test11\n"
                + "Line2: test\t addsd\n"
                + "Line3: test dfsdf";
        winclip = mock(Process.class);
        clipOut = mock(OutputStream.class);
        when(executor.execInConsole(any())).thenReturn(winclip);
        when(winclip.getOutputStream()).thenReturn(clipOut);
        buffer.save(expected);
        verify(executor).execInConsole(
                "target\\outwit-bin-1.25\\bin\\" + "winclip.exe", "-c");
        verify(converter).write(expected, clipOut);
    }


//    @Test
//    void shouldSaveSingleLineTextWhenSaveInvoked() {
//        String testStr = "Line1: test";
//        buffer.save(testStr);
//
//        verify(executor).
//    }


    @Test
    void shouldReturnFalseWhenIsEmptyInvokedAndBufferNotEmpty() {
        String testStr = "Line1: test";
        when(converter.read(any())).thenReturn(testStr);
        assertFalse(buffer.isEmpty());
    }


    @Test
    void shouldShowBufferedTextWhenShowInvoked() {
        String expected = "Line1: test\n"
                + "Line2: test\t addsd\n"
                + "Line3: test dfsdf";
        when(converter.read(any())).thenReturn(expected);
        String actual = buffer.savedText();
        assertEquals(expected, actual);
    }
}