package hci.gnomex.utility;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: HCI - Informatics</p>
 * @author Kirt Henrie
 * @version 2.0
 *
 * Modification:
 * 6/10/03    K. Henrie        Change for hibernate 2.0
 */

import hci.gnomex.constants.Constants;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

public class HibernateSession {
private static Logger LOG = Logger.getLogger(HibernateSession.class);

private static final ThreadLocal session = new ThreadLocal();
private static final ThreadLocal transaction = new ThreadLocal();
private static final ThreadLocal readOnly = new ThreadLocal();

public static final String SESSION_FACTORY_JNDI_NAME = "sessions/GNOMEX_FACTORY";

public static Session currentSession(String username, String who) throws NamingException, HibernateException,
		SQLException {
	// System.out.println("[Hibernate:currentSession] username: " + username + " who: " + who);
	return currentSession(username);

}

public static Session currentSession(String username) throws NamingException, HibernateException, SQLException {
	Session s = (Session) session.get();
	if (s == null || !s.isOpen()) {
		s = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = s.beginTransaction();
		transaction.set(tx);
		session.set(s);
		readOnly.set(false);
	}

	setAppName(s, username);
	return s;
}

public static Session currentReadOnlySession(String username, String who) throws NamingException, HibernateException,
		SQLException {
	// System.out.println("[Hibernate:currentReadOnlySession] username: " + username + " who: " + who);
	return currentReadOnlySession(username);

}

public static Session currentReadOnlySession(String username) throws NamingException, HibernateException, SQLException {
	Session s = (Session) session.get();
	if (s == null || !s.isOpen()) {
		s = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = s.beginTransaction();
		transaction.set(tx);
		session.set(s);
		readOnly.set(true);
	}

	setAppName(s, username);
	return s;
}

public static Session currentSession() throws NamingException, HibernateException, SQLException {
	Session s = (Session) session.get();
	if (s == null) {
		throw new HibernateException(
				"This method can only be invoked if a session already exists in the thread of execution");
	}
	return s;
}

public static void rollback() {
	Session s = (Session) session.get();
	if (s == null) {
		// System.out.println("[Hibernate rollback] session is null.");
		return;
	}

	Transaction tx = s.getTransaction();
	if (tx != null) {
		TransactionStatus txStat = tx.getStatus();
		// System.out.println("[Hibernate rollback] txStat: " + txStat);

		tx.rollback();
		transaction.set(null);
	} else {
		// System.out.println("[Hibernate rollback] tx is null ");
	}

	// if we rolled back, we are done
	try {
		closeSession();
	} catch (Exception e) {
		// we don't care
	}

}

public static void closeSession(String who) throws HibernateException, SQLException {
	// System.out.println("[Hibernate:closeSession] who: " + who);
	closeSession();
}

public static void closeSession() throws HibernateException, SQLException {
	// tx will be null if transaction already rolled back.
	Session s = (Session) session.get();
	if (s == null) {
		// System.out.println("[Hibernate:closeSession] session is NULL");
		return;
	}

	Boolean isReadOnly = (Boolean) readOnly.get();

	// if this is a read only session then evict all loaded instances and cancel all pending saves, updates and deletions.
	if (isReadOnly) {
		s.clear();
		// System.out.println("[Hibernate:closeSession] session is readOnly");
	}

	setAppName(s, null);

	Transaction tx = s.getTransaction();
	if (tx == null) {
		session.set(null);
		transaction.set(null);
		readOnly.set(false);
		// System.out.println("[Hibernate:closeSession] tx is NULL ");

		s.close();
		return;
	}

	// tx can't be null here
	TransactionStatus txStat = tx.getStatus();
	// System.out.println("[Hibernate:closeSession] txStat: " + txStat);

	// if it's not commited or in the process of committing or rolled back or trying to roll back and it's active then commit
	if (txStat != null && txStat.isNotOneOf(TransactionStatus.COMMITTED)
			&& txStat.isNotOneOf(TransactionStatus.COMMITTING) && txStat.isOneOf(TransactionStatus.ACTIVE)
			&& txStat.isNotOneOf(TransactionStatus.ROLLED_BACK) && txStat.isNotOneOf(TransactionStatus.ROLLING_BACK)) {
		try {
			tx.commit();
			// System.out.println("[Hibernate:closeSession] did commit");
			txStat = null;
		} catch (Exception e) {
			LOG.error("[Hibernate:closeSession] Failed to commit Transaction, going to try and rollback ", e);

			try {
				// Maybe the commit above worked and so the transaction is no longer active therefore don't try the rollback or else we will get an inactive
				// tx exception.
				txStat = tx.getStatus();
				if (txStat != null && txStat.isOneOf(TransactionStatus.ACTIVE)) {
					// System.out.println("[Hibernate:closeSession] did rollback on failed commit");
					tx.rollback();
				}
			} catch (Exception e1) {
				LOG.error("[Hibernate:closeSession] Failed to rollback transaction on failed commit ", e1);
			}

		}

	} else {
		// System.out.println("[Hibernate:closeSession] did NOT commit because txStat: " + txStat);
	}

	session.set(null);
	transaction.set(null);
	readOnly.set(false);

	s.close();
}

public static boolean hasCurrentSession() {
	return (session.get() != null);
}

public static void setAppName(Session s, String username) {
	if (s == null) {
		return;
	}

	try {
		Connection con = HibernateUtil.getConnection(s);
		if (con.getMetaData().getDatabaseProductName().toUpperCase().indexOf(Constants.SQL_SERVER) >= 0) {
			CallableStatement stmt;
			stmt = con.prepareCall("{ call master.dbo.setAppUser(?) }");
			stmt.setString(1, username);
			stmt.executeUpdate();
			stmt.close();
		} else {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("set @username='" + username + "';");
			stmt.close();
		}
	} catch (Exception e) {
		LOG.error("[HibernateSession:setAppName] Failed to setAppUser, username: " + username, e);
	}
}
}
