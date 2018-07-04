package com.roulette.game.domain.player;

import com.roulette.game.domain.bet.Bet;
import com.roulette.game.domain.bet.Mode;
import com.roulette.game.exception.IllegalBetException;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created by lusouza on 29/06/18.
 */
public class Player {

    private List<Bet> bets;
    private String nickname;
    private Double totalWin = 0d;
    private Double totalBet = 0d;

    public Player(String nickname){
        this.nickname = nickname;
        this.bets = new LinkedList<>();
    }

    public void bet(Integer numberBet, BigDecimal amount) throws IllegalBetException {
        this.bets.add(new Bet(Optional.of(numberBet), amount, Mode.NUMBER));
    }

    public void bet(Mode mode, BigDecimal amount) throws IllegalBetException {
        this.bets.add(new Bet(Optional.empty(), amount, mode));
    }

    public List<Bet> getBets() {
        return bets;
    }

    public String getNickname() {
        return nickname;
    }

    public void reset() {
        this.bets = new LinkedList<>();
    }

    public void summarizeTotals() {
        this.totalBet += bets.stream().mapToDouble(bet -> bet.getAmountBet().doubleValue()).sum();
        this.totalWin += bets.stream().mapToDouble(bet -> bet.getAmountWon().get().doubleValue()).sum();
    }

    public Double getTotalBet() {
        return totalBet;
    }

    public Double getTotalWin() {
        return totalWin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return nickname != null ? nickname.equals(player.nickname) : player.nickname == null;
    }

    @Override
    public int hashCode() {
        return nickname != null ? nickname.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Player{" +
                "nickName='" + nickname + '\'' +
                '}';
    }
}
