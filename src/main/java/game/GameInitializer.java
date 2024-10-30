package game;

import players.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameInitializer {
    // Начальные параметры игры, у каждого игрока по 5 костей
    public static int[] initializeGame(String[] namePlayersArray) {
        int[] diceCountsArray = new int[namePlayersArray.length];
        for (int i = 0; i < namePlayersArray.length; i++) {
            diceCountsArray[i] = 5;
        }
        return diceCountsArray;
    }

    // Вывести правила в консоль
    public static void getGameRules() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/main/java/game/rules.txt"));
        String rules;
        while ((rules = br.readLine()) != null) {
            System.out.println(rules);
        }
        br.close();
    }

    // Жеребьёвка между игроками
    public static int rollForFirstPlayer(String[] namePlayersArray) {
        Random rnd = new Random();
        boolean hasWinner = false;
        int winnerIndex = -1;

        System.out.println("\nЖеребьёвка!");
        while (!hasWinner) {
            int maxRoll = 0;
            List<String> playersWithMaxRoll = new ArrayList<>();

            for (String player : namePlayersArray) {
                int roll = rnd.nextInt(6) + 1;
                System.out.println("У " + player + " выпало: " + roll);

                // Определяем максимальное значение и игроков с ним
                if (roll > maxRoll) {
                    maxRoll = roll;
                    playersWithMaxRoll.clear();
                    playersWithMaxRoll.add(player);
                } else if (roll == maxRoll) {
                    playersWithMaxRoll.add(player);
                }
            }

            if (playersWithMaxRoll.size() == 1) {
                hasWinner = true;
                String winner = playersWithMaxRoll.getFirst();
                System.out.println("\nПобедитель жеребьёвки: " + winner + "\n");

                // Найти индекс победителя
                winnerIndex = Arrays.asList(namePlayersArray).indexOf(winner);
            } else {
                System.out.println("\nНичья! Переигровка среди игроков: " + playersWithMaxRoll);
                namePlayersArray = playersWithMaxRoll.toArray(new String[0]);
            }
        }
        return winnerIndex;
    }

    // Сдвинуть победителя на первый индекс
    public static void shiftPlayersOrder(String[] namePlayersArray, int winnerIndex) {
        if (winnerIndex > 0) {
            String winner = namePlayersArray[winnerIndex];
            System.arraycopy(namePlayersArray, 0, namePlayersArray, 1, winnerIndex);
            namePlayersArray[0] = winner;
        }
    }

    // Добавить каждому игроку идентификатор "бот" или "человек"
    public static Player[] initializePlayers(String[] namePlayersArray, String humanName) {
        Player[] players = new Player[namePlayersArray.length];
        for (int i = 0; i < namePlayersArray.length; i++) {
            boolean isHuman = namePlayersArray[i].equals(humanName);
            players[i] = new Player(namePlayersArray[i], isHuman);
        }
        return players;
    }
}
