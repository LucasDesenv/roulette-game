package com.roulette.game.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lusouza on 02/07/18.
 */
public class PlayerRepository {

    private static List<Player> players;

    public static void loadPlayers(URL playersFile) {
        players = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader( new InputStreamReader(playersFile.openStream()));
            String nickname;

            while ((nickname = reader.readLine()) != null)
                players.add(new Player(nickname));

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading the players.");
        }
    }

    public static List<Player> getPlayers() {
        return players;
    }

    public static Player findByNickname(String nickName){
        return players.stream()
                .filter(player -> player.getNickName().equals(nickName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid nickname"));
    }
}
