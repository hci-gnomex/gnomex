// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import cern.colt.list.IntArrayList;
import java.io.RandomAccessFile;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import com.affymetrix.genometryImpl.ResiduesChars;
import java.util.HashMap;
import java.util.Map;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.BioSeq;
import java.io.File;

public class MisMatchGraphSym extends GraphSym
{
    int[][] residuesTot;
    private File helperIndex;
    
    public MisMatchGraphSym(final int[] x, final int[] w, final float[] y, final int[] a, final int[] t, final int[] g, final int[] c, final int[] n, final String id, final BioSeq seq) {
        super(x, w, y, id, seq);
        this.residuesTot = null;
        if (a != null && t != null && g != null && c != null && n != null) {
            this.setAllResidues(a, t, g, c, n);
        }
    }
    
    protected File index(final String graphName, final int[] a, final int[] t, final int[] g, final int[] c, final int[] n) {
        this.residuesTot = new int[5][100000];
        System.arraycopy(a, 0, this.residuesTot[0], 0, Math.min(100000, this.getPointCount()));
        System.arraycopy(t, 0, this.residuesTot[1], 0, Math.min(100000, this.getPointCount()));
        System.arraycopy(g, 0, this.residuesTot[2], 0, Math.min(100000, this.getPointCount()));
        System.arraycopy(c, 0, this.residuesTot[3], 0, Math.min(100000, this.getPointCount()));
        System.arraycopy(n, 0, this.residuesTot[4], 0, Math.min(100000, this.getPointCount()));
        if (this.getPointCount() <= 100000) {
            return null;
        }
        return createIndexedFile(graphName, a, t, g, c, n);
    }
    
    @Override
    public Map<String, Object> getLocationProperties(final int x, final SeqSpan span) {
        float y = this.getYCoordFromX(x);
        if (y < 0.0f) {
            return super.getLocationProperties(x, span);
        }
        final int leftBound = this.determineBegIndex(x);
        if (span.getMax() - span.getMin() > 100000 || leftBound < 0) {
            return super.getLocationProperties(x, span);
        }
        final Map<String, Object> locprops = new HashMap<String, Object>();
        locprops.put("x coord", x);
        float ytot = 0.0f;
        for (int i = 0; i < this.residuesTot.length; ++i) {
            y = this.residuesTot[i][leftBound - this.getBufStart()];
            locprops.put(String.valueOf(ResiduesChars.getCharFor(i)), y);
            ytot += y;
        }
        locprops.put("y total", ytot);
        return locprops;
    }
    
    void setAllResidues(final int[] a, final int[] t, final int[] g, final int[] c, final int[] n) {
        if (a.length != t.length || t.length != g.length || g.length != c.length || c.length != n.length) {
            throw new IllegalArgumentException("All arrays should have same length.");
        }
        this.helperIndex = this.index(this.getID() + "helper", a, t, g, c, n);
    }
    
    public int[][] getAllResidues() {
        return this.copyAllResidues();
    }
    
    public synchronized int[][] copyAllResidues() {
        final int[][] tempCoords = new int[this.residuesTot.length][this.getPointCount()];
        for (int i = 0; i < this.getPointCount(); ++i) {
            final float[] temp = this.getAllResiduesY(i);
            for (int j = 0; j < temp.length; ++j) {
                tempCoords[j][i] = (int)temp[j];
            }
        }
        return tempCoords;
    }
    
    public final float[] getAllResiduesY(final int i) {
        final float[] ret = new float[this.residuesTot.length];
        if (i >= this.getPointCount()) {
            Arrays.fill(ret, 0.0f);
            return ret;
        }
        if (i < this.getBufStart() || i >= this.getBufStart() + 100000) {
            this.readIntoBuffers(i);
        }
        for (int j = 0; j < this.residuesTot.length; ++j) {
            ret[j] = this.residuesTot[j][i - this.getBufStart()];
        }
        return ret;
    }
    
    private static File createIndexedFile(String graphName, final int[] a, final int[] t, final int[] g, final int[] c, final int[] n) {
        File bufVal = null;
        DataOutputStream dos = null;
        try {
            if (graphName.length() < 3) {
                graphName += "___";
            }
            bufVal = File.createTempFile(URLEncoder.encode(graphName, "UTF-8"), "idx");
            bufVal.deleteOnExit();
            dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(bufVal)));
            for (int i = 0; i < a.length; ++i) {
                dos.writeInt(a[i]);
                dos.writeInt(t[i]);
                dos.writeInt(g[i]);
                dos.writeInt(c[i]);
                dos.writeInt(n[i]);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(dos);
        }
        return bufVal;
    }
    
    public static File createEmptyIndexFile(String graphName, final int pointCount, int start) {
        File bufVal = null;
        DataOutputStream dos = null;
        try {
            if (graphName.length() < 3) {
                graphName += "___";
            }
            bufVal = File.createTempFile(URLEncoder.encode(graphName, "UTF-8"), "idx");
            bufVal.deleteOnExit();
            dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(bufVal)));
            for (int i = 0; i < pointCount; ++i) {
                dos.writeInt(start++);
                dos.writeInt(0);
                dos.writeInt(0);
                dos.writeInt(0);
                dos.writeInt(0);
                dos.writeInt(0);
                dos.writeInt(0);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(dos);
        }
        return bufVal;
    }
    
    static float[] updateY(final File index, final int offset, final int end, final int[] tempy, final int[][] yR) {
        RandomAccessFile raf = null;
        float ymin = Float.POSITIVE_INFINITY;
        float ymax = Float.NEGATIVE_INFINITY;
        try {
            raf = new RandomAccessFile(index, "rw");
            final int bytesToSkip = offset * 7 * 4;
            raf.seek(bytesToSkip);
            final int[] newy = new int[yR.length];
            for (int len = (offset + tempy.length > end) ? (end - offset) : tempy.length, i = 0; i < len; ++i) {
                raf.readInt();
                final long pos = raf.getFilePointer();
                final int y = raf.readInt() + tempy[i];
                if (y < ymin) {
                    ymin = y;
                }
                if (y > ymax) {
                    ymax = y;
                }
                for (int j = 0; j < yR.length; ++j) {
                    newy[j] = raf.readInt() + yR[j][i];
                }
                raf.seek(pos);
                raf.writeInt(y);
                for (int j = 0; j < yR.length; ++j) {
                    raf.writeInt(newy[j]);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(raf);
        }
        return new float[] { ymin, ymax };
    }
    
    static int[] getXCoords(final File index, final File finalIndex, final File finalHelper, final int len) {
        DataOutputStream dos = null;
        DataInputStream dis = null;
        DataOutputStream hdos = null;
        final IntArrayList xpos = new IntArrayList(len);
        final int[] yR = new int[5];
        try {
            dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(finalIndex)));
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(index)));
            hdos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(finalHelper)));
            for (int i = 0; i < len; ++i) {
                final int x = dis.readInt();
                final int y = dis.readInt();
                for (int j = 0; j < yR.length; ++j) {
                    yR[j] = dis.readInt();
                }
                if (yR[0] >= 0 || yR[1] >= 0 || yR[2] >= 0 || yR[3] >= 0 || yR[4] >= 0) {
                    xpos.add(x);
                    dos.writeInt(x);
                    dos.writeFloat(y);
                    dos.writeInt(1);
                    hdos.writeInt(x);
                    for (int j = 0; j < yR.length; ++j) {
                        hdos.writeInt(yR[j]);
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(dos);
            GeneralUtils.safeClose(dis);
            GeneralUtils.safeClose(hdos);
        }
        xpos.trimToSize();
        return xpos.elements();
    }
    
    static float getFirstY(final File index) {
        DataInputStream dis = null;
        float y = 0.0f;
        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(index)));
            dis.readInt();
            y = dis.readFloat();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(dis);
        }
        return y;
    }
    
    @Override
    protected synchronized void readIntoBuffers(final int start) {
        super.readIntoBuffers(start);
        if (this.helperIndex == null) {
            return;
        }
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(this.helperIndex)));
            final int bytesToSkip = start * 5 * 4;
            final int bytesSkipped = dis.skipBytes(bytesToSkip);
            if (bytesSkipped < bytesToSkip) {
                System.out.println("ERROR: skipped " + bytesSkipped + " out of " + bytesToSkip + " bytes when indexing");
                for (int i = 0; i < 5; ++i) {
                    Arrays.fill(this.residuesTot[i], 0);
                }
                return;
            }
            final int maxPoints = Math.min(100000, this.getPointCount() - start);
            for (int j = 0; j < maxPoints; ++j) {
                for (int k = 0; k < 5; ++k) {
                    this.residuesTot[k][j] = dis.readInt();
                }
            }
            for (int j = 0; j < 5; ++j) {
                Arrays.fill(this.residuesTot[j], maxPoints, 100000, 0);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            for (int l = 0; l < 5; ++l) {
                Arrays.fill(this.residuesTot[l], 0);
            }
        }
        finally {
            GeneralUtils.safeClose(dis);
        }
    }
    
    @Override
    public void clear() {
        super.clear();
        this.residuesTot = null;
    }
    
    @Override
    public FileTypeCategory getCategory() {
        return FileTypeCategory.Mismatch;
    }
}
