package players;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private String name;
    private boolean isHuman;
    private int diceCount; // Количество костей, которые остались
    private List<Integer> diceValues; // Список значений на костях после броска

    int currentBidQuantity; // Количество костей
    int currentBidValue; // Номинал кости

    public Player (String name, boolean isHuman) {
        this.name = name;
        this.isHuman = isHuman;
        this.diceCount = 5;
    }


    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public int getDiceCount() {
        return diceCount;
    }

    public void loseDie() {
        if (diceCount > 0) {
            diceCount--;
        }
    }

    public List<Integer> getDiceValues() {
        return diceValues;
    }
    public void setDiceValues(List<Integer> diceValues) {
        this.diceValues = diceValues;
    }


    // Метод для проверки, выбыл ли игрок из игры
    public boolean isOut() {
        return diceCount == 0;
    }

    public int getCurrentBidQuantity() {
        return currentBidQuantity;
    }
    public void setCurrentBidQuantity(int currentBidQuantity) {
        this.currentBidQuantity = currentBidQuantity;
    }

    public int getCurrentBidValue() {
        return currentBidValue;
    }
    public void setCurrentBidValue(int currentBidValue) {
        this.currentBidValue = currentBidValue;
    }

    public void makeBid(int quantity, int value) {
        this.currentBidQuantity = quantity;
        this.currentBidValue = value;
    }
}
