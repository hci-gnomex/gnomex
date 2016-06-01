package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.Lab;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.GNomExRollbackException;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;

public class DeleteAppUser extends GNomExCommand implements Serializable {

	// the static field for logging in Log4J
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteAppUser.class);

	private Integer idAppUser = null;

	public void validate() {
	}

	public void loadCommand(HttpServletRequest request, HttpSession session) {

		if (request.getParameter("idAppUser") != null && !request.getParameter("idAppUser").equals("")) {
			idAppUser = new Integer(request.getParameter("idAppUser"));
		} else {
			this.addInvalidField("idAppUser", "idAppUser is required.");
		}

	}

	public Command execute() throws GNomExRollbackException {

		try {
			Session sess = HibernateSession.currentSession(this.getUsername());

			// Check permissions
			if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_USERS)) {
				AppUser appUser = (AppUser) sess.load(AppUser.class, idAppUser);

				//
				// Check projects. Delete any empty ones. Error if non-empty ones
				//
				String queryString = "select distinct project " + "from Project project " + "left join project.requests requests "
						+ "left join project.experimentDesignEntries design " + "left join project.experimentFactorEntries factor "
						+ "left join project.qualityControlStepEntries step " + "where project.idAppUser = :idAppUser and "
						+ "(requests.idProject is not null or design.idProject is not null or factor.idProject is not null or step.idProject is not null) ";
				Query query = sess.createQuery(queryString);
				query.setParameter("idAppUser", appUser.getIdAppUser());
				List projects = query.list();
				if (projects.size() > 0) {
					this.addInvalidField("Project", "User owns non-empty projects.  Delete aborted");
					setResponsePage(this.ERROR_JSP);
					return this;
				}

				queryString = "delete Project project where project.idAppUser=:idAppUser";
				query = sess.createQuery(queryString);
				query.setParameter("idAppUser", appUser.getIdAppUser());
				query.executeUpdate();

				//
				// Delete lab members
				//
				for (Iterator i = appUser.getLabs().iterator(); i.hasNext();) {
					Lab lab = (Lab) i.next();
					for (Iterator i1 = lab.getMembers().iterator(); i1.hasNext();) {
						AppUser member = (AppUser) i1.next();
						if (member.getIdAppUser().intValue() == appUser.getIdAppUser().intValue()) {
							lab.getMembers().remove(member);
							break;
						}

					}
					// Remove the lab member from any accounts they are users on
					if (lab.getBillingAccounts() != null) {
						for (Iterator i2 = lab.getBillingAccounts().iterator(); i2.hasNext();) {
							BillingAccount ba = (BillingAccount) i2.next();
							ba.getUsers().remove(appUser);
						}
					}
				}

				// remove appuser as a collaborator (analysis, datatrack, lab and request)
				queryString = "delete AnalysisCollaborator ac where ac.idAppUser=:idAppUser";
				query = sess.createQuery(queryString);
				query.setParameter("idAppUser", appUser.getIdAppUser());
				query.executeUpdate();

				// DataTrackCollaborator and LabCollaborator aren't mapped in the schema so we'll do it the hard way
				Connection con = ((SessionImpl) sess).connection();
				Statement stmt = con.createStatement();

				int idAppUser = appUser.getIdAppUser();

				stmt.executeUpdate("delete from RequestCollaborator where idAppUser=" + idAppUser);
				stmt.executeUpdate("delete from DataTrackCollaborator where idAppUser=" + idAppUser);
				stmt.executeUpdate("delete from LabCollaborator where idAppUser=" + idAppUser);

				stmt.close();

				//
				// Delete app user
				//
				sess.delete(appUser);

				sess.flush();

				this.xmlResult = "<SUCCESS/>";

				setResponsePage(this.SUCCESS_JSP);

			} else {
				this.addInvalidField("insufficient permission", "Insufficient permissions to delete member.");
				setResponsePage(this.ERROR_JSP);
			}

		} catch (Exception e) {
			log.error("An exception has occurred in DeleteAppUser ", e);
			e.printStackTrace();
			throw new GNomExRollbackException(e.getMessage(), true, "Unable to delete app user");

		} finally {
			try {
				HibernateSession.closeSession();
			} catch (Exception e) {
				log.error("Exception trying to close the Hibernate session: " + e);
			}
		}

		return this;
	}

}