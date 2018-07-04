package com.roulette.game.domain.bet;

import com.roulette.game.exception.IllegalBetException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by lusouza on 29/06/18.
 */
public class Bet {
    private Optional<Integer> numberBet;
    private final BigDecimal amountBet;
    private Mode mode;
    private boolean won;
    private AtomicReference<BigDecimal> amountWon = new AtomicReference<>(BigDecimal.ZERO);
    private static final BigDecimal WIN_36X = new BigDecimal(36);
    private static final BigDecimal WIN_2X = new BigDecimal(2);
    private static final Integer MAX_NUMBER_BET = 36;
    private static final Integer MIN_NUMBER_BET = 1;


    public Bet(Optional<Integer> numberBet, BigDecimal amountBet, Mode mode) throws IllegalBetException {
        numberBet.ifPresent(number -> {
            if (number > MAX_NUMBER_BET || number < MIN_NUMBER_BET){
                throw new IllegalBetException();
            }
        });
        this.mode = mode;
        this.numberBet = numberBet;
        this.amountBet = amountBet;
    }

    public Optional<Integer> getNumberBet() {
        return numberBet;
    }

    public Mode getMode() {
        return mode;
    }

    public BigDecimal getAmountBet() {
        return amountBet;
    }

    public boolean isOdd() {
        return Mode.ODD.equals(mode);
    }

    public boolean isEven() {
        return Mode.EVEN.equals(mode);
    }

    public boolean hasWon() {
        return won;
    }

    public void wonByNumber() {
        this.won = true;
        this.amountWon = new AtomicReference<>(this.amountBet.multiply(WIN_36X));
    }

    public void wonByMode() {
        this.won = true;
        this.amountWon = new AtomicReference<>(this.amountBet.multiply(WIN_2X));
    }

    public AtomicReference<BigDecimal> getAmountWon() {
        return amountWon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bet bet = (Bet) o;

        if (numberBet != null ? !numberBet.equals(bet.numberBet) : bet.numberBet != null) return false;
        if (!amountBet.equals(bet.amountBet)) return false;
        return mode == bet.mode;
    }

    @Override
    public int hashCode() {
        int result = numberBet != null ? numberBet.hashCode() : 0;
        result = 31 * result + amountBet.hashCode();
        result = 31 * result + (mode != null ? mode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Bet{" +
                "numberBet=" + numberBet +
                ", amountBet=" + amountBet +
                ", mode=" + mode +
                '}';
    }
}
