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
            } catch (InputMismatchException e) {
                System.out.println("Ошибка! Ввод должен быть числом. Попробуйте снова.\n");
                sc.nextLine();
            }
        }
    }

    // Начальные параметры игры, у каждого игрока по 5 костей
    private static int[] initializeGame(String[] namePlayersArray) {
        int[] diceCountsArray = new int[namePlayersArray.length];
        for (int i = 0; i < namePlayersArray.length; i++) {
            diceCountsArray[i] = 1;
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
        int previousBidQuantity = 0;
        int previousBidValue = 1;
        int lastPlayerWhoSaidNoBelieve = -1; // Индекс игрока, который сказал "Не верю"
        int lastPlayerWhoMadeBid = -1; // Индекс игрока, который сделал ставку

        // Жеребьёвка
        System.out.println("\nНачало игры. Жеребьёвка:");
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
                System.out.println("\nПобедитель: " + playersWithMaxRoll.get(0) + "\n\n");

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

        System.out.println("Начало игры!");
        // Ход игроков
        boolean gameContinues = true;
        boolean firstMove = true;
        int[][] diceValues = null;

        // Бросок костей
        diceValues = rollDice(namePlayersArray, diceCountsArray);
        int[] numberDifferentDice = countDiffDice(namePlayersArray, diceValues);

        // Сумма костей всех игроков
        System.out.print("00 Сумма костей игроков: " + Arrays.toString(numberDifferentDice) + "\n");

        while (gameContinues) {
            for (int i = 0; i < namePlayersArray.length; i++) {
                String currentPlayer = namePlayersArray[i];
                System.out.println("\nХодит игрок " + currentPlayer);

                // Логика ставок
                int quantity = 0;
                int value = 1;
                boolean saidNoBelieve = false;

                // Ставки
                assert players[i] != null;
                if (players[i] != null & players[i].isHuman()) {
                    // Ввод ставки от человека
                    boolean validBid = false;
                    while (!validBid) {
                        if (firstMove) {
                            firstMove = false;

                            System.out.print("Введите количество костей и номинал: ");
                            quantity = sc.nextInt();
                            value = sc.nextInt();
                        } else {
                            System.out.println("Введите количество костей и номинал (или '0 0' для 'Не верю')");
                            System.out.print("Количество костей: ");
                            quantity = sc.nextInt();
                            System.out.print("Номинал: ");
                            value = sc.nextInt();

                            if (quantity == 0 && value == 0) {
                                lastPlayerWhoSaidNoBelieve = i; // Игрок говорит "Не верю"
                                System.out.println(currentPlayer + " говорит: Не верю!");
                                saidNoBelieve = true;
                                break;
                            }
                        }

                        // Проверка на корректность ставки
                        if (isValidBid(quantity, value, previousBidQuantity, previousBidValue, firstMove)) {
                            validBid = true;
                            lastPlayerWhoMadeBid = i; // Запоминаем индекс игрока, который сделал ставку
                        } else {
                            System.out.println("Некорректная ставка. Попробуйте снова.");
                        }
                    }
                } else {
                    // Логика для бота
                    if (firstMove) {
                        firstMove = false;

                        // Генерация новой ставки
                        quantity = previousBidQuantity + 1;
                        value = rand.nextInt(1, 7);
                        if (quantity > diceCountsArray[i]) {
                            quantity = previousBidQuantity;
                        }

                        System.out.println(currentPlayer + ": " + quantity + " " + value);
                        lastPlayerWhoMadeBid = i;
                    } else{
                        if (rand.nextDouble() < 0.4) { // 40% вероятность сказать "Не верю!"
                            System.out.println(currentPlayer + " говорит: Не верю!");
                            saidNoBelieve = true;
                            lastPlayerWhoSaidNoBelieve = i;
                        } else {
                            // Генерация новой ставки
                            quantity = previousBidQuantity + 1;
                            value = previousBidValue;
                            if (quantity > diceCountsArray[i]) {
                                quantity = previousBidQuantity;
                            }

                            System.out.println(currentPlayer + ": " + quantity + " " + value);
                            lastPlayerWhoMadeBid = i;
                        }
                    }
                }

                if (saidNoBelieve) {
                    // Логика вскрытия костей
                    System.out.println(previousBidQuantity + " " + previousBidValue);
                    boolean isTruth = checkTruth(previousBidValue, previousBidQuantity, numberDifferentDice);
                    int losingPlayerIndex = -1;

                    if (isTruth) {
                        // Если ставка была правдой, игрок, который сказал "Не верю", теряет кость
                        losingPlayerIndex = findPlayerIndexWhoSaidNoBelieve(players, namePlayersArray, lastPlayerWhoSaidNoBelieve);
                        diceCountsArray[losingPlayerIndex]--;
                        firstMove = true;
                        System.out.println("Ставка была правдой. " + namePlayersArray[losingPlayerIndex] + " теряет кость. Осталось: " + diceCountsArray[losingPlayerIndex]);
                    } else {
                        // Если ставка была ложной, игрок, который сделал ставку, теряет кость
                        losingPlayerIndex = findPlayerIndexWhoMadeBid(players, namePlayersArray, lastPlayerWhoMadeBid);
                        diceCountsArray[losingPlayerIndex]--;
                        firstMove = true;
                        System.out.println("Ставка была ложной. " + namePlayersArray[losingPlayerIndex] + " теряет кость. Осталось: " + diceCountsArray[losingPlayerIndex]);
                    }

                    // Проверка на наличие проигравшего
                    if (diceCountsArray[losingPlayerIndex] <= 0) {
                        System.out.println(namePlayersArray[losingPlayerIndex] + " больше не имеет костей и выбывает из игры!");

                        if (players[losingPlayerIndex].isHuman()){
                            gameContinues = false;
                            System.out.println("Игра окончена.");
                            break;
                        }

                        // Удаляем игрока из игры
                        players[losingPlayerIndex] = null;
                        numberOfPlayers--;
                        // Проверяем, остался ли только один игрок
                        if (numberOfPlayers == 1) {
                            System.out.println("Поздравляем! " + namePlayersArray[findLastPlayer(diceCountsArray)] + " победил!");
                            hasWinner = true; // Завершаем игру
                        }
                    }
                    // Бросок костей
                    diceValues = rollDice(namePlayersArray, diceCountsArray);
                    numberDifferentDice = countDiffDice(namePlayersArray, diceValues);

                    // Сумма костей всех игроков
                    System.out.print("00 Сумма костей игроков: " + Arrays.toString(numberDifferentDice) + "\n");
                }

                // Обновляем предыдущую ставку
                previousBidQuantity = quantity;
                previousBidValue = value;
            }
        }
    }

    // Метод для нахождения последнего игрока
    private static int findLastPlayer(int[] diceCountsArray) {
        for (int i = 0; i < diceCountsArray.length; i++) {
            if (diceCountsArray[i] > 0) {
                return i; // Возвращаем индекс последнего игрока с костями
            }
        }
        return -1; // Не найден игрок
    }

    // Пример функции для проверки правоты ставки
    private static boolean
    checkTruth(int bidValue, int bidQuantity, int[] numberDifferentDice) {
        return bidQuantity >= numberDifferentDice[bidValue-1];
    }

    // Метод для проверки корректности ставки
    private static boolean isValidBid(int quantity, int value, int previousBidQuantity, int previousBidValue, boolean firstMove) {
        // Ставка должна быть выше предыдущей
        if ((quantity > previousBidQuantity || (quantity == previousBidQuantity && value != previousBidValue)) & quantity <= 6 & quantity >= 1 & !firstMove) {
            return true;
        }
        return false;
    }

    // Функция для нахождения индекса игрока, который сказал "Не верю"
    private static int findPlayerIndexWhoSaidNoBelieve(Player[] players, String[] namePlayersArray, int lastPlayerWhoSaidNoBelieve) {
        return lastPlayerWhoSaidNoBelieve;
    }


    // Функция для нахождения индекса игрока, который сделал ставку
    private static int findPlayerIndexWhoMadeBid(Player[] players, String[] namePlayersArray, int lastPlayerWhoMadeBid) {
        return lastPlayerWhoMadeBid;
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

//        // Выводим результаты. Часть кода для отладки
//        for (int i = 0; i < namePlayersArray.length; i++) {
//            System.out.print(namePlayersArray[i] + ": ");
//            for (int value : diceValues[i]) {
//                System.out.print(value + " ");
//            }
//            System.out.println();
//        }

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
        int choice;
        while (true) {
            System.out.println("""
                    1. Написать имя
                    2. Сгенерировать имя""");
            System.out.print("Выбор: ");
            choice = sc.nextInt();

            if (choice == 1) {
                namePlayer = "TESTPLAYER";
                // namePlayer = sc.nextLine();
                break;
            } else if (choice == 2) {
                namePlayer = nameGeneration[rnd.nextInt(nameGeneration.length)] + rnd.nextInt(0, 100000);
                break;
            } else {
                System.out.println("Не выбран ни один из вариантов. Повторите попытку\n");
            }
        }
        return namePlayer;
    }
}