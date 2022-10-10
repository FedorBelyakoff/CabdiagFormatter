package com;

import com.interfaces.TextBuffer;
import events.Publisher;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;


public class TextBufferImp implements Publisher, FlavorListener, TextBuffer {


    @Override
    public String savedText() {
        return getSavedString();
    }


    @Override
    public boolean isEmpty() {
        return getSavedString().equals("");
    }


    private String  getSavedString() {
//        Clipboard clip = Toolkit.getDefaultToolkit()
//                         .getSystemClipboard();
        DataFlavor dataFlavor = DataFlavor.stringFlavor;

        Clipboard clip = Toolkit.getDefaultToolkit()
                         .getSystemClipboard();
        Object text = null;
        if (clip.isDataFlavorAvailable(dataFlavor)) {
            try {
                text = clip.getData(dataFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }
        }

        if (text instanceof String) {
            return  (String) text;
        } else {
            return "";
        }
    }


    @Override
    public void save(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        clipboard.setContents(stringSelection, null);

//        notifySubscribers(new EventBuilder().addProperty());
    }


    @Override
    public void flavorsChanged(FlavorEvent e) {
        notifySubscribers();
    }
}
