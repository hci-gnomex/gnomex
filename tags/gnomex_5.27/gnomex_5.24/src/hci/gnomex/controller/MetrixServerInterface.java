package hci.gnomex.controller;

import hci.gnomex.model.PropertyDictionary;
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
import java.util.ListIterator;
import java.lang.Exception;

import java.net.InetSocketAddress;
import java.nio.channels.*;

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
import nki.exceptions.*;

public class MetrixServerInterface extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MetrixServerInterface.class);

  public void validate() {

  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {
    //filter = new MetrixInterfaceFilter();
    //HashMap errors = this.loadDetailObject(request, filter);
    //this.addInvalidFields(errors);
  }

  public Command execute() throws RollBackCommandException {
    String srvResp = "<SummaryCollection />";

    try {
      //if(isValid()){
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
        DictionaryHelper dh = DictionaryHelper.getInstance(sess);

        try{
          SocketChannel sChannel = SocketChannel.open();
          sChannel.configureBlocking(true);
          String host = dh.getPropertyDictionary(PropertyDictionary.METRIX_SERVER_HOST);
          int port = Integer.parseInt(dh.getPropertyDictionary(PropertyDictionary.METRIX_SERVER_PORT));

          if(Integer.parseInt(dh.getPropertyDictionary(PropertyDictionary.METRIX_SERVER_PORT)) < 1025){
            this.addInvalidField("Invalid configuration.", "Invalid configuration details for MetrixServer. Please check the dictionary properties.");
            setResponsePage(this.ERROR_JSP);
          }

          if(sChannel.connect(new InetSocketAddress(host, port))){

            // Create OutputStream for sending objects.
            ObjectOutputStream oos = new ObjectOutputStream(sChannel.socket().getOutputStream());

            // Cteate Inputstream for receiving objects.
            ObjectInputStream ois = new ObjectInputStream(sChannel.socket().getInputStream());

            try{
              if(isValid()){
                nki.objects.Command sendCommand = new nki.objects.Command();

                // Set a value for command
                sendCommand.setFormat("XML");
                sendCommand.setState(12); // Select run state (1 - running, 2 - finished, 3 - errors / halted, 4 - FC needs turn, 5 - init) || 12 - ALL
                sendCommand.setCommand("FETCH");
                sendCommand.setMode("CALL");
                sendCommand.setType("DETAIL");
                oos.writeObject(sendCommand);
                oos.flush();

                boolean listen = true;

                Object serverAnswer = new Object();
                serverAnswer = ois.readObject();

                while(listen){
                  if(serverAnswer instanceof Command){	// Answer is a Command with info message.
                    nki.objects.Command commandIn = (nki.objects.Command) serverAnswer;
                    if(commandIn.getCommand() != null){
                      System.out.println("[SERVER] " + commandIn.getCommand());
                    }
                  }

                  if(serverAnswer instanceof SummaryCollection){
                    SummaryCollection sc = (SummaryCollection) serverAnswer;
                    ListIterator litr = sc.getSummaryIterator();

                    while(litr.hasNext()){
                      Summary sum = (Summary) litr.next();
                    }
                  }

                  if(serverAnswer instanceof String){ 			// Server returned a XML String with results.
                    srvResp = (String) serverAnswer;
                    log.info("Server replied with XML");
                    listen = false;
                  }

                  if(serverAnswer instanceof EmptyResultSetCollection){
                    System.out.println(serverAnswer.toString());
                    listen = false;
                  }

                  if(serverAnswer instanceof InvalidCredentialsException){
                    System.out.println(serverAnswer.toString());
                    listen = false;
                  }

                  if(serverAnswer instanceof MissingCommandDetailException){
                    System.out.println(serverAnswer.toString());
                    listen = false;
                  }

                  if(serverAnswer instanceof UnimplementedCommandException){
                    System.out.println(serverAnswer.toString());
                    listen = false;
                  }
                }
              }
            }catch(IOException Ex){
              //		log.error("IOException in Metrix Client.", Ex);
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
      //	}	// end main isValid()
    }catch (NamingException e){
      log.error("An exception has occurred in MetrixServerInterface ", e);
      //e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());

    }catch (SQLException e) {
      log.error("An exception has occurred in MetrixServerInterface ", e);
      // e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in MetrixServerInterface ", e);
      //  e.printStackTrace(System.out);
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