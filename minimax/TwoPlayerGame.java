package minimax;

public interface TwoPlayerGame {

    public int point(Table t, short player);
    public Table turn(Table t, short player, Move move);
    public Move[] possibleMoves(Table t, short player);
}
