package reversi;

import minimax.*;

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

    public ReversiMove(ReversiMove move) {
        this.row = move.row;
        this.col = move.col;
    }

    public void setCoordinates(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static boolean valid(int row, int col) {
        return row >=0 && row < 8 
            && col >=0 && col < 8;
    }

    public String toString() {
        return "ReversiMove("+row+", "+col+")";
    }

    int point;
    
    /**
     * Get the value of point.
     * @return Value of point.
     */
    public int getPoint() {
         return point; 
    }
    
    /**
     * Set the value of point.
     * @param v  Value to assign to point.
     */
    public void setPoint(int  v) {
        this.point = v;
    }

    public boolean equals(Object o) {
        return (o instanceof ReversiMove &&
                row == ((ReversiMove)o).row &&
                row == ((ReversiMove)o).col);
    }
    
} // ReversiMove
