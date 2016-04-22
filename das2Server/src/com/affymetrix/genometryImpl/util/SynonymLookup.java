// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Arrays;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class SynonymLookup
{
    private static final boolean DEFAULT_CS = true;
    private static final boolean DEFAULT_SR = false;
    private static final Pattern LINE_REGEX;
    private static final SynonymLookup DEFAULT_LOOKUP;
    private static final SynonymLookup CHROM_LOOKUP;
    protected final LinkedHashMap<String, Set<String>> lookupHash;
    private final Set<String> preferredNames;
    
    public SynonymLookup() {
        this.lookupHash = new LinkedHashMap<String, Set<String>>();
        this.preferredNames = new HashSet<String>();
    }
    
    public static SynonymLookup getDefaultLookup() {
        return SynonymLookup.DEFAULT_LOOKUP;
    }
    
    public static SynonymLookup getChromosomeLookup() {
        return SynonymLookup.CHROM_LOOKUP;
    }
    
    public void loadSynonyms(final InputStream istream) throws IOException {
        this.loadSynonyms(istream, false);
    }
    
    public void loadSynonyms(final InputStream istream, final boolean setPreferredNames) throws IOException {
        InputStreamReader ireader = null;
        BufferedReader br = null;
        try {
            ireader = new InputStreamReader(istream);
            br = new BufferedReader(ireader);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                final String[] fields = SynonymLookup.LINE_REGEX.split(line);
                if (fields.length < 2) {
                    continue;
                }
                if (setPreferredNames) {
                    this.preferredNames.add(fields[0]);
                }
                this.addSynonyms(fields);
            }
        }
        finally {
            GeneralUtils.safeClose(ireader);
            GeneralUtils.safeClose(br);
        }
    }
    
    public synchronized void addSynonyms(final String[] syns) {
        final Set<String> synonymList = new LinkedHashSet<String>(Arrays.asList(syns));
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
    
    public Set<String> getSynonyms() {
        return this.lookupHash.keySet();
    }
    
    public Set<String> getSynonyms(final String synonym) {
        return this.getSynonyms(synonym, true);
    }
    
    public Set<String> getSynonyms(final String synonym, final boolean cs) {
        if (synonym == null) {
            throw new IllegalArgumentException("str can not be null");
        }
        if (!cs) {
            final Set<String> synonyms = new LinkedHashSet<String>();
            for (final String key : this.lookupHash.keySet()) {
                if (key.equalsIgnoreCase(synonym)) {
                    synonyms.addAll(this.lookupHash.get(key));
                }
            }
            return Collections.unmodifiableSet((Set<? extends String>)synonyms);
        }
        if (this.lookupHash.containsKey(synonym)) {
            return Collections.unmodifiableSet((Set<? extends String>)this.lookupHash.get(synonym));
        }
        return Collections.emptySet();
    }
    
    public boolean isSynonym(final String synonym1, final String synonym2) {
        return this.isSynonym(synonym1, synonym2, true, false);
    }
    
    public boolean isSynonym(final String synonym1, final String synonym2, final boolean cs, final boolean sr) {
        if (synonym1 == null || synonym2 == null) {
            throw new IllegalArgumentException("synonyms can not be null");
        }
        final Collection<String> synonyms = this.getSynonyms(synonym1, cs);
        if (sr && hasRandom(synonym1, cs) && hasRandom(synonym2, cs)) {
            return this.isSynonym(stripRandom(synonym1), stripRandom(synonym2), cs, sr);
        }
        if (cs) {
            return synonyms.contains(synonym2);
        }
        for (final String curstr : synonyms) {
            if (synonym2.equalsIgnoreCase(curstr)) {
                return true;
            }
        }
        return false;
    }
    
    public String getPreferredName(final String synonym) {
        return this.getPreferredName(synonym, true);
    }
    
    public String getPreferredName(final String synonym, final boolean cs) {
        return this.findMatchingSynonym(this.preferredNames, synonym, cs, false);
    }
    
    public String findMatchingSynonym(final Collection<String> choices, final String synonym) {
        return this.findMatchingSynonym(choices, synonym, true, false);
    }
    
    public String findMatchingSynonym(final Collection<String> choices, final String synonym, final boolean cs, final boolean sr) {
        for (final String id : choices) {
            if (this.isSynonym(synonym, id, cs, sr)) {
                return id;
            }
        }
        return synonym;
    }
    
    public String findSecondSynonym(final String synonym) {
        final Set<String> synonymSet = this.lookupHash.get(synonym);
        if (synonymSet == null) {
            return synonym;
        }
        String firstSynonym = "";
        for (final String id : this.lookupHash.get(synonym)) {
            if (firstSynonym.length() != 0) {
                return id;
            }
            firstSynonym = id;
        }
        return firstSynonym;
    }
    
    private static boolean hasRandom(final String synonym, final boolean cs) {
        if (synonym == null) {
            throw new IllegalArgumentException("synonym can not be null");
        }
        return (!cs && synonym.toLowerCase().endsWith("_random")) || synonym.endsWith("_random");
    }
    
    private static String stripRandom(final String synonym) {
        if (!synonym.toLowerCase().endsWith("_random")) {
            throw new IllegalArgumentException("synonym must end with '_random'");
        }
        return synonym.substring(0, synonym.length() - 7);
    }
    
    static {
        LINE_REGEX = Pattern.compile("\\t+");
        DEFAULT_LOOKUP = new SynonymLookup();
        CHROM_LOOKUP = new SynonymLookup();
    }
}
