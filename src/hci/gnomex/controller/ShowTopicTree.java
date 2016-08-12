package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Topic;
import hci.gnomex.model.Visibility;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.TopicTreeLinkInfo;
import hci.report.utility.ReportCommand;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;
public class ShowTopicTree extends ReportCommand implements Serializable {
	private static Logger LOG = Logger.getLogger(ShowTopicTree.class);

	private boolean isAllScope = false;
	private String siteName;
	private List<TopicTreeLinkInfo> topicTree;

	@Override
	public void validate() {
	}

	@Override
	public void loadCommand(HttpServletRequest request, HttpSession session) {
		isAllScope = false;
		if (request.getParameter("scope") != null && request.getParameter("scope").equals("all")) {
			isAllScope = true;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Command execute() throws RollBackCommandException {

		this.SUCCESS_JSP_HTML = "/report_topic_tree.jsp";
		this.ERROR_JSP = "/message.jsp";

		Session sess = null;
		try {

			sess = HibernateSession.currentReadOnlySession("guest");

			// Make sure property dictioary is loaded
			PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);
			// Get the site name (property driven);
			siteName = pdh.getProperty(PropertyDictionary.SITE_TITLE);
			if (siteName == null) {
				siteName = "";
			}

			String queryString = "Select t from Topic t";
			if (!isAllScope) {
				queryString += " where codeVisibility = '" + Visibility.VISIBLE_TO_PUBLIC + "'";
			}
			queryString += " order by name asc";
			List<Topic> topics = (List<Topic>) sess.createQuery(queryString).list();

			topicTree = new ArrayList<TopicTreeLinkInfo>();
			Integer level = 0;
			getTopicsForParent(topics, null, level);

			if (isValid()) {
				this.setSuccessJsp(this, "html");
			} else {
				setResponsePage(this.ERROR_JSP);
			}

		} catch (NamingException e) {
			LOG.error("An exception has occurred in ShowTopicTree ", e);
			e.printStackTrace();
			throw new RollBackCommandException(e.getMessage());

		} catch (SQLException e) {
			LOG.error("An exception has occurred in ShowTopicTree ", e);
			e.printStackTrace();
			throw new RollBackCommandException(e.getMessage());

		} catch (Exception e) {
			LOG.error("An exception has occurred in ShowTopicTree ", e);
			e.printStackTrace();
			throw new RollBackCommandException(e.getMessage());
		} finally {
			try {
				HibernateSession.closeSession();
			} catch (Exception e) {

			}
		}

		return this;
	}

	// recursive function to ensure that topics appear with each parent followed by all of its children.
	// Note that original list is sorted by topic name so that sort will be preserved at each level.
	private void getTopicsForParent(List<Topic> topics, Integer idParent, Integer level) {
		for (Topic topic : topics) {
			if ((topic.getIdParentTopic() == null && idParent == null) || (topic.getIdParentTopic() != null && topic.getIdParentTopic().equals(idParent))) {
				TopicTreeLinkInfo link = new TopicTreeLinkInfo(topic, level, isAllScope);
				topicTree.add(link);
				level++;
				getTopicsForParent(topics, topic.getIdTopic(), level);
				level--;
			}
		}
	}

	@Override
	public HttpServletRequest setRequestState(HttpServletRequest request) {
		request.setAttribute("siteName", this.siteName);
		request.setAttribute("topics", this.topicTree);
		return request;
	}

	@Override
	public HttpServletResponse setResponseState(HttpServletResponse response) {
		return response;
	}

	@Override
	public HttpSession setSessionState(HttpSession session) {
		return session;
	}

	@Override
	public void loadContextPermissions() {
	}

}
