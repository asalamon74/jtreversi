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
public class ReversiGame implements TwoPlayerGame {
    
    public ReversiGame() {
    }


    int pointCalculatedNum;
    
    /**
     * Get the value of pointCalculatedNum.
     * @return Value of pointCalculatedNum.
     */
    public int getPointCalculatedNum() {
         return pointCalculatedNum; 
    }
    
    /**
     * Set the value of pointCalculatedNum.
     * @param v  Value to assign to pointCalculatedNum.
     */
    protected void setPointCalculatedNum(int  v) {
        this.pointCalculatedNum = v;
    }

    public int point(Table t, short player) {
        if( !(t instanceof ReversiTable) ) {
            return 0;
        }
        ++pointCalculatedNum;
        int point = simplePointCalculate((ReversiTable)t);
        if( (numFirstPlayer + numSecondPlayer == 64) ||
            numFirstPlayer == 0 ||
            numSecondPlayer == 0 ||
            ((ReversiTable)t).getPassNum() == 2) {
            if( point < 0 ) {
                point += 10000;
            } else {
                point -= 10000;
            }
        }
        if( player == 1 ) {
            point = -point;
        }
        return point;
    }

    protected int simplePointCalculate(ReversiTable table) {
        numFirstPlayer = 0;
        numSecondPlayer = 0;
        for( int i=0; i<J2MEReversi.SIZE; ++i ) {
            for( int j=0; j<J2MEReversi.SIZE; ++j ) {
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
        // TODO: use heuristic
        return numFirstPlayer - numSecondPlayer;
    }

    public Table turn(Table table, short player, Move move) {
        if( !(move instanceof ReversiMove ) ||
            !(table instanceof ReversiTable) ) {
            return null;
        }
        ReversiTable newTable = new ReversiTable((ReversiTable)table);
        int row = ((ReversiMove)move).row;
        int col = ((ReversiMove)move).col;
        if( row == 8 ) {
            // pass
            newTable.setPassNum(newTable.getPassNum()+1);
            return newTable;
        }

        newTable.setPassNum(0);
        if( newTable.getItem(row, col) != 0 ) {
            return null;
        }

        boolean flipped = false;
        for( int dirrow = -1; dirrow <= 1; ++dirrow ) {
            for( int dircol = -1; dircol <= 1; ++dircol ) {
                if( dirrow == 0 && dircol == 0 ) {
                    continue;
                }
                int c = 1;
                while( ReversiMove.valid(row+c*dirrow,col+c*dircol) && 
                       newTable.getItem(row+c*dirrow,col+c*dircol) == ReversiTable.getPlayerItem((short)(1-player))) {
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
            return newTable;
        }
        return null;
    }

    public boolean hasPossibleMove(Table table, short player) {
        //TODO: enhance
        return possibleMoves(table, player) != null;
    }

    public Move[] possibleMoves(Table table, short player) {
        if( !(table instanceof ReversiTable) ) {
            return null;
        }
        Vector moves = new Vector();

        if( ((ReversiTable)table).getPassNum() == 2 ) {
            // two passes: end of the game
            return null;
        }

        ReversiTable newTable;
        for( int row=0; row<J2MEReversi.SIZE; ++row ) {
            for( int col=0; col<J2MEReversi.SIZE; ++col ) {
                ReversiMove move = new ReversiMove(row, col);
                newTable = (ReversiTable)turn(table, player, move);
                if( newTable != null ) {
                    moves.addElement(move);
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

    protected int numFirstPlayer;
    protected int numSecondPlayer;
    
} // ReversiGame

