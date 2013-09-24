package hci.gnomex.utility;

import org.hibernate.engine.SessionImplementor;
import java.sql.SQLException;
import org.hibernate.HibernateException;
import org.hibernate.id.IdentifierGenerator;
import java.sql.*;
import org.apache.commons.logging.*;
import org.hibernate.util.*;
import java.io.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: HCI - Informatics</p>
 * @author Kirt Henrie
 * @version 1.0
 */

public class Hibernate3xUIDGenerator implements IdentifierGenerator, Serializable {

  private static final Log log = LogFactory.getLog(Hibernate3xUIDGenerator.class);
  private String table;

  public Hibernate3xUIDGenerator(String table) {
    this.table = table;
  }
  public Hibernate3xUIDGenerator() {
  }
  public Serializable generate(SessionImplementor session, Object obj) throws org.hibernate.HibernateException {
    ResultSet rs = null;
    CallableStatement stmt = null;
    int id;

    try {
      stmt = session.connection().prepareCall("{ call dbo.pr_GetNextKey(?, ?, ?) }");

      stmt.setString(1, table);
      stmt.setInt(2, 0);
      stmt.setInt(3, 0);
      stmt.registerOutParameter(2, Types.INTEGER);

      stmt.executeUpdate();

      id = stmt.getInt(2);

      log.debug("Sequence ID generated: " + id);
      return new Integer(id);
    }
    catch (SQLException sqle) {
            JDBCExceptionReporter.logExceptions(sqle);
            throw new RuntimeException( "Cannot get next id " + sqle.toString());
    }
    finally {
      try {
            session.getBatcher().closeStatement(stmt);
      } catch (SQLException sqle) {
        JDBCExceptionReporter.logExceptions(sqle);
        throw new RuntimeException( "Cannot close statement " + sqle.toString());
      }
    }
  }
  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
  }
  public String getTable() {
    return table;
  }
  public void setTable(String table) {
    this.table = table;
  }
}