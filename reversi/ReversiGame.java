package reversi;

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
            // TODO: implement
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

    public int point(Table t, int player) {
        if( !(t instanceof ReversiTable) ) {
            return 0;
        }
        ++pointCalculatedNum;
        int point = simplePointCalculate((ReversiTable)t);
        if( (numFirstPlayer + numSecondPlayer == 64) ||
            numFirstPlayer == 0 ||
            numSecondPlayer == 0 ) {
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
        for( int i=0; i<ReversiCanvas.SIZE; ++i ) {
            for( int j=0; j<ReversiCanvas.SIZE; ++j ) {
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

    public Table turn(Table table, int player, Move move) {
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

        if( newTable.getItem(row, col) == 0 ) {
            return null;
        }

        for( int dirx = -1; dirx <= 1; ++dirx ) {
            for( int diry = -1; diry <= 1; ++diry ) {
                if( dirx == 0 && diry == 0 ) {
                    continue;
                }
                
            }
        }
        return null;
    }

    public Move[] possibleMoves(Table t, int player) {
        return null;
    }

    protected int numFirstPlayer;
    protected int numSecondPlayer;
    
} // ReversiGame

