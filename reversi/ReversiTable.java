package reversi;

import minimax.*;
import util.*;

/**
 * ReversiTable.java
 *
 * Stores a table. 
 *
 * Created: Tue Jul 30 17:28:10 2002
 *
 * @author Salamon Andras
 * @version
 */
public class ReversiTable implements Table  {

    protected int size;
    /** 
     * Two bits for every place:
     * 00: nothing
     * 01: 1
     * 10: 2
     * 11: oops
     */
    protected BitSet bitset; 
    
    public ReversiTable(int size) {
        this.size = size;
        bitset = new BitSet(2*size*size);
        passNum = 0;
        int middle = (size - 1)/2;
        setItem(middle,middle, (byte)2);
        setItem(middle+1,middle+1, (byte)2);
        setItem(middle,middle+1, (byte)1);
        setItem(middle+1,middle, (byte)1);
    }

    // no clone
    public ReversiTable(ReversiTable table) {
        this.size = table.size;
        bitset = new BitSet(table.bitset);
        this.passNum = table.passNum;
    }
    public byte getItem(int row, int col) {
        boolean bit1 = bitset.get(2*(row*size+col));
        boolean bit2 = bitset.get(2*(row*size+col)+1);
        byte b = 0;
        if( bit1 ) {
            b += 2;
        }
        if( bit2 ) {
            b += 1;
        }
        return b;
    }

    public void setItem(int row, int col, byte value) {
        boolean bit2 = ((value % 2) != 0);
        boolean bit1 = (((value >> 1) % 2) != 0);
        bitset.set(2*(row*size+col), bit1);
        bitset.set(2*(row*size+col)+1, bit2);
    }


    public static byte getPlayerItem(byte player) {
        return (byte)(player+1);
    }

    public void flip(int row, int col) {
        setItem(row,col,(byte)(3 - getItem(row,col)));
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
                ret += getItem(j,i);
            }
            ret += "\n";
        }
        return ret;
    }

    public Move getEmptyMove() {
        return new ReversiMove(0,0);
    }

} // ReversiTable
