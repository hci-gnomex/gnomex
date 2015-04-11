package hci.gnomex.controller;

import java.rmi.*;
import javax.ejb.*;
import hci.framework.control.*;
import hci.framework.model.*;

/**
 *  The Stateless Session Bean that executes the Command
 *
 *@author     Kirt Henrie
 *@created    August 18, 2002
 */
public class SBGNomExRequestProcessorBean implements SessionBean {
  private SessionContext sessionContext;


  /**
   *  Executes the Command passed in and returns the processed Command
   *
   *@param  command              Input Command
   *@return                      Processed Command
   *@exception  RemoteException  Description of the Exception
   */
  public Command processCommand(Command command)throws RemoteException {

    try {
      command = command.execute();
    } catch (RollBackCommandException rbce) {
      this.sessionContext.setRollbackOnly();
      throw new EJBException(rbce);
    }

    return command;
  }


  /**
   *  Description of the Method
   */
  public void ejbCreate() { }


  /**
   *  Description of the Method
   *
   *@exception  RemoteException  Description of the Exception
   */
  public void ejbRemove() throws RemoteException { }


  /**
   *  Description of the Method
   *
   *@exception  RemoteException  Description of the Exception
   */
  public void ejbActivate() throws RemoteException { }


  /**
   *  Description of the Method
   *
   *@exception  RemoteException  Description of the Exception
   */
  public void ejbPassivate() throws RemoteException { }


  /**
   *  Sets the sessionContext attribute of the SBBSTRequestProcessorBean object
   *
   *@param  sessionContext       The new sessionContext value
   *@exception  RemoteException  Description of the Exception
   */
  public void setSessionContext(SessionContext sessionContext) throws RemoteException {
    this.sessionContext = sessionContext;
  }
}
