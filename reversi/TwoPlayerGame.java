package reversi;

public interface TwoPlayerGame {

    public int point(Table t, int player);
    public Table turn(Table t, int player, Move move);
    public Move[] possibleMoves(Table t, int player);
}
