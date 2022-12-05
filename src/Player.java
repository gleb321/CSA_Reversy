abstract public class Player {
    final public int code;

    public Player(int code) {
        this.code = code;
    }

    public abstract void makeMove(Board board);
}
