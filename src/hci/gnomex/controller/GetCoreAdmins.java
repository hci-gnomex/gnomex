package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.gnomex.model.AppUser;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by u0395021 on 7/29/2016.
 */
public class GetCoreAdmins extends GNomExCommand implements Serializable {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetRequest.class);

    private Integer idCoreFacility;

    @Override
    public void loadCommand(HttpServletRequest request, HttpSession sess) {
        if (request.getParameter("idCoreFacility") != null && !request.getParameter("idCoreFacility").equals("")) {
            idCoreFacility = new Integer(request.getParameter("idCoreFacility"));
        } else{
            this.addInvalidField("idCoreFacility", "idCoreFacility must be provided");
        }

    }

    @Override
    public Command execute() throws RollBackCommandException {
        try{
            if(this.isValid()){
                Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
                Query q = sess.createQuery("Select au from AppUser au JOIN au.managingCoreFacilities as cf where cf.idCoreFacility = :idCoreFacility and au.isActive = 'Y' ");
                q.setParameter("idCoreFacility", idCoreFacility);
                List admins = q.list();

                Document doc = new Document(new Element("AdminList"));

                for(Iterator<AppUser> i = admins.iterator(); i.hasNext();){
                    AppUser au = i.next();
                    Element admin = new Element("Admin");
                    admin.setAttribute("idAppUser", au.getIdAppUser().toString());
                    admin.setAttribute("display", au.getDisplayNameXMLSafe());
                    doc.getRootElement().addContent(admin);
                }

                org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
                this.xmlResult = out.outputString(doc);

                setResponsePage(this.SUCCESS_JSP);

            } else{
                setResponsePage(this.ERROR_JSP);
            }

        }catch(Exception e){
            log.error("An exception has occurred in GetCoreAdmins ", e);
            e.printStackTrace();
        } finally{
            try {
                this.getSecAdvisor().closeReadOnlyHibernateSession();
            } catch (Exception e) {

            }
        }

        return this;
    }

    public void validate() {
    }
}
