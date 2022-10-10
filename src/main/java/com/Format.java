package com;

import org.apache.commons.lang3.StringUtils;

public abstract class Format {
    public static Format doubleState(Cabdiag cableOn, Cabdiag cableOf) {
        return new Format() {
            @Override
            public String formattedText() {
                return "Свитч: " + cableOf.switchAddress() + ".\n" +
                        "Порт: " + cableOf.port() + ".\n" +
                        "При вкл: все - " +
                        stateText(cableOn.firstState()) +
                        " " + cableOn.firstLength() + "м.\n" +
                        "При выкл:\n\tпервая - " +
                        stateText(cableOf.firstState()) + " на " +
                        cableOf.firstLength() +
                        "м,\n\tвторая - " +
                        stateText(cableOf.secondState()) + " на " +
                        cableOf.secondLength() + "м.";

            }
        };
    }


    public static Format stateOff(Cabdiag cableOf) {
        return new Format() {
            @Override
            public String formattedText() {
                return "Свитч: " + cableOf.switchAddress() + ".\n" +
                                 "Порт: " + cableOf.port() + ".\n" +
                                 "При выкл:\n\tпервая - " +
                                 stateText(cableOf.firstState()) + " на " +
                                 cableOf.firstLength() +
                                 "м,\n\tвторая - " +
                                 stateText(cableOf.secondState()) + " на " +
                                 cableOf.secondLength() + "м.";
            }
        };
    }


    public static Format stateOn(Cabdiag cableOn) {
        if (cableOn.firstState() == cableOn.secondState()) {
            return new CableOnSamePairs(cableOn);
        } else {
            return new CableOnDifferentPairs(cableOn);
        }
    }


    public abstract String formattedText();


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
                return "Нет кабеля.";
            default:
            throw new IllegalStateException("Неизвестное состояние.");
        }
    }


    @Override
    public String toString() {
        return formattedText();
    }


    Format() {
    }


    private static class CableOnDifferentPairs extends Format {
        private final Cabdiag cableOn;


        public CableOnDifferentPairs(Cabdiag cableOn) {
            this.cableOn = cableOn;
        }


        @Override
        public String formattedText() {
            return "Свитч: " + cableOn.switchAddress() + ".\n" +
                             "Порт: " + cableOn.port() + ".\n" +
                             "При вкл:\n" +
                             "\tпервая - " + stateText(cableOn.firstState()) +
                             " на " + cableOn.firstLength() + "м,\n" +
                             "\tвторая - " + stateText(cableOn.secondState()) +
                             " на " + cableOn.secondLength() + "м.";
        }
    }



    private static class CableOnSamePairs extends Format {
        private final Cabdiag cableOn;


        public CableOnSamePairs(Cabdiag cableOn) {
            this.cableOn = cableOn;
        }


        @Override
        public String formattedText() {
            return "Свитч: " + cableOn.switchAddress() + ".\n" +
                             "Порт: " + cableOn.port() + ".\n" +
                             "При вкл: все - " +
                             stateText(cableOn.firstState()) +
                             " " + cableOn.firstLength() + "м.";
        }
    }
}
