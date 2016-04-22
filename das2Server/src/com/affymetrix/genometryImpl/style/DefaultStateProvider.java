// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.style;

import java.util.HashMap;
import java.util.Map;

public class DefaultStateProvider implements StateProvider
{
    private static final Map<String, ITrackStyleExtended> id2annotState;
    private static final Map<String, GraphState> id2graphState;
    private static StateProvider globalStateProvider;
    
    public static StateProvider getGlobalStateProvider() {
        return DefaultStateProvider.globalStateProvider;
    }
    
    public static void setGlobalStateProvider(final StateProvider sp) {
        DefaultStateProvider.globalStateProvider = sp;
    }
    
    @Override
    public ITrackStyleExtended getAnnotStyle(final String name) {
        ITrackStyleExtended style = DefaultStateProvider.id2annotState.get(name.toLowerCase());
        if (style == null) {
            style = new SimpleTrackStyle(name, false);
            DefaultStateProvider.id2annotState.put(name.toLowerCase(), style);
        }
        return style;
    }
    
    @Override
    public ITrackStyleExtended getAnnotStyle(final String name, final String human_name, final String file_type, final Map<String, String> props) {
        return this.getAnnotStyle(name);
    }
    
    @Override
    public GraphState getGraphState(final String id) {
        return this.getGraphState(id, null, null, null);
    }
    
    @Override
    public GraphState getGraphState(final String id, final String human_name, final String extension, final Map<String, String> props) {
        GraphState state = DefaultStateProvider.id2graphState.get(id);
        if (state == null) {
            state = new GraphState(id, human_name, extension, props);
            DefaultStateProvider.id2graphState.put(id, state);
        }
        return state;
    }
    
    static {
        id2annotState = new HashMap<String, ITrackStyleExtended>();
        id2graphState = new HashMap<String, GraphState>();
        DefaultStateProvider.globalStateProvider = new DefaultStateProvider();
    }
}
