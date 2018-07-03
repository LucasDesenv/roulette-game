package com.roulette.game;

import com.roulette.game.exception.IllegalBetException;
import com.roulette.game.player.Mode;
import com.roulette.game.player.Player;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Created by lusouza on 29/06/18.
 */
public class PlayerTest {

    @Test
    public void playerCouldBetOnNumberBetween1And36() throws Exception{
        IntStream.rangeClosed(1,36).boxed().forEach(number -> {
            Player player = new Player("Tiki_Monkey");
            try {
                player.bet(number, BigDecimal.valueOf(250d));
            } catch (IllegalBetException e) {
                fail(e.getMessage());
            }
            assertEquals(number, player.getBets().get(0).getNumberBet().get());
            assertEquals(Mode.NUMBER, player.getBets().get(0).getMode());
            assertEquals(BigDecimal.valueOf(250d), player.getBets().get(0).getAmountBet());
        });
    }

    @Test(expected = IllegalBetException.class)
    public void playerCouldNotBetOnNumberGreater() throws Exception {
        Player player = new Player("Tiki_Monkey");
        player.bet(37, BigDecimal.valueOf(250d));
    }

    @Test(expected = IllegalBetException.class)
    public void playerCouldNotBetOnNumberLowerThan1() throws Exception {
        Player player = new Player("Tiki_Monkey");
        player.bet(0, BigDecimal.valueOf(250d));
    }

    @Test
    public void playerCouldBetOnOdd() throws Exception{
        Player player = new Player("Tiki_Monkey");
        player.bet(Mode.ODD, BigDecimal.valueOf(250d));
        assertEquals(Mode.ODD, player.getBets().get(0).getMode());
        assertEquals(BigDecimal.valueOf(250d), player.getBets().get(0).getAmountBet());
        assertNull(player.getBets().get(0).getNumberBet().orElse(null));
    }

    @Test
    public void playerCouldBetOnEven() throws Exception{
        Player player = new Player("Tiki_Monkey");
        player.bet(Mode.EVEN, BigDecimal.valueOf(250d));
        assertEquals(Mode.EVEN, player.getBets().get(0).getMode());
        assertEquals(BigDecimal.valueOf(250d), player.getBets().get(0).getAmountBet());
        assertNull(player.getBets().get(0).getNumberBet().orElse(null));
    }

    @Test
    public void playerCouldBetManyTimes() throws Exception{
        Player player = new Player("Tiki_Monkey");
        player.bet(Mode.EVEN, BigDecimal.valueOf(250d));
        player.bet(9, BigDecimal.valueOf(250d));
        assertEquals(player.getBets().size(), 2);
    }

    @Test
    public void playersBetShouldBeOrderedByInsert() throws Exception{
        Player player = new Player("Tiki_Monkey");
        player.bet(Mode.EVEN, BigDecimal.valueOf(250d));
        player.bet(9, BigDecimal.valueOf(250d));
        assertEquals(Mode.EVEN, player.getBets().get(0).getMode());
        assertEquals(Integer.valueOf(9), player.getBets().get(1).getNumberBet().get());
    }
}
