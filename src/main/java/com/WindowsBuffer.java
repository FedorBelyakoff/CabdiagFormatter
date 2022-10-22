package com;

import com.annotations.ClipUtilPath;
import com.google.inject.Inject;
import com.interfaces.JavaExecutor;
import com.interfaces.TextBuffer;

import java.io.*;


public class WindowsBuffer implements TextBuffer {

    private final String clipUtilPath;
    private final JavaExecutor executor;
    private final StringIOConverter converter;

    @Inject
    public WindowsBuffer(@ClipUtilPath String clipUtilPath,
                         JavaExecutor executor,
                         StringIOConverter converter) {
        this.clipUtilPath = clipUtilPath;
        this.executor = executor;
        this.converter = converter;
    }


    @Override
    public String savedText() {
        try {
            return savedFromUtil();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean isEmpty() {
        return savedText().equals("");
    }


    @Override
    public void save(String text) {
        try {
            saveToBuffer(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void saveToBuffer(String text) throws IOException {
        OutputStream utilIn = executor
                .execInConsole(
                        clipUtilPath + "\\"+ "winclip.exe", "-c")
                .getOutputStream();
        converter.write(text, utilIn);
    }


    private String savedFromUtil() throws IOException {
        InputStream utilOut = executor
                .execInConsole(clipUtilPath +  "winclip.exe", "-p")
                .getInputStream();
        String saved = converter.read(utilOut);
        utilOut.close();
        return saved;
    }


}
