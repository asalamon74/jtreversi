package minimax;

import java.util.Random;
import java.util.Vector;

import reversi.ReversiMove;

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

    public static Move minimax(int depth, Table state, byte player, TwoPlayerGame tpg, boolean alphabeta, int alpha, boolean order, boolean kill, Move killerMove, boolean root) {
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
        int maxPoint = -1000000; /* -Integer.MIN_VALUE ?? */
        int bestNum=0;
 
        Move pMoves[] = tpg.possibleMoves(state, player);
        if( pMoves == null ) {
            // game ended
            bestMove = state.getEmptyMove();
            bestMove.setPoint(tpg.point(state, player));
            return bestMove;
        }
        int []bestMoves = new int[pMoves.length];

        Table newState = state.copyFrom(state);
        if( depth > 2 && order && pMoves.length > 1) {
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
        boolean success;
        for( int i=0; !cut && i<pMoves.length; ++i ) {
            success = tpg.turn(state, player, pMoves[i], newState);
            if( depth == 1 ) {
                actPoint = tpg.point(newState, player);
            } else {
                if( kill && i != 0 ) {
                    kMove = actMove;
                } else {
                    kMove = null;
                }
		actMove = minimax(depth-1, newState, (byte)(1-player), tpg,  alphabeta, -maxPoint, order , kill, kMove, false);
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
        int bestIndex;
        if( bestNum > 1 ) {
            bestIndex = bestMoves[random(bestNum)];
        } else {
            bestIndex = bestMoves[0];
        }
        bestMove = pMoves[bestIndex];
        bestMove.setPoint(maxPoint);
        return bestMove;
    }

    public static void foreMinimax(int depth, Table state, byte player, TwoPlayerGame tpg, boolean alphabeta, int alpha, boolean order, boolean kill) {
        precalculatedMoves.removeAllElements();
        Move pMoves[] = tpg.possibleMoves(state, (byte)(1-player));
        if( pMoves == null ) {
            return;
        }
        Table newState = state.copyFrom(state);
        Move bestMove;
        for( int i=0; i<pMoves.length; ++i ) {
            tpg.turn(state, player, pMoves[i], newState);
            bestMove = minimax(depth, newState, player, tpg, alphabeta, alpha, order, kill, null, true);
            System.out.println("m:"+pMoves[i]+" bm:"+bestMove);
            precalculatedMoves.addElement( pMoves[i] );
            precalculatedMoves.addElement( bestMove );
        }
    }
    
    public static Move precalculatedBestMove(Move move) {
        System.out.println("size:"+precalculatedMoves.size());
        for( int i=0; i<precalculatedMoves.size(); i += 2) {
            if( ((ReversiMove)precalculatedMoves.elementAt(i)).equals(move) ) {
                return (Move)precalculatedMoves.elementAt(i+1);
            }
        }
        return null;
    }

    protected static int random(int max) {
        return Math.abs(rand.nextInt()) % max;
    }

    protected static Random rand = new Random();
    protected static Vector precalculatedMoves = new Vector();
    
} // Minimax
