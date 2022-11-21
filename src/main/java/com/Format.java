package com;

import static com.Cabdiag.LinkStatus.LINK_DOWN;
import static com.Cabdiag.LinkStatus.LINK_UP;
import static com.Cabdiag.PairState.*;

public abstract class Format {

    Format() {
    }


    public static Format doubleState(Cabdiag cableOn, Cabdiag cableOf) {
        return new Format() {
            @Override
            public String formattedText() {
                String result = head(cableOn);
                result += "При вкл: ";
                result += formatPairs(cableOn);
                result += '\n';
                result += "При выкл: ";
                result += formatPairs(cableOf);
                return result;
            }
        };
    }


    public static Format stateOff(Cabdiag cableOf) {
        return new Format() {
            @Override
            public String formattedText() {
                String result = head(cableOf);
                result += "При выкл: ";
                result += formatPairs(cableOf);
                return result;
            }
        };
    }


    public static Format stateOn(Cabdiag cableOn) {
               return new Format() {
            @Override
            public String formattedText() {
                String result = head(cableOn);
                result += "При вкл: ";
                result += formatPairs(cableOn);
                return result;
            }
        };

    }


    public abstract String formattedText();


    @Override
    public String toString() {
        return formattedText();
    }


    protected String formatPairs(Cabdiag cabdiag) {
        String result = "";
        boolean pairsIsSame = cabdiag.firstState() == cabdiag.secondState()
                && cabdiag.firstLength() == cabdiag.secondLength();

        if (pairsIsSame) {
            result += allPairStr(cabdiag);
        } else {
            if (NO_PRESENT != cabdiag.firstState()) {
                result += firstPairStr(cabdiag);
            }
            if (NO_PRESENT != cabdiag.secondState()) {
                result += secondPairStr(cabdiag);
            }
        }
        result += '.';
        return result;
    }

    protected String head(Cabdiag c) {
        return String.format("Свитч: %s.\nПорт: %s.\n",
                         c.switchAddress(), c.port());
    }

    protected String allPairStr(Cabdiag c) {
        if (Cabdiag.UNDEFINED_LENGTH == c.firstLength()) {
            if (LINK_UP == c.linkStatus()) {
                return "link up без длины";
            } else if (LINK_DOWN == c.linkStatus()) {
                return textOnLinkDown(c);
            } else {
                throw new RuntimeException("Unknown link status!");
            }
        } else {
            return String.format("все - %s на %sм",
                    stateText(c.firstState()), c.secondLength());
        }
    }

    private  String textOnLinkDown(Cabdiag c) {
        if (OK == c.firstState()) {
            return "link down ok";
        } else if (SHUTDOWN == c.firstState()) {
            return "shutdown";
        } else if (c.firstState() == NO_CABLE) {
            return "no cable";
        } else {
            throw new RuntimeException("Unknown pair state!");
        }
    }


    protected String firstPairStr(Cabdiag c) {
        return String.format("\n\tпервая - %s на %sм",
                         stateText(c.firstState()), c.firstLength());
    }


    protected String secondPairStr(Cabdiag c) {
        return String.format(",\n\tвторая - %s на %sм",
                         stateText(c.secondState()), c.secondLength());
    }


    protected String stateText(Cabdiag.PairState s) {
        switch (s) {
            case OK:
                return "подключена";
            case SHORT:
                return "шорт";
            case OPEN:
                return "открыта";
            case SHUTDOWN:
                return "shutdown";
            case NO_CABLE:
                return "no cable";
            default:
                throw new IllegalStateException("Неизвестное состояние.");
        }
    }


}
