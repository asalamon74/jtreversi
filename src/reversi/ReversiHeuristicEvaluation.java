package reversi;

/**
 * ReversiHeuristicEvaluation.java
 *
 *
 * Created: Thu Nov  7 12:13:04 2002
 *
 * @author Salamon Andras
 * @version
 */

public class ReversiHeuristicEvaluation extends ReversiEvaluationFunction {

    protected int pointFirstPlayer;
    protected int pointSecondPlayer;

    protected int[][] heurMatrix = { 
                                  {15,1,8,8,8,8,1,15},
				  {1,2,5,4,4,5,2,1},
				  {8,5,6,6,6,6,5,8},
				  {8,4,6,6,6,6,4,8},
				  {8,4,6,6,6,6,4,8},
				  {8,5,6,6,6,6,5,8},
				  {1,2,5,4,4,5,2,1},
				  {15,1,8,8,8,8,1,15} };
    
    public ReversiHeuristicEvaluation() {
        // TODO: implement
    }

    protected void eval() {
        numFirstPlayer = 0;
        numSecondPlayer = 0;
        pointFirstPlayer = 0;
        pointSecondPlayer = 0;
        for( int i=0; i<J2MEReversi.SIZE; ++i ) {
            for( int j=0; j<J2MEReversi.SIZE; ++j ) {
                int item = table.getItem(i,j);
                switch( item ) {
                case 1: 
                    ++numFirstPlayer;
                    pointFirstPlayer += heurMatrix[i][j];
                    break;
                case 2:
                    ++numSecondPlayer;
                    pointSecondPlayer += heurMatrix[i][j];
                    break;                    
                }
            }
        }
        point = pointFirstPlayer - pointSecondPlayer;
        if( player == 1 ) {
            point = -point;
        }

    }

    
} // ReversiHeuristicEvaluation
