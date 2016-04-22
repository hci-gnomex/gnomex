//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.general;

import com.affymetrix.genometryImpl.GenometryConstants;
import com.affymetrix.genometryImpl.das2.FormatPriorities;
import java.net.URI;
import com.affymetrix.genometryImpl.util.SeqUtils;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.Iterator;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.style.ITrackStyleExtended;
import com.affymetrix.genometryImpl.style.DefaultStateProvider;
import java.util.Collections;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.das2.Das2Type;
import com.affymetrix.genometryImpl.symmetry.SimpleMutableSeqSymmetry;
import java.util.HashSet;
import com.affymetrix.genometryImpl.symmetry.MutableSeqSymmetry;
import java.util.List;
import java.util.Set;
import com.affymetrix.genometryImpl.symloader.SymLoader;
import java.net.URL;
import com.affymetrix.genometryImpl.util.LoadUtils;
import java.util.Map;

public final class GenericFeature
{
    public static final String howtoloadmsg;
    public static final String show_how_to_load;
    public static final boolean default_show_how_to_load = true;
    private static final String WHOLE_GENOME = "Whole Sequence";
    private static final String AUTOLOAD;
    public final String featureName;
    public final Map<String, String> featureProps;
    public final GenericVersion gVersion;
    private boolean visible;
    private LoadUtils.LoadStrategy loadStrategy;
    public URL friendlyURL;
    private LoadUtils.RefreshStatus lastRefresh;
    public final Object typeObj;
    public final SymLoader symL;
    private final Set<String> methods;
    private static final List<LoadUtils.LoadStrategy> standardLoadChoices;
    private final MutableSeqSymmetry requestSym;
    private final MutableSeqSymmetry currentRequestSym;

    public GenericFeature(final String featureName, final Map<String, String> featureProps, final GenericVersion gVersion, final SymLoader gsr, final Object typeObj, final boolean autoload) {
        this.friendlyURL = null;
        this.methods = new HashSet<String>();
        this.requestSym = new SimpleMutableSeqSymmetry();
        this.currentRequestSym = new SimpleMutableSeqSymmetry();
        this.featureName = featureName;
        this.featureProps = featureProps;
        this.gVersion = gVersion;
        this.symL = gsr;
        this.typeObj = typeObj;
        if (typeObj instanceof Das2Type) {
            ((Das2Type)typeObj).setFeature(this);
        }
        this.setFriendlyURL();
        this.setAutoload(autoload);
        this.lastRefresh = LoadUtils.RefreshStatus.NOT_REFRESHED;
    }

    public boolean setAutoload(final boolean auto) {
        if (shouldAutoLoad(this.featureProps, "Whole Sequence") && auto) {
            this.setLoadStrategy(LoadUtils.LoadStrategy.GENOME);
            this.setVisible();
            return true;
        }
        if (!this.visible) {
            this.setLoadStrategy(LoadUtils.LoadStrategy.NO_LOAD);
        }
        return false;
    }

    public void setVisible() {
        this.visible = true;
        if (this.loadStrategy != LoadUtils.LoadStrategy.NO_LOAD) {
            return;
        }
        if (this.gVersion != null && this.gVersion.gServer != null) {
            if (this.gVersion.gServer.serverType.loadStrategyVisibleOnly()) {
                this.setLoadStrategy(LoadUtils.LoadStrategy.VISIBLE);
            }
            else if (this.symL != null) {
                if (this.symL.getLoadChoices().contains(LoadUtils.LoadStrategy.VISIBLE)) {
                    this.setLoadStrategy(LoadUtils.LoadStrategy.VISIBLE);
                }
                else {
                    this.setLoadStrategy(LoadUtils.LoadStrategy.GENOME);
                }
            }
        }
    }

    private void setInvisible() {
        this.visible = false;
        this.setLoadStrategy(LoadUtils.LoadStrategy.NO_LOAD);
    }

    public boolean isVisible() {
        return this.visible;
    }

    public LoadUtils.LoadStrategy getLoadStrategy() {
        return this.loadStrategy;
    }

    public void setLoadStrategy(final LoadUtils.LoadStrategy loadStrategy) {
        this.loadStrategy = loadStrategy;
    }

    public boolean setPreferredLoadStrategy(final LoadUtils.LoadStrategy loadStrategy) {
        if (this.getLoadChoices().contains(loadStrategy)) {
            this.setLoadStrategy(loadStrategy);
            return true;
        }
        this.setLoadStrategy(this.getLoadChoices().get(1));
        Logger.getLogger(GenericFeature.class.getName()).log(Level.WARNING, "Given {0} strategy is not permitted instead using {1} strategy.", new Object[] { loadStrategy, this.getLoadStrategy() });
        return false;
    }

    private static boolean shouldAutoLoad(final Map<String, String> featureProps, final String loadStrategy) {
        return featureProps != null && featureProps.containsKey("load_hint") && featureProps.get("load_hint").equals(loadStrategy);
    }

    private void setFriendlyURL() {
        if (this.featureProps == null || !this.featureProps.containsKey("url") || this.featureProps.get("url").length() == 0) {
            return;
        }
        try {
            this.friendlyURL = new URL(this.featureProps.get("url"));
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(GenericFeature.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String description() {
        if (this.featureProps != null) {
            final String summary = this.featureProps.get("summary");
            final String descrip = this.featureProps.get("description");
            if (summary != null && summary.length() > 0) {
                return summary;
            }
            if (descrip != null && descrip.length() > 0) {
                if (descrip.length() > 100) {
                    return descrip.substring(0, 100) + "...";
                }
                return descrip;
            }
        }
        return this.featureName;
    }

    public Set<String> getMethods() {
        return Collections.unmodifiableSet((Set<? extends String>)this.methods);
    }

    public void addMethod(final String method) {
        this.methods.add(method);
        final ITrackStyleExtended style = DefaultStateProvider.getGlobalStateProvider().getAnnotStyle(method, method, this.getExtension(), this.featureProps);
        style.setFeature(this);
    }

    public void setLastRefreshStatus(final LoadUtils.RefreshStatus status) {
        this.lastRefresh = status;
    }

    public LoadUtils.RefreshStatus getLastRefreshStatus() {
        return this.lastRefresh;
    }

    public void clear(final BioSeq seq) {
        final List<SeqSymmetry> removeList = new ArrayList<SeqSymmetry>();
        for (int i = 0; i < this.requestSym.getChildCount(); ++i) {
            final SeqSymmetry sym = this.requestSym.getChild(i);
            if (sym.getSpan(seq) != null) {
                removeList.add(sym);
            }
        }
        final Iterator i$ = removeList.iterator();
        while (i$.hasNext()) {
            final SeqSymmetry sym = (SeqSymmetry) i$.next();
            this.requestSym.removeChild(sym);
        }
        removeList.clear();
    }

    public void clear() {
        this.requestSym.removeChildren();
        if (this.currentRequestSym.getChildCount() > 0) {
            Logger.getLogger(GenericFeature.class.getName()).log(Level.WARNING, "Genericfeature contains current request sym for server {0}", this.gVersion.gServer.serverType);
            this.currentRequestSym.removeChildren();
        }
        this.methods.clear();
        if (this.symL != null) {
            this.symL.clear();
        }
        this.setInvisible();
        this.setLastRefreshStatus(LoadUtils.RefreshStatus.NOT_REFRESHED);
    }

    public boolean isLoaded(final SeqSpan span) {
        final MutableSeqSymmetry query_sym = new SimpleMutableSeqSymmetry();
        query_sym.addSpan(span);
        final SeqSymmetry optimized_sym = SeqUtils.exclusive(query_sym, this.requestSym, span.getBioSeq());
        return !SeqUtils.hasSpan(optimized_sym);
    }

    public SeqSymmetry optimizeRequest(final SeqSpan span) {
        final MutableSeqSymmetry query_sym = new SimpleMutableSeqSymmetry();
        query_sym.addSpan(span);
        SeqSymmetry optimized_sym = SeqUtils.exclusive(query_sym, this.requestSym, span.getBioSeq());
        optimized_sym = SeqUtils.exclusive(optimized_sym, this.currentRequestSym, span.getBioSeq());
        if (SeqUtils.hasSpan(optimized_sym)) {
            return optimized_sym;
        }
        return null;
    }

    public void addLoadedSpanRequest(final SeqSpan span) {
        final MutableSeqSymmetry query_sym = new SimpleMutableSeqSymmetry();
        query_sym.addSpan(span);
        this.requestSym.addChild(query_sym);
        this.removeCurrentRequest(span);
    }

    public final void removeCurrentRequest(final SeqSpan span) {
        for (int i = 0; i < this.currentRequestSym.getChildCount(); ++i) {
            final SeqSymmetry sym = this.currentRequestSym.getChild(i);
            if (span == sym.getSpan(span.getBioSeq())) {
                this.currentRequestSym.removeChild(sym);
            }
        }
    }

    public void addLoadingSpanRequest(final SeqSpan span) {
        final MutableSeqSymmetry query_sym = new SimpleMutableSeqSymmetry();
        query_sym.addSpan(span);
        this.currentRequestSym.addChild(query_sym);
    }

    public MutableSeqSymmetry getRequestSym() {
        return this.requestSym;
    }

    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        if (this.symL != null) {
            return this.symL.getLoadChoices();
        }
        return GenericFeature.standardLoadChoices;
    }

    @Override
    public String toString() {
        if (!this.featureName.contains("/")) {
            return this.featureName;
        }
        final int lastSlash = this.featureName.lastIndexOf("/");
        return this.featureName.substring(lastSlash + 1, this.featureName.length());
    }

    public URI getURI() {
        if (this.typeObj instanceof Das2Type) {
            return ((Das2Type)this.typeObj).getURI();
        }
        if (this.typeObj instanceof String) {
            return URI.create(this.typeObj.toString());
        }
        if (this.symL != null) {
            return this.symL.uri;
        }
        return null;
    }

    public String getExtension() {
        if (this.typeObj instanceof Das2Type) {
            String ext = FormatPriorities.getFormat((Das2Type)this.typeObj);
            if (ext == null) {
                ext = "";
            }
            return ext;
        }
        if (this.symL != null) {
            return this.symL.extension;
        }
        return "";
    }

    static {
        howtoloadmsg = GenometryConstants.BUNDLE.getString("howtoloadmessage");
        show_how_to_load = GenometryConstants.BUNDLE.getString("show_how_to_load");
        AUTOLOAD = LoadUtils.LoadStrategy.AUTOLOAD.name();
        (standardLoadChoices = new ArrayList<LoadUtils.LoadStrategy>()).add(LoadUtils.LoadStrategy.NO_LOAD);
        GenericFeature.standardLoadChoices.add(LoadUtils.LoadStrategy.VISIBLE);
    }
}
