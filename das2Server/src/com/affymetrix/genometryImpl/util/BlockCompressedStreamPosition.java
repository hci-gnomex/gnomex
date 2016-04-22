// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

public class BlockCompressedStreamPosition
{
    private static final long COMPRESSED_BLOCK_SIZE = 65536L;
    public static final double APPROXIMATE_COMPRESS_RATIO = 2.0;
    public static final int CHUNK_SIZE = 0;
    private final long blockAddress;
    private final int currentOffset;
    
    public BlockCompressedStreamPosition(final long pos) {
        this(pos >> 16, (int)(pos & 0xFFFFL));
    }
    
    public BlockCompressedStreamPosition(final long blockAddress, final int currentOffset) {
        this.blockAddress = blockAddress;
        this.currentOffset = currentOffset;
    }
    
    public long getApproximatePosition() {
        return (long)(this.blockAddress * 2.0 + this.currentOffset);
    }
    
    @Override
    public String toString() {
        return "" + this.blockAddress + ":" + this.currentOffset;
    }
}
