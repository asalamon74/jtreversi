package minimax;

public abstract class TwoPlayerGame {

    protected EvaluationFunction evaluationFunction;

    public abstract void setEvaluationFunction(EvaluationFunction func);

    public int point(Table t, byte player) {
        evaluationFunction.setTable(t, player, true);
        return evaluationFunction.getPoint();
    }

    public void process(Table t, byte player) {
        evaluationFunction.setTable(t, player, false);
    }

    public void resetEvalNum() {
        evaluationFunction.resetEvalNum();
    }

    public int getEvalNum() {
        return evaluationFunction.getEvalNum();
    }

    public boolean isGameEnded() {
        return evaluationFunction.isGameEnded();
    }

    public int getGameResult() {
        return evaluationFunction.getGameResult();
    }

    public abstract boolean turn(Table t, byte player, Move move, Table newTable);
    public abstract Move[] possibleMoves(Table t, byte player);
    public abstract boolean hasPossibleMove(Table t, byte player);
}
