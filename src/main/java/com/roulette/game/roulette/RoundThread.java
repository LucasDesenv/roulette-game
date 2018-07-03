package com.roulette.game.roulette;

import com.roulette.game.player.Mode;
import com.roulette.game.player.Player;
import com.roulette.game.player.PlayerRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

/**
 * Created by lusouza on 02/07/18.
 */
public class RoundThread extends Thread {

    private volatile boolean running = true;

    @Override
    public void run() {
        synchronized(this){
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                while(running){
                    placeTheBet(reader);
                }

        }
    }

    void placeTheBet(BufferedReader reader) {
        try {
            final String[] lines = reader.readLine().split(" ");
            final String nickName = lines[0];
            final String bet = lines[1].toUpperCase();
            final Double amount = Double.valueOf(lines[2]);

            final Player player = PlayerRepository.findByNickname(nickName);

            if (bet.matches("\\d+")){
                final Integer numberBet = Integer.valueOf(lines[1]);
                player.bet(numberBet, BigDecimal.valueOf(amount));
            }else{
                final Mode mode = Mode.valueOf(bet);
                player.bet(mode, BigDecimal.valueOf(amount));
            }
        } catch (IOException e) {
            System.err.println("Error setting up bets: " + e.getMessage());
        }
    }

    void stopRunning() {
        this.running = false;
    }
}
