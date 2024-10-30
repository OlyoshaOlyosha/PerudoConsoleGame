package players;

import java.util.Random;
import java.util.Scanner;

public class Player {
    private final String name;
    private final boolean isHuman;

    public Player (String name, boolean isHuman) {
        this.name = name;
        this.isHuman = isHuman;
    }

    @Override
    public String toString() {
        return name + " " + isHuman;
    }

    public boolean isHuman() {
        return isHuman;
    }

    // Указать количество игроков
    public static int getNumberOfPlayers() {
        Scanner sc = new Scanner(System.in);
        int numberOfPlayers;
        while (true) {
            try {
                System.out.println("\nСколько будет игроков? (от 2 до 4):");
                numberOfPlayers = sc.nextInt();

                if (numberOfPlayers >= 2 && numberOfPlayers <= 4) {
                    break;
                } else {
                    System.out.println("Недопустимое количество игроков. Попробуйте снова.\n");
                }
            } catch (Exception e) {
                System.out.println("Ошибка! Попробуйте снова.\n");
                sc.nextLine();
            }
        }
        return numberOfPlayers;
    }

    // Метод для нахождения последнего игрока
    public static int findLastPlayer(int[] diceCountsArray) {
        for (int i = 0; i < diceCountsArray.length; i++) {
            if (diceCountsArray[i] > 0) {
                return i; // Возвращаем индекс последнего игрока с костями
            }
        }
        return -1; // Не найден игрок
    }

    // Бросок костей каждого игрока
    public static int[][] rollDice(String[] namePlayersArray, int[] diceCountsArray) {
        int[][] diceValues = new int[namePlayersArray.length][];
        Random rand = new Random();

        // Генерируем кости для каждого игрока
        for (int i = 0; i < namePlayersArray.length; i++) {
            diceValues[i] = new int[diceCountsArray[i]];
            for (int j = 0; j < diceCountsArray[i]; j++) {
                diceValues[i][j] = rand.nextInt(6) + 1;
            }
        }

        return diceValues;
    }

    // Считаем количество номинала кости каждого игрока в сумме
    public static int[] countDiffDice(String[] namePlayersArray, int[][] diceValues) {
        int[] numberDifferentDice = {0, 0, 0, 0, 0, 0};
        for (int i = 0; i < namePlayersArray.length; i++) {
            for (int value : diceValues[i]) {
                switch (value) {
                    case 1:
                        numberDifferentDice[0]++;
                        break;
                    case 2:
                        numberDifferentDice[1]++;
                        break;
                    case 3:
                        numberDifferentDice[2]++;
                        break;
                    case 4:
                        numberDifferentDice[3]++;
                        break;
                    case 5:
                        numberDifferentDice[4]++;
                        break;
                    case 6:
                        numberDifferentDice[5]++;
                        break;
                }
            }
        }
        return numberDifferentDice;
    }

    // Считаем количество костей у игроков
    public static int countDicePlayers(String[] namePlayersArray, int[][] diceValues) {
        int countDice = 0;
        for (int i = 0; i < namePlayersArray.length; i++) {
            for (int _ : diceValues[i]) {
                countDice++;
            }
        }
        return countDice;
    }

}
