package players;

import java.util.List;

public class Player {
    private String name;
    private int diceCount; // Количество костей, которые остались
    private List<Integer> diceValues; // Список значений на костях после броска

    int currentBidQuantity; // Количество костей
    int currentBidValue; // Номинал кости

    public Player (String name, int diceCount) {
        this.name = name;
        this.diceCount = diceCount;
    }

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getDiceCount() {
        return diceCount;
    }
    public void setDiceCount(int diceCount) {
        this.diceCount = diceCount;
    }

    public List<Integer> getDiceValues() {
        return diceValues;
    }
    public void setDiceValues(List<Integer> diceValues) {
        this.diceValues = diceValues;
    }

    // Метод для броска костей
    public void rollDice() {
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
