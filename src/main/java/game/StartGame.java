package game;

import players.Player;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class StartGame {
    // Начало игры, цикл
    public static void startGame(String[] namePlayersArray, int[] diceCountsArray, int numberOfPlayers) {
        Scanner sc = new Scanner(System.in);
        Random rnd = new Random();
        int previousBidQuantity = 0;
        int previousBidValue = 1;
        int lastPlayerWhoSaidNoBelieve = -1; // Индекс игрока, который сказал "Не верю"
        int lastPlayerWhoMadeBid = -1; // Индекс игрока, который сделал ставку

        // Определяем первого игрока
        int winnerIndex = GameInitializer.rollForFirstPlayer(namePlayersArray);
        // Сдвигаем победителя на первый индекс массива
        GameInitializer.shiftPlayersOrder(namePlayersArray, winnerIndex);
        // Инициализация игроков
        String humanName = namePlayersArray[numberOfPlayers - 1];
        Player[] players = GameInitializer.initializePlayers(namePlayersArray, humanName);

        System.out.println("Начало игры!");
        // Ход игроков
        boolean gameContinues = true;
        boolean firstMove = true;

        int[][] diceValues = initializeGameSetup(namePlayersArray, diceCountsArray);
        int[] numberDifferentDice = Player.countDiffDice(namePlayersArray, diceValues);

        while (gameContinues) {
            for (int i = 0; i < namePlayersArray.length; i++) {
                if (players[i] != null) {
                    String currentPlayer = namePlayersArray[i];
                    System.out.println("\nХодит игрок: " + currentPlayer);

                    // Логика ставок
                    int quantity = 0;
                    int value = 1;
                    boolean saidNoBelieve = false;
                    int countDice = Player.countDicePlayers(namePlayersArray, diceValues);

                    if (players[i].isHuman()) {
                        // Ввод ставки от человека
                        boolean validBid = false;
                        while (!validBid) {
                            try {
                                if (firstMove) {
                                    firstMove = false;

                                    System.out.println("Введите количество костей и номинал: ");

                                    System.out.print("Количество: ");
                                    quantity = Bid.getValidInput(sc);
                                    System.out.print("Номинал: ");
                                    value = Bid.getValidInput(sc);
                                } else {
                                    System.out.println("Введите количество костей и номинал (или '0' и '0' для 'Не верю')");

                                    System.out.print("Количество: ");
                                    quantity = Bid.getValidInput(sc);
                                    System.out.print("Номинал: ");
                                    value = Bid.getValidInput(sc);

                                    if (quantity == 0 && value == 0) {
                                        lastPlayerWhoSaidNoBelieve = i; // Индекс игрока, который говорит "Не верю!"
                                        System.out.println(currentPlayer + " говорит: Не верю!");
                                        saidNoBelieve = true;
                                        break;
                                    }
                                }

                                // Проверка на корректность ставки
                                if (Bid.isValidBid(quantity, value, previousBidQuantity, previousBidValue, firstMove, countDice)) {
                                    lastPlayerWhoMadeBid = i; // Запоминаем индекс игрока, который сделал ставку
                                    validBid = true;
                                } else {
                                    System.out.println("Некорректная ставка. Попробуйте снова.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Ошибка! Ввод должен быть числом. Попробуйте снова.\n");
                                sc.nextLine();
                            }
                        }
                    } else {
                        // Логика для бота
                        if (firstMove) {
                            firstMove = false;

                            // Генерация новой ставки
                            quantity = previousBidQuantity + 1;
                            value = rnd.nextInt(1, 7);
                            if (quantity > diceCountsArray[i]) {
                                quantity = previousBidQuantity;
                            }

                            System.out.println(currentPlayer + ": " + quantity + " " + value);
                            lastPlayerWhoMadeBid = i;
                        } else {
                            if (rnd.nextDouble() < 0.4 | previousBidQuantity + 1 > diceCountsArray[i]) { // 40% вероятность сказать "Не верю!" или если повышать ставку некуда
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
                        boolean isTruth = Bid.checkTruth(previousBidValue, previousBidQuantity, numberDifferentDice);

                        if (isTruth) {
                            // Если ставка была правдой, игрок, который сказал "Не верю", теряет кость
                            diceCountsArray[lastPlayerWhoSaidNoBelieve]--;
                            firstMove = true;
                            System.out.println("Ставка была правдой. " + namePlayersArray[lastPlayerWhoSaidNoBelieve] + " теряет кость. Осталось: " + diceCountsArray[lastPlayerWhoSaidNoBelieve]);
                        } else {
                            // Если ставка была ложной, игрок, который сделал ставку, теряет кость
                            lastPlayerWhoSaidNoBelieve = lastPlayerWhoMadeBid;
                            diceCountsArray[lastPlayerWhoSaidNoBelieve]--;
                            firstMove = true;
                            System.out.println("Ставка была ложной. " + namePlayersArray[lastPlayerWhoSaidNoBelieve] + " теряет кость. Осталось: " + diceCountsArray[lastPlayerWhoSaidNoBelieve]);
                        }

                        // Проверка на наличие проигравшего
                        if (diceCountsArray[lastPlayerWhoSaidNoBelieve] <= 0) {
                            System.out.println(namePlayersArray[lastPlayerWhoSaidNoBelieve] + " больше не имеет костей и выбывает из игры!");

                            assert players[lastPlayerWhoSaidNoBelieve] != null;
                            if (players[lastPlayerWhoSaidNoBelieve].isHuman()) {
                                gameContinues = false;
                                System.out.println("Игра окончена.");
                                break;
                            }

                            // Удаляем игрока из игры
                            players[lastPlayerWhoSaidNoBelieve] = null;
                            numberOfPlayers--;
                            // Проверяем, остался ли только один игрок
                            if (numberOfPlayers == 1) {
                                System.out.println("Поздравляем! " + namePlayersArray[Player.findLastPlayer(diceCountsArray)] + " победил!");
                                break;
                            }
                        }
                        diceValues = initializeGameSetup(namePlayersArray, diceCountsArray);
                        numberDifferentDice = Player.countDiffDice(namePlayersArray, diceValues);
                    }

                    // Обновляем предыдущую ставку
                    previousBidQuantity = quantity;
                    previousBidValue = value;
                }
            }
        }
    }

    // Основная настройка игры, включая бросок костей для всех игроков и вывод общей суммы костей
    public static int[][] initializeGameSetup(String[] namePlayersArray, int[] diceCountsArray) {
        return Player.rollDice(namePlayersArray, diceCountsArray);
    }
}
