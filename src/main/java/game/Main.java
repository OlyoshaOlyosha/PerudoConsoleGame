package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

            switch (choice) {
                case "a":
                    int amountPlayers = amountPlayers();
                    namePlayers(amountPlayers);
                    break;
                case "b":
                    // Ошибка
                    getGameRules();
                    break;
                case "c":
                    break label;
            }
        }

    }
    // Вывести правила в консоль
    public static void getGameRules() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/game/rules.txt"));
        String rules;
        while ((rules = br.readLine()) != null) {
            System.out.println(rules);
        }
        br.close();
    }
    // Указать количество игроков
    public static int amountPlayers() {
        Scanner sc = new Scanner(System.in);
        int amountPlayers;
        while (true) {
            try {
                System.out.println("Сколько будет игроков? (от 2 до 4):");
                amountPlayers = sc.nextInt();

                if (amountPlayers >= 2 && amountPlayers <= 4) {
                    break;
                } else {
                    System.out.println("Недопустимое количество игроков. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка! Попробуйте снова.");
            }
        }
        return amountPlayers;
    }
    // Указать каждому игроку имя в игре
    public static void namePlayers(int amountPlayers) {
        Scanner sc = new Scanner(System.in);
        Random rnd = new Random();

        String choice;

        // Массив имён для генерации
        String[] nameGeneration  = {"Константин", "Максим", "Дарья", "Виктория", "Артур", "Тимофей", "Алексей", "Иван", "Дмитрий", "Мария", "Станислав", "Никита"};

        // Пустой массив имён для компьютеров
        String[] nameComputerArray = new String[amountPlayers-1];

        System.out.println("Придумайте или сгенерируйте имя");
        // Выбор имени для игрока
        String namePlayer = "";
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

        // Генерация имён для компьютеров
        for (int i = 0; i < amountPlayers-1; i++){
            nameComputerArray[i] = nameGeneration[rnd.nextInt(nameGeneration.length)] + rnd.nextInt(0, 100000);
        }
        System.out.println(namePlayer);
        for (int i = 0; i < amountPlayers-1; i++) {
            System.out.println(nameComputerArray[i]);
        }
    }
}