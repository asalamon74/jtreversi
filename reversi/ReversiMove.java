package reversi;



/**
 * ReversiMove.java
 *
 *
 * Created: Tue Jul 30 22:40:16 2002
 *
 * @author Salamon Andras
 * @version
 */

public class ReversiMove implements Move {
    
    protected int row;
    protected int col;

    public ReversiMove(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static boolean valid(int row, int col) {
        return row >=0 && row < J2MEReversi.SIZE 
            && col >=0 && col < J2MEReversi.SIZE;
    }
    
} // ReversiMove
