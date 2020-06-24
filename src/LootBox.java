import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Scanner;

public class LootBox {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int[] lineOfNumbers = Arrays.stream(scanner.nextLine().split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();

        // This will be the queue
        ArrayDeque<Integer> queueFirstLootBox = new ArrayDeque<>();

        for (int num : lineOfNumbers) {
            queueFirstLootBox.offer(num);
        }

        lineOfNumbers = Arrays.stream(scanner.nextLine().split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();

        // This will be the stack
        ArrayDeque<Integer> stackSecondLootBox = new ArrayDeque<>();

        for (int num : lineOfNumbers) {
            stackSecondLootBox.push(num);
        }

        int collectedItems = 0;

        while (!queueFirstLootBox.isEmpty() && !stackSecondLootBox.isEmpty()) {
            int item = queueFirstLootBox.peek() + stackSecondLootBox.peek();

            if (item % 2 == 0) {
                collectedItems += item;
                queueFirstLootBox.poll();
                stackSecondLootBox.pop();
            } else {
                int removed = stackSecondLootBox.pop();
                queueFirstLootBox.offer(removed);
            }
        }

        if (queueFirstLootBox.isEmpty()) {
            System.out.println("First lootbox is empty");
        } else {
            System.out.println("Second lootbox is empty");
        }

        if (collectedItems >= 100) {
            System.out.println("Your loot was epic! Value: " + collectedItems);
        } else {
            System.out.println("Your loot was poor... Value: " + collectedItems);
        }
    }
}