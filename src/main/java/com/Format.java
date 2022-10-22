package com;

import static com.Cabdiag.PairState.NO_PRESENT;

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


    protected String formatPairs(Cabdiag cableOn) {
        String result = "";
        boolean pairsIsSame = cableOn.firstState() == cableOn.secondState()
                && cableOn.firstLength() == cableOn.secondLength();
        if (pairsIsSame) {
            result += allPairStr(cableOn);
        } else {
            if (NO_PRESENT != cableOn.firstState()) {
                result += firstPairStr(cableOn);
            }
            if (NO_PRESENT != cableOn.secondState()) {
                result += secondPairStr(cableOn);
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
        return String.format("все - %s на %sм",
                         stateText(c.firstState()), c.secondLength());
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
