// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import com.affymetrix.genometryImpl.GenometryModel;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.thread.CThreadHolder;
import com.affymetrix.genometryImpl.thread.ProgressUpdater;
import com.affymetrix.genometryImpl.thread.PositionCalculator;
import org.apache.commons.lang3.mutable.MutableLong;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import com.affymetrix.genometryImpl.util.NibbleIterator;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import com.affymetrix.genometryImpl.util.SeekableBufferedStream;
import net.sf.samtools.util.SeekableStream;
import com.affymetrix.genometryImpl.util.Timer;
import java.io.IOException;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;

public final class NibbleResiduesParser implements Parser
{
    private static int BUFSIZE;
    
    public static BioSeq parse(final InputStream istr, final AnnotatedSeqGroup seq_group) throws IOException {
        return parse(istr, seq_group, 0, Integer.MAX_VALUE);
    }
    
    public static BioSeq parse(final InputStream istr, final AnnotatedSeqGroup seq_group, int start, int end) throws IOException {
        BioSeq result_seq = null;
        InputStream bis = null;
        DataInputStream dis = null;
        try {
            final Timer tim = new Timer();
            tim.start();
            if (istr instanceof SeekableStream) {
                bis = (InputStream)new SeekableBufferedStream((SeekableStream)istr);
            }
            else if (istr instanceof BufferedInputStream) {
                bis = istr;
            }
            else {
                bis = new BufferedInputStream(istr);
            }
            dis = new DataInputStream(bis);
            final String name = dis.readUTF();
            dis.readUTF();
            final int total_residues = dis.readInt();
            if (start < end) {
                start = Math.max(0, start);
                start = Math.min(total_residues, start);
                end = Math.max(0, end);
                end = Math.min(total_residues, end);
            }
            else {
                start = 0;
                end = 0;
            }
            int num_residues = end - start;
            num_residues = Math.max(0, num_residues);
            final BioSeq existing_seq = seq_group.getSeq(name);
            if (existing_seq != null) {
                result_seq = existing_seq;
            }
            else {
                result_seq = seq_group.addSeq(name, num_residues);
            }
            int skipped;
            for (int bytes_to_skip = start / 2; bytes_to_skip > 0; bytes_to_skip -= skipped) {
                skipped = (int)bis.skip(bytes_to_skip);
                if (skipped < 0) {
                    break;
                }
            }
            Logger.getLogger(NibbleResiduesParser.class.getName()).log(Level.INFO, "Chromosome: {0} : residues: {1}", new Object[] { result_seq, num_residues });
            SetResiduesIterator(start, end, dis, result_seq);
        }
        finally {
            GeneralUtils.safeClose(dis);
            GeneralUtils.safeClose(bis);
        }
        return result_seq;
    }
    
    public static boolean parse(final InputStream istr, final OutputStream output) throws FileNotFoundException, IOException {
        return parse(istr, new AnnotatedSeqGroup("No_Data"), output);
    }
    
    public static boolean parse(final InputStream istr, final AnnotatedSeqGroup seq_group, final OutputStream output) throws IOException {
        return parse(istr, seq_group, 0, Integer.MAX_VALUE, output);
    }
    
    public static boolean parse(final InputStream istr, final int start, final int end, final OutputStream output) throws FileNotFoundException, IOException {
        return parse(istr, new AnnotatedSeqGroup("No_Data"), start, end, output);
    }
    
    public static boolean parse(final InputStream istr, final AnnotatedSeqGroup seq_group, final int start, final int end, final OutputStream output) throws IOException {
        final BioSeq seq = parse(istr, seq_group, start, end);
        return writeAnnotations(seq, start, end, output);
    }
    
    public static BioSeq determineChromosome(final InputStream istr, final AnnotatedSeqGroup seq_group) {
        BioSeq result_seq = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        try {
            if (istr instanceof BufferedInputStream) {
                bis = (BufferedInputStream)istr;
            }
            else {
                bis = new BufferedInputStream(istr);
            }
            dis = new DataInputStream(bis);
            final String name = dis.readUTF();
            dis.readUTF();
            final int total_residues = dis.readInt();
            if (seq_group.getSeq(name) != null) {
                return seq_group.getSeq(name);
            }
            result_seq = new BioSeq(name, seq_group.getID(), total_residues);
        }
        catch (Exception ex) {
            Logger.getLogger(NibbleResiduesParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result_seq;
    }
    
    public static byte[] readBNIB(final File seqfile) throws FileNotFoundException, IOException {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new FileInputStream(seqfile));
            final int filesize = (int)seqfile.length();
            final byte[] buf = new byte[filesize];
            dis.readFully(buf);
            return buf;
        }
        finally {
            GeneralUtils.safeClose(dis);
        }
    }
    
    private static void SetResiduesIterator(final int start, final int end, final DataInputStream dis, final BioSeq result_seq) throws IOException {
        final int num_residues = end - start;
        final int extra = Math.max(end % 2, num_residues % 2);
        byte[] nibble_array = new byte[num_residues / 2 + extra];
        final int first = start % 2;
        final int last = first + num_residues;
        dis.readFully(nibble_array);
        if ((result_seq.getMin() < start || result_seq.getMax() > end) && first == 1) {
            final String temp = NibbleIterator.nibblesToString(nibble_array, first, last);
            nibble_array = NibbleIterator.stringToNibbles(temp, 0, temp.length());
        }
        final NibbleIterator residues_provider = new NibbleIterator(nibble_array, num_residues);
        result_seq.setResiduesProvider(residues_provider);
    }
    
    public static void writeBinaryFile(final String file_name, final String seqname, final String seqversion, final String residues) throws IOException {
        FileOutputStream fos = null;
        DataOutputStream dos = null;
        BufferedOutputStream bos = null;
        try {
            final File fil = new File(file_name);
            fos = new FileOutputStream(fil);
            bos = new BufferedOutputStream(fos);
            dos = new DataOutputStream(bos);
            dos.writeUTF(seqname);
            dos.writeUTF(seqversion);
            dos.writeInt(residues.length());
            System.out.println("creating nibble array");
            final byte[] nibble_array = NibbleIterator.stringToNibbles(residues, 0, residues.length());
            System.out.println("done creating nibble array, now writing nibble array out");
            final int chunk_length = 65536;
            if (nibble_array.length > chunk_length) {
                int bytepos;
                for (bytepos = 0; bytepos < nibble_array.length - chunk_length; bytepos += chunk_length) {
                    dos.write(nibble_array, bytepos, chunk_length);
                }
                dos.write(nibble_array, bytepos, nibble_array.length - bytepos);
            }
            else {
                dos.write(nibble_array);
            }
            System.out.println("done writing out nibble file");
        }
        finally {
            GeneralUtils.safeClose(dos);
            GeneralUtils.safeClose(bos);
            GeneralUtils.safeClose(fos);
        }
    }
    
    private static boolean writeAnnotations(final BioSeq seq, int start, int end, final OutputStream outstream) {
        if (seq.getResiduesProvider() == null) {
            return false;
        }
        start = Math.max(0, start);
        end = Math.max(end, start);
        end = Math.min(end, start + seq.getResiduesProvider().getLength());
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new BufferedOutputStream(outstream));
            final long saveStart = start;
            final MutableLong sequenceLoop = new MutableLong();
            final ProgressUpdater progressUpdater = new ProgressUpdater("NibbleResiduesParser ", start, end, new PositionCalculator() {
                @Override
                public long getCurrentPosition() {
                    return saveStart + sequenceLoop.longValue();
                }
            });
            if (CThreadHolder.getInstance().getCurrentCThreadWorker() != null) {
                CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(progressUpdater);
            }
            long lastSleepTime = System.nanoTime();
            sequenceLoop.setValue(0L);
            while (sequenceLoop.longValue() < end - start && !Thread.currentThread().isInterrupted()) {
                final String outString = seq.getResidues((int)sequenceLoop.longValue(), Math.min((int)sequenceLoop.longValue() + NibbleResiduesParser.BUFSIZE, end - start));
                dos.writeBytes(outString);
                final long currentTime = System.nanoTime();
                if (currentTime - lastSleepTime >= 30000000000L) {
                    Thread.sleep(1L);
                    lastSleepTime = currentTime;
                }
                sequenceLoop.add((long)NibbleResiduesParser.BUFSIZE);
            }
            dos.flush();
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public static String getMimeType() {
        return "binary/bnib";
    }
    
    private static void writeFastaToFile(final File bnibFile) {
        InputStream in = null;
        FileOutputStream fos = null;
        try {
            in = new FileInputStream(bnibFile);
            final BioSeq seq = parse(in, new AnnotatedSeqGroup(bnibFile.toString()));
            final String name = bnibFile.getName().replace(".bnib", ".fasta");
            final File outFile = new File(bnibFile.getParentFile(), name);
            fos = new FileOutputStream(outFile);
            fos.write(62);
            fos.write(seq.getID().getBytes());
            fos.write(10);
            writeAnnotations(seq, 0, seq.getLength(), fos);
            fos.write(10);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(fos);
            GeneralUtils.safeClose(in);
        }
    }
    
    public static void main(final String[] args) {
        try {
            String infile_name = null;
            String outfile_name = null;
            String seq_version = null;
            String seq_name = null;
            if (args.length == 1) {
                writeFastaToFile(new File(args[0]));
                return;
            }
            if (args.length >= 4) {
                seq_name = args[0];
                infile_name = args[1];
                outfile_name = args[2];
                seq_version = args[3];
            }
            else {
                System.err.println("Usage: java -cp <exe_filename> com.affymetrix.genometryImpl.parsers.NibbleResiduesParser [seq_name] [in_file] [out_file] [seq_version]. Alternatively, provide just a xxx.bnib file to convert it to xxx.fasta");
                System.exit(1);
            }
            final File fil = new File(infile_name);
            final StringBuffer sb = new StringBuffer();
            final InputStream isr = GeneralUtils.getInputStream(fil, sb);
            final BioSeq seq = FastaParser.parse(isr, null, (int)fil.length());
            final int seqlength = seq.getResidues().length();
            System.out.println("length: " + seqlength);
            writeBinaryFile(outfile_name, seq_name, seq_version, seq.getResidues());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        final BioSeq aseq = parse(is, group);
        if (aseq != GenometryModel.getGenometryModel().getSelectedSeq()) {
            Logger.getLogger(NibbleResiduesParser.class.getName()).log(Level.WARNING, "This is not the currently-selected sequence.");
        }
        return null;
    }
    
    static {
        NibbleResiduesParser.BUFSIZE = 65536;
    }
}
