//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.event;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Set;
import java.util.Iterator;
import javax.swing.KeyStroke;
import com.affymetrix.genometryImpl.util.PreferenceUtils;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class GenericActionHolder
{
    private static GenericActionHolder instance;
    private final List<GenericActionListener> listeners;
    private Map<String, GenericAction> genericActions;

    private GenericActionHolder() {
        this.listeners = new ArrayList<GenericActionListener>();
        this.genericActions = new HashMap<String, GenericAction>();
    }

    public static GenericActionHolder getInstance() {
        return GenericActionHolder.instance;
    }

    public void addGenericAction(final GenericAction genericAction) {
        synchronized (this.genericActions) {
            this.genericActions.put(genericAction.getId(), genericAction);
        }
        final KeyStroke k = PreferenceUtils.getAccelerator(genericAction.getClass().getName());
        genericAction.putValue("AcceleratorKey", k);
        for (final GenericActionListener listener : this.listeners) {
            listener.onCreateGenericAction(genericAction);
        }
    }

    public void addGenericActionSilently(final GenericAction genericAction) {
        synchronized (this.genericActions) {
            this.genericActions.put(genericAction.getId(), genericAction);
        }
        final KeyStroke k = PreferenceUtils.getAccelerator(genericAction.getClass().getName());
        genericAction.putValue("AcceleratorKey", k);
    }

    public void removeGenericAction(final GenericAction genericAction) {
        synchronized (this.genericActions) {
            this.genericActions.remove(genericAction.getId());
        }
    }

    public GenericAction getGenericAction(final String name) {
        synchronized (this.genericActions) {
            return this.genericActions.get(name);
        }
    }

    public Set<String> getGenericActionIds() {
        synchronized (this.genericActions) {
            return new CopyOnWriteArraySet<String>(this.genericActions.keySet());
        }
    }

    public Set<GenericAction> getGenericActions() {
        synchronized (this.genericActions) {
            return new CopyOnWriteArraySet<GenericAction>(this.genericActions.values());
        }
    }

    public void addGenericActionListener(final GenericActionListener listener) {
        this.listeners.add(listener);
        for (final GenericAction genericAction : this.getGenericActions()) {
            listener.onCreateGenericAction(genericAction);
        }
    }

    public void removeGenericActionListener(final GenericActionListener listener) {
        this.listeners.remove(listener);
    }

    public void notifyActionPerformed(final GenericAction action) {
        for (final GenericActionListener listener : new CopyOnWriteArrayList<GenericActionListener>(this.listeners)) {
            listener.notifyGenericAction(action);
        }
    }

    static {
        GenericActionHolder.instance = new GenericActionHolder();
    }
}
