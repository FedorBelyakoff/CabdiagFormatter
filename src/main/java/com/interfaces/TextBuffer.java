package com.interfaces;

public interface TextBuffer {
    String savedText();

    boolean isEmpty();

    void save(String text);
}
