package minimax;

public interface EvaluationFunction {
    public static final int WIN  = 1;
    public static final int DRAW = 2;
    public static final int LOSS = 3;
    public void setTable(Table table, byte player,boolean fullProcess);
    public int getPoint();
    public boolean isGameEnded();
    public int getGameResult();
    public int getEvalNum();
    public void resetEvalNum();

}
