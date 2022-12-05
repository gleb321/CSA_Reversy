public class StupidAIPlayer extends AIPlayer {
    public StupidAIPlayer(int code) {
        super(code);
    }

    public void makeMove(Board board) {
        makeBestMove(board, code);
    }
}
