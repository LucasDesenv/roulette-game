package com.roulette.game;

import com.roulette.game.player.PlayerRepository;
import com.roulette.game.roulette.RouletteTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

/**
 * Created by lusouza on 02/07/18.
 */
public class PlayerRepositoryTest {

    @Before
    public void before(){
        URL playersFile = RouletteTest.class.getClassLoader().getResource("test_pre_registered_players.txt");
        PlayerRepository.loadPlayers(playersFile);
    }

    @Test
    public void mustCreatePlayersBasedOnAFile(){
        Assert.assertEquals(2, PlayerRepository.getPlayers().size());
    }

    @Test
    public void mustFindPlayerByNickname(){
        Assert.assertEquals("Lucas", PlayerRepository.findByNickname("Lucas").getNickName());
        Assert.assertEquals("Alex", PlayerRepository.findByNickname("Alex").getNickName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void mustThrowExceptionWhenPlayerNotFound(){
        PlayerRepository.findByNickname("UNKNOWN_NAME");
    }
}
