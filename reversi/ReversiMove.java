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
    
} // ReversiMove
