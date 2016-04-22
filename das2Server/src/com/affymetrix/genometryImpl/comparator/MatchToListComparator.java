// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.comparator;

import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.util.ArrayList;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.List;
import java.util.Comparator;

public final class MatchToListComparator implements Comparator<String>
{
    List<String> match_list;
    
    public MatchToListComparator(final String filename) {
        this.match_list = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(filename)));
            this.match_list = new ArrayList<String>();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() != 0) {
                    if (line.startsWith("#")) {
                        continue;
                    }
                    final String match_term = line.trim();
                    this.match_list.add(match_term);
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Error initializing MatchToListComparator: ");
            ex.printStackTrace();
            this.match_list = null;
        }
        finally {
            GeneralUtils.safeClose(br);
        }
    }
    
    @Override
    public int compare(final String name1, final String name2) {
        if (this.match_list == null) {
            return 0;
        }
        final int index1 = this.match_list.indexOf(name1);
        final int index2 = this.match_list.indexOf(name2);
        if (index1 == -1 && index2 == -1) {
            return 0;
        }
        if (index1 == -1) {
            return 1;
        }
        if (index2 == -1) {
            return -1;
        }
        return Integer.valueOf(index1).compareTo(Integer.valueOf(index2));
    }
}
