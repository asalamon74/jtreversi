package minimax;

public interface TwoPlayerGame {

    public int point(Table t, byte player);
    public boolean turn(Table t, byte player, Move move, Table newTable);
    public Move[] possibleMoves(Table t, byte player);
    public boolean hasPossibleMove(Table t, byte player);
}
