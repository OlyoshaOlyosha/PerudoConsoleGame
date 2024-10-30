package players;

import java.util.Random;

public class ComputerPlayer {
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
}
