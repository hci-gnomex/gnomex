// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Arrays;
import java.util.Set;

public class SpeciesSynonymsLookup extends SynonymLookup
{
    @Override
    public synchronized void addSynonyms(final String[] syns) {
        Set<String> synonymList = null;
        final String common_name = syns[0];
        final Set<String> values = this.lookupHash.get(common_name);
        if (values != null) {
            synonymList = values;
            for (int i = 0; i < syns.length; ++i) {
                if (i != 1) {
                    synonymList.add(syns[i]);
                }
            }
        }
        else {
            synonymList = new LinkedHashSet<String>(Arrays.asList(syns));
        }
        for (String newSynonym : syns) {
            if (newSynonym != null) {
                newSynonym = newSynonym.trim();
                if (newSynonym.length() != 0) {
                    final Set<String> previousSynonymList = this.lookupHash.put(newSynonym, synonymList);
                    if (previousSynonymList != null) {
                        for (final String existingSynonym : previousSynonymList) {
                            if (synonymList.add(existingSynonym)) {
                                this.lookupHash.put(existingSynonym, synonymList);
                            }
                        }
                    }
                }
            }
        }
    }
}
