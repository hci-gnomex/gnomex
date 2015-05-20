package hci.gnomex.controller;

import java.rmi.*;
import javax.ejb.*;
import hci.framework.control.Command;

public interface SBGNomExRequestProcessor extends EJBObject {
  public Command processCommand(Command command) throws RemoteException;
}