package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;

import hci.framework.control.RollBackCommandException;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.HibernateSession;


import hci.gnomex.model.NumberSequencingCyclesAllowed;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;





import java.sql.Date;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Set;


public class ExperimentQueue extends HttpServlet {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ExperimentQueue.class);
  
  private Document doc;
  
  ArrayList<String> listItems;

  public void init() throws ServletException { 
    listItems = new ArrayList<String>();
  }
  
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
  }
  
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try{
      
      String xmlResult;
      
      Session sess = HibernateGuestSession.currentGuestSession(request.getUserPrincipal().getName());
      
      HttpSession session = request.getSession();
      
      Element root = new Element("HTML");
      doc = new Document(root);
      
      Element head = new Element("HEAD");
      root.addContent(head);
      
      Element body = new Element("BODY");
      body.setAttribute("style", "background-color:whitesmoke");
      root.addContent(body);
      
      Element mainHead = new Element("H1");
      Element hr = new Element("HR");
      Element a = new Element("A");
      a.setAttribute("name", "Illumina");
      mainHead.addContent("Illumina HiSeq2000 Sequencing Queue");
      mainHead.addContent(a);
      mainHead.addContent(hr);
      body.addContent(mainHead);
      
      Element descriptionHeader = new Element("H4");
      descriptionHeader.addContent("Estimated Sequencing Time");
      body.addContent(descriptionHeader);
      
      Element description = new Element("P");
      Element hr1 = new Element("HR");
      description.addContent("In order to keep Illumina sequencing costs down, samples are run in batches. These batches are dictated by a flowcell. An Illumina flowcell contains eight lanes and each lane must be filled with samples prior to starting a sequencing run. Samples are run on a flowcell according to the date in which the samples were received. Genome Analzyer flowcells often require one of the eight lanes to be a genomic DNA control. Between one and twelve samples can be placed in each lane if multiplexing of samples is implemented. All samples on the same flowcell will experience the same type of run (single end read or paired end read) and the same number of sequencing cycles (36, 50, 76 or 101). These factors create challenges in providing firm guidelines in the estimated time frame from drop off of sample to delivery of data. We attempt to have a five week turnaround for samples that are run on single end read sequencing flowcells (although we have been set back a bit while going through the recent HiSeq training). Paired end sequencing runs only represent approximately 20% of the flowcells that are run at this core facility. Therefore, the length of time from submission of a sample to the completion of sequencing can be somewhat longer for paired end libraries. A sequencing run can last take from three to ten days. Pipeline analysis of the sequence data can take an additional one to three days. ");
      description.addContent(hr1);
      body.addContent(description);
      
      StringBuffer query = new StringBuffer("SELECT sp from NumberSequencingCyclesAllowed sp");
      List nsca = sess.createQuery(query.toString()).list();
      
      for(Object temp : nsca)
      {
        NumberSequencingCyclesAllowed cycle = (NumberSequencingCyclesAllowed)temp;

        //Start for loop here  for(Dictionary Entry : numbersequencingcyclesAllowed){
        StringBuffer query1 = new StringBuffer("select seqLane.number, flowCell.lastCycleDate, " +
            "user.lastName, user.firstName from Request as r, FlowCellChannel as flowCell " +
            "join r.appUser as user " +
            "join r.sequenceLanes as seqLane " +
            "where r.completedDate is NULL and " +
            "seqLane.idRequest = r.idRequest and " +
            "flowCell.pipelineFailed = 'N' and flowCell.lastCycleFailed = 'N' and " +
            "seqLane.idSeqRunType = " + cycle.getIdSeqRunType() + " and seqLane.idNumberSequencingCycles = " + cycle.getIdNumberSequencingCycles() + 
            " and seqLane.idFlowCellChannel = flowCell.idFlowCellChannel " +
            "Order by flowCell.lastCycleDate asc");

        List<Object[]> sqlResults = sess.createQuery(query1.toString()).list();
        
        if (sqlResults.size() == 0)
          continue;
        listItems.add(cycle.getName());
      }
      
      orderList(listItems);

      Element hrefTable = new Element("TABLE");
      hrefTable.setAttribute("style", "background-color:whitesmoke");
      Element headRow = new Element("TR");
      Element hrefHead = new Element("TH");
      hrefHead.addContent("Contents");
      headRow.addContent(hrefHead);
      hrefTable.addContent(headRow);
      hrefTable.setAttribute("border", "1");
      Element contentNav = new Element("TD");
      contentNav.addContent(makeList(listItems));
      hrefTable.addContent(contentNav);
      body.addContent(hrefTable);
      
      for(Object temp : nsca)
      {
        NumberSequencingCyclesAllowed cycle = (NumberSequencingCyclesAllowed)temp;
    

      //Start for loop here  for(Dictionary Entry : numbersequencingcyclesAllowed){
      StringBuffer query1 = new StringBuffer("select seqLane.number, flowCell.lastCycleDate, " +
      		"user.lastName, user.firstName from Request as r, FlowCellChannel as flowCell " +
          "join r.appUser as user " +
      		"join r.sequenceLanes as seqLane " +
      		"where r.completedDate is NULL and " +
      		"seqLane.idRequest = r.idRequest and " +
      		"flowCell.pipelineFailed = 'N' and " +
      		"seqLane.idSeqRunType = " + cycle.getIdSeqRunType() + " and seqLane.idNumberSequencingCycles = " + cycle.getIdNumberSequencingCycles() + 
      		" and seqLane.idFlowCellChannel = flowCell.idFlowCellChannel " + 
      		"Order by flowCell.lastCycleDate asc");

      List<Object[]> sqlResults = sess.createQuery(query1.toString()).list();
      
      if (sqlResults.size() == 0)
        continue;
      
      Element heading = new Element("H2");
      Element bold = new Element("B");
      Element anchor = new Element("A");
      Element hr4 = new Element("HR");
      anchor.setAttribute("name", cycle.getName());
      heading.addContent(anchor);
      heading.addContent(cycle.getName());
      heading.addContent(bold);
      heading.addContent(hr4);
      body.addContent(heading);
      
      body.addContent(CreateTable(sqlResults));
      
      Element anchor1 = new Element("A");
      anchor1.addContent("Back to Top");
      anchor1.setAttribute("href", "#Illumina");
      body.addContent(anchor1);
      
      }

      //end loop here.  Will repeat for all entries in nsca dictionary in order
      //to make a table for each type of sequencing experiment
      
      
      XMLOutputter out = new org.jdom.output.XMLOutputter();
      out.setOmitEncoding(true);
      xmlResult = out.outputString(doc);
      xmlResult = xmlResult.replaceAll("&amp;", "&");
      xmlResult = xmlResult.replaceAll("�",     "&micro");
      log.debug(xmlResult);
      
      response.setContentType("text/html");
      PrintWriter outWriter = response.getWriter();
      outWriter.println(xmlResult);
      outWriter.close();
      
    }
    
    catch (Exception e) {
      log.error("An exception has occurred in ExperimentQueue ", e);
      e.printStackTrace();
      try {
        throw new RollBackCommandException(e.getMessage());
      } catch (RollBackCommandException e1) {
        e1.printStackTrace();
      }
    } finally {
      try {
        HibernateSession.closeSession();
      } catch (Exception e) {
      }
    }
  
  }


  private void orderList(ArrayList<String> listItems) {

    String temp = "";
    
    String[] tempi;
    String[] tempj;
    
    for(int i = 1; i < listItems.size(); i++){
      temp = listItems.get(i);
      tempi = listItems.get(i).split(" ");
      int j = i;
      tempj = listItems.get(j - 1).split(" ");
      
      while(j > 0 && Integer.parseInt(tempj[0]) > Integer.parseInt(tempi[0])){
        listItems.set(j, listItems.get(j-1));
        j--;
        if(j-1 > 0)
          tempj = listItems.get(j-1).split(" ");
          
      }
      
      listItems.set(j, temp);
    }
    
    
  }

  private Element makeList(ArrayList<String> s){
    Element orderedList = new Element("OL");
    for(int i = 0; i < s.size(); i++)
    {
      Element listItem = new Element("LI");
      Element anchor = new Element ("A");
      anchor.setAttribute("href", "#" + s.get(i));
      listItem.addContent(anchor);
      listItem.addContent(s.get(i));
      orderedList.addContent(listItem);
    }

    return orderedList;
    
  }
  private Element CreateTable(List<Object[]> sqlResults) {
    
    Element table = new Element("TABLE");
    table.setAttribute("border", "2");
    table.setAttribute("cellpadding", "2");
    table.addContent(makeHeaderRow());
    

    for(int i = 0; i < sqlResults.size(); i++)
    {
      Object[] temp = sqlResults.get(i); 
      if(temp[1] == null)
        continue;
      String submitterInitials = (((String) temp[2]).substring(0, 1)) + (((String) temp[3]).substring(0, 1));
      
      String sampleId = (String)temp[0];
      
      Date dateReceived = (Date)temp[1];
 
      table.addContent(makeRow(sampleId, dateReceived, submitterInitials));
    }
      return table;
  }

 
  
  private Element makeRow(String sampleId, Date dateReceived, String initials)
  {
    Element row = new Element("TR");
    Element cell = new Element("TD");
    cell.addContent(sampleId);
    row.addContent(cell);
    
    cell = new Element("TD");
    cell.addContent(dateReceived.toString());
    row.addContent(cell);
    
    cell = new Element("TD");
    cell.addContent(initials);
    row.addContent(cell);
    
    return row;

  }
  
  private Element makeHeaderRow()
  {
    Element headerRow = new Element("TR");
    
    Element sampleId = new Element("TH");
    sampleId.setAttribute("width", "100");
    sampleId.addContent("Sample ID");
    
    Element dateReceived = new Element("TH");
    dateReceived.setAttribute("width", "100");
    dateReceived.addContent("Date Received");
    
    Element initials = new Element("TH");
    initials.setAttribute("width", "100");
    initials.addContent("Initials");
    
    headerRow.addContent(sampleId);
    headerRow.addContent(dateReceived);
    headerRow.addContent(initials);
    
    return headerRow;
  }

  public void validate() {
  }

}


  


