//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.operator;

import java.util.Map;
import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import com.affymetrix.genometryImpl.util.GraphSymUtils;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;

public abstract class AbstractGraphOperator implements Operator
{
    @Override
    public SeqSymmetry operate(final BioSeq aseq, final List<SeqSymmetry> symList) {
        final ArrayList<ArrayList<Integer>> xCoords = new ArrayList<ArrayList<Integer>>();
        final ArrayList<ArrayList<Integer>> wCoords = new ArrayList<ArrayList<Integer>>();
        final ArrayList<ArrayList<Float>> yCoords = new ArrayList<ArrayList<Float>>();
        boolean hasWidthGraphs = false;
        final int[] index = new int[symList.size()];
        final ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < symList.size(); ++i) {
            final GraphSym graph = (GraphSym) symList.get(i);
            index[i] = 0;
            final int[] xArray = graph.getGraphXCoords();
            final ArrayList<Integer> xCoordList = new ArrayList<Integer>();
            for (int j = 0; j < xArray.length; ++j) {
                xCoordList.add(xArray[j]);
            }
            xCoords.add(xCoordList);
            ArrayList<Integer> wCoordList = null;
            final int[] wArray = graph.getGraphWidthCoords();
            if (wArray != null) {
                hasWidthGraphs = true;
                wCoordList = new ArrayList<Integer>();
                for (int k = 0; k < wArray.length; ++k) {
                    wCoordList.add(wArray[k]);
                }
            }
            wCoords.add(wCoordList);
            final float[] yArray = graph.copyGraphYCoords();
            final ArrayList<Float> yCoordList = new ArrayList<Float>();
            for (int l = 0; l < yArray.length; ++l) {
                yCoordList.add(yArray[l]);
            }
            yCoords.add(yCoordList);
            labels.add(graph.getID());
        }
        final List<Integer> xList = new ArrayList<Integer>();
        final List<Integer> wList = new ArrayList<Integer>();
        final List<Float> yList = new ArrayList<Float>();
        int spanBeginX = Integer.MAX_VALUE;
        for (int m = 0; m < symList.size(); ++m) {
            spanBeginX = Math.min(spanBeginX, xCoords.get(m).get(0));
        }
        boolean lastWidth0 = false;
        for (int spanEndX = 0; spanBeginX < Integer.MAX_VALUE; spanBeginX = spanEndX) {
            spanEndX = Integer.MAX_VALUE;
            for (int i2 = 0; i2 < symList.size(); ++i2) {
                final int graphIndex = index[i2];
                if (graphIndex < xCoords.get(i2).size()) {
                    final int startX = xCoords.get(i2).get(graphIndex);
                    final int endX = startX + getWidth(wCoords.get(i2), graphIndex, hasWidthGraphs);
                    if (startX == endX && startX < spanEndX) {
                        spanEndX = startX;
                    }
                    else if (startX > spanBeginX && startX < spanEndX) {
                        spanEndX = startX;
                    }
                    else if (endX > spanBeginX && endX < spanEndX) {
                        spanEndX = endX;
                    }
                }
            }
            if (lastWidth0) {
                spanBeginX = spanEndX;
            }
            final List<Float> operands = new ArrayList<Float>();
            for (int i3 = 0; i3 < symList.size(); ++i3) {
                float value = 0.0f;
                final int graphIndex2 = index[i3];
                if (graphIndex2 < xCoords.get(i3).size()) {
                    final int startX2 = xCoords.get(i3).get(graphIndex2);
                    final int endX2 = startX2 + getWidth(wCoords.get(i3), graphIndex2, hasWidthGraphs);
                    if (spanBeginX >= startX2 && spanEndX <= endX2) {
                        value = yCoords.get(i3).get(graphIndex2);
                    }
                }
                operands.add(value);
            }
            final float currentY = this.operate(operands);
            xList.add(spanBeginX);
            wList.add(spanEndX - spanBeginX);
            yList.add(currentY);
            for (int i4 = 0; i4 < symList.size(); ++i4) {
                final int graphIndex2 = index[i4];
                if (graphIndex2 < xCoords.get(i4).size()) {
                    final int startX2 = xCoords.get(i4).get(graphIndex2);
                    final int endX2 = startX2 + getWidth(wCoords.get(i4), graphIndex2, hasWidthGraphs);
                    if (endX2 <= spanEndX) {
                        final int[] array = index;
                        final int n = i4;
                        ++array[n];
                    }
                }
            }
            lastWidth0 = (spanEndX == spanBeginX);
        }
        final String symbol = this.getSymbol();
        final String separator = (symbol == null) ? ", " : (" " + symbol + " ");
        final String newname = this.createName(aseq, symList, separator);
        int[] x = intListToArray(xList);
        int[] w = intListToArray(wList);
        float[] y = floatListToArray(yList);
        if (x.length == 0) {
            x = new int[] { xCoords.get(0).get(0) };
            y = new float[] { 0.0f };
            w = new int[] { 1 };
        }
        final GraphSym newsym = new GraphSym(x, w, y, newname, aseq);
        newsym.setGraphName(newname);

		newsym.getGraphState().setGraphStyle(((GraphSym) symList.get(0)).getGraphState().getGraphStyle());
		newsym.getGraphState().setHeatMap(((GraphSym) symList.get(0)).getGraphState().getHeatMap());

//        newsym.getGraphState().setGraphStyle(symList.get(0).getGraphState().getGraphStyle());
//        newsym.getGraphState().setHeatMap(symList.get(0).getGraphState().getHeatMap());
        return newsym;
    }

    protected String createName(final BioSeq aseq, final List<SeqSymmetry> symList, final String separator) {
        String newname = this.getDisplay().toLowerCase() + ": " + ((symList.size() == 2) ? ("(" + symList.get(0).getID() + ")" + separator + "(" + symList.get(1).getID() + ")") : ("(..." + symList.size() + ")"));
        newname = GraphSymUtils.getUniqueGraphID(newname, aseq);
        return newname;
    }

    protected abstract String getSymbol();

    protected abstract float operate(final List<Float> p0);

    private static int getWidth(final ArrayList<Integer> widths, final int index, final boolean hasWidthGraphs) {
        int width = 0;
        if (widths == null) {
            if (hasWidthGraphs) {
                width = 1;
            }
        }
        else {
            width = widths.get(index);
        }
        return width;
    }

    public static int[] intListToArray(final List<Integer> list) {
        final int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static float[] floatListToArray(final List<Float> list) {
        final float[] array = new float[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            array[i] = list.get(i);
        }
        return array;
    }

    @Override
    public boolean supportsTwoTrack() {
        return false;
    }

    @Override
    public FileTypeCategory getOutputCategory() {
        return FileTypeCategory.Graph;
    }

    @Override
    public int getOperandCountMin(final FileTypeCategory category) {
        return (category == FileTypeCategory.Graph) ? 2 : 0;
    }

    @Override
    public int getOperandCountMax(final FileTypeCategory category) {
        return (category == FileTypeCategory.Graph) ? Integer.MAX_VALUE : 0;
    }

    @Override
    public Map<String, Class<?>> getParameters() {
        return null;
    }

    @Override
    public boolean setParameters(final Map<String, Object> parms) {
        return false;
    }

    public static boolean isGraphOperator(final Operator operator) {
        for (final FileTypeCategory category : FileTypeCategory.values()) {
            if (category == FileTypeCategory.Graph) {
                if (operator.getOperandCountMin(category) < 2) {
                    return false;
                }
            }
            else if (operator.getOperandCountMax(category) > 0) {
                return false;
            }
        }
        return true;
    }
}
