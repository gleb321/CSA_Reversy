import java.util.Random;

public abstract class AIPlayer extends Player {
    public AIPlayer(int code) {
        super(code);
    }

    protected void makeBestMove(Board board, int playerCode) {
        var rnd = new Random();
        int i = -1, j = -1;
        int maxNumberOfRecolouredFigures = 0;
        for (var coord : board.getAvailableMoves()) {
            int currentNumberOfRecolouredFigures = board.getNumberOfRecolouredFigures(playerCode, coord[0], coord[1]);
            if (currentNumberOfRecolouredFigures > maxNumberOfRecolouredFigures ||
                    currentNumberOfRecolouredFigures == maxNumberOfRecolouredFigures && rnd.nextBoolean()) {
                maxNumberOfRecolouredFigures = currentNumberOfRecolouredFigures;
                i = coord[0];
                j = coord[1];
            }
        }

        if (i != -1 && j != -1)  {
            board.tryPlaceMove(code, i, j);
        }
    }
}
