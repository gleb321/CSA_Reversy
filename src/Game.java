import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    public Game() {
    }

    public void run() {
        do {
            setup();
            while (!gameIsOver) {
                processPlayersMove();
            }

            printPlayersScore();
            printBoard(firstPlayerTurn ? firstPlayer.code : secondPlayer.code);
            if (board.getPlayerScore(firstPlayer.code) == board.getPlayerScore(secondPlayer.code)) {
                System.out.printf("\t\t\t%sDRAW%s", Color.YELLOW, Color.WHITE);
            } else if (board.getPlayerScore(firstPlayer.code) > board.getPlayerScore(secondPlayer.code)) {
                System.out.printf("\t%sRED%s PLAYER WINS!!!%s\n", Color.RED, Color.YELLOW, Color.WHITE);
            } else {
                System.out.printf("\t%sBLUE%s PLAYER WINS!!!%s\n", Color.BLUE, Color.YELLOW, Color.WHITE);
            }

            if (board.getPlayerScore(firstPlayer.code) > firstPlayerHighScore) {
                firstPlayerHighScore = board.getPlayerScore(firstPlayer.code);
            }

            if (board.getPlayerScore(secondPlayer.code) > secondPlayerHighScore) {
                secondPlayerHighScore = board.getPlayerScore(secondPlayer.code);
            }
            gameIsOver = false;
        } while (showMustGoOn());

        System.out.printf("\n%sRED%s PLAYER HIGH SCORE: %d!!!\n", Color.RED, Color.YELLOW, firstPlayerHighScore);
        System.out.printf("%sBLUE%s PLAYER HIGH SCORE: %d!!!\n", Color.BLUE, Color.YELLOW, secondPlayerHighScore);
        System.out.printf("%s\n", Color.WHITE);
    }

    private static final int inputLength = 1;
    private Board board;
    private Player firstPlayer;
    private Player secondPlayer;
    private boolean gameIsOver;
    private boolean firstPlayerTurn;
    private int firstPlayerHighScore = 0;
    private int secondPlayerHighScore = 0;

    private void setup() {
        board = new Board();
        System.out.printf("Choose first(%sRED%s) player type\n", Color.RED, Color.WHITE);
        firstPlayer = setupPlayer(Board.firstPlayerFieldCode);
        System.out.printf("Choose second(%sBLUE%s) player type\n", Color.BLUE, Color.WHITE);
        secondPlayer = setupPlayer(Board.secondPlayerFieldCode);
        firstPlayerTurn = true;
        gameIsOver = false;
    }

    private Player setupPlayer(int playerCode) {
        boolean incorrectInput = true;
        int optionNumber = 0;
        do {
            System.out.println("Enter number of player type:");
            System.out.println("1 - Stupid AI player: ");
            System.out.println("2 - Clever AI player: ");
            System.out.println("3 - Real player: ");
            String input = new Scanner(System.in).nextLine();
            if (input.length() != inputLength) {
                System.out.println("Incorrect input format");
                continue;
            }

            optionNumber = input.charAt(0) - '0';
            if (optionNumber <= 0 || optionNumber > 3) {
                System.out.println("Incorrect option number");
                continue;
            }

            incorrectInput = false;
        } while (incorrectInput);

        switch (optionNumber) {
            case 1:
                return new StupidAIPlayer(playerCode);
            case 2:
                return new CleverAIPlayer(playerCode);
            default:
                return new RealPlayer(playerCode);
        }
    }

    private void processPlayersMove() {
        Player player = firstPlayerTurn ? firstPlayer : secondPlayer;
        if (!board.prepareForMove(player.code)) {
            gameIsOver = true;
            return;
        }

        printPlayersScore();
        printBoard(player.code);
        player.makeMove(board);
        firstPlayerTurn = !firstPlayerTurn;
        clear();
    }

    private boolean showMustGoOn() {
        String input;
        System.out.println("Do you want to play one more time?");
        do {
            System.out.print("yes/no: ");
            input = new Scanner(System.in).nextLine().toLowerCase();
        } while (!input.equals("yes") && !input.equals("no"));

        return input.equals("yes");
    }

    private void printBoard(int playerCode) {
        board.print(playerCode);
    }
    private void printPlayersScore() {
        board.printPlayersScore();
    }

    private void clear() {
        for (int i = 0; i < 2; ++i) {
            System.out.println("\n\n\n\n\n");
        }
    }
}
