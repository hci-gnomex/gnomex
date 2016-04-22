// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import com.affymetrix.genometryImpl.util.SynonymLookup;
import com.affymetrix.genometryImpl.util.Timer;
import java.io.IOException;
import java.util.regex.Matcher;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import java.util.regex.Pattern;

public final class FastaParser implements Parser
{
    private static final Pattern header_regex;
    public static final int LINELENGTH = 79;
    private static final boolean DEBUG = false;
    
    public static List<BioSeq> parseAll(final InputStream istr, final AnnotatedSeqGroup group) throws IOException {
        final List<BioSeq> seqlist = new ArrayList<BioSeq>();
        BufferedReader br = null;
        final Matcher matcher = FastaParser.header_regex.matcher("");
        try {
            br = new BufferedReader(new InputStreamReader(istr));
            String header = br.readLine();
            while (br.ready() && !Thread.currentThread().isInterrupted() && header != null) {
                matcher.reset(header);
                final boolean matched = matcher.matches();
                if (!matched) {
                    continue;
                }
                final String seqid = matcher.group(1);
                StringBuffer buf = new StringBuffer();
                String line = null;
                while (br.ready() && !Thread.currentThread().isInterrupted()) {
                    line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.length() == 0) {
                        continue;
                    }
                    final char ch = line.charAt(0);
                    if (ch == ';') {
                        continue;
                    }
                    if (ch == '>') {
                        header = line;
                        break;
                    }
                    buf.append(line);
                }
                String residues = new String(buf);
                buf.setLength(0);
                buf = null;
                residues = residues.trim();
                BioSeq seq = group.getSeq(seqid);
                if (seq == null && seqid.indexOf(32) > 0) {
                    final String name = seqid.substring(0, seqid.indexOf(32));
                    seq = group.getSeq(name);
                }
                if (seq == null) {
                    seq = group.addSeq(seqid, residues.length());
                }
                seq.setResidues(residues);
                seqlist.add(seq);
            }
        }
        finally {
            GeneralUtils.safeClose(br);
            GeneralUtils.safeClose(istr);
        }
        return seqlist;
    }
    
    public static BioSeq parseSingle(final InputStream istr, final AnnotatedSeqGroup group) throws IOException {
        final List<BioSeq> bioList = parseAll(istr, group);
        if (bioList == null) {
            return null;
        }
        return bioList.get(0);
    }
    
    public static String parseResidues(final InputStream istr) throws IOException {
        BufferedReader br = null;
        final Matcher matcher = FastaParser.header_regex.matcher("");
        String result = null;
        try {
            br = new BufferedReader(new InputStreamReader(istr));
            String header = br.readLine();
            while (br.ready() && header != null) {
                matcher.reset(header);
                final boolean matched = matcher.matches();
                if (!matched) {
                    continue;
                }
                StringBuffer buf = new StringBuffer();
                String line = null;
                while (br.ready()) {
                    line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.length() == 0) {
                        continue;
                    }
                    final char ch = line.charAt(0);
                    if (ch == ';') {
                        continue;
                    }
                    if (ch == '>') {
                        header = line;
                        break;
                    }
                    buf.append(line);
                }
                result = new String(buf);
                buf.setLength(0);
                buf = null;
                result = result.trim();
            }
        }
        finally {
            GeneralUtils.safeClose(br);
            GeneralUtils.safeClose(istr);
        }
        return result;
    }
    
    public static BioSeq parse(final InputStream istr) throws IOException {
        return parse(istr, null, -1);
    }
    
    public static BioSeq parse(final InputStream istr, final BioSeq aseq, final int max_seq_length) {
        return oldparse(istr, aseq, max_seq_length);
    }
    
    private static BioSeq oldparse(final InputStream istr, final BioSeq aseq, final int max_seq_length) {
        boolean use_buffer_directly = false;
        boolean fixed_length_buffer = false;
        if (max_seq_length > 0) {
            fixed_length_buffer = true;
            use_buffer_directly = true;
        }
        else {
            fixed_length_buffer = false;
            use_buffer_directly = false;
        }
        System.out.println("using buffer directly: " + use_buffer_directly);
        System.out.println("using fixed length buffer: " + fixed_length_buffer);
        final Timer tim = new Timer();
        tim.start();
        BioSeq seq = aseq;
        String seqid = "unknown";
        StringBuffer buf;
        if (fixed_length_buffer) {
            buf = new StringBuffer(max_seq_length);
        }
        else {
            buf = new StringBuffer();
        }
        final Matcher matcher = FastaParser.header_regex.matcher("");
        int line_count = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(istr));
            while (br.ready() && !Thread.currentThread().isInterrupted()) {
                final String header = br.readLine();
                if (header == null) {
                    break;
                }
                matcher.reset(header);
                final boolean matched = matcher.matches();
                if (matched) {
                    seqid = matcher.group(1);
                    break;
                }
            }
            String line = null;
            while (br.ready() && !Thread.currentThread().isInterrupted()) {
                line = br.readLine();
                if (line != null) {
                    if (line.length() == 0) {
                        continue;
                    }
                    final char ch = line.charAt(0);
                    if (ch == ';') {
                        continue;
                    }
                    if (ch == '>') {
                        break;
                    }
                    buf.append(line);
                    ++line_count;
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(br);
            GeneralUtils.safeClose(istr);
        }
        String residues = null;
        if (use_buffer_directly) {
            residues = new String(buf);
        }
        else {
            String temp_residues = new String(buf);
            residues = new String(temp_residues);
            temp_residues = null;
            System.out.println("done constructing residues via array");
            buf = null;
        }
        System.out.println("id: " + seqid);
        if (seq == null) {
            seq = new BioSeq(seqid, seqid, residues.length());
            seq.setResidues(residues);
        }
        else if (SynonymLookup.getDefaultLookup().isSynonym(seq.getID(), seqid)) {
            seq.setResidues(residues);
        }
        else {
            System.out.println("*****  ABORTING MERGE, sequence ids don't match: old seq id = " + seq.getID() + ", new seq id = " + seqid);
        }
        System.out.println("time to execute: " + tim.read() / 1000.0f);
        System.out.println("done loading fasta file");
        System.out.println("length of sequence: " + residues.length());
        return seq;
    }
    
    public static byte[] readFASTA(final File seqfile, final int begin_sequence, int end_sequence) throws FileNotFoundException, IOException, IllegalArgumentException {
        if (begin_sequence < 0) {
            throw new IllegalArgumentException("beginning sequence:" + begin_sequence + " was negative.");
        }
        if (end_sequence < begin_sequence) {
            throw new IllegalArgumentException("range " + begin_sequence + ":" + end_sequence + " was negative.");
        }
        if (!seqfile.exists()) {
            throw new FileNotFoundException("Couldn't find file " + seqfile.toString());
        }
        if (begin_sequence > seqfile.length()) {
            throw new IllegalArgumentException("beginning sequence:" + begin_sequence + " larger than file size:" + (int)seqfile.length());
        }
        if (seqfile.length() <= 2147483647L) {
            end_sequence = Math.min(end_sequence, (int)seqfile.length());
        }
        if (begin_sequence == end_sequence) {
            return null;
        }
        byte[] buf = null;
        final DataInputStream dis = new DataInputStream(new FileInputStream(seqfile));
        final BufferedInputStream bis = new BufferedInputStream(dis);
        try {
            final byte[] header = skipFASTAHeader(seqfile.getName(), bis);
            final int header_len = (header == null) ? 0 : header.length;
            bis.reset();
            long skip_status = BlockUntilSkipped(bis, header_len);
            if (skip_status != header_len) {
                System.out.println("skipped header past EOF");
                return buf;
            }
            final int full_lines_to_skip = begin_sequence / 79;
            final int chars_to_skip = 80 * full_lines_to_skip;
            int line_location = begin_sequence % 79;
            skip_status = BlockUntilSkipped(bis, chars_to_skip);
            if (skip_status != chars_to_skip) {
                System.out.println("skipped lines past EOF");
                return buf;
            }
            skip_status = BlockUntilSkipped(bis, line_location);
            if (skip_status != line_location) {
                System.out.println(line_location + "," + skip_status + ": skipped nucleotides past EOF");
                return buf;
            }
            final int nucleotides_len = end_sequence - begin_sequence;
            buf = new byte[nucleotides_len];
            int i = 0;
            while (i < nucleotides_len) {
                if (line_location == 79) {
                    final byte[] x = { 0 };
                    final int nucleotides_read = bis.read(x, 0, 1);
                    if (nucleotides_read < 1) {
                        System.out.println("Unexpected End of File at newline!");
                        return trimBuffer(buf);
                    }
                    if (nucleotides_read != 1 || x[0] != 10) {
                        throw new AssertionError((Object)("Unexpected char at end of line: " + (char)x[0] + "\nPlease verify that the FASTA file satisfies DAS/2 format assumptions."));
                    }
                    line_location = 0;
                }
                else {
                    final int nucleotides_left_on_this_line = Math.min(79 - line_location, nucleotides_len - i);
                    final int nucleotides_read = bis.read(buf, i, nucleotides_left_on_this_line);
                    if (nucleotides_read == -1) {
                        return trimBuffer(buf);
                    }
                    i += nucleotides_read;
                    line_location += nucleotides_read;
                    if (nucleotides_read != nucleotides_left_on_this_line) {
                        System.out.println("Unexpected EOF: i,nucleotides_read" + i + " " + nucleotides_read);
                        return trimBuffer(buf);
                    }
                    continue;
                }
            }
            return trimBuffer(buf);
        }
        finally {
            GeneralUtils.safeClose(bis);
            GeneralUtils.safeClose(dis);
        }
    }
    
    public static byte[] generateNewHeader(final String chrom_name, final String genome_name, final int start, final int end) {
        final String header = ">" + chrom_name + " range:" + NumberFormat.getIntegerInstance().format(start) + "-" + NumberFormat.getIntegerInstance().format(end) + " interbase genome:" + genome_name + "\n";
        final byte[] result = new byte[header.length()];
        for (int i = 0; i < header.length(); ++i) {
            result[i] = (byte)header.charAt(i);
        }
        return result;
    }
    
    private static byte[] trimBuffer(byte[] buf) {
        int i;
        for (i = buf.length; i >= 0 && (buf[i - 1] == 10 || buf[i - 1] == 0); --i) {}
        if (i == 0) {
            return null;
        }
        final byte[] buf2 = new byte[i];
        System.arraycopy(buf, 0, buf2, 0, i);
        buf = null;
        return buf2;
    }
    
    public static byte[] skipFASTAHeader(final String filename, final BufferedInputStream bis) throws IOException, UnsupportedEncodingException {
        final byte[] header = new byte[500];
        bis.mark(500);
        int bytes_to_read = header.length;
        int begin = 0;
        while (bytes_to_read > 0) {
            final int bytesRead = bis.read(header, begin, bytes_to_read);
            if (bytesRead < 0) {
                break;
            }
            begin = bytesRead;
            bytes_to_read -= bytesRead;
        }
        if (header[0] == 62) {
            for (int i = 1; i < 500; ++i) {
                if (header[i] == 10) {
                    final byte[] header2 = new byte[i + 1];
                    System.arraycopy(header, 0, header2, 0, i + 1);
                    return header2;
                }
            }
            throw new UnsupportedEncodingException("file " + filename + " header does not match expected FASTA format.");
        }
        return null;
    }
    
    private static long BlockUntilSkipped(final BufferedInputStream bis, final int line_location) throws IOException {
        long skip_status;
        for (skip_status = 0L, skip_status = bis.skip(line_location); skip_status < line_location && bis.available() > 0; skip_status += bis.skip(line_location - skip_status)) {}
        return skip_status;
    }
    
    public static String getMimeType() {
        return "text/fasta";
    }
    
    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        parseAll(is, group);
        return null;
    }
    
    static {
        header_regex = Pattern.compile("^\\s*>(.+)");
    }
}
