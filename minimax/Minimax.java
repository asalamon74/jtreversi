package minimax;

/**
 * Minimax.java
 *
 *
 * Created: Tue Jul 30 17:39:32 2002
 *
 * @author Salamon Andras
 * @version
 */

public class Minimax  {

    public static final int MAX_TURN = 60;

    public Minimax(int maxsize) {
        this.maxsize = maxsize;
    }

    public int minimax(int depth, Table state, int player, TwoPlayerGame tpg, Move bestMove, boolean alphabeta, int alpha, boolean order, Move killerMove) {
        Move newMove;
        Move actMove;
        Move kMove;
        boolean cut=false;
        int actPoint;
        int maxPoint = -10000; /* -Integer.MIN_VALUE ?? */
        int bestNum=0;
        int []bestMoves = new int[MAX_TURN];

        if( depth == 0 ) {            
            return tpg.point(state, player);
        }

        Move pMoves[] = tpg.possibleMoves(state, player);

        if( depth > 1 && order ) {
            // TODO: order
        }
        
        if( killerMove != null && pMoves.length > 1 ) {
            // TODO: find the killer move
        }
        actMove = null;
        for( int i=0; !cut && i<pMoves.length; ++i ) {
            Table newState = tpg.turn(state, player, pMoves[i]);
            if( depth == 1 ) {
                actPoint = tpg.point(newState, player);
            } else {
                if( killerMove != null && i != 0 ) {
                    //                    kMove = (Move)actMove.clone();
                    kMove = actMove;
                } else {
                    kMove = null;
                }
		actPoint = -minimax(depth-1, newState, 1-player, tpg, actMove, alphabeta, -maxPoint, order , kMove);
            }
            if( i == 0 || actPoint > maxPoint ) {
                // better move
                maxPoint = actPoint;
		if (alphabeta && alpha < maxPoint) {
                    cut = true;
		}
		bestNum = 1;
		bestMoves[0] = i;
	    } else if (actPoint == maxPoint) {
                // same as the better
		bestMoves[bestNum++] = i;
	    }
        }
        int bestIndex=0;
        if( bestNum > 1 ) {
            // TODO: random selection
            bestIndex = bestMoves[0];
        } else {
            bestIndex = bestMoves[0];
        }
        if( bestMove != null ) {
            bestMove = pMoves[bestIndex];
        }

        return maxPoint;
    }

    protected int maxsize;
    
} // Minimax
