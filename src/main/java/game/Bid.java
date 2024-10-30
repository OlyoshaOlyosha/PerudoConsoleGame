package game;

import java.util.Scanner;

public class Bid {
    // Метод для получения корректного ввода
    public static int getValidInput(Scanner sc) {
        String input = sc.nextLine(); // Считываем строку
        try {
            // Проверяем, что ввод состоит только из цифр

            if (input.matches("\\d+")) {
                return Integer.parseInt(input);
            } else {
                return -1;
            }
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Метод для проверки корректности ставки
    public static boolean isValidBid(int quantity, int value, int previousBidQuantity, boolean firstMove, int countDice) {
        // Ставка должна быть выше предыдущей
        return quantity > previousBidQuantity && value <= 6 && value >= 1 && !firstMove && quantity <= countDice;
    }

    // Пример функции для проверки правоты ставки
    public static boolean
    checkTruth(int bidValue, int bidQuantity, int[] numberDifferentDice) {
        return bidQuantity <= numberDifferentDice[bidValue-1];
    }
}
