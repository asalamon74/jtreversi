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

    public Minimax(int maxsize) {
        this.maxsize = maxsize;
    }

    public Move minimax(int depth, Table state, byte player, TwoPlayerGame tpg, boolean alphabeta, int alpha, boolean order, boolean kill, Move killerMove) {
        Move bestMove;

        if( depth == 0 ) {            
            bestMove = state.getEmptyMove();
            bestMove.setPoint(tpg.point(state, player));
            return bestMove;
        }

        Move newMove;
        Move actMove;
        Move kMove;
        boolean cut=false;
        int actPoint;
        int maxPoint = -10000; /* -Integer.MIN_VALUE ?? */
        int bestNum=0;
 
        Move pMoves[] = tpg.possibleMoves(state, player);
        int []bestMoves = new int[pMoves.length];

        Table newState = state.copyFrom(state);
        if( depth > 2 && order ) {
            int points[] = new int[pMoves.length];
	    for (int oindex=0;oindex<pMoves.length;++oindex) {
                tpg.turn(state, player, pMoves[oindex], newState);
                points[oindex] = tpg.point(newState, player);
	    }
            int oindex3=0;
	    for (int oindex1=0;oindex1<pMoves.length-1;++oindex1) {
		// maxsearch 
		for (int oindex2=oindex1;oindex2<pMoves.length;++oindex2) {
                    if (oindex2==oindex1 || points[oindex2] > points[oindex3]) {
                        oindex3 = oindex2;
		    }
                }
		if (oindex3 != oindex1) {
                    Move swapMove= pMoves[oindex3];
                    pMoves[oindex3] = pMoves[oindex1];
                    pMoves[oindex1] = swapMove;
		}
	    }            
        }
        
        if( kill && killerMove != null && pMoves.length > 1 ) {
            //            System.out.println("killermove move");
	    int kindex=0;
	    while (kindex < pMoves.length && 
                   !pMoves[kindex].equals(killerMove) ) {
			++kindex;
            }
		
            //            System.out.println("kindex:"+kindex+"/"+pMoves.length);
            if (kindex < pMoves.length && kindex != 0) {
                // we have a killermove, but it's not the first one
                Move swapMove= pMoves[0];
                pMoves[0] = pMoves[kindex];
                pMoves[kindex] = swapMove;
            }
        }
        actMove = null;
        for( int i=0; !cut && i<pMoves.length; ++i ) {
            boolean success = tpg.turn(state, player, pMoves[i], newState);
            if( depth == 1 ) {
                actPoint = tpg.point(newState, player);
            } else {
                if( kill && i != 0 ) {
                    kMove = actMove;
                } else {
                    kMove = null;
                }
		actMove = minimax(depth-1, newState, (byte)(1-player), tpg,  alphabeta, -maxPoint, order , kill, kMove);
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
