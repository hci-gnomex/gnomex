package hci.gnomex.utility;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: HCI - Informatics</p>
 * @author Kirt Henrie
 * @version 1.0
 */

public class NoCurrentHibernateSessionException extends Exception {

  public NoCurrentHibernateSessionException() {
    super("A Hibernate Session must exist in the local thread in order to execute this method");
  }
}