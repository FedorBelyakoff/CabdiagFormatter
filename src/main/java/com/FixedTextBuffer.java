package com;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class FixedTextBuffer extends TextBufferImp {
    @Override
    public String savedText() {
        try {
            return getClipboardText();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getClipboardText() throws IOException {
        String dir = System.getProperty("user.dir");
        String utilPackageName = ClipboardUtil.class.getPackage().getName();
        String utilName = ClipboardUtil.class
                         .getName()
                         .replace(utilPackageName + '.', "");
        Process runTask = new ProcessBuilder().directory(new File(dir))
                         .command("java", utilPackageName + "/" + utilName, "show")
                         .start();
        InputStream in = runTask.getInputStream();
        String buffered = read(in);
        in.close();
        return buffered;
    }

    private String read(InputStream in) {
        StringBuilder result = new StringBuilder();
        Scanner sc = new Scanner(in);
        while (sc.hasNextLine()) {
            result.append(sc.nextLine());
            result.append('\n');
        }
        return result.toString();
    }

}
