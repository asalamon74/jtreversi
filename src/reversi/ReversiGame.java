package reversi;

import minimax.*;
import java.util.Vector;

/**
 * ReversiGame.java
 *
 *
 * Created: Tue Jul 30 22:23:40 2002
 *
 * @author Salamon Andras
 * @version
 */
public class ReversiGame extends TwoPlayerGame {
    
    public ReversiGame() {
    }

    public void setEvaluationFunction(EvaluationFunction func) {
        if( !(func instanceof ReversiEvaluationFunction) ) {
            throw new IllegalArgumentException();
        }
        this.evaluationFunction = func;
    }

    public int firstPlayerPoint() {
        return ((ReversiEvaluationFunction)evaluationFunction).firstPlayerPoint();
    }

    public int secondPlayerPoint() {
        return ((ReversiEvaluationFunction)evaluationFunction).secondPlayerPoint();
    }

    public boolean turn(Table table, byte player, Move move, Table newt) {
        if( !(move instanceof ReversiMove ) ||
            !(table instanceof ReversiTable) ||
            !(newt instanceof ReversiTable)) {
            return false;
        }

        int row = ((ReversiMove)move).row;
        int col = ((ReversiMove)move).col;
        if( row != J2MEReversi.SIZE && ((ReversiTable)table).getItem(row, col) != 0 ) {
            return false;
        }

        ReversiTable newTable = (ReversiTable)newt;
        newTable.copyDataFrom((ReversiTable)table);
        if( row == J2MEReversi.SIZE ) {
            // pass
            newTable.setPassNum(newTable.getPassNum()+1);
            return true;
        }

        newTable.setPassNum(0);

        boolean flipped = false;
        for( int dirrow = -1; dirrow <= 1; ++dirrow ) {
            for( int dircol = -1; dircol <= 1; ++dircol ) {
                if( dirrow == 0 && dircol == 0 ) {
                    continue;
                }
                int c = 1;
                while( ReversiMove.valid(row+c*dirrow,col+c*dircol) && 
                       newTable.getItem(row+c*dirrow,col+c*dircol) == ReversiTable.getPlayerItem((byte)(1-player))) {
                    ++c;
                }
		if (c > 1 && ReversiMove.valid(row+c*dirrow,col+c*dircol) 
                        && newTable.getItem(row+c*dirrow,col+c*dircol) == ReversiTable.getPlayerItem(player)) {
                    flipped = true;
		    for (int s1=1;s1 < c;++s1) {
                        newTable.flip(row+s1*dirrow,col+s1*dircol);
                    }
                }

            }
        }

        if( flipped ) {
            newTable.setItem(row, col, ReversiTable.getPlayerItem(player));
            return true;
        }
        return false;
    }

    public boolean hasPossibleMove(Table table, byte player) {
        //TODO: enhance
        ReversiMove[] moves = (ReversiMove[])possibleMoves(table, player);
        return moves != null && (moves.length > 1 || moves[0].row != J2MEReversi.SIZE);
    }

    public Move[] possibleMoves(Table table, byte player) {
        if( !(table instanceof ReversiTable) ) {
            return null;
        }
        Vector moves = new Vector();

        if( ((ReversiTable)table).getPassNum() == 2 ) {
            // two passes: end of the game
            return null;
        }

        ReversiTable newTable = new ReversiTable(J2MEReversi.SIZE);
        ReversiMove move = new ReversiMove(0,0); 
        for( int row=0; row<J2MEReversi.SIZE; ++row ) {
            for( int col=0; col<J2MEReversi.SIZE; ++col ) {
                move.setCoordinates(row, col);
                boolean goodMove = turn(table, player, move, newTable);
                if( goodMove ) {
                    moves.addElement(new ReversiMove(move));
                }
            }
        }

        if( moves.size() == 0 ) {
            // need to pass
            moves.addElement(new ReversiMove(J2MEReversi.SIZE, J2MEReversi.SIZE));
        }
        Move[] retMoves = new ReversiMove[moves.size()];
        for( int m=0; m<moves.size(); ++m ) {
            retMoves[m] = (Move)moves.elementAt(m);
        }
        return retMoves;
    }

} // ReversiGame

