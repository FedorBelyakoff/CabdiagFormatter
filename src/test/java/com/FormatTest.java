package com;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.Cabdiag.PairState.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class FormatTest {
    @BeforeEach
    void setUp() {

    }


    @Test
    void shouldReturnCorrectTextWhenAllPairsOk() {
        String actual = Format.stateOn(allOkInput())
                         .toString();
        String expected = "Свитч: 10.240.27.150.\n" +
                         "Порт: 22.\n" +
                         "При вкл: все - подключена на 55м.";
        assertEquals(expected, actual);
    }


    @Test
    void shouldReturnCorrectTextWhenOnePairIsPresent() {
        String  actual = Format.stateOn(onePairOpened())
                .formattedText();
        String expected = "Свитч: 10.240.27.150.\n" +
                "Порт: 12.\n" +
                "При вкл: \n" +
                "\tпервая - открыта на 120м.";
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectTextWhenFirstOkAndSecondShort() {
        String actual = Format.stateOn(firstOkSecondShortInput())
                         .toString();
        String expected = "Свитч: 10.240.27.150.\n" +
                         "Порт: 12.\n" +
                         "При вкл: \n" +
                         "\tпервая - подключена на 120м,\n" +
                         "\tвторая - шорт на 33м.";
        assertEquals(expected, actual);
    }


    @Test
    void shouldReturnCorrectTextWhenFirsSamePairsAndSecondDifferentInputPutted() {
        String  actual = Format
                .doubleState(allOkInput(), firstOkSecondShortInput())
                .formattedText();
        String expected = "Свитч: 10.240.27.150.\n" +
                         "Порт: 22.\n" +
                         "При вкл: все - подключена на 55м.\n" +
                         "При выкл: \n" +
                         "\tпервая - подключена на 120м,\n" +
                         "\tвторая - шорт на 33м.";
        assertEquals(expected, actual);
    }


    private Cabdiag allOkInput() {
        Cabdiag in = mock(Cabdiag.class);
        when(in.switchAddress()).thenReturn("10.240.27.150");
        when(in.port()).thenReturn(22);
        when(in.firstState()).thenReturn(OK);
        when(in.secondState()).thenReturn(OK);
        when(in.firstLength()).thenReturn(55);
        when(in.secondLength()).thenReturn(55);
        return in;
    }


    private Cabdiag onePairOpened() {
        Cabdiag in = mock(Cabdiag.class);
        when(in.switchAddress()).thenReturn("10.240.27.150");
        when(in.port()).thenReturn(12);
        when(in.firstState()).thenReturn(OPEN);
        when(in.secondState()).thenReturn(NO_PRESENT);
        when(in.firstLength()).thenReturn(120);
        when(in.secondLength()).thenReturn(-1);
        return in;
    }

    private Cabdiag firstOkSecondShortInput() {
        Cabdiag in = mock(Cabdiag.class);
        when(in.switchAddress()).thenReturn("10.240.27.150");
        when(in.port()).thenReturn(12);
        when(in.firstState()).thenReturn(OK);
        when(in.secondState()).thenReturn(SHORT);
        when(in.firstLength()).thenReturn(120);
        when(in.secondLength()).thenReturn(33);
        return in;
    }
}