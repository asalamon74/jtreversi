package reversi;

import minimax.*;

/**
 * ReversiEvaluationFunction
 *
 *
 * Created: Thu Nov  7 12:01:53 2002
 *
 * @author Salamon Andras
 * @version
 */
public class ReversiEvaluationFunction implements EvaluationFunction {

    public ReversiEvaluationFunction() {
        // TODO: implement
    }

    protected ReversiTable table;
    protected byte player;
    protected int numFirstPlayer;
    protected int numSecondPlayer;
    protected int point;
    protected int evalNum = 0;

    public void setTable(Table table, byte player,boolean fullProcess) {
        if( !(table instanceof ReversiTable) ) {
            throw new IllegalArgumentException();
        }
        this.table = (ReversiTable)table;
        this.player = player;
        ++evalNum;
        eval(fullProcess);
    }

    public int getEvalNum() {
        return evalNum;
    }

    public void resetEvalNum() {
        evalNum = 0;
    }

    protected void eval(boolean fullProcess) {
        numFirstPlayer = 0;
        numSecondPlayer = 0;
        for( int i=0; i<8; ++i ) {
            for( int j=0; j<8; ++j ) {
                int item = table.getItem(i,j);
                switch( item ) {
                case 1: 
                    ++numFirstPlayer;
                    break;
                case 2:
                    ++numSecondPlayer;
                    break;                    
                }
            }
        }
        point = numFirstPlayer - numSecondPlayer;
        if( isGameEnded() ) {
            if( point > 0 ) {
                point += Minimax.MAX_POINT;
            } else if( point < 0 ) {
                point -= Minimax.MAX_POINT;
            }
        }
        if( player == 1 ) {
            point = -point;
        }

    }


    public int getPoint() {
        return point;
    }

    public boolean isGameEnded() {
        if( (numFirstPlayer + numSecondPlayer == 64) ||
            numFirstPlayer == 0 ||
            numSecondPlayer == 0 ||
            table.getPassNum() == 2) {
            return true;
        }
        return false;
    }

    public int getGameResult() {
        int piecediff = numFirstPlayer - numSecondPlayer;
        if( player == 1 ) {
            piecediff = -piecediff;
        }

        if( piecediff > 0 ) {
            return WIN;
        } else if( piecediff < 0 ) {
            return LOSS;
        } else {
            return DRAW;
        }
    }

    public int firstPlayerNum() {
        return numFirstPlayer;
    }

    public int secondPlayerNum() {
        return numSecondPlayer;
    }


} // ReversiEvaluationFunction
