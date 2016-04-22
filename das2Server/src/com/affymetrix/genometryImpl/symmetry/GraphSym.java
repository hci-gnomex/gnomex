// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import java.util.HashMap;
import java.util.Map;
import com.affymetrix.genometryImpl.style.DefaultStateProvider;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import com.affymetrix.genometryImpl.util.IndexingUtils;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.style.GraphState;
import java.io.File;
import com.affymetrix.genometryImpl.BioSeq;

public class GraphSym extends RootSeqSymmetry
{
    public static final String PROP_GRAPH_STRAND = "Graph Strand";
    public static final Integer GRAPH_STRAND_PLUS;
    public static final Integer GRAPH_STRAND_MINUS;
    public static final Integer GRAPH_STRAND_BOTH;
    private int pointCount;
    private int xMin;
    private int xMax;
    private float yFirst;
    private boolean hasWidth;
    private boolean validData;
    private final BioSeq graph_original_seq;
    private String gid;
    public static final int BUFSIZE = 100000;
    private int bufStart;
    protected float[] yBuf;
    private int[] wBuf;
    private File bufFile;
    private int[] xCoords;
    private double xDelta;
    private float min_ycoord;
    private float max_ycoord;
    private GraphState gState;
    private boolean id_locked;
    
    public GraphSym(final File f, final int[] x, final float yFirst, final float yMin, final float yMax, final String id, final BioSeq seq) {
        this.pointCount = 0;
        this.xMin = 0;
        this.xMax = 0;
        this.yFirst = 0.0f;
        this.hasWidth = false;
        this.validData = true;
        this.bufStart = 0;
        this.xDelta = 0.0;
        this.min_ycoord = Float.POSITIVE_INFINITY;
        this.max_ycoord = Float.NEGATIVE_INFINITY;
        this.gState = null;
        this.id_locked = false;
        this.gid = id;
        this.graph_original_seq = seq;
        this.hasWidth = true;
        this.xMin = x[0];
        this.xMax = x[x.length - 1];
        this.yFirst = yFirst;
        this.pointCount = x.length;
        this.min_ycoord = yMin;
        this.max_ycoord = yMax;
        this.bufFile = f;
        this.xCoords = new int[this.pointCount];
        this.validData = true;
        this.yBuf = new float[100000];
        this.wBuf = new int[100000];
        System.arraycopy(x, 0, this.xCoords, 0, this.pointCount);
        final SeqSpan span = new SimpleSeqSpan(this.xMin, this.xMax, seq);
        this.addSpan(span);
    }
    
    public GraphSym(final int[] x, final float[] y, final String id, final BioSeq seq) {
        this(x, null, y, id, seq);
    }
    
    public GraphSym(final int[] x, final int[] w, final float[] y, final String id, final BioSeq seq) {
        this.pointCount = 0;
        this.xMin = 0;
        this.xMax = 0;
        this.yFirst = 0.0f;
        this.hasWidth = false;
        this.validData = true;
        this.bufStart = 0;
        this.xDelta = 0.0;
        this.min_ycoord = Float.POSITIVE_INFINITY;
        this.max_ycoord = Float.NEGATIVE_INFINITY;
        this.gState = null;
        this.id_locked = false;
        this.gid = id;
        this.graph_original_seq = seq;
        this.hasWidth = (w != null);
        if (x == null || x.length == 0) {
            this.xMax = seq.getLength();
        }
        else {
            this.setCoords(x, y, w);
        }
        final SeqSpan span = new SimpleSeqSpan(this.xMin, this.xMax, seq);
        this.addSpan(span);
    }
    
    public final void lockID() {
        this.id_locked = true;
    }
    
    public final void setGraphName(final String name) {
        this.getGraphState().getTierStyle().setTrackName(name);
        this.setProperty("name", name);
    }
    
    public final String getGraphName() {
        String gname = this.getGraphState().getTierStyle().getTrackName();
        if (gname == null) {
            gname = this.getID();
        }
        return gname;
    }
    
    @Override
    public String getID() {
        return this.gid;
    }
    
    @Override
    public void setID(final String id) {
        if (this.id_locked) {
            Logger.getLogger(GraphSym.class.getName()).log(Level.WARNING, "called GraphSym.setID() while id was locked:  {0} -> {1}", new Object[] { this.getID(), id });
        }
        else {
            this.gid = id;
        }
    }
    
    protected final synchronized void setCoords(final int[] x, final float[] y, final int[] w) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("X-coords and y-coords must have the same length.");
        }
        if (w != null && x.length != w.length) {
            throw new IllegalArgumentException("X,W, and Y arrays must have the same length");
        }
        this.xMin = x[0];
        this.yFirst = y[0];
        this.pointCount = x.length;
        this.xMax = x[this.pointCount - 1];
        if (w != null) {
            this.xMax += w[this.pointCount - 1];
        }
        this.hasWidth = (w != null);
        this.setVisibleYRange(y);
        this.bufFile = this.index(x, y, w);
    }
    
    private synchronized void setVisibleYRange(final float[] y) {
        this.min_ycoord = Float.POSITIVE_INFINITY;
        this.max_ycoord = Float.NEGATIVE_INFINITY;
        for (final float f : y) {
            if (f < this.min_ycoord) {
                this.min_ycoord = f;
            }
            if (f > this.max_ycoord) {
                this.max_ycoord = f;
            }
        }
    }
    
    protected final synchronized void nullCoords() {
        this.yBuf = null;
        this.wBuf = null;
        if (this.bufFile != null && this.bufFile.exists()) {
            try {
                this.bufFile.delete();
            }
            catch (Exception ex) {}
        }
    }
    
    public final int getPointCount() {
        return this.pointCount;
    }
    
    public final synchronized int[] getGraphXCoords() {
        final int[] tempCoords = new int[this.pointCount];
        for (int i = 0; i < this.pointCount; ++i) {
            tempCoords[i] = this.getGraphXCoord(i);
        }
        return tempCoords;
    }
    
    public int getGraphXCoord(final int i) {
        if (i >= this.pointCount) {
            return 0;
        }
        return (int)(this.xCoords[i] + this.xDelta);
    }
    
    public final int getMinXCoord() {
        return (int)(this.xMin + this.xDelta);
    }
    
    public final int getMaxXCoord() {
        return (int)(this.xMax + this.xDelta);
    }
    
    public final void moveX(final double delta) {
        this.xDelta += delta;
    }
    
    public final float getFirstYCoord() {
        return this.yFirst;
    }
    
    public final String getGraphYCoordString(final int i) {
        return Float.toString(this.getGraphYCoord(i));
    }
    
    public float getGraphYCoord(final int i) {
        if (i >= this.pointCount) {
            return 0.0f;
        }
        if (i == 0) {
            return this.getFirstYCoord();
        }
        if (i < this.bufStart || i >= this.bufStart + 100000) {
            this.readIntoBuffers(i);
        }
        return this.yBuf[i - this.bufStart];
    }
    
    public final float[] getGraphYCoords() {
        return this.copyGraphYCoords();
    }
    
    public synchronized float[] copyGraphYCoords() {
        final float[] tempCoords = new float[this.pointCount];
        for (int i = 0; i < this.pointCount; ++i) {
            tempCoords[i] = this.getGraphYCoord(i);
        }
        return tempCoords;
    }
    
    public synchronized float[] normalizeGraphYCoords() {
        return this.copyGraphYCoords();
    }
    
    public float[] getVisibleYRange() {
        final float[] result = { this.min_ycoord, this.max_ycoord };
        return result;
    }
    
    public final synchronized int[] getGraphWidthCoords() {
        if (!this.hasWidth) {
            return null;
        }
        final int[] tempCoords = new int[this.pointCount];
        for (int i = 0; i < this.pointCount; ++i) {
            tempCoords[i] = this.getGraphWidthCoord(i);
        }
        return tempCoords;
    }
    
    public int getGraphWidthCoord(final int i) {
        if (!this.hasWidth) {
            return 0;
        }
        if (i >= this.pointCount) {
            return 0;
        }
        if (i < this.bufStart || i >= this.bufStart + 100000) {
            this.readIntoBuffers(i);
        }
        if (this.wBuf == null) {
            return 0;
        }
        return this.wBuf[i - this.bufStart];
    }
    
    public boolean hasWidth() {
        return this.hasWidth;
    }
    
    public boolean isValid() {
        return this.validData;
    }
    
    public final int determineBegIndex(final double xmin) {
        final int index = Arrays.binarySearch(this.xCoords, (int)Math.floor(xmin));
        if (index >= 0) {
            return index;
        }
        return Math.max(0, -index - 2);
    }
    
    public final int determineEndIndex(final double xmax, final int prevIndex) {
        int index = Arrays.binarySearch(this.xCoords, (int)Math.ceil(xmax));
        if (index >= 0) {
            return index;
        }
        index = -index - 1;
        index = Math.min(index, this.pointCount - 1);
        index = Math.max(0, index);
        return index;
    }
    
    protected float getYCoordFromX(final int x) {
        final int leftBound = this.determineBegIndex(x);
        if (this.getGraphXCoord(leftBound) == x || (this.hasWidth && this.getGraphXCoord(leftBound) + this.getGraphWidthCoord(leftBound) >= x)) {
            return this.getGraphYCoord(leftBound);
        }
        return -1.0f;
    }
    
    private File index(final int[] x, final float[] y, final int[] w) {
        if (this.pointCount == 0) {
            return null;
        }
        this.xCoords = new int[this.pointCount];
        this.validData = true;
        System.arraycopy(x, 0, this.xCoords, 0, this.pointCount);
        System.arraycopy(y, 0, this.yBuf = new float[100000], 0, Math.min(100000, this.pointCount));
        if (this.hasWidth) {
            System.arraycopy(w, 0, this.wBuf = new int[100000], 0, Math.min(100000, this.pointCount));
        }
        if (this.pointCount <= 100000) {
            return null;
        }
        return IndexingUtils.createIndexedFile(this.pointCount, x, y, w);
    }
    
    protected synchronized void readIntoBuffers(final int start) {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(this.bufFile)));
            this.bufStart = start;
            final int bytesToSkip = start * 3 * 4;
            final int bytesSkipped = dis.skipBytes(bytesToSkip);
            if (bytesSkipped < bytesToSkip) {
                System.out.println("ERROR: skipped " + bytesSkipped + " out of " + bytesToSkip + " bytes when indexing");
                Arrays.fill(this.yBuf, 0.0f);
                if (this.hasWidth) {
                    Arrays.fill(this.wBuf, 0);
                }
                return;
            }
            final int maxPoints = Math.min(100000, this.pointCount - start);
            for (int i = 0; i < maxPoints; ++i) {
                dis.readInt();
                this.yBuf[i] = dis.readFloat();
                final int w = dis.readInt();
                if (this.hasWidth) {
                    this.wBuf[i] = w;
                }
            }
            Arrays.fill(this.yBuf, maxPoints, 100000, 0.0f);
            if (this.hasWidth) {
                Arrays.fill(this.wBuf, maxPoints, 100000, 0);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            Arrays.fill(this.yBuf, 0.0f);
            if (this.hasWidth) {
                Arrays.fill(this.wBuf, 0);
            }
        }
        finally {
            GeneralUtils.safeClose(dis);
        }
    }
    
    protected int getBufStart() {
        return this.bufStart;
    }
    
    public final BioSeq getGraphSeq() {
        return this.graph_original_seq;
    }
    
    public final GraphState getGraphState() {
        if (this.gState != null) {
            return this.gState;
        }
        return DefaultStateProvider.getGlobalStateProvider().getGraphState(this.gid);
    }
    
    public final void setGraphState(final GraphState gState) {
        this.gState = gState;
    }
    
    @Override
    public Object getProperty(final String key) {
        if (key.equals("id") || key.equals("method")) {
            return this.getID();
        }
        return super.getProperty(key);
    }
    
    @Override
    public boolean setProperty(final String name, final Object val) {
        if (name.equals("id") && val != null) {
            this.setID(val.toString());
            return false;
        }
        return super.setProperty(name, val);
    }
    
    public Map<String, Object> getLocationProperties(final int x, final SeqSpan span) {
        final Map<String, Object> locprops = new HashMap<String, Object>();
        locprops.put("x coord", x);
        if (this.isValid()) {
            final float y = this.getYCoordFromX(x);
            if (y < 0.0f) {
                locprops.put("y coord", "no point");
            }
            else {
                locprops.put("y coord", y);
            }
        }
        return locprops;
    }
    
    @Override
    public void clear() {
        super.clear();
        this.validData = false;
        this.yBuf = new float[0];
        this.wBuf = null;
        this.xCoords = new int[0];
        this.bufFile = null;
        this.pointCount = 0;
    }
    
    public boolean isSpecialGraph() {
        return false;
    }
    
    @Override
    public FileTypeCategory getCategory() {
        return FileTypeCategory.Graph;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        if (!this.isValid()) {
            return result;
        }
        result = 31 * result + ((this.gid == null) ? 0 : this.gid.hashCode());
        result = 31 * result + ((this.graph_original_seq == null) ? 0 : this.graph_original_seq.hashCode());
        result = 31 * result + (this.hasWidth ? 1231 : 1237);
        result = 31 * result + Float.floatToIntBits(this.max_ycoord);
        result = 31 * result + Float.floatToIntBits(this.min_ycoord);
        result = 31 * result + this.pointCount;
        result = 31 * result + Arrays.hashCode(this.wBuf);
        result = 31 * result + Arrays.hashCode(this.xCoords);
        final long temp = Double.doubleToLongBits(this.xDelta);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        result = 31 * result + this.xMax;
        result = 31 * result + this.xMin;
        result = 31 * result + Float.floatToIntBits(this.yFirst);
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final GraphSym other = (GraphSym)obj;
        if (!this.isValid() || !other.isValid()) {
            return false;
        }
        if (this.gid == null) {
            if (other.gid != null) {
                return false;
            }
        }
        else if (!this.gid.equals(other.gid)) {
            return false;
        }
        if (this.graph_original_seq == null) {
            if (other.graph_original_seq != null) {
                return false;
            }
        }
        else if (!this.graph_original_seq.equals(other.graph_original_seq)) {
            return false;
        }
        return this.hasWidth == other.hasWidth && Float.floatToIntBits(this.max_ycoord) == Float.floatToIntBits(other.max_ycoord) && Float.floatToIntBits(this.min_ycoord) == Float.floatToIntBits(other.min_ycoord) && this.pointCount == other.pointCount && Arrays.equals(this.wBuf, other.wBuf) && Arrays.equals(this.xCoords, other.xCoords) && Double.doubleToLongBits(this.xDelta) == Double.doubleToLongBits(other.xDelta) && this.xMax == other.xMax && this.xMin == other.xMin && Float.floatToIntBits(this.yFirst) == Float.floatToIntBits(other.yFirst);
    }
    
    static {
        GRAPH_STRAND_PLUS = 1;
        GRAPH_STRAND_MINUS = -1;
        GRAPH_STRAND_BOTH = 2;
    }
}
