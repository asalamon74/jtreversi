package minimax;

/**
 * Abstact class for describing a two-player game.
 * Rules + Evaluation.
 *
 */
public abstract class TwoPlayerGame {

    /**
     * Calculates the point (goodness) of the table.
     *
     * @param t Table (position) to be checked.
     * @param player Player.
     * @return Goodness of the position.
     */
    public int point(Table t, byte player) {
        setTable(t, player, true);
        return getPoint();
    }

    public void process(Table t, byte player) {
        setTable(t, player, false);
    }

    /**
     * Calculates a turn of the game.
     *
     * @param t Table before the turn.
     * @param player Next player.
     * @param move Move to be processed.
     * @param newTable Table after the turn.
     * @return True if the move is legal.
     */
    public abstract boolean turn(Table t, byte player, Move move, Table newTable);

    /**
     * List of possible moves.
     *
     * @param t Table to be checked.
     * @param player Next player.
     * @return Array of possible moves. null if there is no possible move.
     */
    public abstract Move[] possibleMoves(Table t, byte player);

    /**
     * Calculates if it's possible to move.
     *
     * @param t Table to be checked.
     * @param player Next player.
     * @return Is there a possible move?
     */
    public abstract boolean hasPossibleMove(Table t, byte player);

    // evaluation

    /**
     * One of the possible outcome of the game: WIN
     */
    public static final int WIN  = 1;

    /**
     * One of the possible outcome of the game: DRAW
     */
    public static final int DRAW = 2;

    /**
     * One of the possible outcome of the game: LOSS
     */
    public static final int LOSS = 3;

    protected abstract void setTable(Table table, byte player,boolean fullProcess);

    /**
     * Point of the last processed position.
     *
     * @return Goodness of the position.
     */
    public abstract int getPoint();

    /**
     * Is the game ended int the last processed position.
     *
     * @return True if the game is ended.
     */
    public abstract boolean isGameEnded();

    /**
     * Result of the last processed position.
     *
     * @return Result of the last processed position.
     */
    public abstract int getGameResult();

    /**
     * Number of times the eval function has been called.
     *
     * @return Number of times the eval function has been called.
     */
    public abstract int getEvalNum();


    /**
     * Reset the number of evaluation.
     *
     */
    public abstract void resetEvalNum();
}
