package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.ExperimentFile;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestDownloadFilter;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.UploadDownloadHelper;

import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetLinkedSampleFiles extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetLinkedSampleFiles.class);

  private Integer                        idRequest;
  private StringBuffer                   queryBuf = new StringBuffer();
  private String                         serverName;
  private SortedMap<String, List<Element>>     sampleGroups = new TreeMap<String, List<Element>>(new MyComparator());

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = Integer.parseInt(request.getParameter("idRequest"));
    }

    serverName = request.getServerName();

  }

  public Command execute() throws RollBackCommandException {

    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      Request request = (Request) sess.load(Request.class, this.idRequest);

      String baseExperimentDir   = PropertyDictionaryHelper.getInstance(sess).getExperimentDirectory(serverName, request.getIdCoreFacility());
      String directoryName = baseExperimentDir + Request.getCreateYear(request.getCreateDate()) + "/";
      directoryName.replace("\\", "/");

      queryBuf.append("SELECT s.name, s.number, s.idSample, sef.idExpFileRead1, sef.idExpFileRead2, sef.seqRunNumber, sef.idSampleExperimentFile, s.groupName ");
      queryBuf.append("FROM Sample s LEFT JOIN s.sampleExperimentFiles as sef ");
      queryBuf.append("WHERE s.idRequest =  " + this.idRequest);
      queryBuf.append(" Order by s.groupName ");

      List samples = sess.createQuery(queryBuf.toString()).list();

      Document doc = new Document(new Element("SampleList"));

      Integer previousSampleID = null;
      Element sampleNode = new Element("Sample");
      for(ListIterator i = samples.listIterator(); i.hasNext();) {
        Object[] row = (Object[]) i.next();
        if(previousSampleID != null && !previousSampleID.equals((Integer)row[2])) {
          sampleNode = new Element("Sample");
        }
        Element seqRunNumberNode = new Element("SeqRunNumber");
        seqRunNumberNode.setAttribute("idSampleExperimentFile", row[6] != null ? String.valueOf((Integer)row[6]) : "");
        ExperimentFile ef = null;
        File f = null;
        FileDescriptor fd = null;
        sampleNode.setAttribute("name", row[0] != null ? (String)row[0] : "");
        sampleNode.setAttribute("number", row[1] != null ? (String)row[1] : "");
        sampleNode.setAttribute("idSample", row[2] != null ? String.valueOf((Integer)row[2]) : "");
        previousSampleID = (Integer)row[2];

        if(row[3] != null) {
          ef = (ExperimentFile)sess.load(ExperimentFile.class, (Integer)row[3]);
          Element sefNode = new Element("FileDescriptor");
          f = new File(directoryName + ef.getFileName());
          fd = new FileDescriptor("", ef.getFileName().substring(ef.getFileName().lastIndexOf("/") + 1), f, "");
          sefNode.setAttribute("displayName", fd.getDisplayName());
          sefNode.setAttribute("fileSizeText", fd.getFileSizeText());
          sefNode.setAttribute("lastModifyDateDisplay", fd.getLastModifyDateDisplay());
          sefNode.setAttribute("idExperimentFile", String.valueOf((Integer)row[3]));
          sefNode.setAttribute("zipEntryName", ef.getFileName());
          sefNode.setAttribute("readID", "1");
          seqRunNumberNode.addContent(sefNode);
        }

        if(row[4] != null) {
          ef = (ExperimentFile)sess.load(ExperimentFile.class, (Integer)row[4]);
          Element sefNode = new Element("FileDescriptor");
          f = new File(directoryName + ef.getFileName());
          fd = new FileDescriptor("", ef.getFileName().substring(ef.getFileName().lastIndexOf("/") + 1), f, "");
          sefNode.setAttribute("displayName", fd.getDisplayName());
          sefNode.setAttribute("fileSizeText", fd.getFileSizeText());
          sefNode.setAttribute("lastModifyDateDisplay", fd.getLastModifyDateDisplay());
          sefNode.setAttribute("idExperimentFile", String.valueOf((Integer)row[4]));
          sefNode.setAttribute("zipEntryName", ef.getFileName());
          sefNode.setAttribute("readID", "2");
          seqRunNumberNode.addContent(sefNode);
        }
        if(seqRunNumberNode.hasChildren()) {
          sampleNode.addContent(seqRunNumberNode);
        }


        String groupName = row[7] != null ? (String)row[7] : "*||*"; //Needed some obscure string to signify no sample group
        groupName = groupName.equals("") ? "*||*" : groupName;
        sampleNode.setAttribute("groupName", groupName);
        for(Iterator j = sampleGroups.keySet().iterator(); j.hasNext();) {
          String displayName = (String)j.next();
          if(groupName.equals(displayName)) {
            List<Element> temp = sampleGroups.get(displayName);
            temp.add(sampleNode);
            sampleGroups.put(groupName, temp);
            break;
          } else if(groupName.contains(displayName)) {
            List<Element> temp = sampleGroups.get(displayName);
            temp.add(sampleNode);
            sampleGroups.put(groupName, temp);
            sampleGroups.remove(displayName);
            break;
          }
        }

        if(!sampleGroups.containsKey(groupName)){
          List<Element> temp = new ArrayList();
          temp.add(sampleNode);
          sampleGroups.put(groupName, temp);
        }
      }

      HashMap<String,Element> alreadyCreated = new HashMap<String,Element>();
      for(Iterator i = sampleGroups.keySet().iterator(); i.hasNext();) {
        String groupName = (String)i.next();
        String[] group = groupName.split("/");
        Element e = new Element("SampleGroup");
        e.setAttribute("displayName", group[0]);
        for(int j = 1; j < group.length; j++) {
          Element e1 = new Element("SampleGroup");
          e1.setAttribute("displayName", group[j]);
          if(e.hasChildren()) {
            Element child = (Element)(e.getChildren().get(e.getChildren().size() - 1));
            child.addContent(e1);
          } else {
            e.addContent(e1);
          }
        }
        alreadyCreated.put(group[0], e);
      }

      HashMap<String, Element> groups = new HashMap<String, Element>();
      for(Iterator i = sampleGroups.keySet().iterator(); i.hasNext();) {
        String groupName = (String)i.next();
        String restingNode = "";
        List<Element> sampleNodes = sampleGroups.get(groupName);
        if(groupName.equals("*||*")) {
          for(Element sNode : sampleNodes) {
            doc.getRootElement().addContent(sNode);
          }
          continue;
        }
        String[] nameArray = groupName.split("/");
        Element group = alreadyCreated.get(nameArray[0]);
        for(Element samp : sampleNodes) {
          String sampGroup = samp.getAttributeValue("groupName");
          restingNode = sampGroup.substring(sampGroup.lastIndexOf("/") + 1);
          recurseAddChildren(restingNode, group, samp);
        }

        doc.getRootElement().addContent(group);
      }

      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);

    }catch (NamingException e){
      log.error("An exception has occurred in GetRequestDownloadList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetRequestDownloadList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetRequestDownloadList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {

      }
    }

    return this;
  }

  private void recurseAddChildren(String restingNode, Element group, Element sample) {
    if(group.getAttributeValue("displayName").equals(restingNode)) {
      group.addContent(sample);
      return;
    }
    for(Iterator j = group.getChildren().iterator(); j.hasNext();) {
      Element groupChild = (Element)j.next();
      if(groupChild.getName().equals("SampleGroup") && groupChild.getAttributeValue("displayName").equals(restingNode)) {
        groupChild.addContent(sample);
        return;
      } else if(groupChild.getName().equals("SampleGroup") && groupChild.hasChildren()) {
        recurseAddChildren(restingNode, groupChild, sample);
      }
    }

  }

}

class MyComparator implements Comparator<String>{
  @Override
  public int compare(String o1, String o2) {  
    if (o1.length() > o2.length()) {
      return 1;
    } else if (o1.length() < o2.length()) {
      return -1;
    }
    return o1.compareTo(o2);
  }
}