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
// 	if (bitNum <= 0)
// 	    throw new NegativeArraySizeException(""+bitNum);

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

    public BitSet(int bitNum, byte []byteArray, int offset) {
        this( bitNum );
        int retIndex = offset;
        int i = 0;
        while( i<bits.length ) {
            bits[i] = 0;
            for( int j = 56; j>=0; j -=8 ) {
                long octet = byteArray[retIndex++];
                if( octet < 0 ) {
                    octet += 256;
                }
                bits[i] |= octet << j;
            }
            System.out.println("bits["+i+"]:"+bits[i]);
            ++i;
        }
//         long lastLong = 0;
//         while( retIndex < byteArray.length ) {
//             lastLong <<= 8;
//             lastLong += byteArray[retIndex++];
//         } 
//         lastLong <<= 8 *  (8 - byteArray.length % 8);
//         bits[i] = lastLong;
//         System.out.println("bits["+i+"]:"+bits[i]);
    }

    /**
     * Copy data from 'src' BitSet to 'dst' BitSet.
     * We assume, that there is enough room for this.
     */
    public static void copy(BitSet src, BitSet dst) {
        dst.bitNum = src.bitNum;
        System.arraycopy(src.bits, 0, dst.bits, 0, src.length() >> LONG_SIZE);
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

    public void toByteArray(byte []byteArray, int offset) {
        int arrayIndex = offset;
        for( int i=0; i<bits.length; ++i ) {
            byteArray[arrayIndex++] = (byte)(bits[i] >> 56);
            byteArray[arrayIndex++] = (byte)(bits[i] >> 48);
            byteArray[arrayIndex++] = (byte)(bits[i] >> 40);
            byteArray[arrayIndex++] = (byte)(bits[i] >> 32);
            byteArray[arrayIndex++] = (byte)(bits[i] >> 24);
            byteArray[arrayIndex++] = (byte)(bits[i] >> 16);
            byteArray[arrayIndex++] = (byte)(bits[i] >> 8 );
            byteArray[arrayIndex++] = (byte)(bits[i]      );
        }        
    }

    public byte []toByteArray() {
        byte []ret = new byte[8 * bits.length];
        toByteArray(ret, 0);
        return ret;
    }
}
