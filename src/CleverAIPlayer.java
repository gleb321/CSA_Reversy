import java.util.Random;

public class CleverAIPlayer extends AIPlayer {
    public CleverAIPlayer(int code) {
        super(code);
    }

    public void makeMove(Board board) {
        var rnd = new Random();
        int i = -1, j = -1;
        int scoreChange = -64;
        for (var coord : board.getAvailableMoves()) {
            int previousScore = board.getPlayerScore(code);
            board.tryPlaceMove(code, coord[0], coord[1]);
            if (!board.prepareForMove(board.getOpponentCode(code))) {
                i = coord[0];
                j = coord[1];
                break;
            }

            makeBestMove(board, board.getOpponentCode(code));
            int currentScore = board.getPlayerScore(code);
            if (currentScore - previousScore > scoreChange ||
                    currentScore - previousScore == scoreChange && rnd.nextBoolean()) {
                scoreChange = currentScore - previousScore;
                i = coord[0];
                j = coord[1];
            }

            board.undoMove(code);
        }

        if (i != -1 && j != -1)  {
            board.tryPlaceMove(code, i, j);
        }
    }
}
