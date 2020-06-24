import java.util.Scanner;

public class RevoltVer2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = Integer.parseInt(scanner.nextLine());
        int commandsCount = Integer.parseInt(scanner.nextLine());

        char[][] field = new char[n][n];

        int finishRow = 0;
        int finishCol = 0;

        // This will keep the player positions where index 0 will keep the row index
        // and index 1 will keep the column index. This will be much more useful when passing
        // as an argument I will explain this approach at the consultation day
        int[] playerPositions = new int[2];

        // The rest of the code inside the main method will remain pretty much the same
        // the only difference is that we will pass the player positions array as an argument to
        // all the movement methods for each direction
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            if (line.contains("f")) {
                playerPositions[0] = i;
                playerPositions[1] = line.indexOf("f");
            }
            if (line.contains("F")) {
                finishRow = i;
                finishCol = line.indexOf("F");
            }
            field[i] = line.toCharArray();
        }


        boolean hasWon = false;

        while (commandsCount-- > 0 && !hasWon) {
            String command = scanner.nextLine();

            switch (command) {
                case "up":
                    moveUp(field, playerPositions);
                    break;
                case "down":
                    moveDown(field, playerPositions);
                    break;
                case "left":
                    moveLeft(field, playerPositions);
                    break;
                case "right":
                    moveRight(field, playerPositions);
                    break;
            }

            hasWon = playerPositions[0] == finishRow && playerPositions[1] == finishCol;
        }

        // This code appears as duplicated because we have the same output logic
        // inside the RevoltVer1.java file - the first solution we did
        // You can ignore that
        if (hasWon) {
            System.out.println("Player won!");
        } else {
            System.out.println("Player lost!");
        }

        for (int r = 0; r < field.length; r++) {
            for (int c = 0; c < field[r].length; c++) {
                System.out.print(field[r][c]);
            }
            System.out.println();
        }
    }

    // Now take a look at those movements methods since the implementation for each movement is
    // pretty much the same with the only difference is that the positions change as an example:
    // If we want to move up -> we decrease the row with one
    // If we want to move down -> we increase the row with one
    // If we want to move left -> we decrease the column with one
    // If we want to move right -> we increase the column with one
    // This means that we can have the same handling the only thing we need to pass as a difference is
    // whether we want to move on row or column and whether we want to decrease or increase the previous
    // value
    private static void moveUp(char[][] field, int[] playerPositions) {
        // Here move up will pass the index 0 -> we want to change the row inside the
        // playerPositions array and the direction (or movementSpeed) is -1 since we want to go up
        handleMovement(field, playerPositions, 0, -1);
    }

    private static void moveDown(char[][] field, int[] playerPositions) {
        // Here move down will pass the index 0 -> we want to change the row inside the
        // playerPositions array and the direction (or movementSpeed) is +1 since we want to go down
        handleMovement(field, playerPositions, 0, 1);
    }

    private static void moveLeft(char[][] field, int[] playerPositions) {
        // Here move left will pass the index 1 -> we want to change the column inside the
        // playerPositions array and the direction (or movementSpeed) is -1 since we want to go left
        handleMovement(field, playerPositions, 1, -1);
    }

    private static void moveRight(char[][] field, int[] playerPositions) {
        // Here move right will pass the index 1 -> we want to change the column inside the
        // playerPositions array and the direction (or movementSpeed) is +1 since we want to go right
        handleMovement(field, playerPositions, 1, 1);
    }

    public static void handleMovement(char[][] field, int[] playerPositions, int rowOrColIndex, int movementSpeed) {
        // This method does all the movement think about it it is indeed pretty simple
        // and the great benefit is that if we have any bug it will be here on those merely 20 lines

        // First we change the current cell of the player to dash
        field[playerPositions[0]][playerPositions[1]] = '-';

        // Then we move the player by taking the position for the array see how here we say dynamically
        // which position we want to change (the caller of the method must specify that)
        // also we just add the movement speed it will increase if positive and decrease if negative
        // so it will work for both the cases
        playerPositions[rowOrColIndex] += movementSpeed;

        // Now we call additional method to check if the player is out of the matrix bounds and handle that
        // Take a look at the implementation of that later after you have done reading this code
        // you are safe to assume that the method works as expected
        handlePlayerOutOfBoundsMovement(field, playerPositions, rowOrColIndex, movementSpeed);

        // We have moved the player with one step in some direction we dont need to know which one was
        // we care only if there is a bonus or a trap. So in both cases we simply do what what we have to
        // if it is a bonus we will add the movementSpeed to the new position of the player again to the same
        // index specified from the method argument this will move the player in the same direction (again this works
        // for both positive and negative movement parameters
        //
        // In the second case we must subtract the movementSpeed think about it it will simply reverse the previous
        // addition again this works for both signs of the parameter
        if (field[playerPositions[0]][playerPositions[1]] == 'B') {
            playerPositions[rowOrColIndex] += movementSpeed;
        } else if (field[playerPositions[0]][playerPositions[1]] == 'T') {
            playerPositions[rowOrColIndex] -= movementSpeed;
        }

        // Now we need to handle any out of bounds movement again since we may have changed the position of the player
        // from getting bonus or falling into a trap
        handlePlayerOutOfBoundsMovement(field, playerPositions, rowOrColIndex, movementSpeed);

        // Finally we know here we have the correct player position so we need to set the value of that cell to
        // the player symbol 'f' -> note how we get playerPositions[0] (the row of the player) and
        // playerPositions[1] (the column of the player)
        field[playerPositions[0]][playerPositions[1]] = 'f';
    }

    private static void handlePlayerOutOfBoundsMovement(char[][] field, int[] playerPositions, int rowOrColIndex, int movementSpeed) {
        // This is the final method to handle the case when the player is out of the field bounds
        // We know there are two total cases when he can be out regardless of the direction he moves towards
        // also we know that those cases depend on the value of the movement speed
        // if the movement speed is less than zero then the player can only go out of the bounds if his position
        // regardless of row or column is less than zero or -1. So we handle that with setting the player position for that
        // value to the last position in the field field.length - 1
        //
        // We do the opposite for the other two possible directions when he reaches the number of elements inside the
        // field. In those cases we need to set the position to 0.
        // Take a look at the way we choose whether it is a row or a column index we use the same logic as in the
        // handleMovement method.
        if (movementSpeed < 0) {
            if (playerPositions[rowOrColIndex] < 0) {
                playerPositions[rowOrColIndex] = field.length - 1;
            }
        } else {
            if (playerPositions[rowOrColIndex] >= field.length) {
                playerPositions[rowOrColIndex] = 0;
            }
        }
        // NOTE: this will only work for square matrix in order for this code to work for any rectangular
        // matrix you need to twist the code a bit but this way it seems simpler
    }

    // This is the implementation I am not sure if you find this way easier than the previous one, however
    // you can take a look at both there are interesting logic in both of them.

    //   TODO:                       <3 Good Luck! And remember to have Fun! <3
}
