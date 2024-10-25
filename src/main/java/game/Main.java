package game;

import players.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nДобро пожаловать в игру Перудо.");
        label:
        while (true) {
            System.out.println("""           
                    1. Начать игру
                    2. Правила игры
                    3. Выйти
                    """);
            int choice;
            try {
                System.out.print("Выбор: ");
                choice = sc.nextInt();

                List<Player> players = new ArrayList<>();

                switch (choice) {
                    case 1:
                        // Количество игроков
                        int numberOfPlayers = getNumberOfPlayers();

                        // Задать имя компьютерам
                        String[] namePlayersArray = generateRandomName(numberOfPlayers);
                        // Задать имя игроку и добавить в массив
                        namePlayersArray[numberOfPlayers - 1] = getNamePlayers();
                        String namePlayer = namePlayersArray[numberOfPlayers - 1]; // Запомнить кто является игроков, среди компьютеров

                        // Начальные параметры игры
                        int[] diceCountsArray = initializeGame(namePlayersArray);
                        // Старт игры
                        startGame(namePlayersArray, diceCountsArray, numberOfPlayers);
                        break;
                    case 2:
                        getGameRules();
                        break;
                    case 3:
                        System.out.println("До свидания!");
                        break label;
                    default:
                        System.out.println("Не выбран ни один из вариантов. Повторите попытку\n");
                        break;
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Ошибка! Ввод должен быть числом. Попробуйте снова.\n");
                sc.nextLine();
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
    private static void startGame(String[] namePlayersArray, int[] diceCountsArray, int numberOfPlayers) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();
        boolean hasWinner = false;
        String humanName = namePlayersArray[numberOfPlayers - 1];
        Player[] players = new Player[numberOfPlayers];

        // Жеребьёвка
        System.out.println("Начало игры. Жеребьёвка:");
        while (!hasWinner) {
            int maxRoll = 0;
            List<String> playersWithMaxRoll = new ArrayList<>(); // Массив игроков, для переигровки

            // Процесс жеребьёвки
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

                // Добавить каждому игроку идентификатор "бот" или "человек"
                for (int i = 0; i < namePlayersArray.length; i++) {
                    if (namePlayersArray[i].equals(humanName)) {
                        players[i] = new Player(namePlayersArray[i], true);
                    } else {
                        players[i] = new Player(namePlayersArray[i], false);
                    }
                }

            } else {
                System.out.println("\nНичья! Переигровка между игроками: " + playersWithMaxRoll);
                // Переигровка среди игроков с максимальным значением
                namePlayersArray = playersWithMaxRoll.toArray(new String[0]);
            }
        }


        // Ход первого игрока
        int whoMakeMove = 0;
        boolean gameContinues = true;
        int quantity = 0;
        int value = 0;
        int diceCount = numberOfPlayers * 5;

        while (gameContinues) {
            for (int i = 0; i < namePlayersArray.length; i++) {
                String currentPlayer = namePlayersArray[i];
                System.out.println("Ходит игрок " + currentPlayer);

                // Каждый бросает свои кости, не показывая другим
                int[][] results = rollDice(namePlayersArray, diceCountsArray); // Получаем результаты

                // Вывод всех выпавших костей игрока
                int playerIndex = 0; // Индекс игрока (например, Игрок 1)
                System.out.print("Кости " + namePlayersArray[playerIndex] + ": ");
                for (int valueD : results[playerIndex]) {
                    System.out.print(valueD + " ");
                }

                // Если игрок человек
                if (players[i].isHuman()) {
                    quantity = sc.nextInt();
                    value = sc.nextInt();
                }
                // Если игрок бот
                else {
                    System.out.println("bot");
                    // Ход бота: 60% повышение и 40% не верю. Если некуда повышать - не верю
                    int choiceChance = rand.nextInt(0, 100);
                    if (choiceChance < 60) {
                        if (diceCount != 0) {
                            quantity += 1;
                            value = rand.nextInt(1, 7);
                        } else {
                            System.out.println("Не верю");
                        }
                    } else {
                        System.out.println("Не верю");
                    }
                }
            }
            break;
        }
    }

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

        // Выводим результаты. Не отображается в самой игре.
        for (int i = 0; i < namePlayersArray.length; i++) {
            System.out.print(namePlayersArray[i] + ": ");
            for (int value : diceValues[i]) {
                System.out.print(value + " ");
            }
            System.out.println();
        }

        return diceValues;
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
                    System.out.println("Недопустимое количество игроков. Попробуйте снова.\n");
                }
            } catch (Exception e) {
                System.out.println("Ошибка! Попробуйте снова.\n");
                sc.nextLine();
            }
        }
        return numberOfPlayers;
    }

    // Генерация имён для компьютеров
    public static String[] generateRandomName(int numberOfPlayers) {
        Random rnd = new Random();

        // Массив имён для генерации
        String[] nameGeneration = {"Константин", "Максим", "Дарья", "Виктория", "Артур", "Тимофей", "Алексей", "Иван", "Дмитрий", "Мария", "Станислав", "Никита"};

        // Пустой массив имён для компьютеров
        String[] nameComputerArray = new String[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers - 1; i++) {
            nameComputerArray[i] = nameGeneration[rnd.nextInt(nameGeneration.length)] + rnd.nextInt(0, 100000);
        }

        return nameComputerArray;
    }

    // Ввод или генерация имён для игрока
    public static String getNamePlayers() {
        Scanner sc = new Scanner(System.in);
        Random rnd = new Random();

        // Массив имён для генерации
        String[] nameGeneration = {"Константин", "Максим", "Дарья", "Виктория", "Артур", "Тимофей", "Алексей", "Иван", "Дмитрий", "Мария", "Станислав", "Никита"};

        System.out.println("\nПридумайте или сгенерируйте имя");

        // Выбор имени для игрока
        String namePlayer = "";
        String choice;
        while (true) {
            System.out.println("""
                    a. Написать имя
                    b. Сгенерировать имя""");
            System.out.print("Выбор: ");
            choice = sc.nextLine();

            if (choice.equals("a")) {
                namePlayer = sc.nextLine();
                break;
            } else if (choice.equals("b")) {
                namePlayer = nameGeneration[rnd.nextInt(nameGeneration.length)] + rnd.nextInt(0, 100000);
                break;
            } else {
                System.out.println("Не выбран ни один из вариантов. Повторите попытку\n");
            }
        }

        return namePlayer;
    }
}