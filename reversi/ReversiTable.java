package reversi;

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
    protected int[][] matrix;
    
    public ReversiTable(int size) {
        this.size = size;
        matrix = new int[size][size];
        passNum = 0;
    }

    // no clone
    public ReversiTable(ReversiTable table) {
        this.size = table.size;
        matrix = new int[size][size];
        for( int i=0; i<size; ++i ) {
            for( int j=0; j<size; ++j ) {
                matrix[i][j] = table.getItem(i,j);
            }
        }
        this.passNum = table.passNum;
    }
    public int getItem(int row, int col) {
        return matrix[row][col];
    }

    public void setItem(int row, int col, int value) {
        matrix[row][col] = value;
    }


    public static int getPlayerItem(int player) {
        return player+1;
    }

    public void flip(int row, int col) {
        matrix[row][col] = 2 - matrix[row][col];
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
    
    
} // ReversiTable
