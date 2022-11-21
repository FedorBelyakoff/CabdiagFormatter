package com;

import com.google.common.base.MoreObjects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Cabdiag.CableState.*;
import static com.Cabdiag.LinkStatus.LINK_DOWN;
import static com.Cabdiag.LinkStatus.LINK_UP;
import static com.Cabdiag.PairState.*;


public class Cabdiag {
    public static final int UNDEFINED_LENGTH = -1;
    private static final Pattern TWO_PAIRS_PATTERN = Pattern.compile("(.*?)" +
            "(?<switch>([0-9]{1,3}\\.){3}([0-9]){1,3})" +
            "(.*?)(ports)([ \\t]+)" +
            "(?<port>[0-9]+)" +
            "(.*)" +
            "(?<linkStatus>Link Down|Link Up)" +
            "(" +
            "([ \\t]+Pair *[13][ \\t]+" +
            "(?<firstState>OK|Short|Open)" +
            "([ \\t]+)(at)([ \\t]+)" +
            "(?<firstLength>[0-9]+)(.*?))" +
            "|" +
            "([ \\t]+Pair *2[ \\t]+" +
            "(?<secondState>OK|Short|Open)" +
            "([ \\t]+)(at)([ \\t]+)" +
            "(?<secondLength>[0-9]+)" +
            "(.*?))" +
            "){1,2}", Pattern.DOTALL);
    private static final Pattern ALL_PAIRS_PATTERN = Pattern.compile("(.*?)" +
            "(?<switch>([0-9]{1,3}\\.){3}([0-9]){1,3})" +
            "(.*?)" +
            "(ports)([ \\t]+)" +
            "(?<port>[0-9]+)" +
            "(.*?)" +
            "(?<linkStatus>Link Down|Link Up)([ \\t]+)" +
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
        return valueOrUndefined(lengthStr);
    }


    public PairState secondState() {
        String second = textFromGroup("secondState");
        return stateFrom(second);
    }


    public int secondLength() {
        String lengthStr = textFromGroup("secondLength");
        return valueOrUndefined(lengthStr);
    }

    public LinkStatus linkStatus() {
        String status = textFromGroup("linkStatus");
        switch (status) {
            case "Link Up":
                return LINK_UP;
            case "Link Down":
                return LINK_DOWN;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }
    }
    public CableState cableState() {
        boolean ok = firstState() == OK || secondState() == OK;
        boolean shutdown = firstState() == SHUTDOWN
                && secondState() == SHUTDOWN;
        boolean noCable = firstState() == NO_CABLE
                || secondState() == NO_CABLE;
        return (ok || shutdown || noCable)
                ? ON
                : OFF;
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


    private static int valueOrUndefined(String lengthStr) {
        return lengthStr == null || lengthStr.equals("-")
                ? UNDEFINED_LENGTH
                : Integer.parseInt(lengthStr);
    }


    private PairState stateFrom(String text) {
        if (text == null) {
            return NO_PRESENT;
        }
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

    public enum LinkStatus {
        LINK_UP, LINK_DOWN
    }
    public enum PairState {
        OK, SHORT, OPEN, SHUTDOWN, NO_CABLE, NO_PRESENT
    }


    public enum CableState {
        ON, OFF
    }

}
