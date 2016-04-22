// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.servlets;

import java.io.DataOutputStream;
import javax.servlet.ServletOutputStream;
import com.affymetrix.genometryImpl.parsers.FastaParser;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import com.affymetrix.genometryImpl.parsers.TwoBitParser;
import java.io.Closeable;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.OutputStream;
import java.io.InputStream;
import com.affymetrix.genometryImpl.parsers.NibbleResiduesParser;
import java.io.FileInputStream;
import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.List;

final class ServletUtils
{
    static void retrieveRAW(final List<String> ranges, final SeqSpan span, final String sequence_directory, final String seqname, final HttpServletResponse response, final HttpServletRequest request) throws IOException {
        String file_name = sequence_directory + seqname + ".bnib";
        File seqfile = new File(file_name);
        FileInputStream fis = null;
        if (seqfile.exists()) {
            response.setContentType("text/raw");
            try {
                fis = new FileInputStream(seqfile);
                if (!ranges.isEmpty()) {
                    int spanStart = 0;
                    int spanEnd = 0;
                    spanStart = span.getStart();
                    spanEnd = span.getEnd();
                    NibbleResiduesParser.parse((InputStream)fis, spanStart, spanEnd, (OutputStream)response.getOutputStream());
                }
                else {
                    NibbleResiduesParser.parse((InputStream)fis, (OutputStream)response.getOutputStream());
                }
            }
            finally {
                GeneralUtils.safeClose((Closeable)fis);
            }
            return;
        }
        file_name = sequence_directory + seqname + ".2bit";
        seqfile = new File(file_name);
        if (seqfile.exists()) {
            response.setContentType("text/raw");
            if (!ranges.isEmpty()) {
                int spanStart = 0;
                int spanEnd = 0;
                spanStart = span.getStart();
                spanEnd = span.getEnd();
                TwoBitParser.parse(seqfile.toURI(), spanStart, spanEnd, (OutputStream)response.getOutputStream());
            }
            else {
                TwoBitParser.parse(seqfile.toURI(), (OutputStream)response.getOutputStream());
            }
            return;
        }
        final PrintWriter pw = response.getWriter();
        pw.println("File not found: " + file_name);
        pw.println("This DAS/2 server cannot currently handle request:    ");
        pw.println(request.getRequestURL().toString());
    }
    
    static void retrieveBNIB(final String sequence_directory, final String seqname, final HttpServletResponse response, final HttpServletRequest request) throws IOException {
        final String file_name = sequence_directory + seqname + ".bnib";
        final File seqfile = new File(file_name);
        if (seqfile.exists()) {
            response.setContentType(NibbleResiduesParser.getMimeType());
            BufferedInputStream in = null;
            BufferedOutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(seqfile));
                out = new BufferedOutputStream((OutputStream)response.getOutputStream());
                int c;
                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            }
            finally {
                GeneralUtils.safeClose((Closeable)in);
                GeneralUtils.safeClose((Closeable)out);
            }
            return;
        }
        final PrintWriter pw = response.getWriter();
        pw.println("File not found: " + file_name);
        pw.println("This DAS/2 server cannot currently handle request:    ");
        pw.println(request.getRequestURL().toString());
    }
    
    @Deprecated
    static void retrieveFASTA(final List<String> ranges, final SeqSpan span, final String sequence_directory, final String organism_name, final String seqname, final HttpServletResponse response, final HttpServletRequest request) throws IOException {
        final String file_name = sequence_directory + seqname + ".fa";
        final File seqfile = new File(file_name);
        if (!seqfile.exists()) {
            System.out.println("seq request mapping to nonexistent file: " + file_name);
            final PrintWriter pw = response.getWriter();
            pw.println("File not found: " + file_name);
            pw.println("This DAS/2 server cannot currently handle request:    ");
            pw.println(request.getRequestURL().toString());
            return;
        }
        int spanStart = 0;
        int spanEnd = 0;
        if (ranges.isEmpty()) {
            if (seqfile.length() > 2147483647L) {
                spanEnd = Integer.MAX_VALUE;
            }
            else {
                spanEnd = (int)seqfile.length();
            }
        }
        else {
            spanStart = span.getStart();
            spanEnd = span.getEnd();
        }
        response.setContentType(FastaParser.getMimeType());
        final byte[] buf = FastaParser.readFASTA(seqfile, spanStart, spanEnd);
        final byte[] header = FastaParser.generateNewHeader(seqname, organism_name, spanStart, spanEnd);
        OutputFormattedFasta(buf, header, response.getOutputStream());
    }
    
    private static void OutputFormattedFasta(final byte[] buf, final byte[] header, final ServletOutputStream sos) throws IOException, IllegalArgumentException {
        if (buf == null) {
            return;
        }
        final DataOutputStream dos = new DataOutputStream((OutputStream)sos);
        try {
            dos.write(header, 0, header.length);
            final byte[] newlineBuf = { 10 };
            final int lines = buf.length / 79;
            for (int i = 0; i < lines; ++i) {
                dos.write(buf, i * 79, 79);
                dos.write(newlineBuf, 0, 1);
            }
            if (buf.length % 79 > 0) {
                dos.write(buf, lines * 79, buf.length % 79);
                dos.write(newlineBuf, 0, 1);
            }
        }
        finally {
            GeneralUtils.safeClose((Closeable)dos);
        }
    }
}
