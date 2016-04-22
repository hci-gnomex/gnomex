// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.symmetry.TypeContainerAnnot;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.Set;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import java.util.regex.Pattern;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;

public final class SearchUtils
{
    public static List<SeqSymmetry> findLocalSyms(final AnnotatedSeqGroup group, final BioSeq chrFilter, final Pattern regex, final boolean search_props) {
        Set<SeqSymmetry> syms = null;
        if (search_props) {
            syms = new HashSet<SeqSymmetry>(group.findInSymProp(regex));
        }
        else {
            syms = new HashSet<SeqSymmetry>(group.findSyms(regex));
        }
        List<BioSeq> chrs;
        if (chrFilter != null) {
            chrs = new ArrayList<BioSeq>();
            chrs.add(chrFilter);
        }
        else {
            chrs = group.getSeqList();
        }
        final Matcher match = regex.matcher("");
        SymWithProps sym = null;
        final Thread current_thread = Thread.currentThread();
        for (final BioSeq chr : chrs) {
            if (current_thread.isInterrupted()) {
                break;
            }
            for (int annotCount = chr.getAnnotationCount(), i = 0; i < annotCount; ++i) {
                sym = (SymWithProps)chr.getAnnotation(i);
                findIDsInSym(syms, sym, match);
                if (current_thread.isInterrupted()) {
                    break;
                }
            }
        }
        return new ArrayList<SeqSymmetry>(syms);
    }
    
    private static void findIDsInSym(final Set<SeqSymmetry> syms, final SeqSymmetry sym, final Matcher match) {
        if (sym == null) {
            return;
        }
        if (!(sym instanceof TypeContainerAnnot)) {
            if (sym.getID() != null && match.reset(sym.getID()).matches()) {
                syms.add(sym);
                return;
            }
            if (sym instanceof SymWithProps) {
                final String method = BioSeq.determineMethod(sym);
                if (method != null && match.reset(method).matches()) {
                    syms.add(sym);
                    return;
                }
            }
        }
        final int childCount = sym.getChildCount();
        final Thread current_thread = Thread.currentThread();
        for (int i = 0; i < childCount && !current_thread.isInterrupted(); ++i) {
            findIDsInSym(syms, sym.getChild(i), match);
        }
    }
    
    public static Set<SeqSymmetry> findNameInGenome(final String name, final AnnotatedSeqGroup genome) {
        final boolean glob_start = name.startsWith("*");
        final boolean glob_end = name.endsWith("*");
        Set<SeqSymmetry> result = null;
        Pattern name_pattern = null;
        String name_regex = name;
        if (glob_start || glob_end) {
            if (glob_start) {
                name_regex = ".*" + name_regex.substring(1);
            }
            if (glob_end) {
                name_regex = name_regex.substring(0, name_regex.length() - 1) + ".*";
            }
        }
        else {
            name_regex = "^" + name.toLowerCase() + "$";
        }
        Logger.getLogger(SearchUtils.class.getName()).log(Level.INFO, "name arg: {0},  regex to use for pattern-matching: {1}", new Object[] { name, name_regex });
        name_pattern = Pattern.compile(name_regex, 2);
        result = genome.findSyms(name_pattern);
        Logger.getLogger(SearchUtils.class.getName()).log(Level.INFO, "non-indexed regex matches: {0}", result.size());
        final Set<SeqSymmetry> result2 = IndexingUtils.findSymsByName(genome, name_pattern);
        Logger.getLogger(SearchUtils.class.getName()).log(Level.INFO, "indexed regex matches: {0}", result2.size());
        result.addAll(result2);
        return result;
    }
}
