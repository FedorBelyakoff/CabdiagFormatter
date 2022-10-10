package com;

import com.interfaces.TextBuffer;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class ClipboardUtil implements Runnable {
    private static String[] args;
    private TextBuffer buffer;

    private static final String DEFAULT_CMD_CHARSET = "IBM866";


    public static void main(String... args) {
        ClipboardUtil.args = args;
        SwingUtilities.invokeLater(new ClipboardUtil());
    }


    @Override
    public void run() {
        buffer = new TextBufferImp();
        String command = "";
        if (args.length > 0) {
            command = args[0];
        } else {
            showHelp();
            return;
        }
        switch (command) {
            case "show":
                show();
                break;
            case "check":
                check();
                break;
            case "put":
                put();
                break;
            case "help":
                showHelp();
                break;
            default:
                System.err.println("Invalid argument.");
                showHelp();
                break;
        }
    }


    private void put() {
        if (args.length == 2) {
            buffer.save(args[1]);
            System.exit(0);
        }
    }


    private void check() {
        if (buffer.isEmpty()) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }


    private void show() {
        if (buffer.isEmpty()) {
            System.err.println("Clipboard is empty!");
        } else {
            System.out.println(buffer.savedText());
            try {
                OutputStreamWriter writer = new OutputStreamWriter(
                                 System.out, DEFAULT_CMD_CHARSET);
                writer.write(buffer.savedText());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void showHelp() {
        System.out.println("Справка.");
        System.out.println("put <текст> Поместить текст в буфер.");
        System.out.println("show        Смотреть содержимое буфера.");
        System.out.println("check       Проверить, есть ли что-то в буфере.");
        System.out.println("              Если пусто возвращает статус 1, иначе - 0.");
        System.out.println("help        Справка.");
    }
}
