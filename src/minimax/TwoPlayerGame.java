package minimax;

/**
 * Abstact class for describing a two-player game.
 * Rules + Evaluation.
 *
 */
public abstract class TwoPlayerGame {

    public int point(Table t, byte player) {
        setTable(t, player, true);
        return getPoint();
    }

    public void process(Table t, byte player) {
        setTable(t, player, false);
    }

    public abstract boolean turn(Table t, byte player, Move move, Table newTable);
    public abstract Move[] possibleMoves(Table t, byte player);
    public abstract boolean hasPossibleMove(Table t, byte player);

    // evaluation
    public static final int WIN  = 1;
    public static final int DRAW = 2;
    public static final int LOSS = 3;
    protected abstract void setTable(Table table, byte player,boolean fullProcess);
    public abstract int getPoint();
    public abstract boolean isGameEnded();
    public abstract int getGameResult();
    public abstract int getEvalNum();
    public abstract void resetEvalNum();
}
