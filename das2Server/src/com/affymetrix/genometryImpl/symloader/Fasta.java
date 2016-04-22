// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symloader;

import java.io.IOException;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithResidues;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.OutputStream;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Collection;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.regex.Matcher;
import java.io.BufferedInputStream;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import java.util.regex.Pattern;
import com.affymetrix.genometryImpl.parsers.AnnotationWriter;

public class Fasta extends FastaCommon implements AnnotationWriter
{
    private static final Pattern header_regex;
    private static final int COLUMNS = 50;
    
    public Fasta() {
        this(null, null, null);
    }
    
    public Fasta(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        super(uri, "", group);
    }
    
    @Override
    protected boolean initChromosomes() throws Exception {
        BufferedInputStream bis = null;
        BufferedReader br = null;
        final Matcher matcher = Fasta.header_regex.matcher("");
        try {
            bis = LocalUrlCacher.convertURIToBufferedUnzippedStream(this.uri);
            br = new BufferedReader(new InputStreamReader(bis));
            String header = br.readLine();
            while (br.ready() && !Thread.currentThread().isInterrupted()) {
                if (header == null) {
                    continue;
                }
                matcher.reset(header);
                if (!matcher.matches()) {
                    continue;
                }
                final String seqid = matcher.group(1).split(" ")[0];
                BioSeq seq = this.group.getSeq(seqid);
                int count = 0;
                header = null;
                String line = null;
                while (br.ready() && !Thread.currentThread().isInterrupted()) {
                    line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.length() == 0) {
                        continue;
                    }
                    final char firstChar = line.charAt(0);
                    if (firstChar == ';') {
                        continue;
                    }
                    if (firstChar == '>') {
                        header = line;
                        break;
                    }
                    count += line.trim().length();
                }
                if (seq == null) {
                    seq = this.group.addSeq(seqid, count, this.uri.toString());
                }
                this.chrSet.add(seq);
            }
            return !Thread.currentThread().isInterrupted();
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(br);
            GeneralUtils.safeClose(bis);
        }
    }
    
    @Override
    public String getRegionResidues(final SeqSpan span) throws Exception {
        this.init();
        BufferedInputStream bis = null;
        BufferedReader br = null;
        int count = 0;
        String residues = "";
        final Matcher matcher = Fasta.header_regex.matcher("");
        try {
            bis = LocalUrlCacher.convertURIToBufferedUnzippedStream(this.uri);
            br = new BufferedReader(new InputStreamReader(bis), 52428800);
            String header = br.readLine();
            while (br.ready() && !Thread.currentThread().isInterrupted()) {
                if (header == null) {
                    break;
                }
                matcher.reset(header);
                if (!matcher.matches()) {
                    continue;
                }
                final String seqid = matcher.group(1).split(" ")[0];
                final BioSeq seq = this.group.getSeq(seqid);
                final boolean seqMatch = seq != null && seq.equals(span.getBioSeq());
                header = null;
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
                    final char firstChar = line.charAt(0);
                    if (firstChar == ';') {
                        continue;
                    }
                    if (firstChar == '>') {
                        header = line;
                        break;
                    }
                    if (!seqMatch) {
                        continue;
                    }
                    line = line.trim();
                    if (count + line.length() <= span.getMin()) {
                        count += line.length();
                    }
                    else {
                        if (count > span.getMax()) {
                            break;
                        }
                        if (count < span.getMin()) {
                            line = line.substring(span.getMin() - count);
                            count = span.getMin();
                        }
                        if (count + line.length() >= span.getMax()) {
                            line = line.substring(0, count + line.length() - span.getMax());
                        }
                        buf.append(line);
                    }
                }
                residues = new String(buf);
                buf.setLength(0);
                buf = null;
                residues = residues.trim();
                if (seqMatch) {
                    break;
                }
            }
            return residues;
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(br);
            GeneralUtils.safeClose(bis);
        }
    }
    
    @Override
    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final String type, final OutputStream outstream) throws IOException {
        if (syms == null || syms.size() != 1) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "bad symList in FastaAnnotationWriter");
            return false;
        }
        final SeqSymmetry sym = (SeqSymmetry)syms.iterator().next();
        final SimpleSymWithResidues residuesSym = (SimpleSymWithResidues)sym.getChild(0);
        final String residues = residuesSym.getResidues();
        outstream.write(62);
        outstream.write(seq.toString().getBytes());
        outstream.write(10);
        for (int pointer = 0; pointer < residues.length(); pointer += 50) {
            final int end = Math.min(pointer + 50, residues.length());
            outstream.write(residues.substring(pointer, end).getBytes());
            outstream.write(10);
        }
        return true;
    }
    
    @Override
    public String getMimeType() {
        return "text/fasta";
    }
    
    static {
        header_regex = Pattern.compile("^\\s*>\\s*(.+)");
    }
}
