package players;

import java.util.Random;
import java.util.Scanner;

public class HumanPlayer extends Player {
    public HumanPlayer(String name, int diceCount) {
        super(name, diceCount);
    }

    @Override
    public void makeBid(int quantity, int value) {
        Scanner sc = new Scanner(System.in);
        quantity = sc.nextInt();
        value = sc.nextInt();
        super.makeBid(quantity, value);
    }
}
