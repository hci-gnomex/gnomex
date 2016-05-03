package hci.dictionary.utility;

import hci.dictionary.model.DictionaryEntry;
import hci.dictionary.model.NullDictionaryEntry;
import hci.framework.security.SecurityAdvisor;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.hibernate5utils.HibernateDetailObject;

import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.id.Assigned;
import org.hibernate.internal.SessionImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.type.IntegerType;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * Utility class responsible for managing cached Hibernate-mapped dictionary classes.
 * <p>
 * This class follows the singleton idiom for Java, in that classes may not directly instantiate a DictionaryManager, but may retrieve an instance that is instantiated only once for the project. This singleton maintains a cache of the loaded dictionaries and their values, and provides the interface through which the dictionaries can be interacted with.
 * <p>
 * Functionality provided by the DictionaryManager includes:
 * <ul>
 * <li>The ability to specify which DictionaryEntry classes to manage</li>
 * <br>
 * <li>Caching behavior to initially load the dictionary entries, using Hibernate, and retrieve entries from cache for subsequent calls</li>
 * <br>
 * <li>Methods to return the values of an individual dictionary, or all dictionaries as XML data</li>
 * <br>
 * <li>The ability to process inserts, updates, and deletes and automatically persist changes to any dictionary class extending DictionaryEntry, as well as updating the cached values for the modified dictionary entry</li>
 * <br>
 * <li>Methods which can provide the functionality for the loadCommand and execute methods of an instance of the {@link hci.dictionary.utility.DictionaryCommand} class, that wishes to make use of the DictionaryManager. These methods wrap all logic needed to retrieve the appropriate parameters from the HttpServletRequest and execute the desired action (specified by {@link hci.dictionary.utility.DictionaryActions} ).
 * </ul>
 *
 * @author Cody Haroldsen
 * @version 1.0
 * @since 11/4/2005
 */
public final class DictionaryManager implements DictionaryActions, Serializable {
  private static HashMap<String, DictionaryManager> managerMap = new HashMap<>();
  private boolean loaded = false;
  private String dictionaryXmlFile;
  private TreeMap<String, Dictionary> dictionaries;

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DictionaryManager.class);
  private transient ClassLoader loader;

  private DictionaryManager() {
    dictionaries = new TreeMap<>();
  }

  /**
   * A constructor to facilitate testing
   *
   * @param test
   */
  DictionaryManager(boolean test) {

  }

  /**
   * Retrieve an handle to the DictionaryManager.
   *
   * @return The singleton DictionaryManager, which can then be used to interact with the cached dictionaries
   */
  public static synchronized DictionaryManager getDictionaryManager(String dictionaryXmlFile, Session sess, Object o) {
    return getDictionaryManager(dictionaryXmlFile, sess, o, false);
  }

  /**
   * Retrieve an handle to the DictionaryManager.
   *
   * @return The singleton DictionaryManager, which can then be used to interact with the cached dictionaries
   */
  public static synchronized DictionaryManager getDictionaryManager(String dictionaryXmlFile, Session sess, Object o, boolean includeNullEntry) {
    DictionaryManager manager = null;

    if (managerMap.containsKey(dictionaryXmlFile)) {
      manager = managerMap.get(dictionaryXmlFile);
    } else {
      manager = new DictionaryManager();
      manager.dictionaryXmlFile = dictionaryXmlFile;
      manager.loader = o.getClass().getClassLoader();
      manager.registerDictionaries(dictionaryXmlFile, o.getClass().getClassLoader());
      manager.loadAll(sess, includeNullEntry);
      managerMap.put(dictionaryXmlFile, manager);
    }

    return manager;
  }

  private void reloadCache(Session sess, boolean includeNullEntry) {
    this.loadAll(sess, includeNullEntry);
  }

  private Object readResolve() throws ObjectStreamException {
    return DictionaryManager.getDictionaryManager(dictionaryXmlFile, null, null);
  }

  void registerDictionaries(String dictionaryXmlFile, ClassLoader loader) {
    synchronized (this) {
      try {
        SAXBuilder builder = new SAXBuilder();
        InputStream is = getInputStream(dictionaryXmlFile, loader);
        Document doc = builder.build(is);
        registerDictionaries(doc);
      } catch (JDOMException e) {
        log.error("Error loading dictionaries: " + e.getMessage());
      }
    }
  }

  /**
   * Return the {@link InputStream} for the dictionaryXmlFile. If this resource can be loaded as File, then it is. Otherwise it is loaded from the classpath.
   *
   * @param dictionaryXmlFile
   * @return {@link InputStream}
   */
  InputStream getInputStream(String dictionaryXmlFile, ClassLoader loader) {
    InputStream is = null;
    try {
      is = Files.newInputStream(Paths.get(dictionaryXmlFile));
    } catch (Exception e) {
      try {
        is = loader.getResourceAsStream(dictionaryXmlFile);
      } catch (Exception e1) {
        log.error("Error loading stream: " + e1.getMessage(), e1);
      }
    }
    return is;
  }

  private void registerDictionaries(Document doc) {
    dictionaries = new TreeMap<>();
    Iterator<?> i = doc.getRootElement().getChildren().iterator();
    while (i.hasNext()) {
      // Dictionary Element
      Element e = (Element) i.next();
      String dictionaryName = e.getAttribute("displayName").getValue();
      String className = e.getAttribute("className").getValue();

      // Editable?
      boolean editable = true;
      Attribute a = e.getAttribute("editable");
      if (a != null && (a.getValue().equalsIgnoreCase("N") || a.getValue().equalsIgnoreCase("false"))) {
        editable = false;
      }

      // Scrub entries by canRead flag?
      boolean scrubEntries = false;
      Attribute sa = e.getAttribute("scrub");
      if (sa != null && (sa.getValue().equalsIgnoreCase("Y") || sa.getValue().equalsIgnoreCase("true"))) {
        scrubEntries = true;
      }

      // Set dictionary's filters
      HashMap<String, Filter> filters = new HashMap<String, Filter>();
      ArrayList<Filter> filterList = new ArrayList<Filter>();
      Iterator<?> i2 = e.getChildren().iterator();
      while (i2.hasNext()) {
        Element e2 = (Element) i2.next();
        String filterClass = e2.getAttribute("className").getValue();
        String filterField = e2.getAttribute("filterField").getValue();
        Filter f = new Filter(filterClass, filterField);
        filters.put(filterField, f);
        filterList.add(f);
      }

      registerDictionary(className, dictionaryName, filters, filterList, editable, scrubEntries);
    }
  }

  private void registerDictionary(String className, String dictionaryName, HashMap<String, Filter> filters, ArrayList<Filter> filterList, boolean editable, boolean scrubEntries) {
    try {
      // If the parameter is actually a DictionaryEntry, register it
      if (DictionaryEntry.class.isAssignableFrom(Class.forName(className, true, loader))) {
        Dictionary dict = new Dictionary(className, dictionaryName, filters, filterList, editable, scrubEntries);
        dictionaries.put(className, dict);
      }
    } catch (ClassNotFoundException e) {
    }
  }

  private void loadDictionary(Dictionary dict, Session sess, boolean includeNullEntry) throws HibernateException {
    String name = dict.getClassName().substring(dict.getClassName().lastIndexOf(".") + 1);
    dict.dictionaryEntries = new TreeMap<>();

    // Create null entry
    if (includeNullEntry) {
      NullDictionaryEntry nullEntry = new NullDictionaryEntry(dict.getClassName());
      dict.addDictionaryEntry(nullEntry.getDatakey(), nullEntry);
    }

    Iterator<?> i = sess.createQuery("from " + name).list().iterator();
    while (i.hasNext()) {
      DictionaryEntry entry = (DictionaryEntry) i.next();
      dict.addDictionaryEntry(entry.getDatakey(), entry);
    }
  }

  /**
   * Returns a defensive copy the dictionaries map
   *
   * @return
   */
  TreeMap<String, Dictionary> getDictionariesMap() {
    return new TreeMap<String, DictionaryManager.Dictionary>(dictionaries);
  }

  /**
   * Loads the DictionaryEntry values for all dictionaries registered with the DictionaryManager. Call RegisterDictionaries before executing this method.
   *
   * @param sess
   *          The Hibernate session to which the DictionarEntry classes are mapped and which should be used to load the dictionary entries
   *
   * @throws HibernateException
   */
  private void loadAll(Session sess, boolean includeNullEntry) throws HibernateException {
    synchronized (this) {
      Iterator<Dictionary> i = dictionaries.values().iterator();
      while (i.hasNext()) {
        Dictionary dict = i.next();
        loadDictionary(dict, sess, includeNullEntry);
      }
      loaded = true;
    }
  }

  /**
   * Returns true or false depending on whether or not the DictionaryManager has been loaded
   *
   * @return tru or false if the DictionaryManager has been loaded
   */
  private boolean isLoaded() {
    return loaded;
  }

  /**
   * Returns the XML data for the dictionary from the specified dictionary class.
   * <p>
   * The XML structure that is returned is of the form:
   * <p>
   * &lt;DICTIONARIES&gt;<br>
   * &nbsp;&nbsp;&lt;Dictionary className="hci.project.model.foo" displayName="Foo Dictionary"&gt;<br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&lt;DictionaryEntry display="Entry Numero Uno" value="1" /&gt;<br>
   * &nbsp;&nbsp;&nbsp;&nbsp;...<br>
   * &nbsp;&nbsp;&lt;/Dictionary&gt;<br>
   * &lt;/DICTIONARIES&gt;
   *
   * @param className
   *          A String representation of the name of the dictionary Class (ex. "hci.blah.MyDictionary")
   * @return A String containing an XML representation of the specified dictionary
   */
  public String getDictionaryXML(String className, SecurityAdvisor securityAdvisor) {
    synchronized (this) {
      StringBuffer r = new StringBuffer("");

      try {
        if (dictionaries != null && dictionaries.get(className) != null) {
          Dictionary dict = dictionaries.get(className);
          dict.setSecurity(securityAdvisor);
          String xml = dict.toDictionaryXML();
          r.append(xml.substring((xml.indexOf('>') + 1)));
        }
      } catch (XMLReflectException e) {
      }

      return r.toString();
    }
  }

  // Look for the dictionary name in all cached dictionaries
  public static Set<DictionaryEntry> getDictionaryEntries(String className) {
    Set<DictionaryEntry> entries = null;

    Iterator<DictionaryManager> i = managerMap.values().iterator();
    while (i.hasNext()) {
      DictionaryManager dm = i.next();
      if (dm.dictionaries != null && dm.dictionaries.get(className) != null) {
        Dictionary dict = dm.dictionaries.get(className);
        entries = dict.getDictionaryEntries();
        break;
      }
    }
    return entries;
  }

  private static Map<Object, Object> getDictionaryEntryMap(String className) {
    Map<Object, Object> entries = null;

    Iterator<DictionaryManager> i = managerMap.values().iterator();
    while (i.hasNext()) {
      DictionaryManager dm = i.next();
      if (dm.dictionaries != null && dm.dictionaries.get(className) != null) {
        Dictionary dict = dm.dictionaries.get(className);
        entries = dict.getDictionaryEntryMap();
        break;
      }
    }
    return entries;
  }

  public static String getDisplay(String className, String key) {
    String display = null;
    Map<Object, Object> entries = getDictionaryEntryMap(className);
    if (entries != null) {
      DictionaryEntry de = (DictionaryEntry) entries.get(key);
      if (de != null) {
        display = de.getDisplay();
      }
    }
    return display;
  }

  public static String getDisplay(String className, DictionaryEntry de) {
    String display = null;
    Map<Object, Object> entries = getDictionaryEntryMap(className);
    if (entries != null) {
      DictionaryEntry entry = (DictionaryEntry) entries.get(de);
      if (entry != null) {
        display = entry.getDisplay();
      }
    }
    return display;
  }

  public static boolean valueEquals(String className, String key, String display) {
    String realDisplay = getDisplay(className, key);
    if (realDisplay != null) {
      return realDisplay.equals(display);
    } else {
      return false;
    }
  }

  public static boolean valueEquals(String className, DictionaryEntry de, String display) {
    String realDisplay = getDisplay(className, de);
    if (realDisplay != null) {
      return realDisplay.equals(display);
    } else {
      return false;
    }
  }

  public String getValue(String className, String display) {
    String value = null;
    Set<DictionaryEntry> entries = getDictionaryEntries(className);
    if (entries != null) {
      Iterator<DictionaryEntry> i = entries.iterator();
      while (i.hasNext()) {
        DictionaryEntry de = i.next();
        if (de.getDisplay().equals(display)) {
          value = de.getValue();
          break;
        }
      }
    }
    return value;
  }

  /**
   * Returns the XML data of all of the dictionaries currently loaded.
   * <p>
   * The XML structure that is returned is of the form:
   * <p>
   * &lt;DICTIONARIES&gt;<br>
   * &nbsp;&nbsp;&lt;Dictionary className="hci.project.model.foo" displayName="Foo Dictionary"&gt;<br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&lt;DictionaryEntry display="Entry Numero Uno" value="1" /&gt;<br>
   * &nbsp;&nbsp;&nbsp;&nbsp;...<br>
   * &nbsp;&nbsp;&lt;/Dictionary&gt;<br>
   * &nbsp;&nbsp;...<br>
   * &lt;/DICTIONARIES&gt;
   *
   * @param includeNullEntry
   *          is a boolean that is true if a null option is to be included in each dictionary
   * @return A String containing an XML representation of all registered dictionaries
   */
  private String getAllXML(SecurityAdvisor securityAdvisor) {
    synchronized (this) {
      StringBuffer r = new StringBuffer("<Dictionaries>");
      if (this.dictionaries != null) {
        Iterator<Dictionary> it = this.dictionaries.values().iterator();

        while (it.hasNext()) {
          try {
            Dictionary dict = it.next();
            dict.setSecurity(securityAdvisor);
            String xml = dict.toDictionaryXML();
            r.append(xml.substring((xml.indexOf('>') + 1)));
          } catch (XMLReflectException e) {
            e.printStackTrace();
          }
        }
      }
      r.append("</Dictionaries>");
      return r.toString();
    }
  }

  private String getDictXML(SecurityAdvisor securityAdvisor, Dictionary dict) {
    synchronized (this) {
      StringBuffer r = new StringBuffer("<Dictionaries>");
      if (dict != null) {
        try {
          dict.setSecurity(securityAdvisor);
          String xml = dict.toDictionaryXML();
          r.append(xml.substring((xml.indexOf('>') + 1)));
        } catch (XMLReflectException e) {
          e.printStackTrace();
        }
      }
      r.append("</Dictionaries>");
      return r.toString();
    }
  }

  /**
   * Loads the specified command object with the parameters required to execute a given DictionaryAction, from the http request.
   * <p>
   * A valid action (specified by {@link hci.dictionary.utility.DictionaryActions}) must be included as a parameter named 'action' in the http request. If the action parameter exists and has an appropriate value, this method will load the necessary parameters from the request into the {@link hci.dictionary.utility.DictionaryCommand}, in order to later process the command when the execute method is called.
   * <p>
   * Valid actions are: <br>
   * <ul>
   * <li><b>LOAD_XML</b> Return an XML string representing all cached dictionary entries</li><br>
   * <li><b>INSERT_ENTRY</b> Persist the new dictionary entry and add it to the cache</li><br>
   * <li><b>UPDATE_ENTRY</b> Persist the modified dictionary entry and update the cache</li><br>
   * <li><b>DELETE_ENTRY</b> Delete the dictionary entry and remove it from the cache
   * </ul>
   *
   * @param command
   *          A {@link hci.dictionary.utility.DictionaryCommand} in which to load required parameters, included in the request
   * @param request
   *          The HttpServletRequest from which to retrieve parameters
   */
  public void loadCommand(DictionaryCommand command, HttpServletRequest request) {
    command.requestURL = request.getRequestURL().toString();

    command.action = request.getParameter("action");
    if (command.action == null || command.action.equals("")) {
      command.addInvalidField("action", "The action to perform is required to execute this command");
    }

    // Load the necessary parameters for the requested DictionaryAction
    if (LOAD_XML.equals(command.action) || OPEN_EDITOR.equals(command.action) || LOAD_METADATA.equals(command.action)) {
      command.className = request.getParameter("className");
    } else if (INSERT_ENTRY.equals(command.action) || UPDATE_ENTRY.equals(command.action) || DELETE_ENTRY.equals(command.action)) {

      command.className = request.getParameter("className");
      if (command.className == null || command.className.equals("")) {
        command.addInvalidField("className", "The className is required to perform to execute this command");
      } else {
        try {
          // Instantiate the DictionaryEntry class
          Class<?> c = Class.forName(command.className, true, loader);
          command.dictionaryEntry = (DictionaryEntry) c.getConstructor(new Class[] {}).newInstance(new Object[] {});

          // Load the DictionaryEntry (DetailObject) from the request
          command.loadDetailObject(request, command.dictionaryEntry);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Executes the DictionaryCommand that was pre-loaded by the loadCommand method.
   * <p>
   * This will look at the action property set in the DictionaryCommand, and will perform the required actions.
   * <p>
   * Valid actions are: <br>
   * <ul>
   * <li><b>LOAD_XML</b> Return an XML string representing all cached dictionary entries</li><br>
   * <li><b>INSERT_ENTRY</b> Persist the new dictionary entry and add it to the cache</li><br>
   * <li><b>UPDATE_ENTRY</b> Persist the modified dictionary entry and update the cache</li><br>
   * <li><b>DELETE_ENTRY</b> Delete the dictionary entry and remove it from the cache
   * </ul>
   *
   * @param command
   *          A {@link hci.dictionary.utility.DictionaryCommand} object, which has been pre-loaded by the DictionaryManager's loadCommand method
   * @param sess
   *          A Hibernate Session that can be used to persist dictionary entries
   *
   * @throws HibernateException
   */
  public void executeCommand(DictionaryCommand command, Session sess, SecurityAdvisor securityAdvisor) throws HibernateException {
    this.executeCommand(command, sess, securityAdvisor, false);
  }

  /**
   * Executes the DictionaryCommand that was pre-loaded by the loadCommand method.
   * <p>
   * This will look at the action property set in the DictionaryCommand, and will perform the required actions.
   * <p>
   * Valid actions are: <br>
   * <ul>
   * <li><b>LOAD_XML</b> Return an XML string representing all cached dictionary entries</li><br>
   * <li><b>INSERT_ENTRY</b> Persist the new dictionary entry and add it to the cache</li><br>
   * <li><b>UPDATE_ENTRY</b> Persist the modified dictionary entry and update the cache</li><br>
   * <li><b>DELETE_ENTRY</b> Delete the dictionary entry and remove it from the cache
   * </ul>
   *
   * @param command
   *          A {@link hci.dictionary.utility.DictionaryCommand} object, which has been pre-loaded by the DictionaryManager's loadCommand method
   * @param sess
   *          A Hibernate Session that can be used to persist dictionary entries
   *
   * @throws HibernateException
   */
  public void executeCommand(DictionaryCommand command, Session sess, SecurityAdvisor securityAdvisor, boolean includeNullEntry) throws HibernateException {
    // Perform the requested DictionaryAction
    synchronized (this) {
      this.loader = command.getClass().getClassLoader();

      // LOAD XML
      if (LOAD_XML.equals(command.action)) {
        // get the XML for a specific dictionary
        if (command.className != null && !command.className.equals("")) {
          command.xmlResult = "<Dictionaries>" + this.getDictionaryXML(command.className, securityAdvisor) + "</Dictionaries>";
        }
        // get the XML for all dictionaries
        else {
          command.xmlResult = this.getAllXML(securityAdvisor);
        }
      }

      // RELOAD CACHE
      else if (RELOAD_CACHE.equals(command.action)) {
        this.reloadCache(sess, includeNullEntry);
        command.xmlResult = "<SUCCESS/>";
      }

      // LOAD DICTIONARY METADATA FOR FLEX
      else if (LOAD_METADATA.equals(command.action)) {
        if (command.className != null && !command.className.equals("")) {
          Dictionary dict = dictionaries.get(command.className);
          command.xmlResult = dict.createEditorMetaData(sess);
        }
      }

      // ADD DICTIONARY ENTRY
      else if (INSERT_ENTRY.equals(command.action)) {
        try {
          if (securityAdvisor.canUpdate(command.dictionaryEntry)) {
            sess.save(command.dictionaryEntry);
            sess.flush();

            // Add the entry to the cached dictionary
            Dictionary dict = this.dictionaries.get(command.className);
            dict.addDictionaryEntry(command.dictionaryEntry.getDatakey(), command.dictionaryEntry);
            command.xmlResult = this.getDictXML(securityAdvisor, dict);
          }
        } catch (UnknownPermissionException e) {
        }
      }

      // SAVE DICTIONARY ENTRY
      else if (UPDATE_ENTRY.equals(command.action)) {
        try {
          if (securityAdvisor.canUpdate(command.dictionaryEntry)) {
            // Load the existing object
            ClassMetadata meta = sess.getSessionFactory().getClassMetadata(command.dictionaryEntry.getClass());
            Object newObj = null;
            try {
              newObj = command.dictionaryEntry.clone();
            } catch (CloneNotSupportedException ex) {
            }

            Object oldObj = sess.get(command.dictionaryEntry.getClass(), meta.getIdentifier(command.dictionaryEntry, (SessionImpl) sess));

            // Remove the cached dictionary entry (necessary, in case the value
            // property is actually changed,
            // which is possible, since it doesn't necessarily correspond to the
            // actual PK (composite keys for example))
            Dictionary dict = this.dictionaries.get(command.className);
            dict.removeDictionaryEntry(((DictionaryEntry) oldObj).getDatakey());

            meta.setPropertyValues(oldObj, meta.getPropertyValues(newObj));
            sess.update(oldObj);
            sess.flush();

            // Update the cached dictionary entry
            dict.addDictionaryEntry(command.dictionaryEntry.getDatakey(), command.dictionaryEntry);
            command.xmlResult = this.getDictXML(securityAdvisor, dict);
          }
        } catch (UnknownPermissionException e) {
        }
      }

      // REMOVE DICTIONARY ENTRY
      else if (DELETE_ENTRY.equals(command.action)) {
        try {
          boolean error = false;

          if (securityAdvisor.canUpdate(command.dictionaryEntry)) {
            ClassMetadata meta = sess.getSessionFactory().getClassMetadata(command.dictionaryEntry.getClass());
            Object oldObj = sess.get(command.dictionaryEntry.getClass(), meta.getIdentifier(command.dictionaryEntry, (SessionImpl) sess));

            Dictionary dict = null;
            try {
              // Just delete - If there is a foreign-key, this will fail
              sess.delete(oldObj);
              sess.flush();

              // Remove the cached dictionary entry
              dict = this.dictionaries.get(command.className);
              dict.removeDictionaryEntry(command.dictionaryEntry.getDatakey());
            } catch (Exception e) {
              // If the above failed (constraint violation?) try setting is
              // Active = N
              try {
                Method m = command.dictionaryEntry.getClass().getMethod("setIsActive", new Class[] { String.class });

                sess.clear();
                oldObj = sess.get(command.dictionaryEntry.getClass(), meta.getIdentifier(command.dictionaryEntry, (SessionImpl) sess));

                // isActive exists - mark inactive
                m.invoke(oldObj, new Object[] { "N" });
                sess.update(oldObj);
                sess.flush();

                // Update the cached dictionary entry
                dict = this.dictionaries.get(command.className);
                dict.addDictionaryEntry(command.dictionaryEntry.getDatakey(), (DictionaryEntry) oldObj);
              } catch (Exception ex) {
                e.printStackTrace();
                command.xmlResult = "<ERROR message=\"Unable to Delete Entry, possibly because there are records using it.\"/>";
                error = true;
              }
            }

            if (dict != null) {
              command.xmlResult = this.getDictXML(securityAdvisor, dict);
            } else {
              if (!error) {
                command.xmlResult = "<SUCCESS/>";
              }
            }
          }
        } catch (UnknownPermissionException e) {
        }
      }
    }
  }

  /**
   * Returns the display for a given dictionary class and value. If the value is not found then an empty String is returned.
   *
   * @param className
   * @param key
   * @return String - the display for the dictionary value
   */
  public String getDisplayByValue(String className, Object key) {
    if (key != null) {
      Dictionary dict = this.dictionaries.get(className);
      if (dict != null && dict.getDictionaryEntryByValue(key.toString()) != null)
        return dict.getDictionaryEntryByValue(key.toString()).getDisplay();
      else
        return "";
    } else {
      return "";
    }
  }

  /**
   * This Dictionary class is a private inner class of DictionaryManager, so that it is not visible to developers using the HCIDictionary.jar, since there might be some confusion about which class to extend (Dictionary or DictionaryEntry).
   *
   * @author sharoldsen
   */
  private class Dictionary extends HibernateDetailObject {
    private String displayName;
    private String className;
    private TreeMap<Object, Object> dictionaryEntries;
    private HashMap<String, Filter> filters;
    private ArrayList<Filter> filterList;
    private HashMap<?, ?> maxLengths;
    private boolean canWrite = false;
    public boolean isEditable = false;
    public boolean scrubEntries = false;

    public Dictionary(String className, String displayName, HashMap<String, Filter> filters, ArrayList<Filter> filterList, boolean editable, boolean scrub) {
      this.className = className;
      this.displayName = displayName;
      this.filterList = filterList;
      this.filters = filters;
      this.isEditable = editable;
      this.scrubEntries = scrub;
    }

    public void addDictionaryEntry(String key, DictionaryEntry entry) {
      dictionaryEntries.put(key, entry);
    }

    public void removeDictionaryEntry(String key) {
      if (dictionaryEntries != null) {
        dictionaryEntries.remove(key);
      }
    }

    public DictionaryEntry getDictionaryEntryByValue(String key) {
      if (dictionaryEntries != null) {
        return (DictionaryEntry) dictionaryEntries.get(key);
      } else
        return null;
    }

    public TreeMap<Object, Object> getDictionaryEntryMap() {
      return this.dictionaryEntries;
    }

    // In order to return the XML sorted
    public TreeSet<DictionaryEntry> getDictionaryEntries() {
      TreeSet<DictionaryEntry> sortedEntries = null;
      Iterator<Object> i = dictionaryEntries.values().iterator();
      while (i.hasNext()) {
        DictionaryEntry de = (DictionaryEntry) i.next();

        if (sortedEntries == null) {
          sortedEntries = new TreeSet<DictionaryEntry>(de);
        }

        sortedEntries.add(de);
      }

      if (sortedEntries == null) {
        sortedEntries = new TreeSet<DictionaryEntry>();
      }

      return sortedEntries;
    }

    public String getClassName() {
      return className;
    }

    public String getDisplayName() {
      return displayName;
    }

    public void setClassName(String className) {
      this.className = className;
    }

    public void setDisplayName(String displayName) {
      this.displayName = displayName;
    }

    public HashMap<String, Filter> getFilters() {
      return filters;
    }

    public void setFilters(HashMap<String, Filter> filters) {
      this.filters = filters;
    }

    public String getCanWrite() {
      String str = "Y";
      if (!canWrite) {
        str = "N";
      }
      return str;
    }

    private void setCanWrite(boolean canWrite) {
      this.canWrite = canWrite;
    }

    public void setSecurity(SecurityAdvisor securityAdvisor) {

      try {
        if (securityAdvisor.canUpdate(Class.forName(this.className, true, loader)) && isEditable) {
          this.setCanWrite(true);
        } else {
          this.setCanWrite(false);
        }
      } catch (Exception e) {
        this.setCanWrite(false);
      }

      Iterator<Object> i = this.dictionaryEntries.values().iterator();
      while (i.hasNext()) {
        DictionaryEntry de = (DictionaryEntry) i.next();

        try {
          if (securityAdvisor.canUpdate(de) && getCanWriteFilters(de, securityAdvisor)) {
            de.setCanWrite(true);
          } else {
            de.setCanWrite(false);
          }
        } catch (UnknownPermissionException e1) {
          de.setCanWrite(false);
        }

        try {
          // Set can read
          de.canRead(securityAdvisor.canRead(de));
        } catch (UnknownPermissionException e1) {
          de.canRead(false);
        }

        try {
          // Set can update
          de.canUpdate(securityAdvisor.canUpdate(de));
        } catch (UnknownPermissionException e1) {
          de.canUpdate(false);
        }

        try {
          // Set can delete
          de.canDelete(securityAdvisor.canDelete(de));
        } catch (UnknownPermissionException e1) {
          de.canDelete(false);
        }

      }
    }

    public boolean getCanWriteFilters(DictionaryEntry de, SecurityAdvisor securityAdvisor) throws UnknownPermissionException {
      boolean canWrite = true;

      if (this.getFilters() != null) {
        Iterator<Filter> i = this.getFilters().values().iterator();
        while (i.hasNext()) {
          Filter f = i.next();

          Dictionary dict = dictionaries.get(f.filterClass);
          if (dict != null && dict.getDictionaryEntryMap() != null) {
            String fieldValueString = de.getFieldValueString(f.getFilterField());
            try {
              DictionaryEntry de2 = (DictionaryEntry) dict.getDictionaryEntryMap().get(fieldValueString);
              if (de2 != null) {
                canWrite = canWrite && securityAdvisor.canUpdate(de2) && dict.getCanWriteFilters(de2, securityAdvisor);
              }
            } catch (Exception e) {
            }
          }
        }
      }

      return canWrite;
    }

    public String getFilterFieldDisplay(DictionaryEntry de) {
      String display = "";
      if (this.getFilters() != null) {
        Iterator<Filter> i = this.getFilters().values().iterator();
        while (i.hasNext()) {
          Filter f = i.next();
          Dictionary dict = dictionaries.get(f.filterClass);
          String fieldValueString = de.getFieldValueString(f.getFilterField());
          try {
            DictionaryEntry de2 = (DictionaryEntry) dict.getDictionaryEntryMap().get(fieldValueString);
            if (de2 != null) {
              display += ", " + de2.getDisplay() + dict.getFilterFieldDisplay(de2);
            }
          } catch (Exception e) {
          }
        }
      }
      return display;
    }

    // These don't need to show up in the XML returned to the client
    public void registerMethodsToExcludeFromXML() {
      this.excludeMethodFromXML("getFilters");
    }

    private String parseDisplay(String fieldName) {
      String display = "";
      String cPrev = null;
      if (fieldName != null) {
        for (int i = 0; i < fieldName.length(); i++) {
          String c = fieldName.substring(i, i + 1);
          String cNext = (i + 1 < fieldName.length()) ? fieldName.substring(i + 1, i + 2) : null;

          // First character is always upper case
          if (i == 0) {
            display += c.toUpperCase();
          }
          // Add space if the character is upper case followed by or preceded by
          // a lower case character
          else if (c.toUpperCase().equals(c) && (!cPrev.toUpperCase().equals(cPrev) || (cNext != null && !cNext.toUpperCase().equals(cNext)))) {
            display += " " + c;
          } else {
            display += c;
          }

          cPrev = c;
        }
      }

      return display;
    }

    private String parseFilterDisplay(String display) {
      if (display == null) {
        display = "";
      }

      if (display.startsWith("Id ") && display.length() >= 4) {
        display = display.substring(3);
      }

      if (display.endsWith(" Dictionary") && display.length() >= 12) {
        display = display.substring(0, display.lastIndexOf(" Dictionary"));
      }

      return display;
    }

    /**
     * Formats a date object as a string, according to the specified format
     *
     * @param object
     *          The date object to be formatted as a String
     * @param outputStyle
     *          The style in which to forat the date, as specified in DetailObject
     * @return A string representing the date object, formatted as specified
     */
    protected String convertDate(Object o, int outputStyle) {
      String result = null;
      if (o != null) {
        if ((o instanceof java.sql.Date)) {
          java.sql.Date d = (java.sql.Date) o;
          result = this.formatDate(d, outputStyle);
        } else if ((o instanceof java.sql.Timestamp)) {
          java.sql.Timestamp t = (java.sql.Timestamp) o;
          result = this.formatTimestamp(t, outputStyle);
        } else if ((o instanceof java.util.Date)) {
          java.util.Date d = (java.util.Date) o;
          result = this.formatDateTime(d, outputStyle);
        }
      }

      return result;
    }

    /**
     * Returns the base class name. This is the highest class name in the superclass hierarchy
     *
     */
    protected String getBaseClassName() {
      return "Dictionary";
    }

    /**
     * Returns this dictionary object as xml using reflection. Unlike the standard toXMLString() method in HibernateDetailObject, this does not include nodes for collections, and always uses the base class (Dictionary or DictionaryEntry, rather than a subclass) as the root node
     *
     * @return an XML string representing this dictionary
     */
    public String toDictionaryXML() throws XMLReflectException {
      String result = null;

      // Document doc = this.toDictionaryXMLDoc(DATE_OUTPUT_SLASH);
      // org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
      // result = out.outputString(doc);

      try {
        Element dict = new Element("Dictionary");
        dict.setAttribute("displayName", this.displayName);
        dict.setAttribute("className", this.className);
        dict.setAttribute("canWrite", this.getCanWrite());

        // adding filters into xml
        Element filters = new Element("Filters");
        dict.addContent(filters);
        Iterator<Filter> fIter = filterList.iterator();
        while (fIter.hasNext()) {
          Filter f = fIter.next();
          Element e = new Element("filter");
          e.setAttribute("filterField", f.getFilterField());
          e.setAttribute("filterClass", f.getFilterClass());
          filters.addContent(e);
        }

        this.maxLengths = new HashMap<Object, Object>();
        Iterator<DictionaryEntry> i = this.getDictionaryEntries().iterator();

        while (i.hasNext()) {
          DictionaryEntry de = i.next();

          if (!this.scrubEntries || de.canRead()) {
            Document doc = de.toDictionaryXMLDoc(DATE_OUTPUT_SLASH, this.maxLengths);
            dict.addContent(doc.getRootElement());
          }
        }

        // Output the XML document
        XMLOutputter out = new XMLOutputter();
        Document doc = new Document(dict);
        result = out.outputString(doc);
      } catch (XMLReflectException ex) {
        ex.printStackTrace();
      }

      return result;
    }

    private String createEditorMetaData(Session sess) {

      Element dictionary = new Element("Dictionary");
      dictionary.setAttribute("className", this.getClassName());
      dictionary.setAttribute("displayName", this.getDisplayName());
      dictionary.setAttribute("canWrite", this.getCanWrite());
      Element root = new Element("DictMetaData");
      root.addContent(dictionary);
      Document doc = new Document(root);
      Element field = null;
      // Add any filters
      if (this.filters != null) {
        Iterator<Filter> i = this.filterList.iterator();
        while (i.hasNext()) {
          Filter f = i.next();
          int width = 165;
          if (!f.filterClass.equals("YN")) {
            // Filters are supposed to be other dictionaries, so we should be
            // able
            // to just look up the dictionary it represents and grab its display
            // name
            Dictionary dict = dictionaries.get(f.filterClass);

            field = new Element("Field");
            field.setAttribute("isFilter", "Y");
            field.setAttribute("id", f.getFilterField());
            field.setAttribute("className", f.getFilterClass());
            field.setAttribute("dataField", f.getFilterField());
            field.setAttribute("dataType", "comboBox");
            field.setAttribute("caption", parseFilterDisplay((dict != null) ? dict.getDisplayName() : this.parseDisplay(f.getFilterField())));
            field.setAttribute("visible", "Y");
            dictionary.addContent(field);
          }
        }
      }
      // Add the fields
      if (this.className != null) {
        try {
          ClassMetadata meta = sess.getSessionFactory().getClassMetadata(Class.forName(this.className, true, loader));
          java.util.List<String> propertyNames = Arrays.asList(meta.getPropertyNames());

          Object o = Class.forName(this.className, true, loader).getConstructor(new Class[] {}).newInstance(new Object[] {});
          ((DictionaryEntry) o).registerMethodsToExcludeFromXML();

          HashMap<?, ?> controls = new HashMap<Object, Object>();

          Method[] methods = Class.forName(this.className, true, loader).getMethods();
          for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            if (m.getName().indexOf("get") != -1 && m.getParameterTypes().length == 0) {
              // we have a getter - create a field, as long as the getter isn't
              // excluded
              if ((((DictionaryEntry) o).getExcludedMethodsMap() == null || ((DictionaryEntry) o).getExcludedMethodsMap().get(m.getName()) == null) && !m.getName().equals("getDatakey") && !m.getName().equals("getCanWrite") && !m.getName().equals("getCanRead") && !m.getName().equals("getCanUpdate") && !m.getName().equals("getCanDelete")) {

                String fieldName = m.getName().substring(3, 4).toLowerCase() + ((m.getName().length() > 4) ? m.getName().substring(4) : "");

                // Handle field names that start with a capital letter
                try {
                  Class.forName(this.className, true, loader).getDeclaredField(fieldName);
                } catch (Exception e) {
                  try {
                    Class.forName(this.className, true, loader).getDeclaredField(fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                    fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                  } catch (Exception ex) {
                  }
                }

                Object control = null;

                // Filters
                if (this.filters != null && this.filters.containsKey(fieldName)) {
                  Filter f = this.filters.get(fieldName);
                  if (f.filterClass.equals("YN")) {

                    field = new Element("Field");
                    field.setAttribute("isFilter", "Y");
                    field.setAttribute("id", f.getFilterField());
                    field.setAttribute("className", f.getFilterClass());
                    field.setAttribute("dataField", f.getFilterField());
                    field.setAttribute("dataType", "YN");
                    field.setAttribute("caption", parseDisplay(f.getFilterField()));
                    field.setAttribute("visible", "Y");
                    dictionary.addContent(field);
                  }
                }
                // Normal fields
                else {
                  // isActive
                  if (fieldName.equals("isActive")) {

                    field = new Element("Field");
                    field.setAttribute("isFilter", "N");
                    field.setAttribute("id", fieldName);
                    field.setAttribute("className", this.getClassName());
                    field.setAttribute("dataField", fieldName);
                    field.setAttribute("dataType", "isActive");
                    field.setAttribute("caption", "Active ?");
                    field.setAttribute("visible", "Y");
                    dictionary.addContent(field);
                  } else {
                    // Make dates datepickers
                    if (m.getReturnType().equals(java.util.Date.class) || m.getReturnType().equals(java.sql.Date.class) || m.getReturnType().equals(java.sql.Timestamp.class)) {

                      field = new Element("Field");
                      field.setAttribute("isFilter", "N");
                      field.setAttribute("id", fieldName);
                      field.setAttribute("className", this.getClassName());
                      field.setAttribute("dataField", fieldName);
                      field.setAttribute("dataType", "date");
                      field.setAttribute("caption", parseDisplay(fieldName));
                      if (propertyNames.contains(fieldName)) {
                        field.setAttribute("visible", "Y");
                      } else {
                        field.setAttribute("visible", "N");
                      }
                      dictionary.addContent(field);
                    }
                    //
                    // Make all other fields text boxes
                    else {
                      field = new Element("Field");
                      field.setAttribute("isFilter", "N");
                      field.setAttribute("id", fieldName);
                      field.setAttribute("className", this.getClassName());
                      field.setAttribute("dataField", fieldName);
                      field.setAttribute("dataType", "text");
                      field.setAttribute("caption", parseDisplay(fieldName));

                      // Hibernate mapped fields and identifiers (non-integers)
                      // are visible
                      if (propertyNames.contains(fieldName)) {
                        field.setAttribute("visible", "Y");
                        field.setAttribute("isIdentifier", "N");
                      } else {
                        if (isKeyColumn(meta, fieldName) && ((AbstractEntityPersister) meta).getIdentifierGenerator() instanceof Assigned) {
                          field.setAttribute("visible", "Y");
                          field.setAttribute("isIdentifier", "Y");
                        } else {
                          field.setAttribute("visible", "N");
                          field.setAttribute("isIdentifier", "Y");
                        }
                      }

                      if (maxLengths != null) {
                        Integer maxLength = (Integer) maxLengths.get(fieldName);
                        if (maxLength != null) {
                          field.setAttribute("length", maxLength.toString());
                        }
                      }

                      dictionary.addContent(field);
                    }
                  }
                }
              }
            }
          }

        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      XMLOutputter out = new XMLOutputter();
      return out.outputString(doc);
    }
  }

  private boolean isKeyColumn(ClassMetadata meta, String fieldName) {
    String[] keys = ((AbstractEntityPersister) meta).getKeyColumnNames();
    for (int i = 0; i < keys.length; i++) {
      if (keys[i].equals(fieldName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * This Dictionary class is a private inner class of DictionaryManager, so that it is not visible to developers using the HCIDictionary.jar. There should be no reason that it would need to be used outside the DictionaryManager and not having it visible should just make things simpler for users of this API.
   *
   * @author sharoldsen
   */
  private class Filter implements Serializable {
    private String filterClass;
    private String filterField;

    public Filter(String filterClass, String filterField) {
      this.filterClass = filterClass;
      this.filterField = filterField;
    }

    public String getFilterClass() {
      return filterClass;
    }

    public void setFilterClass(String filterClass) {
      this.filterClass = filterClass;
    }

    public String getFilterField() {
      return filterField;
    }

    public void setFilterField(String filterField) {
      this.filterField = filterField;
    }
  }
}
