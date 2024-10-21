package game;

import players.ComputerPlayer;
import players.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

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
                    // Список игроков
                    int numberOfPlayers = getNumberOfPlayers();

                    // Задать имя компьютерам
                    String[] namePlayersArray = generateRandomName(numberOfPlayers);
                    // Задать имя игроку
                    namePlayersArray[numberOfPlayers-1] = getNamePlayers();


                    for (int i = 0; i < numberOfPlayers; i++){
                        System.out.println(namePlayersArray[i]);
                    }
                    break;
                case "b":
                    getGameRules();
                    break;
                case "c":
                    break label;
            }
        }

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
                System.out.println("Сколько будет игроков? (от 2 до 4):");
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

        System.out.println("Придумайте или сгенерируйте имя");
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