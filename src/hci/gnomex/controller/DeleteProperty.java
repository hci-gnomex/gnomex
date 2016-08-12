package hci.gnomex.controller;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Property;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.PropertyEntryValue;
import hci.gnomex.model.PropertyOption;
import hci.gnomex.model.PropertyPlatformApplication;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import org.apache.log4j.Logger;


public class DeleteProperty extends GNomExCommand implements Serializable {



  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(DeleteProperty.class);


  private Integer      idProperty = null;
  private String       deleteAll = null;




  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idProperty") != null && !request.getParameter("idProperty").equals("")) {
      idProperty = new Integer(request.getParameter("idProperty"));
    } else {
      this.addInvalidField("idProperty", "idProperty is required.");
    }
    if (request.getParameter("deleteAll") != null && !request.getParameter("deleteAll").equals("")) {
      deleteAll = request.getParameter("deleteAll");
    } else {
      this.addInvalidField("deleteAll", "deleteAll is required.");
    }

  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    Property property = null;

    int nonBlankSampleCount = 0;
    int nonBlankAnalysisCount = 0;
    int nonBlankDataTrackCount = 0;

    try {
      sess = HibernateSession.currentSession(this.getUsername());
      property = (Property)sess.load(Property.class, idProperty);

      // Check permissions
      if (this.getSecAdvisor().canDelete(property)) {

        StringBuffer propertyEntryQuery = new StringBuffer();
        propertyEntryQuery.append("SELECT pe from PropertyEntry as pe");
        propertyEntryQuery.append(" where pe.idProperty = " + idProperty.intValue());
        propertyEntryQuery.append(" and pe.idSample is not null");
        List samples = sess.createQuery(propertyEntryQuery.toString()).list();
        for(Iterator i1 = samples.iterator(); i1.hasNext();) {
          PropertyEntry pe = (PropertyEntry) i1.next();
          if(pe.getValue() != null && pe.getValue().length() > 0) {
            nonBlankSampleCount++;
          }
        }

        propertyEntryQuery = new StringBuffer();
        propertyEntryQuery.append("SELECT pe from PropertyEntry as pe");
        propertyEntryQuery.append(" where pe.idProperty = " + idProperty.intValue());
        propertyEntryQuery.append(" and pe.idAnalysis is not null");
        List analyses = sess.createQuery(propertyEntryQuery.toString()).list();
        for(Iterator i1 = analyses.iterator(); i1.hasNext();) {
          PropertyEntry pe = (PropertyEntry) i1.next();
          if(pe.getValue() != null && pe.getValue().length() > 0) {
            nonBlankAnalysisCount++;
          }
        }

        propertyEntryQuery = new StringBuffer();
        propertyEntryQuery.append("SELECT pe from PropertyEntry as pe");
        propertyEntryQuery.append(" where pe.idProperty = " + idProperty.intValue());
        propertyEntryQuery.append(" and pe.idDataTrack is not null");
        List dataTracks = sess.createQuery(propertyEntryQuery.toString()).list();
        for(Iterator i1 = dataTracks.iterator(); i1.hasNext();) {
          PropertyEntry pe = (PropertyEntry) i1.next();
          if(pe.getValue() != null && pe.getValue().length() > 0) {
            nonBlankDataTrackCount++;
          }
        }

        if(deleteAll.compareTo("Y")!=0 &&
            (nonBlankSampleCount > 0 || nonBlankAnalysisCount > 0 || nonBlankDataTrackCount > 0)) {
          try {
            sess.clear();
            property = (Property)sess.load(Property.class, idProperty);
            property.setIsActive("N");
            sess.save(property);
            sess.flush();

            DictionaryHelper.reload(sess);

            this.xmlResult = "<NONBLANKVALUES idProperty='" + property.getIdProperty().intValue() + "' sampleCount='"+ nonBlankSampleCount +"' analysisCount='"+ nonBlankAnalysisCount +"' dataTrackCount='"+ nonBlankDataTrackCount +"'/>";
            setResponsePage(this.SUCCESS_JSP);
            return this;

          } catch(Exception e) {
            LOG.error("An exception has occurred in DeleteProperty when trying to inactivate property ", e);

            throw new RollBackCommandException(e.getMessage());

          }
        }

        if(deleteAll.compareTo("Y")==0 &&
            (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES))
            || (nonBlankSampleCount == 0 && nonBlankAnalysisCount == 0 && nonBlankDataTrackCount == 0)) {

          StringBuffer allPropertyEntries = new StringBuffer();
          allPropertyEntries.append("SELECT pe from PropertyEntry as pe");
          allPropertyEntries.append(" where pe.idProperty = " + idProperty.intValue());
          List entriesToDelete = sess.createQuery(allPropertyEntries.toString()).list();
          for(Iterator i1 = entriesToDelete.iterator(); i1.hasNext();) {
            PropertyEntry entry = (PropertyEntry)i1.next();
            for(Iterator i2 = entry.getValues().iterator(); i2.hasNext();) {
              PropertyEntryValue v = (PropertyEntryValue)i2.next();
              sess.delete(v);
            }
            sess.flush();
            entry.setValues(null);
            sess.delete(entry);
          }
          sess.flush();
        }

        //
        // Delete prices associated with property (if any)
        //
        Property.removePriceCategoryForProperty( property, sess );

        //
        // Clear out property organism list
        //
        property.setOrganisms(new TreeSet());
        sess.flush();

        //
        // Clear out property user list
        //
        property.setAppUsers(new TreeSet());
        sess.flush();

        //
        // Clear out property platformApplication list
        //
        if (property.getPlatformApplications() != null) {
          for(Iterator i = property.getPlatformApplications().iterator(); i.hasNext();) {
            PropertyPlatformApplication pa = (PropertyPlatformApplication) i.next();
            sess.delete(pa);
          }
        }
        sess.flush();


        property.setPlatformApplications(new TreeSet());
        sess.flush();

        //
        // Clear out property options list
        //
        if (property.getOptions() != null) {
          for(Iterator i = property.getOptions().iterator(); i.hasNext();) {
            PropertyOption pa = (PropertyOption) i.next();
            sess.delete(pa);
          }
        }
        sess.flush();

        property.setOptions(new TreeSet());
        sess.flush();

        //
        // Clear out property analysisTypes list
        //
        property.setAnalysisTypes(new TreeSet());
        sess.flush();

        sess.update(property);
        sess.flush();

        //
        // Delete property
        //
        sess.delete(property);


        sess.flush();


        DictionaryHelper.reload(sess);

        this.xmlResult = "<SUCCESS/>";

        setResponsePage(this.SUCCESS_JSP);

      } else {
        this.addInvalidField("insufficient permission", "Insufficient permissions to delete property.");
        setResponsePage(this.ERROR_JSP);
      }
    } catch (ConstraintViolationException ce) {
      LOG.error("An exception has occurred in DeleteProperty ", ce);
      c
    } catch (Exception e){
      LOG.error("An exception has occurred in DeleteProperty ", e);

      throw new RollBackCommandException(e.getMessage());

    }finally {
      try {
        HibernateSession.closeSession();
      } catch(Exception e) {

      }
    }

    return this;
  }






}