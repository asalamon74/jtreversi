package reversi;

import minimax.*;

/**
 * ReversiTable.java
 *
 * Stores a table. Would be better with BitSet, but no BitSet in J2ME
 *
 * Created: Tue Jul 30 17:28:10 2002
 *
 * @author Salamon Andras
 * @version
 */
public class ReversiTable implements Table  {

    protected int size;
    protected short[][] matrix;
    
    public ReversiTable(int size) {
        this.size = size;
        matrix = new short[size][size];
        passNum = 0;
        int middle = (size - 1)/2;
        matrix[middle][middle] = 2;
        matrix[middle+1][middle+1] = 2;
        matrix[middle][middle+1] = 1;
        matrix[middle+1][middle] = 1;
    }

    // no clone
    public ReversiTable(ReversiTable table) {
        this.size = table.size;
        matrix = new short[size][size];
        for( int i=0; i<size; ++i ) {
            for( int j=0; j<size; ++j ) {
                matrix[i][j] = table.getItem(i,j);
            }
        }
        this.passNum = table.passNum;
    }
    public short getItem(int row, int col) {
        return matrix[row][col];
    }

    public void setItem(int row, int col, short value) {
        matrix[row][col] = value;
    }


    public static short getPlayerItem(short player) {
        return (short)(player+1);
    }

    public void flip(int row, int col) {
        matrix[row][col] = (short)(3 - matrix[row][col]);
    }

    protected int passNum;
    
    /**
     * Get the value of passNum.
     * @return Value of passNum.
     */
    public int getPassNum() {
         return passNum; 
    }
    
    /**
     * Set the value of passNum.
     * @param v  Value to assign to passNum.
     */
    public void setPassNum(int  v) {
        this.passNum = v;
    }

    public String toString() {
        String ret = "";
        for( int i=0; i<size; ++i ) {
            for( int j=0; j<size; ++j ) {
                ret += matrix[j][i];
            }
            ret += "\n";
        }
        return ret;
    }

    public Move getEmptyMove() {
        return new ReversiMove(0,0);
    }

} // ReversiTable
