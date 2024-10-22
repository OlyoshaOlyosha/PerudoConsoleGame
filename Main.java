package game;

import players.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        label:
        while (true) {
            System.out.println("""
                    
                    
                    a. Начать игру
                    b. Правила игры
                    c. Выйти""");
            String choice;
            Scanner sc = new Scanner(System.in);
            choice = sc.nextLine().substring(0, 1);

            List<Player> players = new ArrayList<>();

            switch (choice) {
                case "a":
                    // Количество игроков
                    int numberOfPlayers = getNumberOfPlayers();

                    // Задать имя компьютерам
                    String[] namePlayersArray = generateRandomName(numberOfPlayers);
                    // Задать имя игроку
                    namePlayersArray[numberOfPlayers-1] = getNamePlayers();

                    // Начальные параметры игры
                    int[] diceCountsArray = initializeGame(namePlayersArray);
                    // Старт игры
                    startGame(namePlayersArray, diceCountsArray);
                    break;
                case "b":
                    getGameRules();
                    break;
                case "c":
                    break label;
            }
        }

    }
    // Начальные параметры игры, у каждого игрока по 5 костей
    private static int[] initializeGame(String[] namePlayersArray) {
        int[] diceCountsArray = new int[namePlayersArray.length];
        for (int i = 0; i < namePlayersArray.length; i++) {
            diceCountsArray[i] = 5;
        }
        return diceCountsArray;
    }

    // Начало игры, цикл
    private static void startGame(String[] namePlayersArray, int[] diceCountsArray) {
        Random rand = new Random();
        boolean hasWinner = false;

        // Жеребьёвка
        System.out.println("Начало игры. Жеребьёвка:");
        while (!hasWinner) {
            int maxRoll = 0;
            List<String> playersWithMaxRoll = new ArrayList<>(); // Массив игроков, для переигровки

            for (String player : namePlayersArray) {
                int number = rand.nextInt(1, 7);
                System.out.println("У " + player + " выпало: " + number);

                // Определяем максимальное значение и игроков с ним
                if (number > maxRoll) {
                    maxRoll = number;
                    playersWithMaxRoll.clear();
                    playersWithMaxRoll.add(player);
                } else if (number == maxRoll) {
                    playersWithMaxRoll.add(player);
                }
            }

            // Проверка на наличие победителя
            if (playersWithMaxRoll.size() == 1) {
                hasWinner = true;
                System.out.println("\nПобедитель: " + playersWithMaxRoll.get(0));

                // Получить индекс игрока по имени
                int winnerIndex = 0;
                for (int i = 0; i < namePlayersArray.length; i++) {
                    if (namePlayersArray[i].equals(playersWithMaxRoll.get(0))) {
                        winnerIndex = i;
                    }
                }

                // Сдвинуть победителя на первый индекс
                for (int i = winnerIndex; i > 0; i--) {
                    namePlayersArray[i] = namePlayersArray[i - 1];
                }
                namePlayersArray[0] = playersWithMaxRoll.get(0);

            } else {
                System.out.println("\nНичья! Переигровка между игроками: " + playersWithMaxRoll);
                // Переигровка среди игроков с максимальным значением
                namePlayersArray = playersWithMaxRoll.toArray(new String[0]);
            }
        }

        // Ход первого игрока

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
                    System.out.println("Недопустимое количество игроков. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка! Попробуйте снова.");
            }
        }
        return numberOfPlayers;
    }

    // Генерация имён для компьютеров
    public static String[] generateRandomName(int numberOfPlayers) {
        Random rnd = new Random();

        // Массив имён для генерации
        String[] nameGeneration  = {"Константин", "Максим", "Дарья", "Виктория", "Артур", "Тимофей", "Алексей", "Иван", "Дмитрий", "Мария", "Станислав", "Никита"};

        // Пустой массив имён для компьютеров
        String[] nameComputerArray = new String[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers-1; i++){
            nameComputerArray[i] = nameGeneration[rnd.nextInt(nameGeneration.length)] + rnd.nextInt(0, 100000);
        }

        return nameComputerArray;
    }

    // Ввод или генерация имён для игрока
    public static String getNamePlayers() {
        Scanner sc = new Scanner(System.in);
        Random rnd = new Random();

        // Массив имён для генерации
        String[] nameGeneration  = {"Константин", "Максим", "Дарья", "Виктория", "Артур", "Тимофей", "Алексей", "Иван", "Дмитрий", "Мария", "Станислав", "Никита"};

        System.out.println("\nПридумайте или сгенерируйте имя");
        // Выбор имени для игрока
        String namePlayer = "";

        String choice;

        try {
            while (true) {
                System.out.println("a. Написать имя\n" +
                        "b. Сгенерировать имя");

                choice = sc.nextLine().substring(0, 1);

                if (choice.equals("a")) {
                    namePlayer = sc.nextLine();
                    break;
                }
                else if (choice.equals("b")) {
                    namePlayer = nameGeneration[rnd.nextInt(nameGeneration.length)] + rnd.nextInt(0, 100000);
                    break;
                }
                else {
                    System.out.println("Не выбран ни один из вариантов. Повторите попытку");
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка! Попробуйте снова.");
        }
        return namePlayer;
    }
}