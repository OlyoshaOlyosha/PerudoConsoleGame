package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public Main() {
    }

    public static void main(String[] args) throws IOException {
        while(true) {
            System.out.println("a. Начать игру\nb. Правила игры\nc. Выйти");
            Scanner sc = new Scanner(System.in);
            switch (sc.nextLine().substring(0, 1)) {
                case "a":
                    int amountPlayers = amountPlayers();
                    namePlayers(amountPlayers);
                    break;
                case "b":
                    getGameRules();
                    break;
                case "c":
                    return;
            }
        }
    }

    public static void getGameRules() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/game/rules.txt"));

        String rules;
        while((rules = br.readLine()) != null) {
            System.out.println(rules);
        }

        br.close();
    }

    public static int amountPlayers() {
        Scanner sc = new Scanner(System.in);

        while(true) {
            while(true) {
                try {
                    System.out.println("Сколько будет игроков? (от 2 до 4):");
                    int amountPlayers = sc.nextInt();
                    if (amountPlayers >= 2 && amountPlayers <= 4) {
                        return amountPlayers;
                    }

                    System.out.println("Недопустимое количество игроков. Попробуйте снова.");
                } catch (Exception var3) {
                    System.out.println("Ошибка! Попробуйте снова.");
                }
            }
        }
    }

    public static void namePlayers(int amountPlayers) {
        Scanner sc = new Scanner(System.in);
        Random rnd = new Random();
        String[] nameGeneration = new String[]{"Константин", "Максим", "Дарья", "Виктория", "Артур", "Тимофей", "Алексей", "Иван", "Дмитрий", "Мария", "Станислав", "Никита"};
        String[] nameComputerArray = new String[amountPlayers - 1];
        System.out.println("Придумайте или сгенерируйте имя");
        String namePlayer = "";

        try {
            while(true) {
                System.out.println("a. Написать имя\nb. Сгенерировать имя");
                String choice = sc.nextLine().substring(0, 1);
                if (choice.equals("a")) {
                    namePlayer = sc.nextLine();
                    break;
                }

                if (choice.equals("b")) {
                    String var10000 = nameGeneration[rnd.nextInt(nameGeneration.length)];
                    namePlayer = var10000 + rnd.nextInt(0, 100000);
                    break;
                }

                System.out.println("Не выбран ни один из вариантов. Повторите попытку");
            }
        } catch (Exception var8) {
            System.out.println("Ошибка! Попробуйте снова.");
        }

        int i;
        for(i = 0; i < amountPlayers - 1; ++i) {
            String var10002 = nameGeneration[rnd.nextInt(nameGeneration.length)];
            nameComputerArray[i] = var10002 + rnd.nextInt(0, 100000);
        }

        System.out.println(namePlayer);

        for(i = 0; i < amountPlayers - 1; ++i) {
            System.out.println(nameComputerArray[i]);
        }

    }
}