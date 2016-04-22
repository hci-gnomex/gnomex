// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.general;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.util.StringEncrypter;
import java.util.prefs.PreferenceChangeEvent;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import com.affymetrix.genometryImpl.util.PreferenceUtils;
import com.affymetrix.genometryImpl.util.LoadUtils;
import javax.swing.ImageIcon;
import java.net.URL;
import com.affymetrix.genometryImpl.util.ServerTypeI;
import java.util.prefs.Preferences;
import java.util.prefs.PreferenceChangeListener;

public final class GenericServer implements Comparable<GenericServer>, PreferenceChangeListener
{
    public final Preferences node;
    public String serverName;
    public String URL;
    public ServerTypeI serverType;
    private String login;
    private String password;
    private boolean enabled;
    public final Object serverObj;
    public final URL friendlyURL;
    private ImageIcon friendlyIcon;
    private boolean friendlyIconAttempted;
    private LoadUtils.ServerStatus serverStatus;
    private final boolean primary;
    private final boolean isDefault;
    
    public GenericServer(final String serverName, final String URL, final ServerTypeI serverType, final boolean enabled, final Object serverObj, final boolean primary, final boolean isDefault) {
        this(serverName, URL, serverType, enabled, false, (serverType == null) ? PreferenceUtils.getRepositoriesNode().node(getHash(URL)) : (serverType.isSaveServersInPrefs() ? PreferenceUtils.getServersNode().node(getHash(URL)) : null), serverObj, primary, isDefault);
    }
    
    public GenericServer(final String serverName, final String URL, final ServerTypeI serverType, final boolean enabled, final Object serverObj, final boolean isDefault) {
        this(serverName, URL, serverType, enabled, false, (serverType == null) ? PreferenceUtils.getRepositoriesNode().node(getHash(URL)) : PreferenceUtils.getServersNode().node(getHash(URL)), serverObj, false, isDefault);
    }
    
    public GenericServer(final Preferences node, final Object serverObj, final ServerTypeI serverType, final boolean isDefault) {
        this(node.get("name", "Unknown"), GeneralUtils.URLDecode(node.get("url", "")), serverType, true, false, node, serverObj, false, isDefault);
    }
    
    public static String getHash(final String str) {
        return Long.toString(str.hashCode() + 2147483647L);
    }
    
    private GenericServer(final String serverName, final String URL, final ServerTypeI serverType, final boolean enabled, final boolean referenceOnly, final Preferences node, final Object serverObj, final boolean primary, final boolean isDefault) {
        this.login = "";
        this.password = "";
        this.enabled = true;
        this.friendlyIcon = null;
        this.friendlyIconAttempted = false;
        this.serverStatus = LoadUtils.ServerStatus.NotInitialized;
        this.serverName = serverName;
        this.URL = URL;
        this.serverType = serverType;
        this.enabled = enabled;
        this.node = node;
        this.serverObj = serverObj;
        this.friendlyURL = determineFriendlyURL(URL, serverType);
        if (this.node != null) {
            this.setEnabled(this.node.getBoolean("enabled", enabled));
            this.setLogin(this.node.get("login", ""));
            this.setPassword(decrypt(this.node.get("password", "")));
            this.node.addPreferenceChangeListener(this);
        }
        this.primary = primary;
        this.isDefault = isDefault;
    }
    
    public ImageIcon getFriendlyIcon() {
        if (this.friendlyIcon == null && !this.friendlyIconAttempted && this.friendlyURL != null) {
            this.friendlyIconAttempted = true;
            this.friendlyIcon = GeneralUtils.determineFriendlyIcon(this.friendlyURL.toString() + "/favicon.ico");
        }
        return this.friendlyIcon;
    }
    
    private static URL determineFriendlyURL(final String URL, final ServerTypeI serverType) {
        if (URL == null) {
            return null;
        }
        String tempURL = URL;
        URL tempFriendlyURL = null;
        if (tempURL.endsWith("/")) {
            tempURL = tempURL.substring(0, tempURL.length() - 1);
        }
        if (serverType != null) {
            tempURL = serverType.adjustURL(tempURL);
        }
        try {
            tempFriendlyURL = new URL(tempURL);
        }
        catch (Exception ex) {}
        return tempFriendlyURL;
    }
    
    public void setServerStatus(final LoadUtils.ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }
    
    public LoadUtils.ServerStatus getServerStatus() {
        return this.serverStatus;
    }
    
    public void setEnabled(final boolean enabled) {
        if (this.node != null) {
            this.node.putBoolean("enabled", enabled);
        }
        this.enabled = enabled;
    }
    
    public void setName(final String name) {
        if (this.node != null) {
            this.node.put("name", name);
        }
        this.serverName = name;
    }
    
    public void enableForSession() {
        this.enabled = true;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setLogin(final String login) {
        if (this.node != null) {
            this.node.put("login", login);
        }
        this.login = login;
    }
    
    public String getLogin() {
        return this.login;
    }
    
    public void setEncryptedPassword(final String password) {
        if (this.node != null) {
            this.node.put("password", password);
        }
        this.password = decrypt(password);
    }
    
    public void setPassword(final String password) {
        if (this.node != null) {
            this.node.put("password", encrypt(password));
        }
        this.password = password;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public boolean isPrimary() {
        return this.primary;
    }
    
    public boolean isDefault() {
        return this.isDefault;
    }
    
    @Override
    public String toString() {
        return this.serverName;
    }
    
    @Override
    public int compareTo(final GenericServer gServer) {
        if (this.isEnabled() != gServer.isEnabled()) {
            return Boolean.valueOf(this.isEnabled()).compareTo(Boolean.valueOf(gServer.isEnabled()));
        }
        if (!this.serverName.equals(gServer.serverName)) {
            return this.serverName.compareTo(gServer.serverName);
        }
        return this.serverType.compareTo(gServer.serverType);
    }
    
    @Override
    public void preferenceChange(final PreferenceChangeEvent evt) {
        final String key = evt.getKey();
        if (!key.equals("name")) {
            if (!key.equals("type")) {
                if (key.equals("login")) {
                    this.login = ((evt.getNewValue() == null) ? "" : evt.getNewValue());
                }
                else if (key.equals("password")) {
                    this.password = ((evt.getNewValue() == null) ? "" : decrypt(evt.getNewValue()));
                }
                else if (key.equals("enabled")) {
                    this.enabled = (evt.getNewValue() == null || Boolean.valueOf(evt.getNewValue()));
                }
            }
        }
    }
    
    private static String decrypt(final String encrypted) {
        if (!encrypted.isEmpty()) {
            try {
                final StringEncrypter encrypter = new StringEncrypter("DESede");
                return encrypter.decrypt(encrypted);
            }
            catch (StringEncrypter.EncryptionException ex) {
                Logger.getLogger(GenericServer.class.getName()).log(Level.SEVERE, null, ex);
                throw new IllegalArgumentException(ex);
            }
        }
        return "";
    }
    
    private static String encrypt(final String password) {
        if (!password.isEmpty()) {
            try {
                final StringEncrypter encrypter = new StringEncrypter("DESede");
                return encrypter.encrypt(password);
            }
            catch (Exception ex) {
                Logger.getLogger(GenericServer.class.getName()).log(Level.SEVERE, null, ex);
                throw new IllegalArgumentException(ex);
            }
        }
        return "";
    }
}
