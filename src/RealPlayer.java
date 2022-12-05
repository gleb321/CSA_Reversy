import java.util.Scanner;

public class RealPlayer extends Player {
    public RealPlayer(int code) {
        super(code);
    }

    public void makeMove(Board board) {
        boolean moveWasPlaced;
        printAvailableMoves(board);
        do {
            String coords = getMove(board);
            int letter = (coords.charAt(0) >= 'a' ? (char) (coords.charAt(0) - ('a' - 'A')) : coords.charAt(0)) - 'A';
            int number = (coords.charAt(1)) - '0' - 1;
            moveWasPlaced = board.tryPlaceMove(code, number, letter);
            if (!moveWasPlaced) {
                System.out.println("This field is unavailable");
            }
        } while (!moveWasPlaced);
    }

    private static final int inputLength = 2;
    private static final int defaultNumberOfPoints = 2;

    private String getMove(Board board) {
        if (board.getPlayerScore(code) != defaultNumberOfPoints) {
            undo(board);
        }

        String input;
        boolean incorrectInput = true;
        do {
            System.out.println("Enter field coordinates:");
            input = new Scanner(System.in).nextLine();
            if (input.length() != inputLength) {
                System.out.println("Incorrect input format");
                continue;
            }

            char letter = input.charAt(0);
            if (!(letter >= 'A' && letter <= (char) ('A' + Board.width) ||
                    letter >= 'a' && letter <= (char) ('a' + Board.width))) {
                System.out.println("Incorrect horizontal field coordinate");
                continue;
            }

            int number = input.charAt(1) - '0';
            if (number <= 0 || number > Board.height) {
                System.out.println("Incorrect horizontal field coordinate");
                continue;
            }

            incorrectInput = false;
        } while (incorrectInput);

        return input;
    }

    private void undo(Board board) {
        String input;
        System.out.println("Do you want to undo your previous move?");
        do {
            System.out.print("yes/no: ");
            input = new Scanner(System.in).nextLine().toLowerCase();
            if (input.equals("yes")) {
                board.undoMove(code);
                board.printPlayersScore();
                board.print(code);
                printAvailableMoves(board);
            }
        } while (!input.equals("yes") && !input.equals("no"));
    }

    private void printAvailableMoves(Board board) {
        System.out.println("Available field coordinates:");
        for (var coord : board.getAvailableMoves()) {
            System.out.printf("%c%d ", (char) ('A' + coord[1]), coord[0] + 1);
        }

        System.out.println();
    }
}
