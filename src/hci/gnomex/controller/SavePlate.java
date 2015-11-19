package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateType;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.comparators.PlateWellComparator;
import hci.gnomex.utility.parsers.ChromatogramParser;
import hci.gnomex.utility.parsers.PlateWellParser;

import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;



public class SavePlate extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SavePlate.class);

  private int                   idInstrumentRun = 0;
  private int                   idPlate;
  private boolean               isNew = true;
  private String                plateWellXMLString;
  private Document              wellsDoc;
  private PlateWellParser       wellParser;

  private int                   quadrant = -1;
  private String                createDateStr = null;
  private String                comments = null;
  private String                label = null;
  private String                codeReactionType = null;
  private String                creator = null;
  private String                codeSealType = null;
  private String                codePlateType = null;




  public void loadCommand(HttpServletRequest request, HttpSession session) {
    if (request.getParameter("idPlate") != null && !request.getParameter("idPlate").equals("-1")) {
      idPlate = Integer.parseInt(request.getParameter("idPlate"));
      isNew = false;
    } 
    if (request.getParameter("quadrant") != null && !request.getParameter("quadrant").equals("")) {
      quadrant = Integer.parseInt(request.getParameter("quadrant"));
    } 
    if (request.getParameter("idInstrumentRun") != null && !request.getParameter("idInstrumentRun").equals("")) {
      idInstrumentRun = Integer.parseInt(request.getParameter("idInstrumentRun"));
    } 
    if (request.getParameter("createDate") != null && !request.getParameter("createDate").equals("")) {
      createDateStr = request.getParameter("createDate");
    }
    if (request.getParameter("comments") != null && !request.getParameter("comments").equals("")) {
      comments = request.getParameter("comments");
    } 
    if (request.getParameter("label") != null && !request.getParameter("label").equals("")) {
      label = request.getParameter("label");
    } 
    if (request.getParameter("codeReactionType") != null && !request.getParameter("codeReactionType").equals("")) {
      codeReactionType = request.getParameter("codeReactionType");
    }
    if (request.getParameter("creator") != null && !request.getParameter("creator").equals("")) {
      creator = request.getParameter("creator");
    } 
    if (request.getParameter("codeSealType") != null && !request.getParameter("codeSealType").equals("")) {
      codeSealType = request.getParameter("codeSealType");
    }
    if (request.getParameter("codePlateType") != null && !request.getParameter("codePlateType").equals("")) {
      codePlateType = request.getParameter("codePlateType");
    }
    if (request.getParameter("plateWellXMLString") != null
        && !request.getParameter("plateWellXMLString").equals("")) {
      plateWellXMLString = request.getParameter("plateWellXMLString");
    }

    StringReader reader = new StringReader(plateWellXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      wellsDoc = sax.build(reader);
      wellParser = new PlateWellParser(wellsDoc);
    }
    catch (JDOMException je) {
      log.error("Cannot parse wellXMLString", je);
      this.addInvalidField("wellXMLString", "Invalid wellXMLString");
    }

  }

  public Command execute() throws RollBackCommandException {

    try {
      Session sess = HibernateSession.currentSession(this.getUsername());

      if (this.getSecurityAdvisor().hasPermission( SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE )) {

        if ( isNew && label != null ) {
          StringBuffer queryBuf = new StringBuffer();
          queryBuf.append("SELECT p from Plate as p WHERE p.label = '");
          queryBuf.append(label);
          queryBuf.append("' AND p.codePlateType = 'REACTION'");
          List plates = sess.createQuery(queryBuf.toString()).list();
          if ( plates.size() > 0 ) {
            this.addInvalidField( "Duplicate Plate Name", "A plate with this name already exists." );
          }
        }
        
        
        if ( isValid() ) {
          wellParser.parse(sess);

          Plate plate;

          if(isNew) {
            plate = new Plate();
            plate.setCodePlateType(PlateType.REACTION_PLATE_TYPE);
            sess.save(plate);
            creator = this.getSecAdvisor().getIdAppUser().toString();
            plate.setCreateDate(new java.util.Date(System.currentTimeMillis()));
          } else {
            plate = (Plate) sess.get(Plate.class, idPlate);
            if ( plate.getCreateDate() == null ) {
              plate.setCreateDate(new java.util.Date(System.currentTimeMillis()));
            }
          }

          if ( idInstrumentRun != 0 ) {
            plate.setIdInstrumentRun(idInstrumentRun);
            plate.setQuadrant(quadrant);
          } else {
            plate.setQuadrant(-1);
          }

          java.util.Date createDate = null;
          if (createDateStr != null) {
            createDate = this.parseDate(createDateStr);
          }
          if ( createDate != null )  {plate.setCreateDate(createDate);}

          if ( creator != null ) {
            plate.setCreator(creator);
          } else if ( plate.getCreator()==null || plate.getCreator().equals("") ) {
            plate.setCreator( this.getSecAdvisor().getIdAppUser() != null ? this.getSecAdvisor().getIdAppUser().toString() : "" ); 
          }
          if ( label != null )            {plate.setLabel(label);}
          if ( comments != null )         {plate.setComments(comments);}
          if ( codeReactionType != null ) {plate.setCodeReactionType(codeReactionType);}
          if ( codeSealType != null )     {plate.setCodeSealType(codeSealType);}
          if ( codePlateType != null && !codePlateType.equals( "" ) )  {
            plate.setCodePlateType(codePlateType);
          } 

          idPlate = plate.getIdPlate();

          sess.flush();

          //
          // Remove wells
          //
          for(Iterator i = plate.getPlateWells().iterator(); i.hasNext();) {
            PlateWell existingWell = (PlateWell)i.next();
            if (!wellParser.getWellMap().containsKey(existingWell.getIdPlateWell().toString())) {
              sess.delete( existingWell );
            }
          }

          //
          // Save wells
          //
          TreeSet<PlateWell> wellsToAdd = new TreeSet<PlateWell>(new PlateWellComparator());
          for (Iterator i = wellParser.getWellMap().keySet().iterator(); i.hasNext();) {
            String idPlateWellString = (String) i.next();
            PlateWell pw = 
              (PlateWell) wellParser.getWellMap().get(idPlateWellString);

            pw.setIdPlate(idPlate);
            pw.setPlate(plate);
            if ( pw.getCreateDate() == null ) {
              pw.setCreateDate(plate.getCreateDate());
            }
            pw.setCodeReactionType( codeReactionType );

            wellsToAdd.add( pw );
          }
          plate.setPlateWells(wellsToAdd);

          sess.flush();


          // Result XML
          Document doc = new Document(new Element("SUCCESS"));

          plate.excludeMethodFromXML("getPlateWells");
          plate.excludeMethodFromXML( "getInstrumentRun" );
          Element pNode = plate.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();

          String creator = plate.getCreator();
          if ( creator != null && !creator.equals( "" ) ) {
            AppUser user = (AppUser)sess.get(AppUser.class, Integer.valueOf(creator));
            pNode.setAttribute( "creator", user != null ? user.getDisplayName() : creator);
          } else {
            pNode.setAttribute( "creator", creator);
          }

          Element pwNode = new Element("plateWells");

          List plateWells = sess.createQuery("SELECT pw from PlateWell as pw where pw.idPlate=" + idPlate).list();

          // Remove redo flags for any wells that might be redos
          removeRedoFlags( plateWells, sess );

          for(Iterator i = plateWells.iterator(); i.hasNext();) {
            PlateWell plateWell = (PlateWell)i.next();
            plateWell.excludeMethodFromXML("getPlate");
            plateWell.excludeMethodFromXML("getSample");
            plateWell.excludeMethodFromXML("getAssay");
            plateWell.excludeMethodFromXML("getPrimer");
            Element node = plateWell.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();

            if ( plateWell.getAssay() != null ) {
              node.setAttribute( "label", plateWell.getAssay().getDisplay() );
            } else if ( plateWell.getPrimer() != null ) {
              node.setAttribute( "label", plateWell.getPrimer().getDisplay() );
            }

            node.setAttribute("requestSubmitDate", "");
            node.setAttribute("requestSubmitter", "");

            if ( plateWell.getIdRequest() != null ) {
              String idRequestString = plateWell.getIdRequest().toString();
              if ( idRequestString != null && !idRequestString.equals("")) {
                Request request = (Request) sess.createQuery("SELECT r from Request as r where r.idRequest=" + idRequestString).uniqueResult();
                if ( request != null ) {
                  node.setAttribute("requestSubmitDate",  request.getCreateDate() != null ? new SimpleDateFormat("MM/dd/yyyy").format(request.getCreateDate()) : "");
                  node.setAttribute("requestSubmitter", request.getOwnerName());
                  node.setAttribute("requestNumber", request.getNumber());
                }
              }
            }

            pwNode.addContent(node);
          }

          sess.flush();

          pNode.addContent(pwNode);
          doc.getRootElement().addContent(pNode);


          XMLOutputter out = new org.jdom.output.XMLOutputter();
          this.xmlResult = out.outputString(doc);

          setResponsePage(this.SUCCESS_JSP);
        }

      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save plate.");
        setResponsePage(this.ERROR_JSP);
      }

    }catch (Exception e){
      log.error("An exception has occurred in SavePlate ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {

      }
    }

    return this;
  }

  private void removeRedoFlags( List plateWells, Session sess ) {
    for(Iterator i = plateWells.iterator(); i.hasNext();) {
      PlateWell reactionWell = (PlateWell)i.next();
      if ( reactionWell.getIsControl() != null && reactionWell.getIsControl().equals("Y") ) {
        break;
      }
      StringBuffer buf = ChromatogramParser.getRedoQuery( reactionWell, true );
      Query query = sess.createQuery(buf.toString());
      List redoWells = query.list();

      // If source redo wells are found, mark the reaction well as a redo, and
      // remove redo flag from the source wells.
      for ( Iterator i2 = redoWells.iterator(); i2.hasNext();) {
        PlateWell redoWell = (PlateWell) i2.next();
        reactionWell.setRedoFlag( "Y" );
        redoWell.setRedoFlag( "N" );
      }
    }
  }


  public void validate() {}
}