package hci.gnomex.controller;

import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.io.IOException;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.lang.Exception;

import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.logging.*;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.jdom.input.SAXBuilder;
import org.jdom.JDOMException;

import nki.objects.*;


public class MetrixServerInterface extends GNomExCommand implements Serializable {
  
  
private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MetrixServerInterface.class);
  
  //private MetrixInterfaceFilter filter;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    //filter = new MetrixInterfaceFilter();
    
    //HashMap errors = this.loadDetailObject(request, filter);
    //this.addInvalidFields(errors);
  }

  public Command execute() throws RollBackCommandException {
	String srvResp = "";
    
	try {
    	
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
        
    	try{
	        SocketChannel sChannel = SocketChannel.open();
	        sChannel.configureBlocking(true);
	        if (sChannel.connect(new InetSocketAddress("forge", 12332))) {

                // Create OutputStream for sending objects.
                ObjectOutputStream oos = new ObjectOutputStream(sChannel.socket().getOutputStream());

                // Cteate Inputstream for receiving objects.
		        ObjectInputStream ois = new ObjectInputStream(sChannel.socket().getInputStream());

			try{
				nki.objects.Command sendCommand = new nki.objects.Command();
				
				// Set a value for command
				sendCommand.setCommandString("XML");
				sendCommand.setCommandDetail("1"); // Select run state (1 - running, 2 - finished, 3 - errors / halted, 4 - FC needs turn, 5 - init) 
				sendCommand.setCommand("FETCH");
				oos.writeObject(sendCommand);
				oos.flush();
				
				boolean listen = true;

				Object serverAnswer = new Object();
				serverAnswer = ois.readObject();
//				while(( serverAnswer = ois.readObject()) != null){
				while(listen){
					if(serverAnswer instanceof Command){	// Answer is a Command with info message.
						nki.objects.Command commandIn = (nki.objects.Command) serverAnswer;
						if(commandIn.getCommandString() != null){
							System.out.println("[SERVER] " + commandIn.getCommandDetail());
						}
					}

					if(serverAnswer instanceof HashMap){	// Answer is a Summary with single runInfo
						
					}
			
					if(serverAnswer instanceof SummaryCollection){
						SummaryCollection sc = (SummaryCollection) serverAnswer;
						//metrixLogger.log(Level.INFO, "[CLIENT] The server answered with a SummaryCollection.");
						System.out.println();
						ListIterator litr = sc.getSummaryIterator();

						int count = 1;
						while(litr.hasNext()){
							Summary sum = (Summary) litr.next();

							count++;
						}
					}

					if(serverAnswer instanceof String){
						srvResp = (String) serverAnswer;
						//System.out.println("Response from METRIX SERVER: " + srvResp);
						listen = false;
					}
				}

			}catch(IOException Ex){
				log.error("IOException in Metrix Client.", Ex);
			}
	        }
	}catch(EOFException ex){
		log.error("Server has shutdown.");
	}catch(NoConnectionPendingException NCPE){
		log.error("Communication channel is not connection and no operation has been initiated.");
	}catch(AsynchronousCloseException ACE){
		log.error("Another client has shutdown the server. Channel communication prohibited by issueing a direct command.");
	}
        
        SAXBuilder builder = new SAXBuilder();
        Document doc = new Document();
        
//      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
    	try{
    		doc = builder.build(new StringReader(srvResp));  		
    	}catch(JDOMException JEx){
    		System.out.println("Error in SAXBuilder " + JEx);
    	}
        
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);
        
        // Send redirect with response SUCCESS or ERROR page.
        setResponsePage(this.SUCCESS_JSP);
        
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to manage workflow.");
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (NamingException e){
      log.error("An exception has occurred in MetrixServerInterface ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in MetrixServerInterface ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in MetrixServerInterface ", e);
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
  
     
}