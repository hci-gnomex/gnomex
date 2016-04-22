// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import com.affymetrix.genometryImpl.util.TwoBitIterator;
import com.affymetrix.genometryImpl.util.SynonymLookup;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.nio.ByteOrder;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.nio.ByteBuffer;
import com.affymetrix.genometryImpl.util.SeekableBufferedStream;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import java.nio.charset.Charset;

public final class TwoBitParser implements Parser
{
    private static final int MAGIC_NUMBER = 440477507;
    private static final int INT_SIZE = 4;
    private static int BUFFER_SIZE;
    private static final int BYTE_MASK = 255;
    private static final long INT_MASK = -1L;
    private static final Charset charset;
    private static int BUFSIZE;
    private static final boolean DEBUG = false;
    
    public static List<BioSeq> parse(final URI uri, final AnnotatedSeqGroup seq_group) throws FileNotFoundException, IOException {
        final SeekableBufferedStream bistr = new SeekableBufferedStream(LocalUrlCacher.getSeekableStream(uri));
        final ByteBuffer buffer = ByteBuffer.allocate(TwoBitParser.BUFFER_SIZE);
        loadBuffer(bistr, buffer);
        final int seq_count = readFileHeader(buffer);
        final List<BioSeq> seqs = readSequenceIndex(uri, bistr, buffer, seq_count, seq_group);
        GeneralUtils.safeClose(bistr);
        return seqs;
    }
    
    public static BioSeq parse(final URI uri, final AnnotatedSeqGroup seq_group, final String seqName) throws FileNotFoundException, IOException {
        final SeekableBufferedStream bistr = new SeekableBufferedStream(LocalUrlCacher.getSeekableStream(uri));
        final ByteBuffer buffer = ByteBuffer.allocate(TwoBitParser.BUFFER_SIZE);
        loadBuffer(bistr, buffer);
        final int seq_count = readFileHeader(buffer);
        final BioSeq retseq = readSequenceIndex(uri, bistr, buffer, seq_count, seq_group, seqName);
        GeneralUtils.safeClose(bistr);
        return retseq;
    }
    
    public static BioSeq parse(final URI uri) throws FileNotFoundException, IOException {
        return parse(uri, new AnnotatedSeqGroup("No_Data")).get(0);
    }
    
    public static boolean parse(final URI uri, final OutputStream out) throws FileNotFoundException, IOException {
        final BioSeq seq = parse(uri, new AnnotatedSeqGroup("No_Data")).get(0);
        return writeAnnotations(seq, 0, seq.getLength(), out);
    }
    
    public static boolean parse(final URI uri, final int start, final int end, final OutputStream out) throws FileNotFoundException, IOException {
        final BioSeq seq = parse(uri, new AnnotatedSeqGroup("No_Data")).get(0);
        return writeAnnotations(seq, start, end, out);
    }
    
    public static boolean parse(final URI uri, final AnnotatedSeqGroup seq_group, final OutputStream out) throws FileNotFoundException, IOException {
        final BioSeq seq = parse(uri, seq_group).get(0);
        return writeAnnotations(seq, 0, seq.getLength(), out);
    }
    
    public static boolean parse(final URI uri, final AnnotatedSeqGroup seq_group, final int start, final int end, final OutputStream out) throws FileNotFoundException, IOException {
        final BioSeq seq = parse(uri, seq_group).get(0);
        return writeAnnotations(seq, start, end, out);
    }
    
    private static String getString(final ByteBuffer buffer, final int length) {
        final byte[] string = new byte[length];
        buffer.get(string);
        return new String(string, TwoBitParser.charset);
    }
    
    private static void loadBuffer(final SeekableBufferedStream bistr, final ByteBuffer buffer) throws IOException {
        buffer.rewind();
        bistr.read(buffer.array());
        buffer.rewind();
    }
    
    private static int readFileHeader(final ByteBuffer buffer) throws IOException {
        int magic = buffer.getInt();
        if (magic != 440477507) {
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.rewind();
            magic = buffer.getInt();
        }
        if (magic != 440477507) {
            throw new IOException("File is not in 2bit format:  Bad magic (0x" + Integer.toHexString(magic) + " actual, 0x" + Integer.toHexString(440477507) + " expected)");
        }
        final int version = buffer.getInt();
        final int seq_count = buffer.getInt();
        final int reserved = buffer.getInt();
        if (version != 0 || reserved != 0) {
            throw new IOException("Unsupported 2bit format: version(" + version + ") and reserved(" + reserved + ") must equal 0");
        }
        return seq_count;
    }
    
    private static List<BioSeq> readSequenceIndex(final URI uri, final SeekableBufferedStream bistr, final ByteBuffer buffer, final int seq_count, final AnnotatedSeqGroup seq_group) throws IOException {
        final List<BioSeq> seqs = new ArrayList<BioSeq>();
        final Map<String, Long> seqOffsets = new HashMap<String, Long>();
        long position = bistr.position();
        for (int i = 0; i < seq_count; ++i) {
            if (buffer.remaining() < 4) {
                position = updateBuffer(bistr, buffer, position);
            }
            final int name_length = buffer.get() & 0xFF;
            if (buffer.remaining() < name_length + 4) {
                position = updateBuffer(bistr, buffer, position);
            }
            final String name = getString(buffer, name_length);
            final long offset = buffer.getInt() & -1L;
            seqOffsets.put(name, offset);
        }
        for (final Map.Entry<String, Long> seqOffset : seqOffsets.entrySet()) {
            seqs.add(readSequenceHeader(uri, bistr, buffer.order(), seqOffset.getValue(), seq_group, seqOffset.getKey()));
        }
        return seqs;
    }
    
    private static BioSeq readSequenceIndex(final URI uri, final SeekableBufferedStream bistr, final ByteBuffer buffer, final int seq_count, final AnnotatedSeqGroup seq_group, final String synonym) throws IOException {
        BioSeq seq = null;
        final SynonymLookup chrLookup = SynonymLookup.getChromosomeLookup();
        long position = bistr.position();
        for (int i = 0; i < seq_count; ++i) {
            if (buffer.remaining() < 4) {
                position = updateBuffer(bistr, buffer, position);
            }
            final int name_length = buffer.get() & 0xFF;
            if (buffer.remaining() < name_length + 4) {
                position = updateBuffer(bistr, buffer, position);
            }
            final String name = getString(buffer, name_length);
            final long offset = buffer.getInt() & -1L;
            if (name.equals(synonym) || chrLookup.isSynonym(name, synonym)) {
                seq = readSequenceHeader(uri, bistr, buffer.order(), offset, seq_group, name);
                break;
            }
        }
        return seq;
    }
    
    private static BioSeq readSequenceHeader(final URI uri, final SeekableBufferedStream bistr, final ByteOrder order, final long offset, final AnnotatedSeqGroup seq_group, final String name) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(order);
        bistr.position(offset);
        loadBuffer(bistr, buffer);
        final long size = buffer.getInt() & -1L;
        final long residueOffset = offset + 4L;
        if (size > 2147483647L) {
            throw new IOException("IGB can not handle sequences larger than 2147483647.  Offending sequence length: " + size);
        }
        final BioSeq seq = seq_group.addSeq(name, (int)size, uri.toString());
        seq.setResiduesProvider(new TwoBitIterator(uri, size, residueOffset, buffer.order()));
        return seq;
    }
    
    private static long updateBuffer(final SeekableBufferedStream bistr, final ByteBuffer buffer, final long position) throws IOException {
        bistr.position(position - buffer.remaining());
        loadBuffer(bistr, buffer);
        return bistr.position();
    }
    
    public static String getMimeType() {
        return "binary/2bit";
    }
    
    private static boolean writeAnnotations(final BioSeq seq, int start, int end, final OutputStream outstream) {
        if (seq.getResiduesProvider() == null) {
            return false;
        }
        start = Math.max(0, start);
        end = Math.max(end, start);
        end = Math.min(end, seq.getLength());
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new BufferedOutputStream(outstream));
            for (int i = start; i < end; i += TwoBitParser.BUFSIZE) {
                final String outString = seq.getResidues(i, Math.min(i + TwoBitParser.BUFSIZE, end));
                dos.writeBytes(outString);
            }
            dos.flush();
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public static void main(final String[] args) {
        final String residues = "NACNTCNNNNNNNNNNNNGTCTCANNNNNGTACTANNNNGGAATTCNNNNNCGTCATAGNNNCTAAANNN";
        final File f = new File("genometryImpl/test/data/2bit/nblocks.2bit");
        ByteArrayOutputStream outStream = null;
        try {
            int start = 11;
            int end = residues.length() + 4;
            outStream = new ByteArrayOutputStream();
            final URI uri = URI.create("http://test.bioviz.org/testdata/nblocks.2bit");
            parse(uri, start, end, outStream);
            System.out.println("Result   :" + outStream.toString());
            if (start < end) {
                start = Math.max(0, start);
                start = Math.min(residues.length(), start);
                end = Math.max(0, end);
                end = Math.min(residues.length(), end);
            }
            else {
                start = 0;
                end = 0;
            }
            System.out.println("Expected :" + residues.substring(start, end));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(outStream);
        }
    }
    
    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        throw new IllegalStateException("2bit should not be processed here");
    }
    
    static {
        TwoBitParser.BUFFER_SIZE = 4096;
        charset = Charset.forName("ASCII");
        TwoBitParser.BUFSIZE = 65536;
    }
}
