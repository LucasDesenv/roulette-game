package com.roulette.game;

import com.roulette.game.player.PlayerRepository;
import com.roulette.game.roulette.Roulette;

import java.net.URL;

/**
 * Created by lusouza on 02/07/18.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        URL playersFile = PlayerRepository.class.getClassLoader().getResource("pre_registered_players.txt");
        PlayerRepository.loadPlayers(playersFile);

        final Roulette roulette = new Roulette(2);
        roulette.start();
        roulette.join();
    }
}
