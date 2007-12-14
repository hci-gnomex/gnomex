package hci.gnomex.lucene;

import java.util.Iterator;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;


public class ProtocolIndexHelper extends IndexHelper {
  
  // Non-indexed fields
  public static final String       ID_PROTOCOL = "idProtocol";
  public static final String       PROTOCOL_TYPE = "protocolType";
  public static final String       CLASS_NAME = "className";

  // Indexed fields
  public static final String       NAME = "name";
  public static final String       DESCRIPTION = "description";
  public static final String       TEXT = "text";

  

}
