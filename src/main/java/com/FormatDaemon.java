package com;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.interfaces.CabdiagFactory;
import com.interfaces.FormatFactory;
import com.interfaces.TextBuffer;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;

import static com.Cabdiag.CableState.ON;


public class FormatDaemon extends JFrame implements FlavorListener, Runnable {
    private static final Logger LOG = Logger.getLogger(FormatDaemon.class);
    private final TextBuffer buffer;
    private final CabdiagFactory cabdiagFactory;
    private final FormatFactory formatFactory;
    private Cabdiag previous;
    private Cabdiag in;
    private String prevFormatted;


    public static void main(String[] args) {
        LOG.debug("FormatDaemon is running.");
        try {
            Injector injector = Guice.createInjector(new DaemonModule());
            FormatDaemon daemon = injector.getInstance(FormatDaemon.class);
            SwingUtilities.invokeLater(daemon);
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    @Inject
    public FormatDaemon(CabdiagFactory cabdiagFactory,
                        FormatFactory formatFactory,
                        TextBuffer buffer) {
        prevFormatted = "";
        this.buffer = buffer;
        this.cabdiagFactory = cabdiagFactory;
        this.formatFactory = formatFactory;
    }


    @Override
    public void run() {
        while (true) {
            LOG.debug("Run - new iteration.");
            if (!buffer.isEmpty()) {
                update();
            }
            int updateTimeMSec = 2_000;
            sleep(updateTimeMSec);
        }
    }


    public void update() {
        if (buffer.isEmpty()) {
            LOG.debug("Buffer is empty.");
            return;
        }

        String saved = buffer.savedText();
        if (prevFormatted.equals(saved))
            return;
        LOG.debug("Text in clipboard: " + saved);
        in = cabdiagFactory.cabdiagOf(saved);
        if (!in.isCorrect()) {
            LOG.debug("Clipboard text isn't cabdiag.");
            previous = null;
            return;
        }

        Format format;
        boolean singleFormat =
                previous == null || in.cableState() == previous.cableState();
        if (singleFormat) {
            System.err.println("Single state format selected.");
            format = singleStateFormat();
            previous = in;
        } else {
            System.err.println("Double state format selected.");
            format = doubleStateFormat();
            previous = null;
        }
        String formatted = format.formattedText();
        prevFormatted = formatted;
        buffer.save(formatted);
        System.err.println(
                "Formatted text saved to buffer. Text: " + formatted);
    }


    private Format singleStateFormat() {
        if (ON == in.cableState()) {
            return formatFactory.stateOn(in);
        } else {
            return formatFactory.stateOff(in);
        }
    }


    private Format doubleStateFormat() {
        if (ON == in.cableState()) {
            return formatFactory.doubleState(in, previous);
        } else {
            return formatFactory.doubleState(previous, in);
        }
    }


    @Override
    public void flavorsChanged(FlavorEvent e) {
        update();
    }


    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
