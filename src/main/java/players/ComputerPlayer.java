package players;

import java.util.Random;

public class ComputerPlayer extends Player {
    public ComputerPlayer(String name, int diceCount) {
        super(name, diceCount);
    }

    @Override
    public void makeBid(int quantity, int value) {
        Random rnd = new Random();

        int chance = rnd.nextInt(100);
        if (chance < 60) {
            if (value < 6) {
                value += 1;
            }
            else if (value == 6) {}
        }
        else {

        }

        super.makeBid(quantity, value);
    }

    // Показать кости игроков, не реализовано
//    public void revealDice() {
//        System.out.println("Игрок " + this.getName() + " Показывает кости: " + Arrays.toString(dice));
//    }
}
