package util;

import java.io.*;

/**
 * Something like java.util.BitSet, which is not available int J2ME.
 * This version is much more simple, but good enough for me.
 */
public class BitSet  {

    private long bits[];  // this will store the bits
    private int bitNum;
    private static final int LONG_SIZE = 6;
    private final static int BITS_PER_BLOCK = 1 << LONG_SIZE;
    private final static int BIT_INDEX_MASK = BITS_PER_BLOCK - 1;

    public BitSet(int bitNum) {
	if (bitNum <= 0)
	    throw new NegativeArraySizeException(""+bitNum);

	bits = new long[(blockIndex(bitNum-1) + 1)];
        this.bitNum = bitNum;
    }

    /** 
     * Copy constuctor.
     */     
    public BitSet(BitSet old) {
        this(old.bitNum);
        System.arraycopy(old.bits, 0, bits, 0, bitNum >> LONG_SIZE);
    }

    public int length() {
        return bitNum;
    }

    private static int blockIndex(int bitIndex) {
        return bitIndex >> LONG_SIZE;
    }

    /**
     * Given a bit index, return a block that masks that bit in its block.
     */
    private static long bit(int bitIndex) {
        return 1L << (bitIndex & BIT_INDEX_MASK);
    }


    public void set(int bitIndex, boolean value) {
        if( value ) {
            set(bitIndex);
        } else {
            clear(bitIndex);
        }
    }

    /**
     * Sets the bit specified by the index to <code>true</code>.
     */
    public void set(int bitIndex) {
	if (bitIndex < 0 || bitIndex > bitNum )
	    throw new IndexOutOfBoundsException(""+bitIndex);

        int blockIndex = blockIndex(bitIndex);

        bits[blockIndex] |= bit(bitIndex);
    }

    /**
     * Sets the bit specified by the index to <code>false</code>.
     */
    public void clear(int bitIndex) {
	if (bitIndex < 0 || bitIndex > bitNum)
	    throw new IndexOutOfBoundsException(""+bitIndex);
	int blockIndex = blockIndex(bitIndex);
	bits[blockIndex] &= ~bit(bitIndex);
    }


    /**
     * Returns the value of the bit with the specified index.
     */
    public boolean get(int bitIndex) {
	if (bitIndex < 0 || bitIndex > bitNum)
	    throw new IndexOutOfBoundsException(""+bitIndex);

	int blockIndex = blockIndex(bitIndex);
        return  ((bits[blockIndex] & bit(bitIndex)) != 0);
    }

    /**
     * String representation.
     */  
    public String toString() {
	StringBuffer buffer = new StringBuffer(8*bitNum + 2);
	String separator = "";
	buffer.append('{');

	for (int i = 0 ; i < bitNum; i++) {
	    if (get(i)) {
	        buffer.append("1");
	    } else {
                buffer.append("0");
            }
        }

	buffer.append('}');
	return buffer.toString();
    }
}
