package minimax;

public abstract class TwoPlayerGame {

    protected EvaluationFunction evaluationFunction;

    // BEGIN:XAITEST
//     protected EvaluationFunction []evals;

//     public void setEvaluationFunctions(EvaluationFunction e1, EvaluationFunction e2) {
//         evals = new EvaluationFunction[2];
//         evals[0] = e1;
//         evals[1] = e2;
//     }
    // END:XAITEST

    public abstract void setEvaluationFunction(EvaluationFunction func);

    public int point(Table t, byte player) {
        // BEGIN:XAITEST
//         if( player == 0 ) {
//             evaluationFunction = evals[0];
//         } else {
//             evaluationFunction = evals[1];
//         }
        // END:XAITEST
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
