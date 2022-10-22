package com;

import com.google.common.base.MoreObjects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Cabdiag.CableState.*;
import static com.Cabdiag.PairState.*;


public class Cabdiag {
    private static final Pattern TWO_PAIRS_PATTERN = Pattern.compile("(.*?)" +
                     "(?<switch>([0-9]{1,3}\\.){3}([0-9]){1,3})" +
                     "(.*?)(ports)([ \\t]+)" +
                     "(?<port>[0-9]+)" +
                     "(.*?)" +
                     "(?<firstState>OK|Short|Open)" +
                     "([ \\t]+)(at)([ \\t]+)" +
                     "(?<firstLength>[0-9]+)" +
                     "(.*?)" +
                     "(?<secondState>OK|Short|Open)" +
                     "([ \\t]+)(at)([ \\t]+)" +
                     "(?<secondLength>[0-9]+)" +
                     "(.*)", Pattern.DOTALL);
    private static final Pattern ALL_PAIRS_PATTERN = Pattern.compile("(.*?)" +
                     "(?<switch>([0-9]{1,3}\\.){3}([0-9]){1,3})" +
                     "(.*?)" +
                     "(ports)([ \\t]+)" +
                     "(?<port>[0-9]+)" +
                     "(.*?)" +
                     "(Link Down|Link Up)([ \\t]+)" +
                     "(?<firstState>(?<secondState>OK|Shutdown|No Cable))" +
                     "([ \\t]+)" +
                     "(?<firstLength>(?<secondLength>-|[0-9]+))" +
                     "(.*)", Pattern.DOTALL);
    private final String text;


    public Cabdiag(String text) {
        this.text = text;
    }


    public boolean isCorrect() {
        return TWO_PAIRS_PATTERN.matcher(text).matches()
                         || ALL_PAIRS_PATTERN.matcher(text).matches();
    }


    public String switchAddress() {
        return textFromGroup("switch");
    }


    public int port() {
        String port = textFromGroup("port");
        return Integer.parseInt(port);
    }


    public PairState firstState() {
        String state = textFromGroup("firstState");
        return stateFrom(state);
    }


    public int firstLength() {
        String lengthStr = textFromGroup("firstLength");
        return lengthStr.equals("-") ? 0 : Integer.parseInt(lengthStr);
    }


    public PairState secondState() {
        String state = textFromGroup("firstState");
        return stateFrom(state);
    }


    public int secondLength() {
        String lengthStr = textFromGroup("secondLength");
        return lengthStr.equals("-") ? 0 : Integer.parseInt(lengthStr);
    }


    private String textFromGroup(String name) {
        Matcher twoMatcher = TWO_PAIRS_PATTERN.matcher(text);
        if (twoMatcher.matches()) {
            return twoMatcher.group(name);
        }
        Matcher allMatcher = ALL_PAIRS_PATTERN.matcher(text);
        if (allMatcher.matches()) {
            return allMatcher.group(name);
        }

        throw new IllegalStateException("Input isn't cabdiag!");
    }



    public CableState cableState() {
        boolean ok = firstState() == OK || secondState() == OK;
        boolean shutdown = firstState() == SHUTDOWN
                         && secondState() == SHUTDOWN;
        return (ok || shutdown) ? ON : OFF;
    }


    @Override
    public String toString() {
        if (this.isCorrect()) {
            return MoreObjects.toStringHelper(this)
                             .add("Switch", switchAddress())
                             .add("Port", port())
                             .add("cable state", cableState())
                             .add("1st pair state", firstState())
                             .add("1st length", firstLength())
                             .add("2nd state", secondState())
                             .add("2nd length", secondLength())
                             .toString();
        } else {
            return MoreObjects.toStringHelper(this)
                             .add("Isn't cabdiag.", 0)
                             .add("text", text)
                             .toString();
        }
    }




    private PairState stateFrom(String text) {
        switch (text) {
            case "Open":
                return OPEN;
            case "Short":
                return SHORT;
            case "OK":
                return OK;
            case "Shutdown":
                return SHUTDOWN;
            case "No Cable":
                return NO_CABLE;
            default:
                throw new IllegalStateException("Pair state not found!");
        }

    }
    public enum PairState {
        OK, SHORT, OPEN, SHUTDOWN, NO_CABLE, NO_PRESENT
    }



    public enum CableState {
        ON, OFF
    }

}
