package minimax;

import java.util.Random;

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

    public Move minimax(int depth, Table state, byte player, TwoPlayerGame tpg, boolean alphabeta, int alpha, boolean order, Move killerMove) {
        Move newMove;
        Move actMove;
        Move kMove;
        boolean cut=false;
        int actPoint;
        int maxPoint = -10000; /* -Integer.MIN_VALUE ?? */
        int bestNum=0;
        int []bestMoves = new int[MAX_TURN];
        Move bestMove;

        if( depth == 0 ) {            
            bestMove = state.getEmptyMove();
            bestMove.setPoint(tpg.point(state, player));
            return bestMove;
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
		actMove = minimax(depth-1, newState, (byte)(1-player), tpg,  alphabeta, -maxPoint, order , kMove);
                actPoint = -actMove.getPoint();
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
            bestIndex = bestMoves[random(bestNum)];
        } else {
            bestIndex = bestMoves[0];
        }
        bestMove = pMoves[bestIndex];
        bestMove.setPoint(maxPoint);
        return bestMove;
    }

    protected int random(int max) {
        int r = Math.abs(rand.nextInt());
        return r % max;
    }

    protected int maxsize;
    protected Random rand = new Random();
    
} // Minimax
