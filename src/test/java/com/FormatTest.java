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
    void shouldReturnCorrectTextWhenNoCable() {
        String actual = Format
                .stateOn(noCable())
                .formattedText();
        String expected = "Свитч: 10.240.27.150.\n" +
                "Порт: 12.\n" +
                "При вкл: no cable.";
        assertEquals(expected, actual);
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
        String actual = Format.stateOn(onePairOpened())
                .formattedText();
        String expected = "Свитч: 10.240.27.150.\n" +
                "Порт: 12.\n" +
                "При вкл: \n" +
                "\tпервая - открыта на 120м.";
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectFormatWhenLinkDownOk() {
        String actual = Format.stateOn(linkDownOk())
                .formattedText();
        String expected = "Свитч: 10.240.27.150.\n" +
                "Порт: 12.\n" +
                "При вкл: link down ok.";
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectFormatWhenLinkDownShutdown() {
        String actual = Format.stateOn(linkDownShutdown()).formattedText();
        String expected = "Свитч: 10.240.27.150.\n" +
                "Порт: 12.\n" +
                "При вкл: shutdown.";
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectTextWhenFirstOkAndSecondShort() {
        String actual = Format
                .stateOn(firstOkSecondShort())
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
        String actual = Format
                .doubleState(allOkInput(), firstOkSecondShort())
                .formattedText();
        String expected = "Свитч: 10.240.27.150.\n" +
                "Порт: 22.\n" +
                "При вкл: все - подключена на 55м.\n" +
                "При выкл: \n" +
                "\tпервая - подключена на 120м,\n" +
                "\tвторая - шорт на 33м.";
        assertEquals(expected, actual);
    }

    private Cabdiag noCable() {
        Cabdiag result = mock(Cabdiag.class);
        when(result.switchAddress()).thenReturn("10.240.27.150");
        when(result.port()).thenReturn(12);
        when(result.isCorrect()).thenReturn(true);
        when(result.cableState()).thenReturn(Cabdiag.CableState.ON);
        when(result.firstState()).thenReturn(NO_CABLE);
        when(result.secondState()).thenReturn(NO_CABLE);
        when(result.firstLength()).thenReturn(-1);
        when(result.secondLength()).thenReturn(-1);
        return result;
    }

    private Cabdiag linkDownOk() {
        Cabdiag result = mock(Cabdiag.class);
        when(result.switchAddress()).thenReturn("10.240.27.150");
        when(result.port()).thenReturn(12);
        when(result.isCorrect()).thenReturn(true);
        when(result.cableState()).thenReturn(Cabdiag.CableState.ON);
        when(result.firstState()).thenReturn(OK);
        when(result.secondState()).thenReturn(OK);
        when(result.firstLength()).thenReturn(-1);
        when(result.secondLength()).thenReturn(-1);
        return result;
    }

    private Cabdiag linkDownShutdown() {
        Cabdiag result = mock(Cabdiag.class);
        when(result.switchAddress()).thenReturn("10.240.27.150");
        when(result.port()).thenReturn(12);
        when(result.isCorrect()).thenReturn(true);
        when(result.cableState()).thenReturn(Cabdiag.CableState.ON);
        when(result.firstState()).thenReturn(SHUTDOWN);
        when(result.secondState()).thenReturn(SHUTDOWN);
        when(result.firstLength()).thenReturn(-1);
        when(result.secondLength()).thenReturn(-1);
        return result;
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

    private Cabdiag firstOkSecondShort() {
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