package com.roulette.game.roulette;

import com.roulette.game.player.Mode;
import com.roulette.game.player.Player;
import com.roulette.game.player.PlayerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

/**
 * Created by lusouza on 02/07/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class RoundThreadTest {

    @Mock
    private RoundThread roundThread = new RoundThread();

    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        URL playersFile = RouletteTest.class.getClassLoader().getResource("test_pre_registered_players.txt");
        PlayerRepository.loadPlayers(playersFile);


    }


    @Test
    public void placeBetsToThePlayers() throws IOException, InterruptedException {
        BufferedReader bufferedReader = org.mockito.Mockito.mock(BufferedReader.class);
        when(bufferedReader.readLine()).thenReturn("Alex EVEN 100").thenReturn("Lucas 9 300");
        doCallRealMethod().when(roundThread).placeTheBet(bufferedReader);
        roundThread.placeTheBet(bufferedReader);
        roundThread.placeTheBet(bufferedReader);

        final Player alex = PlayerRepository.findByNickname("Alex");
        final Player lucas = PlayerRepository.findByNickname("Lucas");

        assertTrue(alex.getBets().size() == 1);
        assertFalse(alex.getBets().get(0).getNumberBet().isPresent());
        assertEquals(BigDecimal.valueOf(100d), alex.getBets().get(0).getAmountBet());
        assertEquals(Mode.EVEN, alex.getBets().get(0).getMode());

        assertTrue(lucas.getBets().size() == 1);
        assertEquals(Integer.valueOf(9), lucas.getBets().get(0).getNumberBet().get());
        assertEquals(BigDecimal.valueOf(300d), lucas.getBets().get(0).getAmountBet());
        assertEquals(Mode.NUMBER, lucas.getBets().get(0).getMode());
    }
}
