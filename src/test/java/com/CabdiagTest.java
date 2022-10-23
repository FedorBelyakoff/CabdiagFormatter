package com;

import com.Cabdiag.PairState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.Cabdiag.PairState.*;
import static org.junit.jupiter.api.Assertions.*;


class CabdiagTest {
    @BeforeEach
    void setUp() {

    }

    @Test
    void shouldReturnCorrectFirstPairLengthWhen2And3PairsIsPresent() {
        int actual = secondAndThirdPairs().firstLength();
        int expected = 50;
        assertEquals(expected, actual);
    }
    @Test
    void shouldReturnCorrectSecondPairLengthWhen2And3PairsIsPresent() {
        int actual = secondAndThirdPairs().secondLength();
        int expected = 30;
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnTrueWhenCabdiagIsCorrect() {
        assertTrue(allPairsOk().isCorrect());
        assertTrue(allPairsShort().isCorrect());
        assertTrue(allPairsShutdown().isCorrect());
        assertTrue(allOpenedPairs().isCorrect());
    }


    @Test
    void shouldReturnFalseWhenCabdiagIsNotCorrect() {
        assertFalse(incorrectCabdiag().isCorrect());
    }


    @Test
    void shouldReturnSecondPairStateShortWhen1PairIsOkAndSecondShort() {
    }


    @Test
    void shouldReturnFirstPairOpenWhenOnlyFirstPairIsPresent() {
        Cabdiag cabdiag = firstPairOpened();
        assertEquals(OPEN, cabdiag.firstState());
        assertEquals(NO_PRESENT, cabdiag.secondState());
    }


    @Test
    void shouldReturnIsCabdiagTrueWhenOnlyFirstPairIsPresent() {
        assertTrue(firstPairOpened().isCorrect());
    }


    @Test
    void shouldReturnSecondPairLengthWhenOnlyFirstPairIsPresent() {
        int expected = 0;
        int actual = firstPairOpened().secondLength();
        assertEquals(expected, actual);
    }


    @Test
    void shouldReturnFirstPairLengthWhenOnlyFirstPairIsPresent() {
        int expected = 26;
        int actual = firstPairOpened().firstLength();
        assertEquals(expected, actual);
    }


    @Test
    void shouldReturnFirstPairStateOkWhenAllPairsOk() {
        PairState actual = allPairsOk().firstState();
        assertEquals(OK, actual);
    }


    @Test
    void shouldReturnSecondPairStateOkWhenAllPairsOk() {
        PairState actual = allPairsOk().secondState();
        assertEquals(OK, actual);
    }


    @Test
    void shouldReturtnFirstPairStateShutdownThenAllShutdown() {
        PairState actual = allPairsShutdown().firstState();
        assertEquals(SHUTDOWN, actual);
    }


    @Test
    void shouldReturtnSecondPairStateShutdownThenAllShutdown() {
        PairState actual = allPairsShutdown().secondState();
        assertEquals(SHUTDOWN, actual);
    }


    @Test
    void shouldReturnFirstPairStateShortThenAllShort() {
        PairState actual = allPairsShort().firstState();
        assertEquals(SHORT, actual);
    }


    @Test
    void shouldReturnSecondPairStateShortThenAllShort() {
        PairState actual = allPairsShort().secondState();
        assertEquals(SHORT, actual);
    }

    private Cabdiag secondAndThirdPairs() {
        return new Cabdiag("\n" +
                "10.240.184.143:5#cable_diag ports 7\n" +
                "Command: cable_diag ports 7\n" +
                "\n" +
                " Perform Cable Diagnostics ...\n" +
                "\n" +
                " Port   Type      Link Status          Test Result          Cable Length (M)\n" +
                " ----  -------  --------------  -------------------------  -----------------\n" +
                "  7     FE         Link Down     Pair2 Open     at 30  M          -\n" +
                "                                 Pair3 Open     at 50  M\n");
    }

    private Cabdiag firstPairOpened() {
        return new Cabdiag("\n" +
                "10.240.184.143:5#cable_diag ports 7\n" +
                "Command: cable_diag ports 7\n" +
                "\n" +
                " Perform Cable Diagnostics ...\n" +
                "\n" +
                " Port   Type      Link Status          Test Result          Cable Length (M)\n" +
                " ----  -------  --------------  -------------------------  -----------------\n" +
                "  7     FE         Link Down     Pair1 Open     at 26  M          -\n");

    }


    private Cabdiag allPairsShort() {
        return new Cabdiag("\n" +
                "10.240.184.143:5#cable_diag ports 7\n" +
                "Command: cable_diag ports 7\n" +
                "\n" +
                " Perform Cable Diagnostics ...\n" +
                "\n" +
                " Port   Type      Link Status          Test Result          Cable Length (M)\n" +
                " ----  -------  --------------  -------------------------  -----------------\n" +
                "  7     FE         Link Down     Pair1 Short     at 26  M          -\n" +
                "                                 Pair2 Short     at 27  M\n");
    }


    private Cabdiag incorrectCabdiag() {
        return new Cabdiag("240.38.145:admin#cable_diag ports 2\n" +
                "Command: cable_diag ports \n" +
                "\n" +
                " Perform Cable Diagnostics ...\n" +
                "\n" +
                " Port   Type    Link Status            Test Result           Cable Length (M)\n" +
                " ----  ------  -------------  -----------------------------  ----------------\n" +
                "  2     FE            OK                             -\n" +
                "\n" +
                "\n");
    }


    private Cabdiag allPairsShutdown() {
        return new Cabdiag("\n" +
                "10.241.190.145:admin#cable_diag ports 21\n" +
                "Command: cable_diag ports 21\n" +
                "\n" +
                "Perform Cable Diagnostics ...\n" +
                "\n" +
                "Port      Type      Link Status    Test Result                 Cable Length (M)\n" +
                "------  ----------  -------------  -------------------------  -----------------\n" +
                "21      100BASE-T   Link Down      Shutdown                          120\n" +
                "\n" +
                "\n");
    }


    private Cabdiag allOpenedPairs() {
        return new Cabdiag("\n" +
                "10.240.38.145:admin#cable_diag ports 3\n" +
                "Command: cable_diag ports 3\n" +
                "\n" +
                " Perform Cable Diagnostics ...\n" +
                "\n" +
                " Port   Type    Link Status            Test Result           Cable Length (M)\n" +
                " ----  ------  -------------  -----------------------------  ----------------\n" +
                "  3     FE      Link Down      Pair1 Open      at 17  M       -\n" +
                "                               Pair2 Open      at 17  M\n" +
                "\n");
    }


    private Cabdiag allPairsOk() {
        return new Cabdiag("10.240.38.145:admin#cable_diag ports 2\n" +
                "Command: cable_diag ports 2\n" +
                "\n" +
                " Perform Cable Diagnostics ...\n" +
                "\n" +
                " Port   Type    Link Status            Test Result           Cable Length (M)\n" +
                " ----  ------  -------------  -----------------------------  ----------------\n" +
                "  2     FE      Link Up        OK                             67\n" +
                "\n");
    }
}