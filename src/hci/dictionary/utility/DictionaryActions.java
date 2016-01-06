package hci.dictionary.utility;

/**
 * Specifies pre-defined strings for the various commands that the
 * {@link hci.dictionary.utility.DictionaryManager} can process.
 * <p>
 * These commands tell the {@link hci.dictionary.utility.DictionaryManager} what
 * action it should perform (within the loadCommand and execute methods), as
 * well as what data it should expect to load from the http session.
 * <p>
 * The functions of the varous commands are as follows:
 * <ul>
 * <li><b>LOAD_XML</b>&nbsp;&nbsp;&nbsp;Return an XML string representing all
 * cached dictionary entries
 * <li><b>INSERT_ENTRY</b>&nbsp;&nbsp;&nbsp;Persist the new dictionary entry and
 * adds it to the cache
 * <li><b>UPDATE_ENTRY</b>&nbsp;&nbsp;&nbsp;Persist the modified dictionary
 * entry and updates the cache
 * <li><b>DELETE_ENTRY</b>&nbsp;&nbsp;&nbsp;Delete the dictionary entry and
 * remove it from the cache
 * </ul>
 * 
 * @author Cody Haroldsen
 * @version 1.0
 * @since 11/5/2005
 */
public interface DictionaryActions {
  public static String LOAD_XML = "load";
  public static String OPEN_EDITOR = "edit";
  public static String INSERT_ENTRY = "add";
  public static String UPDATE_ENTRY = "save";
  public static String DELETE_ENTRY = "delete";
  public static String RELOAD_CACHE = "reload";
  public static String LOAD_METADATA = "metadata";
}
