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

    public int firstPlayerNum() {
        return ((ReversiEvaluationFunction)evaluationFunction).firstPlayerNum();
    }

    public int secondPlayerNum() {
        return ((ReversiEvaluationFunction)evaluationFunction).secondPlayerNum();
    }

    public Table[] animatedTurn(Table table, byte player, Move move, Table newt) {
        return _turn(table, player, move, newt, true);
    }

    public boolean turn(Table table, byte player, Move move, Table newt) {
        return _turn(table, player, move, newt, false) != null;
    }

    private Table[] _turn(Table table, byte player, Move move, Table newt, boolean animated) {
        if( !(move instanceof ReversiMove ) ||
            !(table instanceof ReversiTable) ||
            !(newt instanceof ReversiTable)) {
            return null;
        }

        int row = ((ReversiMove)move).row;
        int col = ((ReversiMove)move).col;
        if( row != 8 && ((ReversiTable)table).getItem(row, col) != 0 ) {
            return null;
        }

        Vector vTables=null;
        Table tables[];

        if( animated ) {
            vTables = new Vector();
        }
        ReversiTable newTable = (ReversiTable)newt;
        newTable.copyDataFrom((ReversiTable)table);
        if( row == 8 ) {
            // pass
            newTable.setPassNum(newTable.getPassNum()+1);
            tables = new Table[1];
            tables[0] = newTable;
            return tables;
        }

        newTable.setPassNum(0);
        newTable.setItem(row, col, ReversiTable.getPlayerItem(player));
        if( animated ) {
            vTables.addElement(new ReversiTable(newTable));
        }
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
                        if( animated ) {
                            vTables.addElement(new ReversiTable(newTable));
                        }
                    }
                }

            }
        }

        if( flipped ) {
            if( animated ) {
                tables = new Table[vTables.size()];
                for( int i=0; i<vTables.size(); ++i ) {
                    tables[i] = (Table)vTables.elementAt(i);
                }
            } else {
                tables = new Table[1];
                tables[0] = newTable;
            }
            return tables;
        } else {
            newTable.setItem(row, col, (byte)0);
        }
        return null;
    }

    public boolean hasPossibleMove(Table table, byte player) {
        //TODO: enhance
        ReversiMove[] moves = (ReversiMove[])possibleMoves(table, player);
        return moves != null && (moves.length > 1 || moves[0].row != 8);
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

        ReversiTable newTable = new ReversiTable();
        ReversiMove move = new ReversiMove(0,0); 
        boolean hasMove = false;
        for( int row=0; row<8; ++row ) {
            for( int col=0; col<8; ++col ) {
                move.setCoordinates(row, col);
                if( !hasMove && ((ReversiTable)table).getItem(row,col) == 0 ) {
                    hasMove = true;
                }
                boolean goodMove = turn(table, player, move, newTable);
                if( goodMove ) {
                    moves.addElement(new ReversiMove(move));
                }
            }
        }

        if( !hasMove ) {
            return null;
        }
        if( moves.size() == 0 ) {
            // need to pass
            moves.addElement(new ReversiMove(8, 8));
        }
        Move[] retMoves = new ReversiMove[moves.size()];
        for( int m=0; m<moves.size(); ++m ) {
            retMoves[m] = (Move)moves.elementAt(m);
        }
        return retMoves;
    }

} // ReversiGame

