package game;

import players.Player;
import players.ComputerPlayer;
import players.HumanPlayer;

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

                switch (choice) {
                    case 1:
                        // Количество игроков
                        int numberOfPlayers = Player.getNumberOfPlayers();

                        // Задать имя компьютерам
                        String[] namePlayersArray = ComputerPlayer.generateRandomName(numberOfPlayers);
                        // Задать имя игроку и добавить в массив
                        namePlayersArray[numberOfPlayers - 1] = HumanPlayer.getNamePlayers();

                        // Начальные параметры игры
                        int[] diceCountsArray = GameInitializer.initializeGame(namePlayersArray);

                        // Старт игры
                        StartGame.startGame(namePlayersArray, diceCountsArray, numberOfPlayers);
                        break;
                    case 2:
                        GameInitializer.getGameRules();
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
}