package com;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;


public class TestDaemon extends FormatDaemon {
    public TestDaemon() {
        super(null, null, null);
        setWindowSettings();
    }


    private void setWindowSettings() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.WHITE);
        setSize(1000, 1000);
        setLayout(null);
        setVisible(true);
    }


    public static void main(String[] args)  {
        try {
            SwingUtilities.invokeLater(TestDaemon::new);
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
