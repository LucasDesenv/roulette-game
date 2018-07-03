package com.roulette.game.roulette;

import com.roulette.game.player.Bet;
import com.roulette.game.player.Mode;
import com.roulette.game.player.Player;
import com.roulette.game.player.PlayerRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.URL;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

/**
 * Created by lusouza on 29/06/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class RouletteTest {
    @Mock
    private Roulette roulette = new Roulette(1);
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    @Rule
    public final ExpectedSystemExit expectedSystemExit = ExpectedSystemExit.none();

    @Before
    public void before(){
        URL playersFile = RouletteTest.class.getClassLoader().getResource("test_pre_registered_players.txt");
        PlayerRepository.loadPlayers(playersFile);
        System.setOut(new PrintStream(outContent));
        when(roulette.getPlayers()).thenReturn(PlayerRepository.getPlayers());
    }

    @After
    public void after(){
        System.setOut(originalOut);
    }

    @Test
    public void mustCalculateTheTotalBetAndWin() throws IOException {

        Player lucas = PlayerRepository.findByNickname("Lucas");
        lucas.bet(Mode.EVEN, BigDecimal.TEN);

        Player alex = PlayerRepository.findByNickname("Alex");
        alex.bet(Mode.ODD, BigDecimal.valueOf(1000));

        when(roulette.generateRandomNumber()).thenReturn(20);
        doCallRealMethod().when(roulette).markWinners();
        doCallRealMethod().when(roulette).rollBall();
        roulette.rollBall();
        roulette.markWinners();
        assertEquals(Double.valueOf(10d), lucas.getTotalBet());
        assertEquals(Double.valueOf(20d), lucas.getTotalWin());
        assertEquals(Double.valueOf(1000d), alex.getTotalBet());
        assertEquals(Double.valueOf(0d), alex.getTotalWin());
    }

    @Test
    public void mustSetABetAsWinnerByMode() throws IOException {

        Player lucas = PlayerRepository.findByNickname("Lucas");
        lucas.bet(Mode.EVEN, BigDecimal.TEN);

        Player alex = PlayerRepository.findByNickname("Alex");
        alex.bet(Mode.ODD, BigDecimal.valueOf(1000));

        when(roulette.generateRandomNumber()).thenReturn(20);
        doCallRealMethod().when(roulette).markWinners();
        doCallRealMethod().when(roulette).rollBall();
        roulette.rollBall();
        roulette.markWinners();
        assertTrue(lucas.getBets().get(0).hasWon());
        assertEquals(Double.valueOf(10d), lucas.getTotalBet());
        assertEquals(Double.valueOf(20d), lucas.getTotalWin());
        assertEquals(BigDecimal.valueOf(20), lucas.getBets().get(0).getAmountWon().get());

        assertFalse(alex.getBets().get(0).hasWon());
    }

    @Test
    public void mustSetABetAsWinnerByNumber() throws IOException {

        Player lucas = PlayerRepository.findByNickname("Lucas");
        lucas.bet(16, BigDecimal.valueOf(1000));

        Player alex = PlayerRepository.findByNickname("Alex");
        alex.bet(Mode.ODD, BigDecimal.valueOf(1000));

        when(roulette.generateRandomNumber()).thenReturn(16);
        doCallRealMethod().when(roulette).markWinners();
        doCallRealMethod().when(roulette).rollBall();
        roulette.rollBall();
        roulette.markWinners();

        assertFalse(alex.getBets().get(0).hasWon());

        assertTrue(lucas.getBets().get(0).hasWon());
        assertEquals(Double.valueOf(1000d), lucas.getTotalBet());
        assertEquals(Double.valueOf(36000d), lucas.getTotalWin());
        assertEquals(BigDecimal.valueOf(36000), lucas.getBets().get(0).getAmountWon().get());
    }

    @Test
    public void canHaveNoWinners() throws IOException {

        Player lucas = PlayerRepository.findByNickname("Lucas");
        lucas.bet(16, BigDecimal.valueOf(7500));

        Player alex = PlayerRepository.findByNickname("Alex");
        alex.bet(27, BigDecimal.valueOf(1000));

        when(roulette.generateRandomNumber()).thenReturn(35);
        doCallRealMethod().when(roulette).markWinners();
        doCallRealMethod().when(roulette).rollBall();
        roulette.rollBall();
        roulette.markWinners();
        assertFalse(lucas.getBets().get(0).hasWon());
        assertEquals(Double.valueOf(0d), lucas.getTotalWin());
        assertEquals(Double.valueOf(7500), lucas.getTotalBet());

        assertFalse(alex.getBets().get(0).hasWon());
        assertEquals(Double.valueOf(0d), alex.getTotalWin());
        assertEquals(Double.valueOf(1000d), alex.getTotalBet());
    }

    @Test
    public void shouldPrintTheBets() throws IOException {

        Player lucas = PlayerRepository.findByNickname("Lucas");
        lucas.bet(16, BigDecimal.valueOf(1000));

        Player alex = PlayerRepository.findByNickname("Alex");
        alex.bet(Mode.ODD, BigDecimal.valueOf(1000));

        when(roulette.generateRandomNumber()).thenReturn(16);
        doCallRealMethod().when(roulette).markWinners();
        doCallRealMethod().when(roulette).rollBall();
        doCallRealMethod().when(roulette).printBets();

        roulette.rollBall();
        roulette.markWinners();
        roulette.printBets();
        final String expectedOutput = "\n" +
                "Number: 16\n" +
                "         Player            Bet        Outcome       Winnings\n" +
                "              -              -              -              -\n" +
                "          Lucas             16            WIN          36000\n" +
                "           Alex            ODD           LOSE              0\n";

        assertEquals(outContent.toString(), expectedOutput);
    }

    @Test
    public void shouldPrintTheBetsEvenIfNoBets() throws IOException {
        when(roulette.generateRandomNumber()).thenReturn(16);
        doCallRealMethod().when(roulette).markWinners();
        doCallRealMethod().when(roulette).rollBall();
        doCallRealMethod().when(roulette).printBets();

        roulette.rollBall();
        roulette.markWinners();
        roulette.printBets();
        final String expectedOutput = "\n" +
                "Number: 16\n" +
                "         Player            Bet        Outcome       Winnings\n" +
                "              -              -              -              -\n";

        assertEquals(outContent.toString(), expectedOutput);
    }

    @Test
    public void shouldPrintPlayersFinalResults() throws IOException {

        Player lucas = PlayerRepository.findByNickname("Lucas");
        lucas.bet(16, BigDecimal.valueOf(1000));
        lucas.bet(Mode.EVEN, BigDecimal.valueOf(7500));

        Player alex = PlayerRepository.findByNickname("Alex");
        alex.bet(Mode.ODD, BigDecimal.valueOf(1000));
        alex.bet(23, BigDecimal.valueOf(199));

        when(roulette.generateRandomNumber()).thenReturn(16);
        doCallRealMethod().when(roulette).markWinners();
        doCallRealMethod().when(roulette).rollBall();
        doCallRealMethod().when(roulette).printPlayersFinalResult();

        roulette.rollBall();
        roulette.markWinners();
        roulette.printPlayersFinalResult();
        final String expectedOutput = "\n" +
                "Number: 16\n" +
                "\n" +
                " # Total Win x Bet #\n" +
                "\n" +
                "         Player      Total Win      Total Bet\n" +
                "              -              -              -\n" +
                "          Lucas        51000.0         8500.0\n" +
                "           Alex            0.0         1199.0\n";

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void mustResetBets() throws IOException {

        Player lucas = PlayerRepository.findByNickname("Lucas");
        lucas.bet(Mode.EVEN, BigDecimal.TEN);

        Player alex = PlayerRepository.findByNickname("Alex");
        alex.bet(Mode.ODD, BigDecimal.valueOf(1000));

        doCallRealMethod().when(roulette).restartBets();
        roulette.restartBets();
        assertTrue(lucas.getBets().isEmpty());
        assertTrue(alex.getBets().isEmpty());
    }

    @Test
    public void testCompleteFlow(){
        final Roulette roulette = Mockito.spy(new Roulette(1));
        Player lucas = PlayerRepository.findByNickname("Lucas");
        lucas.bet(16, BigDecimal.valueOf(1000));
        lucas.bet(Mode.EVEN, BigDecimal.valueOf(7500));

        Player alex = PlayerRepository.findByNickname("Alex");
        alex.bet(Mode.ODD, BigDecimal.valueOf(1000));
        alex.bet(23, BigDecimal.valueOf(199));

        when(roulette.generateRandomNumber()).thenReturn(16);
        doCallRealMethod().when(roulette).markWinners();
        doCallRealMethod().when(roulette).rollBall();
        doCallRealMethod().when(roulette).printBets();
        doCallRealMethod().when(roulette).printPlayersFinalResult();
        doCallRealMethod().when(roulette).executeTask();
        doCallRealMethod().when(roulette).startBets();

        expectedSystemExit.expectSystemExitWithStatus(0);
        expectedSystemExit.checkAssertionAfterwards(() -> {
            final String outputExpected = "\n" +
                    "Number: 16\n" +
                    "         Player            Bet        Outcome       Winnings\n" +
                    "              -              -              -              -\n" +
                    "          Lucas             16            WIN          36000\n" +
                    "          Lucas           EVEN            WIN          15000\n" +
                    "           Alex            ODD           LOSE              0\n" +
                    "           Alex             23           LOSE              0\n" +
                    "\n" +
                    " # Total Win x Bet #\n" +
                    "\n" +
                    "         Player      Total Win      Total Bet\n" +
                    "              -              -              -\n" +
                    "          Lucas        51000.0         8500.0\n" +
                    "           Alex            0.0         1199.0\n";
            assertEquals(outputExpected,outContent.toString());

            assertTrue(alex.getBets().stream().noneMatch(Bet::hasWon));

            assertTrue(lucas.getBets().stream().allMatch(Bet::hasWon));
            assertEquals(Double.valueOf(8500d), lucas.getTotalBet());
            assertEquals(Double.valueOf(51000d), lucas.getTotalWin());
            assertEquals(BigDecimal.valueOf(36000), lucas.getBets().get(0).getAmountWon().get());
            assertEquals(BigDecimal.valueOf(15000), lucas.getBets().get(1).getAmountWon().get());

        });

        roulette.startBets();
        roulette.executeTask();
    }

}