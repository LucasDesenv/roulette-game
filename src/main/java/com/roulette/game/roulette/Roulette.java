package com.roulette.game.roulette;

import com.roulette.game.player.Bet;
import com.roulette.game.player.Player;
import com.roulette.game.player.PlayerRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by lusouza on 29/06/18.
 */
public class Roulette extends Thread{
    private Integer numberRolled;
    private RoundThread roundThread;
    private static final PrimitiveIterator.OfInt NUMBERS = new Random().ints(1,36).iterator();
    private Integer numberOfRounds;
    private List<Player> players;

    public Roulette() {
        numberOfRounds = Integer.MAX_VALUE;
        this.players = PlayerRepository.getPlayers();
    }

    public Roulette(Integer numberOfRounds) {
        this();
        this.numberOfRounds = numberOfRounds;
    }

    List<Player> getPlayers() {
        return players;
    }

    @Override
    public void run() {
        startBets();
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                executeTask();
            }
        }, 10000, 10000);
    }

    void executeTask() {
        roundThread.stopRunning();
        rollBall();
        markWinners();
        printBets();
        if (--numberOfRounds <= 0){
            printPlayersFinalResult();
            System.exit(0);
        }
        restartBets();
        roundThread.interrupt();
        startBets();
    }

    void restartBets() {
        getPlayers().forEach(Player::reset);
    }

    void printPlayersFinalResult() {
        System.out.println();
        System.out.println(" # Total Win x Bet #");
        System.out.println();

        final List<Player> players = getPlayers();
        final Object[][] table = new String[players.size() + 2][];
        table[0] = new String[]{"Player","Total Win","Total Bet"};
        table[1] = new String[]{"-","-","-"};
        final AtomicInteger tableIndex = new AtomicInteger(2);

        players.forEach(player -> {
            table[tableIndex.getAndIncrement()] = new String[]{ player.getNickName(), player.getTotalWin().toString(), player.getTotalBet().toString()};
        });

        for (final Object[] row : table) {
            if (row != null)
                System.out.format("%15s%15s%15s\n", row);
        }
    }

    void rollBall() {
        numberRolled = generateRandomNumber();
        System.out.printf("\nNumber: %d%n", numberRolled);
    }

    int generateRandomNumber() {
        return NUMBERS.nextInt();
    }

    void startBets() {
        roundThread = new RoundThread();
        roundThread.start();
    }

    void printBets() {
        final List<Bet> allBets = getPlayers().stream().map(Player::getBets).flatMap(List::stream).collect(Collectors.toList());
        final Object[][] table = new String[allBets.size() + 2][];
        table[0] = new String[]{"Player","Bet","Outcome", "Winnings"};
        table[1] = new String[]{"-","-","-", "-"};
        final AtomicInteger tableIndex = new AtomicInteger(2);

        getPlayers().forEach(player -> {
            player.getBets().forEach(bet -> {
                final String winLoseLabel = bet.hasWon() ? "WIN" : "LOSE";
                final String betLabel = bet.getNumberBet().isPresent() ? bet.getNumberBet().get().toString() : bet.getMode().toString();

                table[tableIndex.getAndIncrement()] = new String[]{ player.getNickName(), betLabel, winLoseLabel, String.valueOf(bet.getAmountWon())};
            });
        });

        for (final Object[] row : table) {
            if (row != null)
                System.out.format("%15s%15s%15s%15s\n", row);
        }
    }

    void markWinners() {
        final boolean rolledAnEven = numberRolled % 2 == 0;
        final boolean rolledAnOdd = !rolledAnEven;

        getPlayers().forEach(player -> {
            player.getBets().forEach(bet -> {
                final boolean wonByNumber = numberRolled.equals(bet.getNumberBet().orElse(null));
                final boolean wonByMode = (bet.isOdd() && rolledAnOdd) || (bet.isEven() && rolledAnEven);
                if (wonByNumber){
                    bet.wonByNumber();
                }
                if (wonByMode){
                    bet.wonByMode();
                }
            });
            player.summarizeTotals();
        });
    }

}
