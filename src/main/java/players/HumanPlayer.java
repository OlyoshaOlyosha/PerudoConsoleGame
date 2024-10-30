package players;

import java.util.Random;
import java.util.Scanner;

public class HumanPlayer {
    // Ввод или генерация имён для игрока
    public static String getNamePlayers() {
        Scanner sc = new Scanner(System.in);
        Random rnd = new Random();

        // Массив имён для генерации
        String[] nameGeneration = {"Константин", "Максим", "Дарья", "Виктория", "Артур", "Тимофей", "Алексей", "Иван", "Дмитрий", "Мария", "Станислав", "Никита"};

        System.out.println("\nПридумайте или сгенерируйте имя");

        // Выбор имени для игрока
        String namePlayer;
        int choice;
        while (true) {
            System.out.println("""
                    1. Написать имя
                    2. Сгенерировать имя""");
            System.out.print("Выбор: ");
            choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Имя: ");
                namePlayer = sc.nextLine();
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
