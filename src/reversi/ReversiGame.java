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
    
    public ReversiGame(int [][]heurMatrix) {
        this(heurMatrix, 0, 0, false);
    }

    public ReversiGame(int [][]heurMatrix, int libertyPenalty, int sBonus,boolean squareErase) {
        this.heurMatrix = heurMatrix;
        this.libertyPenalty = libertyPenalty;
        this.sBonus = sBonus;
        this.squareErase = squareErase;
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

    // simple evaluation
    protected ReversiTable table;
    protected byte player;
    protected int numFirstPlayer;
    protected int numSecondPlayer;
    protected int point;
    protected int evalNum = 0;

    protected void setTable(Table table, byte player,boolean fullProcess) {
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
    
    // heuristic evaluation
    protected int pointFirstPlayer;
    protected int pointSecondPlayer;
    protected int numFirstFreeNeighbours;
    protected int numSecondFreeNeighbours;
    protected int[][] heurMatrix;
    protected int libertyPenalty;
    protected int sBonus;
    protected int s01;
    protected int s11;
    protected boolean squareErase;
    protected int[][] tableIntArray = new int[8][8];
    
    protected void eraseSquareHeuristic(int i, int j, int id, int jd) {
        s01 = heurMatrix[i][j+jd];
        s11 = heurMatrix[i+id][j+jd];
        heurMatrix[i][j+jd] = 0;
        heurMatrix[i+id][j] = 0;
        heurMatrix[i+id][j+jd] = 0;
    }

    protected void restoreSquareHeuristic(int i, int j, int id, int jd) {
        heurMatrix[i][j+jd] = s01;
        heurMatrix[i+id][j] = s01;
        heurMatrix[i+id][j+jd] = s11;
    }

    
    protected int squareBonus() {
        boolean c1 = true;
        boolean c2 = true;
        boolean c3 = true;
        boolean c4 = true;
        int bonus[] = new int[3];
        bonus[1] = 0;
        bonus[2] = 0;
        int corner1 = tableIntArray[0][0];
        if( corner1 != 0 ) {
            int c1r=1;
            while( c1r < 8 && tableIntArray[0][c1r] == corner1 ) {
                ++c1r;
            }
            bonus[corner1] += c1r-1;
            if( c1r == 8 ) {
                c2 = false;
            }
        }
        int corner2 = tableIntArray[0][7];;
        if( corner2 != 0 ) {
            if( c2 ) {
                int c2l=1; 
                while( c2l < 8 && tableIntArray[0][7-c2l] == corner2 ) {
                    ++c2l;
                }
                bonus[corner2] += c2l-1;
                if( c2l == 8 ) {
                    c1 = false;
                }
            }
            int c2r=1; 
            while( c2r < 8 && tableIntArray[c2r][7] == corner2 ) {
                ++c2r;
            }
            bonus[corner2] += c2r-1;
            if( c2r == 8 ) {
                c3 = false;
            }
            
        }
        int corner3 = tableIntArray[7][7];;
        if( corner3 != 0 ) {
            if( c3 ) {
                int c3l=1; 
                while( c3l < 8 && tableIntArray[7-c3l][7] == corner3 ) {
                    ++c3l;
                }
                bonus[corner3] += c3l-1;
            }
            int c3r=1; 
            while( c3r < 8 && tableIntArray[7][7-c3r] == corner3 ) {
                ++c3r;
            }
            bonus[corner3] += c3r-1;
            if( c3r == 8 ) {
                c4 = false;
            }            
        }
        int corner4 = tableIntArray[7][0];;
        if( corner4 != 0 ) {
            if( c4 ) {
                int c4l=1; 
                while( c4l < 8 && tableIntArray[7][c4l] == corner4 ) {
                    ++c4l;
                }
                bonus[corner4] += c4l-1;
            }
            int c4r=1; 
            while( c4r < 8 && tableIntArray[7-c4r][0] == corner4 ) {
                ++c4r;
            }
            bonus[corner4] += c4r-1;
            if( c4r == 8 ) {
                c1 = false;
            }            
        }
        if( corner1 != 0 && c1) {
            int c1l=1; 
            while( c1l < 8 && tableIntArray[c1l][0] == corner1 ) {
                ++c1l;
            }
            bonus[corner1] += c1l-1;
        }
        return bonus[1] - bonus[2];
    }

    protected int freeNeighbours(int i, int j) {
        int freeNeighbours = 0;
        for( int id = -1; id <=1; ++id ) {
            for( int jd = -1; jd <=1; ++jd ) {
                if( i+id>=0 && i+id<8 && j+jd>=0 && j+jd<8 && 
                    tableIntArray[i+id][j+jd] == 0 ) {
                    ++freeNeighbours;
                }
            }
        }
        return freeNeighbours;
    }

    protected void eval(boolean fullProcess) {
        table.convertToIntArray(tableIntArray);

        boolean lazyProcess = !fullProcess || isGameEnded() ||
                              numFirstPlayer+ numSecondPlayer > 58;

        numFirstPlayer = 0;
        numSecondPlayer = 0;

        pointFirstPlayer = 0;
        pointSecondPlayer = 0;
        numFirstFreeNeighbours = 0;
        numSecondFreeNeighbours = 0;

        if( !lazyProcess && squareErase ) {
            if( tableIntArray[0][0] != 0 ) {
                eraseSquareHeuristic(0,0,1,1);
            }
            if( tableIntArray[0][7] != 0 ) {
                eraseSquareHeuristic(0,7,1,-1);
            }
            if( tableIntArray[7][7] != 0 ) {
                eraseSquareHeuristic(7,7,-1,-1);
            }
            if( tableIntArray[7][0] != 0 ) {
                eraseSquareHeuristic(7,0,-1,1);
            }
        }

        for( int i=0; i<8; ++i ) {
            for( int j=0; j<8; ++j ) {
                int item = tableIntArray[i][j];
                switch( item ) {
                case 1: 
                    ++numFirstPlayer;
                    if( !lazyProcess ) {
                        pointFirstPlayer += heurMatrix[i][j];
                        if( libertyPenalty != 0 ) {
                            numFirstFreeNeighbours += freeNeighbours(i,j);
                        }
                    }
                    break;
                case 2:
                    ++numSecondPlayer;
                    if( !lazyProcess ) {
                        pointSecondPlayer += heurMatrix[i][j];
                        if( libertyPenalty != 0 ) {
                            numSecondFreeNeighbours += freeNeighbours(i,j);
                        }
                    }
                    break;                    
                }
            }
        }
        if( !lazyProcess && squareErase ) {
            restoreSquareHeuristic(0,0,1,1);
            restoreSquareHeuristic(0,7,1,-1);
            restoreSquareHeuristic(7,7,-1,-1);
            restoreSquareHeuristic(7,0,-1,1);            
        }
        //        System.out.println("lp:"+libertyPenalty+"nffn:"+numFirstFreeNeighbours+" nsfn:"+numSecondFreeNeighbours);
        int squareBonusPoint = 0;
        if( !lazyProcess &&sBonus != 0 ) {
            squareBonusPoint = squareBonus();
            //            System.out.println("sbp:"+squareBonusPoint);
        }
        if( lazyProcess ) {
            point = numFirstPlayer - numSecondPlayer;
            if( isGameEnded() ) {
                if( point > 0 ) {
                    point += Minimax.MAX_POINT;
                } else if( point < 0 ) {
                    point -= Minimax.MAX_POINT;
                }
            }
        } else {
            point = pointFirstPlayer - pointSecondPlayer + 
                libertyPenalty * (numSecondFreeNeighbours - numFirstFreeNeighbours) +
                sBonus * squareBonusPoint;
        }
        //System.out.println("liberty:"+libertyPenalty * (numSecondFreeNeighbours - numFirstFreeNeighbours));
        if( player == 1 ) {
            point = -point;
        }
        
    }

} // ReversiGame

