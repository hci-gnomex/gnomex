//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.util;

import java.util.Collections;
import java.util.TreeSet;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import java.awt.Color;
import com.affymetrix.common.CommonUtils;
import javax.swing.KeyStroke;
import java.util.Collection;
import java.net.URLConnection;
import java.net.URL;
import java.util.prefs.InvalidPreferencesFormatException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.prefs.BackingStoreException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.Window;
import java.util.prefs.Preferences;
import java.util.SortedSet;
import javax.swing.JFileChooser;

public abstract class PreferenceUtils
{
    public static final String PREFS_MAIN = "/com/affymetrix";
    public static final String MENU_NODE_NAME = "main_menu";
    public static final String ASK_BEFORE_EXITING = "Ask before exiting";
    public static final String CONFIRM_BEFORE_DELETE = "Confirm before delete";
    public static final String CONFIRM_BEFORE_CLEAR = "Confirm before clear";
    public static final String CONFIRM_BEFORE_LOAD = "Confirm before load";
    public static final String CONFIRM_BEFORE_REFRESH = "Confirm before refresh";
    public static final String AUTO_LOAD = "Auto Load Data";
    public static final String AUTO_LOAD_SEQUENCE = "Auto Load Sequence";
    public static final String COVERAGE_SUMMARY_HEATMAP = "Coverage Summary as HeatMap";
    public static final String DISPLAY_ERRORS_STATUS_BAR = "Display Errors on Status Bar";
    public static final String SHOW_COLLAPSE_OPTION = "Show Collapse Option";
    public static final boolean default_display_errors = false;
    public static final boolean default_ask_before_exiting = true;
    public static final boolean default_confirm_before_delete = true;
    public static final boolean default_confirm_before_clear = true;
    public static final boolean default_confirm_before_load = true;
    public static final boolean default_confirm_before_refresh = true;
    public static final boolean default_auto_load = true;
    public static final boolean default_auto_load_sequence = false;
    public static final boolean default_coverage_summary_heatmap = true;
    private static final String DEFAULT_PREFS_MODE = "igb";
    private static final String SLASH_STANDIN = "%";
    private static String prefs_mode;
    public static final String PREFS_THRESHOLD = "Threshold Value";
    public static final String PREFS_AUTOLOAD = "Enable Auto load";
    private static JFileChooser static_chooser;
    private static final SortedSet<String> keystroke_node_names;

    public static Preferences getTopNode() {
        return Preferences.userRoot().node("/com/affymetrix/" + PreferenceUtils.prefs_mode);
    }

    public static Preferences getAltNode(final String name) {
        return Preferences.userRoot().node("/com/affymetrix/" + name);
    }

    public static Preferences getKeystrokesNode() {
        return getTopNode().node("keystrokes");
    }

    public static Preferences getToolbarNode() {
        return getTopNode().node("toolbar");
    }

    public static Preferences getLocationsNode() {
        return getTopNode().node("locations");
    }

    public static Preferences getGenomesNode() {
        return getTopNode().node("genomes");
    }

    public static Preferences getServersNode() {
        return getTopNode().node("servers");
    }

    public static Preferences getRepositoriesNode() {
        return getTopNode().node("repositories");
    }

    public static Preferences getGraphPrefsNode() {
        return getTopNode().node("graphs");
    }

    public static Preferences getWindowPrefsNode() {
        return getTopNode().node("window");
    }

    public static Preferences getSessionPrefsNode() {
        return getTopNode().node("session");
    }

    public static Preferences getExportPrefsNode() {
        return getTopNode().node("export");
    }

    public static Preferences getCertificatePrefsNode() {
        return getTopNode().node("certificate");
    }

    public static void saveIntParam(final String param_name, final int param) {
        try {
            getTopNode().putInt(param_name, param);
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static int getIntParam(final String param_name, final int def) {
        return getTopNode().getInt(param_name, def);
    }

    public static boolean getBooleanParam(final String param_name, final boolean def) {
        return getTopNode().getBoolean(param_name, def);
    }

    public static void saveWindowLocation(final Window w, final String name) {
        final Rectangle r = w.getBounds();
        try {
            final Preferences p = getWindowPrefsNode();
            p.putInt(name + " x", r.x);
            p.putInt(name + " y", r.y);
            p.putInt(name + " width", r.width);
            p.putInt(name + " height", r.height);
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static Rectangle retrieveWindowLocation(final String name, final Rectangle def) {
        final Rectangle r = new Rectangle();
        final Preferences p = getWindowPrefsNode();
        r.x = p.getInt(name + " x", def.x);
        r.y = p.getInt(name + " y", def.y);
        r.width = p.getInt(name + " width", def.width);
        r.height = p.getInt(name + " height", def.height);
        return r;
    }

    public static void setWindowSize(final Window w, final Rectangle r) {
        final Toolkit kit = Toolkit.getDefaultToolkit();
        final Dimension dim = kit.getScreenSize();
        if (r.x < 0 || r.x > dim.width) {
            r.x = 0;
        }
        if (r.y < 0 || r.y > dim.height) {
            r.y = 0;
        }
        if (r.width < 50) {
            r.width = 50;
        }
        if (r.height < 50) {
            r.height = 50;
        }
        r.width = Math.min(r.width, (int)(dim.width * 0.99));
        r.height = Math.min(r.height, (int)(dim.height * 0.99));
        w.setBounds(r);
        if (w instanceof Frame) {
            ((Frame)w).setState(0);
        }
    }

    public static void saveComponentState(final String name, final String state) {
        if (state == null) {
            getWindowPrefsNode().remove(name + " state");
        }
        else {
            getWindowPrefsNode().put(name + " state", state);
        }
    }

    public static String getComponentState(final String name) {
        return getWindowPrefsNode().get(name + " state", null);
    }

    public static void saveSelectedTab(final String tray, final String tab) {
        if (tab == null) {
            getWindowPrefsNode().remove(tray + " selected");
        }
        else {
            getWindowPrefsNode().put(tray + " selected", tab);
        }
    }

    public static String getSelectedTab(final String tray) {
        return getWindowPrefsNode().get(tray + " selected", null);
    }

    public static void saveDividerLocation(final String tray, final double dividerProportionalLocation) {
        getWindowPrefsNode().put(tray + " dvdrloc", String.valueOf(dividerProportionalLocation));
    }

    public static double getDividerLocation(final String tray) {
        final String locString = getWindowPrefsNode().get(tray + " dvdrloc", null);
        double dividerLocation = -1.0;
        try {
            dividerLocation = Double.parseDouble(locString);
        }
        catch (Exception ex) {}
        return dividerLocation;
    }

    public static JFileChooser getJFileChooser() {
        if (PreferenceUtils.static_chooser == null) {
            PreferenceUtils.static_chooser = new UniFileChooser("XML File", "xml");
        }
        PreferenceUtils.static_chooser.setFileSelectionMode(0);
        PreferenceUtils.static_chooser.rescanCurrentDirectory();
        return PreferenceUtils.static_chooser;
    }

    public static void exportPreferences(final Preferences prefs, final File f) throws IOException, BackingStoreException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            prefs.exportSubtree(fos);
        }
        finally {
            GeneralUtils.safeClose(fos);
        }
    }

    public static void importPreferences(final File f) throws IOException, InvalidPreferencesFormatException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            Preferences.importPreferences(fis);
        }
        finally {
            GeneralUtils.safeClose(fis);
        }
    }

    public static void importPreferences(final URL url) throws IOException, InvalidPreferencesFormatException {
        InputStream is = null;
        try {
            final URLConnection uc = url.openConnection();
            is = uc.getInputStream();
            Preferences.importPreferences(is);
        }
        finally {
            GeneralUtils.safeClose(is);
        }
    }

    public static void clearPreferences() throws BackingStoreException {
        getTopNode().removeNode();
    }

    public static void clearAllPreferences() throws BackingStoreException {
        Preferences.userRoot().node("/com/affymetrix").removeNode();
    }

    public static void printPreferences() throws BackingStoreException, IOException {
        getTopNode().exportSubtree(System.out);
    }

    public static void printAllPreferences() throws BackingStoreException, IOException {
        Preferences.userRoot().node("/com/affymetrix").exportSubtree(System.out);
    }

    public static Collection<String> getKeystrokesNodeNames() {
        return PreferenceUtils.keystroke_node_names;
    }

    public static KeyStroke getAccelerator(final String action_command) {
        if (action_command == null) {
            return null;
        }
        final String str = getKeystrokesNode().get(action_command, "");
        final KeyStroke ks = KeyStroke.getKeyStroke(str);
        PreferenceUtils.keystroke_node_names.add(action_command);
        if (ks == null) {
            if (!"".equals(str)) {
                System.out.println("Bad format accelerator set for '" + action_command + "':");
                System.out.println("  invalid '" + str + "'");
            }
            getKeystrokesNode().put(action_command, "");
        }
        return ks;
    }

    public static String getAppDataDirectory() {
        return CommonUtils.getInstance().getAppDataDirectory();
    }

    public static void putColor(final Preferences node, final String key, final Color c) {
        node.put(key, "0x" + getColorString(c));
    }

    public static String getColorString(final Color c) {
        final int i = c.getRGB() & 0xFFFFFF;
        String s;
        for (s = Integer.toHexString(i).toUpperCase(); s.length() < 6; s = "0" + s) {}
        return s;
    }

    public static Color getColor(final Preferences node, final String key, final Color default_color) {
        Color result = default_color;
        final String value = node.get(key, "unknown");
        if (!value.equals("unknown")) {
            try {
                result = Color.decode(value);
            }
            catch (Exception e) {
                System.out.println("Couldn't decode color preference for '" + key + "' from '" + value + "'");
            }
        }
        return result;
    }

    public static JCheckBox createCheckBox(final String title, final Preferences node, final String pref_name, final boolean default_val) {
        final JCheckBox check_box = new JCheckBox(title);
        check_box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ae) {
                node.putBoolean(pref_name, check_box.isSelected());
            }
        });
        check_box.setSelected(node.getBoolean(pref_name, default_val));
        node.addPreferenceChangeListener(new PreferenceChangeListener() {
            @Override
            public void preferenceChange(final PreferenceChangeEvent evt) {
                if (evt.getNode().equals(node) && evt.getKey().equals(pref_name)) {
                    check_box.setSelected(Boolean.valueOf(evt.getNewValue()));
                }
            }
        });
        return check_box;
    }

    public static JComboBox createComboBox(final Preferences node, final String pref_name, final String[] options, final String default_value) {
        final String[] interned_options = new String[options.length];
        for (int i = 0; i < options.length; ++i) {
            interned_options[i] = options[i].intern();
        }
        default_value.intern();
        final JComboBox combo_box = new JComboBox(interned_options);
        final String current_stored_value = node.get(pref_name, default_value).intern();
        combo_box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ae) {
                final String selection = (String)combo_box.getSelectedItem();
                if (selection != null) {
                    node.put(pref_name, selection);
                }
            }
        });
        combo_box.setSelectedItem(current_stored_value);
        node.addPreferenceChangeListener(new PreferenceChangeListener() {
            @Override
            public void preferenceChange(final PreferenceChangeEvent evt) {
                if (evt.getNode().equals(node) && evt.getKey().equals(pref_name) && !combo_box.getSelectedItem().equals(evt.getNewValue()) && evt.getNewValue() != null) {
                    combo_box.setSelectedItem(evt.getNewValue().intern());
                }
            }
        });
        return combo_box;
    }

    private static String shortNodeName(final String s, final boolean remove_slash) {
        String short_s;
        if (s.length() >= 80) {
            short_s = UrlToFileName.toMd5(s);
        }
        else {
            short_s = s;
        }
        if (remove_slash) {
            short_s = short_s.replaceAll("/", "%");
        }
        return short_s;
    }

    public static Preferences getSubnode(final Preferences parent, final String name) {
        return getSubnode(parent, name, false);
    }

    public static Preferences getSubnode(final Preferences parent, final String name, final boolean remove_slash) {
        final String short_name = shortNodeName(name, remove_slash);
        return parent.node(short_name);
    }

    public static JFrame createFrame(final String name, final JPanel panel) {
        if (name.length() > 70) {
            throw new IllegalArgumentException("Title of the frame must be less than 70 chars.");
        }
        final JFrame frame = new JFrame(name);
        frame.setName(name);
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(1);
        panel.setVisible(true);
        frame.pack();
        final Rectangle pos = retrieveWindowLocation(frame.getTitle(), frame.getBounds());
        if (pos != null) {
            setWindowSize(frame, pos);
        }
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent evt) {
                PreferenceUtils.saveWindowLocation(frame, frame.getTitle());
            }
        });
        return frame;
    }

    public static void setPrefsMode(final String mode) {
        PreferenceUtils.prefs_mode = mode;
    }

    static {
        PreferenceUtils.prefs_mode = "igb";
        PreferenceUtils.static_chooser = null;
        keystroke_node_names = Collections.synchronizedSortedSet(new TreeSet<String>());
    }
}
