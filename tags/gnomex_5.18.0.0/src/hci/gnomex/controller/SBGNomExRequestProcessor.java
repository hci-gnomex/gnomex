package hci.gnomex.controller;

import hci.framework.control.Command;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface SBGNomExRequestProcessor extends EJBObject {
  public Command processCommand(Command command) throws RemoteException;
}