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
    protected int numFirstFreeNeighbours;
    protected int numSecondFreeNeighbours;
    protected int[][] heurMatrix;
    protected int libertyPenalty;
    protected int sBonus;
    protected int s01;
    protected int s11;
    protected boolean squareErase;
    protected int[][] tableIntArray = new int[8][8];
    
    public ReversiHeuristicEvaluation(int [][]heurMatrix) {
        this(heurMatrix, 0, 0, false);
    }

    public ReversiHeuristicEvaluation(int [][]heurMatrix, int libertyPenalty, int sBonus,boolean squareErase) {
        this.heurMatrix = heurMatrix;
        this.libertyPenalty = libertyPenalty;
        this.sBonus = sBonus;
        this.squareErase = squareErase;
    }

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
        super.eval(fullProcess);
        if( !fullProcess || isGameEnded() ||
            numFirstPlayer+ numSecondPlayer > 58 ) {
            // do nothing point is calculated by super
            return;
        }
        pointFirstPlayer = 0;
        pointSecondPlayer = 0;
        numFirstFreeNeighbours = 0;
        numSecondFreeNeighbours = 0;
        if( squareErase ) {
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
                    pointFirstPlayer += heurMatrix[i][j];
                    if( libertyPenalty != 0 ) {
                        numFirstFreeNeighbours += freeNeighbours(i,j);
                    }
                    break;
                case 2:
                    pointSecondPlayer += heurMatrix[i][j];
                    if( libertyPenalty != 0 ) {
                        numSecondFreeNeighbours += freeNeighbours(i,j);
                    }
                    break;                    
                }
            }
        }
        if( squareErase ) {
            restoreSquareHeuristic(0,0,1,1);
            restoreSquareHeuristic(0,7,1,-1);
            restoreSquareHeuristic(7,7,-1,-1);
            restoreSquareHeuristic(7,0,-1,1);            
        }
        //        System.out.println("lp:"+libertyPenalty+"nffn:"+numFirstFreeNeighbours+" nsfn:"+numSecondFreeNeighbours);
        int squareBonusPoint = 0;
        if( sBonus != 0 ) {
            squareBonusPoint = squareBonus();
            //            System.out.println("sbp:"+squareBonusPoint);
        }
        point = pointFirstPlayer - pointSecondPlayer + 
            libertyPenalty * (numSecondFreeNeighbours - numFirstFreeNeighbours) +
            sBonus * squareBonusPoint;
        //System.out.println("liberty:"+libertyPenalty * (numSecondFreeNeighbours - numFirstFreeNeighbours));
        if( player == 1 ) {
            point = -point;
        }
        
    }
    
} // ReversiHeuristicEvaluation
