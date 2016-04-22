// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symloader;

import com.affymetrix.genometryImpl.util.Timer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Map;
import java.util.HashMap;
import com.affymetrix.genometryImpl.util.SeekableBufferedStream;

public class TwoBitNew extends SymLoader
{
    public static boolean DEBUG;
    public int DEFAULT_BUFFER_SIZE;
    private SeekableBufferedStream raf;
    private boolean reverse;
    private HashMap<String, Long> seq2pos;
    private String cur_seq_name;
    private long[][] cur_nn_blocks;
    private long[][] cur_mask_blocks;
    private long cur_seq_pos;
    private long cur_dna_size;
    private int cur_nn_block_num;
    private int cur_mask_block_num;
    private int[] cur_bits;
    private byte[] buffer;
    private long buffer_size;
    private long buffer_pos;
    private long start_file_pos;
    private long file_pos;
    private static final char[] bit_chars;
    private final Map<BioSeq, String> chrMap;
    
    public TwoBitNew(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        super(uri, featureName, group);
        this.DEFAULT_BUFFER_SIZE = 10000;
        this.reverse = false;
        this.seq2pos = new HashMap<String, Long>();
        this.chrMap = new HashMap<BioSeq, String>();
    }
    
    public void init() throws Exception {
        if (this.isInitialized) {
            return;
        }
        this.raf = new SeekableBufferedStream(LocalUrlCacher.getSeekableStream(this.uri));
        final long sign = this.readFourBytes();
        if (sign == 440477507L) {
            if (TwoBitNew.DEBUG) {
                System.err.println("2bit: Normal number architecture");
            }
        }
        else {
            if (sign != 1126646042L) {
                throw new Exception("Wrong start signature in 2BIT format");
            }
            this.reverse = true;
            if (TwoBitNew.DEBUG) {
                System.err.println("2bit: Reverse number architecture");
            }
        }
        this.readFourBytes();
        final int seq_qnt = (int)this.readFourBytes();
        this.readFourBytes();
        for (int i = 0; i < seq_qnt; ++i) {
            final int name_len = this.raf.read();
            final char[] chars = new char[name_len];
            for (int j = 0; j < name_len; ++j) {
                chars[j] = (char)this.raf.read();
            }
            final String seq_name = new String(chars);
            final BioSeq seq = this.group.getSeq(seq_name);
            if (seq != null) {
                this.chrMap.put(seq, seq_name);
                final long pos = this.readFourBytes();
                this.seq2pos.put(seq_name, pos);
                if (TwoBitNew.DEBUG) {
                    System.err.println("2bit: Sequence name=[" + seq_name + "], " + "pos=" + pos);
                }
            }
        }
        super.init();
    }
    
    @Override
    public List<BioSeq> getChromosomeList() throws Exception {
        this.init();
        return new ArrayList<BioSeq>(this.chrMap.keySet());
    }
    
    @Override
    public String getRegionResidues(final SeqSpan span) throws Exception {
        this.init();
        final BioSeq seq = span.getBioSeq();
        if (this.chrMap.containsKey(seq)) {
            this.setCurrentSequence(this.chrMap.get(seq));
            return this.getResidueString(span.getMin(), span.getMax() - span.getMin());
        }
        Logger.getLogger(TwoBit.class.getName()).log(Level.WARNING, "Seq {0} not present {1}", new Object[] { seq.getID(), this.uri.toString() });
        return "";
    }
    
    private String getResidueString(final int start, final int len) throws IOException {
        if (this.cur_seq_name == null) {
            throw new RuntimeException("Sequence is not set");
        }
        System.out.println(">" + this.cur_seq_name + " pos=" + this.cur_seq_pos + ", len=" + len);
        final char[] residues = new char[len];
        this.setCurrentSequencePosition(start);
        for (int qnt = 0; qnt < residues.length; ++qnt) {
            final int ch = this.read();
            if (ch < 0) {
                break;
            }
            residues[qnt] = (char)ch;
        }
        this.close();
        return new String(residues);
    }
    
    private long readFourBytes() throws Exception {
        long ret = 0L;
        if (!this.reverse) {
            ret = this.raf.read();
            ret += this.raf.read() * 256;
            ret += this.raf.read() * 65536;
            ret += this.raf.read() * 16777216;
        }
        else {
            ret = this.raf.read() * 16777216;
            ret += this.raf.read() * 65536;
            ret += this.raf.read() * 256;
            ret += this.raf.read();
        }
        return ret;
    }
    
    private void setCurrentSequence(final String seq_name) throws Exception {
        if (this.cur_seq_name != null) {
            throw new Exception("Sequence [" + this.cur_seq_name + "] was not closed");
        }
        if (this.seq2pos.get(seq_name) == null) {
            throw new Exception("Sequence [" + seq_name + "] was not found in 2bit file");
        }
        this.cur_seq_name = seq_name;
        final long pos = this.seq2pos.get(seq_name);
        this.raf.seek(pos);
        final long dna_size = this.readFourBytes();
        if (TwoBitNew.DEBUG) {
            System.err.println("2bit: Sequence name=[" + this.cur_seq_name + "], dna_size=" + dna_size);
        }
        this.cur_dna_size = dna_size;
        final int nn_block_qnt = (int)this.readFourBytes();
        this.cur_nn_blocks = new long[nn_block_qnt][2];
        for (int i = 0; i < nn_block_qnt; ++i) {
            this.cur_nn_blocks[i][0] = this.readFourBytes();
        }
        for (int i = 0; i < nn_block_qnt; ++i) {
            this.cur_nn_blocks[i][1] = this.readFourBytes();
        }
        if (TwoBitNew.DEBUG) {
            System.err.print("NN-blocks: ");
            for (int i = 0; i < nn_block_qnt; ++i) {
                System.err.print("[" + this.cur_nn_blocks[i][0] + "," + this.cur_nn_blocks[i][1] + "] ");
            }
            System.err.println();
        }
        final int mask_block_qnt = (int)this.readFourBytes();
        this.cur_mask_blocks = new long[mask_block_qnt][2];
        for (int j = 0; j < mask_block_qnt; ++j) {
            this.cur_mask_blocks[j][0] = this.readFourBytes();
        }
        for (int j = 0; j < mask_block_qnt; ++j) {
            this.cur_mask_blocks[j][1] = this.readFourBytes();
        }
        if (TwoBitNew.DEBUG) {
            System.err.print("Mask-blocks: ");
            for (int j = 0; j < mask_block_qnt; ++j) {
                System.err.print("[" + this.cur_mask_blocks[j][0] + "," + this.cur_mask_blocks[j][1] + "] ");
            }
            System.err.println();
        }
        this.readFourBytes();
        this.start_file_pos = this.raf.position();
        this.reset();
    }
    
    private synchronized void reset() throws IOException {
        this.cur_seq_pos = 0L;
        this.cur_nn_block_num = ((this.cur_nn_blocks.length > 0) ? 0 : -1);
        this.cur_mask_block_num = ((this.cur_mask_blocks.length > 0) ? 0 : -1);
        this.cur_bits = new int[4];
        this.file_pos = this.start_file_pos;
        this.buffer_size = 0L;
        this.buffer_pos = -1L;
    }
    
    private long getCurrentSequencePosition() {
        if (this.cur_seq_name == null) {
            throw new RuntimeException("Sequence is not set");
        }
        return this.cur_seq_pos;
    }
    
    private void setCurrentSequencePosition(final long pos) throws IOException {
        if (this.cur_seq_name == null) {
            throw new RuntimeException("Sequence is not set");
        }
        if (pos > this.cur_dna_size) {
            throw new RuntimeException("Postion is too high (more than " + this.cur_dna_size + ")");
        }
        if (this.cur_seq_pos > pos) {
            this.reset();
        }
        this.skip(pos - this.cur_seq_pos);
    }
    
    private void loadBits() throws IOException {
        if (this.buffer == null || this.buffer_pos < 0L || this.file_pos < this.buffer_pos || this.file_pos >= this.buffer_pos + this.buffer_size) {
            if (this.buffer == null || this.buffer.length != this.DEFAULT_BUFFER_SIZE) {
                this.buffer = new byte[this.DEFAULT_BUFFER_SIZE];
            }
            this.buffer_pos = this.file_pos;
            this.buffer_size = this.raf.read(this.buffer);
        }
        int cur_byte = this.buffer[(int)(this.file_pos - this.buffer_pos)] & 0xFF;
        for (int i = 0; i < 4; ++i) {
            this.cur_bits[3 - i] = cur_byte % 4;
            cur_byte /= 4;
        }
    }
    
    private int read() throws IOException {
        if (this.cur_seq_name == null) {
            throw new IOException("Sequence is not set");
        }
        if (this.cur_seq_pos == this.cur_dna_size) {
            if (TwoBitNew.DEBUG) {
                System.err.println("End of sequence (file position:" + this.raf.position() + " )");
            }
            return -1;
        }
        final int bit_num = (int)this.cur_seq_pos % 4;
        if (bit_num == 0) {
            this.loadBits();
        }
        else if (bit_num == 3) {
            ++this.file_pos;
        }
        char ret = 'N';
        if (this.cur_nn_block_num >= 0 && this.cur_nn_blocks[this.cur_nn_block_num][0] <= this.cur_seq_pos) {
            if (this.cur_bits[bit_num] != 0) {
                throw new IOException("Wrong data in NN-block (" + this.cur_bits[bit_num] + ") " + "at position " + this.cur_seq_pos);
            }
            if (this.cur_nn_blocks[this.cur_nn_block_num][0] + this.cur_nn_blocks[this.cur_nn_block_num][1] == this.cur_seq_pos + 1L) {
                ++this.cur_nn_block_num;
                if (this.cur_nn_block_num >= this.cur_nn_blocks.length) {
                    this.cur_nn_block_num = -1;
                }
            }
            ret = 'N';
        }
        else {
            ret = TwoBitNew.bit_chars[this.cur_bits[bit_num]];
        }
        if (this.cur_mask_block_num >= 0 && this.cur_mask_blocks[this.cur_mask_block_num][0] <= this.cur_seq_pos) {
            ret = Character.toLowerCase(ret);
            if (this.cur_mask_blocks[this.cur_mask_block_num][0] + this.cur_mask_blocks[this.cur_mask_block_num][1] == this.cur_seq_pos + 1L) {
                ++this.cur_mask_block_num;
                if (this.cur_mask_block_num >= this.cur_mask_blocks.length) {
                    this.cur_mask_block_num = -1;
                }
            }
        }
        ++this.cur_seq_pos;
        return ret;
    }
    
    private synchronized long skip(long n) throws IOException {
        if (this.cur_seq_name == null) {
            throw new IOException("Sequence is not set");
        }
        if (n < 4L) {
            int ret;
            for (ret = 0; ret < n && this.read() >= 0; ++ret) {}
            return ret;
        }
        if (n > this.cur_dna_size - this.cur_seq_pos) {
            n = this.cur_dna_size - this.cur_seq_pos;
        }
        this.cur_seq_pos += n;
        this.file_pos = this.start_file_pos + this.cur_seq_pos / 4L;
        this.raf.seek(this.file_pos);
        if (this.cur_seq_pos % 4L != 0L) {
            this.loadBits();
        }
        while (this.cur_nn_block_num >= 0 && this.cur_nn_blocks[this.cur_nn_block_num][0] + this.cur_nn_blocks[this.cur_nn_block_num][1] <= this.cur_seq_pos) {
            ++this.cur_nn_block_num;
            if (this.cur_nn_block_num >= this.cur_nn_blocks.length) {
                this.cur_nn_block_num = -1;
            }
        }
        while (this.cur_mask_block_num >= 0 && this.cur_mask_blocks[this.cur_mask_block_num][0] + this.cur_mask_blocks[this.cur_mask_block_num][1] <= this.cur_seq_pos) {
            ++this.cur_mask_block_num;
            if (this.cur_mask_block_num >= this.cur_mask_blocks.length) {
                this.cur_mask_block_num = -1;
            }
        }
        return n;
    }
    
    private void close() throws IOException {
        this.cur_seq_name = null;
        this.cur_nn_blocks = null;
        this.cur_mask_blocks = null;
        this.cur_seq_pos = -1L;
        this.cur_dna_size = -1L;
        this.cur_nn_block_num = -1;
        this.cur_mask_block_num = -1;
        this.cur_bits = null;
        this.buffer_size = 0L;
        this.buffer_pos = -1L;
        this.file_pos = -1L;
        this.start_file_pos = -1L;
    }
    
    private int available() throws IOException {
        if (this.cur_seq_name == null) {
            throw new IOException("Sequence is not set");
        }
        return (int)(this.cur_dna_size - this.cur_seq_pos);
    }
    
    private void closeParser() throws Exception {
        this.raf.close();
    }
    
    private String loadFragment(final long seq_pos, final int len) throws IOException {
        if (this.cur_seq_name == null) {
            throw new IOException("Sequence is not set");
        }
        this.setCurrentSequencePosition(seq_pos);
        final char[] ret = new char[len];
        int i;
        for (i = 0; i < len; ++i) {
            final int ch = this.read();
            if (ch < 0) {
                break;
            }
            ret[i] = (char)ch;
        }
        return new String(ret, 0, i);
    }
    
    private void printFastaSequence() throws IOException {
        if (this.cur_seq_name == null) {
            throw new RuntimeException("Sequence is not set");
        }
        this.printFastaSequence(this.cur_dna_size - this.cur_seq_pos);
    }
    
    private void printFastaSequence(final long len) throws IOException {
        if (this.cur_seq_name == null) {
            throw new RuntimeException("Sequence is not set");
        }
        System.out.println(">" + this.cur_seq_name + " pos=" + this.cur_seq_pos + ", len=" + len);
        final char[] line = new char[60];
        boolean end = false;
        long qnt_all = 0L;
        while (!end) {
            int qnt;
            for (qnt = 0; qnt < line.length && qnt_all < len; ++qnt, ++qnt_all) {
                final int ch = this.read();
                if (ch < 0) {
                    end = true;
                    break;
                }
                line[qnt] = (char)ch;
            }
            if (qnt > 0) {
                System.out.println(new String(line, 0, qnt));
            }
            if (qnt_all >= len) {
                end = true;
            }
        }
    }
    
    public static void main(final String[] args) throws Exception {
        final TwoBitNew p = new TwoBitNew(new URI("http://igbquickload.org/quickload/H_sapiens_Feb_2009/H_sapiens_Feb_2009.2bit"), null, null);
        final Timer timer = new Timer();
        timer.start();
        p.printFastaSequence(100000L);
        timer.print();
        p.close();
        p.closeParser();
    }
    
    static {
        TwoBitNew.DEBUG = false;
        bit_chars = new char[] { 'T', 'C', 'A', 'G' };
    }
}
