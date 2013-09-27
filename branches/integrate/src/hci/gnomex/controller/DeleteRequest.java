package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.ExperimentCollaborator;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateType;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.ReactionType;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.TransferLog;
import hci.gnomex.model.WorkItem;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class DeleteRequest extends GNomExCommand implements Serializable {



  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteRequest.class);

  private String requestsToDeleteXMLString = null;
  private Document requestsToDeleteDoc = null;

  private Integer      idRequest = null;
  private String       serverName;
  private String       baseDir;




  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = new Integer(request.getParameter("idRequest"));
    } 

    if (request.getParameter("requestsToDeleteXMLString") != null && !request.getParameter("requestsToDeleteXMLString").equals("")) {
      requestsToDeleteXMLString = request.getParameter("requestsToDeleteXMLString");
      StringReader reader = new StringReader(requestsToDeleteXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        requestsToDeleteDoc = sax.build(reader);     
      } catch (JDOMException je ) {
        log.error( "Cannot parse requestsToDeleteXMLString", je );
        this.addInvalidField( "requestsToDeleteXMLString", "Invalid requestsToDeleteXMLString");
      }
    } 
    if ( requestsToDeleteXMLString == null && idRequest == null ) {
      this.addInvalidField("idRequest(s)", "idRequest(s) required.");
    }
    serverName = request.getServerName();

  }

  public Command execute() throws RollBackCommandException {


    if ( requestsToDeleteDoc != null ) {
      for(Iterator i = this.requestsToDeleteDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
        Element node = (Element)i.next();
        Integer id = Integer.parseInt(node.getAttributeValue("idRequest"));

        doDeleteRequest( id );
      }
    } else if ( idRequest != null ) {
      doDeleteRequest( idRequest );
    }

    return this;
  }

  private void doDeleteRequest(Integer idReq) throws RollBackCommandException{
    try {

      Session sess = HibernateSession.currentSession(this.getUsername());
      Request req = (Request)sess.get(Request.class, idReq);

      if (req!=null) {

        baseDir = PropertyDictionaryHelper.getInstance(sess).getExperimentDirectory(serverName, req.getIdCoreFacility());

        if (this.getSecAdvisor().canDelete(req)) {

          // Remove the work items
          for(Iterator i = req.getWorkItems().iterator(); i.hasNext();) {
            WorkItem wi = (WorkItem)i.next();
            sess.delete(wi);
          }
          sess.flush();

          // Remove references to labeled samples on hyb
          for(Iterator i = req.getHybridizations().iterator(); i.hasNext();) {
            Hybridization h = (Hybridization)i.next();
            h.setIdLabeledSampleChannel1(null);
            h.setIdLabeledSampleChannel2(null);
          }
          sess.flush();


          // Delete the labeled samples next
          for(Iterator i = req.getLabeledSamples().iterator(); i.hasNext();) {
            LabeledSample ls  = (LabeledSample)i.next();
            sess.delete(ls);
          }
          sess.flush();

          // Delete sequence lanes
          for(Iterator i = req.getSequenceLanes().iterator(); i.hasNext();) {
            SequenceLane lane = (SequenceLane)i.next();
            sess.delete(lane);
          }
          sess.flush();

          // Delete billing items
          for(Iterator i = req.getBillingItems().iterator(); i.hasNext();) {
            BillingItem bi = (BillingItem)i.next();
            sess.delete(bi);
          }
          sess.flush(); 

          // Remove transfer logs 
          List transferLogs = sess.createQuery("SELECT x from TransferLog x where x.idRequest = '" + req.getIdRequest() + "'").list();
          for(Iterator i = transferLogs.iterator(); i.hasNext();) {
            TransferLog tl = (TransferLog)i.next();
            sess.delete(tl);
          }
          sess.flush();

          // Delete source plates
          if (req.getSamples().size() > 0) {
            String sourcePlateQuery = "SELECT pw from PlateWell pw left join pw.plate p where (p.codePlateType='" + PlateType.SOURCE_PLATE_TYPE + "' or p.idPlate is NULL) and pw.idSample in (";
            Boolean firstSample = true;
            for(Iterator i = req.getSamples().iterator();i.hasNext();) {
              Sample s = (Sample)i.next();
              if (!firstSample) {
                sourcePlateQuery += ", ";
              }
              sourcePlateQuery += s.getIdSample().toString();
              firstSample = false;
            }
            sourcePlateQuery += ")";
            List sourcePlates = sess.createQuery(sourcePlateQuery).list();
            Integer idSourcePlate = null;
            for(Iterator i = sourcePlates.iterator();i.hasNext();) {
              PlateWell well = (PlateWell)i.next();
              idSourcePlate = well.getIdPlate();
              sess.delete(well);
            }
            if (idSourcePlate != null) {
              Plate sourcePlate = (Plate)sess.load(Plate.class, idSourcePlate);
              if (sourcePlate != null) {
                sess.delete(sourcePlate);
              }
              sess.flush();
            }
          }

          //
          // If Cherry Picking - delete dest plate well too
          //
          //
          if (req.getCodeRequestCategory().equals(RequestCategory.CHERRY_PICKING_REQUEST_CATEGORY)) {
            if (req.getSamples().size() > 0) {
              String rxnPlateQuery = "SELECT pw from PlateWell pw join pw.plate p where p.idInstrumentRun is null and p.codeReactionType='" + ReactionType.CHERRY_PICKING_REACTION_TYPE +  "' and pw.idSample in (";
              Boolean firstSample = true;
              for(Iterator i = req.getSamples().iterator();i.hasNext();) {
                Sample s = (Sample)i.next();
                if (!firstSample) {
                  rxnPlateQuery += ", ";
                }
                rxnPlateQuery += s.getIdSample().toString();
                firstSample = false;
              }
              rxnPlateQuery += ")";
              List rxnPlates = sess.createQuery(rxnPlateQuery).list();
              Integer idRxnPlate = null;
              for(Iterator i = rxnPlates.iterator();i.hasNext();) {
                PlateWell well = (PlateWell)i.next();
                idRxnPlate = well.getIdPlate();
                sess.delete(well);
              }
              if (idRxnPlate != null) {
                Plate rxnPlate = (Plate)sess.load(Plate.class, idRxnPlate);
                if (rxnPlate != null) {
                  sess.delete(rxnPlate);
                }
                sess.flush();
              }
            }

          }
          // Delete (unlink) collaborators
          //
          for (Iterator i1 = req.getCollaborators().iterator(); i1.hasNext();) {
            ExperimentCollaborator ec = (ExperimentCollaborator)i1.next();
            sess.delete(ec);
          }
          sess.flush();

          //Delete files from filesystem
          SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
          String createYear = formatter.format(req.getCreateDate());
          if (!baseDir.endsWith("/") && !baseDir.endsWith("\\")) {
            baseDir += "/";
          }
          String directoryName = baseDir + createYear + "/" + req.getNumber().replace("R1", "R"); 
          removeExperimentFiles(directoryName);

          //
          // Delete Request
          //
          Hibernate.initialize(req.getSeqLibTreatments());
          req.setSeqLibTreatments(new TreeSet());
          sess.flush();

          sess.delete(req);

          sess.flush();

          this.xmlResult = "<SUCCESS/>";

          setResponsePage(this.SUCCESS_JSP);


        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permissions to delete this request.");
          setResponsePage(this.ERROR_JSP);
        }
      }
    }catch (Exception e){
      log.error("An exception has occurred in DeleteRequest ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {

      }
    }
  }

  private void removeExperimentFiles(String folderName) throws IOException{
    File f = new File(folderName);
    String [] folderContents = f.list();

    if(folderContents == null || folderContents.length == 0){
      if (f.exists()) {
        if (!f.delete()) {
          log.error("Unable to remove " + f.getName() + " from file system");
        }
      }
      return;
    }

    for(int i = 0; i < folderContents.length; i++){
      File child = new File(folderName + "/" + folderContents[i]);
      if(child.isDirectory()){
        removeExperimentFiles(child.getCanonicalPath());
      }
      else{
        if (!child.delete()) {
          log.error("Unable to remove " + child.getName() + " from file system");
        } 
      }
    }

    if (!f.delete()) {
      log.error("Unable to remove " + f.getName() + " from file system");
    } 
  }






}