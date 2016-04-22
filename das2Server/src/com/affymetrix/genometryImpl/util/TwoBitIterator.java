// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import java.io.IOException;
import java.nio.ByteBuffer;
import com.affymetrix.genometryImpl.symmetry.SimpleMutableSeqSymmetry;
import java.nio.ByteOrder;
import com.affymetrix.genometryImpl.symmetry.MutableSeqSymmetry;
import java.net.URI;

public final class TwoBitIterator implements SearchableCharIterator
{
    private static final int BUFFER_SIZE = 4096;
    private static final int INT_SIZE = 4;
    private static final int RESIDUES_PER_BYTE = 4;
    private static final int BYTE_MASK = 255;
    private static final int CHAR_MASK = 3;
    private static final char[] BASES;
    private boolean initialized;
    private final URI uri;
    private long length;
    private long offset;
    private final MutableSeqSymmetry nBlocks;
    private final MutableSeqSymmetry maskBlocks;
    private final ByteOrder byteOrder;
    
    public TwoBitIterator(final URI uri, final long length, final long offset, final ByteOrder byteOrder) {
        this.initialized = false;
        this.uri = uri;
        this.length = length;
        this.offset = offset;
        this.nBlocks = new SimpleMutableSeqSymmetry();
        this.maskBlocks = new SimpleMutableSeqSymmetry();
        this.byteOrder = byteOrder;
        if (this.length > 2147483647L) {
            throw new IllegalArgumentException("IGB can not handle sequences larger than 2147483647.  Offending sequence length: " + length);
        }
    }
    
    private void init() {
        if (this.initialized) {
            return;
        }
        SeekableBufferedStream bistr = null;
        try {
            bistr = new SeekableBufferedStream(LocalUrlCacher.getSeekableStream(this.uri));
            bistr.position(this.offset);
            final ByteBuffer buffer = ByteBuffer.allocate(4096);
            buffer.order(this.byteOrder);
            loadBuffer(bistr, buffer);
            readBlocks(bistr, buffer, this.nBlocks);
            this.offset += 4 + this.nBlocks.getSpanCount() * 4 * 2;
            readBlocks(bistr, buffer, this.maskBlocks);
            this.offset += 4 + this.maskBlocks.getSpanCount() * 4 * 2;
            if (buffer.getInt() != 0) {
                throw new IOException("Unknown 2bit format: sequence's reserved field is non zero");
            }
            this.offset += 4L;
            this.initialized = true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(bistr);
        }
    }
    
    private static void readBlocks(final SeekableBufferedStream bistr, final ByteBuffer buffer, final MutableSeqSymmetry sym) throws IOException {
        long position = bistr.position();
        if (buffer.remaining() < 4) {
            position = updateBuffer(bistr, buffer, position);
        }
        final int block_count = buffer.getInt();
        final int[] blockStarts = new int[block_count];
        for (int i = 0; i < block_count; ++i) {
            if (buffer.remaining() < 4) {
                position = updateBuffer(bistr, buffer, position);
            }
            blockStarts[i] = buffer.getInt();
        }
        for (int i = 0; i < block_count; ++i) {
            if (buffer.remaining() < 4) {
                position = updateBuffer(bistr, buffer, position);
            }
            sym.addSpan(new SimpleSeqSpan(blockStarts[i], blockStarts[i] + buffer.getInt(), null));
        }
    }
    
    private static long updateBuffer(final SeekableBufferedStream bistr, final ByteBuffer buffer, final long position) throws IOException {
        bistr.position(position - buffer.remaining());
        loadBuffer(bistr, buffer);
        return bistr.position();
    }
    
    private static void loadBuffer(final SeekableBufferedStream bistr, final ByteBuffer buffer) throws IOException {
        buffer.rewind();
        bistr.read(buffer.array());
        buffer.rewind();
    }
    
    @Override
    public String substring(int start, int end) {
        this.init();
        SeekableBufferedStream bistr = null;
        try {
            start = Math.max(0, start);
            end = Math.max(end, start);
            end = Math.min(end, this.getLength());
            final int requiredLength = end - start;
            final long startOffset = start / 4;
            final long bytesToRead = calculateBytesToRead(start, end);
            int beginLength = Math.min(4 - start % 4, requiredLength);
            int endLength = Math.min(end % 4, requiredLength);
            if (bytesToRead == 1L) {
                if (start % 4 == 0) {
                    beginLength = 0;
                }
                else {
                    endLength = 0;
                }
            }
            bistr = new SeekableBufferedStream(LocalUrlCacher.getSeekableStream(this.uri));
            bistr.position(this.offset + startOffset);
            return this.parse(bistr, start, bytesToRead, requiredLength, beginLength, endLength);
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(TwoBitIterator.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex2) {
            Logger.getLogger(TwoBitIterator.class.getName()).log(Level.SEVERE, null, ex2);
        }
        finally {
            GeneralUtils.safeClose(bistr);
        }
        return "";
    }
    
    private String parse(final SeekableBufferedStream bistr, final int start, final long bytesToRead, final int requiredLength, final int beginLength, final int endLength) throws IOException {
        final char[] residues = new char[requiredLength];
        final byte[] valueBuffer = new byte[4096];
        int residueCounter = 0;
        long residuePosition = start;
        final MutableSeqSymmetry tempNBlocks = GetBlocks(start, this.nBlocks);
        final MutableSeqSymmetry tempMaskBlocks = GetBlocks(start, this.maskBlocks);
        final ByteBuffer buffer = ByteBuffer.allocate(Math.max(requiredLength, 4096));
        buffer.order(this.byteOrder);
        loadBuffer(bistr, buffer);
        SeqSpan nBlock = null;
        SeqSpan maskBlock = null;
        char[] temp = null;
        for (int i = 0; i < bytesToRead; i += 4096) {
            buffer.get(valueBuffer);
            for (int k = 0; k < 4096 && residueCounter < requiredLength; ++k) {
                temp = this.parseByte(valueBuffer[k], k, bytesToRead, start, requiredLength, beginLength, endLength);
                for (int j = 0; j < temp.length && residueCounter < requiredLength; residues[residueCounter++] = temp[j], ++residuePosition, ++j) {
                    nBlock = processResidue(residuePosition, temp, j, nBlock, tempNBlocks, false);
                    maskBlock = processResidue(residuePosition, temp, j, maskBlock, tempMaskBlocks, true);
                }
            }
            bistr.position(bistr.position() + 4096L);
        }
        return new String(residues);
    }
    
    private static long calculateBytesToRead(final long start, final long end) {
        if (start / 4L == end / 4L) {
            return 1L;
        }
        final int endExtra = (end % 4L != 0L) ? 1 : 0;
        final long bytesToRead = end / 4L - start / 4L + endExtra;
        return bytesToRead;
    }
    
    private static MutableSeqSymmetry GetBlocks(final long start, final MutableSeqSymmetry blocks) {
        final MutableSeqSymmetry tempBlocks = new SimpleMutableSeqSymmetry();
        for (int i = 0; i < blocks.getSpanCount(); ++i) {
            final SeqSpan span = blocks.getSpan(i);
            if (start <= span.getStart() || start < span.getEnd()) {
                tempBlocks.addSpan(span);
            }
        }
        return tempBlocks;
    }
    
    private static SeqSpan processResidue(final long residuePosition, final char[] temp, final int pos, SeqSpan block, final MutableSeqSymmetry blocks, final boolean isMask) {
        if (block == null) {
            block = GetNextBlock(blocks);
        }
        if (block != null) {
            if (residuePosition == block.getEnd()) {
                blocks.removeSpan(block);
                block = null;
            }
            else if (residuePosition >= block.getStart()) {
                if (isMask) {
                    temp[pos] = Character.toLowerCase(temp[pos]);
                }
                else {
                    temp[pos] = 'N';
                }
            }
        }
        return block;
    }
    
    private static SeqSpan GetNextBlock(final MutableSeqSymmetry Blocks) {
        if (Blocks.getSpanCount() > 0) {
            return Blocks.getSpan(0);
        }
        return null;
    }
    
    private char[] parseByte(final byte valueBuffer, final int k, final long bytesToRead, final int start, final int requiredLength, final int beginLength, final int endLength) {
        char[] temp = null;
        if (bytesToRead == 1L) {
            temp = parseByte(valueBuffer, start % 4, requiredLength);
        }
        else if (k == 0 && beginLength != 0) {
            temp = parseByte(valueBuffer, beginLength, true);
        }
        else if (k == bytesToRead - 1L && endLength != 0) {
            temp = parseByte(valueBuffer, endLength, false);
        }
        else {
            temp = parseByte(valueBuffer);
        }
        return temp;
    }
    
    private static char[] parseByte(final byte valueBuffer, final int size, final boolean isFirst) {
        final char[] temp = parseByte(valueBuffer);
        final char[] newTemp = new char[size];
        final int skip = isFirst ? (temp.length - size) : 0;
        for (int i = 0; i < size; ++i) {
            newTemp[i] = temp[skip + i];
        }
        return newTemp;
    }
    
    private static char[] parseByte(final byte valueBuffer, final int position, final int length) {
        final char[] temp = parseByte(valueBuffer);
        final char[] newTemp = new char[length];
        for (int i = 0; i < length; ++i) {
            newTemp[i] = temp[position + i];
        }
        return newTemp;
    }
    
    private static char[] parseByte(final byte valueBuffer) {
        final char[] temp = new char[4];
        int value = valueBuffer & 0xFF;
        for (int j = 4; j > 0; --j) {
            final int dna = value & 0x3;
            value >>= 2;
            temp[j - 1] = TwoBitIterator.BASES[dna];
        }
        return temp;
    }
    
    @Override
    public int indexOf(final String needle, final int offset) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public int getLength() {
        return (int)this.length;
    }
    
    static {
        BASES = new char[] { 'T', 'C', 'A', 'G' };
    }
}
