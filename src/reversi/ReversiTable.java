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

    /** 
     * Two bits for every place:
     * 00: nothing
     * 01: 1
     * 10: 2
     * 11: oops
     */
    protected BitSet bitset; 
    
    public ReversiTable() {
        bitset = new BitSet(2*8*8);
        passNum = 0;
        setItem(3,3, (byte)2);
        setItem(4,4, (byte)2);
        setItem(3,4, (byte)1);
        setItem(4,3, (byte)1);
    }

    // no clone
    public ReversiTable(ReversiTable table) {
        bitset = new BitSet(table.bitset);
        this.passNum = table.passNum;
    }

    public ReversiTable(byte []byteArray, int offset) {
        bitset = new BitSet(2*8*8, byteArray, offset);
        //        System.out.println("getItem(3,3):"+getItem(3,3));
        passNum = byteArray[offset+16];
        //        System.out.println("passNum:"+passNum);
    }

    public void copyDataFrom(Table table) {
        ReversiTable rtable = (ReversiTable)table;
        BitSet.copy(rtable.bitset, bitset);
        this.passNum = rtable.passNum;
    }

    public Table copyFrom() {
        ReversiTable rtable = new ReversiTable(this);
        return rtable;
    }

    public byte getItem(int row, int col) {
        boolean bit1 = bitset.get(2*(row*8+col));
        boolean bit2 = bitset.get(2*(row*8+col)+1);
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
        bitset.set(2*(row*8+col), bit1);
        bitset.set(2*(row*8+col)+1, bit2);
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

    /**
     * Should use StringBuffer instead of String, but this method
     * is only for debug purposes.
     */
    public String toString() {
        String ret = "";
        for( int i=0; i<8; ++i ) {
            for( int j=0; j<8; ++j ) {
                ret += getItem(j,i);
            }
            ret += "\n";
        }
        ret += "pass: "+getPassNum()+ "\n";
        return ret;
    }

    public void toByteArray(byte []byteArray, int offset) {
        bitset.toByteArray(byteArray, offset);
        byteArray[offset+16] = (byte)passNum;
    }

    public byte []toByteArray() {
        byte []byteArray = new byte[17];
        toByteArray(byteArray, 0);
        return byteArray;
    }

    public Move getEmptyMove() {
        return new ReversiMove(0,0);
    }

    public void convertToIntArray(int [][]array) {
        for( int i=0; i<8; ++i ) {
            for( int j=0; j<8; ++j ) {
                array[i][j] = getItem(i,j);
            }
        }
    }

} // ReversiTable
