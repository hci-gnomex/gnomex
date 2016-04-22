// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public final class SpeciesLookup
{
    private static final boolean DEFAULT_CS = true;
    private static final Pattern STANDARD_REGEX;
    private static final Pattern UCSC_REGEX;
    private static final Pattern NON_STANDARD_REGEX;
    private static final SpeciesSynonymsLookup speciesLookup;
    private static final SpeciesLookup singleton;
    
    public static SpeciesLookup getSpeciesLookup() {
        return SpeciesLookup.singleton;
    }
    
    public static void load(final InputStream genericSpecies) throws IOException {
        SpeciesLookup.speciesLookup.loadSynonyms(genericSpecies, true);
    }
    
    public static String getCommonSpeciesName(final String species) {
        return SpeciesLookup.speciesLookup.findSecondSynonym(species);
    }
    
    public static String getSpeciesName(final String version) {
        return getSpeciesName(version, true);
    }
    
    public static String getSpeciesName(final String version, final boolean cs) {
        String species = null;
        species = SpeciesLookup.speciesLookup.getPreferredName(version, cs);
        if (species.equals(version)) {
            species = getSpeciesName(version, SpeciesLookup.STANDARD_REGEX, cs);
        }
        if (species == null) {
            species = getSpeciesName(version, SpeciesLookup.NON_STANDARD_REGEX, cs);
        }
        if (species == null) {
            species = getSpeciesName(version, SpeciesLookup.UCSC_REGEX, cs);
        }
        if (species == null) {
            species = SpeciesLookup.speciesLookup.getPreferredName(version, cs);
        }
        Pattern pattern = Pattern.compile("(\\S+)(?>_(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)_\\d{4})");
        Matcher m = pattern.matcher(species);
        if (m.find()) {
            species = m.group(1);
        }
        else {
            pattern = Pattern.compile("^([a-zA-Z]{2,6})[\\d]+$");
            m = pattern.matcher(species);
            if (m.find()) {
                species = species.replaceAll("[\\d]+", "");
            }
        }
        pattern = Pattern.compile("([a-zA-Z]+)((_[a-zA-Z]+)+)");
        m = pattern.matcher(species);
        if (m.find()) {
            species = m.group(1).toUpperCase() + m.group(2).toLowerCase();
        }
        return species;
    }
    
    private static String getSpeciesName(final String version, final Pattern regex, final boolean cs) {
        final Matcher matcher = regex.matcher(version);
        String matched = null;
        if (matcher.matches()) {
            matched = matcher.group(1);
        }
        if (matched == null || matched.isEmpty()) {
            return null;
        }
        final String preferred = SpeciesLookup.speciesLookup.getPreferredName(matched, cs);
        if (matched.equals(preferred)) {
            return null;
        }
        return preferred;
    }
    
    public static boolean isSynonym(final String synonym1, final String synonym2) {
        return SpeciesLookup.speciesLookup.isSynonym(synonym1, synonym2);
    }
    
    public static String getStandardName(String version) {
        final Set<Pattern> patterns = new HashSet<Pattern>();
        patterns.add(SpeciesLookup.STANDARD_REGEX);
        patterns.add(SpeciesLookup.UCSC_REGEX);
        for (final Pattern pattern : patterns) {
            final Matcher matcher = pattern.matcher(version);
            if (matcher.matches()) {
                return formatSpeciesName(pattern, matcher.group(1));
            }
        }
        version = version.trim().replaceAll("\\s+", "_");
        return version;
    }
    
    private static String formatSpeciesName(final Pattern pattern, final String species) {
        if (SpeciesLookup.STANDARD_REGEX.equals(pattern)) {
            return species.substring(0, 1).toUpperCase() + species.substring(1, species.length()).toLowerCase();
        }
        return species.toLowerCase();
    }
    
    static {
        STANDARD_REGEX = Pattern.compile("^([a-zA-Z]+_[a-zA-Z]+).*$");
        UCSC_REGEX = Pattern.compile("^([a-zA-Z]{2,6})[\\d]+$");
        NON_STANDARD_REGEX = Pattern.compile("^([a-zA-Z]+_[a-zA-Z]+_[a-zA-Z]+).*$");
        speciesLookup = new SpeciesSynonymsLookup();
        singleton = new SpeciesLookup();
    }
}
