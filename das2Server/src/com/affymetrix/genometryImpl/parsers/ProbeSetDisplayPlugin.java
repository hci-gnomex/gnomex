// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import java.io.IOException;
import java.util.Iterator;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import com.affymetrix.genometryImpl.util.SeqUtils;
import java.util.Set;
import java.util.HashSet;
import java.io.OutputStream;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Collection;

public final class ProbeSetDisplayPlugin implements AnnotationWriter
{
    public static final String CONSENSUS_TYPE = "netaffx consensus";
    static final String PROBESET_TYPE = "netaffx probesets";
    static final String CROSSHYB_TYPE = "netaffx crosshyb";
    static final String POLY_A_STACKS_TYPE = "netaffx poly_a_stacks";
    static final String POLY_A_SITES_TYPE = "netaffx poly_a_sites";
    
    public static String getArrayNameFromRequestedType(final String requested_type) {
        String result = null;
        final int index = requested_type.indexOf(" netaffx consensus");
        if (index > 0) {
            result = requested_type.substring(0, index);
        }
        return result;
    }
    
    public static void collectAndWriteAnnotations(final Collection<? extends SeqSymmetry> consensus_syms, final boolean writeConsensus, final BioSeq genome_seq, final String array_name, final OutputStream outstream) {
        String array_name_prefix = "";
        if (array_name != null && array_name.trim().length() >= 0) {
            array_name_prefix = array_name + " ";
        }
        final String requested_type = array_name_prefix + "netaffx consensus";
        final String probeset_type = array_name_prefix + "netaffx probesets";
        final String poly_a_sites_type = array_name_prefix + "netaffx poly_a_sites";
        final String poly_a_stacks_type = array_name_prefix + "netaffx poly_a_stacks";
        final Set<SeqSymmetry> probesets = new HashSet<SeqSymmetry>();
        final Set<SeqSymmetry> crossHybProbes = new HashSet<SeqSymmetry>();
        final Set<SeqSymmetry> polyASites = new HashSet<SeqSymmetry>();
        final Set<SeqSymmetry> polyAStacks = new HashSet<SeqSymmetry>();
        findProbeSets(consensus_syms, genome_seq, probeset_type, probesets, crossHybProbes, poly_a_sites_type, polyASites, poly_a_stacks_type, polyAStacks);
        final PSLParser psl_parser = new PSLParser();
        if (writeConsensus) {
            writePSLTrack(psl_parser, consensus_syms, genome_seq, requested_type, "Consensus Sequences", outstream);
        }
        writePSLTrack(psl_parser, probesets, genome_seq, probeset_type, "Probe Sets", outstream);
        writePSLTrack(psl_parser, polyASites, genome_seq, poly_a_sites_type, "Poly-A Sites", outstream);
        writePSLTrack(psl_parser, polyAStacks, genome_seq, poly_a_stacks_type, "Poly-A Stacks", outstream);
        writePSLTrack(psl_parser, crossHybProbes, genome_seq, "netaffx crosshyb", "Cross-Hybridized Probes", outstream);
    }
    
    private static void findProbeSets(final Collection<? extends SeqSymmetry> consensus_syms, final BioSeq genome_seq, final String probeset_type, final Set<SeqSymmetry> probesets, final Set<SeqSymmetry> crossHybProbes, final String poly_a_sites_type, final Set<SeqSymmetry> polyASites, final String poly_a_stacks_type, final Set<SeqSymmetry> polyAStacks) {
        for (final SeqSymmetry current_c2g : consensus_syms) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            final BioSeq aseq = SeqUtils.getOtherSeq(current_c2g, genome_seq);
            if (aseq == null) {
                continue;
            }
            for (int maxm = aseq.getAnnotationCount(), m = 0; m < maxm; ++m) {
                final SeqSymmetry container = aseq.getAnnotation(m);
                for (int cindex = 0; cindex < container.getChildCount(); ++cindex) {
                    final SeqSymmetry cons_annot = container.getChild(cindex);
                    if (cons_annot instanceof SymWithProps) {
                        findProbeSet(cons_annot, probeset_type, probesets, crossHybProbes, poly_a_sites_type, polyASites, poly_a_stacks_type, polyAStacks);
                    }
                }
            }
        }
    }
    
    private static void findProbeSet(final SeqSymmetry cons_annot, final String probeset_type, final Set<SeqSymmetry> probesets, final Set<SeqSymmetry> crossHybProbes, final String poly_a_sites_type, final Set<SeqSymmetry> polyASites, final String poly_a_stacks_type, final Set<SeqSymmetry> polyAStacks) {
        Set<SeqSymmetry> probesetsFound = null;
        final SymWithProps cons_sym = (SymWithProps)cons_annot;
        final String type = (String)cons_sym.getProperty("method");
        if (type.endsWith("netaffx probesets")) {
            probesetsFound = probesets;
        }
        else if (type.endsWith("netaffx crosshyb")) {
            probesetsFound = crossHybProbes;
        }
        else if (type.endsWith("netaffx poly_a_sites")) {
            probesetsFound = polyASites;
        }
        else {
            if (!type.endsWith("netaffx poly_a_stacks")) {
                System.out.println("findProbeSet: ERROR: couldn't find type:---" + type + "---");
                System.out.println("types are:" + probeset_type + "," + "netaffx crosshyb" + "," + poly_a_sites_type + "," + poly_a_stacks_type + ",");
                return;
            }
            probesetsFound = polyAStacks;
        }
        for (int childCount = cons_sym.getChildCount(), n = 0; n < childCount; ++n) {
            final SeqSymmetry cons_child = cons_annot.getChild(n);
            probesetsFound.add(cons_child);
        }
    }
    
    public static void writePSLTrack(final PSLParser parser, final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final String type, final String description, final OutputStream outstream) {
        if (!syms.isEmpty()) {
            parser.writeAnnotations(syms, seq, true, type, description, outstream);
        }
    }
    
    @Override
    public String getMimeType() {
        return "text/plain";
    }
    
    @Override
    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final String type, final OutputStream outstream) throws IOException {
        final String array_name = getArrayNameFromRequestedType(type);
        System.out.println("in ProbesetDisplayPlugin.writeAnnotations(), array_name: " + array_name);
        if (array_name == null || seq == null) {
            return false;
        }
        collectAndWriteAnnotations(syms, true, seq, array_name, outstream);
        return true;
    }
}
