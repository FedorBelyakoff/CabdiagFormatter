package com;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class StringIOConverterTest {
    private static final String DEFAULT_CMD_CHARSET = "IBM866";

    private StringIOConverter converter;

    @BeforeEach
    void setUp() {
        converter = new StringIOConverter();
    }

    @Test
    void shouldConvertInputStreamToCorrectStr() throws UnsupportedEncodingException {
        String expected = "Line1: русский текстtest11\n"
                + "Line2: test\t addsd\n"
                + "Line3: test dfsdf";
        String cmdEncoded = new String(expected.getBytes(DEFAULT_CMD_CHARSET), DEFAULT_CMD_CHARSET);
        System.out.println(cmdEncoded);
        System.out.println(expected);
        System.out.println();
        ByteArrayInputStream in = new ByteArrayInputStream(
                cmdEncoded.getBytes(DEFAULT_CMD_CHARSET));
        String actual = converter.read(in);
        assertEquals(expected, actual);
    }


    @Test
    void shouldWriteCorrectTextToOutStream() throws UnsupportedEncodingException {
        String expected = "Line1: текст текст\n"
                + "Line2: test\t addsd\n"
                + "Line3: test dfsdf";
        ByteArrayOutputStream out = new ByteArrayOutputStream(expected.length());
        converter.write(expected, out);
        String actual = out.toString(DEFAULT_CMD_CHARSET);
        assertEquals(actual, expected);
    }
}