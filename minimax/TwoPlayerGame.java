package minimax;

public interface TwoPlayerGame {

    public int point(Table t, byte player);
    public Table turn(Table t, byte player, Move move);
    public Move[] possibleMoves(Table t, byte player);
    public boolean hasPossibleMove(Table t, byte player);
}
