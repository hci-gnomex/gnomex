//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.util;

import java.util.Iterator;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.util.List;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Collection;
import java.util.Comparator;
import java.util.Collections;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.io.File;
import java.util.regex.Pattern;

public class SortTabFile
{
    private static final Pattern line_regex;
    private static final Pattern tab_regex;

    public static boolean sort(final File file) {
        BufferedReader br = null;
        String line = null;
        final List<String> list = new ArrayList<String>(1000);
        final List<String> templist = new ArrayList<String>(1000);
        final String unzippedStreamName = GeneralUtils.stripEndings(file.getName());
        final String extension = ParserController.getExtension(unzippedStreamName);
        final Comparator<String> comparator = new LineComparator(extension);
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while ((line = br.readLine()) != null) {
                if (line.startsWith("track")) {
                    Collections.sort(templist, comparator);
                    list.addAll(templist);
                    templist.clear();
                }
                templist.add(line);
            }
            Collections.sort(templist, comparator);
            list.addAll(templist);
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(SortTabFile.class.getName()).log(Level.SEVERE, "Could not find file " + file, ex);
            return false;
        }
        catch (IOException ex2) {
            Logger.getLogger(SortTabFile.class.getName()).log(Level.SEVERE, null, ex2);
            return false;
        }
        finally {
            GeneralUtils.safeClose(br);
        }
        return writeFile(file, list);
    }

    private static boolean writeFile(final File file, final List<String> lines) {
        BufferedWriter bw = null;
        try {
            if (!file.canWrite()) {
                Logger.getLogger(SortTabFile.class.getName()).log(Level.SEVERE, "Cannot write to file {0}", file);
                return false;
            }
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            for (final String line : lines) {
                bw.write(line + "\n");
            }
            bw.flush();
            return true;
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(SortTabFile.class.getName()).log(Level.SEVERE, "Could not find file " + file, ex);
            return false;
        }
        catch (IOException ex2) {
            Logger.getLogger(SortTabFile.class.getName()).log(Level.SEVERE, null, ex2);
            return false;
        }
        finally {
            GeneralUtils.safeClose(bw);
            return true;
        }
//        return true;
    }

    static {
        line_regex = Pattern.compile("\\s+");
        tab_regex = Pattern.compile("\\t");
    }

    private static final class LineComparator implements Comparator<String>
    {
        private final int column;
        private final int or_column;
        private final String ext;
        private final Pattern regex;

        public LineComparator(final String ext) {
            final int[] columns = determineColumns(ext);
            this.column = columns[0] - 1;
            this.or_column = columns[1] - 1;
            this.ext = ext;
            this.regex = determineRegex(ext);
        }

        @Override
        public int compare(final String o1, final String o2) {
            if (o1.startsWith("track") || o2.startsWith("track")) {
                return 0;
            }
            final int[] mins = this.minimum(o1, o2);
            return Integer.valueOf(mins[0]).compareTo(Integer.valueOf(mins[1]));
        }

        private int[] minimum(final String o1, final String o2) {
            final int[] mins = new int[2];
            int col = this.column;
            int or_col = this.or_column;
            String[] o1Fields = this.regex.split(o1);
            if (o1Fields.length == 1) {
                o1Fields = SortTabFile.line_regex.split(o1);
            }
            String[] o2Fields = this.regex.split(o2);
            if (o2Fields.length == 1) {
                o2Fields = SortTabFile.line_regex.split(o2);
            }
            if (this.ext.endsWith(".bed")) {
                final boolean includes_bin_field = o1Fields.length > 6 && (o1Fields[6].startsWith("+") || o1Fields[6].startsWith("-") || o1Fields[6].startsWith("."));
                if (includes_bin_field) {
                    ++col;
                    ++or_col;
                }
            }
            mins[0] = Integer.valueOf(o1Fields[col]);
            mins[1] = Integer.valueOf(o2Fields[col]);
            if (or_col > 0) {
                mins[0] = Math.min(mins[0], Integer.valueOf(o1Fields[or_col]));
                mins[1] = Math.min(mins[1], Integer.valueOf(o2Fields[or_col]));
            }
            return mins;
        }

        private static Pattern determineRegex(final String ext) {
            if (ext.equals(".psl") || ext.endsWith(".link.psl")) {
                return SortTabFile.tab_regex;
            }
            if (ext.equals(".bed")) {
                return SortTabFile.tab_regex;
            }
            return null;
        }

        private static int[] determineColumns(final String ext) {
            if (ext.equals(".psl") || ext.endsWith(".link.psl")) {
                return new int[] { 16, -1 };
            }
            if (ext.equals(".bed")) {
                return new int[] { 2, 3 };
            }
            return new int[] { -1, -1 };
        }
    }
}
