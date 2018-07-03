package com.roulette.game.player;

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
    private String nickName;
    private Double totalWin = 0d;
    private Double totalBet = 0d;

    public Player(String nickName){
        this.nickName = nickName;
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

    public String getNickName() {
        return nickName;
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

        return nickName != null ? nickName.equals(player.nickName) : player.nickName == null;
    }

    @Override
    public int hashCode() {
        return nickName != null ? nickName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Player{" +
                "nickName='" + nickName + '\'' +
                '}';
    }
}
